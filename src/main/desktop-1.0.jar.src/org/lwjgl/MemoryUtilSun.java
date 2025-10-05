/*     */ package org.lwjgl;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.nio.Buffer;
/*     */ import sun.misc.Unsafe;
/*     */ import sun.reflect.FieldAccessor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class MemoryUtilSun
/*     */ {
/*     */   private static class AccessorUnsafe
/*     */     implements MemoryUtil.Accessor
/*     */   {
/*     */     private final Unsafe unsafe;
/*     */     private final long address;
/*     */     
/*     */     AccessorUnsafe() {
/*     */       try {
/*  62 */         this.unsafe = getUnsafeInstance();
/*  63 */         this.address = this.unsafe.objectFieldOffset(MemoryUtil.getAddressField());
/*  64 */       } catch (Exception e) {
/*  65 */         throw new UnsupportedOperationException(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public long getAddress(Buffer buffer) {
/*  70 */       return this.unsafe.getLong(buffer, this.address);
/*     */     }
/*     */     
/*     */     private static Unsafe getUnsafeInstance() {
/*  74 */       Field[] fields = Unsafe.class.getDeclaredFields();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  84 */       for (Field field : fields) {
/*  85 */         if (field.getType().equals(Unsafe.class)) {
/*     */ 
/*     */           
/*  88 */           int modifiers = field.getModifiers();
/*  89 */           if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) {
/*     */ 
/*     */             
/*  92 */             field.setAccessible(true);
/*     */             try {
/*  94 */               return (Unsafe)field.get(null);
/*  95 */             } catch (IllegalAccessException e) {
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 101 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class AccessorReflectFast
/*     */     implements MemoryUtil.Accessor
/*     */   {
/*     */     private final FieldAccessor addressAccessor;
/*     */     
/*     */     AccessorReflectFast() {
/*     */       Field address;
/*     */       try {
/* 114 */         address = MemoryUtil.getAddressField();
/* 115 */       } catch (NoSuchFieldException e) {
/* 116 */         throw new UnsupportedOperationException(e);
/*     */       } 
/* 118 */       address.setAccessible(true);
/*     */       
/*     */       try {
/* 121 */         Method m = Field.class.getDeclaredMethod("acquireFieldAccessor", new Class[] { boolean.class });
/* 122 */         m.setAccessible(true);
/* 123 */         this.addressAccessor = (FieldAccessor)m.invoke(address, new Object[] { Boolean.valueOf(true) });
/* 124 */       } catch (Exception e) {
/* 125 */         throw new UnsupportedOperationException(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public long getAddress(Buffer buffer) {
/* 130 */       return this.addressAccessor.getLong(buffer);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\MemoryUtilSun.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */