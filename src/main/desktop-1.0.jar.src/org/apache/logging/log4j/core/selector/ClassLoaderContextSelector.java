/*     */ package org.apache.logging.log4j.core.selector;
/*     */ 
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.impl.ContextAnchor;
/*     */ import org.apache.logging.log4j.spi.LoggerContext;
/*     */ import org.apache.logging.log4j.spi.LoggerContextShutdownAware;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
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
/*     */ public class ClassLoaderContextSelector
/*     */   implements ContextSelector, LoggerContextShutdownAware
/*     */ {
/*  50 */   private static final AtomicReference<LoggerContext> DEFAULT_CONTEXT = new AtomicReference<>();
/*     */   
/*  52 */   protected static final StatusLogger LOGGER = StatusLogger.getLogger();
/*     */   
/*  54 */   protected static final ConcurrentMap<String, AtomicReference<WeakReference<LoggerContext>>> CONTEXT_MAP = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void shutdown(String fqcn, ClassLoader loader, boolean currentContext, boolean allContexts) {
/*  60 */     LoggerContext ctx = null;
/*  61 */     if (currentContext) {
/*  62 */       ctx = ContextAnchor.THREAD_CONTEXT.get();
/*  63 */     } else if (loader != null) {
/*  64 */       ctx = findContext(loader);
/*     */     } else {
/*  66 */       Class<?> clazz = StackLocatorUtil.getCallerClass(fqcn);
/*  67 */       if (clazz != null) {
/*  68 */         ctx = findContext(clazz.getClassLoader());
/*     */       }
/*  70 */       if (ctx == null) {
/*  71 */         ctx = ContextAnchor.THREAD_CONTEXT.get();
/*     */       }
/*     */     } 
/*  74 */     if (ctx != null) {
/*  75 */       ctx.stop(50L, TimeUnit.MILLISECONDS);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void contextShutdown(LoggerContext loggerContext) {
/*  81 */     if (loggerContext instanceof LoggerContext) {
/*  82 */       removeContext((LoggerContext)loggerContext);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasContext(String fqcn, ClassLoader loader, boolean currentContext) {
/*     */     LoggerContext ctx;
/*  89 */     if (currentContext) {
/*  90 */       ctx = ContextAnchor.THREAD_CONTEXT.get();
/*  91 */     } else if (loader != null) {
/*  92 */       ctx = findContext(loader);
/*     */     } else {
/*  94 */       Class<?> clazz = StackLocatorUtil.getCallerClass(fqcn);
/*  95 */       if (clazz != null) {
/*  96 */         ctx = findContext(clazz.getClassLoader());
/*     */       } else {
/*  98 */         ctx = ContextAnchor.THREAD_CONTEXT.get();
/*     */       } 
/*     */     } 
/* 101 */     return (ctx != null && ctx.isStarted());
/*     */   }
/*     */   
/*     */   private LoggerContext findContext(ClassLoader loaderOrNull) {
/* 105 */     ClassLoader loader = (loaderOrNull != null) ? loaderOrNull : ClassLoader.getSystemClassLoader();
/* 106 */     String name = toContextMapKey(loader);
/* 107 */     AtomicReference<WeakReference<LoggerContext>> ref = CONTEXT_MAP.get(name);
/* 108 */     if (ref != null) {
/* 109 */       WeakReference<LoggerContext> weakRef = ref.get();
/* 110 */       return weakRef.get();
/*     */     } 
/* 112 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext) {
/* 117 */     return getContext(fqcn, loader, currentContext, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext, URI configLocation) {
/* 123 */     return getContext(fqcn, loader, null, currentContext, configLocation);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerContext getContext(String fqcn, ClassLoader loader, Map.Entry<String, Object> entry, boolean currentContext, URI configLocation) {
/* 129 */     if (currentContext) {
/* 130 */       LoggerContext ctx = ContextAnchor.THREAD_CONTEXT.get();
/* 131 */       if (ctx != null) {
/* 132 */         return ctx;
/*     */       }
/* 134 */       return getDefault();
/* 135 */     }  if (loader != null) {
/* 136 */       return locateContext(loader, entry, configLocation);
/*     */     }
/* 138 */     Class<?> clazz = StackLocatorUtil.getCallerClass(fqcn);
/* 139 */     if (clazz != null) {
/* 140 */       return locateContext(clazz.getClassLoader(), entry, configLocation);
/*     */     }
/* 142 */     LoggerContext lc = ContextAnchor.THREAD_CONTEXT.get();
/* 143 */     if (lc != null) {
/* 144 */       return lc;
/*     */     }
/* 146 */     return getDefault();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeContext(LoggerContext context) {
/* 152 */     for (Map.Entry<String, AtomicReference<WeakReference<LoggerContext>>> entry : CONTEXT_MAP.entrySet()) {
/* 153 */       LoggerContext ctx = ((WeakReference<LoggerContext>)((AtomicReference<WeakReference<LoggerContext>>)entry.getValue()).get()).get();
/* 154 */       if (ctx == context) {
/* 155 */         CONTEXT_MAP.remove(entry.getKey());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isClassLoaderDependent() {
/* 163 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<LoggerContext> getLoggerContexts() {
/* 168 */     List<LoggerContext> list = new ArrayList<>();
/* 169 */     Collection<AtomicReference<WeakReference<LoggerContext>>> coll = CONTEXT_MAP.values();
/* 170 */     for (AtomicReference<WeakReference<LoggerContext>> ref : coll) {
/* 171 */       LoggerContext ctx = ((WeakReference<LoggerContext>)ref.get()).get();
/* 172 */       if (ctx != null) {
/* 173 */         list.add(ctx);
/*     */       }
/*     */     } 
/* 176 */     return Collections.unmodifiableList(list);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private LoggerContext locateContext(ClassLoader loaderOrNull, Map.Entry<String, Object> entry, URI configLocation) {
/* 182 */     ClassLoader loader = (loaderOrNull != null) ? loaderOrNull : ClassLoader.getSystemClassLoader();
/* 183 */     String name = toContextMapKey(loader);
/* 184 */     AtomicReference<WeakReference<LoggerContext>> ref = CONTEXT_MAP.get(name);
/* 185 */     if (ref == null) {
/* 186 */       if (configLocation == null) {
/* 187 */         ClassLoader parent = loader.getParent();
/* 188 */         while (parent != null) {
/*     */           
/* 190 */           ref = CONTEXT_MAP.get(toContextMapKey(parent));
/* 191 */           if (ref != null) {
/* 192 */             WeakReference<LoggerContext> r = ref.get();
/* 193 */             LoggerContext loggerContext = r.get();
/* 194 */             if (loggerContext != null) {
/* 195 */               return loggerContext;
/*     */             }
/*     */           } 
/* 198 */           parent = parent.getParent();
/*     */         } 
/*     */       } 
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
/* 218 */       LoggerContext loggerContext1 = createContext(name, configLocation);
/* 219 */       if (entry != null) {
/* 220 */         loggerContext1.putObject(entry.getKey(), entry.getValue());
/*     */       }
/*     */       
/* 223 */       LoggerContext newContext = ((WeakReference<LoggerContext>)((AtomicReference<WeakReference<LoggerContext>>)CONTEXT_MAP.computeIfAbsent(name, k -> new AtomicReference(new WeakReference<>(ctx)))).get()).get();
/* 224 */       if (newContext == loggerContext1) {
/* 225 */         loggerContext1.addShutdownListener(this);
/*     */       }
/* 227 */       return newContext;
/*     */     } 
/* 229 */     WeakReference<LoggerContext> weakRef = ref.get();
/* 230 */     LoggerContext ctx = weakRef.get();
/* 231 */     if (ctx != null) {
/* 232 */       if (entry != null && ctx.getObject(entry.getKey()) == null) {
/* 233 */         ctx.putObject(entry.getKey(), entry.getValue());
/*     */       }
/* 235 */       if (ctx.getConfigLocation() == null && configLocation != null) {
/* 236 */         LOGGER.debug("Setting configuration to {}", configLocation);
/* 237 */         ctx.setConfigLocation(configLocation);
/* 238 */       } else if (ctx.getConfigLocation() != null && configLocation != null && 
/* 239 */         !ctx.getConfigLocation().equals(configLocation)) {
/* 240 */         LOGGER.warn("locateContext called with URI {}. Existing LoggerContext has URI {}", configLocation, ctx
/* 241 */             .getConfigLocation());
/*     */       } 
/* 243 */       return ctx;
/*     */     } 
/* 245 */     ctx = createContext(name, configLocation);
/* 246 */     if (entry != null) {
/* 247 */       ctx.putObject(entry.getKey(), entry.getValue());
/*     */     }
/* 249 */     ref.compareAndSet(weakRef, new WeakReference<>(ctx));
/* 250 */     return ctx;
/*     */   }
/*     */   
/*     */   protected LoggerContext createContext(String name, URI configLocation) {
/* 254 */     return new LoggerContext(name, null, configLocation);
/*     */   }
/*     */   
/*     */   protected String toContextMapKey(ClassLoader loader) {
/* 258 */     return Integer.toHexString(System.identityHashCode(loader));
/*     */   }
/*     */   
/*     */   protected LoggerContext getDefault() {
/* 262 */     LoggerContext ctx = DEFAULT_CONTEXT.get();
/* 263 */     if (ctx != null) {
/* 264 */       return ctx;
/*     */     }
/* 266 */     DEFAULT_CONTEXT.compareAndSet(null, createContext(defaultContextName(), null));
/* 267 */     return DEFAULT_CONTEXT.get();
/*     */   }
/*     */   
/*     */   protected String defaultContextName() {
/* 271 */     return "Default";
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\selector\ClassLoaderContextSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */