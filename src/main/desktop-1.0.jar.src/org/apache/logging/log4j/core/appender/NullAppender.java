/*    */ package org.apache.logging.log4j.core.appender;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.Property;
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
/*    */ @Plugin(name = "Null", category = "Core", elementType = "appender", printObject = true)
/*    */ public class NullAppender
/*    */   extends AbstractAppender
/*    */ {
/*    */   public static final String PLUGIN_NAME = "Null";
/*    */   
/*    */   @PluginFactory
/*    */   public static NullAppender createAppender(@PluginAttribute(value = "name", defaultString = "null") String name) {
/* 39 */     return new NullAppender(name);
/*    */   }
/*    */   
/*    */   private NullAppender(String name) {
/* 43 */     super(name, null, null, true, Property.EMPTY_ARRAY);
/*    */   }
/*    */   
/*    */   public void append(LogEvent event) {}
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\NullAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */