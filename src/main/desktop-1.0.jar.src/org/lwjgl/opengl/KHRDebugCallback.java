/*     */ package org.lwjgl.opengl;
/*     */ 
/*     */ import org.lwjgl.PointerWrapperAbstract;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class KHRDebugCallback
/*     */   extends PointerWrapperAbstract
/*     */ {
/*     */   private static final int GL_DEBUG_SEVERITY_HIGH = 37190;
/*     */   private static final int GL_DEBUG_SEVERITY_MEDIUM = 37191;
/*     */   private static final int GL_DEBUG_SEVERITY_LOW = 37192;
/*     */   private static final int GL_DEBUG_SEVERITY_NOTIFICATION = 33387;
/*     */   private static final int GL_DEBUG_SOURCE_API = 33350;
/*     */   private static final int GL_DEBUG_SOURCE_WINDOW_SYSTEM = 33351;
/*     */   private static final int GL_DEBUG_SOURCE_SHADER_COMPILER = 33352;
/*     */   private static final int GL_DEBUG_SOURCE_THIRD_PARTY = 33353;
/*     */   private static final int GL_DEBUG_SOURCE_APPLICATION = 33354;
/*     */   private static final int GL_DEBUG_SOURCE_OTHER = 33355;
/*     */   private static final int GL_DEBUG_TYPE_ERROR = 33356;
/*     */   private static final int GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR = 33357;
/*     */   private static final int GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR = 33358;
/*     */   private static final int GL_DEBUG_TYPE_PORTABILITY = 33359;
/*     */   private static final int GL_DEBUG_TYPE_PERFORMANCE = 33360;
/*     */   private static final int GL_DEBUG_TYPE_OTHER = 33361;
/*     */   private static final int GL_DEBUG_TYPE_MARKER = 33384;
/*     */   private static final long CALLBACK_POINTER;
/*     */   private final Handler handler;
/*     */   
/*     */   static {
/*  75 */     long pointer = 0L;
/*     */     
/*     */     try {
/*  78 */       pointer = ((Long)Class.forName("org.lwjgl.opengl.CallbackUtil").getDeclaredMethod("getDebugCallbackKHR", new Class[0]).invoke(null, new Object[0])).longValue();
/*  79 */     } catch (Exception e) {}
/*     */ 
/*     */     
/*  82 */     CALLBACK_POINTER = pointer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public KHRDebugCallback() {
/*  92 */     this(new Handler() { public void handleMessage(int source, int type, int id, int severity, String message) {
/*     */             String description;
/*  94 */             System.err.println("[LWJGL] KHR_debug message");
/*  95 */             System.err.println("\tID: " + id);
/*     */ 
/*     */             
/*  98 */             switch (source) {
/*     */               case 33350:
/* 100 */                 description = "API";
/*     */                 break;
/*     */               case 33351:
/* 103 */                 description = "WINDOW SYSTEM";
/*     */                 break;
/*     */               case 33352:
/* 106 */                 description = "SHADER COMPILER";
/*     */                 break;
/*     */               case 33353:
/* 109 */                 description = "THIRD PARTY";
/*     */                 break;
/*     */               case 33354:
/* 112 */                 description = "APPLICATION";
/*     */                 break;
/*     */               case 33355:
/* 115 */                 description = "OTHER";
/*     */                 break;
/*     */               default:
/* 118 */                 description = printUnknownToken(source); break;
/*     */             } 
/* 120 */             System.err.println("\tSource: " + description);
/*     */             
/* 122 */             switch (type) {
/*     */               case 33356:
/* 124 */                 description = "ERROR";
/*     */                 break;
/*     */               case 33357:
/* 127 */                 description = "DEPRECATED BEHAVIOR";
/*     */                 break;
/*     */               case 33358:
/* 130 */                 description = "UNDEFINED BEHAVIOR";
/*     */                 break;
/*     */               case 33359:
/* 133 */                 description = "PORTABILITY";
/*     */                 break;
/*     */               case 33360:
/* 136 */                 description = "PERFORMANCE";
/*     */                 break;
/*     */               case 33361:
/* 139 */                 description = "OTHER";
/*     */                 break;
/*     */               case 33384:
/* 142 */                 description = "MARKER";
/*     */                 break;
/*     */               default:
/* 145 */                 description = printUnknownToken(type); break;
/*     */             } 
/* 147 */             System.err.println("\tType: " + description);
/*     */             
/* 149 */             switch (severity) {
/*     */               case 37190:
/* 151 */                 description = "HIGH";
/*     */                 break;
/*     */               case 37191:
/* 154 */                 description = "MEDIUM";
/*     */                 break;
/*     */               case 37192:
/* 157 */                 description = "LOW";
/*     */                 break;
/*     */               case 33387:
/* 160 */                 description = "NOTIFICATION";
/*     */                 break;
/*     */               default:
/* 163 */                 description = printUnknownToken(severity); break;
/*     */             } 
/* 165 */             System.err.println("\tSeverity: " + description);
/*     */             
/* 167 */             System.err.println("\tMessage: " + message);
/*     */           }
/*     */           
/*     */           private String printUnknownToken(int token) {
/* 171 */             return "Unknown (0x" + Integer.toHexString(token).toUpperCase() + ")";
/*     */           } }
/*     */       );
/*     */   }
/*     */ 
/*     */   
/*     */   public static interface Handler
/*     */   {
/*     */     void handleMessage(int param1Int1, int param1Int2, int param1Int3, int param1Int4, String param1String);
/*     */   }
/*     */ 
/*     */   
/*     */   public KHRDebugCallback(Handler handler) {
/* 184 */     super(CALLBACK_POINTER);
/*     */     
/* 186 */     this.handler = handler;
/*     */   }
/*     */   
/*     */   Handler getHandler() {
/* 190 */     return this.handler;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\KHRDebugCallback.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */