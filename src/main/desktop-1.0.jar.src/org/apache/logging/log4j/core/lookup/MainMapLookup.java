/*    */ package org.apache.logging.log4j.core.lookup;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
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
/*    */ @Plugin(name = "main", category = "Lookup")
/*    */ public class MainMapLookup
/*    */   extends MapLookup
/*    */ {
/* 37 */   static final MapLookup MAIN_SINGLETON = new MapLookup(MapLookup.newMap(0));
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MainMapLookup() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public MainMapLookup(Map<String, String> map) {
/* 47 */     super(map);
/*    */   }
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
/*    */   public static void setMainArguments(String... args) {
/* 79 */     if (args == null) {
/*    */       return;
/*    */     }
/* 82 */     initMap(args, MAIN_SINGLETON.getMap());
/*    */   }
/*    */ 
/*    */   
/*    */   public String lookup(LogEvent event, String key) {
/* 87 */     return MAIN_SINGLETON.getMap().get(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public String lookup(String key) {
/* 92 */     return MAIN_SINGLETON.getMap().get(key);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\lookup\MainMapLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */