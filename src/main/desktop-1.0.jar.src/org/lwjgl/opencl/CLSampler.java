/*    */ package org.lwjgl.opencl;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class CLSampler
/*    */   extends CLObjectChild<CLContext>
/*    */ {
/* 41 */   private static final InfoUtil<CLSampler> util = CLPlatform.getInfoUtilInstance(CLSampler.class, "CL_SAMPLER_UTIL");
/*    */   
/*    */   CLSampler(long pointer, CLContext context) {
/* 44 */     super(pointer, context);
/* 45 */     if (isValid()) {
/* 46 */       context.getCLSamplerRegistry().registerObject(this);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getInfoInt(int param_name) {
/* 59 */     return util.getInfoInt(this, param_name);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long getInfoLong(int param_name) {
/* 71 */     return util.getInfoLong(this, param_name);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   int release() {
/*    */     try {
/* 78 */       return super.release();
/*    */     } finally {
/* 80 */       if (!isValid())
/* 81 */         getParent().getCLSamplerRegistry().unregisterObject(this); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opencl\CLSampler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */