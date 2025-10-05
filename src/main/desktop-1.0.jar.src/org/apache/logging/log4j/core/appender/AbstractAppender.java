/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.core.Appender;
/*     */ import org.apache.logging.log4j.core.ErrorHandler;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
/*     */ import org.apache.logging.log4j.core.filter.AbstractFilterable;
/*     */ import org.apache.logging.log4j.core.impl.LocationAware;
/*     */ import org.apache.logging.log4j.core.layout.PatternLayout;
/*     */ import org.apache.logging.log4j.core.util.Integers;
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
/*     */ public abstract class AbstractAppender
/*     */   extends AbstractFilterable
/*     */   implements Appender, LocationAware
/*     */ {
/*     */   private final String name;
/*     */   private final boolean ignoreExceptions;
/*     */   private final Layout<? extends Serializable> layout;
/*     */   
/*     */   public static abstract class Builder<B extends Builder<B>>
/*     */     extends AbstractFilterable.Builder<B>
/*     */   {
/*     */     @PluginBuilderAttribute
/*     */     private boolean ignoreExceptions = true;
/*     */     @PluginElement("Layout")
/*     */     private Layout<? extends Serializable> layout;
/*     */     @PluginBuilderAttribute
/*     */     @Required(message = "No appender name provided")
/*     */     private String name;
/*     */     @PluginConfiguration
/*     */     private Configuration configuration;
/*     */     
/*     */     public Configuration getConfiguration() {
/*  66 */       return this.configuration;
/*     */     }
/*     */     
/*     */     public Layout<? extends Serializable> getLayout() {
/*  70 */       return this.layout;
/*     */     }
/*     */     
/*     */     public String getName() {
/*  74 */       return this.name;
/*     */     }
/*     */     
/*     */     public Layout<? extends Serializable> getOrCreateLayout() {
/*  78 */       if (this.layout == null) {
/*  79 */         return (Layout<? extends Serializable>)PatternLayout.createDefaultLayout();
/*     */       }
/*  81 */       return this.layout;
/*     */     }
/*     */     
/*     */     public Layout<? extends Serializable> getOrCreateLayout(Charset charset) {
/*  85 */       if (this.layout == null) {
/*  86 */         return (Layout<? extends Serializable>)PatternLayout.newBuilder().withCharset(charset).build();
/*     */       }
/*  88 */       return this.layout;
/*     */     }
/*     */     
/*     */     public boolean isIgnoreExceptions() {
/*  92 */       return this.ignoreExceptions;
/*     */     }
/*     */     
/*     */     public B setConfiguration(Configuration configuration) {
/*  96 */       this.configuration = configuration;
/*  97 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setIgnoreExceptions(boolean ignoreExceptions) {
/* 101 */       this.ignoreExceptions = ignoreExceptions;
/* 102 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setLayout(Layout<? extends Serializable> layout) {
/* 106 */       this.layout = layout;
/* 107 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setName(String name) {
/* 111 */       this.name = name;
/* 112 */       return (B)asBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public B withConfiguration(Configuration configuration) {
/* 120 */       this.configuration = configuration;
/* 121 */       return (B)asBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public B withIgnoreExceptions(boolean ignoreExceptions) {
/* 129 */       return setIgnoreExceptions(ignoreExceptions);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public B withLayout(Layout<? extends Serializable> layout) {
/* 137 */       return setLayout(layout);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public B withName(String name) {
/* 145 */       return setName(name);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static int parseInt(String s, int defaultValue) {
/*     */     try {
/* 152 */       return Integers.parseInt(s, defaultValue);
/* 153 */     } catch (NumberFormatException e) {
/* 154 */       LOGGER.error("Could not parse \"{}\" as an integer,  using default value {}: {}", s, Integer.valueOf(defaultValue), e);
/* 155 */       return defaultValue;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 162 */   private ErrorHandler handler = new DefaultErrorHandler(this);
/*     */ 
/*     */   
/*     */   public boolean requiresLocation() {
/* 166 */     return (this.layout instanceof LocationAware && ((LocationAware)this.layout).requiresLocation());
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
/*     */   @Deprecated
/*     */   protected AbstractAppender(String name, Filter filter, Layout<? extends Serializable> layout) {
/* 179 */     this(name, filter, layout, true, Property.EMPTY_ARRAY);
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
/*     */   @Deprecated
/*     */   protected AbstractAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions) {
/* 195 */     this(name, filter, layout, ignoreExceptions, Property.EMPTY_ARRAY);
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
/*     */   protected AbstractAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, Property[] properties) {
/* 210 */     super(filter, properties);
/* 211 */     this.name = Objects.<String>requireNonNull(name, "name");
/* 212 */     this.layout = layout;
/* 213 */     this.ignoreExceptions = ignoreExceptions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String msg) {
/* 222 */     this.handler.error(msg);
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
/*     */   public void error(String msg, LogEvent event, Throwable t) {
/* 234 */     this.handler.error(msg, event, t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String msg, Throwable t) {
/* 244 */     this.handler.error(msg, t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ErrorHandler getHandler() {
/* 254 */     return this.handler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Layout<? extends Serializable> getLayout() {
/* 264 */     return this.layout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 274 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean ignoreExceptions() {
/* 285 */     return this.ignoreExceptions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHandler(ErrorHandler handler) {
/* 295 */     if (handler == null) {
/* 296 */       LOGGER.error("The handler cannot be set to null");
/*     */       return;
/*     */     } 
/* 299 */     if (isStarted()) {
/* 300 */       LOGGER.error("The handler cannot be changed once the appender is started");
/*     */       return;
/*     */     } 
/* 303 */     this.handler = handler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Serializable toSerializable(LogEvent event) {
/* 314 */     return (this.layout != null) ? this.layout.toSerializable(event) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 319 */     return this.name;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\AbstractAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */