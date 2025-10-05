/*    */ package org.apache.logging.log4j.core.appender;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ import org.apache.logging.log4j.core.Layout;
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
/*    */ @Plugin(name = "CountingNoOp", category = "Core", elementType = "appender", printObject = true)
/*    */ public class CountingNoOpAppender
/*    */   extends AbstractAppender
/*    */ {
/* 37 */   private final AtomicLong total = new AtomicLong();
/*    */   
/*    */   public CountingNoOpAppender(String name, Layout<?> layout) {
/* 40 */     super(name, null, (Layout)layout, true, Property.EMPTY_ARRAY);
/*    */   }
/*    */   
/*    */   private CountingNoOpAppender(String name, Layout<?> layout, Property[] properties) {
/* 44 */     super(name, null, (Layout)layout, true, properties);
/*    */   }
/*    */   
/*    */   public long getCount() {
/* 48 */     return this.total.get();
/*    */   }
/*    */ 
/*    */   
/*    */   public void append(LogEvent event) {
/* 53 */     this.total.incrementAndGet();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @PluginFactory
/*    */   public static CountingNoOpAppender createAppender(@PluginAttribute("name") String name) {
/* 61 */     return new CountingNoOpAppender(Objects.<String>requireNonNull(name), null, Property.EMPTY_ARRAY);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\CountingNoOpAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */