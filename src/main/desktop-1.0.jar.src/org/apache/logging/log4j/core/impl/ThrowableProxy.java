/*     */ package org.apache.logging.log4j.core.impl;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.Stack;
/*     */ import org.apache.logging.log4j.core.pattern.PlainTextRenderer;
/*     */ import org.apache.logging.log4j.core.pattern.TextRenderer;
/*     */ import org.apache.logging.log4j.util.StackLocatorUtil;
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
/*     */ public class ThrowableProxy
/*     */   implements Serializable
/*     */ {
/*  52 */   static final ThrowableProxy[] EMPTY_ARRAY = new ThrowableProxy[0];
/*     */   
/*     */   private static final char EOL = '\n';
/*     */   
/*  56 */   private static final String EOL_STR = String.valueOf('\n');
/*     */ 
/*     */   
/*     */   private static final long serialVersionUID = -2752771578252251910L;
/*     */ 
/*     */   
/*     */   private final ThrowableProxy causeProxy;
/*     */ 
/*     */   
/*     */   private int commonElementCount;
/*     */   
/*     */   private final ExtendedStackTraceElement[] extendedStackTrace;
/*     */   
/*     */   private final String localizedMessage;
/*     */   
/*     */   private final String message;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final ThrowableProxy[] suppressedProxies;
/*     */   
/*     */   private final transient Throwable throwable;
/*     */ 
/*     */   
/*     */   ThrowableProxy() {
/*  81 */     this.throwable = null;
/*  82 */     this.name = null;
/*  83 */     this.extendedStackTrace = ExtendedStackTraceElement.EMPTY_ARRAY;
/*  84 */     this.causeProxy = null;
/*  85 */     this.message = null;
/*  86 */     this.localizedMessage = null;
/*  87 */     this.suppressedProxies = EMPTY_ARRAY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThrowableProxy(Throwable throwable) {
/*  96 */     this(throwable, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ThrowableProxy(Throwable throwable, Set<Throwable> visited) {
/* 106 */     this.throwable = throwable;
/* 107 */     this.name = throwable.getClass().getName();
/* 108 */     this.message = throwable.getMessage();
/* 109 */     this.localizedMessage = throwable.getLocalizedMessage();
/* 110 */     Map<String, ThrowableProxyHelper.CacheEntry> map = new HashMap<>();
/* 111 */     Stack<Class<?>> stack = StackLocatorUtil.getCurrentStackTrace();
/* 112 */     this.extendedStackTrace = ThrowableProxyHelper.toExtendedStackTrace(this, stack, map, null, throwable.getStackTrace());
/* 113 */     Throwable throwableCause = throwable.getCause();
/* 114 */     Set<Throwable> causeVisited = new HashSet<>(1);
/* 115 */     this.causeProxy = (throwableCause == null) ? null : new ThrowableProxy(throwable, stack, map, throwableCause, visited, causeVisited);
/*     */     
/* 117 */     this.suppressedProxies = ThrowableProxyHelper.toSuppressedProxies(throwable, visited);
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
/*     */   private ThrowableProxy(Throwable parent, Stack<Class<?>> stack, Map<String, ThrowableProxyHelper.CacheEntry> map, Throwable cause, Set<Throwable> suppressedVisited, Set<Throwable> causeVisited) {
/* 134 */     causeVisited.add(cause);
/* 135 */     this.throwable = cause;
/* 136 */     this.name = cause.getClass().getName();
/* 137 */     this.message = this.throwable.getMessage();
/* 138 */     this.localizedMessage = this.throwable.getLocalizedMessage();
/* 139 */     this.extendedStackTrace = ThrowableProxyHelper.toExtendedStackTrace(this, stack, map, parent.getStackTrace(), cause.getStackTrace());
/* 140 */     Throwable causeCause = cause.getCause();
/* 141 */     this.causeProxy = (causeCause == null || causeVisited.contains(causeCause)) ? null : new ThrowableProxy(parent, stack, map, causeCause, suppressedVisited, causeVisited);
/*     */     
/* 143 */     this.suppressedProxies = ThrowableProxyHelper.toSuppressedProxies(cause, suppressedVisited);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 148 */     if (this == obj) {
/* 149 */       return true;
/*     */     }
/* 151 */     if (obj == null) {
/* 152 */       return false;
/*     */     }
/* 154 */     if (getClass() != obj.getClass()) {
/* 155 */       return false;
/*     */     }
/* 157 */     ThrowableProxy other = (ThrowableProxy)obj;
/* 158 */     if (!Objects.equals(this.causeProxy, other.causeProxy)) {
/* 159 */       return false;
/*     */     }
/* 161 */     if (this.commonElementCount != other.commonElementCount) {
/* 162 */       return false;
/*     */     }
/* 164 */     if (!Objects.equals(this.name, other.name)) {
/* 165 */       return false;
/*     */     }
/* 167 */     if (!Arrays.equals((Object[])this.extendedStackTrace, (Object[])other.extendedStackTrace)) {
/* 168 */       return false;
/*     */     }
/* 170 */     if (!Arrays.equals((Object[])this.suppressedProxies, (Object[])other.suppressedProxies)) {
/* 171 */       return false;
/*     */     }
/* 173 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void formatWrapper(StringBuilder sb, ThrowableProxy cause, String suffix) {
/* 183 */     formatWrapper(sb, cause, null, (TextRenderer)PlainTextRenderer.getInstance(), suffix);
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
/*     */   public void formatWrapper(StringBuilder sb, ThrowableProxy cause, List<String> ignorePackages, String suffix) {
/* 195 */     formatWrapper(sb, cause, ignorePackages, (TextRenderer)PlainTextRenderer.getInstance(), suffix);
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
/*     */   public void formatWrapper(StringBuilder sb, ThrowableProxy cause, List<String> ignorePackages, TextRenderer textRenderer, String suffix) {
/* 209 */     formatWrapper(sb, cause, ignorePackages, textRenderer, suffix, EOL_STR);
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
/*     */   public void formatWrapper(StringBuilder sb, ThrowableProxy cause, List<String> ignorePackages, TextRenderer textRenderer, String suffix, String lineSeparator) {
/* 224 */     ThrowableProxyRenderer.formatWrapper(sb, cause, ignorePackages, textRenderer, suffix, lineSeparator);
/*     */   }
/*     */   
/*     */   public ThrowableProxy getCauseProxy() {
/* 228 */     return this.causeProxy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCauseStackTraceAsString(String suffix) {
/* 238 */     return getCauseStackTraceAsString(null, (TextRenderer)PlainTextRenderer.getInstance(), suffix, EOL_STR);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCauseStackTraceAsString(List<String> packages, String suffix) {
/* 249 */     return getCauseStackTraceAsString(packages, (TextRenderer)PlainTextRenderer.getInstance(), suffix, EOL_STR);
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
/*     */   public String getCauseStackTraceAsString(List<String> ignorePackages, TextRenderer textRenderer, String suffix) {
/* 261 */     return getCauseStackTraceAsString(ignorePackages, textRenderer, suffix, EOL_STR);
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
/*     */   public String getCauseStackTraceAsString(List<String> ignorePackages, TextRenderer textRenderer, String suffix, String lineSeparator) {
/* 274 */     StringBuilder sb = new StringBuilder();
/* 275 */     ThrowableProxyRenderer.formatCauseStackTrace(this, sb, ignorePackages, textRenderer, suffix, lineSeparator);
/* 276 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCommonElementCount() {
/* 286 */     return this.commonElementCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setCommonElementCount(int value) {
/* 297 */     this.commonElementCount = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExtendedStackTraceElement[] getExtendedStackTrace() {
/* 306 */     return this.extendedStackTrace;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getExtendedStackTraceAsString() {
/* 315 */     return getExtendedStackTraceAsString(null, (TextRenderer)PlainTextRenderer.getInstance(), "", EOL_STR);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getExtendedStackTraceAsString(String suffix) {
/* 325 */     return getExtendedStackTraceAsString(null, (TextRenderer)PlainTextRenderer.getInstance(), suffix, EOL_STR);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getExtendedStackTraceAsString(List<String> ignorePackages, String suffix) {
/* 336 */     return getExtendedStackTraceAsString(ignorePackages, (TextRenderer)PlainTextRenderer.getInstance(), suffix, EOL_STR);
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
/*     */   public String getExtendedStackTraceAsString(List<String> ignorePackages, TextRenderer textRenderer, String suffix) {
/* 348 */     return getExtendedStackTraceAsString(ignorePackages, textRenderer, suffix, EOL_STR);
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
/*     */   public String getExtendedStackTraceAsString(List<String> ignorePackages, TextRenderer textRenderer, String suffix, String lineSeparator) {
/* 361 */     StringBuilder sb = new StringBuilder(1024);
/* 362 */     formatExtendedStackTraceTo(sb, ignorePackages, textRenderer, suffix, lineSeparator);
/* 363 */     return sb.toString();
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
/*     */   public void formatExtendedStackTraceTo(StringBuilder sb, List<String> ignorePackages, TextRenderer textRenderer, String suffix, String lineSeparator) {
/* 376 */     ThrowableProxyRenderer.formatExtendedStackTraceTo(this, sb, ignorePackages, textRenderer, suffix, lineSeparator);
/*     */   }
/*     */   
/*     */   public String getLocalizedMessage() {
/* 380 */     return this.localizedMessage;
/*     */   }
/*     */   
/*     */   public String getMessage() {
/* 384 */     return this.message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 393 */     return this.name;
/*     */   }
/*     */   
/*     */   public StackTraceElement[] getStackTrace() {
/* 397 */     return (this.throwable == null) ? null : this.throwable.getStackTrace();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThrowableProxy[] getSuppressedProxies() {
/* 406 */     return this.suppressedProxies;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSuppressedStackTrace(String suffix) {
/* 416 */     ThrowableProxy[] suppressed = getSuppressedProxies();
/* 417 */     if (suppressed == null || suppressed.length == 0) {
/* 418 */       return "";
/*     */     }
/* 420 */     StringBuilder sb = (new StringBuilder("Suppressed Stack Trace Elements:")).append('\n');
/* 421 */     for (ThrowableProxy proxy : suppressed) {
/* 422 */       sb.append(proxy.getExtendedStackTraceAsString(suffix));
/*     */     }
/* 424 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getThrowable() {
/* 433 */     return this.throwable;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 438 */     int prime = 31;
/* 439 */     int result = 1;
/* 440 */     result = 31 * result + ((this.causeProxy == null) ? 0 : this.causeProxy.hashCode());
/* 441 */     result = 31 * result + this.commonElementCount;
/* 442 */     result = 31 * result + ((this.extendedStackTrace == null) ? 0 : Arrays.hashCode((Object[])this.extendedStackTrace));
/* 443 */     result = 31 * result + ((this.suppressedProxies == null) ? 0 : Arrays.hashCode((Object[])this.suppressedProxies));
/* 444 */     result = 31 * result + ((this.name == null) ? 0 : this.name.hashCode());
/* 445 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 450 */     String msg = this.message;
/* 451 */     return (msg != null) ? (this.name + ": " + msg) : this.name;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\impl\ThrowableProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */