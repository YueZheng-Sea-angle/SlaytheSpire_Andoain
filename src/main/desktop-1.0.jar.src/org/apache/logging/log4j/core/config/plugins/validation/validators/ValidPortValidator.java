/*    */ package org.apache.logging.log4j.core.config.plugins.validation.validators;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.core.config.plugins.convert.TypeConverters;
/*    */ import org.apache.logging.log4j.core.config.plugins.validation.ConstraintValidator;
/*    */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.ValidPort;
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
/*    */ public class ValidPortValidator
/*    */   implements ConstraintValidator<ValidPort>
/*    */ {
/* 32 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*    */   
/*    */   private ValidPort annotation;
/*    */ 
/*    */   
/*    */   public void initialize(ValidPort annotation) {
/* 38 */     this.annotation = annotation;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValid(String name, Object value) {
/* 43 */     if (value instanceof CharSequence) {
/* 44 */       return isValid(name, TypeConverters.convert(value.toString(), Integer.class, Integer.valueOf(-1)));
/*    */     }
/* 46 */     if (!Integer.class.isInstance(value)) {
/* 47 */       LOGGER.error(this.annotation.message());
/* 48 */       return false;
/*    */     } 
/* 50 */     int port = ((Integer)value).intValue();
/* 51 */     if (port < 0 || port > 65535) {
/* 52 */       LOGGER.error(this.annotation.message());
/* 53 */       return false;
/*    */     } 
/* 55 */     return true;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\plugins\validation\validators\ValidPortValidator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */