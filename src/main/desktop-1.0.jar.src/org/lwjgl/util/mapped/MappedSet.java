/*    */ package org.lwjgl.util.mapped;
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
/*    */ public class MappedSet
/*    */ {
/*    */   public static MappedSet2 create(MappedObject a, MappedObject b) {
/* 48 */     return new MappedSet2(a, b);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static MappedSet3 create(MappedObject a, MappedObject b, MappedObject c) {
/* 57 */     return new MappedSet3(a, b, c);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static MappedSet4 create(MappedObject a, MappedObject b, MappedObject c, MappedObject d) {
/* 66 */     return new MappedSet4(a, b, c, d);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjg\\util\mapped\MappedSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */