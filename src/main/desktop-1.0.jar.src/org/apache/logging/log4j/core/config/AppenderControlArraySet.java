/*     */ package org.apache.logging.log4j.core.config;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
/*     */ import org.apache.logging.log4j.core.Appender;
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
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
/*     */ @PerformanceSensitive
/*     */ public class AppenderControlArraySet
/*     */ {
/*  36 */   private static final AtomicReferenceFieldUpdater<AppenderControlArraySet, AppenderControl[]> appenderArrayUpdater = (AtomicReferenceFieldUpdater)AtomicReferenceFieldUpdater.newUpdater(AppenderControlArraySet.class, (Class)AppenderControl[].class, "appenderArray");
/*  37 */   private volatile AppenderControl[] appenderArray = AppenderControl.EMPTY_ARRAY;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(AppenderControl control) {
/*     */     while (true) {
/*  49 */       AppenderControl[] original = this.appenderArray;
/*  50 */       for (AppenderControl existing : original) {
/*  51 */         if (existing.equals(control)) {
/*  52 */           return false;
/*     */         }
/*     */       } 
/*  55 */       AppenderControl[] copy = Arrays.<AppenderControl>copyOf(original, original.length + 1);
/*  56 */       copy[copy.length - 1] = control;
/*  57 */       boolean success = appenderArrayUpdater.compareAndSet(this, original, copy);
/*  58 */       if (success) {
/*  59 */         return true;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AppenderControl remove(String name) {
/*     */     while (true) {
/*  71 */       boolean success = true;
/*  72 */       AppenderControl[] original = this.appenderArray;
/*  73 */       for (int i = 0; i < original.length; i++) {
/*  74 */         AppenderControl appenderControl = original[i];
/*  75 */         if (Objects.equals(name, appenderControl.getAppenderName())) {
/*  76 */           AppenderControl[] copy = removeElementAt(i, original);
/*  77 */           if (appenderArrayUpdater.compareAndSet(this, original, copy)) {
/*  78 */             return appenderControl;
/*     */           }
/*  80 */           success = false;
/*     */           break;
/*     */         } 
/*     */       } 
/*  84 */       if (success)
/*  85 */         return null; 
/*     */     } 
/*     */   }
/*     */   private AppenderControl[] removeElementAt(int i, AppenderControl[] array) {
/*  89 */     AppenderControl[] result = Arrays.<AppenderControl>copyOf(array, array.length - 1);
/*  90 */     System.arraycopy(array, i + 1, result, i, result.length - i);
/*  91 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Appender> asMap() {
/* 100 */     Map<String, Appender> result = new HashMap<>();
/* 101 */     for (AppenderControl appenderControl : this.appenderArray) {
/* 102 */       result.put(appenderControl.getAppenderName(), appenderControl.getAppender());
/*     */     }
/* 104 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AppenderControl[] clear() {
/* 113 */     return appenderArrayUpdater.getAndSet(this, AppenderControl.EMPTY_ARRAY);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 117 */     return (this.appenderArray.length == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AppenderControl[] get() {
/* 126 */     return this.appenderArray;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 131 */     return "AppenderControlArraySet [appenderArray=" + Arrays.toString((Object[])this.appenderArray) + "]";
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\AppenderControlArraySet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */