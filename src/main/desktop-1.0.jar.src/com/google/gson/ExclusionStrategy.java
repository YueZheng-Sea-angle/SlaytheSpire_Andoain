package com.google.gson;

public interface ExclusionStrategy {
  boolean shouldSkipField(FieldAttributes paramFieldAttributes);
  
  boolean shouldSkipClass(Class<?> paramClass);
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\com\google\gson\ExclusionStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */