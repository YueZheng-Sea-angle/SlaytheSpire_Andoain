/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.LineNumberReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringReader;
/*     */ import java.io.StringWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public final class Throwables
/*     */ {
/*     */   public static Throwable getRootCause(Throwable throwable) {
/*  46 */     Throwable slowPointer = throwable;
/*  47 */     boolean advanceSlowPointer = false;
/*     */     
/*  49 */     Throwable parent = throwable;
/*     */     Throwable cause;
/*  51 */     while ((cause = parent.getCause()) != null) {
/*  52 */       parent = cause;
/*  53 */       if (parent == slowPointer) {
/*  54 */         throw new IllegalArgumentException("loop in causal chain");
/*     */       }
/*  56 */       if (advanceSlowPointer) {
/*  57 */         slowPointer = slowPointer.getCause();
/*     */       }
/*  59 */       advanceSlowPointer = !advanceSlowPointer;
/*     */     } 
/*  61 */     return parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<String> toStringList(Throwable throwable) {
/*  72 */     StringWriter sw = new StringWriter();
/*  73 */     PrintWriter pw = new PrintWriter(sw);
/*     */     try {
/*  75 */       throwable.printStackTrace(pw);
/*  76 */     } catch (RuntimeException runtimeException) {}
/*     */ 
/*     */     
/*  79 */     pw.flush();
/*  80 */     List<String> lines = new ArrayList<>();
/*  81 */     LineNumberReader reader = new LineNumberReader(new StringReader(sw.toString()));
/*     */     try {
/*  83 */       String line = reader.readLine();
/*  84 */       while (line != null) {
/*  85 */         lines.add(line);
/*  86 */         line = reader.readLine();
/*     */       } 
/*  88 */     } catch (IOException ex) {
/*  89 */       if (ex instanceof java.io.InterruptedIOException) {
/*  90 */         Thread.currentThread().interrupt();
/*     */       }
/*  92 */       lines.add(ex.toString());
/*     */     } finally {
/*  94 */       Closer.closeSilently(reader);
/*     */     } 
/*  96 */     return lines;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void rethrow(Throwable t) {
/* 106 */     rethrow0(t);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T extends Throwable> void rethrow0(Throwable t) throws T {
/* 111 */     throw (T)t;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\Throwables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */