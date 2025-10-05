package org.apache.logging.log4j.core.selector;

import java.net.URI;
import org.apache.logging.log4j.core.LoggerContext;

public interface NamedContextSelector extends ContextSelector {
  LoggerContext locateContext(String paramString, Object paramObject, URI paramURI);
  
  LoggerContext removeContext(String paramString);
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\selector\NamedContextSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */