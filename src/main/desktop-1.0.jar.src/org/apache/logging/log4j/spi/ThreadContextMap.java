package org.apache.logging.log4j.spi;

import java.util.Map;

public interface ThreadContextMap {
  void clear();
  
  boolean containsKey(String paramString);
  
  String get(String paramString);
  
  Map<String, String> getCopy();
  
  Map<String, String> getImmutableMapOrNull();
  
  boolean isEmpty();
  
  void put(String paramString1, String paramString2);
  
  void remove(String paramString);
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\spi\ThreadContextMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */