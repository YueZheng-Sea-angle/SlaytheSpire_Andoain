/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.text.Format;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Arrays;
/*     */ import java.util.Locale;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FormattedMessage
/*     */   implements Message
/*     */ {
/*     */   private static final long serialVersionUID = -665975803997290697L;
/*     */   private static final int HASHVAL = 31;
/*     */   private static final String FORMAT_SPECIFIER = "%(\\d+\\$)?([-#+ 0,(\\<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z%])";
/*  37 */   private static final Pattern MSG_PATTERN = Pattern.compile("%(\\d+\\$)?([-#+ 0,(\\<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z%])");
/*     */   
/*     */   private String messagePattern;
/*     */   
/*     */   private transient Object[] argArray;
/*     */   
/*     */   private String[] stringArgs;
/*     */   
/*     */   private transient String formattedMessage;
/*     */   
/*     */   private final Throwable throwable;
/*     */   
/*     */   private Message message;
/*     */   
/*     */   private final Locale locale;
/*     */ 
/*     */   
/*     */   public FormattedMessage(Locale locale, String messagePattern, Object arg) {
/*  55 */     this(locale, messagePattern, new Object[] { arg }, (Throwable)null);
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
/*     */   public FormattedMessage(Locale locale, String messagePattern, Object arg1, Object arg2) {
/*  67 */     this(locale, messagePattern, new Object[] { arg1, arg2 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FormattedMessage(Locale locale, String messagePattern, Object... arguments) {
/*  78 */     this(locale, messagePattern, arguments, (Throwable)null);
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
/*     */   public FormattedMessage(Locale locale, String messagePattern, Object[] arguments, Throwable throwable) {
/*  90 */     this.locale = locale;
/*  91 */     this.messagePattern = messagePattern;
/*  92 */     this.argArray = arguments;
/*  93 */     this.throwable = throwable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FormattedMessage(String messagePattern, Object arg) {
/* 102 */     this(messagePattern, new Object[] { arg }, (Throwable)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FormattedMessage(String messagePattern, Object arg1, Object arg2) {
/* 112 */     this(messagePattern, new Object[] { arg1, arg2 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FormattedMessage(String messagePattern, Object... arguments) {
/* 121 */     this(messagePattern, arguments, (Throwable)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FormattedMessage(String messagePattern, Object[] arguments, Throwable throwable) {
/* 131 */     this.locale = Locale.getDefault(Locale.Category.FORMAT);
/* 132 */     this.messagePattern = messagePattern;
/* 133 */     this.argArray = arguments;
/* 134 */     this.throwable = throwable;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 140 */     if (this == o) {
/* 141 */       return true;
/*     */     }
/* 143 */     if (o == null || getClass() != o.getClass()) {
/* 144 */       return false;
/*     */     }
/*     */     
/* 147 */     FormattedMessage that = (FormattedMessage)o;
/*     */     
/* 149 */     if ((this.messagePattern != null) ? !this.messagePattern.equals(that.messagePattern) : (that.messagePattern != null)) {
/* 150 */       return false;
/*     */     }
/* 152 */     if (!Arrays.equals((Object[])this.stringArgs, (Object[])that.stringArgs)) {
/* 153 */       return false;
/*     */     }
/*     */     
/* 156 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormat() {
/* 165 */     return this.messagePattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedMessage() {
/* 174 */     if (this.formattedMessage == null) {
/* 175 */       if (this.message == null) {
/* 176 */         this.message = getMessage(this.messagePattern, this.argArray, this.throwable);
/*     */       }
/* 178 */       this.formattedMessage = this.message.getFormattedMessage();
/*     */     } 
/* 180 */     return this.formattedMessage;
/*     */   }
/*     */   
/*     */   protected Message getMessage(String msgPattern, Object[] args, Throwable aThrowable) {
/*     */     try {
/* 185 */       MessageFormat format = new MessageFormat(msgPattern);
/* 186 */       Format[] formats = format.getFormats();
/* 187 */       if (formats != null && formats.length > 0) {
/* 188 */         return new MessageFormatMessage(this.locale, msgPattern, args);
/*     */       }
/* 190 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/*     */     try {
/* 194 */       if (MSG_PATTERN.matcher(msgPattern).find()) {
/* 195 */         return new StringFormattedMessage(this.locale, msgPattern, args);
/*     */       }
/* 197 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/* 200 */     return new ParameterizedMessage(msgPattern, args, aThrowable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getParameters() {
/* 209 */     if (this.argArray != null) {
/* 210 */       return this.argArray;
/*     */     }
/* 212 */     return (Object[])this.stringArgs;
/*     */   }
/*     */ 
/*     */   
/*     */   public Throwable getThrowable() {
/* 217 */     if (this.throwable != null) {
/* 218 */       return this.throwable;
/*     */     }
/* 220 */     if (this.message == null) {
/* 221 */       this.message = getMessage(this.messagePattern, this.argArray, null);
/*     */     }
/* 223 */     return this.message.getThrowable();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 229 */     int result = (this.messagePattern != null) ? this.messagePattern.hashCode() : 0;
/* 230 */     result = 31 * result + ((this.stringArgs != null) ? Arrays.hashCode((Object[])this.stringArgs) : 0);
/* 231 */     return result;
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 235 */     in.defaultReadObject();
/* 236 */     this.formattedMessage = in.readUTF();
/* 237 */     this.messagePattern = in.readUTF();
/* 238 */     int length = in.readInt();
/* 239 */     this.stringArgs = new String[length];
/* 240 */     for (int i = 0; i < length; i++) {
/* 241 */       this.stringArgs[i] = in.readUTF();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 247 */     return getFormattedMessage();
/*     */   }
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 251 */     out.defaultWriteObject();
/* 252 */     getFormattedMessage();
/* 253 */     out.writeUTF(this.formattedMessage);
/* 254 */     out.writeUTF(this.messagePattern);
/* 255 */     out.writeInt(this.argArray.length);
/* 256 */     this.stringArgs = new String[this.argArray.length];
/* 257 */     int i = 0;
/* 258 */     for (Object obj : this.argArray) {
/* 259 */       String string = String.valueOf(obj);
/* 260 */       this.stringArgs[i] = string;
/* 261 */       out.writeUTF(string);
/* 262 */       i++;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\message\FormattedMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */