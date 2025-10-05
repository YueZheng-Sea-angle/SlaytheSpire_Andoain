/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.net.Facility;
/*     */ import org.apache.logging.log4j.core.net.Priority;
/*     */ import org.apache.logging.log4j.core.util.NetUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "SyslogLayout", category = "Core", elementType = "layout", printObject = true)
/*     */ public final class SyslogLayout
/*     */   extends AbstractStringLayout
/*     */ {
/*     */   public static class Builder<B extends Builder<B>>
/*     */     extends AbstractStringLayout.Builder<B>
/*     */     implements org.apache.logging.log4j.core.util.Builder<SyslogLayout>
/*     */   {
/*     */     @PluginBuilderAttribute
/*     */     private Facility facility;
/*     */     @PluginBuilderAttribute("newLine")
/*     */     private boolean includeNewLine;
/*     */     @PluginBuilderAttribute("newLineEscape")
/*     */     private String escapeNL;
/*     */     
/*     */     public Builder() {
/*  64 */       this.facility = Facility.LOCAL0;
/*     */       setCharset(StandardCharsets.UTF_8);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public SyslogLayout build() {
/*  75 */       return new SyslogLayout(this.facility, this.includeNewLine, this.escapeNL, getCharset());
/*     */     }
/*     */     
/*     */     public Facility getFacility() {
/*  79 */       return this.facility;
/*     */     }
/*     */     
/*     */     public boolean isIncludeNewLine() {
/*  83 */       return this.includeNewLine;
/*     */     }
/*     */     
/*     */     public String getEscapeNL() {
/*  87 */       return this.escapeNL;
/*     */     }
/*     */     
/*     */     public B setFacility(Facility facility) {
/*  91 */       this.facility = facility;
/*  92 */       return asBuilder();
/*     */     }
/*     */     
/*     */     public B setIncludeNewLine(boolean includeNewLine) {
/*  96 */       this.includeNewLine = includeNewLine;
/*  97 */       return asBuilder();
/*     */     }
/*     */     
/*     */     public B setEscapeNL(String escapeNL) {
/* 101 */       this.escapeNL = escapeNL;
/* 102 */       return asBuilder();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static <B extends Builder<B>> B newBuilder() {
/* 109 */     return (B)(new Builder<>()).asBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 115 */   public static final Pattern NEWLINE_PATTERN = Pattern.compile("\\r?\\n");
/*     */ 
/*     */   
/*     */   private final Facility facility;
/*     */   
/*     */   private final boolean includeNewLine;
/*     */   
/*     */   private final String escapeNewLine;
/*     */   
/* 124 */   private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd HH:mm:ss", Locale.ENGLISH);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 129 */   private final String localHostname = NetUtils.getLocalHostname();
/*     */   
/*     */   protected SyslogLayout(Facility facility, boolean includeNL, String escapeNL, Charset charset) {
/* 132 */     super(charset);
/* 133 */     this.facility = facility;
/* 134 */     this.includeNewLine = includeNL;
/* 135 */     this.escapeNewLine = (escapeNL == null) ? null : Matcher.quoteReplacement(escapeNL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toSerializable(LogEvent event) {
/* 146 */     StringBuilder buf = getStringBuilder();
/*     */     
/* 148 */     buf.append('<');
/* 149 */     buf.append(Priority.getPriority(this.facility, event.getLevel()));
/* 150 */     buf.append('>');
/* 151 */     addDate(event.getTimeMillis(), buf);
/* 152 */     buf.append(' ');
/* 153 */     buf.append(this.localHostname);
/* 154 */     buf.append(' ');
/*     */     
/* 156 */     String message = event.getMessage().getFormattedMessage();
/* 157 */     if (null != this.escapeNewLine) {
/* 158 */       message = NEWLINE_PATTERN.matcher(message).replaceAll(this.escapeNewLine);
/*     */     }
/* 160 */     buf.append(message);
/*     */     
/* 162 */     if (this.includeNewLine) {
/* 163 */       buf.append('\n');
/*     */     }
/* 165 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private synchronized void addDate(long timestamp, StringBuilder buf) {
/* 169 */     int index = buf.length() + 4;
/* 170 */     buf.append(this.dateFormat.format(new Date(timestamp)));
/*     */     
/* 172 */     if (buf.charAt(index) == '0') {
/* 173 */       buf.setCharAt(index, ' ');
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
/*     */   public Map<String, String> getContentFormat() {
/* 191 */     Map<String, String> result = new HashMap<>();
/* 192 */     result.put("structured", "false");
/* 193 */     result.put("formatType", "logfilepatternreceiver");
/* 194 */     result.put("dateFormat", this.dateFormat.toPattern());
/* 195 */     result.put("format", "<LEVEL>TIMESTAMP PROP(HOSTNAME) MESSAGE");
/* 196 */     return result;
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
/*     */   public static SyslogLayout createLayout(Facility facility, boolean includeNewLine, String escapeNL, Charset charset) {
/* 212 */     return new SyslogLayout(facility, includeNewLine, escapeNL, charset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Facility getFacility() {
/* 221 */     return this.facility;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\layout\SyslogLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */