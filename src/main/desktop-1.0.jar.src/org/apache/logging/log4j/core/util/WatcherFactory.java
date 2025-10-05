/*    */ package org.apache.logging.log4j.core.util;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.ConfigurationFileWatcher;
/*    */ import org.apache.logging.log4j.core.config.ConfigurationListener;
/*    */ import org.apache.logging.log4j.core.config.Reconfigurable;
/*    */ import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
/*    */ import org.apache.logging.log4j.core.config.plugins.util.PluginType;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WatcherFactory
/*    */ {
/* 39 */   private static Logger LOGGER = (Logger)StatusLogger.getLogger();
/* 40 */   private static PluginManager pluginManager = new PluginManager("Watcher");
/*    */   
/*    */   private static volatile WatcherFactory factory;
/*    */   
/*    */   private final Map<String, PluginType<?>> plugins;
/*    */   
/*    */   private WatcherFactory(List<String> packages) {
/* 47 */     pluginManager.collectPlugins(packages);
/* 48 */     this.plugins = pluginManager.getPlugins();
/*    */   }
/*    */   
/*    */   public static WatcherFactory getInstance(List<String> packages) {
/* 52 */     if (factory == null) {
/* 53 */       synchronized (pluginManager) {
/* 54 */         if (factory == null) {
/* 55 */           factory = new WatcherFactory(packages);
/*    */         }
/*    */       } 
/*    */     }
/* 59 */     return factory;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Watcher newWatcher(Source source, Configuration configuration, Reconfigurable reconfigurable, List<ConfigurationListener> configurationListeners, long lastModifiedMillis) {
/* 65 */     if (source.getFile() != null) {
/* 66 */       return (Watcher)new ConfigurationFileWatcher(configuration, reconfigurable, configurationListeners, lastModifiedMillis);
/*    */     }
/*    */     
/* 69 */     String name = source.getURI().getScheme();
/* 70 */     PluginType<?> pluginType = this.plugins.get(name);
/* 71 */     if (pluginType != null) {
/* 72 */       return instantiate(name, pluginType.getPluginClass(), configuration, reconfigurable, configurationListeners, lastModifiedMillis);
/*    */     }
/*    */     
/* 75 */     LOGGER.info("No Watcher plugin is available for protocol '{}'", name);
/* 76 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T extends Watcher> T instantiate(String name, Class<T> clazz, Configuration configuration, Reconfigurable reconfigurable, List<ConfigurationListener> listeners, long lastModifiedMillis) {
/* 83 */     Objects.requireNonNull(clazz, "No class provided");
/*    */     
/*    */     try {
/* 86 */       Constructor<T> constructor = clazz.getConstructor(new Class[] { Configuration.class, Reconfigurable.class, List.class, long.class });
/* 87 */       return constructor.newInstance(new Object[] { configuration, reconfigurable, listeners, Long.valueOf(lastModifiedMillis) });
/* 88 */     } catch (NoSuchMethodException ex) {
/* 89 */       throw new IllegalArgumentException("No valid constructor for Watcher plugin " + name, ex);
/* 90 */     } catch (LinkageError|InstantiationException e) {
/*    */ 
/*    */       
/* 93 */       throw new IllegalArgumentException(e);
/* 94 */     } catch (IllegalAccessException e) {
/* 95 */       throw new IllegalStateException(e);
/* 96 */     } catch (InvocationTargetException e) {
/* 97 */       Throwables.rethrow(e.getCause());
/* 98 */       throw new InternalError("Unreachable");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\WatcherFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */