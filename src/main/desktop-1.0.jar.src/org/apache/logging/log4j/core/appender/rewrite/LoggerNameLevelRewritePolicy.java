/*    */ package org.apache.logging.log4j.core.appender.rewrite;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Locale;
/*    */ import java.util.Map;
/*    */ import org.apache.logging.log4j.Level;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*    */ import org.apache.logging.log4j.core.impl.Log4jLogEvent;
/*    */ import org.apache.logging.log4j.core.util.KeyValuePair;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Plugin(name = "LoggerNameLevelRewritePolicy", category = "Core", elementType = "rewritePolicy", printObject = true)
/*    */ public class LoggerNameLevelRewritePolicy
/*    */   implements RewritePolicy
/*    */ {
/*    */   private final String loggerName;
/*    */   private final Map<Level, Level> map;
/*    */   
/*    */   @PluginFactory
/*    */   public static LoggerNameLevelRewritePolicy createPolicy(@PluginAttribute("logger") String loggerNamePrefix, @PluginElement("KeyValuePair") KeyValuePair[] levelPairs) {
/* 57 */     Map<Level, Level> newMap = new HashMap<>(levelPairs.length);
/* 58 */     for (KeyValuePair keyValuePair : levelPairs) {
/* 59 */       newMap.put(getLevel(keyValuePair.getKey()), getLevel(keyValuePair.getValue()));
/*    */     }
/* 61 */     return new LoggerNameLevelRewritePolicy(loggerNamePrefix, newMap);
/*    */   }
/*    */   
/*    */   private static Level getLevel(String name) {
/* 65 */     return Level.getLevel(name.toUpperCase(Locale.ROOT));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private LoggerNameLevelRewritePolicy(String loggerName, Map<Level, Level> map) {
/* 73 */     this.loggerName = loggerName;
/* 74 */     this.map = map;
/*    */   }
/*    */ 
/*    */   
/*    */   public LogEvent rewrite(LogEvent event) {
/* 79 */     if (event.getLoggerName() == null || !event.getLoggerName().startsWith(this.loggerName)) {
/* 80 */       return event;
/*    */     }
/* 82 */     Level sourceLevel = event.getLevel();
/* 83 */     Level newLevel = this.map.get(sourceLevel);
/* 84 */     if (newLevel == null || newLevel == sourceLevel) {
/* 85 */       return event;
/*    */     }
/* 87 */     return (LogEvent)(new Log4jLogEvent.Builder(event)).setLevel(newLevel).build();
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rewrite\LoggerNameLevelRewritePolicy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */