/*    */ package org.apache.logging.log4j.core.net;
/*    */ 
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import javax.mail.Address;
/*    */ import javax.mail.Message;
/*    */ import javax.mail.MessagingException;
/*    */ import javax.mail.Session;
/*    */ import javax.mail.internet.AddressException;
/*    */ import javax.mail.internet.InternetAddress;
/*    */ import javax.mail.internet.MimeMessage;
/*    */ import org.apache.logging.log4j.core.util.Builder;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MimeMessageBuilder
/*    */   implements Builder<MimeMessage>
/*    */ {
/*    */   private final MimeMessage message;
/*    */   
/*    */   public MimeMessageBuilder(Session session) {
/* 37 */     this.message = new MimeMessage(session);
/*    */   }
/*    */   
/*    */   public MimeMessageBuilder setFrom(String from) throws MessagingException {
/* 41 */     InternetAddress address = parseAddress(from);
/*    */     
/* 43 */     if (null != address) {
/* 44 */       this.message.setFrom((Address)address);
/*    */     } else {
/*    */       try {
/* 47 */         this.message.setFrom();
/* 48 */       } catch (Exception ex) {
/* 49 */         this.message.setFrom((Address)null);
/*    */       } 
/*    */     } 
/* 52 */     return this;
/*    */   }
/*    */   
/*    */   public MimeMessageBuilder setReplyTo(String replyTo) throws MessagingException {
/* 56 */     InternetAddress[] addresses = parseAddresses(replyTo);
/*    */     
/* 58 */     if (null != addresses) {
/* 59 */       this.message.setReplyTo((Address[])addresses);
/*    */     }
/* 61 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public MimeMessageBuilder setRecipients(Message.RecipientType recipientType, String recipients) throws MessagingException {
/* 66 */     InternetAddress[] addresses = parseAddresses(recipients);
/*    */     
/* 68 */     if (null != addresses) {
/* 69 */       this.message.setRecipients(recipientType, (Address[])addresses);
/*    */     }
/* 71 */     return this;
/*    */   }
/*    */   
/*    */   public MimeMessageBuilder setSubject(String subject) throws MessagingException {
/* 75 */     if (subject != null) {
/* 76 */       this.message.setSubject(subject, StandardCharsets.UTF_8.name());
/*    */     }
/* 78 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public MimeMessage getMimeMessage() {
/* 86 */     return build();
/*    */   }
/*    */ 
/*    */   
/*    */   public MimeMessage build() {
/* 91 */     return this.message;
/*    */   }
/*    */   
/*    */   private static InternetAddress parseAddress(String address) throws AddressException {
/* 95 */     return (address == null) ? null : new InternetAddress(address);
/*    */   }
/*    */   
/*    */   private static InternetAddress[] parseAddresses(String addresses) throws AddressException {
/* 99 */     return (addresses == null) ? null : InternetAddress.parse(addresses, true);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\net\MimeMessageBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */