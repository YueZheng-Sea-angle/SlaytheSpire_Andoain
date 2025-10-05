/*     */ package org.apache.logging.log4j.core.config.composite;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.config.AbstractConfiguration;
/*     */ import org.apache.logging.log4j.core.config.Node;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginType;
/*     */ import org.apache.logging.log4j.core.filter.CompositeFilter;
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
/*     */ public class DefaultMergeStrategy
/*     */   implements MergeStrategy
/*     */ {
/*     */   private static final String APPENDERS = "appenders";
/*     */   private static final String PROPERTIES = "properties";
/*     */   private static final String LOGGERS = "loggers";
/*     */   private static final String SCRIPTS = "scripts";
/*     */   private static final String FILTERS = "filters";
/*     */   private static final String STATUS = "status";
/*     */   private static final String NAME = "name";
/*     */   private static final String REF = "ref";
/*     */   
/*     */   public void mergeRootProperties(Node rootNode, AbstractConfiguration configuration) {
/*  73 */     for (Map.Entry<String, String> attribute : (Iterable<Map.Entry<String, String>>)configuration.getRootNode().getAttributes().entrySet()) {
/*  74 */       boolean isFound = false;
/*  75 */       for (Map.Entry<String, String> targetAttribute : (Iterable<Map.Entry<String, String>>)rootNode.getAttributes().entrySet()) {
/*  76 */         if (((String)targetAttribute.getKey()).equalsIgnoreCase(attribute.getKey())) {
/*  77 */           if (((String)attribute.getKey()).equalsIgnoreCase("status")) {
/*  78 */             Level targetLevel = Level.getLevel(((String)targetAttribute.getValue()).toUpperCase());
/*  79 */             Level sourceLevel = Level.getLevel(((String)attribute.getValue()).toUpperCase());
/*  80 */             if (targetLevel != null && sourceLevel != null) {
/*  81 */               if (sourceLevel.isLessSpecificThan(targetLevel)) {
/*  82 */                 targetAttribute.setValue(attribute.getValue());
/*     */               }
/*     */             }
/*  85 */             else if (sourceLevel != null) {
/*  86 */               targetAttribute.setValue(attribute.getValue());
/*     */             } 
/*  88 */           } else if (((String)attribute.getKey()).equalsIgnoreCase("monitorInterval")) {
/*  89 */             int sourceInterval = Integer.parseInt(attribute.getValue());
/*  90 */             int targetInterval = Integer.parseInt(targetAttribute.getValue());
/*  91 */             if (targetInterval == 0 || sourceInterval < targetInterval) {
/*  92 */               targetAttribute.setValue(attribute.getValue());
/*     */             }
/*  94 */           } else if (((String)attribute.getKey()).equalsIgnoreCase("packages")) {
/*  95 */             String sourcePackages = attribute.getValue();
/*  96 */             String targetPackages = targetAttribute.getValue();
/*  97 */             if (sourcePackages != null) {
/*  98 */               if (targetPackages != null) {
/*  99 */                 targetAttribute.setValue(targetPackages + "," + sourcePackages);
/*     */               } else {
/*     */                 
/* 102 */                 targetAttribute.setValue(sourcePackages);
/*     */               } 
/*     */             }
/*     */           } else {
/*     */             
/* 107 */             targetAttribute.setValue(attribute.getValue());
/*     */           } 
/* 109 */           isFound = true;
/*     */         } 
/*     */       } 
/* 112 */       if (!isFound) {
/* 113 */         rootNode.getAttributes().put(attribute.getKey(), attribute.getValue());
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
/*     */   public void mergConfigurations(Node target, Node source, PluginManager pluginManager) {
/* 127 */     for (Node sourceChildNode : source.getChildren()) {
/* 128 */       boolean isFilter = isFilterNode(sourceChildNode);
/* 129 */       boolean isMerged = false;
/* 130 */       for (Node targetChildNode : target.getChildren()) {
/* 131 */         Map<String, Node> targetLoggers; if (isFilter) {
/* 132 */           if (isFilterNode(targetChildNode)) {
/* 133 */             updateFilterNode(target, targetChildNode, sourceChildNode, pluginManager);
/* 134 */             isMerged = true;
/*     */             
/*     */             break;
/*     */           } 
/*     */           continue;
/*     */         } 
/* 140 */         if (!targetChildNode.getName().equalsIgnoreCase(sourceChildNode.getName())) {
/*     */           continue;
/*     */         }
/*     */         
/* 144 */         switch (targetChildNode.getName().toLowerCase()) {
/*     */           case "properties":
/*     */           case "scripts":
/*     */           case "appenders":
/* 148 */             for (Node node : sourceChildNode.getChildren()) {
/* 149 */               for (Node targetNode : targetChildNode.getChildren()) {
/* 150 */                 if (Objects.equals(targetNode.getAttributes().get("name"), node.getAttributes().get("name"))) {
/* 151 */                   targetChildNode.getChildren().remove(targetNode);
/*     */                   break;
/*     */                 } 
/*     */               } 
/* 155 */               targetChildNode.getChildren().add(node);
/*     */             } 
/* 157 */             isMerged = true;
/*     */             continue;
/*     */           
/*     */           case "loggers":
/* 161 */             targetLoggers = new HashMap<>();
/* 162 */             for (Node node : targetChildNode.getChildren()) {
/* 163 */               targetLoggers.put(node.getName(), node);
/*     */             }
/* 165 */             for (Node node : sourceChildNode.getChildren()) {
/* 166 */               Node targetNode = getLoggerNode(targetChildNode, (String)node.getAttributes().get("name"));
/* 167 */               Node loggerNode = new Node(targetChildNode, node.getName(), node.getType());
/* 168 */               if (targetNode != null) {
/* 169 */                 targetNode.getAttributes().putAll(node.getAttributes());
/* 170 */                 for (Node sourceLoggerChild : node.getChildren()) {
/* 171 */                   if (isFilterNode(sourceLoggerChild)) {
/* 172 */                     boolean foundFilter = false;
/* 173 */                     for (Node targetChild : targetNode.getChildren()) {
/* 174 */                       if (isFilterNode(targetChild)) {
/* 175 */                         updateFilterNode(loggerNode, targetChild, sourceLoggerChild, pluginManager);
/*     */                         
/* 177 */                         foundFilter = true;
/*     */                         break;
/*     */                       } 
/*     */                     } 
/* 181 */                     if (!foundFilter) {
/*     */                       
/* 183 */                       Node node1 = new Node(loggerNode, sourceLoggerChild.getName(), sourceLoggerChild.getType());
/* 184 */                       node1.getAttributes().putAll(sourceLoggerChild.getAttributes());
/* 185 */                       node1.getChildren().addAll(sourceLoggerChild.getChildren());
/* 186 */                       targetNode.getChildren().add(node1);
/*     */                     } 
/*     */                     continue;
/*     */                   } 
/* 190 */                   Node childNode = new Node(loggerNode, sourceLoggerChild.getName(), sourceLoggerChild.getType());
/* 191 */                   childNode.getAttributes().putAll(sourceLoggerChild.getAttributes());
/* 192 */                   childNode.getChildren().addAll(sourceLoggerChild.getChildren());
/* 193 */                   if (childNode.getName().equalsIgnoreCase("AppenderRef")) {
/* 194 */                     for (Node targetChild : targetNode.getChildren()) {
/* 195 */                       if (isSameReference(targetChild, childNode)) {
/* 196 */                         targetNode.getChildren().remove(targetChild);
/*     */                         break;
/*     */                       } 
/*     */                     } 
/*     */                   } else {
/* 201 */                     for (Node targetChild : targetNode.getChildren()) {
/* 202 */                       if (isSameName(targetChild, childNode)) {
/* 203 */                         targetNode.getChildren().remove(targetChild);
/*     */                         
/*     */                         break;
/*     */                       } 
/*     */                     } 
/*     */                   } 
/* 209 */                   targetNode.getChildren().add(childNode);
/*     */                 } 
/*     */                 continue;
/*     */               } 
/* 213 */               loggerNode.getAttributes().putAll(node.getAttributes());
/* 214 */               loggerNode.getChildren().addAll(node.getChildren());
/* 215 */               targetChildNode.getChildren().add(loggerNode);
/*     */             } 
/*     */             
/* 218 */             isMerged = true;
/*     */             continue;
/*     */         } 
/*     */         
/* 222 */         targetChildNode.getChildren().addAll(sourceChildNode.getChildren());
/* 223 */         isMerged = true;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 229 */       if (!isMerged) {
/* 230 */         if (sourceChildNode.getName().equalsIgnoreCase("Properties")) {
/* 231 */           target.getChildren().add(0, sourceChildNode); continue;
/*     */         } 
/* 233 */         target.getChildren().add(sourceChildNode);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private Node getLoggerNode(Node parentNode, String name) {
/* 240 */     for (Node node : parentNode.getChildren()) {
/* 241 */       String nodeName = (String)node.getAttributes().get("name");
/* 242 */       if (name == null && nodeName == null) {
/* 243 */         return node;
/*     */       }
/* 245 */       if (nodeName != null && nodeName.equals(name)) {
/* 246 */         return node;
/*     */       }
/*     */     } 
/* 249 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateFilterNode(Node target, Node targetChildNode, Node sourceChildNode, PluginManager pluginManager) {
/* 254 */     if (CompositeFilter.class.isAssignableFrom(targetChildNode.getType().getPluginClass())) {
/* 255 */       Node node = new Node(targetChildNode, sourceChildNode.getName(), sourceChildNode.getType());
/* 256 */       node.getChildren().addAll(sourceChildNode.getChildren());
/* 257 */       node.getAttributes().putAll(sourceChildNode.getAttributes());
/* 258 */       targetChildNode.getChildren().add(node);
/*     */     } else {
/* 260 */       PluginType pluginType = pluginManager.getPluginType("filters");
/* 261 */       Node filtersNode = new Node(targetChildNode, "filters", pluginType);
/* 262 */       Node node = new Node(filtersNode, sourceChildNode.getName(), sourceChildNode.getType());
/* 263 */       node.getAttributes().putAll(sourceChildNode.getAttributes());
/* 264 */       List<Node> children = filtersNode.getChildren();
/* 265 */       children.add(targetChildNode);
/* 266 */       children.add(node);
/* 267 */       List<Node> nodes = target.getChildren();
/* 268 */       nodes.remove(targetChildNode);
/* 269 */       nodes.add(filtersNode);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isFilterNode(Node node) {
/* 274 */     return Filter.class.isAssignableFrom(node.getType().getPluginClass());
/*     */   }
/*     */   
/*     */   private boolean isSameName(Node node1, Node node2) {
/* 278 */     String value = (String)node1.getAttributes().get("name");
/* 279 */     return (value != null && value.toLowerCase().equals(((String)node2.getAttributes().get("name")).toLowerCase()));
/*     */   }
/*     */   
/*     */   private boolean isSameReference(Node node1, Node node2) {
/* 283 */     String value = (String)node1.getAttributes().get("ref");
/* 284 */     return (value != null && value.toLowerCase().equals(((String)node2.getAttributes().get("ref")).toLowerCase()));
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\composite\DefaultMergeStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */