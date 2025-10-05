package org.apache.commons.net;

import java.util.EventListener;

public interface ProtocolCommandListener extends EventListener {
  void protocolCommandSent(ProtocolCommandEvent paramProtocolCommandEvent);
  
  void protocolReplyReceived(ProtocolCommandEvent paramProtocolCommandEvent);
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\ProtocolCommandListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */