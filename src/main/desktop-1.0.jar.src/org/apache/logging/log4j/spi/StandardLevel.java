/*    */ package org.apache.logging.log4j.spi;
/*    */ 
/*    */ import java.util.EnumSet;
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
/*    */ public enum StandardLevel
/*    */ {
/* 29 */   OFF(0),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   FATAL(100),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 39 */   ERROR(200),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 44 */   WARN(300),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 49 */   INFO(400),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 54 */   DEBUG(500),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 59 */   TRACE(600),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 64 */   ALL(2147483647); private static final EnumSet<StandardLevel> LEVELSET;
/*    */   static {
/* 66 */     LEVELSET = EnumSet.allOf(StandardLevel.class);
/*    */   }
/*    */   private final int intLevel;
/*    */   
/*    */   StandardLevel(int val) {
/* 71 */     this.intLevel = val;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int intLevel() {
/* 80 */     return this.intLevel;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static StandardLevel getStandardLevel(int intLevel) {
/* 90 */     StandardLevel level = OFF;
/* 91 */     for (StandardLevel lvl : LEVELSET) {
/* 92 */       if (lvl.intLevel() > intLevel) {
/*    */         break;
/*    */       }
/* 95 */       level = lvl;
/*    */     } 
/* 97 */     return level;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\spi\StandardLevel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */