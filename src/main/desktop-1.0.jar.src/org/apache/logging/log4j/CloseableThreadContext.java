/*     */ package org.apache.logging.log4j;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class CloseableThreadContext
/*     */ {
/*     */   public static Instance push(String message) {
/*  50 */     return (new Instance()).push(message);
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
/*     */   public static Instance push(String message, Object... args) {
/*  62 */     return (new Instance()).push(message, args);
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
/*     */   public static Instance put(String key, String value) {
/*  75 */     return (new Instance()).put(key, value);
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
/*     */   public static Instance pushAll(List<String> messages) {
/*  87 */     return (new Instance()).pushAll(messages);
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
/*     */   public static Instance putAll(Map<String, String> values) {
/* 100 */     return (new Instance()).putAll(values);
/*     */   }
/*     */   
/*     */   public static class Instance
/*     */     implements AutoCloseable {
/* 105 */     private int pushCount = 0;
/* 106 */     private final Map<String, String> originalValues = new HashMap<>();
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
/*     */     public Instance push(String message) {
/* 119 */       ThreadContext.push(message);
/* 120 */       this.pushCount++;
/* 121 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Instance push(String message, Object[] args) {
/* 133 */       ThreadContext.push(message, args);
/* 134 */       this.pushCount++;
/* 135 */       return this;
/*     */     }
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
/*     */     public Instance put(String key, String value) {
/* 149 */       if (!this.originalValues.containsKey(key)) {
/* 150 */         this.originalValues.put(key, ThreadContext.get(key));
/*     */       }
/* 152 */       ThreadContext.put(key, value);
/* 153 */       return this;
/*     */     }
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
/*     */     public Instance putAll(Map<String, String> values) {
/* 166 */       Map<String, String> currentValues = ThreadContext.getContext();
/* 167 */       ThreadContext.putAll(values);
/* 168 */       for (String key : values.keySet()) {
/* 169 */         if (!this.originalValues.containsKey(key)) {
/* 170 */           this.originalValues.put(key, currentValues.get(key));
/*     */         }
/*     */       } 
/* 173 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Instance pushAll(List<String> messages) {
/* 185 */       for (String message : messages) {
/* 186 */         push(message);
/*     */       }
/* 188 */       return this;
/*     */     }
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
/*     */     public void close() {
/* 202 */       closeStack();
/* 203 */       closeMap();
/*     */     }
/*     */     
/*     */     private void closeMap() {
/* 207 */       for (Iterator<Map.Entry<String, String>> it = this.originalValues.entrySet().iterator(); it.hasNext(); ) {
/* 208 */         Map.Entry<String, String> entry = it.next();
/* 209 */         String key = entry.getKey();
/* 210 */         String originalValue = entry.getValue();
/* 211 */         if (null == originalValue) {
/* 212 */           ThreadContext.remove(key);
/*     */         } else {
/* 214 */           ThreadContext.put(key, originalValue);
/*     */         } 
/* 216 */         it.remove();
/*     */       } 
/*     */     }
/*     */     
/*     */     private void closeStack() {
/* 221 */       for (int i = 0; i < this.pushCount; i++) {
/* 222 */         ThreadContext.pop();
/*     */       }
/* 224 */       this.pushCount = 0;
/*     */     }
/*     */     
/*     */     private Instance() {}
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\CloseableThreadContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */