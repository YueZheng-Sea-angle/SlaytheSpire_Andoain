/*    */ package META-INF.versions.9.org.apache.logging.log4j.util;
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
/* 25 */       return Long.toString(ProcessHandle.current().pid());
/* 26 */     } catch (Exception ex) {
/* 27 */       return "-";
/*    */     } 
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\META-INF\versions\9\org\apache\logging\log4\\util\ProcessIdUtil.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */