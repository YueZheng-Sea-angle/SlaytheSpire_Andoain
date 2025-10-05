package org.apache.logging.log4j.core.time;

import org.apache.logging.log4j.util.StringBuilderFormattable;

public interface Instant extends StringBuilderFormattable {
  long getEpochSecond();
  
  int getNanoOfSecond();
  
  long getEpochMillisecond();
  
  int getNanoOfMillisecond();
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\time\Instant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */