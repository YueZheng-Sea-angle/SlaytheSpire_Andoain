/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ @Plugin(name = "SerializedLayout", category = "Core", elementType = "layout", printObject = true)
/*     */ public final class SerializedLayout
/*     */   extends AbstractLayout<LogEvent>
/*     */ {
/*     */   private static byte[] serializedHeader;
/*     */   
/*     */   static {
/*  44 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*     */     try {
/*  46 */       (new ObjectOutputStream(baos)).close();
/*  47 */       serializedHeader = baos.toByteArray();
/*  48 */     } catch (Exception ex) {
/*  49 */       LOGGER.error("Unable to generate Object stream header", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private SerializedLayout() {
/*  54 */     super(null, null, null);
/*  55 */     LOGGER.warn("SerializedLayout is deprecated due to the inherent security weakness in Java Serialization, see https://www.owasp.org/index.php/Deserialization_of_untrusted_data Consider using another layout, e.g. JsonLayout");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] toByteArray(LogEvent event) {
/*  66 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*  67 */     try (ObjectOutputStream oos = new PrivateObjectOutputStream(baos)) {
/*  68 */       oos.writeObject(event);
/*  69 */       oos.reset();
/*  70 */     } catch (IOException ioe) {
/*  71 */       LOGGER.error("Serialization of LogEvent failed.", ioe);
/*     */     } 
/*  73 */     return baos.toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LogEvent toSerializable(LogEvent event) {
/*  84 */     return event;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @PluginFactory
/*     */   public static SerializedLayout createLayout() {
/*  94 */     return new SerializedLayout();
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getHeader() {
/*  99 */     return serializedHeader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getContentType() {
/* 108 */     return "application/octet-stream";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class PrivateObjectOutputStream
/*     */     extends ObjectOutputStream
/*     */   {
/*     */     public PrivateObjectOutputStream(OutputStream os) throws IOException {
/* 117 */       super(os);
/*     */     }
/*     */     
/*     */     protected void writeStreamHeader() {}
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\layout\SerializedLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */