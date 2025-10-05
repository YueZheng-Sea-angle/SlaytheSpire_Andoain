/*     */ package org.apache.logging.log4j.core.appender.mom;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.AbstractAppender;
/*     */ import org.apache.logging.log4j.core.appender.AbstractManager;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAliases;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
/*     */ import org.apache.logging.log4j.core.net.JndiManager;
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
/*     */ @Plugin(name = "JMS", category = "Core", elementType = "appender", printObject = true)
/*     */ @PluginAliases({"JMSQueue", "JMSTopic"})
/*     */ public class JmsAppender
/*     */   extends AbstractAppender
/*     */ {
/*     */   private volatile JmsManager manager;
/*     */   
/*     */   public static class Builder<B extends Builder<B>>
/*     */     extends AbstractAppender.Builder<B>
/*     */     implements org.apache.logging.log4j.core.util.Builder<JmsAppender>
/*     */   {
/*     */     public static final int DEFAULT_RECONNECT_INTERVAL_MILLIS = 5000;
/*     */     @PluginBuilderAttribute
/*     */     private String factoryName;
/*     */     @PluginBuilderAttribute
/*     */     private String providerUrl;
/*     */     @PluginBuilderAttribute
/*     */     private String urlPkgPrefixes;
/*     */     @PluginBuilderAttribute
/*     */     private String securityPrincipalName;
/*     */     @PluginBuilderAttribute(sensitive = true)
/*     */     private String securityCredentials;
/*     */     @PluginBuilderAttribute
/*     */     @Required(message = "A javax.jms.ConnectionFactory JNDI name must be specified")
/*     */     private String factoryBindingName;
/*     */     @PluginBuilderAttribute
/*     */     @PluginAliases({"queueBindingName", "topicBindingName"})
/*     */     @Required(message = "A javax.jms.Destination JNDI name must be specified")
/*     */     private String destinationBindingName;
/*     */     @PluginBuilderAttribute
/*     */     private String userName;
/*     */     @PluginBuilderAttribute(sensitive = true)
/*     */     private char[] password;
/*     */     @PluginBuilderAttribute
/*  85 */     private long reconnectIntervalMillis = 5000L;
/*     */ 
/*     */ 
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private boolean immediateFail;
/*     */ 
/*     */ 
/*     */     
/*     */     private JmsManager jmsManager;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public JmsAppender build() {
/* 100 */       JmsManager actualJmsManager = this.jmsManager;
/* 101 */       JmsManager.JmsManagerConfiguration configuration = null;
/* 102 */       if (actualJmsManager == null) {
/* 103 */         Properties jndiProperties = JndiManager.createProperties(this.factoryName, this.providerUrl, this.urlPkgPrefixes, this.securityPrincipalName, this.securityCredentials, null);
/*     */         
/* 105 */         configuration = new JmsManager.JmsManagerConfiguration(jndiProperties, this.factoryBindingName, this.destinationBindingName, this.userName, this.password, false, this.reconnectIntervalMillis);
/*     */         
/* 107 */         actualJmsManager = (JmsManager)AbstractManager.getManager(getName(), JmsManager.FACTORY, configuration);
/*     */       } 
/* 109 */       if (actualJmsManager == null)
/*     */       {
/* 111 */         return null;
/*     */       }
/* 113 */       Layout<? extends Serializable> layout = getLayout();
/* 114 */       if (layout == null) {
/* 115 */         JmsAppender.LOGGER.error("No layout provided for JmsAppender");
/* 116 */         return null;
/*     */       } 
/*     */       try {
/* 119 */         return new JmsAppender(getName(), getFilter(), layout, isIgnoreExceptions(), getPropertyArray(), actualJmsManager);
/*     */       }
/* 121 */       catch (JMSException e) {
/*     */         
/* 123 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public Builder setDestinationBindingName(String destinationBindingName) {
/* 128 */       this.destinationBindingName = destinationBindingName;
/* 129 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setFactoryBindingName(String factoryBindingName) {
/* 133 */       this.factoryBindingName = factoryBindingName;
/* 134 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setFactoryName(String factoryName) {
/* 138 */       this.factoryName = factoryName;
/* 139 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setImmediateFail(boolean immediateFail) {
/* 143 */       this.immediateFail = immediateFail;
/* 144 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setJmsManager(JmsManager jmsManager) {
/* 148 */       this.jmsManager = jmsManager;
/* 149 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setPassword(char[] password) {
/* 153 */       this.password = password;
/* 154 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public Builder setPassword(String password) {
/* 162 */       this.password = (password == null) ? null : password.toCharArray();
/* 163 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setProviderUrl(String providerUrl) {
/* 167 */       this.providerUrl = providerUrl;
/* 168 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setReconnectIntervalMillis(long reconnectIntervalMillis) {
/* 172 */       this.reconnectIntervalMillis = reconnectIntervalMillis;
/* 173 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setSecurityCredentials(String securityCredentials) {
/* 177 */       this.securityCredentials = securityCredentials;
/* 178 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setSecurityPrincipalName(String securityPrincipalName) {
/* 182 */       this.securityPrincipalName = securityPrincipalName;
/* 183 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setUrlPkgPrefixes(String urlPkgPrefixes) {
/* 187 */       this.urlPkgPrefixes = urlPkgPrefixes;
/* 188 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public Builder setUsername(String username) {
/* 196 */       this.userName = username;
/* 197 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setUserName(String userName) {
/* 201 */       this.userName = userName;
/* 202 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 210 */       return "Builder [name=" + getName() + ", factoryName=" + this.factoryName + ", providerUrl=" + this.providerUrl + ", urlPkgPrefixes=" + this.urlPkgPrefixes + ", securityPrincipalName=" + this.securityPrincipalName + ", securityCredentials=" + this.securityCredentials + ", factoryBindingName=" + this.factoryBindingName + ", destinationBindingName=" + this.destinationBindingName + ", username=" + this.userName + ", layout=" + 
/*     */ 
/*     */ 
/*     */         
/* 214 */         getLayout() + ", filter=" + getFilter() + ", ignoreExceptions=" + isIgnoreExceptions() + ", jmsManager=" + this.jmsManager + "]";
/*     */     }
/*     */     
/*     */     private Builder() {}
/*     */   }
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/* 222 */     return new Builder<>();
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
/*     */   protected JmsAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, Property[] properties, JmsManager manager) throws JMSException {
/* 234 */     super(name, filter, layout, ignoreExceptions, properties);
/* 235 */     this.manager = manager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected JmsAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, JmsManager manager) throws JMSException {
/* 247 */     super(name, filter, layout, ignoreExceptions, Property.EMPTY_ARRAY);
/* 248 */     this.manager = manager;
/*     */   }
/*     */ 
/*     */   
/*     */   public void append(LogEvent event) {
/* 253 */     this.manager.send(event, toSerializable(event));
/*     */   }
/*     */   
/*     */   public JmsManager getManager() {
/* 257 */     return this.manager;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 262 */     setStopping();
/* 263 */     boolean stopped = stop(timeout, timeUnit, false);
/* 264 */     stopped &= this.manager.stop(timeout, timeUnit);
/* 265 */     setStopped();
/* 266 */     return stopped;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\mom\JmsAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */