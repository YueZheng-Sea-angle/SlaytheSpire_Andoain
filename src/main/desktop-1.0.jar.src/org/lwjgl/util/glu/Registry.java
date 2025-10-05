/*    */ package org.lwjgl.util.glu;
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
/*    */ public class Registry
/*    */   extends Util
/*    */ {
/*    */   private static final String versionString = "1.3";
/*    */   private static final String extensionString = "GLU_EXT_nurbs_tessellator GLU_EXT_object_space_tess ";
/*    */   
/*    */   public static String gluGetString(int name) {
/* 56 */     if (name == 100800)
/* 57 */       return "1.3"; 
/* 58 */     if (name == 100801) {
/* 59 */       return "GLU_EXT_nurbs_tessellator GLU_EXT_object_space_tess ";
/*    */     }
/* 61 */     return null;
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
/*    */ 
/*    */   
/*    */   public static boolean gluCheckExtension(String extName, String extString) {
/* 75 */     if (extString == null || extName == null) {
/* 76 */       return false;
/*    */     }
/* 78 */     return (extString.indexOf(extName) != -1);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjg\\util\glu\Registry.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */