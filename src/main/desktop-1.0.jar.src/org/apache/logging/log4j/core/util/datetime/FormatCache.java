/*     */ package org.apache.logging.log4j.core.util.datetime;
/*     */ 
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Arrays;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
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
/*     */ abstract class FormatCache<F extends Format>
/*     */ {
/*     */   static final int NONE = -1;
/*  45 */   private final ConcurrentMap<MultipartKey, F> cInstanceCache = new ConcurrentHashMap<>(7);
/*     */ 
/*     */   
/*  48 */   private static final ConcurrentMap<MultipartKey, String> cDateTimeInstanceCache = new ConcurrentHashMap<>(7);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public F getInstance() {
/*  58 */     return getDateTimeInstance(3, 3, TimeZone.getDefault(), Locale.getDefault());
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
/*     */   public F getInstance(String pattern, TimeZone timeZone, Locale locale) {
/*  74 */     if (pattern == null) {
/*  75 */       throw new NullPointerException("pattern must not be null");
/*     */     }
/*  77 */     if (timeZone == null) {
/*  78 */       timeZone = TimeZone.getDefault();
/*     */     }
/*  80 */     if (locale == null) {
/*  81 */       locale = Locale.getDefault();
/*     */     }
/*  83 */     MultipartKey key = new MultipartKey(new Object[] { pattern, timeZone, locale });
/*  84 */     Format format = (Format)this.cInstanceCache.get(key);
/*  85 */     if (format == null) {
/*  86 */       format = (Format)createInstance(pattern, timeZone, locale);
/*  87 */       Format format1 = (Format)this.cInstanceCache.putIfAbsent(key, (F)format);
/*  88 */       if (format1 != null)
/*     */       {
/*     */         
/*  91 */         format = format1;
/*     */       }
/*     */     } 
/*  94 */     return (F)format;
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
/*     */   protected abstract F createInstance(String paramString, TimeZone paramTimeZone, Locale paramLocale);
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
/*     */   private F getDateTimeInstance(Integer dateStyle, Integer timeStyle, TimeZone timeZone, Locale locale) {
/* 125 */     if (locale == null) {
/* 126 */       locale = Locale.getDefault();
/*     */     }
/* 128 */     String pattern = getPatternForStyle(dateStyle, timeStyle, locale);
/* 129 */     return getInstance(pattern, timeZone, locale);
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
/*     */   F getDateTimeInstance(int dateStyle, int timeStyle, TimeZone timeZone, Locale locale) {
/* 147 */     return getDateTimeInstance(Integer.valueOf(dateStyle), Integer.valueOf(timeStyle), timeZone, locale);
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
/*     */   F getDateInstance(int dateStyle, TimeZone timeZone, Locale locale) {
/* 164 */     return getDateTimeInstance(Integer.valueOf(dateStyle), (Integer)null, timeZone, locale);
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
/*     */   F getTimeInstance(int timeStyle, TimeZone timeZone, Locale locale) {
/* 181 */     return getDateTimeInstance((Integer)null, Integer.valueOf(timeStyle), timeZone, locale);
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
/*     */   static String getPatternForStyle(Integer dateStyle, Integer timeStyle, Locale locale) {
/* 195 */     MultipartKey key = new MultipartKey(new Object[] { dateStyle, timeStyle, locale });
/*     */     
/* 197 */     String pattern = cDateTimeInstanceCache.get(key);
/* 198 */     if (pattern == null) {
/*     */       try {
/*     */         DateFormat formatter;
/* 201 */         if (dateStyle == null) {
/* 202 */           formatter = DateFormat.getTimeInstance(timeStyle.intValue(), locale);
/*     */         }
/* 204 */         else if (timeStyle == null) {
/* 205 */           formatter = DateFormat.getDateInstance(dateStyle.intValue(), locale);
/*     */         } else {
/*     */           
/* 208 */           formatter = DateFormat.getDateTimeInstance(dateStyle.intValue(), timeStyle.intValue(), locale);
/*     */         } 
/* 210 */         pattern = ((SimpleDateFormat)formatter).toPattern();
/* 211 */         String previous = cDateTimeInstanceCache.putIfAbsent(key, pattern);
/* 212 */         if (previous != null)
/*     */         {
/*     */ 
/*     */           
/* 216 */           pattern = previous;
/*     */         }
/* 218 */       } catch (ClassCastException ex) {
/* 219 */         throw new IllegalArgumentException("No date time pattern for locale: " + locale);
/*     */       } 
/*     */     }
/* 222 */     return pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class MultipartKey
/*     */   {
/*     */     private final Object[] keys;
/*     */ 
/*     */     
/*     */     private int hashCode;
/*     */ 
/*     */ 
/*     */     
/*     */     public MultipartKey(Object... keys) {
/* 238 */       this.keys = keys;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 249 */       return Arrays.equals(this.keys, ((MultipartKey)obj).keys);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 257 */       if (this.hashCode == 0) {
/* 258 */         int rc = 0;
/* 259 */         for (Object key : this.keys) {
/* 260 */           if (key != null) {
/* 261 */             rc = rc * 7 + key.hashCode();
/*     */           }
/*     */         } 
/* 264 */         this.hashCode = rc;
/*     */       } 
/* 266 */       return this.hashCode;
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\datetime\FormatCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */