/*     */ package org.apache.logging.log4j.core.filter;
/*     */ 
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.Logger;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "StringMatchFilter", category = "Core", elementType = "filter", printObject = true)
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public final class StringMatchFilter
/*     */   extends AbstractFilter
/*     */ {
/*     */   public static final String ATTR_MATCH = "match";
/*     */   private final String text;
/*     */   
/*     */   private StringMatchFilter(String text, Filter.Result onMatch, Filter.Result onMismatch) {
/*  43 */     super(onMatch, onMismatch);
/*  44 */     this.text = text;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
/*  50 */     return filter(logger.getMessageFactory().newMessage(msg, params).getFormattedMessage());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
/*  56 */     return filter(logger.getMessageFactory().newMessage(msg).getFormattedMessage());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
/*  62 */     return filter(msg.getFormattedMessage());
/*     */   }
/*     */ 
/*     */   
/*     */   public Filter.Result filter(LogEvent event) {
/*  67 */     return filter(event.getMessage().getFormattedMessage());
/*     */   }
/*     */   
/*     */   private Filter.Result filter(String msg) {
/*  71 */     return msg.contains(this.text) ? this.onMatch : this.onMismatch;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0) {
/*  77 */     return filter(logger.getMessageFactory().newMessage(msg, new Object[] { p0 }).getFormattedMessage());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1) {
/*  83 */     return filter(logger.getMessageFactory().newMessage(msg, new Object[] { p0, p1 }).getFormattedMessage());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2) {
/*  89 */     return filter(logger.getMessageFactory().newMessage(msg, new Object[] { p0, p1, p2 }).getFormattedMessage());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3) {
/*  95 */     return filter(logger.getMessageFactory().newMessage(msg, new Object[] { p0, p1, p2, p3 }).getFormattedMessage());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 102 */     return filter(logger.getMessageFactory().newMessage(msg, new Object[] { p0, p1, p2, p3, p4 }).getFormattedMessage());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 109 */     return filter(logger.getMessageFactory().newMessage(msg, new Object[] { p0, p1, p2, p3, p4, p5 }).getFormattedMessage());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 116 */     return filter(logger.getMessageFactory().newMessage(msg, new Object[] { p0, p1, p2, p3, p4, p5, p6 }).getFormattedMessage());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 124 */     return filter(logger.getMessageFactory().newMessage(msg, new Object[] { p0, p1, p2, p3, p4, p5, p6, p7 }).getFormattedMessage());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 132 */     return filter(logger.getMessageFactory().newMessage(msg, new Object[] { p0, p1, p2, p3, p4, p5, p6, p7, p8
/* 133 */           }).getFormattedMessage());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 141 */     return filter(logger.getMessageFactory().newMessage(msg, new Object[] { p0, p1, p2, p3, p4, p5, p6, p7, p8, p9
/* 142 */           }).getFormattedMessage());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 147 */     return this.text;
/*     */   }
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/* 152 */     return new Builder();
/*     */   }
/*     */   
/*     */   public static class Builder extends AbstractFilter.AbstractFilterBuilder<Builder> implements org.apache.logging.log4j.core.util.Builder<StringMatchFilter> { @PluginBuilderAttribute
/* 156 */     private String text = "";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setMatchString(String text) {
/* 165 */       this.text = text;
/* 166 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public StringMatchFilter build() {
/* 171 */       return new StringMatchFilter(this.text, getOnMatch(), getOnMismatch());
/*     */     } }
/*     */ 
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\filter\StringMatchFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */