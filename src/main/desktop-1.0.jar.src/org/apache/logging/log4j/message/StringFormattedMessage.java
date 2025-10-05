/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.IllegalFormatException;
/*     */ import java.util.Locale;
/*     */ import org.apache.logging.log4j.Logger;
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
/*     */ public class StringFormattedMessage
/*     */   implements Message
/*     */ {
/*  40 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */   
/*     */   private static final long serialVersionUID = -665975803997290697L;
/*     */ 
/*     */   
/*     */   private static final int HASHVAL = 31;
/*     */   
/*     */   private String messagePattern;
/*     */   
/*     */   private transient Object[] argArray;
/*     */   
/*     */   private String[] stringArgs;
/*     */   
/*     */   private transient String formattedMessage;
/*     */   
/*     */   private transient Throwable throwable;
/*     */   
/*     */   private final Locale locale;
/*     */ 
/*     */   
/*     */   public StringFormattedMessage(Locale locale, String messagePattern, Object... arguments) {
/*  62 */     this.locale = locale;
/*  63 */     this.messagePattern = messagePattern;
/*  64 */     this.argArray = arguments;
/*  65 */     if (arguments != null && arguments.length > 0 && arguments[arguments.length - 1] instanceof Throwable) {
/*  66 */       this.throwable = (Throwable)arguments[arguments.length - 1];
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
/*     */   public StringFormattedMessage(String messagePattern, Object... arguments) {
/*  78 */     this(Locale.getDefault(Locale.Category.FORMAT), messagePattern, arguments);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedMessage() {
/*  87 */     if (this.formattedMessage == null) {
/*  88 */       this.formattedMessage = formatMessage(this.messagePattern, this.argArray);
/*     */     }
/*  90 */     return this.formattedMessage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormat() {
/*  99 */     return this.messagePattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getParameters() {
/* 108 */     if (this.argArray != null) {
/* 109 */       return this.argArray;
/*     */     }
/* 111 */     return (Object[])this.stringArgs;
/*     */   }
/*     */   
/*     */   protected String formatMessage(String msgPattern, Object... args) {
/*     */     try {
/* 116 */       return String.format(this.locale, msgPattern, args);
/* 117 */     } catch (IllegalFormatException ife) {
/* 118 */       LOGGER.error("Unable to format msg: " + msgPattern, ife);
/* 119 */       return msgPattern;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 125 */     if (this == o) {
/* 126 */       return true;
/*     */     }
/* 128 */     if (o == null || getClass() != o.getClass()) {
/* 129 */       return false;
/*     */     }
/*     */     
/* 132 */     StringFormattedMessage that = (StringFormattedMessage)o;
/*     */     
/* 134 */     if ((this.messagePattern != null) ? !this.messagePattern.equals(that.messagePattern) : (that.messagePattern != null)) {
/* 135 */       return false;
/*     */     }
/*     */     
/* 138 */     return Arrays.equals((Object[])this.stringArgs, (Object[])that.stringArgs);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 143 */     int result = (this.messagePattern != null) ? this.messagePattern.hashCode() : 0;
/* 144 */     result = 31 * result + ((this.stringArgs != null) ? Arrays.hashCode((Object[])this.stringArgs) : 0);
/* 145 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 151 */     return getFormattedMessage();
/*     */   }
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 155 */     out.defaultWriteObject();
/* 156 */     getFormattedMessage();
/* 157 */     out.writeUTF(this.formattedMessage);
/* 158 */     out.writeUTF(this.messagePattern);
/* 159 */     out.writeInt(this.argArray.length);
/* 160 */     this.stringArgs = new String[this.argArray.length];
/* 161 */     int i = 0;
/* 162 */     for (Object obj : this.argArray) {
/* 163 */       String string = String.valueOf(obj);
/* 164 */       this.stringArgs[i] = string;
/* 165 */       out.writeUTF(string);
/* 166 */       i++;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 171 */     in.defaultReadObject();
/* 172 */     this.formattedMessage = in.readUTF();
/* 173 */     this.messagePattern = in.readUTF();
/* 174 */     int length = in.readInt();
/* 175 */     this.stringArgs = new String[length];
/* 176 */     for (int i = 0; i < length; i++) {
/* 177 */       this.stringArgs[i] = in.readUTF();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getThrowable() {
/* 188 */     return this.throwable;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\message\StringFormattedMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */