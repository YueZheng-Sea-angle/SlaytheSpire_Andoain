package com.badlogic.gdx.assets;

public class AssetLoaderParameters<T> {
  public LoadedCallback loadedCallback;
  
  public static interface LoadedCallback {
    void finishedLoading(AssetManager param1AssetManager, String param1String, Class param1Class);
  }
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\com\badlogic\gdx\assets\AssetLoaderParameters.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */