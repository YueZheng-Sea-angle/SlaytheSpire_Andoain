/*     */ package org.apache.logging.log4j.core.config.plugins.convert;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.net.InetAddress;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.security.Provider;
/*     */ import java.security.Security;
/*     */ import java.util.UUID;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.appender.rolling.action.Duration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.util.CronExpression;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.Constants;
/*     */ import org.apache.logging.log4j.util.LoaderUtil;
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
/*     */ public final class TypeConverters
/*     */ {
/*     */   public static final String CATEGORY = "TypeConverter";
/*     */   
/*     */   @Plugin(name = "BigDecimal", category = "TypeConverter")
/*     */   public static class BigDecimalConverter
/*     */     implements TypeConverter<BigDecimal>
/*     */   {
/*     */     public BigDecimal convert(String s) {
/*  67 */       return new BigDecimal(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "BigInteger", category = "TypeConverter")
/*     */   public static class BigIntegerConverter
/*     */     implements TypeConverter<BigInteger>
/*     */   {
/*     */     public BigInteger convert(String s) {
/*  78 */       return new BigInteger(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "Boolean", category = "TypeConverter")
/*     */   public static class BooleanConverter
/*     */     implements TypeConverter<Boolean>
/*     */   {
/*     */     public Boolean convert(String s) {
/*  89 */       return Boolean.valueOf(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "ByteArray", category = "TypeConverter")
/*     */   public static class ByteArrayConverter
/*     */     implements TypeConverter<byte[]>
/*     */   {
/*     */     private static final String PREFIX_0x = "0x";
/*     */ 
/*     */ 
/*     */     
/*     */     private static final String PREFIX_BASE64 = "Base64:";
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public byte[] convert(String value) {
/*     */       byte[] bytes;
/* 112 */       if (value == null || value.isEmpty()) {
/* 113 */         bytes = Constants.EMPTY_BYTE_ARRAY;
/* 114 */       } else if (value.startsWith("Base64:")) {
/* 115 */         String lexicalXSDBase64Binary = value.substring("Base64:".length());
/* 116 */         bytes = Base64Converter.parseBase64Binary(lexicalXSDBase64Binary);
/* 117 */       } else if (value.startsWith("0x")) {
/* 118 */         String lexicalXSDHexBinary = value.substring("0x".length());
/* 119 */         bytes = HexConverter.parseHexBinary(lexicalXSDHexBinary);
/*     */       } else {
/* 121 */         bytes = value.getBytes(Charset.defaultCharset());
/*     */       } 
/* 123 */       return bytes;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "Byte", category = "TypeConverter")
/*     */   public static class ByteConverter
/*     */     implements TypeConverter<Byte>
/*     */   {
/*     */     public Byte convert(String s) {
/* 134 */       return Byte.valueOf(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "Character", category = "TypeConverter")
/*     */   public static class CharacterConverter
/*     */     implements TypeConverter<Character>
/*     */   {
/*     */     public Character convert(String s) {
/* 145 */       if (s.length() != 1) {
/* 146 */         throw new IllegalArgumentException("Character string must be of length 1: " + s);
/*     */       }
/* 148 */       return Character.valueOf(s.toCharArray()[0]);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "CharacterArray", category = "TypeConverter")
/*     */   public static class CharArrayConverter
/*     */     implements TypeConverter<char[]>
/*     */   {
/*     */     public char[] convert(String s) {
/* 159 */       return s.toCharArray();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "Charset", category = "TypeConverter")
/*     */   public static class CharsetConverter
/*     */     implements TypeConverter<Charset>
/*     */   {
/*     */     public Charset convert(String s) {
/* 170 */       return Charset.forName(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "Class", category = "TypeConverter")
/*     */   public static class ClassConverter
/*     */     implements TypeConverter<Class<?>>
/*     */   {
/*     */     public Class<?> convert(String s) throws ClassNotFoundException {
/* 181 */       switch (s.toLowerCase()) {
/*     */         case "boolean":
/* 183 */           return boolean.class;
/*     */         case "byte":
/* 185 */           return byte.class;
/*     */         case "char":
/* 187 */           return char.class;
/*     */         case "double":
/* 189 */           return double.class;
/*     */         case "float":
/* 191 */           return float.class;
/*     */         case "int":
/* 193 */           return int.class;
/*     */         case "long":
/* 195 */           return long.class;
/*     */         case "short":
/* 197 */           return short.class;
/*     */         case "void":
/* 199 */           return void.class;
/*     */       } 
/* 201 */       return LoaderUtil.loadClass(s);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Plugin(name = "CronExpression", category = "TypeConverter")
/*     */   public static class CronExpressionConverter
/*     */     implements TypeConverter<CronExpression>
/*     */   {
/*     */     public CronExpression convert(String s) throws Exception {
/* 211 */       return new CronExpression(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "Double", category = "TypeConverter")
/*     */   public static class DoubleConverter
/*     */     implements TypeConverter<Double>
/*     */   {
/*     */     public Double convert(String s) {
/* 222 */       return Double.valueOf(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "Duration", category = "TypeConverter")
/*     */   public static class DurationConverter
/*     */     implements TypeConverter<Duration>
/*     */   {
/*     */     public Duration convert(String s) {
/* 234 */       return Duration.parse(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "File", category = "TypeConverter")
/*     */   public static class FileConverter
/*     */     implements TypeConverter<File>
/*     */   {
/*     */     public File convert(String s) {
/* 245 */       return new File(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "Float", category = "TypeConverter")
/*     */   public static class FloatConverter
/*     */     implements TypeConverter<Float>
/*     */   {
/*     */     public Float convert(String s) {
/* 256 */       return Float.valueOf(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "InetAddress", category = "TypeConverter")
/*     */   public static class InetAddressConverter
/*     */     implements TypeConverter<InetAddress>
/*     */   {
/*     */     public InetAddress convert(String s) throws Exception {
/* 267 */       return InetAddress.getByName(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "Integer", category = "TypeConverter")
/*     */   public static class IntegerConverter
/*     */     implements TypeConverter<Integer>
/*     */   {
/*     */     public Integer convert(String s) {
/* 278 */       return Integer.valueOf(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "Level", category = "TypeConverter")
/*     */   public static class LevelConverter
/*     */     implements TypeConverter<Level>
/*     */   {
/*     */     public Level convert(String s) {
/* 289 */       return Level.valueOf(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "Long", category = "TypeConverter")
/*     */   public static class LongConverter
/*     */     implements TypeConverter<Long>
/*     */   {
/*     */     public Long convert(String s) {
/* 300 */       return Long.valueOf(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "Path", category = "TypeConverter")
/*     */   public static class PathConverter
/*     */     implements TypeConverter<Path>
/*     */   {
/*     */     public Path convert(String s) throws Exception {
/* 312 */       return Paths.get(s, new String[0]);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "Pattern", category = "TypeConverter")
/*     */   public static class PatternConverter
/*     */     implements TypeConverter<Pattern>
/*     */   {
/*     */     public Pattern convert(String s) {
/* 323 */       return Pattern.compile(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "SecurityProvider", category = "TypeConverter")
/*     */   public static class SecurityProviderConverter
/*     */     implements TypeConverter<Provider>
/*     */   {
/*     */     public Provider convert(String s) {
/* 334 */       return Security.getProvider(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "Short", category = "TypeConverter")
/*     */   public static class ShortConverter
/*     */     implements TypeConverter<Short>
/*     */   {
/*     */     public Short convert(String s) {
/* 345 */       return Short.valueOf(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "String", category = "TypeConverter")
/*     */   public static class StringConverter
/*     */     implements TypeConverter<String>
/*     */   {
/*     */     public String convert(String s) {
/* 356 */       return s;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "URI", category = "TypeConverter")
/*     */   public static class UriConverter
/*     */     implements TypeConverter<URI>
/*     */   {
/*     */     public URI convert(String s) throws URISyntaxException {
/* 367 */       return new URI(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "URL", category = "TypeConverter")
/*     */   public static class UrlConverter
/*     */     implements TypeConverter<URL>
/*     */   {
/*     */     public URL convert(String s) throws MalformedURLException {
/* 378 */       return new URL(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "UUID", category = "TypeConverter")
/*     */   public static class UuidConverter
/*     */     implements TypeConverter<UUID>
/*     */   {
/*     */     public UUID convert(String s) throws Exception {
/* 390 */       return UUID.fromString(s);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T convert(String s, Class<? extends T> clazz, Object defaultValue) {
/* 413 */     TypeConverter<T> converter = (TypeConverter)TypeConverterRegistry.getInstance().findCompatibleConverter(clazz);
/* 414 */     if (s == null)
/*     */     {
/*     */       
/* 417 */       return parseDefaultValue(converter, defaultValue);
/*     */     }
/*     */     try {
/* 420 */       return converter.convert(s);
/* 421 */     } catch (Exception e) {
/* 422 */       LOGGER.warn("Error while converting string [{}] to type [{}]. Using default value [{}].", s, clazz, defaultValue, e);
/*     */       
/* 424 */       return parseDefaultValue(converter, defaultValue);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T> T parseDefaultValue(TypeConverter<T> converter, Object defaultValue) {
/* 430 */     if (defaultValue == null) {
/* 431 */       return null;
/*     */     }
/* 433 */     if (!(defaultValue instanceof String)) {
/* 434 */       return (T)defaultValue;
/*     */     }
/*     */     try {
/* 437 */       return converter.convert((String)defaultValue);
/* 438 */     } catch (Exception e) {
/* 439 */       LOGGER.debug("Can't parse default value [{}] for type [{}].", defaultValue, converter.getClass(), e);
/* 440 */       return null;
/*     */     } 
/*     */   }
/*     */   
/* 444 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\plugins\convert\TypeConverters.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */