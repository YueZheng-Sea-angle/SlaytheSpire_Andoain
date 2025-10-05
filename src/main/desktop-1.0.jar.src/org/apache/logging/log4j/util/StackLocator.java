/*     */ package org.apache.logging.log4j.util;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Stack;
/*     */ import java.util.function.Predicate;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class StackLocator
/*     */ {
/*     */   static final int JDK_7u25_OFFSET;
/*     */   private static final Method GET_CALLER_CLASS;
/*     */   private static final StackLocator INSTANCE;
/*     */   
/*     */   static {
/*     */     Method getCallerClass;
/*  61 */     int java7u25CompensationOffset = 0;
/*     */     try {
/*  63 */       Class<?> sunReflectionClass = LoaderUtil.loadClass("sun.reflect.Reflection");
/*  64 */       getCallerClass = sunReflectionClass.getDeclaredMethod("getCallerClass", new Class[] { int.class });
/*  65 */       Object o = getCallerClass.invoke(null, new Object[] { Integer.valueOf(0) });
/*  66 */       getCallerClass.invoke(null, new Object[] { Integer.valueOf(0) });
/*  67 */       if (o == null || o != sunReflectionClass) {
/*  68 */         getCallerClass = null;
/*  69 */         java7u25CompensationOffset = -1;
/*     */       } else {
/*  71 */         o = getCallerClass.invoke(null, new Object[] { Integer.valueOf(1) });
/*  72 */         if (o == sunReflectionClass) {
/*  73 */           System.out.println("WARNING: Java 1.7.0_25 is in use which has a broken implementation of Reflection.getCallerClass().  Please consider upgrading to Java 1.7.0_40 or later.");
/*     */           
/*  75 */           java7u25CompensationOffset = 1;
/*     */         } 
/*     */       } 
/*  78 */     } catch (Exception|LinkageError e) {
/*  79 */       System.out.println("WARNING: sun.reflect.Reflection.getCallerClass is not supported. This will impact performance.");
/*  80 */       getCallerClass = null;
/*  81 */       java7u25CompensationOffset = -1;
/*     */     } 
/*     */     
/*  84 */     GET_CALLER_CLASS = getCallerClass;
/*  85 */     JDK_7u25_OFFSET = java7u25CompensationOffset;
/*     */     
/*  87 */     INSTANCE = new StackLocator();
/*     */   }
/*     */   
/*     */   public static StackLocator getInstance() {
/*  91 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PerformanceSensitive
/*     */   public Class<?> getCallerClass(Class<?> sentinelClass, Predicate<Class<?>> callerPredicate) {
/* 102 */     if (sentinelClass == null) {
/* 103 */       throw new IllegalArgumentException("sentinelClass cannot be null");
/*     */     }
/* 105 */     if (callerPredicate == null) {
/* 106 */       throw new IllegalArgumentException("callerPredicate cannot be null");
/*     */     }
/* 108 */     boolean foundSentinel = false;
/*     */     Class<?> clazz;
/* 110 */     for (int i = 2; null != (clazz = getCallerClass(i)); i++) {
/* 111 */       if (sentinelClass.equals(clazz)) {
/* 112 */         foundSentinel = true;
/* 113 */       } else if (foundSentinel && callerPredicate.test(clazz)) {
/* 114 */         return clazz;
/*     */       } 
/*     */     } 
/* 117 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   @PerformanceSensitive
/*     */   public Class<?> getCallerClass(int depth) {
/* 123 */     if (depth < 0) {
/* 124 */       throw new IndexOutOfBoundsException(Integer.toString(depth));
/*     */     }
/* 126 */     if (GET_CALLER_CLASS == null) {
/* 127 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 132 */       return (Class)GET_CALLER_CLASS.invoke(null, new Object[] { Integer.valueOf(depth + 1 + JDK_7u25_OFFSET) });
/* 133 */     } catch (Exception e) {
/*     */ 
/*     */       
/* 136 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @PerformanceSensitive
/*     */   public Class<?> getCallerClass(String fqcn, String pkg) {
/* 143 */     boolean next = false;
/*     */     Class<?> clazz;
/* 145 */     for (int i = 2; null != (clazz = getCallerClass(i)); i++) {
/* 146 */       if (fqcn.equals(clazz.getName())) {
/* 147 */         next = true;
/*     */       
/*     */       }
/* 150 */       else if (next && clazz.getName().startsWith(pkg)) {
/* 151 */         return clazz;
/*     */       } 
/*     */     } 
/*     */     
/* 155 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   @PerformanceSensitive
/*     */   public Class<?> getCallerClass(Class<?> anchor) {
/* 161 */     boolean next = false;
/*     */     Class<?> clazz;
/* 163 */     for (int i = 2; null != (clazz = getCallerClass(i)); i++) {
/* 164 */       if (anchor.equals(clazz)) {
/* 165 */         next = true;
/*     */       
/*     */       }
/* 168 */       else if (next) {
/* 169 */         return clazz;
/*     */       } 
/*     */     } 
/* 172 */     return Object.class;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @PerformanceSensitive
/*     */   public Stack<Class<?>> getCurrentStackTrace() {
/* 179 */     if (PrivateSecurityManagerStackTraceUtil.isEnabled()) {
/* 180 */       return PrivateSecurityManagerStackTraceUtil.getCurrentStackTrace();
/*     */     }
/*     */     
/* 183 */     Stack<Class<?>> classes = new Stack<>();
/*     */     Class<?> clazz;
/* 185 */     for (int i = 1; null != (clazz = getCallerClass(i)); i++) {
/* 186 */       classes.push(clazz);
/*     */     }
/* 188 */     return classes;
/*     */   }
/*     */   
/*     */   public StackTraceElement calcLocation(String fqcnOfLogger) {
/* 192 */     if (fqcnOfLogger == null) {
/* 193 */       return null;
/*     */     }
/*     */     
/* 196 */     StackTraceElement[] stackTrace = (new Throwable()).getStackTrace();
/* 197 */     boolean found = false;
/* 198 */     for (int i = 0; i < stackTrace.length; i++) {
/* 199 */       String className = stackTrace[i].getClassName();
/* 200 */       if (fqcnOfLogger.equals(className)) {
/*     */         
/* 202 */         found = true;
/*     */       
/*     */       }
/* 205 */       else if (found && !fqcnOfLogger.equals(className)) {
/* 206 */         return stackTrace[i];
/*     */       } 
/*     */     } 
/* 209 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public StackTraceElement getStackTraceElement(int depth) {
/* 215 */     StackTraceElement[] elements = (new Throwable()).getStackTrace();
/* 216 */     int i = 0;
/* 217 */     for (StackTraceElement element : elements) {
/* 218 */       if (isValid(element)) {
/* 219 */         if (i == depth) {
/* 220 */           return element;
/*     */         }
/* 222 */         i++;
/*     */       } 
/*     */     } 
/* 225 */     throw new IndexOutOfBoundsException(Integer.toString(depth));
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isValid(StackTraceElement element) {
/* 230 */     if (element.isNativeMethod()) {
/* 231 */       return false;
/*     */     }
/* 233 */     String cn = element.getClassName();
/*     */     
/* 235 */     if (cn.startsWith("sun.reflect.")) {
/* 236 */       return false;
/*     */     }
/* 238 */     String mn = element.getMethodName();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 243 */     if (cn.startsWith("java.lang.reflect.") && (mn.equals("invoke") || mn.equals("newInstance"))) {
/* 244 */       return false;
/*     */     }
/*     */     
/* 247 */     if (cn.startsWith("jdk.internal.reflect.")) {
/* 248 */       return false;
/*     */     }
/*     */     
/* 251 */     if (cn.equals("java.lang.Class") && mn.equals("newInstance")) {
/* 252 */       return false;
/*     */     }
/*     */     
/* 255 */     if (cn.equals("java.lang.invoke.MethodHandle") && mn.startsWith("invoke")) {
/* 256 */       return false;
/*     */     }
/*     */     
/* 259 */     return true;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4\\util\StackLocator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */