/*     */ package org.apache.logging.log4j.core.config.builder.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.StringReader;
/*     */ import java.io.StringWriter;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.xml.stream.XMLOutputFactory;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerConfigurationException;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.TransformerFactoryConfigurationError;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationException;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationSource;
/*     */ import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.AppenderRefComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.Component;
/*     */ import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.CustomLevelComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.FilterComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.KeyValuePairComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.LoggerComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.PropertyComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.ScriptComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.ScriptFileComponentBuilder;
/*     */ import org.apache.logging.log4j.core.util.Throwables;
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
/*     */ public class DefaultConfigurationBuilder<T extends BuiltConfiguration>
/*     */   implements ConfigurationBuilder<T>
/*     */ {
/*     */   private static final String INDENT = "  ";
/*  73 */   private final Component root = new Component();
/*     */   
/*     */   private Component loggers;
/*     */   private Component appenders;
/*     */   private Component filters;
/*     */   private Component properties;
/*     */   private Component customLevels;
/*     */   private Component scripts;
/*     */   private final Class<T> clazz;
/*     */   private ConfigurationSource source;
/*     */   private int monitorInterval;
/*     */   private Level level;
/*     */   private String verbosity;
/*     */   private String destination;
/*     */   private String packages;
/*     */   private String shutdownFlag;
/*     */   private long shutdownTimeoutMillis;
/*     */   private String advertiser;
/*     */   private LoggerContext loggerContext;
/*     */   private String name;
/*     */   
/*     */   public static void formatXml(Source source, Result result) throws TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException {
/*  95 */     Transformer transformer = TransformerFactory.newInstance().newTransformer();
/*  96 */     transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", Integer.toString("  ".length()));
/*  97 */     transformer.setOutputProperty("indent", "yes");
/*  98 */     transformer.transform(source, result);
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultConfigurationBuilder() {
/* 103 */     this((Class)BuiltConfiguration.class);
/* 104 */     this.root.addAttribute("name", "Built");
/*     */   }
/*     */   
/*     */   public DefaultConfigurationBuilder(Class<T> clazz) {
/* 108 */     if (clazz == null) {
/* 109 */       throw new IllegalArgumentException("A Configuration class must be provided");
/*     */     }
/* 111 */     this.clazz = clazz;
/* 112 */     List<Component> components = this.root.getComponents();
/* 113 */     this.properties = new Component("Properties");
/* 114 */     components.add(this.properties);
/* 115 */     this.scripts = new Component("Scripts");
/* 116 */     components.add(this.scripts);
/* 117 */     this.customLevels = new Component("CustomLevels");
/* 118 */     components.add(this.customLevels);
/* 119 */     this.filters = new Component("Filters");
/* 120 */     components.add(this.filters);
/* 121 */     this.appenders = new Component("Appenders");
/* 122 */     components.add(this.appenders);
/* 123 */     this.loggers = new Component("Loggers");
/* 124 */     components.add(this.loggers);
/*     */   }
/*     */   
/*     */   protected ConfigurationBuilder<T> add(Component parent, ComponentBuilder<?> builder) {
/* 128 */     parent.getComponents().add(builder.build());
/* 129 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> add(AppenderComponentBuilder builder) {
/* 134 */     return add(this.appenders, (ComponentBuilder<?>)builder);
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> add(CustomLevelComponentBuilder builder) {
/* 139 */     return add(this.customLevels, (ComponentBuilder<?>)builder);
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> add(FilterComponentBuilder builder) {
/* 144 */     return add(this.filters, (ComponentBuilder<?>)builder);
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> add(ScriptComponentBuilder builder) {
/* 149 */     return add(this.scripts, (ComponentBuilder<?>)builder);
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> add(ScriptFileComponentBuilder builder) {
/* 154 */     return add(this.scripts, (ComponentBuilder<?>)builder);
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> add(LoggerComponentBuilder builder) {
/* 159 */     return add(this.loggers, (ComponentBuilder<?>)builder);
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> add(RootLoggerComponentBuilder builder) {
/* 164 */     for (Component c : this.loggers.getComponents()) {
/* 165 */       if (c.getPluginType().equals("root")) {
/* 166 */         throw new ConfigurationException("Root Logger was previously defined");
/*     */       }
/*     */     } 
/* 169 */     return add(this.loggers, (ComponentBuilder<?>)builder);
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> addProperty(String key, String value) {
/* 174 */     this.properties.addComponent((Component)newComponent(key, "Property", value).build());
/* 175 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public T build() {
/* 180 */     return build(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public T build(boolean initialize) {
/*     */     BuiltConfiguration builtConfiguration;
/*     */     try {
/* 187 */       if (this.source == null) {
/* 188 */         this.source = ConfigurationSource.NULL_SOURCE;
/*     */       }
/* 190 */       Constructor<T> constructor = this.clazz.getConstructor(new Class[] { LoggerContext.class, ConfigurationSource.class, Component.class });
/* 191 */       builtConfiguration = (BuiltConfiguration)constructor.newInstance(new Object[] { this.loggerContext, this.source, this.root });
/* 192 */       builtConfiguration.getRootNode().getAttributes().putAll(this.root.getAttributes());
/* 193 */       if (this.name != null) {
/* 194 */         builtConfiguration.setName(this.name);
/*     */       }
/* 196 */       if (this.level != null) {
/* 197 */         builtConfiguration.getStatusConfiguration().withStatus(this.level);
/*     */       }
/* 199 */       if (this.verbosity != null) {
/* 200 */         builtConfiguration.getStatusConfiguration().withVerbosity(this.verbosity);
/*     */       }
/* 202 */       if (this.destination != null) {
/* 203 */         builtConfiguration.getStatusConfiguration().withDestination(this.destination);
/*     */       }
/* 205 */       if (this.packages != null) {
/* 206 */         builtConfiguration.setPluginPackages(this.packages);
/*     */       }
/* 208 */       if (this.shutdownFlag != null) {
/* 209 */         builtConfiguration.setShutdownHook(this.shutdownFlag);
/*     */       }
/* 211 */       if (this.shutdownTimeoutMillis > 0L) {
/* 212 */         builtConfiguration.setShutdownTimeoutMillis(this.shutdownTimeoutMillis);
/*     */       }
/* 214 */       if (this.advertiser != null) {
/* 215 */         builtConfiguration.createAdvertiser(this.advertiser, this.source);
/*     */       }
/* 217 */       builtConfiguration.setMonitorInterval(this.monitorInterval);
/* 218 */     } catch (Exception ex) {
/* 219 */       throw new IllegalArgumentException("Invalid Configuration class specified", ex);
/*     */     } 
/* 221 */     builtConfiguration.getStatusConfiguration().initialize();
/* 222 */     if (initialize) {
/* 223 */       builtConfiguration.initialize();
/*     */     }
/* 225 */     return (T)builtConfiguration;
/*     */   }
/*     */ 
/*     */   
/*     */   private String formatXml(String xml) throws TransformerConfigurationException, TransformerException, TransformerFactoryConfigurationError {
/* 230 */     StringWriter writer = new StringWriter();
/* 231 */     formatXml(new StreamSource(new StringReader(xml)), new StreamResult(writer));
/* 232 */     return writer.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeXmlConfiguration(OutputStream output) throws IOException {
/*     */     try {
/* 238 */       XMLStreamWriter xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(output);
/* 239 */       writeXmlConfiguration(xmlWriter);
/* 240 */       xmlWriter.close();
/* 241 */     } catch (XMLStreamException e) {
/* 242 */       if (e.getNestedException() instanceof IOException) {
/* 243 */         throw (IOException)e.getNestedException();
/*     */       }
/* 245 */       Throwables.rethrow(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toXmlConfiguration() {
/* 251 */     StringWriter writer = new StringWriter();
/*     */     try {
/* 253 */       XMLStreamWriter xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(writer);
/* 254 */       writeXmlConfiguration(xmlWriter);
/* 255 */       xmlWriter.close();
/* 256 */       return formatXml(writer.toString());
/* 257 */     } catch (XMLStreamException|TransformerException e) {
/* 258 */       Throwables.rethrow(e);
/*     */       
/* 260 */       return writer.toString();
/*     */     } 
/*     */   }
/*     */   private void writeXmlConfiguration(XMLStreamWriter xmlWriter) throws XMLStreamException {
/* 264 */     xmlWriter.writeStartDocument();
/* 265 */     xmlWriter.writeStartElement("Configuration");
/* 266 */     if (this.name != null) {
/* 267 */       xmlWriter.writeAttribute("name", this.name);
/*     */     }
/* 269 */     if (this.level != null) {
/* 270 */       xmlWriter.writeAttribute("status", this.level.name());
/*     */     }
/* 272 */     if (this.verbosity != null) {
/* 273 */       xmlWriter.writeAttribute("verbose", this.verbosity);
/*     */     }
/* 275 */     if (this.destination != null) {
/* 276 */       xmlWriter.writeAttribute("dest", this.destination);
/*     */     }
/* 278 */     if (this.packages != null) {
/* 279 */       xmlWriter.writeAttribute("packages", this.packages);
/*     */     }
/* 281 */     if (this.shutdownFlag != null) {
/* 282 */       xmlWriter.writeAttribute("shutdownHook", this.shutdownFlag);
/*     */     }
/* 284 */     if (this.shutdownTimeoutMillis > 0L) {
/* 285 */       xmlWriter.writeAttribute("shutdownTimeout", String.valueOf(this.shutdownTimeoutMillis));
/*     */     }
/* 287 */     if (this.advertiser != null) {
/* 288 */       xmlWriter.writeAttribute("advertiser", this.advertiser);
/*     */     }
/* 290 */     if (this.monitorInterval > 0) {
/* 291 */       xmlWriter.writeAttribute("monitorInterval", String.valueOf(this.monitorInterval));
/*     */     }
/*     */     
/* 294 */     writeXmlSection(xmlWriter, this.properties);
/* 295 */     writeXmlSection(xmlWriter, this.scripts);
/* 296 */     writeXmlSection(xmlWriter, this.customLevels);
/* 297 */     if (this.filters.getComponents().size() == 1) {
/* 298 */       writeXmlComponent(xmlWriter, this.filters.getComponents().get(0));
/* 299 */     } else if (this.filters.getComponents().size() > 1) {
/* 300 */       writeXmlSection(xmlWriter, this.filters);
/*     */     } 
/* 302 */     writeXmlSection(xmlWriter, this.appenders);
/* 303 */     writeXmlSection(xmlWriter, this.loggers);
/*     */     
/* 305 */     xmlWriter.writeEndElement();
/* 306 */     xmlWriter.writeEndDocument();
/*     */   }
/*     */   
/*     */   private void writeXmlSection(XMLStreamWriter xmlWriter, Component component) throws XMLStreamException {
/* 310 */     if (!component.getAttributes().isEmpty() || !component.getComponents().isEmpty() || component.getValue() != null) {
/* 311 */       writeXmlComponent(xmlWriter, component);
/*     */     }
/*     */   }
/*     */   
/*     */   private void writeXmlComponent(XMLStreamWriter xmlWriter, Component component) throws XMLStreamException {
/* 316 */     if (!component.getComponents().isEmpty() || component.getValue() != null) {
/* 317 */       xmlWriter.writeStartElement(component.getPluginType());
/* 318 */       writeXmlAttributes(xmlWriter, component);
/* 319 */       for (Component subComponent : component.getComponents()) {
/* 320 */         writeXmlComponent(xmlWriter, subComponent);
/*     */       }
/* 322 */       if (component.getValue() != null) {
/* 323 */         xmlWriter.writeCharacters(component.getValue());
/*     */       }
/* 325 */       xmlWriter.writeEndElement();
/*     */     } else {
/* 327 */       xmlWriter.writeEmptyElement(component.getPluginType());
/* 328 */       writeXmlAttributes(xmlWriter, component);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeXmlAttributes(XMLStreamWriter xmlWriter, Component component) throws XMLStreamException {
/* 333 */     for (Map.Entry<String, String> attribute : (Iterable<Map.Entry<String, String>>)component.getAttributes().entrySet()) {
/* 334 */       xmlWriter.writeAttribute(attribute.getKey(), attribute.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ScriptComponentBuilder newScript(String name, String language, String text) {
/* 340 */     return new DefaultScriptComponentBuilder((DefaultConfigurationBuilder)this, name, language, text);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ScriptFileComponentBuilder newScriptFile(String path) {
/* 346 */     return new DefaultScriptFileComponentBuilder((DefaultConfigurationBuilder)this, path, path);
/*     */   }
/*     */ 
/*     */   
/*     */   public ScriptFileComponentBuilder newScriptFile(String name, String path) {
/* 351 */     return new DefaultScriptFileComponentBuilder((DefaultConfigurationBuilder)this, name, path);
/*     */   }
/*     */ 
/*     */   
/*     */   public AppenderComponentBuilder newAppender(String name, String type) {
/* 356 */     return new DefaultAppenderComponentBuilder((DefaultConfigurationBuilder)this, name, type);
/*     */   }
/*     */ 
/*     */   
/*     */   public AppenderRefComponentBuilder newAppenderRef(String ref) {
/* 361 */     return new DefaultAppenderRefComponentBuilder((DefaultConfigurationBuilder)this, ref);
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggerComponentBuilder newAsyncLogger(String name) {
/* 366 */     return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder)this, name, null, "AsyncLogger");
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggerComponentBuilder newAsyncLogger(String name, boolean includeLocation) {
/* 371 */     return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder)this, name, null, "AsyncLogger", includeLocation);
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggerComponentBuilder newAsyncLogger(String name, Level level) {
/* 376 */     return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder)this, name, level.toString(), "AsyncLogger");
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggerComponentBuilder newAsyncLogger(String name, Level level, boolean includeLocation) {
/* 381 */     return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder)this, name, level.toString(), "AsyncLogger", includeLocation);
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggerComponentBuilder newAsyncLogger(String name, String level) {
/* 386 */     return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder)this, name, level, "AsyncLogger");
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggerComponentBuilder newAsyncLogger(String name, String level, boolean includeLocation) {
/* 391 */     return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder)this, name, level, "AsyncLogger", includeLocation);
/*     */   }
/*     */ 
/*     */   
/*     */   public RootLoggerComponentBuilder newAsyncRootLogger() {
/* 396 */     return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder)this, "AsyncRoot");
/*     */   }
/*     */ 
/*     */   
/*     */   public RootLoggerComponentBuilder newAsyncRootLogger(boolean includeLocation) {
/* 401 */     return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder)this, null, "AsyncRoot", includeLocation);
/*     */   }
/*     */ 
/*     */   
/*     */   public RootLoggerComponentBuilder newAsyncRootLogger(Level level) {
/* 406 */     return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder)this, level.toString(), "AsyncRoot");
/*     */   }
/*     */ 
/*     */   
/*     */   public RootLoggerComponentBuilder newAsyncRootLogger(Level level, boolean includeLocation) {
/* 411 */     return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder)this, level.toString(), "AsyncRoot", includeLocation);
/*     */   }
/*     */ 
/*     */   
/*     */   public RootLoggerComponentBuilder newAsyncRootLogger(String level) {
/* 416 */     return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder)this, level, "AsyncRoot");
/*     */   }
/*     */ 
/*     */   
/*     */   public RootLoggerComponentBuilder newAsyncRootLogger(String level, boolean includeLocation) {
/* 421 */     return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder)this, level, "AsyncRoot", includeLocation);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <B extends ComponentBuilder<B>> ComponentBuilder<B> newComponent(String type) {
/* 427 */     return new DefaultComponentBuilder<>(this, type);
/*     */   }
/*     */ 
/*     */   
/*     */   public <B extends ComponentBuilder<B>> ComponentBuilder<B> newComponent(String name, String type) {
/* 432 */     return new DefaultComponentBuilder<>(this, name, type);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <B extends ComponentBuilder<B>> ComponentBuilder<B> newComponent(String name, String type, String value) {
/* 438 */     return new DefaultComponentBuilder<>(this, name, type, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public PropertyComponentBuilder newProperty(String name, String value) {
/* 443 */     return new DefaultPropertyComponentBuilder((DefaultConfigurationBuilder)this, name, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public KeyValuePairComponentBuilder newKeyValuePair(String key, String value) {
/* 448 */     return new DefaultKeyValuePairComponentBuilder((DefaultConfigurationBuilder)this, key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public CustomLevelComponentBuilder newCustomLevel(String name, int level) {
/* 453 */     return new DefaultCustomLevelComponentBuilder((DefaultConfigurationBuilder)this, name, level);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FilterComponentBuilder newFilter(String type, Filter.Result onMatch, Filter.Result onMismatch) {
/* 459 */     return new DefaultFilterComponentBuilder((DefaultConfigurationBuilder)this, type, onMatch.name(), onMismatch.name());
/*     */   }
/*     */ 
/*     */   
/*     */   public FilterComponentBuilder newFilter(String type, String onMatch, String onMismatch) {
/* 464 */     return new DefaultFilterComponentBuilder((DefaultConfigurationBuilder)this, type, onMatch, onMismatch);
/*     */   }
/*     */ 
/*     */   
/*     */   public LayoutComponentBuilder newLayout(String type) {
/* 469 */     return new DefaultLayoutComponentBuilder((DefaultConfigurationBuilder)this, type);
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggerComponentBuilder newLogger(String name) {
/* 474 */     return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder)this, name, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggerComponentBuilder newLogger(String name, boolean includeLocation) {
/* 479 */     return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder)this, name, null, includeLocation);
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggerComponentBuilder newLogger(String name, Level level) {
/* 484 */     return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder)this, name, level.toString());
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggerComponentBuilder newLogger(String name, Level level, boolean includeLocation) {
/* 489 */     return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder)this, name, level.toString(), includeLocation);
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggerComponentBuilder newLogger(String name, String level) {
/* 494 */     return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder)this, name, level);
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggerComponentBuilder newLogger(String name, String level, boolean includeLocation) {
/* 499 */     return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder)this, name, level, includeLocation);
/*     */   }
/*     */ 
/*     */   
/*     */   public RootLoggerComponentBuilder newRootLogger() {
/* 504 */     return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder)this, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public RootLoggerComponentBuilder newRootLogger(boolean includeLocation) {
/* 509 */     return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder)this, null, includeLocation);
/*     */   }
/*     */ 
/*     */   
/*     */   public RootLoggerComponentBuilder newRootLogger(Level level) {
/* 514 */     return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder)this, level.toString());
/*     */   }
/*     */ 
/*     */   
/*     */   public RootLoggerComponentBuilder newRootLogger(Level level, boolean includeLocation) {
/* 519 */     return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder)this, level.toString(), includeLocation);
/*     */   }
/*     */ 
/*     */   
/*     */   public RootLoggerComponentBuilder newRootLogger(String level) {
/* 524 */     return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder)this, level);
/*     */   }
/*     */ 
/*     */   
/*     */   public RootLoggerComponentBuilder newRootLogger(String level, boolean includeLocation) {
/* 529 */     return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder)this, level, includeLocation);
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> setAdvertiser(String advertiser) {
/* 534 */     this.advertiser = advertiser;
/* 535 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> setConfigurationName(String name) {
/* 546 */     this.name = name;
/* 547 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> setConfigurationSource(ConfigurationSource configurationSource) {
/* 558 */     this.source = configurationSource;
/* 559 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> setMonitorInterval(String intervalSeconds) {
/* 564 */     this.monitorInterval = Integer.parseInt(intervalSeconds);
/* 565 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> setPackages(String packages) {
/* 570 */     this.packages = packages;
/* 571 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> setShutdownHook(String flag) {
/* 576 */     this.shutdownFlag = flag;
/* 577 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> setShutdownTimeout(long timeout, TimeUnit timeUnit) {
/* 582 */     this.shutdownTimeoutMillis = timeUnit.toMillis(timeout);
/* 583 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> setStatusLevel(Level level) {
/* 588 */     this.level = level;
/* 589 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> setVerbosity(String verbosity) {
/* 594 */     this.verbosity = verbosity;
/* 595 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> setDestination(String destination) {
/* 600 */     this.destination = destination;
/* 601 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLoggerContext(LoggerContext loggerContext) {
/* 606 */     this.loggerContext = loggerContext;
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> addRootProperty(String key, String value) {
/* 611 */     this.root.getAttributes().put(key, value);
/* 612 */     return this;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\builder\impl\DefaultConfigurationBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */