/*     */ package org.apache.logging.log4j.core.appender.mom.jeromq;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.core.appender.AbstractManager;
/*     */ import org.apache.logging.log4j.core.appender.ManagerFactory;
/*     */ import org.apache.logging.log4j.core.util.Cancellable;
/*     */ import org.apache.logging.log4j.core.util.ShutdownCallbackRegistry;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ import org.zeromq.ZMQ;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JeroMqManager
/*     */   extends AbstractManager
/*     */ {
/*     */   public static final String SYS_PROPERTY_ENABLE_SHUTDOWN_HOOK = "log4j.jeromq.enableShutdownHook";
/*     */   public static final String SYS_PROPERTY_IO_THREADS = "log4j.jeromq.ioThreads";
/*  49 */   private static final JeroMqManagerFactory FACTORY = new JeroMqManagerFactory();
/*     */   
/*     */   private static final ZMQ.Context CONTEXT;
/*     */   private static final Cancellable SHUTDOWN_HOOK;
/*     */   private final ZMQ.Socket publisher;
/*     */   
/*     */   static {
/*  56 */     LOGGER.trace("JeroMqManager using ZMQ version {}", ZMQ.getVersionString());
/*     */     
/*  58 */     int ioThreads = PropertiesUtil.getProperties().getIntegerProperty("log4j.jeromq.ioThreads", 1);
/*  59 */     LOGGER.trace("JeroMqManager creating ZMQ context with ioThreads = {}", Integer.valueOf(ioThreads));
/*  60 */     CONTEXT = ZMQ.context(ioThreads);
/*     */     
/*  62 */     boolean enableShutdownHook = PropertiesUtil.getProperties().getBooleanProperty("log4j.jeromq.enableShutdownHook", true);
/*     */     
/*  64 */     if (enableShutdownHook) {
/*  65 */       SHUTDOWN_HOOK = ((ShutdownCallbackRegistry)LogManager.getFactory()).addShutdownCallback(CONTEXT::close);
/*     */     } else {
/*  67 */       SHUTDOWN_HOOK = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private JeroMqManager(String name, JeroMqConfiguration config) {
/*  74 */     super(null, name);
/*  75 */     this.publisher = CONTEXT.socket(1);
/*  76 */     this.publisher.setAffinity(config.affinity);
/*  77 */     this.publisher.setBacklog(config.backlog);
/*  78 */     this.publisher.setDelayAttachOnConnect(config.delayAttachOnConnect);
/*  79 */     if (config.identity != null) {
/*  80 */       this.publisher.setIdentity(config.identity);
/*     */     }
/*  82 */     this.publisher.setIPv4Only(config.ipv4Only);
/*  83 */     this.publisher.setLinger(config.linger);
/*  84 */     this.publisher.setMaxMsgSize(config.maxMsgSize);
/*  85 */     this.publisher.setRcvHWM(config.rcvHwm);
/*  86 */     this.publisher.setReceiveBufferSize(config.receiveBufferSize);
/*  87 */     this.publisher.setReceiveTimeOut(config.receiveTimeOut);
/*  88 */     this.publisher.setReconnectIVL(config.reconnectIVL);
/*  89 */     this.publisher.setReconnectIVLMax(config.reconnectIVLMax);
/*  90 */     this.publisher.setSendBufferSize(config.sendBufferSize);
/*  91 */     this.publisher.setSendTimeOut(config.sendTimeOut);
/*  92 */     this.publisher.setSndHWM(config.sndHwm);
/*  93 */     this.publisher.setTCPKeepAlive(config.tcpKeepAlive);
/*  94 */     this.publisher.setTCPKeepAliveCount(config.tcpKeepAliveCount);
/*  95 */     this.publisher.setTCPKeepAliveIdle(config.tcpKeepAliveIdle);
/*  96 */     this.publisher.setTCPKeepAliveInterval(config.tcpKeepAliveInterval);
/*  97 */     this.publisher.setXpubVerbose(config.xpubVerbose);
/*  98 */     for (String endpoint : config.endpoints) {
/*  99 */       this.publisher.bind(endpoint);
/*     */     }
/* 101 */     LOGGER.debug("Created JeroMqManager with {}", config);
/*     */   }
/*     */   
/*     */   public boolean send(byte[] data) {
/* 105 */     return this.publisher.send(data);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean releaseSub(long timeout, TimeUnit timeUnit) {
/* 110 */     this.publisher.close();
/* 111 */     return true;
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
/*     */   public static JeroMqManager getJeroMqManager(String name, long affinity, long backlog, boolean delayAttachOnConnect, byte[] identity, boolean ipv4Only, long linger, long maxMsgSize, long rcvHwm, long receiveBufferSize, int receiveTimeOut, long reconnectIVL, long reconnectIVLMax, long sendBufferSize, int sendTimeOut, long sndHwm, int tcpKeepAlive, long tcpKeepAliveCount, long tcpKeepAliveIdle, long tcpKeepAliveInterval, boolean xpubVerbose, List<String> endpoints) {
/* 124 */     return (JeroMqManager)getManager(name, FACTORY, new JeroMqConfiguration(affinity, backlog, delayAttachOnConnect, identity, ipv4Only, linger, maxMsgSize, rcvHwm, receiveBufferSize, receiveTimeOut, reconnectIVL, reconnectIVLMax, sendBufferSize, sendTimeOut, sndHwm, tcpKeepAlive, tcpKeepAliveCount, tcpKeepAliveIdle, tcpKeepAliveInterval, xpubVerbose, endpoints));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ZMQ.Context getContext() {
/* 132 */     return CONTEXT;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class JeroMqConfiguration
/*     */   {
/*     */     private final long affinity;
/*     */     
/*     */     private final long backlog;
/*     */     
/*     */     private final boolean delayAttachOnConnect;
/*     */     
/*     */     private final byte[] identity;
/*     */     
/*     */     private final boolean ipv4Only;
/*     */     
/*     */     private final long linger;
/*     */     private final long maxMsgSize;
/*     */     private final long rcvHwm;
/*     */     private final long receiveBufferSize;
/*     */     private final int receiveTimeOut;
/*     */     private final long reconnectIVL;
/*     */     private final long reconnectIVLMax;
/*     */     private final long sendBufferSize;
/*     */     private final int sendTimeOut;
/*     */     private final long sndHwm;
/*     */     private final int tcpKeepAlive;
/*     */     private final long tcpKeepAliveCount;
/*     */     private final long tcpKeepAliveIdle;
/*     */     private final long tcpKeepAliveInterval;
/*     */     private final boolean xpubVerbose;
/*     */     private final List<String> endpoints;
/*     */     
/*     */     private JeroMqConfiguration(long affinity, long backlog, boolean delayAttachOnConnect, byte[] identity, boolean ipv4Only, long linger, long maxMsgSize, long rcvHwm, long receiveBufferSize, int receiveTimeOut, long reconnectIVL, long reconnectIVLMax, long sendBufferSize, int sendTimeOut, long sndHwm, int tcpKeepAlive, long tcpKeepAliveCount, long tcpKeepAliveIdle, long tcpKeepAliveInterval, boolean xpubVerbose, List<String> endpoints) {
/* 166 */       this.affinity = affinity;
/* 167 */       this.backlog = backlog;
/* 168 */       this.delayAttachOnConnect = delayAttachOnConnect;
/* 169 */       this.identity = identity;
/* 170 */       this.ipv4Only = ipv4Only;
/* 171 */       this.linger = linger;
/* 172 */       this.maxMsgSize = maxMsgSize;
/* 173 */       this.rcvHwm = rcvHwm;
/* 174 */       this.receiveBufferSize = receiveBufferSize;
/* 175 */       this.receiveTimeOut = receiveTimeOut;
/* 176 */       this.reconnectIVL = reconnectIVL;
/* 177 */       this.reconnectIVLMax = reconnectIVLMax;
/* 178 */       this.sendBufferSize = sendBufferSize;
/* 179 */       this.sendTimeOut = sendTimeOut;
/* 180 */       this.sndHwm = sndHwm;
/* 181 */       this.tcpKeepAlive = tcpKeepAlive;
/* 182 */       this.tcpKeepAliveCount = tcpKeepAliveCount;
/* 183 */       this.tcpKeepAliveIdle = tcpKeepAliveIdle;
/* 184 */       this.tcpKeepAliveInterval = tcpKeepAliveInterval;
/* 185 */       this.xpubVerbose = xpubVerbose;
/* 186 */       this.endpoints = endpoints;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 191 */       return "JeroMqConfiguration{affinity=" + this.affinity + ", backlog=" + this.backlog + ", delayAttachOnConnect=" + this.delayAttachOnConnect + ", identity=" + 
/*     */ 
/*     */ 
/*     */         
/* 195 */         Arrays.toString(this.identity) + ", ipv4Only=" + this.ipv4Only + ", linger=" + this.linger + ", maxMsgSize=" + this.maxMsgSize + ", rcvHwm=" + this.rcvHwm + ", receiveBufferSize=" + this.receiveBufferSize + ", receiveTimeOut=" + this.receiveTimeOut + ", reconnectIVL=" + this.reconnectIVL + ", reconnectIVLMax=" + this.reconnectIVLMax + ", sendBufferSize=" + this.sendBufferSize + ", sendTimeOut=" + this.sendTimeOut + ", sndHwm=" + this.sndHwm + ", tcpKeepAlive=" + this.tcpKeepAlive + ", tcpKeepAliveCount=" + this.tcpKeepAliveCount + ", tcpKeepAliveIdle=" + this.tcpKeepAliveIdle + ", tcpKeepAliveInterval=" + this.tcpKeepAliveInterval + ", xpubVerbose=" + this.xpubVerbose + ", endpoints=" + this.endpoints + '}';
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
/*     */   private static class JeroMqManagerFactory
/*     */     implements ManagerFactory<JeroMqManager, JeroMqConfiguration>
/*     */   {
/*     */     private JeroMqManagerFactory() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public JeroMqManager createManager(String name, JeroMqManager.JeroMqConfiguration data) {
/* 220 */       return new JeroMqManager(name, data);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\mom\jeromq\JeroMqManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */