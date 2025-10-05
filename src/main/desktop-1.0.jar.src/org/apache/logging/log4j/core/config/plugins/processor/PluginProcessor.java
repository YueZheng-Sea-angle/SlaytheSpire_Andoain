/*     */ package org.apache.logging.log4j.core.config.plugins.processor;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import javax.annotation.processing.AbstractProcessor;
/*     */ import javax.annotation.processing.Messager;
/*     */ import javax.annotation.processing.RoundEnvironment;
/*     */ import javax.annotation.processing.SupportedAnnotationTypes;
/*     */ import javax.lang.model.SourceVersion;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ElementVisitor;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.util.Elements;
/*     */ import javax.lang.model.util.SimpleElementVisitor7;
/*     */ import javax.tools.Diagnostic;
/*     */ import javax.tools.FileObject;
/*     */ import javax.tools.StandardLocation;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAliases;
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
/*     */ @SupportedAnnotationTypes({"org.apache.logging.log4j.core.config.plugins.*"})
/*     */ public class PluginProcessor
/*     */   extends AbstractProcessor
/*     */ {
/*     */   public static final String PLUGIN_CACHE_FILE = "META-INF/org/apache/logging/log4j/core/config/plugins/Log4j2Plugins.dat";
/*  62 */   private final PluginCache pluginCache = new PluginCache();
/*     */ 
/*     */   
/*     */   public SourceVersion getSupportedSourceVersion() {
/*  66 */     return SourceVersion.latest();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
/*  71 */     Messager messager = this.processingEnv.getMessager();
/*  72 */     messager.printMessage(Diagnostic.Kind.NOTE, "Processing Log4j annotations");
/*     */     try {
/*  74 */       Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith((Class)Plugin.class);
/*  75 */       if (elements.isEmpty()) {
/*  76 */         messager.printMessage(Diagnostic.Kind.NOTE, "No elements to process");
/*  77 */         return false;
/*     */       } 
/*  79 */       collectPlugins(elements);
/*  80 */       writeCacheFile(elements.<Element>toArray(new Element[elements.size()]));
/*  81 */       messager.printMessage(Diagnostic.Kind.NOTE, "Annotations processed");
/*  82 */       return true;
/*  83 */     } catch (Exception ex) {
/*  84 */       ex.printStackTrace();
/*  85 */       error(ex.getMessage());
/*  86 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void error(CharSequence message) {
/*  91 */     this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message);
/*     */   }
/*     */   
/*     */   private void collectPlugins(Iterable<? extends Element> elements) {
/*  95 */     Elements elementUtils = this.processingEnv.getElementUtils();
/*  96 */     ElementVisitor<PluginEntry, Plugin> pluginVisitor = new PluginElementVisitor(elementUtils);
/*  97 */     ElementVisitor<Collection<PluginEntry>, Plugin> pluginAliasesVisitor = new PluginAliasesElementVisitor(elementUtils);
/*     */     
/*  99 */     for (Element element : elements) {
/* 100 */       Plugin plugin = element.<Plugin>getAnnotation(Plugin.class);
/* 101 */       if (plugin == null) {
/*     */         continue;
/*     */       }
/* 104 */       PluginEntry entry = element.<PluginEntry, Plugin>accept(pluginVisitor, plugin);
/* 105 */       Map<String, PluginEntry> category = this.pluginCache.getCategory(entry.getCategory());
/* 106 */       category.put(entry.getKey(), entry);
/* 107 */       Collection<PluginEntry> entries = element.<Collection<PluginEntry>, Plugin>accept(pluginAliasesVisitor, plugin);
/* 108 */       for (PluginEntry pluginEntry : entries) {
/* 109 */         category.put(pluginEntry.getKey(), pluginEntry);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeCacheFile(Element... elements) throws IOException {
/* 115 */     FileObject fileObject = this.processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/org/apache/logging/log4j/core/config/plugins/Log4j2Plugins.dat", elements);
/*     */     
/* 117 */     try (OutputStream out = fileObject.openOutputStream()) {
/* 118 */       this.pluginCache.writeCache(out);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static class PluginElementVisitor
/*     */     extends SimpleElementVisitor7<PluginEntry, Plugin>
/*     */   {
/*     */     private final Elements elements;
/*     */ 
/*     */     
/*     */     private PluginElementVisitor(Elements elements) {
/* 130 */       this.elements = elements;
/*     */     }
/*     */ 
/*     */     
/*     */     public PluginEntry visitType(TypeElement e, Plugin plugin) {
/* 135 */       Objects.requireNonNull(plugin, "Plugin annotation is null.");
/* 136 */       PluginEntry entry = new PluginEntry();
/* 137 */       entry.setKey(plugin.name().toLowerCase(Locale.US));
/* 138 */       entry.setClassName(this.elements.getBinaryName(e).toString());
/* 139 */       entry.setName("".equals(plugin.elementType()) ? plugin.name() : plugin.elementType());
/* 140 */       entry.setPrintable(plugin.printObject());
/* 141 */       entry.setDefer(plugin.deferChildren());
/* 142 */       entry.setCategory(plugin.category());
/* 143 */       return entry;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class PluginAliasesElementVisitor
/*     */     extends SimpleElementVisitor7<Collection<PluginEntry>, Plugin>
/*     */   {
/*     */     private final Elements elements;
/*     */ 
/*     */     
/*     */     private PluginAliasesElementVisitor(Elements elements) {
/* 155 */       super(Collections.emptyList());
/* 156 */       this.elements = elements;
/*     */     }
/*     */ 
/*     */     
/*     */     public Collection<PluginEntry> visitType(TypeElement e, Plugin plugin) {
/* 161 */       PluginAliases aliases = e.<PluginAliases>getAnnotation(PluginAliases.class);
/* 162 */       if (aliases == null) {
/* 163 */         return this.DEFAULT_VALUE;
/*     */       }
/* 165 */       Collection<PluginEntry> entries = new ArrayList<>((aliases.value()).length);
/* 166 */       for (String alias : aliases.value()) {
/* 167 */         PluginEntry entry = new PluginEntry();
/* 168 */         entry.setKey(alias.toLowerCase(Locale.US));
/* 169 */         entry.setClassName(this.elements.getBinaryName(e).toString());
/* 170 */         entry.setName("".equals(plugin.elementType()) ? alias : plugin.elementType());
/* 171 */         entry.setPrintable(plugin.printObject());
/* 172 */         entry.setDefer(plugin.deferChildren());
/* 173 */         entry.setCategory(plugin.category());
/* 174 */         entries.add(entry);
/*     */       } 
/* 176 */       return entries;
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\plugins\processor\PluginProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */