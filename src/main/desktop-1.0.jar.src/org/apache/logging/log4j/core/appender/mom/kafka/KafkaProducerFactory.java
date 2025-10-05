package org.apache.logging.log4j.core.appender.mom.kafka;

import java.util.Properties;
import org.apache.kafka.clients.producer.Producer;

public interface KafkaProducerFactory {
  Producer<byte[], byte[]> newKafkaProducer(Properties paramProperties);
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\mom\kafka\KafkaProducerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */