package com.badlogic.gdx.graphics.g3d.environment;

import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;
import com.badlogic.gdx.math.Matrix4;

public interface ShadowMap {
  Matrix4 getProjViewTrans();
  
  TextureDescriptor getDepthMap();
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\com\badlogic\gdx\graphics\g3d\environment\ShadowMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */