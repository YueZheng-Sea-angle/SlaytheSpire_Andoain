/*     */ package org.apache.logging.log4j.core.appender.rolling;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.core.appender.rolling.action.Action;
/*     */ import org.apache.logging.log4j.core.appender.rolling.action.CommonsCompressAction;
/*     */ import org.apache.logging.log4j.core.appender.rolling.action.GzCompressAction;
/*     */ import org.apache.logging.log4j.core.appender.rolling.action.ZipCompressAction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum FileExtension
/*     */ {
/*  31 */   ZIP(".zip")
/*     */   {
/*     */     Action createCompressAction(String renameTo, String compressedName, boolean deleteSource, int compressionLevel)
/*     */     {
/*  35 */       return (Action)new ZipCompressAction(source(renameTo), target(compressedName), deleteSource, compressionLevel);
/*     */     }
/*     */   },
/*  38 */   GZ(".gz")
/*     */   {
/*     */     Action createCompressAction(String renameTo, String compressedName, boolean deleteSource, int compressionLevel)
/*     */     {
/*  42 */       return (Action)new GzCompressAction(source(renameTo), target(compressedName), deleteSource, compressionLevel);
/*     */     }
/*     */   },
/*  45 */   BZIP2(".bz2")
/*     */   {
/*     */     
/*     */     Action createCompressAction(String renameTo, String compressedName, boolean deleteSource, int compressionLevel)
/*     */     {
/*  50 */       return (Action)new CommonsCompressAction("bzip2", source(renameTo), target(compressedName), deleteSource);
/*     */     }
/*     */   },
/*  53 */   DEFLATE(".deflate")
/*     */   {
/*     */     
/*     */     Action createCompressAction(String renameTo, String compressedName, boolean deleteSource, int compressionLevel)
/*     */     {
/*  58 */       return (Action)new CommonsCompressAction("deflate", source(renameTo), target(compressedName), deleteSource);
/*     */     }
/*     */   },
/*  61 */   PACK200(".pack200")
/*     */   {
/*     */     
/*     */     Action createCompressAction(String renameTo, String compressedName, boolean deleteSource, int compressionLevel)
/*     */     {
/*  66 */       return (Action)new CommonsCompressAction("pack200", source(renameTo), target(compressedName), deleteSource);
/*     */     }
/*     */   },
/*  69 */   XZ(".xz")
/*     */   {
/*     */     
/*     */     Action createCompressAction(String renameTo, String compressedName, boolean deleteSource, int compressionLevel)
/*     */     {
/*  74 */       return (Action)new CommonsCompressAction("xz", source(renameTo), target(compressedName), deleteSource);
/*     */     }
/*     */   };
/*     */   
/*     */   public static FileExtension lookup(String fileExtension) {
/*  79 */     for (FileExtension ext : values()) {
/*  80 */       if (ext.isExtensionFor(fileExtension)) {
/*  81 */         return ext;
/*     */       }
/*     */     } 
/*  84 */     return null;
/*     */   }
/*     */   private final String extension;
/*     */   public static FileExtension lookupForFile(String fileName) {
/*  88 */     for (FileExtension ext : values()) {
/*  89 */       if (fileName.endsWith(ext.extension)) {
/*  90 */         return ext;
/*     */       }
/*     */     } 
/*  93 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   FileExtension(String extension) {
/*  99 */     Objects.requireNonNull(extension, "extension");
/* 100 */     this.extension = extension;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String getExtension() {
/* 107 */     return this.extension;
/*     */   }
/*     */   
/*     */   boolean isExtensionFor(String s) {
/* 111 */     return s.endsWith(this.extension);
/*     */   }
/*     */   
/*     */   int length() {
/* 115 */     return this.extension.length();
/*     */   }
/*     */   
/*     */   File source(String fileName) {
/* 119 */     return new File(fileName);
/*     */   }
/*     */   
/*     */   File target(String fileName) {
/* 123 */     return new File(fileName);
/*     */   }
/*     */   
/*     */   abstract Action createCompressAction(String paramString1, String paramString2, boolean paramBoolean, int paramInt);
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\FileExtension.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */