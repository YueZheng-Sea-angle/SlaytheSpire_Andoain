/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.LineNumberReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Serializable;
/*     */ import java.io.StringReader;
/*     */ import java.io.StringWriter;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.pattern.DatePatternConverter;
/*     */ import org.apache.logging.log4j.core.util.Transform;
/*     */ import org.apache.logging.log4j.util.Strings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "HtmlLayout", category = "Core", elementType = "layout", printObject = true)
/*     */ public final class HtmlLayout
/*     */   extends AbstractStringLayout
/*     */ {
/*     */   public static final String DEFAULT_FONT_FAMILY = "arial,sans-serif";
/*     */   private static final String TRACE_PREFIX = "<br />&nbsp;&nbsp;&nbsp;&nbsp;";
/*  60 */   private static final String REGEXP = Strings.LINE_SEPARATOR.equals("\n") ? "\n" : (Strings.LINE_SEPARATOR + "|\n");
/*     */   
/*     */   private static final String DEFAULT_TITLE = "Log4j Log Messages";
/*     */   private static final String DEFAULT_CONTENT_TYPE = "text/html";
/*     */   private static final String DEFAULT_DATE_PATTERN = "JVM_ELAPSE_TIME";
/*  65 */   private final long jvmStartTime = ManagementFactory.getRuntimeMXBean().getStartTime();
/*     */   
/*     */   private final boolean locationInfo;
/*     */   
/*     */   private final String title;
/*     */   private final String contentType;
/*     */   private final String font;
/*     */   private final String fontSize;
/*     */   private final String headerSize;
/*     */   private final DatePatternConverter datePatternConverter;
/*     */   
/*     */   public enum FontSize
/*     */   {
/*  78 */     SMALLER("smaller"), XXSMALL("xx-small"), XSMALL("x-small"), SMALL("small"), MEDIUM("medium"), LARGE("large"),
/*  79 */     XLARGE("x-large"), XXLARGE("xx-large"), LARGER("larger");
/*     */     
/*     */     private final String size;
/*     */     
/*     */     FontSize(String size) {
/*  84 */       this.size = size;
/*     */     }
/*     */     
/*     */     public String getFontSize() {
/*  88 */       return this.size;
/*     */     }
/*     */     
/*     */     public static FontSize getFontSize(String size) {
/*  92 */       for (FontSize fontSize : values()) {
/*  93 */         if (fontSize.size.equals(size)) {
/*  94 */           return fontSize;
/*     */         }
/*     */       } 
/*  97 */       return SMALL;
/*     */     }
/*     */     
/*     */     public FontSize larger() {
/* 101 */       return (ordinal() < XXLARGE.ordinal()) ? values()[ordinal() + 1] : this;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private HtmlLayout(boolean locationInfo, String title, String contentType, Charset charset, String font, String fontSize, String headerSize, String datePattern, String timezone) {
/* 107 */     super(charset);
/* 108 */     this.locationInfo = locationInfo;
/* 109 */     this.title = title;
/* 110 */     this.contentType = addCharsetToContentType(contentType);
/* 111 */     this.font = font;
/* 112 */     this.fontSize = fontSize;
/* 113 */     this.headerSize = headerSize;
/* 114 */     this
/* 115 */       .datePatternConverter = "JVM_ELAPSE_TIME".equals(datePattern) ? null : DatePatternConverter.newInstance(new String[] { datePattern, timezone });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTitle() {
/* 122 */     return this.title;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLocationInfo() {
/* 129 */     return this.locationInfo;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requiresLocation() {
/* 134 */     return this.locationInfo;
/*     */   }
/*     */   
/*     */   private String addCharsetToContentType(String contentType) {
/* 138 */     if (contentType == null) {
/* 139 */       return "text/html; charset=" + getCharset();
/*     */     }
/* 141 */     return contentType.contains("charset") ? contentType : (contentType + "; charset=" + getCharset());
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
/* 152 */     StringBuilder sbuf = getStringBuilder();
/*     */     
/* 154 */     sbuf.append(Strings.LINE_SEPARATOR).append("<tr>").append(Strings.LINE_SEPARATOR);
/*     */     
/* 156 */     sbuf.append("<td>");
/*     */     
/* 158 */     if (this.datePatternConverter == null) {
/* 159 */       sbuf.append(event.getTimeMillis() - this.jvmStartTime);
/*     */     } else {
/* 161 */       this.datePatternConverter.format(event, sbuf);
/*     */     } 
/* 163 */     sbuf.append("</td>").append(Strings.LINE_SEPARATOR);
/*     */     
/* 165 */     String escapedThread = Transform.escapeHtmlTags(event.getThreadName());
/* 166 */     sbuf.append("<td title=\"").append(escapedThread).append(" thread\">");
/* 167 */     sbuf.append(escapedThread);
/* 168 */     sbuf.append("</td>").append(Strings.LINE_SEPARATOR);
/*     */     
/* 170 */     sbuf.append("<td title=\"Level\">");
/* 171 */     if (event.getLevel().equals(Level.DEBUG)) {
/* 172 */       sbuf.append("<font color=\"#339933\">");
/* 173 */       sbuf.append(Transform.escapeHtmlTags(String.valueOf(event.getLevel())));
/* 174 */       sbuf.append("</font>");
/* 175 */     } else if (event.getLevel().isMoreSpecificThan(Level.WARN)) {
/* 176 */       sbuf.append("<font color=\"#993300\"><strong>");
/* 177 */       sbuf.append(Transform.escapeHtmlTags(String.valueOf(event.getLevel())));
/* 178 */       sbuf.append("</strong></font>");
/*     */     } else {
/* 180 */       sbuf.append(Transform.escapeHtmlTags(String.valueOf(event.getLevel())));
/*     */     } 
/* 182 */     sbuf.append("</td>").append(Strings.LINE_SEPARATOR);
/*     */     
/* 184 */     String escapedLogger = Transform.escapeHtmlTags(event.getLoggerName());
/* 185 */     if (Strings.isEmpty(escapedLogger)) {
/* 186 */       escapedLogger = "root";
/*     */     }
/* 188 */     sbuf.append("<td title=\"").append(escapedLogger).append(" logger\">");
/* 189 */     sbuf.append(escapedLogger);
/* 190 */     sbuf.append("</td>").append(Strings.LINE_SEPARATOR);
/*     */     
/* 192 */     if (this.locationInfo) {
/* 193 */       StackTraceElement element = event.getSource();
/* 194 */       sbuf.append("<td>");
/* 195 */       sbuf.append(Transform.escapeHtmlTags(element.getFileName()));
/* 196 */       sbuf.append(':');
/* 197 */       sbuf.append(element.getLineNumber());
/* 198 */       sbuf.append("</td>").append(Strings.LINE_SEPARATOR);
/*     */     } 
/*     */     
/* 201 */     sbuf.append("<td title=\"Message\">");
/* 202 */     sbuf.append(Transform.escapeHtmlTags(event.getMessage().getFormattedMessage()).replaceAll(REGEXP, "<br />"));
/* 203 */     sbuf.append("</td>").append(Strings.LINE_SEPARATOR);
/* 204 */     sbuf.append("</tr>").append(Strings.LINE_SEPARATOR);
/*     */     
/* 206 */     if (event.getContextStack() != null && !event.getContextStack().isEmpty()) {
/* 207 */       sbuf.append("<tr><td bgcolor=\"#EEEEEE\" style=\"font-size : ").append(this.fontSize);
/* 208 */       sbuf.append(";\" colspan=\"6\" ");
/* 209 */       sbuf.append("title=\"Nested Diagnostic Context\">");
/* 210 */       sbuf.append("NDC: ").append(Transform.escapeHtmlTags(event.getContextStack().toString()));
/* 211 */       sbuf.append("</td></tr>").append(Strings.LINE_SEPARATOR);
/*     */     } 
/*     */     
/* 214 */     if (event.getContextData() != null && !event.getContextData().isEmpty()) {
/* 215 */       sbuf.append("<tr><td bgcolor=\"#EEEEEE\" style=\"font-size : ").append(this.fontSize);
/* 216 */       sbuf.append(";\" colspan=\"6\" ");
/* 217 */       sbuf.append("title=\"Mapped Diagnostic Context\">");
/* 218 */       sbuf.append("MDC: ").append(Transform.escapeHtmlTags(event.getContextData().toMap().toString()));
/* 219 */       sbuf.append("</td></tr>").append(Strings.LINE_SEPARATOR);
/*     */     } 
/*     */     
/* 222 */     Throwable throwable = event.getThrown();
/* 223 */     if (throwable != null) {
/* 224 */       sbuf.append("<tr><td bgcolor=\"#993300\" style=\"color:White; font-size : ").append(this.fontSize);
/* 225 */       sbuf.append(";\" colspan=\"6\">");
/* 226 */       appendThrowableAsHtml(throwable, sbuf);
/* 227 */       sbuf.append("</td></tr>").append(Strings.LINE_SEPARATOR);
/*     */     } 
/*     */     
/* 230 */     return sbuf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getContentType() {
/* 238 */     return this.contentType;
/*     */   }
/*     */   
/*     */   private void appendThrowableAsHtml(Throwable throwable, StringBuilder sbuf) {
/* 242 */     StringWriter sw = new StringWriter();
/* 243 */     PrintWriter pw = new PrintWriter(sw);
/*     */     try {
/* 245 */       throwable.printStackTrace(pw);
/* 246 */     } catch (RuntimeException runtimeException) {}
/*     */ 
/*     */     
/* 249 */     pw.flush();
/* 250 */     LineNumberReader reader = new LineNumberReader(new StringReader(sw.toString()));
/* 251 */     ArrayList<String> lines = new ArrayList<>();
/*     */     try {
/* 253 */       String line = reader.readLine();
/* 254 */       while (line != null) {
/* 255 */         lines.add(line);
/* 256 */         line = reader.readLine();
/*     */       } 
/* 258 */     } catch (IOException ex) {
/* 259 */       if (ex instanceof java.io.InterruptedIOException) {
/* 260 */         Thread.currentThread().interrupt();
/*     */       }
/* 262 */       lines.add(ex.toString());
/*     */     } 
/* 264 */     boolean first = true;
/* 265 */     for (String line : lines) {
/* 266 */       if (!first) {
/* 267 */         sbuf.append("<br />&nbsp;&nbsp;&nbsp;&nbsp;");
/*     */       } else {
/* 269 */         first = false;
/*     */       } 
/* 271 */       sbuf.append(Transform.escapeHtmlTags(line));
/* 272 */       sbuf.append(Strings.LINE_SEPARATOR);
/*     */     } 
/*     */   }
/*     */   
/*     */   private StringBuilder appendLs(StringBuilder sbuilder, String s) {
/* 277 */     sbuilder.append(s).append(Strings.LINE_SEPARATOR);
/* 278 */     return sbuilder;
/*     */   }
/*     */   
/*     */   private StringBuilder append(StringBuilder sbuilder, String s) {
/* 282 */     sbuilder.append(s);
/* 283 */     return sbuilder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getHeader() {
/* 292 */     StringBuilder sbuf = new StringBuilder();
/* 293 */     append(sbuf, "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" ");
/* 294 */     appendLs(sbuf, "\"http://www.w3.org/TR/html4/loose.dtd\">");
/* 295 */     appendLs(sbuf, "<html>");
/* 296 */     appendLs(sbuf, "<head>");
/* 297 */     append(sbuf, "<meta charset=\"");
/* 298 */     append(sbuf, getCharset().toString());
/* 299 */     appendLs(sbuf, "\"/>");
/* 300 */     append(sbuf, "<title>").append(this.title);
/* 301 */     appendLs(sbuf, "</title>");
/* 302 */     appendLs(sbuf, "<style type=\"text/css\">");
/* 303 */     appendLs(sbuf, "<!--");
/* 304 */     append(sbuf, "body, table {font-family:").append(this.font).append("; font-size: ");
/* 305 */     appendLs(sbuf, this.headerSize).append(";}");
/* 306 */     appendLs(sbuf, "th {background: #336699; color: #FFFFFF; text-align: left;}");
/* 307 */     appendLs(sbuf, "-->");
/* 308 */     appendLs(sbuf, "</style>");
/* 309 */     appendLs(sbuf, "</head>");
/* 310 */     appendLs(sbuf, "<body bgcolor=\"#FFFFFF\" topmargin=\"6\" leftmargin=\"6\">");
/* 311 */     appendLs(sbuf, "<hr size=\"1\" noshade=\"noshade\">");
/* 312 */     appendLs(sbuf, "Log session start time " + new Date() + "<br>");
/* 313 */     appendLs(sbuf, "<br>");
/* 314 */     appendLs(sbuf, "<table cellspacing=\"0\" cellpadding=\"4\" border=\"1\" bordercolor=\"#224466\" width=\"100%\">");
/*     */     
/* 316 */     appendLs(sbuf, "<tr>");
/* 317 */     appendLs(sbuf, "<th>Time</th>");
/* 318 */     appendLs(sbuf, "<th>Thread</th>");
/* 319 */     appendLs(sbuf, "<th>Level</th>");
/* 320 */     appendLs(sbuf, "<th>Logger</th>");
/* 321 */     if (this.locationInfo) {
/* 322 */       appendLs(sbuf, "<th>File:Line</th>");
/*     */     }
/* 324 */     appendLs(sbuf, "<th>Message</th>");
/* 325 */     appendLs(sbuf, "</tr>");
/* 326 */     return sbuf.toString().getBytes(getCharset());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getFooter() {
/* 335 */     StringBuilder sbuf = new StringBuilder();
/* 336 */     appendLs(sbuf, "</table>");
/* 337 */     appendLs(sbuf, "<br>");
/* 338 */     appendLs(sbuf, "</body></html>");
/* 339 */     return getBytes(sbuf.toString());
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
/*     */   @Deprecated
/*     */   @PluginFactory
/*     */   public static HtmlLayout createLayout(@PluginAttribute("locationInfo") boolean locationInfo, @PluginAttribute(value = "title", defaultString = "Log4j Log Messages") String title, @PluginAttribute("contentType") String contentType, @PluginAttribute(value = "charset", defaultString = "UTF-8") Charset charset, @PluginAttribute("fontSize") String fontSize, @PluginAttribute(value = "fontName", defaultString = "arial,sans-serif") String font) {
/* 361 */     FontSize fs = FontSize.getFontSize(fontSize);
/* 362 */     fontSize = fs.getFontSize();
/* 363 */     String headerSize = fs.larger().getFontSize();
/* 364 */     if (contentType == null) {
/* 365 */       contentType = "text/html; charset=" + charset;
/*     */     }
/* 367 */     return new HtmlLayout(locationInfo, title, contentType, charset, font, fontSize, headerSize, "JVM_ELAPSE_TIME", null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HtmlLayout createDefaultLayout() {
/* 377 */     return newBuilder().build();
/*     */   }
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/* 382 */     return new Builder();
/*     */   }
/*     */   
/*     */   public static class Builder
/*     */     implements org.apache.logging.log4j.core.util.Builder<HtmlLayout> {
/*     */     @PluginBuilderAttribute
/*     */     private boolean locationInfo = false;
/*     */     @PluginBuilderAttribute
/* 390 */     private String title = "Log4j Log Messages";
/*     */     
/*     */     @PluginBuilderAttribute
/* 393 */     private String contentType = null;
/*     */     
/*     */     @PluginBuilderAttribute
/* 396 */     private Charset charset = StandardCharsets.UTF_8;
/*     */     
/*     */     @PluginBuilderAttribute
/* 399 */     private HtmlLayout.FontSize fontSize = HtmlLayout.FontSize.SMALL;
/*     */     
/*     */     @PluginBuilderAttribute
/* 402 */     private String fontName = "arial,sans-serif";
/*     */     
/*     */     @PluginBuilderAttribute
/* 405 */     private String datePattern = "JVM_ELAPSE_TIME";
/*     */     
/*     */     @PluginBuilderAttribute
/* 408 */     private String timezone = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withLocationInfo(boolean locationInfo) {
/* 415 */       this.locationInfo = locationInfo;
/* 416 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withTitle(String title) {
/* 420 */       this.title = title;
/* 421 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withContentType(String contentType) {
/* 425 */       this.contentType = contentType;
/* 426 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withCharset(Charset charset) {
/* 430 */       this.charset = charset;
/* 431 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withFontSize(HtmlLayout.FontSize fontSize) {
/* 435 */       this.fontSize = fontSize;
/* 436 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withFontName(String fontName) {
/* 440 */       this.fontName = fontName;
/* 441 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setDatePattern(String datePattern) {
/* 445 */       this.datePattern = datePattern;
/* 446 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setTimezone(String timezone) {
/* 450 */       this.timezone = timezone;
/* 451 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public HtmlLayout build() {
/* 457 */       if (this.contentType == null) {
/* 458 */         this.contentType = "text/html; charset=" + this.charset;
/*     */       }
/* 460 */       return new HtmlLayout(this.locationInfo, this.title, this.contentType, this.charset, this.fontName, this.fontSize.getFontSize(), this.fontSize
/* 461 */           .larger().getFontSize(), this.datePattern, this.timezone);
/*     */     }
/*     */     
/*     */     private Builder() {}
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\layout\HtmlLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */