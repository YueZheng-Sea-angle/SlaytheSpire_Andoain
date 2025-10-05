/*     */ package org.apache.commons.net.ftp.parser;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.commons.net.ftp.FTPClientConfig;
/*     */ import org.apache.commons.net.ftp.FTPFileEntryParser;
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
/*     */ 
/*     */ 
/*     */ public class DefaultFTPFileEntryParserFactory
/*     */   implements FTPFileEntryParserFactory
/*     */ {
/*     */   private static final String JAVA_IDENTIFIER = "\\p{javaJavaIdentifierStart}(\\p{javaJavaIdentifierPart})*";
/*     */   private static final String JAVA_QUALIFIED_NAME = "(\\p{javaJavaIdentifierStart}(\\p{javaJavaIdentifierPart})*\\.)+\\p{javaJavaIdentifierStart}(\\p{javaJavaIdentifierPart})*";
/*  46 */   private static final Pattern JAVA_QUALIFIED_NAME_PATTERN = Pattern.compile("(\\p{javaJavaIdentifierStart}(\\p{javaJavaIdentifierPart})*\\.)+\\p{javaJavaIdentifierStart}(\\p{javaJavaIdentifierPart})*");
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
/*     */   public FTPFileEntryParser createFileEntryParser(String key) {
/*  91 */     if (key == null) {
/*  92 */       throw new ParserInitializationException("Parser key cannot be null");
/*     */     }
/*  94 */     return createFileEntryParser(key, null);
/*     */   }
/*     */   
/*     */   private FTPFileEntryParser createFileEntryParser(String key, FTPClientConfig config) {
/*     */     UnixFTPEntryParser unixFTPEntryParser;
/*  99 */     FTPFileEntryParser parser = null;
/*     */ 
/*     */     
/* 102 */     if (JAVA_QUALIFIED_NAME_PATTERN.matcher(key).matches()) {
/*     */       
/*     */       try {
/* 105 */         Class<?> parserClass = Class.forName(key);
/*     */         try {
/* 107 */           parser = (FTPFileEntryParser)parserClass.newInstance();
/* 108 */         } catch (ClassCastException e) {
/* 109 */           throw new ParserInitializationException(parserClass.getName() + " does not implement the interface " + "org.apache.commons.net.ftp.FTPFileEntryParser.", e);
/*     */         
/*     */         }
/* 112 */         catch (Exception e) {
/* 113 */           throw new ParserInitializationException("Error initializing parser", e);
/* 114 */         } catch (ExceptionInInitializerError e) {
/* 115 */           throw new ParserInitializationException("Error initializing parser", e);
/*     */         } 
/* 117 */       } catch (ClassNotFoundException e) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 122 */     if (parser == null) {
/* 123 */       String ukey = key.toUpperCase(Locale.ENGLISH);
/* 124 */       if (ukey.indexOf("UNIX") >= 0) {
/*     */         
/* 126 */         unixFTPEntryParser = new UnixFTPEntryParser(config, false);
/*     */       }
/* 128 */       else if (ukey.indexOf("UNIX_LTRIM") >= 0) {
/*     */         
/* 130 */         unixFTPEntryParser = new UnixFTPEntryParser(config, true);
/*     */       }
/* 132 */       else if (ukey.indexOf("VMS") >= 0) {
/*     */         
/* 134 */         VMSVersioningFTPEntryParser vMSVersioningFTPEntryParser = new VMSVersioningFTPEntryParser(config);
/*     */       }
/* 136 */       else if (ukey.indexOf("WINDOWS") >= 0) {
/*     */         
/* 138 */         parser = createNTFTPEntryParser(config);
/*     */       }
/* 140 */       else if (ukey.indexOf("OS/2") >= 0) {
/*     */         
/* 142 */         OS2FTPEntryParser oS2FTPEntryParser = new OS2FTPEntryParser(config);
/*     */       }
/* 144 */       else if (ukey.indexOf("OS/400") >= 0 || ukey.indexOf("AS/400") >= 0) {
/*     */ 
/*     */         
/* 147 */         parser = createOS400FTPEntryParser(config);
/*     */       }
/* 149 */       else if (ukey.indexOf("MVS") >= 0) {
/*     */         
/* 151 */         MVSFTPEntryParser mVSFTPEntryParser = new MVSFTPEntryParser();
/*     */       }
/* 153 */       else if (ukey.indexOf("NETWARE") >= 0) {
/*     */         
/* 155 */         NetwareFTPEntryParser netwareFTPEntryParser = new NetwareFTPEntryParser(config);
/*     */       }
/* 157 */       else if (ukey.indexOf("MACOS PETER") >= 0) {
/*     */         
/* 159 */         MacOsPeterFTPEntryParser macOsPeterFTPEntryParser = new MacOsPeterFTPEntryParser(config);
/*     */       }
/* 161 */       else if (ukey.indexOf("TYPE: L8") >= 0) {
/*     */ 
/*     */ 
/*     */         
/* 165 */         unixFTPEntryParser = new UnixFTPEntryParser(config);
/*     */       }
/*     */       else {
/*     */         
/* 169 */         throw new ParserInitializationException("Unknown parser type: " + key);
/*     */       } 
/*     */     } 
/*     */     
/* 173 */     if (unixFTPEntryParser instanceof org.apache.commons.net.ftp.Configurable) {
/* 174 */       unixFTPEntryParser.configure(config);
/*     */     }
/* 176 */     return (FTPFileEntryParser)unixFTPEntryParser;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTPFileEntryParser createFileEntryParser(FTPClientConfig config) throws ParserInitializationException {
/* 204 */     String key = config.getServerSystemKey();
/* 205 */     return createFileEntryParser(key, config);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FTPFileEntryParser createUnixFTPEntryParser() {
/* 211 */     return (FTPFileEntryParser)new UnixFTPEntryParser();
/*     */   }
/*     */ 
/*     */   
/*     */   public FTPFileEntryParser createVMSVersioningFTPEntryParser() {
/* 216 */     return (FTPFileEntryParser)new VMSVersioningFTPEntryParser();
/*     */   }
/*     */   
/*     */   public FTPFileEntryParser createNetwareFTPEntryParser() {
/* 220 */     return (FTPFileEntryParser)new NetwareFTPEntryParser();
/*     */   }
/*     */ 
/*     */   
/*     */   public FTPFileEntryParser createNTFTPEntryParser() {
/* 225 */     return createNTFTPEntryParser(null);
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
/*     */   private FTPFileEntryParser createNTFTPEntryParser(FTPClientConfig config) {
/* 237 */     if (config != null && "WINDOWS".equals(config.getServerSystemKey()))
/*     */     {
/*     */       
/* 240 */       return (FTPFileEntryParser)new NTFTPEntryParser(config);
/*     */     }
/* 242 */     return (FTPFileEntryParser)new CompositeFileEntryParser(new FTPFileEntryParser[] { (FTPFileEntryParser)new NTFTPEntryParser(config), (FTPFileEntryParser)new UnixFTPEntryParser(config, (config != null && "UNIX_LTRIM".equals(config.getServerSystemKey()))) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTPFileEntryParser createOS2FTPEntryParser() {
/* 253 */     return (FTPFileEntryParser)new OS2FTPEntryParser();
/*     */   }
/*     */ 
/*     */   
/*     */   public FTPFileEntryParser createOS400FTPEntryParser() {
/* 258 */     return createOS400FTPEntryParser(null);
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
/*     */   private FTPFileEntryParser createOS400FTPEntryParser(FTPClientConfig config) {
/* 270 */     if (config != null && "OS/400".equals(config.getServerSystemKey()))
/*     */     {
/*     */       
/* 273 */       return (FTPFileEntryParser)new OS400FTPEntryParser(config);
/*     */     }
/* 275 */     return (FTPFileEntryParser)new CompositeFileEntryParser(new FTPFileEntryParser[] { (FTPFileEntryParser)new OS400FTPEntryParser(config), (FTPFileEntryParser)new UnixFTPEntryParser(config, (config != null && "UNIX_LTRIM".equals(config.getServerSystemKey()))) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTPFileEntryParser createMVSEntryParser() {
/* 286 */     return (FTPFileEntryParser)new MVSFTPEntryParser();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\ftp\parser\DefaultFTPFileEntryParserFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */