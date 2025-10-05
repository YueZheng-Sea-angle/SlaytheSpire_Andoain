/*     */ package org.apache.logging.log4j.core.appender.mom.kafka;
/*     */ 
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Objects;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import org.apache.kafka.clients.producer.Producer;
/*     */ import org.apache.kafka.clients.producer.ProducerRecord;
/*     */ import org.apache.kafka.clients.producer.RecordMetadata;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.appender.AbstractManager;
/*     */ import org.apache.logging.log4j.core.appender.ManagerFactory;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.util.Log4jThread;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class KafkaManager
/*     */   extends AbstractManager
/*     */ {
/*     */   public static final String DEFAULT_TIMEOUT_MILLIS = "30000";
/*  44 */   static KafkaProducerFactory producerFactory = new DefaultKafkaProducerFactory();
/*     */   
/*  46 */   private final Properties config = new Properties();
/*     */   
/*     */   private Producer<byte[], byte[]> producer;
/*     */   private final int timeoutMillis;
/*     */   private final String topic;
/*     */   private final String key;
/*     */   private final boolean syncSend;
/*  53 */   private static final KafkaManagerFactory factory = new KafkaManagerFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public KafkaManager(LoggerContext loggerContext, String name, String topic, boolean syncSend, Property[] properties, String key) {
/*  61 */     super(loggerContext, name);
/*  62 */     this.topic = Objects.<String>requireNonNull(topic, "topic");
/*  63 */     this.syncSend = syncSend;
/*     */     
/*  65 */     this.config.setProperty("key.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
/*  66 */     this.config.setProperty("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
/*  67 */     this.config.setProperty("batch.size", "0");
/*     */     
/*  69 */     for (Property property : properties) {
/*  70 */       this.config.setProperty(property.getName(), property.getValue());
/*     */     }
/*     */     
/*  73 */     this.key = key;
/*     */     
/*  75 */     this.timeoutMillis = Integer.parseInt(this.config.getProperty("timeout.ms", "30000"));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean releaseSub(long timeout, TimeUnit timeUnit) {
/*  80 */     if (timeout > 0L) {
/*  81 */       closeProducer(timeout, timeUnit);
/*     */     } else {
/*  83 */       closeProducer(this.timeoutMillis, TimeUnit.MILLISECONDS);
/*     */     } 
/*  85 */     return true;
/*     */   }
/*     */   
/*     */   private void closeProducer(long timeout, TimeUnit timeUnit) {
/*  89 */     if (this.producer != null) {
/*     */ 
/*     */       
/*  92 */       Log4jThread log4jThread = new Log4jThread(() -> { if (this.producer != null) this.producer.close();  }"KafkaManager-CloseThread");
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  97 */       log4jThread.setDaemon(true);
/*  98 */       log4jThread.start();
/*     */       try {
/* 100 */         log4jThread.join(timeUnit.toMillis(timeout));
/* 101 */       } catch (InterruptedException ignore) {
/* 102 */         Thread.currentThread().interrupt();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void send(byte[] msg) throws ExecutionException, InterruptedException, TimeoutException {
/* 109 */     if (this.producer != null) {
/* 110 */       byte[] newKey = null;
/*     */       
/* 112 */       if (this.key != null && this.key.contains("${")) {
/*     */         
/* 114 */         newKey = getLoggerContext().getConfiguration().getStrSubstitutor().replace(this.key).getBytes(StandardCharsets.UTF_8);
/* 115 */       } else if (this.key != null) {
/* 116 */         newKey = this.key.getBytes(StandardCharsets.UTF_8);
/*     */       } 
/*     */       
/* 119 */       ProducerRecord<byte[], byte[]> newRecord = new ProducerRecord(this.topic, newKey, msg);
/* 120 */       if (this.syncSend) {
/* 121 */         Future<RecordMetadata> response = this.producer.send(newRecord);
/* 122 */         response.get(this.timeoutMillis, TimeUnit.MILLISECONDS);
/*     */       } else {
/* 124 */         this.producer.send(newRecord, (metadata, e) -> {
/*     */               if (e != null) {
/*     */                 LOGGER.error("Unable to write to Kafka in appender [" + getName() + "]", e);
/*     */               }
/*     */             });
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void startup() {
/* 134 */     if (this.producer == null) {
/* 135 */       this.producer = producerFactory.newKafkaProducer(this.config);
/*     */     }
/*     */   }
/*     */   
/*     */   public String getTopic() {
/* 140 */     return this.topic;
/*     */   }
/*     */ 
/*     */   
/*     */   public static KafkaManager getManager(LoggerContext loggerContext, String name, String topic, boolean syncSend, Property[] properties, String key) {
/* 145 */     StringBuilder sb = new StringBuilder(name);
/* 146 */     sb.append(" ").append(topic).append(" ").append(syncSend + "");
/* 147 */     for (Property prop : properties) {
/* 148 */       sb.append(" ").append(prop.getName()).append("=").append(prop.getValue());
/*     */     }
/* 150 */     return (KafkaManager)getManager(sb.toString(), factory, new FactoryData(loggerContext, topic, syncSend, properties, key));
/*     */   }
/*     */   
/*     */   private static class FactoryData
/*     */   {
/*     */     private final LoggerContext loggerContext;
/*     */     private final String topic;
/*     */     private final boolean syncSend;
/*     */     private final Property[] properties;
/*     */     private final String key;
/*     */     
/*     */     public FactoryData(LoggerContext loggerContext, String topic, boolean syncSend, Property[] properties, String key) {
/* 162 */       this.loggerContext = loggerContext;
/* 163 */       this.topic = topic;
/* 164 */       this.syncSend = syncSend;
/* 165 */       this.properties = properties;
/* 166 */       this.key = key;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class KafkaManagerFactory implements ManagerFactory<KafkaManager, FactoryData> {
/*     */     private KafkaManagerFactory() {}
/*     */     
/*     */     public KafkaManager createManager(String name, KafkaManager.FactoryData data) {
/* 174 */       return new KafkaManager(data.loggerContext, name, data.topic, data.syncSend, data.properties, data.key);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\mom\kafka\KafkaManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */