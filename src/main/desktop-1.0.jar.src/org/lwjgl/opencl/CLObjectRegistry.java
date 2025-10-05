/*    */ package org.lwjgl.opencl;
/*    */ 
/*    */ import org.lwjgl.LWJGLUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class CLObjectRegistry<T extends CLObjectChild>
/*    */ {
/*    */   private FastLongMap<T> registry;
/*    */   
/*    */   final boolean isEmpty() {
/* 18 */     return (this.registry == null || this.registry.isEmpty());
/*    */   }
/*    */   
/*    */   final T getObject(long id) {
/* 22 */     return (this.registry == null) ? null : this.registry.get(id);
/*    */   }
/*    */   
/*    */   final boolean hasObject(long id) {
/* 26 */     return (this.registry != null && this.registry.containsKey(id));
/*    */   }
/*    */   
/*    */   final Iterable<FastLongMap.Entry<T>> getAll() {
/* 30 */     return this.registry;
/*    */   }
/*    */   
/*    */   void registerObject(T object) {
/* 34 */     FastLongMap<T> map = getMap();
/* 35 */     Long key = Long.valueOf(object.getPointer());
/*    */     
/* 37 */     if (LWJGLUtil.DEBUG && map.containsKey(key.longValue())) {
/* 38 */       throw new IllegalStateException("Duplicate object found: " + object.getClass() + " - " + key);
/*    */     }
/* 40 */     getMap().put(object.getPointer(), object);
/*    */   }
/*    */   
/*    */   void unregisterObject(T object) {
/* 44 */     getMap().remove(object.getPointerUnsafe());
/*    */   }
/*    */   
/*    */   private FastLongMap<T> getMap() {
/* 48 */     if (this.registry == null) {
/* 49 */       this.registry = new FastLongMap<T>();
/*    */     }
/* 51 */     return this.registry;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opencl\CLObjectRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */