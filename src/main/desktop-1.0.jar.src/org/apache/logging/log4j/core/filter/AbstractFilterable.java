/*     */ package org.apache.logging.log4j.core.filter;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.core.AbstractLifeCycle;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LifeCycle2;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractFilterable
/*     */   extends AbstractLifeCycle
/*     */   implements Filterable
/*     */ {
/*     */   private volatile Filter filter;
/*     */   @PluginElement("Properties")
/*     */   private final Property[] propertyArray;
/*     */   
/*     */   public static abstract class Builder<B extends Builder<B>>
/*     */   {
/*     */     @PluginElement("Filter")
/*     */     private Filter filter;
/*     */     @PluginElement("Properties")
/*     */     private Property[] propertyArray;
/*     */     
/*     */     public B asBuilder() {
/*  51 */       return (B)this;
/*     */     }
/*     */     
/*     */     public Filter getFilter() {
/*  55 */       return this.filter;
/*     */     }
/*     */     
/*     */     public Property[] getPropertyArray() {
/*  59 */       return this.propertyArray;
/*     */     }
/*     */     
/*     */     public B setFilter(Filter filter) {
/*  63 */       this.filter = filter;
/*  64 */       return asBuilder();
/*     */     }
/*     */     
/*     */     public B setPropertyArray(Property[] properties) {
/*  68 */       this.propertyArray = properties;
/*  69 */       return asBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public B withFilter(Filter filter) {
/*  81 */       return setFilter(filter);
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
/*     */   protected AbstractFilterable() {
/*  95 */     this((Filter)null, Property.EMPTY_ARRAY);
/*     */   }
/*     */   
/*     */   protected AbstractFilterable(Filter filter) {
/*  99 */     this(filter, Property.EMPTY_ARRAY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractFilterable(Filter filter, Property[] propertyArray) {
/* 106 */     this.filter = filter;
/* 107 */     this.propertyArray = (propertyArray == null) ? Property.EMPTY_ARRAY : propertyArray;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void addFilter(Filter filter) {
/* 116 */     if (filter == null) {
/*     */       return;
/*     */     }
/* 119 */     if (this.filter == null) {
/* 120 */       this.filter = filter;
/* 121 */     } else if (this.filter instanceof CompositeFilter) {
/* 122 */       this.filter = ((CompositeFilter)this.filter).addFilter(filter);
/*     */     } else {
/* 124 */       Filter[] filters = { this.filter, filter };
/* 125 */       this.filter = CompositeFilter.createFilters(filters);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter getFilter() {
/* 135 */     return this.filter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasFilter() {
/* 144 */     return (this.filter != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFiltered(LogEvent event) {
/* 154 */     return (this.filter != null && this.filter.filter(event) == Filter.Result.DENY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void removeFilter(Filter filter) {
/* 163 */     if (this.filter == null || filter == null) {
/*     */       return;
/*     */     }
/* 166 */     if (this.filter == filter || this.filter.equals(filter)) {
/* 167 */       this.filter = null;
/* 168 */     } else if (this.filter instanceof CompositeFilter) {
/* 169 */       CompositeFilter composite = (CompositeFilter)this.filter;
/* 170 */       composite = composite.removeFilter(filter);
/* 171 */       if (composite.size() > 1) {
/* 172 */         this.filter = composite;
/* 173 */       } else if (composite.size() == 1) {
/* 174 */         Iterator<Filter> iter = composite.iterator();
/* 175 */         this.filter = iter.next();
/*     */       } else {
/* 177 */         this.filter = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/* 187 */     setStarting();
/* 188 */     if (this.filter != null) {
/* 189 */       this.filter.start();
/*     */     }
/* 191 */     setStarted();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 199 */     return stop(timeout, timeUnit, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean stop(long timeout, TimeUnit timeUnit, boolean changeLifeCycleState) {
/* 206 */     if (changeLifeCycleState) {
/* 207 */       setStopping();
/*     */     }
/* 209 */     boolean stopped = true;
/* 210 */     if (this.filter != null) {
/* 211 */       if (this.filter instanceof LifeCycle2) {
/* 212 */         stopped = ((LifeCycle2)this.filter).stop(timeout, timeUnit);
/*     */       } else {
/* 214 */         this.filter.stop();
/* 215 */         stopped = true;
/*     */       } 
/*     */     }
/* 218 */     if (changeLifeCycleState) {
/* 219 */       setStopped();
/*     */     }
/* 221 */     return stopped;
/*     */   }
/*     */   
/*     */   public Property[] getPropertyArray() {
/* 225 */     return this.propertyArray;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\filter\AbstractFilterable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */