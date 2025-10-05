/*     */ package org.apache.logging.log4j.core.selector;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.naming.NamingException;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.impl.ContextAnchor;
/*     */ import org.apache.logging.log4j.core.net.JndiManager;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
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
/*     */ public class JndiContextSelector
/*     */   implements NamedContextSelector
/*     */ {
/*  90 */   private static final LoggerContext CONTEXT = new LoggerContext("Default");
/*     */   
/*  92 */   private static final ConcurrentMap<String, LoggerContext> CONTEXT_MAP = new ConcurrentHashMap<>();
/*     */ 
/*     */   
/*  95 */   private static final StatusLogger LOGGER = StatusLogger.getLogger();
/*     */   
/*     */   public JndiContextSelector() {
/*  98 */     if (!JndiManager.isJndiContextSelectorEnabled()) {
/*  99 */       throw new IllegalStateException("JNDI must be enabled by setting log4j2.enableJndiContextSelector=true");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdown(String fqcn, ClassLoader loader, boolean currentContext, boolean allContexts) {
/* 105 */     LoggerContext ctx = ContextAnchor.THREAD_CONTEXT.get();
/* 106 */     if (ctx == null) {
/* 107 */       String loggingContextName = getContextName();
/* 108 */       if (loggingContextName != null) {
/* 109 */         ctx = CONTEXT_MAP.get(loggingContextName);
/*     */       }
/*     */     } 
/* 112 */     if (ctx != null) {
/* 113 */       ctx.stop(50L, TimeUnit.MILLISECONDS);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasContext(String fqcn, ClassLoader loader, boolean currentContext) {
/* 119 */     LoggerContext ctx = ContextAnchor.THREAD_CONTEXT.get();
/* 120 */     if (ctx == null) {
/* 121 */       String loggingContextName = getContextName();
/* 122 */       if (loggingContextName == null) {
/* 123 */         return false;
/*     */       }
/* 125 */       ctx = CONTEXT_MAP.get(loggingContextName);
/*     */     } 
/* 127 */     return (ctx != null && ctx.isStarted());
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext) {
/* 132 */     return getContext(fqcn, loader, currentContext, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext, URI configLocation) {
/* 139 */     LoggerContext lc = ContextAnchor.THREAD_CONTEXT.get();
/* 140 */     if (lc != null) {
/* 141 */       return lc;
/*     */     }
/*     */     
/* 144 */     String loggingContextName = null;
/*     */     
/* 146 */     try (JndiManager jndiManager = JndiManager.getDefaultManager()) {
/* 147 */       loggingContextName = (String)jndiManager.lookup("java:comp/env/log4j/context-name");
/* 148 */     } catch (NamingException ne) {
/* 149 */       LOGGER.error("Unable to lookup {}", "java:comp/env/log4j/context-name", ne);
/*     */     } 
/*     */     
/* 152 */     return (loggingContextName == null) ? CONTEXT : locateContext(loggingContextName, null, configLocation);
/*     */   }
/*     */   
/*     */   private String getContextName() {
/* 156 */     String loggingContextName = null;
/*     */     
/* 158 */     try (JndiManager jndiManager = JndiManager.getDefaultManager()) {
/* 159 */       loggingContextName = (String)jndiManager.lookup("java:comp/env/log4j/context-name");
/* 160 */     } catch (NamingException ne) {
/* 161 */       LOGGER.error("Unable to lookup {}", "java:comp/env/log4j/context-name", ne);
/*     */     } 
/* 163 */     return loggingContextName;
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggerContext locateContext(String name, Object externalContext, URI configLocation) {
/* 168 */     if (name == null) {
/* 169 */       LOGGER.error("A context name is required to locate a LoggerContext");
/* 170 */       return null;
/*     */     } 
/* 172 */     if (!CONTEXT_MAP.containsKey(name)) {
/* 173 */       LoggerContext ctx = new LoggerContext(name, externalContext, configLocation);
/* 174 */       CONTEXT_MAP.putIfAbsent(name, ctx);
/*     */     } 
/* 176 */     return CONTEXT_MAP.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeContext(LoggerContext context) {
/* 182 */     for (Map.Entry<String, LoggerContext> entry : CONTEXT_MAP.entrySet()) {
/* 183 */       if (((LoggerContext)entry.getValue()).equals(context)) {
/* 184 */         CONTEXT_MAP.remove(entry.getKey());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isClassLoaderDependent() {
/* 191 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggerContext removeContext(String name) {
/* 196 */     return CONTEXT_MAP.remove(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<LoggerContext> getLoggerContexts() {
/* 201 */     return Collections.unmodifiableList(new ArrayList<>(CONTEXT_MAP.values()));
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\selector\JndiContextSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */