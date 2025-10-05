/*     */ package org.apache.commons.net.telnet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TerminalTypeOptionHandler
/*     */   extends TelnetOptionHandler
/*     */ {
/*     */   private final String termType;
/*     */   protected static final int TERMINAL_TYPE = 24;
/*     */   protected static final int TERMINAL_TYPE_SEND = 1;
/*     */   protected static final int TERMINAL_TYPE_IS = 0;
/*     */   
/*     */   public TerminalTypeOptionHandler(String termtype, boolean initlocal, boolean initremote, boolean acceptlocal, boolean acceptremote) {
/*  63 */     super(24, initlocal, initremote, acceptlocal, acceptremote);
/*     */     
/*  65 */     this.termType = termtype;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TerminalTypeOptionHandler(String termtype) {
/*  76 */     super(24, false, false, false, false);
/*  77 */     this.termType = termtype;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] answerSubnegotiation(int[] suboptionData, int suboptionLength) {
/*  91 */     if (suboptionData != null && suboptionLength > 1 && this.termType != null)
/*     */     {
/*     */       
/*  94 */       if (suboptionData[0] == 24 && suboptionData[1] == 1) {
/*     */ 
/*     */         
/*  97 */         int[] response = new int[this.termType.length() + 2];
/*     */         
/*  99 */         response[0] = 24;
/* 100 */         response[1] = 0;
/*     */         
/* 102 */         for (int ii = 0; ii < this.termType.length(); ii++)
/*     */         {
/* 104 */           response[ii + 2] = this.termType.charAt(ii);
/*     */         }
/*     */         
/* 107 */         return response;
/*     */       } 
/*     */     }
/* 110 */     return null;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\telnet\TerminalTypeOptionHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */