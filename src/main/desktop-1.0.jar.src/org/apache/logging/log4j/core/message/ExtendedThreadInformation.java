/*     */ package org.apache.logging.log4j.core.message;
/*     */ 
/*     */ import java.lang.management.LockInfo;
/*     */ import java.lang.management.MonitorInfo;
/*     */ import java.lang.management.ThreadInfo;
/*     */ import org.apache.logging.log4j.message.ThreadInformation;
/*     */ import org.apache.logging.log4j.util.StringBuilders;
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
/*     */ class ExtendedThreadInformation
/*     */   implements ThreadInformation
/*     */ {
/*     */   private final ThreadInfo threadInfo;
/*     */   
/*     */   ExtendedThreadInformation(ThreadInfo thread) {
/*  35 */     this.threadInfo = thread;
/*     */   }
/*     */ 
/*     */   
/*     */   public void printThreadInfo(StringBuilder sb) {
/*  40 */     StringBuilders.appendDqValue(sb, this.threadInfo.getThreadName());
/*  41 */     sb.append(" Id=").append(this.threadInfo.getThreadId()).append(' ');
/*  42 */     formatState(sb, this.threadInfo);
/*  43 */     if (this.threadInfo.isSuspended()) {
/*  44 */       sb.append(" (suspended)");
/*     */     }
/*  46 */     if (this.threadInfo.isInNative()) {
/*  47 */       sb.append(" (in native)");
/*     */     }
/*  49 */     sb.append('\n');
/*     */   }
/*     */ 
/*     */   
/*     */   public void printStack(StringBuilder sb, StackTraceElement[] stack) {
/*  54 */     int i = 0;
/*  55 */     for (StackTraceElement element : stack) {
/*  56 */       sb.append("\tat ").append(element.toString());
/*  57 */       sb.append('\n');
/*  58 */       if (i == 0 && this.threadInfo.getLockInfo() != null) {
/*  59 */         Thread.State ts = this.threadInfo.getThreadState();
/*  60 */         switch (ts) {
/*     */           case BLOCKED:
/*  62 */             sb.append("\t-  blocked on ");
/*  63 */             formatLock(sb, this.threadInfo.getLockInfo());
/*  64 */             sb.append('\n');
/*     */             break;
/*     */           case WAITING:
/*  67 */             sb.append("\t-  waiting on ");
/*  68 */             formatLock(sb, this.threadInfo.getLockInfo());
/*  69 */             sb.append('\n');
/*     */             break;
/*     */           case TIMED_WAITING:
/*  72 */             sb.append("\t-  waiting on ");
/*  73 */             formatLock(sb, this.threadInfo.getLockInfo());
/*  74 */             sb.append('\n');
/*     */             break;
/*     */         } 
/*     */ 
/*     */       
/*     */       } 
/*  80 */       for (MonitorInfo mi : this.threadInfo.getLockedMonitors()) {
/*  81 */         if (mi.getLockedStackDepth() == i) {
/*  82 */           sb.append("\t-  locked ");
/*  83 */           formatLock(sb, mi);
/*  84 */           sb.append('\n');
/*     */         } 
/*     */       } 
/*  87 */       i++;
/*     */     } 
/*     */     
/*  90 */     LockInfo[] locks = this.threadInfo.getLockedSynchronizers();
/*  91 */     if (locks.length > 0) {
/*  92 */       sb.append("\n\tNumber of locked synchronizers = ").append(locks.length).append('\n');
/*  93 */       for (LockInfo li : locks) {
/*  94 */         sb.append("\t- ");
/*  95 */         formatLock(sb, li);
/*  96 */         sb.append('\n');
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void formatLock(StringBuilder sb, LockInfo lock) {
/* 102 */     sb.append('<').append(lock.getIdentityHashCode()).append("> (a ");
/* 103 */     sb.append(lock.getClassName()).append(')');
/*     */   } private void formatState(StringBuilder sb, ThreadInfo info) {
/*     */     StackTraceElement element;
/*     */     String className, method;
/* 107 */     Thread.State state = info.getThreadState();
/* 108 */     sb.append(state);
/* 109 */     switch (state) {
/*     */       case BLOCKED:
/* 111 */         sb.append(" (on object monitor owned by \"");
/* 112 */         sb.append(info.getLockOwnerName()).append("\" Id=").append(info.getLockOwnerId()).append(')');
/*     */         break;
/*     */       
/*     */       case WAITING:
/* 116 */         element = info.getStackTrace()[0];
/* 117 */         className = element.getClassName();
/* 118 */         method = element.getMethodName();
/* 119 */         if (className.equals("java.lang.Object") && method.equals("wait")) {
/* 120 */           sb.append(" (on object monitor");
/* 121 */           if (info.getLockOwnerName() != null) {
/* 122 */             sb.append(" owned by \"");
/* 123 */             sb.append(info.getLockOwnerName()).append("\" Id=").append(info.getLockOwnerId());
/*     */           } 
/* 125 */           sb.append(')'); break;
/* 126 */         }  if (className.equals("java.lang.Thread") && method.equals("join")) {
/* 127 */           sb.append(" (on completion of thread ").append(info.getLockOwnerId()).append(')'); break;
/*     */         } 
/* 129 */         sb.append(" (parking for lock");
/* 130 */         if (info.getLockOwnerName() != null) {
/* 131 */           sb.append(" owned by \"");
/* 132 */           sb.append(info.getLockOwnerName()).append("\" Id=").append(info.getLockOwnerId());
/*     */         } 
/* 134 */         sb.append(')');
/*     */         break;
/*     */ 
/*     */       
/*     */       case TIMED_WAITING:
/* 139 */         element = info.getStackTrace()[0];
/* 140 */         className = element.getClassName();
/* 141 */         method = element.getMethodName();
/* 142 */         if (className.equals("java.lang.Object") && method.equals("wait")) {
/* 143 */           sb.append(" (on object monitor");
/* 144 */           if (info.getLockOwnerName() != null) {
/* 145 */             sb.append(" owned by \"");
/* 146 */             sb.append(info.getLockOwnerName()).append("\" Id=").append(info.getLockOwnerId());
/*     */           } 
/* 148 */           sb.append(')'); break;
/* 149 */         }  if (className.equals("java.lang.Thread") && method.equals("sleep")) {
/* 150 */           sb.append(" (sleeping)"); break;
/* 151 */         }  if (className.equals("java.lang.Thread") && method.equals("join")) {
/* 152 */           sb.append(" (on completion of thread ").append(info.getLockOwnerId()).append(')'); break;
/*     */         } 
/* 154 */         sb.append(" (parking for lock");
/* 155 */         if (info.getLockOwnerName() != null) {
/* 156 */           sb.append(" owned by \"");
/* 157 */           sb.append(info.getLockOwnerName()).append("\" Id=").append(info.getLockOwnerId());
/*     */         } 
/* 159 */         sb.append(')');
/*     */         break;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\message\ExtendedThreadInformation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */