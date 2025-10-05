package org.apache.logging.log4j.core.appender.rolling.action;

import java.io.IOException;

public interface Action extends Runnable {
  boolean execute() throws IOException;
  
  void close();
  
  boolean isComplete();
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\action\Action.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */