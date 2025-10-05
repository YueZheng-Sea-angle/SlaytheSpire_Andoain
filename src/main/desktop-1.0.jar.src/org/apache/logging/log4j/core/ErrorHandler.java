package org.apache.logging.log4j.core;

public interface ErrorHandler {
  void error(String paramString);
  
  void error(String paramString, Throwable paramThrowable);
  
  void error(String paramString, LogEvent paramLogEvent, Throwable paramThrowable);
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\ErrorHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */