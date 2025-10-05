/*     */ package org.apache.logging.log4j.core.impl;
/*     */ 
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.ThreadContext;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.async.InternalAsyncUtil;
/*     */ import org.apache.logging.log4j.core.time.Instant;
/*     */ import org.apache.logging.log4j.core.time.MutableInstant;
/*     */ import org.apache.logging.log4j.core.util.Clock;
/*     */ import org.apache.logging.log4j.core.util.Constants;
/*     */ import org.apache.logging.log4j.core.util.NanoClock;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.message.ParameterConsumer;
/*     */ import org.apache.logging.log4j.message.ParameterVisitable;
/*     */ import org.apache.logging.log4j.message.ReusableMessage;
/*     */ import org.apache.logging.log4j.message.SimpleMessage;
/*     */ import org.apache.logging.log4j.message.TimestampMessage;
/*     */ import org.apache.logging.log4j.util.ReadOnlyStringMap;
/*     */ import org.apache.logging.log4j.util.StackLocatorUtil;
/*     */ import org.apache.logging.log4j.util.StringBuilders;
/*     */ import org.apache.logging.log4j.util.StringMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MutableLogEvent
/*     */   implements LogEvent, ReusableMessage, ParameterVisitable
/*     */ {
/*  44 */   private static final Message EMPTY = (Message)new SimpleMessage("");
/*     */   
/*     */   private int threadPriority;
/*     */   private long threadId;
/*  48 */   private final MutableInstant instant = new MutableInstant();
/*     */   private long nanoTime;
/*     */   private short parameterCount;
/*     */   private boolean includeLocation;
/*     */   private boolean endOfBatch = false;
/*     */   private Level level;
/*     */   private String threadName;
/*     */   private String loggerName;
/*     */   private Message message;
/*     */   private String messageFormat;
/*     */   private StringBuilder messageText;
/*     */   private Object[] parameters;
/*     */   private Throwable thrown;
/*     */   private ThrowableProxy thrownProxy;
/*  62 */   private StringMap contextData = ContextDataFactory.createContextData();
/*     */   
/*     */   private Marker marker;
/*     */   private String loggerFqcn;
/*     */   private StackTraceElement source;
/*     */   private ThreadContext.ContextStack contextStack;
/*     */   transient boolean reserved = false;
/*     */   
/*     */   public MutableLogEvent() {
/*  71 */     this(null, null);
/*     */   }
/*     */   
/*     */   public MutableLogEvent(StringBuilder msgText, Object[] replacementParameters) {
/*  75 */     this.messageText = msgText;
/*  76 */     this.parameters = replacementParameters;
/*     */   }
/*     */ 
/*     */   
/*     */   public Log4jLogEvent toImmutable() {
/*  81 */     return createMemento();
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
/*     */   public void initFrom(LogEvent event) {
/*  95 */     this.loggerFqcn = event.getLoggerFqcn();
/*  96 */     this.marker = event.getMarker();
/*  97 */     this.level = event.getLevel();
/*  98 */     this.loggerName = event.getLoggerName();
/*  99 */     this.thrown = event.getThrown();
/* 100 */     this.thrownProxy = event.getThrownProxy();
/*     */     
/* 102 */     this.instant.initFrom(event.getInstant());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 107 */     this.contextData.putAll(event.getContextData());
/*     */     
/* 109 */     this.contextStack = event.getContextStack();
/* 110 */     this.source = event.isIncludeLocation() ? event.getSource() : null;
/* 111 */     this.threadId = event.getThreadId();
/* 112 */     this.threadName = event.getThreadName();
/* 113 */     this.threadPriority = event.getThreadPriority();
/* 114 */     this.endOfBatch = event.isEndOfBatch();
/* 115 */     this.includeLocation = event.isIncludeLocation();
/* 116 */     this.nanoTime = event.getNanoTime();
/* 117 */     setMessage(event.getMessage());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 124 */     this.loggerFqcn = null;
/* 125 */     this.marker = null;
/* 126 */     this.level = null;
/* 127 */     this.loggerName = null;
/* 128 */     this.message = null;
/* 129 */     this.messageFormat = null;
/* 130 */     this.thrown = null;
/* 131 */     this.thrownProxy = null;
/* 132 */     this.source = null;
/* 133 */     if (this.contextData != null) {
/* 134 */       if (this.contextData.isFrozen()) {
/* 135 */         this.contextData = null;
/*     */       } else {
/* 137 */         this.contextData.clear();
/*     */       } 
/*     */     }
/* 140 */     this.contextStack = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 147 */     StringBuilders.trimToMaxSize(this.messageText, Constants.MAX_REUSABLE_MESSAGE_SIZE);
/*     */     
/* 149 */     if (this.parameters != null) {
/* 150 */       Arrays.fill(this.parameters, (Object)null);
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
/*     */   public String getLoggerFqcn() {
/* 164 */     return this.loggerFqcn;
/*     */   }
/*     */   
/*     */   public void setLoggerFqcn(String loggerFqcn) {
/* 168 */     this.loggerFqcn = loggerFqcn;
/*     */   }
/*     */ 
/*     */   
/*     */   public Marker getMarker() {
/* 173 */     return this.marker;
/*     */   }
/*     */   
/*     */   public void setMarker(Marker marker) {
/* 177 */     this.marker = marker;
/*     */   }
/*     */ 
/*     */   
/*     */   public Level getLevel() {
/* 182 */     if (this.level == null) {
/* 183 */       this.level = Level.OFF;
/*     */     }
/* 185 */     return this.level;
/*     */   }
/*     */   
/*     */   public void setLevel(Level level) {
/* 189 */     this.level = level;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLoggerName() {
/* 194 */     return this.loggerName;
/*     */   }
/*     */   
/*     */   public void setLoggerName(String loggerName) {
/* 198 */     this.loggerName = loggerName;
/*     */   }
/*     */ 
/*     */   
/*     */   public Message getMessage() {
/* 203 */     if (this.message == null) {
/* 204 */       return (this.messageText == null) ? EMPTY : (Message)this;
/*     */     }
/* 206 */     return this.message;
/*     */   }
/*     */   
/*     */   public void setMessage(Message msg) {
/* 210 */     if (msg instanceof ReusableMessage) {
/* 211 */       ReusableMessage reusable = (ReusableMessage)msg;
/* 212 */       reusable.formatTo(getMessageTextForWriting());
/* 213 */       this.messageFormat = msg.getFormat();
/* 214 */       this.parameters = reusable.swapParameters((this.parameters == null) ? new Object[10] : this.parameters);
/* 215 */       this.parameterCount = reusable.getParameterCount();
/*     */     } else {
/* 217 */       this.message = InternalAsyncUtil.makeMessageImmutable(msg);
/*     */     } 
/*     */   }
/*     */   
/*     */   private StringBuilder getMessageTextForWriting() {
/* 222 */     if (this.messageText == null)
/*     */     {
/* 224 */       this.messageText = new StringBuilder(Constants.INITIAL_REUSABLE_MESSAGE_SIZE);
/*     */     }
/* 226 */     this.messageText.setLength(0);
/* 227 */     return this.messageText;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedMessage() {
/* 235 */     return this.messageText.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormat() {
/* 243 */     return this.messageFormat;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getParameters() {
/* 251 */     return (this.parameters == null) ? null : Arrays.<Object>copyOf(this.parameters, this.parameterCount);
/*     */   }
/*     */ 
/*     */   
/*     */   public <S> void forEachParameter(ParameterConsumer<S> action, S state) {
/* 256 */     if (this.parameters != null) {
/* 257 */       for (short i = 0; i < this.parameterCount; i = (short)(i + 1)) {
/* 258 */         action.accept(this.parameters[i], i, state);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getThrowable() {
/* 268 */     return getThrown();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void formatTo(StringBuilder buffer) {
/* 276 */     buffer.append(this.messageText);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] swapParameters(Object[] emptyReplacement) {
/* 287 */     Object[] result = this.parameters;
/* 288 */     this.parameters = emptyReplacement;
/* 289 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getParameterCount() {
/* 297 */     return this.parameterCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public Message memento() {
/* 302 */     if (this.message == null) {
/* 303 */       this.message = new MementoMessage(String.valueOf(this.messageText), this.messageFormat, getParameters());
/*     */     }
/* 305 */     return this.message;
/*     */   }
/*     */ 
/*     */   
/*     */   public Throwable getThrown() {
/* 310 */     return this.thrown;
/*     */   }
/*     */   
/*     */   public void setThrown(Throwable thrown) {
/* 314 */     this.thrown = thrown;
/*     */   }
/*     */   
/*     */   void initTime(Clock clock, NanoClock nanoClock) {
/* 318 */     if (this.message instanceof TimestampMessage) {
/* 319 */       this.instant.initFromEpochMilli(((TimestampMessage)this.message).getTimestamp(), 0);
/*     */     } else {
/* 321 */       this.instant.initFrom(clock);
/*     */     } 
/* 323 */     this.nanoTime = nanoClock.nanoTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getTimeMillis() {
/* 328 */     return this.instant.getEpochMillisecond();
/*     */   }
/*     */   
/*     */   public void setTimeMillis(long timeMillis) {
/* 332 */     this.instant.initFromEpochMilli(timeMillis, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public Instant getInstant() {
/* 337 */     return (Instant)this.instant;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThrowableProxy getThrownProxy() {
/* 346 */     if (this.thrownProxy == null && this.thrown != null) {
/* 347 */       this.thrownProxy = new ThrowableProxy(this.thrown);
/*     */     }
/* 349 */     return this.thrownProxy;
/*     */   }
/*     */   
/*     */   public void setSource(StackTraceElement source) {
/* 353 */     this.source = source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StackTraceElement getSource() {
/* 363 */     if (this.source != null) {
/* 364 */       return this.source;
/*     */     }
/* 366 */     if (this.loggerFqcn == null || !this.includeLocation) {
/* 367 */       return null;
/*     */     }
/* 369 */     this.source = StackLocatorUtil.calcLocation(this.loggerFqcn);
/* 370 */     return this.source;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ReadOnlyStringMap getContextData() {
/* 376 */     return (ReadOnlyStringMap)this.contextData;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> getContextMap() {
/* 381 */     return this.contextData.toMap();
/*     */   }
/*     */   
/*     */   public void setContextData(StringMap mutableContextData) {
/* 385 */     this.contextData = mutableContextData;
/*     */   }
/*     */ 
/*     */   
/*     */   public ThreadContext.ContextStack getContextStack() {
/* 390 */     return this.contextStack;
/*     */   }
/*     */   
/*     */   public void setContextStack(ThreadContext.ContextStack contextStack) {
/* 394 */     this.contextStack = contextStack;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getThreadId() {
/* 399 */     return this.threadId;
/*     */   }
/*     */   
/*     */   public void setThreadId(long threadId) {
/* 403 */     this.threadId = threadId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getThreadName() {
/* 408 */     return this.threadName;
/*     */   }
/*     */   
/*     */   public void setThreadName(String threadName) {
/* 412 */     this.threadName = threadName;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getThreadPriority() {
/* 417 */     return this.threadPriority;
/*     */   }
/*     */   
/*     */   public void setThreadPriority(int threadPriority) {
/* 421 */     this.threadPriority = threadPriority;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isIncludeLocation() {
/* 426 */     return this.includeLocation;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIncludeLocation(boolean includeLocation) {
/* 431 */     this.includeLocation = includeLocation;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEndOfBatch() {
/* 436 */     return this.endOfBatch;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEndOfBatch(boolean endOfBatch) {
/* 441 */     this.endOfBatch = endOfBatch;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getNanoTime() {
/* 446 */     return this.nanoTime;
/*     */   }
/*     */   
/*     */   public void setNanoTime(long nanoTime) {
/* 450 */     this.nanoTime = nanoTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object writeReplace() {
/* 458 */     return new Log4jLogEvent.LogEventProxy(this, this.includeLocation);
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 462 */     throw new InvalidObjectException("Proxy required");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Log4jLogEvent createMemento() {
/* 472 */     return Log4jLogEvent.deserialize(Log4jLogEvent.serialize(this, this.includeLocation));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initializeBuilder(Log4jLogEvent.Builder builder) {
/* 480 */     builder.setContextData(this.contextData)
/* 481 */       .setContextStack(this.contextStack)
/* 482 */       .setEndOfBatch(this.endOfBatch)
/* 483 */       .setIncludeLocation(this.includeLocation)
/* 484 */       .setLevel(getLevel())
/* 485 */       .setLoggerFqcn(this.loggerFqcn)
/* 486 */       .setLoggerName(this.loggerName)
/* 487 */       .setMarker(this.marker)
/* 488 */       .setMessage(memento())
/* 489 */       .setNanoTime(this.nanoTime)
/* 490 */       .setSource(this.source)
/* 491 */       .setThreadId(this.threadId)
/* 492 */       .setThreadName(this.threadName)
/* 493 */       .setThreadPriority(this.threadPriority)
/* 494 */       .setThrown(getThrown())
/* 495 */       .setThrownProxy(this.thrownProxy)
/* 496 */       .setInstant((Instant)this.instant);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\impl\MutableLogEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */