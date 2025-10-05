/*     */ package org.lwjgl.opengl;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.lwjgl.LWJGLUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XRandR
/*     */ {
/*     */   private static Screen[] current;
/*     */   private static String primaryScreenIdentifier;
/*     */   private static Screen[] savedConfiguration;
/*     */   private static Map<String, Screen[]> screens;
/*  63 */   private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");
/*     */   
/*     */   private static void populate() {
/*  66 */     if (screens != null) {
/*     */       return;
/*     */     }
/*  69 */     screens = (Map)new HashMap<String, Screen>();
/*     */     
/*     */     try {
/*  72 */       Process p = Runtime.getRuntime().exec(new String[] { "xrandr", "-q" });
/*     */       
/*  74 */       List<Screen> currentList = new ArrayList<Screen>();
/*  75 */       List<Screen> possibles = new ArrayList<Screen>();
/*  76 */       String name = null;
/*     */ 
/*     */       
/*  79 */       int[] currentScreenPosition = new int[2];
/*     */       
/*  81 */       BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
/*     */       String line;
/*  83 */       while ((line = br.readLine()) != null) {
/*  84 */         line = line.trim();
/*  85 */         String[] sa = WHITESPACE_PATTERN.split(line);
/*     */         
/*  87 */         if ("connected".equals(sa[1])) {
/*     */           
/*  89 */           if (name != null) {
/*  90 */             screens.put(name, possibles.toArray(new Screen[possibles.size()]));
/*  91 */             possibles.clear();
/*     */           } 
/*  93 */           name = sa[0];
/*     */ 
/*     */           
/*  96 */           if ("primary".equals(sa[2])) {
/*  97 */             parseScreenHeader(currentScreenPosition, sa[3]);
/*     */             
/*  99 */             primaryScreenIdentifier = name; continue;
/*     */           } 
/* 101 */           parseScreenHeader(currentScreenPosition, sa[2]);
/*     */           continue;
/*     */         } 
/* 104 */         Matcher m = SCREEN_MODELINE_PATTERN.matcher(sa[0]);
/* 105 */         if (m.matches())
/*     */         {
/* 107 */           parseScreenModeline(possibles, currentList, name, Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), sa, currentScreenPosition);
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 116 */       screens.put(name, possibles.toArray(new Screen[possibles.size()]));
/*     */       
/* 118 */       current = currentList.<Screen>toArray(new Screen[currentList.size()]);
/*     */ 
/*     */       
/* 121 */       if (primaryScreenIdentifier == null) {
/* 122 */         long totalPixels = Long.MIN_VALUE;
/* 123 */         for (Screen screen : current) {
/* 124 */           if (1L * screen.width * screen.height > totalPixels) {
/* 125 */             primaryScreenIdentifier = screen.name;
/* 126 */             totalPixels = 1L * screen.width * screen.height;
/*     */           } 
/*     */         } 
/*     */       } 
/* 130 */     } catch (Throwable e) {
/* 131 */       LWJGLUtil.log("Exception in XRandR.populate(): " + e.getMessage());
/* 132 */       screens.clear();
/* 133 */       current = new Screen[0];
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Screen[] getConfiguration() {
/* 142 */     populate();
/*     */ 
/*     */     
/* 145 */     for (Screen screen : current) {
/* 146 */       if (screen.name.equals(primaryScreenIdentifier)) {
/* 147 */         return new Screen[] { screen };
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 152 */     return (Screen[])current.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setConfiguration(boolean disableOthers, Screen... screens) {
/* 162 */     if (screens.length == 0) {
/* 163 */       throw new IllegalArgumentException("Must specify at least one screen");
/*     */     }
/* 165 */     List<String> cmd = new ArrayList<String>();
/* 166 */     cmd.add("xrandr");
/*     */     
/* 168 */     if (disableOthers)
/*     */     {
/* 170 */       for (Screen screen : current) {
/* 171 */         boolean disable = true;
/* 172 */         for (Screen screen1 : screens) {
/* 173 */           if (screen1.name.equals(screen.name)) {
/* 174 */             disable = false;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/* 179 */         if (disable) {
/* 180 */           cmd.add("--output");
/* 181 */           cmd.add(screen.name);
/* 182 */           cmd.add("--off");
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 188 */     for (Screen screen : screens) {
/* 189 */       screen.getArgs(cmd);
/*     */     }
/*     */     try {
/* 192 */       Process p = Runtime.getRuntime().exec(cmd.<String>toArray(new String[cmd.size()]));
/*     */       
/* 194 */       BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
/*     */       String line;
/* 196 */       while ((line = br.readLine()) != null) {
/* 197 */         LWJGLUtil.log("Unexpected output from xrandr process: " + line);
/*     */       }
/* 199 */       current = screens;
/* 200 */     } catch (IOException e) {
/* 201 */       LWJGLUtil.log("XRandR exception in setConfiguration(): " + e.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void saveConfiguration() {
/* 211 */     populate();
/* 212 */     savedConfiguration = (Screen[])current.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void restoreConfiguration() {
/* 220 */     if (savedConfiguration != null) {
/* 221 */       setConfiguration(true, savedConfiguration);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String[] getScreenNames() {
/* 230 */     populate();
/* 231 */     return (String[])screens.keySet().toArray((Object[])new String[screens.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Screen[] getResolutions(String name) {
/* 241 */     populate();
/*     */     
/* 243 */     return (Screen[])((Screen[])screens.get(name)).clone();
/*     */   }
/*     */   
/* 246 */   private static final Pattern SCREEN_HEADER_PATTERN = Pattern.compile("^(\\d+)x(\\d+)[+](\\d+)[+](\\d+)$");
/* 247 */   private static final Pattern SCREEN_MODELINE_PATTERN = Pattern.compile("^(\\d+)x(\\d+)$");
/* 248 */   private static final Pattern FREQ_PATTERN = Pattern.compile("^(\\d+)[.](\\d+)(?:\\s*[*])?(?:\\s*[+])?$");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void parseScreenModeline(List<Screen> allModes, List<Screen> current, String name, int width, int height, String[] modeLine, int[] screenPosition) {
/* 260 */     for (int i = 1; i < modeLine.length; i++) {
/* 261 */       String freqS = modeLine[i];
/* 262 */       if (!"+".equals(freqS)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 268 */         Matcher m = FREQ_PATTERN.matcher(freqS);
/* 269 */         if (!m.matches()) {
/* 270 */           LWJGLUtil.log("Frequency match failed: " + Arrays.toString((Object[])modeLine));
/*     */           
/*     */           return;
/*     */         } 
/* 274 */         int freq = Integer.parseInt(m.group(1));
/*     */         
/* 276 */         Screen s = new Screen(name, width, height, freq, 0, 0);
/* 277 */         if (freqS.contains("*")) {
/*     */           
/* 279 */           current.add(new Screen(name, width, height, freq, screenPosition[0], screenPosition[1]));
/*     */           
/* 281 */           allModes.add(0, s);
/*     */         } else {
/*     */           
/* 284 */           allModes.add(s);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void parseScreenHeader(int[] screenPosition, String resPos) {
/* 296 */     Matcher m = SCREEN_HEADER_PATTERN.matcher(resPos);
/* 297 */     if (!m.matches()) {
/*     */       
/* 299 */       screenPosition[0] = 0;
/* 300 */       screenPosition[1] = 0;
/*     */       return;
/*     */     } 
/* 303 */     screenPosition[0] = Integer.parseInt(m.group(3));
/* 304 */     screenPosition[1] = Integer.parseInt(m.group(4));
/*     */   }
/*     */   
/*     */   static Screen DisplayModetoScreen(DisplayMode mode) {
/* 308 */     populate();
/* 309 */     Screen primary = findPrimary(current);
/* 310 */     return new Screen(primary.name, mode.getWidth(), mode.getHeight(), mode.getFrequency(), primary.xPos, primary.yPos);
/*     */   }
/*     */   
/*     */   static DisplayMode ScreentoDisplayMode(Screen... screens) {
/* 314 */     populate();
/* 315 */     Screen primary = findPrimary(screens);
/* 316 */     return new DisplayMode(primary.width, primary.height, 24, primary.freq);
/*     */   }
/*     */   
/*     */   private static Screen findPrimary(Screen... screens) {
/* 320 */     for (Screen screen : screens) {
/* 321 */       if (screen.name.equals(primaryScreenIdentifier)) {
/* 322 */         return screen;
/*     */       }
/*     */     } 
/*     */     
/* 326 */     return screens[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Screen
/*     */     implements Cloneable
/*     */   {
/*     */     public final String name;
/*     */ 
/*     */     
/*     */     public final int width;
/*     */ 
/*     */     
/*     */     public final int height;
/*     */ 
/*     */     
/*     */     public final int freq;
/*     */ 
/*     */     
/*     */     public int xPos;
/*     */ 
/*     */     
/*     */     public int yPos;
/*     */ 
/*     */ 
/*     */     
/*     */     Screen(String name, int width, int height, int freq, int xPos, int yPos) {
/* 355 */       this.name = name;
/* 356 */       this.width = width;
/* 357 */       this.height = height;
/* 358 */       this.freq = freq;
/* 359 */       this.xPos = xPos;
/* 360 */       this.yPos = yPos;
/*     */     }
/*     */     
/*     */     private void getArgs(List<String> argList) {
/* 364 */       argList.add("--output");
/* 365 */       argList.add(this.name);
/* 366 */       argList.add("--mode");
/* 367 */       argList.add(this.width + "x" + this.height);
/* 368 */       argList.add("--rate");
/* 369 */       argList.add(Integer.toString(this.freq));
/* 370 */       argList.add("--pos");
/* 371 */       argList.add(this.xPos + "x" + this.yPos);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 376 */       return this.name + " " + this.width + "x" + this.height + " @ " + this.xPos + "x" + this.yPos + " with " + this.freq + "Hz";
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\XRandR.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */