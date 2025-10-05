/*     */ package org.apache.logging.log4j;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.apache.logging.log4j.spi.StandardLevel;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Level
/*     */   implements Comparable<Level>, Serializable
/*     */ {
/*     */   public static final Level OFF;
/*     */   public static final Level FATAL;
/*     */   public static final Level ERROR;
/*     */   public static final Level WARN;
/*     */   public static final Level INFO;
/*     */   public static final Level DEBUG;
/*     */   public static final Level TRACE;
/*     */   public static final Level ALL;
/*     */   public static final String CATEGORY = "Level";
/*  93 */   private static final ConcurrentMap<String, Level> LEVELS = new ConcurrentHashMap<>();
/*     */   private static final long serialVersionUID = 1581082L;
/*     */   private final String name;
/*     */   
/*     */   static {
/*  98 */     OFF = new Level("OFF", StandardLevel.OFF.intLevel());
/*  99 */     FATAL = new Level("FATAL", StandardLevel.FATAL.intLevel());
/* 100 */     ERROR = new Level("ERROR", StandardLevel.ERROR.intLevel());
/* 101 */     WARN = new Level("WARN", StandardLevel.WARN.intLevel());
/* 102 */     INFO = new Level("INFO", StandardLevel.INFO.intLevel());
/* 103 */     DEBUG = new Level("DEBUG", StandardLevel.DEBUG.intLevel());
/* 104 */     TRACE = new Level("TRACE", StandardLevel.TRACE.intLevel());
/* 105 */     ALL = new Level("ALL", StandardLevel.ALL.intLevel());
/*     */   }
/*     */ 
/*     */   
/*     */   private final int intLevel;
/*     */   private final StandardLevel standardLevel;
/*     */   
/*     */   private Level(String name, int intLevel) {
/* 113 */     if (Strings.isEmpty(name)) {
/* 114 */       throw new IllegalArgumentException("Illegal null or empty Level name.");
/*     */     }
/* 116 */     if (intLevel < 0) {
/* 117 */       throw new IllegalArgumentException("Illegal Level int less than zero.");
/*     */     }
/* 119 */     this.name = name;
/* 120 */     this.intLevel = intLevel;
/* 121 */     this.standardLevel = StandardLevel.getStandardLevel(intLevel);
/* 122 */     if (LEVELS.putIfAbsent(name, this) != null) {
/* 123 */       throw new IllegalStateException("Level " + name + " has already been defined.");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int intLevel() {
/* 133 */     return this.intLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StandardLevel getStandardLevel() {
/* 142 */     return this.standardLevel;
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
/*     */   public boolean isInRange(Level minLevel, Level maxLevel) {
/* 155 */     return (this.intLevel >= minLevel.intLevel && this.intLevel <= maxLevel.intLevel);
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
/*     */   public boolean isLessSpecificThan(Level level) {
/* 173 */     return (this.intLevel >= level.intLevel);
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
/*     */   public boolean isMoreSpecificThan(Level level) {
/* 189 */     return (this.intLevel <= level.intLevel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Level clone() throws CloneNotSupportedException {
/* 196 */     throw new CloneNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(Level other) {
/* 202 */     return (this.intLevel < other.intLevel) ? -1 : ((this.intLevel > other.intLevel) ? 1 : 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 207 */     return (other instanceof Level && other == this);
/*     */   }
/*     */   
/*     */   public Class<Level> getDeclaringClass() {
/* 211 */     return Level.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 216 */     return this.name.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String name() {
/* 225 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 230 */     return this.name;
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
/*     */   public static Level forName(String name, int intValue) {
/* 242 */     Level level = LEVELS.get(name);
/* 243 */     if (level != null) {
/* 244 */       return level;
/*     */     }
/*     */     try {
/* 247 */       return new Level(name, intValue);
/* 248 */     } catch (IllegalStateException ex) {
/*     */       
/* 250 */       return LEVELS.get(name);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Level getLevel(String name) {
/* 261 */     return LEVELS.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Level toLevel(String sArg) {
/* 272 */     return toLevel(sArg, DEBUG);
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
/*     */   public static Level toLevel(String name, Level defaultLevel) {
/* 284 */     if (name == null) {
/* 285 */       return defaultLevel;
/*     */     }
/* 287 */     Level level = LEVELS.get(toUpperCase(name.trim()));
/* 288 */     return (level == null) ? defaultLevel : level;
/*     */   }
/*     */   
/*     */   private static String toUpperCase(String name) {
/* 292 */     return name.toUpperCase(Locale.ENGLISH);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Level[] values() {
/* 301 */     Collection<Level> values = LEVELS.values();
/* 302 */     return values.<Level>toArray(new Level[values.size()]);
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
/*     */   public static Level valueOf(String name) {
/* 314 */     Objects.requireNonNull(name, "No level name given.");
/* 315 */     String levelName = toUpperCase(name.trim());
/* 316 */     Level level = LEVELS.get(levelName);
/* 317 */     if (level != null) {
/* 318 */       return level;
/*     */     }
/* 320 */     throw new IllegalArgumentException("Unknown level constant [" + levelName + "].");
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
/*     */   public static <T extends Enum<T>> T valueOf(Class<T> enumType, String name) {
/* 337 */     return Enum.valueOf(enumType, name);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object readResolve() {
/* 342 */     return valueOf(this.name);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\Level.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */