/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
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
/*     */ 
/*     */ public final class Transform
/*     */ {
/*     */   private static final String CDATA_START = "<![CDATA[";
/*     */   private static final String CDATA_END = "]]>";
/*     */   private static final String CDATA_PSEUDO_END = "]]&gt;";
/*     */   private static final String CDATA_EMBEDED_END = "]]>]]&gt;<![CDATA[";
/*  31 */   private static final int CDATA_END_LEN = "]]>".length();
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
/*     */   public static String escapeHtmlTags(String input) {
/*  49 */     if (Strings.isEmpty(input) || (input
/*  50 */       .indexOf('"') == -1 && input
/*  51 */       .indexOf('&') == -1 && input
/*  52 */       .indexOf('<') == -1 && input
/*  53 */       .indexOf('>') == -1)) {
/*  54 */       return input;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  60 */     StringBuilder buf = new StringBuilder(input.length() + 6);
/*     */     
/*  62 */     int len = input.length();
/*  63 */     for (int i = 0; i < len; i++) {
/*  64 */       char ch = input.charAt(i);
/*  65 */       if (ch > '>') {
/*  66 */         buf.append(ch);
/*     */       } else {
/*  68 */         switch (ch) {
/*     */           case '<':
/*  70 */             buf.append("&lt;");
/*     */             break;
/*     */           case '>':
/*  73 */             buf.append("&gt;");
/*     */             break;
/*     */           case '&':
/*  76 */             buf.append("&amp;");
/*     */             break;
/*     */           case '"':
/*  79 */             buf.append("&quot;");
/*     */             break;
/*     */           default:
/*  82 */             buf.append(ch); break;
/*     */         } 
/*     */       } 
/*     */     } 
/*  86 */     return buf.toString();
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
/*     */   public static void appendEscapingCData(StringBuilder buf, String str) {
/*  99 */     if (str != null) {
/* 100 */       int end = str.indexOf("]]>");
/* 101 */       if (end < 0) {
/* 102 */         buf.append(str);
/*     */       } else {
/* 104 */         int start = 0;
/* 105 */         while (end > -1) {
/* 106 */           buf.append(str.substring(start, end));
/* 107 */           buf.append("]]>]]&gt;<![CDATA[");
/* 108 */           start = end + CDATA_END_LEN;
/* 109 */           if (start < str.length()) {
/* 110 */             end = str.indexOf("]]>", start);
/*     */             continue;
/*     */           } 
/*     */           return;
/*     */         } 
/* 115 */         buf.append(str.substring(start));
/*     */       } 
/*     */     } 
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
/*     */   public static String escapeJsonControlCharacters(String input) {
/* 133 */     if (Strings.isEmpty(input) || (input
/* 134 */       .indexOf('"') == -1 && input
/* 135 */       .indexOf('\\') == -1 && input
/* 136 */       .indexOf('/') == -1 && input
/* 137 */       .indexOf('\b') == -1 && input
/* 138 */       .indexOf('\f') == -1 && input
/* 139 */       .indexOf('\n') == -1 && input
/* 140 */       .indexOf('\r') == -1 && input
/* 141 */       .indexOf('\t') == -1)) {
/* 142 */       return input;
/*     */     }
/*     */     
/* 145 */     StringBuilder buf = new StringBuilder(input.length() + 6);
/*     */     
/* 147 */     int len = input.length();
/* 148 */     for (int i = 0; i < len; i++) {
/* 149 */       char ch = input.charAt(i);
/* 150 */       String escBs = "\\";
/* 151 */       switch (ch) {
/*     */         case '"':
/* 153 */           buf.append("\\");
/* 154 */           buf.append(ch);
/*     */           break;
/*     */         case '\\':
/* 157 */           buf.append("\\");
/* 158 */           buf.append(ch);
/*     */           break;
/*     */         case '/':
/* 161 */           buf.append("\\");
/* 162 */           buf.append(ch);
/*     */           break;
/*     */         case '\b':
/* 165 */           buf.append("\\");
/* 166 */           buf.append('b');
/*     */           break;
/*     */         case '\f':
/* 169 */           buf.append("\\");
/* 170 */           buf.append('f');
/*     */           break;
/*     */         case '\n':
/* 173 */           buf.append("\\");
/* 174 */           buf.append('n');
/*     */           break;
/*     */         case '\r':
/* 177 */           buf.append("\\");
/* 178 */           buf.append('r');
/*     */           break;
/*     */         case '\t':
/* 181 */           buf.append("\\");
/* 182 */           buf.append('t');
/*     */           break;
/*     */         default:
/* 185 */           buf.append(ch); break;
/*     */       } 
/*     */     } 
/* 188 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\Transform.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */