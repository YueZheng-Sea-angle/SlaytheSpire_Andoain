/*    */ package org.apache.logging.log4j.core.layout.internal;
/*    */ 
/*    */ import java.util.List;
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
/*    */ public class ExcludeChecker
/*    */   implements ListChecker
/*    */ {
/*    */   private final List<String> list;
/*    */   
/*    */   public ExcludeChecker(List<String> list) {
/* 28 */     this.list = list;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean check(String key) {
/* 33 */     return !this.list.contains(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 38 */     return "ThreadContextExcludes=" + this.list.toString();
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\layout\internal\ExcludeChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */