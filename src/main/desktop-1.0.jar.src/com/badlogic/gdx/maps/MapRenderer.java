package com.badlogic.gdx.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;

public interface MapRenderer {
  void setView(OrthographicCamera paramOrthographicCamera);
  
  void setView(Matrix4 paramMatrix4, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);
  
  void render();
  
  void render(int[] paramArrayOfint);
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\com\badlogic\gdx\maps\MapRenderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */