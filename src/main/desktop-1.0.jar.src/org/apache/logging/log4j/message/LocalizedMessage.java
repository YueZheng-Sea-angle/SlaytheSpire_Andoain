/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Locale;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LocalizedMessage
/*     */   implements Message, LoggerNameAwareMessage
/*     */ {
/*     */   private static final long serialVersionUID = 3893703791567290742L;
/*     */   private String baseName;
/*     */   private transient ResourceBundle resourceBundle;
/*     */   private final Locale locale;
/*  47 */   private transient StatusLogger logger = StatusLogger.getLogger();
/*     */   
/*     */   private String loggerName;
/*     */   
/*     */   private String key;
/*     */   
/*     */   private String[] stringArgs;
/*     */   
/*     */   private transient Object[] argArray;
/*     */   
/*     */   private String formattedMessage;
/*     */   
/*     */   private transient Throwable throwable;
/*     */ 
/*     */   
/*     */   public LocalizedMessage(String messagePattern, Object[] arguments) {
/*  63 */     this((ResourceBundle)null, (Locale)null, messagePattern, arguments);
/*     */   }
/*     */   
/*     */   public LocalizedMessage(String baseName, String key, Object[] arguments) {
/*  67 */     this(baseName, (Locale)null, key, arguments);
/*     */   }
/*     */   
/*     */   public LocalizedMessage(ResourceBundle bundle, String key, Object[] arguments) {
/*  71 */     this(bundle, (Locale)null, key, arguments);
/*     */   }
/*     */   
/*     */   public LocalizedMessage(String baseName, Locale locale, String key, Object[] arguments) {
/*  75 */     this.key = key;
/*  76 */     this.argArray = arguments;
/*  77 */     this.throwable = null;
/*  78 */     this.baseName = baseName;
/*  79 */     this.resourceBundle = null;
/*  80 */     this.locale = locale;
/*     */   }
/*     */ 
/*     */   
/*     */   public LocalizedMessage(ResourceBundle bundle, Locale locale, String key, Object[] arguments) {
/*  85 */     this.key = key;
/*  86 */     this.argArray = arguments;
/*  87 */     this.throwable = null;
/*  88 */     this.baseName = null;
/*  89 */     this.resourceBundle = bundle;
/*  90 */     this.locale = locale;
/*     */   }
/*     */   
/*     */   public LocalizedMessage(Locale locale, String key, Object[] arguments) {
/*  94 */     this((ResourceBundle)null, locale, key, arguments);
/*     */   }
/*     */   
/*     */   public LocalizedMessage(String messagePattern, Object arg) {
/*  98 */     this((ResourceBundle)null, (Locale)null, messagePattern, new Object[] { arg });
/*     */   }
/*     */   
/*     */   public LocalizedMessage(String baseName, String key, Object arg) {
/* 102 */     this(baseName, (Locale)null, key, new Object[] { arg });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LocalizedMessage(ResourceBundle bundle, String key) {
/* 109 */     this(bundle, (Locale)null, key, new Object[0]);
/*     */   }
/*     */   
/*     */   public LocalizedMessage(ResourceBundle bundle, String key, Object arg) {
/* 113 */     this(bundle, (Locale)null, key, new Object[] { arg });
/*     */   }
/*     */   
/*     */   public LocalizedMessage(String baseName, Locale locale, String key, Object arg) {
/* 117 */     this(baseName, locale, key, new Object[] { arg });
/*     */   }
/*     */   
/*     */   public LocalizedMessage(ResourceBundle bundle, Locale locale, String key, Object arg) {
/* 121 */     this(bundle, locale, key, new Object[] { arg });
/*     */   }
/*     */   
/*     */   public LocalizedMessage(Locale locale, String key, Object arg) {
/* 125 */     this((ResourceBundle)null, locale, key, new Object[] { arg });
/*     */   }
/*     */   
/*     */   public LocalizedMessage(String messagePattern, Object arg1, Object arg2) {
/* 129 */     this((ResourceBundle)null, (Locale)null, messagePattern, new Object[] { arg1, arg2 });
/*     */   }
/*     */   
/*     */   public LocalizedMessage(String baseName, String key, Object arg1, Object arg2) {
/* 133 */     this(baseName, (Locale)null, key, new Object[] { arg1, arg2 });
/*     */   }
/*     */   
/*     */   public LocalizedMessage(ResourceBundle bundle, String key, Object arg1, Object arg2) {
/* 137 */     this(bundle, (Locale)null, key, new Object[] { arg1, arg2 });
/*     */   }
/*     */ 
/*     */   
/*     */   public LocalizedMessage(String baseName, Locale locale, String key, Object arg1, Object arg2) {
/* 142 */     this(baseName, locale, key, new Object[] { arg1, arg2 });
/*     */   }
/*     */ 
/*     */   
/*     */   public LocalizedMessage(ResourceBundle bundle, Locale locale, String key, Object arg1, Object arg2) {
/* 147 */     this(bundle, locale, key, new Object[] { arg1, arg2 });
/*     */   }
/*     */   
/*     */   public LocalizedMessage(Locale locale, String key, Object arg1, Object arg2) {
/* 151 */     this((ResourceBundle)null, locale, key, new Object[] { arg1, arg2 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLoggerName(String name) {
/* 161 */     this.loggerName = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLoggerName() {
/* 171 */     return this.loggerName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedMessage() {
/* 181 */     if (this.formattedMessage != null) {
/* 182 */       return this.formattedMessage;
/*     */     }
/* 184 */     ResourceBundle bundle = this.resourceBundle;
/* 185 */     if (bundle == null) {
/* 186 */       if (this.baseName != null) {
/* 187 */         bundle = getResourceBundle(this.baseName, this.locale, false);
/*     */       } else {
/* 189 */         bundle = getResourceBundle(this.loggerName, this.locale, true);
/*     */       } 
/*     */     }
/* 192 */     String myKey = getFormat();
/* 193 */     String msgPattern = (bundle == null || !bundle.containsKey(myKey)) ? myKey : bundle.getString(myKey);
/* 194 */     Object[] array = (this.argArray == null) ? (Object[])this.stringArgs : this.argArray;
/* 195 */     FormattedMessage msg = new FormattedMessage(msgPattern, array);
/* 196 */     this.formattedMessage = msg.getFormattedMessage();
/* 197 */     this.throwable = msg.getThrowable();
/* 198 */     return this.formattedMessage;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFormat() {
/* 203 */     return this.key;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] getParameters() {
/* 208 */     if (this.argArray != null) {
/* 209 */       return this.argArray;
/*     */     }
/* 211 */     return (Object[])this.stringArgs;
/*     */   }
/*     */ 
/*     */   
/*     */   public Throwable getThrowable() {
/* 216 */     return this.throwable;
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
/*     */   protected ResourceBundle getResourceBundle(String rbBaseName, Locale resourceBundleLocale, boolean loop) {
/* 230 */     ResourceBundle rb = null;
/*     */     
/* 232 */     if (rbBaseName == null) {
/* 233 */       return null;
/*     */     }
/*     */     try {
/* 236 */       if (resourceBundleLocale != null) {
/* 237 */         rb = ResourceBundle.getBundle(rbBaseName, resourceBundleLocale);
/*     */       } else {
/* 239 */         rb = ResourceBundle.getBundle(rbBaseName);
/*     */       } 
/* 241 */     } catch (MissingResourceException ex) {
/* 242 */       if (!loop) {
/* 243 */         this.logger.debug("Unable to locate ResourceBundle " + rbBaseName);
/* 244 */         return null;
/*     */       } 
/*     */     } 
/*     */     
/* 248 */     String substr = rbBaseName;
/*     */     int i;
/* 250 */     while (rb == null && (i = substr.lastIndexOf('.')) > 0) {
/* 251 */       substr = substr.substring(0, i);
/*     */       try {
/* 253 */         if (resourceBundleLocale != null) {
/* 254 */           rb = ResourceBundle.getBundle(substr, resourceBundleLocale); continue;
/*     */         } 
/* 256 */         rb = ResourceBundle.getBundle(substr);
/*     */       }
/* 258 */       catch (MissingResourceException ex) {
/* 259 */         this.logger.debug("Unable to locate ResourceBundle " + substr);
/*     */       } 
/*     */     } 
/* 262 */     return rb;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 267 */     return getFormattedMessage();
/*     */   }
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 271 */     out.defaultWriteObject();
/* 272 */     getFormattedMessage();
/* 273 */     out.writeUTF(this.formattedMessage);
/* 274 */     out.writeUTF(this.key);
/* 275 */     out.writeUTF(this.baseName);
/* 276 */     out.writeInt(this.argArray.length);
/* 277 */     this.stringArgs = new String[this.argArray.length];
/* 278 */     int i = 0;
/* 279 */     for (Object obj : this.argArray) {
/* 280 */       this.stringArgs[i] = obj.toString();
/* 281 */       i++;
/*     */     } 
/* 283 */     out.writeObject(this.stringArgs);
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 287 */     in.defaultReadObject();
/* 288 */     this.formattedMessage = in.readUTF();
/* 289 */     this.key = in.readUTF();
/* 290 */     this.baseName = in.readUTF();
/* 291 */     in.readInt();
/* 292 */     this.stringArgs = (String[])in.readObject();
/* 293 */     this.logger = StatusLogger.getLogger();
/* 294 */     this.resourceBundle = null;
/* 295 */     this.argArray = null;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\message\LocalizedMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */