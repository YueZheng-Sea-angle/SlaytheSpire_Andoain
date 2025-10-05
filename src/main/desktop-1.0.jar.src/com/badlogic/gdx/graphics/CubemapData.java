package com.badlogic.gdx.graphics;

public interface CubemapData {
  boolean isPrepared();
  
  void prepare();
  
  void consumeCubemapData();
  
  int getWidth();
  
  int getHeight();
  
  boolean isManaged();
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\com\badlogic\gdx\graphics\CubemapData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */