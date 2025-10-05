/*    */ package org.apache.logging.log4j.core.appender.rolling;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Plugin(name = "SizeBasedTriggeringPolicy", category = "Core", printObject = true)
/*    */ public class SizeBasedTriggeringPolicy
/*    */   extends AbstractTriggeringPolicy
/*    */ {
/*    */   private static final long MAX_FILE_SIZE = 10485760L;
/*    */   private final long maxFileSize;
/*    */   private RollingFileManager manager;
/*    */   
/*    */   protected SizeBasedTriggeringPolicy() {
/* 44 */     this.maxFileSize = 10485760L;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected SizeBasedTriggeringPolicy(long maxFileSize) {
/* 53 */     this.maxFileSize = maxFileSize;
/*    */   }
/*    */   
/*    */   public long getMaxFileSize() {
/* 57 */     return this.maxFileSize;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void initialize(RollingFileManager aManager) {
/* 66 */     this.manager = aManager;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isTriggeringEvent(LogEvent event) {
/* 77 */     boolean triggered = (this.manager.getFileSize() > this.maxFileSize);
/* 78 */     if (triggered) {
/* 79 */       this.manager.getPatternProcessor().updateTime();
/*    */     }
/* 81 */     return triggered;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 86 */     return "SizeBasedTriggeringPolicy(size=" + this.maxFileSize + ')';
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @PluginFactory
/*    */   public static SizeBasedTriggeringPolicy createPolicy(@PluginAttribute("size") String size) {
/* 97 */     long maxSize = (size == null) ? 10485760L : FileSize.parse(size, 10485760L);
/* 98 */     return new SizeBasedTriggeringPolicy(maxSize);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\SizeBasedTriggeringPolicy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */