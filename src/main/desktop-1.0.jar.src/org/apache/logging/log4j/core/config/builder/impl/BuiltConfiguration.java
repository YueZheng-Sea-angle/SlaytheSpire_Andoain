/*     */ package org.apache.logging.log4j.core.config.builder.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.config.AbstractConfiguration;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationSource;
/*     */ import org.apache.logging.log4j.core.config.Node;
/*     */ import org.apache.logging.log4j.core.config.Reconfigurable;
/*     */ import org.apache.logging.log4j.core.config.builder.api.Component;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginType;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.ResolverUtil;
/*     */ import org.apache.logging.log4j.core.config.status.StatusConfiguration;
/*     */ import org.apache.logging.log4j.core.util.Patterns;
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
/*     */ public class BuiltConfiguration
/*     */   extends AbstractConfiguration
/*     */ {
/*  43 */   private static final String[] VERBOSE_CLASSES = new String[] { ResolverUtil.class.getName() };
/*     */   private final StatusConfiguration statusConfig;
/*     */   protected Component rootComponent;
/*     */   private Component loggersComponent;
/*     */   private Component appendersComponent;
/*     */   private Component filtersComponent;
/*     */   private Component propertiesComponent;
/*     */   private Component customLevelsComponent;
/*     */   private Component scriptsComponent;
/*  52 */   private String contentType = "text";
/*     */   
/*     */   public BuiltConfiguration(LoggerContext loggerContext, ConfigurationSource source, Component rootComponent) {
/*  55 */     super(loggerContext, source);
/*  56 */     this.statusConfig = (new StatusConfiguration()).withVerboseClasses(VERBOSE_CLASSES).withStatus(getDefaultStatus());
/*  57 */     for (Component component : rootComponent.getComponents()) {
/*  58 */       switch (component.getPluginType()) {
/*     */         case "Scripts":
/*  60 */           this.scriptsComponent = component;
/*     */ 
/*     */         
/*     */         case "Loggers":
/*  64 */           this.loggersComponent = component;
/*     */ 
/*     */         
/*     */         case "Appenders":
/*  68 */           this.appendersComponent = component;
/*     */ 
/*     */         
/*     */         case "Filters":
/*  72 */           this.filtersComponent = component;
/*     */ 
/*     */         
/*     */         case "Properties":
/*  76 */           this.propertiesComponent = component;
/*     */ 
/*     */         
/*     */         case "CustomLevels":
/*  80 */           this.customLevelsComponent = component;
/*     */       } 
/*     */ 
/*     */     
/*     */     } 
/*  85 */     this.rootComponent = rootComponent;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setup() {
/*  90 */     List<Node> children = this.rootNode.getChildren();
/*  91 */     if (this.propertiesComponent.getComponents().size() > 0) {
/*  92 */       children.add(convertToNode(this.rootNode, this.propertiesComponent));
/*     */     }
/*  94 */     if (this.scriptsComponent.getComponents().size() > 0) {
/*  95 */       children.add(convertToNode(this.rootNode, this.scriptsComponent));
/*     */     }
/*  97 */     if (this.customLevelsComponent.getComponents().size() > 0) {
/*  98 */       children.add(convertToNode(this.rootNode, this.customLevelsComponent));
/*     */     }
/* 100 */     children.add(convertToNode(this.rootNode, this.loggersComponent));
/* 101 */     children.add(convertToNode(this.rootNode, this.appendersComponent));
/* 102 */     if (this.filtersComponent.getComponents().size() > 0) {
/* 103 */       if (this.filtersComponent.getComponents().size() == 1) {
/* 104 */         children.add(convertToNode(this.rootNode, this.filtersComponent.getComponents().get(0)));
/*     */       } else {
/* 106 */         children.add(convertToNode(this.rootNode, this.filtersComponent));
/*     */       } 
/*     */     }
/* 109 */     this.rootComponent = null;
/*     */   }
/*     */   
/*     */   public String getContentType() {
/* 113 */     return this.contentType;
/*     */   }
/*     */   
/*     */   public void setContentType(String contentType) {
/* 117 */     this.contentType = contentType;
/*     */   }
/*     */   
/*     */   public void createAdvertiser(String advertiserString, ConfigurationSource configSource) {
/* 121 */     byte[] buffer = null;
/*     */     try {
/* 123 */       if (configSource != null) {
/* 124 */         InputStream is = configSource.getInputStream();
/* 125 */         if (is != null) {
/* 126 */           buffer = toByteArray(is);
/*     */         }
/*     */       } 
/* 129 */     } catch (IOException ioe) {
/* 130 */       LOGGER.warn("Unable to read configuration source " + configSource.toString());
/*     */     } 
/* 132 */     createAdvertiser(advertiserString, configSource, buffer, this.contentType);
/*     */   }
/*     */   
/*     */   public StatusConfiguration getStatusConfiguration() {
/* 136 */     return this.statusConfig;
/*     */   }
/*     */   
/*     */   public void setPluginPackages(String packages) {
/* 140 */     this.pluginPackages.addAll(Arrays.asList(packages.split(Patterns.COMMA_SEPARATOR)));
/*     */   }
/*     */   
/*     */   public void setShutdownHook(String flag) {
/* 144 */     this.isShutdownHookEnabled = !"disable".equalsIgnoreCase(flag);
/*     */   }
/*     */   
/*     */   public void setShutdownTimeoutMillis(long shutdownTimeoutMillis) {
/* 148 */     this.shutdownTimeoutMillis = shutdownTimeoutMillis;
/*     */   }
/*     */   
/*     */   public void setMonitorInterval(int intervalSeconds) {
/* 152 */     if (this instanceof Reconfigurable && intervalSeconds > 0) {
/* 153 */       initializeWatchers((Reconfigurable)this, getConfigurationSource(), intervalSeconds);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public PluginManager getPluginManager() {
/* 159 */     return this.pluginManager;
/*     */   }
/*     */   
/*     */   protected Node convertToNode(Node parent, Component component) {
/* 163 */     String name = component.getPluginType();
/* 164 */     PluginType<?> pluginType = this.pluginManager.getPluginType(name);
/* 165 */     Node node = new Node(parent, name, pluginType);
/* 166 */     node.getAttributes().putAll(component.getAttributes());
/* 167 */     node.setValue(component.getValue());
/* 168 */     List<Node> children = node.getChildren();
/* 169 */     for (Component child : component.getComponents()) {
/* 170 */       children.add(convertToNode(node, child));
/*     */     }
/* 172 */     return node;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\builder\impl\BuiltConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */