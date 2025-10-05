/*    */ package org.apache.logging.log4j.core.appender.nosql;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
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
/*    */ public class DefaultNoSqlObject
/*    */   implements NoSqlObject<Map<String, Object>>
/*    */ {
/* 35 */   private final Map<String, Object> map = new HashMap<>();
/*    */ 
/*    */ 
/*    */   
/*    */   public void set(String field, Object value) {
/* 40 */     this.map.put(field, value);
/*    */   }
/*    */ 
/*    */   
/*    */   public void set(String field, NoSqlObject<Map<String, Object>> value) {
/* 45 */     this.map.put(field, value.unwrap());
/*    */   }
/*    */ 
/*    */   
/*    */   public void set(String field, Object[] values) {
/* 50 */     this.map.put(field, Arrays.asList(values));
/*    */   }
/*    */ 
/*    */   
/*    */   public void set(String field, NoSqlObject<Map<String, Object>>[] values) {
/* 55 */     List<Map<String, Object>> list = new ArrayList<>(values.length);
/* 56 */     for (NoSqlObject<Map<String, Object>> value : values) {
/* 57 */       list.add(value.unwrap());
/*    */     }
/* 59 */     this.map.put(field, list);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Object> unwrap() {
/* 64 */     return this.map;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\nosql\DefaultNoSqlObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */