/*     */ package org.apache.logging.log4j.core.config.status;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.core.util.FileUtils;
/*     */ import org.apache.logging.log4j.core.util.NetUtils;
/*     */ import org.apache.logging.log4j.status.StatusConsoleListener;
/*     */ import org.apache.logging.log4j.status.StatusListener;
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
/*     */ public class StatusConfiguration
/*     */ {
/*  42 */   private static final PrintStream DEFAULT_STREAM = System.out;
/*  43 */   private static final Level DEFAULT_STATUS = Level.ERROR;
/*  44 */   private static final Verbosity DEFAULT_VERBOSITY = Verbosity.QUIET;
/*     */   
/*  46 */   private final Collection<String> errorMessages = new LinkedBlockingQueue<>();
/*  47 */   private final StatusLogger logger = StatusLogger.getLogger();
/*     */   
/*     */   private volatile boolean initialized;
/*     */   
/*  51 */   private PrintStream destination = DEFAULT_STREAM;
/*  52 */   private Level status = DEFAULT_STATUS;
/*  53 */   private Verbosity verbosity = DEFAULT_VERBOSITY;
/*     */   
/*     */   private String[] verboseClasses;
/*     */ 
/*     */   
/*     */   public enum Verbosity
/*     */   {
/*  60 */     QUIET, VERBOSE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Verbosity toVerbosity(String value) {
/*  69 */       return Boolean.parseBoolean(value) ? VERBOSE : QUIET;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String message) {
/*  80 */     if (!this.initialized) {
/*  81 */       this.errorMessages.add(message);
/*     */     } else {
/*  83 */       this.logger.error(message);
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
/*     */   public StatusConfiguration withDestination(String destination) {
/*     */     try {
/*  98 */       this.destination = parseStreamName(destination);
/*  99 */     } catch (URISyntaxException e) {
/* 100 */       error("Could not parse URI [" + destination + "]. Falling back to default of stdout.");
/* 101 */       this.destination = DEFAULT_STREAM;
/* 102 */     } catch (FileNotFoundException e) {
/* 103 */       error("File could not be found at [" + destination + "]. Falling back to default of stdout.");
/* 104 */       this.destination = DEFAULT_STREAM;
/*     */     } 
/* 106 */     return this;
/*     */   }
/*     */   
/*     */   private PrintStream parseStreamName(String name) throws URISyntaxException, FileNotFoundException {
/* 110 */     if (name == null || name.equalsIgnoreCase("out")) {
/* 111 */       return DEFAULT_STREAM;
/*     */     }
/* 113 */     if (name.equalsIgnoreCase("err")) {
/* 114 */       return System.err;
/*     */     }
/* 116 */     URI destUri = NetUtils.toURI(name);
/* 117 */     File output = FileUtils.fileFromUri(destUri);
/* 118 */     if (output == null)
/*     */     {
/* 120 */       return DEFAULT_STREAM;
/*     */     }
/* 122 */     FileOutputStream fos = new FileOutputStream(output);
/* 123 */     return new PrintStream(fos, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StatusConfiguration withStatus(String status) {
/* 134 */     this.status = Level.toLevel(status, null);
/* 135 */     if (this.status == null) {
/* 136 */       error("Invalid status level specified: " + status + ". Defaulting to ERROR.");
/* 137 */       this.status = Level.ERROR;
/*     */     } 
/* 139 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StatusConfiguration withStatus(Level status) {
/* 149 */     this.status = status;
/* 150 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StatusConfiguration withVerbosity(String verbosity) {
/* 161 */     this.verbosity = Verbosity.toVerbosity(verbosity);
/* 162 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StatusConfiguration withVerboseClasses(String... verboseClasses) {
/* 172 */     this.verboseClasses = verboseClasses;
/* 173 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initialize() {
/* 180 */     if (!this.initialized) {
/* 181 */       if (this.status == Level.OFF) {
/* 182 */         this.initialized = true;
/*     */       } else {
/* 184 */         boolean configured = configureExistingStatusConsoleListener();
/* 185 */         if (!configured) {
/* 186 */           registerNewStatusConsoleListener();
/*     */         }
/* 188 */         migrateSavedLogMessages();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean configureExistingStatusConsoleListener() {
/* 194 */     boolean configured = false;
/* 195 */     for (StatusListener statusListener : this.logger.getListeners()) {
/* 196 */       if (statusListener instanceof StatusConsoleListener) {
/* 197 */         StatusConsoleListener listener = (StatusConsoleListener)statusListener;
/* 198 */         listener.setLevel(this.status);
/* 199 */         this.logger.updateListenerLevel(this.status);
/* 200 */         if (this.verbosity == Verbosity.QUIET) {
/* 201 */           listener.setFilters(this.verboseClasses);
/*     */         }
/* 203 */         configured = true;
/*     */       } 
/*     */     } 
/* 206 */     return configured;
/*     */   }
/*     */ 
/*     */   
/*     */   private void registerNewStatusConsoleListener() {
/* 211 */     StatusConsoleListener listener = new StatusConsoleListener(this.status, this.destination);
/* 212 */     if (this.verbosity == Verbosity.QUIET) {
/* 213 */       listener.setFilters(this.verboseClasses);
/*     */     }
/* 215 */     this.logger.registerListener((StatusListener)listener);
/*     */   }
/*     */   
/*     */   private void migrateSavedLogMessages() {
/* 219 */     for (String message : this.errorMessages) {
/* 220 */       this.logger.error(message);
/*     */     }
/* 222 */     this.initialized = true;
/* 223 */     this.errorMessages.clear();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\status\StatusConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */