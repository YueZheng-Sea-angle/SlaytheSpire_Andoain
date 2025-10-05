/*    */ package org.apache.logging.log4j.core.jackson;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*    */ import com.fasterxml.jackson.databind.JsonSerializer;
/*    */ import com.fasterxml.jackson.databind.Module;
/*    */ import com.fasterxml.jackson.databind.module.SimpleModule;
/*    */ import org.apache.logging.log4j.Level;
/*    */ import org.apache.logging.log4j.Marker;
/*    */ import org.apache.logging.log4j.ThreadContext;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.impl.ExtendedStackTraceElement;
/*    */ import org.apache.logging.log4j.core.impl.ThrowableProxy;
/*    */ import org.apache.logging.log4j.core.time.Instant;
/*    */ import org.apache.logging.log4j.message.Message;
/*    */ import org.apache.logging.log4j.message.ObjectMessage;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class Initializers
/*    */ {
/*    */   static class SetupContextInitializer
/*    */   {
/*    */     void setupModule(Module.SetupContext context, boolean includeStacktrace, boolean stacktraceAsString) {
/* 47 */       context.setMixInAnnotations(StackTraceElement.class, StackTraceElementMixIn.class);
/*    */       
/* 49 */       context.setMixInAnnotations(Marker.class, MarkerMixIn.class);
/* 50 */       context.setMixInAnnotations(Level.class, LevelMixIn.class);
/* 51 */       context.setMixInAnnotations(Instant.class, InstantMixIn.class);
/* 52 */       context.setMixInAnnotations(LogEvent.class, LogEventWithContextListMixIn.class);
/*    */       
/* 54 */       context.setMixInAnnotations(ExtendedStackTraceElement.class, ExtendedStackTraceElementMixIn.class);
/* 55 */       context.setMixInAnnotations(ThrowableProxy.class, includeStacktrace ? (stacktraceAsString ? ThrowableProxyWithStacktraceAsStringMixIn.class : ThrowableProxyMixIn.class) : ThrowableProxyWithoutStacktraceMixIn.class);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static class SetupContextJsonInitializer
/*    */   {
/*    */     void setupModule(Module.SetupContext context, boolean includeStacktrace, boolean stacktraceAsString) {
/* 69 */       context.setMixInAnnotations(StackTraceElement.class, StackTraceElementMixIn.class);
/*    */       
/* 71 */       context.setMixInAnnotations(Marker.class, MarkerMixIn.class);
/* 72 */       context.setMixInAnnotations(Level.class, LevelMixIn.class);
/* 73 */       context.setMixInAnnotations(Instant.class, InstantMixIn.class);
/* 74 */       context.setMixInAnnotations(LogEvent.class, LogEventJsonMixIn.class);
/*    */       
/* 76 */       context.setMixInAnnotations(ExtendedStackTraceElement.class, ExtendedStackTraceElementMixIn.class);
/* 77 */       context.setMixInAnnotations(ThrowableProxy.class, includeStacktrace ? (stacktraceAsString ? ThrowableProxyWithStacktraceAsStringMixIn.class : ThrowableProxyMixIn.class) : ThrowableProxyWithoutStacktraceMixIn.class);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static class SimpleModuleInitializer
/*    */   {
/*    */     void initialize(SimpleModule simpleModule, boolean objectMessageAsJsonObject) {
/* 89 */       simpleModule.addDeserializer(StackTraceElement.class, (JsonDeserializer)new Log4jStackTraceElementDeserializer());
/* 90 */       simpleModule.addDeserializer(ThreadContext.ContextStack.class, (JsonDeserializer)new MutableThreadContextStackDeserializer());
/* 91 */       if (objectMessageAsJsonObject) {
/* 92 */         simpleModule.addSerializer(ObjectMessage.class, (JsonSerializer)new ObjectMessageSerializer());
/*    */       }
/* 94 */       simpleModule.addSerializer(Message.class, (JsonSerializer)new MessageSerializer());
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\jackson\Initializers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */