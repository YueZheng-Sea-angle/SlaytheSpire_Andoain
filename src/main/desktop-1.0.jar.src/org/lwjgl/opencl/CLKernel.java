/*     */ package org.lwjgl.opencl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CLKernel
/*     */   extends CLObjectChild<CLProgram>
/*     */ {
/*  41 */   private static final CLKernelUtil util = (CLKernelUtil)CLPlatform.<CLKernel>getInfoUtilInstance(CLKernel.class, "CL_KERNEL_UTIL");
/*     */   
/*     */   CLKernel(long pointer, CLProgram program) {
/*  44 */     super(pointer, program);
/*  45 */     if (isValid()) {
/*  46 */       program.getCLKernelRegistry().registerObject(this);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CLKernel setArg(int index, byte value) {
/*  63 */     util.setArg(this, index, value);
/*  64 */     return this;
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
/*     */   public CLKernel setArg(int index, short value) {
/*  77 */     util.setArg(this, index, value);
/*  78 */     return this;
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
/*     */   public CLKernel setArg(int index, int value) {
/*  91 */     util.setArg(this, index, value);
/*  92 */     return this;
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
/*     */   public CLKernel setArg(int index, long value) {
/* 105 */     util.setArg(this, index, value);
/* 106 */     return this;
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
/*     */   public CLKernel setArg(int index, float value) {
/* 119 */     util.setArg(this, index, value);
/* 120 */     return this;
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
/*     */   public CLKernel setArg(int index, double value) {
/* 133 */     util.setArg(this, index, value);
/* 134 */     return this;
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
/*     */   public CLKernel setArg(int index, CLObject value) {
/* 147 */     util.setArg(this, index, value);
/* 148 */     return this;
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
/*     */   public CLKernel setArgSize(int index, long size) {
/* 160 */     util.setArgSize(this, index, size);
/* 161 */     return this;
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
/*     */   public String getInfoString(int param_name) {
/* 174 */     return util.getInfoString(this, param_name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getInfoInt(int param_name) {
/* 185 */     return util.getInfoInt(this, param_name);
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
/*     */   public long getWorkGroupInfoSize(CLDevice device, int param_name) {
/* 198 */     return util.getWorkGroupInfoSize(this, device, param_name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long[] getWorkGroupInfoSizeArray(CLDevice device, int param_name) {
/* 209 */     return util.getWorkGroupInfoSizeArray(this, device, param_name);
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
/*     */   public long getWorkGroupInfoLong(CLDevice device, int param_name) {
/* 221 */     return util.getWorkGroupInfoLong(this, device, param_name);
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
/*     */   int release() {
/*     */     try {
/* 255 */       return super.release();
/*     */     } finally {
/* 257 */       if (!isValid())
/* 258 */         getParent().getCLKernelRegistry().unregisterObject(this); 
/*     */     } 
/*     */   }
/*     */   
/*     */   static interface CLKernelUtil extends InfoUtil<CLKernel> {
/*     */     void setArg(CLKernel param1CLKernel, int param1Int, byte param1Byte);
/*     */     
/*     */     void setArg(CLKernel param1CLKernel, int param1Int, short param1Short);
/*     */     
/*     */     void setArg(CLKernel param1CLKernel, int param1Int1, int param1Int2);
/*     */     
/*     */     void setArg(CLKernel param1CLKernel, int param1Int, long param1Long);
/*     */     
/*     */     void setArg(CLKernel param1CLKernel, int param1Int, float param1Float);
/*     */     
/*     */     void setArg(CLKernel param1CLKernel, int param1Int, double param1Double);
/*     */     
/*     */     void setArg(CLKernel param1CLKernel, int param1Int, CLObject param1CLObject);
/*     */     
/*     */     void setArgSize(CLKernel param1CLKernel, int param1Int, long param1Long);
/*     */     
/*     */     long getWorkGroupInfoSize(CLKernel param1CLKernel, CLDevice param1CLDevice, int param1Int);
/*     */     
/*     */     long[] getWorkGroupInfoSizeArray(CLKernel param1CLKernel, CLDevice param1CLDevice, int param1Int);
/*     */     
/*     */     long getWorkGroupInfoLong(CLKernel param1CLKernel, CLDevice param1CLDevice, int param1Int);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opencl\CLKernel.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */