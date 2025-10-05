/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.DefaultConfiguration;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.ValidPort;
/*     */ import org.apache.logging.log4j.core.filter.AbstractFilterable;
/*     */ import org.apache.logging.log4j.core.filter.ThresholdFilter;
/*     */ import org.apache.logging.log4j.core.layout.HtmlLayout;
/*     */ import org.apache.logging.log4j.core.net.SmtpManager;
/*     */ import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
/*     */ import org.apache.logging.log4j.core.util.Booleans;
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
/*     */ @Plugin(name = "SMTP", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class SmtpAppender
/*     */   extends AbstractAppender
/*     */ {
/*     */   private static final int DEFAULT_BUFFER_SIZE = 512;
/*     */   private final SmtpManager manager;
/*     */   
/*     */   private SmtpAppender(String name, Filter filter, Layout<? extends Serializable> layout, SmtpManager manager, boolean ignoreExceptions, Property[] properties) {
/*  72 */     super(name, filter, layout, ignoreExceptions, properties);
/*  73 */     this.manager = manager;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */     extends AbstractAppender.Builder<Builder>
/*     */     implements org.apache.logging.log4j.core.util.Builder<SmtpAppender>
/*     */   {
/*     */     @PluginBuilderAttribute
/*     */     private String to;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private String cc;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private String bcc;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private String from;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private String replyTo;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private String subject;
/*     */     @PluginBuilderAttribute
/*  99 */     private String smtpProtocol = "smtp";
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private String smtpHost;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     @ValidPort
/*     */     private int smtpPort;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private String smtpUsername;
/*     */     
/*     */     @PluginBuilderAttribute(sensitive = true)
/*     */     private String smtpPassword;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private boolean smtpDebug;
/*     */     
/*     */     @PluginBuilderAttribute
/* 118 */     private int bufferSize = 512;
/*     */ 
/*     */ 
/*     */     
/*     */     @PluginElement("SSL")
/*     */     private SslConfiguration sslConfiguration;
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setTo(String to) {
/* 128 */       this.to = to;
/* 129 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setCc(String cc) {
/* 136 */       this.cc = cc;
/* 137 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setBcc(String bcc) {
/* 144 */       this.bcc = bcc;
/* 145 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setFrom(String from) {
/* 152 */       this.from = from;
/* 153 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setReplyTo(String replyTo) {
/* 160 */       this.replyTo = replyTo;
/* 161 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setSubject(String subject) {
/* 169 */       this.subject = subject;
/* 170 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setSmtpProtocol(String smtpProtocol) {
/* 177 */       this.smtpProtocol = smtpProtocol;
/* 178 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setSmtpHost(String smtpHost) {
/* 185 */       this.smtpHost = smtpHost;
/* 186 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setSmtpPort(int smtpPort) {
/* 193 */       this.smtpPort = smtpPort;
/* 194 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setSmtpUsername(String smtpUsername) {
/* 201 */       this.smtpUsername = smtpUsername;
/* 202 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setSmtpPassword(String smtpPassword) {
/* 209 */       this.smtpPassword = smtpPassword;
/* 210 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setSmtpDebug(boolean smtpDebug) {
/* 217 */       this.smtpDebug = smtpDebug;
/* 218 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setBufferSize(int bufferSize) {
/* 225 */       this.bufferSize = bufferSize;
/* 226 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setSslConfiguration(SslConfiguration sslConfiguration) {
/* 233 */       this.sslConfiguration = sslConfiguration;
/* 234 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setLayout(Layout<? extends Serializable> layout) {
/* 243 */       return super.setLayout(layout);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setFilter(Filter filter) {
/* 252 */       return (Builder)super.setFilter(filter);
/*     */     }
/*     */ 
/*     */     
/*     */     public SmtpAppender build() {
/* 257 */       if (getLayout() == null) {
/* 258 */         setLayout((Layout<? extends Serializable>)HtmlLayout.createDefaultLayout());
/*     */       }
/* 260 */       if (getFilter() == null) {
/* 261 */         setFilter((Filter)ThresholdFilter.createFilter(null, null, null));
/*     */       }
/* 263 */       SmtpManager smtpManager = SmtpManager.getSmtpManager(getConfiguration(), this.to, this.cc, this.bcc, this.from, this.replyTo, this.subject, this.smtpProtocol, this.smtpHost, this.smtpPort, this.smtpUsername, this.smtpPassword, this.smtpDebug, 
/*     */           
/* 265 */           getFilter().toString(), this.bufferSize, this.sslConfiguration);
/* 266 */       return new SmtpAppender(getName(), getFilter(), getLayout(), smtpManager, isIgnoreExceptions(), getPropertyArray());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/* 275 */     return new Builder();
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
/*     */   public static SmtpAppender createAppender(@PluginConfiguration Configuration config, @PluginAttribute("name") @Required String name, @PluginAttribute("to") String to, @PluginAttribute("cc") String cc, @PluginAttribute("bcc") String bcc, @PluginAttribute("from") String from, @PluginAttribute("replyTo") String replyTo, @PluginAttribute("subject") String subject, @PluginAttribute("smtpProtocol") String smtpProtocol, @PluginAttribute("smtpHost") String smtpHost, @PluginAttribute(value = "smtpPort", defaultString = "0") @ValidPort String smtpPortStr, @PluginAttribute("smtpUsername") String smtpUsername, @PluginAttribute(value = "smtpPassword", sensitive = true) String smtpPassword, @PluginAttribute("smtpDebug") String smtpDebug, @PluginAttribute("bufferSize") String bufferSizeStr, @PluginElement("Layout") Layout<? extends Serializable> layout, @PluginElement("Filter") Filter filter, @PluginAttribute("ignoreExceptions") String ignore) {
/*     */     HtmlLayout htmlLayout;
/*     */     ThresholdFilter thresholdFilter;
/* 303 */     if (name == null) {
/* 304 */       LOGGER.error("No name provided for SmtpAppender");
/* 305 */       return null;
/*     */     } 
/*     */     
/* 308 */     boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
/* 309 */     int smtpPort = AbstractAppender.parseInt(smtpPortStr, 0);
/* 310 */     boolean isSmtpDebug = Boolean.parseBoolean(smtpDebug);
/* 311 */     int bufferSize = (bufferSizeStr == null) ? 512 : Integer.parseInt(bufferSizeStr);
/*     */     
/* 313 */     if (layout == null) {
/* 314 */       htmlLayout = HtmlLayout.createDefaultLayout();
/*     */     }
/* 316 */     if (filter == null) {
/* 317 */       thresholdFilter = ThresholdFilter.createFilter(null, null, null);
/*     */     }
/* 319 */     Configuration configuration = (config != null) ? config : (Configuration)new DefaultConfiguration();
/*     */     
/* 321 */     SmtpManager manager = SmtpManager.getSmtpManager(configuration, to, cc, bcc, from, replyTo, subject, smtpProtocol, smtpHost, smtpPort, smtpUsername, smtpPassword, isSmtpDebug, thresholdFilter
/* 322 */         .toString(), bufferSize, null);
/* 323 */     if (manager == null) {
/* 324 */       return null;
/*     */     }
/*     */     
/* 327 */     return new SmtpAppender(name, (Filter)thresholdFilter, (Layout<? extends Serializable>)htmlLayout, manager, ignoreExceptions, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFiltered(LogEvent event) {
/* 337 */     boolean filtered = super.isFiltered(event);
/* 338 */     if (filtered) {
/* 339 */       this.manager.add(event);
/*     */     }
/* 341 */     return filtered;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(LogEvent event) {
/* 352 */     this.manager.sendEvents(getLayout(), event);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\SmtpAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */