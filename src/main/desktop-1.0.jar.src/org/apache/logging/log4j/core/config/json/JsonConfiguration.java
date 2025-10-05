/*     */ package org.apache.logging.log4j.core.config.json;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.config.AbstractConfiguration;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationSource;
/*     */ import org.apache.logging.log4j.core.config.Node;
/*     */ import org.apache.logging.log4j.core.config.Reconfigurable;
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
/*     */ public class JsonConfiguration
/*     */   extends AbstractConfiguration
/*     */   implements Reconfigurable
/*     */ {
/*  49 */   private static final String[] VERBOSE_CLASSES = new String[] { ResolverUtil.class.getName() };
/*  50 */   private final List<Status> status = new ArrayList<>();
/*     */   private JsonNode root;
/*     */   
/*     */   public JsonConfiguration(LoggerContext loggerContext, ConfigurationSource configSource) {
/*  54 */     super(loggerContext, configSource);
/*  55 */     File configFile = configSource.getFile();
/*     */     try {
/*     */       byte[] buffer;
/*  58 */       try (InputStream configStream = configSource.getInputStream()) {
/*  59 */         buffer = toByteArray(configStream);
/*     */       } 
/*  61 */       InputStream is = new ByteArrayInputStream(buffer);
/*  62 */       this.root = getObjectMapper().readTree(is);
/*  63 */       if (this.root.size() == 1) {
/*  64 */         for (JsonNode node : this.root) {
/*  65 */           this.root = node;
/*     */         }
/*     */       }
/*  68 */       processAttributes(this.rootNode, this.root);
/*     */       
/*  70 */       StatusConfiguration statusConfig = (new StatusConfiguration()).withVerboseClasses(VERBOSE_CLASSES).withStatus(getDefaultStatus());
/*  71 */       int monitorIntervalSeconds = 0;
/*  72 */       for (Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)this.rootNode.getAttributes().entrySet()) {
/*  73 */         String key = entry.getKey();
/*  74 */         String value = getConfigurationStrSubstitutor().replace(entry.getValue());
/*     */         
/*  76 */         if ("status".equalsIgnoreCase(key)) {
/*  77 */           statusConfig.withStatus(value); continue;
/*  78 */         }  if ("dest".equalsIgnoreCase(key)) {
/*  79 */           statusConfig.withDestination(value); continue;
/*  80 */         }  if ("shutdownHook".equalsIgnoreCase(key)) {
/*  81 */           this.isShutdownHookEnabled = !"disable".equalsIgnoreCase(value); continue;
/*  82 */         }  if ("shutdownTimeout".equalsIgnoreCase(key)) {
/*  83 */           this.shutdownTimeoutMillis = Long.parseLong(value); continue;
/*  84 */         }  if ("verbose".equalsIgnoreCase(entry.getKey())) {
/*  85 */           statusConfig.withVerbosity(value); continue;
/*  86 */         }  if ("packages".equalsIgnoreCase(key)) {
/*  87 */           this.pluginPackages.addAll(Arrays.asList(value.split(Patterns.COMMA_SEPARATOR))); continue;
/*  88 */         }  if ("name".equalsIgnoreCase(key)) {
/*  89 */           setName(value); continue;
/*  90 */         }  if ("monitorInterval".equalsIgnoreCase(key)) {
/*  91 */           monitorIntervalSeconds = Integer.parseInt(value); continue;
/*  92 */         }  if ("advertiser".equalsIgnoreCase(key)) {
/*  93 */           createAdvertiser(value, configSource, buffer, "application/json");
/*     */         }
/*     */       } 
/*  96 */       initializeWatchers(this, configSource, monitorIntervalSeconds);
/*  97 */       statusConfig.initialize();
/*  98 */       if (getName() == null) {
/*  99 */         setName(configSource.getLocation());
/*     */       }
/* 101 */     } catch (Exception ex) {
/* 102 */       LOGGER.error("Error parsing " + configSource.getLocation(), ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected ObjectMapper getObjectMapper() {
/* 107 */     return (new ObjectMapper()).configure(JsonParser.Feature.ALLOW_COMMENTS, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setup() {
/* 112 */     Iterator<Map.Entry<String, JsonNode>> iter = this.root.fields();
/* 113 */     List<Node> children = this.rootNode.getChildren();
/* 114 */     while (iter.hasNext()) {
/* 115 */       Map.Entry<String, JsonNode> entry = iter.next();
/* 116 */       JsonNode n = entry.getValue();
/* 117 */       if (n.isObject()) {
/* 118 */         LOGGER.debug("Processing node for object {}", entry.getKey());
/* 119 */         children.add(constructNode(entry.getKey(), this.rootNode, n)); continue;
/* 120 */       }  if (n.isArray()) {
/* 121 */         LOGGER.error("Arrays are not supported at the root configuration.");
/*     */       }
/*     */     } 
/* 124 */     LOGGER.debug("Completed parsing configuration");
/* 125 */     if (this.status.size() > 0) {
/* 126 */       for (Status s : this.status) {
/* 127 */         LOGGER.error("Error processing element {}: {}", s.name, s.errorType);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Configuration reconfigure() {
/*     */     try {
/* 135 */       ConfigurationSource source = getConfigurationSource().resetInputStream();
/* 136 */       if (source == null) {
/* 137 */         return null;
/*     */       }
/* 139 */       return (Configuration)new JsonConfiguration(getLoggerContext(), source);
/* 140 */     } catch (IOException ex) {
/* 141 */       LOGGER.error("Cannot locate file {}", getConfigurationSource(), ex);
/*     */       
/* 143 */       return null;
/*     */     } 
/*     */   } private Node constructNode(String name, Node parent, JsonNode jsonNode) {
/*     */     String t;
/* 147 */     PluginType<?> type = this.pluginManager.getPluginType(name);
/* 148 */     Node node = new Node(parent, name, type);
/* 149 */     processAttributes(node, jsonNode);
/* 150 */     Iterator<Map.Entry<String, JsonNode>> iter = jsonNode.fields();
/* 151 */     List<Node> children = node.getChildren();
/* 152 */     while (iter.hasNext()) {
/* 153 */       Map.Entry<String, JsonNode> entry = iter.next();
/* 154 */       JsonNode n = entry.getValue();
/* 155 */       if (n.isArray() || n.isObject()) {
/* 156 */         if (type == null) {
/* 157 */           this.status.add(new Status(name, n, ErrorType.CLASS_NOT_FOUND));
/*     */         }
/* 159 */         if (n.isArray()) {
/* 160 */           LOGGER.debug("Processing node for array {}", entry.getKey());
/* 161 */           for (int i = 0; i < n.size(); i++) {
/* 162 */             String pluginType = getType(n.get(i), entry.getKey());
/* 163 */             PluginType<?> entryType = this.pluginManager.getPluginType(pluginType);
/* 164 */             Node item = new Node(node, entry.getKey(), entryType);
/* 165 */             processAttributes(item, n.get(i));
/* 166 */             if (pluginType.equals(entry.getKey())) {
/* 167 */               LOGGER.debug("Processing {}[{}]", entry.getKey(), Integer.valueOf(i));
/*     */             } else {
/* 169 */               LOGGER.debug("Processing {} {}[{}]", pluginType, entry.getKey(), Integer.valueOf(i));
/*     */             } 
/* 171 */             Iterator<Map.Entry<String, JsonNode>> itemIter = n.get(i).fields();
/* 172 */             List<Node> itemChildren = item.getChildren();
/* 173 */             while (itemIter.hasNext()) {
/* 174 */               Map.Entry<String, JsonNode> itemEntry = itemIter.next();
/* 175 */               if (((JsonNode)itemEntry.getValue()).isObject()) {
/* 176 */                 LOGGER.debug("Processing node for object {}", itemEntry.getKey());
/* 177 */                 itemChildren.add(constructNode(itemEntry.getKey(), item, itemEntry.getValue())); continue;
/* 178 */               }  if (((JsonNode)itemEntry.getValue()).isArray()) {
/* 179 */                 JsonNode array = itemEntry.getValue();
/* 180 */                 String entryName = itemEntry.getKey();
/* 181 */                 LOGGER.debug("Processing array for object {}", entryName);
/* 182 */                 for (int j = 0; j < array.size(); j++) {
/* 183 */                   itemChildren.add(constructNode(entryName, item, array.get(j)));
/*     */                 }
/*     */               } 
/*     */             } 
/*     */             
/* 188 */             children.add(item);
/*     */           }  continue;
/*     */         } 
/* 191 */         LOGGER.debug("Processing node for object {}", entry.getKey());
/* 192 */         children.add(constructNode(entry.getKey(), node, n));
/*     */         continue;
/*     */       } 
/* 195 */       LOGGER.debug("Node {} is of type {}", entry.getKey(), n.getNodeType());
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 200 */     if (type == null) {
/* 201 */       t = "null";
/*     */     } else {
/* 203 */       t = type.getElementName() + ':' + type.getPluginClass();
/*     */     } 
/*     */ 
/*     */     
/* 207 */     String p = (node.getParent() == null) ? "null" : ((node.getParent().getName() == null) ? "root" : node.getParent().getName());
/* 208 */     LOGGER.debug("Returning {} with parent {} of type {}", node.getName(), p, t);
/* 209 */     return node;
/*     */   }
/*     */   
/*     */   private String getType(JsonNode node, String name) {
/* 213 */     Iterator<Map.Entry<String, JsonNode>> iter = node.fields();
/* 214 */     while (iter.hasNext()) {
/* 215 */       Map.Entry<String, JsonNode> entry = iter.next();
/* 216 */       if (((String)entry.getKey()).equalsIgnoreCase("type")) {
/* 217 */         JsonNode n = entry.getValue();
/* 218 */         if (n.isValueNode()) {
/* 219 */           return n.asText();
/*     */         }
/*     */       } 
/*     */     } 
/* 223 */     return name;
/*     */   }
/*     */   
/*     */   private void processAttributes(Node parent, JsonNode node) {
/* 227 */     Map<String, String> attrs = parent.getAttributes();
/* 228 */     Iterator<Map.Entry<String, JsonNode>> iter = node.fields();
/* 229 */     while (iter.hasNext()) {
/* 230 */       Map.Entry<String, JsonNode> entry = iter.next();
/* 231 */       if (!((String)entry.getKey()).equalsIgnoreCase("type")) {
/* 232 */         JsonNode n = entry.getValue();
/* 233 */         if (n.isValueNode()) {
/* 234 */           attrs.put(entry.getKey(), n.asText());
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 242 */     return getClass().getSimpleName() + "[location=" + getConfigurationSource() + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private enum ErrorType
/*     */   {
/* 249 */     CLASS_NOT_FOUND;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class Status
/*     */   {
/*     */     private final JsonNode node;
/*     */     
/*     */     private final String name;
/*     */     private final JsonConfiguration.ErrorType errorType;
/*     */     
/*     */     public Status(String name, JsonNode node, JsonConfiguration.ErrorType errorType) {
/* 261 */       this.name = name;
/* 262 */       this.node = node;
/* 263 */       this.errorType = errorType;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 268 */       return "Status [name=" + this.name + ", errorType=" + this.errorType + ", node=" + this.node + "]";
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\json\JsonConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */