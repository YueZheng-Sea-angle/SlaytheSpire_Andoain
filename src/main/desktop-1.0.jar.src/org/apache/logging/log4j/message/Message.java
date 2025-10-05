package org.apache.logging.log4j.message;

import java.io.Serializable;

public interface Message extends Serializable {
  String getFormattedMessage();
  
  String getFormat();
  
  Object[] getParameters();
  
  Throwable getThrowable();
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\message\Message.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */