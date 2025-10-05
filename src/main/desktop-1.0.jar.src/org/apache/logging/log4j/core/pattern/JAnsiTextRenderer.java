/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.fusesource.jansi.Ansi;
/*     */ import org.fusesource.jansi.AnsiRenderer;
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
/*     */ public final class JAnsiTextRenderer
/*     */   implements TextRenderer
/*     */ {
/*     */   public static final Map<String, AnsiRenderer.Code[]> DefaultExceptionStyleMap;
/*     */   static final Map<String, AnsiRenderer.Code[]> DefaultMessageStyleMap;
/*     */   private static final Map<String, Map<String, AnsiRenderer.Code[]>> PrefedinedStyleMaps;
/*     */   private final String beginToken;
/*     */   private final int beginTokenLen;
/*     */   private final String endToken;
/*     */   private final int endTokenLen;
/*     */   private final Map<String, AnsiRenderer.Code[]> styleMap;
/*     */   
/*     */   private static void put(Map<String, AnsiRenderer.Code[]> map, String name, AnsiRenderer.Code... codes) {
/*  88 */     map.put(name, codes);
/*     */   }
/*     */   
/*     */   static {
/*  92 */     Map<String, Map<String, AnsiRenderer.Code[]>> tempPreDefs = new HashMap<>();
/*     */ 
/*     */ 
/*     */     
/*  96 */     Map<String, AnsiRenderer.Code[]> map = (Map)new HashMap<>();
/*  97 */     put(map, "Prefix", new AnsiRenderer.Code[] { AnsiRenderer.Code.WHITE });
/*  98 */     put(map, "Name", new AnsiRenderer.Code[] { AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.WHITE });
/*  99 */     put(map, "NameMessageSeparator", new AnsiRenderer.Code[] { AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.WHITE });
/* 100 */     put(map, "Message", new AnsiRenderer.Code[] { AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.WHITE, AnsiRenderer.Code.BOLD });
/* 101 */     put(map, "At", new AnsiRenderer.Code[] { AnsiRenderer.Code.WHITE });
/* 102 */     put(map, "CauseLabel", new AnsiRenderer.Code[] { AnsiRenderer.Code.WHITE });
/* 103 */     put(map, "Text", new AnsiRenderer.Code[] { AnsiRenderer.Code.WHITE });
/* 104 */     put(map, "More", new AnsiRenderer.Code[] { AnsiRenderer.Code.WHITE });
/* 105 */     put(map, "Suppressed", new AnsiRenderer.Code[] { AnsiRenderer.Code.WHITE });
/*     */     
/* 107 */     put(map, "StackTraceElement.ClassName", new AnsiRenderer.Code[] { AnsiRenderer.Code.YELLOW });
/* 108 */     put(map, "StackTraceElement.ClassMethodSeparator", new AnsiRenderer.Code[] { AnsiRenderer.Code.YELLOW });
/* 109 */     put(map, "StackTraceElement.MethodName", new AnsiRenderer.Code[] { AnsiRenderer.Code.YELLOW });
/* 110 */     put(map, "StackTraceElement.NativeMethod", new AnsiRenderer.Code[] { AnsiRenderer.Code.YELLOW });
/* 111 */     put(map, "StackTraceElement.FileName", new AnsiRenderer.Code[] { AnsiRenderer.Code.RED });
/* 112 */     put(map, "StackTraceElement.LineNumber", new AnsiRenderer.Code[] { AnsiRenderer.Code.RED });
/* 113 */     put(map, "StackTraceElement.Container", new AnsiRenderer.Code[] { AnsiRenderer.Code.RED });
/* 114 */     put(map, "StackTraceElement.ContainerSeparator", new AnsiRenderer.Code[] { AnsiRenderer.Code.WHITE });
/* 115 */     put(map, "StackTraceElement.UnknownSource", new AnsiRenderer.Code[] { AnsiRenderer.Code.RED });
/*     */     
/* 117 */     put(map, "ExtraClassInfo.Inexact", new AnsiRenderer.Code[] { AnsiRenderer.Code.YELLOW });
/* 118 */     put(map, "ExtraClassInfo.Container", new AnsiRenderer.Code[] { AnsiRenderer.Code.YELLOW });
/* 119 */     put(map, "ExtraClassInfo.ContainerSeparator", new AnsiRenderer.Code[] { AnsiRenderer.Code.YELLOW });
/* 120 */     put(map, "ExtraClassInfo.Location", new AnsiRenderer.Code[] { AnsiRenderer.Code.YELLOW });
/* 121 */     put(map, "ExtraClassInfo.Version", new AnsiRenderer.Code[] { AnsiRenderer.Code.YELLOW });
/*     */     
/* 123 */     DefaultExceptionStyleMap = Collections.unmodifiableMap((Map)map);
/* 124 */     tempPreDefs.put("Spock", DefaultExceptionStyleMap);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 129 */     map = (Map)new HashMap<>();
/* 130 */     put(map, "Prefix", new AnsiRenderer.Code[] { AnsiRenderer.Code.WHITE });
/* 131 */     put(map, "Name", new AnsiRenderer.Code[] { AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.YELLOW, AnsiRenderer.Code.BOLD });
/* 132 */     put(map, "NameMessageSeparator", new AnsiRenderer.Code[] { AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.YELLOW });
/* 133 */     put(map, "Message", new AnsiRenderer.Code[] { AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.WHITE, AnsiRenderer.Code.BOLD });
/* 134 */     put(map, "At", new AnsiRenderer.Code[] { AnsiRenderer.Code.WHITE });
/* 135 */     put(map, "CauseLabel", new AnsiRenderer.Code[] { AnsiRenderer.Code.WHITE });
/* 136 */     put(map, "Text", new AnsiRenderer.Code[] { AnsiRenderer.Code.WHITE });
/* 137 */     put(map, "More", new AnsiRenderer.Code[] { AnsiRenderer.Code.WHITE });
/* 138 */     put(map, "Suppressed", new AnsiRenderer.Code[] { AnsiRenderer.Code.WHITE });
/*     */     
/* 140 */     put(map, "StackTraceElement.ClassName", new AnsiRenderer.Code[] { AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.WHITE });
/* 141 */     put(map, "StackTraceElement.ClassMethodSeparator", new AnsiRenderer.Code[] { AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.YELLOW });
/* 142 */     put(map, "StackTraceElement.MethodName", new AnsiRenderer.Code[] { AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.YELLOW });
/* 143 */     put(map, "StackTraceElement.NativeMethod", new AnsiRenderer.Code[] { AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.YELLOW });
/* 144 */     put(map, "StackTraceElement.FileName", new AnsiRenderer.Code[] { AnsiRenderer.Code.RED });
/* 145 */     put(map, "StackTraceElement.LineNumber", new AnsiRenderer.Code[] { AnsiRenderer.Code.RED });
/* 146 */     put(map, "StackTraceElement.Container", new AnsiRenderer.Code[] { AnsiRenderer.Code.RED });
/* 147 */     put(map, "StackTraceElement.ContainerSeparator", new AnsiRenderer.Code[] { AnsiRenderer.Code.WHITE });
/* 148 */     put(map, "StackTraceElement.UnknownSource", new AnsiRenderer.Code[] { AnsiRenderer.Code.RED });
/*     */     
/* 150 */     put(map, "ExtraClassInfo.Inexact", new AnsiRenderer.Code[] { AnsiRenderer.Code.YELLOW });
/* 151 */     put(map, "ExtraClassInfo.Container", new AnsiRenderer.Code[] { AnsiRenderer.Code.WHITE });
/* 152 */     put(map, "ExtraClassInfo.ContainerSeparator", new AnsiRenderer.Code[] { AnsiRenderer.Code.WHITE });
/* 153 */     put(map, "ExtraClassInfo.Location", new AnsiRenderer.Code[] { AnsiRenderer.Code.YELLOW });
/* 154 */     put(map, "ExtraClassInfo.Version", new AnsiRenderer.Code[] { AnsiRenderer.Code.YELLOW });
/*     */     
/* 156 */     tempPreDefs.put("Kirk", (Map)Collections.unmodifiableMap((Map)map));
/*     */ 
/*     */     
/* 159 */     Map<String, AnsiRenderer.Code[]> temp = (Map)new HashMap<>();
/*     */     
/* 161 */     DefaultMessageStyleMap = Collections.unmodifiableMap((Map)temp);
/*     */     
/* 163 */     PrefedinedStyleMaps = Collections.unmodifiableMap(tempPreDefs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAnsiTextRenderer(String[] formats, Map<String, AnsiRenderer.Code[]> defaultStyleMap) {
/*     */     Map<String, AnsiRenderer.Code[]> map;
/* 173 */     String tempBeginToken = "@|";
/* 174 */     String tempEndToken = "|@";
/*     */     
/* 176 */     if (formats.length > 1) {
/* 177 */       String allStylesStr = formats[1];
/*     */       
/* 179 */       String[] allStyleAssignmentsArr = allStylesStr.split(" ");
/* 180 */       map = (Map)new HashMap<>(allStyleAssignmentsArr.length + defaultStyleMap.size());
/* 181 */       map.putAll((Map)defaultStyleMap);
/* 182 */       for (String styleAssignmentStr : allStyleAssignmentsArr) {
/* 183 */         String[] styleAssignmentArr = styleAssignmentStr.split("=");
/* 184 */         if (styleAssignmentArr.length != 2) {
/* 185 */           StatusLogger.getLogger().warn("{} parsing style \"{}\", expected format: StyleName=Code(,Code)*", 
/* 186 */               getClass().getSimpleName(), styleAssignmentStr);
/*     */         } else {
/* 188 */           String styleName = styleAssignmentArr[0];
/* 189 */           String codeListStr = styleAssignmentArr[1];
/* 190 */           String[] codeNames = codeListStr.split(",");
/* 191 */           if (codeNames.length == 0) {
/* 192 */             StatusLogger.getLogger().warn("{} parsing style \"{}\", expected format: StyleName=Code(,Code)*", 
/*     */                 
/* 194 */                 getClass().getSimpleName(), styleAssignmentStr);
/*     */           } else {
/* 196 */             String predefinedMapName; Map<String, AnsiRenderer.Code[]> predefinedMap; AnsiRenderer.Code[] codes; int i; switch (styleName) {
/*     */               case "BeginToken":
/* 198 */                 tempBeginToken = codeNames[0];
/*     */                 break;
/*     */               case "EndToken":
/* 201 */                 tempEndToken = codeNames[0];
/*     */                 break;
/*     */               case "StyleMapName":
/* 204 */                 predefinedMapName = codeNames[0];
/* 205 */                 predefinedMap = PrefedinedStyleMaps.get(predefinedMapName);
/* 206 */                 if (predefinedMap != null) {
/* 207 */                   map.putAll((Map)predefinedMap); break;
/*     */                 } 
/* 209 */                 StatusLogger.getLogger().warn("Unknown predefined map name {}, pick one of {}", predefinedMapName, null);
/*     */                 break;
/*     */ 
/*     */               
/*     */               default:
/* 214 */                 codes = new AnsiRenderer.Code[codeNames.length];
/* 215 */                 for (i = 0; i < codes.length; i++) {
/* 216 */                   codes[i] = toCode(codeNames[i]);
/*     */                 }
/* 218 */                 map.put(styleName, codes); break;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } else {
/* 224 */       map = defaultStyleMap;
/*     */     } 
/* 226 */     this.styleMap = map;
/* 227 */     this.beginToken = tempBeginToken;
/* 228 */     this.endToken = tempEndToken;
/* 229 */     this.beginTokenLen = tempBeginToken.length();
/* 230 */     this.endTokenLen = tempEndToken.length();
/*     */   }
/*     */   
/*     */   public Map<String, AnsiRenderer.Code[]> getStyleMap() {
/* 234 */     return this.styleMap;
/*     */   }
/*     */   
/*     */   private void render(Ansi ansi, AnsiRenderer.Code code) {
/* 238 */     if (code.isColor()) {
/* 239 */       if (code.isBackground()) {
/* 240 */         ansi.bg(code.getColor());
/*     */       } else {
/* 242 */         ansi.fg(code.getColor());
/*     */       } 
/* 244 */     } else if (code.isAttribute()) {
/* 245 */       ansi.a(code.getAttribute());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void render(Ansi ansi, AnsiRenderer.Code... codes) {
/* 250 */     for (AnsiRenderer.Code code : codes) {
/* 251 */       render(ansi, code);
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
/*     */   private String render(String text, String... names) {
/* 265 */     Ansi ansi = Ansi.ansi();
/* 266 */     for (String name : names) {
/* 267 */       AnsiRenderer.Code[] codes = this.styleMap.get(name);
/* 268 */       if (codes != null) {
/* 269 */         render(ansi, codes);
/*     */       } else {
/* 271 */         render(ansi, toCode(name));
/*     */       } 
/*     */     } 
/* 274 */     return ansi.a(text).reset().toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void render(String input, StringBuilder output, String styleName) throws IllegalArgumentException {
/* 281 */     output.append(render(input, new String[] { styleName }));
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(StringBuilder input, StringBuilder output) throws IllegalArgumentException {
/* 286 */     int i = 0;
/*     */ 
/*     */     
/*     */     while (true) {
/* 290 */       int j = input.indexOf(this.beginToken, i);
/* 291 */       if (j == -1) {
/* 292 */         if (i == 0) {
/* 293 */           output.append(input);
/*     */           return;
/*     */         } 
/* 296 */         output.append(input.substring(i, input.length()));
/*     */         return;
/*     */       } 
/* 299 */       output.append(input.substring(i, j));
/* 300 */       int k = input.indexOf(this.endToken, j);
/*     */       
/* 302 */       if (k == -1) {
/* 303 */         output.append(input);
/*     */         return;
/*     */       } 
/* 306 */       j += this.beginTokenLen;
/* 307 */       String spec = input.substring(j, k);
/*     */       
/* 309 */       String[] items = spec.split(" ", 2);
/* 310 */       if (items.length == 1) {
/* 311 */         output.append(input);
/*     */         return;
/*     */       } 
/* 314 */       String replacement = render(items[1], items[0].split(","));
/*     */       
/* 316 */       output.append(replacement);
/*     */       
/* 318 */       i = k + this.endTokenLen;
/*     */     } 
/*     */   }
/*     */   
/*     */   private AnsiRenderer.Code toCode(String name) {
/* 323 */     return AnsiRenderer.Code.valueOf(name.toUpperCase(Locale.ENGLISH));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 328 */     return "JAnsiMessageRenderer [beginToken=" + this.beginToken + ", beginTokenLen=" + this.beginTokenLen + ", endToken=" + this.endToken + ", endTokenLen=" + this.endTokenLen + ", styleMap=" + this.styleMap + "]";
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\JAnsiTextRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */