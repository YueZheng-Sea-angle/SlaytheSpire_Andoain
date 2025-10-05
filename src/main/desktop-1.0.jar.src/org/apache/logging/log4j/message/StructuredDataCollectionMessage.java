/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.util.StringBuilderFormattable;
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
/*     */ public class StructuredDataCollectionMessage
/*     */   implements StringBuilderFormattable, MessageCollectionMessage<StructuredDataMessage>
/*     */ {
/*     */   private static final long serialVersionUID = 5725337076388822924L;
/*     */   private final List<StructuredDataMessage> structuredDataMessageList;
/*     */   
/*     */   public StructuredDataCollectionMessage(List<StructuredDataMessage> messages) {
/*  35 */     this.structuredDataMessageList = messages;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<StructuredDataMessage> iterator() {
/*  40 */     return this.structuredDataMessageList.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFormattedMessage() {
/*  45 */     StringBuilder sb = new StringBuilder();
/*  46 */     formatTo(sb);
/*  47 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFormat() {
/*  52 */     StringBuilder sb = new StringBuilder();
/*  53 */     for (StructuredDataMessage msg : this.structuredDataMessageList) {
/*  54 */       if (msg.getFormat() != null) {
/*  55 */         if (sb.length() > 0) {
/*  56 */           sb.append(", ");
/*     */         }
/*  58 */         sb.append(msg.getFormat());
/*     */       } 
/*     */     } 
/*  61 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void formatTo(StringBuilder buffer) {
/*  66 */     for (StructuredDataMessage msg : this.structuredDataMessageList) {
/*  67 */       msg.formatTo(buffer);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] getParameters() {
/*  73 */     List<Object[]> objectList = new ArrayList();
/*  74 */     int count = 0;
/*  75 */     for (StructuredDataMessage msg : this.structuredDataMessageList) {
/*  76 */       Object[] arrayOfObject = msg.getParameters();
/*  77 */       if (arrayOfObject != null) {
/*  78 */         objectList.add(arrayOfObject);
/*  79 */         count += arrayOfObject.length;
/*     */       } 
/*     */     } 
/*  82 */     Object[] objects = new Object[count];
/*  83 */     int index = 0;
/*  84 */     for (Object[] objs : objectList) {
/*  85 */       for (Object obj : objs) {
/*  86 */         objects[index++] = obj;
/*     */       }
/*     */     } 
/*  89 */     return objects;
/*     */   }
/*     */ 
/*     */   
/*     */   public Throwable getThrowable() {
/*  94 */     for (StructuredDataMessage msg : this.structuredDataMessageList) {
/*  95 */       Throwable t = msg.getThrowable();
/*  96 */       if (t != null) {
/*  97 */         return t;
/*     */       }
/*     */     } 
/* 100 */     return null;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\message\StructuredDataCollectionMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */