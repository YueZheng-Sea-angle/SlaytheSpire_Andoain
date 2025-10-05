/*     */ package org.apache.logging.log4j.core.lookup;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.util.Strings;
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
/*     */ @Plugin(name = "java", category = "Lookup")
/*     */ public class JavaLookup
/*     */   extends AbstractLookup
/*     */ {
/*  31 */   private final SystemPropertiesLookup spLookup = new SystemPropertiesLookup();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getHardware() {
/*  38 */     return "processors: " + Runtime.getRuntime().availableProcessors() + ", architecture: " + 
/*  39 */       getSystemProperty("os.arch") + getSystemProperty("-", "sun.arch.data.model") + 
/*  40 */       getSystemProperty(", instruction sets: ", "sun.cpu.isalist");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLocale() {
/*  48 */     return "default locale: " + Locale.getDefault() + ", platform encoding: " + getSystemProperty("file.encoding");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getOperatingSystem() {
/*  56 */     return getSystemProperty("os.name") + " " + getSystemProperty("os.version") + 
/*  57 */       getSystemProperty(" ", "sun.os.patch.level") + ", architecture: " + getSystemProperty("os.arch") + 
/*  58 */       getSystemProperty("-", "sun.arch.data.model");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRuntime() {
/*  66 */     return getSystemProperty("java.runtime.name") + " (build " + getSystemProperty("java.runtime.version") + ") from " + 
/*  67 */       getSystemProperty("java.vendor");
/*     */   }
/*     */   
/*     */   private String getSystemProperty(String name) {
/*  71 */     return this.spLookup.lookup(name);
/*     */   }
/*     */   
/*     */   private String getSystemProperty(String prefix, String name) {
/*  75 */     String value = getSystemProperty(name);
/*  76 */     if (Strings.isEmpty(value)) {
/*  77 */       return "";
/*     */     }
/*  79 */     return prefix + value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getVirtualMachine() {
/*  87 */     return getSystemProperty("java.vm.name") + " (build " + getSystemProperty("java.vm.version") + ", " + 
/*  88 */       getSystemProperty("java.vm.info") + ")";
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
/*     */   public String lookup(LogEvent event, String key) {
/* 102 */     switch (key) {
/*     */       case "version":
/* 104 */         return "Java version " + getSystemProperty("java.version");
/*     */       case "runtime":
/* 106 */         return getRuntime();
/*     */       case "vm":
/* 108 */         return getVirtualMachine();
/*     */       case "os":
/* 110 */         return getOperatingSystem();
/*     */       case "hw":
/* 112 */         return getHardware();
/*     */       case "locale":
/* 114 */         return getLocale();
/*     */     } 
/* 116 */     throw new IllegalArgumentException(key);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\lookup\JavaLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */