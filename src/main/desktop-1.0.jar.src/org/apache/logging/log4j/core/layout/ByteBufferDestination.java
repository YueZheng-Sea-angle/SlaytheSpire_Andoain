package org.apache.logging.log4j.core.layout;

import java.nio.ByteBuffer;

public interface ByteBufferDestination {
  ByteBuffer getByteBuffer();
  
  ByteBuffer drain(ByteBuffer paramByteBuffer);
  
  void writeBytes(ByteBuffer paramByteBuffer);
  
  void writeBytes(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\layout\ByteBufferDestination.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */