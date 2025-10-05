/*     */ package org.apache.commons.net.imap;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.commons.net.SocketClient;
/*     */ import org.apache.commons.net.io.CRLFLineReader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IMAP
/*     */   extends SocketClient
/*     */ {
/*     */   public static final int DEFAULT_PORT = 143;
/*     */   protected static final String __DEFAULT_ENCODING = "ISO-8859-1";
/*     */   private IMAPState __state;
/*     */   protected BufferedWriter __writer;
/*     */   protected BufferedReader _reader;
/*     */   private int _replyCode;
/*     */   private final List<String> _replyLines;
/*     */   
/*     */   public static interface IMAPChunkListener
/*     */   {
/*     */     boolean chunkReceived(IMAP param1IMAP);
/*     */   }
/*     */   
/*     */   public enum IMAPState
/*     */   {
/*  45 */     DISCONNECTED_STATE,
/*     */     
/*  47 */     NOT_AUTH_STATE,
/*     */     
/*  49 */     AUTH_STATE,
/*     */     
/*  51 */     LOGOUT_STATE;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   public static final IMAPChunkListener TRUE_CHUNK_LISTENER = new IMAPChunkListener()
/*     */     {
/*     */       public boolean chunkReceived(IMAP imap) {
/* 106 */         return true;
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   private volatile IMAPChunkListener __chunkListener;
/* 112 */   private final char[] _initialID = new char[] { 'A', 'A', 'A', 'A' };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IMAP() {
/* 120 */     setDefaultPort(143);
/* 121 */     this.__state = IMAPState.DISCONNECTED_STATE;
/* 122 */     this._reader = null;
/* 123 */     this.__writer = null;
/* 124 */     this._replyLines = new ArrayList<String>();
/* 125 */     createCommandSupport();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void __getReply() throws IOException {
/* 135 */     __getReply(true);
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
/*     */   private void __getReply(boolean wantTag) throws IOException {
/* 147 */     this._replyLines.clear();
/* 148 */     String line = this._reader.readLine();
/*     */     
/* 150 */     if (line == null) {
/* 151 */       throw new EOFException("Connection closed without indication.");
/*     */     }
/*     */     
/* 154 */     this._replyLines.add(line);
/*     */     
/* 156 */     if (wantTag) {
/* 157 */       while (IMAPReply.isUntagged(line)) {
/* 158 */         int literalCount = IMAPReply.literalCount(line);
/* 159 */         boolean isMultiLine = (literalCount >= 0);
/* 160 */         while (literalCount >= 0) {
/* 161 */           line = this._reader.readLine();
/* 162 */           if (line == null) {
/* 163 */             throw new EOFException("Connection closed without indication.");
/*     */           }
/* 165 */           this._replyLines.add(line);
/* 166 */           literalCount -= line.length() + 2;
/*     */         } 
/* 168 */         if (isMultiLine) {
/* 169 */           IMAPChunkListener il = this.__chunkListener;
/* 170 */           if (il != null) {
/* 171 */             boolean clear = il.chunkReceived(this);
/* 172 */             if (clear) {
/* 173 */               fireReplyReceived(3, getReplyString());
/* 174 */               this._replyLines.clear();
/*     */             } 
/*     */           } 
/*     */         } 
/* 178 */         line = this._reader.readLine();
/* 179 */         if (line == null) {
/* 180 */           throw new EOFException("Connection closed without indication.");
/*     */         }
/* 182 */         this._replyLines.add(line);
/*     */       } 
/*     */       
/* 185 */       this._replyCode = IMAPReply.getReplyCode(line);
/*     */     } else {
/* 187 */       this._replyCode = IMAPReply.getUntaggedReplyCode(line);
/*     */     } 
/*     */     
/* 190 */     fireReplyReceived(this._replyCode, getReplyString());
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
/*     */   protected void fireReplyReceived(int replyCode, String ignored) {
/* 204 */     if (getCommandSupport().getListenerCount() > 0) {
/* 205 */       getCommandSupport().fireReplyReceived(replyCode, getReplyString());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void _connectAction_() throws IOException {
/* 216 */     super._connectAction_();
/* 217 */     this._reader = (BufferedReader)new CRLFLineReader(new InputStreamReader(this._input_, "ISO-8859-1"));
/*     */ 
/*     */     
/* 220 */     this.__writer = new BufferedWriter(new OutputStreamWriter(this._output_, "ISO-8859-1"));
/*     */ 
/*     */     
/* 223 */     int tmo = getSoTimeout();
/* 224 */     if (tmo <= 0) {
/* 225 */       setSoTimeout(this.connectTimeout);
/*     */     }
/* 227 */     __getReply(false);
/* 228 */     if (tmo <= 0) {
/* 229 */       setSoTimeout(tmo);
/*     */     }
/* 231 */     setState(IMAPState.NOT_AUTH_STATE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setState(IMAPState state) {
/* 242 */     this.__state = state;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IMAPState getState() {
/* 253 */     return this.__state;
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
/*     */   public void disconnect() throws IOException {
/* 267 */     super.disconnect();
/* 268 */     this._reader = null;
/* 269 */     this.__writer = null;
/* 270 */     this._replyLines.clear();
/* 271 */     setState(IMAPState.DISCONNECTED_STATE);
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
/*     */   private int sendCommandWithID(String commandID, String command, String args) throws IOException {
/* 285 */     StringBuilder __commandBuffer = new StringBuilder();
/* 286 */     if (commandID != null) {
/*     */       
/* 288 */       __commandBuffer.append(commandID);
/* 289 */       __commandBuffer.append(' ');
/*     */     } 
/* 291 */     __commandBuffer.append(command);
/*     */     
/* 293 */     if (args != null) {
/*     */       
/* 295 */       __commandBuffer.append(' ');
/* 296 */       __commandBuffer.append(args);
/*     */     } 
/* 298 */     __commandBuffer.append("\r\n");
/*     */     
/* 300 */     String message = __commandBuffer.toString();
/* 301 */     this.__writer.write(message);
/* 302 */     this.__writer.flush();
/*     */     
/* 304 */     fireCommandSent(command, message);
/*     */     
/* 306 */     __getReply();
/* 307 */     return this._replyCode;
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
/*     */   public int sendCommand(String command, String args) throws IOException {
/* 320 */     return sendCommandWithID(generateCommandID(), command, args);
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
/*     */   public int sendCommand(String command) throws IOException {
/* 333 */     return sendCommand(command, (String)null);
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
/*     */   public int sendCommand(IMAPCommand command, String args) throws IOException {
/* 347 */     return sendCommand(command.getIMAPCommand(), args);
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
/*     */   public boolean doCommand(IMAPCommand command, String args) throws IOException {
/* 361 */     return IMAPReply.isSuccess(sendCommand(command, args));
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
/*     */   public int sendCommand(IMAPCommand command) throws IOException {
/* 375 */     return sendCommand(command, (String)null);
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
/*     */   public boolean doCommand(IMAPCommand command) throws IOException {
/* 388 */     return IMAPReply.isSuccess(sendCommand(command));
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
/*     */   public int sendData(String command) throws IOException {
/* 400 */     return sendCommandWithID((String)null, command, (String)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getReplyStrings() {
/* 410 */     return this._replyLines.<String>toArray(new String[this._replyLines.size()]);
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
/*     */   public String getReplyString() {
/* 422 */     StringBuilder buffer = new StringBuilder(256);
/* 423 */     for (String s : this._replyLines) {
/*     */       
/* 425 */       buffer.append(s);
/* 426 */       buffer.append("\r\n");
/*     */     } 
/*     */     
/* 429 */     return buffer.toString();
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
/*     */   public void setChunkListener(IMAPChunkListener listener) {
/* 444 */     this.__chunkListener = listener;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String generateCommandID() {
/* 453 */     String res = new String(this._initialID);
/*     */     
/* 455 */     boolean carry = true;
/* 456 */     for (int i = this._initialID.length - 1; carry && i >= 0; i--) {
/*     */       
/* 458 */       if (this._initialID[i] == 'Z') {
/*     */         
/* 460 */         this._initialID[i] = 'A';
/*     */       }
/*     */       else {
/*     */         
/* 464 */         this._initialID[i] = (char)(this._initialID[i] + 1);
/* 465 */         carry = false;
/*     */       } 
/*     */     } 
/* 468 */     return res;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\imap\IMAP.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */