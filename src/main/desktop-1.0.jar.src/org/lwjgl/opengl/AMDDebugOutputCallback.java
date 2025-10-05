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
/*     */ public final class AMDDebugOutputCallback
/*     */   extends PointerWrapperAbstract
/*     */ {
/*     */   private static final int GL_DEBUG_SEVERITY_HIGH_AMD = 37190;
/*     */   private static final int GL_DEBUG_SEVERITY_MEDIUM_AMD = 37191;
/*     */   private static final int GL_DEBUG_SEVERITY_LOW_AMD = 37192;
/*     */   private static final int GL_DEBUG_CATEGORY_API_ERROR_AMD = 37193;
/*     */   private static final int GL_DEBUG_CATEGORY_WINDOW_SYSTEM_AMD = 37194;
/*     */   private static final int GL_DEBUG_CATEGORY_DEPRECATION_AMD = 37195;
/*     */   private static final int GL_DEBUG_CATEGORY_UNDEFINED_BEHAVIOR_AMD = 37196;
/*     */   private static final int GL_DEBUG_CATEGORY_PERFORMANCE_AMD = 37197;
/*     */   private static final int GL_DEBUG_CATEGORY_SHADER_COMPILER_AMD = 37198;
/*     */   private static final int GL_DEBUG_CATEGORY_APPLICATION_AMD = 37199;
/*     */   private static final int GL_DEBUG_CATEGORY_OTHER_AMD = 37200;
/*     */   private static final long CALLBACK_POINTER;
/*     */   private final Handler handler;
/*     */   
/*     */   static {
/*  64 */     long pointer = 0L;
/*     */     
/*     */     try {
/*  67 */       pointer = ((Long)Class.forName("org.lwjgl.opengl.CallbackUtil").getDeclaredMethod("getDebugOutputCallbackAMD", new Class[0]).invoke(null, new Object[0])).longValue();
/*  68 */     } catch (Exception e) {}
/*     */ 
/*     */     
/*  71 */     CALLBACK_POINTER = pointer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AMDDebugOutputCallback() {
/*  81 */     this(new Handler() { public void handleMessage(int id, int category, int severity, String message) {
/*     */             String description;
/*  83 */             System.err.println("[LWJGL] AMD_debug_output message");
/*  84 */             System.err.println("\tID: " + id);
/*     */ 
/*     */             
/*  87 */             switch (category) {
/*     */               case 37193:
/*  89 */                 description = "API ERROR";
/*     */                 break;
/*     */               case 37194:
/*  92 */                 description = "WINDOW SYSTEM";
/*     */                 break;
/*     */               case 37195:
/*  95 */                 description = "DEPRECATION";
/*     */                 break;
/*     */               case 37196:
/*  98 */                 description = "UNDEFINED BEHAVIOR";
/*     */                 break;
/*     */               case 37197:
/* 101 */                 description = "PERFORMANCE";
/*     */                 break;
/*     */               case 37198:
/* 104 */                 description = "SHADER COMPILER";
/*     */                 break;
/*     */               case 37199:
/* 107 */                 description = "APPLICATION";
/*     */                 break;
/*     */               case 37200:
/* 110 */                 description = "OTHER";
/*     */                 break;
/*     */               default:
/* 113 */                 description = printUnknownToken(category); break;
/*     */             } 
/* 115 */             System.err.println("\tCategory: " + description);
/*     */             
/* 117 */             switch (severity) {
/*     */               case 37190:
/* 119 */                 description = "HIGH";
/*     */                 break;
/*     */               case 37191:
/* 122 */                 description = "MEDIUM";
/*     */                 break;
/*     */               case 37192:
/* 125 */                 description = "LOW";
/*     */                 break;
/*     */               default:
/* 128 */                 description = printUnknownToken(severity); break;
/*     */             } 
/* 130 */             System.err.println("\tSeverity: " + description);
/*     */             
/* 132 */             System.err.println("\tMessage: " + message);
/*     */           }
/*     */           
/*     */           private String printUnknownToken(int token) {
/* 136 */             return "Unknown (0x" + Integer.toHexString(token).toUpperCase() + ")";
/*     */           } }
/*     */       );
/*     */   }
/*     */ 
/*     */   
/*     */   public static interface Handler
/*     */   {
/*     */     void handleMessage(int param1Int1, int param1Int2, int param1Int3, String param1String);
/*     */   }
/*     */ 
/*     */   
/*     */   public AMDDebugOutputCallback(Handler handler) {
/* 149 */     super(CALLBACK_POINTER);
/*     */     
/* 151 */     this.handler = handler;
/*     */   }
/*     */   
/*     */   Handler getHandler() {
/* 155 */     return this.handler;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\AMDDebugOutputCallback.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */