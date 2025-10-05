/*    */ package org.apache.logging.log4j.core.tools;
/*    */ 
/*    */ import org.apache.logging.log4j.core.tools.picocli.CommandLine.Option;
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
/*    */ public class BasicCommandLineArguments
/*    */ {
/*    */   @Option(names = {"--help", "-?", "-h"}, usageHelp = true, description = {"Prints this help and exits."})
/*    */   private boolean help;
/*    */   
/*    */   public boolean isHelp() {
/* 27 */     return this.help;
/*    */   }
/*    */   
/*    */   public void setHelp(boolean help) {
/* 31 */     this.help = help;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\tools\BasicCommandLineArguments.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */