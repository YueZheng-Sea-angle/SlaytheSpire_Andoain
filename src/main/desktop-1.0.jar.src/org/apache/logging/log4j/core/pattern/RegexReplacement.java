/*    */ package org.apache.logging.log4j.core.pattern;
/*    */ 
/*    */ import java.util.regex.Pattern;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
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
/*    */ @Plugin(name = "replace", category = "Core", printObject = true)
/*    */ public final class RegexReplacement
/*    */ {
/* 34 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*    */ 
/*    */ 
/*    */   
/*    */   private final Pattern pattern;
/*    */ 
/*    */ 
/*    */   
/*    */   private final String substitution;
/*    */ 
/*    */ 
/*    */   
/*    */   private RegexReplacement(Pattern pattern, String substitution) {
/* 47 */     this.pattern = pattern;
/* 48 */     this.substitution = substitution;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String format(String msg) {
/* 57 */     return this.pattern.matcher(msg).replaceAll(this.substitution);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 62 */     return "replace(regex=" + this.pattern.pattern() + ", replacement=" + this.substitution + ')';
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
/*    */   @PluginFactory
/*    */   public static RegexReplacement createRegexReplacement(@PluginAttribute("regex") Pattern regex, @PluginAttribute("replacement") String replacement) {
/* 75 */     if (regex == null) {
/* 76 */       LOGGER.error("A regular expression is required for replacement");
/* 77 */       return null;
/*    */     } 
/* 79 */     if (replacement == null) {
/* 80 */       LOGGER.error("A replacement string is required to perform replacement");
/*    */     }
/*    */     
/* 83 */     return new RegexReplacement(regex, replacement);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\RegexReplacement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */