/*    */ package org.apache.logging.log4j.core;
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
/*    */ public class Version
/*    */ {
/*    */   public static void main(String[] args) {
/* 23 */     System.out.println(getProductString());
/*    */   }
/*    */   
/*    */   public static String getProductString() {
/* 27 */     Package pkg = Version.class.getPackage();
/* 28 */     if (pkg == null) {
/* 29 */       return "Apache Log4j";
/*    */     }
/* 31 */     return String.format("%s %s", new Object[] { pkg.getSpecificationTitle(), pkg.getSpecificationVersion() });
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\Version.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */