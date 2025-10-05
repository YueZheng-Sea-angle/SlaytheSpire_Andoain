/*     */ package net.java.games.input;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Version
/*     */ {
/*     */   private static final String apiVersion = "2.0.5";
/*     */   private static final String buildNumber = "1088";
/*     */   private static final String antBuildNumberToken = "@BUILD_NUMBER@";
/*     */   private static final String antAPIVersionToken = "@API_VERSION@";
/*     */   
/*     */   public static String getVersion() {
/* 109 */     String version = "Unversioned";
/* 110 */     if (!"@API_VERSION@".equals("2.0.5")) {
/* 111 */       version = "2.0.5";
/*     */     }
/* 113 */     if (!"@BUILD_NUMBER@".equals("1088")) {
/* 114 */       version = version + "-b1088";
/*     */     }
/* 116 */     return version;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\net\java\games\input\Version.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */