package com.badlogic.gdx.graphics.g3d.particles.batches;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.particles.ResourceData;

public interface ParticleBatch<T extends com.badlogic.gdx.graphics.g3d.particles.renderers.ParticleControllerRenderData> extends RenderableProvider, ResourceData.Configurable {
  void begin();
  
  void draw(T paramT);
  
  void end();
  
  void save(AssetManager paramAssetManager, ResourceData paramResourceData);
  
  void load(AssetManager paramAssetManager, ResourceData paramResourceData);
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\com\badlogic\gdx\graphics\g3d\particles\batches\ParticleBatch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */