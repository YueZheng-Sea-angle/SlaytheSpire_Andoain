package org.apache.logging.log4j.spi;

import java.util.Map;
import org.apache.logging.log4j.util.StringMap;

public interface ReadOnlyThreadContextMap {
  void clear();
  
  boolean containsKey(String paramString);
  
  String get(String paramString);
  
  Map<String, String> getCopy();
  
  Map<String, String> getImmutableMapOrNull();
  
  StringMap getReadOnlyContextData();
  
  boolean isEmpty();
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\spi\ReadOnlyThreadContextMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */