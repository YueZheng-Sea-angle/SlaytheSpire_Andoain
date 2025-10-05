package org.apache.logging.log4j.core.async;

import java.util.concurrent.BlockingQueue;

public interface BlockingQueueFactory<E> {
  public static final String ELEMENT_TYPE = "BlockingQueueFactory";
  
  BlockingQueue<E> create(int paramInt);
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\async\BlockingQueueFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */