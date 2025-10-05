/*     */ package org.apache.logging.log4j.core.lookup;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationAware;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginType;
/*     */ import org.apache.logging.log4j.core.net.JndiManager;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.core.util.ReflectionUtil;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.Constants;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Interpolator
/*     */   extends AbstractConfigurationAwareLookup
/*     */ {
/*     */   public static final char PREFIX_SEPARATOR = ':';
/*     */   private static final String LOOKUP_KEY_WEB = "web";
/*     */   private static final String LOOKUP_KEY_DOCKER = "docker";
/*     */   private static final String LOOKUP_KEY_KUBERNETES = "kubernetes";
/*     */   private static final String LOOKUP_KEY_SPRING = "spring";
/*     */   private static final String LOOKUP_KEY_JNDI = "jndi";
/*     */   private static final String LOOKUP_KEY_JVMRUNARGS = "jvmrunargs";
/*  55 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*  57 */   private final Map<String, StrLookup> strLookupMap = new HashMap<>();
/*     */   
/*     */   private final StrLookup defaultLookup;
/*     */   
/*     */   public Interpolator(StrLookup defaultLookup) {
/*  62 */     this(defaultLookup, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Interpolator(StrLookup defaultLookup, List<String> pluginPackages) {
/*  73 */     this.defaultLookup = (defaultLookup == null) ? new MapLookup(new HashMap<>()) : defaultLookup;
/*  74 */     PluginManager manager = new PluginManager("Lookup");
/*  75 */     manager.collectPlugins(pluginPackages);
/*  76 */     Map<String, PluginType<?>> plugins = manager.getPlugins();
/*     */     
/*  78 */     for (Map.Entry<String, PluginType<?>> entry : plugins.entrySet()) {
/*     */       try {
/*  80 */         Class<? extends StrLookup> clazz = ((PluginType)entry.getValue()).getPluginClass().asSubclass(StrLookup.class);
/*  81 */         if (!clazz.getName().equals("org.apache.logging.log4j.core.lookup.JndiLookup") || JndiManager.isJndiLookupEnabled()) {
/*  82 */           this.strLookupMap.put(((String)entry.getKey()).toLowerCase(), ReflectionUtil.instantiate(clazz));
/*     */         }
/*  84 */       } catch (Throwable t) {
/*  85 */         handleError(entry.getKey(), t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Interpolator() {
/*  94 */     this((Map<String, String>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Interpolator(Map<String, String> properties) {
/* 101 */     this.defaultLookup = new MapLookup((properties == null) ? new HashMap<>() : properties);
/*     */     
/* 103 */     this.strLookupMap.put("log4j", new Log4jLookup());
/* 104 */     this.strLookupMap.put("sys", new SystemPropertiesLookup());
/* 105 */     this.strLookupMap.put("env", new EnvironmentLookup());
/* 106 */     this.strLookupMap.put("main", MainMapLookup.MAIN_SINGLETON);
/* 107 */     this.strLookupMap.put("marker", new MarkerLookup());
/* 108 */     this.strLookupMap.put("java", new JavaLookup());
/* 109 */     this.strLookupMap.put("lower", new LowerLookup());
/* 110 */     this.strLookupMap.put("upper", new UpperLookup());
/*     */     
/* 112 */     if (JndiManager.isJndiLookupEnabled()) {
/*     */       
/*     */       try {
/* 115 */         this.strLookupMap.put("jndi", 
/* 116 */             Loader.newCheckedInstanceOf("org.apache.logging.log4j.core.lookup.JndiLookup", StrLookup.class));
/* 117 */       } catch (LinkageError|Exception e) {
/* 118 */         handleError("jndi", e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 124 */       this.strLookupMap.put("jvmrunargs", 
/* 125 */           Loader.newCheckedInstanceOf("org.apache.logging.log4j.core.lookup.JmxRuntimeInputArgumentsLookup", StrLookup.class));
/*     */     }
/* 127 */     catch (LinkageError|Exception e) {
/* 128 */       handleError("jvmrunargs", e);
/*     */     } 
/* 130 */     this.strLookupMap.put("date", new DateLookup());
/* 131 */     this.strLookupMap.put("ctx", new ContextMapLookup());
/* 132 */     if (Constants.IS_WEB_APP) {
/*     */       try {
/* 134 */         this.strLookupMap.put("web", 
/* 135 */             Loader.newCheckedInstanceOf("org.apache.logging.log4j.web.WebLookup", StrLookup.class));
/* 136 */       } catch (Exception ignored) {
/* 137 */         handleError("web", ignored);
/*     */       } 
/*     */     } else {
/* 140 */       LOGGER.debug("Not in a ServletContext environment, thus not loading WebLookup plugin.");
/*     */     } 
/*     */     try {
/* 143 */       this.strLookupMap.put("docker", 
/* 144 */           Loader.newCheckedInstanceOf("org.apache.logging.log4j.docker.DockerLookup", StrLookup.class));
/* 145 */     } catch (Exception ignored) {
/* 146 */       handleError("docker", ignored);
/*     */     } 
/*     */     try {
/* 149 */       this.strLookupMap.put("spring", 
/* 150 */           Loader.newCheckedInstanceOf("org.apache.logging.log4j.spring.cloud.config.client.SpringLookup", StrLookup.class));
/* 151 */     } catch (Exception ignored) {
/* 152 */       handleError("spring", ignored);
/*     */     } 
/*     */     try {
/* 155 */       this.strLookupMap.put("kubernetes", 
/* 156 */           Loader.newCheckedInstanceOf("org.apache.logging.log4j.kubernetes.KubernetesLookup", StrLookup.class));
/* 157 */     } catch (Exception|NoClassDefFoundError error) {
/* 158 */       handleError("kubernetes", error);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Map<String, StrLookup> getStrLookupMap() {
/* 163 */     return this.strLookupMap;
/*     */   }
/*     */   
/*     */   private void handleError(String lookupKey, Throwable t) {
/* 167 */     switch (lookupKey) {
/*     */       
/*     */       case "jndi":
/* 170 */         LOGGER.warn("JNDI lookup class is not available because this JRE does not support JNDI. JNDI string lookups will not be available, continuing configuration. Ignoring " + t);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case "jvmrunargs":
/* 176 */         LOGGER.warn("JMX runtime input lookup class is not available because this JRE does not support JMX. JMX lookups will not be available, continuing configuration. Ignoring " + t);
/*     */ 
/*     */ 
/*     */       
/*     */       case "web":
/* 181 */         LOGGER.info("Log4j appears to be running in a Servlet environment, but there's no log4j-web module available. If you want better web container support, please add the log4j-web JAR to your web archive or server lib directory.");
/*     */       
/*     */       case "docker":
/*     */       case "spring":
/*     */         return;
/*     */       
/*     */       case "kubernetes":
/* 188 */         if (t instanceof NoClassDefFoundError) {
/* 189 */           LOGGER.warn("Unable to create Kubernetes lookup due to missing dependency: {}", t.getMessage());
/*     */         }
/*     */     } 
/*     */     
/* 193 */     LOGGER.error("Unable to create Lookup for {}", lookupKey, t);
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
/*     */   public String lookup(LogEvent event, String var) {
/* 212 */     if (var == null) {
/* 213 */       return null;
/*     */     }
/*     */     
/* 216 */     int prefixPos = var.indexOf(':');
/* 217 */     if (prefixPos >= 0) {
/* 218 */       String prefix = var.substring(0, prefixPos).toLowerCase(Locale.US);
/* 219 */       String name = var.substring(prefixPos + 1);
/* 220 */       StrLookup lookup = this.strLookupMap.get(prefix);
/* 221 */       if (lookup instanceof ConfigurationAware) {
/* 222 */         ((ConfigurationAware)lookup).setConfiguration(this.configuration);
/*     */       }
/* 224 */       String value = null;
/* 225 */       if (lookup != null) {
/* 226 */         value = (event == null) ? lookup.lookup(name) : lookup.lookup(event, name);
/*     */       }
/*     */       
/* 229 */       if (value != null) {
/* 230 */         return value;
/*     */       }
/* 232 */       var = var.substring(prefixPos + 1);
/*     */     } 
/* 234 */     if (this.defaultLookup != null) {
/* 235 */       return (event == null) ? this.defaultLookup.lookup(var) : this.defaultLookup.lookup(event, var);
/*     */     }
/* 237 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 242 */     StringBuilder sb = new StringBuilder();
/* 243 */     for (String name : this.strLookupMap.keySet()) {
/* 244 */       if (sb.length() == 0) {
/* 245 */         sb.append('{');
/*     */       } else {
/* 247 */         sb.append(", ");
/*     */       } 
/*     */       
/* 250 */       sb.append(name);
/*     */     } 
/* 252 */     if (sb.length() > 0) {
/* 253 */       sb.append('}');
/*     */     }
/* 255 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\lookup\Interpolator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */