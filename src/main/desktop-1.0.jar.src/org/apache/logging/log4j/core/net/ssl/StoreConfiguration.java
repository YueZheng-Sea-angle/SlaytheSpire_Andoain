/*     */ package org.apache.logging.log4j.core.net.ssl;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Objects;
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
/*     */ public class StoreConfiguration<T>
/*     */ {
/*  28 */   protected static final StatusLogger LOGGER = StatusLogger.getLogger();
/*     */   
/*     */   private String location;
/*     */   private PasswordProvider passwordProvider;
/*     */   
/*     */   public StoreConfiguration(String location, PasswordProvider passwordProvider) {
/*  34 */     this.location = location;
/*  35 */     this.passwordProvider = Objects.<PasswordProvider>requireNonNull(passwordProvider, "passwordProvider");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public StoreConfiguration(String location, char[] password) {
/*  43 */     this(location, new MemoryPasswordProvider(password));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public StoreConfiguration(String location, String password) {
/*  51 */     this(location, new MemoryPasswordProvider((password == null) ? null : password.toCharArray()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearSecrets() {
/*  58 */     this.location = null;
/*  59 */     this.passwordProvider = null;
/*     */   }
/*     */   
/*     */   public String getLocation() {
/*  63 */     return this.location;
/*     */   }
/*     */   
/*     */   public void setLocation(String location) {
/*  67 */     this.location = location;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public String getPassword() {
/*  76 */     return String.valueOf(this.passwordProvider.getPassword());
/*     */   }
/*     */   
/*     */   public char[] getPasswordAsCharArray() {
/*  80 */     return this.passwordProvider.getPassword();
/*     */   }
/*     */   
/*     */   public void setPassword(char[] password) {
/*  84 */     this.passwordProvider = new MemoryPasswordProvider(password);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setPassword(String password) {
/*  93 */     this.passwordProvider = new MemoryPasswordProvider((password == null) ? null : password.toCharArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected T load() throws StoreConfigurationException {
/* 100 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 105 */     int prime = 31;
/* 106 */     int result = 1;
/* 107 */     result = 31 * result + ((this.location == null) ? 0 : this.location.hashCode());
/* 108 */     result = 31 * result + Arrays.hashCode(this.passwordProvider.getPassword());
/* 109 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 114 */     if (this == obj) {
/* 115 */       return true;
/*     */     }
/* 117 */     if (obj == null) {
/* 118 */       return false;
/*     */     }
/* 120 */     if (getClass() != obj.getClass()) {
/* 121 */       return false;
/*     */     }
/* 123 */     StoreConfiguration<?> other = (StoreConfiguration)obj;
/* 124 */     if (!Objects.equals(this.location, other.location)) {
/* 125 */       return false;
/*     */     }
/* 127 */     if (!Arrays.equals(this.passwordProvider.getPassword(), other.passwordProvider.getPassword())) {
/* 128 */       return false;
/*     */     }
/* 130 */     return true;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\net\ssl\StoreConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */