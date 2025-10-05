/*    */ package org.lwjgl.input;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Method;
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedActionException;
/*    */ import java.security.PrivilegedExceptionAction;
/*    */ import org.lwjgl.opengl.Display;
/*    */ import org.lwjgl.opengl.InputImplementation;
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
/*    */ final class OpenGLPackageAccess
/*    */ {
/*    */   static final Object global_lock;
/*    */   
/*    */   static {
/*    */     try {
/* 52 */       global_lock = AccessController.doPrivileged(new PrivilegedExceptionAction() {
/*    */             public Object run() throws Exception {
/* 54 */               Field lock_field = Class.forName("org.lwjgl.opengl.GlobalLock").getDeclaredField("lock");
/* 55 */               lock_field.setAccessible(true);
/* 56 */               return lock_field.get(null);
/*    */             }
/*    */           });
/* 59 */     } catch (PrivilegedActionException e) {
/* 60 */       throw new Error(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static InputImplementation createImplementation() {
/*    */     try {
/* 69 */       return AccessController.<InputImplementation>doPrivileged(new PrivilegedExceptionAction<InputImplementation>() {
/*    */             public InputImplementation run() throws Exception {
/* 71 */               Method getImplementation_method = Display.class.getDeclaredMethod("getImplementation", new Class[0]);
/* 72 */               getImplementation_method.setAccessible(true);
/* 73 */               return (InputImplementation)getImplementation_method.invoke(null, new Object[0]);
/*    */             }
/*    */           });
/* 76 */     } catch (PrivilegedActionException e) {
/* 77 */       throw new Error(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\input\OpenGLPackageAccess.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */