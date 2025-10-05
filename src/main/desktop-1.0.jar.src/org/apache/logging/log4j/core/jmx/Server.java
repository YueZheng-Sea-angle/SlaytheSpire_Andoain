/*     */ package org.apache.logging.log4j.core.jmx;
/*     */ 
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import javax.management.InstanceAlreadyExistsException;
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.MBeanRegistrationException;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.NotCompliantMBeanException;
/*     */ import javax.management.ObjectName;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.core.Appender;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.appender.AsyncAppender;
/*     */ import org.apache.logging.log4j.core.async.AsyncLoggerConfig;
/*     */ import org.apache.logging.log4j.core.async.AsyncLoggerContext;
/*     */ import org.apache.logging.log4j.core.config.LoggerConfig;
/*     */ import org.apache.logging.log4j.core.impl.Log4jContextFactory;
/*     */ import org.apache.logging.log4j.core.selector.ContextSelector;
/*     */ import org.apache.logging.log4j.core.util.Constants;
/*     */ import org.apache.logging.log4j.core.util.Log4jThreadFactory;
/*     */ import org.apache.logging.log4j.spi.LoggerContextFactory;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Server
/*     */ {
/*     */   private static final String CONTEXT_NAME_ALL = "*";
/*     */   public static final String DOMAIN = "org.apache.logging.log4j2";
/*     */   private static final String PROPERTY_DISABLE_JMX = "log4j2.disable.jmx";
/*     */   private static final String PROPERTY_ASYNC_NOTIF = "log4j2.jmx.notify.async";
/*     */   private static final String THREAD_NAME_PREFIX = "jmx.notif";
/*  65 */   private static final StatusLogger LOGGER = StatusLogger.getLogger();
/*  66 */   static final Executor executor = isJmxDisabled() ? null : createExecutor();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ExecutorService createExecutor() {
/*  79 */     boolean defaultAsync = !Constants.IS_WEB_APP;
/*  80 */     boolean async = PropertiesUtil.getProperties().getBooleanProperty("log4j2.jmx.notify.async", defaultAsync);
/*  81 */     return async ? Executors.newFixedThreadPool(1, (ThreadFactory)Log4jThreadFactory.createDaemonThreadFactory("jmx.notif")) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String escape(String name) {
/*  93 */     StringBuilder sb = new StringBuilder(name.length() * 2);
/*  94 */     boolean needsQuotes = false;
/*  95 */     for (int i = 0; i < name.length(); i++) {
/*  96 */       char c = name.charAt(i);
/*  97 */       switch (c) {
/*     */         
/*     */         case '"':
/*     */         case '*':
/*     */         case '?':
/*     */         case '\\':
/* 103 */           sb.append('\\');
/* 104 */           needsQuotes = true;
/*     */ 
/*     */         
/*     */         case ',':
/*     */         case ':':
/*     */         case '=':
/* 110 */           needsQuotes = true;
/*     */ 
/*     */         
/*     */         case '\r':
/*     */           break;
/*     */         
/*     */         case '\n':
/* 117 */           sb.append("\\n");
/* 118 */           needsQuotes = true;
/*     */           break;
/*     */         default:
/* 121 */           sb.append(c); break;
/*     */       } 
/* 123 */     }  if (needsQuotes) {
/* 124 */       sb.insert(0, '"');
/* 125 */       sb.append('"');
/*     */     } 
/* 127 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private static boolean isJmxDisabled() {
/* 131 */     return PropertiesUtil.getProperties().getBooleanProperty("log4j2.disable.jmx");
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reregisterMBeansAfterReconfigure() {
/* 136 */     if (isJmxDisabled()) {
/* 137 */       LOGGER.debug("JMX disabled for Log4j2. Not registering MBeans.");
/*     */       return;
/*     */     } 
/* 140 */     MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
/* 141 */     reregisterMBeansAfterReconfigure(mbs);
/*     */   }
/*     */   
/*     */   public static void reregisterMBeansAfterReconfigure(MBeanServer mbs) {
/* 145 */     if (isJmxDisabled()) {
/* 146 */       LOGGER.debug("JMX disabled for Log4j2. Not registering MBeans.");
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*     */     try {
/* 153 */       ContextSelector selector = getContextSelector();
/* 154 */       if (selector == null) {
/* 155 */         LOGGER.debug("Could not register MBeans: no ContextSelector found.");
/*     */         return;
/*     */       } 
/* 158 */       LOGGER.trace("Reregistering MBeans after reconfigure. Selector={}", selector);
/* 159 */       List<LoggerContext> contexts = selector.getLoggerContexts();
/* 160 */       int i = 0;
/* 161 */       for (LoggerContext ctx : contexts) {
/* 162 */         LOGGER.trace("Reregistering context ({}/{}): '{}' {}", Integer.valueOf(++i), Integer.valueOf(contexts.size()), ctx.getName(), ctx);
/*     */ 
/*     */         
/* 165 */         unregisterLoggerContext(ctx.getName(), mbs);
/*     */         
/* 167 */         LoggerContextAdmin mbean = new LoggerContextAdmin(ctx, executor);
/* 168 */         register(mbs, mbean, mbean.getObjectName());
/*     */         
/* 170 */         if (ctx instanceof AsyncLoggerContext) {
/* 171 */           RingBufferAdmin rbmbean = ((AsyncLoggerContext)ctx).createRingBufferAdmin();
/* 172 */           if (rbmbean.getBufferSize() > 0L)
/*     */           {
/* 174 */             register(mbs, rbmbean, rbmbean.getObjectName());
/*     */           }
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 183 */         registerStatusLogger(ctx.getName(), mbs, executor);
/* 184 */         registerContextSelector(ctx.getName(), selector, mbs, executor);
/*     */         
/* 186 */         registerLoggerConfigs(ctx, mbs, executor);
/* 187 */         registerAppenders(ctx, mbs, executor);
/*     */       } 
/* 189 */     } catch (Exception ex) {
/* 190 */       LOGGER.error("Could not register mbeans", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void unregisterMBeans() {
/* 198 */     if (isJmxDisabled()) {
/* 199 */       LOGGER.debug("JMX disabled for Log4j2. Not unregistering MBeans.");
/*     */       return;
/*     */     } 
/* 202 */     unregisterMBeans(ManagementFactory.getPlatformMBeanServer());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void unregisterMBeans(MBeanServer mbs) {
/* 211 */     if (mbs != null) {
/* 212 */       unregisterStatusLogger("*", mbs);
/* 213 */       unregisterContextSelector("*", mbs);
/* 214 */       unregisterContexts(mbs);
/* 215 */       unregisterLoggerConfigs("*", mbs);
/* 216 */       unregisterAsyncLoggerRingBufferAdmins("*", mbs);
/* 217 */       unregisterAsyncLoggerConfigRingBufferAdmins("*", mbs);
/* 218 */       unregisterAppenders("*", mbs);
/* 219 */       unregisterAsyncAppenders("*", mbs);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ContextSelector getContextSelector() {
/* 229 */     LoggerContextFactory factory = LogManager.getFactory();
/* 230 */     if (factory instanceof Log4jContextFactory) {
/* 231 */       ContextSelector selector = ((Log4jContextFactory)factory).getSelector();
/* 232 */       return selector;
/*     */     } 
/* 234 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void unregisterLoggerContext(String loggerContextName) {
/* 244 */     if (isJmxDisabled()) {
/* 245 */       LOGGER.debug("JMX disabled for Log4j2. Not unregistering MBeans.");
/*     */       return;
/*     */     } 
/* 248 */     MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
/* 249 */     unregisterLoggerContext(loggerContextName, mbs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void unregisterLoggerContext(String contextName, MBeanServer mbs) {
/* 260 */     String search = String.format("org.apache.logging.log4j2:type=%s", new Object[] { escape(contextName), "*" });
/* 261 */     unregisterAllMatching(search, mbs);
/*     */ 
/*     */     
/* 264 */     unregisterStatusLogger(contextName, mbs);
/* 265 */     unregisterContextSelector(contextName, mbs);
/* 266 */     unregisterLoggerConfigs(contextName, mbs);
/* 267 */     unregisterAppenders(contextName, mbs);
/* 268 */     unregisterAsyncAppenders(contextName, mbs);
/* 269 */     unregisterAsyncLoggerRingBufferAdmins(contextName, mbs);
/* 270 */     unregisterAsyncLoggerConfigRingBufferAdmins(contextName, mbs);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void registerStatusLogger(String contextName, MBeanServer mbs, Executor executor) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
/* 276 */     StatusLoggerAdmin mbean = new StatusLoggerAdmin(contextName, executor);
/* 277 */     register(mbs, mbean, mbean.getObjectName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void registerContextSelector(String contextName, ContextSelector selector, MBeanServer mbs, Executor executor) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
/* 284 */     ContextSelectorAdmin mbean = new ContextSelectorAdmin(contextName, selector);
/* 285 */     register(mbs, mbean, mbean.getObjectName());
/*     */   }
/*     */   
/*     */   private static void unregisterStatusLogger(String contextName, MBeanServer mbs) {
/* 289 */     String search = String.format("org.apache.logging.log4j2:type=%s,component=StatusLogger", new Object[] { escape(contextName), "*" });
/* 290 */     unregisterAllMatching(search, mbs);
/*     */   }
/*     */   
/*     */   private static void unregisterContextSelector(String contextName, MBeanServer mbs) {
/* 294 */     String search = String.format("org.apache.logging.log4j2:type=%s,component=ContextSelector", new Object[] { escape(contextName), "*" });
/* 295 */     unregisterAllMatching(search, mbs);
/*     */   }
/*     */   
/*     */   private static void unregisterLoggerConfigs(String contextName, MBeanServer mbs) {
/* 299 */     String pattern = "org.apache.logging.log4j2:type=%s,component=Loggers,name=%s";
/* 300 */     String search = String.format("org.apache.logging.log4j2:type=%s,component=Loggers,name=%s", new Object[] { escape(contextName), "*" });
/* 301 */     unregisterAllMatching(search, mbs);
/*     */   }
/*     */   
/*     */   private static void unregisterContexts(MBeanServer mbs) {
/* 305 */     String pattern = "org.apache.logging.log4j2:type=%s";
/* 306 */     String search = String.format("org.apache.logging.log4j2:type=%s", new Object[] { "*" });
/* 307 */     unregisterAllMatching(search, mbs);
/*     */   }
/*     */   
/*     */   private static void unregisterAppenders(String contextName, MBeanServer mbs) {
/* 311 */     String pattern = "org.apache.logging.log4j2:type=%s,component=Appenders,name=%s";
/* 312 */     String search = String.format("org.apache.logging.log4j2:type=%s,component=Appenders,name=%s", new Object[] { escape(contextName), "*" });
/* 313 */     unregisterAllMatching(search, mbs);
/*     */   }
/*     */   
/*     */   private static void unregisterAsyncAppenders(String contextName, MBeanServer mbs) {
/* 317 */     String pattern = "org.apache.logging.log4j2:type=%s,component=AsyncAppenders,name=%s";
/* 318 */     String search = String.format("org.apache.logging.log4j2:type=%s,component=AsyncAppenders,name=%s", new Object[] { escape(contextName), "*" });
/* 319 */     unregisterAllMatching(search, mbs);
/*     */   }
/*     */   
/*     */   private static void unregisterAsyncLoggerRingBufferAdmins(String contextName, MBeanServer mbs) {
/* 323 */     String pattern1 = "org.apache.logging.log4j2:type=%s,component=AsyncLoggerRingBuffer";
/* 324 */     String search1 = String.format("org.apache.logging.log4j2:type=%s,component=AsyncLoggerRingBuffer", new Object[] { escape(contextName) });
/* 325 */     unregisterAllMatching(search1, mbs);
/*     */   }
/*     */   
/*     */   private static void unregisterAsyncLoggerConfigRingBufferAdmins(String contextName, MBeanServer mbs) {
/* 329 */     String pattern2 = "org.apache.logging.log4j2:type=%s,component=Loggers,name=%s,subtype=RingBuffer";
/* 330 */     String search2 = String.format("org.apache.logging.log4j2:type=%s,component=Loggers,name=%s,subtype=RingBuffer", new Object[] { escape(contextName), "*" });
/* 331 */     unregisterAllMatching(search2, mbs);
/*     */   }
/*     */   
/*     */   private static void unregisterAllMatching(String search, MBeanServer mbs) {
/*     */     try {
/* 336 */       ObjectName pattern = new ObjectName(search);
/* 337 */       Set<ObjectName> found = mbs.queryNames(pattern, null);
/* 338 */       if (found == null || found.isEmpty()) {
/* 339 */         LOGGER.trace("Unregistering but no MBeans found matching '{}'", search);
/*     */       } else {
/* 341 */         LOGGER.trace("Unregistering {} MBeans: {}", Integer.valueOf(found.size()), found);
/*     */       } 
/* 343 */       if (found != null) {
/* 344 */         for (ObjectName objectName : found) {
/* 345 */           mbs.unregisterMBean(objectName);
/*     */         }
/*     */       }
/* 348 */     } catch (InstanceNotFoundException ex) {
/* 349 */       LOGGER.debug("Could not unregister MBeans for " + search + ". Ignoring " + ex);
/* 350 */     } catch (Exception ex) {
/* 351 */       LOGGER.error("Could not unregister MBeans for " + search, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void registerLoggerConfigs(LoggerContext ctx, MBeanServer mbs, Executor executor) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
/* 358 */     Map<String, LoggerConfig> map = ctx.getConfiguration().getLoggers();
/* 359 */     for (String name : map.keySet()) {
/* 360 */       LoggerConfig cfg = map.get(name);
/* 361 */       LoggerConfigAdmin mbean = new LoggerConfigAdmin(ctx, cfg);
/* 362 */       register(mbs, mbean, mbean.getObjectName());
/*     */       
/* 364 */       if (cfg instanceof AsyncLoggerConfig) {
/* 365 */         AsyncLoggerConfig async = (AsyncLoggerConfig)cfg;
/* 366 */         RingBufferAdmin rbmbean = async.createRingBufferAdmin(ctx.getName());
/* 367 */         register(mbs, rbmbean, rbmbean.getObjectName());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void registerAppenders(LoggerContext ctx, MBeanServer mbs, Executor executor) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
/* 375 */     Map<String, Appender> map = ctx.getConfiguration().getAppenders();
/* 376 */     for (String name : map.keySet()) {
/* 377 */       Appender appender = map.get(name);
/*     */       
/* 379 */       if (appender instanceof AsyncAppender) {
/* 380 */         AsyncAppender async = (AsyncAppender)appender;
/* 381 */         AsyncAppenderAdmin asyncAppenderAdmin = new AsyncAppenderAdmin(ctx.getName(), async);
/* 382 */         register(mbs, asyncAppenderAdmin, asyncAppenderAdmin.getObjectName()); continue;
/*     */       } 
/* 384 */       AppenderAdmin mbean = new AppenderAdmin(ctx.getName(), appender);
/* 385 */       register(mbs, mbean, mbean.getObjectName());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void register(MBeanServer mbs, Object mbean, ObjectName objectName) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
/* 392 */     if (mbs.isRegistered(objectName)) {
/*     */       try {
/* 394 */         mbs.unregisterMBean(objectName);
/* 395 */       } catch (MBeanRegistrationException|InstanceNotFoundException ex) {
/* 396 */         LOGGER.trace("Failed to unregister MBean {}", objectName);
/*     */       } 
/*     */     }
/* 399 */     LOGGER.debug("Registering MBean {}", objectName);
/* 400 */     mbs.registerMBean(mbean, objectName);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\jmx\Server.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */