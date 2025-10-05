/*    */ package org.slf4j.impl;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ 
/*    */ 
/*    */ class OutputChoice
/*    */ {
/*    */   final OutputChoiceType outputChoiceType;
/*    */   final PrintStream targetPrintStream;
/*    */   
/*    */   enum OutputChoiceType
/*    */   {
/* 14 */     SYS_OUT, CACHED_SYS_OUT, SYS_ERR, CACHED_SYS_ERR, FILE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   OutputChoice(OutputChoiceType outputChoiceType) {
/* 21 */     if (outputChoiceType == OutputChoiceType.FILE) {
/* 22 */       throw new IllegalArgumentException();
/*    */     }
/* 24 */     this.outputChoiceType = outputChoiceType;
/* 25 */     if (outputChoiceType == OutputChoiceType.CACHED_SYS_OUT) {
/* 26 */       this.targetPrintStream = System.out;
/* 27 */     } else if (outputChoiceType == OutputChoiceType.CACHED_SYS_ERR) {
/* 28 */       this.targetPrintStream = System.err;
/*    */     } else {
/* 30 */       this.targetPrintStream = null;
/*    */     } 
/*    */   }
/*    */   
/*    */   OutputChoice(PrintStream printStream) {
/* 35 */     this.outputChoiceType = OutputChoiceType.FILE;
/* 36 */     this.targetPrintStream = printStream;
/*    */   }
/*    */   
/*    */   PrintStream getTargetPrintStream() {
/* 40 */     switch (this.outputChoiceType) {
/*    */       case SYS_OUT:
/* 42 */         return System.out;
/*    */       case SYS_ERR:
/* 44 */         return System.err;
/*    */       case CACHED_SYS_ERR:
/*    */       case CACHED_SYS_OUT:
/*    */       case FILE:
/* 48 */         return this.targetPrintStream;
/*    */     } 
/* 50 */     throw new IllegalArgumentException();
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\slf4j\impl\OutputChoice.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */