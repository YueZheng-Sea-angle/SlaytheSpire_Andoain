/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.nio.file.FileSystems;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.GroupPrincipal;
/*     */ import java.nio.file.attribute.PosixFileAttributeView;
/*     */ import java.nio.file.attribute.PosixFilePermission;
/*     */ import java.nio.file.attribute.UserPrincipal;
/*     */ import java.nio.file.attribute.UserPrincipalLookupService;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FileUtils
/*     */ {
/*     */   private static final String PROTOCOL_FILE = "file";
/*     */   private static final String JBOSS_FILE = "vfsfile";
/*  48 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static File fileFromUri(URI uri) {
/*  60 */     if (uri == null) {
/*  61 */       return null;
/*     */     }
/*  63 */     if (uri.isAbsolute()) {
/*  64 */       if ("vfsfile".equals(uri.getScheme())) {
/*     */         
/*     */         try {
/*  67 */           uri = new URI("file", uri.getSchemeSpecificPart(), uri.getFragment());
/*  68 */         } catch (URISyntaxException uRISyntaxException) {}
/*     */       }
/*     */ 
/*     */       
/*     */       try {
/*  73 */         if ("file".equals(uri.getScheme())) {
/*  74 */           return new File(uri);
/*     */         }
/*  76 */       } catch (Exception ex) {
/*  77 */         LOGGER.warn("Invalid URI {}", uri);
/*     */       } 
/*     */     } else {
/*  80 */       File file = new File(uri.toString());
/*     */       try {
/*  82 */         if (file.exists()) {
/*  83 */           return file;
/*     */         }
/*  85 */         String path = uri.getPath();
/*  86 */         return new File(path);
/*  87 */       } catch (Exception ex) {
/*  88 */         LOGGER.warn("Invalid URI {}", uri);
/*     */       } 
/*     */     } 
/*  91 */     return null;
/*     */   }
/*     */   
/*     */   public static boolean isFile(URL url) {
/*  95 */     return (url != null && (url.getProtocol().equals("file") || url.getProtocol().equals("vfsfile")));
/*     */   }
/*     */   
/*     */   public static String getFileExtension(File file) {
/*  99 */     String fileName = file.getName();
/* 100 */     if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
/* 101 */       return fileName.substring(fileName.lastIndexOf(".") + 1);
/*     */     }
/* 103 */     return null;
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
/*     */   public static void mkdir(File dir, boolean createDirectoryIfNotExisting) throws IOException {
/* 115 */     if (!dir.exists()) {
/* 116 */       if (!createDirectoryIfNotExisting) {
/* 117 */         throw new IOException("The directory " + dir.getAbsolutePath() + " does not exist.");
/*     */       }
/* 119 */       if (!dir.mkdirs()) {
/* 120 */         throw new IOException("Could not create directory " + dir.getAbsolutePath());
/*     */       }
/*     */     } 
/* 123 */     if (!dir.isDirectory()) {
/* 124 */       throw new IOException("File " + dir + " exists and is not a directory. Unable to create directory.");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void makeParentDirs(File file) throws IOException {
/* 135 */     File parent = ((File)Objects.<File>requireNonNull(file, "file")).getCanonicalFile().getParentFile();
/* 136 */     if (parent != null) {
/* 137 */       mkdir(parent, true);
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
/*     */   public static void defineFilePosixAttributeView(Path path, Set<PosixFilePermission> filePermissions, String fileOwner, String fileGroup) throws IOException {
/* 154 */     PosixFileAttributeView view = Files.<PosixFileAttributeView>getFileAttributeView(path, PosixFileAttributeView.class, new java.nio.file.LinkOption[0]);
/* 155 */     if (view != null) {
/*     */       
/* 157 */       UserPrincipalLookupService lookupService = FileSystems.getDefault().getUserPrincipalLookupService();
/* 158 */       if (fileOwner != null) {
/* 159 */         UserPrincipal userPrincipal = lookupService.lookupPrincipalByName(fileOwner);
/* 160 */         if (userPrincipal != null)
/*     */         {
/*     */ 
/*     */ 
/*     */           
/* 165 */           view.setOwner(userPrincipal);
/*     */         }
/*     */       } 
/* 168 */       if (fileGroup != null) {
/* 169 */         GroupPrincipal groupPrincipal = lookupService.lookupPrincipalByGroupName(fileGroup);
/* 170 */         if (groupPrincipal != null)
/*     */         {
/*     */           
/* 173 */           view.setGroup(groupPrincipal);
/*     */         }
/*     */       } 
/* 176 */       if (filePermissions != null) {
/* 177 */         view.setPermissions(filePermissions);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isFilePosixAttributeViewSupported() {
/* 188 */     return FileSystems.getDefault().supportedFileAttributeViews().contains("posix");
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\FileUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */