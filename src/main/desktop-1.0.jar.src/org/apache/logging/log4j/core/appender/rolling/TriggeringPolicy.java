package org.apache.logging.log4j.core.appender.rolling;

import org.apache.logging.log4j.core.LogEvent;

public interface TriggeringPolicy {
  void initialize(RollingFileManager paramRollingFileManager);
  
  boolean isTriggeringEvent(LogEvent paramLogEvent);
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\TriggeringPolicy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */