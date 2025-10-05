/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.layout.ByteBufferDestination;
/*     */ import org.apache.logging.log4j.core.util.Constants;
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
/*     */ public abstract class AbstractOutputStreamAppender<M extends OutputStreamManager>
/*     */   extends AbstractAppender
/*     */ {
/*     */   private final boolean immediateFlush;
/*     */   private final M manager;
/*     */   
/*     */   public static abstract class Builder<B extends Builder<B>>
/*     */     extends AbstractAppender.Builder<B>
/*     */   {
/*     */     @PluginBuilderAttribute
/*     */     private boolean bufferedIo = true;
/*     */     @PluginBuilderAttribute
/*  46 */     private int bufferSize = Constants.ENCODER_BYTE_BUFFER_SIZE;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private boolean immediateFlush = true;
/*     */ 
/*     */     
/*     */     public int getBufferSize() {
/*  53 */       return this.bufferSize;
/*     */     }
/*     */     
/*     */     public boolean isBufferedIo() {
/*  57 */       return this.bufferedIo;
/*     */     }
/*     */     
/*     */     public boolean isImmediateFlush() {
/*  61 */       return this.immediateFlush;
/*     */     }
/*     */     
/*     */     public B withImmediateFlush(boolean immediateFlush) {
/*  65 */       this.immediateFlush = immediateFlush;
/*  66 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withBufferedIo(boolean bufferedIo) {
/*  70 */       this.bufferedIo = bufferedIo;
/*  71 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withBufferSize(int bufferSize) {
/*  75 */       this.bufferSize = bufferSize;
/*  76 */       return (B)asBuilder();
/*     */     }
/*     */   }
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
/*     */   @Deprecated
/*     */   protected AbstractOutputStreamAppender(String name, Layout<? extends Serializable> layout, Filter filter, boolean ignoreExceptions, boolean immediateFlush, M manager) {
/* 103 */     super(name, filter, layout, ignoreExceptions, Property.EMPTY_ARRAY);
/* 104 */     this.manager = manager;
/* 105 */     this.immediateFlush = immediateFlush;
/*     */   }
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
/*     */   protected AbstractOutputStreamAppender(String name, Layout<? extends Serializable> layout, Filter filter, boolean ignoreExceptions, boolean immediateFlush, Property[] properties, M manager) {
/* 120 */     super(name, filter, layout, ignoreExceptions, properties);
/* 121 */     this.manager = manager;
/* 122 */     this.immediateFlush = immediateFlush;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getImmediateFlush() {
/* 131 */     return this.immediateFlush;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public M getManager() {
/* 140 */     return this.manager;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 145 */     if (getLayout() == null) {
/* 146 */       LOGGER.error("No layout set for the appender named [" + getName() + "].");
/*     */     }
/* 148 */     if (this.manager == null) {
/* 149 */       LOGGER.error("No OutputStreamManager set for the appender named [" + getName() + "].");
/*     */     }
/* 151 */     super.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 156 */     return stop(timeout, timeUnit, true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean stop(long timeout, TimeUnit timeUnit, boolean changeLifeCycleState) {
/* 161 */     boolean stopped = super.stop(timeout, timeUnit, changeLifeCycleState);
/* 162 */     stopped &= this.manager.stop(timeout, timeUnit);
/* 163 */     if (changeLifeCycleState) {
/* 164 */       setStopped();
/*     */     }
/* 166 */     LOGGER.debug("Appender {} stopped with status {}", getName(), Boolean.valueOf(stopped));
/* 167 */     return stopped;
/*     */   }
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
/*     */   public void append(LogEvent event) {
/*     */     try {
/* 181 */       tryAppend(event);
/* 182 */     } catch (AppenderLoggingException ex) {
/* 183 */       error("Unable to write to stream " + this.manager.getName() + " for appender " + getName(), event, (Throwable)ex);
/* 184 */       throw ex;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void tryAppend(LogEvent event) {
/* 189 */     if (Constants.ENABLE_DIRECT_ENCODERS) {
/* 190 */       directEncodeEvent(event);
/*     */     } else {
/* 192 */       writeByteArrayToManager(event);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void directEncodeEvent(LogEvent event) {
/* 197 */     getLayout().encode(event, (ByteBufferDestination)this.manager);
/* 198 */     if (this.immediateFlush || event.isEndOfBatch()) {
/* 199 */       this.manager.flush();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void writeByteArrayToManager(LogEvent event) {
/* 204 */     byte[] bytes = getLayout().toByteArray(event);
/* 205 */     if (bytes != null && bytes.length > 0)
/* 206 */       this.manager.write(bytes, (this.immediateFlush || event.isEndOfBatch())); 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\AbstractOutputStreamAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */