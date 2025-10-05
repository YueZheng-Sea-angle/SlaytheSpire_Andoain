/*     */ package org.apache.logging.log4j.message;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ class BasicThreadInformation
/*     */   implements ThreadInformation
/*     */ {
/*     */   private static final int HASH_SHIFT = 32;
/*     */   private static final int HASH_MULTIPLIER = 31;
/*     */   private final long id;
/*     */   private final String name;
/*     */   private final String longName;
/*     */   private final Thread.State state;
/*     */   private final int priority;
/*     */   private final boolean isAlive;
/*     */   private final boolean isDaemon;
/*     */   private final String threadGroupName;
/*     */   
/*     */   BasicThreadInformation(Thread thread) {
/*  42 */     this.id = thread.getId();
/*  43 */     this.name = thread.getName();
/*  44 */     this.longName = thread.toString();
/*  45 */     this.state = thread.getState();
/*  46 */     this.priority = thread.getPriority();
/*  47 */     this.isAlive = thread.isAlive();
/*  48 */     this.isDaemon = thread.isDaemon();
/*  49 */     ThreadGroup group = thread.getThreadGroup();
/*  50 */     this.threadGroupName = (group == null) ? null : group.getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  55 */     if (this == o) {
/*  56 */       return true;
/*     */     }
/*  58 */     if (o == null || getClass() != o.getClass()) {
/*  59 */       return false;
/*     */     }
/*     */     
/*  62 */     BasicThreadInformation that = (BasicThreadInformation)o;
/*     */     
/*  64 */     if (this.id != that.id) {
/*  65 */       return false;
/*     */     }
/*  67 */     if ((this.name != null) ? !this.name.equals(that.name) : (that.name != null)) {
/*  68 */       return false;
/*     */     }
/*     */     
/*  71 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  76 */     int result = (int)(this.id ^ this.id >>> 32L);
/*  77 */     result = 31 * result + ((this.name != null) ? this.name.hashCode() : 0);
/*  78 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printThreadInfo(StringBuilder sb) {
/*  87 */     StringBuilders.appendDqValue(sb, this.name).append(' ');
/*  88 */     if (this.isDaemon) {
/*  89 */       sb.append("daemon ");
/*     */     }
/*  91 */     sb.append("prio=").append(this.priority).append(" tid=").append(this.id).append(' ');
/*  92 */     if (this.threadGroupName != null) {
/*  93 */       StringBuilders.appendKeyDqValue(sb, "group", this.threadGroupName);
/*     */     }
/*  95 */     sb.append('\n');
/*  96 */     sb.append("\tThread state: ").append(this.state.name()).append('\n');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printStack(StringBuilder sb, StackTraceElement[] trace) {
/* 106 */     for (StackTraceElement element : trace)
/* 107 */       sb.append("\tat ").append(element).append('\n'); 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\message\BasicThreadInformation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */