/*    */ package org.apache.logging.log4j.core.pattern;
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
/*    */ public final class PlainTextRenderer
/*    */   implements TextRenderer
/*    */ {
/* 24 */   private static final PlainTextRenderer INSTANCE = new PlainTextRenderer();
/*    */   
/*    */   public static PlainTextRenderer getInstance() {
/* 27 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(String input, StringBuilder output, String styleName) {
/* 32 */     output.append(input);
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(StringBuilder input, StringBuilder output) {
/* 37 */     output.append(input);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\PlainTextRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */