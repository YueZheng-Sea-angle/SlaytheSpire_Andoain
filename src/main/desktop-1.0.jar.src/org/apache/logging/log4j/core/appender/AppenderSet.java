/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.core.Appender;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.Node;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginNode;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
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
/*     */ @Plugin(name = "AppenderSet", category = "Core", printObject = true, deferChildren = true)
/*     */ public class AppenderSet
/*     */ {
/*     */   public static class Builder
/*     */     implements org.apache.logging.log4j.core.util.Builder<AppenderSet>
/*     */   {
/*     */     @PluginNode
/*     */     private Node node;
/*     */     @PluginConfiguration
/*     */     @Required
/*     */     private Configuration configuration;
/*     */     
/*     */     public AppenderSet build() {
/*  51 */       if (this.configuration == null) {
/*  52 */         AppenderSet.LOGGER.error("Configuration is missing from AppenderSet {}", this);
/*  53 */         return null;
/*     */       } 
/*  55 */       if (this.node == null) {
/*  56 */         AppenderSet.LOGGER.error("No node in AppenderSet {}", this);
/*  57 */         return null;
/*     */       } 
/*  59 */       List<Node> children = this.node.getChildren();
/*  60 */       if (children == null) {
/*  61 */         AppenderSet.LOGGER.error("No children node in AppenderSet {}", this);
/*  62 */         return null;
/*     */       } 
/*  64 */       Map<String, Node> map = new HashMap<>(children.size());
/*  65 */       for (Node childNode : children) {
/*  66 */         String key = (String)childNode.getAttributes().get("name");
/*  67 */         if (key == null) {
/*  68 */           AppenderSet.LOGGER.error("The attribute 'name' is missing from from the node {} in AppenderSet {}", childNode, children);
/*     */           continue;
/*     */         } 
/*  71 */         map.put(key, childNode);
/*     */       } 
/*     */       
/*  74 */       return new AppenderSet(this.configuration, map);
/*     */     }
/*     */     
/*     */     public Node getNode() {
/*  78 */       return this.node;
/*     */     }
/*     */     
/*     */     public Configuration getConfiguration() {
/*  82 */       return this.configuration;
/*     */     }
/*     */     
/*     */     public Builder withNode(Node node) {
/*  86 */       this.node = node;
/*  87 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withConfiguration(Configuration configuration) {
/*  91 */       this.configuration = configuration;
/*  92 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  97 */       return getClass().getName() + " [node=" + this.node + ", configuration=" + this.configuration + "]";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 102 */   private static final StatusLogger LOGGER = StatusLogger.getLogger();
/*     */   
/*     */   private final Configuration configuration;
/*     */   private final Map<String, Node> nodeMap;
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/* 109 */     return new Builder();
/*     */   }
/*     */   
/*     */   private AppenderSet(Configuration configuration, Map<String, Node> appenders) {
/* 113 */     this.configuration = configuration;
/* 114 */     this.nodeMap = appenders;
/*     */   }
/*     */   
/*     */   public Appender createAppender(String actualAppenderName, String sourceAppenderName) {
/* 118 */     Node node = this.nodeMap.get(actualAppenderName);
/* 119 */     if (node == null) {
/* 120 */       LOGGER.error("No node named {} in {}", actualAppenderName, this);
/* 121 */       return null;
/*     */     } 
/* 123 */     node.getAttributes().put("name", sourceAppenderName);
/* 124 */     if (node.getType().getElementName().equals("appender")) {
/* 125 */       Node appNode = new Node(node);
/* 126 */       this.configuration.createConfiguration(appNode, null);
/* 127 */       if (appNode.getObject() instanceof Appender) {
/* 128 */         Appender app = (Appender)appNode.getObject();
/* 129 */         app.start();
/* 130 */         return app;
/*     */       } 
/* 132 */       LOGGER.error("Unable to create Appender of type " + node.getName());
/* 133 */       return null;
/*     */     } 
/* 135 */     LOGGER.error("No Appender was configured for name {} " + actualAppenderName);
/* 136 */     return null;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\AppenderSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */