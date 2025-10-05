package com.badlogic.gdx.audio;

import com.badlogic.gdx.utils.Disposable;

public interface AudioDevice extends Disposable {
  boolean isMono();
  
  void writeSamples(short[] paramArrayOfshort, int paramInt1, int paramInt2);
  
  void writeSamples(float[] paramArrayOffloat, int paramInt1, int paramInt2);
  
  int getLatency();
  
  void dispose();
  
  void setVolume(float paramFloat);
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\com\badlogic\gdx\audio\AudioDevice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */