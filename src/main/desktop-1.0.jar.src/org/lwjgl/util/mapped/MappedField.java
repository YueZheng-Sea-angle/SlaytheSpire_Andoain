package org.lwjgl.util.mapped;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface MappedField {
  long byteOffset() default -1L;
  
  long byteLength() default -1L;
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjg\\util\mapped\MappedField.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */