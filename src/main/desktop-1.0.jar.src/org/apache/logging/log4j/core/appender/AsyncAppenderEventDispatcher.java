/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.AppenderControl;
/*     */ import org.apache.logging.log4j.core.impl.Log4jLogEvent;
/*     */ import org.apache.logging.log4j.core.util.Log4jThread;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
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
/*     */ class AsyncAppenderEventDispatcher
/*     */   extends Log4jThread
/*     */ {
/*  33 */   private static final LogEvent STOP_EVENT = (LogEvent)new Log4jLogEvent();
/*     */   
/*  35 */   private static final AtomicLong THREAD_COUNTER = new AtomicLong(0L);
/*     */   
/*  37 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */   
/*     */   private final AppenderControl errorAppender;
/*     */ 
/*     */   
/*     */   private final List<AppenderControl> appenders;
/*     */ 
/*     */   
/*     */   private final BlockingQueue<LogEvent> queue;
/*     */   
/*     */   private final AtomicBoolean stoppedRef;
/*     */ 
/*     */   
/*     */   AsyncAppenderEventDispatcher(String name, AppenderControl errorAppender, List<AppenderControl> appenders, BlockingQueue<LogEvent> queue) {
/*  52 */     super("AsyncAppenderEventDispatcher-" + THREAD_COUNTER.incrementAndGet() + "-" + name);
/*  53 */     setDaemon(true);
/*  54 */     this.errorAppender = errorAppender;
/*  55 */     this.appenders = appenders;
/*  56 */     this.queue = queue;
/*  57 */     this.stoppedRef = new AtomicBoolean(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*  62 */     LOGGER.trace("{} has started.", getName());
/*  63 */     dispatchAll();
/*  64 */     dispatchRemaining();
/*     */   }
/*     */   
/*     */   private void dispatchAll() {
/*  68 */     while (!this.stoppedRef.get()) {
/*     */       LogEvent event;
/*     */       try {
/*  71 */         event = this.queue.take();
/*  72 */       } catch (InterruptedException ignored) {
/*     */         
/*  74 */         interrupt();
/*     */         break;
/*     */       } 
/*  77 */       if (event == STOP_EVENT) {
/*     */         break;
/*     */       }
/*  80 */       event.setEndOfBatch(this.queue.isEmpty());
/*  81 */       dispatch(event);
/*     */     } 
/*  83 */     LOGGER.trace("{} has stopped.", getName());
/*     */   }
/*     */   
/*     */   private void dispatchRemaining() {
/*  87 */     int eventCount = 0;
/*     */     
/*     */     while (true) {
/*  90 */       LogEvent event = this.queue.poll();
/*  91 */       if (event == null) {
/*     */         break;
/*     */       }
/*     */       
/*  95 */       if (event == STOP_EVENT) {
/*     */         continue;
/*     */       }
/*  98 */       event.setEndOfBatch(this.queue.isEmpty());
/*  99 */       dispatch(event);
/* 100 */       eventCount++;
/*     */     } 
/* 102 */     LOGGER.trace("{} has processed the last {} remaining event(s).", 
/*     */         
/* 104 */         getName(), Integer.valueOf(eventCount));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void dispatch(LogEvent event) {
/* 114 */     boolean succeeded = false;
/*     */     
/* 116 */     for (int appenderIndex = 0; appenderIndex < this.appenders.size(); appenderIndex++) {
/* 117 */       AppenderControl control = this.appenders.get(appenderIndex);
/*     */       try {
/* 119 */         control.callAppender(event);
/* 120 */         succeeded = true;
/* 121 */       } catch (Throwable error) {
/*     */ 
/*     */         
/* 124 */         LOGGER.trace("{} has failed to call appender {}", 
/*     */             
/* 126 */             getName(), control.getAppenderName(), error);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 131 */     if (!succeeded && this.errorAppender != null) {
/*     */       try {
/* 133 */         this.errorAppender.callAppender(event);
/* 134 */       } catch (Throwable error) {
/*     */ 
/*     */         
/* 137 */         LOGGER.trace("{} has failed to call the error appender {}", 
/*     */             
/* 139 */             getName(), this.errorAppender.getAppenderName(), error);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void stop(long timeoutMillis) throws InterruptedException {
/* 148 */     boolean stopped = this.stoppedRef.compareAndSet(false, true);
/* 149 */     if (stopped) {
/* 150 */       LOGGER.trace("{} is signaled to stop.", getName());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 156 */     while (Thread.State.NEW.equals(getState()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 165 */     boolean added = this.queue.offer(STOP_EVENT);
/* 166 */     if (!added) {
/* 167 */       interrupt();
/*     */     }
/*     */ 
/*     */     
/* 171 */     join(timeoutMillis);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\AsyncAppenderEventDispatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */