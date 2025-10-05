/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import org.apache.logging.log4j.util.Constants;
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
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
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public class ReusableParameterizedMessage
/*     */   implements ReusableMessage, ParameterVisitable, Clearable
/*     */ {
/*     */   private static final int MIN_BUILDER_SIZE = 512;
/*     */   private static final int MAX_PARMS = 10;
/*     */   private static final long serialVersionUID = 7800075879295123856L;
/*     */   private transient ThreadLocal<StringBuilder> buffer;
/*     */   private String messagePattern;
/*     */   private int argCount;
/*     */   private int usedCount;
/*  43 */   private final int[] indices = new int[256];
/*     */   private transient Object[] varargs;
/*  45 */   private transient Object[] params = new Object[10];
/*     */ 
/*     */   
/*     */   private transient Throwable throwable;
/*     */ 
/*     */   
/*     */   transient boolean reserved = false;
/*     */ 
/*     */ 
/*     */   
/*     */   private Object[] getTrimmedParams() {
/*  56 */     return (this.varargs == null) ? Arrays.<Object>copyOf(this.params, this.argCount) : this.varargs;
/*     */   }
/*     */   
/*     */   private Object[] getParams() {
/*  60 */     return (this.varargs == null) ? this.params : this.varargs;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] swapParameters(Object[] emptyReplacement) {
/*     */     Object[] result;
/*  67 */     if (this.varargs == null) {
/*  68 */       result = this.params;
/*  69 */       if (emptyReplacement.length >= 10) {
/*  70 */         this.params = emptyReplacement;
/*  71 */       } else if (this.argCount <= emptyReplacement.length) {
/*     */ 
/*     */         
/*  74 */         System.arraycopy(this.params, 0, emptyReplacement, 0, this.argCount);
/*     */         
/*  76 */         for (int i = 0; i < this.argCount; i++) {
/*  77 */           this.params[i] = null;
/*     */         }
/*  79 */         result = emptyReplacement;
/*     */       } else {
/*     */         
/*  82 */         this.params = new Object[10];
/*     */ 
/*     */       
/*     */       }
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/*  92 */       if (this.argCount <= emptyReplacement.length) {
/*  93 */         result = emptyReplacement;
/*     */       } else {
/*  95 */         result = new Object[this.argCount];
/*     */       } 
/*     */       
/*  98 */       System.arraycopy(this.varargs, 0, result, 0, this.argCount);
/*     */     } 
/* 100 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public short getParameterCount() {
/* 106 */     return (short)this.argCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public <S> void forEachParameter(ParameterConsumer<S> action, S state) {
/* 111 */     Object[] parameters = getParams(); short i;
/* 112 */     for (i = 0; i < this.argCount; i = (short)(i + 1)) {
/* 113 */       action.accept(parameters[i], i, state);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Message memento() {
/* 119 */     return new ParameterizedMessage(this.messagePattern, getTrimmedParams());
/*     */   }
/*     */   
/*     */   private void init(String messagePattern, int argCount, Object[] paramArray) {
/* 123 */     this.varargs = null;
/* 124 */     this.messagePattern = messagePattern;
/* 125 */     this.argCount = argCount;
/* 126 */     int placeholderCount = count(messagePattern, this.indices);
/* 127 */     initThrowable(paramArray, argCount, placeholderCount);
/* 128 */     this.usedCount = Math.min(placeholderCount, argCount);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int count(String messagePattern, int[] indices) {
/*     */     try {
/* 134 */       return ParameterFormatter.countArgumentPlaceholders2(messagePattern, indices);
/* 135 */     } catch (Exception ex) {
/* 136 */       return ParameterFormatter.countArgumentPlaceholders(messagePattern);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void initThrowable(Object[] params, int argCount, int usedParams) {
/* 141 */     if (usedParams < argCount && params[argCount - 1] instanceof Throwable) {
/* 142 */       this.throwable = (Throwable)params[argCount - 1];
/*     */     } else {
/* 144 */       this.throwable = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   ReusableParameterizedMessage set(String messagePattern, Object... arguments) {
/* 149 */     init(messagePattern, (arguments == null) ? 0 : arguments.length, arguments);
/* 150 */     this.varargs = arguments;
/* 151 */     return this;
/*     */   }
/*     */   
/*     */   ReusableParameterizedMessage set(String messagePattern, Object p0) {
/* 155 */     this.params[0] = p0;
/* 156 */     init(messagePattern, 1, this.params);
/* 157 */     return this;
/*     */   }
/*     */   
/*     */   ReusableParameterizedMessage set(String messagePattern, Object p0, Object p1) {
/* 161 */     this.params[0] = p0;
/* 162 */     this.params[1] = p1;
/* 163 */     init(messagePattern, 2, this.params);
/* 164 */     return this;
/*     */   }
/*     */   
/*     */   ReusableParameterizedMessage set(String messagePattern, Object p0, Object p1, Object p2) {
/* 168 */     this.params[0] = p0;
/* 169 */     this.params[1] = p1;
/* 170 */     this.params[2] = p2;
/* 171 */     init(messagePattern, 3, this.params);
/* 172 */     return this;
/*     */   }
/*     */   
/*     */   ReusableParameterizedMessage set(String messagePattern, Object p0, Object p1, Object p2, Object p3) {
/* 176 */     this.params[0] = p0;
/* 177 */     this.params[1] = p1;
/* 178 */     this.params[2] = p2;
/* 179 */     this.params[3] = p3;
/* 180 */     init(messagePattern, 4, this.params);
/* 181 */     return this;
/*     */   }
/*     */   
/*     */   ReusableParameterizedMessage set(String messagePattern, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 185 */     this.params[0] = p0;
/* 186 */     this.params[1] = p1;
/* 187 */     this.params[2] = p2;
/* 188 */     this.params[3] = p3;
/* 189 */     this.params[4] = p4;
/* 190 */     init(messagePattern, 5, this.params);
/* 191 */     return this;
/*     */   }
/*     */   
/*     */   ReusableParameterizedMessage set(String messagePattern, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 195 */     this.params[0] = p0;
/* 196 */     this.params[1] = p1;
/* 197 */     this.params[2] = p2;
/* 198 */     this.params[3] = p3;
/* 199 */     this.params[4] = p4;
/* 200 */     this.params[5] = p5;
/* 201 */     init(messagePattern, 6, this.params);
/* 202 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   ReusableParameterizedMessage set(String messagePattern, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 207 */     this.params[0] = p0;
/* 208 */     this.params[1] = p1;
/* 209 */     this.params[2] = p2;
/* 210 */     this.params[3] = p3;
/* 211 */     this.params[4] = p4;
/* 212 */     this.params[5] = p5;
/* 213 */     this.params[6] = p6;
/* 214 */     init(messagePattern, 7, this.params);
/* 215 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   ReusableParameterizedMessage set(String messagePattern, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 220 */     this.params[0] = p0;
/* 221 */     this.params[1] = p1;
/* 222 */     this.params[2] = p2;
/* 223 */     this.params[3] = p3;
/* 224 */     this.params[4] = p4;
/* 225 */     this.params[5] = p5;
/* 226 */     this.params[6] = p6;
/* 227 */     this.params[7] = p7;
/* 228 */     init(messagePattern, 8, this.params);
/* 229 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   ReusableParameterizedMessage set(String messagePattern, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 234 */     this.params[0] = p0;
/* 235 */     this.params[1] = p1;
/* 236 */     this.params[2] = p2;
/* 237 */     this.params[3] = p3;
/* 238 */     this.params[4] = p4;
/* 239 */     this.params[5] = p5;
/* 240 */     this.params[6] = p6;
/* 241 */     this.params[7] = p7;
/* 242 */     this.params[8] = p8;
/* 243 */     init(messagePattern, 9, this.params);
/* 244 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   ReusableParameterizedMessage set(String messagePattern, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 249 */     this.params[0] = p0;
/* 250 */     this.params[1] = p1;
/* 251 */     this.params[2] = p2;
/* 252 */     this.params[3] = p3;
/* 253 */     this.params[4] = p4;
/* 254 */     this.params[5] = p5;
/* 255 */     this.params[6] = p6;
/* 256 */     this.params[7] = p7;
/* 257 */     this.params[8] = p8;
/* 258 */     this.params[9] = p9;
/* 259 */     init(messagePattern, 10, this.params);
/* 260 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormat() {
/* 269 */     return this.messagePattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getParameters() {
/* 278 */     return getTrimmedParams();
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
/* 292 */     return this.throwable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedMessage() {
/* 301 */     StringBuilder sb = getBuffer();
/* 302 */     formatTo(sb);
/* 303 */     String result = sb.toString();
/* 304 */     StringBuilders.trimToMaxSize(sb, Constants.MAX_REUSABLE_MESSAGE_SIZE);
/* 305 */     return result;
/*     */   }
/*     */   
/*     */   private StringBuilder getBuffer() {
/* 309 */     if (this.buffer == null) {
/* 310 */       this.buffer = new ThreadLocal<>();
/*     */     }
/* 312 */     StringBuilder result = this.buffer.get();
/* 313 */     if (result == null) {
/* 314 */       int currentPatternLength = (this.messagePattern == null) ? 0 : this.messagePattern.length();
/* 315 */       result = new StringBuilder(Math.max(512, currentPatternLength * 2));
/* 316 */       this.buffer.set(result);
/*     */     } 
/* 318 */     result.setLength(0);
/* 319 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void formatTo(StringBuilder builder) {
/* 324 */     if (this.indices[0] < 0) {
/* 325 */       ParameterFormatter.formatMessage(builder, this.messagePattern, getParams(), this.argCount);
/*     */     } else {
/* 327 */       ParameterFormatter.formatMessage2(builder, this.messagePattern, getParams(), this.usedCount, this.indices);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ReusableParameterizedMessage reserve() {
/* 337 */     this.reserved = true;
/* 338 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 343 */     return "ReusableParameterizedMessage[messagePattern=" + getFormat() + ", stringArgs=" + 
/* 344 */       Arrays.toString(getParameters()) + ", throwable=" + getThrowable() + ']';
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 351 */     this.reserved = false;
/* 352 */     this.varargs = null;
/* 353 */     this.messagePattern = null;
/* 354 */     this.throwable = null;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\message\ReusableParameterizedMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */