package org.apache.logging.log4j.message;

public interface FlowMessageFactory {
  EntryMessage newEntryMessage(Message paramMessage);
  
  ExitMessage newExitMessage(Object paramObject, Message paramMessage);
  
  ExitMessage newExitMessage(EntryMessage paramEntryMessage);
  
  ExitMessage newExitMessage(Object paramObject, EntryMessage paramEntryMessage);
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\message\FlowMessageFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */