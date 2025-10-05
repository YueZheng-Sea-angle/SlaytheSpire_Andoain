/*     */ package org.apache.logging.log4j.spi;
/*     */ 
/*     */ import org.apache.logging.log4j.message.MessageFactory;
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
/*     */ public interface LoggerContext
/*     */ {
/*     */   Object getExternalContext();
/*     */   
/*     */   default Object getObject(String key) {
/*  38 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default Object putObject(String key, Object value) {
/*  49 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default Object putObjectIfAbsent(String key, Object value) {
/*  60 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default Object removeObject(String key) {
/*  70 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default boolean removeObject(String key, Object value) {
/*  81 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ExtendedLogger getLogger(String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default ExtendedLogger getLogger(Class<?> cls) {
/*  98 */     String canonicalName = cls.getCanonicalName();
/*  99 */     return getLogger((canonicalName != null) ? canonicalName : cls.getName());
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
/*     */   ExtendedLogger getLogger(String paramString, MessageFactory paramMessageFactory);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default ExtendedLogger getLogger(Class<?> cls, MessageFactory messageFactory) {
/* 120 */     String canonicalName = cls.getCanonicalName();
/* 121 */     return getLogger((canonicalName != null) ? canonicalName : cls.getName(), messageFactory);
/*     */   }
/*     */   
/*     */   boolean hasLogger(String paramString);
/*     */   
/*     */   boolean hasLogger(String paramString, MessageFactory paramMessageFactory);
/*     */   
/*     */   boolean hasLogger(String paramString, Class<? extends MessageFactory> paramClass);
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\spi\LoggerContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */