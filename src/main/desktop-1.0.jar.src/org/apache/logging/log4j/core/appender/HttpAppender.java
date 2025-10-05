/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.net.URL;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
/*     */ import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
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
/*     */ @Plugin(name = "Http", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class HttpAppender
/*     */   extends AbstractAppender
/*     */ {
/*     */   private final HttpManager manager;
/*     */   
/*     */   public static class Builder<B extends Builder<B>>
/*     */     extends AbstractAppender.Builder<B>
/*     */     implements org.apache.logging.log4j.core.util.Builder<HttpAppender>
/*     */   {
/*     */     @PluginBuilderAttribute
/*     */     @Required(message = "No URL provided for HttpAppender")
/*     */     private URL url;
/*     */     @PluginBuilderAttribute
/*  55 */     private String method = "POST";
/*     */     
/*     */     @PluginBuilderAttribute
/*  58 */     private int connectTimeoutMillis = 0;
/*     */     
/*     */     @PluginBuilderAttribute
/*  61 */     private int readTimeoutMillis = 0;
/*     */ 
/*     */     
/*     */     @PluginElement("Headers")
/*     */     private Property[] headers;
/*     */ 
/*     */     
/*     */     @PluginElement("SslConfiguration")
/*     */     private SslConfiguration sslConfiguration;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private boolean verifyHostname = true;
/*     */ 
/*     */     
/*     */     public HttpAppender build() {
/*  76 */       HttpManager httpManager = new HttpURLConnectionManager(getConfiguration(), getConfiguration().getLoggerContext(), getName(), this.url, this.method, this.connectTimeoutMillis, this.readTimeoutMillis, this.headers, this.sslConfiguration, this.verifyHostname);
/*     */       
/*  78 */       return new HttpAppender(getName(), getLayout(), getFilter(), isIgnoreExceptions(), httpManager, 
/*  79 */           getPropertyArray());
/*     */     }
/*     */     
/*     */     public URL getUrl() {
/*  83 */       return this.url;
/*     */     }
/*     */     
/*     */     public String getMethod() {
/*  87 */       return this.method;
/*     */     }
/*     */     
/*     */     public int getConnectTimeoutMillis() {
/*  91 */       return this.connectTimeoutMillis;
/*     */     }
/*     */     
/*     */     public int getReadTimeoutMillis() {
/*  95 */       return this.readTimeoutMillis;
/*     */     }
/*     */     
/*     */     public Property[] getHeaders() {
/*  99 */       return this.headers;
/*     */     }
/*     */     
/*     */     public SslConfiguration getSslConfiguration() {
/* 103 */       return this.sslConfiguration;
/*     */     }
/*     */     
/*     */     public boolean isVerifyHostname() {
/* 107 */       return this.verifyHostname;
/*     */     }
/*     */     
/*     */     public B setUrl(URL url) {
/* 111 */       this.url = url;
/* 112 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setMethod(String method) {
/* 116 */       this.method = method;
/* 117 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setConnectTimeoutMillis(int connectTimeoutMillis) {
/* 121 */       this.connectTimeoutMillis = connectTimeoutMillis;
/* 122 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setReadTimeoutMillis(int readTimeoutMillis) {
/* 126 */       this.readTimeoutMillis = readTimeoutMillis;
/* 127 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setHeaders(Property[] headers) {
/* 131 */       this.headers = headers;
/* 132 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setSslConfiguration(SslConfiguration sslConfiguration) {
/* 136 */       this.sslConfiguration = sslConfiguration;
/* 137 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setVerifyHostname(boolean verifyHostname) {
/* 141 */       this.verifyHostname = verifyHostname;
/* 142 */       return (B)asBuilder();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static <B extends Builder<B>> B newBuilder() {
/* 151 */     return (B)(new Builder<>()).asBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private HttpAppender(String name, Layout<? extends Serializable> layout, Filter filter, boolean ignoreExceptions, HttpManager manager, Property[] properties) {
/* 158 */     super(name, filter, layout, ignoreExceptions, properties);
/* 159 */     Objects.requireNonNull(layout, "layout");
/* 160 */     this.manager = Objects.<HttpManager>requireNonNull(manager, "manager");
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 165 */     super.start();
/* 166 */     this.manager.startup();
/*     */   }
/*     */ 
/*     */   
/*     */   public void append(LogEvent event) {
/*     */     try {
/* 172 */       this.manager.send(getLayout(), event);
/* 173 */     } catch (Exception e) {
/* 174 */       error("Unable to send HTTP in appender [" + getName() + "]", event, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 180 */     setStopping();
/* 181 */     boolean stopped = stop(timeout, timeUnit, false);
/* 182 */     stopped &= this.manager.stop(timeout, timeUnit);
/* 183 */     setStopped();
/* 184 */     return stopped;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 189 */     return "HttpAppender{name=" + 
/* 190 */       getName() + ", state=" + 
/* 191 */       getState() + '}';
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\HttpAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */