/*     */ package org.apache.logging.log4j.core.appender.nosql;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.appender.AbstractAppender;
/*     */ import org.apache.logging.log4j.core.appender.db.AbstractDatabaseAppender;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.util.Booleans;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "NoSql", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class NoSqlAppender
/*     */   extends AbstractDatabaseAppender<NoSqlDatabaseManager<?>>
/*     */ {
/*     */   private final String description;
/*     */   
/*     */   public static class Builder<B extends Builder<B>>
/*     */     extends AbstractAppender.Builder<B>
/*     */     implements org.apache.logging.log4j.core.util.Builder<NoSqlAppender>
/*     */   {
/*     */     @PluginBuilderAttribute("bufferSize")
/*     */     private int bufferSize;
/*     */     @PluginElement("NoSqlProvider")
/*     */     private NoSqlProvider<?> provider;
/*     */     
/*     */     public NoSqlAppender build() {
/*  67 */       String name = getName();
/*  68 */       if (this.provider == null) {
/*  69 */         NoSqlAppender.LOGGER.error("NoSQL provider not specified for appender [{}].", name);
/*  70 */         return null;
/*     */       } 
/*     */       
/*  73 */       String managerName = "noSqlManager{ description=" + name + ", bufferSize=" + this.bufferSize + ", provider=" + this.provider + " }";
/*     */ 
/*     */       
/*  76 */       NoSqlDatabaseManager<?> manager = NoSqlDatabaseManager.getNoSqlDatabaseManager(managerName, this.bufferSize, this.provider);
/*     */       
/*  78 */       if (manager == null) {
/*  79 */         return null;
/*     */       }
/*     */       
/*  82 */       return new NoSqlAppender(name, getFilter(), getLayout(), isIgnoreExceptions(), getPropertyArray(), manager);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setBufferSize(int bufferSize) {
/*  94 */       this.bufferSize = bufferSize;
/*  95 */       return (B)asBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setProvider(NoSqlProvider<?> provider) {
/* 106 */       this.provider = provider;
/* 107 */       return (B)asBuilder();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static NoSqlAppender createAppender(String name, String ignore, Filter filter, String bufferSize, NoSqlProvider<?> provider) {
/* 139 */     if (provider == null) {
/* 140 */       LOGGER.error("NoSQL provider not specified for appender [{}].", name);
/* 141 */       return null;
/*     */     } 
/*     */     
/* 144 */     int bufferSizeInt = AbstractAppender.parseInt(bufferSize, 0);
/* 145 */     boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
/*     */     
/* 147 */     String managerName = "noSqlManager{ description=" + name + ", bufferSize=" + bufferSizeInt + ", provider=" + provider + " }";
/*     */ 
/*     */     
/* 150 */     NoSqlDatabaseManager<?> manager = NoSqlDatabaseManager.getNoSqlDatabaseManager(managerName, bufferSizeInt, provider);
/*     */     
/* 152 */     if (manager == null) {
/* 153 */       return null;
/*     */     }
/*     */     
/* 156 */     return new NoSqlAppender(name, filter, null, ignoreExceptions, null, manager);
/*     */   }
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static <B extends Builder<B>> B newBuilder() {
/* 161 */     return (B)(new Builder<>()).asBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private NoSqlAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, Property[] properties, NoSqlDatabaseManager<?> manager) {
/* 168 */     super(name, filter, layout, ignoreExceptions, properties, manager);
/* 169 */     this.description = getName() + "{ manager=" + getManager() + " }";
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 174 */     return this.description;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\nosql\NoSqlAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */