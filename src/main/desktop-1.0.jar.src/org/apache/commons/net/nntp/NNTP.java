/*      */ package org.apache.commons.net.nntp;
/*      */ 
/*      */ import java.io.BufferedReader;
/*      */ import java.io.BufferedWriter;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.OutputStreamWriter;
/*      */ import org.apache.commons.net.MalformedServerReplyException;
/*      */ import org.apache.commons.net.ProtocolCommandSupport;
/*      */ import org.apache.commons.net.SocketClient;
/*      */ import org.apache.commons.net.io.CRLFLineReader;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class NNTP
/*      */   extends SocketClient
/*      */ {
/*      */   public static final int DEFAULT_PORT = 119;
/*      */   private static final String __DEFAULT_ENCODING = "ISO-8859-1";
/*      */   boolean _isAllowedToPost;
/*      */   int _replyCode;
/*      */   String _replyString;
/*      */   protected BufferedReader _reader_;
/*      */   protected BufferedWriter _writer_;
/*      */   protected ProtocolCommandSupport _commandSupport_;
/*      */   
/*      */   public NNTP() {
/*  121 */     setDefaultPort(119);
/*  122 */     this._replyString = null;
/*  123 */     this._reader_ = null;
/*  124 */     this._writer_ = null;
/*  125 */     this._isAllowedToPost = false;
/*  126 */     this._commandSupport_ = new ProtocolCommandSupport(this);
/*      */   }
/*      */ 
/*      */   
/*      */   private void __getReply() throws IOException {
/*  131 */     this._replyString = this._reader_.readLine();
/*      */     
/*  133 */     if (this._replyString == null) {
/*  134 */       throw new NNTPConnectionClosedException("Connection closed without indication.");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  140 */     if (this._replyString.length() < 3) {
/*  141 */       throw new MalformedServerReplyException("Truncated server reply: " + this._replyString);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  147 */       this._replyCode = Integer.parseInt(this._replyString.substring(0, 3));
/*      */     }
/*  149 */     catch (NumberFormatException e) {
/*      */       
/*  151 */       throw new MalformedServerReplyException("Could not parse response code.\nServer Reply: " + this._replyString);
/*      */     } 
/*      */ 
/*      */     
/*  155 */     fireReplyReceived(this._replyCode, this._replyString + "\r\n");
/*      */     
/*  157 */     if (this._replyCode == 400) {
/*  158 */       throw new NNTPConnectionClosedException("NNTP response 400 received.  Server closed connection.");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _connectAction_() throws IOException {
/*  172 */     super._connectAction_();
/*  173 */     this._reader_ = (BufferedReader)new CRLFLineReader(new InputStreamReader(this._input_, "ISO-8859-1"));
/*      */ 
/*      */     
/*  176 */     this._writer_ = new BufferedWriter(new OutputStreamWriter(this._output_, "ISO-8859-1"));
/*      */ 
/*      */     
/*  179 */     __getReply();
/*      */     
/*  181 */     this._isAllowedToPost = (this._replyCode == 200);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void disconnect() throws IOException {
/*  195 */     super.disconnect();
/*  196 */     this._reader_ = null;
/*  197 */     this._writer_ = null;
/*  198 */     this._replyString = null;
/*  199 */     this._isAllowedToPost = false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAllowedToPost() {
/*  212 */     return this._isAllowedToPost;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int sendCommand(String command, String args) throws IOException {
/*  237 */     StringBuilder __commandBuffer = new StringBuilder();
/*  238 */     __commandBuffer.append(command);
/*      */     
/*  240 */     if (args != null) {
/*      */       
/*  242 */       __commandBuffer.append(' ');
/*  243 */       __commandBuffer.append(args);
/*      */     } 
/*  245 */     __commandBuffer.append("\r\n");
/*      */     
/*      */     String message;
/*  248 */     this._writer_.write(message = __commandBuffer.toString());
/*  249 */     this._writer_.flush();
/*      */     
/*  251 */     fireCommandSent(command, message);
/*      */     
/*  253 */     __getReply();
/*  254 */     return this._replyCode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int sendCommand(int command, String args) throws IOException {
/*  281 */     return sendCommand(NNTPCommand.getCommand(command), args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int sendCommand(String command) throws IOException {
/*  305 */     return sendCommand(command, (String)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int sendCommand(int command) throws IOException {
/*  330 */     return sendCommand(command, (String)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getReplyCode() {
/*  344 */     return this._replyCode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getReply() throws IOException {
/*  366 */     __getReply();
/*  367 */     return this._replyCode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getReplyString() {
/*  379 */     return this._replyString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int article(String messageId) throws IOException {
/*  400 */     return sendCommand(0, messageId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int article(long articleNumber) throws IOException {
/*  420 */     return sendCommand(0, Long.toString(articleNumber));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int article() throws IOException {
/*  438 */     return sendCommand(0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int body(String messageId) throws IOException {
/*  460 */     return sendCommand(1, messageId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int body(long articleNumber) throws IOException {
/*  480 */     return sendCommand(1, Long.toString(articleNumber));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int body() throws IOException {
/*  498 */     return sendCommand(1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int head(String messageId) throws IOException {
/*  520 */     return sendCommand(3, messageId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int head(long articleNumber) throws IOException {
/*  540 */     return sendCommand(3, Long.toString(articleNumber));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int head() throws IOException {
/*  558 */     return sendCommand(3);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int stat(String messageId) throws IOException {
/*  580 */     return sendCommand(14, messageId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int stat(long articleNumber) throws IOException {
/*  600 */     return sendCommand(14, Long.toString(articleNumber));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int stat() throws IOException {
/*  618 */     return sendCommand(14);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int group(String newsgroup) throws IOException {
/*  638 */     return sendCommand(2, newsgroup);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int help() throws IOException {
/*  657 */     return sendCommand(4);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int ihave(String messageId) throws IOException {
/*  678 */     return sendCommand(5, messageId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int last() throws IOException {
/*  697 */     return sendCommand(6);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int list() throws IOException {
/*  717 */     return sendCommand(7);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int next() throws IOException {
/*  737 */     return sendCommand(10);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int newgroups(String date, String time, boolean GMT, String distributions) throws IOException {
/*  764 */     StringBuilder buffer = new StringBuilder();
/*      */     
/*  766 */     buffer.append(date);
/*  767 */     buffer.append(' ');
/*  768 */     buffer.append(time);
/*      */     
/*  770 */     if (GMT) {
/*      */       
/*  772 */       buffer.append(' ');
/*  773 */       buffer.append("GMT");
/*      */     } 
/*      */     
/*  776 */     if (distributions != null) {
/*      */       
/*  778 */       buffer.append(" <");
/*  779 */       buffer.append(distributions);
/*  780 */       buffer.append('>');
/*      */     } 
/*      */     
/*  783 */     return sendCommand(8, buffer.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int newnews(String newsgroups, String date, String time, boolean GMT, String distributions) throws IOException {
/*  812 */     StringBuilder buffer = new StringBuilder();
/*      */     
/*  814 */     buffer.append(newsgroups);
/*  815 */     buffer.append(' ');
/*  816 */     buffer.append(date);
/*  817 */     buffer.append(' ');
/*  818 */     buffer.append(time);
/*      */     
/*  820 */     if (GMT) {
/*      */       
/*  822 */       buffer.append(' ');
/*  823 */       buffer.append("GMT");
/*      */     } 
/*      */     
/*  826 */     if (distributions != null) {
/*      */       
/*  828 */       buffer.append(" <");
/*  829 */       buffer.append(distributions);
/*  830 */       buffer.append('>');
/*      */     } 
/*      */     
/*  833 */     return sendCommand(9, buffer.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int post() throws IOException {
/*  853 */     return sendCommand(11);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int quit() throws IOException {
/*  873 */     return sendCommand(12);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int authinfoUser(String username) throws IOException {
/*  892 */     String userParameter = "USER " + username;
/*  893 */     return sendCommand(15, userParameter);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int authinfoPass(String password) throws IOException {
/*  914 */     String passParameter = "PASS " + password;
/*  915 */     return sendCommand(15, passParameter);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int xover(String selectedArticles) throws IOException {
/*  939 */     return sendCommand(16, selectedArticles);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int xhdr(String header, String selectedArticles) throws IOException {
/*  965 */     StringBuilder command = new StringBuilder(header);
/*  966 */     command.append(" ");
/*  967 */     command.append(selectedArticles);
/*  968 */     return sendCommand(17, command.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int listActive(String wildmat) throws IOException {
/*  981 */     StringBuilder command = new StringBuilder("ACTIVE ");
/*  982 */     command.append(wildmat);
/*  983 */     return sendCommand(7, command.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public int article(int a) throws IOException {
/*  991 */     return article(a);
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public int body(int a) throws IOException {
/*  997 */     return body(a);
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public int head(int a) throws IOException {
/* 1003 */     return head(a);
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public int stat(int a) throws IOException {
/* 1009 */     return stat(a);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ProtocolCommandSupport getCommandSupport() {
/* 1017 */     return this._commandSupport_;
/*      */   }
/*      */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\nntp\NNTP.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */