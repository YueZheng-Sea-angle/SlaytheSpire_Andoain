package com.badlogic.gdx.graphics.glutils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Matrix4;

public interface ImmediateModeRenderer {
  void begin(Matrix4 paramMatrix4, int paramInt);
  
  void flush();
  
  void color(Color paramColor);
  
  void color(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);
  
  void color(float paramFloat);
  
  void texCoord(float paramFloat1, float paramFloat2);
  
  void normal(float paramFloat1, float paramFloat2, float paramFloat3);
  
  void vertex(float paramFloat1, float paramFloat2, float paramFloat3);
  
  void end();
  
  int getNumVertices();
  
  int getMaxVertices();
  
  void dispose();
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\com\badlogic\gdx\graphics\glutils\ImmediateModeRenderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */