/*     */ package org.apache.logging.log4j.core.config;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.core.Appender;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.AppenderLoggingException;
/*     */ import org.apache.logging.log4j.core.filter.AbstractFilterable;
/*     */ import org.apache.logging.log4j.core.filter.Filterable;
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AppenderControl
/*     */   extends AbstractFilterable
/*     */ {
/*  38 */   static final AppenderControl[] EMPTY_ARRAY = new AppenderControl[0];
/*     */   
/*  40 */   private final ThreadLocal<AppenderControl> recursive = new ThreadLocal<>();
/*     */ 
/*     */   
/*     */   private final Appender appender;
/*     */ 
/*     */   
/*     */   private final Level level;
/*     */   
/*     */   private final int intLevel;
/*     */   
/*     */   private final String appenderName;
/*     */ 
/*     */   
/*     */   public AppenderControl(Appender appender, Level level, Filter filter) {
/*  54 */     super(filter);
/*  55 */     this.appender = appender;
/*  56 */     this.appenderName = appender.getName();
/*  57 */     this.level = level;
/*  58 */     this.intLevel = (level == null) ? Level.ALL.intLevel() : level.intLevel();
/*  59 */     start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAppenderName() {
/*  68 */     return this.appenderName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Appender getAppender() {
/*  77 */     return this.appender;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void callAppender(LogEvent event) {
/*  86 */     if (shouldSkip(event)) {
/*     */       return;
/*     */     }
/*  89 */     callAppenderPreventRecursion(event);
/*     */   }
/*     */   
/*     */   private boolean shouldSkip(LogEvent event) {
/*  93 */     return (isFilteredByAppenderControl(event) || isFilteredByLevel(event) || isRecursiveCall());
/*     */   }
/*     */   
/*     */   @PerformanceSensitive
/*     */   private boolean isFilteredByAppenderControl(LogEvent event) {
/*  98 */     Filter filter = getFilter();
/*  99 */     return (filter != null && Filter.Result.DENY == filter.filter(event));
/*     */   }
/*     */   
/*     */   @PerformanceSensitive
/*     */   private boolean isFilteredByLevel(LogEvent event) {
/* 104 */     return (this.level != null && this.intLevel < event.getLevel().intLevel());
/*     */   }
/*     */   
/*     */   @PerformanceSensitive
/*     */   private boolean isRecursiveCall() {
/* 109 */     if (this.recursive.get() != null) {
/* 110 */       appenderErrorHandlerMessage("Recursive call to appender ");
/* 111 */       return true;
/*     */     } 
/* 113 */     return false;
/*     */   }
/*     */   
/*     */   private String appenderErrorHandlerMessage(String prefix) {
/* 117 */     String result = createErrorMsg(prefix);
/* 118 */     this.appender.getHandler().error(result);
/* 119 */     return result;
/*     */   }
/*     */   
/*     */   private void callAppenderPreventRecursion(LogEvent event) {
/*     */     try {
/* 124 */       this.recursive.set(this);
/* 125 */       callAppender0(event);
/*     */     } finally {
/* 127 */       this.recursive.set(null);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void callAppender0(LogEvent event) {
/* 132 */     ensureAppenderStarted();
/* 133 */     if (!isFilteredByAppender(event)) {
/* 134 */       tryCallAppender(event);
/*     */     }
/*     */   }
/*     */   
/*     */   private void ensureAppenderStarted() {
/* 139 */     if (!this.appender.isStarted()) {
/* 140 */       handleError("Attempted to append to non-started appender ");
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleError(String prefix) {
/* 145 */     String msg = appenderErrorHandlerMessage(prefix);
/* 146 */     if (!this.appender.ignoreExceptions()) {
/* 147 */       throw new AppenderLoggingException(msg);
/*     */     }
/*     */   }
/*     */   
/*     */   private String createErrorMsg(String prefix) {
/* 152 */     return prefix + this.appender.getName();
/*     */   }
/*     */   
/*     */   private boolean isFilteredByAppender(LogEvent event) {
/* 156 */     return (this.appender instanceof Filterable && ((Filterable)this.appender).isFiltered(event));
/*     */   }
/*     */   
/*     */   private void tryCallAppender(LogEvent event) {
/*     */     try {
/* 161 */       this.appender.append(event);
/* 162 */     } catch (RuntimeException error) {
/* 163 */       handleAppenderError(event, error);
/* 164 */     } catch (Throwable throwable) {
/* 165 */       handleAppenderError(event, (RuntimeException)new AppenderLoggingException(throwable));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleAppenderError(LogEvent event, RuntimeException ex) {
/* 170 */     this.appender.getHandler().error(createErrorMsg("An exception occurred processing Appender "), event, ex);
/* 171 */     if (!this.appender.ignoreExceptions()) {
/* 172 */       throw ex;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 183 */     if (obj == this) {
/* 184 */       return true;
/*     */     }
/* 186 */     if (!(obj instanceof AppenderControl)) {
/* 187 */       return false;
/*     */     }
/* 189 */     AppenderControl other = (AppenderControl)obj;
/* 190 */     return Objects.equals(this.appenderName, other.appenderName);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 195 */     return this.appenderName.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 200 */     return super.toString() + "[appender=" + this.appender + ", appenderName=" + this.appenderName + ", level=" + this.level + ", intLevel=" + this.intLevel + ", recursive=" + this.recursive + ", filter=" + 
/* 201 */       getFilter() + "]";
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\AppenderControl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */