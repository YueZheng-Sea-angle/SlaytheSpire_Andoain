package org.apache.logging.log4j.message;

public interface ThreadInformation {
  void printThreadInfo(StringBuilder paramStringBuilder);
  
  void printStack(StringBuilder paramStringBuilder, StackTraceElement[] paramArrayOfStackTraceElement);
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\message\ThreadInformation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */