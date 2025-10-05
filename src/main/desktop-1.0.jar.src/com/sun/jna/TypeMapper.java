package com.sun.jna;

public interface TypeMapper {
  FromNativeConverter getFromNativeConverter(Class<?> paramClass);
  
  ToNativeConverter getToNativeConverter(Class<?> paramClass);
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\com\sun\jna\TypeMapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */