/*     */ package org.apache.logging.log4j.core.script;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.Serializable;
/*     */ import java.nio.file.Path;
/*     */ import java.security.AccessController;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import javax.script.Bindings;
/*     */ import javax.script.Compilable;
/*     */ import javax.script.CompiledScript;
/*     */ import javax.script.ScriptEngine;
/*     */ import javax.script.ScriptEngineFactory;
/*     */ import javax.script.ScriptEngineManager;
/*     */ import javax.script.ScriptException;
/*     */ import javax.script.SimpleBindings;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.util.FileWatcher;
/*     */ import org.apache.logging.log4j.core.util.WatchManager;
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
/*     */ public class ScriptManager
/*     */   implements FileWatcher, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -2534169384971965196L;
/*     */   private static final String KEY_THREADING = "THREADING";
/*     */   
/*     */   private abstract class AbstractScriptRunner
/*     */     implements ScriptRunner
/*     */   {
/*     */     private static final String KEY_STATUS_LOGGER = "statusLogger";
/*     */     private static final String KEY_CONFIGURATION = "configuration";
/*     */     
/*     */     private AbstractScriptRunner() {}
/*     */     
/*     */     public Bindings createBindings() {
/*  57 */       SimpleBindings bindings = new SimpleBindings();
/*  58 */       bindings.put("configuration", ScriptManager.this.configuration);
/*  59 */       bindings.put("statusLogger", ScriptManager.logger);
/*  60 */       return bindings;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   private static final Logger logger = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private final Configuration configuration;
/*  70 */   private final ScriptEngineManager manager = new ScriptEngineManager();
/*  71 */   private final ConcurrentMap<String, ScriptRunner> scriptRunners = new ConcurrentHashMap<>();
/*     */   private final String languages;
/*     */   private final WatchManager watchManager;
/*     */   
/*     */   public ScriptManager(Configuration configuration, WatchManager watchManager) {
/*  76 */     this.configuration = configuration;
/*  77 */     this.watchManager = watchManager;
/*  78 */     List<ScriptEngineFactory> factories = this.manager.getEngineFactories();
/*  79 */     if (logger.isDebugEnabled()) {
/*  80 */       StringBuilder sb = new StringBuilder();
/*  81 */       int factorySize = factories.size();
/*  82 */       logger.debug("Installed {} script engine{}", Integer.valueOf(factorySize), (factorySize != 1) ? "s" : "");
/*  83 */       for (ScriptEngineFactory factory : factories) {
/*  84 */         String threading = Objects.toString(factory.getParameter("THREADING"), null);
/*  85 */         if (threading == null) {
/*  86 */           threading = "Not Thread Safe";
/*     */         }
/*  88 */         StringBuilder names = new StringBuilder();
/*  89 */         List<String> languageNames = factory.getNames();
/*  90 */         for (String name : languageNames) {
/*  91 */           if (names.length() > 0) {
/*  92 */             names.append(", ");
/*     */           }
/*  94 */           names.append(name);
/*     */         } 
/*  96 */         if (sb.length() > 0) {
/*  97 */           sb.append(", ");
/*     */         }
/*  99 */         sb.append(names);
/* 100 */         boolean compiled = factory.getScriptEngine() instanceof Compilable;
/* 101 */         logger.debug("{} version: {}, language: {}, threading: {}, compile: {}, names: {}, factory class: {}", factory
/* 102 */             .getEngineName(), factory.getEngineVersion(), factory.getLanguageName(), threading, 
/* 103 */             Boolean.valueOf(compiled), languageNames, factory.getClass().getName());
/*     */       } 
/* 105 */       this.languages = sb.toString();
/*     */     } else {
/* 107 */       StringBuilder names = new StringBuilder();
/* 108 */       for (ScriptEngineFactory factory : factories) {
/* 109 */         for (String name : factory.getNames()) {
/* 110 */           if (names.length() > 0) {
/* 111 */             names.append(", ");
/*     */           }
/* 113 */           names.append(name);
/*     */         } 
/*     */       } 
/* 116 */       this.languages = names.toString();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addScript(AbstractScript script) {
/* 121 */     ScriptEngine engine = this.manager.getEngineByName(script.getLanguage());
/* 122 */     if (engine == null) {
/* 123 */       logger.error("No ScriptEngine found for language " + script.getLanguage() + ". Available languages are: " + this.languages);
/*     */       
/*     */       return;
/*     */     } 
/* 127 */     if (engine.getFactory().getParameter("THREADING") == null) {
/* 128 */       this.scriptRunners.put(script.getName(), new ThreadLocalScriptRunner(script));
/*     */     } else {
/* 130 */       this.scriptRunners.put(script.getName(), new MainScriptRunner(engine, script));
/*     */     } 
/*     */     
/* 133 */     if (script instanceof ScriptFile) {
/* 134 */       ScriptFile scriptFile = (ScriptFile)script;
/* 135 */       Path path = scriptFile.getPath();
/* 136 */       if (scriptFile.isWatched() && path != null) {
/* 137 */         this.watchManager.watchFile(path.toFile(), this);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public Bindings createBindings(AbstractScript script) {
/* 143 */     return getScriptRunner(script).createBindings();
/*     */   }
/*     */   
/*     */   public AbstractScript getScript(String name) {
/* 147 */     ScriptRunner runner = this.scriptRunners.get(name);
/* 148 */     return (runner != null) ? runner.getScript() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void fileModified(File file) {
/* 153 */     ScriptRunner runner = this.scriptRunners.get(file.toString());
/* 154 */     if (runner == null) {
/* 155 */       logger.info("{} is not a running script", file.getName());
/*     */       return;
/*     */     } 
/* 158 */     ScriptEngine engine = runner.getScriptEngine();
/* 159 */     AbstractScript script = runner.getScript();
/* 160 */     if (engine.getFactory().getParameter("THREADING") == null) {
/* 161 */       this.scriptRunners.put(script.getName(), new ThreadLocalScriptRunner(script));
/*     */     } else {
/* 163 */       this.scriptRunners.put(script.getName(), new MainScriptRunner(engine, script));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Object execute(String name, Bindings bindings) {
/* 169 */     ScriptRunner scriptRunner = this.scriptRunners.get(name);
/* 170 */     if (scriptRunner == null) {
/* 171 */       logger.warn("No script named {} could be found", name);
/* 172 */       return null;
/*     */     } 
/* 174 */     return AccessController.doPrivileged(() -> scriptRunner.execute(bindings));
/*     */   }
/*     */   
/*     */   private static interface ScriptRunner {
/*     */     Bindings createBindings();
/*     */     
/*     */     Object execute(Bindings param1Bindings);
/*     */     
/*     */     AbstractScript getScript();
/*     */     
/*     */     ScriptEngine getScriptEngine();
/*     */   }
/*     */   
/*     */   private class MainScriptRunner
/*     */     extends AbstractScriptRunner {
/*     */     private final AbstractScript script;
/*     */     private final CompiledScript compiledScript;
/*     */     private final ScriptEngine scriptEngine;
/*     */     
/*     */     public MainScriptRunner(ScriptEngine scriptEngine, AbstractScript script) {
/* 194 */       this.script = script;
/* 195 */       this.scriptEngine = scriptEngine;
/* 196 */       CompiledScript compiled = null;
/* 197 */       if (scriptEngine instanceof Compilable) {
/* 198 */         ScriptManager.logger.debug("Script {} is compilable", script.getName());
/* 199 */         compiled = AccessController.<CompiledScript>doPrivileged(() -> {
/*     */               try {
/*     */                 return ((Compilable)scriptEngine).compile(script.getScriptText());
/* 202 */               } catch (Throwable ex) {
/*     */                 ScriptManager.logger.warn("Error compiling script", ex);
/*     */ 
/*     */                 
/*     */                 return null;
/*     */               } 
/*     */             });
/*     */       } 
/*     */ 
/*     */       
/* 212 */       this.compiledScript = compiled;
/*     */     }
/*     */ 
/*     */     
/*     */     public ScriptEngine getScriptEngine() {
/* 217 */       return this.scriptEngine;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object execute(Bindings bindings) {
/* 222 */       if (this.compiledScript != null) {
/*     */         try {
/* 224 */           return this.compiledScript.eval(bindings);
/* 225 */         } catch (ScriptException ex) {
/* 226 */           ScriptManager.logger.error("Error running script " + this.script.getName(), ex);
/* 227 */           return null;
/*     */         } 
/*     */       }
/*     */       try {
/* 231 */         return this.scriptEngine.eval(this.script.getScriptText(), bindings);
/* 232 */       } catch (ScriptException ex) {
/* 233 */         ScriptManager.logger.error("Error running script " + this.script.getName(), ex);
/* 234 */         return null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public AbstractScript getScript() {
/* 240 */       return this.script;
/*     */     }
/*     */   }
/*     */   
/*     */   private class ThreadLocalScriptRunner extends AbstractScriptRunner {
/*     */     private final AbstractScript script;
/*     */     
/* 247 */     private final ThreadLocal<ScriptManager.MainScriptRunner> runners = new ThreadLocal<ScriptManager.MainScriptRunner>()
/*     */       {
/*     */         protected ScriptManager.MainScriptRunner initialValue() {
/* 250 */           ScriptEngine engine = ScriptManager.this.manager.getEngineByName(ScriptManager.ThreadLocalScriptRunner.this.script.getLanguage());
/* 251 */           return new ScriptManager.MainScriptRunner(engine, ScriptManager.ThreadLocalScriptRunner.this.script);
/*     */         }
/*     */       };
/*     */     
/*     */     public ThreadLocalScriptRunner(AbstractScript script) {
/* 256 */       this.script = script;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object execute(Bindings bindings) {
/* 261 */       return ((ScriptManager.MainScriptRunner)this.runners.get()).execute(bindings);
/*     */     }
/*     */ 
/*     */     
/*     */     public AbstractScript getScript() {
/* 266 */       return this.script;
/*     */     }
/*     */ 
/*     */     
/*     */     public ScriptEngine getScriptEngine() {
/* 271 */       return ((ScriptManager.MainScriptRunner)this.runners.get()).getScriptEngine();
/*     */     }
/*     */   }
/*     */   
/*     */   private ScriptRunner getScriptRunner(AbstractScript script) {
/* 276 */     return this.scriptRunners.get(script.getName());
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\script\ScriptManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */