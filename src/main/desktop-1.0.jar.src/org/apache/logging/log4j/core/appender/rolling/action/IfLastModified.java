/*     */ package org.apache.logging.log4j.core.appender.rolling.action;
/*     */ 
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.nio.file.attribute.FileTime;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.util.Clock;
/*     */ import org.apache.logging.log4j.core.util.ClockFactory;
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
/*     */ @Plugin(name = "IfLastModified", category = "Core", printObject = true)
/*     */ public final class IfLastModified
/*     */   implements PathCondition
/*     */ {
/*  42 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*  43 */   private static final Clock CLOCK = ClockFactory.getClock();
/*     */   
/*     */   private final Duration age;
/*     */   private final PathCondition[] nestedConditions;
/*     */   
/*     */   private IfLastModified(Duration age, PathCondition[] nestedConditions) {
/*  49 */     this.age = Objects.<Duration>requireNonNull(age, "age");
/*  50 */     this.nestedConditions = PathCondition.copy(nestedConditions);
/*     */   }
/*     */   
/*     */   public Duration getAge() {
/*  54 */     return this.age;
/*     */   }
/*     */   
/*     */   public List<PathCondition> getNestedConditions() {
/*  58 */     return Collections.unmodifiableList(Arrays.asList(this.nestedConditions));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean accept(Path basePath, Path relativePath, BasicFileAttributes attrs) {
/*  69 */     FileTime fileTime = attrs.lastModifiedTime();
/*  70 */     long millis = fileTime.toMillis();
/*  71 */     long ageMillis = CLOCK.currentTimeMillis() - millis;
/*  72 */     boolean result = (ageMillis >= this.age.toMillis());
/*  73 */     String match = result ? ">=" : "<";
/*  74 */     String accept = result ? "ACCEPTED" : "REJECTED";
/*  75 */     LOGGER.trace("IfLastModified {}: {} ageMillis '{}' {} '{}'", accept, relativePath, Long.valueOf(ageMillis), match, this.age);
/*  76 */     if (result) {
/*  77 */       return IfAll.accept(this.nestedConditions, basePath, relativePath, attrs);
/*     */     }
/*  79 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void beforeFileTreeWalk() {
/*  89 */     IfAll.beforeFileTreeWalk(this.nestedConditions);
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
/*     */   @PluginFactory
/*     */   public static IfLastModified createAgeCondition(@PluginAttribute("age") Duration age, @PluginElement("PathConditions") PathCondition... nestedConditions) {
/* 105 */     return new IfLastModified(age, nestedConditions);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 110 */     String nested = (this.nestedConditions.length == 0) ? "" : (" AND " + Arrays.toString((Object[])this.nestedConditions));
/* 111 */     return "IfLastModified(age=" + this.age + nested + ")";
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\action\IfLastModified.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */