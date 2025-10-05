/*     */ package org.apache.logging.log4j.core.config.composite;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.core.config.AbstractConfiguration;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationFactory;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationSource;
/*     */ import org.apache.logging.log4j.core.config.Node;
/*     */ import org.apache.logging.log4j.core.config.Reconfigurable;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.ResolverUtil;
/*     */ import org.apache.logging.log4j.core.config.status.StatusConfiguration;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.core.util.Patterns;
/*     */ import org.apache.logging.log4j.core.util.Source;
/*     */ import org.apache.logging.log4j.core.util.WatchManager;
/*     */ import org.apache.logging.log4j.core.util.Watcher;
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
/*     */ public class CompositeConfiguration
/*     */   extends AbstractConfiguration
/*     */   implements Reconfigurable
/*     */ {
/*     */   public static final String MERGE_STRATEGY_PROPERTY = "log4j.mergeStrategy";
/*  52 */   private static final String[] VERBOSE_CLASSES = new String[] { ResolverUtil.class.getName() };
/*     */ 
/*     */ 
/*     */   
/*     */   private final List<? extends AbstractConfiguration> configurations;
/*     */ 
/*     */   
/*     */   private MergeStrategy mergeStrategy;
/*     */ 
/*     */ 
/*     */   
/*     */   public CompositeConfiguration(List<? extends AbstractConfiguration> configurations) {
/*  64 */     super(((AbstractConfiguration)configurations.get(0)).getLoggerContext(), ConfigurationSource.COMPOSITE_SOURCE);
/*  65 */     this.rootNode = ((AbstractConfiguration)configurations.get(0)).getRootNode();
/*  66 */     this.configurations = configurations;
/*  67 */     String mergeStrategyClassName = PropertiesUtil.getProperties().getStringProperty("log4j.mergeStrategy", DefaultMergeStrategy.class
/*  68 */         .getName());
/*     */     try {
/*  70 */       this.mergeStrategy = (MergeStrategy)Loader.newInstanceOf(mergeStrategyClassName);
/*  71 */     } catch (ClassNotFoundException|IllegalAccessException|NoSuchMethodException|java.lang.reflect.InvocationTargetException|InstantiationException ex) {
/*     */       
/*  73 */       this.mergeStrategy = new DefaultMergeStrategy();
/*     */     } 
/*  75 */     for (AbstractConfiguration config : configurations) {
/*  76 */       this.mergeStrategy.mergeRootProperties(this.rootNode, config);
/*     */     }
/*     */     
/*  79 */     StatusConfiguration statusConfig = (new StatusConfiguration()).withVerboseClasses(VERBOSE_CLASSES).withStatus(getDefaultStatus());
/*  80 */     for (Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)this.rootNode.getAttributes().entrySet()) {
/*  81 */       String key = entry.getKey();
/*  82 */       String value = getConfigurationStrSubstitutor().replace(entry.getValue());
/*  83 */       if ("status".equalsIgnoreCase(key)) {
/*  84 */         statusConfig.withStatus(value.toUpperCase()); continue;
/*  85 */       }  if ("dest".equalsIgnoreCase(key)) {
/*  86 */         statusConfig.withDestination(value); continue;
/*  87 */       }  if ("shutdownHook".equalsIgnoreCase(key)) {
/*  88 */         this.isShutdownHookEnabled = !"disable".equalsIgnoreCase(value); continue;
/*  89 */       }  if ("shutdownTimeout".equalsIgnoreCase(key)) {
/*  90 */         this.shutdownTimeoutMillis = Long.parseLong(value); continue;
/*  91 */       }  if ("verbose".equalsIgnoreCase(key)) {
/*  92 */         statusConfig.withVerbosity(value); continue;
/*  93 */       }  if ("packages".equalsIgnoreCase(key)) {
/*  94 */         this.pluginPackages.addAll(Arrays.asList(value.split(Patterns.COMMA_SEPARATOR))); continue;
/*  95 */       }  if ("name".equalsIgnoreCase(key)) {
/*  96 */         setName(value);
/*     */       }
/*     */     } 
/*  99 */     statusConfig.initialize();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setup() {
/* 104 */     AbstractConfiguration targetConfiguration = this.configurations.get(0);
/* 105 */     staffChildConfiguration(targetConfiguration);
/* 106 */     WatchManager watchManager = getWatchManager();
/* 107 */     WatchManager targetWatchManager = targetConfiguration.getWatchManager();
/* 108 */     if (targetWatchManager.getIntervalSeconds() > 0) {
/* 109 */       watchManager.setIntervalSeconds(targetWatchManager.getIntervalSeconds());
/* 110 */       Map<Source, Watcher> watchers = targetWatchManager.getConfigurationWatchers();
/* 111 */       for (Map.Entry<Source, Watcher> entry : watchers.entrySet()) {
/* 112 */         watchManager.watch(entry.getKey(), ((Watcher)entry.getValue()).newWatcher(this, this.listeners, ((Watcher)entry
/* 113 */               .getValue()).getLastModified()));
/*     */       }
/*     */     } 
/* 116 */     for (AbstractConfiguration sourceConfiguration : this.configurations.subList(1, this.configurations.size())) {
/* 117 */       staffChildConfiguration(sourceConfiguration);
/* 118 */       Node sourceRoot = sourceConfiguration.getRootNode();
/* 119 */       this.mergeStrategy.mergConfigurations(this.rootNode, sourceRoot, getPluginManager());
/* 120 */       if (LOGGER.isEnabled(Level.ALL)) {
/* 121 */         StringBuilder sb = new StringBuilder();
/* 122 */         printNodes("", this.rootNode, sb);
/* 123 */         System.out.println(sb.toString());
/*     */       } 
/* 125 */       int monitorInterval = sourceConfiguration.getWatchManager().getIntervalSeconds();
/* 126 */       if (monitorInterval > 0) {
/* 127 */         int currentInterval = watchManager.getIntervalSeconds();
/* 128 */         if (currentInterval <= 0 || monitorInterval < currentInterval) {
/* 129 */           watchManager.setIntervalSeconds(monitorInterval);
/*     */         }
/* 131 */         WatchManager sourceWatchManager = sourceConfiguration.getWatchManager();
/* 132 */         Map<Source, Watcher> watchers = sourceWatchManager.getConfigurationWatchers();
/* 133 */         for (Map.Entry<Source, Watcher> entry : watchers.entrySet()) {
/* 134 */           watchManager.watch(entry.getKey(), ((Watcher)entry.getValue()).newWatcher(this, this.listeners, ((Watcher)entry
/* 135 */                 .getValue()).getLastModified()));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Configuration reconfigure() {
/* 143 */     LOGGER.debug("Reconfiguring composite configuration");
/* 144 */     List<AbstractConfiguration> configs = new ArrayList<>();
/* 145 */     ConfigurationFactory factory = ConfigurationFactory.getInstance();
/* 146 */     for (AbstractConfiguration config : this.configurations) {
/* 147 */       Configuration configuration; ConfigurationSource source = config.getConfigurationSource();
/* 148 */       URI sourceURI = source.getURI();
/* 149 */       AbstractConfiguration abstractConfiguration1 = config;
/* 150 */       if (sourceURI == null) {
/* 151 */         LOGGER.warn("Unable to determine URI for configuration {}, changes to it will be ignored", config
/* 152 */             .getName());
/*     */       } else {
/* 154 */         configuration = factory.getConfiguration(getLoggerContext(), config.getName(), sourceURI);
/* 155 */         if (configuration == null) {
/* 156 */           LOGGER.warn("Unable to reload configuration {}, changes to it will be ignored", config.getName());
/*     */         }
/*     */       } 
/* 159 */       configs.add((AbstractConfiguration)configuration);
/*     */     } 
/*     */ 
/*     */     
/* 163 */     return (Configuration)new CompositeConfiguration(configs);
/*     */   }
/*     */   
/*     */   private void staffChildConfiguration(AbstractConfiguration childConfiguration) {
/* 167 */     childConfiguration.setPluginManager(this.pluginManager);
/* 168 */     childConfiguration.setScriptManager(this.scriptManager);
/* 169 */     childConfiguration.setup();
/*     */   }
/*     */   
/*     */   private void printNodes(String indent, Node node, StringBuilder sb) {
/* 173 */     sb.append(indent).append(node.getName()).append(" type: ").append(node.getType()).append("\n");
/* 174 */     sb.append(indent).append(node.getAttributes().toString()).append("\n");
/* 175 */     for (Node child : node.getChildren()) {
/* 176 */       printNodes(indent + "  ", child, sb);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 182 */     return getClass().getName() + "@" + Integer.toHexString(hashCode()) + " [configurations=" + this.configurations + ", mergeStrategy=" + this.mergeStrategy + ", rootNode=" + this.rootNode + ", listeners=" + this.listeners + ", pluginPackages=" + this.pluginPackages + ", pluginManager=" + this.pluginManager + ", isShutdownHookEnabled=" + this.isShutdownHookEnabled + ", shutdownTimeoutMillis=" + this.shutdownTimeoutMillis + ", scriptManager=" + this.scriptManager + "]";
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\composite\CompositeConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */