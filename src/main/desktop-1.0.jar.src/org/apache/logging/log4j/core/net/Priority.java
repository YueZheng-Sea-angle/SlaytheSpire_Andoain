/*    */ package org.apache.logging.log4j.core.net;
/*    */ 
/*    */ import org.apache.logging.log4j.Level;
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
/*    */ public class Priority
/*    */ {
/*    */   private final Facility facility;
/*    */   private final Severity severity;
/*    */   
/*    */   public Priority(Facility facility, Severity severity) {
/* 35 */     this.facility = facility;
/* 36 */     this.severity = severity;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int getPriority(Facility facility, Level level) {
/* 46 */     return toPriority(facility, Severity.getSeverity(level));
/*    */   }
/*    */   
/*    */   private static int toPriority(Facility aFacility, Severity aSeverity) {
/* 50 */     return (aFacility.getCode() << 3) + aSeverity.getCode();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Facility getFacility() {
/* 58 */     return this.facility;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Severity getSeverity() {
/* 66 */     return this.severity;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getValue() {
/* 74 */     return toPriority(this.facility, this.severity);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 79 */     return Integer.toString(getValue());
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\net\Priority.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */