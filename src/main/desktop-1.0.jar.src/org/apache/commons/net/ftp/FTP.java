/*      */ package org.apache.commons.net.ftp;
/*      */ 
/*      */ import java.io.BufferedReader;
/*      */ import java.io.BufferedWriter;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.io.Reader;
/*      */ import java.net.InetAddress;
/*      */ import java.net.SocketException;
/*      */ import java.net.SocketTimeoutException;
/*      */ import java.util.ArrayList;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class FTP
/*      */   extends SocketClient
/*      */ {
/*      */   public static final int DEFAULT_DATA_PORT = 20;
/*      */   public static final int DEFAULT_PORT = 21;
/*      */   public static final int ASCII_FILE_TYPE = 0;
/*      */   public static final int EBCDIC_FILE_TYPE = 1;
/*      */   public static final int BINARY_FILE_TYPE = 2;
/*      */   public static final int LOCAL_FILE_TYPE = 3;
/*      */   public static final int NON_PRINT_TEXT_FORMAT = 4;
/*      */   public static final int TELNET_TEXT_FORMAT = 5;
/*      */   public static final int CARRIAGE_CONTROL_TEXT_FORMAT = 6;
/*      */   public static final int FILE_STRUCTURE = 7;
/*      */   public static final int RECORD_STRUCTURE = 8;
/*      */   public static final int PAGE_STRUCTURE = 9;
/*      */   public static final int STREAM_TRANSFER_MODE = 10;
/*      */   public static final int BLOCK_TRANSFER_MODE = 11;
/*      */   public static final int COMPRESSED_TRANSFER_MODE = 12;
/*      */   public static final String DEFAULT_CONTROL_ENCODING = "ISO-8859-1";
/*      */   public static final int REPLY_CODE_LEN = 3;
/*      */   private static final String __modes = "AEILNTCFRPSBC";
/*      */   protected int _replyCode;
/*      */   protected ArrayList<String> _replyLines;
/*      */   protected boolean _newReplyString;
/*      */   protected String _replyString;
/*      */   protected String _controlEncoding;
/*      */   protected ProtocolCommandSupport _commandSupport_;
/*      */   protected boolean strictMultilineParsing = false;
/*      */   protected BufferedReader _controlInput_;
/*      */   protected BufferedWriter _controlOutput_;
/*      */   
/*      */   public FTP() {
/*  264 */     setDefaultPort(21);
/*  265 */     this._replyLines = new ArrayList<String>();
/*  266 */     this._newReplyString = false;
/*  267 */     this._replyString = null;
/*  268 */     this._controlEncoding = "ISO-8859-1";
/*  269 */     this._commandSupport_ = new ProtocolCommandSupport(this);
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean __strictCheck(String line, String code) {
/*  274 */     return (!line.startsWith(code) || line.charAt(3) != ' ');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean __lenientCheck(String line) {
/*  283 */     return (line.length() <= 3 || line.charAt(3) == '-' || !Character.isDigit(line.charAt(0)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void __getReply() throws IOException {
/*  292 */     __getReply(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void __getReplyNoReport() throws IOException {
/*  303 */     __getReply(false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void __getReply(boolean reportReply) throws IOException {
/*  310 */     this._newReplyString = true;
/*  311 */     this._replyLines.clear();
/*      */     
/*  313 */     String line = this._controlInput_.readLine();
/*      */     
/*  315 */     if (line == null) {
/*  316 */       throw new FTPConnectionClosedException("Connection closed without indication.");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  322 */     int length = line.length();
/*  323 */     if (length < 3) {
/*  324 */       throw new MalformedServerReplyException("Truncated server reply: " + line);
/*      */     }
/*      */ 
/*      */     
/*  328 */     String code = null;
/*      */     
/*      */     try {
/*  331 */       code = line.substring(0, 3);
/*  332 */       this._replyCode = Integer.parseInt(code);
/*      */     }
/*  334 */     catch (NumberFormatException e) {
/*      */       
/*  336 */       throw new MalformedServerReplyException("Could not parse response code.\nServer Reply: " + line);
/*      */     } 
/*      */ 
/*      */     
/*  340 */     this._replyLines.add(line);
/*      */ 
/*      */     
/*  343 */     if (length > 3 && line.charAt(3) == '-')
/*      */     {
/*      */       while (true) {
/*      */         
/*  347 */         line = this._controlInput_.readLine();
/*      */         
/*  349 */         if (line == null) {
/*  350 */           throw new FTPConnectionClosedException("Connection closed without indication.");
/*      */         }
/*      */ 
/*      */         
/*  354 */         this._replyLines.add(line);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  360 */         if (isStrictMultilineParsing() ? __strictCheck(line, code) : __lenientCheck(line))
/*      */           continue;  break;
/*      */       }  } 
/*  363 */     if (reportReply) {
/*  364 */       fireReplyReceived(this._replyCode, getReplyString());
/*      */     }
/*      */     
/*  367 */     if (this._replyCode == 421) {
/*  368 */       throw new FTPConnectionClosedException("FTP response 421 received.  Server closed connection.");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _connectAction_() throws IOException {
/*  379 */     _connectAction_((Reader)null);
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
/*      */   protected void _connectAction_(Reader socketIsReader) throws IOException {
/*  392 */     super._connectAction_();
/*  393 */     if (socketIsReader == null) {
/*  394 */       this._controlInput_ = (BufferedReader)new CRLFLineReader(new InputStreamReader(this._input_, getControlEncoding()));
/*      */     } else {
/*      */       
/*  397 */       this._controlInput_ = (BufferedReader)new CRLFLineReader(socketIsReader);
/*      */     } 
/*  399 */     this._controlOutput_ = new BufferedWriter(new OutputStreamWriter(this._output_, getControlEncoding()));
/*      */     
/*  401 */     if (this.connectTimeout > 0) {
/*  402 */       int original = this._socket_.getSoTimeout();
/*  403 */       this._socket_.setSoTimeout(this.connectTimeout);
/*      */       try {
/*  405 */         __getReply();
/*      */         
/*  407 */         if (FTPReply.isPositivePreliminary(this._replyCode)) {
/*  408 */           __getReply();
/*      */         }
/*  410 */       } catch (SocketTimeoutException e) {
/*  411 */         IOException ioe = new IOException("Timed out waiting for initial connect reply");
/*  412 */         ioe.initCause(e);
/*  413 */         throw ioe;
/*      */       } finally {
/*  415 */         this._socket_.setSoTimeout(original);
/*      */       } 
/*      */     } else {
/*  418 */       __getReply();
/*      */       
/*  420 */       if (FTPReply.isPositivePreliminary(this._replyCode)) {
/*  421 */         __getReply();
/*      */       }
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
/*      */ 
/*      */   
/*      */   public void setControlEncoding(String encoding) {
/*  438 */     this._controlEncoding = encoding;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getControlEncoding() {
/*  447 */     return this._controlEncoding;
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
/*      */   public void disconnect() throws IOException {
/*  463 */     super.disconnect();
/*  464 */     this._controlInput_ = null;
/*  465 */     this._controlOutput_ = null;
/*  466 */     this._newReplyString = false;
/*  467 */     this._replyString = null;
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
/*      */   public int sendCommand(String command, String args) throws IOException {
/*  493 */     if (this._controlOutput_ == null) {
/*  494 */       throw new IOException("Connection is not open");
/*      */     }
/*      */     
/*  497 */     String message = __buildMessage(command, args);
/*      */     
/*  499 */     __send(message);
/*      */     
/*  501 */     fireCommandSent(command, message);
/*      */     
/*  503 */     __getReply();
/*  504 */     return this._replyCode;
/*      */   }
/*      */   
/*      */   private String __buildMessage(String command, String args) {
/*  508 */     StringBuilder __commandBuffer = new StringBuilder();
/*      */     
/*  510 */     __commandBuffer.append(command);
/*      */     
/*  512 */     if (args != null) {
/*      */       
/*  514 */       __commandBuffer.append(' ');
/*  515 */       __commandBuffer.append(args);
/*      */     } 
/*  517 */     __commandBuffer.append("\r\n");
/*  518 */     return __commandBuffer.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private void __send(String message) throws IOException, FTPConnectionClosedException, SocketException {
/*      */     try {
/*  524 */       this._controlOutput_.write(message);
/*  525 */       this._controlOutput_.flush();
/*      */     }
/*  527 */     catch (SocketException e) {
/*      */       
/*  529 */       if (!isConnected())
/*      */       {
/*  531 */         throw new FTPConnectionClosedException("Connection unexpectedly closed.");
/*      */       }
/*      */ 
/*      */       
/*  535 */       throw e;
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
/*      */   protected void __noop() throws IOException {
/*  548 */     String msg = __buildMessage(FTPCmd.NOOP.getCommand(), (String)null);
/*  549 */     __send(msg);
/*  550 */     __getReplyNoReport();
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
/*      */   @Deprecated
/*      */   public int sendCommand(int command, String args) throws IOException {
/*  578 */     return sendCommand(FTPCommand.getCommand(command), args);
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
/*      */   public int sendCommand(FTPCmd command) throws IOException {
/*  602 */     return sendCommand(command, (String)null);
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
/*      */   public int sendCommand(FTPCmd command, String args) throws IOException {
/*  628 */     return sendCommand(command.getCommand(), args);
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
/*      */   public int sendCommand(String command) throws IOException {
/*  651 */     return sendCommand(command, (String)null);
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
/*  676 */     return sendCommand(command, (String)null);
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
/*  690 */     return this._replyCode;
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
/*  712 */     __getReply();
/*  713 */     return this._replyCode;
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
/*      */   public String[] getReplyStrings() {
/*  726 */     return this._replyLines.<String>toArray(new String[this._replyLines.size()]);
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
/*      */   public String getReplyString() {
/*  740 */     if (!this._newReplyString) {
/*  741 */       return this._replyString;
/*      */     }
/*      */     
/*  744 */     StringBuilder buffer = new StringBuilder(256);
/*      */     
/*  746 */     for (String line : this._replyLines) {
/*  747 */       buffer.append(line);
/*  748 */       buffer.append("\r\n");
/*      */     } 
/*      */     
/*  751 */     this._newReplyString = false;
/*      */     
/*  753 */     return this._replyString = buffer.toString();
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
/*      */   public int user(String username) throws IOException {
/*  773 */     return sendCommand(FTPCmd.USER, username);
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
/*      */   public int pass(String password) throws IOException {
/*  791 */     return sendCommand(FTPCmd.PASS, password);
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
/*      */   public int acct(String account) throws IOException {
/*  810 */     return sendCommand(FTPCmd.ACCT, account);
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
/*      */   public int abor() throws IOException {
/*  829 */     return sendCommand(FTPCmd.ABOR);
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
/*      */   public int cwd(String directory) throws IOException {
/*  848 */     return sendCommand(FTPCmd.CWD, directory);
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
/*      */   public int cdup() throws IOException {
/*  866 */     return sendCommand(FTPCmd.CDUP);
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
/*      */   public int quit() throws IOException {
/*  884 */     return sendCommand(FTPCmd.QUIT);
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
/*      */   public int rein() throws IOException {
/*  902 */     return sendCommand(FTPCmd.REIN);
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
/*      */   public int smnt(String dir) throws IOException {
/*  921 */     return sendCommand(FTPCmd.SMNT, dir);
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
/*      */   public int port(InetAddress host, int port) throws IOException {
/*  942 */     StringBuilder info = new StringBuilder(24);
/*      */     
/*  944 */     info.append(host.getHostAddress().replace('.', ','));
/*  945 */     int num = port >>> 8;
/*  946 */     info.append(',');
/*  947 */     info.append(num);
/*  948 */     info.append(',');
/*  949 */     num = port & 0xFF;
/*  950 */     info.append(num);
/*      */     
/*  952 */     return sendCommand(FTPCmd.PORT, info.toString());
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
/*      */   
/*      */   public int eprt(InetAddress host, int port) throws IOException {
/*  982 */     StringBuilder info = new StringBuilder();
/*      */ 
/*      */ 
/*      */     
/*  986 */     String h = host.getHostAddress();
/*  987 */     int num = h.indexOf("%");
/*  988 */     if (num > 0) {
/*  989 */       h = h.substring(0, num);
/*      */     }
/*      */     
/*  992 */     info.append("|");
/*      */     
/*  994 */     if (host instanceof java.net.Inet4Address) {
/*  995 */       info.append("1");
/*  996 */     } else if (host instanceof java.net.Inet6Address) {
/*  997 */       info.append("2");
/*      */     } 
/*  999 */     info.append("|");
/* 1000 */     info.append(h);
/* 1001 */     info.append("|");
/* 1002 */     info.append(port);
/* 1003 */     info.append("|");
/*      */     
/* 1005 */     return sendCommand(FTPCmd.EPRT, info.toString());
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
/*      */   public int pasv() throws IOException {
/* 1025 */     return sendCommand(FTPCmd.PASV);
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
/*      */   public int epsv() throws IOException {
/* 1046 */     return sendCommand(FTPCmd.EPSV);
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
/*      */   public int type(int fileType, int formatOrByteSize) throws IOException {
/* 1068 */     StringBuilder arg = new StringBuilder();
/*      */     
/* 1070 */     arg.append("AEILNTCFRPSBC".charAt(fileType));
/* 1071 */     arg.append(' ');
/* 1072 */     if (fileType == 3) {
/* 1073 */       arg.append(formatOrByteSize);
/*      */     } else {
/* 1075 */       arg.append("AEILNTCFRPSBC".charAt(formatOrByteSize));
/*      */     } 
/*      */     
/* 1078 */     return sendCommand(FTPCmd.TYPE, arg.toString());
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
/*      */   public int type(int fileType) throws IOException {
/* 1099 */     return sendCommand(FTPCmd.TYPE, "AEILNTCFRPSBC".substring(fileType, fileType + 1));
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
/*      */   public int stru(int structure) throws IOException {
/* 1120 */     return sendCommand(FTPCmd.STRU, "AEILNTCFRPSBC".substring(structure, structure + 1));
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
/*      */   public int mode(int mode) throws IOException {
/* 1141 */     return sendCommand(FTPCmd.MODE, "AEILNTCFRPSBC".substring(mode, mode + 1));
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
/*      */   public int retr(String pathname) throws IOException {
/* 1164 */     return sendCommand(FTPCmd.RETR, pathname);
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
/*      */   public int stor(String pathname) throws IOException {
/* 1187 */     return sendCommand(FTPCmd.STOR, pathname);
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
/*      */   public int stou() throws IOException {
/* 1208 */     return sendCommand(FTPCmd.STOU);
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
/*      */   public int stou(String pathname) throws IOException {
/* 1231 */     return sendCommand(FTPCmd.STOU, pathname);
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
/*      */   public int appe(String pathname) throws IOException {
/* 1254 */     return sendCommand(FTPCmd.APPE, pathname);
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
/*      */   public int allo(int bytes) throws IOException {
/* 1273 */     return sendCommand(FTPCmd.ALLO, Integer.toString(bytes));
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
/*      */   public int feat() throws IOException {
/* 1286 */     return sendCommand(FTPCmd.FEAT);
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
/*      */   public int allo(int bytes, int recordSize) throws IOException {
/* 1306 */     return sendCommand(FTPCmd.ALLO, Integer.toString(bytes) + " R " + Integer.toString(recordSize));
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
/*      */   public int rest(String marker) throws IOException {
/* 1326 */     return sendCommand(FTPCmd.REST, marker);
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
/*      */   public int mdtm(String file) throws IOException {
/* 1338 */     return sendCommand(FTPCmd.MDTM, file);
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
/*      */   public int mfmt(String pathname, String timeval) throws IOException {
/* 1361 */     return sendCommand(FTPCmd.MFMT, timeval + " " + pathname);
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
/*      */   public int rnfr(String pathname) throws IOException {
/* 1381 */     return sendCommand(FTPCmd.RNFR, pathname);
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
/*      */   public int rnto(String pathname) throws IOException {
/* 1400 */     return sendCommand(FTPCmd.RNTO, pathname);
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
/*      */   public int dele(String pathname) throws IOException {
/* 1419 */     return sendCommand(FTPCmd.DELE, pathname);
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
/*      */   public int rmd(String pathname) throws IOException {
/* 1438 */     return sendCommand(FTPCmd.RMD, pathname);
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
/*      */   public int mkd(String pathname) throws IOException {
/* 1457 */     return sendCommand(FTPCmd.MKD, pathname);
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
/*      */   public int pwd() throws IOException {
/* 1475 */     return sendCommand(FTPCmd.PWD);
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
/*      */   public int list() throws IOException {
/* 1496 */     return sendCommand(FTPCmd.LIST);
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
/*      */   public int list(String pathname) throws IOException {
/* 1519 */     return sendCommand(FTPCmd.LIST, pathname);
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
/*      */   public int mlsd() throws IOException {
/* 1541 */     return sendCommand(FTPCmd.MLSD);
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
/*      */   public int mlsd(String path) throws IOException {
/* 1565 */     return sendCommand(FTPCmd.MLSD, path);
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
/*      */   public int mlst() throws IOException {
/* 1587 */     return sendCommand(FTPCmd.MLST);
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
/*      */   public int mlst(String path) throws IOException {
/* 1611 */     return sendCommand(FTPCmd.MLST, path);
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
/*      */   public int nlst() throws IOException {
/* 1632 */     return sendCommand(FTPCmd.NLST);
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
/*      */   public int nlst(String pathname) throws IOException {
/* 1655 */     return sendCommand(FTPCmd.NLST, pathname);
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
/*      */   public int site(String parameters) throws IOException {
/* 1674 */     return sendCommand(FTPCmd.SITE, parameters);
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
/*      */   public int syst() throws IOException {
/* 1692 */     return sendCommand(FTPCmd.SYST);
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
/* 1710 */     return sendCommand(FTPCmd.STAT);
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
/*      */   public int stat(String pathname) throws IOException {
/* 1729 */     return sendCommand(FTPCmd.STAT, pathname);
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
/*      */   public int help() throws IOException {
/* 1747 */     return sendCommand(FTPCmd.HELP);
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
/*      */   public int help(String command) throws IOException {
/* 1766 */     return sendCommand(FTPCmd.HELP, command);
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
/*      */   public int noop() throws IOException {
/* 1784 */     return sendCommand(FTPCmd.NOOP);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isStrictMultilineParsing() {
/* 1793 */     return this.strictMultilineParsing;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setStrictMultilineParsing(boolean strictMultilineParsing) {
/* 1802 */     this.strictMultilineParsing = strictMultilineParsing;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ProtocolCommandSupport getCommandSupport() {
/* 1810 */     return this._commandSupport_;
/*      */   }
/*      */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\ftp\FTP.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */