/*    */ package com.badlogic.gdx;
/*    */ 
/*    */ import com.badlogic.gdx.files.FileHandle;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface Files
/*    */ {
/*    */   FileHandle getFileHandle(String paramString, FileType paramFileType);
/*    */   
/*    */   FileHandle classpath(String paramString);
/*    */   
/*    */   FileHandle internal(String paramString);
/*    */   
/*    */   FileHandle external(String paramString);
/*    */   
/*    */   FileHandle absolute(String paramString);
/*    */   
/*    */   FileHandle local(String paramString);
/*    */   
/*    */   String getExternalStoragePath();
/*    */   
/*    */   boolean isExternalStorageAvailable();
/*    */   
/*    */   String getLocalStoragePath();
/*    */   
/*    */   boolean isLocalStorageAvailable();
/*    */   
/*    */   public enum FileType
/*    */   {
/* 33 */     Classpath,
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 38 */     Internal,
/*    */ 
/*    */     
/* 41 */     External,
/*    */ 
/*    */ 
/*    */     
/* 45 */     Absolute,
/*    */ 
/*    */     
/* 48 */     Local;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\com\badlogic\gdx\Files.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */