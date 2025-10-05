/*     */ package org.apache.logging.log4j.spi;
/*     */ 
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.message.MessageFactory;
/*     */ import org.apache.logging.log4j.util.StackLocatorUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExtendedLoggerWrapper
/*     */   extends AbstractLogger
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final ExtendedLogger logger;
/*     */   
/*     */   public ExtendedLoggerWrapper(ExtendedLogger logger, String name, MessageFactory messageFactory) {
/*  45 */     super(name, messageFactory);
/*  46 */     this.logger = logger;
/*     */   }
/*     */ 
/*     */   
/*     */   public Level getLevel() {
/*  51 */     return this.logger.getLevel();
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
/*     */   public boolean isEnabled(Level level, Marker marker, Message message, Throwable t) {
/*  65 */     return this.logger.isEnabled(level, marker, message, t);
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
/*     */   public boolean isEnabled(Level level, Marker marker, CharSequence message, Throwable t) {
/*  79 */     return this.logger.isEnabled(level, marker, message, t);
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
/*     */   public boolean isEnabled(Level level, Marker marker, Object message, Throwable t) {
/*  93 */     return this.logger.isEnabled(level, marker, message, t);
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
/*     */   public boolean isEnabled(Level level, Marker marker, String message) {
/* 106 */     return this.logger.isEnabled(level, marker, message);
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
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object... params) {
/* 120 */     return this.logger.isEnabled(level, marker, message, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0) {
/* 125 */     return this.logger.isEnabled(level, marker, message, p0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1) {
/* 131 */     return this.logger.isEnabled(level, marker, message, p0, p1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
/* 137 */     return this.logger.isEnabled(level, marker, message, p0, p1, p2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
/* 143 */     return this.logger.isEnabled(level, marker, message, p0, p1, p2, p3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 150 */     return this.logger.isEnabled(level, marker, message, p0, p1, p2, p3, p4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 157 */     return this.logger.isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 164 */     return this.logger.isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5, p6);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 172 */     return this.logger.isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 180 */     return this.logger.isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 188 */     return this.logger.isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
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
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Throwable t) {
/* 202 */     return this.logger.isEnabled(level, marker, message, t);
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
/*     */   public void logMessage(String fqcn, Level level, Marker marker, Message message, Throwable t) {
/* 218 */     if (this.logger instanceof LocationAwareLogger && requiresLocation()) {
/* 219 */       ((LocationAwareLogger)this.logger).logMessage(level, marker, fqcn, StackLocatorUtil.calcLocation(fqcn), message, t);
/*     */     }
/*     */     
/* 222 */     this.logger.logMessage(fqcn, level, marker, message, t);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\spi\ExtendedLoggerWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */