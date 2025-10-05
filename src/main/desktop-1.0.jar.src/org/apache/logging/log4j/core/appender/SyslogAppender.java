/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.layout.LoggerFields;
/*     */ import org.apache.logging.log4j.core.layout.Rfc5424Layout;
/*     */ import org.apache.logging.log4j.core.layout.SyslogLayout;
/*     */ import org.apache.logging.log4j.core.net.AbstractSocketManager;
/*     */ import org.apache.logging.log4j.core.net.Advertiser;
/*     */ import org.apache.logging.log4j.core.net.Facility;
/*     */ import org.apache.logging.log4j.core.net.Protocol;
/*     */ import org.apache.logging.log4j.core.net.SocketOptions;
/*     */ import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
/*     */ import org.apache.logging.log4j.core.util.Constants;
/*     */ import org.apache.logging.log4j.util.EnglishEnums;
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
/*     */ @Plugin(name = "Syslog", category = "Core", elementType = "appender", printObject = true)
/*     */ public class SyslogAppender
/*     */   extends SocketAppender
/*     */ {
/*     */   protected static final String RFC5424 = "RFC5424";
/*     */   
/*     */   public static class Builder<B extends Builder<B>>
/*     */     extends SocketAppender.AbstractBuilder<B>
/*     */     implements org.apache.logging.log4j.core.util.Builder<SocketAppender>
/*     */   {
/*     */     @PluginBuilderAttribute("facility")
/*  53 */     private Facility facility = Facility.LOCAL0;
/*     */     
/*     */     @PluginBuilderAttribute("id")
/*     */     private String id;
/*     */     
/*     */     @PluginBuilderAttribute("enterpriseNumber")
/*  59 */     private int enterpriseNumber = 18060;
/*     */     
/*     */     @PluginBuilderAttribute("includeMdc")
/*     */     private boolean includeMdc = true;
/*     */     
/*     */     @PluginBuilderAttribute("mdcId")
/*     */     private String mdcId;
/*     */     
/*     */     @PluginBuilderAttribute("mdcPrefix")
/*     */     private String mdcPrefix;
/*     */     
/*     */     @PluginBuilderAttribute("eventPrefix")
/*     */     private String eventPrefix;
/*     */     
/*     */     @PluginBuilderAttribute("newLine")
/*     */     private boolean newLine;
/*     */     
/*     */     @PluginBuilderAttribute("newLineEscape")
/*     */     private String escapeNL;
/*     */     
/*     */     @PluginBuilderAttribute("appName")
/*     */     private String appName;
/*     */     
/*     */     @PluginBuilderAttribute("messageId")
/*     */     private String msgId;
/*     */     
/*     */     @PluginBuilderAttribute("mdcExcludes")
/*     */     private String excludes;
/*     */     
/*     */     @PluginBuilderAttribute("mdcIncludes")
/*     */     private String includes;
/*     */     
/*     */     @PluginBuilderAttribute("mdcRequired")
/*     */     private String required;
/*     */     
/*     */     @PluginBuilderAttribute("format")
/*     */     private String format;
/*     */     
/*     */     @PluginBuilderAttribute("charset")
/*  98 */     private Charset charsetName = StandardCharsets.UTF_8;
/*     */ 
/*     */     
/*     */     @PluginBuilderAttribute("exceptionPattern")
/*     */     private String exceptionPattern;
/*     */ 
/*     */     
/*     */     @PluginElement("LoggerFields")
/*     */     private LoggerFields[] loggerFields;
/*     */ 
/*     */     
/*     */     public SyslogAppender build() {
/* 110 */       Protocol protocol = getProtocol();
/* 111 */       SslConfiguration sslConfiguration = getSslConfiguration();
/* 112 */       boolean useTlsMessageFormat = (sslConfiguration != null || protocol == Protocol.SSL);
/* 113 */       Configuration configuration = getConfiguration();
/* 114 */       Layout<? extends Serializable> layout = getLayout();
/* 115 */       if (layout == null)
/*     */       {
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
/* 127 */         layout = "RFC5424".equalsIgnoreCase(this.format) ? (Layout<? extends Serializable>)Rfc5424Layout.createLayout(this.facility, this.id, this.enterpriseNumber, this.includeMdc, this.mdcId, this.mdcPrefix, this.eventPrefix, this.newLine, this.escapeNL, this.appName, this.msgId, this.excludes, this.includes, this.required, this.exceptionPattern, useTlsMessageFormat, this.loggerFields, configuration) : (Layout<? extends Serializable>)((SyslogLayout.Builder)SyslogLayout.newBuilder().setFacility(this.facility).setIncludeNewLine(this.newLine).setEscapeNL(this.escapeNL).setCharset(this.charsetName)).build();
/*     */       }
/*     */       
/* 130 */       String name = getName();
/* 131 */       if (name == null) {
/* 132 */         SyslogAppender.LOGGER.error("No name provided for SyslogAppender");
/* 133 */         return null;
/*     */       } 
/* 135 */       AbstractSocketManager manager = SocketAppender.createSocketManager(name, protocol, getHost(), getPort(), getConnectTimeoutMillis(), sslConfiguration, 
/* 136 */           getReconnectDelayMillis(), getImmediateFail(), layout, Constants.ENCODER_BYTE_BUFFER_SIZE, (SocketOptions)null);
/*     */       
/* 138 */       return new SyslogAppender(name, layout, getFilter(), isIgnoreExceptions(), isImmediateFlush(), manager, 
/* 139 */           getAdvertise() ? configuration.getAdvertiser() : null, null);
/*     */     }
/*     */     
/*     */     public Facility getFacility() {
/* 143 */       return this.facility;
/*     */     }
/*     */     
/*     */     public String getId() {
/* 147 */       return this.id;
/*     */     }
/*     */     
/*     */     public int getEnterpriseNumber() {
/* 151 */       return this.enterpriseNumber;
/*     */     }
/*     */     
/*     */     public boolean isIncludeMdc() {
/* 155 */       return this.includeMdc;
/*     */     }
/*     */     
/*     */     public String getMdcId() {
/* 159 */       return this.mdcId;
/*     */     }
/*     */     
/*     */     public String getMdcPrefix() {
/* 163 */       return this.mdcPrefix;
/*     */     }
/*     */     
/*     */     public String getEventPrefix() {
/* 167 */       return this.eventPrefix;
/*     */     }
/*     */     
/*     */     public boolean isNewLine() {
/* 171 */       return this.newLine;
/*     */     }
/*     */     
/*     */     public String getEscapeNL() {
/* 175 */       return this.escapeNL;
/*     */     }
/*     */     
/*     */     public String getAppName() {
/* 179 */       return this.appName;
/*     */     }
/*     */     
/*     */     public String getMsgId() {
/* 183 */       return this.msgId;
/*     */     }
/*     */     
/*     */     public String getExcludes() {
/* 187 */       return this.excludes;
/*     */     }
/*     */     
/*     */     public String getIncludes() {
/* 191 */       return this.includes;
/*     */     }
/*     */     
/*     */     public String getRequired() {
/* 195 */       return this.required;
/*     */     }
/*     */     
/*     */     public String getFormat() {
/* 199 */       return this.format;
/*     */     }
/*     */     
/*     */     public Charset getCharsetName() {
/* 203 */       return this.charsetName;
/*     */     }
/*     */     
/*     */     public String getExceptionPattern() {
/* 207 */       return this.exceptionPattern;
/*     */     }
/*     */     
/*     */     public LoggerFields[] getLoggerFields() {
/* 211 */       return this.loggerFields;
/*     */     }
/*     */     
/*     */     public B setFacility(Facility facility) {
/* 215 */       this.facility = facility;
/* 216 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setId(String id) {
/* 220 */       this.id = id;
/* 221 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setEnterpriseNumber(int enterpriseNumber) {
/* 225 */       this.enterpriseNumber = enterpriseNumber;
/* 226 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setIncludeMdc(boolean includeMdc) {
/* 230 */       this.includeMdc = includeMdc;
/* 231 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setMdcId(String mdcId) {
/* 235 */       this.mdcId = mdcId;
/* 236 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setMdcPrefix(String mdcPrefix) {
/* 240 */       this.mdcPrefix = mdcPrefix;
/* 241 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setEventPrefix(String eventPrefix) {
/* 245 */       this.eventPrefix = eventPrefix;
/* 246 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setNewLine(boolean newLine) {
/* 250 */       this.newLine = newLine;
/* 251 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setEscapeNL(String escapeNL) {
/* 255 */       this.escapeNL = escapeNL;
/* 256 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setAppName(String appName) {
/* 260 */       this.appName = appName;
/* 261 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setMsgId(String msgId) {
/* 265 */       this.msgId = msgId;
/* 266 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setExcludes(String excludes) {
/* 270 */       this.excludes = excludes;
/* 271 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setIncludes(String includes) {
/* 275 */       this.includes = includes;
/* 276 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setRequired(String required) {
/* 280 */       this.required = required;
/* 281 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setFormat(String format) {
/* 285 */       this.format = format;
/* 286 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setCharsetName(Charset charset) {
/* 290 */       this.charsetName = charset;
/* 291 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setExceptionPattern(String exceptionPattern) {
/* 295 */       this.exceptionPattern = exceptionPattern;
/* 296 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setLoggerFields(LoggerFields[] loggerFields) {
/* 300 */       this.loggerFields = loggerFields;
/* 301 */       return (B)asBuilder();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SyslogAppender(String name, Layout<? extends Serializable> layout, Filter filter, boolean ignoreExceptions, boolean immediateFlush, AbstractSocketManager manager, Advertiser advertiser, Property[] properties) {
/* 310 */     super(name, layout, filter, manager, ignoreExceptions, immediateFlush, advertiser, properties);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected SyslogAppender(String name, Layout<? extends Serializable> layout, Filter filter, boolean ignoreExceptions, boolean immediateFlush, AbstractSocketManager manager, Advertiser advertiser) {
/* 321 */     super(name, layout, filter, manager, ignoreExceptions, immediateFlush, advertiser, Property.EMPTY_ARRAY);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static <B extends Builder<B>> SyslogAppender createAppender(String host, int port, String protocolStr, SslConfiguration sslConfiguration, int connectTimeoutMillis, int reconnectDelayMillis, boolean immediateFail, String name, boolean immediateFlush, boolean ignoreExceptions, Facility facility, String id, int enterpriseNumber, boolean includeMdc, String mdcId, String mdcPrefix, String eventPrefix, boolean newLine, String escapeNL, String appName, String msgId, String excludes, String includes, String required, String format, Filter filter, Configuration configuration, Charset charset, String exceptionPattern, LoggerFields[] loggerFields, boolean advertise) {
/* 400 */     return ((Builder<B>)((Builder<Builder<B>>)((Builder<Builder<Builder<B>>>)((Builder)((Builder<Builder>)((Builder<Builder<Builder>>)((Builder<Builder<Builder<Builder>>>)((Builder<Builder<Builder<Builder<Builder>>>>)((Builder<Builder<Builder<Builder<Builder<Builder>>>>>)((Builder<Builder<Builder<Builder<Builder<Builder<Builder>>>>>>)((Builder<Builder<Builder<Builder<Builder<Builder<Builder<Builder>>>>>>>)((Builder<Builder<Builder<Builder<Builder<Builder<Builder<Builder<Builder>>>>>>>>)((Builder<Builder<Builder<Builder<Builder<Builder<Builder<Builder<Builder<Builder>>>>>>>>>)newSyslogAppenderBuilder()
/* 401 */       .withHost(host))
/* 402 */       .withPort(port))
/* 403 */       .withProtocol((Protocol)EnglishEnums.valueOf(Protocol.class, protocolStr)))
/* 404 */       .withSslConfiguration(sslConfiguration))
/* 405 */       .withConnectTimeoutMillis(connectTimeoutMillis))
/* 406 */       .withReconnectDelayMillis(reconnectDelayMillis))
/* 407 */       .withImmediateFail(immediateFail)).setName(appName))
/* 408 */       .withImmediateFlush(immediateFlush)).setIgnoreExceptions(ignoreExceptions)).setFilter(filter))
/* 409 */       .setConfiguration(configuration))
/* 410 */       .withAdvertise(advertise))
/* 411 */       .setFacility(facility)
/* 412 */       .setId(id)
/* 413 */       .setEnterpriseNumber(enterpriseNumber)
/* 414 */       .setIncludeMdc(includeMdc)
/* 415 */       .setMdcId(mdcId)
/* 416 */       .setMdcPrefix(mdcPrefix)
/* 417 */       .setEventPrefix(eventPrefix)
/* 418 */       .setNewLine(newLine)
/* 419 */       .setAppName(appName)
/* 420 */       .setMsgId(msgId)
/* 421 */       .setExcludes(excludes)
/* 422 */       .setIncludeMdc(includeMdc)
/* 423 */       .setRequired(required)
/* 424 */       .setFormat(format)
/* 425 */       .setCharsetName(charset)
/* 426 */       .setExceptionPattern(exceptionPattern)
/* 427 */       .setLoggerFields(loggerFields)
/* 428 */       .build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static <B extends Builder<B>> B newSyslogAppenderBuilder() {
/* 435 */     return (B)(new Builder<>()).asBuilder();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\SyslogAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */