/*    */ package org.apache.logging.log4j.util;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Method;
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
/*    */ public class ProcessIdUtil
/*    */ {
/*    */   public static final String DEFAULT_PROCESSID = "-";
/*    */   
/*    */   public static String getProcessId() {
/*    */     try {
/* 33 */       Class<?> managementFactoryClass = Class.forName("java.lang.management.ManagementFactory");
/* 34 */       Method getRuntimeMXBean = managementFactoryClass.getDeclaredMethod("getRuntimeMXBean", new Class[0]);
/* 35 */       Class<?> runtimeMXBeanClass = Class.forName("java.lang.management.RuntimeMXBean");
/* 36 */       Method getName = runtimeMXBeanClass.getDeclaredMethod("getName", new Class[0]);
/*    */       
/* 38 */       Object runtimeMXBean = getRuntimeMXBean.invoke(null, new Object[0]);
/* 39 */       String name = (String)getName.invoke(runtimeMXBean, new Object[0]);
/*    */       
/* 41 */       return name.split("@")[0];
/* 42 */     } catch (Exception ex) {
/*    */       try {
/* 44 */         return (new File("/proc/self")).getCanonicalFile().getName();
/* 45 */       } catch (IOException iOException) {
/*    */ 
/*    */ 
/*    */         
/* 49 */         return "-";
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4\\util\ProcessIdUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */