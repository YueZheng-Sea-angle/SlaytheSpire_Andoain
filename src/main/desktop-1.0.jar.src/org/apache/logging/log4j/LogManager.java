/*     */ package org.apache.logging.log4j;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.Map;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import org.apache.logging.log4j.internal.LogManagerStatus;
/*     */ import org.apache.logging.log4j.message.MessageFactory;
/*     */ import org.apache.logging.log4j.message.StringFormatterMessageFactory;
/*     */ import org.apache.logging.log4j.simple.SimpleLoggerContextFactory;
/*     */ import org.apache.logging.log4j.spi.LoggerContext;
/*     */ import org.apache.logging.log4j.spi.LoggerContextFactory;
/*     */ import org.apache.logging.log4j.spi.Provider;
/*     */ import org.apache.logging.log4j.spi.Terminable;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.LoaderUtil;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ import org.apache.logging.log4j.util.ProviderUtil;
/*     */ import org.apache.logging.log4j.util.StackLocatorUtil;
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
/*     */ public class LogManager
/*     */ {
/*     */   public static final String FACTORY_PROPERTY_NAME = "log4j2.loggerContextFactory";
/*     */   public static final String ROOT_LOGGER_NAME = "";
/*  61 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */   
/*  64 */   private static final String FQCN = LogManager.class.getName();
/*     */ 
/*     */ 
/*     */   
/*     */   private static volatile LoggerContextFactory factory;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  74 */     PropertiesUtil managerProps = PropertiesUtil.getProperties();
/*  75 */     String factoryClassName = managerProps.getStringProperty("log4j2.loggerContextFactory");
/*  76 */     if (factoryClassName != null) {
/*     */       try {
/*  78 */         factory = (LoggerContextFactory)LoaderUtil.newCheckedInstanceOf(factoryClassName, LoggerContextFactory.class);
/*  79 */       } catch (ClassNotFoundException cnfe) {
/*  80 */         LOGGER.error("Unable to locate configured LoggerContextFactory {}", factoryClassName);
/*  81 */       } catch (Exception ex) {
/*  82 */         LOGGER.error("Unable to create configured LoggerContextFactory {}", factoryClassName, ex);
/*     */       } 
/*     */     }
/*     */     
/*  86 */     if (factory == null) {
/*  87 */       SortedMap<Integer, LoggerContextFactory> factories = new TreeMap<>();
/*     */ 
/*     */       
/*  90 */       if (ProviderUtil.hasProviders()) {
/*  91 */         for (Provider provider : ProviderUtil.getProviders()) {
/*  92 */           Class<? extends LoggerContextFactory> factoryClass = provider.loadLoggerContextFactory();
/*  93 */           if (factoryClass != null) {
/*     */             try {
/*  95 */               factories.put(provider.getPriority(), factoryClass.newInstance());
/*  96 */             } catch (Exception e) {
/*  97 */               LOGGER.error("Unable to create class {} specified in provider URL {}", factoryClass.getName(), provider
/*  98 */                   .getUrl(), e);
/*     */             } 
/*     */           }
/*     */         } 
/*     */         
/* 103 */         if (factories.isEmpty()) {
/* 104 */           LOGGER.error("Log4j2 could not find a logging implementation. Please add log4j-core to the classpath. Using SimpleLogger to log to the console...");
/*     */           
/* 106 */           factory = (LoggerContextFactory)new SimpleLoggerContextFactory();
/* 107 */         } else if (factories.size() == 1) {
/* 108 */           factory = factories.get(factories.lastKey());
/*     */         } else {
/* 110 */           StringBuilder sb = new StringBuilder("Multiple logging implementations found: \n");
/* 111 */           for (Map.Entry<Integer, LoggerContextFactory> entry : factories.entrySet()) {
/* 112 */             sb.append("Factory: ").append(((LoggerContextFactory)entry.getValue()).getClass().getName());
/* 113 */             sb.append(", Weighting: ").append(entry.getKey()).append('\n');
/*     */           } 
/* 115 */           factory = factories.get(factories.lastKey());
/* 116 */           sb.append("Using factory: ").append(factory.getClass().getName());
/* 117 */           LOGGER.warn(sb.toString());
/*     */         } 
/*     */       } else {
/*     */         
/* 121 */         LOGGER.error("Log4j2 could not find a logging implementation. Please add log4j-core to the classpath. Using SimpleLogger to log to the console...");
/*     */         
/* 123 */         factory = (LoggerContextFactory)new SimpleLoggerContextFactory();
/*     */       } 
/* 125 */       LogManagerStatus.setInitialized(true);
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean exists(String name) {
/* 143 */     return getContext().hasLogger(name);
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
/*     */   
/*     */   public static LoggerContext getContext() {
/*     */     try {
/* 157 */       return factory.getContext(FQCN, null, null, true);
/* 158 */     } catch (IllegalStateException ex) {
/* 159 */       LOGGER.warn(ex.getMessage() + " Using SimpleLogger");
/* 160 */       return (new SimpleLoggerContextFactory()).getContext(FQCN, null, null, true);
/*     */     } 
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
/*     */ 
/*     */   
/*     */   public static LoggerContext getContext(boolean currentContext) {
/*     */     try {
/* 176 */       return factory.getContext(FQCN, null, null, currentContext, null, null);
/* 177 */     } catch (IllegalStateException ex) {
/* 178 */       LOGGER.warn(ex.getMessage() + " Using SimpleLogger");
/* 179 */       return (new SimpleLoggerContextFactory()).getContext(FQCN, null, null, currentContext, null, null);
/*     */     } 
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static LoggerContext getContext(ClassLoader loader, boolean currentContext) {
/*     */     try {
/* 196 */       return factory.getContext(FQCN, loader, null, currentContext);
/* 197 */     } catch (IllegalStateException ex) {
/* 198 */       LOGGER.warn(ex.getMessage() + " Using SimpleLogger");
/* 199 */       return (new SimpleLoggerContextFactory()).getContext(FQCN, loader, null, currentContext);
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LoggerContext getContext(ClassLoader loader, boolean currentContext, Object externalContext) {
/*     */     try {
/* 218 */       return factory.getContext(FQCN, loader, externalContext, currentContext);
/* 219 */     } catch (IllegalStateException ex) {
/* 220 */       LOGGER.warn(ex.getMessage() + " Using SimpleLogger");
/* 221 */       return (new SimpleLoggerContextFactory()).getContext(FQCN, loader, externalContext, currentContext);
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LoggerContext getContext(ClassLoader loader, boolean currentContext, URI configLocation) {
/*     */     try {
/* 240 */       return factory.getContext(FQCN, loader, null, currentContext, configLocation, null);
/* 241 */     } catch (IllegalStateException ex) {
/* 242 */       LOGGER.warn(ex.getMessage() + " Using SimpleLogger");
/* 243 */       return (new SimpleLoggerContextFactory()).getContext(FQCN, loader, null, currentContext, configLocation, null);
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LoggerContext getContext(ClassLoader loader, boolean currentContext, Object externalContext, URI configLocation) {
/*     */     try {
/* 264 */       return factory.getContext(FQCN, loader, externalContext, currentContext, configLocation, null);
/* 265 */     } catch (IllegalStateException ex) {
/* 266 */       LOGGER.warn(ex.getMessage() + " Using SimpleLogger");
/* 267 */       return (new SimpleLoggerContextFactory()).getContext(FQCN, loader, externalContext, currentContext, configLocation, null);
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LoggerContext getContext(ClassLoader loader, boolean currentContext, Object externalContext, URI configLocation, String name) {
/*     */     try {
/* 289 */       return factory.getContext(FQCN, loader, externalContext, currentContext, configLocation, name);
/* 290 */     } catch (IllegalStateException ex) {
/* 291 */       LOGGER.warn(ex.getMessage() + " Using SimpleLogger");
/* 292 */       return (new SimpleLoggerContextFactory()).getContext(FQCN, loader, externalContext, currentContext, configLocation, name);
/*     */     } 
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected static LoggerContext getContext(String fqcn, boolean currentContext) {
/*     */     try {
/* 309 */       return factory.getContext(fqcn, null, null, currentContext);
/* 310 */     } catch (IllegalStateException ex) {
/* 311 */       LOGGER.warn(ex.getMessage() + " Using SimpleLogger");
/* 312 */       return (new SimpleLoggerContextFactory()).getContext(fqcn, null, null, currentContext);
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext) {
/*     */     try {
/* 331 */       return factory.getContext(fqcn, loader, null, currentContext);
/* 332 */     } catch (IllegalStateException ex) {
/* 333 */       LOGGER.warn(ex.getMessage() + " Using SimpleLogger");
/* 334 */       return (new SimpleLoggerContextFactory()).getContext(fqcn, loader, null, currentContext);
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext, URI configLocation, String name) {
/*     */     try {
/* 356 */       return factory.getContext(fqcn, loader, null, currentContext, configLocation, name);
/* 357 */     } catch (IllegalStateException ex) {
/* 358 */       LOGGER.warn(ex.getMessage() + " Using SimpleLogger");
/* 359 */       return (new SimpleLoggerContextFactory()).getContext(fqcn, loader, null, currentContext);
/*     */     } 
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
/*     */   
/*     */   public static void shutdown() {
/* 373 */     shutdown(false);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void shutdown(boolean currentContext) {
/* 392 */     factory.shutdown(FQCN, null, currentContext, false);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void shutdown(boolean currentContext, boolean allContexts) {
/* 412 */     factory.shutdown(FQCN, null, currentContext, allContexts);
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
/*     */ 
/*     */   
/*     */   public static void shutdown(LoggerContext context) {
/* 426 */     if (context instanceof Terminable) {
/* 427 */       ((Terminable)context).terminate();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LoggerContextFactory getFactory() {
/* 437 */     return factory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setFactory(LoggerContextFactory factory) {
/* 455 */     LogManager.factory = factory;
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
/*     */ 
/*     */   
/*     */   public static Logger getFormatterLogger() {
/* 469 */     return getFormatterLogger(StackLocatorUtil.getCallerClass(2));
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
/*     */   public static Logger getFormatterLogger(Class<?> clazz) {
/* 500 */     return getLogger((clazz != null) ? clazz : StackLocatorUtil.getCallerClass(2), (MessageFactory)StringFormatterMessageFactory.INSTANCE);
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
/*     */   public static Logger getFormatterLogger(Object value) {
/* 532 */     return getLogger((value != null) ? value.getClass() : StackLocatorUtil.getCallerClass(2), (MessageFactory)StringFormatterMessageFactory.INSTANCE);
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
/*     */   public static Logger getFormatterLogger(String name) {
/* 563 */     return (name == null) ? getFormatterLogger(StackLocatorUtil.getCallerClass(2)) : getLogger(name, (MessageFactory)StringFormatterMessageFactory.INSTANCE);
/*     */   }
/*     */ 
/*     */   
/*     */   private static Class<?> callerClass(Class<?> clazz) {
/* 568 */     if (clazz != null) {
/* 569 */       return clazz;
/*     */     }
/* 571 */     Class<?> candidate = StackLocatorUtil.getCallerClass(3);
/* 572 */     if (candidate == null) {
/* 573 */       throw new UnsupportedOperationException("No class provided, and an appropriate one cannot be found.");
/*     */     }
/* 575 */     return candidate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Logger getLogger() {
/* 585 */     return getLogger(StackLocatorUtil.getCallerClass(2));
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
/*     */   
/*     */   public static Logger getLogger(Class<?> clazz) {
/* 598 */     Class<?> cls = callerClass(clazz);
/* 599 */     return (Logger)getContext(cls.getClassLoader(), false).getLogger(cls);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static Logger getLogger(Class<?> clazz, MessageFactory messageFactory) {
/* 614 */     Class<?> cls = callerClass(clazz);
/* 615 */     return (Logger)getContext(cls.getClassLoader(), false).getLogger(cls, messageFactory);
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
/*     */   public static Logger getLogger(MessageFactory messageFactory) {
/* 627 */     return getLogger(StackLocatorUtil.getCallerClass(2), messageFactory);
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
/*     */   
/*     */   public static Logger getLogger(Object value) {
/* 640 */     return getLogger((value != null) ? value.getClass() : StackLocatorUtil.getCallerClass(2));
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static Logger getLogger(Object value, MessageFactory messageFactory) {
/* 655 */     return getLogger((value != null) ? value.getClass() : StackLocatorUtil.getCallerClass(2), messageFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Logger getLogger(String name) {
/* 666 */     return (name != null) ? (Logger)getContext(false).getLogger(name) : getLogger(StackLocatorUtil.getCallerClass(2));
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
/*     */   
/*     */   public static Logger getLogger(String name, MessageFactory messageFactory) {
/* 679 */     return (name != null) ? (Logger)getContext(false).getLogger(name, messageFactory) : getLogger(
/* 680 */         StackLocatorUtil.getCallerClass(2), messageFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static Logger getLogger(String fqcn, String name) {
/* 691 */     return (Logger)factory.getContext(fqcn, null, null, false).getLogger(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Logger getRootLogger() {
/* 700 */     return getLogger("");
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\LogManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */