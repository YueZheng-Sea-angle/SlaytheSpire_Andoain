/*     */ package org.apache.logging.log4j.core.impl;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.core.pattern.PlainTextRenderer;
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
/*     */ public final class ExtendedStackTraceElement
/*     */   implements Serializable
/*     */ {
/*  38 */   static final ExtendedStackTraceElement[] EMPTY_ARRAY = new ExtendedStackTraceElement[0];
/*     */   
/*     */   private static final long serialVersionUID = -2171069569241280505L;
/*     */   
/*     */   private final ExtendedClassInfo extraClassInfo;
/*     */   
/*     */   private final StackTraceElement stackTraceElement;
/*     */ 
/*     */   
/*     */   public ExtendedStackTraceElement(StackTraceElement stackTraceElement, ExtendedClassInfo extraClassInfo) {
/*  48 */     this.stackTraceElement = stackTraceElement;
/*  49 */     this.extraClassInfo = extraClassInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExtendedStackTraceElement(String declaringClass, String methodName, String fileName, int lineNumber, boolean exact, String location, String version) {
/*  57 */     this(new StackTraceElement(declaringClass, methodName, fileName, lineNumber), new ExtendedClassInfo(exact, location, version));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  63 */     if (this == obj) {
/*  64 */       return true;
/*     */     }
/*  66 */     if (obj == null) {
/*  67 */       return false;
/*     */     }
/*  69 */     if (!(obj instanceof ExtendedStackTraceElement)) {
/*  70 */       return false;
/*     */     }
/*  72 */     ExtendedStackTraceElement other = (ExtendedStackTraceElement)obj;
/*  73 */     if (!Objects.equals(this.extraClassInfo, other.extraClassInfo)) {
/*  74 */       return false;
/*     */     }
/*  76 */     if (!Objects.equals(this.stackTraceElement, other.stackTraceElement)) {
/*  77 */       return false;
/*     */     }
/*  79 */     return true;
/*     */   }
/*     */   
/*     */   public String getClassName() {
/*  83 */     return this.stackTraceElement.getClassName();
/*     */   }
/*     */   
/*     */   public boolean getExact() {
/*  87 */     return this.extraClassInfo.getExact();
/*     */   }
/*     */   
/*     */   public ExtendedClassInfo getExtraClassInfo() {
/*  91 */     return this.extraClassInfo;
/*     */   }
/*     */   
/*     */   public String getFileName() {
/*  95 */     return this.stackTraceElement.getFileName();
/*     */   }
/*     */   
/*     */   public int getLineNumber() {
/*  99 */     return this.stackTraceElement.getLineNumber();
/*     */   }
/*     */   
/*     */   public String getLocation() {
/* 103 */     return this.extraClassInfo.getLocation();
/*     */   }
/*     */   
/*     */   public String getMethodName() {
/* 107 */     return this.stackTraceElement.getMethodName();
/*     */   }
/*     */   
/*     */   public StackTraceElement getStackTraceElement() {
/* 111 */     return this.stackTraceElement;
/*     */   }
/*     */   
/*     */   public String getVersion() {
/* 115 */     return this.extraClassInfo.getVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 120 */     return Objects.hash(new Object[] { this.extraClassInfo, this.stackTraceElement });
/*     */   }
/*     */   
/*     */   public boolean isNativeMethod() {
/* 124 */     return this.stackTraceElement.isNativeMethod();
/*     */   }
/*     */   
/*     */   void renderOn(StringBuilder output, TextRenderer textRenderer) {
/* 128 */     render(this.stackTraceElement, output, textRenderer);
/* 129 */     textRenderer.render(" ", output, "Text");
/* 130 */     this.extraClassInfo.renderOn(output, textRenderer);
/*     */   }
/*     */   
/*     */   private void render(StackTraceElement stElement, StringBuilder output, TextRenderer textRenderer) {
/* 134 */     String fileName = stElement.getFileName();
/* 135 */     int lineNumber = stElement.getLineNumber();
/* 136 */     textRenderer.render(getClassName(), output, "StackTraceElement.ClassName");
/* 137 */     textRenderer.render(".", output, "StackTraceElement.ClassMethodSeparator");
/* 138 */     textRenderer.render(stElement.getMethodName(), output, "StackTraceElement.MethodName");
/* 139 */     if (stElement.isNativeMethod()) {
/* 140 */       textRenderer.render("(Native Method)", output, "StackTraceElement.NativeMethod");
/* 141 */     } else if (fileName != null && lineNumber >= 0) {
/* 142 */       textRenderer.render("(", output, "StackTraceElement.Container");
/* 143 */       textRenderer.render(fileName, output, "StackTraceElement.FileName");
/* 144 */       textRenderer.render(":", output, "StackTraceElement.ContainerSeparator");
/* 145 */       textRenderer.render(Integer.toString(lineNumber), output, "StackTraceElement.LineNumber");
/* 146 */       textRenderer.render(")", output, "StackTraceElement.Container");
/* 147 */     } else if (fileName != null) {
/* 148 */       textRenderer.render("(", output, "StackTraceElement.Container");
/* 149 */       textRenderer.render(fileName, output, "StackTraceElement.FileName");
/* 150 */       textRenderer.render(")", output, "StackTraceElement.Container");
/*     */     } else {
/* 152 */       textRenderer.render("(", output, "StackTraceElement.Container");
/* 153 */       textRenderer.render("Unknown Source", output, "StackTraceElement.UnknownSource");
/* 154 */       textRenderer.render(")", output, "StackTraceElement.Container");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 160 */     StringBuilder sb = new StringBuilder();
/* 161 */     renderOn(sb, (TextRenderer)PlainTextRenderer.getInstance());
/* 162 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\impl\ExtendedStackTraceElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */