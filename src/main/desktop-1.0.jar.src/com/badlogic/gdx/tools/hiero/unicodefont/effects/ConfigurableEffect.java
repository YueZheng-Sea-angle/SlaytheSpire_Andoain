package com.badlogic.gdx.tools.hiero.unicodefont.effects;

import java.util.List;

public interface ConfigurableEffect extends Effect {
  List getValues();
  
  void setValues(List paramList);
  
  public static interface Value {
    String getName();
    
    void setString(String param1String);
    
    String getString();
    
    Object getObject();
    
    void showDialog();
  }
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\com\badlogic\gdx\tools\hier\\unicodefont\effects\ConfigurableEffect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */