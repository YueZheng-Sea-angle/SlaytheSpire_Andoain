/*     */ package org.apache.logging.log4j.core.async;
/*     */ 
/*     */ import com.lmax.disruptor.EventFactory;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.ThreadContext;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.impl.ContextDataFactory;
/*     */ import org.apache.logging.log4j.core.impl.Log4jLogEvent;
/*     */ import org.apache.logging.log4j.core.impl.MementoMessage;
/*     */ import org.apache.logging.log4j.core.impl.ThrowableProxy;
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
/*     */ 
/*     */ 
/*     */ public class RingBufferLogEvent
/*     */   implements LogEvent, ReusableMessage, CharSequence, ParameterVisitable
/*     */ {
/*  49 */   public static final Factory FACTORY = new Factory();
/*     */   
/*     */   private static final long serialVersionUID = 8462119088943934758L;
/*  52 */   private static final Message EMPTY = (Message)new SimpleMessage("");
/*     */   private boolean populated;
/*     */   private int threadPriority;
/*     */   private long threadId;
/*     */   
/*     */   private static class Factory implements EventFactory<RingBufferLogEvent> {
/*     */     private Factory() {}
/*     */     
/*     */     public RingBufferLogEvent newInstance() {
/*  61 */       return new RingBufferLogEvent();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   private final MutableInstant instant = new MutableInstant();
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
/*     */   private transient Throwable thrown;
/*     */   private ThrowableProxy thrownProxy;
/*  82 */   private StringMap contextData = ContextDataFactory.createContextData();
/*     */   
/*     */   private Marker marker;
/*     */   
/*     */   private String fqcn;
/*     */   
/*     */   private StackTraceElement location;
/*     */   
/*     */   private ThreadContext.ContextStack contextStack;
/*     */   
/*     */   private transient AsyncLogger asyncLogger;
/*     */   
/*     */   public void setValues(AsyncLogger anAsyncLogger, String aLoggerName, Marker aMarker, String theFqcn, Level aLevel, Message msg, Throwable aThrowable, StringMap mutableContextData, ThreadContext.ContextStack aContextStack, long threadId, String threadName, int threadPriority, StackTraceElement aLocation, Clock clock, NanoClock nanoClock) {
/*  95 */     this.threadPriority = threadPriority;
/*  96 */     this.threadId = threadId;
/*  97 */     this.level = aLevel;
/*  98 */     this.threadName = threadName;
/*  99 */     this.loggerName = aLoggerName;
/* 100 */     setMessage(msg);
/* 101 */     initTime(clock);
/* 102 */     this.nanoTime = nanoClock.nanoTime();
/* 103 */     this.thrown = aThrowable;
/* 104 */     this.thrownProxy = null;
/* 105 */     this.marker = aMarker;
/* 106 */     this.fqcn = theFqcn;
/* 107 */     this.location = aLocation;
/* 108 */     this.contextData = mutableContextData;
/* 109 */     this.contextStack = aContextStack;
/* 110 */     this.asyncLogger = anAsyncLogger;
/* 111 */     this.populated = true;
/*     */   }
/*     */   
/*     */   private void initTime(Clock clock) {
/* 115 */     if (this.message instanceof TimestampMessage) {
/* 116 */       this.instant.initFromEpochMilli(((TimestampMessage)this.message).getTimestamp(), 0);
/*     */     } else {
/* 118 */       this.instant.initFrom(clock);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public LogEvent toImmutable() {
/* 124 */     return createMemento();
/*     */   }
/*     */   
/*     */   private void setMessage(Message msg) {
/* 128 */     if (msg instanceof ReusableMessage) {
/* 129 */       ReusableMessage reusable = (ReusableMessage)msg;
/* 130 */       reusable.formatTo(getMessageTextForWriting());
/* 131 */       this.messageFormat = reusable.getFormat();
/* 132 */       this.parameters = reusable.swapParameters((this.parameters == null) ? new Object[10] : this.parameters);
/* 133 */       this.parameterCount = reusable.getParameterCount();
/*     */     } else {
/* 135 */       this.message = InternalAsyncUtil.makeMessageImmutable(msg);
/*     */     } 
/*     */   }
/*     */   
/*     */   private StringBuilder getMessageTextForWriting() {
/* 140 */     if (this.messageText == null)
/*     */     {
/*     */       
/* 143 */       this.messageText = new StringBuilder(Constants.INITIAL_REUSABLE_MESSAGE_SIZE);
/*     */     }
/* 145 */     this.messageText.setLength(0);
/* 146 */     return this.messageText;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(boolean endOfBatch) {
/* 155 */     this.endOfBatch = endOfBatch;
/* 156 */     this.asyncLogger.actualAsyncLog(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPopulated() {
/* 163 */     return this.populated;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEndOfBatch() {
/* 173 */     return this.endOfBatch;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEndOfBatch(boolean endOfBatch) {
/* 178 */     this.endOfBatch = endOfBatch;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isIncludeLocation() {
/* 183 */     return this.includeLocation;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIncludeLocation(boolean includeLocation) {
/* 188 */     this.includeLocation = includeLocation;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLoggerName() {
/* 193 */     return this.loggerName;
/*     */   }
/*     */ 
/*     */   
/*     */   public Marker getMarker() {
/* 198 */     return this.marker;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLoggerFqcn() {
/* 203 */     return this.fqcn;
/*     */   }
/*     */ 
/*     */   
/*     */   public Level getLevel() {
/* 208 */     if (this.level == null) {
/* 209 */       this.level = Level.OFF;
/*     */     }
/* 211 */     return this.level;
/*     */   }
/*     */ 
/*     */   
/*     */   public Message getMessage() {
/* 216 */     if (this.message == null) {
/* 217 */       return (this.messageText == null) ? EMPTY : (Message)this;
/*     */     }
/* 219 */     return this.message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedMessage() {
/* 227 */     return (this.messageText != null) ? this.messageText
/* 228 */       .toString() : ((this.message == null) ? null : this.message
/* 229 */       .getFormattedMessage());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormat() {
/* 237 */     return this.messageFormat;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getParameters() {
/* 245 */     return (this.parameters == null) ? null : Arrays.<Object>copyOf(this.parameters, this.parameterCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getThrowable() {
/* 253 */     return getThrown();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void formatTo(StringBuilder buffer) {
/* 261 */     buffer.append(this.messageText);
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
/* 272 */     Object[] result = this.parameters;
/* 273 */     this.parameters = emptyReplacement;
/* 274 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getParameterCount() {
/* 282 */     return this.parameterCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public <S> void forEachParameter(ParameterConsumer<S> action, S state) {
/* 287 */     if (this.parameters != null) {
/* 288 */       for (short i = 0; i < this.parameterCount; i = (short)(i + 1)) {
/* 289 */         action.accept(this.parameters[i], i, state);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Message memento() {
/* 296 */     if (this.message == null) {
/* 297 */       this.message = (Message)new MementoMessage(String.valueOf(this.messageText), this.messageFormat, getParameters());
/*     */     }
/* 299 */     return this.message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 306 */     return this.messageText.length();
/*     */   }
/*     */ 
/*     */   
/*     */   public char charAt(int index) {
/* 311 */     return this.messageText.charAt(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public CharSequence subSequence(int start, int end) {
/* 316 */     return this.messageText.subSequence(start, end);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getThrown() {
/* 322 */     if (this.thrown == null && 
/* 323 */       this.thrownProxy != null) {
/* 324 */       this.thrown = this.thrownProxy.getThrowable();
/*     */     }
/*     */     
/* 327 */     return this.thrown;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ThrowableProxy getThrownProxy() {
/* 333 */     if (this.thrownProxy == null && 
/* 334 */       this.thrown != null) {
/* 335 */       this.thrownProxy = new ThrowableProxy(this.thrown);
/*     */     }
/*     */     
/* 338 */     return this.thrownProxy;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ReadOnlyStringMap getContextData() {
/* 344 */     return (ReadOnlyStringMap)this.contextData;
/*     */   }
/*     */   
/*     */   void setContextData(StringMap contextData) {
/* 348 */     this.contextData = contextData;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> getContextMap() {
/* 354 */     return this.contextData.toMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public ThreadContext.ContextStack getContextStack() {
/* 359 */     return this.contextStack;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getThreadId() {
/* 364 */     return this.threadId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getThreadName() {
/* 369 */     return this.threadName;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getThreadPriority() {
/* 374 */     return this.threadPriority;
/*     */   }
/*     */ 
/*     */   
/*     */   public StackTraceElement getSource() {
/* 379 */     return this.location;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getTimeMillis() {
/* 384 */     return (this.message instanceof TimestampMessage) ? ((TimestampMessage)this.message).getTimestamp() : this.instant.getEpochMillisecond();
/*     */   }
/*     */ 
/*     */   
/*     */   public Instant getInstant() {
/* 389 */     return (Instant)this.instant;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getNanoTime() {
/* 394 */     return this.nanoTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 401 */     this.populated = false;
/*     */     
/* 403 */     this.asyncLogger = null;
/* 404 */     this.loggerName = null;
/* 405 */     this.marker = null;
/* 406 */     this.fqcn = null;
/* 407 */     this.level = null;
/* 408 */     this.message = null;
/* 409 */     this.messageFormat = null;
/* 410 */     this.thrown = null;
/* 411 */     this.thrownProxy = null;
/* 412 */     this.contextStack = null;
/* 413 */     this.location = null;
/* 414 */     if (this.contextData != null) {
/* 415 */       if (this.contextData.isFrozen()) {
/* 416 */         this.contextData = null;
/*     */       } else {
/* 418 */         this.contextData.clear();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 423 */     if (Constants.ENABLE_THREADLOCALS) {
/* 424 */       StringBuilders.trimToMaxSize(this.messageText, Constants.MAX_REUSABLE_MESSAGE_SIZE);
/*     */       
/* 426 */       if (this.parameters != null) {
/* 427 */         Arrays.fill(this.parameters, (Object)null);
/*     */       
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 433 */       this.messageText = null;
/* 434 */       this.parameters = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 439 */     getThrownProxy();
/* 440 */     out.defaultWriteObject();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LogEvent createMemento() {
/* 449 */     return (LogEvent)(new Log4jLogEvent.Builder(this)).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initializeBuilder(Log4jLogEvent.Builder builder) {
/* 458 */     builder.setContextData(this.contextData)
/* 459 */       .setContextStack(this.contextStack)
/* 460 */       .setEndOfBatch(this.endOfBatch)
/* 461 */       .setIncludeLocation(this.includeLocation)
/* 462 */       .setLevel(getLevel())
/* 463 */       .setLoggerFqcn(this.fqcn)
/* 464 */       .setLoggerName(this.loggerName)
/* 465 */       .setMarker(this.marker)
/* 466 */       .setMessage(memento())
/* 467 */       .setNanoTime(this.nanoTime)
/* 468 */       .setSource(this.location)
/* 469 */       .setThreadId(this.threadId)
/* 470 */       .setThreadName(this.threadName)
/* 471 */       .setThreadPriority(this.threadPriority)
/* 472 */       .setThrown(getThrown())
/* 473 */       .setThrownProxy(this.thrownProxy)
/* 474 */       .setInstant((Instant)this.instant);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\async\RingBufferLogEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */