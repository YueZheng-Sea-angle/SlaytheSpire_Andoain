package com.google.gson.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface SerializedName {
  String value();
  
  String[] alternate() default {};
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\com\google\gson\annotations\SerializedName.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */