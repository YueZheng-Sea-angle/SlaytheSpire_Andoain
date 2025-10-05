/*     */ package org.apache.logging.log4j.core.appender.rewrite;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.impl.ContextDataFactory;
/*     */ import org.apache.logging.log4j.core.impl.Log4jLogEvent;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.StringMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "PropertiesRewritePolicy", category = "Core", elementType = "rewritePolicy", printObject = true)
/*     */ public final class PropertiesRewritePolicy
/*     */   implements RewritePolicy
/*     */ {
/*  47 */   protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private final Map<Property, Boolean> properties;
/*     */   
/*     */   private final Configuration config;
/*     */   
/*     */   private PropertiesRewritePolicy(Configuration config, List<Property> props) {
/*  54 */     this.config = config;
/*  55 */     this.properties = new HashMap<>(props.size());
/*  56 */     for (Property property : props) {
/*  57 */       Boolean interpolate = Boolean.valueOf(property.getValue().contains("${"));
/*  58 */       this.properties.put(property, interpolate);
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
/*     */   public LogEvent rewrite(LogEvent source) {
/*  70 */     StringMap newContextData = ContextDataFactory.createContextData(source.getContextData());
/*  71 */     for (Map.Entry<Property, Boolean> entry : this.properties.entrySet()) {
/*  72 */       Property prop = entry.getKey();
/*  73 */       newContextData.putValue(prop.getName(), ((Boolean)entry.getValue()).booleanValue() ? this.config
/*  74 */           .getStrSubstitutor().replace(prop.getValue()) : prop.getValue());
/*     */     } 
/*     */     
/*  77 */     return (LogEvent)(new Log4jLogEvent.Builder(source)).setContextData(newContextData).build();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  82 */     StringBuilder sb = new StringBuilder();
/*  83 */     sb.append(" {");
/*  84 */     boolean first = true;
/*  85 */     for (Map.Entry<Property, Boolean> entry : this.properties.entrySet()) {
/*  86 */       if (!first) {
/*  87 */         sb.append(", ");
/*     */       }
/*  89 */       Property prop = entry.getKey();
/*  90 */       sb.append(prop.getName()).append('=').append(prop.getValue());
/*  91 */       first = false;
/*     */     } 
/*  93 */     sb.append('}');
/*  94 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static PropertiesRewritePolicy createPolicy(@PluginConfiguration Configuration config, @PluginElement("Properties") Property[] props) {
/* 106 */     if (props == null || props.length == 0) {
/* 107 */       LOGGER.error("Properties must be specified for the PropertiesRewritePolicy");
/* 108 */       return null;
/*     */     } 
/* 110 */     List<Property> properties = Arrays.asList(props);
/* 111 */     return new PropertiesRewritePolicy(config, properties);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rewrite\PropertiesRewritePolicy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */