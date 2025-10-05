/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.FileDescriptor;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
/*     */ import org.apache.logging.log4j.core.layout.PatternLayout;
/*     */ import org.apache.logging.log4j.core.util.Booleans;
/*     */ import org.apache.logging.log4j.core.util.CloseShieldOutputStream;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.core.util.Throwables;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "Console", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class ConsoleAppender
/*     */   extends AbstractOutputStreamAppender<OutputStreamManager>
/*     */ {
/*     */   public static final String PLUGIN_NAME = "Console";
/*     */   private static final String JANSI_CLASS = "org.fusesource.jansi.WindowsAnsiOutputStream";
/*  56 */   private static ConsoleManagerFactory factory = new ConsoleManagerFactory();
/*  57 */   private static final Target DEFAULT_TARGET = Target.SYSTEM_OUT;
/*  58 */   private static final AtomicInteger COUNT = new AtomicInteger();
/*     */ 
/*     */ 
/*     */   
/*     */   private final Target target;
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Target
/*     */   {
/*  68 */     SYSTEM_OUT
/*     */     {
/*     */       public Charset getDefaultCharset()
/*     */       {
/*  72 */         return getCharset("sun.stdout.encoding", Charset.defaultCharset());
/*     */       }
/*     */     },
/*     */ 
/*     */     
/*  77 */     SYSTEM_ERR
/*     */     {
/*     */       public Charset getDefaultCharset()
/*     */       {
/*  81 */         return getCharset("sun.stderr.encoding", Charset.defaultCharset());
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */     
/*     */     protected Charset getCharset(String property, Charset defaultCharset) {
/*  88 */       return (new PropertiesUtil(PropertiesUtil.getSystemProperties())).getCharsetProperty(property, defaultCharset);
/*     */     }
/*     */ 
/*     */     
/*     */     public abstract Charset getDefaultCharset();
/*     */   }
/*     */   
/*     */   private ConsoleAppender(String name, Layout<? extends Serializable> layout, Filter filter, OutputStreamManager manager, boolean ignoreExceptions, Target target, Property[] properties) {
/*  96 */     super(name, layout, filter, ignoreExceptions, true, properties, manager);
/*  97 */     this.target = target;
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
/*     */   @Deprecated
/*     */   public static ConsoleAppender createAppender(Layout<? extends Serializable> layout, Filter filter, String targetStr, String name, String follow, String ignore) {
/*     */     PatternLayout patternLayout;
/* 120 */     if (name == null) {
/* 121 */       LOGGER.error("No name provided for ConsoleAppender");
/* 122 */       return null;
/*     */     } 
/* 124 */     if (layout == null) {
/* 125 */       patternLayout = PatternLayout.createDefaultLayout();
/*     */     }
/* 127 */     boolean isFollow = Boolean.parseBoolean(follow);
/* 128 */     boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
/* 129 */     Target target = (targetStr == null) ? DEFAULT_TARGET : Target.valueOf(targetStr);
/* 130 */     return new ConsoleAppender(name, (Layout<? extends Serializable>)patternLayout, filter, getManager(target, isFollow, false, (Layout<? extends Serializable>)patternLayout), ignoreExceptions, target, null);
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
/*     */   @Deprecated
/*     */   public static ConsoleAppender createAppender(Layout<? extends Serializable> layout, Filter filter, Target target, String name, boolean follow, boolean direct, boolean ignoreExceptions) {
/*     */     PatternLayout patternLayout;
/* 159 */     if (name == null) {
/* 160 */       LOGGER.error("No name provided for ConsoleAppender");
/* 161 */       return null;
/*     */     } 
/* 163 */     if (layout == null) {
/* 164 */       patternLayout = PatternLayout.createDefaultLayout();
/*     */     }
/* 166 */     target = (target == null) ? Target.SYSTEM_OUT : target;
/* 167 */     if (follow && direct) {
/* 168 */       LOGGER.error("Cannot use both follow and direct on ConsoleAppender");
/* 169 */       return null;
/*     */     } 
/* 171 */     return new ConsoleAppender(name, (Layout<? extends Serializable>)patternLayout, filter, getManager(target, follow, direct, (Layout<? extends Serializable>)patternLayout), ignoreExceptions, target, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ConsoleAppender createDefaultAppenderForLayout(Layout<? extends Serializable> layout) {
/* 176 */     return new ConsoleAppender("DefaultConsole-" + COUNT.incrementAndGet(), layout, null, 
/* 177 */         getDefaultManager(DEFAULT_TARGET, false, false, layout), true, DEFAULT_TARGET, null);
/*     */   }
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static <B extends Builder<B>> B newBuilder() {
/* 182 */     return (B)(new Builder<>()).asBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder<B extends Builder<B>>
/*     */     extends AbstractOutputStreamAppender.Builder<B>
/*     */     implements org.apache.logging.log4j.core.util.Builder<ConsoleAppender>
/*     */   {
/*     */     @PluginBuilderAttribute
/*     */     @Required
/* 194 */     private ConsoleAppender.Target target = ConsoleAppender.DEFAULT_TARGET;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private boolean follow;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private boolean direct;
/*     */     
/*     */     public B setTarget(ConsoleAppender.Target aTarget) {
/* 203 */       this.target = aTarget;
/* 204 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setFollow(boolean shouldFollow) {
/* 208 */       this.follow = shouldFollow;
/* 209 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setDirect(boolean shouldDirect) {
/* 213 */       this.direct = shouldDirect;
/* 214 */       return (B)asBuilder();
/*     */     }
/*     */ 
/*     */     
/*     */     public ConsoleAppender build() {
/* 219 */       if (this.follow && this.direct) {
/* 220 */         throw new IllegalArgumentException("Cannot use both follow and direct on ConsoleAppender '" + getName() + "'");
/*     */       }
/* 222 */       Layout<? extends Serializable> layout = getOrCreateLayout(this.target.getDefaultCharset());
/* 223 */       return new ConsoleAppender(getName(), layout, getFilter(), ConsoleAppender.getManager(this.target, this.follow, this.direct, layout), 
/* 224 */           isIgnoreExceptions(), this.target, getPropertyArray());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static OutputStreamManager getDefaultManager(Target target, boolean follow, boolean direct, Layout<? extends Serializable> layout) {
/* 230 */     OutputStream os = getOutputStream(follow, direct, target);
/*     */ 
/*     */     
/* 233 */     String managerName = target.name() + '.' + follow + '.' + direct + "-" + COUNT.get();
/* 234 */     return OutputStreamManager.getManager(managerName, new FactoryData(os, managerName, layout), factory);
/*     */   }
/*     */ 
/*     */   
/*     */   private static OutputStreamManager getManager(Target target, boolean follow, boolean direct, Layout<? extends Serializable> layout) {
/* 239 */     OutputStream os = getOutputStream(follow, direct, target);
/* 240 */     String managerName = target.name() + '.' + follow + '.' + direct;
/* 241 */     return OutputStreamManager.getManager(managerName, new FactoryData(os, managerName, layout), factory);
/*     */   }
/*     */   private static OutputStream getOutputStream(boolean follow, boolean direct, Target target) {
/*     */     CloseShieldOutputStream closeShieldOutputStream;
/* 245 */     String enc = Charset.defaultCharset().name();
/*     */ 
/*     */     
/*     */     try {
/* 249 */       OutputStream outputStream = (target == Target.SYSTEM_OUT) ? (direct ? new FileOutputStream(FileDescriptor.out) : (follow ? new PrintStream(new SystemOutStream(), true, enc) : System.out)) : (direct ? new FileOutputStream(FileDescriptor.err) : (follow ? new PrintStream(new SystemErrStream(), true, enc) : System.err));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 255 */       closeShieldOutputStream = new CloseShieldOutputStream(outputStream);
/* 256 */     } catch (UnsupportedEncodingException ex) {
/* 257 */       throw new IllegalStateException("Unsupported default encoding " + enc, ex);
/*     */     } 
/* 259 */     PropertiesUtil propsUtil = PropertiesUtil.getProperties();
/* 260 */     if (!propsUtil.isOsWindows() || propsUtil.getBooleanProperty("log4j.skipJansi", true) || direct) {
/* 261 */       return (OutputStream)closeShieldOutputStream;
/*     */     }
/*     */     
/*     */     try {
/* 265 */       Class<?> clazz = Loader.loadClass("org.fusesource.jansi.WindowsAnsiOutputStream");
/* 266 */       Constructor<?> constructor = clazz.getConstructor(new Class[] { OutputStream.class });
/* 267 */       return (OutputStream)new CloseShieldOutputStream((OutputStream)constructor.newInstance(new Object[] { closeShieldOutputStream }));
/* 268 */     } catch (ClassNotFoundException cnfe) {
/* 269 */       LOGGER.debug("Jansi is not installed, cannot find {}", "org.fusesource.jansi.WindowsAnsiOutputStream");
/* 270 */     } catch (NoSuchMethodException nsme) {
/* 271 */       LOGGER.warn("{} is missing the proper constructor", "org.fusesource.jansi.WindowsAnsiOutputStream");
/* 272 */     } catch (Exception ex) {
/* 273 */       LOGGER.warn("Unable to instantiate {} due to {}", "org.fusesource.jansi.WindowsAnsiOutputStream", clean(Throwables.getRootCause(ex).toString()).trim());
/*     */     } 
/* 275 */     return (OutputStream)closeShieldOutputStream;
/*     */   }
/*     */   
/*     */   private static String clean(String string) {
/* 279 */     return string.replace(false, ' ');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SystemErrStream
/*     */     extends OutputStream
/*     */   {
/*     */     public void close() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void flush() {
/* 296 */       System.err.flush();
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(byte[] b) throws IOException {
/* 301 */       System.err.write(b);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(byte[] b, int off, int len) throws IOException {
/* 306 */       System.err.write(b, off, len);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(int b) {
/* 311 */       System.err.write(b);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SystemOutStream
/*     */     extends OutputStream
/*     */   {
/*     */     public void close() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void flush() {
/* 329 */       System.out.flush();
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(byte[] b) throws IOException {
/* 334 */       System.out.write(b);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(byte[] b, int off, int len) throws IOException {
/* 339 */       System.out.write(b, off, len);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(int b) throws IOException {
/* 344 */       System.out.write(b);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FactoryData
/*     */   {
/*     */     private final OutputStream os;
/*     */ 
/*     */     
/*     */     private final String name;
/*     */ 
/*     */     
/*     */     private final Layout<? extends Serializable> layout;
/*     */ 
/*     */ 
/*     */     
/*     */     public FactoryData(OutputStream os, String type, Layout<? extends Serializable> layout) {
/* 364 */       this.os = os;
/* 365 */       this.name = type;
/* 366 */       this.layout = layout;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ConsoleManagerFactory
/*     */     implements ManagerFactory<OutputStreamManager, FactoryData>
/*     */   {
/*     */     private ConsoleManagerFactory() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public OutputStreamManager createManager(String name, ConsoleAppender.FactoryData data) {
/* 384 */       return new OutputStreamManager(data.os, data.name, data.layout, true);
/*     */     }
/*     */   }
/*     */   
/*     */   public Target getTarget() {
/* 389 */     return this.target;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\ConsoleAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */