package org.apache.logging.log4j.core;

import java.util.List;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.apache.logging.log4j.util.StringMap;

public interface ContextDataInjector {
  StringMap injectContextData(List<Property> paramList, StringMap paramStringMap);
  
  ReadOnlyStringMap rawContextData();
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\ContextDataInjector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */