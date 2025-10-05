package org.lwjgl.util.glu.tessellation;

class GLUface {
  public GLUface next;
  
  public GLUface prev;
  
  public GLUhalfEdge anEdge;
  
  public Object data;
  
  public GLUface trail;
  
  public boolean marked;
  
  public boolean inside;
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjg\\util\glu\tessellation\GLUface.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */