/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
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
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public final class ReusableMessageFactory
/*     */   implements MessageFactory2, Serializable
/*     */ {
/*  38 */   public static final ReusableMessageFactory INSTANCE = new ReusableMessageFactory();
/*     */   
/*     */   private static final long serialVersionUID = -8970940216592525651L;
/*  41 */   private static ThreadLocal<ReusableParameterizedMessage> threadLocalParameterized = new ThreadLocal<>();
/*  42 */   private static ThreadLocal<ReusableSimpleMessage> threadLocalSimpleMessage = new ThreadLocal<>();
/*  43 */   private static ThreadLocal<ReusableObjectMessage> threadLocalObjectMessage = new ThreadLocal<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ReusableParameterizedMessage getParameterized() {
/*  52 */     ReusableParameterizedMessage result = threadLocalParameterized.get();
/*  53 */     if (result == null) {
/*  54 */       result = new ReusableParameterizedMessage();
/*  55 */       threadLocalParameterized.set(result);
/*     */     } 
/*  57 */     return result.reserved ? (new ReusableParameterizedMessage()).reserve() : result.reserve();
/*     */   }
/*     */   
/*     */   private static ReusableSimpleMessage getSimple() {
/*  61 */     ReusableSimpleMessage result = threadLocalSimpleMessage.get();
/*  62 */     if (result == null) {
/*  63 */       result = new ReusableSimpleMessage();
/*  64 */       threadLocalSimpleMessage.set(result);
/*     */     } 
/*  66 */     return result;
/*     */   }
/*     */   
/*     */   private static ReusableObjectMessage getObject() {
/*  70 */     ReusableObjectMessage result = threadLocalObjectMessage.get();
/*  71 */     if (result == null) {
/*  72 */       result = new ReusableObjectMessage();
/*  73 */       threadLocalObjectMessage.set(result);
/*     */     } 
/*  75 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void release(Message message) {
/*  86 */     if (message instanceof Clearable) {
/*  87 */       ((Clearable)message).clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Message newMessage(CharSequence charSequence) {
/*  93 */     ReusableSimpleMessage result = getSimple();
/*  94 */     result.set(charSequence);
/*  95 */     return result;
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
/*     */   public Message newMessage(String message, Object... params) {
/* 109 */     return getParameterized().set(message, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public Message newMessage(String message, Object p0) {
/* 114 */     return getParameterized().set(message, p0);
/*     */   }
/*     */ 
/*     */   
/*     */   public Message newMessage(String message, Object p0, Object p1) {
/* 119 */     return getParameterized().set(message, p0, p1);
/*     */   }
/*     */ 
/*     */   
/*     */   public Message newMessage(String message, Object p0, Object p1, Object p2) {
/* 124 */     return getParameterized().set(message, p0, p1, p2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3) {
/* 130 */     return getParameterized().set(message, p0, p1, p2, p3);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 136 */     return getParameterized().set(message, p0, p1, p2, p3, p4);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 142 */     return getParameterized().set(message, p0, p1, p2, p3, p4, p5);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 148 */     return getParameterized().set(message, p0, p1, p2, p3, p4, p5, p6);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 154 */     return getParameterized().set(message, p0, p1, p2, p3, p4, p5, p6, p7);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 160 */     return getParameterized().set(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 166 */     return getParameterized().set(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
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
/*     */   public Message newMessage(String message) {
/* 179 */     ReusableSimpleMessage result = getSimple();
/* 180 */     result.set(message);
/* 181 */     return result;
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
/*     */   public Message newMessage(Object message) {
/* 195 */     ReusableObjectMessage result = getObject();
/* 196 */     result.set(message);
/* 197 */     return result;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\message\ReusableMessageFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */