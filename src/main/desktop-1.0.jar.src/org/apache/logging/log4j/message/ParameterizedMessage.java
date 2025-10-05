/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import org.apache.logging.log4j.util.Constants;
/*     */ import org.apache.logging.log4j.util.StringBuilderFormattable;
/*     */ import org.apache.logging.log4j.util.StringBuilders;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ParameterizedMessage
/*     */   implements Message, StringBuilderFormattable
/*     */ {
/*     */   private static final int DEFAULT_STRING_BUILDER_SIZE = 255;
/*     */   public static final String RECURSION_PREFIX = "[...";
/*     */   public static final String RECURSION_SUFFIX = "...]";
/*     */   public static final String ERROR_PREFIX = "[!!!";
/*     */   public static final String ERROR_SEPARATOR = "=>";
/*     */   public static final String ERROR_MSG_SEPARATOR = ":";
/*     */   public static final String ERROR_SUFFIX = "!!!]";
/*     */   private static final long serialVersionUID = -665975803997290697L;
/*     */   private static final int HASHVAL = 31;
/*  72 */   private static ThreadLocal<StringBuilder> threadLocalStringBuilder = new ThreadLocal<>();
/*     */ 
/*     */   
/*     */   private String messagePattern;
/*     */ 
/*     */   
/*     */   private transient Object[] argArray;
/*     */ 
/*     */   
/*     */   private String formattedMessage;
/*     */   
/*     */   private transient Throwable throwable;
/*     */   
/*     */   private int[] indices;
/*     */   
/*     */   private int usedCount;
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ParameterizedMessage(String messagePattern, String[] arguments, Throwable throwable) {
/*  92 */     this.argArray = (Object[])arguments;
/*  93 */     this.throwable = throwable;
/*  94 */     init(messagePattern);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParameterizedMessage(String messagePattern, Object[] arguments, Throwable throwable) {
/* 105 */     this.argArray = arguments;
/* 106 */     this.throwable = throwable;
/* 107 */     init(messagePattern);
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
/*     */   public ParameterizedMessage(String messagePattern, Object... arguments) {
/* 122 */     this.argArray = arguments;
/* 123 */     init(messagePattern);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParameterizedMessage(String messagePattern, Object arg) {
/* 132 */     this(messagePattern, new Object[] { arg });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParameterizedMessage(String messagePattern, Object arg0, Object arg1) {
/* 142 */     this(messagePattern, new Object[] { arg0, arg1 });
/*     */   }
/*     */   
/*     */   private void init(String messagePattern) {
/* 146 */     this.messagePattern = messagePattern;
/* 147 */     int len = Math.max(1, (messagePattern == null) ? 0 : (messagePattern.length() >> 1));
/* 148 */     this.indices = new int[len];
/* 149 */     int placeholders = ParameterFormatter.countArgumentPlaceholders2(messagePattern, this.indices);
/* 150 */     initThrowable(this.argArray, placeholders);
/* 151 */     this.usedCount = Math.min(placeholders, (this.argArray == null) ? 0 : this.argArray.length);
/*     */   }
/*     */   
/*     */   private void initThrowable(Object[] params, int usedParams) {
/* 155 */     if (params != null) {
/* 156 */       int argCount = params.length;
/* 157 */       if (usedParams < argCount && this.throwable == null && params[argCount - 1] instanceof Throwable) {
/* 158 */         this.throwable = (Throwable)params[argCount - 1];
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormat() {
/* 169 */     return this.messagePattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getParameters() {
/* 178 */     return this.argArray;
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
/*     */   public Throwable getThrowable() {
/* 192 */     return this.throwable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedMessage() {
/* 201 */     if (this.formattedMessage == null) {
/* 202 */       StringBuilder buffer = getThreadLocalStringBuilder();
/* 203 */       formatTo(buffer);
/* 204 */       this.formattedMessage = buffer.toString();
/* 205 */       StringBuilders.trimToMaxSize(buffer, Constants.MAX_REUSABLE_MESSAGE_SIZE);
/*     */     } 
/* 207 */     return this.formattedMessage;
/*     */   }
/*     */   
/*     */   private static StringBuilder getThreadLocalStringBuilder() {
/* 211 */     StringBuilder buffer = threadLocalStringBuilder.get();
/* 212 */     if (buffer == null) {
/* 213 */       buffer = new StringBuilder(255);
/* 214 */       threadLocalStringBuilder.set(buffer);
/*     */     } 
/* 216 */     buffer.setLength(0);
/* 217 */     return buffer;
/*     */   }
/*     */ 
/*     */   
/*     */   public void formatTo(StringBuilder buffer) {
/* 222 */     if (this.formattedMessage != null) {
/* 223 */       buffer.append(this.formattedMessage);
/* 224 */     } else if (this.indices[0] < 0) {
/* 225 */       ParameterFormatter.formatMessage(buffer, this.messagePattern, this.argArray, this.usedCount);
/*     */     } else {
/* 227 */       ParameterFormatter.formatMessage2(buffer, this.messagePattern, this.argArray, this.usedCount, this.indices);
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
/*     */   public static String format(String messagePattern, Object[] arguments) {
/* 239 */     return ParameterFormatter.format(messagePattern, arguments);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 244 */     if (this == o) {
/* 245 */       return true;
/*     */     }
/* 247 */     if (o == null || getClass() != o.getClass()) {
/* 248 */       return false;
/*     */     }
/*     */     
/* 251 */     ParameterizedMessage that = (ParameterizedMessage)o;
/*     */     
/* 253 */     if ((this.messagePattern != null) ? !this.messagePattern.equals(that.messagePattern) : (that.messagePattern != null)) {
/* 254 */       return false;
/*     */     }
/* 256 */     if (!Arrays.equals(this.argArray, that.argArray)) {
/* 257 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 261 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 266 */     int result = (this.messagePattern != null) ? this.messagePattern.hashCode() : 0;
/* 267 */     result = 31 * result + ((this.argArray != null) ? Arrays.hashCode(this.argArray) : 0);
/* 268 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int countArgumentPlaceholders(String messagePattern) {
/* 278 */     return ParameterFormatter.countArgumentPlaceholders(messagePattern);
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
/*     */   public static String deepToString(Object o) {
/* 300 */     return ParameterFormatter.deepToString(o);
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
/*     */   public static String identityToString(Object obj) {
/* 324 */     return ParameterFormatter.identityToString(obj);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 329 */     return "ParameterizedMessage[messagePattern=" + this.messagePattern + ", stringArgs=" + 
/* 330 */       Arrays.toString(this.argArray) + ", throwable=" + this.throwable + ']';
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\message\ParameterizedMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */