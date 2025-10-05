package org.apache.logging.log4j.util;

import java.io.Serializable;
import java.util.Map;

public interface ReadOnlyStringMap extends Serializable {
  Map<String, String> toMap();
  
  boolean containsKey(String paramString);
  
  <V> void forEach(BiConsumer<String, ? super V> paramBiConsumer);
  
  <V, S> void forEach(TriConsumer<String, ? super V, S> paramTriConsumer, S paramS);
  
  <V> V getValue(String paramString);
  
  boolean isEmpty();
  
  int size();
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4\\util\ReadOnlyStringMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */