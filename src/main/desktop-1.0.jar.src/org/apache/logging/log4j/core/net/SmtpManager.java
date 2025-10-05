/*     */ package org.apache.logging.log4j.core.net;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Date;
/*     */ import java.util.Properties;
/*     */ import javax.activation.DataSource;
/*     */ import javax.mail.Authenticator;
/*     */ import javax.mail.BodyPart;
/*     */ import javax.mail.Message;
/*     */ import javax.mail.MessagingException;
/*     */ import javax.mail.Multipart;
/*     */ import javax.mail.PasswordAuthentication;
/*     */ import javax.mail.Session;
/*     */ import javax.mail.Transport;
/*     */ import javax.mail.internet.InternetHeaders;
/*     */ import javax.mail.internet.MimeBodyPart;
/*     */ import javax.mail.internet.MimeMessage;
/*     */ import javax.mail.internet.MimeMultipart;
/*     */ import javax.mail.internet.MimeUtility;
/*     */ import javax.mail.util.ByteArrayDataSource;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import org.apache.logging.log4j.LoggingException;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.AbstractManager;
/*     */ import org.apache.logging.log4j.core.appender.ManagerFactory;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.layout.AbstractStringLayout;
/*     */ import org.apache.logging.log4j.core.layout.PatternLayout;
/*     */ import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
/*     */ import org.apache.logging.log4j.core.util.CyclicBuffer;
/*     */ import org.apache.logging.log4j.core.util.NetUtils;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
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
/*     */ public class SmtpManager
/*     */   extends AbstractManager
/*     */ {
/*  58 */   private static final SMTPManagerFactory FACTORY = new SMTPManagerFactory();
/*     */   
/*     */   private final Session session;
/*     */   
/*     */   private final CyclicBuffer<LogEvent> buffer;
/*     */   
/*     */   private volatile MimeMessage message;
/*     */   
/*     */   private final FactoryData data;
/*     */ 
/*     */   
/*     */   private static MimeMessage createMimeMessage(FactoryData data, Session session, LogEvent appendEvent) throws MessagingException {
/*  70 */     return (new MimeMessageBuilder(session)).setFrom(data.from).setReplyTo(data.replyto)
/*  71 */       .setRecipients(Message.RecipientType.TO, data.to).setRecipients(Message.RecipientType.CC, data.cc)
/*  72 */       .setRecipients(Message.RecipientType.BCC, data.bcc).setSubject(data.subject.toSerializable(appendEvent))
/*  73 */       .build();
/*     */   }
/*     */ 
/*     */   
/*     */   protected SmtpManager(String name, Session session, MimeMessage message, FactoryData data) {
/*  78 */     super(null, name);
/*  79 */     this.session = session;
/*  80 */     this.message = message;
/*  81 */     this.data = data;
/*  82 */     this.buffer = new CyclicBuffer(LogEvent.class, data.numElements);
/*     */   }
/*     */   
/*     */   public void add(LogEvent event) {
/*  86 */     this.buffer.add(event.toImmutable());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SmtpManager getSmtpManager(Configuration config, String to, String cc, String bcc, String from, String replyTo, String subject, String protocol, String host, int port, String username, String password, boolean isDebug, String filterName, int numElements, SslConfiguration sslConfiguration) {
/*  97 */     if (Strings.isEmpty(protocol)) {
/*  98 */       protocol = "smtp";
/*     */     }
/*     */     
/* 101 */     String name = createManagerName(to, cc, bcc, from, replyTo, subject, protocol, host, port, username, isDebug, filterName);
/* 102 */     AbstractStringLayout.Serializer subjectSerializer = PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(subject).build();
/*     */     
/* 104 */     return (SmtpManager)getManager(name, FACTORY, new FactoryData(to, cc, bcc, from, replyTo, subjectSerializer, protocol, host, port, username, password, isDebug, numElements, sslConfiguration));
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
/*     */   static String createManagerName(String to, String cc, String bcc, String from, String replyTo, String subject, String protocol, String host, int port, String username, boolean isDebug, String filterName) {
/* 129 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 131 */     if (to != null) {
/* 132 */       sb.append(to);
/*     */     }
/* 134 */     sb.append(':');
/* 135 */     if (cc != null) {
/* 136 */       sb.append(cc);
/*     */     }
/* 138 */     sb.append(':');
/* 139 */     if (bcc != null) {
/* 140 */       sb.append(bcc);
/*     */     }
/* 142 */     sb.append(':');
/* 143 */     if (from != null) {
/* 144 */       sb.append(from);
/*     */     }
/* 146 */     sb.append(':');
/* 147 */     if (replyTo != null) {
/* 148 */       sb.append(replyTo);
/*     */     }
/* 150 */     sb.append(':');
/* 151 */     if (subject != null) {
/* 152 */       sb.append(subject);
/*     */     }
/* 154 */     sb.append(':');
/* 155 */     sb.append(protocol).append(':').append(host).append(':').append(port).append(':');
/* 156 */     if (username != null) {
/* 157 */       sb.append(username);
/*     */     }
/* 159 */     sb.append(isDebug ? ":debug:" : "::");
/* 160 */     sb.append(filterName);
/*     */     
/* 162 */     return "SMTP:" + sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendEvents(Layout<?> layout, LogEvent appendEvent) {
/* 171 */     if (this.message == null) {
/* 172 */       connect(appendEvent);
/*     */     }
/*     */     try {
/* 175 */       LogEvent[] priorEvents = removeAllBufferedEvents();
/*     */ 
/*     */       
/* 178 */       byte[] rawBytes = formatContentToBytes(priorEvents, appendEvent, layout);
/*     */       
/* 180 */       String contentType = layout.getContentType();
/* 181 */       String encoding = getEncoding(rawBytes, contentType);
/* 182 */       byte[] encodedBytes = encodeContentToBytes(rawBytes, encoding);
/*     */       
/* 184 */       InternetHeaders headers = getHeaders(contentType, encoding);
/* 185 */       MimeMultipart mp = getMimeMultipart(encodedBytes, headers);
/*     */       
/* 187 */       String subject = this.data.subject.toSerializable(appendEvent);
/*     */       
/* 189 */       sendMultipartMessage(this.message, mp, subject);
/* 190 */     } catch (MessagingException|IOException|RuntimeException e) {
/* 191 */       logError("Caught exception while sending e-mail notification.", e);
/* 192 */       throw new LoggingException("Error occurred while sending email", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   LogEvent[] removeAllBufferedEvents() {
/* 197 */     return (LogEvent[])this.buffer.removeAll();
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte[] formatContentToBytes(LogEvent[] priorEvents, LogEvent appendEvent, Layout<?> layout) throws IOException {
/* 202 */     ByteArrayOutputStream raw = new ByteArrayOutputStream();
/* 203 */     writeContent(priorEvents, appendEvent, layout, raw);
/* 204 */     return raw.toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeContent(LogEvent[] priorEvents, LogEvent appendEvent, Layout<?> layout, ByteArrayOutputStream out) throws IOException {
/* 210 */     writeHeader(layout, out);
/* 211 */     writeBuffer(priorEvents, appendEvent, layout, out);
/* 212 */     writeFooter(layout, out);
/*     */   }
/*     */   
/*     */   protected void writeHeader(Layout<?> layout, OutputStream out) throws IOException {
/* 216 */     byte[] header = layout.getHeader();
/* 217 */     if (header != null) {
/* 218 */       out.write(header);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeBuffer(LogEvent[] priorEvents, LogEvent appendEvent, Layout<?> layout, OutputStream out) throws IOException {
/* 224 */     for (LogEvent priorEvent : priorEvents) {
/* 225 */       byte[] arrayOfByte = layout.toByteArray(priorEvent);
/* 226 */       out.write(arrayOfByte);
/*     */     } 
/*     */     
/* 229 */     byte[] bytes = layout.toByteArray(appendEvent);
/* 230 */     out.write(bytes);
/*     */   }
/*     */   
/*     */   protected void writeFooter(Layout<?> layout, OutputStream out) throws IOException {
/* 234 */     byte[] footer = layout.getFooter();
/* 235 */     if (footer != null) {
/* 236 */       out.write(footer);
/*     */     }
/*     */   }
/*     */   
/*     */   protected String getEncoding(byte[] rawBytes, String contentType) {
/* 241 */     ByteArrayDataSource byteArrayDataSource = new ByteArrayDataSource(rawBytes, contentType);
/* 242 */     return MimeUtility.getEncoding((DataSource)byteArrayDataSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte[] encodeContentToBytes(byte[] rawBytes, String encoding) throws MessagingException, IOException {
/* 247 */     ByteArrayOutputStream encoded = new ByteArrayOutputStream();
/* 248 */     encodeContent(rawBytes, encoding, encoded);
/* 249 */     return encoded.toByteArray();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void encodeContent(byte[] bytes, String encoding, ByteArrayOutputStream out) throws MessagingException, IOException {
/* 254 */     try (OutputStream encoder = MimeUtility.encode(out, encoding)) {
/* 255 */       encoder.write(bytes);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected InternetHeaders getHeaders(String contentType, String encoding) {
/* 260 */     InternetHeaders headers = new InternetHeaders();
/* 261 */     headers.setHeader("Content-Type", contentType + "; charset=UTF-8");
/* 262 */     headers.setHeader("Content-Transfer-Encoding", encoding);
/* 263 */     return headers;
/*     */   }
/*     */ 
/*     */   
/*     */   protected MimeMultipart getMimeMultipart(byte[] encodedBytes, InternetHeaders headers) throws MessagingException {
/* 268 */     MimeMultipart mp = new MimeMultipart();
/* 269 */     MimeBodyPart part = new MimeBodyPart(headers, encodedBytes);
/* 270 */     mp.addBodyPart((BodyPart)part);
/* 271 */     return mp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected void sendMultipartMessage(MimeMessage msg, MimeMultipart mp) throws MessagingException {
/* 279 */     synchronized (msg) {
/* 280 */       msg.setContent((Multipart)mp);
/* 281 */       msg.setSentDate(new Date());
/* 282 */       Transport.send((Message)msg);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void sendMultipartMessage(MimeMessage msg, MimeMultipart mp, String subject) throws MessagingException {
/* 287 */     synchronized (msg) {
/* 288 */       msg.setContent((Multipart)mp);
/* 289 */       msg.setSentDate(new Date());
/* 290 */       msg.setSubject(subject);
/* 291 */       Transport.send((Message)msg);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static class FactoryData
/*     */   {
/*     */     private final String to;
/*     */     
/*     */     private final String cc;
/*     */     
/*     */     private final String bcc;
/*     */     
/*     */     private final String from;
/*     */     
/*     */     private final String replyto;
/*     */     private final AbstractStringLayout.Serializer subject;
/*     */     private final String protocol;
/*     */     private final String host;
/*     */     private final int port;
/*     */     private final String username;
/*     */     private final String password;
/*     */     private final boolean isDebug;
/*     */     private final int numElements;
/*     */     private final SslConfiguration sslConfiguration;
/*     */     
/*     */     public FactoryData(String to, String cc, String bcc, String from, String replyTo, AbstractStringLayout.Serializer subjectSerializer, String protocol, String host, int port, String username, String password, boolean isDebug, int numElements, SslConfiguration sslConfiguration) {
/* 318 */       this.to = to;
/* 319 */       this.cc = cc;
/* 320 */       this.bcc = bcc;
/* 321 */       this.from = from;
/* 322 */       this.replyto = replyTo;
/* 323 */       this.subject = subjectSerializer;
/* 324 */       this.protocol = protocol;
/* 325 */       this.host = host;
/* 326 */       this.port = port;
/* 327 */       this.username = username;
/* 328 */       this.password = password;
/* 329 */       this.isDebug = isDebug;
/* 330 */       this.numElements = numElements;
/* 331 */       this.sslConfiguration = sslConfiguration;
/*     */     }
/*     */   }
/*     */   
/*     */   private synchronized void connect(LogEvent appendEvent) {
/* 336 */     if (this.message != null) {
/*     */       return;
/*     */     }
/*     */     try {
/* 340 */       this.message = createMimeMessage(this.data, this.session, appendEvent);
/* 341 */     } catch (MessagingException e) {
/* 342 */       logError("Could not set SmtpAppender message options", (Throwable)e);
/* 343 */       this.message = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static class SMTPManagerFactory
/*     */     implements ManagerFactory<SmtpManager, FactoryData>
/*     */   {
/*     */     private SMTPManagerFactory() {}
/*     */     
/*     */     public SmtpManager createManager(String name, SmtpManager.FactoryData data) {
/* 354 */       String prefix = "mail." + data.protocol;
/*     */       
/* 356 */       Properties properties = PropertiesUtil.getSystemProperties();
/* 357 */       properties.setProperty("mail.transport.protocol", data.protocol);
/* 358 */       if (properties.getProperty("mail.host") == null)
/*     */       {
/* 360 */         properties.setProperty("mail.host", NetUtils.getLocalHostname());
/*     */       }
/*     */       
/* 363 */       if (null != data.host) {
/* 364 */         properties.setProperty(prefix + ".host", data.host);
/*     */       }
/* 366 */       if (data.port > 0) {
/* 367 */         properties.setProperty(prefix + ".port", String.valueOf(data.port));
/*     */       }
/*     */       
/* 370 */       Authenticator authenticator = buildAuthenticator(data.username, data.password);
/* 371 */       if (null != authenticator) {
/* 372 */         properties.setProperty(prefix + ".auth", "true");
/*     */       }
/*     */       
/* 375 */       if (data.protocol.equals("smtps")) {
/* 376 */         SslConfiguration sslConfiguration = data.sslConfiguration;
/* 377 */         if (sslConfiguration != null) {
/* 378 */           SSLSocketFactory sslSocketFactory = sslConfiguration.getSslSocketFactory();
/* 379 */           properties.put(prefix + ".ssl.socketFactory", sslSocketFactory);
/* 380 */           properties.setProperty(prefix + ".ssl.checkserveridentity", Boolean.toString(sslConfiguration.isVerifyHostName()));
/*     */         } 
/*     */       } 
/*     */       
/* 384 */       Session session = Session.getInstance(properties, authenticator);
/* 385 */       session.setProtocolForAddress("rfc822", data.protocol);
/* 386 */       session.setDebug(data.isDebug);
/* 387 */       return new SmtpManager(name, session, null, data);
/*     */     }
/*     */     
/*     */     private Authenticator buildAuthenticator(final String username, final String password) {
/* 391 */       if (null != password && null != username) {
/* 392 */         return new Authenticator() {
/* 393 */             private final PasswordAuthentication passwordAuthentication = new PasswordAuthentication(username, password);
/*     */ 
/*     */ 
/*     */             
/*     */             protected PasswordAuthentication getPasswordAuthentication() {
/* 398 */               return this.passwordAuthentication;
/*     */             }
/*     */           };
/*     */       }
/* 402 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\net\SmtpManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */