/*      */ package org.apache.logging.log4j.spi;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Field;
/*      */ import org.apache.logging.log4j.Level;
/*      */ import org.apache.logging.log4j.LogBuilder;
/*      */ import org.apache.logging.log4j.LoggingException;
/*      */ import org.apache.logging.log4j.Marker;
/*      */ import org.apache.logging.log4j.MarkerManager;
/*      */ import org.apache.logging.log4j.internal.DefaultLogBuilder;
/*      */ import org.apache.logging.log4j.message.DefaultFlowMessageFactory;
/*      */ import org.apache.logging.log4j.message.EntryMessage;
/*      */ import org.apache.logging.log4j.message.FlowMessageFactory;
/*      */ import org.apache.logging.log4j.message.Message;
/*      */ import org.apache.logging.log4j.message.MessageFactory;
/*      */ import org.apache.logging.log4j.message.MessageFactory2;
/*      */ import org.apache.logging.log4j.message.ParameterizedMessage;
/*      */ import org.apache.logging.log4j.message.ParameterizedMessageFactory;
/*      */ import org.apache.logging.log4j.message.ReusableMessageFactory;
/*      */ import org.apache.logging.log4j.message.SimpleMessage;
/*      */ import org.apache.logging.log4j.message.StringFormattedMessage;
/*      */ import org.apache.logging.log4j.status.StatusLogger;
/*      */ import org.apache.logging.log4j.util.Constants;
/*      */ import org.apache.logging.log4j.util.LambdaUtil;
/*      */ import org.apache.logging.log4j.util.LoaderUtil;
/*      */ import org.apache.logging.log4j.util.MessageSupplier;
/*      */ import org.apache.logging.log4j.util.PerformanceSensitive;
/*      */ import org.apache.logging.log4j.util.PropertiesUtil;
/*      */ import org.apache.logging.log4j.util.StackLocatorUtil;
/*      */ import org.apache.logging.log4j.util.Strings;
/*      */ import org.apache.logging.log4j.util.Supplier;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class AbstractLogger
/*      */   implements ExtendedLogger, LocationAwareLogger, Serializable
/*      */ {
/*   64 */   public static final Marker FLOW_MARKER = MarkerManager.getMarker("FLOW");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   69 */   public static final Marker ENTRY_MARKER = MarkerManager.getMarker("ENTER").setParents(new Marker[] { FLOW_MARKER });
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   74 */   public static final Marker EXIT_MARKER = MarkerManager.getMarker("EXIT").setParents(new Marker[] { FLOW_MARKER });
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   79 */   public static final Marker EXCEPTION_MARKER = MarkerManager.getMarker("EXCEPTION");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   84 */   public static final Marker THROWING_MARKER = MarkerManager.getMarker("THROWING").setParents(new Marker[] { EXCEPTION_MARKER });
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   89 */   public static final Marker CATCHING_MARKER = MarkerManager.getMarker("CATCHING").setParents(new Marker[] { EXCEPTION_MARKER });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   95 */   public static final Class<? extends MessageFactory> DEFAULT_MESSAGE_FACTORY_CLASS = createClassForProperty("log4j2.messageFactory", ReusableMessageFactory.class, ParameterizedMessageFactory.class);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  102 */   public static final Class<? extends FlowMessageFactory> DEFAULT_FLOW_MESSAGE_FACTORY_CLASS = createFlowClassForProperty("log4j2.flowMessageFactory", DefaultFlowMessageFactory.class);
/*      */   
/*      */   private static final long serialVersionUID = 2L;
/*      */   
/*  106 */   private static final String FQCN = AbstractLogger.class.getName();
/*      */   
/*      */   private static final String THROWING = "Throwing";
/*      */   private static final String CATCHING = "Catching";
/*      */   protected final String name;
/*      */   private final MessageFactory2 messageFactory;
/*      */   private final FlowMessageFactory flowMessageFactory;
/*  113 */   private static final ThreadLocal<int[]> recursionDepthHolder = (ThreadLocal)new ThreadLocal<>();
/*      */ 
/*      */   
/*      */   protected final transient ThreadLocal<DefaultLogBuilder> logBuilder;
/*      */ 
/*      */   
/*      */   public AbstractLogger() {
/*  120 */     this.name = getClass().getName();
/*  121 */     this.messageFactory = createDefaultMessageFactory();
/*  122 */     this.flowMessageFactory = createDefaultFlowMessageFactory();
/*  123 */     this.logBuilder = new LocalLogBuilder(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AbstractLogger(String name) {
/*  132 */     this(name, (MessageFactory)createDefaultMessageFactory());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AbstractLogger(String name, MessageFactory messageFactory) {
/*  142 */     this.name = name;
/*  143 */     this.messageFactory = (messageFactory == null) ? createDefaultMessageFactory() : narrow(messageFactory);
/*  144 */     this.flowMessageFactory = createDefaultFlowMessageFactory();
/*  145 */     this.logBuilder = new LocalLogBuilder(this);
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
/*      */   public static void checkMessageFactory(ExtendedLogger logger, MessageFactory messageFactory) {
/*  157 */     String name = logger.getName();
/*  158 */     MessageFactory loggerMessageFactory = logger.getMessageFactory();
/*  159 */     if (messageFactory != null && !loggerMessageFactory.equals(messageFactory)) {
/*  160 */       StatusLogger.getLogger().warn("The Logger {} was created with the message factory {} and is now requested with the message factory {}, which may create log events with unexpected formatting.", name, loggerMessageFactory, messageFactory);
/*      */ 
/*      */     
/*      */     }
/*  164 */     else if (messageFactory == null && !loggerMessageFactory.getClass().equals(DEFAULT_MESSAGE_FACTORY_CLASS)) {
/*      */       
/*  166 */       StatusLogger.getLogger()
/*  167 */         .warn("The Logger {} was created with the message factory {} and is now requested with a null message factory (defaults to {}), which may create log events with unexpected formatting.", name, loggerMessageFactory, DEFAULT_MESSAGE_FACTORY_CLASS
/*      */ 
/*      */           
/*  170 */           .getName());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void catching(Level level, Throwable throwable) {
/*  176 */     catching(FQCN, level, throwable);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void catching(String fqcn, Level level, Throwable throwable) {
/*  187 */     if (isEnabled(level, CATCHING_MARKER, (Object)null, (Throwable)null)) {
/*  188 */       logMessageSafely(fqcn, level, CATCHING_MARKER, catchingMsg(throwable), throwable);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void catching(Throwable throwable) {
/*  194 */     if (isEnabled(Level.ERROR, CATCHING_MARKER, (Object)null, (Throwable)null)) {
/*  195 */       logMessageSafely(FQCN, Level.ERROR, CATCHING_MARKER, catchingMsg(throwable), throwable);
/*      */     }
/*      */   }
/*      */   
/*      */   protected Message catchingMsg(Throwable throwable) {
/*  200 */     return this.messageFactory.newMessage("Catching");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Class<? extends MessageFactory> createClassForProperty(String property, Class<ReusableMessageFactory> reusableParameterizedMessageFactoryClass, Class<ParameterizedMessageFactory> parameterizedMessageFactoryClass) {
/*      */     try {
/*  208 */       String fallback = Constants.ENABLE_THREADLOCALS ? reusableParameterizedMessageFactoryClass.getName() : parameterizedMessageFactoryClass.getName();
/*  209 */       String clsName = PropertiesUtil.getProperties().getStringProperty(property, fallback);
/*  210 */       return LoaderUtil.loadClass(clsName).asSubclass(MessageFactory.class);
/*  211 */     } catch (Throwable throwable) {
/*  212 */       return (Class)parameterizedMessageFactoryClass;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static Class<? extends FlowMessageFactory> createFlowClassForProperty(String property, Class<DefaultFlowMessageFactory> defaultFlowMessageFactoryClass) {
/*      */     try {
/*  219 */       String clsName = PropertiesUtil.getProperties().getStringProperty(property, defaultFlowMessageFactoryClass.getName());
/*  220 */       return LoaderUtil.loadClass(clsName).asSubclass(FlowMessageFactory.class);
/*  221 */     } catch (Throwable throwable) {
/*  222 */       return (Class)defaultFlowMessageFactoryClass;
/*      */     } 
/*      */   }
/*      */   
/*      */   private static MessageFactory2 createDefaultMessageFactory() {
/*      */     try {
/*  228 */       MessageFactory result = DEFAULT_MESSAGE_FACTORY_CLASS.newInstance();
/*  229 */       return narrow(result);
/*  230 */     } catch (InstantiationException|IllegalAccessException e) {
/*  231 */       throw new IllegalStateException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static MessageFactory2 narrow(MessageFactory result) {
/*  236 */     if (result instanceof MessageFactory2) {
/*  237 */       return (MessageFactory2)result;
/*      */     }
/*  239 */     return new MessageFactory2Adapter(result);
/*      */   }
/*      */   
/*      */   private static FlowMessageFactory createDefaultFlowMessageFactory() {
/*      */     try {
/*  244 */       return DEFAULT_FLOW_MESSAGE_FACTORY_CLASS.newInstance();
/*  245 */     } catch (InstantiationException|IllegalAccessException e) {
/*  246 */       throw new IllegalStateException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, CharSequence message) {
/*  252 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, CharSequence message, Throwable throwable) {
/*  257 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, Message message) {
/*  262 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, (message != null) ? message.getThrowable() : null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, Message message, Throwable throwable) {
/*  267 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, Object message) {
/*  272 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, Object message, Throwable throwable) {
/*  277 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, String message) {
/*  282 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, String message, Object... params) {
/*  287 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, params);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, String message, Throwable throwable) {
/*  292 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Message message) {
/*  297 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, (message != null) ? message.getThrowable() : null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Message message, Throwable throwable) {
/*  302 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(CharSequence message) {
/*  307 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(CharSequence message, Throwable throwable) {
/*  312 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Object message) {
/*  317 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Object message, Throwable throwable) {
/*  322 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(String message) {
/*  327 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(String message, Object... params) {
/*  332 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, params);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(String message, Throwable throwable) {
/*  337 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Supplier<?> messageSupplier) {
/*  342 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, messageSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Supplier<?> messageSupplier, Throwable throwable) {
/*  347 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, messageSupplier, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, Supplier<?> messageSupplier) {
/*  352 */     logIfEnabled(FQCN, Level.DEBUG, marker, messageSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, String message, Supplier<?>... paramSuppliers) {
/*  357 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, paramSuppliers);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, Supplier<?> messageSupplier, Throwable throwable) {
/*  362 */     logIfEnabled(FQCN, Level.DEBUG, marker, messageSupplier, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(String message, Supplier<?>... paramSuppliers) {
/*  367 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, paramSuppliers);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, MessageSupplier messageSupplier) {
/*  372 */     logIfEnabled(FQCN, Level.DEBUG, marker, messageSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, MessageSupplier messageSupplier, Throwable throwable) {
/*  377 */     logIfEnabled(FQCN, Level.DEBUG, marker, messageSupplier, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(MessageSupplier messageSupplier) {
/*  382 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, messageSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(MessageSupplier messageSupplier, Throwable throwable) {
/*  387 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, messageSupplier, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, String message, Object p0) {
/*  392 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, p0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, String message, Object p0, Object p1) {
/*  397 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, p0, p1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, String message, Object p0, Object p1, Object p2) {
/*  402 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, p0, p1, p2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
/*  408 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, p0, p1, p2, p3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/*  414 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, p0, p1, p2, p3, p4);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/*  420 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, p0, p1, p2, p3, p4, p5);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/*  427 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, p0, p1, p2, p3, p4, p5, p6);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/*  434 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/*  441 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/*  448 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(String message, Object p0) {
/*  453 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, p0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(String message, Object p0, Object p1) {
/*  458 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, p0, p1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(String message, Object p0, Object p1, Object p2) {
/*  463 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, p0, p1, p2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(String message, Object p0, Object p1, Object p2, Object p3) {
/*  468 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, p0, p1, p2, p3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/*  474 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, p0, p1, p2, p3, p4);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/*  480 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, p0, p1, p2, p3, p4, p5);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/*  486 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/*  493 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/*  500 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/*  507 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected EntryMessage enter(String fqcn, String format, Supplier<?>... paramSuppliers) {
/*  518 */     EntryMessage entryMsg = null;
/*  519 */     if (isEnabled(Level.TRACE, ENTRY_MARKER, (Object)null, (Throwable)null)) {
/*  520 */       logMessageSafely(fqcn, Level.TRACE, ENTRY_MARKER, (Message)(entryMsg = entryMsg(format, paramSuppliers)), (Throwable)null);
/*      */     }
/*  522 */     return entryMsg;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected EntryMessage enter(String fqcn, String format, MessageSupplier... paramSuppliers) {
/*  534 */     EntryMessage entryMsg = null;
/*  535 */     if (isEnabled(Level.TRACE, ENTRY_MARKER, (Object)null, (Throwable)null)) {
/*  536 */       logMessageSafely(fqcn, Level.TRACE, ENTRY_MARKER, (Message)(entryMsg = entryMsg(format, paramSuppliers)), (Throwable)null);
/*      */     }
/*  538 */     return entryMsg;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected EntryMessage enter(String fqcn, String format, Object... params) {
/*  549 */     EntryMessage entryMsg = null;
/*  550 */     if (isEnabled(Level.TRACE, ENTRY_MARKER, (Object)null, (Throwable)null)) {
/*  551 */       logMessageSafely(fqcn, Level.TRACE, ENTRY_MARKER, (Message)(entryMsg = entryMsg(format, params)), (Throwable)null);
/*      */     }
/*  553 */     return entryMsg;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected EntryMessage enter(String fqcn, MessageSupplier messageSupplier) {
/*  564 */     EntryMessage message = null;
/*  565 */     if (isEnabled(Level.TRACE, ENTRY_MARKER, (Object)null, (Throwable)null)) {
/*  566 */       logMessageSafely(fqcn, Level.TRACE, ENTRY_MARKER, (Message)(message = this.flowMessageFactory.newEntryMessage(messageSupplier
/*  567 */             .get())), (Throwable)null);
/*      */     }
/*  569 */     return message;
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
/*      */   protected EntryMessage enter(String fqcn, Message message) {
/*  582 */     EntryMessage flowMessage = null;
/*  583 */     if (isEnabled(Level.TRACE, ENTRY_MARKER, (Object)null, (Throwable)null)) {
/*  584 */       logMessageSafely(fqcn, Level.TRACE, ENTRY_MARKER, (Message)(flowMessage = this.flowMessageFactory.newEntryMessage(message)), (Throwable)null);
/*      */     }
/*      */     
/*  587 */     return flowMessage;
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void entry() {
/*  593 */     entry(FQCN, (Object[])null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void entry(Object... params) {
/*  598 */     entry(FQCN, params);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void entry(String fqcn, Object... params) {
/*  608 */     if (isEnabled(Level.TRACE, ENTRY_MARKER, (Object)null, (Throwable)null)) {
/*  609 */       if (params == null) {
/*  610 */         logMessageSafely(fqcn, Level.TRACE, ENTRY_MARKER, (Message)entryMsg((String)null, (Supplier<?>[])null), (Throwable)null);
/*      */       } else {
/*  612 */         logMessageSafely(fqcn, Level.TRACE, ENTRY_MARKER, (Message)entryMsg((String)null, params), (Throwable)null);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   protected EntryMessage entryMsg(String format, Object... params) {
/*  618 */     int count = (params == null) ? 0 : params.length;
/*  619 */     if (count == 0) {
/*  620 */       if (Strings.isEmpty(format)) {
/*  621 */         return this.flowMessageFactory.newEntryMessage(null);
/*      */       }
/*  623 */       return this.flowMessageFactory.newEntryMessage((Message)new SimpleMessage(format));
/*      */     } 
/*  625 */     if (format != null) {
/*  626 */       return this.flowMessageFactory.newEntryMessage((Message)new ParameterizedMessage(format, params));
/*      */     }
/*  628 */     StringBuilder sb = new StringBuilder();
/*  629 */     sb.append("params(");
/*  630 */     for (int i = 0; i < count; i++) {
/*  631 */       if (i > 0) {
/*  632 */         sb.append(", ");
/*      */       }
/*  634 */       Object parm = params[i];
/*  635 */       sb.append((parm instanceof Message) ? ((Message)parm).getFormattedMessage() : String.valueOf(parm));
/*      */     } 
/*  637 */     sb.append(')');
/*  638 */     return this.flowMessageFactory.newEntryMessage((Message)new SimpleMessage(sb));
/*      */   }
/*      */   
/*      */   protected EntryMessage entryMsg(String format, MessageSupplier... paramSuppliers) {
/*  642 */     int count = (paramSuppliers == null) ? 0 : paramSuppliers.length;
/*  643 */     Object[] params = new Object[count];
/*  644 */     for (int i = 0; i < count; i++) {
/*  645 */       params[i] = paramSuppliers[i].get();
/*  646 */       params[i] = (params[i] != null) ? ((Message)params[i]).getFormattedMessage() : null;
/*      */     } 
/*  648 */     return entryMsg(format, params);
/*      */   }
/*      */   
/*      */   protected EntryMessage entryMsg(String format, Supplier<?>... paramSuppliers) {
/*  652 */     int count = (paramSuppliers == null) ? 0 : paramSuppliers.length;
/*  653 */     Object[] params = new Object[count];
/*  654 */     for (int i = 0; i < count; i++) {
/*  655 */       params[i] = paramSuppliers[i].get();
/*  656 */       if (params[i] instanceof Message) {
/*  657 */         params[i] = ((Message)params[i]).getFormattedMessage();
/*      */       }
/*      */     } 
/*  660 */     return entryMsg(format, params);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Marker marker, Message message) {
/*  665 */     logIfEnabled(FQCN, Level.ERROR, marker, message, (message != null) ? message.getThrowable() : null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Marker marker, Message message, Throwable throwable) {
/*  670 */     logIfEnabled(FQCN, Level.ERROR, marker, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Marker marker, CharSequence message) {
/*  675 */     logIfEnabled(FQCN, Level.ERROR, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Marker marker, CharSequence message, Throwable throwable) {
/*  680 */     logIfEnabled(FQCN, Level.ERROR, marker, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Marker marker, Object message) {
/*  685 */     logIfEnabled(FQCN, Level.ERROR, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Marker marker, Object message, Throwable throwable) {
/*  690 */     logIfEnabled(FQCN, Level.ERROR, marker, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Marker marker, String message) {
/*  695 */     logIfEnabled(FQCN, Level.ERROR, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Marker marker, String message, Object... params) {
/*  700 */     logIfEnabled(FQCN, Level.ERROR, marker, message, params);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Marker marker, String message, Throwable throwable) {
/*  705 */     logIfEnabled(FQCN, Level.ERROR, marker, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Message message) {
/*  710 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, (message != null) ? message.getThrowable() : null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Message message, Throwable throwable) {
/*  715 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(CharSequence message) {
/*  720 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(CharSequence message, Throwable throwable) {
/*  725 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Object message) {
/*  730 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Object message, Throwable throwable) {
/*  735 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(String message) {
/*  740 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(String message, Object... params) {
/*  745 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, params);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(String message, Throwable throwable) {
/*  750 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Supplier<?> messageSupplier) {
/*  755 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, messageSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Supplier<?> messageSupplier, Throwable throwable) {
/*  760 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, messageSupplier, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Marker marker, Supplier<?> messageSupplier) {
/*  765 */     logIfEnabled(FQCN, Level.ERROR, marker, messageSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Marker marker, String message, Supplier<?>... paramSuppliers) {
/*  770 */     logIfEnabled(FQCN, Level.ERROR, marker, message, paramSuppliers);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Marker marker, Supplier<?> messageSupplier, Throwable throwable) {
/*  775 */     logIfEnabled(FQCN, Level.ERROR, marker, messageSupplier, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(String message, Supplier<?>... paramSuppliers) {
/*  780 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, paramSuppliers);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Marker marker, MessageSupplier messageSupplier) {
/*  785 */     logIfEnabled(FQCN, Level.ERROR, marker, messageSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Marker marker, MessageSupplier messageSupplier, Throwable throwable) {
/*  790 */     logIfEnabled(FQCN, Level.ERROR, marker, messageSupplier, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(MessageSupplier messageSupplier) {
/*  795 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, messageSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(MessageSupplier messageSupplier, Throwable throwable) {
/*  800 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, messageSupplier, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Marker marker, String message, Object p0) {
/*  805 */     logIfEnabled(FQCN, Level.ERROR, marker, message, p0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Marker marker, String message, Object p0, Object p1) {
/*  810 */     logIfEnabled(FQCN, Level.ERROR, marker, message, p0, p1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Marker marker, String message, Object p0, Object p1, Object p2) {
/*  815 */     logIfEnabled(FQCN, Level.ERROR, marker, message, p0, p1, p2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
/*  821 */     logIfEnabled(FQCN, Level.ERROR, marker, message, p0, p1, p2, p3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/*  827 */     logIfEnabled(FQCN, Level.ERROR, marker, message, p0, p1, p2, p3, p4);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/*  833 */     logIfEnabled(FQCN, Level.ERROR, marker, message, p0, p1, p2, p3, p4, p5);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/*  840 */     logIfEnabled(FQCN, Level.ERROR, marker, message, p0, p1, p2, p3, p4, p5, p6);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/*  847 */     logIfEnabled(FQCN, Level.ERROR, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/*  854 */     logIfEnabled(FQCN, Level.ERROR, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/*  861 */     logIfEnabled(FQCN, Level.ERROR, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(String message, Object p0) {
/*  866 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, p0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(String message, Object p0, Object p1) {
/*  871 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, p0, p1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(String message, Object p0, Object p1, Object p2) {
/*  876 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, p0, p1, p2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(String message, Object p0, Object p1, Object p2, Object p3) {
/*  881 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, p0, p1, p2, p3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/*  887 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, p0, p1, p2, p3, p4);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/*  893 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, p0, p1, p2, p3, p4, p5);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/*  899 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/*  905 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/*  911 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/*  917 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void exit() {
/*  923 */     exit(FQCN, (Object)null);
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public <R> R exit(R result) {
/*  929 */     return exit(FQCN, result);
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
/*      */   protected <R> R exit(String fqcn, R result) {
/*  941 */     if (isEnabled(Level.TRACE, EXIT_MARKER, (CharSequence)null, (Throwable)null)) {
/*  942 */       logMessageSafely(fqcn, Level.TRACE, EXIT_MARKER, exitMsg((String)null, result), (Throwable)null);
/*      */     }
/*  944 */     return result;
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
/*      */   protected <R> R exit(String fqcn, String format, R result) {
/*  956 */     if (isEnabled(Level.TRACE, EXIT_MARKER, (CharSequence)null, (Throwable)null)) {
/*  957 */       logMessageSafely(fqcn, Level.TRACE, EXIT_MARKER, exitMsg(format, result), (Throwable)null);
/*      */     }
/*  959 */     return result;
/*      */   }
/*      */   
/*      */   protected Message exitMsg(String format, Object result) {
/*  963 */     if (result == null) {
/*  964 */       if (format == null) {
/*  965 */         return this.messageFactory.newMessage("Exit");
/*      */       }
/*  967 */       return this.messageFactory.newMessage("Exit: " + format);
/*      */     } 
/*  969 */     if (format == null) {
/*  970 */       return this.messageFactory.newMessage("Exit with(" + result + ')');
/*      */     }
/*  972 */     return this.messageFactory.newMessage("Exit: " + format, result);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, Message message) {
/*  978 */     logIfEnabled(FQCN, Level.FATAL, marker, message, (message != null) ? message.getThrowable() : null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, Message message, Throwable throwable) {
/*  983 */     logIfEnabled(FQCN, Level.FATAL, marker, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, CharSequence message) {
/*  988 */     logIfEnabled(FQCN, Level.FATAL, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, CharSequence message, Throwable throwable) {
/*  993 */     logIfEnabled(FQCN, Level.FATAL, marker, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, Object message) {
/*  998 */     logIfEnabled(FQCN, Level.FATAL, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, Object message, Throwable throwable) {
/* 1003 */     logIfEnabled(FQCN, Level.FATAL, marker, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, String message) {
/* 1008 */     logIfEnabled(FQCN, Level.FATAL, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, String message, Object... params) {
/* 1013 */     logIfEnabled(FQCN, Level.FATAL, marker, message, params);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, String message, Throwable throwable) {
/* 1018 */     logIfEnabled(FQCN, Level.FATAL, marker, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Message message) {
/* 1023 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, (message != null) ? message.getThrowable() : null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Message message, Throwable throwable) {
/* 1028 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(CharSequence message) {
/* 1033 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(CharSequence message, Throwable throwable) {
/* 1038 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Object message) {
/* 1043 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Object message, Throwable throwable) {
/* 1048 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(String message) {
/* 1053 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(String message, Object... params) {
/* 1058 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, params);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(String message, Throwable throwable) {
/* 1063 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Supplier<?> messageSupplier) {
/* 1068 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, messageSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Supplier<?> messageSupplier, Throwable throwable) {
/* 1073 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, messageSupplier, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, Supplier<?> messageSupplier) {
/* 1078 */     logIfEnabled(FQCN, Level.FATAL, marker, messageSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, String message, Supplier<?>... paramSuppliers) {
/* 1083 */     logIfEnabled(FQCN, Level.FATAL, marker, message, paramSuppliers);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, Supplier<?> messageSupplier, Throwable throwable) {
/* 1088 */     logIfEnabled(FQCN, Level.FATAL, marker, messageSupplier, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(String message, Supplier<?>... paramSuppliers) {
/* 1093 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, paramSuppliers);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, MessageSupplier messageSupplier) {
/* 1098 */     logIfEnabled(FQCN, Level.FATAL, marker, messageSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, MessageSupplier messageSupplier, Throwable throwable) {
/* 1103 */     logIfEnabled(FQCN, Level.FATAL, marker, messageSupplier, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(MessageSupplier messageSupplier) {
/* 1108 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, messageSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(MessageSupplier messageSupplier, Throwable throwable) {
/* 1113 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, messageSupplier, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, String message, Object p0) {
/* 1118 */     logIfEnabled(FQCN, Level.FATAL, marker, message, p0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, String message, Object p0, Object p1) {
/* 1123 */     logIfEnabled(FQCN, Level.FATAL, marker, message, p0, p1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, String message, Object p0, Object p1, Object p2) {
/* 1128 */     logIfEnabled(FQCN, Level.FATAL, marker, message, p0, p1, p2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
/* 1134 */     logIfEnabled(FQCN, Level.FATAL, marker, message, p0, p1, p2, p3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 1140 */     logIfEnabled(FQCN, Level.FATAL, marker, message, p0, p1, p2, p3, p4);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 1146 */     logIfEnabled(FQCN, Level.FATAL, marker, message, p0, p1, p2, p3, p4, p5);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 1152 */     logIfEnabled(FQCN, Level.FATAL, marker, message, p0, p1, p2, p3, p4, p5, p6);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 1158 */     logIfEnabled(FQCN, Level.FATAL, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 1165 */     logIfEnabled(FQCN, Level.FATAL, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 1172 */     logIfEnabled(FQCN, Level.FATAL, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(String message, Object p0) {
/* 1177 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, p0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(String message, Object p0, Object p1) {
/* 1182 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, p0, p1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(String message, Object p0, Object p1, Object p2) {
/* 1187 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, p0, p1, p2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(String message, Object p0, Object p1, Object p2, Object p3) {
/* 1192 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, p0, p1, p2, p3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 1198 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, p0, p1, p2, p3, p4);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 1204 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, p0, p1, p2, p3, p4, p5);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 1210 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 1216 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 1222 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 1229 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <MF extends MessageFactory> MF getMessageFactory() {
/* 1235 */     return (MF)this.messageFactory;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getName() {
/* 1240 */     return this.name;
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Marker marker, Message message) {
/* 1245 */     logIfEnabled(FQCN, Level.INFO, marker, message, (message != null) ? message.getThrowable() : null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Marker marker, Message message, Throwable throwable) {
/* 1250 */     logIfEnabled(FQCN, Level.INFO, marker, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Marker marker, CharSequence message) {
/* 1255 */     logIfEnabled(FQCN, Level.INFO, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Marker marker, CharSequence message, Throwable throwable) {
/* 1260 */     logIfEnabled(FQCN, Level.INFO, marker, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Marker marker, Object message) {
/* 1265 */     logIfEnabled(FQCN, Level.INFO, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Marker marker, Object message, Throwable throwable) {
/* 1270 */     logIfEnabled(FQCN, Level.INFO, marker, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Marker marker, String message) {
/* 1275 */     logIfEnabled(FQCN, Level.INFO, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Marker marker, String message, Object... params) {
/* 1280 */     logIfEnabled(FQCN, Level.INFO, marker, message, params);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Marker marker, String message, Throwable throwable) {
/* 1285 */     logIfEnabled(FQCN, Level.INFO, marker, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Message message) {
/* 1290 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, (message != null) ? message.getThrowable() : null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Message message, Throwable throwable) {
/* 1295 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(CharSequence message) {
/* 1300 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(CharSequence message, Throwable throwable) {
/* 1305 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Object message) {
/* 1310 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Object message, Throwable throwable) {
/* 1315 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(String message) {
/* 1320 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(String message, Object... params) {
/* 1325 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, params);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(String message, Throwable throwable) {
/* 1330 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Supplier<?> messageSupplier) {
/* 1335 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, messageSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Supplier<?> messageSupplier, Throwable throwable) {
/* 1340 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, messageSupplier, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Marker marker, Supplier<?> messageSupplier) {
/* 1345 */     logIfEnabled(FQCN, Level.INFO, marker, messageSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Marker marker, String message, Supplier<?>... paramSuppliers) {
/* 1350 */     logIfEnabled(FQCN, Level.INFO, marker, message, paramSuppliers);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Marker marker, Supplier<?> messageSupplier, Throwable throwable) {
/* 1355 */     logIfEnabled(FQCN, Level.INFO, marker, messageSupplier, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(String message, Supplier<?>... paramSuppliers) {
/* 1360 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, paramSuppliers);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Marker marker, MessageSupplier messageSupplier) {
/* 1365 */     logIfEnabled(FQCN, Level.INFO, marker, messageSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Marker marker, MessageSupplier messageSupplier, Throwable throwable) {
/* 1370 */     logIfEnabled(FQCN, Level.INFO, marker, messageSupplier, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(MessageSupplier messageSupplier) {
/* 1375 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, messageSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(MessageSupplier messageSupplier, Throwable throwable) {
/* 1380 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, messageSupplier, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Marker marker, String message, Object p0) {
/* 1385 */     logIfEnabled(FQCN, Level.INFO, marker, message, p0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Marker marker, String message, Object p0, Object p1) {
/* 1390 */     logIfEnabled(FQCN, Level.INFO, marker, message, p0, p1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Marker marker, String message, Object p0, Object p1, Object p2) {
/* 1395 */     logIfEnabled(FQCN, Level.INFO, marker, message, p0, p1, p2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
/* 1401 */     logIfEnabled(FQCN, Level.INFO, marker, message, p0, p1, p2, p3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 1407 */     logIfEnabled(FQCN, Level.INFO, marker, message, p0, p1, p2, p3, p4);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 1413 */     logIfEnabled(FQCN, Level.INFO, marker, message, p0, p1, p2, p3, p4, p5);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 1419 */     logIfEnabled(FQCN, Level.INFO, marker, message, p0, p1, p2, p3, p4, p5, p6);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 1425 */     logIfEnabled(FQCN, Level.INFO, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 1432 */     logIfEnabled(FQCN, Level.INFO, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 1439 */     logIfEnabled(FQCN, Level.INFO, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(String message, Object p0) {
/* 1444 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, p0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(String message, Object p0, Object p1) {
/* 1449 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, p0, p1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(String message, Object p0, Object p1, Object p2) {
/* 1454 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, p0, p1, p2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(String message, Object p0, Object p1, Object p2, Object p3) {
/* 1459 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, p0, p1, p2, p3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 1465 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, p0, p1, p2, p3, p4);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 1471 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, p0, p1, p2, p3, p4, p5);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 1477 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 1484 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 1491 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 1498 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isDebugEnabled() {
/* 1503 */     return isEnabled(Level.DEBUG, (Marker)null, (String)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isDebugEnabled(Marker marker) {
/* 1508 */     return isEnabled(Level.DEBUG, marker, (Object)null, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isEnabled(Level level) {
/* 1513 */     return isEnabled(level, (Marker)null, (Object)null, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isEnabled(Level level, Marker marker) {
/* 1518 */     return isEnabled(level, marker, (Object)null, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isErrorEnabled() {
/* 1523 */     return isEnabled(Level.ERROR, (Marker)null, (Object)null, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isErrorEnabled(Marker marker) {
/* 1528 */     return isEnabled(Level.ERROR, marker, (Object)null, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFatalEnabled() {
/* 1533 */     return isEnabled(Level.FATAL, (Marker)null, (Object)null, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFatalEnabled(Marker marker) {
/* 1538 */     return isEnabled(Level.FATAL, marker, (Object)null, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isInfoEnabled() {
/* 1543 */     return isEnabled(Level.INFO, (Marker)null, (Object)null, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isInfoEnabled(Marker marker) {
/* 1548 */     return isEnabled(Level.INFO, marker, (Object)null, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isTraceEnabled() {
/* 1553 */     return isEnabled(Level.TRACE, (Marker)null, (Object)null, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isTraceEnabled(Marker marker) {
/* 1558 */     return isEnabled(Level.TRACE, marker, (Object)null, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isWarnEnabled() {
/* 1563 */     return isEnabled(Level.WARN, (Marker)null, (Object)null, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isWarnEnabled(Marker marker) {
/* 1568 */     return isEnabled(Level.WARN, marker, (Object)null, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, Message message) {
/* 1573 */     logIfEnabled(FQCN, level, marker, message, (message != null) ? message.getThrowable() : null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, Message message, Throwable throwable) {
/* 1578 */     logIfEnabled(FQCN, level, marker, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, CharSequence message) {
/* 1583 */     logIfEnabled(FQCN, level, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, CharSequence message, Throwable throwable) {
/* 1588 */     if (isEnabled(level, marker, message, throwable)) {
/* 1589 */       logMessage(FQCN, level, marker, message, throwable);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, Object message) {
/* 1595 */     logIfEnabled(FQCN, level, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, Object message, Throwable throwable) {
/* 1600 */     if (isEnabled(level, marker, message, throwable)) {
/* 1601 */       logMessage(FQCN, level, marker, message, throwable);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, String message) {
/* 1607 */     logIfEnabled(FQCN, level, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, String message, Object... params) {
/* 1612 */     logIfEnabled(FQCN, level, marker, message, params);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, String message, Throwable throwable) {
/* 1617 */     logIfEnabled(FQCN, level, marker, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Message message) {
/* 1622 */     logIfEnabled(FQCN, level, (Marker)null, message, (message != null) ? message.getThrowable() : null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Message message, Throwable throwable) {
/* 1627 */     logIfEnabled(FQCN, level, (Marker)null, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, CharSequence message) {
/* 1632 */     logIfEnabled(FQCN, level, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, CharSequence message, Throwable throwable) {
/* 1637 */     logIfEnabled(FQCN, level, (Marker)null, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Object message) {
/* 1642 */     logIfEnabled(FQCN, level, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Object message, Throwable throwable) {
/* 1647 */     logIfEnabled(FQCN, level, (Marker)null, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, String message) {
/* 1652 */     logIfEnabled(FQCN, level, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, String message, Object... params) {
/* 1657 */     logIfEnabled(FQCN, level, (Marker)null, message, params);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, String message, Throwable throwable) {
/* 1662 */     logIfEnabled(FQCN, level, (Marker)null, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Supplier<?> messageSupplier) {
/* 1667 */     logIfEnabled(FQCN, level, (Marker)null, messageSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Supplier<?> messageSupplier, Throwable throwable) {
/* 1672 */     logIfEnabled(FQCN, level, (Marker)null, messageSupplier, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, Supplier<?> messageSupplier) {
/* 1677 */     logIfEnabled(FQCN, level, marker, messageSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, String message, Supplier<?>... paramSuppliers) {
/* 1682 */     logIfEnabled(FQCN, level, marker, message, paramSuppliers);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, Supplier<?> messageSupplier, Throwable throwable) {
/* 1687 */     logIfEnabled(FQCN, level, marker, messageSupplier, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, String message, Supplier<?>... paramSuppliers) {
/* 1692 */     logIfEnabled(FQCN, level, (Marker)null, message, paramSuppliers);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, MessageSupplier messageSupplier) {
/* 1697 */     logIfEnabled(FQCN, level, marker, messageSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, MessageSupplier messageSupplier, Throwable throwable) {
/* 1702 */     logIfEnabled(FQCN, level, marker, messageSupplier, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, MessageSupplier messageSupplier) {
/* 1707 */     logIfEnabled(FQCN, level, (Marker)null, messageSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, MessageSupplier messageSupplier, Throwable throwable) {
/* 1712 */     logIfEnabled(FQCN, level, (Marker)null, messageSupplier, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, String message, Object p0) {
/* 1717 */     logIfEnabled(FQCN, level, marker, message, p0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, String message, Object p0, Object p1) {
/* 1722 */     logIfEnabled(FQCN, level, marker, message, p0, p1);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
/* 1728 */     logIfEnabled(FQCN, level, marker, message, p0, p1, p2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
/* 1734 */     logIfEnabled(FQCN, level, marker, message, p0, p1, p2, p3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 1740 */     logIfEnabled(FQCN, level, marker, message, p0, p1, p2, p3, p4);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 1746 */     logIfEnabled(FQCN, level, marker, message, p0, p1, p2, p3, p4, p5);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 1752 */     logIfEnabled(FQCN, level, marker, message, p0, p1, p2, p3, p4, p5, p6);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 1759 */     logIfEnabled(FQCN, level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 1766 */     logIfEnabled(FQCN, level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 1773 */     logIfEnabled(FQCN, level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, String message, Object p0) {
/* 1778 */     logIfEnabled(FQCN, level, (Marker)null, message, p0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, String message, Object p0, Object p1) {
/* 1783 */     logIfEnabled(FQCN, level, (Marker)null, message, p0, p1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, String message, Object p0, Object p1, Object p2) {
/* 1788 */     logIfEnabled(FQCN, level, (Marker)null, message, p0, p1, p2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3) {
/* 1793 */     logIfEnabled(FQCN, level, (Marker)null, message, p0, p1, p2, p3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 1799 */     logIfEnabled(FQCN, level, (Marker)null, message, p0, p1, p2, p3, p4);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 1805 */     logIfEnabled(FQCN, level, (Marker)null, message, p0, p1, p2, p3, p4, p5);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 1811 */     logIfEnabled(FQCN, level, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 1817 */     logIfEnabled(FQCN, level, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 1823 */     logIfEnabled(FQCN, level, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 1829 */     logIfEnabled(FQCN, level, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, Message message, Throwable throwable) {
/* 1835 */     if (isEnabled(level, marker, message, throwable)) {
/* 1836 */       logMessageSafely(fqcn, level, marker, message, throwable);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, MessageSupplier messageSupplier, Throwable throwable) {
/* 1843 */     if (isEnabled(level, marker, messageSupplier, throwable)) {
/* 1844 */       logMessage(fqcn, level, marker, messageSupplier, throwable);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, Object message, Throwable throwable) {
/* 1851 */     if (isEnabled(level, marker, message, throwable)) {
/* 1852 */       logMessage(fqcn, level, marker, message, throwable);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, CharSequence message, Throwable throwable) {
/* 1859 */     if (isEnabled(level, marker, message, throwable)) {
/* 1860 */       logMessage(fqcn, level, marker, message, throwable);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, Supplier<?> messageSupplier, Throwable throwable) {
/* 1867 */     if (isEnabled(level, marker, messageSupplier, throwable)) {
/* 1868 */       logMessage(fqcn, level, marker, messageSupplier, throwable);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, String message) {
/* 1874 */     if (isEnabled(level, marker, message)) {
/* 1875 */       logMessage(fqcn, level, marker, message);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Supplier<?>... paramSuppliers) {
/* 1882 */     if (isEnabled(level, marker, message)) {
/* 1883 */       logMessage(fqcn, level, marker, message, paramSuppliers);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object... params) {
/* 1890 */     if (isEnabled(level, marker, message, params)) {
/* 1891 */       logMessage(fqcn, level, marker, message, params);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0) {
/* 1898 */     if (isEnabled(level, marker, message, p0)) {
/* 1899 */       logMessage(fqcn, level, marker, message, p0);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1) {
/* 1906 */     if (isEnabled(level, marker, message, p0, p1)) {
/* 1907 */       logMessage(fqcn, level, marker, message, p0, p1);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
/* 1914 */     if (isEnabled(level, marker, message, p0, p1, p2)) {
/* 1915 */       logMessage(fqcn, level, marker, message, p0, p1, p2);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
/* 1922 */     if (isEnabled(level, marker, message, p0, p1, p2, p3)) {
/* 1923 */       logMessage(fqcn, level, marker, message, p0, p1, p2, p3);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 1930 */     if (isEnabled(level, marker, message, p0, p1, p2, p3, p4)) {
/* 1931 */       logMessage(fqcn, level, marker, message, p0, p1, p2, p3, p4);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 1938 */     if (isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5)) {
/* 1939 */       logMessage(fqcn, level, marker, message, p0, p1, p2, p3, p4, p5);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 1947 */     if (isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5, p6)) {
/* 1948 */       logMessage(fqcn, level, marker, message, p0, p1, p2, p3, p4, p5, p6);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 1956 */     if (isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7)) {
/* 1957 */       logMessage(fqcn, level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 1965 */     if (isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8)) {
/* 1966 */       logMessage(fqcn, level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 1974 */     if (isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9)) {
/* 1975 */       logMessage(fqcn, level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Throwable throwable) {
/* 1982 */     if (isEnabled(level, marker, message, throwable)) {
/* 1983 */       logMessage(fqcn, level, marker, message, throwable);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, CharSequence message, Throwable throwable) {
/* 1989 */     logMessageSafely(fqcn, level, marker, this.messageFactory.newMessage(message), throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, Object message, Throwable throwable) {
/* 1994 */     logMessageSafely(fqcn, level, marker, this.messageFactory.newMessage(message), throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, MessageSupplier messageSupplier, Throwable throwable) {
/* 1999 */     Message message = LambdaUtil.get(messageSupplier);
/*      */     
/* 2001 */     Throwable effectiveThrowable = (throwable == null && message != null) ? message.getThrowable() : throwable;
/*      */     
/* 2003 */     logMessageSafely(fqcn, level, marker, message, effectiveThrowable);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, Supplier<?> messageSupplier, Throwable throwable) {
/* 2008 */     Message message = LambdaUtil.getMessage(messageSupplier, (MessageFactory)this.messageFactory);
/*      */     
/* 2010 */     Throwable effectiveThrowable = (throwable == null && message != null) ? message.getThrowable() : throwable;
/*      */     
/* 2012 */     logMessageSafely(fqcn, level, marker, message, effectiveThrowable);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, String message, Throwable throwable) {
/* 2017 */     logMessageSafely(fqcn, level, marker, this.messageFactory.newMessage(message), throwable);
/*      */   }
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, String message) {
/* 2021 */     Message msg = this.messageFactory.newMessage(message);
/* 2022 */     logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
/*      */   }
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, String message, Object... params) {
/* 2027 */     Message msg = this.messageFactory.newMessage(message, params);
/* 2028 */     logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
/*      */   }
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, String message, Object p0) {
/* 2033 */     Message msg = this.messageFactory.newMessage(message, p0);
/* 2034 */     logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
/*      */   }
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, String message, Object p0, Object p1) {
/* 2039 */     Message msg = this.messageFactory.newMessage(message, p0, p1);
/* 2040 */     logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
/*      */   }
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
/* 2045 */     Message msg = this.messageFactory.newMessage(message, p0, p1, p2);
/* 2046 */     logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
/*      */   }
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
/* 2051 */     Message msg = this.messageFactory.newMessage(message, p0, p1, p2, p3);
/* 2052 */     logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
/*      */   }
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 2057 */     Message msg = this.messageFactory.newMessage(message, p0, p1, p2, p3, p4);
/* 2058 */     logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
/*      */   }
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 2063 */     Message msg = this.messageFactory.newMessage(message, p0, p1, p2, p3, p4, p5);
/* 2064 */     logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 2070 */     Message msg = this.messageFactory.newMessage(message, p0, p1, p2, p3, p4, p5, p6);
/* 2071 */     logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 2077 */     Message msg = this.messageFactory.newMessage(message, p0, p1, p2, p3, p4, p5, p6, p7);
/* 2078 */     logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 2084 */     Message msg = this.messageFactory.newMessage(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/* 2085 */     logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 2091 */     Message msg = this.messageFactory.newMessage(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/* 2092 */     logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
/*      */   }
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, String message, Supplier<?>... paramSuppliers) {
/* 2097 */     Message msg = this.messageFactory.newMessage(message, LambdaUtil.getAll((Supplier[])paramSuppliers));
/* 2098 */     logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void logMessage(Level level, Marker marker, String fqcn, StackTraceElement location, Message message, Throwable throwable) {
/*      */     try {
/* 2105 */       incrementRecursionDepth();
/* 2106 */       log(level, marker, fqcn, location, message, throwable);
/* 2107 */     } catch (Throwable ex) {
/* 2108 */       handleLogMessageException(ex, fqcn, message);
/*      */     } finally {
/* 2110 */       decrementRecursionDepth();
/* 2111 */       ReusableMessageFactory.release(message);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void log(Level level, Marker marker, String fqcn, StackTraceElement location, Message message, Throwable throwable) {
/* 2117 */     logMessage(fqcn, level, marker, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void printf(Level level, Marker marker, String format, Object... params) {
/* 2122 */     if (isEnabled(level, marker, format, params)) {
/* 2123 */       StringFormattedMessage stringFormattedMessage = new StringFormattedMessage(format, params);
/* 2124 */       logMessageSafely(FQCN, level, marker, (Message)stringFormattedMessage, stringFormattedMessage.getThrowable());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void printf(Level level, String format, Object... params) {
/* 2130 */     if (isEnabled(level, (Marker)null, format, params)) {
/* 2131 */       StringFormattedMessage stringFormattedMessage = new StringFormattedMessage(format, params);
/* 2132 */       logMessageSafely(FQCN, level, (Marker)null, (Message)stringFormattedMessage, stringFormattedMessage.getThrowable());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @PerformanceSensitive
/*      */   private void logMessageSafely(String fqcn, Level level, Marker marker, Message message, Throwable throwable) {
/*      */     try {
/* 2142 */       logMessageTrackRecursion(fqcn, level, marker, message, throwable);
/*      */     } finally {
/*      */       
/* 2145 */       ReusableMessageFactory.release(message);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @PerformanceSensitive
/*      */   private void logMessageTrackRecursion(String fqcn, Level level, Marker marker, Message message, Throwable throwable) {
/*      */     try {
/* 2158 */       incrementRecursionDepth();
/* 2159 */       tryLogMessage(fqcn, getLocation(fqcn), level, marker, message, throwable);
/*      */     } finally {
/* 2161 */       decrementRecursionDepth();
/*      */     } 
/*      */   }
/*      */   
/*      */   private static int[] getRecursionDepthHolder() {
/* 2166 */     int[] result = recursionDepthHolder.get();
/* 2167 */     if (result == null) {
/* 2168 */       result = new int[1];
/* 2169 */       recursionDepthHolder.set(result);
/*      */     } 
/* 2171 */     return result;
/*      */   }
/*      */   
/*      */   private static void incrementRecursionDepth() {
/* 2175 */     getRecursionDepthHolder()[0] = getRecursionDepthHolder()[0] + 1;
/*      */   }
/*      */   
/*      */   private static void decrementRecursionDepth() {
/* 2179 */     int newDepth = getRecursionDepthHolder()[0] = getRecursionDepthHolder()[0] - 1;
/* 2180 */     if (newDepth < 0) {
/* 2181 */       throw new IllegalStateException("Recursion depth became negative: " + newDepth);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int getRecursionDepth() {
/* 2192 */     return getRecursionDepthHolder()[0];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @PerformanceSensitive
/*      */   private void tryLogMessage(String fqcn, StackTraceElement location, Level level, Marker marker, Message message, Throwable throwable) {
/*      */     try {
/* 2205 */       log(level, marker, fqcn, location, message, throwable);
/* 2206 */     } catch (Throwable t) {
/*      */       
/* 2208 */       handleLogMessageException(t, fqcn, message);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @PerformanceSensitive
/*      */   private StackTraceElement getLocation(String fqcn) {
/* 2216 */     return requiresLocation() ? StackLocatorUtil.calcLocation(fqcn) : null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void handleLogMessageException(Throwable throwable, String fqcn, Message message) {
/* 2222 */     if (throwable instanceof LoggingException) {
/* 2223 */       throw (LoggingException)throwable;
/*      */     }
/* 2225 */     StatusLogger.getLogger().warn("{} caught {} logging {}: {}", fqcn, throwable
/*      */         
/* 2227 */         .getClass().getName(), message
/* 2228 */         .getClass().getSimpleName(), message
/* 2229 */         .getFormat(), throwable);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T extends Throwable> T throwing(T throwable) {
/* 2235 */     return throwing(FQCN, Level.ERROR, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public <T extends Throwable> T throwing(Level level, T throwable) {
/* 2240 */     return throwing(FQCN, level, throwable);
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
/*      */   protected <T extends Throwable> T throwing(String fqcn, Level level, T throwable) {
/* 2253 */     if (isEnabled(level, THROWING_MARKER, (Object)null, (Throwable)null)) {
/* 2254 */       logMessageSafely(fqcn, level, THROWING_MARKER, throwingMsg((Throwable)throwable), (Throwable)throwable);
/*      */     }
/* 2256 */     return throwable;
/*      */   }
/*      */   
/*      */   protected Message throwingMsg(Throwable throwable) {
/* 2260 */     return this.messageFactory.newMessage("Throwing");
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, Message message) {
/* 2265 */     logIfEnabled(FQCN, Level.TRACE, marker, message, (message != null) ? message.getThrowable() : null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, Message message, Throwable throwable) {
/* 2270 */     logIfEnabled(FQCN, Level.TRACE, marker, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, CharSequence message) {
/* 2275 */     logIfEnabled(FQCN, Level.TRACE, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, CharSequence message, Throwable throwable) {
/* 2280 */     logIfEnabled(FQCN, Level.TRACE, marker, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, Object message) {
/* 2285 */     logIfEnabled(FQCN, Level.TRACE, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, Object message, Throwable throwable) {
/* 2290 */     logIfEnabled(FQCN, Level.TRACE, marker, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, String message) {
/* 2295 */     logIfEnabled(FQCN, Level.TRACE, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, String message, Object... params) {
/* 2300 */     logIfEnabled(FQCN, Level.TRACE, marker, message, params);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, String message, Throwable throwable) {
/* 2305 */     logIfEnabled(FQCN, Level.TRACE, marker, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Message message) {
/* 2310 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, (message != null) ? message.getThrowable() : null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Message message, Throwable throwable) {
/* 2315 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(CharSequence message) {
/* 2320 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(CharSequence message, Throwable throwable) {
/* 2325 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Object message) {
/* 2330 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Object message, Throwable throwable) {
/* 2335 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(String message) {
/* 2340 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(String message, Object... params) {
/* 2345 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, params);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(String message, Throwable throwable) {
/* 2350 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Supplier<?> messageSupplier) {
/* 2355 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, messageSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Supplier<?> messageSupplier, Throwable throwable) {
/* 2360 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, messageSupplier, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, Supplier<?> messageSupplier) {
/* 2365 */     logIfEnabled(FQCN, Level.TRACE, marker, messageSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, String message, Supplier<?>... paramSuppliers) {
/* 2370 */     logIfEnabled(FQCN, Level.TRACE, marker, message, paramSuppliers);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, Supplier<?> messageSupplier, Throwable throwable) {
/* 2375 */     logIfEnabled(FQCN, Level.TRACE, marker, messageSupplier, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(String message, Supplier<?>... paramSuppliers) {
/* 2380 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, paramSuppliers);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, MessageSupplier messageSupplier) {
/* 2385 */     logIfEnabled(FQCN, Level.TRACE, marker, messageSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, MessageSupplier messageSupplier, Throwable throwable) {
/* 2390 */     logIfEnabled(FQCN, Level.TRACE, marker, messageSupplier, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(MessageSupplier messageSupplier) {
/* 2395 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, messageSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(MessageSupplier messageSupplier, Throwable throwable) {
/* 2400 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, messageSupplier, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, String message, Object p0) {
/* 2405 */     logIfEnabled(FQCN, Level.TRACE, marker, message, p0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, String message, Object p0, Object p1) {
/* 2410 */     logIfEnabled(FQCN, Level.TRACE, marker, message, p0, p1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, String message, Object p0, Object p1, Object p2) {
/* 2415 */     logIfEnabled(FQCN, Level.TRACE, marker, message, p0, p1, p2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
/* 2421 */     logIfEnabled(FQCN, Level.TRACE, marker, message, p0, p1, p2, p3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 2427 */     logIfEnabled(FQCN, Level.TRACE, marker, message, p0, p1, p2, p3, p4);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 2433 */     logIfEnabled(FQCN, Level.TRACE, marker, message, p0, p1, p2, p3, p4, p5);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 2439 */     logIfEnabled(FQCN, Level.TRACE, marker, message, p0, p1, p2, p3, p4, p5, p6);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 2445 */     logIfEnabled(FQCN, Level.TRACE, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 2452 */     logIfEnabled(FQCN, Level.TRACE, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 2459 */     logIfEnabled(FQCN, Level.TRACE, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(String message, Object p0) {
/* 2464 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, p0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(String message, Object p0, Object p1) {
/* 2469 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, p0, p1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(String message, Object p0, Object p1, Object p2) {
/* 2474 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, p0, p1, p2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(String message, Object p0, Object p1, Object p2, Object p3) {
/* 2479 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, p0, p1, p2, p3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 2485 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, p0, p1, p2, p3, p4);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 2491 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, p0, p1, p2, p3, p4, p5);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 2497 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 2503 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 2509 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 2515 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/*      */   }
/*      */ 
/*      */   
/*      */   public EntryMessage traceEntry() {
/* 2520 */     return enter(FQCN, (String)null, (Object[])null);
/*      */   }
/*      */ 
/*      */   
/*      */   public EntryMessage traceEntry(String format, Object... params) {
/* 2525 */     return enter(FQCN, format, params);
/*      */   }
/*      */ 
/*      */   
/*      */   public EntryMessage traceEntry(Supplier<?>... paramSuppliers) {
/* 2530 */     return enter(FQCN, (String)null, paramSuppliers);
/*      */   }
/*      */ 
/*      */   
/*      */   public EntryMessage traceEntry(String format, Supplier<?>... paramSuppliers) {
/* 2535 */     return enter(FQCN, format, paramSuppliers);
/*      */   }
/*      */ 
/*      */   
/*      */   public EntryMessage traceEntry(Message message) {
/* 2540 */     return enter(FQCN, message);
/*      */   }
/*      */ 
/*      */   
/*      */   public void traceExit() {
/* 2545 */     exit(FQCN, (String)null, (Object)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public <R> R traceExit(R result) {
/* 2550 */     return exit(FQCN, (String)null, result);
/*      */   }
/*      */ 
/*      */   
/*      */   public <R> R traceExit(String format, R result) {
/* 2555 */     return exit(FQCN, format, result);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void traceExit(EntryMessage message) {
/* 2561 */     if (message != null && isEnabled(Level.TRACE, EXIT_MARKER, (Message)message, (Throwable)null)) {
/* 2562 */       logMessageSafely(FQCN, Level.TRACE, EXIT_MARKER, (Message)this.flowMessageFactory.newExitMessage(message), (Throwable)null);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <R> R traceExit(EntryMessage message, R result) {
/* 2569 */     if (message != null && isEnabled(Level.TRACE, EXIT_MARKER, (Message)message, (Throwable)null)) {
/* 2570 */       logMessageSafely(FQCN, Level.TRACE, EXIT_MARKER, (Message)this.flowMessageFactory.newExitMessage(result, message), (Throwable)null);
/*      */     }
/* 2572 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <R> R traceExit(Message message, R result) {
/* 2578 */     if (message != null && isEnabled(Level.TRACE, EXIT_MARKER, message, (Throwable)null)) {
/* 2579 */       logMessageSafely(FQCN, Level.TRACE, EXIT_MARKER, (Message)this.flowMessageFactory.newExitMessage(result, message), (Throwable)null);
/*      */     }
/* 2581 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, Message message) {
/* 2586 */     logIfEnabled(FQCN, Level.WARN, marker, message, (message != null) ? message.getThrowable() : null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, Message message, Throwable throwable) {
/* 2591 */     logIfEnabled(FQCN, Level.WARN, marker, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, CharSequence message) {
/* 2596 */     logIfEnabled(FQCN, Level.WARN, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, CharSequence message, Throwable throwable) {
/* 2601 */     logIfEnabled(FQCN, Level.WARN, marker, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, Object message) {
/* 2606 */     logIfEnabled(FQCN, Level.WARN, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, Object message, Throwable throwable) {
/* 2611 */     logIfEnabled(FQCN, Level.WARN, marker, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, String message) {
/* 2616 */     logIfEnabled(FQCN, Level.WARN, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, String message, Object... params) {
/* 2621 */     logIfEnabled(FQCN, Level.WARN, marker, message, params);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, String message, Throwable throwable) {
/* 2626 */     logIfEnabled(FQCN, Level.WARN, marker, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Message message) {
/* 2631 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, (message != null) ? message.getThrowable() : null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Message message, Throwable throwable) {
/* 2636 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(CharSequence message) {
/* 2641 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(CharSequence message, Throwable throwable) {
/* 2646 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Object message) {
/* 2651 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Object message, Throwable throwable) {
/* 2656 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(String message) {
/* 2661 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(String message, Object... params) {
/* 2666 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, params);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(String message, Throwable throwable) {
/* 2671 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Supplier<?> messageSupplier) {
/* 2676 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, messageSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Supplier<?> messageSupplier, Throwable throwable) {
/* 2681 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, messageSupplier, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, Supplier<?> messageSupplier) {
/* 2686 */     logIfEnabled(FQCN, Level.WARN, marker, messageSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, String message, Supplier<?>... paramSuppliers) {
/* 2691 */     logIfEnabled(FQCN, Level.WARN, marker, message, paramSuppliers);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, Supplier<?> messageSupplier, Throwable throwable) {
/* 2696 */     logIfEnabled(FQCN, Level.WARN, marker, messageSupplier, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(String message, Supplier<?>... paramSuppliers) {
/* 2701 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, paramSuppliers);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, MessageSupplier messageSupplier) {
/* 2706 */     logIfEnabled(FQCN, Level.WARN, marker, messageSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, MessageSupplier messageSupplier, Throwable throwable) {
/* 2711 */     logIfEnabled(FQCN, Level.WARN, marker, messageSupplier, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(MessageSupplier messageSupplier) {
/* 2716 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, messageSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(MessageSupplier messageSupplier, Throwable throwable) {
/* 2721 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, messageSupplier, throwable);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, String message, Object p0) {
/* 2726 */     logIfEnabled(FQCN, Level.WARN, marker, message, p0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, String message, Object p0, Object p1) {
/* 2731 */     logIfEnabled(FQCN, Level.WARN, marker, message, p0, p1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, String message, Object p0, Object p1, Object p2) {
/* 2736 */     logIfEnabled(FQCN, Level.WARN, marker, message, p0, p1, p2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
/* 2742 */     logIfEnabled(FQCN, Level.WARN, marker, message, p0, p1, p2, p3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 2748 */     logIfEnabled(FQCN, Level.WARN, marker, message, p0, p1, p2, p3, p4);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 2754 */     logIfEnabled(FQCN, Level.WARN, marker, message, p0, p1, p2, p3, p4, p5);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 2760 */     logIfEnabled(FQCN, Level.WARN, marker, message, p0, p1, p2, p3, p4, p5, p6);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 2766 */     logIfEnabled(FQCN, Level.WARN, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 2772 */     logIfEnabled(FQCN, Level.WARN, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 2779 */     logIfEnabled(FQCN, Level.WARN, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(String message, Object p0) {
/* 2784 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, p0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(String message, Object p0, Object p1) {
/* 2789 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, p0, p1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(String message, Object p0, Object p1, Object p2) {
/* 2794 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, p0, p1, p2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(String message, Object p0, Object p1, Object p2, Object p3) {
/* 2799 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, p0, p1, p2, p3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 2805 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, p0, p1, p2, p3, p4);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 2811 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, p0, p1, p2, p3, p4, p5);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 2817 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 2823 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 2829 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 2836 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/*      */   }
/*      */   
/*      */   protected boolean requiresLocation() {
/* 2840 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public LogBuilder atTrace() {
/* 2850 */     return atLevel(Level.TRACE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public LogBuilder atDebug() {
/* 2859 */     return atLevel(Level.DEBUG);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public LogBuilder atInfo() {
/* 2868 */     return atLevel(Level.INFO);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public LogBuilder atWarn() {
/* 2877 */     return atLevel(Level.WARN);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public LogBuilder atError() {
/* 2886 */     return atLevel(Level.ERROR);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public LogBuilder atFatal() {
/* 2895 */     return atLevel(Level.FATAL);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public LogBuilder always() {
/* 2904 */     DefaultLogBuilder builder = this.logBuilder.get();
/* 2905 */     if (builder.isInUse()) {
/* 2906 */       return (LogBuilder)new DefaultLogBuilder(this);
/*      */     }
/* 2908 */     return builder.reset(Level.OFF);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public LogBuilder atLevel(Level level) {
/* 2917 */     if (isEnabled(level)) {
/* 2918 */       return getLogBuilder(level).reset(level);
/*      */     }
/* 2920 */     return LogBuilder.NOOP;
/*      */   }
/*      */ 
/*      */   
/*      */   private DefaultLogBuilder getLogBuilder(Level level) {
/* 2925 */     DefaultLogBuilder builder = this.logBuilder.get();
/* 2926 */     return (Constants.ENABLE_THREADLOCALS && !builder.isInUse()) ? builder : new DefaultLogBuilder(this, level);
/*      */   }
/*      */   
/*      */   private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
/* 2930 */     s.defaultReadObject();
/*      */     try {
/* 2932 */       Field f = getClass().getDeclaredField("logBuilder");
/* 2933 */       f.setAccessible(true);
/* 2934 */       f.set(this, new LocalLogBuilder(this));
/* 2935 */     } catch (NoSuchFieldException|IllegalAccessException ex) {
/* 2936 */       StatusLogger.getLogger().warn("Unable to initialize LogBuilder");
/*      */     } 
/*      */   }
/*      */   
/*      */   private class LocalLogBuilder
/*      */     extends ThreadLocal<DefaultLogBuilder> {
/*      */     LocalLogBuilder(AbstractLogger logger) {
/* 2943 */       this.logger = logger;
/*      */     }
/*      */     private AbstractLogger logger;
/*      */     
/*      */     protected DefaultLogBuilder initialValue() {
/* 2948 */       return new DefaultLogBuilder(this.logger);
/*      */     }
/*      */   }
/*      */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\spi\AbstractLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */