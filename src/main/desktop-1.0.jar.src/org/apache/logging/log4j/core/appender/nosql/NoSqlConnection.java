package org.apache.logging.log4j.core.appender.nosql;

import java.io.Closeable;

public interface NoSqlConnection<W, T extends NoSqlObject<W>> extends Closeable {
  T createObject();
  
  T[] createList(int paramInt);
  
  void insertObject(NoSqlObject<W> paramNoSqlObject);
  
  void close();
  
  boolean isClosed();
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\nosql\NoSqlConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */