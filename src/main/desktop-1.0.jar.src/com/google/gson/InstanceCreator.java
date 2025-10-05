package com.google.gson;

import java.lang.reflect.Type;

public interface InstanceCreator<T> {
  T createInstance(Type paramType);
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\com\google\gson\InstanceCreator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */