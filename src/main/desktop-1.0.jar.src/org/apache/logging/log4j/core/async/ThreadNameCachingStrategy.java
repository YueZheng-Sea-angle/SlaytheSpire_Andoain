/*    */ package org.apache.logging.log4j.core.async;
/*    */ 
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
/*    */ import org.apache.logging.log4j.util.Constants;
/*    */ import org.apache.logging.log4j.util.PropertiesUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum ThreadNameCachingStrategy
/*    */ {
/* 31 */   CACHED
/*    */   {
/*    */     public String getThreadName() {
/* 34 */       String result = ThreadNameCachingStrategy.THREADLOCAL_NAME.get();
/* 35 */       if (result == null) {
/* 36 */         result = Thread.currentThread().getName();
/* 37 */         ThreadNameCachingStrategy.THREADLOCAL_NAME.set(result);
/*    */       } 
/* 39 */       return result;
/*    */     }
/*    */   },
/* 42 */   UNCACHED
/*    */   {
/*    */     public String getThreadName() {
/* 45 */       return Thread.currentThread().getName();
/*    */     } };
/*    */   private static final StatusLogger LOGGER;
/*    */   static {
/* 49 */     LOGGER = StatusLogger.getLogger();
/* 50 */     THREADLOCAL_NAME = new ThreadLocal<>();
/* 51 */     DEFAULT_STRATEGY = isAllocatingThreadGetName() ? CACHED : UNCACHED;
/*    */   }
/*    */   private static final ThreadLocal<String> THREADLOCAL_NAME; static final ThreadNameCachingStrategy DEFAULT_STRATEGY;
/*    */   
/*    */   public static ThreadNameCachingStrategy create() {
/* 56 */     String name = PropertiesUtil.getProperties().getStringProperty("AsyncLogger.ThreadNameStrategy");
/*    */     try {
/* 58 */       ThreadNameCachingStrategy result = (name != null) ? valueOf(name) : DEFAULT_STRATEGY;
/* 59 */       LOGGER.debug("AsyncLogger.ThreadNameStrategy={} (user specified {}, default is {})", result
/* 60 */           .name(), name, DEFAULT_STRATEGY.name());
/* 61 */       return result;
/* 62 */     } catch (Exception ex) {
/* 63 */       LOGGER.debug("Using AsyncLogger.ThreadNameStrategy.{}: '{}' not valid: {}", DEFAULT_STRATEGY
/* 64 */           .name(), name, ex.toString());
/* 65 */       return DEFAULT_STRATEGY;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   static boolean isAllocatingThreadGetName() {
/* 71 */     if (Constants.JAVA_MAJOR_VERSION == 8) {
/*    */       try {
/* 73 */         Pattern javaVersionPattern = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)_(\\d+)");
/* 74 */         Matcher m = javaVersionPattern.matcher(System.getProperty("java.version"));
/* 75 */         if (m.matches()) {
/* 76 */           return (Integer.parseInt(m.group(3)) == 0 && Integer.parseInt(m.group(4)) < 102);
/*    */         }
/* 78 */         return true;
/* 79 */       } catch (Exception e) {
/* 80 */         return true;
/*    */       } 
/*    */     }
/* 83 */     return (Constants.JAVA_MAJOR_VERSION < 8);
/*    */   }
/*    */   
/*    */   abstract String getThreadName();
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\async\ThreadNameCachingStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */