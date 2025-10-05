package org.apache.logging.log4j.util;

import org.apache.logging.log4j.message.MultiformatMessage;

public interface MultiFormatStringBuilderFormattable extends MultiformatMessage, StringBuilderFormattable {
  void formatTo(String[] paramArrayOfString, StringBuilder paramStringBuilder);
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4\\util\MultiFormatStringBuilderFormattable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */