/*    */ package org.apache.logging.log4j.util;
/*    */ 
/*    */ import java.util.Stack;
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
/*    */ final class PrivateSecurityManagerStackTraceUtil
/*    */ {
/*    */   private static final PrivateSecurityManager SECURITY_MANAGER;
/*    */   
/*    */   static {
/*    */     PrivateSecurityManager psm;
/*    */     try {
/* 32 */       SecurityManager sm = System.getSecurityManager();
/* 33 */       if (sm != null) {
/* 34 */         sm.checkPermission(new RuntimePermission("createSecurityManager"));
/*    */       }
/* 36 */       psm = new PrivateSecurityManager();
/* 37 */     } catch (SecurityException ignored) {
/* 38 */       psm = null;
/*    */     } 
/*    */     
/* 41 */     SECURITY_MANAGER = psm;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static boolean isEnabled() {
/* 49 */     return (SECURITY_MANAGER != null);
/*    */   }
/*    */ 
/*    */   
/*    */   static Stack<Class<?>> getCurrentStackTrace() {
/* 54 */     Class<?>[] array = SECURITY_MANAGER.getClassContext();
/* 55 */     Stack<Class<?>> classes = new Stack<>();
/* 56 */     classes.ensureCapacity(array.length);
/* 57 */     for (Class<?> clazz : array) {
/* 58 */       classes.push(clazz);
/*    */     }
/* 60 */     return classes;
/*    */   }
/*    */   
/*    */   private static final class PrivateSecurityManager extends SecurityManager {
/*    */     private PrivateSecurityManager() {}
/*    */     
/*    */     protected Class<?>[] getClassContext() {
/* 67 */       return super.getClassContext();
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4\\util\PrivateSecurityManagerStackTraceUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */