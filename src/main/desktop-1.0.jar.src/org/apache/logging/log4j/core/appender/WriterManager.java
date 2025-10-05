/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.core.StringLayout;
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
/*     */ public class WriterManager
/*     */   extends AbstractManager
/*     */ {
/*     */   protected final StringLayout layout;
/*     */   private volatile Writer writer;
/*     */   
/*     */   public static <T> WriterManager getManager(String name, T data, ManagerFactory<? extends WriterManager, T> factory) {
/*  42 */     return AbstractManager.<WriterManager, T>getManager(name, (ManagerFactory)factory, data);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WriterManager(Writer writer, String streamName, StringLayout layout, boolean writeHeader) {
/*  50 */     super(null, streamName);
/*  51 */     this.writer = writer;
/*  52 */     this.layout = layout;
/*  53 */     if (writeHeader && layout != null) {
/*  54 */       byte[] header = layout.getHeader();
/*  55 */       if (header != null) {
/*     */         try {
/*  57 */           this.writer.write(new String(header, layout.getCharset()));
/*  58 */         } catch (IOException e) {
/*  59 */           logError("Unable to write header", e);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected synchronized void closeWriter() {
/*  66 */     Writer w = this.writer;
/*     */     try {
/*  68 */       w.close();
/*  69 */     } catch (IOException ex) {
/*  70 */       logError("Unable to close stream", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void flush() {
/*     */     try {
/*  79 */       this.writer.flush();
/*  80 */     } catch (IOException ex) {
/*  81 */       String msg = "Error flushing stream " + getName();
/*  82 */       throw new AppenderLoggingException(msg, ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected Writer getWriter() {
/*  87 */     return this.writer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/*  95 */     return (getCount() > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean releaseSub(long timeout, TimeUnit timeUnit) {
/* 103 */     writeFooter();
/* 104 */     closeWriter();
/* 105 */     return true;
/*     */   }
/*     */   
/*     */   protected void setWriter(Writer writer) {
/* 109 */     byte[] header = this.layout.getHeader();
/* 110 */     if (header != null) {
/*     */       try {
/* 112 */         writer.write(new String(header, this.layout.getCharset()));
/* 113 */         this.writer = writer;
/* 114 */       } catch (IOException ioe) {
/* 115 */         logError("Unable to write header", ioe);
/*     */       } 
/*     */     } else {
/* 118 */       this.writer = writer;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void write(String str) {
/*     */     try {
/* 130 */       this.writer.write(str);
/* 131 */     } catch (IOException ex) {
/* 132 */       String msg = "Error writing to stream " + getName();
/* 133 */       throw new AppenderLoggingException(msg, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeFooter() {
/* 141 */     if (this.layout == null) {
/*     */       return;
/*     */     }
/* 144 */     byte[] footer = this.layout.getFooter();
/* 145 */     if (footer != null && footer.length > 0)
/* 146 */       write(new String(footer, this.layout.getCharset())); 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\WriterManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */