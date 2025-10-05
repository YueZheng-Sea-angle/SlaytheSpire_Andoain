/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.text.MessageFormat;
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
/*     */ public class MessageFormatMessage
/*     */   implements Message
/*     */ {
/*  39 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */   
/*     */   private static final long serialVersionUID = 1L;
/*     */ 
/*     */   
/*     */   private static final int HASHVAL = 31;
/*     */   
/*     */   private String messagePattern;
/*     */   
/*     */   private transient Object[] parameters;
/*     */   
/*     */   private String[] serializedParameters;
/*     */   
/*     */   private transient String formattedMessage;
/*     */   
/*     */   private transient Throwable throwable;
/*     */   
/*     */   private final Locale locale;
/*     */ 
/*     */   
/*     */   public MessageFormatMessage(Locale locale, String messagePattern, Object... parameters) {
/*  61 */     this.locale = locale;
/*  62 */     this.messagePattern = messagePattern;
/*  63 */     this.parameters = parameters;
/*  64 */     int length = (parameters == null) ? 0 : parameters.length;
/*  65 */     if (length > 0 && parameters[length - 1] instanceof Throwable) {
/*  66 */       this.throwable = (Throwable)parameters[length - 1];
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageFormatMessage(String messagePattern, Object... parameters) {
/*  77 */     this(Locale.getDefault(Locale.Category.FORMAT), messagePattern, parameters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedMessage() {
/*  86 */     if (this.formattedMessage == null) {
/*  87 */       this.formattedMessage = formatMessage(this.messagePattern, this.parameters);
/*     */     }
/*  89 */     return this.formattedMessage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormat() {
/*  98 */     return this.messagePattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getParameters() {
/* 107 */     if (this.parameters != null) {
/* 108 */       return this.parameters;
/*     */     }
/* 110 */     return (Object[])this.serializedParameters;
/*     */   }
/*     */   
/*     */   protected String formatMessage(String msgPattern, Object... args) {
/*     */     try {
/* 115 */       MessageFormat temp = new MessageFormat(msgPattern, this.locale);
/* 116 */       return temp.format(args);
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
/* 132 */     MessageFormatMessage that = (MessageFormatMessage)o;
/*     */     
/* 134 */     if ((this.messagePattern != null) ? !this.messagePattern.equals(that.messagePattern) : (that.messagePattern != null)) {
/* 135 */       return false;
/*     */     }
/* 137 */     return Arrays.equals((Object[])this.serializedParameters, (Object[])that.serializedParameters);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 142 */     int result = (this.messagePattern != null) ? this.messagePattern.hashCode() : 0;
/* 143 */     result = 31 * result + ((this.serializedParameters != null) ? Arrays.hashCode((Object[])this.serializedParameters) : 0);
/* 144 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 150 */     return getFormattedMessage();
/*     */   }
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 154 */     getFormattedMessage();
/* 155 */     out.writeUTF(this.formattedMessage);
/* 156 */     out.writeUTF(this.messagePattern);
/* 157 */     int length = (this.parameters == null) ? 0 : this.parameters.length;
/* 158 */     out.writeInt(length);
/* 159 */     this.serializedParameters = new String[length];
/* 160 */     if (length > 0) {
/* 161 */       for (int i = 0; i < length; i++) {
/* 162 */         this.serializedParameters[i] = String.valueOf(this.parameters[i]);
/* 163 */         out.writeUTF(this.serializedParameters[i]);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException {
/* 169 */     this.parameters = null;
/* 170 */     this.throwable = null;
/* 171 */     this.formattedMessage = in.readUTF();
/* 172 */     this.messagePattern = in.readUTF();
/* 173 */     int length = in.readInt();
/* 174 */     this.serializedParameters = new String[length];
/* 175 */     for (int i = 0; i < length; i++) {
/* 176 */       this.serializedParameters[i] = in.readUTF();
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
/* 187 */     return this.throwable;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\message\MessageFormatMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */