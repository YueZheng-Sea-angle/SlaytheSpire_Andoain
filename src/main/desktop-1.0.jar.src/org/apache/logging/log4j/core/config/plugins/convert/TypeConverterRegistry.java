/*     */ package org.apache.logging.log4j.core.config.plugins.convert;
/*     */ 
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.UnknownFormatConversionException;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginType;
/*     */ import org.apache.logging.log4j.core.util.ReflectionUtil;
/*     */ import org.apache.logging.log4j.core.util.TypeUtil;
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
/*     */ public class TypeConverterRegistry
/*     */ {
/*  42 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   private static volatile TypeConverterRegistry INSTANCE;
/*  44 */   private static final Object INSTANCE_LOCK = new Object();
/*     */   
/*  46 */   private final ConcurrentMap<Type, TypeConverter<?>> registry = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TypeConverterRegistry getInstance() {
/*  54 */     TypeConverterRegistry result = INSTANCE;
/*  55 */     if (result == null) {
/*  56 */       synchronized (INSTANCE_LOCK) {
/*  57 */         result = INSTANCE;
/*  58 */         if (result == null) {
/*  59 */           INSTANCE = result = new TypeConverterRegistry();
/*     */         }
/*     */       } 
/*     */     }
/*  63 */     return result;
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
/*     */   public TypeConverter<?> findCompatibleConverter(Type type) {
/*  77 */     Objects.requireNonNull(type, "No type was provided");
/*  78 */     TypeConverter<?> primary = this.registry.get(type);
/*     */     
/*  80 */     if (primary != null) {
/*  81 */       return primary;
/*     */     }
/*     */     
/*  84 */     if (type instanceof Class) {
/*  85 */       Class<?> clazz = (Class)type;
/*  86 */       if (clazz.isEnum()) {
/*     */         
/*  88 */         EnumConverter<? extends Enum> converter = new EnumConverter<>(clazz.asSubclass(Enum.class));
/*  89 */         synchronized (INSTANCE_LOCK) {
/*  90 */           return registerConverter(type, converter);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  95 */     for (Map.Entry<Type, TypeConverter<?>> entry : this.registry.entrySet()) {
/*  96 */       Type key = entry.getKey();
/*  97 */       if (TypeUtil.isAssignable(type, key)) {
/*  98 */         LOGGER.debug("Found compatible TypeConverter<{}> for type [{}].", key, type);
/*  99 */         TypeConverter<?> value = entry.getValue();
/* 100 */         synchronized (INSTANCE_LOCK) {
/* 101 */           return registerConverter(type, value);
/*     */         } 
/*     */       } 
/*     */     } 
/* 105 */     throw new UnknownFormatConversionException(type.toString());
/*     */   }
/*     */   
/*     */   private TypeConverterRegistry() {
/* 109 */     LOGGER.trace("TypeConverterRegistry initializing.");
/* 110 */     PluginManager manager = new PluginManager("TypeConverter");
/* 111 */     manager.collectPlugins();
/* 112 */     loadKnownTypeConverters(manager.getPlugins().values());
/* 113 */     registerPrimitiveTypes();
/*     */   }
/*     */   
/*     */   private void loadKnownTypeConverters(Collection<PluginType<?>> knownTypes) {
/* 117 */     for (PluginType<?> knownType : knownTypes) {
/* 118 */       Class<?> clazz = knownType.getPluginClass();
/* 119 */       if (TypeConverter.class.isAssignableFrom(clazz)) {
/*     */         
/* 121 */         Class<? extends TypeConverter> pluginClass = clazz.asSubclass(TypeConverter.class);
/* 122 */         Type conversionType = getTypeConverterSupportedType(pluginClass);
/* 123 */         TypeConverter<?> converter = (TypeConverter)ReflectionUtil.instantiate(pluginClass);
/* 124 */         registerConverter(conversionType, converter);
/*     */       } 
/*     */     } 
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
/*     */   private TypeConverter<?> registerConverter(Type conversionType, TypeConverter<?> converter) {
/* 139 */     TypeConverter<?> conflictingConverter = this.registry.get(conversionType);
/* 140 */     if (conflictingConverter != null) {
/*     */       boolean overridable;
/* 142 */       if (converter instanceof Comparable) {
/*     */         
/* 144 */         Comparable<TypeConverter<?>> comparableConverter = (Comparable)converter;
/*     */         
/* 146 */         overridable = (comparableConverter.compareTo(conflictingConverter) < 0);
/* 147 */       } else if (conflictingConverter instanceof Comparable) {
/*     */         
/* 149 */         Comparable<TypeConverter<?>> comparableConflictingConverter = (Comparable)conflictingConverter;
/*     */         
/* 151 */         overridable = (comparableConflictingConverter.compareTo(converter) > 0);
/*     */       } else {
/* 153 */         overridable = false;
/*     */       } 
/* 155 */       if (overridable) {
/* 156 */         LOGGER.debug("Replacing TypeConverter [{}] for type [{}] with [{}] after comparison.", conflictingConverter, conversionType, converter);
/*     */ 
/*     */         
/* 159 */         this.registry.put(conversionType, converter);
/* 160 */         return converter;
/*     */       } 
/* 162 */       LOGGER.warn("Ignoring TypeConverter [{}] for type [{}] that conflicts with [{}], since they are not comparable.", converter, conversionType, conflictingConverter);
/*     */ 
/*     */       
/* 165 */       return conflictingConverter;
/*     */     } 
/*     */     
/* 168 */     this.registry.put(conversionType, converter);
/* 169 */     return converter;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Type getTypeConverterSupportedType(Class<? extends TypeConverter> typeConverterClass) {
/* 174 */     for (Type type : typeConverterClass.getGenericInterfaces()) {
/* 175 */       if (type instanceof ParameterizedType) {
/* 176 */         ParameterizedType pType = (ParameterizedType)type;
/* 177 */         if (TypeConverter.class.equals(pType.getRawType()))
/*     */         {
/* 179 */           return pType.getActualTypeArguments()[0];
/*     */         }
/*     */       } 
/*     */     } 
/* 183 */     return void.class;
/*     */   }
/*     */   
/*     */   private void registerPrimitiveTypes() {
/* 187 */     registerTypeAlias(Boolean.class, boolean.class);
/* 188 */     registerTypeAlias(Byte.class, byte.class);
/* 189 */     registerTypeAlias(Character.class, char.class);
/* 190 */     registerTypeAlias(Double.class, double.class);
/* 191 */     registerTypeAlias(Float.class, float.class);
/* 192 */     registerTypeAlias(Integer.class, int.class);
/* 193 */     registerTypeAlias(Long.class, long.class);
/* 194 */     registerTypeAlias(Short.class, short.class);
/*     */   }
/*     */   
/*     */   private void registerTypeAlias(Type knownType, Type aliasType) {
/* 198 */     this.registry.putIfAbsent(aliasType, this.registry.get(knownType));
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\plugins\convert\TypeConverterRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */