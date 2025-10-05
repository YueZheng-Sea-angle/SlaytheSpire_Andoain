/*     */ package org.apache.logging.log4j.core.appender.rolling.action;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ public class CompositeAction
/*     */   extends AbstractAction
/*     */ {
/*     */   private final Action[] actions;
/*     */   private final boolean stopOnError;
/*     */   
/*     */   public CompositeAction(List<Action> actions, boolean stopOnError) {
/*  47 */     this.actions = new Action[actions.size()];
/*  48 */     actions.toArray(this.actions);
/*  49 */     this.stopOnError = stopOnError;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/*     */     try {
/*  58 */       execute();
/*  59 */     } catch (IOException ex) {
/*  60 */       LOGGER.warn("Exception during file rollover.", ex);
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
/*     */   public boolean execute() throws IOException {
/*  72 */     if (this.stopOnError) {
/*  73 */       for (Action action : this.actions) {
/*  74 */         if (!action.execute()) {
/*  75 */           return false;
/*     */         }
/*     */       } 
/*     */       
/*  79 */       return true;
/*     */     } 
/*  81 */     boolean status = true;
/*  82 */     IOException exception = null;
/*     */     
/*  84 */     for (Action action : this.actions) {
/*     */       try {
/*  86 */         status &= action.execute();
/*  87 */       } catch (IOException ex) {
/*  88 */         status = false;
/*     */         
/*  90 */         if (exception == null) {
/*  91 */           exception = ex;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  96 */     if (exception != null) {
/*  97 */       throw exception;
/*     */     }
/*     */     
/* 100 */     return status;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 105 */     return CompositeAction.class.getSimpleName() + Arrays.toString((Object[])this.actions);
/*     */   }
/*     */   
/*     */   public Action[] getActions() {
/* 109 */     return this.actions;
/*     */   }
/*     */   
/*     */   public boolean isStopOnError() {
/* 113 */     return this.stopOnError;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\action\CompositeAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */