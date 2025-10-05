/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.layout.PatternLayout;
/*     */ import org.apache.logging.log4j.core.util.CloseShieldOutputStream;
/*     */ import org.apache.logging.log4j.core.util.NullOutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "OutputStream", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class OutputStreamAppender
/*     */   extends AbstractOutputStreamAppender<OutputStreamManager>
/*     */ {
/*     */   public static class Builder<B extends Builder<B>>
/*     */     extends AbstractOutputStreamAppender.Builder<B>
/*     */     implements org.apache.logging.log4j.core.util.Builder<OutputStreamAppender>
/*     */   {
/*     */     private boolean follow = false;
/*     */     private final boolean ignoreExceptions = true;
/*     */     private OutputStream target;
/*     */     
/*     */     public OutputStreamAppender build() {
/*  60 */       Layout<? extends Serializable> layout = getLayout();
/*  61 */       Layout<? extends Serializable> actualLayout = (layout == null) ? (Layout<? extends Serializable>)PatternLayout.createDefaultLayout() : layout;
/*     */       
/*  63 */       return new OutputStreamAppender(getName(), actualLayout, getFilter(), OutputStreamAppender.getManager(this.target, this.follow, actualLayout), true, 
/*  64 */           getPropertyArray());
/*     */     }
/*     */     
/*     */     public B setFollow(boolean shouldFollow) {
/*  68 */       this.follow = shouldFollow;
/*  69 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setTarget(OutputStream aTarget) {
/*  73 */       this.target = aTarget;
/*  74 */       return (B)asBuilder();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FactoryData
/*     */   {
/*     */     private final Layout<? extends Serializable> layout;
/*     */ 
/*     */ 
/*     */     
/*     */     private final String name;
/*     */ 
/*     */ 
/*     */     
/*     */     private final OutputStream os;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public FactoryData(OutputStream os, String type, Layout<? extends Serializable> layout) {
/*  97 */       this.os = os;
/*  98 */       this.name = type;
/*  99 */       this.layout = layout;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class OutputStreamManagerFactory
/*     */     implements ManagerFactory<OutputStreamManager, FactoryData>
/*     */   {
/*     */     private OutputStreamManagerFactory() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public OutputStreamManager createManager(String name, OutputStreamAppender.FactoryData data) {
/* 119 */       return new OutputStreamManager(data.os, data.name, data.layout, true);
/*     */     }
/*     */   }
/*     */   
/* 123 */   private static OutputStreamManagerFactory factory = new OutputStreamManagerFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public static OutputStreamAppender createAppender(Layout<? extends Serializable> layout, Filter filter, OutputStream target, String name, boolean follow, boolean ignore) {
/*     */     PatternLayout patternLayout;
/* 148 */     if (name == null) {
/* 149 */       LOGGER.error("No name provided for OutputStreamAppender");
/* 150 */       return null;
/*     */     } 
/* 152 */     if (layout == null) {
/* 153 */       patternLayout = PatternLayout.createDefaultLayout();
/*     */     }
/* 155 */     return new OutputStreamAppender(name, (Layout<? extends Serializable>)patternLayout, filter, getManager(target, follow, (Layout<? extends Serializable>)patternLayout), ignore, null);
/*     */   }
/*     */ 
/*     */   
/*     */   private static OutputStreamManager getManager(OutputStream target, boolean follow, Layout<? extends Serializable> layout) {
/* 160 */     OutputStream os = (target == null) ? (OutputStream)NullOutputStream.getInstance() : (OutputStream)new CloseShieldOutputStream(target);
/* 161 */     OutputStream targetRef = (target == null) ? os : target;
/* 162 */     String managerName = targetRef.getClass().getName() + "@" + Integer.toHexString(targetRef.hashCode()) + '.' + follow;
/*     */     
/* 164 */     return OutputStreamManager.getManager(managerName, new FactoryData(os, managerName, layout), factory);
/*     */   }
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static <B extends Builder<B>> B newBuilder() {
/* 169 */     return (B)(new Builder<>()).asBuilder();
/*     */   }
/*     */ 
/*     */   
/*     */   private OutputStreamAppender(String name, Layout<? extends Serializable> layout, Filter filter, OutputStreamManager manager, boolean ignoreExceptions, Property[] properties) {
/* 174 */     super(name, layout, filter, ignoreExceptions, true, properties, manager);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\OutputStreamAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */