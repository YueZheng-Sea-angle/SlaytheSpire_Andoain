/*     */ package META-INF.versions.9.org.apache.logging.log4j.util;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Stack;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.logging.log4j.util.PrivateSecurityManagerStackTraceUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StackLocator
/*     */ {
/*  31 */   private static final StackWalker walker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
/*     */   
/*  33 */   private static final StackWalker stackWalker = StackWalker.getInstance();
/*     */   
/*  35 */   private static final org.apache.logging.log4j.util.StackLocator INSTANCE = new org.apache.logging.log4j.util.StackLocator();
/*     */   
/*     */   public static org.apache.logging.log4j.util.StackLocator getInstance() {
/*  38 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getCallerClass(Class<?> sentinelClass, Predicate<Class<?>> callerPredicate) {
/*  45 */     if (sentinelClass == null) {
/*  46 */       throw new IllegalArgumentException("sentinelClass cannot be null");
/*     */     }
/*  48 */     if (callerPredicate == null) {
/*  49 */       throw new IllegalArgumentException("callerPredicate cannot be null");
/*     */     }
/*  51 */     return walker.<Class<?>>walk(s -> (Class)s.map(StackWalker.StackFrame::getDeclaringClass).dropWhile(()).dropWhile(()).findFirst().orElse(null));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getCallerClass(String fqcn) {
/*  61 */     return getCallerClass(fqcn, "");
/*     */   }
/*     */   
/*     */   public Class<?> getCallerClass(String fqcn, String pkg) {
/*  65 */     return ((Optional)walker.<Optional>walk(s -> s.dropWhile(()).dropWhile(()).dropWhile(()).findFirst()))
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  70 */       .map(StackWalker.StackFrame::getDeclaringClass)
/*  71 */       .orElse(null);
/*     */   }
/*     */   
/*     */   public Class<?> getCallerClass(Class<?> anchor) {
/*  75 */     return ((Optional)walker.<Optional>walk(s -> s.dropWhile(()).dropWhile(()).findFirst()))
/*     */       
/*  77 */       .map(StackWalker.StackFrame::getDeclaringClass).orElse(null);
/*     */   }
/*     */   
/*     */   public Class<?> getCallerClass(int depth) {
/*  81 */     return ((Optional)walker.<Optional>walk(s -> s.skip(depth).findFirst())).map(StackWalker.StackFrame::getDeclaringClass).orElse(null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Stack<Class<?>> getCurrentStackTrace() {
/*  86 */     if (PrivateSecurityManagerStackTraceUtil.isEnabled()) {
/*  87 */       return PrivateSecurityManagerStackTraceUtil.getCurrentStackTrace();
/*     */     }
/*  89 */     Stack<Class<?>> stack = new Stack<>();
/*  90 */     List<Class<?>> classes = walker.<List<Class<?>>>walk(s -> (List)s.map(()).collect(Collectors.toList()));
/*  91 */     stack.addAll(classes);
/*  92 */     return stack;
/*     */   }
/*     */   
/*     */   public StackTraceElement calcLocation(String fqcnOfLogger) {
/*  96 */     return ((Optional)stackWalker.<Optional>walk(s -> s.dropWhile(()).dropWhile(()).findFirst()))
/*     */ 
/*     */       
/*  99 */       .map(StackWalker.StackFrame::toStackTraceElement).orElse(null);
/*     */   }
/*     */   
/*     */   public StackTraceElement getStackTraceElement(int depth) {
/* 103 */     return ((Optional)stackWalker.<Optional>walk(s -> s.skip(depth).findFirst()))
/* 104 */       .map(StackWalker.StackFrame::toStackTraceElement).orElse(null);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\META-INF\versions\9\org\apache\logging\log4\\util\StackLocator.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */