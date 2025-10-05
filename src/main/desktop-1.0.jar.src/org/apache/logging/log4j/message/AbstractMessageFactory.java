/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.io.Serializable;
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
/*     */ public abstract class AbstractMessageFactory
/*     */   implements MessageFactory2, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -1307891137684031187L;
/*     */   
/*     */   public Message newMessage(CharSequence message) {
/*  39 */     return new SimpleMessage(message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Message newMessage(Object message) {
/*  49 */     return new ObjectMessage(message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Message newMessage(String message) {
/*  59 */     return new SimpleMessage(message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Message newMessage(String message, Object p0) {
/*  67 */     return newMessage(message, new Object[] { p0 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Message newMessage(String message, Object p0, Object p1) {
/*  75 */     return newMessage(message, new Object[] { p0, p1 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Message newMessage(String message, Object p0, Object p1, Object p2) {
/*  83 */     return newMessage(message, new Object[] { p0, p1, p2 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3) {
/*  91 */     return newMessage(message, new Object[] { p0, p1, p2, p3 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/*  99 */     return newMessage(message, new Object[] { p0, p1, p2, p3, p4 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 107 */     return newMessage(message, new Object[] { p0, p1, p2, p3, p4, p5 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 116 */     return newMessage(message, new Object[] { p0, p1, p2, p3, p4, p5, p6 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 125 */     return newMessage(message, new Object[] { p0, p1, p2, p3, p4, p5, p6, p7 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 134 */     return newMessage(message, new Object[] { p0, p1, p2, p3, p4, p5, p6, p7, p8 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 143 */     return newMessage(message, new Object[] { p0, p1, p2, p3, p4, p5, p6, p7, p8, p9 });
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\message\AbstractMessageFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */