/*     */ package org.apache.logging.log4j.core.config.xml;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import javax.xml.validation.Schema;
/*     */ import javax.xml.validation.SchemaFactory;
/*     */ import javax.xml.validation.Validator;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.config.AbstractConfiguration;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationSource;
/*     */ import org.apache.logging.log4j.core.config.Node;
/*     */ import org.apache.logging.log4j.core.config.Reconfigurable;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginType;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.ResolverUtil;
/*     */ import org.apache.logging.log4j.core.config.status.StatusConfiguration;
/*     */ import org.apache.logging.log4j.core.util.Closer;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.core.util.Patterns;
/*     */ import org.apache.logging.log4j.core.util.Throwables;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.Text;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
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
/*     */ public class XmlConfiguration
/*     */   extends AbstractConfiguration
/*     */   implements Reconfigurable
/*     */ {
/*     */   private static final String XINCLUDE_FIXUP_LANGUAGE = "http://apache.org/xml/features/xinclude/fixup-language";
/*     */   private static final String XINCLUDE_FIXUP_BASE_URIS = "http://apache.org/xml/features/xinclude/fixup-base-uris";
/*  67 */   private static final String[] VERBOSE_CLASSES = new String[] { ResolverUtil.class.getName() };
/*     */   
/*     */   private static final String LOG4J_XSD = "Log4j-config.xsd";
/*  70 */   private final List<Status> status = new ArrayList<>();
/*     */   private Element rootElement;
/*     */   private boolean strict;
/*     */   private String schemaResource;
/*     */   
/*     */   public XmlConfiguration(LoggerContext loggerContext, ConfigurationSource configSource) {
/*  76 */     super(loggerContext, configSource);
/*  77 */     File configFile = configSource.getFile();
/*  78 */     byte[] buffer = null;
/*     */     try {
/*     */       Document document;
/*  81 */       InputStream configStream = configSource.getInputStream();
/*     */       try {
/*  83 */         buffer = toByteArray(configStream);
/*     */       } finally {
/*  85 */         Closer.closeSilently(configStream);
/*     */       } 
/*  87 */       InputSource source = new InputSource(new ByteArrayInputStream(buffer));
/*  88 */       source.setSystemId(configSource.getLocation());
/*  89 */       DocumentBuilder documentBuilder = newDocumentBuilder(true);
/*     */       
/*     */       try {
/*  92 */         document = documentBuilder.parse(source);
/*  93 */       } catch (Exception e) {
/*     */         
/*  95 */         Throwable throwable = Throwables.getRootCause(e);
/*  96 */         if (throwable instanceof UnsupportedOperationException) {
/*  97 */           LOGGER.warn("The DocumentBuilder {} does not support an operation: {}.Trying again without XInclude...", documentBuilder, e);
/*     */ 
/*     */ 
/*     */           
/* 101 */           document = newDocumentBuilder(false).parse(source);
/*     */         } else {
/* 103 */           throw e;
/*     */         } 
/*     */       } 
/* 106 */       this.rootElement = document.getDocumentElement();
/* 107 */       Map<String, String> attrs = processAttributes(this.rootNode, this.rootElement);
/*     */       
/* 109 */       StatusConfiguration statusConfig = (new StatusConfiguration()).withVerboseClasses(VERBOSE_CLASSES).withStatus(getDefaultStatus());
/* 110 */       int monitorIntervalSeconds = 0;
/* 111 */       for (Map.Entry<String, String> entry : attrs.entrySet()) {
/* 112 */         String key = entry.getKey();
/* 113 */         String value = getConfigurationStrSubstitutor().replace(entry.getValue());
/* 114 */         if ("status".equalsIgnoreCase(key)) {
/* 115 */           statusConfig.withStatus(value); continue;
/* 116 */         }  if ("dest".equalsIgnoreCase(key)) {
/* 117 */           statusConfig.withDestination(value); continue;
/* 118 */         }  if ("shutdownHook".equalsIgnoreCase(key)) {
/* 119 */           this.isShutdownHookEnabled = !"disable".equalsIgnoreCase(value); continue;
/* 120 */         }  if ("shutdownTimeout".equalsIgnoreCase(key)) {
/* 121 */           this.shutdownTimeoutMillis = Long.parseLong(value); continue;
/* 122 */         }  if ("verbose".equalsIgnoreCase(key)) {
/* 123 */           statusConfig.withVerbosity(value); continue;
/* 124 */         }  if ("packages".equalsIgnoreCase(key)) {
/* 125 */           this.pluginPackages.addAll(Arrays.asList(value.split(Patterns.COMMA_SEPARATOR))); continue;
/* 126 */         }  if ("name".equalsIgnoreCase(key)) {
/* 127 */           setName(value); continue;
/* 128 */         }  if ("strict".equalsIgnoreCase(key)) {
/* 129 */           this.strict = Boolean.parseBoolean(value); continue;
/* 130 */         }  if ("schema".equalsIgnoreCase(key)) {
/* 131 */           this.schemaResource = value; continue;
/* 132 */         }  if ("monitorInterval".equalsIgnoreCase(key)) {
/* 133 */           monitorIntervalSeconds = Integer.parseInt(value); continue;
/* 134 */         }  if ("advertiser".equalsIgnoreCase(key)) {
/* 135 */           createAdvertiser(value, configSource, buffer, "text/xml");
/*     */         }
/*     */       } 
/* 138 */       initializeWatchers(this, configSource, monitorIntervalSeconds);
/* 139 */       statusConfig.initialize();
/* 140 */     } catch (SAXException|IOException|ParserConfigurationException e) {
/* 141 */       LOGGER.error("Error parsing " + configSource.getLocation(), e);
/*     */     } 
/* 143 */     if (this.strict && this.schemaResource != null && buffer != null) {
/* 144 */       try (InputStream is = Loader.getResourceAsStream(this.schemaResource, XmlConfiguration.class.getClassLoader())) {
/* 145 */         if (is != null) {
/* 146 */           Source src = new StreamSource(is, "Log4j-config.xsd");
/* 147 */           SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
/* 148 */           Schema schema = null;
/*     */           try {
/* 150 */             schema = factory.newSchema(src);
/* 151 */           } catch (SAXException ex) {
/* 152 */             LOGGER.error("Error parsing Log4j schema", ex);
/*     */           } 
/* 154 */           if (schema != null) {
/* 155 */             Validator validator = schema.newValidator();
/*     */             try {
/* 157 */               validator.validate(new StreamSource(new ByteArrayInputStream(buffer)));
/* 158 */             } catch (IOException ioe) {
/* 159 */               LOGGER.error("Error reading configuration for validation", ioe);
/* 160 */             } catch (SAXException ex) {
/* 161 */               LOGGER.error("Error validating configuration", ex);
/*     */             } 
/*     */           } 
/*     */         } 
/* 165 */       } catch (Exception ex) {
/* 166 */         LOGGER.error("Unable to access schema {}", this.schemaResource, ex);
/*     */       } 
/*     */     }
/*     */     
/* 170 */     if (getName() == null) {
/* 171 */       setName(configSource.getLocation());
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
/*     */   static DocumentBuilder newDocumentBuilder(boolean xIncludeAware) throws ParserConfigurationException {
/* 183 */     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/* 184 */     factory.setNamespaceAware(true);
/*     */     
/* 186 */     disableDtdProcessing(factory);
/*     */     
/* 188 */     if (xIncludeAware) {
/* 189 */       enableXInclude(factory);
/*     */     }
/* 191 */     return factory.newDocumentBuilder();
/*     */   }
/*     */   
/*     */   private static void disableDtdProcessing(DocumentBuilderFactory factory) {
/* 195 */     factory.setValidating(false);
/* 196 */     factory.setExpandEntityReferences(false);
/* 197 */     setFeature(factory, "http://xml.org/sax/features/external-general-entities", false);
/* 198 */     setFeature(factory, "http://xml.org/sax/features/external-parameter-entities", false);
/* 199 */     setFeature(factory, "http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
/*     */   }
/*     */   
/*     */   private static void setFeature(DocumentBuilderFactory factory, String featureName, boolean value) {
/*     */     try {
/* 204 */       factory.setFeature(featureName, value);
/* 205 */     } catch (Exception|LinkageError e) {
/* 206 */       getStatusLogger().error("Caught {} setting feature {} to {} on DocumentBuilderFactory {}: {}", e
/* 207 */           .getClass().getCanonicalName(), featureName, Boolean.valueOf(value), factory, e, e);
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
/*     */   private static void enableXInclude(DocumentBuilderFactory factory) {
/*     */     try {
/* 220 */       factory.setXIncludeAware(true);
/* 221 */     } catch (UnsupportedOperationException e) {
/* 222 */       LOGGER.warn("The DocumentBuilderFactory [{}] does not support XInclude: {}", factory, e);
/* 223 */     } catch (AbstractMethodError|NoSuchMethodError err) {
/* 224 */       LOGGER.warn("The DocumentBuilderFactory [{}] is out of date and does not support XInclude: {}", factory, err);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 230 */       factory.setFeature("http://apache.org/xml/features/xinclude/fixup-base-uris", true);
/* 231 */     } catch (ParserConfigurationException e) {
/* 232 */       LOGGER.warn("The DocumentBuilderFactory [{}] does not support the feature [{}]: {}", factory, "http://apache.org/xml/features/xinclude/fixup-base-uris", e);
/*     */     }
/* 234 */     catch (AbstractMethodError err) {
/* 235 */       LOGGER.warn("The DocumentBuilderFactory [{}] is out of date and does not support setFeature: {}", factory, err);
/*     */     } 
/*     */     
/*     */     try {
/* 239 */       factory.setFeature("http://apache.org/xml/features/xinclude/fixup-language", true);
/* 240 */     } catch (ParserConfigurationException e) {
/* 241 */       LOGGER.warn("The DocumentBuilderFactory [{}] does not support the feature [{}]: {}", factory, "http://apache.org/xml/features/xinclude/fixup-language", e);
/*     */     }
/* 243 */     catch (AbstractMethodError err) {
/* 244 */       LOGGER.warn("The DocumentBuilderFactory [{}] is out of date and does not support setFeature: {}", factory, err);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setup() {
/* 251 */     if (this.rootElement == null) {
/* 252 */       LOGGER.error("No logging configuration");
/*     */       return;
/*     */     } 
/* 255 */     constructHierarchy(this.rootNode, this.rootElement);
/* 256 */     if (this.status.size() > 0) {
/* 257 */       for (Status s : this.status) {
/* 258 */         LOGGER.error("Error processing element {} ({}): {}", s.name, s.element, s.errorType);
/*     */       }
/*     */       return;
/*     */     } 
/* 262 */     this.rootElement = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Configuration reconfigure() {
/*     */     try {
/* 268 */       ConfigurationSource source = getConfigurationSource().resetInputStream();
/* 269 */       if (source == null) {
/* 270 */         return null;
/*     */       }
/* 272 */       XmlConfiguration config = new XmlConfiguration(getLoggerContext(), source);
/* 273 */       return (config.rootElement == null) ? null : (Configuration)config;
/* 274 */     } catch (IOException ex) {
/* 275 */       LOGGER.error("Cannot locate file {}", getConfigurationSource(), ex);
/*     */       
/* 277 */       return null;
/*     */     } 
/*     */   }
/*     */   private void constructHierarchy(Node node, Element element) {
/* 281 */     processAttributes(node, element);
/* 282 */     StringBuilder buffer = new StringBuilder();
/* 283 */     NodeList list = element.getChildNodes();
/* 284 */     List<Node> children = node.getChildren();
/* 285 */     for (int i = 0; i < list.getLength(); i++) {
/* 286 */       Node w3cNode = list.item(i);
/* 287 */       if (w3cNode instanceof Element) {
/* 288 */         Element child = (Element)w3cNode;
/* 289 */         String name = getType(child);
/* 290 */         PluginType<?> type = this.pluginManager.getPluginType(name);
/* 291 */         Node childNode = new Node(node, name, type);
/* 292 */         constructHierarchy(childNode, child);
/* 293 */         if (type == null) {
/* 294 */           String value = childNode.getValue();
/* 295 */           if (!childNode.hasChildren() && value != null) {
/* 296 */             node.getAttributes().put(name, value);
/*     */           } else {
/* 298 */             this.status.add(new Status(name, element, ErrorType.CLASS_NOT_FOUND));
/*     */           } 
/*     */         } else {
/* 301 */           children.add(childNode);
/*     */         } 
/* 303 */       } else if (w3cNode instanceof Text) {
/* 304 */         Text data = (Text)w3cNode;
/* 305 */         buffer.append(data.getData());
/*     */       } 
/*     */     } 
/*     */     
/* 309 */     String text = buffer.toString().trim();
/* 310 */     if (text.length() > 0 || (!node.hasChildren() && !node.isRoot())) {
/* 311 */       node.setValue(text);
/*     */     }
/*     */   }
/*     */   
/*     */   private String getType(Element element) {
/* 316 */     if (this.strict) {
/* 317 */       NamedNodeMap attrs = element.getAttributes();
/* 318 */       for (int i = 0; i < attrs.getLength(); i++) {
/* 319 */         Node w3cNode = attrs.item(i);
/* 320 */         if (w3cNode instanceof Attr) {
/* 321 */           Attr attr = (Attr)w3cNode;
/* 322 */           if (attr.getName().equalsIgnoreCase("type")) {
/* 323 */             String type = attr.getValue();
/* 324 */             attrs.removeNamedItem(attr.getName());
/* 325 */             return type;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 330 */     return element.getTagName();
/*     */   }
/*     */   
/*     */   private Map<String, String> processAttributes(Node node, Element element) {
/* 334 */     NamedNodeMap attrs = element.getAttributes();
/* 335 */     Map<String, String> attributes = node.getAttributes();
/*     */     
/* 337 */     for (int i = 0; i < attrs.getLength(); i++) {
/* 338 */       Node w3cNode = attrs.item(i);
/* 339 */       if (w3cNode instanceof Attr) {
/* 340 */         Attr attr = (Attr)w3cNode;
/* 341 */         if (!attr.getName().equals("xml:base"))
/*     */         {
/*     */           
/* 344 */           attributes.put(attr.getName(), attr.getValue()); } 
/*     */       } 
/*     */     } 
/* 347 */     return attributes;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 352 */     return getClass().getSimpleName() + "[location=" + getConfigurationSource() + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private enum ErrorType
/*     */   {
/* 359 */     CLASS_NOT_FOUND;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class Status
/*     */   {
/*     */     private final Element element;
/*     */     
/*     */     private final String name;
/*     */     private final XmlConfiguration.ErrorType errorType;
/*     */     
/*     */     public Status(String name, Element element, XmlConfiguration.ErrorType errorType) {
/* 371 */       this.name = name;
/* 372 */       this.element = element;
/* 373 */       this.errorType = errorType;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 378 */       return "Status [name=" + this.name + ", element=" + this.element + ", errorType=" + this.errorType + "]";
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\xml\XmlConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */