/*     */ package org.apache.logging.log4j.core.net;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.net.ConnectException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.appender.AppenderLoggingException;
/*     */ import org.apache.logging.log4j.core.appender.ManagerFactory;
/*     */ import org.apache.logging.log4j.core.appender.OutputStreamManager;
/*     */ import org.apache.logging.log4j.core.util.Closer;
/*     */ import org.apache.logging.log4j.core.util.Log4jThread;
/*     */ import org.apache.logging.log4j.core.util.NullOutputStream;
/*     */ import org.apache.logging.log4j.util.Strings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TcpSocketManager
/*     */   extends AbstractSocketManager
/*     */ {
/*     */   public static final int DEFAULT_RECONNECTION_DELAY_MILLIS = 30000;
/*     */   private static final int DEFAULT_PORT = 4560;
/*  55 */   private static final TcpSocketManagerFactory<TcpSocketManager, FactoryData> FACTORY = new TcpSocketManagerFactory<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int reconnectionDelayMillis;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Reconnector reconnector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Socket socket;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final SocketOptions socketOptions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean retry;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean immediateFail;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int connectTimeoutMillis;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public TcpSocketManager(String name, OutputStream os, Socket socket, InetAddress inetAddress, String host, int port, int connectTimeoutMillis, int reconnectionDelayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize) {
/* 104 */     this(name, os, socket, inetAddress, host, port, connectTimeoutMillis, reconnectionDelayMillis, immediateFail, layout, bufferSize, (SocketOptions)null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TcpSocketManager(String name, OutputStream os, Socket socket, InetAddress inetAddress, String host, int port, int connectTimeoutMillis, int reconnectionDelayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize, SocketOptions socketOptions) {
/* 138 */     super(name, os, inetAddress, host, port, layout, true, bufferSize);
/* 139 */     this.connectTimeoutMillis = connectTimeoutMillis;
/* 140 */     this.reconnectionDelayMillis = reconnectionDelayMillis;
/* 141 */     this.socket = socket;
/* 142 */     this.immediateFail = immediateFail;
/* 143 */     this.retry = (reconnectionDelayMillis > 0);
/* 144 */     if (socket == null) {
/* 145 */       this.reconnector = createReconnector();
/* 146 */       this.reconnector.start();
/*     */     } 
/* 148 */     this.socketOptions = socketOptions;
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
/*     */   @Deprecated
/*     */   public static TcpSocketManager getSocketManager(String host, int port, int connectTimeoutMillis, int reconnectDelayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize) {
/* 171 */     return getSocketManager(host, port, connectTimeoutMillis, reconnectDelayMillis, immediateFail, layout, bufferSize, (SocketOptions)null);
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
/*     */   public static TcpSocketManager getSocketManager(String host, int port, int connectTimeoutMillis, int reconnectDelayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize, SocketOptions socketOptions) {
/* 193 */     if (Strings.isEmpty(host)) {
/* 194 */       throw new IllegalArgumentException("A host name is required");
/*     */     }
/* 196 */     if (port <= 0) {
/* 197 */       port = 4560;
/*     */     }
/* 199 */     if (reconnectDelayMillis == 0) {
/* 200 */       reconnectDelayMillis = 30000;
/*     */     }
/* 202 */     return (TcpSocketManager)getManager("TCP:" + host + ':' + port, new FactoryData(host, port, connectTimeoutMillis, reconnectDelayMillis, immediateFail, layout, bufferSize, socketOptions), FACTORY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void write(byte[] bytes, int offset, int length, boolean immediateFlush) {
/* 209 */     if (this.socket == null) {
/* 210 */       if (this.reconnector != null && !this.immediateFail) {
/* 211 */         this.reconnector.latch();
/*     */       }
/* 213 */       if (this.socket == null) {
/* 214 */         throw new AppenderLoggingException("Error writing to " + getName() + ": socket not available");
/*     */       }
/*     */     } 
/* 217 */     synchronized (this) {
/*     */       try {
/* 219 */         writeAndFlush(bytes, offset, length, immediateFlush);
/* 220 */       } catch (IOException causeEx) {
/* 221 */         String config = this.inetAddress + ":" + this.port;
/* 222 */         if (this.retry && this.reconnector == null) {
/* 223 */           this.reconnector = createReconnector();
/*     */           try {
/* 225 */             this.reconnector.reconnect();
/* 226 */           } catch (IOException reconnEx) {
/* 227 */             LOGGER.debug("Cannot reestablish socket connection to {}: {}; starting reconnector thread {}", config, reconnEx
/* 228 */                 .getLocalizedMessage(), this.reconnector.getName(), reconnEx);
/* 229 */             this.reconnector.start();
/* 230 */             throw new AppenderLoggingException(
/* 231 */                 String.format("Error sending to %s for %s", new Object[] { getName(), config }), causeEx);
/*     */           } 
/*     */           try {
/* 234 */             writeAndFlush(bytes, offset, length, immediateFlush);
/* 235 */           } catch (IOException e) {
/* 236 */             throw new AppenderLoggingException(
/* 237 */                 String.format("Error writing to %s after reestablishing connection for %s", new Object[] { getName(), config }), causeEx);
/*     */           } 
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 243 */         String message = String.format("Error writing to %s for connection %s", new Object[] { getName(), config });
/* 244 */         throw new AppenderLoggingException(message, causeEx);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeAndFlush(byte[] bytes, int offset, int length, boolean immediateFlush) throws IOException {
/* 252 */     OutputStream outputStream = getOutputStream();
/* 253 */     outputStream.write(bytes, offset, length);
/* 254 */     if (immediateFlush) {
/* 255 */       outputStream.flush();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected synchronized boolean closeOutputStream() {
/* 261 */     boolean closed = super.closeOutputStream();
/* 262 */     if (this.reconnector != null) {
/* 263 */       this.reconnector.shutdown();
/* 264 */       this.reconnector.interrupt();
/* 265 */       this.reconnector = null;
/*     */     } 
/* 267 */     Socket oldSocket = this.socket;
/* 268 */     this.socket = null;
/* 269 */     if (oldSocket != null) {
/*     */       try {
/* 271 */         oldSocket.close();
/* 272 */       } catch (IOException e) {
/* 273 */         LOGGER.error("Could not close socket {}", this.socket);
/* 274 */         return false;
/*     */       } 
/*     */     }
/* 277 */     return closed;
/*     */   }
/*     */   
/*     */   public int getConnectTimeoutMillis() {
/* 281 */     return this.connectTimeoutMillis;
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
/*     */   public Map<String, String> getContentFormat() {
/* 295 */     Map<String, String> result = new HashMap<>(super.getContentFormat());
/* 296 */     result.put("protocol", "tcp");
/* 297 */     result.put("direction", "out");
/* 298 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class Reconnector
/*     */     extends Log4jThread
/*     */   {
/* 306 */     private final CountDownLatch latch = new CountDownLatch(1);
/*     */     
/*     */     private boolean shutdown = false;
/*     */     
/*     */     private final Object owner;
/*     */     
/*     */     public Reconnector(OutputStreamManager owner) {
/* 313 */       super("TcpSocketManager-Reconnector");
/* 314 */       this.owner = owner;
/*     */     }
/*     */     
/*     */     public void latch() {
/*     */       try {
/* 319 */         this.latch.await();
/* 320 */       } catch (InterruptedException interruptedException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void shutdown() {
/* 326 */       this.shutdown = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 331 */       while (!this.shutdown) {
/*     */         try {
/* 333 */           sleep(TcpSocketManager.this.reconnectionDelayMillis);
/* 334 */           reconnect();
/* 335 */         } catch (InterruptedException ie) {
/* 336 */           TcpSocketManager.LOGGER.debug("Reconnection interrupted.");
/* 337 */         } catch (ConnectException ex) {
/* 338 */           TcpSocketManager.LOGGER.debug("{}:{} refused connection", TcpSocketManager.this.host, Integer.valueOf(TcpSocketManager.this.port));
/* 339 */         } catch (IOException ioe) {
/* 340 */           TcpSocketManager.LOGGER.debug("Unable to reconnect to {}:{}", TcpSocketManager.this.host, Integer.valueOf(TcpSocketManager.this.port));
/*     */         } finally {
/* 342 */           this.latch.countDown();
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     void reconnect() throws IOException {
/* 348 */       TcpSocketManager.FACTORY; List<InetSocketAddress> socketAddresses = TcpSocketManager.TcpSocketManagerFactory.resolver.resolveHost(TcpSocketManager.this.host, TcpSocketManager.this.port);
/* 349 */       if (socketAddresses.size() == 1) {
/* 350 */         TcpSocketManager.LOGGER.debug("Reconnecting " + socketAddresses.get(0));
/* 351 */         connect(socketAddresses.get(0));
/*     */       } else {
/* 353 */         IOException ioe = null;
/* 354 */         for (InetSocketAddress socketAddress : socketAddresses) {
/*     */           try {
/* 356 */             TcpSocketManager.LOGGER.debug("Reconnecting " + socketAddress);
/* 357 */             connect(socketAddress);
/*     */             return;
/* 359 */           } catch (IOException ex) {
/* 360 */             ioe = ex;
/*     */           } 
/*     */         } 
/* 363 */         throw ioe;
/*     */       } 
/*     */     }
/*     */     
/*     */     private void connect(InetSocketAddress socketAddress) throws IOException {
/* 368 */       Socket sock = TcpSocketManager.this.createSocket(socketAddress);
/*     */       
/* 370 */       OutputStream newOS = sock.getOutputStream();
/* 371 */       InetAddress prev = (TcpSocketManager.this.socket != null) ? TcpSocketManager.this.socket.getInetAddress() : null;
/* 372 */       synchronized (this.owner) {
/* 373 */         Closer.closeSilently(TcpSocketManager.this.getOutputStream());
/* 374 */         TcpSocketManager.this.setOutputStream(newOS);
/* 375 */         TcpSocketManager.this.socket = sock;
/* 376 */         TcpSocketManager.this.reconnector = null;
/* 377 */         this.shutdown = true;
/*     */       } 
/* 379 */       String type = (prev != null && prev.getHostAddress().equals(socketAddress.getAddress().getHostAddress())) ? "reestablished" : "established";
/*     */       
/* 381 */       TcpSocketManager.LOGGER.debug("Connection to {}:{} {}: {}", TcpSocketManager.this.host, Integer.valueOf(TcpSocketManager.this.port), type, TcpSocketManager.this.socket);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 386 */       return "Reconnector [latch=" + this.latch + ", shutdown=" + this.shutdown + "]";
/*     */     }
/*     */   }
/*     */   
/*     */   private Reconnector createReconnector() {
/* 391 */     Reconnector recon = new Reconnector(this);
/* 392 */     recon.setDaemon(true);
/* 393 */     recon.setPriority(1);
/* 394 */     return recon;
/*     */   }
/*     */   
/*     */   protected Socket createSocket(InetSocketAddress socketAddress) throws IOException {
/* 398 */     return createSocket(socketAddress, this.socketOptions, this.connectTimeoutMillis);
/*     */   }
/*     */ 
/*     */   
/*     */   protected static Socket createSocket(InetSocketAddress socketAddress, SocketOptions socketOptions, int connectTimeoutMillis) throws IOException {
/* 403 */     LOGGER.debug("Creating socket {}", socketAddress.toString());
/* 404 */     Socket newSocket = new Socket();
/* 405 */     if (socketOptions != null)
/*     */     {
/* 407 */       socketOptions.apply(newSocket);
/*     */     }
/* 409 */     newSocket.connect(socketAddress, connectTimeoutMillis);
/* 410 */     if (socketOptions != null)
/*     */     {
/* 412 */       socketOptions.apply(newSocket);
/*     */     }
/* 414 */     return newSocket;
/*     */   }
/*     */ 
/*     */   
/*     */   static class FactoryData
/*     */   {
/*     */     protected final String host;
/*     */     
/*     */     protected final int port;
/*     */     
/*     */     protected final int connectTimeoutMillis;
/*     */     
/*     */     protected final int reconnectDelayMillis;
/*     */     protected final boolean immediateFail;
/*     */     protected final Layout<? extends Serializable> layout;
/*     */     protected final int bufferSize;
/*     */     protected final SocketOptions socketOptions;
/*     */     
/*     */     public FactoryData(String host, int port, int connectTimeoutMillis, int reconnectDelayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize, SocketOptions socketOptions) {
/* 433 */       this.host = host;
/* 434 */       this.port = port;
/* 435 */       this.connectTimeoutMillis = connectTimeoutMillis;
/* 436 */       this.reconnectDelayMillis = reconnectDelayMillis;
/* 437 */       this.immediateFail = immediateFail;
/* 438 */       this.layout = layout;
/* 439 */       this.bufferSize = bufferSize;
/* 440 */       this.socketOptions = socketOptions;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 445 */       return "FactoryData [host=" + this.host + ", port=" + this.port + ", connectTimeoutMillis=" + this.connectTimeoutMillis + ", reconnectDelayMillis=" + this.reconnectDelayMillis + ", immediateFail=" + this.immediateFail + ", layout=" + this.layout + ", bufferSize=" + this.bufferSize + ", socketOptions=" + this.socketOptions + "]";
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
/*     */   protected static class TcpSocketManagerFactory<M extends TcpSocketManager, T extends FactoryData>
/*     */     implements ManagerFactory<M, T>
/*     */   {
/* 462 */     static TcpSocketManager.HostResolver resolver = new TcpSocketManager.HostResolver();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public M createManager(String name, T data) {
/*     */       InetAddress inetAddress;
/*     */       try {
/* 470 */         inetAddress = InetAddress.getByName(((TcpSocketManager.FactoryData)data).host);
/* 471 */       } catch (UnknownHostException ex) {
/* 472 */         TcpSocketManager.LOGGER.error("Could not find address of {}: {}", ((TcpSocketManager.FactoryData)data).host, ex, ex);
/* 473 */         return null;
/*     */       } 
/* 475 */       Socket socket = null;
/*     */       
/*     */       try {
/* 478 */         socket = createSocket(data);
/* 479 */         OutputStream os = socket.getOutputStream();
/* 480 */         return createManager(name, os, socket, inetAddress, data);
/* 481 */       } catch (IOException ex) {
/* 482 */         TcpSocketManager.LOGGER.error("TcpSocketManager ({}) caught exception and will continue:", name, ex);
/* 483 */         NullOutputStream nullOutputStream = NullOutputStream.getInstance();
/*     */         
/* 485 */         if (((TcpSocketManager.FactoryData)data).reconnectDelayMillis == 0) {
/* 486 */           Closer.closeSilently(socket);
/* 487 */           return null;
/*     */         } 
/* 489 */         return createManager(name, (OutputStream)nullOutputStream, null, inetAddress, data);
/*     */       } 
/*     */     }
/*     */     
/*     */     M createManager(String name, OutputStream os, Socket socket, InetAddress inetAddress, T data) {
/* 494 */       return (M)new TcpSocketManager(name, os, socket, inetAddress, ((TcpSocketManager.FactoryData)data).host, ((TcpSocketManager.FactoryData)data).port, ((TcpSocketManager.FactoryData)data).connectTimeoutMillis, ((TcpSocketManager.FactoryData)data).reconnectDelayMillis, ((TcpSocketManager.FactoryData)data).immediateFail, ((TcpSocketManager.FactoryData)data).layout, ((TcpSocketManager.FactoryData)data).bufferSize, ((TcpSocketManager.FactoryData)data).socketOptions);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Socket createSocket(T data) throws IOException {
/* 500 */       List<InetSocketAddress> socketAddresses = resolver.resolveHost(((TcpSocketManager.FactoryData)data).host, ((TcpSocketManager.FactoryData)data).port);
/* 501 */       IOException ioe = null;
/* 502 */       for (InetSocketAddress socketAddress : socketAddresses) {
/*     */         try {
/* 504 */           return TcpSocketManager.createSocket(socketAddress, ((TcpSocketManager.FactoryData)data).socketOptions, ((TcpSocketManager.FactoryData)data).connectTimeoutMillis);
/* 505 */         } catch (IOException ex) {
/* 506 */           ioe = ex;
/*     */         } 
/*     */       } 
/* 509 */       throw new IOException(errorMessage(data, socketAddresses), ioe);
/*     */     }
/*     */     
/*     */     protected String errorMessage(T data, List<InetSocketAddress> socketAddresses) {
/* 513 */       StringBuilder sb = new StringBuilder("Unable to create socket for ");
/* 514 */       sb.append(((TcpSocketManager.FactoryData)data).host).append(" at port ").append(((TcpSocketManager.FactoryData)data).port);
/* 515 */       if (socketAddresses.size() == 1) {
/* 516 */         if (!((InetSocketAddress)socketAddresses.get(0)).getAddress().getHostAddress().equals(((TcpSocketManager.FactoryData)data).host)) {
/* 517 */           sb.append(" using ip address ").append(((InetSocketAddress)socketAddresses.get(0)).getAddress().getHostAddress());
/* 518 */           sb.append(" and port ").append(((InetSocketAddress)socketAddresses.get(0)).getPort());
/*     */         } 
/*     */       } else {
/* 521 */         sb.append(" using ip addresses and ports ");
/* 522 */         for (int i = 0; i < socketAddresses.size(); i++) {
/* 523 */           if (i > 0) {
/* 524 */             sb.append(", ");
/* 525 */             sb.append(((InetSocketAddress)socketAddresses.get(i)).getAddress().getHostAddress());
/* 526 */             sb.append(":").append(((InetSocketAddress)socketAddresses.get(i)).getPort());
/*     */           } 
/*     */         } 
/*     */       } 
/* 530 */       return sb.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setHostResolver(HostResolver resolver) {
/* 540 */     TcpSocketManagerFactory.resolver = resolver;
/*     */   }
/*     */   
/*     */   public static class HostResolver
/*     */   {
/*     */     public List<InetSocketAddress> resolveHost(String host, int port) throws UnknownHostException {
/* 546 */       InetAddress[] addresses = InetAddress.getAllByName(host);
/* 547 */       List<InetSocketAddress> socketAddresses = new ArrayList<>(addresses.length);
/* 548 */       for (InetAddress address : addresses) {
/* 549 */         socketAddresses.add(new InetSocketAddress(address, port));
/*     */       }
/* 551 */       return socketAddresses;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SocketOptions getSocketOptions() {
/* 559 */     return this.socketOptions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket getSocket() {
/* 566 */     return this.socket;
/*     */   }
/*     */   
/*     */   public int getReconnectionDelayMillis() {
/* 570 */     return this.reconnectionDelayMillis;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 575 */     return "TcpSocketManager [reconnectionDelayMillis=" + this.reconnectionDelayMillis + ", reconnector=" + this.reconnector + ", socket=" + this.socket + ", socketOptions=" + this.socketOptions + ", retry=" + this.retry + ", immediateFail=" + this.immediateFail + ", connectTimeoutMillis=" + this.connectTimeoutMillis + ", inetAddress=" + this.inetAddress + ", host=" + this.host + ", port=" + this.port + ", layout=" + this.layout + ", byteBuffer=" + this.byteBuffer + ", count=" + this.count + "]";
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\net\TcpSocketManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */