/*     */ package org.apache.logging.log4j.core.appender.routing;
/*     */ 
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.config.Node;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginNode;
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
/*     */ @Plugin(name = "Route", category = "Core", printObject = true, deferChildren = true)
/*     */ public final class Route
/*     */ {
/*  33 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private final Node node;
/*     */   private final String appenderRef;
/*     */   private final String key;
/*     */   
/*     */   private Route(Node node, String appenderRef, String key) {
/*  40 */     this.node = node;
/*  41 */     this.appenderRef = appenderRef;
/*  42 */     this.key = key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getNode() {
/*  50 */     return this.node;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAppenderRef() {
/*  58 */     return this.appenderRef;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getKey() {
/*  66 */     return this.key;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  71 */     StringBuilder sb = new StringBuilder("Route(");
/*  72 */     sb.append("type=");
/*  73 */     if (this.appenderRef != null) {
/*  74 */       sb.append("static Reference=").append(this.appenderRef);
/*  75 */     } else if (this.node != null) {
/*  76 */       sb.append("dynamic - type=").append(this.node.getName());
/*     */     } else {
/*  78 */       sb.append("invalid Route");
/*     */     } 
/*  80 */     if (this.key != null) {
/*  81 */       sb.append(" key='").append(this.key).append('\'');
/*     */     } else {
/*  83 */       sb.append(" default");
/*     */     } 
/*  85 */     sb.append(')');
/*  86 */     return sb.toString();
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
/*     */   @PluginFactory
/*     */   public static Route createRoute(@PluginAttribute("ref") String appenderRef, @PluginAttribute("key") String key, @PluginNode Node node) {
/* 101 */     if (node != null && node.hasChildren()) {
/* 102 */       if (appenderRef != null) {
/* 103 */         LOGGER.error("A route cannot be configured with an appender reference and an appender definition");
/* 104 */         return null;
/*     */       } 
/* 106 */     } else if (appenderRef == null) {
/* 107 */       LOGGER.error("A route must specify an appender reference or an appender definition");
/* 108 */       return null;
/*     */     } 
/* 110 */     return new Route(node, appenderRef, key);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\routing\Route.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */