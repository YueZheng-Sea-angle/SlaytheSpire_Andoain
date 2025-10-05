/*     */ package org.apache.logging.log4j.core.impl;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.core.LifeCycle;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.config.AbstractConfiguration;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationFactory;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationSource;
/*     */ import org.apache.logging.log4j.core.config.composite.CompositeConfiguration;
/*     */ import org.apache.logging.log4j.core.selector.ClassLoaderContextSelector;
/*     */ import org.apache.logging.log4j.core.selector.ContextSelector;
/*     */ import org.apache.logging.log4j.core.util.Cancellable;
/*     */ import org.apache.logging.log4j.core.util.Constants;
/*     */ import org.apache.logging.log4j.core.util.DefaultShutdownCallbackRegistry;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.core.util.ShutdownCallbackRegistry;
/*     */ import org.apache.logging.log4j.spi.LoggerContext;
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
/*     */ public class Log4jContextFactory
/*     */   implements LoggerContextFactory, ShutdownCallbackRegistry
/*     */ {
/*  49 */   private static final StatusLogger LOGGER = StatusLogger.getLogger();
/*     */   
/*  51 */   private static final boolean SHUTDOWN_HOOK_ENABLED = (PropertiesUtil.getProperties().getBooleanProperty("log4j.shutdownHookEnabled", true) && !Constants.IS_WEB_APP);
/*     */ 
/*     */   
/*     */   private final ContextSelector selector;
/*     */ 
/*     */   
/*     */   private final ShutdownCallbackRegistry shutdownCallbackRegistry;
/*     */ 
/*     */   
/*     */   public Log4jContextFactory() {
/*  61 */     this(createContextSelector(), createShutdownCallbackRegistry());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Log4jContextFactory(ContextSelector selector) {
/*  69 */     this(selector, createShutdownCallbackRegistry());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Log4jContextFactory(ShutdownCallbackRegistry shutdownCallbackRegistry) {
/*  80 */     this(createContextSelector(), shutdownCallbackRegistry);
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
/*     */   public Log4jContextFactory(ContextSelector selector, ShutdownCallbackRegistry shutdownCallbackRegistry) {
/*  92 */     this.selector = Objects.<ContextSelector>requireNonNull(selector, "No ContextSelector provided");
/*  93 */     this.shutdownCallbackRegistry = Objects.<ShutdownCallbackRegistry>requireNonNull(shutdownCallbackRegistry, "No ShutdownCallbackRegistry provided");
/*  94 */     LOGGER.debug("Using ShutdownCallbackRegistry {}", shutdownCallbackRegistry.getClass());
/*  95 */     initializeShutdownCallbackRegistry();
/*     */   }
/*     */   
/*     */   private static ContextSelector createContextSelector() {
/*     */     try {
/* 100 */       ContextSelector selector = (ContextSelector)Loader.newCheckedInstanceOfProperty("Log4jContextSelector", ContextSelector.class);
/*     */       
/* 102 */       if (selector != null) {
/* 103 */         return selector;
/*     */       }
/* 105 */     } catch (Exception e) {
/* 106 */       LOGGER.error("Unable to create custom ContextSelector. Falling back to default.", e);
/*     */     } 
/* 108 */     return (ContextSelector)new ClassLoaderContextSelector();
/*     */   }
/*     */   
/*     */   private static ShutdownCallbackRegistry createShutdownCallbackRegistry() {
/*     */     try {
/* 113 */       ShutdownCallbackRegistry registry = (ShutdownCallbackRegistry)Loader.newCheckedInstanceOfProperty("log4j.shutdownCallbackRegistry", ShutdownCallbackRegistry.class);
/*     */ 
/*     */       
/* 116 */       if (registry != null) {
/* 117 */         return registry;
/*     */       }
/* 119 */     } catch (Exception e) {
/* 120 */       LOGGER.error("Unable to create custom ShutdownCallbackRegistry. Falling back to default.", e);
/*     */     } 
/* 122 */     return (ShutdownCallbackRegistry)new DefaultShutdownCallbackRegistry();
/*     */   }
/*     */   
/*     */   private void initializeShutdownCallbackRegistry() {
/* 126 */     if (isShutdownHookEnabled() && this.shutdownCallbackRegistry instanceof LifeCycle) {
/*     */       try {
/* 128 */         ((LifeCycle)this.shutdownCallbackRegistry).start();
/* 129 */       } catch (IllegalStateException e) {
/* 130 */         LOGGER.error("Cannot start ShutdownCallbackRegistry, already shutting down.");
/* 131 */         throw e;
/* 132 */       } catch (RuntimeException e) {
/* 133 */         LOGGER.error("There was an error starting the ShutdownCallbackRegistry.", e);
/*     */       } 
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
/*     */   public LoggerContext getContext(String fqcn, ClassLoader loader, Object externalContext, boolean currentContext) {
/* 150 */     LoggerContext ctx = this.selector.getContext(fqcn, loader, currentContext);
/* 151 */     if (externalContext != null && ctx.getExternalContext() == null) {
/* 152 */       ctx.setExternalContext(externalContext);
/*     */     }
/* 154 */     if (ctx.getState() == LifeCycle.State.INITIALIZED) {
/* 155 */       ctx.start();
/*     */     }
/* 157 */     return ctx;
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
/*     */   public LoggerContext getContext(String fqcn, ClassLoader loader, Object externalContext, boolean currentContext, ConfigurationSource source) {
/* 172 */     LoggerContext ctx = this.selector.getContext(fqcn, loader, currentContext, null);
/* 173 */     if (externalContext != null && ctx.getExternalContext() == null) {
/* 174 */       ctx.setExternalContext(externalContext);
/*     */     }
/* 176 */     if (ctx.getState() == LifeCycle.State.INITIALIZED) {
/* 177 */       if (source != null) {
/* 178 */         ContextAnchor.THREAD_CONTEXT.set(ctx);
/* 179 */         Configuration config = ConfigurationFactory.getInstance().getConfiguration(ctx, source);
/* 180 */         LOGGER.debug("Starting LoggerContext[name={}] from configuration {}", ctx.getName(), source);
/* 181 */         ctx.start(config);
/* 182 */         ContextAnchor.THREAD_CONTEXT.remove();
/*     */       } else {
/* 184 */         ctx.start();
/*     */       } 
/*     */     }
/* 187 */     return ctx;
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
/*     */   public LoggerContext getContext(String fqcn, ClassLoader loader, Object externalContext, boolean currentContext, Configuration configuration) {
/* 202 */     LoggerContext ctx = this.selector.getContext(fqcn, loader, currentContext, null);
/* 203 */     if (externalContext != null && ctx.getExternalContext() == null) {
/* 204 */       ctx.setExternalContext(externalContext);
/*     */     }
/* 206 */     if (ctx.getState() == LifeCycle.State.INITIALIZED) {
/* 207 */       ContextAnchor.THREAD_CONTEXT.set(ctx);
/*     */       try {
/* 209 */         ctx.start(configuration);
/*     */       } finally {
/* 211 */         ContextAnchor.THREAD_CONTEXT.remove();
/*     */       } 
/*     */     } 
/* 214 */     return ctx;
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
/*     */   public LoggerContext getContext(String fqcn, ClassLoader loader, Object externalContext, boolean currentContext, URI configLocation, String name) {
/* 230 */     LoggerContext ctx = this.selector.getContext(fqcn, loader, currentContext, configLocation);
/* 231 */     if (externalContext != null && ctx.getExternalContext() == null) {
/* 232 */       ctx.setExternalContext(externalContext);
/*     */     }
/* 234 */     if (name != null) {
/* 235 */       ctx.setName(name);
/*     */     }
/* 237 */     if (ctx.getState() == LifeCycle.State.INITIALIZED) {
/* 238 */       if (configLocation != null || name != null) {
/* 239 */         ContextAnchor.THREAD_CONTEXT.set(ctx);
/* 240 */         Configuration config = ConfigurationFactory.getInstance().getConfiguration(ctx, name, configLocation);
/* 241 */         LOGGER.debug("Starting LoggerContext[name={}] from configuration at {}", ctx.getName(), configLocation);
/* 242 */         ctx.start(config);
/* 243 */         ContextAnchor.THREAD_CONTEXT.remove();
/*     */       } else {
/* 245 */         ctx.start();
/*     */       } 
/*     */     }
/* 248 */     return ctx;
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggerContext getContext(String fqcn, ClassLoader loader, Map.Entry<String, Object> entry, boolean currentContext, URI configLocation, String name) {
/* 253 */     LoggerContext ctx = this.selector.getContext(fqcn, loader, entry, currentContext, configLocation);
/* 254 */     if (name != null) {
/* 255 */       ctx.setName(name);
/*     */     }
/* 257 */     if (ctx.getState() == LifeCycle.State.INITIALIZED) {
/* 258 */       if (configLocation != null || name != null) {
/* 259 */         ContextAnchor.THREAD_CONTEXT.set(ctx);
/* 260 */         Configuration config = ConfigurationFactory.getInstance().getConfiguration(ctx, name, configLocation);
/* 261 */         LOGGER.debug("Starting LoggerContext[name={}] from configuration at {}", ctx.getName(), configLocation);
/* 262 */         ctx.start(config);
/* 263 */         ContextAnchor.THREAD_CONTEXT.remove();
/*     */       } else {
/* 265 */         ctx.start();
/*     */       } 
/*     */     }
/* 268 */     return ctx;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerContext getContext(String fqcn, ClassLoader loader, Object externalContext, boolean currentContext, List<URI> configLocations, String name) {
/* 274 */     LoggerContext ctx = this.selector.getContext(fqcn, loader, currentContext, null);
/* 275 */     if (externalContext != null && ctx.getExternalContext() == null) {
/* 276 */       ctx.setExternalContext(externalContext);
/*     */     }
/* 278 */     if (name != null) {
/* 279 */       ctx.setName(name);
/*     */     }
/* 281 */     if (ctx.getState() == LifeCycle.State.INITIALIZED) {
/* 282 */       if (configLocations != null && !configLocations.isEmpty()) {
/* 283 */         ContextAnchor.THREAD_CONTEXT.set(ctx);
/* 284 */         List<AbstractConfiguration> configurations = new ArrayList<>(configLocations.size());
/* 285 */         for (URI configLocation : configLocations) {
/*     */           
/* 287 */           Configuration currentReadConfiguration = ConfigurationFactory.getInstance().getConfiguration(ctx, name, configLocation);
/* 288 */           if (currentReadConfiguration != null) {
/* 289 */             if (currentReadConfiguration instanceof org.apache.logging.log4j.core.config.DefaultConfiguration) {
/* 290 */               LOGGER.warn("Unable to locate configuration {}, ignoring", configLocation.toString()); continue;
/*     */             } 
/* 292 */             if (currentReadConfiguration instanceof AbstractConfiguration) {
/* 293 */               configurations.add((AbstractConfiguration)currentReadConfiguration); continue;
/*     */             } 
/* 295 */             LOGGER.error("Found configuration {}, which is not an AbstractConfiguration and can't be handled by CompositeConfiguration", configLocation);
/*     */             
/*     */             continue;
/*     */           } 
/*     */           
/* 300 */           LOGGER.info("Unable to access configuration {}, ignoring", configLocation.toString());
/*     */         } 
/*     */         
/* 303 */         if (configurations.isEmpty()) {
/* 304 */           LOGGER.error("No configurations could be created for {}", configLocations.toString());
/* 305 */         } else if (configurations.size() == 1) {
/* 306 */           AbstractConfiguration config = configurations.get(0);
/* 307 */           LOGGER.debug("Starting LoggerContext[name={}] from configuration at {}", ctx.getName(), config
/* 308 */               .getConfigurationSource().getLocation());
/* 309 */           ctx.start((Configuration)config);
/*     */         } else {
/* 311 */           CompositeConfiguration compositeConfiguration = new CompositeConfiguration(configurations);
/* 312 */           LOGGER.debug("Starting LoggerContext[name={}] from configurations at {}", ctx.getName(), configLocations);
/*     */           
/* 314 */           ctx.start((Configuration)compositeConfiguration);
/*     */         } 
/*     */         
/* 317 */         ContextAnchor.THREAD_CONTEXT.remove();
/*     */       } else {
/* 319 */         ctx.start();
/*     */       } 
/*     */     }
/* 322 */     return ctx;
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdown(String fqcn, ClassLoader loader, boolean currentContext, boolean allContexts) {
/* 327 */     if (this.selector.hasContext(fqcn, loader, currentContext)) {
/* 328 */       this.selector.shutdown(fqcn, loader, currentContext, allContexts);
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
/*     */   public boolean hasContext(String fqcn, ClassLoader loader, boolean currentContext) {
/* 343 */     return this.selector.hasContext(fqcn, loader, currentContext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContextSelector getSelector() {
/* 351 */     return this.selector;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ShutdownCallbackRegistry getShutdownCallbackRegistry() {
/* 361 */     return this.shutdownCallbackRegistry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeContext(LoggerContext context) {
/* 371 */     if (context instanceof LoggerContext) {
/* 372 */       this.selector.removeContext((LoggerContext)context);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isClassLoaderDependent() {
/* 378 */     return this.selector.isClassLoaderDependent();
/*     */   }
/*     */ 
/*     */   
/*     */   public Cancellable addShutdownCallback(Runnable callback) {
/* 383 */     return isShutdownHookEnabled() ? this.shutdownCallbackRegistry.addShutdownCallback(callback) : null;
/*     */   }
/*     */   
/*     */   public boolean isShutdownHookEnabled() {
/* 387 */     return SHUTDOWN_HOOK_ENABLED;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\impl\Log4jContextFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */