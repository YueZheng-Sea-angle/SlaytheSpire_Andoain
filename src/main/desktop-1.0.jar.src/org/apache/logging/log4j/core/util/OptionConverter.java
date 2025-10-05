/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Locale;
/*     */ import java.util.Properties;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ import org.apache.logging.log4j.util.Strings;
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
/*     */ public final class OptionConverter
/*     */ {
/*  34 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */   
/*     */   private static final String DELIM_START = "${";
/*     */   
/*     */   private static final char DELIM_STOP = '}';
/*     */   
/*     */   private static final int DELIM_START_LEN = 2;
/*     */   
/*     */   private static final int DELIM_STOP_LEN = 1;
/*     */   
/*     */   private static final int ONE_K = 1024;
/*     */ 
/*     */   
/*     */   public static String[] concatenateArrays(String[] l, String[] r) {
/*  49 */     int len = l.length + r.length;
/*  50 */     String[] a = new String[len];
/*     */     
/*  52 */     System.arraycopy(l, 0, a, 0, l.length);
/*  53 */     System.arraycopy(r, 0, a, l.length, r.length);
/*     */     
/*  55 */     return a;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String convertSpecialChars(String s) {
/*  60 */     int len = s.length();
/*  61 */     StringBuilder sbuf = new StringBuilder(len);
/*     */     
/*  63 */     int i = 0;
/*  64 */     while (i < len) {
/*  65 */       char c = s.charAt(i++);
/*  66 */       if (c == '\\') {
/*  67 */         c = s.charAt(i++);
/*  68 */         switch (c) {
/*     */           case 'n':
/*  70 */             c = '\n';
/*     */             break;
/*     */           case 'r':
/*  73 */             c = '\r';
/*     */             break;
/*     */           case 't':
/*  76 */             c = '\t';
/*     */             break;
/*     */           case 'f':
/*  79 */             c = '\f';
/*     */             break;
/*     */           case 'b':
/*  82 */             c = '\b';
/*     */             break;
/*     */           case '"':
/*  85 */             c = '"';
/*     */             break;
/*     */           case '\'':
/*  88 */             c = '\'';
/*     */             break;
/*     */           case '\\':
/*  91 */             c = '\\';
/*     */             break;
/*     */         } 
/*     */ 
/*     */       
/*     */       } 
/*  97 */       sbuf.append(c);
/*     */     } 
/*  99 */     return sbuf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object instantiateByKey(Properties props, String key, Class<?> superClass, Object defaultValue) {
/* 106 */     String className = findAndSubst(key, props);
/* 107 */     if (className == null) {
/* 108 */       LOGGER.error("Could not find value for key {}", key);
/* 109 */       return defaultValue;
/*     */     } 
/*     */     
/* 112 */     return instantiateByClassName(className.trim(), superClass, defaultValue);
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
/*     */   public static boolean toBoolean(String value, boolean defaultValue) {
/* 128 */     if (value == null) {
/* 129 */       return defaultValue;
/*     */     }
/* 131 */     String trimmedVal = value.trim();
/* 132 */     if ("true".equalsIgnoreCase(trimmedVal)) {
/* 133 */       return true;
/*     */     }
/* 135 */     if ("false".equalsIgnoreCase(trimmedVal)) {
/* 136 */       return false;
/*     */     }
/* 138 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int toInt(String value, int defaultValue) {
/* 148 */     if (value != null) {
/* 149 */       String s = value.trim();
/*     */       try {
/* 151 */         return Integer.parseInt(s);
/* 152 */       } catch (NumberFormatException e) {
/* 153 */         LOGGER.error("[{}] is not in proper int form.", s, e);
/*     */       } 
/*     */     } 
/* 156 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public static Level toLevel(String value, Level defaultValue) {
/* 160 */     if (value == null) {
/* 161 */       return defaultValue;
/*     */     }
/*     */     
/* 164 */     value = value.trim();
/*     */     
/* 166 */     int hashIndex = value.indexOf('#');
/* 167 */     if (hashIndex == -1) {
/* 168 */       if ("NULL".equalsIgnoreCase(value)) {
/* 169 */         return null;
/*     */       }
/*     */       
/* 172 */       return Level.toLevel(value, defaultValue);
/*     */     } 
/*     */ 
/*     */     
/* 176 */     Level result = defaultValue;
/*     */     
/* 178 */     String clazz = value.substring(hashIndex + 1);
/* 179 */     String levelName = value.substring(0, hashIndex);
/*     */ 
/*     */     
/* 182 */     if ("NULL".equalsIgnoreCase(levelName)) {
/* 183 */       return null;
/*     */     }
/*     */     
/* 186 */     LOGGER.debug("toLevel:class=[" + clazz + "]:pri=[" + levelName + "]");
/*     */ 
/*     */     
/*     */     try {
/* 190 */       Class<?> customLevel = Loader.loadClass(clazz);
/*     */ 
/*     */ 
/*     */       
/* 194 */       Class<?>[] paramTypes = new Class[] { String.class, Level.class };
/*     */       
/* 196 */       Method toLevelMethod = customLevel.getMethod("toLevel", paramTypes);
/*     */ 
/*     */       
/* 199 */       Object[] params = { levelName, defaultValue };
/* 200 */       Object o = toLevelMethod.invoke(null, params);
/*     */       
/* 202 */       result = (Level)o;
/* 203 */     } catch (ClassNotFoundException e) {
/* 204 */       LOGGER.warn("custom level class [" + clazz + "] not found.");
/* 205 */     } catch (NoSuchMethodException e) {
/* 206 */       LOGGER.warn("custom level class [" + clazz + "] does not have a class function toLevel(String, Level)", e);
/*     */     }
/* 208 */     catch (InvocationTargetException e) {
/* 209 */       if (e.getTargetException() instanceof InterruptedException || e
/* 210 */         .getTargetException() instanceof java.io.InterruptedIOException) {
/* 211 */         Thread.currentThread().interrupt();
/*     */       }
/* 213 */       LOGGER.warn("custom level class [" + clazz + "] could not be instantiated", e);
/* 214 */     } catch (ClassCastException e) {
/* 215 */       LOGGER.warn("class [" + clazz + "] is not a subclass of org.apache.log4j.Level", e);
/* 216 */     } catch (IllegalAccessException e) {
/* 217 */       LOGGER.warn("class [" + clazz + "] cannot be instantiated due to access restrictions", e);
/* 218 */     } catch (RuntimeException e) {
/* 219 */       LOGGER.warn("class [" + clazz + "], level [" + levelName + "] conversion failed.", e);
/*     */     } 
/* 221 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long toFileSize(String value, long defaultValue) {
/* 231 */     if (value == null) {
/* 232 */       return defaultValue;
/*     */     }
/*     */     
/* 235 */     String str = value.trim().toUpperCase(Locale.ENGLISH);
/* 236 */     long multiplier = 1L;
/*     */     
/*     */     int index;
/* 239 */     if ((index = str.indexOf("KB")) != -1) {
/* 240 */       multiplier = 1024L;
/* 241 */       str = str.substring(0, index);
/* 242 */     } else if ((index = str.indexOf("MB")) != -1) {
/* 243 */       multiplier = 1048576L;
/* 244 */       str = str.substring(0, index);
/* 245 */     } else if ((index = str.indexOf("GB")) != -1) {
/* 246 */       multiplier = 1073741824L;
/* 247 */       str = str.substring(0, index);
/*     */     } 
/*     */     try {
/* 250 */       return Long.parseLong(str) * multiplier;
/* 251 */     } catch (NumberFormatException e) {
/* 252 */       LOGGER.error("[{}] is not in proper int form.", str);
/* 253 */       LOGGER.error("[{}] not in expected format.", value, e);
/*     */       
/* 255 */       return defaultValue;
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
/*     */   public static String findAndSubst(String key, Properties props) {
/* 267 */     String value = props.getProperty(key);
/* 268 */     if (value == null) {
/* 269 */       return null;
/*     */     }
/*     */     
/*     */     try {
/* 273 */       return substVars(value, props);
/* 274 */     } catch (IllegalArgumentException e) {
/* 275 */       LOGGER.error("Bad option value [{}].", value, e);
/* 276 */       return value;
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
/*     */   public static Object instantiateByClassName(String className, Class<?> superClass, Object defaultValue) {
/* 293 */     if (className != null) {
/*     */       try {
/* 295 */         Class<?> classObj = Loader.loadClass(className);
/* 296 */         if (!superClass.isAssignableFrom(classObj)) {
/* 297 */           LOGGER.error("A \"{}\" object is not assignable to a \"{}\" variable.", className, superClass
/* 298 */               .getName());
/* 299 */           LOGGER.error("The class \"{}\" was loaded by [{}] whereas object of type [{}] was loaded by [{}].", superClass
/* 300 */               .getName(), superClass.getClassLoader(), classObj.getTypeName(), classObj.getName());
/* 301 */           return defaultValue;
/*     */         } 
/* 303 */         return classObj.newInstance();
/* 304 */       } catch (Exception e) {
/* 305 */         LOGGER.error("Could not instantiate class [{}].", className, e);
/*     */       } 
/*     */     }
/* 308 */     return defaultValue;
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
/*     */   public static String substVars(String val, Properties props) throws IllegalArgumentException {
/* 351 */     StringBuilder sbuf = new StringBuilder();
/*     */     
/* 353 */     int i = 0;
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 358 */       int j = val.indexOf("${", i);
/* 359 */       if (j == -1) {
/*     */         
/* 361 */         if (i == 0) {
/* 362 */           return val;
/*     */         }
/*     */         
/* 365 */         sbuf.append(val.substring(i, val.length()));
/* 366 */         return sbuf.toString();
/*     */       } 
/* 368 */       sbuf.append(val.substring(i, j));
/* 369 */       int k = val.indexOf('}', j);
/* 370 */       if (k == -1) {
/* 371 */         throw new IllegalArgumentException(Strings.dquote(val) + " has no closing brace. Opening brace at position " + j + '.');
/*     */       }
/*     */ 
/*     */       
/* 375 */       j += 2;
/* 376 */       String key = val.substring(j, k);
/*     */       
/* 378 */       String replacement = PropertiesUtil.getProperties().getStringProperty(key, null);
/*     */       
/* 380 */       if (replacement == null && props != null) {
/* 381 */         replacement = props.getProperty(key);
/*     */       }
/*     */       
/* 384 */       if (replacement != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 390 */         String recursiveReplacement = substVars(replacement, props);
/* 391 */         sbuf.append(recursiveReplacement);
/*     */       } 
/* 393 */       i = k + 1;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\OptionConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */