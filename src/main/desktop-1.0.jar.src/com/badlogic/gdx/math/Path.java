package com.badlogic.gdx.math;

public interface Path<T> {
  T derivativeAt(T paramT, float paramFloat);
  
  T valueAt(T paramT, float paramFloat);
  
  float approximate(T paramT);
  
  float locate(T paramT);
  
  float approxLength(int paramInt);
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\com\badlogic\gdx\math\Path.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */