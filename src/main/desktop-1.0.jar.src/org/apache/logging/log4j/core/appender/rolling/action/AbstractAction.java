/*     */ package org.apache.logging.log4j.core.appender.rolling.action;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.logging.log4j.Logger;
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
/*     */ public abstract class AbstractAction
/*     */   implements Action
/*     */ {
/*  33 */   protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean complete = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean interrupted = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean execute() throws IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void run() {
/*  64 */     if (!this.interrupted) {
/*     */       try {
/*  66 */         execute();
/*  67 */       } catch (RuntimeException|IOException ex) {
/*  68 */         reportException(ex);
/*  69 */       } catch (Error e) {
/*     */ 
/*     */         
/*  72 */         reportException(new RuntimeException(e));
/*     */       } 
/*     */       
/*  75 */       this.complete = true;
/*  76 */       this.interrupted = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void close() {
/*  85 */     this.interrupted = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isComplete() {
/*  95 */     return this.complete;
/*     */   }
/*     */   
/*     */   public boolean isInterrupted() {
/*  99 */     return this.interrupted;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void reportException(Exception ex) {
/* 108 */     LOGGER.warn("Exception reported by action '{}'", getClass(), ex);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\action\AbstractAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */