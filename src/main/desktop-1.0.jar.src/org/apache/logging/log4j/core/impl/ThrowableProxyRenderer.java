/*     */ package org.apache.logging.log4j.core.impl;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.core.pattern.TextRenderer;
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
/*     */ class ThrowableProxyRenderer
/*     */ {
/*     */   private static final String TAB = "\t";
/*     */   private static final String CAUSED_BY_LABEL = "Caused by: ";
/*     */   private static final String SUPPRESSED_LABEL = "Suppressed: ";
/*     */   private static final String WRAPPED_BY_LABEL = "Wrapped by: ";
/*     */   
/*     */   static void formatWrapper(StringBuilder sb, ThrowableProxy cause, List<String> ignorePackages, TextRenderer textRenderer, String suffix, String lineSeparator) {
/*  42 */     Throwable caused = (cause.getCauseProxy() != null) ? cause.getCauseProxy().getThrowable() : null;
/*  43 */     if (caused != null) {
/*  44 */       formatWrapper(sb, cause.getCauseProxy(), ignorePackages, textRenderer, suffix, lineSeparator);
/*  45 */       sb.append("Wrapped by: ");
/*  46 */       renderSuffix(suffix, sb, textRenderer);
/*     */     } 
/*  48 */     renderOn(cause, sb, textRenderer);
/*  49 */     renderSuffix(suffix, sb, textRenderer);
/*  50 */     textRenderer.render(lineSeparator, sb, "Text");
/*  51 */     formatElements(sb, "", cause.getCommonElementCount(), cause
/*  52 */         .getThrowable().getStackTrace(), cause.getExtendedStackTrace(), ignorePackages, textRenderer, suffix, lineSeparator);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void formatCause(StringBuilder sb, String prefix, ThrowableProxy cause, List<String> ignorePackages, TextRenderer textRenderer, String suffix, String lineSeparator) {
/*  57 */     formatThrowableProxy(sb, prefix, "Caused by: ", cause, ignorePackages, textRenderer, suffix, lineSeparator);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void formatThrowableProxy(StringBuilder sb, String prefix, String causeLabel, ThrowableProxy throwableProxy, List<String> ignorePackages, TextRenderer textRenderer, String suffix, String lineSeparator) {
/*  63 */     if (throwableProxy == null) {
/*     */       return;
/*     */     }
/*  66 */     textRenderer.render(prefix, sb, "Prefix");
/*  67 */     textRenderer.render(causeLabel, sb, "CauseLabel");
/*  68 */     renderOn(throwableProxy, sb, textRenderer);
/*  69 */     renderSuffix(suffix, sb, textRenderer);
/*  70 */     textRenderer.render(lineSeparator, sb, "Text");
/*  71 */     formatElements(sb, prefix, throwableProxy.getCommonElementCount(), throwableProxy
/*  72 */         .getStackTrace(), throwableProxy.getExtendedStackTrace(), ignorePackages, textRenderer, suffix, lineSeparator);
/*  73 */     formatSuppressed(sb, prefix + "\t", throwableProxy.getSuppressedProxies(), ignorePackages, textRenderer, suffix, lineSeparator);
/*  74 */     formatCause(sb, prefix, throwableProxy.getCauseProxy(), ignorePackages, textRenderer, suffix, lineSeparator);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void formatSuppressed(StringBuilder sb, String prefix, ThrowableProxy[] suppressedProxies, List<String> ignorePackages, TextRenderer textRenderer, String suffix, String lineSeparator) {
/*  79 */     if (suppressedProxies == null) {
/*     */       return;
/*     */     }
/*  82 */     for (ThrowableProxy suppressedProxy : suppressedProxies) {
/*  83 */       formatThrowableProxy(sb, prefix, "Suppressed: ", suppressedProxy, ignorePackages, textRenderer, suffix, lineSeparator);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void formatElements(StringBuilder sb, String prefix, int commonCount, StackTraceElement[] causedTrace, ExtendedStackTraceElement[] extStackTrace, List<String> ignorePackages, TextRenderer textRenderer, String suffix, String lineSeparator) {
/*  90 */     if (ignorePackages == null || ignorePackages.isEmpty()) {
/*  91 */       for (ExtendedStackTraceElement element : extStackTrace) {
/*  92 */         formatEntry(element, sb, prefix, textRenderer, suffix, lineSeparator);
/*     */       }
/*     */     } else {
/*  95 */       int count = 0;
/*  96 */       for (int i = 0; i < extStackTrace.length; i++) {
/*  97 */         if (!ignoreElement(causedTrace[i], ignorePackages)) {
/*  98 */           if (count > 0) {
/*  99 */             appendSuppressedCount(sb, prefix, count, textRenderer, suffix, lineSeparator);
/* 100 */             count = 0;
/*     */           } 
/* 102 */           formatEntry(extStackTrace[i], sb, prefix, textRenderer, suffix, lineSeparator);
/*     */         } else {
/* 104 */           count++;
/*     */         } 
/*     */       } 
/* 107 */       if (count > 0) {
/* 108 */         appendSuppressedCount(sb, prefix, count, textRenderer, suffix, lineSeparator);
/*     */       }
/*     */     } 
/* 111 */     if (commonCount != 0) {
/* 112 */       textRenderer.render(prefix, sb, "Prefix");
/* 113 */       textRenderer.render("\t... ", sb, "More");
/* 114 */       textRenderer.render(Integer.toString(commonCount), sb, "More");
/* 115 */       textRenderer.render(" more", sb, "More");
/* 116 */       renderSuffix(suffix, sb, textRenderer);
/* 117 */       textRenderer.render(lineSeparator, sb, "Text");
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void renderSuffix(String suffix, StringBuilder sb, TextRenderer textRenderer) {
/* 122 */     if (!suffix.isEmpty()) {
/* 123 */       textRenderer.render(" ", sb, "Suffix");
/* 124 */       textRenderer.render(suffix, sb, "Suffix");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void appendSuppressedCount(StringBuilder sb, String prefix, int count, TextRenderer textRenderer, String suffix, String lineSeparator) {
/* 130 */     textRenderer.render(prefix, sb, "Prefix");
/* 131 */     if (count == 1) {
/* 132 */       textRenderer.render("\t... ", sb, "Suppressed");
/*     */     } else {
/* 134 */       textRenderer.render("\t... suppressed ", sb, "Suppressed");
/* 135 */       textRenderer.render(Integer.toString(count), sb, "Suppressed");
/* 136 */       textRenderer.render(" lines", sb, "Suppressed");
/*     */     } 
/* 138 */     renderSuffix(suffix, sb, textRenderer);
/* 139 */     textRenderer.render(lineSeparator, sb, "Text");
/*     */   }
/*     */ 
/*     */   
/*     */   private static void formatEntry(ExtendedStackTraceElement extStackTraceElement, StringBuilder sb, String prefix, TextRenderer textRenderer, String suffix, String lineSeparator) {
/* 144 */     textRenderer.render(prefix, sb, "Prefix");
/* 145 */     textRenderer.render("\tat ", sb, "At");
/* 146 */     extStackTraceElement.renderOn(sb, textRenderer);
/* 147 */     renderSuffix(suffix, sb, textRenderer);
/* 148 */     textRenderer.render(lineSeparator, sb, "Text");
/*     */   }
/*     */   
/*     */   private static boolean ignoreElement(StackTraceElement element, List<String> ignorePackages) {
/* 152 */     if (ignorePackages != null) {
/* 153 */       String className = element.getClassName();
/* 154 */       for (String pkg : ignorePackages) {
/* 155 */         if (className.startsWith(pkg)) {
/* 156 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 160 */     return false;
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
/*     */   static void formatExtendedStackTraceTo(ThrowableProxy src, StringBuilder sb, List<String> ignorePackages, TextRenderer textRenderer, String suffix, String lineSeparator) {
/* 174 */     textRenderer.render(src.getName(), sb, "Name");
/* 175 */     textRenderer.render(": ", sb, "NameMessageSeparator");
/* 176 */     textRenderer.render(src.getMessage(), sb, "Message");
/* 177 */     renderSuffix(suffix, sb, textRenderer);
/* 178 */     textRenderer.render(lineSeparator, sb, "Text");
/* 179 */     StackTraceElement[] causedTrace = (src.getThrowable() != null) ? src.getThrowable().getStackTrace() : null;
/* 180 */     formatElements(sb, "", 0, causedTrace, src.getExtendedStackTrace(), ignorePackages, textRenderer, suffix, lineSeparator);
/* 181 */     formatSuppressed(sb, "\t", src.getSuppressedProxies(), ignorePackages, textRenderer, suffix, lineSeparator);
/* 182 */     formatCause(sb, "", src.getCauseProxy(), ignorePackages, textRenderer, suffix, lineSeparator);
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
/*     */   static void formatCauseStackTrace(ThrowableProxy src, StringBuilder sb, List<String> ignorePackages, TextRenderer textRenderer, String suffix, String lineSeparator) {
/* 196 */     ThrowableProxy causeProxy = src.getCauseProxy();
/* 197 */     if (causeProxy != null) {
/* 198 */       formatWrapper(sb, causeProxy, ignorePackages, textRenderer, suffix, lineSeparator);
/* 199 */       sb.append("Wrapped by: ");
/* 200 */       renderSuffix(suffix, sb, textRenderer);
/*     */     } 
/* 202 */     renderOn(src, sb, textRenderer);
/* 203 */     renderSuffix(suffix, sb, textRenderer);
/* 204 */     textRenderer.render(lineSeparator, sb, "Text");
/* 205 */     formatElements(sb, "", 0, src.getStackTrace(), src.getExtendedStackTrace(), ignorePackages, textRenderer, suffix, lineSeparator);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void renderOn(ThrowableProxy src, StringBuilder output, TextRenderer textRenderer) {
/* 210 */     String msg = src.getMessage();
/* 211 */     textRenderer.render(src.getName(), output, "Name");
/* 212 */     if (msg != null) {
/* 213 */       textRenderer.render(": ", output, "NameMessageSeparator");
/* 214 */       textRenderer.render(msg, output, "Message");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\impl\ThrowableProxyRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */