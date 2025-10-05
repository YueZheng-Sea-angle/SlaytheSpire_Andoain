/*    */ package org.apache.logging.log4j.core.config;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
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
/*    */ @Plugin(name = "CustomLevels", category = "Core", printObject = true)
/*    */ public final class CustomLevels
/*    */ {
/*    */   private final List<CustomLevelConfig> customLevels;
/*    */   
/*    */   private CustomLevels(CustomLevelConfig[] customLevels) {
/* 38 */     this.customLevels = new ArrayList<>(Arrays.asList(customLevels));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @PluginFactory
/*    */   public static CustomLevels createCustomLevels(@PluginElement("CustomLevels") CustomLevelConfig[] customLevels) {
/* 50 */     return new CustomLevels((customLevels == null) ? CustomLevelConfig.EMPTY_ARRAY : customLevels);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<CustomLevelConfig> getCustomLevels() {
/* 58 */     return this.customLevels;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\CustomLevels.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */