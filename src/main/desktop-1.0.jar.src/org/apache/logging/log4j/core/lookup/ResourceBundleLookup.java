/*    */ package org.apache.logging.log4j.core.lookup;
/*    */ 
/*    */ import java.util.MissingResourceException;
/*    */ import java.util.ResourceBundle;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.Marker;
/*    */ import org.apache.logging.log4j.MarkerManager;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
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
/*    */ @Plugin(name = "bundle", category = "Lookup")
/*    */ public class ResourceBundleLookup
/*    */   extends AbstractLookup
/*    */ {
/* 35 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/* 36 */   private static final Marker LOOKUP = MarkerManager.getMarker("LOOKUP");
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
/*    */   public String lookup(LogEvent event, String key) {
/* 51 */     if (key == null) {
/* 52 */       return null;
/*    */     }
/* 54 */     String[] keys = key.split(":");
/* 55 */     int keyLen = keys.length;
/* 56 */     if (keyLen != 2) {
/* 57 */       LOGGER.warn(LOOKUP, "Bad ResourceBundle key format [{}]. Expected format is BundleName:KeyName.", key);
/* 58 */       return null;
/*    */     } 
/* 60 */     String bundleName = keys[0];
/* 61 */     String bundleKey = keys[1];
/*    */     
/*    */     try {
/* 64 */       return ResourceBundle.getBundle(bundleName).getString(bundleKey);
/* 65 */     } catch (MissingResourceException e) {
/* 66 */       LOGGER.warn(LOOKUP, "Error looking up ResourceBundle [{}].", bundleName, e);
/* 67 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\lookup\ResourceBundleLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */