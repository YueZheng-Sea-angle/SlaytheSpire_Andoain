/*     */ package org.apache.logging.log4j.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.text.DecimalFormat;
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
/*     */ public class Timer
/*     */   implements Serializable, StringBuilderFormattable
/*     */ {
/*     */   private static final long serialVersionUID = 9175191792439630013L;
/*     */   private final String name;
/*     */   private Status status;
/*     */   private long elapsedTime;
/*     */   private final int iterations;
/*     */   
/*     */   public enum Status
/*     */   {
/*  33 */     Started, Stopped, Paused;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  38 */   private static long NANO_PER_SECOND = 1000000000L;
/*  39 */   private static long NANO_PER_MINUTE = NANO_PER_SECOND * 60L;
/*  40 */   private static long NANO_PER_HOUR = NANO_PER_MINUTE * 60L;
/*  41 */   private ThreadLocal<Long> startTime = new ThreadLocal<Long>() {
/*     */       protected Long initialValue() {
/*  43 */         return Long.valueOf(0L);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Timer(String name) {
/*  54 */     this(name, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Timer(String name, int iterations) {
/*  64 */     this.name = name;
/*  65 */     this.status = Status.Stopped;
/*  66 */     this.iterations = (iterations > 0) ? iterations : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void start() {
/*  74 */     this.startTime.set(Long.valueOf(System.nanoTime()));
/*  75 */     this.elapsedTime = 0L;
/*  76 */     this.status = Status.Started;
/*     */   }
/*     */   
/*     */   public synchronized void startOrResume() {
/*  80 */     if (this.status == Status.Stopped) {
/*  81 */       start();
/*     */     } else {
/*  83 */       resume();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized String stop() {
/*  92 */     this.elapsedTime += System.nanoTime() - ((Long)this.startTime.get()).longValue();
/*  93 */     this.startTime.set(Long.valueOf(0L));
/*  94 */     this.status = Status.Stopped;
/*  95 */     return toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void pause() {
/* 103 */     this.elapsedTime += System.nanoTime() - ((Long)this.startTime.get()).longValue();
/* 104 */     this.startTime.set(Long.valueOf(0L));
/* 105 */     this.status = Status.Paused;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void resume() {
/* 113 */     this.startTime.set(Long.valueOf(System.nanoTime()));
/* 114 */     this.status = Status.Started;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 123 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getElapsedTime() {
/* 133 */     return this.elapsedTime / 1000000L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getElapsedNanoTime() {
/* 143 */     return this.elapsedTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Status getStatus() {
/* 153 */     return this.status;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 162 */     StringBuilder result = new StringBuilder();
/* 163 */     formatTo(result);
/* 164 */     return result.toString();
/*     */   } public void formatTo(StringBuilder buffer) {
/*     */     long nanoseconds, hours, minutes, seconds;
/*     */     String elapsed;
/*     */     DecimalFormat numFormat;
/* 169 */     buffer.append("Timer ").append(this.name);
/* 170 */     switch (this.status) {
/*     */       case Started:
/* 172 */         buffer.append(" started");
/*     */         return;
/*     */       case Paused:
/* 175 */         buffer.append(" paused");
/*     */         return;
/*     */       case Stopped:
/* 178 */         nanoseconds = this.elapsedTime;
/*     */         
/* 180 */         hours = nanoseconds / NANO_PER_HOUR;
/*     */         
/* 182 */         nanoseconds %= NANO_PER_HOUR;
/*     */         
/* 184 */         minutes = nanoseconds / NANO_PER_MINUTE;
/*     */         
/* 186 */         nanoseconds %= NANO_PER_MINUTE;
/*     */         
/* 188 */         seconds = nanoseconds / NANO_PER_SECOND;
/*     */         
/* 190 */         nanoseconds %= NANO_PER_SECOND;
/*     */         
/* 192 */         elapsed = "";
/*     */         
/* 194 */         if (hours > 0L) {
/* 195 */           elapsed = elapsed + hours + " hours ";
/*     */         }
/* 197 */         if (minutes > 0L || hours > 0L) {
/* 198 */           elapsed = elapsed + minutes + " minutes ";
/*     */         }
/*     */ 
/*     */         
/* 202 */         numFormat = new DecimalFormat("#0");
/* 203 */         elapsed = elapsed + numFormat.format(seconds) + '.';
/* 204 */         numFormat = new DecimalFormat("000000000");
/* 205 */         elapsed = elapsed + numFormat.format(nanoseconds) + " seconds";
/* 206 */         buffer.append(" stopped. Elapsed time: ").append(elapsed);
/* 207 */         if (this.iterations > 0) {
/* 208 */           nanoseconds = this.elapsedTime / this.iterations;
/*     */           
/* 210 */           hours = nanoseconds / NANO_PER_HOUR;
/*     */           
/* 212 */           nanoseconds %= NANO_PER_HOUR;
/*     */           
/* 214 */           minutes = nanoseconds / NANO_PER_MINUTE;
/*     */           
/* 216 */           nanoseconds %= NANO_PER_MINUTE;
/*     */           
/* 218 */           seconds = nanoseconds / NANO_PER_SECOND;
/*     */           
/* 220 */           nanoseconds %= NANO_PER_SECOND;
/*     */           
/* 222 */           elapsed = "";
/*     */           
/* 224 */           if (hours > 0L) {
/* 225 */             elapsed = elapsed + hours + " hours ";
/*     */           }
/* 227 */           if (minutes > 0L || hours > 0L) {
/* 228 */             elapsed = elapsed + minutes + " minutes ";
/*     */           }
/*     */           
/* 231 */           numFormat = new DecimalFormat("#0");
/* 232 */           elapsed = elapsed + numFormat.format(seconds) + '.';
/* 233 */           numFormat = new DecimalFormat("000000000");
/* 234 */           elapsed = elapsed + numFormat.format(nanoseconds) + " seconds";
/* 235 */           buffer.append(" Average per iteration: ").append(elapsed);
/*     */         } 
/*     */         return;
/*     */     } 
/* 239 */     buffer.append(' ').append(this.status);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 246 */     if (this == o) {
/* 247 */       return true;
/*     */     }
/* 249 */     if (!(o instanceof Timer)) {
/* 250 */       return false;
/*     */     }
/*     */     
/* 253 */     Timer timer = (Timer)o;
/*     */     
/* 255 */     if (this.elapsedTime != timer.elapsedTime) {
/* 256 */       return false;
/*     */     }
/* 258 */     if (this.startTime != timer.startTime) {
/* 259 */       return false;
/*     */     }
/* 261 */     if ((this.name != null) ? !this.name.equals(timer.name) : (timer.name != null)) {
/* 262 */       return false;
/*     */     }
/* 264 */     if ((this.status != null) ? !this.status.equals(timer.status) : (timer.status != null)) {
/* 265 */       return false;
/*     */     }
/*     */     
/* 268 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 274 */     int result = (this.name != null) ? this.name.hashCode() : 0;
/* 275 */     result = 29 * result + ((this.status != null) ? this.status.hashCode() : 0);
/* 276 */     long time = ((Long)this.startTime.get()).longValue();
/* 277 */     result = 29 * result + (int)(time ^ time >>> 32L);
/* 278 */     result = 29 * result + (int)(this.elapsedTime ^ this.elapsedTime >>> 32L);
/* 279 */     return result;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4\\util\Timer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */