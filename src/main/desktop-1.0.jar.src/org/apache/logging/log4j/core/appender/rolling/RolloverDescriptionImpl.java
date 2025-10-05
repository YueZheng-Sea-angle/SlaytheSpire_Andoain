/*     */ package org.apache.logging.log4j.core.appender.rolling;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.core.appender.rolling.action.Action;
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
/*     */ public final class RolloverDescriptionImpl
/*     */   implements RolloverDescription
/*     */ {
/*     */   private final String activeFileName;
/*     */   private final boolean append;
/*     */   private final Action synchronous;
/*     */   private final Action asynchronous;
/*     */   
/*     */   public RolloverDescriptionImpl(String activeFileName, boolean append, Action synchronous, Action asynchronous) {
/*  60 */     Objects.requireNonNull(activeFileName, "activeFileName");
/*     */     
/*  62 */     this.append = append;
/*  63 */     this.activeFileName = activeFileName;
/*  64 */     this.synchronous = synchronous;
/*  65 */     this.asynchronous = asynchronous;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getActiveFileName() {
/*  75 */     return this.activeFileName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getAppend() {
/*  83 */     return this.append;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Action getSynchronous() {
/*  94 */     return this.synchronous;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Action getAsynchronous() {
/* 105 */     return this.asynchronous;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\RolloverDescriptionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */