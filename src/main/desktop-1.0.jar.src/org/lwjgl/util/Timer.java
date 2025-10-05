/*     */ package org.lwjgl.util;
/*     */ 
/*     */ import org.lwjgl.Sys;
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
/*     */ public class Timer
/*     */ {
/*  50 */   private static long resolution = Sys.getTimerResolution();
/*     */ 
/*     */   
/*     */   private static final int QUERY_INTERVAL = 50;
/*     */ 
/*     */   
/*     */   private static int queryCount;
/*     */ 
/*     */   
/*     */   private static long currentTime;
/*     */   
/*     */   private long startTime;
/*     */   
/*     */   private long lastTime;
/*     */   
/*     */   private boolean paused;
/*     */ 
/*     */   
/*     */   static {
/*  69 */     tick();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Timer() {
/*  76 */     reset();
/*  77 */     resume();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getTime() {
/*  84 */     if (!this.paused) {
/*  85 */       this.lastTime = currentTime - this.startTime;
/*     */     }
/*     */     
/*  88 */     return (float)(this.lastTime / resolution);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPaused() {
/*  94 */     return this.paused;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pause() {
/* 104 */     this.paused = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 112 */     set(0.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resume() {
/* 120 */     this.paused = false;
/* 121 */     this.startTime = currentTime - this.lastTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(float newTime) {
/* 129 */     long newTimeInTicks = (long)(newTime * resolution);
/* 130 */     this.startTime = currentTime - newTimeInTicks;
/* 131 */     this.lastTime = newTimeInTicks;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void tick() {
/* 140 */     currentTime = Sys.getTime();
/*     */ 
/*     */     
/* 143 */     queryCount++;
/* 144 */     if (queryCount > 50) {
/* 145 */       queryCount = 0;
/* 146 */       resolution = Sys.getTimerResolution();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 154 */     return "Timer[Time=" + getTime() + ", Paused=" + this.paused + "]";
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjg\\util\Timer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */