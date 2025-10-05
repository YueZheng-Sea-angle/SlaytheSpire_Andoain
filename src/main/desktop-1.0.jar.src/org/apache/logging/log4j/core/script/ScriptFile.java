/*     */ package org.apache.logging.log4j.core.script;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.net.URI;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.util.ExtensionLanguageMapping;
/*     */ import org.apache.logging.log4j.core.util.FileUtils;
/*     */ import org.apache.logging.log4j.core.util.IOUtils;
/*     */ import org.apache.logging.log4j.core.util.NetUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "ScriptFile", category = "Core", printObject = true)
/*     */ public class ScriptFile
/*     */   extends AbstractScript
/*     */ {
/*     */   private final Path filePath;
/*     */   private final boolean isWatched;
/*     */   
/*     */   public ScriptFile(String name, Path filePath, String language, boolean isWatched, String scriptText) {
/*  49 */     super(name, language, scriptText);
/*  50 */     this.filePath = filePath;
/*  51 */     this.isWatched = isWatched;
/*     */   }
/*     */   
/*     */   public Path getPath() {
/*  55 */     return this.filePath;
/*     */   }
/*     */   
/*     */   public boolean isWatched() {
/*  59 */     return this.isWatched;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static ScriptFile createScript(@PluginAttribute("name") String name, @PluginAttribute("language") String language, @PluginAttribute("path") String filePathOrUri, @PluginAttribute("isWatched") Boolean isWatched, @PluginAttribute("charset") Charset charset) {
/*     */     String scriptText;
/*  71 */     if (filePathOrUri == null) {
/*  72 */       LOGGER.error("No script path provided for ScriptFile");
/*  73 */       return null;
/*     */     } 
/*  75 */     if (name == null) {
/*  76 */       name = filePathOrUri;
/*     */     }
/*  78 */     URI uri = NetUtils.toURI(filePathOrUri);
/*  79 */     File file = FileUtils.fileFromUri(uri);
/*  80 */     if (language == null && file != null) {
/*  81 */       String fileExtension = FileUtils.getFileExtension(file);
/*  82 */       if (fileExtension != null) {
/*  83 */         ExtensionLanguageMapping mapping = ExtensionLanguageMapping.getByExtension(fileExtension);
/*  84 */         if (mapping != null) {
/*  85 */           language = mapping.getLanguage();
/*     */         }
/*     */       } 
/*     */     } 
/*  89 */     if (language == null) {
/*  90 */       LOGGER.info("No script language supplied, defaulting to {}", "JavaScript");
/*  91 */       language = "JavaScript";
/*     */     } 
/*     */     
/*  94 */     Charset actualCharset = (charset == null) ? Charset.defaultCharset() : charset;
/*     */     
/*  96 */     try (Reader reader = new InputStreamReader((file != null) ? new FileInputStream(file) : uri
/*  97 */           .toURL().openStream(), actualCharset)) {
/*  98 */       scriptText = IOUtils.toString(reader);
/*  99 */     } catch (IOException e) {
/* 100 */       LOGGER.error("{}: language={}, path={}, actualCharset={}", e.getClass().getSimpleName(), language, filePathOrUri, actualCharset);
/*     */       
/* 102 */       return null;
/*     */     } 
/* 104 */     Path path = (file != null) ? Paths.get(file.toURI()) : Paths.get(uri);
/* 105 */     if (path == null) {
/* 106 */       LOGGER.error("Unable to convert {} to a Path", uri.toString());
/* 107 */       return null;
/*     */     } 
/* 109 */     return new ScriptFile(name, path, language, ((isWatched == null) ? Boolean.FALSE : isWatched).booleanValue(), scriptText);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 114 */     StringBuilder sb = new StringBuilder();
/* 115 */     if (!getName().equals(this.filePath.toString())) {
/* 116 */       sb.append("name=").append(getName()).append(", ");
/*     */     }
/* 118 */     sb.append("path=").append(this.filePath);
/* 119 */     if (getLanguage() != null) {
/* 120 */       sb.append(", language=").append(getLanguage());
/*     */     }
/* 122 */     sb.append(", isWatched=").append(this.isWatched);
/* 123 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\script\ScriptFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */