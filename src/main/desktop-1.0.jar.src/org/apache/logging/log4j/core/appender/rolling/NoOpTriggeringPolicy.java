/*    */ package org.apache.logging.log4j.core.appender.rolling;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
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
/*    */ @Plugin(name = "NoOpTriggeringPolicy", category = "Core", printObject = true)
/*    */ public class NoOpTriggeringPolicy
/*    */   extends AbstractTriggeringPolicy
/*    */ {
/* 33 */   public static final NoOpTriggeringPolicy INSTANCE = new NoOpTriggeringPolicy();
/*    */   
/*    */   @PluginFactory
/*    */   public static NoOpTriggeringPolicy createPolicy() {
/* 37 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void initialize(RollingFileManager manager) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isTriggeringEvent(LogEvent logEvent) {
/* 48 */     return false;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\NoOpTriggeringPolicy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */