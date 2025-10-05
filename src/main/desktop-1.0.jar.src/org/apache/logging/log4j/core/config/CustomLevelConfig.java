/*     */ package org.apache.logging.log4j.core.config;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "CustomLevel", category = "Core", printObject = true)
/*     */ public final class CustomLevelConfig
/*     */ {
/*  37 */   static final CustomLevelConfig[] EMPTY_ARRAY = new CustomLevelConfig[0];
/*     */   
/*     */   private final String levelName;
/*     */   private final int intLevel;
/*     */   
/*     */   private CustomLevelConfig(String levelName, int intLevel) {
/*  43 */     this.levelName = Objects.<String>requireNonNull(levelName, "levelName is null");
/*  44 */     this.intLevel = intLevel;
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
/*     */   @PluginFactory
/*     */   public static CustomLevelConfig createLevel(@PluginAttribute("name") String levelName, @PluginAttribute("intLevel") int intLevel) {
/*  61 */     StatusLogger.getLogger().debug("Creating CustomLevel(name='{}', intValue={})", levelName, Integer.valueOf(intLevel));
/*  62 */     Level.forName(levelName, intLevel);
/*  63 */     return new CustomLevelConfig(levelName, intLevel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLevelName() {
/*  72 */     return this.levelName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIntLevel() {
/*  82 */     return this.intLevel;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  87 */     return this.intLevel ^ this.levelName.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/*  92 */     if (this == object) {
/*  93 */       return true;
/*     */     }
/*  95 */     if (!(object instanceof CustomLevelConfig)) {
/*  96 */       return false;
/*     */     }
/*  98 */     CustomLevelConfig other = (CustomLevelConfig)object;
/*  99 */     return (this.intLevel == other.intLevel && this.levelName.equals(other.levelName));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 104 */     return "CustomLevel[name=" + this.levelName + ", intLevel=" + this.intLevel + "]";
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\CustomLevelConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */