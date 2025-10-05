/*    */ package org.apache.logging.log4j.core.config.plugins.visitors;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.Node;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginValue;
/*    */ import org.apache.logging.log4j.util.StringBuilders;
/*    */ import org.apache.logging.log4j.util.Strings;
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
/*    */ public class PluginValueVisitor
/*    */   extends AbstractPluginVisitor<PluginValue>
/*    */ {
/*    */   public PluginValueVisitor() {
/* 32 */     super(PluginValue.class);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object visit(Configuration configuration, Node node, LogEvent event, StringBuilder log) {
/* 38 */     String name = this.annotation.value();
/* 39 */     String elementValue = node.getValue();
/* 40 */     String attributeValue = (String)node.getAttributes().get(name);
/* 41 */     String rawValue = null;
/* 42 */     if (Strings.isNotEmpty(elementValue)) {
/* 43 */       if (Strings.isNotEmpty(attributeValue)) {
/* 44 */         LOGGER.error("Configuration contains {} with both attribute value ({}) AND element value ({}). Please specify only one value. Using the element value.", node
/*    */             
/* 46 */             .getName(), attributeValue, elementValue);
/*    */       }
/* 48 */       rawValue = elementValue;
/*    */     } else {
/* 50 */       rawValue = removeAttributeValue(node.getAttributes(), name, new String[0]);
/*    */     } 
/* 52 */     String value = this.substitutor.replace(event, rawValue);
/* 53 */     StringBuilders.appendKeyDqValue(log, name, value);
/* 54 */     return value;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\plugins\visitors\PluginValueVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */