/*     */ package org.apache.logging.log4j.core.osgi;
/*     */ 
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.net.URI;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.impl.ContextAnchor;
/*     */ import org.apache.logging.log4j.core.selector.ClassLoaderContextSelector;
/*     */ import org.apache.logging.log4j.util.StackLocatorUtil;
/*     */ import org.osgi.framework.Bundle;
/*     */ import org.osgi.framework.BundleReference;
/*     */ import org.osgi.framework.FrameworkUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BundleContextSelector
/*     */   extends ClassLoaderContextSelector
/*     */ {
/*     */   public void shutdown(String fqcn, ClassLoader loader, boolean currentContext, boolean allContexts) {
/*  45 */     LoggerContext ctx = null;
/*  46 */     Bundle bundle = null;
/*  47 */     if (currentContext) {
/*  48 */       ctx = ContextAnchor.THREAD_CONTEXT.get();
/*  49 */       ContextAnchor.THREAD_CONTEXT.remove();
/*     */     } 
/*  51 */     if (ctx == null && loader instanceof BundleReference) {
/*  52 */       bundle = ((BundleReference)loader).getBundle();
/*  53 */       ctx = getLoggerContext(bundle);
/*  54 */       removeLoggerContext(ctx);
/*     */     } 
/*  56 */     if (ctx == null) {
/*  57 */       Class<?> callerClass = StackLocatorUtil.getCallerClass(fqcn);
/*  58 */       if (callerClass != null) {
/*  59 */         bundle = FrameworkUtil.getBundle(callerClass);
/*  60 */         ctx = getLoggerContext(FrameworkUtil.getBundle(callerClass));
/*  61 */         removeLoggerContext(ctx);
/*     */       } 
/*     */     } 
/*  64 */     if (ctx == null) {
/*  65 */       ctx = ContextAnchor.THREAD_CONTEXT.get();
/*  66 */       ContextAnchor.THREAD_CONTEXT.remove();
/*     */     } 
/*  68 */     if (ctx != null) {
/*  69 */       ctx.stop(50L, TimeUnit.MILLISECONDS);
/*     */     }
/*  71 */     if (bundle != null && allContexts) {
/*  72 */       Bundle[] bundles = bundle.getBundleContext().getBundles();
/*  73 */       for (Bundle bdl : bundles) {
/*  74 */         ctx = getLoggerContext(bdl);
/*  75 */         if (ctx != null)
/*  76 */           ctx.stop(50L, TimeUnit.MILLISECONDS); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private LoggerContext getLoggerContext(Bundle bundle) {
/*  82 */     String name = ((Bundle)Objects.<Bundle>requireNonNull(bundle, "No Bundle provided")).getSymbolicName();
/*  83 */     AtomicReference<WeakReference<LoggerContext>> ref = (AtomicReference<WeakReference<LoggerContext>>)CONTEXT_MAP.get(name);
/*  84 */     if (ref != null && ref.get() != null) {
/*  85 */       return ((WeakReference<LoggerContext>)ref.get()).get();
/*     */     }
/*  87 */     return null;
/*     */   }
/*     */   
/*     */   private void removeLoggerContext(LoggerContext context) {
/*  91 */     CONTEXT_MAP.remove(context.getName());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasContext(String fqcn, ClassLoader loader, boolean currentContext) {
/*  96 */     if (currentContext && ContextAnchor.THREAD_CONTEXT.get() != null) {
/*  97 */       return ((LoggerContext)ContextAnchor.THREAD_CONTEXT.get()).isStarted();
/*     */     }
/*  99 */     if (loader instanceof BundleReference) {
/* 100 */       return hasContext(((BundleReference)loader).getBundle());
/*     */     }
/* 102 */     Class<?> callerClass = StackLocatorUtil.getCallerClass(fqcn);
/* 103 */     if (callerClass != null) {
/* 104 */       return hasContext(FrameworkUtil.getBundle(callerClass));
/*     */     }
/* 106 */     return (ContextAnchor.THREAD_CONTEXT.get() != null && ((LoggerContext)ContextAnchor.THREAD_CONTEXT.get()).isStarted());
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext, URI configLocation) {
/* 111 */     if (currentContext) {
/* 112 */       LoggerContext ctx = ContextAnchor.THREAD_CONTEXT.get();
/* 113 */       if (ctx != null) {
/* 114 */         return ctx;
/*     */       }
/* 116 */       return getDefault();
/*     */     } 
/*     */     
/* 119 */     if (loader instanceof BundleReference) {
/* 120 */       return locateContext(((BundleReference)loader).getBundle(), configLocation);
/*     */     }
/* 122 */     Class<?> callerClass = StackLocatorUtil.getCallerClass(fqcn);
/* 123 */     if (callerClass != null) {
/* 124 */       return locateContext(FrameworkUtil.getBundle(callerClass), configLocation);
/*     */     }
/* 126 */     LoggerContext lc = ContextAnchor.THREAD_CONTEXT.get();
/* 127 */     return (lc == null) ? getDefault() : lc;
/*     */   }
/*     */   
/*     */   private static boolean hasContext(Bundle bundle) {
/* 131 */     String name = ((Bundle)Objects.<Bundle>requireNonNull(bundle, "No Bundle provided")).getSymbolicName();
/* 132 */     AtomicReference<WeakReference<LoggerContext>> ref = (AtomicReference<WeakReference<LoggerContext>>)CONTEXT_MAP.get(name);
/* 133 */     return (ref != null && ref.get() != null && ((WeakReference)ref.get()).get() != null && ((LoggerContext)((WeakReference<LoggerContext>)ref.get()).get()).isStarted());
/*     */   }
/*     */   
/*     */   private static LoggerContext locateContext(Bundle bundle, URI configLocation) {
/* 137 */     String name = ((Bundle)Objects.<Bundle>requireNonNull(bundle, "No Bundle provided")).getSymbolicName();
/* 138 */     AtomicReference<WeakReference<LoggerContext>> ref = (AtomicReference<WeakReference<LoggerContext>>)CONTEXT_MAP.get(name);
/* 139 */     if (ref == null) {
/* 140 */       LoggerContext context = new LoggerContext(name, bundle, configLocation);
/* 141 */       CONTEXT_MAP.putIfAbsent(name, new AtomicReference(new WeakReference<>(context)));
/*     */       
/* 143 */       return ((WeakReference<LoggerContext>)((AtomicReference<WeakReference<LoggerContext>>)CONTEXT_MAP.get(name)).get()).get();
/*     */     } 
/* 145 */     WeakReference<LoggerContext> r = ref.get();
/* 146 */     LoggerContext ctx = r.get();
/* 147 */     if (ctx == null) {
/* 148 */       LoggerContext context = new LoggerContext(name, bundle, configLocation);
/* 149 */       ref.compareAndSet(r, new WeakReference<>(context));
/* 150 */       return ((WeakReference<LoggerContext>)ref.get()).get();
/*     */     } 
/* 152 */     URI oldConfigLocation = ctx.getConfigLocation();
/* 153 */     if (oldConfigLocation == null && configLocation != null) {
/* 154 */       LOGGER.debug("Setting bundle ({}) configuration to {}", name, configLocation);
/* 155 */       ctx.setConfigLocation(configLocation);
/* 156 */     } else if (oldConfigLocation != null && configLocation != null && !configLocation.equals(oldConfigLocation)) {
/* 157 */       LOGGER.warn("locateContext called with URI [{}], but existing LoggerContext has URI [{}]", configLocation, oldConfigLocation);
/*     */     } 
/*     */     
/* 160 */     return ctx;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\osgi\BundleContextSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */