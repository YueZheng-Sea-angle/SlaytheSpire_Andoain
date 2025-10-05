/*     */ package org.apache.logging.log4j.util;
/*     */ 
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Stack;
/*     */ import java.util.function.Predicate;
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
/*     */ public final class StackLocatorUtil
/*     */ {
/*  29 */   private static StackLocator stackLocator = null;
/*     */   private static volatile boolean errorLogged;
/*     */   
/*     */   static {
/*  33 */     stackLocator = StackLocator.getInstance();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PerformanceSensitive
/*     */   public static Class<?> getCallerClass(int depth) {
/*  45 */     return stackLocator.getCallerClass(depth + 1);
/*     */   }
/*     */   
/*     */   public static StackTraceElement getStackTraceElement(int depth) {
/*  49 */     return stackLocator.getStackTraceElement(depth + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PerformanceSensitive
/*     */   public static Class<?> getCallerClass(String fqcn) {
/*  58 */     return getCallerClass(fqcn, "");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PerformanceSensitive
/*     */   public static Class<?> getCallerClass(String fqcn, String pkg) {
/*  70 */     return stackLocator.getCallerClass(fqcn, pkg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PerformanceSensitive
/*     */   public static Class<?> getCallerClass(Class<?> sentinelClass, Predicate<Class<?>> callerPredicate) {
/*  82 */     return stackLocator.getCallerClass(sentinelClass, callerPredicate);
/*     */   }
/*     */ 
/*     */   
/*     */   @PerformanceSensitive
/*     */   public static Class<?> getCallerClass(Class<?> anchor) {
/*  88 */     return stackLocator.getCallerClass(anchor);
/*     */   }
/*     */ 
/*     */   
/*     */   @PerformanceSensitive
/*     */   public static Stack<Class<?>> getCurrentStackTrace() {
/*  94 */     return stackLocator.getCurrentStackTrace();
/*     */   }
/*     */   
/*     */   public static StackTraceElement calcLocation(String fqcnOfLogger) {
/*     */     try {
/*  99 */       return stackLocator.calcLocation(fqcnOfLogger);
/* 100 */     } catch (NoSuchElementException ex) {
/* 101 */       if (!errorLogged) {
/* 102 */         errorLogged = true;
/* 103 */         StatusLogger.getLogger().warn("Unable to locate stack trace element for {}", fqcnOfLogger, ex);
/*     */       } 
/* 105 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4\\util\StackLocatorUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */