/*     */ package org.apache.logging.log4j.util;
/*     */ 
/*     */ import java.net.URL;
/*     */ import java.security.Permission;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.spi.LoggerContextFactory;
/*     */ import org.apache.logging.log4j.spi.Provider;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.osgi.framework.AdaptPermission;
/*     */ import org.osgi.framework.AdminPermission;
/*     */ import org.osgi.framework.Bundle;
/*     */ import org.osgi.framework.BundleActivator;
/*     */ import org.osgi.framework.BundleContext;
/*     */ import org.osgi.framework.BundleEvent;
/*     */ import org.osgi.framework.BundleListener;
/*     */ import org.osgi.framework.InvalidSyntaxException;
/*     */ import org.osgi.framework.ServiceReference;
/*     */ import org.osgi.framework.SynchronousBundleListener;
/*     */ import org.osgi.framework.wiring.BundleWire;
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
/*     */ 
/*     */ 
/*     */ public class Activator
/*     */   implements BundleActivator, SynchronousBundleListener
/*     */ {
/*  49 */   private static final SecurityManager SECURITY_MANAGER = System.getSecurityManager();
/*     */   
/*  51 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */   
/*     */   private boolean lockingProviderUtil;
/*     */ 
/*     */   
/*     */   private static void checkPermission(Permission permission) {
/*  58 */     if (SECURITY_MANAGER != null) {
/*  59 */       SECURITY_MANAGER.checkPermission(permission);
/*     */     }
/*     */   }
/*     */   
/*     */   private void loadProvider(Bundle bundle) {
/*  64 */     if (bundle.getState() == 1) {
/*     */       return;
/*     */     }
/*     */     try {
/*  68 */       checkPermission((Permission)new AdminPermission(bundle, "resource"));
/*  69 */       checkPermission((Permission)new AdaptPermission(BundleWiring.class.getName(), bundle, "adapt"));
/*  70 */       BundleContext bundleContext = bundle.getBundleContext();
/*  71 */       if (bundleContext == null) {
/*  72 */         LOGGER.debug("Bundle {} has no context (state={}), skipping loading provider", bundle.getSymbolicName(), toStateString(bundle.getState()));
/*     */       } else {
/*  74 */         loadProvider(bundleContext, (BundleWiring)bundle.adapt(BundleWiring.class));
/*     */       } 
/*  76 */     } catch (SecurityException e) {
/*  77 */       LOGGER.debug("Cannot access bundle [{}] contents. Ignoring.", bundle.getSymbolicName(), e);
/*  78 */     } catch (Exception e) {
/*  79 */       LOGGER.warn("Problem checking bundle {} for Log4j 2 provider.", bundle.getSymbolicName(), e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private String toStateString(int state) {
/*  84 */     switch (state) {
/*     */       case 1:
/*  86 */         return "UNINSTALLED";
/*     */       case 2:
/*  88 */         return "INSTALLED";
/*     */       case 4:
/*  90 */         return "RESOLVED";
/*     */       case 8:
/*  92 */         return "STARTING";
/*     */       case 16:
/*  94 */         return "STOPPING";
/*     */       case 32:
/*  96 */         return "ACTIVE";
/*     */     } 
/*  98 */     return Integer.toString(state);
/*     */   }
/*     */ 
/*     */   
/*     */   private void loadProvider(BundleContext bundleContext, BundleWiring bundleWiring) {
/* 103 */     String filter = "(APIVersion>=2.6.0)";
/*     */     try {
/* 105 */       Collection<ServiceReference<Provider>> serviceReferences = bundleContext.getServiceReferences(Provider.class, "(APIVersion>=2.6.0)");
/* 106 */       Provider maxProvider = null;
/* 107 */       for (ServiceReference<Provider> serviceReference : serviceReferences) {
/* 108 */         Provider provider = (Provider)bundleContext.getService(serviceReference);
/* 109 */         if (maxProvider == null || provider.getPriority().intValue() > maxProvider.getPriority().intValue()) {
/* 110 */           maxProvider = provider;
/*     */         }
/*     */       } 
/* 113 */       if (maxProvider != null) {
/* 114 */         ProviderUtil.addProvider(maxProvider);
/*     */       }
/* 116 */     } catch (InvalidSyntaxException ex) {
/* 117 */       LOGGER.error("Invalid service filter: (APIVersion>=2.6.0)", (Throwable)ex);
/*     */     } 
/* 119 */     List<URL> urls = bundleWiring.findEntries("META-INF", "log4j-provider.properties", 0);
/* 120 */     for (URL url : urls) {
/* 121 */       ProviderUtil.loadProvider(url, bundleWiring.getClassLoader());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void start(BundleContext bundleContext) throws Exception {
/* 127 */     ProviderUtil.STARTUP_LOCK.lock();
/* 128 */     this.lockingProviderUtil = true;
/* 129 */     BundleWiring self = (BundleWiring)bundleContext.getBundle().adapt(BundleWiring.class);
/* 130 */     List<BundleWire> required = self.getRequiredWires(LoggerContextFactory.class.getName());
/* 131 */     for (BundleWire wire : required) {
/* 132 */       loadProvider(bundleContext, wire.getProviderWiring());
/*     */     }
/* 134 */     bundleContext.addBundleListener((BundleListener)this);
/* 135 */     Bundle[] bundles = bundleContext.getBundles();
/* 136 */     for (Bundle bundle : bundles) {
/* 137 */       loadProvider(bundle);
/*     */     }
/* 139 */     unlockIfReady();
/*     */   }
/*     */   
/*     */   private void unlockIfReady() {
/* 143 */     if (this.lockingProviderUtil && !ProviderUtil.PROVIDERS.isEmpty()) {
/* 144 */       ProviderUtil.STARTUP_LOCK.unlock();
/* 145 */       this.lockingProviderUtil = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop(BundleContext bundleContext) throws Exception {
/* 151 */     bundleContext.removeBundleListener((BundleListener)this);
/* 152 */     unlockIfReady();
/*     */   }
/*     */ 
/*     */   
/*     */   public void bundleChanged(BundleEvent event) {
/* 157 */     switch (event.getType()) {
/*     */       case 2:
/* 159 */         loadProvider(event.getBundle());
/* 160 */         unlockIfReady();
/*     */         break;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4\\util\Activator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */