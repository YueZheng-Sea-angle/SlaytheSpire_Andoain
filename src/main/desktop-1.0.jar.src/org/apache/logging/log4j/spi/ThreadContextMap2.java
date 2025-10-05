package org.apache.logging.log4j.spi;

import java.util.Map;
import org.apache.logging.log4j.util.StringMap;

public interface ThreadContextMap2 extends ThreadContextMap {
  void putAll(Map<String, String> paramMap);
  
  StringMap getReadOnlyContextData();
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\spi\ThreadContextMap2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */