/*    */ package org.apache.logging.log4j.core.util;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ public enum ExtensionLanguageMapping
/*    */ {
/* 26 */   JS("js", "JavaScript"), JAVASCRIPT("javascript", "JavaScript"), GVY("gvy", "Groovy"),
/* 27 */   GROOVY("groovy", "Groovy"), BSH("bsh", "beanshell"), BEANSHELL("beanshell", "beanshell"),
/* 28 */   JY("jy", "jython"), JYTHON("jython", "jython"), FTL("ftl", "freemarker"),
/* 29 */   FREEMARKER("freemarker", "freemarker"), VM("vm", "velocity"), VELOCITY("velocity", "velocity"),
/* 30 */   AWK("awk", "awk"), EJS("ejs", "ejs"), TCL("tcl", "tcl"), HS("hs", "jaskell"), JELLY("jelly", "jelly"),
/* 31 */   JEP("jep", "jep"), JEXL("jexl", "jexl"), JEXL2("jexl2", "jexl2"),
/* 32 */   RB("rb", "ruby"), RUBY("ruby", "ruby"), JUDO("judo", "judo"), JUDI("judi", "judo"), SCALA("scala", "scala"),
/* 33 */   CLJ("clj", "Clojure");
/*    */   
/*    */   private final String extension;
/*    */   
/*    */   private final String language;
/*    */   
/*    */   ExtensionLanguageMapping(String extension, String language) {
/* 40 */     this.extension = extension;
/* 41 */     this.language = language;
/*    */   }
/*    */   
/*    */   public String getExtension() {
/* 45 */     return this.extension;
/*    */   }
/*    */   
/*    */   public String getLanguage() {
/* 49 */     return this.language;
/*    */   }
/*    */   
/*    */   public static ExtensionLanguageMapping getByExtension(String extension) {
/* 53 */     for (ExtensionLanguageMapping mapping : values()) {
/* 54 */       if (mapping.extension.equals(extension)) {
/* 55 */         return mapping;
/*    */       }
/*    */     } 
/* 58 */     return null;
/*    */   }
/*    */   
/*    */   public static List<ExtensionLanguageMapping> getByLanguage(String language) {
/* 62 */     List<ExtensionLanguageMapping> list = new ArrayList<>();
/* 63 */     for (ExtensionLanguageMapping mapping : values()) {
/* 64 */       if (mapping.language.equals(language)) {
/* 65 */         list.add(mapping);
/*    */       }
/*    */     } 
/* 68 */     return list;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\ExtensionLanguageMapping.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */