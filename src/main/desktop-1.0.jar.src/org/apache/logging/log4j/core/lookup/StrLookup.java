package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.core.LogEvent;

public interface StrLookup {
  public static final String CATEGORY = "Lookup";
  
  String lookup(String paramString);
  
  String lookup(LogEvent paramLogEvent, String paramString);
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\lookup\StrLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */