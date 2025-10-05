/*    */ package org.apache.logging.log4j.core.config.plugins.validation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.reflect.ParameterizedType;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import org.apache.logging.log4j.core.util.ReflectionUtil;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ConstraintValidators
/*    */ {
/*    */   public static Collection<ConstraintValidator<?>> findValidators(Annotation... annotations) {
/* 45 */     Collection<ConstraintValidator<?>> validators = new ArrayList<>();
/*    */     
/* 47 */     for (Annotation annotation : annotations) {
/* 48 */       Class<? extends Annotation> type = annotation.annotationType();
/* 49 */       if (type.isAnnotationPresent((Class)Constraint.class)) {
/* 50 */         ConstraintValidator<?> validator = getValidator(annotation, type);
/* 51 */         if (validator != null) {
/* 52 */           validators.add(validator);
/*    */         }
/*    */       } 
/*    */     } 
/* 56 */     return validators;
/*    */   }
/*    */ 
/*    */   
/*    */   private static <A extends Annotation> ConstraintValidator<A> getValidator(A annotation, Class<? extends A> type) {
/* 61 */     Constraint constraint = type.<Constraint>getAnnotation(Constraint.class);
/* 62 */     Class<? extends ConstraintValidator<?>> validatorClass = constraint.value();
/* 63 */     if (type.equals(getConstraintValidatorAnnotationType(validatorClass))) {
/*    */ 
/*    */       
/* 66 */       ConstraintValidator<A> validator = (ConstraintValidator<A>)ReflectionUtil.instantiate(validatorClass);
/* 67 */       validator.initialize(annotation);
/* 68 */       return validator;
/*    */     } 
/* 70 */     return null;
/*    */   }
/*    */   
/*    */   private static Type getConstraintValidatorAnnotationType(Class<? extends ConstraintValidator<?>> type) {
/* 74 */     for (Type parentType : type.getGenericInterfaces()) {
/* 75 */       if (parentType instanceof ParameterizedType) {
/* 76 */         ParameterizedType parameterizedType = (ParameterizedType)parentType;
/* 77 */         if (ConstraintValidator.class.equals(parameterizedType.getRawType())) {
/* 78 */           return parameterizedType.getActualTypeArguments()[0];
/*    */         }
/*    */       } 
/*    */     } 
/* 82 */     return void.class;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\plugins\validation\ConstraintValidators.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */