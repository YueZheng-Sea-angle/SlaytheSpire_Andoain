package org.apache.logging.log4j.spi;

import java.io.Closeable;

public interface LoggerAdapter<L> extends Closeable {
  L getLogger(String paramString);
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\spi\LoggerAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */