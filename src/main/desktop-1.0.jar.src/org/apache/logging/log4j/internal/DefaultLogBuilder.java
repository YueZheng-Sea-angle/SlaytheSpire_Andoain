/*     */ package org.apache.logging.log4j.internal;
/*     */ 
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.LogBuilder;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.message.SimpleMessage;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.LambdaUtil;
/*     */ import org.apache.logging.log4j.util.StackLocatorUtil;
/*     */ import org.apache.logging.log4j.util.Supplier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultLogBuilder
/*     */   implements LogBuilder
/*     */ {
/*  36 */   private static Message EMPTY_MESSAGE = (Message)new SimpleMessage("");
/*  37 */   private static final String FQCN = DefaultLogBuilder.class.getName();
/*  38 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private final Logger logger;
/*     */   private Level level;
/*     */   private Marker marker;
/*     */   private Throwable throwable;
/*     */   private StackTraceElement location;
/*     */   private volatile boolean inUse;
/*     */   private long threadId;
/*     */   
/*     */   public DefaultLogBuilder(Logger logger, Level level) {
/*  49 */     this.logger = logger;
/*  50 */     this.level = level;
/*  51 */     this.threadId = Thread.currentThread().getId();
/*  52 */     this.inUse = true;
/*     */   }
/*     */   
/*     */   public DefaultLogBuilder(Logger logger) {
/*  56 */     this.logger = logger;
/*  57 */     this.inUse = false;
/*  58 */     this.threadId = Thread.currentThread().getId();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LogBuilder reset(Level level) {
/*  67 */     this.inUse = true;
/*  68 */     this.level = level;
/*  69 */     this.marker = null;
/*  70 */     this.throwable = null;
/*  71 */     this.location = null;
/*  72 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LogBuilder withMarker(Marker marker) {
/*  77 */     this.marker = marker;
/*  78 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LogBuilder withThrowable(Throwable throwable) {
/*  83 */     this.throwable = throwable;
/*  84 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LogBuilder withLocation() {
/*  89 */     this.location = StackLocatorUtil.getStackTraceElement(2);
/*  90 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LogBuilder withLocation(StackTraceElement location) {
/*  95 */     this.location = location;
/*  96 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isInUse() {
/* 100 */     return this.inUse;
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(Message message) {
/* 105 */     if (isValid()) {
/* 106 */       logMessage(message);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(CharSequence message) {
/* 112 */     if (isValid()) {
/* 113 */       logMessage(this.logger.getMessageFactory().newMessage(message));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(String message) {
/* 119 */     if (isValid()) {
/* 120 */       logMessage(this.logger.getMessageFactory().newMessage(message));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(String message, Object... params) {
/* 126 */     if (isValid()) {
/* 127 */       logMessage(this.logger.getMessageFactory().newMessage(message, params));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(String message, Supplier<?>... params) {
/* 133 */     if (isValid()) {
/* 134 */       logMessage(this.logger.getMessageFactory().newMessage(message, LambdaUtil.getAll((Supplier[])params)));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(Supplier<Message> messageSupplier) {
/* 140 */     if (isValid()) {
/* 141 */       logMessage((Message)messageSupplier.get());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(Object message) {
/* 147 */     if (isValid()) {
/* 148 */       logMessage(this.logger.getMessageFactory().newMessage(message));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(String message, Object p0) {
/* 154 */     if (isValid()) {
/* 155 */       logMessage(this.logger.getMessageFactory().newMessage(message, new Object[] { p0 }));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(String message, Object p0, Object p1) {
/* 161 */     if (isValid()) {
/* 162 */       logMessage(this.logger.getMessageFactory().newMessage(message, new Object[] { p0, p1 }));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(String message, Object p0, Object p1, Object p2) {
/* 168 */     if (isValid()) {
/* 169 */       logMessage(this.logger.getMessageFactory().newMessage(message, new Object[] { p0, p1, p2 }));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(String message, Object p0, Object p1, Object p2, Object p3) {
/* 175 */     if (isValid()) {
/* 176 */       logMessage(this.logger.getMessageFactory().newMessage(message, new Object[] { p0, p1, p2, p3 }));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 182 */     if (isValid()) {
/* 183 */       logMessage(this.logger.getMessageFactory().newMessage(message, new Object[] { p0, p1, p2, p3, p4 }));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 189 */     if (isValid()) {
/* 190 */       logMessage(this.logger.getMessageFactory().newMessage(message, new Object[] { p0, p1, p2, p3, p4, p5 }));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 196 */     if (isValid()) {
/* 197 */       logMessage(this.logger.getMessageFactory().newMessage(message, new Object[] { p0, p1, p2, p3, p4, p5, p6 }));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void log(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 204 */     if (isValid()) {
/* 205 */       logMessage(this.logger.getMessageFactory().newMessage(message, new Object[] { p0, p1, p2, p3, p4, p5, p6, p7 }));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void log(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 212 */     if (isValid()) {
/* 213 */       logMessage(this.logger.getMessageFactory().newMessage(message, new Object[] { p0, p1, p2, p3, p4, p5, p6, p7, p8 }));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void log(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 220 */     if (isValid()) {
/* 221 */       logMessage(this.logger.getMessageFactory().newMessage(message, new Object[] { p0, p1, p2, p3, p4, p5, p6, p7, p8, p9 }));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void log() {
/* 227 */     if (isValid()) {
/* 228 */       logMessage(EMPTY_MESSAGE);
/*     */     }
/*     */   }
/*     */   
/*     */   private void logMessage(Message message) {
/*     */     try {
/* 234 */       this.logger.logMessage(this.level, this.marker, FQCN, this.location, message, this.throwable);
/*     */     } finally {
/* 236 */       this.inUse = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isValid() {
/* 241 */     if (!this.inUse) {
/* 242 */       LOGGER.warn("Attempt to reuse LogBuilder was ignored. {}", 
/* 243 */           StackLocatorUtil.getCallerClass(2));
/* 244 */       return false;
/*     */     } 
/* 246 */     if (this.threadId != Thread.currentThread().getId()) {
/* 247 */       LOGGER.warn("LogBuilder can only be used on the owning thread. {}", 
/* 248 */           StackLocatorUtil.getCallerClass(2));
/* 249 */       return false;
/*     */     } 
/* 251 */     return true;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\internal\DefaultLogBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */