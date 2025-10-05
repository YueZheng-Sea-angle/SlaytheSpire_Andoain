/*     */ package org.apache.commons.net.imap;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.commons.net.MalformedServerReplyException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class IMAPReply
/*     */ {
/*     */   public static final int OK = 0;
/*     */   public static final int NO = 1;
/*     */   public static final int BAD = 2;
/*     */   public static final int CONT = 3;
/*     */   public static final int PARTIAL = 3;
/*     */   private static final String IMAP_OK = "OK";
/*     */   private static final String IMAP_NO = "NO";
/*     */   private static final String IMAP_BAD = "BAD";
/*     */   private static final String IMAP_UNTAGGED_PREFIX = "* ";
/*     */   private static final String IMAP_CONTINUATION_PREFIX = "+";
/*     */   private static final String TAGGED_RESPONSE = "^\\w+ (\\S+).*";
/*     */   
/*     */   public static boolean isUntagged(String line) {
/*  77 */     return line.startsWith("* ");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isContinuation(String line) {
/*  86 */     return line.startsWith("+");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  91 */   private static final Pattern TAGGED_PATTERN = Pattern.compile("^\\w+ (\\S+).*");
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String UNTAGGED_RESPONSE = "^\\* (\\S+).*";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getReplyCode(String line) throws IOException {
/* 101 */     return getReplyCode(line, TAGGED_PATTERN);
/*     */   }
/*     */ 
/*     */   
/* 105 */   private static final Pattern UNTAGGED_PATTERN = Pattern.compile("^\\* (\\S+).*");
/*     */   
/* 107 */   private static final Pattern LITERAL_PATTERN = Pattern.compile("\\{(\\d+)\\}$");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int literalCount(String line) {
/* 116 */     Matcher m = LITERAL_PATTERN.matcher(line);
/* 117 */     if (m.find()) {
/* 118 */       return Integer.parseInt(m.group(1));
/*     */     }
/* 120 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getUntaggedReplyCode(String line) throws IOException {
/* 131 */     return getReplyCode(line, UNTAGGED_PATTERN);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int getReplyCode(String line, Pattern pattern) throws IOException {
/* 136 */     if (isContinuation(line)) {
/* 137 */       return 3;
/*     */     }
/* 139 */     Matcher m = pattern.matcher(line);
/* 140 */     if (m.matches()) {
/* 141 */       String code = m.group(1);
/* 142 */       if (code.equals("OK")) {
/* 143 */         return 0;
/*     */       }
/* 145 */       if (code.equals("BAD")) {
/* 146 */         return 2;
/*     */       }
/* 148 */       if (code.equals("NO")) {
/* 149 */         return 1;
/*     */       }
/*     */     } 
/* 152 */     throw new MalformedServerReplyException("Received unexpected IMAP protocol response from server: '" + line + "'.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isSuccess(int replyCode) {
/* 163 */     return (replyCode == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isContinuation(int replyCode) {
/* 171 */     return (replyCode == 3);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\imap\IMAPReply.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */