/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.DefaultConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "JsonLayout", category = "Core", elementType = "layout", printObject = true)
/*     */ public final class JsonLayout
/*     */   extends AbstractJacksonLayout
/*     */ {
/*     */   private static final String DEFAULT_FOOTER = "]";
/*     */   private static final String DEFAULT_HEADER = "[";
/*     */   static final String CONTENT_TYPE = "application/json";
/*     */   
/*     */   public static class Builder<B extends Builder<B>>
/*     */     extends AbstractJacksonLayout.Builder<B>
/*     */     implements org.apache.logging.log4j.core.util.Builder<JsonLayout>
/*     */   {
/*     */     @PluginBuilderAttribute
/*     */     private boolean propertiesAsList;
/*     */     @PluginBuilderAttribute
/*     */     private boolean objectMessageAsJsonObject;
/*     */     @PluginElement("AdditionalField")
/*     */     private KeyValuePair[] additionalFields;
/*     */     
/*     */     public Builder() {
/*  92 */       setCharset(StandardCharsets.UTF_8);
/*     */     }
/*     */ 
/*     */     
/*     */     public JsonLayout build() {
/*  97 */       boolean encodeThreadContextAsList = (isProperties() && this.propertiesAsList);
/*  98 */       String headerPattern = toStringOrNull(getHeader());
/*  99 */       String footerPattern = toStringOrNull(getFooter());
/* 100 */       return new JsonLayout(getConfiguration(), isLocationInfo(), isProperties(), encodeThreadContextAsList, 
/* 101 */           isComplete(), isCompact(), getEventEol(), getEndOfLine(), headerPattern, footerPattern, getCharset(), 
/* 102 */           isIncludeStacktrace(), isStacktraceAsString(), isIncludeNullDelimiter(), isIncludeTimeMillis(), 
/* 103 */           getAdditionalFields(), getObjectMessageAsJsonObject());
/*     */     }
/*     */     
/*     */     public boolean isPropertiesAsList() {
/* 107 */       return this.propertiesAsList;
/*     */     }
/*     */     
/*     */     public B setPropertiesAsList(boolean propertiesAsList) {
/* 111 */       this.propertiesAsList = propertiesAsList;
/* 112 */       return asBuilder();
/*     */     }
/*     */     
/*     */     public boolean getObjectMessageAsJsonObject() {
/* 116 */       return this.objectMessageAsJsonObject;
/*     */     }
/*     */     
/*     */     public B setObjectMessageAsJsonObject(boolean objectMessageAsJsonObject) {
/* 120 */       this.objectMessageAsJsonObject = objectMessageAsJsonObject;
/* 121 */       return asBuilder();
/*     */     }
/*     */ 
/*     */     
/*     */     public KeyValuePair[] getAdditionalFields() {
/* 126 */       return this.additionalFields;
/*     */     }
/*     */ 
/*     */     
/*     */     public B setAdditionalFields(KeyValuePair[] additionalFields) {
/* 131 */       this.additionalFields = additionalFields;
/* 132 */       return asBuilder();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected JsonLayout(Configuration config, boolean locationInfo, boolean properties, boolean encodeThreadContextAsList, boolean complete, boolean compact, boolean eventEol, String endOfLine, String headerPattern, String footerPattern, Charset charset, boolean includeStacktrace) {
/* 144 */     super(config, (new JacksonFactory.JSON(encodeThreadContextAsList, includeStacktrace, false, false)).newWriter(locationInfo, properties, compact), charset, compact, complete, eventEol, endOfLine, 
/*     */ 
/*     */         
/* 147 */         PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(headerPattern).setDefaultPattern("[").build(), 
/* 148 */         PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(footerPattern).setDefaultPattern("]").build(), false, (KeyValuePair[])null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JsonLayout(Configuration config, boolean locationInfo, boolean properties, boolean encodeThreadContextAsList, boolean complete, boolean compact, boolean eventEol, String endOfLine, String headerPattern, String footerPattern, Charset charset, boolean includeStacktrace, boolean stacktraceAsString, boolean includeNullDelimiter, boolean includeTimeMillis, KeyValuePair[] additionalFields, boolean objectMessageAsJsonObject) {
/* 159 */     super(config, (new JacksonFactory.JSON(encodeThreadContextAsList, includeStacktrace, stacktraceAsString, objectMessageAsJsonObject)).newWriter(locationInfo, properties, compact, includeTimeMillis), charset, compact, complete, eventEol, endOfLine, 
/*     */ 
/*     */         
/* 162 */         PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(headerPattern).setDefaultPattern("[").build(), 
/* 163 */         PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(footerPattern).setDefaultPattern("]").build(), includeNullDelimiter, additionalFields);
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
/*     */   public byte[] getHeader() {
/* 175 */     if (!this.complete) {
/* 176 */       return null;
/*     */     }
/* 178 */     StringBuilder buf = new StringBuilder();
/* 179 */     String str = serializeToString(getHeaderSerializer());
/* 180 */     if (str != null) {
/* 181 */       buf.append(str);
/*     */     }
/* 183 */     buf.append(this.eol);
/* 184 */     return getBytes(buf.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getFooter() {
/* 194 */     if (!this.complete) {
/* 195 */       return null;
/*     */     }
/* 197 */     StringBuilder buf = new StringBuilder();
/* 198 */     buf.append(this.eol);
/* 199 */     String str = serializeToString(getFooterSerializer());
/* 200 */     if (str != null) {
/* 201 */       buf.append(str);
/*     */     }
/* 203 */     buf.append(this.eol);
/* 204 */     return getBytes(buf.toString());
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> getContentFormat() {
/* 209 */     Map<String, String> result = new HashMap<>();
/* 210 */     result.put("version", "2.0");
/* 211 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getContentType() {
/* 219 */     return "application/json; charset=" + getCharset();
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
/*     */   @Deprecated
/*     */   public static JsonLayout createLayout(Configuration config, boolean locationInfo, boolean properties, boolean propertiesAsList, boolean complete, boolean compact, boolean eventEol, String headerPattern, String footerPattern, Charset charset, boolean includeStacktrace) {
/* 267 */     boolean encodeThreadContextAsList = (properties && propertiesAsList);
/* 268 */     return new JsonLayout(config, locationInfo, properties, encodeThreadContextAsList, complete, compact, eventEol, null, headerPattern, footerPattern, charset, includeStacktrace, false, false, false, null, false);
/*     */   }
/*     */ 
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static <B extends Builder<B>> B newBuilder() {
/* 274 */     return (B)(new Builder<>()).asBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JsonLayout createDefaultLayout() {
/* 283 */     return new JsonLayout((Configuration)new DefaultConfiguration(), false, false, false, false, false, false, null, "[", "]", StandardCharsets.UTF_8, true, false, false, false, null, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void toSerializable(LogEvent event, Writer writer) throws IOException {
/* 289 */     if (this.complete && this.eventCount > 0L) {
/* 290 */       writer.append(", ");
/*     */     }
/* 292 */     super.toSerializable(event, writer);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\layout\JsonLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */