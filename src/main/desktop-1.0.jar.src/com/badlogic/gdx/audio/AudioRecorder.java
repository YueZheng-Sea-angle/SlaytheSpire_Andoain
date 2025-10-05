package com.badlogic.gdx.audio;

import com.badlogic.gdx.utils.Disposable;

public interface AudioRecorder extends Disposable {
  void read(short[] paramArrayOfshort, int paramInt1, int paramInt2);
  
  void dispose();
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\com\badlogic\gdx\audio\AudioRecorder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */