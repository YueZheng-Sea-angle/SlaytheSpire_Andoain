/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Objects;
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
/*     */ public final class ReflectionUtil
/*     */ {
/*     */   public static <T extends java.lang.reflect.AccessibleObject & Member> boolean isAccessible(T member) {
/*  46 */     Objects.requireNonNull(member, "No member provided");
/*  47 */     return (Modifier.isPublic(((Member)member).getModifiers()) && Modifier.isPublic(((Member)member).getDeclaringClass().getModifiers()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends java.lang.reflect.AccessibleObject & Member> void makeAccessible(T member) {
/*  58 */     if (!isAccessible(member) && !member.isAccessible()) {
/*  59 */       member.setAccessible(true);
/*     */     }
/*     */   }
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
/*     */   public static void makeAccessible(Field field) {
/*  73 */     Objects.requireNonNull(field, "No field provided");
/*  74 */     if ((!isAccessible(field) || Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
/*  75 */       field.setAccessible(true);
/*     */     }
/*     */   }
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
/*     */   public static Object getFieldValue(Field field, Object instance) {
/*  90 */     makeAccessible(field);
/*  91 */     if (!Modifier.isStatic(field.getModifiers())) {
/*  92 */       Objects.requireNonNull(instance, "No instance given for non-static field");
/*     */     }
/*     */     try {
/*  95 */       return field.get(instance);
/*  96 */     } catch (IllegalAccessException e) {
/*  97 */       throw new UnsupportedOperationException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object getStaticFieldValue(Field field) {
/* 110 */     return getFieldValue(field, null);
/*     */   }
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
/*     */   public static void setFieldValue(Field field, Object instance, Object value) {
/* 124 */     makeAccessible(field);
/* 125 */     if (!Modifier.isStatic(field.getModifiers())) {
/* 126 */       Objects.requireNonNull(instance, "No instance given for non-static field");
/*     */     }
/*     */     try {
/* 129 */       field.set(instance, value);
/* 130 */     } catch (IllegalAccessException e) {
/* 131 */       throw new UnsupportedOperationException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setStaticFieldValue(Field field, Object value) {
/* 144 */     setFieldValue(field, null, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Constructor<T> getDefaultConstructor(Class<T> clazz) {
/* 156 */     Objects.requireNonNull(clazz, "No class provided");
/*     */     try {
/* 158 */       Constructor<T> constructor = clazz.getDeclaredConstructor(new Class[0]);
/* 159 */       makeAccessible(constructor);
/* 160 */       return constructor;
/* 161 */     } catch (NoSuchMethodException ignored) {
/*     */       try {
/* 163 */         Constructor<T> constructor = clazz.getConstructor(new Class[0]);
/* 164 */         makeAccessible(constructor);
/* 165 */         return constructor;
/* 166 */       } catch (NoSuchMethodException e) {
/* 167 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     } 
/*     */   }
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
/*     */   public static <T> T instantiate(Class<T> clazz) {
/* 186 */     Objects.requireNonNull(clazz, "No class provided");
/* 187 */     Constructor<T> constructor = getDefaultConstructor(clazz);
/*     */     try {
/* 189 */       return constructor.newInstance(new Object[0]);
/* 190 */     } catch (LinkageError|InstantiationException e) {
/*     */ 
/*     */       
/* 193 */       throw new IllegalArgumentException(e);
/* 194 */     } catch (IllegalAccessException e) {
/* 195 */       throw new IllegalStateException(e);
/* 196 */     } catch (InvocationTargetException e) {
/* 197 */       Throwables.rethrow(e.getCause());
/* 198 */       throw new InternalError("Unreachable");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\ReflectionUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */