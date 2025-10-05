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
/*     */ import org.apache.logging.log4j.core.config.DefaultConfiguration;
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
/*     */ @Plugin(name = "YamlLayout", category = "Core", elementType = "layout", printObject = true)
/*     */ public final class YamlLayout
/*     */   extends AbstractJacksonLayout
/*     */ {
/*     */   private static final String DEFAULT_FOOTER = "";
/*     */   private static final String DEFAULT_HEADER = "";
/*     */   static final String CONTENT_TYPE = "application/yaml";
/*     */   
/*     */   public static class Builder<B extends Builder<B>>
/*     */     extends AbstractJacksonLayout.Builder<B>
/*     */     implements org.apache.logging.log4j.core.util.Builder<YamlLayout>
/*     */   {
/*     */     public Builder() {
/*  61 */       setCharset(StandardCharsets.UTF_8);
/*     */     }
/*     */ 
/*     */     
/*     */     public YamlLayout build() {
/*  66 */       String headerPattern = toStringOrNull(getHeader());
/*  67 */       String footerPattern = toStringOrNull(getFooter());
/*  68 */       return new YamlLayout(getConfiguration(), isLocationInfo(), isProperties(), isComplete(), 
/*  69 */           isCompact(), getEventEol(), getEndOfLine(), headerPattern, footerPattern, getCharset(), 
/*  70 */           isIncludeStacktrace(), isStacktraceAsString(), isIncludeNullDelimiter(), 
/*  71 */           isIncludeTimeMillis(), getAdditionalFields());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected YamlLayout(Configuration config, boolean locationInfo, boolean properties, boolean complete, boolean compact, boolean eventEol, String headerPattern, String footerPattern, Charset charset, boolean includeStacktrace) {
/*  82 */     super(config, (new JacksonFactory.YAML(includeStacktrace, false)).newWriter(locationInfo, properties, compact), charset, compact, complete, eventEol, (String)null, 
/*     */         
/*  84 */         PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(headerPattern).setDefaultPattern("").build(), 
/*  85 */         PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(footerPattern).setDefaultPattern("").build(), false, (KeyValuePair[])null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private YamlLayout(Configuration config, boolean locationInfo, boolean properties, boolean complete, boolean compact, boolean eventEol, String endOfLine, String headerPattern, String footerPattern, Charset charset, boolean includeStacktrace, boolean stacktraceAsString, boolean includeNullDelimiter, boolean includeTimeMillis, KeyValuePair[] additionalFields) {
/*  95 */     super(config, (new JacksonFactory.YAML(includeStacktrace, stacktraceAsString))
/*  96 */         .newWriter(locationInfo, properties, compact, includeTimeMillis), charset, compact, complete, eventEol, endOfLine, 
/*     */         
/*  98 */         PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(headerPattern).setDefaultPattern("").build(), 
/*  99 */         PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(footerPattern).setDefaultPattern("").build(), includeNullDelimiter, additionalFields);
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
/* 111 */     if (!this.complete) {
/* 112 */       return null;
/*     */     }
/* 114 */     StringBuilder buf = new StringBuilder();
/* 115 */     String str = serializeToString(getHeaderSerializer());
/* 116 */     if (str != null) {
/* 117 */       buf.append(str);
/*     */     }
/* 119 */     buf.append(this.eol);
/* 120 */     return getBytes(buf.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getFooter() {
/* 130 */     if (!this.complete) {
/* 131 */       return null;
/*     */     }
/* 133 */     StringBuilder buf = new StringBuilder();
/* 134 */     buf.append(this.eol);
/* 135 */     String str = serializeToString(getFooterSerializer());
/* 136 */     if (str != null) {
/* 137 */       buf.append(str);
/*     */     }
/* 139 */     buf.append(this.eol);
/* 140 */     return getBytes(buf.toString());
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> getContentFormat() {
/* 145 */     Map<String, String> result = new HashMap<>();
/* 146 */     result.put("version", "2.0");
/* 147 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getContentType() {
/* 155 */     return "application/yaml; charset=" + getCharset();
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
/*     */   @Deprecated
/*     */   public static AbstractJacksonLayout createLayout(Configuration config, boolean locationInfo, boolean properties, String headerPattern, String footerPattern, Charset charset, boolean includeStacktrace) {
/* 188 */     return new YamlLayout(config, locationInfo, properties, false, false, true, null, headerPattern, footerPattern, charset, includeStacktrace, false, false, false, null);
/*     */   }
/*     */ 
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static <B extends Builder<B>> B newBuilder() {
/* 194 */     return (B)(new Builder<>()).asBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static AbstractJacksonLayout createDefaultLayout() {
/* 203 */     return new YamlLayout((Configuration)new DefaultConfiguration(), false, false, false, false, false, null, "", "", StandardCharsets.UTF_8, true, false, false, false, null);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\layout\YamlLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */