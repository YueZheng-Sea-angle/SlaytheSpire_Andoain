/*     */ package org.apache.commons.net.nntp;
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
/*     */ public final class NewsgroupInfo
/*     */ {
/*     */   public static final int UNKNOWN_POSTING_PERMISSION = 0;
/*     */   public static final int MODERATED_POSTING_PERMISSION = 1;
/*     */   public static final int PERMITTED_POSTING_PERMISSION = 2;
/*     */   public static final int PROHIBITED_POSTING_PERMISSION = 3;
/*     */   private String __newsgroup;
/*     */   private long __estimatedArticleCount;
/*     */   private long __firstArticle;
/*     */   private long __lastArticle;
/*     */   private int __postingPermission;
/*     */   
/*     */   void _setNewsgroup(String newsgroup) {
/*  62 */     this.__newsgroup = newsgroup;
/*     */   }
/*     */ 
/*     */   
/*     */   void _setArticleCount(long count) {
/*  67 */     this.__estimatedArticleCount = count;
/*     */   }
/*     */ 
/*     */   
/*     */   void _setFirstArticle(long first) {
/*  72 */     this.__firstArticle = first;
/*     */   }
/*     */ 
/*     */   
/*     */   void _setLastArticle(long last) {
/*  77 */     this.__lastArticle = last;
/*     */   }
/*     */ 
/*     */   
/*     */   void _setPostingPermission(int permission) {
/*  82 */     this.__postingPermission = permission;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNewsgroup() {
/*  92 */     return this.__newsgroup;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getArticleCountLong() {
/* 103 */     return this.__estimatedArticleCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getFirstArticleLong() {
/* 113 */     return this.__firstArticle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLastArticleLong() {
/* 123 */     return this.__lastArticle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPostingPermission() {
/* 134 */     return this.__postingPermission;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public int getArticleCount() {
/* 159 */     return (int)this.__estimatedArticleCount;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public int getFirstArticle() {
/* 164 */     return (int)this.__firstArticle;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public int getLastArticle() {
/* 169 */     return (int)this.__lastArticle;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\nntp\NewsgroupInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */