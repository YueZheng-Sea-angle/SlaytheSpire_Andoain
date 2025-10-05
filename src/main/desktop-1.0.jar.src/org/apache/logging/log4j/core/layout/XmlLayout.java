/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerationException;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.util.KeyValuePair;
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
/*     */ @Plugin(name = "XmlLayout", category = "Core", elementType = "layout", printObject = true)
/*     */ public final class XmlLayout
/*     */   extends AbstractJacksonLayout
/*     */ {
/*     */   private static final String ROOT_TAG = "Events";
/*     */   
/*     */   public static class Builder<B extends Builder<B>>
/*     */     extends AbstractJacksonLayout.Builder<B>
/*     */     implements org.apache.logging.log4j.core.util.Builder<XmlLayout>
/*     */   {
/*     */     public Builder() {
/*  71 */       setCharset(StandardCharsets.UTF_8);
/*     */     }
/*     */ 
/*     */     
/*     */     public XmlLayout build() {
/*  76 */       return new XmlLayout(getConfiguration(), isLocationInfo(), isProperties(), isComplete(), 
/*  77 */           isCompact(), getEndOfLine(), getCharset(), isIncludeStacktrace(), isStacktraceAsString(), 
/*  78 */           isIncludeNullDelimiter(), isIncludeTimeMillis(), getAdditionalFields());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected XmlLayout(boolean locationInfo, boolean properties, boolean complete, boolean compact, Charset charset, boolean includeStacktrace) {
/*  88 */     this((Configuration)null, locationInfo, properties, complete, compact, (String)null, charset, includeStacktrace, false, false, false, (KeyValuePair[])null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private XmlLayout(Configuration config, boolean locationInfo, boolean properties, boolean complete, boolean compact, String endOfLine, Charset charset, boolean includeStacktrace, boolean stacktraceAsString, boolean includeNullDelimiter, boolean includeTimeMillis, KeyValuePair[] additionalFields) {
/*  97 */     super(config, (new JacksonFactory.XML(includeStacktrace, stacktraceAsString)).newWriter(locationInfo, properties, compact, includeTimeMillis), charset, compact, complete, false, endOfLine, (AbstractStringLayout.Serializer)null, (AbstractStringLayout.Serializer)null, includeNullDelimiter, additionalFields);
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
/*     */   public byte[] getHeader() {
/* 114 */     if (!this.complete) {
/* 115 */       return null;
/*     */     }
/* 117 */     StringBuilder buf = new StringBuilder();
/* 118 */     buf.append("<?xml version=\"1.0\" encoding=\"");
/* 119 */     buf.append(getCharset().name());
/* 120 */     buf.append("\"?>");
/* 121 */     buf.append(this.eol);
/*     */     
/* 123 */     buf.append('<');
/* 124 */     buf.append("Events");
/* 125 */     buf.append(" xmlns=\"http://logging.apache.org/log4j/2.0/events\">");
/* 126 */     buf.append(this.eol);
/* 127 */     return buf.toString().getBytes(getCharset());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getFooter() {
/* 137 */     if (!this.complete) {
/* 138 */       return null;
/*     */     }
/* 140 */     return getBytes("</Events>" + this.eol);
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
/*     */   public Map<String, String> getContentFormat() {
/* 154 */     Map<String, String> result = new HashMap<>();
/*     */     
/* 156 */     result.put("xsd", "log4j-events.xsd");
/* 157 */     result.put("version", "2.0");
/* 158 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getContentType() {
/* 166 */     return "text/xml; charset=" + getCharset();
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
/*     */   @Deprecated
/*     */   public static XmlLayout createLayout(boolean locationInfo, boolean properties, boolean complete, boolean compact, Charset charset, boolean includeStacktrace) {
/* 191 */     return new XmlLayout(null, locationInfo, properties, complete, compact, null, charset, includeStacktrace, false, false, false, null);
/*     */   }
/*     */ 
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static <B extends Builder<B>> B newBuilder() {
/* 197 */     return (B)(new Builder<>()).asBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static XmlLayout createDefaultLayout() {
/* 206 */     return new XmlLayout(null, false, false, false, false, null, StandardCharsets.UTF_8, true, false, false, false, null);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\layout\XmlLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */