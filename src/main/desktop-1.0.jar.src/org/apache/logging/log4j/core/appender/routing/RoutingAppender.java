/*     */ package org.apache.logging.log4j.core.appender.routing;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import javax.script.Bindings;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.Appender;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LifeCycle2;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.AbstractAppender;
/*     */ import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
/*     */ import org.apache.logging.log4j.core.config.AppenderControl;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.Node;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.script.AbstractScript;
/*     */ import org.apache.logging.log4j.core.script.ScriptManager;
/*     */ import org.apache.logging.log4j.core.util.Booleans;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "Routing", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class RoutingAppender
/*     */   extends AbstractAppender
/*     */ {
/*     */   public static final String STATIC_VARIABLES_KEY = "staticVariables";
/*     */   private static final String DEFAULT_KEY = "ROUTING_APPENDER_DEFAULT";
/*     */   private final Routes routes;
/*     */   private Route defaultRoute;
/*     */   private final Configuration configuration;
/*     */   
/*     */   public static class Builder<B extends Builder<B>>
/*     */     extends AbstractAppender.Builder<B>
/*     */     implements org.apache.logging.log4j.core.util.Builder<RoutingAppender>
/*     */   {
/*     */     @PluginElement("Script")
/*     */     private AbstractScript defaultRouteScript;
/*     */     @PluginElement("Routes")
/*     */     private Routes routes;
/*     */     @PluginElement("RewritePolicy")
/*     */     private RewritePolicy rewritePolicy;
/*     */     @PluginElement("PurgePolicy")
/*     */     private PurgePolicy purgePolicy;
/*     */     
/*     */     public RoutingAppender build() {
/*  78 */       String name = getName();
/*  79 */       if (name == null) {
/*  80 */         RoutingAppender.LOGGER.error("No name defined for this RoutingAppender");
/*  81 */         return null;
/*     */       } 
/*  83 */       if (this.routes == null) {
/*  84 */         RoutingAppender.LOGGER.error("No routes defined for RoutingAppender {}", name);
/*  85 */         return null;
/*     */       } 
/*  87 */       return new RoutingAppender(name, getFilter(), isIgnoreExceptions(), this.routes, this.rewritePolicy, 
/*  88 */           getConfiguration(), this.purgePolicy, this.defaultRouteScript, getPropertyArray());
/*     */     }
/*     */     
/*     */     public Routes getRoutes() {
/*  92 */       return this.routes;
/*     */     }
/*     */     
/*     */     public AbstractScript getDefaultRouteScript() {
/*  96 */       return this.defaultRouteScript;
/*     */     }
/*     */     
/*     */     public RewritePolicy getRewritePolicy() {
/* 100 */       return this.rewritePolicy;
/*     */     }
/*     */     
/*     */     public PurgePolicy getPurgePolicy() {
/* 104 */       return this.purgePolicy;
/*     */     }
/*     */     
/*     */     public B withRoutes(Routes routes) {
/* 108 */       this.routes = routes;
/* 109 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withDefaultRouteScript(AbstractScript defaultRouteScript) {
/* 113 */       this.defaultRouteScript = defaultRouteScript;
/* 114 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withRewritePolicy(RewritePolicy rewritePolicy) {
/* 118 */       this.rewritePolicy = rewritePolicy;
/* 119 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public void withPurgePolicy(PurgePolicy purgePolicy) {
/* 123 */       this.purgePolicy = purgePolicy;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static <B extends Builder<B>> B newBuilder() {
/* 130 */     return (B)(new Builder<>()).asBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 138 */   private final ConcurrentMap<String, CreatedRouteAppenderControl> createdAppenders = new ConcurrentHashMap<>();
/* 139 */   private final Map<String, AppenderControl> createdAppendersUnmodifiableView = Collections.unmodifiableMap((Map)this.createdAppenders);
/*     */   
/* 141 */   private final ConcurrentMap<String, RouteAppenderControl> referencedAppenders = new ConcurrentHashMap<>();
/*     */   private final RewritePolicy rewritePolicy;
/*     */   private final PurgePolicy purgePolicy;
/*     */   private final AbstractScript defaultRouteScript;
/* 145 */   private final ConcurrentMap<Object, Object> scriptStaticVariables = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private RoutingAppender(String name, Filter filter, boolean ignoreExceptions, Routes routes, RewritePolicy rewritePolicy, Configuration configuration, PurgePolicy purgePolicy, AbstractScript defaultRouteScript, Property[] properties) {
/* 150 */     super(name, filter, null, ignoreExceptions, properties);
/* 151 */     this.routes = routes;
/* 152 */     this.configuration = configuration;
/* 153 */     this.rewritePolicy = rewritePolicy;
/* 154 */     this.purgePolicy = purgePolicy;
/* 155 */     if (this.purgePolicy != null) {
/* 156 */       this.purgePolicy.initialize(this);
/*     */     }
/* 158 */     this.defaultRouteScript = defaultRouteScript;
/* 159 */     Route defRoute = null;
/* 160 */     for (Route route : routes.getRoutes()) {
/* 161 */       if (route.getKey() == null) {
/* 162 */         if (defRoute == null) {
/* 163 */           defRoute = route;
/*     */         } else {
/* 165 */           error("Multiple default routes. Route " + route.toString() + " will be ignored");
/*     */         } 
/*     */       }
/*     */     } 
/* 169 */     this.defaultRoute = defRoute;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 174 */     if (this.defaultRouteScript != null) {
/* 175 */       if (this.configuration == null) {
/* 176 */         error("No Configuration defined for RoutingAppender; required for Script element.");
/*     */       } else {
/* 178 */         ScriptManager scriptManager = this.configuration.getScriptManager();
/* 179 */         scriptManager.addScript(this.defaultRouteScript);
/* 180 */         Bindings bindings = scriptManager.createBindings(this.defaultRouteScript);
/* 181 */         bindings.put("staticVariables", this.scriptStaticVariables);
/* 182 */         Object object = scriptManager.execute(this.defaultRouteScript.getName(), bindings);
/* 183 */         Route route = this.routes.getRoute(Objects.toString(object, null));
/* 184 */         if (route != null) {
/* 185 */           this.defaultRoute = route;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 190 */     for (Route route : this.routes.getRoutes()) {
/* 191 */       if (route.getAppenderRef() != null) {
/* 192 */         Appender appender = this.configuration.getAppender(route.getAppenderRef());
/* 193 */         if (appender != null) {
/* 194 */           String key = (route == this.defaultRoute) ? "ROUTING_APPENDER_DEFAULT" : route.getKey();
/* 195 */           this.referencedAppenders.put(key, new ReferencedRouteAppenderControl(appender));
/*     */         } else {
/* 197 */           error("Appender " + route.getAppenderRef() + " cannot be located. Route ignored");
/*     */         } 
/*     */       } 
/*     */     } 
/* 201 */     super.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 206 */     setStopping();
/* 207 */     stop(timeout, timeUnit, false);
/*     */     
/* 209 */     for (Map.Entry<String, CreatedRouteAppenderControl> entry : this.createdAppenders.entrySet()) {
/* 210 */       Appender appender = ((CreatedRouteAppenderControl)entry.getValue()).getAppender();
/* 211 */       if (appender instanceof LifeCycle2) {
/* 212 */         ((LifeCycle2)appender).stop(timeout, timeUnit); continue;
/*     */       } 
/* 214 */       appender.stop();
/*     */     } 
/*     */     
/* 217 */     setStopped();
/* 218 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void append(LogEvent event) {
/* 223 */     if (this.rewritePolicy != null) {
/* 224 */       event = this.rewritePolicy.rewrite(event);
/*     */     }
/* 226 */     String pattern = this.routes.getPattern(event, this.scriptStaticVariables);
/*     */     
/* 228 */     String key = (pattern != null) ? this.configuration.getStrSubstitutor().replace(event, pattern) : ((this.defaultRoute.getKey() != null) ? this.defaultRoute.getKey() : "ROUTING_APPENDER_DEFAULT");
/* 229 */     RouteAppenderControl control = getControl(key, event);
/* 230 */     if (control != null) {
/*     */       try {
/* 232 */         control.callAppender(event);
/*     */       } finally {
/* 234 */         control.release();
/*     */       } 
/*     */     }
/* 237 */     updatePurgePolicy(key, event);
/*     */   }
/*     */   
/*     */   private void updatePurgePolicy(String key, LogEvent event) {
/* 241 */     if (this.purgePolicy != null && 
/*     */ 
/*     */       
/* 244 */       !this.referencedAppenders.containsKey(key)) {
/* 245 */       this.purgePolicy.update(key, event);
/*     */     }
/*     */   }
/*     */   
/*     */   private synchronized RouteAppenderControl getControl(String key, LogEvent event) {
/* 250 */     RouteAppenderControl control = getAppender(key);
/* 251 */     if (control != null) {
/* 252 */       control.checkout();
/* 253 */       return control;
/*     */     } 
/* 255 */     Route route = null;
/* 256 */     for (Route r : this.routes.getRoutes()) {
/* 257 */       if (r.getAppenderRef() == null && key.equals(r.getKey())) {
/* 258 */         route = r;
/*     */         break;
/*     */       } 
/*     */     } 
/* 262 */     if (route == null) {
/* 263 */       route = this.defaultRoute;
/* 264 */       control = getAppender("ROUTING_APPENDER_DEFAULT");
/* 265 */       if (control != null) {
/* 266 */         control.checkout();
/* 267 */         return control;
/*     */       } 
/*     */     } 
/* 270 */     if (route != null) {
/* 271 */       Appender app = createAppender(route, event);
/* 272 */       if (app == null) {
/* 273 */         return null;
/*     */       }
/* 275 */       CreatedRouteAppenderControl created = new CreatedRouteAppenderControl(app);
/* 276 */       control = created;
/* 277 */       this.createdAppenders.put(key, created);
/*     */     } 
/*     */     
/* 280 */     if (control != null) {
/* 281 */       control.checkout();
/*     */     }
/* 283 */     return control;
/*     */   }
/*     */   
/*     */   private RouteAppenderControl getAppender(String key) {
/* 287 */     RouteAppenderControl result = this.referencedAppenders.get(key);
/* 288 */     if (result == null) {
/* 289 */       return this.createdAppenders.get(key);
/*     */     }
/* 291 */     return result;
/*     */   }
/*     */   
/*     */   private Appender createAppender(Route route, LogEvent event) {
/* 295 */     Node routeNode = route.getNode();
/* 296 */     for (Node node : routeNode.getChildren()) {
/* 297 */       if (node.getType().getElementName().equals("appender")) {
/* 298 */         Node appNode = new Node(node);
/* 299 */         this.configuration.createConfiguration(appNode, event);
/* 300 */         if (appNode.getObject() instanceof Appender) {
/* 301 */           Appender app = (Appender)appNode.getObject();
/* 302 */           app.start();
/* 303 */           return app;
/*     */         } 
/* 305 */         error("Unable to create Appender of type " + node.getName());
/* 306 */         return null;
/*     */       } 
/*     */     } 
/* 309 */     error("No Appender was configured for route " + route.getKey());
/* 310 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, AppenderControl> getAppenders() {
/* 318 */     return this.createdAppendersUnmodifiableView;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void deleteAppender(String key) {
/* 327 */     LOGGER.debug("Deleting route with {} key ", key);
/*     */     
/* 329 */     CreatedRouteAppenderControl control = this.createdAppenders.remove(key);
/* 330 */     if (null != control) {
/* 331 */       LOGGER.debug("Stopping route with {} key", key);
/*     */ 
/*     */       
/* 334 */       synchronized (this) {
/* 335 */         control.pendingDeletion = true;
/*     */       } 
/*     */ 
/*     */       
/* 339 */       control.tryStopAppender();
/* 340 */     } else if (this.referencedAppenders.containsKey(key)) {
/* 341 */       LOGGER.debug("Route {} using an appender reference may not be removed because the appender may be used outside of the RoutingAppender", key);
/*     */     } else {
/*     */       
/* 344 */       LOGGER.debug("Route with {} key already deleted", key);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static RoutingAppender createAppender(String name, String ignore, Routes routes, Configuration config, RewritePolicy rewritePolicy, PurgePolicy purgePolicy, Filter filter) {
/* 370 */     boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
/* 371 */     if (name == null) {
/* 372 */       LOGGER.error("No name provided for RoutingAppender");
/* 373 */       return null;
/*     */     } 
/* 375 */     if (routes == null) {
/* 376 */       LOGGER.error("No routes defined for RoutingAppender");
/* 377 */       return null;
/*     */     } 
/* 379 */     return new RoutingAppender(name, filter, ignoreExceptions, routes, rewritePolicy, config, purgePolicy, null, null);
/*     */   }
/*     */   
/*     */   public Route getDefaultRoute() {
/* 383 */     return this.defaultRoute;
/*     */   }
/*     */   
/*     */   public AbstractScript getDefaultRouteScript() {
/* 387 */     return this.defaultRouteScript;
/*     */   }
/*     */   
/*     */   public PurgePolicy getPurgePolicy() {
/* 391 */     return this.purgePolicy;
/*     */   }
/*     */   
/*     */   public RewritePolicy getRewritePolicy() {
/* 395 */     return this.rewritePolicy;
/*     */   }
/*     */   
/*     */   public Routes getRoutes() {
/* 399 */     return this.routes;
/*     */   }
/*     */   
/*     */   public Configuration getConfiguration() {
/* 403 */     return this.configuration;
/*     */   }
/*     */   
/*     */   public ConcurrentMap<Object, Object> getScriptStaticVariables() {
/* 407 */     return this.scriptStaticVariables;
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
/*     */   private static abstract class RouteAppenderControl
/*     */     extends AppenderControl
/*     */   {
/*     */     RouteAppenderControl(Appender appender) {
/* 426 */       super(appender, null, null);
/*     */     }
/*     */     
/*     */     abstract void checkout();
/*     */     
/*     */     abstract void release();
/*     */   }
/*     */   
/*     */   private static final class CreatedRouteAppenderControl
/*     */     extends RouteAppenderControl {
/*     */     private volatile boolean pendingDeletion;
/* 437 */     private final AtomicInteger depth = new AtomicInteger();
/*     */     
/*     */     CreatedRouteAppenderControl(Appender appender) {
/* 440 */       super(appender);
/*     */     }
/*     */ 
/*     */     
/*     */     void checkout() {
/* 445 */       if (this.pendingDeletion) {
/* 446 */         LOGGER.warn("CreatedRouteAppenderControl.checkout invoked on a RouteAppenderControl that is pending deletion");
/*     */       }
/*     */       
/* 449 */       this.depth.incrementAndGet();
/*     */     }
/*     */ 
/*     */     
/*     */     void release() {
/* 454 */       this.depth.decrementAndGet();
/* 455 */       tryStopAppender();
/*     */     }
/*     */     
/*     */     void tryStopAppender() {
/* 459 */       if (this.pendingDeletion && this.depth
/*     */ 
/*     */ 
/*     */         
/* 463 */         .compareAndSet(0, -100000)) {
/* 464 */         Appender appender = getAppender();
/* 465 */         LOGGER.debug("Stopping appender {}", appender);
/* 466 */         appender.stop();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ReferencedRouteAppenderControl
/*     */     extends RouteAppenderControl {
/*     */     ReferencedRouteAppenderControl(Appender appender) {
/* 474 */       super(appender);
/*     */     }
/*     */     
/*     */     void checkout() {}
/*     */     
/*     */     void release() {}
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\routing\RoutingAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */