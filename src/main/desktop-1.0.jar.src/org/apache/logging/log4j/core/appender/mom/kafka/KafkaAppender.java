/*     */ package org.apache.logging.log4j.core.appender.mom.kafka;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.AbstractLifeCycle;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.AbstractAppender;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "Kafka", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class KafkaAppender
/*     */   extends AbstractAppender
/*     */ {
/*     */   private final Integer retryCount;
/*     */   private final KafkaManager manager;
/*     */   
/*     */   public static class Builder<B extends Builder<B>>
/*     */     extends AbstractAppender.Builder<B>
/*     */     implements org.apache.logging.log4j.core.util.Builder<KafkaAppender>
/*     */   {
/*     */     @PluginAttribute("retryCount")
/*     */     private String retryCount;
/*     */     @PluginAttribute("topic")
/*     */     private String topic;
/*     */     @PluginAttribute("key")
/*     */     private String key;
/*     */     @PluginAttribute(value = "syncSend", defaultBoolean = true)
/*     */     private boolean syncSend;
/*     */     
/*     */     public KafkaAppender build() {
/*  71 */       Layout<? extends Serializable> layout = getLayout();
/*  72 */       if (layout == null) {
/*  73 */         KafkaAppender.LOGGER.error("No layout provided for KafkaAppender");
/*  74 */         return null;
/*     */       } 
/*  76 */       KafkaManager kafkaManager = KafkaManager.getManager(getConfiguration().getLoggerContext(), getName(), this.topic, this.syncSend, 
/*  77 */           getPropertyArray(), this.key);
/*  78 */       return new KafkaAppender(getName(), layout, getFilter(), isIgnoreExceptions(), kafkaManager, 
/*  79 */           getPropertyArray(), getRetryCount());
/*     */     }
/*     */     
/*     */     public String getTopic() {
/*  83 */       return this.topic;
/*     */     }
/*     */     
/*     */     public boolean isSyncSend() {
/*  87 */       return this.syncSend;
/*     */     }
/*     */     
/*     */     public B setTopic(String topic) {
/*  91 */       this.topic = topic;
/*  92 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setSyncSend(boolean syncSend) {
/*  96 */       this.syncSend = syncSend;
/*  97 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setKey(String key) {
/* 101 */       this.key = key;
/* 102 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public Integer getRetryCount() {
/* 106 */       Integer intRetryCount = null;
/*     */       try {
/* 108 */         intRetryCount = Integer.valueOf(this.retryCount);
/* 109 */       } catch (NumberFormatException numberFormatException) {}
/*     */ 
/*     */       
/* 112 */       return intRetryCount;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static KafkaAppender createAppender(Layout<? extends Serializable> layout, Filter filter, String name, boolean ignoreExceptions, String topic, Property[] properties, Configuration configuration, String key) {
/* 124 */     if (layout == null) {
/* 125 */       AbstractLifeCycle.LOGGER.error("No layout provided for KafkaAppender");
/* 126 */       return null;
/*     */     } 
/* 128 */     KafkaManager kafkaManager = KafkaManager.getManager(configuration.getLoggerContext(), name, topic, true, properties, key);
/*     */     
/* 130 */     return new KafkaAppender(name, layout, filter, ignoreExceptions, kafkaManager, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static <B extends Builder<B>> B newBuilder() {
/* 140 */     return (B)(new Builder<>()).asBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private KafkaAppender(String name, Layout<? extends Serializable> layout, Filter filter, boolean ignoreExceptions, KafkaManager manager, Property[] properties, Integer retryCount) {
/* 148 */     super(name, filter, layout, ignoreExceptions, properties);
/* 149 */     this.manager = Objects.<KafkaManager>requireNonNull(manager, "manager");
/* 150 */     this.retryCount = retryCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public void append(LogEvent event) {
/* 155 */     if (event.getLoggerName() != null && event.getLoggerName().startsWith("org.apache.kafka")) {
/* 156 */       LOGGER.warn("Recursive logging from [{}] for appender [{}].", event.getLoggerName(), getName());
/*     */     } else {
/*     */       try {
/* 159 */         tryAppend(event);
/* 160 */       } catch (Exception e) {
/*     */         
/* 162 */         if (this.retryCount != null) {
/* 163 */           int currentRetryAttempt = 0;
/* 164 */           while (currentRetryAttempt < this.retryCount.intValue()) {
/* 165 */             currentRetryAttempt++;
/*     */             try {
/* 167 */               tryAppend(event);
/*     */               break;
/* 169 */             } catch (Exception exception) {}
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 174 */         error("Unable to write to Kafka in appender [" + getName() + "]", event, e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private void tryAppend(LogEvent event) throws ExecutionException, InterruptedException, TimeoutException {
/*     */     byte[] data;
/* 180 */     Layout<? extends Serializable> layout = getLayout();
/*     */     
/* 182 */     if (layout instanceof org.apache.logging.log4j.core.layout.SerializedLayout) {
/* 183 */       byte[] header = layout.getHeader();
/* 184 */       byte[] body = layout.toByteArray(event);
/* 185 */       data = new byte[header.length + body.length];
/* 186 */       System.arraycopy(header, 0, data, 0, header.length);
/* 187 */       System.arraycopy(body, 0, data, header.length, body.length);
/*     */     } else {
/* 189 */       data = layout.toByteArray(event);
/*     */     } 
/* 191 */     this.manager.send(data);
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 196 */     super.start();
/* 197 */     this.manager.startup();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 202 */     setStopping();
/* 203 */     boolean stopped = stop(timeout, timeUnit, false);
/* 204 */     stopped &= this.manager.stop(timeout, timeUnit);
/* 205 */     setStopped();
/* 206 */     return stopped;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 211 */     return "KafkaAppender{name=" + getName() + ", state=" + getState() + ", topic=" + this.manager.getTopic() + '}';
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\mom\kafka\KafkaAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */