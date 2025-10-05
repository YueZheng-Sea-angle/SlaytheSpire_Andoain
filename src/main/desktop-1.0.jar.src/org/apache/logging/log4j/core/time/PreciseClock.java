package org.apache.logging.log4j.core.time;

import org.apache.logging.log4j.core.util.Clock;

public interface PreciseClock extends Clock {
  void init(MutableInstant paramMutableInstant);
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\time\PreciseClock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */