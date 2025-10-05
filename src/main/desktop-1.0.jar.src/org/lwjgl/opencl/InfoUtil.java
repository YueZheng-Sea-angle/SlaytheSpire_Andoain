package org.lwjgl.opencl;

interface InfoUtil<T extends CLObject> {
  int getInfoInt(T paramT, int paramInt);
  
  long getInfoSize(T paramT, int paramInt);
  
  long[] getInfoSizeArray(T paramT, int paramInt);
  
  long getInfoLong(T paramT, int paramInt);
  
  String getInfoString(T paramT, int paramInt);
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opencl\InfoUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */