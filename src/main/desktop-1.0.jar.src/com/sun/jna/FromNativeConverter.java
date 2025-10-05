package com.sun.jna;

public interface FromNativeConverter {
  Object fromNative(Object paramObject, FromNativeContext paramFromNativeContext);
  
  Class<?> nativeType();
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\com\sun\jna\FromNativeConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */