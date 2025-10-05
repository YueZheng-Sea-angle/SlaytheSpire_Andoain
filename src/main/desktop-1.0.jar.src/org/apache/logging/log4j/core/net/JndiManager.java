/*     */ package org.apache.logging.log4j.core.net;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.naming.InitialContext;
/*     */ import javax.naming.NamingException;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.appender.AbstractManager;
/*     */ import org.apache.logging.log4j.core.appender.ManagerFactory;
/*     */ import org.apache.logging.log4j.core.util.JndiCloser;
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
/*     */ public class JndiManager
/*     */   extends AbstractManager
/*     */ {
/*  41 */   private static final JndiManagerFactory FACTORY = new JndiManagerFactory();
/*     */   
/*     */   private static final String PREFIX = "log4j2.enableJndi";
/*     */   private static final String JAVA_SCHEME = "java";
/*     */   private final InitialContext context;
/*     */   
/*     */   private static boolean isJndiEnabled(String subKey) {
/*  48 */     return PropertiesUtil.getProperties().getBooleanProperty("log4j2.enableJndi" + subKey, false);
/*     */   }
/*     */   
/*     */   public static boolean isJndiEnabled() {
/*  52 */     return (isJndiContextSelectorEnabled() || isJndiJmsEnabled() || isJndiLookupEnabled());
/*     */   }
/*     */   
/*     */   public static boolean isJndiContextSelectorEnabled() {
/*  56 */     return isJndiEnabled("ContextSelector");
/*     */   }
/*     */   
/*     */   public static boolean isJndiJmsEnabled() {
/*  60 */     return isJndiEnabled("Jms");
/*     */   }
/*     */   
/*     */   public static boolean isJndiLookupEnabled() {
/*  64 */     return isJndiEnabled("Lookup");
/*     */   }
/*     */   
/*     */   private JndiManager(String name, InitialContext context) {
/*  68 */     super(null, name);
/*  69 */     this.context = context;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JndiManager getDefaultManager() {
/*  78 */     return (JndiManager)getManager(JndiManager.class.getName(), FACTORY, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JndiManager getDefaultManager(String name) {
/*  88 */     return (JndiManager)getManager(name, FACTORY, null);
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
/*     */   public static JndiManager getJndiManager(String initialContextFactoryName, String providerURL, String urlPkgPrefixes, String securityPrincipal, String securityCredentials, Properties additionalProperties) {
/* 110 */     Properties properties = createProperties(initialContextFactoryName, providerURL, urlPkgPrefixes, securityPrincipal, securityCredentials, additionalProperties);
/*     */     
/* 112 */     return (JndiManager)getManager(createManagerName(), FACTORY, properties);
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
/*     */   public static JndiManager getJndiManager(Properties properties) {
/* 124 */     return (JndiManager)getManager(createManagerName(), FACTORY, properties);
/*     */   }
/*     */   
/*     */   private static String createManagerName() {
/* 128 */     return JndiManager.class.getName() + '@' + JndiManager.class.hashCode();
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
/*     */   public static Properties createProperties(String initialContextFactoryName, String providerURL, String urlPkgPrefixes, String securityPrincipal, String securityCredentials, Properties additionalProperties) {
/* 153 */     if (initialContextFactoryName == null) {
/* 154 */       return null;
/*     */     }
/* 156 */     Properties properties = new Properties();
/* 157 */     properties.setProperty("java.naming.factory.initial", initialContextFactoryName);
/* 158 */     if (providerURL != null) {
/* 159 */       properties.setProperty("java.naming.provider.url", providerURL);
/*     */     } else {
/* 161 */       LOGGER.warn("The JNDI InitialContextFactory class name [{}] was provided, but there was no associated provider URL. This is likely to cause problems.", initialContextFactoryName);
/*     */     } 
/*     */     
/* 164 */     if (urlPkgPrefixes != null) {
/* 165 */       properties.setProperty("java.naming.factory.url.pkgs", urlPkgPrefixes);
/*     */     }
/* 167 */     if (securityPrincipal != null) {
/* 168 */       properties.setProperty("java.naming.security.principal", securityPrincipal);
/* 169 */       if (securityCredentials != null) {
/* 170 */         properties.setProperty("java.naming.security.credentials", securityCredentials);
/*     */       } else {
/* 172 */         LOGGER.warn("A security principal [{}] was provided, but with no corresponding security credentials.", securityPrincipal);
/*     */       } 
/*     */     } 
/*     */     
/* 176 */     if (additionalProperties != null) {
/* 177 */       properties.putAll(additionalProperties);
/*     */     }
/* 179 */     return properties;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean releaseSub(long timeout, TimeUnit timeUnit) {
/* 184 */     return JndiCloser.closeSilently(this.context);
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
/*     */   public <T> T lookup(String name) throws NamingException {
/* 197 */     if (this.context == null) {
/* 198 */       return null;
/*     */     }
/*     */     try {
/* 201 */       URI uri = new URI(name);
/* 202 */       if (uri.getScheme() == null || uri.getScheme().equals("java")) {
/* 203 */         return (T)this.context.lookup(name);
/*     */       }
/* 205 */       LOGGER.warn("Unsupported JNDI URI - {}", name);
/* 206 */     } catch (URISyntaxException ex) {
/* 207 */       LOGGER.warn("Invalid  JNDI URI - {}", name);
/*     */     } 
/* 209 */     return null;
/*     */   }
/*     */   
/*     */   private static class JndiManagerFactory implements ManagerFactory<JndiManager, Properties> {
/*     */     private JndiManagerFactory() {}
/*     */     
/*     */     public JndiManager createManager(String name, Properties data) {
/* 216 */       if (!JndiManager.isJndiEnabled()) {
/* 217 */         throw new IllegalStateException(String.format("JNDI must be enabled by setting one of the %s* properties to true", new Object[] { "log4j2.enableJndi" }));
/*     */       }
/*     */       try {
/* 220 */         return new JndiManager(name, new InitialContext(data));
/* 221 */       } catch (NamingException e) {
/* 222 */         JndiManager.LOGGER.error("Error creating JNDI InitialContext for '{}'.", name, e);
/* 223 */         return null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 230 */     return "JndiManager [context=" + this.context + ", count=" + this.count + "]";
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\net\JndiManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */