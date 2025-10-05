/*    */ package org.apache.logging.log4j.core.lookup;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import javax.naming.NamingException;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.Marker;
/*    */ import org.apache.logging.log4j.MarkerManager;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.net.JndiManager;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Plugin(name = "jndi", category = "Lookup")
/*    */ public class JndiLookup
/*    */   extends AbstractLookup
/*    */ {
/* 37 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/* 38 */   private static final Marker LOOKUP = MarkerManager.getMarker("LOOKUP");
/*    */ 
/*    */ 
/*    */   
/*    */   static final String CONTAINER_JNDI_RESOURCE_PATH_PREFIX = "java:comp/env/";
/*    */ 
/*    */ 
/*    */   
/*    */   public JndiLookup() {
/* 47 */     if (!JndiManager.isJndiLookupEnabled()) {
/* 48 */       throw new IllegalStateException("JNDI must be enabled by setting log4j2.enableJndiLookup=true");
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String lookup(LogEvent event, String key) {
/* 61 */     if (key == null) {
/* 62 */       return null;
/*    */     }
/* 64 */     String jndiName = convertJndiName(key);
/* 65 */     try (JndiManager jndiManager = JndiManager.getDefaultManager()) {
/* 66 */       return Objects.toString(jndiManager.lookup(jndiName), null);
/* 67 */     } catch (NamingException e) {
/* 68 */       LOGGER.warn(LOOKUP, "Error looking up JNDI resource [{}].", jndiName, e);
/* 69 */       return null;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private String convertJndiName(String jndiName) {
/* 81 */     if (!jndiName.startsWith("java:comp/env/") && jndiName.indexOf(':') == -1) {
/* 82 */       return "java:comp/env/" + jndiName;
/*    */     }
/* 84 */     return jndiName;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\lookup\JndiLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */