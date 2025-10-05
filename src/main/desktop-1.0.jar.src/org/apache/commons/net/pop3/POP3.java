/*     */ package org.apache.commons.net.pop3;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.commons.net.MalformedServerReplyException;
/*     */ import org.apache.commons.net.ProtocolCommandListener;
/*     */ import org.apache.commons.net.ProtocolCommandSupport;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class POP3
/*     */   extends SocketClient
/*     */ {
/*     */   public static final int DEFAULT_PORT = 110;
/*     */   public static final int DISCONNECTED_STATE = -1;
/*     */   public static final int AUTHORIZATION_STATE = 0;
/*     */   public static final int TRANSACTION_STATE = 1;
/*     */   public static final int UPDATE_STATE = 2;
/*     */   static final String _OK = "+OK";
/*     */   static final String _OK_INT = "+ ";
/*     */   static final String _ERROR = "-ERR";
/*     */   static final String _DEFAULT_ENCODING = "ISO-8859-1";
/*     */   private int __popState;
/*     */   BufferedWriter _writer;
/*     */   BufferedReader _reader;
/*     */   int _replyCode;
/*     */   String _lastReplyLine;
/*     */   List<String> _replyLines;
/*     */   protected ProtocolCommandSupport _commandSupport_;
/*     */   
/*     */   public POP3() {
/* 101 */     setDefaultPort(110);
/* 102 */     this.__popState = -1;
/* 103 */     this._reader = null;
/* 104 */     this._writer = null;
/* 105 */     this._replyLines = new ArrayList<String>();
/* 106 */     this._commandSupport_ = new ProtocolCommandSupport(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void __getReply() throws IOException {
/* 113 */     this._replyLines.clear();
/* 114 */     String line = this._reader.readLine();
/*     */     
/* 116 */     if (line == null) {
/* 117 */       throw new EOFException("Connection closed without indication.");
/*     */     }
/*     */     
/* 120 */     if (line.startsWith("+OK")) {
/* 121 */       this._replyCode = 0;
/* 122 */     } else if (line.startsWith("-ERR")) {
/* 123 */       this._replyCode = 1;
/* 124 */     } else if (line.startsWith("+ ")) {
/* 125 */       this._replyCode = 2;
/*     */     } else {
/* 127 */       throw new MalformedServerReplyException("Received invalid POP3 protocol response from server." + line);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 132 */     this._replyLines.add(line);
/* 133 */     this._lastReplyLine = line;
/*     */     
/* 135 */     fireReplyReceived(this._replyCode, getReplyString());
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
/* 146 */     super._connectAction_();
/* 147 */     this._reader = (BufferedReader)new CRLFLineReader(new InputStreamReader(this._input_, "ISO-8859-1"));
/*     */ 
/*     */     
/* 150 */     this._writer = new BufferedWriter(new OutputStreamWriter(this._output_, "ISO-8859-1"));
/*     */ 
/*     */     
/* 153 */     __getReply();
/* 154 */     setState(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setState(int state) {
/* 164 */     this.__popState = state;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getState() {
/* 175 */     return this.__popState;
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
/*     */   public void getAdditionalReply() throws IOException {
/* 187 */     String line = this._reader.readLine();
/* 188 */     while (line != null) {
/*     */       
/* 190 */       this._replyLines.add(line);
/* 191 */       if (line.equals(".")) {
/*     */         break;
/*     */       }
/* 194 */       line = this._reader.readLine();
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
/*     */   
/*     */   public void disconnect() throws IOException {
/* 210 */     super.disconnect();
/* 211 */     this._reader = null;
/* 212 */     this._writer = null;
/* 213 */     this._lastReplyLine = null;
/* 214 */     this._replyLines.clear();
/* 215 */     setState(-1);
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
/*     */   public int sendCommand(String command, String args) throws IOException {
/* 229 */     if (this._writer == null) {
/* 230 */       throw new IllegalStateException("Socket is not connected");
/*     */     }
/* 232 */     StringBuilder __commandBuffer = new StringBuilder();
/* 233 */     __commandBuffer.append(command);
/*     */     
/* 235 */     if (args != null) {
/*     */       
/* 237 */       __commandBuffer.append(' ');
/* 238 */       __commandBuffer.append(args);
/*     */     } 
/* 240 */     __commandBuffer.append("\r\n");
/*     */     
/* 242 */     String message = __commandBuffer.toString();
/* 243 */     this._writer.write(message);
/* 244 */     this._writer.flush();
/*     */     
/* 246 */     fireCommandSent(command, message);
/*     */     
/* 248 */     __getReply();
/* 249 */     return this._replyCode;
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
/* 262 */     return sendCommand(command, (String)null);
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
/*     */   public int sendCommand(int command, String args) throws IOException {
/* 276 */     return sendCommand(POP3Command._commands[command], args);
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
/*     */   public int sendCommand(int command) throws IOException {
/* 290 */     return sendCommand(POP3Command._commands[command], (String)null);
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
/*     */   public String[] getReplyStrings() {
/* 308 */     return this._replyLines.<String>toArray(new String[this._replyLines.size()]);
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
/*     */   public String getReplyString() {
/* 325 */     StringBuilder buffer = new StringBuilder(256);
/*     */     
/* 327 */     for (String entry : this._replyLines) {
/*     */       
/* 329 */       buffer.append(entry);
/* 330 */       buffer.append("\r\n");
/*     */     } 
/*     */     
/* 333 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeProtocolCommandistener(ProtocolCommandListener listener) {
/* 344 */     removeProtocolCommandListener(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ProtocolCommandSupport getCommandSupport() {
/* 352 */     return this._commandSupport_;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\pop3\POP3.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */