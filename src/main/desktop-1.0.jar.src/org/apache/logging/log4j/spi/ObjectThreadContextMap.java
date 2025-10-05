package org.apache.logging.log4j.spi;

import java.util.Map;

public interface ObjectThreadContextMap extends CleanableThreadContextMap {
  <V> V getValue(String paramString);
  
  <V> void putValue(String paramString, V paramV);
  
  <V> void putAllValues(Map<String, V> paramMap);
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\spi\ObjectThreadContextMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */