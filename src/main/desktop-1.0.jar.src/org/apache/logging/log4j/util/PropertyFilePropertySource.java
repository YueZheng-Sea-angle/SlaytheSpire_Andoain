/*    */ package org.apache.logging.log4j.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.net.URL;
/*    */ import java.util.Properties;
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
/*    */ public class PropertyFilePropertySource
/*    */   extends PropertiesPropertySource
/*    */ {
/*    */   public PropertyFilePropertySource(String fileName) {
/* 32 */     super(loadPropertiesFile(fileName));
/*    */   }
/*    */   
/*    */   private static Properties loadPropertiesFile(String fileName) {
/* 36 */     Properties props = new Properties();
/* 37 */     for (URL url : LoaderUtil.findResources(fileName)) {
/* 38 */       try (InputStream in = url.openStream()) {
/* 39 */         props.load(in);
/* 40 */       } catch (IOException e) {
/* 41 */         LowLevelLogUtil.logException("Unable to read " + url, e);
/*    */       } 
/*    */     } 
/* 44 */     return props;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getPriority() {
/* 49 */     return 0;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4\\util\PropertyFilePropertySource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */