/*    */ package org.apache.commons.net.nntp;
/*    */ 
/*    */ import java.util.Iterator;
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
/*    */ class ArticleIterator
/*    */   implements Iterator<Article>, Iterable<Article>
/*    */ {
/*    */   private final Iterator<String> stringIterator;
/*    */   
/*    */   public ArticleIterator(Iterable<String> iterableString) {
/* 32 */     this.stringIterator = iterableString.iterator();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasNext() {
/* 37 */     return this.stringIterator.hasNext();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Article next() {
/* 47 */     String line = this.stringIterator.next();
/* 48 */     return NNTPClient.__parseArticleEntry(line);
/*    */   }
/*    */ 
/*    */   
/*    */   public void remove() {
/* 53 */     this.stringIterator.remove();
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator<Article> iterator() {
/* 58 */     return this;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\nntp\ArticleIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */