/*    */ package org.apache.logging.log4j.core.config.plugins.util;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import java.lang.reflect.Modifier;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class PluginUtil
/*    */ {
/*    */   public static Map<String, PluginType<?>> collectPluginsByCategory(String category) {
/* 42 */     Objects.requireNonNull(category, "category");
/* 43 */     return collectPluginsByCategoryAndPackage(category, Collections.emptyList());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Map<String, PluginType<?>> collectPluginsByCategoryAndPackage(String category, List<String> packages) {
/* 52 */     Objects.requireNonNull(category, "category");
/* 53 */     Objects.requireNonNull(packages, "packages");
/* 54 */     PluginManager pluginManager = new PluginManager(category);
/* 55 */     pluginManager.collectPlugins(packages);
/* 56 */     return pluginManager.getPlugins();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <V> V instantiatePlugin(Class<V> pluginClass) {
/* 64 */     Objects.requireNonNull(pluginClass, "pluginClass");
/* 65 */     Method pluginFactoryMethod = findPluginFactoryMethod(pluginClass);
/*    */     
/*    */     try {
/* 68 */       V instance = (V)pluginFactoryMethod.invoke((Object)null, new Object[0]);
/* 69 */       return instance;
/* 70 */     } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException error) {
/* 71 */       String message = String.format("failed to instantiate plugin of type %s using the factory method %s", new Object[] { pluginClass, pluginFactoryMethod });
/*    */ 
/*    */       
/* 74 */       throw new IllegalStateException(message, error);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Method findPluginFactoryMethod(Class<?> pluginClass) {
/* 83 */     Objects.requireNonNull(pluginClass, "pluginClass");
/* 84 */     for (Method method : pluginClass.getDeclaredMethods()) {
/* 85 */       boolean methodAnnotated = method.isAnnotationPresent((Class)PluginFactory.class);
/* 86 */       if (methodAnnotated) {
/* 87 */         boolean methodStatic = Modifier.isStatic(method.getModifiers());
/* 88 */         if (methodStatic) {
/* 89 */           return method;
/*    */         }
/*    */       } 
/*    */     } 
/* 93 */     throw new IllegalStateException("no factory method found for class " + pluginClass);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\plugin\\util\PluginUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */