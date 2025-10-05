/*     */ package org.lwjgl.opencl;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import org.lwjgl.PointerBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CLProgram
/*     */   extends CLObjectChild<CLContext>
/*     */ {
/*  45 */   private static final CLProgramUtil util = (CLProgramUtil)CLPlatform.<CLProgram>getInfoUtilInstance(CLProgram.class, "CL_PROGRAM_UTIL");
/*     */   
/*     */   private final CLObjectRegistry<CLKernel> clKernels;
/*     */   
/*     */   CLProgram(long pointer, CLContext context) {
/*  50 */     super(pointer, context);
/*     */     
/*  52 */     if (isValid()) {
/*  53 */       context.getCLProgramRegistry().registerObject(this);
/*  54 */       this.clKernels = new CLObjectRegistry<CLKernel>();
/*     */     } else {
/*  56 */       this.clKernels = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CLKernel getCLKernel(long id) {
/*  67 */     return this.clKernels.getObject(id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CLKernel[] createKernelsInProgram() {
/*  78 */     return util.createKernelsInProgram(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getInfoString(int param_name) {
/*  89 */     return util.getInfoString(this, param_name);
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
/* 100 */     return util.getInfoInt(this, param_name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long[] getInfoSizeArray(int param_name) {
/* 111 */     return util.getInfoSizeArray(this, param_name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CLDevice[] getInfoDevices() {
/* 120 */     return util.getInfoDevices(this);
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
/*     */   public ByteBuffer getInfoBinaries(ByteBuffer target) {
/* 135 */     return util.getInfoBinaries(this, target);
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
/*     */   public ByteBuffer[] getInfoBinaries(ByteBuffer[] target) {
/* 150 */     return util.getInfoBinaries(this, target);
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
/*     */   public String getBuildInfoString(CLDevice device, int param_name) {
/* 163 */     return util.getBuildInfoString(this, device, param_name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBuildInfoInt(CLDevice device, int param_name) {
/* 174 */     return util.getBuildInfoInt(this, device, param_name);
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
/*     */   CLObjectRegistry<CLKernel> getCLKernelRegistry() {
/* 196 */     return this.clKernels;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void registerCLKernels(PointerBuffer kernels) {
/* 204 */     for (int i = kernels.position(); i < kernels.limit(); i++) {
/* 205 */       long pointer = kernels.get(i);
/* 206 */       if (pointer != 0L)
/* 207 */         new CLKernel(pointer, this); 
/*     */     } 
/*     */   }
/*     */   
/*     */   int release() {
/*     */     try {
/* 213 */       return super.release();
/*     */     } finally {
/* 215 */       if (!isValid())
/* 216 */         getParent().getCLProgramRegistry().unregisterObject(this); 
/*     */     } 
/*     */   }
/*     */   
/*     */   static interface CLProgramUtil extends InfoUtil<CLProgram> {
/*     */     CLKernel[] createKernelsInProgram(CLProgram param1CLProgram);
/*     */     
/*     */     CLDevice[] getInfoDevices(CLProgram param1CLProgram);
/*     */     
/*     */     ByteBuffer getInfoBinaries(CLProgram param1CLProgram, ByteBuffer param1ByteBuffer);
/*     */     
/*     */     ByteBuffer[] getInfoBinaries(CLProgram param1CLProgram, ByteBuffer[] param1ArrayOfByteBuffer);
/*     */     
/*     */     String getBuildInfoString(CLProgram param1CLProgram, CLDevice param1CLDevice, int param1Int);
/*     */     
/*     */     int getBuildInfoInt(CLProgram param1CLProgram, CLDevice param1CLDevice, int param1Int);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opencl\CLProgram.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */