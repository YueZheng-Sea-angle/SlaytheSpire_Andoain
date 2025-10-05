/*    */ package org.apache.logging.log4j.message;
/*    */ 
/*    */ import java.util.ResourceBundle;
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
/*    */ 
/*    */ 
/*    */ public class LocalizedMessageFactory
/*    */   extends AbstractMessageFactory
/*    */ {
/*    */   private static final long serialVersionUID = -1996295808703146741L;
/*    */   private final transient ResourceBundle resourceBundle;
/*    */   private final String baseName;
/*    */   
/*    */   public LocalizedMessageFactory(ResourceBundle resourceBundle) {
/* 39 */     this.resourceBundle = resourceBundle;
/* 40 */     this.baseName = null;
/*    */   }
/*    */   
/*    */   public LocalizedMessageFactory(String baseName) {
/* 44 */     this.resourceBundle = null;
/* 45 */     this.baseName = baseName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getBaseName() {
/* 54 */     return this.baseName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ResourceBundle getResourceBundle() {
/* 63 */     return this.resourceBundle;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Message newMessage(String key) {
/* 71 */     if (this.resourceBundle == null) {
/* 72 */       return new LocalizedMessage(this.baseName, key);
/*    */     }
/* 74 */     return new LocalizedMessage(this.resourceBundle, key);
/*    */   }
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
/*    */   public Message newMessage(String key, Object... params) {
/* 88 */     if (this.resourceBundle == null) {
/* 89 */       return new LocalizedMessage(this.baseName, key, params);
/*    */     }
/* 91 */     return new LocalizedMessage(this.resourceBundle, key, params);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\message\LocalizedMessageFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */