/*    */ package org.apache.logging.log4j.core.config.plugins.validation.validators;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.net.InetAddress;
/*    */ import java.net.UnknownHostException;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.core.config.plugins.validation.ConstraintValidator;
/*    */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.ValidHost;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
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
/*    */ public class ValidHostValidator
/*    */   implements ConstraintValidator<ValidHost>
/*    */ {
/* 35 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*    */   
/*    */   private ValidHost annotation;
/*    */ 
/*    */   
/*    */   public void initialize(ValidHost annotation) {
/* 41 */     this.annotation = annotation;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValid(String name, Object value) {
/* 46 */     if (value == null) {
/* 47 */       LOGGER.error(this.annotation.message());
/* 48 */       return false;
/*    */     } 
/* 50 */     if (value instanceof InetAddress)
/*    */     {
/* 52 */       return true;
/*    */     }
/*    */     try {
/* 55 */       InetAddress.getByName(value.toString());
/* 56 */       return true;
/* 57 */     } catch (UnknownHostException e) {
/* 58 */       LOGGER.error(this.annotation.message(), e);
/* 59 */       return false;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\plugins\validation\validators\ValidHostValidator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */