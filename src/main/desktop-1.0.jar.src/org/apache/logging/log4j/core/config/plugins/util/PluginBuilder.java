/*     */ package org.apache.logging.log4j.core.config.plugins.util;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationException;
/*     */ import org.apache.logging.log4j.core.config.Node;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAliases;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.ConstraintValidator;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.ConstraintValidators;
/*     */ import org.apache.logging.log4j.core.config.plugins.visitors.PluginVisitor;
/*     */ import org.apache.logging.log4j.core.config.plugins.visitors.PluginVisitors;
/*     */ import org.apache.logging.log4j.core.util.Builder;
/*     */ import org.apache.logging.log4j.core.util.ReflectionUtil;
/*     */ import org.apache.logging.log4j.core.util.TypeUtil;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.StringBuilders;
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
/*     */ public class PluginBuilder
/*     */   implements Builder<Object>
/*     */ {
/*  55 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */   
/*     */   private final PluginType<?> pluginType;
/*     */   
/*     */   private final Class<?> clazz;
/*     */   
/*     */   private Configuration configuration;
/*     */   
/*     */   private Node node;
/*     */   
/*     */   private LogEvent event;
/*     */ 
/*     */   
/*     */   public PluginBuilder(PluginType<?> pluginType) {
/*  70 */     this.pluginType = pluginType;
/*  71 */     this.clazz = pluginType.getPluginClass();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PluginBuilder withConfiguration(Configuration configuration) {
/*  81 */     this.configuration = configuration;
/*  82 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PluginBuilder withConfigurationNode(Node node) {
/*  92 */     this.node = node;
/*  93 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PluginBuilder forLogEvent(LogEvent event) {
/* 103 */     this.event = event;
/* 104 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object build() {
/* 114 */     verify();
/*     */     
/*     */     try {
/* 117 */       LOGGER.debug("Building Plugin[name={}, class={}].", this.pluginType.getElementName(), this.pluginType
/* 118 */           .getPluginClass().getName());
/* 119 */       Builder<?> builder = createBuilder(this.clazz);
/* 120 */       if (builder != null) {
/* 121 */         injectFields(builder);
/* 122 */         return builder.build();
/*     */       } 
/* 124 */     } catch (ConfigurationException e) {
/* 125 */       LOGGER.error("Could not create plugin of type {} for element {}", this.clazz, this.node.getName(), e);
/* 126 */       return null;
/* 127 */     } catch (Throwable t) {
/* 128 */       LOGGER.error("Could not create plugin of type {} for element {}: {}", this.clazz, this.node
/* 129 */           .getName(), ((t instanceof InvocationTargetException) ? ((InvocationTargetException)t)
/* 130 */           .getCause() : t).toString(), t);
/*     */     } 
/*     */     
/*     */     try {
/* 134 */       Method factory = findFactoryMethod(this.clazz);
/* 135 */       Object[] params = generateParameters(factory);
/* 136 */       return factory.invoke((Object)null, params);
/* 137 */     } catch (Throwable t) {
/* 138 */       LOGGER.error("Unable to invoke factory method in {} for element {}: {}", this.clazz, this.node
/* 139 */           .getName(), ((t instanceof InvocationTargetException) ? ((InvocationTargetException)t)
/* 140 */           .getCause() : t).toString(), t);
/* 141 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void verify() {
/* 146 */     Objects.requireNonNull(this.configuration, "No Configuration object was set.");
/* 147 */     Objects.requireNonNull(this.node, "No Node object was set.");
/*     */   }
/*     */ 
/*     */   
/*     */   private static Builder<?> createBuilder(Class<?> clazz) throws InvocationTargetException, IllegalAccessException {
/* 152 */     for (Method method : clazz.getDeclaredMethods()) {
/* 153 */       if (method.isAnnotationPresent((Class)PluginBuilderFactory.class) && 
/* 154 */         Modifier.isStatic(method.getModifiers()) && 
/* 155 */         TypeUtil.isAssignable(Builder.class, method.getReturnType())) {
/* 156 */         ReflectionUtil.makeAccessible(method);
/* 157 */         return (Builder)method.invoke((Object)null, new Object[0]);
/*     */       } 
/*     */     } 
/* 160 */     return null;
/*     */   }
/*     */   
/*     */   private void injectFields(Builder<?> builder) throws IllegalAccessException {
/* 164 */     List<Field> fields = TypeUtil.getAllDeclaredFields(builder.getClass());
/* 165 */     AccessibleObject.setAccessible(fields.<AccessibleObject>toArray((AccessibleObject[])new Field[0]), true);
/* 166 */     StringBuilder log = new StringBuilder();
/* 167 */     boolean invalid = false;
/* 168 */     String reason = "";
/* 169 */     for (Field field : fields) {
/* 170 */       log.append((log.length() == 0) ? (simpleName(builder) + "(") : ", ");
/* 171 */       Annotation[] annotations = field.getDeclaredAnnotations();
/* 172 */       String[] aliases = extractPluginAliases(annotations);
/* 173 */       for (Annotation a : annotations) {
/* 174 */         if (!(a instanceof PluginAliases)) {
/*     */ 
/*     */ 
/*     */           
/* 178 */           PluginVisitor<? extends Annotation> visitor = PluginVisitors.findVisitor(a.annotationType());
/* 179 */           if (visitor != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 187 */             Object object = visitor.setAliases(aliases).setAnnotation(a).setConversionType(field.getType()).setStrSubstitutor((this.event == null) ? this.configuration.getConfigurationStrSubstitutor() : this.configuration.getStrSubstitutor()).setMember(field).visit(this.configuration, this.node, this.event, log);
/*     */             
/* 189 */             if (object != null) {
/* 190 */               field.set(builder, object);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/* 195 */       Collection<ConstraintValidator<?>> validators = ConstraintValidators.findValidators(annotations);
/* 196 */       Object value = field.get(builder);
/* 197 */       for (ConstraintValidator<?> validator : validators) {
/* 198 */         if (!validator.isValid(field.getName(), value)) {
/* 199 */           invalid = true;
/* 200 */           if (!reason.isEmpty()) {
/* 201 */             reason = reason + ", ";
/*     */           }
/* 203 */           reason = reason + "field '" + field.getName() + "' has invalid value '" + value + "'";
/*     */         } 
/*     */       } 
/*     */     } 
/* 207 */     log.append((log.length() == 0) ? (builder.getClass().getSimpleName() + "()") : ")");
/* 208 */     LOGGER.debug(log.toString());
/* 209 */     if (invalid) {
/* 210 */       throw new ConfigurationException("Arguments given for element " + this.node.getName() + " are invalid: " + reason);
/*     */     }
/* 212 */     checkForRemainingAttributes();
/* 213 */     verifyNodeChildrenUsed();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String simpleName(Object object) {
/* 220 */     if (object == null) {
/* 221 */       return "null";
/*     */     }
/* 223 */     String cls = object.getClass().getName();
/* 224 */     int index = cls.lastIndexOf('.');
/* 225 */     return (index < 0) ? cls : cls.substring(index + 1);
/*     */   }
/*     */   
/*     */   private static Method findFactoryMethod(Class<?> clazz) {
/* 229 */     for (Method method : clazz.getDeclaredMethods()) {
/* 230 */       if (method.isAnnotationPresent((Class)PluginFactory.class) && 
/* 231 */         Modifier.isStatic(method.getModifiers())) {
/* 232 */         ReflectionUtil.makeAccessible(method);
/* 233 */         return method;
/*     */       } 
/*     */     } 
/* 236 */     throw new IllegalStateException("No factory method found for class " + clazz.getName());
/*     */   }
/*     */   
/*     */   private Object[] generateParameters(Method factory) {
/* 240 */     StringBuilder log = new StringBuilder();
/* 241 */     Class<?>[] types = factory.getParameterTypes();
/* 242 */     Annotation[][] annotations = factory.getParameterAnnotations();
/* 243 */     Object[] args = new Object[annotations.length];
/* 244 */     boolean invalid = false;
/* 245 */     for (int i = 0; i < annotations.length; i++) {
/* 246 */       log.append((log.length() == 0) ? (factory.getName() + "(") : ", ");
/* 247 */       String[] aliases = extractPluginAliases(annotations[i]);
/* 248 */       for (Annotation a : annotations[i]) {
/* 249 */         if (!(a instanceof PluginAliases)) {
/*     */ 
/*     */           
/* 252 */           PluginVisitor<? extends Annotation> visitor = PluginVisitors.findVisitor(a
/* 253 */               .annotationType());
/* 254 */           if (visitor != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 262 */             Object object = visitor.setAliases(aliases).setAnnotation(a).setConversionType(types[i]).setStrSubstitutor((this.event == null) ? this.configuration.getConfigurationStrSubstitutor() : this.configuration.getStrSubstitutor()).setMember(factory).visit(this.configuration, this.node, this.event, log);
/*     */             
/* 264 */             if (object != null) {
/* 265 */               args[i] = object;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/* 270 */       Collection<ConstraintValidator<?>> validators = ConstraintValidators.findValidators(annotations[i]);
/* 271 */       Object value = args[i];
/* 272 */       String argName = "arg[" + i + "](" + simpleName(value) + ")";
/* 273 */       for (ConstraintValidator<?> validator : validators) {
/* 274 */         if (!validator.isValid(argName, value)) {
/* 275 */           invalid = true;
/*     */         }
/*     */       } 
/*     */     } 
/* 279 */     log.append((log.length() == 0) ? (factory.getName() + "()") : ")");
/* 280 */     checkForRemainingAttributes();
/* 281 */     verifyNodeChildrenUsed();
/* 282 */     LOGGER.debug(log.toString());
/* 283 */     if (invalid) {
/* 284 */       throw new ConfigurationException("Arguments given for element " + this.node.getName() + " are invalid");
/*     */     }
/* 286 */     return args;
/*     */   }
/*     */   
/*     */   private static String[] extractPluginAliases(Annotation... parmTypes) {
/* 290 */     String[] aliases = null;
/* 291 */     for (Annotation a : parmTypes) {
/* 292 */       if (a instanceof PluginAliases) {
/* 293 */         aliases = ((PluginAliases)a).value();
/*     */       }
/*     */     } 
/* 296 */     return aliases;
/*     */   }
/*     */   
/*     */   private void checkForRemainingAttributes() {
/* 300 */     Map<String, String> attrs = this.node.getAttributes();
/* 301 */     if (!attrs.isEmpty()) {
/* 302 */       StringBuilder sb = new StringBuilder();
/* 303 */       for (String key : attrs.keySet()) {
/* 304 */         if (sb.length() == 0) {
/* 305 */           sb.append(this.node.getName());
/* 306 */           sb.append(" contains ");
/* 307 */           if (attrs.size() == 1) {
/* 308 */             sb.append("an invalid element or attribute ");
/*     */           } else {
/* 310 */             sb.append("invalid attributes ");
/*     */           } 
/*     */         } else {
/* 313 */           sb.append(", ");
/*     */         } 
/* 315 */         StringBuilders.appendDqValue(sb, key);
/*     */       } 
/* 317 */       LOGGER.error(sb.toString());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void verifyNodeChildrenUsed() {
/* 322 */     List<Node> children = this.node.getChildren();
/* 323 */     if (!this.pluginType.isDeferChildren() && !children.isEmpty())
/* 324 */       for (Node child : children) {
/* 325 */         String nodeType = this.node.getType().getElementName();
/* 326 */         String start = nodeType.equals(this.node.getName()) ? this.node.getName() : (nodeType + ' ' + this.node.getName());
/* 327 */         LOGGER.error("{} has no parameter that matches element {}", start, child.getName());
/*     */       }  
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\plugin\\util\PluginBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */