/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import com.fasterxml.jackson.core.PrettyPrinter;
/*     */ import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
/*     */ import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.ObjectWriter;
/*     */ import com.fasterxml.jackson.databind.ser.FilterProvider;
/*     */ import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
/*     */ import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
/*     */ import com.fasterxml.jackson.dataformat.xml.util.DefaultXmlPrettyPrinter;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.apache.logging.log4j.core.impl.Log4jLogEvent;
/*     */ import org.apache.logging.log4j.core.jackson.Log4jJsonObjectMapper;
/*     */ import org.apache.logging.log4j.core.jackson.Log4jXmlObjectMapper;
/*     */ import org.apache.logging.log4j.core.jackson.Log4jYamlObjectMapper;
/*     */ import org.codehaus.stax2.XMLStreamWriter2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class JacksonFactory
/*     */ {
/*     */   protected abstract String getPropertyNameForTimeMillis();
/*     */   
/*     */   protected abstract String getPropertyNameForInstant();
/*     */   
/*     */   protected abstract String getPropertNameForContextMap();
/*     */   
/*     */   protected abstract String getPropertNameForSource();
/*     */   
/*     */   protected abstract String getPropertNameForNanoTime();
/*     */   
/*     */   protected abstract PrettyPrinter newCompactPrinter();
/*     */   
/*     */   protected abstract ObjectMapper newObjectMapper();
/*     */   
/*     */   protected abstract PrettyPrinter newPrettyPrinter();
/*     */   
/*     */   static class JSON
/*     */     extends JacksonFactory
/*     */   {
/*     */     private final boolean encodeThreadContextAsList;
/*     */     private final boolean includeStacktrace;
/*     */     private final boolean stacktraceAsString;
/*     */     private final boolean objectMessageAsJsonObject;
/*     */     
/*     */     public JSON(boolean encodeThreadContextAsList, boolean includeStacktrace, boolean stacktraceAsString, boolean objectMessageAsJsonObject) {
/*  51 */       this.encodeThreadContextAsList = encodeThreadContextAsList;
/*  52 */       this.includeStacktrace = includeStacktrace;
/*  53 */       this.stacktraceAsString = stacktraceAsString;
/*  54 */       this.objectMessageAsJsonObject = objectMessageAsJsonObject;
/*     */     }
/*     */ 
/*     */     
/*     */     protected String getPropertNameForContextMap() {
/*  59 */       return "contextMap";
/*     */     }
/*     */ 
/*     */     
/*     */     protected String getPropertyNameForTimeMillis() {
/*  64 */       return "timeMillis";
/*     */     }
/*     */ 
/*     */     
/*     */     protected String getPropertyNameForInstant() {
/*  69 */       return "instant";
/*     */     }
/*     */ 
/*     */     
/*     */     protected String getPropertNameForSource() {
/*  74 */       return "source";
/*     */     }
/*     */ 
/*     */     
/*     */     protected String getPropertNameForNanoTime() {
/*  79 */       return "nanoTime";
/*     */     }
/*     */ 
/*     */     
/*     */     protected PrettyPrinter newCompactPrinter() {
/*  84 */       return (PrettyPrinter)new MinimalPrettyPrinter();
/*     */     }
/*     */ 
/*     */     
/*     */     protected ObjectMapper newObjectMapper() {
/*  89 */       return (ObjectMapper)new Log4jJsonObjectMapper(this.encodeThreadContextAsList, this.includeStacktrace, this.stacktraceAsString, this.objectMessageAsJsonObject);
/*     */     }
/*     */ 
/*     */     
/*     */     protected PrettyPrinter newPrettyPrinter() {
/*  94 */       return (PrettyPrinter)new DefaultPrettyPrinter();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class XML
/*     */     extends JacksonFactory
/*     */   {
/*     */     static final int DEFAULT_INDENT = 1;
/*     */     
/*     */     private final boolean includeStacktrace;
/*     */     private final boolean stacktraceAsString;
/*     */     
/*     */     public XML(boolean includeStacktrace, boolean stacktraceAsString) {
/* 108 */       this.includeStacktrace = includeStacktrace;
/* 109 */       this.stacktraceAsString = stacktraceAsString;
/*     */     }
/*     */ 
/*     */     
/*     */     protected String getPropertyNameForTimeMillis() {
/* 114 */       return "TimeMillis";
/*     */     }
/*     */ 
/*     */     
/*     */     protected String getPropertyNameForInstant() {
/* 119 */       return "Instant";
/*     */     }
/*     */ 
/*     */     
/*     */     protected String getPropertNameForContextMap() {
/* 124 */       return "ContextMap";
/*     */     }
/*     */ 
/*     */     
/*     */     protected String getPropertNameForSource() {
/* 129 */       return "Source";
/*     */     }
/*     */ 
/*     */     
/*     */     protected String getPropertNameForNanoTime() {
/* 134 */       return "nanoTime";
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected PrettyPrinter newCompactPrinter() {
/* 140 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     protected ObjectMapper newObjectMapper() {
/* 145 */       return (ObjectMapper)new Log4jXmlObjectMapper(this.includeStacktrace, this.stacktraceAsString);
/*     */     }
/*     */ 
/*     */     
/*     */     protected PrettyPrinter newPrettyPrinter() {
/* 150 */       return (PrettyPrinter)new JacksonFactory.Log4jXmlPrettyPrinter(1);
/*     */     }
/*     */   }
/*     */   
/*     */   static class YAML
/*     */     extends JacksonFactory
/*     */   {
/*     */     private final boolean includeStacktrace;
/*     */     private final boolean stacktraceAsString;
/*     */     
/*     */     public YAML(boolean includeStacktrace, boolean stacktraceAsString) {
/* 161 */       this.includeStacktrace = includeStacktrace;
/* 162 */       this.stacktraceAsString = stacktraceAsString;
/*     */     }
/*     */ 
/*     */     
/*     */     protected String getPropertyNameForTimeMillis() {
/* 167 */       return "timeMillis";
/*     */     }
/*     */ 
/*     */     
/*     */     protected String getPropertyNameForInstant() {
/* 172 */       return "instant";
/*     */     }
/*     */ 
/*     */     
/*     */     protected String getPropertNameForContextMap() {
/* 177 */       return "contextMap";
/*     */     }
/*     */ 
/*     */     
/*     */     protected String getPropertNameForSource() {
/* 182 */       return "source";
/*     */     }
/*     */ 
/*     */     
/*     */     protected String getPropertNameForNanoTime() {
/* 187 */       return "nanoTime";
/*     */     }
/*     */ 
/*     */     
/*     */     protected PrettyPrinter newCompactPrinter() {
/* 192 */       return (PrettyPrinter)new MinimalPrettyPrinter();
/*     */     }
/*     */ 
/*     */     
/*     */     protected ObjectMapper newObjectMapper() {
/* 197 */       return (ObjectMapper)new Log4jYamlObjectMapper(false, this.includeStacktrace, this.stacktraceAsString);
/*     */     }
/*     */ 
/*     */     
/*     */     protected PrettyPrinter newPrettyPrinter() {
/* 202 */       return (PrettyPrinter)new DefaultPrettyPrinter();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class Log4jXmlPrettyPrinter
/*     */     extends DefaultXmlPrettyPrinter
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Log4jXmlPrettyPrinter(int nesting) {
/* 222 */       this._nesting = nesting;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void writePrologLinefeed(XMLStreamWriter2 sw) throws XMLStreamException {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public DefaultXmlPrettyPrinter createInstance() {
/* 235 */       return new Log4jXmlPrettyPrinter(1);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ObjectWriter newWriter(boolean locationInfo, boolean properties, boolean compact) {
/* 257 */     return newWriter(locationInfo, properties, compact, false);
/*     */   }
/*     */ 
/*     */   
/*     */   ObjectWriter newWriter(boolean locationInfo, boolean properties, boolean compact, boolean includeMillis) {
/* 262 */     SimpleFilterProvider filters = new SimpleFilterProvider();
/* 263 */     Set<String> except = new HashSet<>(3);
/* 264 */     if (!locationInfo) {
/* 265 */       except.add(getPropertNameForSource());
/*     */     }
/* 267 */     if (!properties) {
/* 268 */       except.add(getPropertNameForContextMap());
/*     */     }
/* 270 */     if (includeMillis) {
/* 271 */       except.add(getPropertyNameForInstant());
/*     */     } else {
/* 273 */       except.add(getPropertyNameForTimeMillis());
/*     */     } 
/* 275 */     except.add(getPropertNameForNanoTime());
/* 276 */     filters.addFilter(Log4jLogEvent.class.getName(), SimpleBeanPropertyFilter.serializeAllExcept(except));
/* 277 */     ObjectWriter writer = newObjectMapper().writer(compact ? newCompactPrinter() : newPrettyPrinter());
/* 278 */     return writer.with((FilterProvider)filters);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\layout\JacksonFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */