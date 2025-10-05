/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.net.URI;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationSource;
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
/*     */ public class Source
/*     */ {
/*     */   private final File file;
/*     */   private final URI uri;
/*     */   private final String location;
/*     */   
/*     */   public Source(ConfigurationSource source) {
/*  44 */     this.file = source.getFile();
/*  45 */     this.uri = source.getURI();
/*  46 */     this.location = source.getLocation();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Source(File file) {
/*  56 */     this.file = Objects.<File>requireNonNull(file, "file is null");
/*  57 */     this.location = file.getAbsolutePath();
/*  58 */     this.uri = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Source(URI uri, long lastModified) {
/*  67 */     this.uri = Objects.<URI>requireNonNull(uri, "URI is null");
/*  68 */     this.location = uri.toString();
/*  69 */     this.file = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getFile() {
/*  79 */     return this.file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI getURI() {
/*  89 */     return this.uri;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLocation() {
/*  99 */     return this.location;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 104 */     return this.location;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 109 */     if (this == o) {
/* 110 */       return true;
/*     */     }
/* 112 */     if (!(o instanceof Source)) {
/* 113 */       return false;
/*     */     }
/* 115 */     Source source = (Source)o;
/* 116 */     return Objects.equals(this.location, source.location);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 121 */     return 31 + Objects.hashCode(this.location);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\Source.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */