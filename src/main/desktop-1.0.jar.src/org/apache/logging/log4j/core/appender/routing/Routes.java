/*     */ package org.apache.logging.log4j.core.appender.routing;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import javax.script.Bindings;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
/*     */ import org.apache.logging.log4j.core.script.AbstractScript;
/*     */ import org.apache.logging.log4j.core.script.ScriptManager;
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
/*     */ @Plugin(name = "Routes", category = "Core", printObject = true)
/*     */ public final class Routes
/*     */ {
/*     */   private static final String LOG_EVENT_KEY = "logEvent";
/*     */   
/*     */   public static class Builder
/*     */     implements org.apache.logging.log4j.core.util.Builder<Routes>
/*     */   {
/*     */     @PluginConfiguration
/*     */     private Configuration configuration;
/*     */     @PluginAttribute("pattern")
/*     */     private String pattern;
/*     */     @PluginElement("Script")
/*     */     private AbstractScript patternScript;
/*     */     @PluginElement("Routes")
/*     */     @Required
/*     */     private Route[] routes;
/*     */     
/*     */     public Routes build() {
/*  65 */       if (this.routes == null || this.routes.length == 0) {
/*  66 */         Routes.LOGGER.error("No Routes configured.");
/*  67 */         return null;
/*     */       } 
/*  69 */       if ((this.patternScript != null && this.pattern != null) || (this.patternScript == null && this.pattern == null)) {
/*  70 */         Routes.LOGGER.warn("In a Routes element, you must configure either a Script element or a pattern attribute.");
/*     */       }
/*  72 */       if (this.patternScript != null) {
/*  73 */         if (this.configuration == null) {
/*  74 */           Routes.LOGGER.error("No Configuration defined for Routes; required for Script");
/*     */         } else {
/*  76 */           this.configuration.getScriptManager().addScript(this.patternScript);
/*     */         } 
/*     */       }
/*  79 */       return new Routes(this.configuration, this.patternScript, this.pattern, this.routes);
/*     */     }
/*     */     
/*     */     public Configuration getConfiguration() {
/*  83 */       return this.configuration;
/*     */     }
/*     */     
/*     */     public String getPattern() {
/*  87 */       return this.pattern;
/*     */     }
/*     */     
/*     */     public AbstractScript getPatternScript() {
/*  91 */       return this.patternScript;
/*     */     }
/*     */     
/*     */     public Route[] getRoutes() {
/*  95 */       return this.routes;
/*     */     }
/*     */     
/*     */     public Builder withConfiguration(Configuration configuration) {
/*  99 */       this.configuration = configuration;
/* 100 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withPattern(String pattern) {
/* 104 */       this.pattern = pattern;
/* 105 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withPatternScript(AbstractScript patternScript) {
/* 109 */       this.patternScript = patternScript;
/* 110 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withRoutes(Route[] routes) {
/* 114 */       this.routes = routes;
/* 115 */       return this;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 120 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private final Configuration configuration;
/*     */   
/*     */   private final String pattern;
/*     */   
/*     */   private final AbstractScript patternScript;
/*     */   
/*     */   private final Route[] routes;
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static Routes createRoutes(String pattern, Route... routes) {
/* 133 */     if (routes == null || routes.length == 0) {
/* 134 */       LOGGER.error("No routes configured");
/* 135 */       return null;
/*     */     } 
/* 137 */     return new Routes(null, null, pattern, routes);
/*     */   }
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/* 142 */     return new Builder();
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
/*     */   private Routes(Configuration configuration, AbstractScript patternScript, String pattern, Route... routes) {
/* 155 */     this.configuration = configuration;
/* 156 */     this.patternScript = patternScript;
/* 157 */     this.pattern = pattern;
/* 158 */     this.routes = routes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPattern(LogEvent event, ConcurrentMap<Object, Object> scriptStaticVariables) {
/* 168 */     if (this.patternScript != null) {
/* 169 */       ScriptManager scriptManager = this.configuration.getScriptManager();
/* 170 */       Bindings bindings = scriptManager.createBindings(this.patternScript);
/* 171 */       bindings.put("staticVariables", scriptStaticVariables);
/* 172 */       bindings.put("logEvent", event);
/* 173 */       Object object = scriptManager.execute(this.patternScript.getName(), bindings);
/* 174 */       bindings.remove("logEvent");
/* 175 */       return Objects.toString(object, null);
/*     */     } 
/* 177 */     return this.pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractScript getPatternScript() {
/* 185 */     return this.patternScript;
/*     */   }
/*     */   
/*     */   public Route getRoute(String key) {
/* 189 */     for (Route route : this.routes) {
/* 190 */       if (Objects.equals(route.getKey(), key)) {
/* 191 */         return route;
/*     */       }
/*     */     } 
/* 194 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Route[] getRoutes() {
/* 202 */     return this.routes;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 207 */     StringBuilder sb = new StringBuilder("{");
/* 208 */     boolean first = true;
/* 209 */     for (Route route : this.routes) {
/* 210 */       if (!first) {
/* 211 */         sb.append(',');
/*     */       }
/* 213 */       first = false;
/* 214 */       sb.append(route.toString());
/*     */     } 
/* 216 */     sb.append('}');
/* 217 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\routing\Routes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */