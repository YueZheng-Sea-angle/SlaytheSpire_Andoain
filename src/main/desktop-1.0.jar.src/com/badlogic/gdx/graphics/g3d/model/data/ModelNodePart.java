package com.badlogic.gdx.graphics.g3d.model.data;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.ArrayMap;

public class ModelNodePart {
  public String materialId;
  
  public String meshPartId;
  
  public ArrayMap<String, Matrix4> bones;
  
  public int[][] uvMapping;
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\com\badlogic\gdx\graphics\g3d\model\data\ModelNodePart.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */