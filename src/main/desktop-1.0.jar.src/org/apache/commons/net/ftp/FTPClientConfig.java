/*     */ package org.apache.commons.net.ftp;
/*     */ 
/*     */ import java.text.DateFormatSymbols;
/*     */ import java.util.Collection;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.TreeMap;
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
/*     */ public class FTPClientConfig
/*     */ {
/*     */   public static final String SYST_UNIX = "UNIX";
/*     */   public static final String SYST_UNIX_TRIM_LEADING = "UNIX_LTRIM";
/*     */   public static final String SYST_VMS = "VMS";
/*     */   public static final String SYST_NT = "WINDOWS";
/*     */   public static final String SYST_OS2 = "OS/2";
/*     */   public static final String SYST_OS400 = "OS/400";
/*     */   public static final String SYST_AS400 = "AS/400";
/*     */   public static final String SYST_MVS = "MVS";
/*     */   public static final String SYST_L8 = "TYPE: L8";
/*     */   public static final String SYST_NETWARE = "NETWARE";
/*     */   public static final String SYST_MACOS_PETER = "MACOS PETER";
/*     */   private final String serverSystemKey;
/* 223 */   private String defaultDateFormatStr = null;
/* 224 */   private String recentDateFormatStr = null;
/*     */   private boolean lenientFutureDates = true;
/* 226 */   private String serverLanguageCode = null;
/* 227 */   private String shortMonthNames = null;
/* 228 */   private String serverTimeZoneId = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean saveUnparseableEntries = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTPClientConfig(String systemKey) {
/* 241 */     this.serverSystemKey = systemKey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTPClientConfig() {
/* 249 */     this("UNIX");
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
/*     */   public FTPClientConfig(String systemKey, String defaultDateFormatStr, String recentDateFormatStr, String serverLanguageCode, String shortMonthNames, String serverTimeZoneId) {
/* 275 */     this(systemKey);
/* 276 */     this.defaultDateFormatStr = defaultDateFormatStr;
/* 277 */     this.recentDateFormatStr = recentDateFormatStr;
/* 278 */     this.serverLanguageCode = serverLanguageCode;
/* 279 */     this.shortMonthNames = shortMonthNames;
/* 280 */     this.serverTimeZoneId = serverTimeZoneId;
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
/*     */   public FTPClientConfig(String systemKey, String defaultDateFormatStr, String recentDateFormatStr, String serverLanguageCode, String shortMonthNames, String serverTimeZoneId, boolean lenientFutureDates, boolean saveUnparseableEntries) {
/* 312 */     this(systemKey);
/* 313 */     this.defaultDateFormatStr = defaultDateFormatStr;
/* 314 */     this.lenientFutureDates = lenientFutureDates;
/* 315 */     this.recentDateFormatStr = recentDateFormatStr;
/* 316 */     this.saveUnparseableEntries = saveUnparseableEntries;
/* 317 */     this.serverLanguageCode = serverLanguageCode;
/* 318 */     this.shortMonthNames = shortMonthNames;
/* 319 */     this.serverTimeZoneId = serverTimeZoneId;
/*     */   }
/*     */ 
/*     */   
/*     */   FTPClientConfig(String systemKey, FTPClientConfig config) {
/* 324 */     this.serverSystemKey = systemKey;
/* 325 */     this.defaultDateFormatStr = config.defaultDateFormatStr;
/* 326 */     this.lenientFutureDates = config.lenientFutureDates;
/* 327 */     this.recentDateFormatStr = config.recentDateFormatStr;
/* 328 */     this.saveUnparseableEntries = config.saveUnparseableEntries;
/* 329 */     this.serverLanguageCode = config.serverLanguageCode;
/* 330 */     this.serverTimeZoneId = config.serverTimeZoneId;
/* 331 */     this.shortMonthNames = config.shortMonthNames;
/*     */   }
/*     */   
/* 334 */   private static final Map<String, Object> LANGUAGE_CODE_MAP = new TreeMap<String, Object>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 344 */     LANGUAGE_CODE_MAP.put("en", Locale.ENGLISH);
/* 345 */     LANGUAGE_CODE_MAP.put("de", Locale.GERMAN);
/* 346 */     LANGUAGE_CODE_MAP.put("it", Locale.ITALIAN);
/* 347 */     LANGUAGE_CODE_MAP.put("es", new Locale("es", "", ""));
/* 348 */     LANGUAGE_CODE_MAP.put("pt", new Locale("pt", "", ""));
/* 349 */     LANGUAGE_CODE_MAP.put("da", new Locale("da", "", ""));
/* 350 */     LANGUAGE_CODE_MAP.put("sv", new Locale("sv", "", ""));
/* 351 */     LANGUAGE_CODE_MAP.put("no", new Locale("no", "", ""));
/* 352 */     LANGUAGE_CODE_MAP.put("nl", new Locale("nl", "", ""));
/* 353 */     LANGUAGE_CODE_MAP.put("ro", new Locale("ro", "", ""));
/* 354 */     LANGUAGE_CODE_MAP.put("sq", new Locale("sq", "", ""));
/* 355 */     LANGUAGE_CODE_MAP.put("sh", new Locale("sh", "", ""));
/* 356 */     LANGUAGE_CODE_MAP.put("sk", new Locale("sk", "", ""));
/* 357 */     LANGUAGE_CODE_MAP.put("sl", new Locale("sl", "", ""));
/*     */ 
/*     */ 
/*     */     
/* 361 */     LANGUAGE_CODE_MAP.put("fr", "jan|fév|mar|avr|mai|jun|jui|aoû|sep|oct|nov|déc");
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
/*     */   public String getServerSystemKey() {
/* 376 */     return this.serverSystemKey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefaultDateFormatStr() {
/* 385 */     return this.defaultDateFormatStr;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRecentDateFormatStr() {
/* 394 */     return this.recentDateFormatStr;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getServerTimeZoneId() {
/* 402 */     return this.serverTimeZoneId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getShortMonthNames() {
/* 413 */     return this.shortMonthNames;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getServerLanguageCode() {
/* 423 */     return this.serverLanguageCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLenientFutureDates() {
/* 434 */     return this.lenientFutureDates;
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
/*     */   public void setDefaultDateFormatStr(String defaultDateFormatStr) {
/* 451 */     this.defaultDateFormatStr = defaultDateFormatStr;
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
/*     */   public void setRecentDateFormatStr(String recentDateFormatStr) {
/* 472 */     this.recentDateFormatStr = recentDateFormatStr;
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
/*     */   public void setLenientFutureDates(boolean lenientFutureDates) {
/* 496 */     this.lenientFutureDates = lenientFutureDates;
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
/*     */   public void setServerTimeZoneId(String serverTimeZoneId) {
/* 513 */     this.serverTimeZoneId = serverTimeZoneId;
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
/*     */   public void setShortMonthNames(String shortMonthNames) {
/* 534 */     this.shortMonthNames = shortMonthNames;
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
/*     */   
/*     */   public void setServerLanguageCode(String serverLanguageCode) {
/* 578 */     this.serverLanguageCode = serverLanguageCode;
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
/*     */   public static DateFormatSymbols lookupDateFormatSymbols(String languageCode) {
/* 595 */     Object lang = LANGUAGE_CODE_MAP.get(languageCode);
/* 596 */     if (lang != null) {
/* 597 */       if (lang instanceof Locale)
/* 598 */         return new DateFormatSymbols((Locale)lang); 
/* 599 */       if (lang instanceof String) {
/* 600 */         return getDateFormatSymbols((String)lang);
/*     */       }
/*     */     } 
/* 603 */     return new DateFormatSymbols(Locale.US);
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
/*     */   public static DateFormatSymbols getDateFormatSymbols(String shortmonths) {
/* 616 */     String[] months = splitShortMonthString(shortmonths);
/* 617 */     DateFormatSymbols dfs = new DateFormatSymbols(Locale.US);
/* 618 */     dfs.setShortMonths(months);
/* 619 */     return dfs;
/*     */   }
/*     */   
/*     */   private static String[] splitShortMonthString(String shortmonths) {
/* 623 */     StringTokenizer st = new StringTokenizer(shortmonths, "|");
/* 624 */     int monthcnt = st.countTokens();
/* 625 */     if (12 != monthcnt) {
/* 626 */       throw new IllegalArgumentException("expecting a pipe-delimited string containing 12 tokens");
/*     */     }
/*     */     
/* 629 */     String[] months = new String[13];
/* 630 */     int pos = 0;
/* 631 */     while (st.hasMoreTokens()) {
/* 632 */       months[pos++] = st.nextToken();
/*     */     }
/* 634 */     months[pos] = "";
/* 635 */     return months;
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
/*     */   public static Collection<String> getSupportedLanguageCodes() {
/* 647 */     return LANGUAGE_CODE_MAP.keySet();
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
/*     */   public void setUnparseableEntries(boolean saveUnparseable) {
/* 659 */     this.saveUnparseableEntries = saveUnparseable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getUnparseableEntries() {
/* 670 */     return this.saveUnparseableEntries;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\ftp\FTPClientConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */