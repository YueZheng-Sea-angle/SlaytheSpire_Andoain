/*     */ package org.apache.logging.log4j.spi;
/*     */ 
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.net.URL;
/*     */ import java.util.Properties;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Provider
/*     */ {
/*     */   public static final String FACTORY_PRIORITY = "FactoryPriority";
/*     */   public static final String THREAD_CONTEXT_MAP = "ThreadContextMap";
/*     */   public static final String LOGGER_CONTEXT_FACTORY = "LoggerContextFactory";
/*  45 */   private static final Integer DEFAULT_PRIORITY = Integer.valueOf(-1);
/*  46 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private final Integer priority;
/*     */   private final String className;
/*     */   private final Class<? extends LoggerContextFactory> loggerContextFactoryClass;
/*     */   private final String threadContextMap;
/*     */   private final Class<? extends ThreadContextMap> threadContextMapClass;
/*     */   private final String versions;
/*     */   private final URL url;
/*     */   private final WeakReference<ClassLoader> classLoader;
/*     */   
/*     */   public Provider(Properties props, URL url, ClassLoader classLoader) {
/*  58 */     this.url = url;
/*  59 */     this.classLoader = new WeakReference<>(classLoader);
/*  60 */     String weight = props.getProperty("FactoryPriority");
/*  61 */     this.priority = (weight == null) ? DEFAULT_PRIORITY : Integer.valueOf(weight);
/*  62 */     this.className = props.getProperty("LoggerContextFactory");
/*  63 */     this.threadContextMap = props.getProperty("ThreadContextMap");
/*  64 */     this.loggerContextFactoryClass = null;
/*  65 */     this.threadContextMapClass = null;
/*  66 */     this.versions = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Provider(Integer priority, String versions, Class<? extends LoggerContextFactory> loggerContextFactoryClass) {
/*  71 */     this(priority, versions, loggerContextFactoryClass, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Provider(Integer priority, String versions, Class<? extends LoggerContextFactory> loggerContextFactoryClass, Class<? extends ThreadContextMap> threadContextMapClass) {
/*  78 */     this.url = null;
/*  79 */     this.classLoader = null;
/*  80 */     this.priority = priority;
/*  81 */     this.loggerContextFactoryClass = loggerContextFactoryClass;
/*  82 */     this.threadContextMapClass = threadContextMapClass;
/*  83 */     this.className = null;
/*  84 */     this.threadContextMap = null;
/*  85 */     this.versions = versions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getVersions() {
/*  93 */     return this.versions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getPriority() {
/* 102 */     return this.priority;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/* 112 */     if (this.loggerContextFactoryClass != null) {
/* 113 */       return this.loggerContextFactoryClass.getName();
/*     */     }
/* 115 */     return this.className;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<? extends LoggerContextFactory> loadLoggerContextFactory() {
/* 124 */     if (this.loggerContextFactoryClass != null) {
/* 125 */       return this.loggerContextFactoryClass;
/*     */     }
/* 127 */     if (this.className == null) {
/* 128 */       return null;
/*     */     }
/* 130 */     ClassLoader loader = this.classLoader.get();
/* 131 */     if (loader == null) {
/* 132 */       return null;
/*     */     }
/*     */     try {
/* 135 */       Class<?> clazz = loader.loadClass(this.className);
/* 136 */       if (LoggerContextFactory.class.isAssignableFrom(clazz)) {
/* 137 */         return clazz.asSubclass(LoggerContextFactory.class);
/*     */       }
/* 139 */     } catch (Exception e) {
/* 140 */       LOGGER.error("Unable to create class {} specified in {}", this.className, this.url.toString(), e);
/*     */     } 
/* 142 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getThreadContextMap() {
/* 151 */     if (this.threadContextMapClass != null) {
/* 152 */       return this.threadContextMapClass.getName();
/*     */     }
/* 154 */     return this.threadContextMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<? extends ThreadContextMap> loadThreadContextMap() {
/* 163 */     if (this.threadContextMapClass != null) {
/* 164 */       return this.threadContextMapClass;
/*     */     }
/* 166 */     if (this.threadContextMap == null) {
/* 167 */       return null;
/*     */     }
/* 169 */     ClassLoader loader = this.classLoader.get();
/* 170 */     if (loader == null) {
/* 171 */       return null;
/*     */     }
/*     */     try {
/* 174 */       Class<?> clazz = loader.loadClass(this.threadContextMap);
/* 175 */       if (ThreadContextMap.class.isAssignableFrom(clazz)) {
/* 176 */         return clazz.asSubclass(ThreadContextMap.class);
/*     */       }
/* 178 */     } catch (Exception e) {
/* 179 */       LOGGER.error("Unable to create class {} specified in {}", this.threadContextMap, this.url.toString(), e);
/*     */     } 
/* 181 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getUrl() {
/* 190 */     return this.url;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 195 */     StringBuilder result = new StringBuilder("Provider[");
/* 196 */     if (!DEFAULT_PRIORITY.equals(this.priority)) {
/* 197 */       result.append("priority=").append(this.priority).append(", ");
/*     */     }
/* 199 */     if (this.threadContextMap != null) {
/* 200 */       result.append("threadContextMap=").append(this.threadContextMap).append(", ");
/* 201 */     } else if (this.threadContextMapClass != null) {
/* 202 */       result.append("threadContextMapClass=").append(this.threadContextMapClass.getName());
/*     */     } 
/* 204 */     if (this.className != null) {
/* 205 */       result.append("className=").append(this.className).append(", ");
/* 206 */     } else if (this.loggerContextFactoryClass != null) {
/* 207 */       result.append("class=").append(this.loggerContextFactoryClass.getName());
/*     */     } 
/* 209 */     if (this.url != null) {
/* 210 */       result.append("url=").append(this.url);
/*     */     }
/*     */     ClassLoader loader;
/* 213 */     if (this.classLoader == null || (loader = this.classLoader.get()) == null) {
/* 214 */       result.append(", classLoader=null(not reachable)");
/*     */     } else {
/* 216 */       result.append(", classLoader=").append(loader);
/*     */     } 
/* 218 */     result.append("]");
/* 219 */     return result.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 224 */     if (this == o) {
/* 225 */       return true;
/*     */     }
/* 227 */     if (o == null || getClass() != o.getClass()) {
/* 228 */       return false;
/*     */     }
/*     */     
/* 231 */     Provider provider = (Provider)o;
/*     */     
/* 233 */     if ((this.priority != null) ? !this.priority.equals(provider.priority) : (provider.priority != null)) {
/* 234 */       return false;
/*     */     }
/* 236 */     if ((this.className != null) ? !this.className.equals(provider.className) : (provider.className != null)) {
/* 237 */       return false;
/*     */     }
/* 239 */     if ((this.loggerContextFactoryClass != null) ? !this.loggerContextFactoryClass.equals(provider.loggerContextFactoryClass) : (provider.loggerContextFactoryClass != null)) {
/* 240 */       return false;
/*     */     }
/* 242 */     return (this.versions != null) ? this.versions.equals(provider.versions) : ((provider.versions == null));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 247 */     int result = (this.priority != null) ? this.priority.hashCode() : 0;
/* 248 */     result = 31 * result + ((this.className != null) ? this.className.hashCode() : 0);
/* 249 */     result = 31 * result + ((this.loggerContextFactoryClass != null) ? this.loggerContextFactoryClass.hashCode() : 0);
/* 250 */     result = 31 * result + ((this.versions != null) ? this.versions.hashCode() : 0);
/* 251 */     return result;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\spi\Provider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */