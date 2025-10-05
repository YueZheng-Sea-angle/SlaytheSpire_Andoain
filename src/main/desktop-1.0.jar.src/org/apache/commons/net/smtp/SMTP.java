/*     */ package org.apache.commons.net.smtp;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SMTP
/*     */   extends SocketClient
/*     */ {
/*     */   public static final int DEFAULT_PORT = 25;
/*     */   private static final String __DEFAULT_ENCODING = "ISO-8859-1";
/*     */   protected final String encoding;
/*     */   protected ProtocolCommandSupport _commandSupport_;
/*     */   BufferedReader _reader;
/*     */   BufferedWriter _writer;
/*     */   private int _replyCode;
/*     */   private final ArrayList<String> _replyLines;
/*     */   private boolean _newReplyString;
/*     */   private String _replyString;
/*     */   
/*     */   public SMTP() {
/* 122 */     this("ISO-8859-1");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SMTP(String encoding) {
/* 131 */     setDefaultPort(25);
/* 132 */     this._replyLines = new ArrayList<String>();
/* 133 */     this._newReplyString = false;
/* 134 */     this._replyString = null;
/* 135 */     this._commandSupport_ = new ProtocolCommandSupport(this);
/* 136 */     this.encoding = encoding;
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
/*     */   private int __sendCommand(String command, String args, boolean includeSpace) throws IOException {
/* 151 */     StringBuilder __commandBuffer = new StringBuilder();
/* 152 */     __commandBuffer.append(command);
/*     */     
/* 154 */     if (args != null) {
/*     */       
/* 156 */       if (includeSpace) {
/* 157 */         __commandBuffer.append(' ');
/*     */       }
/* 159 */       __commandBuffer.append(args);
/*     */     } 
/*     */     
/* 162 */     __commandBuffer.append("\r\n");
/*     */     
/*     */     String message;
/* 165 */     this._writer.write(message = __commandBuffer.toString());
/* 166 */     this._writer.flush();
/*     */     
/* 168 */     fireCommandSent(command, message);
/*     */     
/* 170 */     __getReply();
/* 171 */     return this._replyCode;
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
/*     */   private int __sendCommand(int command, String args, boolean includeSpace) throws IOException {
/* 185 */     return __sendCommand(SMTPCommand.getCommand(command), args, includeSpace);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void __getReply() throws IOException {
/* 192 */     this._newReplyString = true;
/* 193 */     this._replyLines.clear();
/*     */     
/* 195 */     String line = this._reader.readLine();
/*     */     
/* 197 */     if (line == null) {
/* 198 */       throw new SMTPConnectionClosedException("Connection closed without indication.");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 204 */     int length = line.length();
/* 205 */     if (length < 3) {
/* 206 */       throw new MalformedServerReplyException("Truncated server reply: " + line);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 212 */       String code = line.substring(0, 3);
/* 213 */       this._replyCode = Integer.parseInt(code);
/*     */     }
/* 215 */     catch (NumberFormatException e) {
/*     */       
/* 217 */       throw new MalformedServerReplyException("Could not parse response code.\nServer Reply: " + line);
/*     */     } 
/*     */ 
/*     */     
/* 221 */     this._replyLines.add(line);
/*     */ 
/*     */     
/* 224 */     if (length > 3 && line.charAt(3) == '-') {
/*     */       do
/*     */       {
/*     */         
/* 228 */         line = this._reader.readLine();
/*     */         
/* 230 */         if (line == null) {
/* 231 */           throw new SMTPConnectionClosedException("Connection closed without indication.");
/*     */         }
/*     */ 
/*     */         
/* 235 */         this._replyLines.add(line);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 241 */       while (line.length() < 4 || line.charAt(3) == '-' || !Character.isDigit(line.charAt(0)));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 248 */     fireReplyReceived(this._replyCode, getReplyString());
/*     */     
/* 250 */     if (this._replyCode == 421) {
/* 251 */       throw new SMTPConnectionClosedException("SMTP response 421 received.  Server closed connection.");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void _connectAction_() throws IOException {
/* 260 */     super._connectAction_();
/* 261 */     this._reader = (BufferedReader)new CRLFLineReader(new InputStreamReader(this._input_, this.encoding));
/*     */ 
/*     */     
/* 264 */     this._writer = new BufferedWriter(new OutputStreamWriter(this._output_, this.encoding));
/*     */ 
/*     */     
/* 267 */     __getReply();
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
/*     */   public void disconnect() throws IOException {
/* 283 */     super.disconnect();
/* 284 */     this._reader = null;
/* 285 */     this._writer = null;
/* 286 */     this._replyString = null;
/* 287 */     this._replyLines.clear();
/* 288 */     this._newReplyString = false;
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
/*     */   public int sendCommand(String command, String args) throws IOException {
/* 314 */     return __sendCommand(command, args, true);
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
/*     */   public int sendCommand(int command, String args) throws IOException {
/* 341 */     return sendCommand(SMTPCommand.getCommand(command), args);
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
/*     */   public int sendCommand(String command) throws IOException {
/* 365 */     return sendCommand(command, (String)null);
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
/*     */   public int sendCommand(int command) throws IOException {
/* 390 */     return sendCommand(command, (String)null);
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
/*     */   public int getReplyCode() {
/* 404 */     return this._replyCode;
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
/*     */   public int getReply() throws IOException {
/* 426 */     __getReply();
/* 427 */     return this._replyCode;
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
/*     */   public String[] getReplyStrings() {
/* 440 */     return this._replyLines.<String>toArray(new String[this._replyLines.size()]);
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
/*     */   public String getReplyString() {
/* 454 */     if (!this._newReplyString) {
/* 455 */       return this._replyString;
/*     */     }
/*     */     
/* 458 */     StringBuilder buffer = new StringBuilder();
/*     */     
/* 460 */     for (String line : this._replyLines) {
/*     */       
/* 462 */       buffer.append(line);
/* 463 */       buffer.append("\r\n");
/*     */     } 
/*     */     
/* 466 */     this._newReplyString = false;
/*     */     
/* 468 */     return this._replyString = buffer.toString();
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
/*     */   public int helo(String hostname) throws IOException {
/* 488 */     return sendCommand(0, hostname);
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
/*     */   public int mail(String reversePath) throws IOException {
/* 508 */     return __sendCommand(1, reversePath, false);
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
/*     */   public int rcpt(String forwardPath) throws IOException {
/* 528 */     return __sendCommand(2, forwardPath, false);
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
/*     */   public int data() throws IOException {
/* 547 */     return sendCommand(3);
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
/*     */   public int send(String reversePath) throws IOException {
/* 567 */     return sendCommand(4, reversePath);
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
/*     */   public int soml(String reversePath) throws IOException {
/* 587 */     return sendCommand(5, reversePath);
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
/*     */   public int saml(String reversePath) throws IOException {
/* 607 */     return sendCommand(6, reversePath);
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
/*     */   public int rset() throws IOException {
/* 626 */     return sendCommand(7);
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
/*     */   public int vrfy(String user) throws IOException {
/* 646 */     return sendCommand(8, user);
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
/*     */   public int expn(String name) throws IOException {
/* 666 */     return sendCommand(9, name);
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
/*     */   public int help() throws IOException {
/* 684 */     return sendCommand(10);
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
/*     */   public int help(String command) throws IOException {
/* 703 */     return sendCommand(10, command);
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
/*     */   public int noop() throws IOException {
/* 721 */     return sendCommand(11);
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
/*     */   public int turn() throws IOException {
/* 740 */     return sendCommand(12);
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
/*     */   public int quit() throws IOException {
/* 759 */     return sendCommand(13);
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
/* 770 */     removeProtocolCommandListener(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ProtocolCommandSupport getCommandSupport() {
/* 778 */     return this._commandSupport_;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\smtp\SMTP.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */