/*      */ package org.apache.commons.net.ftp;
/*      */ 
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.BufferedWriter;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.OutputStream;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.io.Reader;
/*      */ import java.net.InetAddress;
/*      */ import java.net.InetSocketAddress;
/*      */ import java.net.ServerSocket;
/*      */ import java.net.Socket;
/*      */ import java.net.SocketException;
/*      */ import java.net.SocketTimeoutException;
/*      */ import java.net.UnknownHostException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Locale;
/*      */ import java.util.Properties;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import org.apache.commons.net.MalformedServerReplyException;
/*      */ import org.apache.commons.net.ftp.parser.DefaultFTPFileEntryParserFactory;
/*      */ import org.apache.commons.net.ftp.parser.FTPFileEntryParserFactory;
/*      */ import org.apache.commons.net.ftp.parser.MLSxEntryParser;
/*      */ import org.apache.commons.net.io.CRLFLineReader;
/*      */ import org.apache.commons.net.io.CopyStreamAdapter;
/*      */ import org.apache.commons.net.io.CopyStreamEvent;
/*      */ import org.apache.commons.net.io.CopyStreamListener;
/*      */ import org.apache.commons.net.io.FromNetASCIIInputStream;
/*      */ import org.apache.commons.net.io.SocketInputStream;
/*      */ import org.apache.commons.net.io.SocketOutputStream;
/*      */ import org.apache.commons.net.io.ToNetASCIIOutputStream;
/*      */ import org.apache.commons.net.io.Util;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class FTPClient
/*      */   extends FTP
/*      */   implements Configurable
/*      */ {
/*      */   public static final String FTP_SYSTEM_TYPE = "org.apache.commons.net.ftp.systemType";
/*      */   public static final String FTP_SYSTEM_TYPE_DEFAULT = "org.apache.commons.net.ftp.systemType.default";
/*      */   public static final String SYSTEM_TYPE_PROPERTIES = "/systemType.properties";
/*      */   public static final int ACTIVE_LOCAL_DATA_CONNECTION_MODE = 0;
/*      */   public static final int ACTIVE_REMOTE_DATA_CONNECTION_MODE = 1;
/*      */   public static final int PASSIVE_LOCAL_DATA_CONNECTION_MODE = 2;
/*      */   public static final int PASSIVE_REMOTE_DATA_CONNECTION_MODE = 3;
/*      */   private int __dataConnectionMode;
/*      */   private int __dataTimeout;
/*      */   private int __passivePort;
/*      */   private String __passiveHost;
/*      */   private final Random __random;
/*      */   private int __activeMinPort;
/*      */   private int __activeMaxPort;
/*      */   private InetAddress __activeExternalHost;
/*      */   private InetAddress __reportActiveExternalHost;
/*      */   private InetAddress __passiveLocalHost;
/*      */   private int __fileType;
/*      */   private int __fileFormat;
/*      */   private int __fileStructure;
/*      */   private int __fileTransferMode;
/*      */   private boolean __remoteVerificationEnabled;
/*      */   private long __restartOffset;
/*      */   private FTPFileEntryParserFactory __parserFactory;
/*      */   private int __bufferSize;
/*      */   private int __sendDataSocketBufferSize;
/*      */   private int __receiveDataSocketBufferSize;
/*      */   private boolean __listHiddenFiles;
/*      */   private boolean __useEPSVwithIPv4;
/*      */   private String __systemName;
/*      */   private FTPFileEntryParser __entryParser;
/*      */   private String __entryParserKey;
/*      */   private FTPClientConfig __configuration;
/*      */   private CopyStreamListener __copyStreamListener;
/*      */   private long __controlKeepAliveTimeout;
/*  406 */   private int __controlKeepAliveReplyTimeout = 1000;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean __passiveNatWorkaround = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  417 */   private static final Pattern __PARMS_PAT = Pattern.compile("(\\d{1,3},\\d{1,3},\\d{1,3},\\d{1,3}),(\\d{1,3}),(\\d{1,3})");
/*      */ 
/*      */   
/*      */   private boolean __autodetectEncoding = false;
/*      */ 
/*      */   
/*      */   private HashMap<String, Set<String>> __featuresMap;
/*      */ 
/*      */   
/*      */   private static class PropertiesSingleton
/*      */   {
/*      */     static final Properties PROPERTIES;
/*      */ 
/*      */     
/*      */     static {
/*  432 */       InputStream resourceAsStream = FTPClient.class.getResourceAsStream("/systemType.properties");
/*  433 */       Properties p = null;
/*  434 */       if (resourceAsStream != null) {
/*  435 */         p = new Properties();
/*      */         
/*  437 */         try { p.load(resourceAsStream); }
/*  438 */         catch (IOException e)
/*      */         
/*      */         { 
/*      */           try {
/*  442 */             resourceAsStream.close();
/*  443 */           } catch (IOException iOException) {} } finally { try { resourceAsStream.close(); } catch (IOException e) {} }
/*      */       
/*      */       } 
/*      */ 
/*      */       
/*  448 */       PROPERTIES = p;
/*      */     }
/*      */   }
/*      */   
/*      */   private static Properties getOverrideProperties() {
/*  453 */     return PropertiesSingleton.PROPERTIES;
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
/*      */   public FTPClient() {
/*  476 */     __initDefaults();
/*  477 */     this.__dataTimeout = -1;
/*  478 */     this.__remoteVerificationEnabled = true;
/*  479 */     this.__parserFactory = (FTPFileEntryParserFactory)new DefaultFTPFileEntryParserFactory();
/*  480 */     this.__configuration = null;
/*  481 */     this.__listHiddenFiles = false;
/*  482 */     this.__useEPSVwithIPv4 = false;
/*  483 */     this.__random = new Random();
/*  484 */     this.__passiveLocalHost = null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void __initDefaults() {
/*  490 */     this.__dataConnectionMode = 0;
/*  491 */     this.__passiveHost = null;
/*  492 */     this.__passivePort = -1;
/*  493 */     this.__activeExternalHost = null;
/*  494 */     this.__reportActiveExternalHost = null;
/*  495 */     this.__activeMinPort = 0;
/*  496 */     this.__activeMaxPort = 0;
/*  497 */     this.__fileType = 0;
/*  498 */     this.__fileStructure = 7;
/*  499 */     this.__fileFormat = 4;
/*  500 */     this.__fileTransferMode = 10;
/*  501 */     this.__restartOffset = 0L;
/*  502 */     this.__systemName = null;
/*  503 */     this.__entryParser = null;
/*  504 */     this.__entryParserKey = "";
/*  505 */     this.__featuresMap = null;
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
/*      */   static String __parsePathname(String reply) {
/*  527 */     String param = reply.substring(4);
/*  528 */     if (param.startsWith("\"")) {
/*  529 */       StringBuilder sb = new StringBuilder();
/*  530 */       boolean quoteSeen = false;
/*      */       
/*  532 */       for (int i = 1; i < param.length(); i++) {
/*  533 */         char ch = param.charAt(i);
/*  534 */         if (ch == '"') {
/*  535 */           if (quoteSeen) {
/*  536 */             sb.append(ch);
/*  537 */             quoteSeen = false;
/*      */           } else {
/*      */             
/*  540 */             quoteSeen = true;
/*      */           } 
/*      */         } else {
/*  543 */           if (quoteSeen) {
/*  544 */             return sb.toString();
/*      */           }
/*  546 */           sb.append(ch);
/*      */         } 
/*      */       } 
/*  549 */       if (quoteSeen) {
/*  550 */         return sb.toString();
/*      */       }
/*      */     } 
/*      */     
/*  554 */     return param;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _parsePassiveModeReply(String reply) throws MalformedServerReplyException {
/*  565 */     Matcher m = __PARMS_PAT.matcher(reply);
/*  566 */     if (!m.find()) {
/*  567 */       throw new MalformedServerReplyException("Could not parse passive host information.\nServer Reply: " + reply);
/*      */     }
/*      */ 
/*      */     
/*  571 */     this.__passiveHost = m.group(1).replace(',', '.');
/*      */ 
/*      */     
/*      */     try {
/*  575 */       int oct1 = Integer.parseInt(m.group(2));
/*  576 */       int oct2 = Integer.parseInt(m.group(3));
/*  577 */       this.__passivePort = oct1 << 8 | oct2;
/*      */     }
/*  579 */     catch (NumberFormatException e) {
/*      */       
/*  581 */       throw new MalformedServerReplyException("Could not parse passive port information.\nServer Reply: " + reply);
/*      */     } 
/*      */ 
/*      */     
/*  585 */     if (this.__passiveNatWorkaround) {
/*      */       try {
/*  587 */         InetAddress host = InetAddress.getByName(this.__passiveHost);
/*      */         
/*  589 */         if (host.isSiteLocalAddress()) {
/*  590 */           InetAddress remote = getRemoteAddress();
/*  591 */           if (!remote.isSiteLocalAddress()) {
/*  592 */             String hostAddress = remote.getHostAddress();
/*  593 */             fireReplyReceived(0, "[Replacing site local address " + this.__passiveHost + " with " + hostAddress + "]\n");
/*      */             
/*  595 */             this.__passiveHost = hostAddress;
/*      */           } 
/*      */         } 
/*  598 */       } catch (UnknownHostException e) {
/*  599 */         throw new MalformedServerReplyException("Could not parse passive host information.\nServer Reply: " + reply);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _parseExtendedPassiveModeReply(String reply) throws MalformedServerReplyException {
/*      */     int port;
/*  608 */     reply = reply.substring(reply.indexOf('(') + 1, reply.indexOf(')')).trim();
/*      */ 
/*      */ 
/*      */     
/*  612 */     char delim1 = reply.charAt(0);
/*  613 */     char delim2 = reply.charAt(1);
/*  614 */     char delim3 = reply.charAt(2);
/*  615 */     char delim4 = reply.charAt(reply.length() - 1);
/*      */     
/*  617 */     if (delim1 != delim2 || delim2 != delim3 || delim3 != delim4)
/*      */     {
/*  619 */       throw new MalformedServerReplyException("Could not parse extended passive host information.\nServer Reply: " + reply);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  626 */       port = Integer.parseInt(reply.substring(3, reply.length() - 1));
/*      */     }
/*  628 */     catch (NumberFormatException e) {
/*      */       
/*  630 */       throw new MalformedServerReplyException("Could not parse extended passive host information.\nServer Reply: " + reply);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  636 */     this.__passiveHost = getRemoteAddress().getHostAddress();
/*  637 */     this.__passivePort = port;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean __storeFile(FTPCmd command, String remote, InputStream local) throws IOException {
/*  643 */     return _storeFile(command.getCommand(), remote, local);
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
/*      */   protected boolean _storeFile(String command, String remote, InputStream local) throws IOException {
/*      */     OutputStream output;
/*  657 */     Socket socket = _openDataConnection_(command, remote);
/*      */     
/*  659 */     if (socket == null) {
/*  660 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  665 */     if (this.__fileType == 0) {
/*  666 */       ToNetASCIIOutputStream toNetASCIIOutputStream = new ToNetASCIIOutputStream(getBufferedOutputStream(socket.getOutputStream()));
/*      */     } else {
/*  668 */       output = getBufferedOutputStream(socket.getOutputStream());
/*      */     } 
/*      */     
/*  671 */     CSL csl = null;
/*  672 */     if (this.__controlKeepAliveTimeout > 0L) {
/*  673 */       csl = new CSL(this, this.__controlKeepAliveTimeout, this.__controlKeepAliveReplyTimeout);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  679 */       Util.copyStream(local, output, getBufferSize(), -1L, __mergeListeners(csl), false);
/*      */ 
/*      */     
/*      */     }
/*  683 */     catch (IOException e) {
/*      */       
/*  685 */       Util.closeQuietly(socket);
/*  686 */       if (csl != null) {
/*  687 */         csl.cleanUp();
/*      */       }
/*  689 */       throw e;
/*      */     } 
/*      */     
/*  692 */     output.close();
/*  693 */     socket.close();
/*  694 */     if (csl != null) {
/*  695 */       csl.cleanUp();
/*      */     }
/*      */     
/*  698 */     boolean ok = completePendingCommand();
/*  699 */     return ok;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private OutputStream __storeFileStream(FTPCmd command, String remote) throws IOException {
/*  705 */     return _storeFileStream(command.getCommand(), remote);
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
/*      */   protected OutputStream _storeFileStream(String command, String remote) throws IOException {
/*      */     OutputStream output;
/*  718 */     Socket socket = _openDataConnection_(command, remote);
/*      */     
/*  720 */     if (socket == null) {
/*  721 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  725 */     if (this.__fileType == 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  733 */       ToNetASCIIOutputStream toNetASCIIOutputStream = new ToNetASCIIOutputStream(getBufferedOutputStream(socket.getOutputStream()));
/*      */     } else {
/*  735 */       output = socket.getOutputStream();
/*      */     } 
/*  737 */     return (OutputStream)new SocketOutputStream(socket, output);
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
/*      */   @Deprecated
/*      */   protected Socket _openDataConnection_(int command, String arg) throws IOException {
/*  764 */     return _openDataConnection_(FTPCommand.getCommand(command), arg);
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
/*      */   protected Socket _openDataConnection_(FTPCmd command, String arg) throws IOException {
/*  789 */     return _openDataConnection_(command.getCommand(), arg);
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
/*      */   protected Socket _openDataConnection_(String command, String arg) throws IOException {
/*      */     Socket socket;
/*  814 */     if (this.__dataConnectionMode != 0 && this.__dataConnectionMode != 2)
/*      */     {
/*  816 */       return null;
/*      */     }
/*      */     
/*  819 */     boolean isInet6Address = getRemoteAddress() instanceof java.net.Inet6Address;
/*      */ 
/*      */ 
/*      */     
/*  823 */     if (this.__dataConnectionMode == 0) {
/*      */ 
/*      */ 
/*      */       
/*  827 */       ServerSocket server = this._serverSocketFactory_.createServerSocket(getActivePort(), 1, getHostAddress());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/*  837 */         if (isInet6Address) {
/*  838 */           if (!FTPReply.isPositiveCompletion(eprt(getReportHostAddress(), server.getLocalPort()))) {
/*  839 */             return null;
/*      */           }
/*      */         }
/*  842 */         else if (!FTPReply.isPositiveCompletion(port(getReportHostAddress(), server.getLocalPort()))) {
/*  843 */           return null;
/*      */         } 
/*      */ 
/*      */         
/*  847 */         if (this.__restartOffset > 0L && !restart(this.__restartOffset)) {
/*  848 */           return null;
/*      */         }
/*      */         
/*  851 */         if (!FTPReply.isPositivePreliminary(sendCommand(command, arg))) {
/*  852 */           return null;
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  859 */         if (this.__dataTimeout >= 0) {
/*  860 */           server.setSoTimeout(this.__dataTimeout);
/*      */         }
/*  862 */         socket = server.accept();
/*      */ 
/*      */         
/*  865 */         if (this.__dataTimeout >= 0) {
/*  866 */           socket.setSoTimeout(this.__dataTimeout);
/*      */         }
/*  868 */         if (this.__receiveDataSocketBufferSize > 0) {
/*  869 */           socket.setReceiveBufferSize(this.__receiveDataSocketBufferSize);
/*      */         }
/*  871 */         if (this.__sendDataSocketBufferSize > 0) {
/*  872 */           socket.setSendBufferSize(this.__sendDataSocketBufferSize);
/*      */         }
/*      */       } finally {
/*  875 */         server.close();
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */ 
/*      */       
/*  888 */       boolean attemptEPSV = (isUseEPSVwithIPv4() || isInet6Address);
/*  889 */       if (attemptEPSV && epsv() == 229) {
/*      */         
/*  891 */         _parseExtendedPassiveModeReply(this._replyLines.get(0));
/*      */       }
/*      */       else {
/*      */         
/*  895 */         if (isInet6Address) {
/*  896 */           return null;
/*      */         }
/*      */         
/*  899 */         if (pasv() != 227) {
/*  900 */           return null;
/*      */         }
/*  902 */         _parsePassiveModeReply(this._replyLines.get(0));
/*      */       } 
/*      */       
/*  905 */       socket = this._socketFactory_.createSocket();
/*  906 */       if (this.__receiveDataSocketBufferSize > 0) {
/*  907 */         socket.setReceiveBufferSize(this.__receiveDataSocketBufferSize);
/*      */       }
/*  909 */       if (this.__sendDataSocketBufferSize > 0) {
/*  910 */         socket.setSendBufferSize(this.__sendDataSocketBufferSize);
/*      */       }
/*  912 */       if (this.__passiveLocalHost != null) {
/*  913 */         socket.bind(new InetSocketAddress(this.__passiveLocalHost, 0));
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  920 */       if (this.__dataTimeout >= 0) {
/*  921 */         socket.setSoTimeout(this.__dataTimeout);
/*      */       }
/*      */       
/*  924 */       socket.connect(new InetSocketAddress(this.__passiveHost, this.__passivePort), this.connectTimeout);
/*  925 */       if (this.__restartOffset > 0L && !restart(this.__restartOffset)) {
/*      */         
/*  927 */         socket.close();
/*  928 */         return null;
/*      */       } 
/*      */       
/*  931 */       if (!FTPReply.isPositivePreliminary(sendCommand(command, arg))) {
/*      */         
/*  933 */         socket.close();
/*  934 */         return null;
/*      */       } 
/*      */     } 
/*      */     
/*  938 */     if (this.__remoteVerificationEnabled && !verifyRemote(socket)) {
/*      */       
/*  940 */       socket.close();
/*      */       
/*  942 */       throw new IOException("Host attempting data connection " + socket.getInetAddress().getHostAddress() + " is not same as server " + getRemoteAddress().getHostAddress());
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  947 */     return socket;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _connectAction_() throws IOException {
/*  954 */     _connectAction_((Reader)null);
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
/*      */   protected void _connectAction_(Reader socketIsReader) throws IOException {
/*  966 */     super._connectAction_(socketIsReader);
/*  967 */     __initDefaults();
/*      */ 
/*      */     
/*  970 */     if (this.__autodetectEncoding) {
/*      */       
/*  972 */       ArrayList<String> oldReplyLines = new ArrayList<String>(this._replyLines);
/*  973 */       int oldReplyCode = this._replyCode;
/*  974 */       if (hasFeature("UTF8") || hasFeature("UTF-8")) {
/*      */         
/*  976 */         setControlEncoding("UTF-8");
/*  977 */         this._controlInput_ = (BufferedReader)new CRLFLineReader(new InputStreamReader(this._input_, getControlEncoding()));
/*      */         
/*  979 */         this._controlOutput_ = new BufferedWriter(new OutputStreamWriter(this._output_, getControlEncoding()));
/*      */       } 
/*      */ 
/*      */       
/*  983 */       this._replyLines.clear();
/*  984 */       this._replyLines.addAll(oldReplyLines);
/*  985 */       this._replyCode = oldReplyCode;
/*  986 */       this._newReplyString = true;
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
/*      */   
/*      */   public void setDataTimeout(int timeout) {
/* 1003 */     this.__dataTimeout = timeout;
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
/*      */   public void setParserFactory(FTPFileEntryParserFactory parserFactory) {
/* 1016 */     this.__parserFactory = parserFactory;
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
/*      */   public void disconnect() throws IOException {
/* 1029 */     super.disconnect();
/* 1030 */     __initDefaults();
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
/*      */   public void setRemoteVerificationEnabled(boolean enable) {
/* 1045 */     this.__remoteVerificationEnabled = enable;
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
/*      */   public boolean isRemoteVerificationEnabled() {
/* 1057 */     return this.__remoteVerificationEnabled;
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
/*      */   public boolean login(String username, String password) throws IOException {
/* 1077 */     user(username);
/*      */     
/* 1079 */     if (FTPReply.isPositiveCompletion(this._replyCode)) {
/* 1080 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1085 */     if (!FTPReply.isPositiveIntermediate(this._replyCode)) {
/* 1086 */       return false;
/*      */     }
/*      */     
/* 1089 */     return FTPReply.isPositiveCompletion(pass(password));
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
/*      */   public boolean login(String username, String password, String account) throws IOException {
/* 1113 */     user(username);
/*      */     
/* 1115 */     if (FTPReply.isPositiveCompletion(this._replyCode)) {
/* 1116 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1121 */     if (!FTPReply.isPositiveIntermediate(this._replyCode)) {
/* 1122 */       return false;
/*      */     }
/*      */     
/* 1125 */     pass(password);
/*      */     
/* 1127 */     if (FTPReply.isPositiveCompletion(this._replyCode)) {
/* 1128 */       return true;
/*      */     }
/*      */     
/* 1131 */     if (!FTPReply.isPositiveIntermediate(this._replyCode)) {
/* 1132 */       return false;
/*      */     }
/*      */     
/* 1135 */     return FTPReply.isPositiveCompletion(acct(account));
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
/*      */   public boolean logout() throws IOException {
/* 1152 */     return FTPReply.isPositiveCompletion(quit());
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
/*      */   public boolean changeWorkingDirectory(String pathname) throws IOException {
/* 1171 */     return FTPReply.isPositiveCompletion(cwd(pathname));
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
/*      */   public boolean changeToParentDirectory() throws IOException {
/* 1189 */     return FTPReply.isPositiveCompletion(cdup());
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
/*      */   public boolean structureMount(String pathname) throws IOException {
/* 1208 */     return FTPReply.isPositiveCompletion(smnt(pathname));
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
/*      */   public boolean reinitialize() throws IOException {
/* 1227 */     rein();
/*      */     
/* 1229 */     if (FTPReply.isPositiveCompletion(this._replyCode) || (FTPReply.isPositivePreliminary(this._replyCode) && FTPReply.isPositiveCompletion(getReply()))) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1234 */       __initDefaults();
/*      */       
/* 1236 */       return true;
/*      */     } 
/*      */     
/* 1239 */     return false;
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
/*      */   public void enterLocalActiveMode() {
/* 1254 */     this.__dataConnectionMode = 0;
/* 1255 */     this.__passiveHost = null;
/* 1256 */     this.__passivePort = -1;
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
/*      */   public void enterLocalPassiveMode() {
/* 1277 */     this.__dataConnectionMode = 2;
/*      */ 
/*      */     
/* 1280 */     this.__passiveHost = null;
/* 1281 */     this.__passivePort = -1;
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
/*      */   
/*      */   public boolean enterRemoteActiveMode(InetAddress host, int port) throws IOException {
/* 1312 */     if (FTPReply.isPositiveCompletion(port(host, port))) {
/*      */       
/* 1314 */       this.__dataConnectionMode = 1;
/* 1315 */       this.__passiveHost = null;
/* 1316 */       this.__passivePort = -1;
/* 1317 */       return true;
/*      */     } 
/* 1319 */     return false;
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
/*      */   public boolean enterRemotePassiveMode() throws IOException {
/* 1346 */     if (pasv() != 227) {
/* 1347 */       return false;
/*      */     }
/*      */     
/* 1350 */     this.__dataConnectionMode = 3;
/* 1351 */     _parsePassiveModeReply(this._replyLines.get(0));
/*      */     
/* 1353 */     return true;
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
/*      */   public String getPassiveHost() {
/* 1370 */     return this.__passiveHost;
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
/*      */   public int getPassivePort() {
/* 1387 */     return this.__passivePort;
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
/*      */   public int getDataConnectionMode() {
/* 1400 */     return this.__dataConnectionMode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int getActivePort() {
/* 1410 */     if (this.__activeMinPort > 0 && this.__activeMaxPort >= this.__activeMinPort) {
/*      */       
/* 1412 */       if (this.__activeMaxPort == this.__activeMinPort) {
/* 1413 */         return this.__activeMaxPort;
/*      */       }
/*      */       
/* 1416 */       return this.__random.nextInt(this.__activeMaxPort - this.__activeMinPort + 1) + this.__activeMinPort;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1421 */     return 0;
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
/*      */   private InetAddress getHostAddress() {
/* 1433 */     if (this.__activeExternalHost != null)
/*      */     {
/* 1435 */       return this.__activeExternalHost;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1440 */     return getLocalAddress();
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
/*      */   private InetAddress getReportHostAddress() {
/* 1453 */     if (this.__reportActiveExternalHost != null) {
/* 1454 */       return this.__reportActiveExternalHost;
/*      */     }
/* 1456 */     return getHostAddress();
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
/*      */   public void setActivePortRange(int minPort, int maxPort) {
/* 1469 */     this.__activeMinPort = minPort;
/* 1470 */     this.__activeMaxPort = maxPort;
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
/*      */   public void setActiveExternalIPAddress(String ipAddress) throws UnknownHostException {
/* 1483 */     this.__activeExternalHost = InetAddress.getByName(ipAddress);
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
/*      */   public void setPassiveLocalIPAddress(String ipAddress) throws UnknownHostException {
/* 1495 */     this.__passiveLocalHost = InetAddress.getByName(ipAddress);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPassiveLocalIPAddress(InetAddress inetAddress) {
/* 1506 */     this.__passiveLocalHost = inetAddress;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public InetAddress getPassiveLocalIPAddress() {
/* 1517 */     return this.__passiveLocalHost;
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
/*      */   public void setReportActiveExternalIPAddress(String ipAddress) throws UnknownHostException {
/* 1531 */     this.__reportActiveExternalHost = InetAddress.getByName(ipAddress);
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
/*      */ 
/*      */   
/*      */   public boolean setFileType(int fileType) throws IOException {
/* 1563 */     if (FTPReply.isPositiveCompletion(type(fileType))) {
/*      */       
/* 1565 */       this.__fileType = fileType;
/* 1566 */       this.__fileFormat = 4;
/* 1567 */       return true;
/*      */     } 
/* 1569 */     return false;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setFileType(int fileType, int formatOrByteSize) throws IOException {
/* 1612 */     if (FTPReply.isPositiveCompletion(type(fileType, formatOrByteSize))) {
/*      */       
/* 1614 */       this.__fileType = fileType;
/* 1615 */       this.__fileFormat = formatOrByteSize;
/* 1616 */       return true;
/*      */     } 
/* 1618 */     return false;
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
/*      */   public boolean setFileStructure(int structure) throws IOException {
/* 1640 */     if (FTPReply.isPositiveCompletion(stru(structure))) {
/*      */       
/* 1642 */       this.__fileStructure = structure;
/* 1643 */       return true;
/*      */     } 
/* 1645 */     return false;
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
/*      */   public boolean setFileTransferMode(int mode) throws IOException {
/* 1667 */     if (FTPReply.isPositiveCompletion(mode(mode))) {
/*      */       
/* 1669 */       this.__fileTransferMode = mode;
/* 1670 */       return true;
/*      */     } 
/* 1672 */     return false;
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
/*      */   public boolean remoteRetrieve(String filename) throws IOException {
/* 1693 */     if (this.__dataConnectionMode == 1 || this.__dataConnectionMode == 3)
/*      */     {
/* 1695 */       return FTPReply.isPositivePreliminary(retr(filename));
/*      */     }
/* 1697 */     return false;
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
/*      */   public boolean remoteStore(String filename) throws IOException {
/* 1720 */     if (this.__dataConnectionMode == 1 || this.__dataConnectionMode == 3)
/*      */     {
/* 1722 */       return FTPReply.isPositivePreliminary(stor(filename));
/*      */     }
/* 1724 */     return false;
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
/*      */   public boolean remoteStoreUnique(String filename) throws IOException {
/* 1748 */     if (this.__dataConnectionMode == 1 || this.__dataConnectionMode == 3)
/*      */     {
/* 1750 */       return FTPReply.isPositivePreliminary(stou(filename));
/*      */     }
/* 1752 */     return false;
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
/*      */   public boolean remoteStoreUnique() throws IOException {
/* 1776 */     if (this.__dataConnectionMode == 1 || this.__dataConnectionMode == 3)
/*      */     {
/* 1778 */       return FTPReply.isPositivePreliminary(stou());
/*      */     }
/* 1780 */     return false;
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
/*      */   public boolean remoteAppend(String filename) throws IOException {
/* 1804 */     if (this.__dataConnectionMode == 1 || this.__dataConnectionMode == 3)
/*      */     {
/* 1806 */       return FTPReply.isPositivePreliminary(appe(filename));
/*      */     }
/* 1808 */     return false;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean completePendingCommand() throws IOException {
/* 1857 */     return FTPReply.isPositiveCompletion(getReply());
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
/*      */ 
/*      */   
/*      */   public boolean retrieveFile(String remote, OutputStream local) throws IOException {
/* 1889 */     return _retrieveFile(FTPCmd.RETR.getCommand(), remote, local);
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
/*      */   protected boolean _retrieveFile(String command, String remote, OutputStream local) throws IOException {
/*      */     InputStream input;
/* 1903 */     Socket socket = _openDataConnection_(command, remote);
/*      */     
/* 1905 */     if (socket == null) {
/* 1906 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 1910 */     if (this.__fileType == 0) {
/* 1911 */       FromNetASCIIInputStream fromNetASCIIInputStream = new FromNetASCIIInputStream(getBufferedInputStream(socket.getInputStream()));
/*      */     } else {
/* 1913 */       input = getBufferedInputStream(socket.getInputStream());
/*      */     } 
/*      */     
/* 1916 */     CSL csl = null;
/* 1917 */     if (this.__controlKeepAliveTimeout > 0L) {
/* 1918 */       csl = new CSL(this, this.__controlKeepAliveTimeout, this.__controlKeepAliveReplyTimeout);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1924 */       Util.copyStream(input, local, getBufferSize(), -1L, __mergeListeners(csl), false);
/*      */     }
/*      */     finally {
/*      */       
/* 1928 */       Util.closeQuietly(input);
/* 1929 */       Util.closeQuietly(socket);
/* 1930 */       if (csl != null) {
/* 1931 */         csl.cleanUp();
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1936 */     boolean ok = completePendingCommand();
/* 1937 */     return ok;
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public InputStream retrieveFileStream(String remote) throws IOException {
/* 1971 */     return _retrieveFileStream(FTPCmd.RETR.getCommand(), remote);
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
/*      */   protected InputStream _retrieveFileStream(String command, String remote) throws IOException {
/*      */     InputStream input;
/* 1984 */     Socket socket = _openDataConnection_(command, remote);
/*      */     
/* 1986 */     if (socket == null) {
/* 1987 */       return null;
/*      */     }
/*      */ 
/*      */     
/* 1991 */     if (this.__fileType == 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1999 */       FromNetASCIIInputStream fromNetASCIIInputStream = new FromNetASCIIInputStream(getBufferedInputStream(socket.getInputStream()));
/*      */     } else {
/* 2001 */       input = socket.getInputStream();
/*      */     } 
/* 2003 */     return (InputStream)new SocketInputStream(socket, input);
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
/*      */   
/*      */   public boolean storeFile(String remote, InputStream local) throws IOException {
/* 2034 */     return __storeFile(FTPCmd.STOR, remote, local);
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public OutputStream storeFileStream(String remote) throws IOException {
/* 2068 */     return __storeFileStream(FTPCmd.STOR, remote);
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
/*      */   
/*      */   public boolean appendFile(String remote, InputStream local) throws IOException {
/* 2099 */     return __storeFile(FTPCmd.APPE, remote, local);
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
/*      */ 
/*      */ 
/*      */   
/*      */   public OutputStream appendFileStream(String remote) throws IOException {
/* 2132 */     return __storeFileStream(FTPCmd.APPE, remote);
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
/*      */ 
/*      */   
/*      */   public boolean storeUniqueFile(String remote, InputStream local) throws IOException {
/* 2164 */     return __storeFile(FTPCmd.STOU, remote, local);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public OutputStream storeUniqueFileStream(String remote) throws IOException {
/* 2200 */     return __storeFileStream(FTPCmd.STOU, remote);
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
/*      */   public boolean storeUniqueFile(InputStream local) throws IOException {
/* 2229 */     return __storeFile(FTPCmd.STOU, (String)null, local);
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
/*      */ 
/*      */ 
/*      */   
/*      */   public OutputStream storeUniqueFileStream() throws IOException {
/* 2262 */     return __storeFileStream(FTPCmd.STOU, (String)null);
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
/*      */   public boolean allocate(int bytes) throws IOException {
/* 2280 */     return FTPReply.isPositiveCompletion(allo(bytes));
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
/*      */   public boolean features() throws IOException {
/* 2301 */     return FTPReply.isPositiveCompletion(feat());
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
/*      */   public String[] featureValues(String feature) throws IOException {
/* 2316 */     if (!initFeatureMap()) {
/* 2317 */       return null;
/*      */     }
/* 2319 */     Set<String> entries = this.__featuresMap.get(feature.toUpperCase(Locale.ENGLISH));
/* 2320 */     if (entries != null) {
/* 2321 */       return entries.<String>toArray(new String[entries.size()]);
/*      */     }
/* 2323 */     return null;
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
/*      */   public String featureValue(String feature) throws IOException {
/* 2339 */     String[] values = featureValues(feature);
/* 2340 */     if (values != null) {
/* 2341 */       return values[0];
/*      */     }
/* 2343 */     return null;
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
/*      */   public boolean hasFeature(String feature) throws IOException {
/* 2359 */     if (!initFeatureMap()) {
/* 2360 */       return false;
/*      */     }
/* 2362 */     return this.__featuresMap.containsKey(feature.toUpperCase(Locale.ENGLISH));
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
/*      */   public boolean hasFeature(String feature, String value) throws IOException {
/* 2381 */     if (!initFeatureMap()) {
/* 2382 */       return false;
/*      */     }
/* 2384 */     Set<String> entries = this.__featuresMap.get(feature.toUpperCase(Locale.ENGLISH));
/* 2385 */     if (entries != null) {
/* 2386 */       return entries.contains(value);
/*      */     }
/* 2388 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean initFeatureMap() throws IOException {
/* 2395 */     if (this.__featuresMap == null) {
/*      */       
/* 2397 */       int replyCode = feat();
/* 2398 */       if (replyCode == 530) {
/* 2399 */         return false;
/*      */       }
/* 2401 */       boolean success = FTPReply.isPositiveCompletion(replyCode);
/*      */       
/* 2403 */       this.__featuresMap = new HashMap<String, Set<String>>();
/* 2404 */       if (!success) {
/* 2405 */         return false;
/*      */       }
/* 2407 */       for (String l : getReplyStrings()) {
/* 2408 */         if (l.startsWith(" ")) {
/*      */           
/* 2410 */           String value = "";
/* 2411 */           int varsep = l.indexOf(' ', 1);
/* 2412 */           if (varsep > 0) {
/* 2413 */             key = l.substring(1, varsep);
/* 2414 */             value = l.substring(varsep + 1);
/*      */           } else {
/* 2416 */             key = l.substring(1);
/*      */           } 
/* 2418 */           String key = key.toUpperCase(Locale.ENGLISH);
/* 2419 */           Set<String> entries = this.__featuresMap.get(key);
/* 2420 */           if (entries == null) {
/* 2421 */             entries = new HashSet<String>();
/* 2422 */             this.__featuresMap.put(key, entries);
/*      */           } 
/* 2424 */           entries.add(value);
/*      */         } 
/*      */       } 
/*      */     } 
/* 2428 */     return true;
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
/*      */   public boolean allocate(int bytes, int recordSize) throws IOException {
/* 2447 */     return FTPReply.isPositiveCompletion(allo(bytes, recordSize));
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
/*      */   public boolean doCommand(String command, String params) throws IOException {
/* 2469 */     return FTPReply.isPositiveCompletion(sendCommand(command, params));
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
/*      */   public String[] doCommandAsStrings(String command, String params) throws IOException {
/* 2490 */     boolean success = FTPReply.isPositiveCompletion(sendCommand(command, params));
/* 2491 */     if (success) {
/* 2492 */       return getReplyStrings();
/*      */     }
/* 2494 */     return null;
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
/*      */   public FTPFile mlistFile(String pathname) throws IOException {
/* 2508 */     boolean success = FTPReply.isPositiveCompletion(sendCommand(FTPCmd.MLST, pathname));
/* 2509 */     if (success) {
/* 2510 */       String entry = getReplyStrings()[1].substring(1);
/* 2511 */       return MLSxEntryParser.parseEntry(entry);
/*      */     } 
/* 2513 */     return null;
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
/*      */   public FTPFile[] mlistDir() throws IOException {
/* 2526 */     return mlistDir((String)null);
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
/*      */   public FTPFile[] mlistDir(String pathname) throws IOException {
/* 2539 */     FTPListParseEngine engine = initiateMListParsing(pathname);
/* 2540 */     return engine.getFiles();
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
/*      */   public FTPFile[] mlistDir(String pathname, FTPFileFilter filter) throws IOException {
/* 2554 */     FTPListParseEngine engine = initiateMListParsing(pathname);
/* 2555 */     return engine.getFiles(filter);
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
/*      */   protected boolean restart(long offset) throws IOException {
/* 2579 */     this.__restartOffset = 0L;
/* 2580 */     return FTPReply.isPositiveIntermediate(rest(Long.toString(offset)));
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
/*      */   public void setRestartOffset(long offset) {
/* 2601 */     if (offset >= 0L) {
/* 2602 */       this.__restartOffset = offset;
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
/*      */   public long getRestartOffset() {
/* 2614 */     return this.__restartOffset;
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
/*      */   public boolean rename(String from, String to) throws IOException {
/* 2635 */     if (!FTPReply.isPositiveIntermediate(rnfr(from))) {
/* 2636 */       return false;
/*      */     }
/*      */     
/* 2639 */     return FTPReply.isPositiveCompletion(rnto(to));
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
/*      */   public boolean abort() throws IOException {
/* 2657 */     return FTPReply.isPositiveCompletion(abor());
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
/*      */   public boolean deleteFile(String pathname) throws IOException {
/* 2675 */     return FTPReply.isPositiveCompletion(dele(pathname));
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
/*      */   public boolean removeDirectory(String pathname) throws IOException {
/* 2694 */     return FTPReply.isPositiveCompletion(rmd(pathname));
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
/*      */   public boolean makeDirectory(String pathname) throws IOException {
/* 2715 */     return FTPReply.isPositiveCompletion(mkd(pathname));
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
/*      */   public String printWorkingDirectory() throws IOException {
/* 2734 */     if (pwd() != 257) {
/* 2735 */       return null;
/*      */     }
/*      */     
/* 2738 */     return __parsePathname(this._replyLines.get(this._replyLines.size() - 1));
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
/*      */   public boolean sendSiteCommand(String arguments) throws IOException {
/* 2756 */     return FTPReply.isPositiveCompletion(site(arguments));
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
/*      */   
/*      */   public String getSystemType() throws IOException {
/* 2787 */     if (this.__systemName == null) {
/* 2788 */       if (FTPReply.isPositiveCompletion(syst())) {
/*      */         
/* 2790 */         this.__systemName = ((String)this._replyLines.get(this._replyLines.size() - 1)).substring(4);
/*      */       } else {
/*      */         
/* 2793 */         String systDefault = System.getProperty("org.apache.commons.net.ftp.systemType.default");
/* 2794 */         if (systDefault != null) {
/* 2795 */           this.__systemName = systDefault;
/*      */         } else {
/* 2797 */           throw new IOException("Unable to determine system type - response: " + getReplyString());
/*      */         } 
/*      */       } 
/*      */     }
/* 2801 */     return this.__systemName;
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
/*      */   public String listHelp() throws IOException {
/* 2821 */     if (FTPReply.isPositiveCompletion(help())) {
/* 2822 */       return getReplyString();
/*      */     }
/* 2824 */     return null;
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
/*      */   public String listHelp(String command) throws IOException {
/* 2844 */     if (FTPReply.isPositiveCompletion(help(command))) {
/* 2845 */       return getReplyString();
/*      */     }
/* 2847 */     return null;
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
/*      */   public boolean sendNoOp() throws IOException {
/* 2866 */     return FTPReply.isPositiveCompletion(noop());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] listNames(String pathname) throws IOException {
/* 2901 */     Socket socket = _openDataConnection_(FTPCmd.NLST, getListArguments(pathname));
/*      */     
/* 2903 */     if (socket == null) {
/* 2904 */       return null;
/*      */     }
/*      */     
/* 2907 */     BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), getControlEncoding()));
/*      */ 
/*      */     
/* 2910 */     ArrayList<String> results = new ArrayList<String>();
/*      */     String line;
/* 2912 */     while ((line = reader.readLine()) != null) {
/* 2913 */       results.add(line);
/*      */     }
/*      */     
/* 2916 */     reader.close();
/* 2917 */     socket.close();
/*      */     
/* 2919 */     if (completePendingCommand()) {
/*      */       
/* 2921 */       String[] names = new String[results.size()];
/* 2922 */       return results.<String>toArray(names);
/*      */     } 
/*      */     
/* 2925 */     return null;
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
/*      */   public String[] listNames() throws IOException {
/* 2952 */     return listNames((String)null);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FTPFile[] listFiles(String pathname) throws IOException {
/* 3012 */     FTPListParseEngine engine = initiateListParsing((String)null, pathname);
/* 3013 */     return engine.getFiles();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FTPFile[] listFiles() throws IOException {
/* 3065 */     return listFiles((String)null);
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
/*      */   public FTPFile[] listFiles(String pathname, FTPFileFilter filter) throws IOException {
/* 3080 */     FTPListParseEngine engine = initiateListParsing((String)null, pathname);
/* 3081 */     return engine.getFiles(filter);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FTPFile[] listDirectories() throws IOException {
/* 3129 */     return listDirectories((String)null);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FTPFile[] listDirectories(String parent) throws IOException {
/* 3177 */     return listFiles(parent, FTPFileFilters.DIRECTORIES);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FTPListParseEngine initiateListParsing() throws IOException {
/* 3216 */     return initiateListParsing((String)null);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FTPListParseEngine initiateListParsing(String pathname) throws IOException {
/* 3272 */     return initiateListParsing((String)null, pathname);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FTPListParseEngine initiateListParsing(String parserKey, String pathname) throws IOException {
/* 3334 */     __createParser(parserKey);
/* 3335 */     return initiateListParsing(this.__entryParser, pathname);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void __createParser(String parserKey) throws IOException {
/* 3343 */     if (this.__entryParser == null || (parserKey != null && !this.__entryParserKey.equals(parserKey))) {
/* 3344 */       if (null != parserKey) {
/*      */ 
/*      */         
/* 3347 */         this.__entryParser = this.__parserFactory.createFileEntryParser(parserKey);
/*      */         
/* 3349 */         this.__entryParserKey = parserKey;
/*      */ 
/*      */ 
/*      */       
/*      */       }
/* 3354 */       else if (null != this.__configuration && this.__configuration.getServerSystemKey().length() > 0) {
/* 3355 */         this.__entryParser = this.__parserFactory.createFileEntryParser(this.__configuration);
/*      */         
/* 3357 */         this.__entryParserKey = this.__configuration.getServerSystemKey();
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */         
/* 3363 */         String systemType = System.getProperty("org.apache.commons.net.ftp.systemType");
/* 3364 */         if (systemType == null) {
/* 3365 */           systemType = getSystemType();
/* 3366 */           Properties override = getOverrideProperties();
/* 3367 */           if (override != null) {
/* 3368 */             String newType = override.getProperty(systemType);
/* 3369 */             if (newType != null) {
/* 3370 */               systemType = newType;
/*      */             }
/*      */           } 
/*      */         } 
/* 3374 */         if (null != this.__configuration) {
/* 3375 */           this.__entryParser = this.__parserFactory.createFileEntryParser(new FTPClientConfig(systemType, this.__configuration));
/*      */         } else {
/* 3377 */           this.__entryParser = this.__parserFactory.createFileEntryParser(systemType);
/*      */         } 
/* 3379 */         this.__entryParserKey = systemType;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private FTPListParseEngine initiateListParsing(FTPFileEntryParser parser, String pathname) throws IOException {
/* 3405 */     Socket socket = _openDataConnection_(FTPCmd.LIST, getListArguments(pathname));
/*      */     
/* 3407 */     FTPListParseEngine engine = new FTPListParseEngine(parser, this.__configuration);
/* 3408 */     if (socket == null)
/*      */     {
/* 3410 */       return engine;
/*      */     }
/*      */     
/*      */     try {
/* 3414 */       engine.readServerList(socket.getInputStream(), getControlEncoding());
/*      */     } finally {
/*      */       
/* 3417 */       Util.closeQuietly(socket);
/*      */     } 
/*      */     
/* 3420 */     completePendingCommand();
/* 3421 */     return engine;
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
/*      */   private FTPListParseEngine initiateMListParsing(String pathname) throws IOException {
/* 3433 */     Socket socket = _openDataConnection_(FTPCmd.MLSD, pathname);
/* 3434 */     FTPListParseEngine engine = new FTPListParseEngine((FTPFileEntryParser)MLSxEntryParser.getInstance(), this.__configuration);
/* 3435 */     if (socket == null)
/*      */     {
/* 3437 */       return engine;
/*      */     }
/*      */     
/*      */     try {
/* 3441 */       engine.readServerList(socket.getInputStream(), getControlEncoding());
/*      */     } finally {
/*      */       
/* 3444 */       Util.closeQuietly(socket);
/* 3445 */       completePendingCommand();
/*      */     } 
/* 3447 */     return engine;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getListArguments(String pathname) {
/* 3456 */     if (getListHiddenFiles()) {
/*      */       
/* 3458 */       if (pathname != null) {
/*      */         
/* 3460 */         StringBuilder sb = new StringBuilder(pathname.length() + 3);
/* 3461 */         sb.append("-a ");
/* 3462 */         sb.append(pathname);
/* 3463 */         return sb.toString();
/*      */       } 
/*      */ 
/*      */       
/* 3467 */       return "-a";
/*      */     } 
/*      */ 
/*      */     
/* 3471 */     return pathname;
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
/*      */   public String getStatus() throws IOException {
/* 3489 */     if (FTPReply.isPositiveCompletion(stat())) {
/* 3490 */       return getReplyString();
/*      */     }
/* 3492 */     return null;
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
/*      */   public String getStatus(String pathname) throws IOException {
/* 3512 */     if (FTPReply.isPositiveCompletion(stat(pathname))) {
/* 3513 */       return getReplyString();
/*      */     }
/* 3515 */     return null;
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
/*      */   public String getModificationTime(String pathname) throws IOException {
/* 3531 */     if (FTPReply.isPositiveCompletion(mdtm(pathname))) {
/* 3532 */       return getReplyStrings()[0].substring(4);
/*      */     }
/* 3534 */     return null;
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
/*      */   public FTPFile mdtmFile(String pathname) throws IOException {
/* 3551 */     if (FTPReply.isPositiveCompletion(mdtm(pathname))) {
/* 3552 */       String reply = getReplyStrings()[0].substring(4);
/* 3553 */       FTPFile file = new FTPFile();
/* 3554 */       file.setName(pathname);
/* 3555 */       file.setRawListing(reply);
/* 3556 */       file.setTimestamp(MLSxEntryParser.parseGMTdateTime(reply));
/* 3557 */       return file;
/*      */     } 
/* 3559 */     return null;
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
/*      */   public boolean setModificationTime(String pathname, String timeval) throws IOException {
/* 3581 */     return FTPReply.isPositiveCompletion(mfmt(pathname, timeval));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBufferSize(int bufSize) {
/* 3591 */     this.__bufferSize = bufSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getBufferSize() {
/* 3599 */     return this.__bufferSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSendDataSocketBufferSize(int bufSize) {
/* 3610 */     this.__sendDataSocketBufferSize = bufSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getSendDataSocketBufferSize() {
/* 3619 */     return this.__sendDataSocketBufferSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setReceieveDataSocketBufferSize(int bufSize) {
/* 3630 */     this.__receiveDataSocketBufferSize = bufSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getReceiveDataSocketBufferSize() {
/* 3639 */     return this.__receiveDataSocketBufferSize;
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
/*      */   public void configure(FTPClientConfig config) {
/* 3652 */     this.__configuration = config;
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
/*      */   public void setListHiddenFiles(boolean listHiddenFiles) {
/* 3665 */     this.__listHiddenFiles = listHiddenFiles;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getListHiddenFiles() {
/* 3674 */     return this.__listHiddenFiles;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isUseEPSVwithIPv4() {
/* 3684 */     return this.__useEPSVwithIPv4;
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
/*      */   public void setUseEPSVwithIPv4(boolean selected) {
/* 3703 */     this.__useEPSVwithIPv4 = selected;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCopyStreamListener(CopyStreamListener listener) {
/* 3714 */     this.__copyStreamListener = listener;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CopyStreamListener getCopyStreamListener() {
/* 3724 */     return this.__copyStreamListener;
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
/*      */   public void setControlKeepAliveTimeout(long controlIdle) {
/* 3736 */     this.__controlKeepAliveTimeout = controlIdle * 1000L;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getControlKeepAliveTimeout() {
/* 3745 */     return this.__controlKeepAliveTimeout / 1000L;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setControlKeepAliveReplyTimeout(int timeout) {
/* 3756 */     this.__controlKeepAliveReplyTimeout = timeout;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getControlKeepAliveReplyTimeout() {
/* 3765 */     return this.__controlKeepAliveReplyTimeout;
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
/*      */   public void setPassiveNatWorkaround(boolean enabled) {
/* 3781 */     this.__passiveNatWorkaround = enabled;
/*      */   }
/*      */   
/*      */   private OutputStream getBufferedOutputStream(OutputStream outputStream) {
/* 3785 */     if (this.__bufferSize > 0) {
/* 3786 */       return new BufferedOutputStream(outputStream, this.__bufferSize);
/*      */     }
/* 3788 */     return new BufferedOutputStream(outputStream);
/*      */   }
/*      */   
/*      */   private InputStream getBufferedInputStream(InputStream inputStream) {
/* 3792 */     if (this.__bufferSize > 0) {
/* 3793 */       return new BufferedInputStream(inputStream, this.__bufferSize);
/*      */     }
/* 3795 */     return new BufferedInputStream(inputStream);
/*      */   }
/*      */ 
/*      */   
/*      */   private static class CSL
/*      */     implements CopyStreamListener
/*      */   {
/*      */     private final FTPClient parent;
/*      */     private final long idle;
/*      */     private final int currentSoTimeout;
/* 3805 */     private long time = System.currentTimeMillis();
/*      */     private int notAcked;
/*      */     
/*      */     CSL(FTPClient parent, long idleTime, int maxWait) throws SocketException {
/* 3809 */       this.idle = idleTime;
/* 3810 */       this.parent = parent;
/* 3811 */       this.currentSoTimeout = parent.getSoTimeout();
/* 3812 */       parent.setSoTimeout(maxWait);
/*      */     }
/*      */ 
/*      */     
/*      */     public void bytesTransferred(CopyStreamEvent event) {
/* 3817 */       bytesTransferred(event.getTotalBytesTransferred(), event.getBytesTransferred(), event.getStreamSize());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void bytesTransferred(long totalBytesTransferred, int bytesTransferred, long streamSize) {
/* 3823 */       long now = System.currentTimeMillis();
/* 3824 */       if (now - this.time > this.idle) {
/*      */         try {
/* 3826 */           this.parent.__noop();
/* 3827 */         } catch (SocketTimeoutException e) {
/* 3828 */           this.notAcked++;
/* 3829 */         } catch (IOException e) {}
/*      */ 
/*      */         
/* 3832 */         this.time = now;
/*      */       } 
/*      */     }
/*      */     
/*      */     void cleanUp() throws IOException {
/*      */       try {
/* 3838 */         while (this.notAcked-- > 0) {
/* 3839 */           this.parent.__getReplyNoReport();
/*      */         }
/*      */       } finally {
/* 3842 */         this.parent.setSoTimeout(this.currentSoTimeout);
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
/*      */   private CopyStreamListener __mergeListeners(CopyStreamListener local) {
/* 3856 */     if (local == null) {
/* 3857 */       return this.__copyStreamListener;
/*      */     }
/* 3859 */     if (this.__copyStreamListener == null) {
/* 3860 */       return local;
/*      */     }
/*      */     
/* 3863 */     CopyStreamAdapter merged = new CopyStreamAdapter();
/* 3864 */     merged.addCopyStreamListener(local);
/* 3865 */     merged.addCopyStreamListener(this.__copyStreamListener);
/* 3866 */     return (CopyStreamListener)merged;
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
/*      */   public void setAutodetectUTF8(boolean autodetect) {
/* 3878 */     this.__autodetectEncoding = autodetect;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getAutodetectUTF8() {
/* 3887 */     return this.__autodetectEncoding;
/*      */   }
/*      */ 
/*      */   
/*      */   FTPFileEntryParser getEntryParser() {
/* 3892 */     return this.__entryParser;
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
/*      */   @Deprecated
/*      */   public String getSystemName() throws IOException {
/* 3905 */     if (this.__systemName == null && FTPReply.isPositiveCompletion(syst())) {
/* 3906 */       this.__systemName = ((String)this._replyLines.get(this._replyLines.size() - 1)).substring(4);
/*      */     }
/* 3908 */     return this.__systemName;
/*      */   }
/*      */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\ftp\FTPClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */