package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.Level;

public interface AsyncQueueFullPolicy {
  EventRoute getRoute(long paramLong, Level paramLevel);
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\async\AsyncQueueFullPolicy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */