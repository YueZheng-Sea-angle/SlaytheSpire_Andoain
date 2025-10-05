package com.badlogic.gdx.net;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.utils.Disposable;

public interface ServerSocket extends Disposable {
  Net.Protocol getProtocol();
  
  Socket accept(SocketHints paramSocketHints);
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\com\badlogic\gdx\net\ServerSocket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */