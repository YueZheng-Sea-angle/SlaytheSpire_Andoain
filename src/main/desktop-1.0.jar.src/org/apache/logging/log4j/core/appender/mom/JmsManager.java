/*     */ package org.apache.logging.log4j.core.appender.mom;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.jms.Connection;
/*     */ import javax.jms.ConnectionFactory;
/*     */ import javax.jms.Destination;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.MapMessage;
/*     */ import javax.jms.Message;
/*     */ import javax.jms.MessageConsumer;
/*     */ import javax.jms.MessageProducer;
/*     */ import javax.jms.Session;
/*     */ import javax.naming.NamingException;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.AbstractManager;
/*     */ import org.apache.logging.log4j.core.appender.AppenderLoggingException;
/*     */ import org.apache.logging.log4j.core.appender.ManagerFactory;
/*     */ import org.apache.logging.log4j.core.net.JndiManager;
/*     */ import org.apache.logging.log4j.core.util.Log4jThread;
/*     */ import org.apache.logging.log4j.message.MapMessage;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JmsManager
/*     */   extends AbstractManager
/*     */ {
/*     */   public static class JmsManagerConfiguration
/*     */   {
/*     */     private final Properties jndiProperties;
/*     */     private final String connectionFactoryName;
/*     */     private final String destinationName;
/*     */     private final String userName;
/*     */     private final char[] password;
/*     */     private final boolean immediateFail;
/*     */     private final boolean retry;
/*     */     private final long reconnectIntervalMillis;
/*     */     
/*     */     JmsManagerConfiguration(Properties jndiProperties, String connectionFactoryName, String destinationName, String userName, char[] password, boolean immediateFail, long reconnectIntervalMillis) {
/*  67 */       this.jndiProperties = jndiProperties;
/*  68 */       this.connectionFactoryName = connectionFactoryName;
/*  69 */       this.destinationName = destinationName;
/*  70 */       this.userName = userName;
/*  71 */       this.password = password;
/*  72 */       this.immediateFail = immediateFail;
/*  73 */       this.reconnectIntervalMillis = reconnectIntervalMillis;
/*  74 */       this.retry = (reconnectIntervalMillis > 0L);
/*     */     }
/*     */     
/*     */     public String getConnectionFactoryName() {
/*  78 */       return this.connectionFactoryName;
/*     */     }
/*     */     
/*     */     public String getDestinationName() {
/*  82 */       return this.destinationName;
/*     */     }
/*     */     
/*     */     public JndiManager getJndiManager() {
/*  86 */       return JndiManager.getJndiManager(getJndiProperties());
/*     */     }
/*     */     
/*     */     public Properties getJndiProperties() {
/*  90 */       return this.jndiProperties;
/*     */     }
/*     */     
/*     */     public char[] getPassword() {
/*  94 */       return this.password;
/*     */     }
/*     */     
/*     */     public long getReconnectIntervalMillis() {
/*  98 */       return this.reconnectIntervalMillis;
/*     */     }
/*     */     
/*     */     public String getUserName() {
/* 102 */       return this.userName;
/*     */     }
/*     */     
/*     */     public boolean isImmediateFail() {
/* 106 */       return this.immediateFail;
/*     */     }
/*     */     
/*     */     public boolean isRetry() {
/* 110 */       return this.retry;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 115 */       return "JmsManagerConfiguration [jndiProperties=" + this.jndiProperties + ", connectionFactoryName=" + this.connectionFactoryName + ", destinationName=" + this.destinationName + ", userName=" + this.userName + ", immediateFail=" + this.immediateFail + ", retry=" + this.retry + ", reconnectIntervalMillis=" + this.reconnectIntervalMillis + "]";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class JmsManagerFactory
/*     */     implements ManagerFactory<JmsManager, JmsManagerConfiguration>
/*     */   {
/*     */     private JmsManagerFactory() {}
/*     */ 
/*     */     
/*     */     public JmsManager createManager(String name, JmsManager.JmsManagerConfiguration data) {
/* 127 */       if (JndiManager.isJndiJmsEnabled()) {
/*     */         try {
/* 129 */           return new JmsManager(name, data);
/* 130 */         } catch (Exception e) {
/* 131 */           JmsManager.logger().error("Error creating JmsManager using JmsManagerConfiguration [{}]", data, e);
/* 132 */           return null;
/*     */         } 
/*     */       }
/* 135 */       JmsManager.logger().error("JNDI must be enabled by setting log4j2.enableJndiJms=true");
/* 136 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class Reconnector
/*     */     extends Log4jThread
/*     */   {
/* 146 */     private final CountDownLatch latch = new CountDownLatch(1);
/*     */     
/*     */     private volatile boolean shutdown;
/*     */     
/*     */     private final Object owner;
/*     */     
/*     */     private Reconnector(Object owner) {
/* 153 */       super("JmsManager-Reconnector");
/* 154 */       this.owner = owner;
/*     */     }
/*     */     
/*     */     public void latch() {
/*     */       try {
/* 159 */         this.latch.await();
/* 160 */       } catch (InterruptedException interruptedException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void reconnect() throws NamingException, JMSException {
/* 166 */       JndiManager jndiManager2 = JmsManager.this.getJndiManager();
/* 167 */       Connection connection2 = JmsManager.this.createConnection(jndiManager2);
/* 168 */       Session session2 = JmsManager.this.createSession(connection2);
/* 169 */       Destination destination2 = JmsManager.this.createDestination(jndiManager2);
/* 170 */       MessageProducer messageProducer2 = JmsManager.this.createMessageProducer(session2, destination2);
/* 171 */       connection2.start();
/* 172 */       synchronized (this.owner) {
/* 173 */         JmsManager.this.jndiManager = jndiManager2;
/* 174 */         JmsManager.this.connection = connection2;
/* 175 */         JmsManager.this.session = session2;
/* 176 */         JmsManager.this.destination = destination2;
/* 177 */         JmsManager.this.messageProducer = messageProducer2;
/* 178 */         JmsManager.this.reconnector = null;
/* 179 */         this.shutdown = true;
/*     */       } 
/* 181 */       JmsManager.logger().debug("Connection reestablished to {}", JmsManager.this.configuration);
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 186 */       while (!this.shutdown) {
/*     */         try {
/* 188 */           sleep(JmsManager.this.configuration.getReconnectIntervalMillis());
/* 189 */           reconnect();
/* 190 */         } catch (InterruptedException|JMSException|NamingException e) {
/* 191 */           JmsManager.logger().debug("Cannot reestablish JMS connection to {}: {}", JmsManager.this.configuration, e.getLocalizedMessage(), e);
/*     */         } finally {
/*     */           
/* 194 */           this.latch.countDown();
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public void shutdown() {
/* 200 */       this.shutdown = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 205 */   static final JmsManagerFactory FACTORY = new JmsManagerFactory();
/*     */ 
/*     */ 
/*     */   
/*     */   private final JmsManagerConfiguration configuration;
/*     */ 
/*     */   
/*     */   private volatile Reconnector reconnector;
/*     */ 
/*     */   
/*     */   private volatile JndiManager jndiManager;
/*     */ 
/*     */   
/*     */   private volatile Connection connection;
/*     */ 
/*     */   
/*     */   private volatile Session session;
/*     */ 
/*     */   
/*     */   private volatile Destination destination;
/*     */ 
/*     */   
/*     */   private volatile MessageProducer messageProducer;
/*     */ 
/*     */ 
/*     */   
/*     */   public static JmsManager getJmsManager(String name, Properties jndiProperties, String connectionFactoryName, String destinationName, String userName, char[] password, boolean immediateFail, long reconnectIntervalMillis) {
/* 232 */     JmsManagerConfiguration configuration = new JmsManagerConfiguration(jndiProperties, connectionFactoryName, destinationName, userName, password, immediateFail, reconnectIntervalMillis);
/*     */     
/* 234 */     return (JmsManager)getManager(name, FACTORY, configuration);
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
/*     */   private JmsManager(String name, JmsManagerConfiguration configuration) {
/* 247 */     super(null, name);
/* 248 */     this.configuration = configuration;
/* 249 */     this.jndiManager = configuration.getJndiManager();
/*     */     try {
/* 251 */       this.connection = createConnection(this.jndiManager);
/* 252 */       this.session = createSession(this.connection);
/* 253 */       this.destination = createDestination(this.jndiManager);
/* 254 */       this.messageProducer = createMessageProducer(this.session, this.destination);
/* 255 */       this.connection.start();
/* 256 */     } catch (NamingException|JMSException e) {
/* 257 */       this.reconnector = createReconnector();
/* 258 */       this.reconnector.start();
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean closeConnection() {
/* 263 */     if (this.connection == null) {
/* 264 */       return true;
/*     */     }
/* 266 */     Connection temp = this.connection;
/* 267 */     this.connection = null;
/*     */     try {
/* 269 */       temp.close();
/* 270 */       return true;
/* 271 */     } catch (JMSException e) {
/* 272 */       StatusLogger.getLogger().debug("Caught exception closing JMS Connection: {} ({}); continuing JMS manager shutdown", e
/*     */           
/* 274 */           .getLocalizedMessage(), temp, e);
/* 275 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean closeJndiManager() {
/* 280 */     if (this.jndiManager == null) {
/* 281 */       return true;
/*     */     }
/* 283 */     JndiManager tmp = this.jndiManager;
/* 284 */     this.jndiManager = null;
/* 285 */     tmp.close();
/* 286 */     return true;
/*     */   }
/*     */   
/*     */   private boolean closeMessageProducer() {
/* 290 */     if (this.messageProducer == null) {
/* 291 */       return true;
/*     */     }
/* 293 */     MessageProducer temp = this.messageProducer;
/* 294 */     this.messageProducer = null;
/*     */     try {
/* 296 */       temp.close();
/* 297 */       return true;
/* 298 */     } catch (JMSException e) {
/* 299 */       StatusLogger.getLogger().debug("Caught exception closing JMS MessageProducer: {} ({}); continuing JMS manager shutdown", e
/*     */           
/* 301 */           .getLocalizedMessage(), temp, e);
/* 302 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean closeSession() {
/* 307 */     if (this.session == null) {
/* 308 */       return true;
/*     */     }
/* 310 */     Session temp = this.session;
/* 311 */     this.session = null;
/*     */     try {
/* 313 */       temp.close();
/* 314 */       return true;
/* 315 */     } catch (JMSException e) {
/* 316 */       StatusLogger.getLogger().debug("Caught exception closing JMS Session: {} ({}); continuing JMS manager shutdown", e
/*     */           
/* 318 */           .getLocalizedMessage(), temp, e);
/* 319 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private Connection createConnection(JndiManager jndiManager) throws NamingException, JMSException {
/* 324 */     ConnectionFactory connectionFactory = (ConnectionFactory)jndiManager.lookup(this.configuration.getConnectionFactoryName());
/* 325 */     if (this.configuration.getUserName() != null && this.configuration.getPassword() != null) {
/* 326 */       return connectionFactory.createConnection(this.configuration.getUserName(), 
/* 327 */           (this.configuration.getPassword() == null) ? null : String.valueOf(this.configuration.getPassword()));
/*     */     }
/* 329 */     return connectionFactory.createConnection();
/*     */   }
/*     */ 
/*     */   
/*     */   private Destination createDestination(JndiManager jndiManager) throws NamingException {
/* 334 */     return (Destination)jndiManager.lookup(this.configuration.getDestinationName());
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
/*     */   public Message createMessage(Serializable object) throws JMSException {
/* 359 */     if (object instanceof String)
/* 360 */       return (Message)this.session.createTextMessage((String)object); 
/* 361 */     if (object instanceof MapMessage) {
/* 362 */       return (Message)map((MapMessage<?, ?>)object, this.session.createMapMessage());
/*     */     }
/* 364 */     return (Message)this.session.createObjectMessage(object);
/*     */   }
/*     */   
/*     */   private void createMessageAndSend(LogEvent event, Serializable serializable) throws JMSException {
/* 368 */     Message message = createMessage(serializable);
/* 369 */     message.setJMSTimestamp(event.getTimeMillis());
/* 370 */     this.messageProducer.send(message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageConsumer createMessageConsumer() throws JMSException {
/* 380 */     return this.session.createConsumer(this.destination);
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
/*     */   public MessageProducer createMessageProducer(Session session, Destination destination) throws JMSException {
/* 395 */     return session.createProducer(destination);
/*     */   }
/*     */   
/*     */   private Reconnector createReconnector() {
/* 399 */     Reconnector recon = new Reconnector(this);
/* 400 */     recon.setDaemon(true);
/* 401 */     recon.setPriority(1);
/* 402 */     return recon;
/*     */   }
/*     */   
/*     */   private Session createSession(Connection connection) throws JMSException {
/* 406 */     return connection.createSession(false, 1);
/*     */   }
/*     */   
/*     */   public JmsManagerConfiguration getJmsManagerConfiguration() {
/* 410 */     return this.configuration;
/*     */   }
/*     */   
/*     */   JndiManager getJndiManager() {
/* 414 */     return this.configuration.getJndiManager();
/*     */   }
/*     */   
/*     */   <T> T lookup(String destinationName) throws NamingException {
/* 418 */     return (T)this.jndiManager.lookup(destinationName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private MapMessage map(MapMessage<?, ?> log4jMapMessage, MapMessage jmsMapMessage) {
/* 424 */     log4jMapMessage.forEach((key, value) -> {
/*     */           try {
/*     */             jmsMapMessage.setObject(key, value);
/* 427 */           } catch (JMSException e) {
/*     */             throw new IllegalArgumentException(String.format("%s mapping key '%s' to value '%s': %s", new Object[] { e.getClass(), key, value, e.getLocalizedMessage() }), e);
/*     */           } 
/*     */         });
/*     */     
/* 432 */     return jmsMapMessage;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean releaseSub(long timeout, TimeUnit timeUnit) {
/* 437 */     if (this.reconnector != null) {
/* 438 */       this.reconnector.shutdown();
/* 439 */       this.reconnector.interrupt();
/* 440 */       this.reconnector = null;
/*     */     } 
/* 442 */     boolean closed = false;
/* 443 */     closed &= closeJndiManager();
/* 444 */     closed &= closeMessageProducer();
/* 445 */     closed &= closeSession();
/* 446 */     closed &= closeConnection();
/* 447 */     return (closed && this.jndiManager.stop(timeout, timeUnit));
/*     */   }
/*     */   
/*     */   void send(LogEvent event, Serializable serializable) {
/* 451 */     if (this.messageProducer == null && 
/* 452 */       this.reconnector != null && !this.configuration.isImmediateFail()) {
/* 453 */       this.reconnector.latch();
/* 454 */       if (this.messageProducer == null) {
/* 455 */         throw new AppenderLoggingException("Error sending to JMS Manager '" + 
/* 456 */             getName() + "': JMS message producer not available");
/*     */       }
/*     */     } 
/*     */     
/* 460 */     synchronized (this) {
/*     */       try {
/* 462 */         createMessageAndSend(event, serializable);
/* 463 */       } catch (JMSException causeEx) {
/* 464 */         if (this.configuration.isRetry() && this.reconnector == null) {
/* 465 */           this.reconnector = createReconnector();
/*     */           try {
/* 467 */             closeJndiManager();
/* 468 */             this.reconnector.reconnect();
/* 469 */           } catch (NamingException|JMSException reconnEx) {
/* 470 */             logger().debug("Cannot reestablish JMS connection to {}: {}; starting reconnector thread {}", this.configuration, reconnEx
/* 471 */                 .getLocalizedMessage(), this.reconnector.getName(), reconnEx);
/* 472 */             this.reconnector.start();
/* 473 */             throw new AppenderLoggingException(
/* 474 */                 String.format("JMS exception sending to %s for %s", new Object[] { getName(), this.configuration }), causeEx);
/*     */           } 
/*     */           try {
/* 477 */             createMessageAndSend(event, serializable);
/* 478 */           } catch (JMSException e) {
/* 479 */             throw new AppenderLoggingException(
/* 480 */                 String.format("Error sending to %s after reestablishing JMS connection for %s", new Object[] {
/* 481 */                     getName(), this.configuration
/*     */                   }), causeEx);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\mom\JmsManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */