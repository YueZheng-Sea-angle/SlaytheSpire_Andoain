/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
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
/*     */ public abstract class AbstractLayout<T extends Serializable>
/*     */   implements Layout<T>
/*     */ {
/*     */   public static abstract class Builder<B extends Builder<B>>
/*     */   {
/*     */     @PluginConfiguration
/*     */     private Configuration configuration;
/*     */     @PluginBuilderAttribute
/*     */     private byte[] footer;
/*     */     @PluginBuilderAttribute
/*     */     private byte[] header;
/*     */     
/*     */     public B asBuilder() {
/*  57 */       return (B)this;
/*     */     }
/*     */     
/*     */     public Configuration getConfiguration() {
/*  61 */       return this.configuration;
/*     */     }
/*     */     
/*     */     public byte[] getFooter() {
/*  65 */       return this.footer;
/*     */     }
/*     */     
/*     */     public byte[] getHeader() {
/*  69 */       return this.header;
/*     */     }
/*     */     
/*     */     public B setConfiguration(Configuration configuration) {
/*  73 */       this.configuration = configuration;
/*  74 */       return asBuilder();
/*     */     }
/*     */     
/*     */     public B setFooter(byte[] footer) {
/*  78 */       this.footer = footer;
/*  79 */       return asBuilder();
/*     */     }
/*     */     
/*     */     public B setHeader(byte[] header) {
/*  83 */       this.header = header;
/*  84 */       return asBuilder();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Configuration configuration;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected long eventCount;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final byte[] footer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final byte[] header;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public AbstractLayout(byte[] header, byte[] footer) {
/* 125 */     this(null, header, footer);
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
/*     */   public AbstractLayout(Configuration configuration, byte[] header, byte[] footer) {
/* 139 */     this.configuration = configuration;
/* 140 */     this.header = header;
/* 141 */     this.footer = footer;
/*     */   }
/*     */   
/*     */   public Configuration getConfiguration() {
/* 145 */     return this.configuration;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> getContentFormat() {
/* 150 */     return new HashMap<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getFooter() {
/* 160 */     return this.footer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getHeader() {
/* 170 */     return this.header;
/*     */   }
/*     */   
/*     */   protected void markEvent() {
/* 174 */     this.eventCount++;
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
/*     */   public void encode(LogEvent event, ByteBufferDestination destination) {
/* 209 */     byte[] data = toByteArray(event);
/* 210 */     destination.writeBytes(data, 0, data.length);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\layout\AbstractLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */