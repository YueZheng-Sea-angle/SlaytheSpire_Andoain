/*    */ package org.lwjgl.opencl;
/*    */ 
/*    */ import java.util.Set;
/*    */ import java.util.StringTokenizer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CLPlatformCapabilities
/*    */ {
/*    */   public final int majorVersion;
/*    */   public final int minorVersion;
/*    */   public final boolean OpenCL11;
/*    */   public final boolean OpenCL12;
/*    */   final boolean CL_APPLE_ContextLoggingFunctions;
/*    */   public final boolean CL_APPLE_SetMemObjectDestructor;
/*    */   public final boolean CL_APPLE_gl_sharing;
/*    */   public final boolean CL_KHR_d3d10_sharing;
/*    */   public final boolean CL_KHR_gl_event;
/*    */   public final boolean CL_KHR_gl_sharing;
/*    */   public final boolean CL_KHR_icd;
/*    */   
/*    */   public CLPlatformCapabilities(CLPlatform platform) {
/* 24 */     String extensionList = platform.getInfoString(2308);
/* 25 */     String version = platform.getInfoString(2305);
/* 26 */     if (!version.startsWith("OpenCL ")) {
/* 27 */       throw new RuntimeException("Invalid OpenCL version string: " + version);
/*    */     }
/*    */     try {
/* 30 */       StringTokenizer tokenizer = new StringTokenizer(version.substring(7), ". ");
/*    */       
/* 32 */       this.majorVersion = Integer.parseInt(tokenizer.nextToken());
/* 33 */       this.minorVersion = Integer.parseInt(tokenizer.nextToken());
/*    */       
/* 35 */       this.OpenCL11 = (1 < this.majorVersion || (1 == this.majorVersion && 1 <= this.minorVersion));
/* 36 */       this.OpenCL12 = (1 < this.majorVersion || (1 == this.majorVersion && 2 <= this.minorVersion));
/* 37 */     } catch (RuntimeException e) {
/* 38 */       throw new RuntimeException("The major and/or minor OpenCL version \"" + version + "\" is malformed: " + e.getMessage());
/*    */     } 
/*    */     
/* 41 */     Set<String> extensions = APIUtil.getExtensions(extensionList);
/* 42 */     this.CL_APPLE_ContextLoggingFunctions = (extensions.contains("cl_APPLE_ContextLoggingFunctions") && CLCapabilities.CL_APPLE_ContextLoggingFunctions);
/* 43 */     this.CL_APPLE_SetMemObjectDestructor = (extensions.contains("cl_APPLE_SetMemObjectDestructor") && CLCapabilities.CL_APPLE_SetMemObjectDestructor);
/* 44 */     this.CL_APPLE_gl_sharing = (extensions.contains("cl_APPLE_gl_sharing") && CLCapabilities.CL_APPLE_gl_sharing);
/* 45 */     this.CL_KHR_d3d10_sharing = extensions.contains("cl_khr_d3d10_sharing");
/* 46 */     this.CL_KHR_gl_event = (extensions.contains("cl_khr_gl_event") && CLCapabilities.CL_KHR_gl_event);
/* 47 */     this.CL_KHR_gl_sharing = (extensions.contains("cl_khr_gl_sharing") && CLCapabilities.CL_KHR_gl_sharing);
/* 48 */     this.CL_KHR_icd = (extensions.contains("cl_khr_icd") && CLCapabilities.CL_KHR_icd);
/*    */   }
/*    */   
/*    */   public int getMajorVersion() {
/* 52 */     return this.majorVersion;
/*    */   }
/*    */   
/*    */   public int getMinorVersion() {
/* 56 */     return this.minorVersion;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 60 */     StringBuilder buf = new StringBuilder();
/*    */     
/* 62 */     buf.append("OpenCL ").append(this.majorVersion).append('.').append(this.minorVersion);
/*    */     
/* 64 */     buf.append(" - Extensions: ");
/* 65 */     if (this.CL_APPLE_ContextLoggingFunctions) buf.append("cl_apple_contextloggingfunctions "); 
/* 66 */     if (this.CL_APPLE_SetMemObjectDestructor) buf.append("cl_apple_setmemobjectdestructor "); 
/* 67 */     if (this.CL_APPLE_gl_sharing) buf.append("cl_apple_gl_sharing "); 
/* 68 */     if (this.CL_KHR_d3d10_sharing) buf.append("cl_khr_d3d10_sharing "); 
/* 69 */     if (this.CL_KHR_gl_event) buf.append("cl_khr_gl_event "); 
/* 70 */     if (this.CL_KHR_gl_sharing) buf.append("cl_khr_gl_sharing "); 
/* 71 */     if (this.CL_KHR_icd) buf.append("cl_khr_icd ");
/*    */     
/* 73 */     return buf.toString();
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opencl\CLPlatformCapabilities.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */