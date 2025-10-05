/*     */ package org.apache.logging.log4j.core.impl;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.core.pattern.PlainTextRenderer;
/*     */ import org.apache.logging.log4j.core.pattern.TextRenderer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ExtendedClassInfo
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final boolean exact;
/*     */   private final String location;
/*     */   private final String version;
/*     */   
/*     */   public ExtendedClassInfo(boolean exact, String location, String version) {
/*  46 */     this.exact = exact;
/*  47 */     this.location = location;
/*  48 */     this.version = version;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  53 */     if (this == obj) {
/*  54 */       return true;
/*     */     }
/*  56 */     if (obj == null) {
/*  57 */       return false;
/*     */     }
/*  59 */     if (!(obj instanceof ExtendedClassInfo)) {
/*  60 */       return false;
/*     */     }
/*  62 */     ExtendedClassInfo other = (ExtendedClassInfo)obj;
/*  63 */     if (this.exact != other.exact) {
/*  64 */       return false;
/*     */     }
/*  66 */     if (!Objects.equals(this.location, other.location)) {
/*  67 */       return false;
/*     */     }
/*  69 */     if (!Objects.equals(this.version, other.version)) {
/*  70 */       return false;
/*     */     }
/*  72 */     return true;
/*     */   }
/*     */   
/*     */   public boolean getExact() {
/*  76 */     return this.exact;
/*     */   }
/*     */   
/*     */   public String getLocation() {
/*  80 */     return this.location;
/*     */   }
/*     */   
/*     */   public String getVersion() {
/*  84 */     return this.version;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  89 */     return Objects.hash(new Object[] { Boolean.valueOf(this.exact), this.location, this.version });
/*     */   }
/*     */   
/*     */   public void renderOn(StringBuilder output, TextRenderer textRenderer) {
/*  93 */     if (!this.exact) {
/*  94 */       textRenderer.render("~", output, "ExtraClassInfo.Inexact");
/*     */     }
/*  96 */     textRenderer.render("[", output, "ExtraClassInfo.Container");
/*  97 */     textRenderer.render(this.location, output, "ExtraClassInfo.Location");
/*  98 */     textRenderer.render(":", output, "ExtraClassInfo.ContainerSeparator");
/*  99 */     textRenderer.render(this.version, output, "ExtraClassInfo.Version");
/* 100 */     textRenderer.render("]", output, "ExtraClassInfo.Container");
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 105 */     StringBuilder sb = new StringBuilder();
/* 106 */     renderOn(sb, (TextRenderer)PlainTextRenderer.getInstance());
/* 107 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\impl\ExtendedClassInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */