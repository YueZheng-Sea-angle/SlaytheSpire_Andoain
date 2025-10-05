/*     */ package org.apache.logging.log4j.status;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.message.MessageFactory;
/*     */ import org.apache.logging.log4j.message.ParameterizedNoReferenceMessageFactory;
/*     */ import org.apache.logging.log4j.simple.SimpleLogger;
/*     */ import org.apache.logging.log4j.spi.AbstractLogger;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
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
/*     */ public final class StatusLogger
/*     */   extends AbstractLogger
/*     */ {
/*     */   public static final String MAX_STATUS_ENTRIES = "log4j2.status.entries";
/*     */   public static final String DEFAULT_STATUS_LISTENER_LEVEL = "log4j2.StatusLogger.level";
/*     */   public static final String STATUS_DATE_FORMAT = "log4j2.StatusLogger.DateFormat";
/*     */   private static final long serialVersionUID = 2L;
/*     */   private static final String NOT_AVAIL = "?";
/*  78 */   private static final PropertiesUtil PROPS = new PropertiesUtil("log4j2.StatusLogger.properties");
/*     */   
/*  80 */   private static final int MAX_ENTRIES = PROPS.getIntegerProperty("log4j2.status.entries", 200);
/*     */   
/*  82 */   private static final String DEFAULT_STATUS_LEVEL = PROPS.getStringProperty("log4j2.StatusLogger.level");
/*     */ 
/*     */   
/*  85 */   private static final StatusLogger STATUS_LOGGER = new StatusLogger(StatusLogger.class.getName(), (MessageFactory)ParameterizedNoReferenceMessageFactory.INSTANCE);
/*     */ 
/*     */   
/*     */   private final SimpleLogger logger;
/*     */   
/*  90 */   private final Collection<StatusListener> listeners = new CopyOnWriteArrayList<>();
/*     */   
/*  92 */   private final ReadWriteLock listenersLock = new ReentrantReadWriteLock();
/*     */ 
/*     */ 
/*     */   
/*  96 */   private final Queue<StatusData> messages = new BoundedQueue<>(MAX_ENTRIES);
/*     */   
/*  98 */   private final Lock msgLock = new ReentrantLock();
/*     */ 
/*     */   
/*     */   private int listenersLevel;
/*     */ 
/*     */   
/*     */   private StatusLogger(String name, MessageFactory messageFactory) {
/* 105 */     super(name, messageFactory);
/* 106 */     String dateFormat = PROPS.getStringProperty("log4j2.StatusLogger.DateFormat", "");
/* 107 */     boolean showDateTime = !Strings.isEmpty(dateFormat);
/* 108 */     this.logger = new SimpleLogger("StatusLogger", Level.ERROR, false, true, showDateTime, false, dateFormat, messageFactory, PROPS, System.err);
/*     */     
/* 110 */     this.listenersLevel = Level.toLevel(DEFAULT_STATUS_LEVEL, Level.WARN).intLevel();
/*     */ 
/*     */     
/* 113 */     if (isDebugPropertyEnabled()) {
/* 114 */       this.logger.setLevel(Level.TRACE);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isDebugPropertyEnabled() {
/* 120 */     return PropertiesUtil.getProperties().getBooleanProperty("log4j2.debug", false, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StatusLogger getLogger() {
/* 129 */     return STATUS_LOGGER;
/*     */   }
/*     */   
/*     */   public void setLevel(Level level) {
/* 133 */     this.logger.setLevel(level);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerListener(StatusListener listener) {
/* 142 */     this.listenersLock.writeLock().lock();
/*     */     try {
/* 144 */       this.listeners.add(listener);
/* 145 */       Level lvl = listener.getStatusLevel();
/* 146 */       if (this.listenersLevel < lvl.intLevel()) {
/* 147 */         this.listenersLevel = lvl.intLevel();
/*     */       }
/*     */     } finally {
/* 150 */       this.listenersLock.writeLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeListener(StatusListener listener) {
/* 160 */     closeSilently(listener);
/* 161 */     this.listenersLock.writeLock().lock();
/*     */     try {
/* 163 */       this.listeners.remove(listener);
/* 164 */       int lowest = Level.toLevel(DEFAULT_STATUS_LEVEL, Level.WARN).intLevel();
/* 165 */       for (StatusListener statusListener : this.listeners) {
/* 166 */         int level = statusListener.getStatusLevel().intLevel();
/* 167 */         if (lowest < level) {
/* 168 */           lowest = level;
/*     */         }
/*     */       } 
/* 171 */       this.listenersLevel = lowest;
/*     */     } finally {
/* 173 */       this.listenersLock.writeLock().unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void updateListenerLevel(Level status) {
/* 178 */     if (status.intLevel() > this.listenersLevel) {
/* 179 */       this.listenersLevel = status.intLevel();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterable<StatusListener> getListeners() {
/* 189 */     return this.listeners;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 196 */     this.listenersLock.writeLock().lock();
/*     */     try {
/* 198 */       for (StatusListener listener : this.listeners) {
/* 199 */         closeSilently(listener);
/*     */       }
/*     */     } finally {
/* 202 */       this.listeners.clear();
/* 203 */       this.listenersLock.writeLock().unlock();
/*     */       
/* 205 */       clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void closeSilently(Closeable resource) {
/*     */     try {
/* 211 */       resource.close();
/* 212 */     } catch (IOException iOException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<StatusData> getStatusData() {
/* 223 */     this.msgLock.lock();
/*     */     try {
/* 225 */       return new ArrayList<>(this.messages);
/*     */     } finally {
/* 227 */       this.msgLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 235 */     this.msgLock.lock();
/*     */     try {
/* 237 */       this.messages.clear();
/*     */     } finally {
/* 239 */       this.msgLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Level getLevel() {
/* 245 */     return this.logger.getLevel();
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
/*     */   public void logMessage(String fqcn, Level level, Marker marker, Message msg, Throwable t) {
/* 260 */     StackTraceElement element = null;
/* 261 */     if (fqcn != null) {
/* 262 */       element = getStackTraceElement(fqcn, Thread.currentThread().getStackTrace());
/*     */     }
/* 264 */     StatusData data = new StatusData(element, level, msg, t, null);
/* 265 */     this.msgLock.lock();
/*     */     try {
/* 267 */       this.messages.add(data);
/*     */     } finally {
/* 269 */       this.msgLock.unlock();
/*     */     } 
/*     */     
/* 272 */     if (isDebugPropertyEnabled() || this.listeners.size() <= 0) {
/* 273 */       this.logger.logMessage(fqcn, level, marker, msg, t);
/*     */     } else {
/* 275 */       for (StatusListener listener : this.listeners) {
/* 276 */         if (data.getLevel().isMoreSpecificThan(listener.getStatusLevel())) {
/* 277 */           listener.log(data);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private StackTraceElement getStackTraceElement(String fqcn, StackTraceElement[] stackTrace) {
/* 284 */     if (fqcn == null) {
/* 285 */       return null;
/*     */     }
/* 287 */     boolean next = false;
/* 288 */     for (StackTraceElement element : stackTrace) {
/* 289 */       String className = element.getClassName();
/* 290 */       if (next && !fqcn.equals(className)) {
/* 291 */         return element;
/*     */       }
/* 293 */       if (fqcn.equals(className)) {
/* 294 */         next = true;
/* 295 */       } else if ("?".equals(className)) {
/*     */         break;
/*     */       } 
/*     */     } 
/* 299 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Throwable t) {
/* 304 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message) {
/* 309 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object... params) {
/* 314 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0) {
/* 319 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1) {
/* 325 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
/* 331 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
/* 337 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 344 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 351 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 358 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 366 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 374 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 382 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, CharSequence message, Throwable t) {
/* 387 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, Object message, Throwable t) {
/* 392 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, Message message, Throwable t) {
/* 397 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker) {
/* 403 */     if (isDebugPropertyEnabled()) {
/* 404 */       return true;
/*     */     }
/* 406 */     if (this.listeners.size() > 0) {
/* 407 */       return (this.listenersLevel >= level.intLevel());
/*     */     }
/* 409 */     return this.logger.isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class BoundedQueue<E>
/*     */     extends ConcurrentLinkedQueue<E>
/*     */   {
/*     */     private static final long serialVersionUID = -3945953719763255337L;
/*     */ 
/*     */     
/*     */     private final int size;
/*     */ 
/*     */     
/*     */     BoundedQueue(int size) {
/* 424 */       this.size = size;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean add(E object) {
/* 429 */       super.add(object);
/* 430 */       while (StatusLogger.this.messages.size() > this.size) {
/* 431 */         StatusLogger.this.messages.poll();
/*     */       }
/* 433 */       return (this.size > 0);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\status\StatusLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */