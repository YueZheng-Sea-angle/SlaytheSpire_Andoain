package org.apache.logging.log4j.core.jmx;

public interface RingBufferAdminMBean {
  public static final String PATTERN_ASYNC_LOGGER = "org.apache.logging.log4j2:type=%s,component=AsyncLoggerRingBuffer";
  
  public static final String PATTERN_ASYNC_LOGGER_CONFIG = "org.apache.logging.log4j2:type=%s,component=Loggers,name=%s,subtype=RingBuffer";
  
  long getBufferSize();
  
  long getRemainingCapacity();
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\jmx\RingBufferAdminMBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */