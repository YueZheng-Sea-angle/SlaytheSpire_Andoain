/*     */ package org.apache.commons.net.telnet;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TelnetClient
/*     */   extends Telnet
/*     */ {
/*     */   private InputStream __input;
/*     */   private OutputStream __output;
/*     */   protected boolean readerThread = true;
/*     */   private TelnetInputListener inputListener;
/*     */   
/*     */   public TelnetClient() {
/*  57 */     super("VT100");
/*     */     
/*  59 */     this.__input = null;
/*  60 */     this.__output = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TelnetClient(String termtype) {
/*  71 */     super(termtype);
/*  72 */     this.__input = null;
/*  73 */     this.__output = null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void _flushOutputStream() throws IOException {
/*  79 */     this._output_.flush();
/*     */   }
/*     */   
/*     */   void _closeOutputStream() throws IOException {
/*  83 */     this._output_.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void _connectAction_() throws IOException {
/*  94 */     super._connectAction_();
/*  95 */     TelnetInputStream tmp = new TelnetInputStream(this._input_, this, this.readerThread);
/*  96 */     if (this.readerThread)
/*     */     {
/*  98 */       tmp._start();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 106 */     this.__input = new BufferedInputStream(tmp);
/* 107 */     this.__output = new TelnetOutputStream(this);
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
/*     */   public void disconnect() throws IOException {
/* 120 */     if (this.__input != null) {
/* 121 */       this.__input.close();
/*     */     }
/* 123 */     if (this.__output != null) {
/* 124 */       this.__output.close();
/*     */     }
/* 126 */     super.disconnect();
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
/*     */   public OutputStream getOutputStream() {
/* 138 */     return this.__output;
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
/*     */   public InputStream getInputStream() {
/* 150 */     return this.__input;
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
/*     */   public boolean getLocalOptionState(int option) {
/* 163 */     return (_stateIsWill(option) && _requestedWill(option));
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
/*     */   public boolean getRemoteOptionState(int option) {
/* 177 */     return (_stateIsDo(option) && _requestedDo(option));
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
/*     */   public boolean sendAYT(long timeout) throws IOException, IllegalArgumentException, InterruptedException {
/* 198 */     return _sendAYT(timeout);
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
/*     */   public void sendSubnegotiation(int[] message) throws IOException, IllegalArgumentException {
/* 222 */     if (message.length < 1) {
/* 223 */       throw new IllegalArgumentException("zero length message");
/*     */     }
/* 225 */     _sendSubnegotiation(message);
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
/*     */   public void sendCommand(byte command) throws IOException, IllegalArgumentException {
/* 245 */     _sendCommand(command);
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
/*     */   public void addOptionHandler(TelnetOptionHandler opthand) throws InvalidTelnetOptionException, IOException {
/* 262 */     super.addOptionHandler(opthand);
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
/*     */   public void deleteOptionHandler(int optcode) throws InvalidTelnetOptionException, IOException {
/* 278 */     super.deleteOptionHandler(optcode);
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
/*     */   public void registerSpyStream(OutputStream spystream) {
/* 291 */     _registerSpyStream(spystream);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stopSpyStream() {
/* 300 */     _stopSpyStream();
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
/*     */   public void registerNotifHandler(TelnetNotificationHandler notifhand) {
/* 313 */     super.registerNotifHandler(notifhand);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unregisterNotifHandler() {
/* 323 */     super.unregisterNotifHandler();
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
/*     */   public void setReaderThread(boolean flag) {
/* 354 */     this.readerThread = flag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getReaderThread() {
/* 364 */     return this.readerThread;
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
/*     */   public synchronized void registerInputListener(TelnetInputListener listener) {
/* 391 */     this.inputListener = listener;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void unregisterInputListener() {
/* 401 */     this.inputListener = null;
/*     */   }
/*     */ 
/*     */   
/*     */   void notifyInputListener() {
/*     */     TelnetInputListener listener;
/* 407 */     synchronized (this) {
/* 408 */       listener = this.inputListener;
/*     */     } 
/* 410 */     if (listener != null)
/* 411 */       listener.telnetInputAvailable(); 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\telnet\TelnetClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */