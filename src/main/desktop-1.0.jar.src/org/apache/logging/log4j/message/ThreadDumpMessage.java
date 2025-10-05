/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.ServiceConfigurationError;
/*     */ import java.util.ServiceLoader;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.StringBuilderFormattable;
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
/*     */ @AsynchronouslyFormattable
/*     */ public class ThreadDumpMessage
/*     */   implements Message, StringBuilderFormattable
/*     */ {
/*     */   private static final long serialVersionUID = -1103400781608841088L;
/*     */   private static ThreadInfoFactory FACTORY;
/*     */   private volatile Map<ThreadInformation, StackTraceElement[]> threads;
/*     */   private final String title;
/*     */   private String formattedMessage;
/*     */   
/*     */   public ThreadDumpMessage(String title) {
/*  49 */     this.title = (title == null) ? "" : title;
/*  50 */     this.threads = getFactory().createThreadInfo();
/*     */   }
/*     */   
/*     */   private ThreadDumpMessage(String formattedMsg, String title) {
/*  54 */     this.formattedMessage = formattedMsg;
/*  55 */     this.title = (title == null) ? "" : title;
/*     */   }
/*     */   
/*     */   private static ThreadInfoFactory getFactory() {
/*  59 */     if (FACTORY == null) {
/*  60 */       FACTORY = initFactory(ThreadDumpMessage.class.getClassLoader());
/*     */     }
/*  62 */     return FACTORY;
/*     */   }
/*     */   
/*     */   private static ThreadInfoFactory initFactory(ClassLoader classLoader) {
/*  66 */     ServiceLoader<ThreadInfoFactory> serviceLoader = ServiceLoader.load(ThreadInfoFactory.class, classLoader);
/*  67 */     ThreadInfoFactory result = null;
/*     */     try {
/*  69 */       Iterator<ThreadInfoFactory> iterator = serviceLoader.iterator();
/*  70 */       while (result == null && iterator.hasNext()) {
/*  71 */         result = iterator.next();
/*     */       }
/*  73 */     } catch (ServiceConfigurationError|LinkageError|Exception unavailable) {
/*  74 */       StatusLogger.getLogger().info("ThreadDumpMessage uses BasicThreadInfoFactory: could not load extended ThreadInfoFactory: {}", unavailable
/*  75 */           .toString());
/*  76 */       result = null;
/*     */     } 
/*  78 */     return (result == null) ? new BasicThreadInfoFactory() : result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  83 */     return getFormattedMessage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedMessage() {
/*  92 */     if (this.formattedMessage != null) {
/*  93 */       return this.formattedMessage;
/*     */     }
/*  95 */     StringBuilder sb = new StringBuilder(255);
/*  96 */     formatTo(sb);
/*  97 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void formatTo(StringBuilder sb) {
/* 102 */     sb.append(this.title);
/* 103 */     if (this.title.length() > 0) {
/* 104 */       sb.append('\n');
/*     */     }
/* 106 */     for (Map.Entry<ThreadInformation, StackTraceElement[]> entry : this.threads.entrySet()) {
/* 107 */       ThreadInformation info = entry.getKey();
/* 108 */       info.printThreadInfo(sb);
/* 109 */       info.printStack(sb, entry.getValue());
/* 110 */       sb.append('\n');
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormat() {
/* 120 */     return (this.title == null) ? "" : this.title;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getParameters() {
/* 130 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object writeReplace() {
/* 138 */     return new ThreadDumpMessageProxy(this);
/*     */   }
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 143 */     throw new InvalidObjectException("Proxy required");
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ThreadDumpMessageProxy
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = -3476620450287648269L;
/*     */     
/*     */     private final String formattedMsg;
/*     */     private final String title;
/*     */     
/*     */     ThreadDumpMessageProxy(ThreadDumpMessage msg) {
/* 156 */       this.formattedMsg = msg.getFormattedMessage();
/* 157 */       this.title = msg.title;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected Object readResolve() {
/* 165 */       return new ThreadDumpMessage(this.formattedMsg, this.title);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface ThreadInfoFactory
/*     */   {
/*     */     Map<ThreadInformation, StackTraceElement[]> createThreadInfo();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class BasicThreadInfoFactory
/*     */     implements ThreadInfoFactory
/*     */   {
/*     */     private BasicThreadInfoFactory() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<ThreadInformation, StackTraceElement[]> createThreadInfo() {
/* 186 */       Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
/*     */       
/* 188 */       Map<ThreadInformation, StackTraceElement[]> threads = (Map)new HashMap<>(map.size());
/* 189 */       for (Map.Entry<Thread, StackTraceElement[]> entry : map.entrySet()) {
/* 190 */         threads.put(new BasicThreadInformation(entry.getKey()), entry.getValue());
/*     */       }
/* 192 */       return threads;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getThrowable() {
/* 203 */     return null;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\message\ThreadDumpMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */