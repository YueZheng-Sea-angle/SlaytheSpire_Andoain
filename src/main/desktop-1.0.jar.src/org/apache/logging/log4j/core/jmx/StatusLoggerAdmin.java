/*     */ package org.apache.logging.log4j.core.jmx;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import javax.management.MBeanNotificationInfo;
/*     */ import javax.management.Notification;
/*     */ import javax.management.NotificationBroadcasterSupport;
/*     */ import javax.management.ObjectName;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.status.StatusData;
/*     */ import org.apache.logging.log4j.status.StatusListener;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StatusLoggerAdmin
/*     */   extends NotificationBroadcasterSupport
/*     */   implements StatusListener, StatusLoggerAdminMBean
/*     */ {
/*  39 */   private final AtomicLong sequenceNo = new AtomicLong();
/*     */   private final ObjectName objectName;
/*     */   private final String contextName;
/*  42 */   private Level level = Level.WARN;
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
/*     */   public StatusLoggerAdmin(String contextName, Executor executor) {
/*  58 */     super(executor, new MBeanNotificationInfo[] { createNotificationInfo() });
/*  59 */     this.contextName = contextName;
/*     */     try {
/*  61 */       String mbeanName = String.format("org.apache.logging.log4j2:type=%s,component=StatusLogger", new Object[] { Server.escape(contextName) });
/*  62 */       this.objectName = new ObjectName(mbeanName);
/*  63 */     } catch (Exception e) {
/*  64 */       throw new IllegalStateException(e);
/*     */     } 
/*  66 */     removeListeners(contextName);
/*  67 */     StatusLogger.getLogger().registerListener(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void removeListeners(String ctxName) {
/*  76 */     StatusLogger logger = StatusLogger.getLogger();
/*  77 */     Iterable<StatusListener> listeners = logger.getListeners();
/*     */     
/*  79 */     for (StatusListener statusListener : listeners) {
/*  80 */       if (statusListener instanceof StatusLoggerAdmin) {
/*  81 */         StatusLoggerAdmin adminListener = (StatusLoggerAdmin)statusListener;
/*  82 */         if (ctxName != null && ctxName.equals(adminListener.contextName)) {
/*  83 */           logger.removeListener(adminListener);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static MBeanNotificationInfo createNotificationInfo() {
/*  90 */     String[] notifTypes = { "com.apache.logging.log4j.core.jmx.statuslogger.data", "com.apache.logging.log4j.core.jmx.statuslogger.message" };
/*     */ 
/*     */     
/*  93 */     String name = Notification.class.getName();
/*  94 */     String description = "StatusLogger has logged an event";
/*  95 */     return new MBeanNotificationInfo(notifTypes, name, "StatusLogger has logged an event");
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getStatusDataHistory() {
/* 100 */     List<StatusData> data = getStatusData();
/* 101 */     String[] result = new String[data.size()];
/* 102 */     for (int i = 0; i < result.length; i++) {
/* 103 */       result[i] = ((StatusData)data.get(i)).getFormattedStatus();
/*     */     }
/* 105 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<StatusData> getStatusData() {
/* 110 */     return StatusLogger.getLogger().getStatusData();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLevel() {
/* 115 */     return this.level.name();
/*     */   }
/*     */ 
/*     */   
/*     */   public Level getStatusLevel() {
/* 120 */     return this.level;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLevel(String level) {
/* 125 */     this.level = Level.toLevel(level, Level.ERROR);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContextName() {
/* 130 */     return this.contextName;
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
/*     */   public void log(StatusData data) {
/* 143 */     Notification notifMsg = new Notification("com.apache.logging.log4j.core.jmx.statuslogger.message", getObjectName(), nextSeqNo(), nowMillis(), data.getFormattedStatus());
/* 144 */     sendNotification(notifMsg);
/*     */     
/* 146 */     Notification notifData = new Notification("com.apache.logging.log4j.core.jmx.statuslogger.data", getObjectName(), nextSeqNo(), nowMillis());
/* 147 */     notifData.setUserData(data);
/* 148 */     sendNotification(notifData);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectName getObjectName() {
/* 159 */     return this.objectName;
/*     */   }
/*     */   
/*     */   private long nextSeqNo() {
/* 163 */     return this.sequenceNo.getAndIncrement();
/*     */   }
/*     */   
/*     */   private long nowMillis() {
/* 167 */     return System.currentTimeMillis();
/*     */   }
/*     */   
/*     */   public void close() throws IOException {}
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\jmx\StatusLoggerAdmin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */