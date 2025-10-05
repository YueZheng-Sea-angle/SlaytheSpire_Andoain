package org.lwjgl.util.vector;

import java.nio.FloatBuffer;

public interface ReadableVector {
  float length();
  
  float lengthSquared();
  
  Vector store(FloatBuffer paramFloatBuffer);
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjg\\util\vector\ReadableVector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */