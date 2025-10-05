/*    */ package org.apache.logging.log4j.core.impl;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LoggerContext;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ContextAnchor
/*    */ {
/* 29 */   public static final ThreadLocal<LoggerContext> THREAD_CONTEXT = new ThreadLocal<>();
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\impl\ContextAnchor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */