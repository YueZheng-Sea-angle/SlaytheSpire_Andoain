/*     */ package org.apache.logging.log4j.core.filter;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.core.AbstractLifeCycle;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LifeCycle2;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.Logger;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.util.ObjectArrayIterator;
/*     */ import org.apache.logging.log4j.message.Message;
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
/*     */ @Plugin(name = "filters", category = "Core", printObject = true)
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public final class CompositeFilter
/*     */   extends AbstractLifeCycle
/*     */   implements Iterable<Filter>, Filter
/*     */ {
/*     */   private final Filter[] filters;
/*     */   
/*     */   private CompositeFilter() {
/*  50 */     this.filters = Filter.EMPTY_ARRAY;
/*     */   }
/*     */   
/*     */   private CompositeFilter(Filter[] filters) {
/*  54 */     this.filters = (filters == null) ? Filter.EMPTY_ARRAY : filters;
/*     */   }
/*     */   
/*     */   public CompositeFilter addFilter(Filter filter) {
/*  58 */     if (filter == null)
/*     */     {
/*  60 */       return this;
/*     */     }
/*  62 */     if (filter instanceof CompositeFilter) {
/*  63 */       int size = this.filters.length + ((CompositeFilter)filter).size();
/*  64 */       Filter[] arrayOfFilter = Arrays.<Filter>copyOf(this.filters, size);
/*  65 */       int index = this.filters.length;
/*  66 */       for (Filter currentFilter : ((CompositeFilter)filter).filters) {
/*  67 */         arrayOfFilter[index] = currentFilter;
/*     */       }
/*  69 */       return new CompositeFilter(arrayOfFilter);
/*     */     } 
/*  71 */     Filter[] copy = Arrays.<Filter>copyOf(this.filters, this.filters.length + 1);
/*  72 */     copy[this.filters.length] = filter;
/*  73 */     return new CompositeFilter(copy);
/*     */   }
/*     */   
/*     */   public CompositeFilter removeFilter(Filter filter) {
/*  77 */     if (filter == null)
/*     */     {
/*  79 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  84 */     List<Filter> filterList = new ArrayList<>(Arrays.asList(this.filters));
/*  85 */     if (filter instanceof CompositeFilter) {
/*  86 */       for (Filter currentFilter : ((CompositeFilter)filter).filters) {
/*  87 */         filterList.remove(currentFilter);
/*     */       }
/*     */     } else {
/*  90 */       filterList.remove(filter);
/*     */     } 
/*  92 */     return new CompositeFilter(filterList.<Filter>toArray(new Filter[this.filters.length - 1]));
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<Filter> iterator() {
/*  97 */     return (Iterator<Filter>)new ObjectArrayIterator((Object[])this.filters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public List<Filter> getFilters() {
/* 108 */     return Arrays.asList(this.filters);
/*     */   }
/*     */   
/*     */   public Filter[] getFiltersArray() {
/* 112 */     return this.filters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 121 */     return (this.filters.length == 0);
/*     */   }
/*     */   
/*     */   public int size() {
/* 125 */     return this.filters.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 130 */     setStarting();
/* 131 */     for (Filter filter : this.filters) {
/* 132 */       filter.start();
/*     */     }
/* 134 */     setStarted();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 139 */     setStopping();
/* 140 */     for (Filter filter : this.filters) {
/* 141 */       if (filter instanceof LifeCycle2) {
/* 142 */         ((LifeCycle2)filter).stop(timeout, timeUnit);
/*     */       } else {
/* 144 */         filter.stop();
/*     */       } 
/*     */     } 
/* 147 */     setStopped();
/* 148 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result getOnMismatch() {
/* 158 */     return Filter.Result.NEUTRAL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result getOnMatch() {
/* 168 */     return Filter.Result.NEUTRAL;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
/* 189 */     Filter.Result result = Filter.Result.NEUTRAL;
/* 190 */     for (int i = 0; i < this.filters.length; i++) {
/* 191 */       result = this.filters[i].filter(logger, level, marker, msg, params);
/* 192 */       if (result == Filter.Result.ACCEPT || result == Filter.Result.DENY) {
/* 193 */         return result;
/*     */       }
/*     */     } 
/* 196 */     return result;
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
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0) {
/* 216 */     Filter.Result result = Filter.Result.NEUTRAL;
/* 217 */     for (int i = 0; i < this.filters.length; i++) {
/* 218 */       result = this.filters[i].filter(logger, level, marker, msg, p0);
/* 219 */       if (result == Filter.Result.ACCEPT || result == Filter.Result.DENY) {
/* 220 */         return result;
/*     */       }
/*     */     } 
/* 223 */     return result;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1) {
/* 244 */     Filter.Result result = Filter.Result.NEUTRAL;
/* 245 */     for (int i = 0; i < this.filters.length; i++) {
/* 246 */       result = this.filters[i].filter(logger, level, marker, msg, p0, p1);
/* 247 */       if (result == Filter.Result.ACCEPT || result == Filter.Result.DENY) {
/* 248 */         return result;
/*     */       }
/*     */     } 
/* 251 */     return result;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2) {
/* 273 */     Filter.Result result = Filter.Result.NEUTRAL;
/* 274 */     for (int i = 0; i < this.filters.length; i++) {
/* 275 */       result = this.filters[i].filter(logger, level, marker, msg, p0, p1, p2);
/* 276 */       if (result == Filter.Result.ACCEPT || result == Filter.Result.DENY) {
/* 277 */         return result;
/*     */       }
/*     */     } 
/* 280 */     return result;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3) {
/* 303 */     Filter.Result result = Filter.Result.NEUTRAL;
/* 304 */     for (int i = 0; i < this.filters.length; i++) {
/* 305 */       result = this.filters[i].filter(logger, level, marker, msg, p0, p1, p2, p3);
/* 306 */       if (result == Filter.Result.ACCEPT || result == Filter.Result.DENY) {
/* 307 */         return result;
/*     */       }
/*     */     } 
/* 310 */     return result;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 335 */     Filter.Result result = Filter.Result.NEUTRAL;
/* 336 */     for (int i = 0; i < this.filters.length; i++) {
/* 337 */       result = this.filters[i].filter(logger, level, marker, msg, p0, p1, p2, p3, p4);
/* 338 */       if (result == Filter.Result.ACCEPT || result == Filter.Result.DENY) {
/* 339 */         return result;
/*     */       }
/*     */     } 
/* 342 */     return result;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 368 */     Filter.Result result = Filter.Result.NEUTRAL;
/* 369 */     for (int i = 0; i < this.filters.length; i++) {
/* 370 */       result = this.filters[i].filter(logger, level, marker, msg, p0, p1, p2, p3, p4, p5);
/* 371 */       if (result == Filter.Result.ACCEPT || result == Filter.Result.DENY) {
/* 372 */         return result;
/*     */       }
/*     */     } 
/* 375 */     return result;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 402 */     Filter.Result result = Filter.Result.NEUTRAL;
/* 403 */     for (int i = 0; i < this.filters.length; i++) {
/* 404 */       result = this.filters[i].filter(logger, level, marker, msg, p0, p1, p2, p3, p4, p5, p6);
/* 405 */       if (result == Filter.Result.ACCEPT || result == Filter.Result.DENY) {
/* 406 */         return result;
/*     */       }
/*     */     } 
/* 409 */     return result;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 438 */     Filter.Result result = Filter.Result.NEUTRAL;
/* 439 */     for (int i = 0; i < this.filters.length; i++) {
/* 440 */       result = this.filters[i].filter(logger, level, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7);
/* 441 */       if (result == Filter.Result.ACCEPT || result == Filter.Result.DENY) {
/* 442 */         return result;
/*     */       }
/*     */     } 
/* 445 */     return result;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 475 */     Filter.Result result = Filter.Result.NEUTRAL;
/* 476 */     for (int i = 0; i < this.filters.length; i++) {
/* 477 */       result = this.filters[i].filter(logger, level, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/* 478 */       if (result == Filter.Result.ACCEPT || result == Filter.Result.DENY) {
/* 479 */         return result;
/*     */       }
/*     */     } 
/* 482 */     return result;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 513 */     Filter.Result result = Filter.Result.NEUTRAL;
/* 514 */     for (int i = 0; i < this.filters.length; i++) {
/* 515 */       result = this.filters[i].filter(logger, level, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/* 516 */       if (result == Filter.Result.ACCEPT || result == Filter.Result.DENY) {
/* 517 */         return result;
/*     */       }
/*     */     } 
/* 520 */     return result;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
/* 541 */     Filter.Result result = Filter.Result.NEUTRAL;
/* 542 */     for (int i = 0; i < this.filters.length; i++) {
/* 543 */       result = this.filters[i].filter(logger, level, marker, msg, t);
/* 544 */       if (result == Filter.Result.ACCEPT || result == Filter.Result.DENY) {
/* 545 */         return result;
/*     */       }
/*     */     } 
/* 548 */     return result;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
/* 569 */     Filter.Result result = Filter.Result.NEUTRAL;
/* 570 */     for (int i = 0; i < this.filters.length; i++) {
/* 571 */       result = this.filters[i].filter(logger, level, marker, msg, t);
/* 572 */       if (result == Filter.Result.ACCEPT || result == Filter.Result.DENY) {
/* 573 */         return result;
/*     */       }
/*     */     } 
/* 576 */     return result;
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
/*     */   public Filter.Result filter(LogEvent event) {
/* 588 */     Filter.Result result = Filter.Result.NEUTRAL;
/* 589 */     for (int i = 0; i < this.filters.length; i++) {
/* 590 */       result = this.filters[i].filter(event);
/* 591 */       if (result == Filter.Result.ACCEPT || result == Filter.Result.DENY) {
/* 592 */         return result;
/*     */       }
/*     */     } 
/* 595 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 600 */     StringBuilder sb = new StringBuilder();
/* 601 */     for (int i = 0; i < this.filters.length; i++) {
/* 602 */       if (sb.length() == 0) {
/* 603 */         sb.append('{');
/*     */       } else {
/* 605 */         sb.append(", ");
/*     */       } 
/* 607 */       sb.append(this.filters[i].toString());
/*     */     } 
/* 609 */     if (sb.length() > 0) {
/* 610 */       sb.append('}');
/*     */     }
/* 612 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static CompositeFilter createFilters(@PluginElement("Filters") Filter[] filters) {
/* 624 */     return new CompositeFilter(filters);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\filter\CompositeFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */