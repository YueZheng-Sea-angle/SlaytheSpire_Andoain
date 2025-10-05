package org.apache.logging.log4j.core;

import java.io.Serializable;

public interface Appender extends LifeCycle {
  public static final String ELEMENT_TYPE = "appender";
  
  void append(LogEvent paramLogEvent);
  
  String getName();
  
  Layout<? extends Serializable> getLayout();
  
  boolean ignoreExceptions();
  
  ErrorHandler getHandler();
  
  void setHandler(ErrorHandler paramErrorHandler);
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\Appender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */