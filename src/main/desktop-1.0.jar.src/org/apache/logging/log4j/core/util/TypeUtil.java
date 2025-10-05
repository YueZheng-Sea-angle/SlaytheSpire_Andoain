/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.WildcardType;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class TypeUtil
/*     */ {
/*     */   public static List<Field> getAllDeclaredFields(Class<?> cls) {
/*  55 */     List<Field> fields = new ArrayList<>();
/*  56 */     while (cls != null) {
/*  57 */       Collections.addAll(fields, cls.getDeclaredFields());
/*  58 */       cls = cls.getSuperclass();
/*     */     } 
/*  60 */     return fields;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isAssignable(Type lhs, Type rhs) {
/*  71 */     Objects.requireNonNull(lhs, "No left hand side type provided");
/*  72 */     Objects.requireNonNull(rhs, "No right hand side type provided");
/*  73 */     if (lhs.equals(rhs)) {
/*  74 */       return true;
/*     */     }
/*  76 */     if (Object.class.equals(lhs))
/*     */     {
/*  78 */       return true;
/*     */     }
/*     */     
/*  81 */     if (lhs instanceof Class) {
/*  82 */       Class<?> lhsClass = (Class)lhs;
/*  83 */       if (rhs instanceof Class) {
/*     */         
/*  85 */         Class<?> rhsClass = (Class)rhs;
/*  86 */         return lhsClass.isAssignableFrom(rhsClass);
/*     */       } 
/*  88 */       if (rhs instanceof ParameterizedType) {
/*     */         
/*  90 */         Type rhsRawType = ((ParameterizedType)rhs).getRawType();
/*  91 */         if (rhsRawType instanceof Class) {
/*  92 */           return lhsClass.isAssignableFrom((Class)rhsRawType);
/*     */         }
/*     */       } 
/*  95 */       if (lhsClass.isArray() && rhs instanceof GenericArrayType)
/*     */       {
/*  97 */         return isAssignable(lhsClass.getComponentType(), ((GenericArrayType)rhs).getGenericComponentType());
/*     */       }
/*     */     } 
/*     */     
/* 101 */     if (lhs instanceof ParameterizedType) {
/* 102 */       ParameterizedType lhsType = (ParameterizedType)lhs;
/* 103 */       if (rhs instanceof Class) {
/* 104 */         Type lhsRawType = lhsType.getRawType();
/* 105 */         if (lhsRawType instanceof Class) {
/* 106 */           return ((Class)lhsRawType).isAssignableFrom((Class)rhs);
/*     */         }
/* 108 */       } else if (rhs instanceof ParameterizedType) {
/* 109 */         ParameterizedType rhsType = (ParameterizedType)rhs;
/* 110 */         return isParameterizedAssignable(lhsType, rhsType);
/*     */       } 
/*     */     } 
/*     */     
/* 114 */     if (lhs instanceof GenericArrayType) {
/* 115 */       Type lhsComponentType = ((GenericArrayType)lhs).getGenericComponentType();
/* 116 */       if (rhs instanceof Class) {
/*     */         
/* 118 */         Class<?> rhsClass = (Class)rhs;
/* 119 */         if (rhsClass.isArray()) {
/* 120 */           return isAssignable(lhsComponentType, rhsClass.getComponentType());
/*     */         }
/* 122 */       } else if (rhs instanceof GenericArrayType) {
/* 123 */         return isAssignable(lhsComponentType, ((GenericArrayType)rhs).getGenericComponentType());
/*     */       } 
/*     */     } 
/*     */     
/* 127 */     if (lhs instanceof WildcardType) {
/* 128 */       return isWildcardAssignable((WildcardType)lhs, rhs);
/*     */     }
/*     */     
/* 131 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean isParameterizedAssignable(ParameterizedType lhs, ParameterizedType rhs) {
/* 135 */     if (lhs.equals(rhs))
/*     */     {
/* 137 */       return true;
/*     */     }
/* 139 */     Type[] lhsTypeArguments = lhs.getActualTypeArguments();
/* 140 */     Type[] rhsTypeArguments = rhs.getActualTypeArguments();
/* 141 */     int size = lhsTypeArguments.length;
/* 142 */     if (rhsTypeArguments.length != size)
/*     */     {
/* 144 */       return false;
/*     */     }
/* 146 */     for (int i = 0; i < size; i++) {
/*     */       
/* 148 */       Type lhsArgument = lhsTypeArguments[i];
/* 149 */       Type rhsArgument = rhsTypeArguments[i];
/* 150 */       if (!lhsArgument.equals(rhsArgument) && (!(lhsArgument instanceof WildcardType) || 
/*     */         
/* 152 */         !isWildcardAssignable((WildcardType)lhsArgument, rhsArgument))) {
/* 153 */         return false;
/*     */       }
/*     */     } 
/* 156 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean isWildcardAssignable(WildcardType lhs, Type rhs) {
/* 160 */     Type[] lhsUpperBounds = getEffectiveUpperBounds(lhs);
/* 161 */     Type[] lhsLowerBounds = getEffectiveLowerBounds(lhs);
/* 162 */     if (rhs instanceof WildcardType) {
/*     */       
/* 164 */       WildcardType rhsType = (WildcardType)rhs;
/* 165 */       Type[] rhsUpperBounds = getEffectiveUpperBounds(rhsType);
/* 166 */       Type[] rhsLowerBounds = getEffectiveLowerBounds(rhsType);
/* 167 */       for (Type lhsUpperBound : lhsUpperBounds) {
/* 168 */         for (Type rhsUpperBound : rhsUpperBounds) {
/* 169 */           if (!isBoundAssignable(lhsUpperBound, rhsUpperBound)) {
/* 170 */             return false;
/*     */           }
/*     */         } 
/* 173 */         for (Type rhsLowerBound : rhsLowerBounds) {
/* 174 */           if (!isBoundAssignable(lhsUpperBound, rhsLowerBound)) {
/* 175 */             return false;
/*     */           }
/*     */         } 
/*     */       } 
/* 179 */       for (Type lhsLowerBound : lhsLowerBounds) {
/* 180 */         for (Type rhsUpperBound : rhsUpperBounds) {
/* 181 */           if (!isBoundAssignable(rhsUpperBound, lhsLowerBound)) {
/* 182 */             return false;
/*     */           }
/*     */         } 
/* 185 */         for (Type rhsLowerBound : rhsLowerBounds) {
/* 186 */           if (!isBoundAssignable(rhsLowerBound, lhsLowerBound)) {
/* 187 */             return false;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 193 */       for (Type lhsUpperBound : lhsUpperBounds) {
/* 194 */         if (!isBoundAssignable(lhsUpperBound, rhs)) {
/* 195 */           return false;
/*     */         }
/*     */       } 
/* 198 */       for (Type lhsLowerBound : lhsLowerBounds) {
/* 199 */         if (!isBoundAssignable(lhsLowerBound, rhs)) {
/* 200 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/* 204 */     return true;
/*     */   }
/*     */   
/*     */   private static Type[] getEffectiveUpperBounds(WildcardType type) {
/* 208 */     Type[] upperBounds = type.getUpperBounds();
/* 209 */     (new Type[1])[0] = Object.class; return (upperBounds.length == 0) ? new Type[1] : upperBounds;
/*     */   }
/*     */   
/*     */   private static Type[] getEffectiveLowerBounds(WildcardType type) {
/* 213 */     Type[] lowerBounds = type.getLowerBounds();
/* 214 */     (new Type[1])[0] = null; return (lowerBounds.length == 0) ? new Type[1] : lowerBounds;
/*     */   }
/*     */   
/*     */   private static boolean isBoundAssignable(Type lhs, Type rhs) {
/* 218 */     return (rhs == null || (lhs != null && isAssignable(lhs, rhs)));
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\TypeUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */