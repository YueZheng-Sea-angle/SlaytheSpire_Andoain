/*     */ package org.apache.logging.log4j.core.appender.rolling;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.core.LifeCycle;
/*     */ import org.apache.logging.log4j.core.LifeCycle2;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "Policies", category = "Core", printObject = true)
/*     */ public final class CompositeTriggeringPolicy
/*     */   extends AbstractTriggeringPolicy
/*     */ {
/*     */   private final TriggeringPolicy[] triggeringPolicies;
/*     */   
/*     */   private CompositeTriggeringPolicy(TriggeringPolicy... triggeringPolicies) {
/*  39 */     this.triggeringPolicies = triggeringPolicies;
/*     */   }
/*     */   
/*     */   public TriggeringPolicy[] getTriggeringPolicies() {
/*  43 */     return this.triggeringPolicies;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initialize(RollingFileManager manager) {
/*  52 */     for (TriggeringPolicy triggeringPolicy : this.triggeringPolicies) {
/*  53 */       LOGGER.debug("Initializing triggering policy {}", triggeringPolicy.toString());
/*  54 */       triggeringPolicy.initialize(manager);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTriggeringEvent(LogEvent event) {
/*  65 */     for (TriggeringPolicy triggeringPolicy : this.triggeringPolicies) {
/*  66 */       if (triggeringPolicy.isTriggeringEvent(event)) {
/*  67 */         return true;
/*     */       }
/*     */     } 
/*  70 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static CompositeTriggeringPolicy createPolicy(@PluginElement("Policies") TriggeringPolicy... triggeringPolicy) {
/*  81 */     return new CompositeTriggeringPolicy(triggeringPolicy);
/*     */   }
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/*     */     int i;
/*  86 */     setStopping();
/*  87 */     boolean stopped = true;
/*  88 */     for (TriggeringPolicy triggeringPolicy : this.triggeringPolicies) {
/*  89 */       if (triggeringPolicy instanceof LifeCycle2) {
/*  90 */         stopped &= ((LifeCycle2)triggeringPolicy).stop(timeout, timeUnit);
/*  91 */       } else if (triggeringPolicy instanceof LifeCycle) {
/*  92 */         ((LifeCycle)triggeringPolicy).stop();
/*  93 */         i = stopped & true;
/*     */       } 
/*     */     } 
/*  96 */     setStopped();
/*  97 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 102 */     return "CompositeTriggeringPolicy(policies=" + Arrays.toString((Object[])this.triggeringPolicies) + ")";
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\CompositeTriggeringPolicy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */