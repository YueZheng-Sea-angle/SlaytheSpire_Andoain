package org.apache.logging.log4j.core.appender.rolling;

public interface RolloverListener {
  void rolloverTriggered(String paramString);
  
  void rolloverComplete(String paramString);
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\RolloverListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */