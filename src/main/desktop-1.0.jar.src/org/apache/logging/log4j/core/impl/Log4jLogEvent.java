/*      */ package org.apache.logging.log4j.core.impl;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InvalidObjectException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.rmi.MarshalledObject;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import org.apache.logging.log4j.Level;
/*      */ import org.apache.logging.log4j.Marker;
/*      */ import org.apache.logging.log4j.ThreadContext;
/*      */ import org.apache.logging.log4j.core.ContextDataInjector;
/*      */ import org.apache.logging.log4j.core.LogEvent;
/*      */ import org.apache.logging.log4j.core.async.RingBufferLogEvent;
/*      */ import org.apache.logging.log4j.core.config.Property;
/*      */ import org.apache.logging.log4j.core.time.Instant;
/*      */ import org.apache.logging.log4j.core.time.MutableInstant;
/*      */ import org.apache.logging.log4j.core.util.Clock;
/*      */ import org.apache.logging.log4j.core.util.ClockFactory;
/*      */ import org.apache.logging.log4j.core.util.DummyNanoClock;
/*      */ import org.apache.logging.log4j.core.util.NanoClock;
/*      */ import org.apache.logging.log4j.message.LoggerNameAwareMessage;
/*      */ import org.apache.logging.log4j.message.Message;
/*      */ import org.apache.logging.log4j.message.ReusableMessage;
/*      */ import org.apache.logging.log4j.message.SimpleMessage;
/*      */ import org.apache.logging.log4j.message.TimestampMessage;
/*      */ import org.apache.logging.log4j.status.StatusLogger;
/*      */ import org.apache.logging.log4j.util.ReadOnlyStringMap;
/*      */ import org.apache.logging.log4j.util.StackLocatorUtil;
/*      */ import org.apache.logging.log4j.util.StringMap;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Log4jLogEvent
/*      */   implements LogEvent
/*      */ {
/*      */   private static final long serialVersionUID = -8393305700508709443L;
/*   56 */   private static final Clock CLOCK = ClockFactory.getClock();
/*   57 */   private static volatile NanoClock nanoClock = (NanoClock)new DummyNanoClock();
/*   58 */   private static final ContextDataInjector CONTEXT_DATA_INJECTOR = ContextDataInjectorFactory.createInjector();
/*      */   
/*      */   private final String loggerFqcn;
/*      */   private final Marker marker;
/*      */   private final Level level;
/*      */   private final String loggerName;
/*      */   private Message message;
/*   65 */   private final MutableInstant instant = new MutableInstant();
/*      */   
/*      */   private final transient Throwable thrown;
/*      */   private ThrowableProxy thrownProxy;
/*      */   private final StringMap contextData;
/*      */   private final ThreadContext.ContextStack contextStack;
/*      */   private long threadId;
/*      */   private String threadName;
/*      */   private int threadPriority;
/*      */   private StackTraceElement source;
/*      */   private boolean includeLocation;
/*      */   private boolean endOfBatch = false;
/*      */   private final transient long nanoTime;
/*      */   
/*      */   public static class Builder
/*      */     implements org.apache.logging.log4j.core.util.Builder<LogEvent>
/*      */   {
/*      */     private String loggerFqcn;
/*      */     private Marker marker;
/*      */     private Level level;
/*      */     private String loggerName;
/*      */     private Message message;
/*      */     private Throwable thrown;
/*   88 */     private final MutableInstant instant = new MutableInstant();
/*      */     private ThrowableProxy thrownProxy;
/*   90 */     private StringMap contextData = Log4jLogEvent.createContextData((List)null);
/*   91 */     private ThreadContext.ContextStack contextStack = ThreadContext.getImmutableStack();
/*      */     
/*      */     private long threadId;
/*      */     
/*      */     private String threadName;
/*      */     
/*      */     private int threadPriority;
/*      */     private StackTraceElement source;
/*      */     private boolean includeLocation;
/*      */     private boolean endOfBatch = false;
/*      */     private long nanoTime;
/*      */     
/*      */     public Builder(LogEvent other) {
/*  104 */       Objects.requireNonNull(other);
/*  105 */       if (other instanceof RingBufferLogEvent) {
/*  106 */         ((RingBufferLogEvent)other).initializeBuilder(this);
/*      */         return;
/*      */       } 
/*  109 */       if (other instanceof MutableLogEvent) {
/*  110 */         ((MutableLogEvent)other).initializeBuilder(this);
/*      */         return;
/*      */       } 
/*  113 */       this.loggerFqcn = other.getLoggerFqcn();
/*  114 */       this.marker = other.getMarker();
/*  115 */       this.level = other.getLevel();
/*  116 */       this.loggerName = other.getLoggerName();
/*  117 */       this.message = other.getMessage();
/*  118 */       this.instant.initFrom(other.getInstant());
/*  119 */       this.thrown = other.getThrown();
/*  120 */       this.contextStack = other.getContextStack();
/*  121 */       this.includeLocation = other.isIncludeLocation();
/*  122 */       this.endOfBatch = other.isEndOfBatch();
/*  123 */       this.nanoTime = other.getNanoTime();
/*      */ 
/*      */       
/*  126 */       if (other instanceof Log4jLogEvent) {
/*  127 */         Log4jLogEvent evt = (Log4jLogEvent)other;
/*  128 */         this.contextData = evt.contextData;
/*  129 */         this.thrownProxy = evt.thrownProxy;
/*  130 */         this.source = evt.source;
/*  131 */         this.threadId = evt.threadId;
/*  132 */         this.threadName = evt.threadName;
/*  133 */         this.threadPriority = evt.threadPriority;
/*      */       } else {
/*  135 */         if (other.getContextData() instanceof StringMap) {
/*  136 */           this.contextData = (StringMap)other.getContextData();
/*      */         } else {
/*  138 */           if (this.contextData.isFrozen()) {
/*  139 */             this.contextData = ContextDataFactory.createContextData();
/*      */           } else {
/*  141 */             this.contextData.clear();
/*      */           } 
/*  143 */           this.contextData.putAll(other.getContextData());
/*      */         } 
/*      */         
/*  146 */         this.thrownProxy = other.getThrownProxy();
/*  147 */         this.source = other.getSource();
/*  148 */         this.threadId = other.getThreadId();
/*  149 */         this.threadName = other.getThreadName();
/*  150 */         this.threadPriority = other.getThreadPriority();
/*      */       } 
/*      */     }
/*      */     
/*      */     public Builder setLevel(Level level) {
/*  155 */       this.level = level;
/*  156 */       return this;
/*      */     }
/*      */     
/*      */     public Builder setLoggerFqcn(String loggerFqcn) {
/*  160 */       this.loggerFqcn = loggerFqcn;
/*  161 */       return this;
/*      */     }
/*      */     
/*      */     public Builder setLoggerName(String loggerName) {
/*  165 */       this.loggerName = loggerName;
/*  166 */       return this;
/*      */     }
/*      */     
/*      */     public Builder setMarker(Marker marker) {
/*  170 */       this.marker = marker;
/*  171 */       return this;
/*      */     }
/*      */     
/*      */     public Builder setMessage(Message message) {
/*  175 */       this.message = message;
/*  176 */       return this;
/*      */     }
/*      */     
/*      */     public Builder setThrown(Throwable thrown) {
/*  180 */       this.thrown = thrown;
/*  181 */       return this;
/*      */     }
/*      */     
/*      */     public Builder setTimeMillis(long timeMillis) {
/*  185 */       this.instant.initFromEpochMilli(timeMillis, 0);
/*  186 */       return this;
/*      */     }
/*      */     
/*      */     public Builder setInstant(Instant instant) {
/*  190 */       this.instant.initFrom(instant);
/*  191 */       return this;
/*      */     }
/*      */     
/*      */     public Builder setThrownProxy(ThrowableProxy thrownProxy) {
/*  195 */       this.thrownProxy = thrownProxy;
/*  196 */       return this;
/*      */     }
/*      */     
/*      */     @Deprecated
/*      */     public Builder setContextMap(Map<String, String> contextMap) {
/*  201 */       this.contextData = ContextDataFactory.createContextData();
/*  202 */       if (contextMap != null) {
/*  203 */         for (Map.Entry<String, String> entry : contextMap.entrySet()) {
/*  204 */           this.contextData.putValue(entry.getKey(), entry.getValue());
/*      */         }
/*      */       }
/*  207 */       return this;
/*      */     }
/*      */     
/*      */     public Builder setContextData(StringMap contextData) {
/*  211 */       this.contextData = contextData;
/*  212 */       return this;
/*      */     }
/*      */     
/*      */     public Builder setContextStack(ThreadContext.ContextStack contextStack) {
/*  216 */       this.contextStack = contextStack;
/*  217 */       return this;
/*      */     }
/*      */     
/*      */     public Builder setThreadId(long threadId) {
/*  221 */       this.threadId = threadId;
/*  222 */       return this;
/*      */     }
/*      */     
/*      */     public Builder setThreadName(String threadName) {
/*  226 */       this.threadName = threadName;
/*  227 */       return this;
/*      */     }
/*      */     
/*      */     public Builder setThreadPriority(int threadPriority) {
/*  231 */       this.threadPriority = threadPriority;
/*  232 */       return this;
/*      */     }
/*      */     
/*      */     public Builder setSource(StackTraceElement source) {
/*  236 */       this.source = source;
/*  237 */       return this;
/*      */     }
/*      */     
/*      */     public Builder setIncludeLocation(boolean includeLocation) {
/*  241 */       this.includeLocation = includeLocation;
/*  242 */       return this;
/*      */     }
/*      */     
/*      */     public Builder setEndOfBatch(boolean endOfBatch) {
/*  246 */       this.endOfBatch = endOfBatch;
/*  247 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder setNanoTime(long nanoTime) {
/*  257 */       this.nanoTime = nanoTime;
/*  258 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public Log4jLogEvent build() {
/*  263 */       initTimeFields();
/*      */ 
/*      */       
/*  266 */       Log4jLogEvent result = new Log4jLogEvent(this.loggerName, this.marker, this.loggerFqcn, this.level, this.message, this.thrown, this.thrownProxy, this.contextData, this.contextStack, this.threadId, this.threadName, this.threadPriority, this.source, this.instant.getEpochMillisecond(), this.instant.getNanoOfMillisecond(), this.nanoTime);
/*  267 */       result.setIncludeLocation(this.includeLocation);
/*  268 */       result.setEndOfBatch(this.endOfBatch);
/*  269 */       return result;
/*      */     }
/*      */     
/*      */     private void initTimeFields() {
/*  273 */       if (this.instant.getEpochMillisecond() == 0L) {
/*  274 */         this.instant.initFrom(Log4jLogEvent.CLOCK);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public Builder() {}
/*      */   }
/*      */ 
/*      */   
/*      */   public static Builder newBuilder() {
/*  284 */     return new Builder();
/*      */   }
/*      */   
/*      */   public Log4jLogEvent() {
/*  288 */     this("", null, "", null, null, (Throwable)null, null, null, null, 0L, null, 0, null, CLOCK, nanoClock
/*  289 */         .nanoTime());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Log4jLogEvent(long timestamp) {
/*  298 */     this("", null, "", null, null, (Throwable)null, null, null, null, 0L, null, 0, null, timestamp, 0, nanoClock
/*  299 */         .nanoTime());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Log4jLogEvent(String loggerName, Marker marker, String loggerFQCN, Level level, Message message, Throwable t) {
/*  315 */     this(loggerName, marker, loggerFQCN, level, message, null, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Log4jLogEvent(String loggerName, Marker marker, String loggerFQCN, Level level, Message message, List<Property> properties, Throwable t) {
/*  331 */     this(loggerName, marker, loggerFQCN, level, message, t, null, createContextData(properties), 
/*  332 */         (ThreadContext.getDepth() == 0) ? null : ThreadContext.cloneStack(), 0L, null, 0, null, CLOCK, nanoClock
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  338 */         .nanoTime());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Log4jLogEvent(String loggerName, Marker marker, String loggerFQCN, StackTraceElement source, Level level, Message message, List<Property> properties, Throwable t) {
/*  355 */     this(loggerName, marker, loggerFQCN, level, message, t, null, createContextData(properties), 
/*  356 */         (ThreadContext.getDepth() == 0) ? null : ThreadContext.cloneStack(), 0L, null, 0, source, CLOCK, nanoClock
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  362 */         .nanoTime());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Log4jLogEvent(String loggerName, Marker marker, String loggerFQCN, Level level, Message message, Throwable t, Map<String, String> mdc, ThreadContext.ContextStack ndc, String threadName, StackTraceElement location, long timestampMillis) {
/*  385 */     this(loggerName, marker, loggerFQCN, level, message, t, null, createContextData(mdc), ndc, 0L, threadName, 0, location, timestampMillis, 0, nanoClock
/*  386 */         .nanoTime());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static Log4jLogEvent createEvent(String loggerName, Marker marker, String loggerFQCN, Level level, Message message, Throwable thrown, ThrowableProxy thrownProxy, Map<String, String> mdc, ThreadContext.ContextStack ndc, String threadName, StackTraceElement location, long timestamp) {
/*  414 */     Log4jLogEvent result = new Log4jLogEvent(loggerName, marker, loggerFQCN, level, message, thrown, thrownProxy, createContextData(mdc), ndc, 0L, threadName, 0, location, timestamp, 0, nanoClock.nanoTime());
/*  415 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Log4jLogEvent(String loggerName, Marker marker, String loggerFQCN, Level level, Message message, Throwable thrown, ThrowableProxy thrownProxy, StringMap contextData, ThreadContext.ContextStack contextStack, long threadId, String threadName, int threadPriority, StackTraceElement source, long timestampMillis, int nanoOfMillisecond, long nanoTime) {
/*  443 */     this(loggerName, marker, loggerFQCN, level, message, thrown, thrownProxy, contextData, contextStack, threadId, threadName, threadPriority, source, nanoTime);
/*      */     
/*  445 */     long millis = (message instanceof TimestampMessage) ? ((TimestampMessage)message).getTimestamp() : timestampMillis;
/*      */     
/*  447 */     this.instant.initFromEpochMilli(millis, nanoOfMillisecond);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Log4jLogEvent(String loggerName, Marker marker, String loggerFQCN, Level level, Message message, Throwable thrown, ThrowableProxy thrownProxy, StringMap contextData, ThreadContext.ContextStack contextStack, long threadId, String threadName, int threadPriority, StackTraceElement source, Clock clock, long nanoTime) {
/*  454 */     this(loggerName, marker, loggerFQCN, level, message, thrown, thrownProxy, contextData, contextStack, threadId, threadName, threadPriority, source, nanoTime);
/*  455 */     if (message instanceof TimestampMessage) {
/*  456 */       this.instant.initFromEpochMilli(((TimestampMessage)message).getTimestamp(), 0);
/*      */     } else {
/*  458 */       this.instant.initFrom(clock);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Log4jLogEvent(String loggerName, Marker marker, String loggerFQCN, Level level, Message message, Throwable thrown, ThrowableProxy thrownProxy, StringMap contextData, ThreadContext.ContextStack contextStack, long threadId, String threadName, int threadPriority, StackTraceElement source, long nanoTime) {
/*  466 */     this.loggerName = loggerName;
/*  467 */     this.marker = marker;
/*  468 */     this.loggerFqcn = loggerFQCN;
/*  469 */     this.level = (level == null) ? Level.OFF : level;
/*  470 */     this.message = message;
/*  471 */     this.thrown = thrown;
/*  472 */     this.thrownProxy = thrownProxy;
/*  473 */     this.contextData = (contextData == null) ? ContextDataFactory.createContextData() : contextData;
/*  474 */     this.contextStack = (contextStack == null) ? (ThreadContext.ContextStack)ThreadContext.EMPTY_STACK : contextStack;
/*  475 */     this.threadId = threadId;
/*  476 */     this.threadName = threadName;
/*  477 */     this.threadPriority = threadPriority;
/*  478 */     this.source = source;
/*  479 */     if (message instanceof LoggerNameAwareMessage) {
/*  480 */       ((LoggerNameAwareMessage)message).setLoggerName(loggerName);
/*      */     }
/*  482 */     this.nanoTime = nanoTime;
/*      */   }
/*      */   
/*      */   private static StringMap createContextData(Map<String, String> contextMap) {
/*  486 */     StringMap result = ContextDataFactory.createContextData();
/*  487 */     if (contextMap != null) {
/*  488 */       for (Map.Entry<String, String> entry : contextMap.entrySet()) {
/*  489 */         result.putValue(entry.getKey(), entry.getValue());
/*      */       }
/*      */     }
/*  492 */     return result;
/*      */   }
/*      */   
/*      */   private static StringMap createContextData(List<Property> properties) {
/*  496 */     StringMap reusable = ContextDataFactory.createContextData();
/*  497 */     return CONTEXT_DATA_INJECTOR.injectContextData(properties, reusable);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static NanoClock getNanoClock() {
/*  505 */     return nanoClock;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setNanoClock(NanoClock nanoClock) {
/*  517 */     Log4jLogEvent.nanoClock = Objects.<NanoClock>requireNonNull(nanoClock, "NanoClock must be non-null");
/*  518 */     StatusLogger.getLogger().trace("Using {} for nanosecond timestamps.", nanoClock.getClass().getSimpleName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Builder asBuilder() {
/*  526 */     return new Builder(this);
/*      */   }
/*      */ 
/*      */   
/*      */   public Log4jLogEvent toImmutable() {
/*  531 */     if (getMessage() instanceof ReusableMessage) {
/*  532 */       makeMessageImmutable();
/*      */     }
/*  534 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Level getLevel() {
/*  543 */     return this.level;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getLoggerName() {
/*  552 */     return this.loggerName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Message getMessage() {
/*  561 */     return this.message;
/*      */   }
/*      */   
/*      */   public void makeMessageImmutable() {
/*  565 */     this.message = new MementoMessage(this.message.getFormattedMessage(), this.message.getFormat(), this.message.getParameters());
/*      */   }
/*      */ 
/*      */   
/*      */   public long getThreadId() {
/*  570 */     if (this.threadId == 0L) {
/*  571 */       this.threadId = Thread.currentThread().getId();
/*      */     }
/*  573 */     return this.threadId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getThreadName() {
/*  582 */     if (this.threadName == null) {
/*  583 */       this.threadName = Thread.currentThread().getName();
/*      */     }
/*  585 */     return this.threadName;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getThreadPriority() {
/*  590 */     if (this.threadPriority == 0) {
/*  591 */       this.threadPriority = Thread.currentThread().getPriority();
/*      */     }
/*  593 */     return this.threadPriority;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getTimeMillis() {
/*  601 */     return this.instant.getEpochMillisecond();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Instant getInstant() {
/*  610 */     return (Instant)this.instant;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Throwable getThrown() {
/*  619 */     return this.thrown;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ThrowableProxy getThrownProxy() {
/*  628 */     if (this.thrownProxy == null && this.thrown != null) {
/*  629 */       this.thrownProxy = new ThrowableProxy(this.thrown);
/*      */     }
/*  631 */     return this.thrownProxy;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Marker getMarker() {
/*  641 */     return this.marker;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getLoggerFqcn() {
/*  650 */     return this.loggerFqcn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ReadOnlyStringMap getContextData() {
/*  660 */     return (ReadOnlyStringMap)this.contextData;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<String, String> getContextMap() {
/*  668 */     return this.contextData.toMap();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ThreadContext.ContextStack getContextStack() {
/*  677 */     return this.contextStack;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StackTraceElement getSource() {
/*  687 */     if (this.source != null) {
/*  688 */       return this.source;
/*      */     }
/*  690 */     if (this.loggerFqcn == null || !this.includeLocation) {
/*  691 */       return null;
/*      */     }
/*  693 */     this.source = StackLocatorUtil.calcLocation(this.loggerFqcn);
/*  694 */     return this.source;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isIncludeLocation() {
/*  699 */     return this.includeLocation;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setIncludeLocation(boolean includeLocation) {
/*  704 */     this.includeLocation = includeLocation;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isEndOfBatch() {
/*  709 */     return this.endOfBatch;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setEndOfBatch(boolean endOfBatch) {
/*  714 */     this.endOfBatch = endOfBatch;
/*      */   }
/*      */ 
/*      */   
/*      */   public long getNanoTime() {
/*  719 */     return this.nanoTime;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object writeReplace() {
/*  727 */     getThrownProxy();
/*  728 */     return new LogEventProxy(this, this.includeLocation);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Serializable serialize(LogEvent event, boolean includeLocation) {
/*  741 */     if (event instanceof Log4jLogEvent) {
/*  742 */       event.getThrownProxy();
/*  743 */       return new LogEventProxy((Log4jLogEvent)event, includeLocation);
/*      */     } 
/*  745 */     return new LogEventProxy(event, includeLocation);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Serializable serialize(Log4jLogEvent event, boolean includeLocation) {
/*  758 */     event.getThrownProxy();
/*  759 */     return new LogEventProxy(event, includeLocation);
/*      */   }
/*      */   
/*      */   public static boolean canDeserialize(Serializable event) {
/*  763 */     return event instanceof LogEventProxy;
/*      */   }
/*      */   
/*      */   public static Log4jLogEvent deserialize(Serializable event) {
/*  767 */     Objects.requireNonNull(event, "Event cannot be null");
/*  768 */     if (event instanceof LogEventProxy) {
/*  769 */       LogEventProxy proxy = (LogEventProxy)event;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  774 */       Log4jLogEvent result = new Log4jLogEvent(proxy.loggerName, proxy.marker, proxy.loggerFQCN, proxy.level, proxy.message, proxy.thrown, proxy.thrownProxy, proxy.contextData, proxy.contextStack, proxy.threadId, proxy.threadName, proxy.threadPriority, proxy.source, proxy.timeMillis, proxy.nanoOfMillisecond, proxy.nanoTime);
/*  775 */       result.setEndOfBatch(proxy.isEndOfBatch);
/*  776 */       result.setIncludeLocation(proxy.isLocationRequired);
/*  777 */       return result;
/*      */     } 
/*  779 */     throw new IllegalArgumentException("Event is not a serialized LogEvent: " + event.toString());
/*      */   }
/*      */   
/*      */   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/*  783 */     throw new InvalidObjectException("Proxy required");
/*      */   }
/*      */   
/*      */   public static LogEvent createMemento(LogEvent logEvent) {
/*  787 */     return (new Builder(logEvent)).build();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Log4jLogEvent createMemento(LogEvent event, boolean includeLocation) {
/*  796 */     return deserialize(serialize(event, includeLocation));
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/*  801 */     StringBuilder sb = new StringBuilder();
/*  802 */     String n = this.loggerName.isEmpty() ? "root" : this.loggerName;
/*  803 */     sb.append("Logger=").append(n);
/*  804 */     sb.append(" Level=").append(this.level.name());
/*  805 */     sb.append(" Message=").append((this.message == null) ? null : this.message.getFormattedMessage());
/*  806 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean equals(Object o) {
/*  811 */     if (this == o) {
/*  812 */       return true;
/*      */     }
/*  814 */     if (o == null || getClass() != o.getClass()) {
/*  815 */       return false;
/*      */     }
/*      */     
/*  818 */     Log4jLogEvent that = (Log4jLogEvent)o;
/*      */     
/*  820 */     if (this.endOfBatch != that.endOfBatch) {
/*  821 */       return false;
/*      */     }
/*  823 */     if (this.includeLocation != that.includeLocation) {
/*  824 */       return false;
/*      */     }
/*  826 */     if (!this.instant.equals(that.instant)) {
/*  827 */       return false;
/*      */     }
/*  829 */     if (this.nanoTime != that.nanoTime) {
/*  830 */       return false;
/*      */     }
/*  832 */     if ((this.loggerFqcn != null) ? !this.loggerFqcn.equals(that.loggerFqcn) : (that.loggerFqcn != null)) {
/*  833 */       return false;
/*      */     }
/*  835 */     if ((this.level != null) ? !this.level.equals(that.level) : (that.level != null)) {
/*  836 */       return false;
/*      */     }
/*  838 */     if ((this.source != null) ? !this.source.equals(that.source) : (that.source != null)) {
/*  839 */       return false;
/*      */     }
/*  841 */     if ((this.marker != null) ? !this.marker.equals(that.marker) : (that.marker != null)) {
/*  842 */       return false;
/*      */     }
/*  844 */     if ((this.contextData != null) ? !this.contextData.equals(that.contextData) : (that.contextData != null)) {
/*  845 */       return false;
/*      */     }
/*  847 */     if (!this.message.equals(that.message)) {
/*  848 */       return false;
/*      */     }
/*  850 */     if (!this.loggerName.equals(that.loggerName)) {
/*  851 */       return false;
/*      */     }
/*  853 */     if ((this.contextStack != null) ? !this.contextStack.equals(that.contextStack) : (that.contextStack != null)) {
/*  854 */       return false;
/*      */     }
/*  856 */     if (this.threadId != that.threadId) {
/*  857 */       return false;
/*      */     }
/*  859 */     if ((this.threadName != null) ? !this.threadName.equals(that.threadName) : (that.threadName != null)) {
/*  860 */       return false;
/*      */     }
/*  862 */     if (this.threadPriority != that.threadPriority) {
/*  863 */       return false;
/*      */     }
/*  865 */     if ((this.thrown != null) ? !this.thrown.equals(that.thrown) : (that.thrown != null)) {
/*  866 */       return false;
/*      */     }
/*  868 */     if ((this.thrownProxy != null) ? !this.thrownProxy.equals(that.thrownProxy) : (that.thrownProxy != null)) {
/*  869 */       return false;
/*      */     }
/*      */     
/*  872 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  878 */     int result = (this.loggerFqcn != null) ? this.loggerFqcn.hashCode() : 0;
/*  879 */     result = 31 * result + ((this.marker != null) ? this.marker.hashCode() : 0);
/*  880 */     result = 31 * result + ((this.level != null) ? this.level.hashCode() : 0);
/*  881 */     result = 31 * result + this.loggerName.hashCode();
/*  882 */     result = 31 * result + this.message.hashCode();
/*  883 */     result = 31 * result + this.instant.hashCode();
/*  884 */     result = 31 * result + (int)(this.nanoTime ^ this.nanoTime >>> 32L);
/*  885 */     result = 31 * result + ((this.thrown != null) ? this.thrown.hashCode() : 0);
/*  886 */     result = 31 * result + ((this.thrownProxy != null) ? this.thrownProxy.hashCode() : 0);
/*  887 */     result = 31 * result + ((this.contextData != null) ? this.contextData.hashCode() : 0);
/*  888 */     result = 31 * result + ((this.contextStack != null) ? this.contextStack.hashCode() : 0);
/*  889 */     result = 31 * result + (int)(this.threadId ^ this.threadId >>> 32L);
/*  890 */     result = 31 * result + ((this.threadName != null) ? this.threadName.hashCode() : 0);
/*  891 */     result = 31 * result + (this.threadPriority ^ this.threadPriority >>> 32);
/*  892 */     result = 31 * result + ((this.source != null) ? this.source.hashCode() : 0);
/*  893 */     result = 31 * result + (this.includeLocation ? 1 : 0);
/*  894 */     result = 31 * result + (this.endOfBatch ? 1 : 0);
/*      */     
/*  896 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   static class LogEventProxy
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = -8634075037355293699L;
/*      */     
/*      */     private final String loggerFQCN;
/*      */     
/*      */     private final Marker marker;
/*      */     
/*      */     private final Level level;
/*      */     
/*      */     private final String loggerName;
/*      */     
/*      */     private final transient Message message;
/*      */     
/*      */     private MarshalledObject<Message> marshalledMessage;
/*      */     
/*      */     private String messageString;
/*      */     
/*      */     private final long timeMillis;
/*      */     
/*      */     private final int nanoOfMillisecond;
/*      */     private final transient Throwable thrown;
/*      */     private final ThrowableProxy thrownProxy;
/*      */     private final StringMap contextData;
/*      */     private final ThreadContext.ContextStack contextStack;
/*      */     private final long threadId;
/*      */     private final String threadName;
/*      */     private final int threadPriority;
/*      */     private final StackTraceElement source;
/*      */     private final boolean isLocationRequired;
/*      */     private final boolean isEndOfBatch;
/*      */     private final transient long nanoTime;
/*      */     
/*      */     public LogEventProxy(Log4jLogEvent event, boolean includeLocation) {
/*  935 */       this.loggerFQCN = event.loggerFqcn;
/*  936 */       this.marker = event.marker;
/*  937 */       this.level = event.level;
/*  938 */       this.loggerName = event.loggerName;
/*  939 */       this
/*      */         
/*  941 */         .message = (event.message instanceof ReusableMessage) ? memento((ReusableMessage)event.message) : event.message;
/*  942 */       this.timeMillis = event.instant.getEpochMillisecond();
/*  943 */       this.nanoOfMillisecond = event.instant.getNanoOfMillisecond();
/*  944 */       this.thrown = event.thrown;
/*  945 */       this.thrownProxy = event.thrownProxy;
/*  946 */       this.contextData = event.contextData;
/*  947 */       this.contextStack = event.contextStack;
/*  948 */       this.source = includeLocation ? event.getSource() : null;
/*  949 */       this.threadId = event.getThreadId();
/*  950 */       this.threadName = event.getThreadName();
/*  951 */       this.threadPriority = event.getThreadPriority();
/*  952 */       this.isLocationRequired = includeLocation;
/*  953 */       this.isEndOfBatch = event.endOfBatch;
/*  954 */       this.nanoTime = event.nanoTime;
/*      */     }
/*      */     
/*      */     public LogEventProxy(LogEvent event, boolean includeLocation) {
/*  958 */       this.loggerFQCN = event.getLoggerFqcn();
/*  959 */       this.marker = event.getMarker();
/*  960 */       this.level = event.getLevel();
/*  961 */       this.loggerName = event.getLoggerName();
/*      */       
/*  963 */       Message temp = event.getMessage();
/*  964 */       this
/*  965 */         .message = (temp instanceof ReusableMessage) ? memento((ReusableMessage)temp) : temp;
/*      */       
/*  967 */       this.timeMillis = event.getInstant().getEpochMillisecond();
/*  968 */       this.nanoOfMillisecond = event.getInstant().getNanoOfMillisecond();
/*  969 */       this.thrown = event.getThrown();
/*  970 */       this.thrownProxy = event.getThrownProxy();
/*  971 */       this.contextData = memento(event.getContextData());
/*  972 */       this.contextStack = event.getContextStack();
/*  973 */       this.source = includeLocation ? event.getSource() : null;
/*  974 */       this.threadId = event.getThreadId();
/*  975 */       this.threadName = event.getThreadName();
/*  976 */       this.threadPriority = event.getThreadPriority();
/*  977 */       this.isLocationRequired = includeLocation;
/*  978 */       this.isEndOfBatch = event.isEndOfBatch();
/*  979 */       this.nanoTime = event.getNanoTime();
/*      */     }
/*      */     
/*      */     private static Message memento(ReusableMessage message) {
/*  983 */       return message.memento();
/*      */     }
/*      */     
/*      */     private static StringMap memento(ReadOnlyStringMap data) {
/*  987 */       StringMap result = ContextDataFactory.createContextData();
/*  988 */       result.putAll(data);
/*  989 */       return result;
/*      */     }
/*      */     
/*      */     private static MarshalledObject<Message> marshall(Message msg) {
/*      */       try {
/*  994 */         return new MarshalledObject<>(msg);
/*  995 */       } catch (Exception ex) {
/*  996 */         return null;
/*      */       } 
/*      */     }
/*      */     
/*      */     private void writeObject(ObjectOutputStream s) throws IOException {
/* 1001 */       this.messageString = this.message.getFormattedMessage();
/* 1002 */       this.marshalledMessage = marshall(this.message);
/* 1003 */       s.defaultWriteObject();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected Object readResolve() {
/* 1011 */       Log4jLogEvent result = new Log4jLogEvent(this.loggerName, this.marker, this.loggerFQCN, this.level, message(), this.thrown, this.thrownProxy, this.contextData, this.contextStack, this.threadId, this.threadName, this.threadPriority, this.source, this.timeMillis, this.nanoOfMillisecond, this.nanoTime);
/*      */ 
/*      */       
/* 1014 */       result.setEndOfBatch(this.isEndOfBatch);
/* 1015 */       result.setIncludeLocation(this.isLocationRequired);
/* 1016 */       return result;
/*      */     }
/*      */     
/*      */     private Message message() {
/* 1020 */       if (this.marshalledMessage != null) {
/*      */         try {
/* 1022 */           return this.marshalledMessage.get();
/* 1023 */         } catch (Exception exception) {}
/*      */       }
/*      */ 
/*      */       
/* 1027 */       return (Message)new SimpleMessage(this.messageString);
/*      */     }
/*      */   }
/*      */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\impl\Log4jLogEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */