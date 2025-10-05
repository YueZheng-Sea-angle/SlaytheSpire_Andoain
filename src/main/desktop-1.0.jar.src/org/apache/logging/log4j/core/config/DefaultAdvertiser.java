/*    */ package org.apache.logging.log4j.core.config;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.net.Advertiser;
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
/*    */ @Plugin(name = "default", category = "Core", elementType = "advertiser", printObject = false)
/*    */ public class DefaultAdvertiser
/*    */   implements Advertiser
/*    */ {
/*    */   public Object advertise(Map<String, String> properties) {
/* 37 */     return null;
/*    */   }
/*    */   
/*    */   public void unadvertise(Object advertisedObject) {}
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\DefaultAdvertiser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */