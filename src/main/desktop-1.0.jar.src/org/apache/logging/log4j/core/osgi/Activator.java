/*     */ package org.apache.logging.log4j.core.osgi;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Hashtable;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginRegistry;
/*     */ import org.apache.logging.log4j.core.impl.Log4jProvider;
/*     */ import org.apache.logging.log4j.core.impl.ThreadContextDataInjector;
/*     */ import org.apache.logging.log4j.core.impl.ThreadContextDataProvider;
/*     */ import org.apache.logging.log4j.core.util.ContextDataProvider;
/*     */ import org.apache.logging.log4j.spi.Provider;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ import org.osgi.framework.Bundle;
/*     */ import org.osgi.framework.BundleActivator;
/*     */ import org.osgi.framework.BundleContext;
/*     */ import org.osgi.framework.BundleEvent;
/*     */ import org.osgi.framework.BundleListener;
/*     */ import org.osgi.framework.InvalidSyntaxException;
/*     */ import org.osgi.framework.ServiceReference;
/*     */ import org.osgi.framework.ServiceRegistration;
/*     */ import org.osgi.framework.SynchronousBundleListener;
/*     */ import org.osgi.framework.wiring.BundleWiring;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Activator
/*     */   implements BundleActivator, SynchronousBundleListener
/*     */ {
/*  50 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*  52 */   private final AtomicReference<BundleContext> contextRef = new AtomicReference<>();
/*     */   
/*  54 */   ServiceRegistration provideRegistration = null;
/*  55 */   ServiceRegistration contextDataRegistration = null;
/*     */ 
/*     */   
/*     */   public void start(BundleContext context) throws Exception {
/*  59 */     Log4jProvider log4jProvider = new Log4jProvider();
/*  60 */     Hashtable<String, String> props = new Hashtable<>();
/*  61 */     props.put("APIVersion", "2.60");
/*  62 */     ThreadContextDataProvider threadContextDataProvider = new ThreadContextDataProvider();
/*  63 */     this.provideRegistration = context.registerService(Provider.class.getName(), log4jProvider, props);
/*  64 */     this.contextDataRegistration = context.registerService(ContextDataProvider.class.getName(), threadContextDataProvider, null);
/*     */     
/*  66 */     loadContextProviders(context);
/*     */     
/*  68 */     if (PropertiesUtil.getProperties().getStringProperty("Log4jContextSelector") == null) {
/*  69 */       System.setProperty("Log4jContextSelector", BundleContextSelector.class.getName());
/*     */     }
/*  71 */     if (this.contextRef.compareAndSet(null, context)) {
/*  72 */       context.addBundleListener((BundleListener)this);
/*     */       
/*  74 */       scanInstalledBundlesForPlugins(context);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void scanInstalledBundlesForPlugins(BundleContext context) {
/*  79 */     Bundle[] bundles = context.getBundles();
/*  80 */     for (Bundle bundle : bundles)
/*     */     {
/*  82 */       scanBundleForPlugins(bundle);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void scanBundleForPlugins(Bundle bundle) {
/*  87 */     long bundleId = bundle.getBundleId();
/*     */     
/*  89 */     if (bundle.getState() == 32 && bundleId != 0L) {
/*  90 */       LOGGER.trace("Scanning bundle [{}, id=%d] for plugins.", bundle.getSymbolicName(), Long.valueOf(bundleId));
/*  91 */       PluginRegistry.getInstance().loadFromBundle(bundleId, ((BundleWiring)bundle
/*  92 */           .adapt(BundleWiring.class)).getClassLoader());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void loadContextProviders(BundleContext bundleContext) {
/*     */     try {
/*  99 */       Collection<ServiceReference<ContextDataProvider>> serviceReferences = bundleContext.getServiceReferences(ContextDataProvider.class, null);
/* 100 */       for (ServiceReference<ContextDataProvider> serviceReference : serviceReferences) {
/* 101 */         ContextDataProvider provider = (ContextDataProvider)bundleContext.getService(serviceReference);
/* 102 */         ThreadContextDataInjector.contextDataProviders.add(provider);
/*     */       } 
/* 104 */     } catch (InvalidSyntaxException ex) {
/* 105 */       LOGGER.error("Error accessing context data provider", (Throwable)ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void stopBundlePlugins(Bundle bundle) {
/* 110 */     LOGGER.trace("Stopping bundle [{}] plugins.", bundle.getSymbolicName());
/*     */     
/* 112 */     PluginRegistry.getInstance().clearBundlePlugins(bundle.getBundleId());
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop(BundleContext context) throws Exception {
/* 117 */     this.provideRegistration.unregister();
/* 118 */     this.contextDataRegistration.unregister();
/* 119 */     this.contextRef.compareAndSet(context, null);
/* 120 */     LogManager.shutdown();
/*     */   }
/*     */ 
/*     */   
/*     */   public void bundleChanged(BundleEvent event) {
/* 125 */     switch (event.getType()) {
/*     */       
/*     */       case 2:
/* 128 */         scanBundleForPlugins(event.getBundle());
/*     */         break;
/*     */       
/*     */       case 256:
/* 132 */         stopBundlePlugins(event.getBundle());
/*     */         break;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\osgi\Activator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */