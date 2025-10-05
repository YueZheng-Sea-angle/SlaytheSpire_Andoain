/*    */ package org.apache.logging.log4j.core.jackson;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonCreator;
/*    */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*    */ import com.fasterxml.jackson.annotation.JsonValue;
/*    */ import org.apache.logging.log4j.Level;
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
/*    */ @JsonIgnoreProperties({"name", "declaringClass", "standardLevel"})
/*    */ abstract class LevelMixIn
/*    */ {
/*    */   @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
/*    */   public static Level getLevel(String name) {
/* 38 */     return null;
/*    */   }
/*    */   
/*    */   @JsonValue
/*    */   public abstract String name();
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\jackson\LevelMixIn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */