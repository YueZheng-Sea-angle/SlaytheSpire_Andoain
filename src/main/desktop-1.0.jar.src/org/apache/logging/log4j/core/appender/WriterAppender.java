/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.Writer;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.StringLayout;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.layout.PatternLayout;
/*     */ import org.apache.logging.log4j.core.util.CloseShieldWriter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "Writer", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class WriterAppender
/*     */   extends AbstractWriterAppender<WriterManager>
/*     */ {
/*     */   public static class Builder<B extends Builder<B>>
/*     */     extends AbstractAppender.Builder<B>
/*     */     implements org.apache.logging.log4j.core.util.Builder<WriterAppender>
/*     */   {
/*     */     private boolean follow = false;
/*     */     private Writer target;
/*     */     
/*     */     public WriterAppender build() {
/*  50 */       StringLayout layout = (StringLayout)getLayout();
/*  51 */       StringLayout actualLayout = (layout != null) ? layout : (StringLayout)PatternLayout.createDefaultLayout();
/*  52 */       return new WriterAppender(getName(), actualLayout, getFilter(), WriterAppender.getManager(this.target, this.follow, actualLayout), 
/*  53 */           isIgnoreExceptions(), getPropertyArray());
/*     */     }
/*     */     
/*     */     public B setFollow(boolean shouldFollow) {
/*  57 */       this.follow = shouldFollow;
/*  58 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setTarget(Writer aTarget) {
/*  62 */       this.target = aTarget;
/*  63 */       return (B)asBuilder();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FactoryData
/*     */   {
/*     */     private final StringLayout layout;
/*     */ 
/*     */ 
/*     */     
/*     */     private final String name;
/*     */ 
/*     */ 
/*     */     
/*     */     private final Writer writer;
/*     */ 
/*     */ 
/*     */     
/*     */     public FactoryData(Writer writer, String type, StringLayout layout) {
/*  85 */       this.writer = writer;
/*  86 */       this.name = type;
/*  87 */       this.layout = layout;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class WriterManagerFactory
/*     */     implements ManagerFactory<WriterManager, FactoryData>
/*     */   {
/*     */     private WriterManagerFactory() {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public WriterManager createManager(String name, WriterAppender.FactoryData data) {
/* 104 */       return new WriterManager(data.writer, data.name, data.layout, true);
/*     */     }
/*     */   }
/*     */   
/* 108 */   private static WriterManagerFactory factory = new WriterManagerFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public static WriterAppender createAppender(StringLayout layout, Filter filter, Writer target, String name, boolean follow, boolean ignore) {
/*     */     PatternLayout patternLayout;
/* 133 */     if (name == null) {
/* 134 */       LOGGER.error("No name provided for WriterAppender");
/* 135 */       return null;
/*     */     } 
/* 137 */     if (layout == null) {
/* 138 */       patternLayout = PatternLayout.createDefaultLayout();
/*     */     }
/* 140 */     return new WriterAppender(name, (StringLayout)patternLayout, filter, getManager(target, follow, (StringLayout)patternLayout), ignore, null);
/*     */   }
/*     */   
/*     */   private static WriterManager getManager(Writer target, boolean follow, StringLayout layout) {
/* 144 */     CloseShieldWriter closeShieldWriter = new CloseShieldWriter(target);
/* 145 */     String managerName = target.getClass().getName() + "@" + Integer.toHexString(target.hashCode()) + '.' + follow;
/*     */     
/* 147 */     return WriterManager.getManager(managerName, new FactoryData((Writer)closeShieldWriter, managerName, layout), factory);
/*     */   }
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static <B extends Builder<B>> B newBuilder() {
/* 152 */     return (B)(new Builder<>()).asBuilder();
/*     */   }
/*     */ 
/*     */   
/*     */   private WriterAppender(String name, StringLayout layout, Filter filter, WriterManager manager, boolean ignoreExceptions, Property[] properties) {
/* 157 */     super(name, layout, filter, ignoreExceptions, true, properties, manager);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\WriterAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */