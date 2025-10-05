/*    */ package META-INF.versions.9.org.apache.logging.log4j.util.internal;
/*    */ 
/*    */ import java.io.ObjectInputFilter;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
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
/*    */ public class DefaultObjectInputFilter
/*    */   implements ObjectInputFilter
/*    */ {
/* 26 */   private static final List<String> REQUIRED_JAVA_CLASSES = Arrays.asList(new String[] { "java.math.BigDecimal", "java.math.BigInteger", "java.rmi.MarshalledObject", "[B" });
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   private static final List<String> REQUIRED_JAVA_PACKAGES = Arrays.asList(new String[] { "java.lang.", "java.time", "java.util.", "org.apache.logging.log4j.", "[Lorg.apache.logging.log4j." });
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private final ObjectInputFilter delegate;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultObjectInputFilter() {
/* 45 */     this.delegate = null;
/*    */   }
/*    */   
/*    */   public DefaultObjectInputFilter(ObjectInputFilter filter) {
/* 49 */     this.delegate = filter;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static org.apache.logging.log4j.util.internal.DefaultObjectInputFilter newInstance(ObjectInputFilter filter) {
/* 58 */     return new org.apache.logging.log4j.util.internal.DefaultObjectInputFilter(filter);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ObjectInputFilter.Status checkInput(ObjectInputFilter.FilterInfo filterInfo) {
/* 64 */     ObjectInputFilter.Status status = null;
/* 65 */     if (this.delegate != null) {
/* 66 */       status = this.delegate.checkInput(filterInfo);
/* 67 */       if (status != ObjectInputFilter.Status.UNDECIDED) {
/* 68 */         return status;
/*    */       }
/*    */     } 
/* 71 */     ObjectInputFilter serialFilter = ObjectInputFilter.Config.getSerialFilter();
/* 72 */     if (serialFilter != null) {
/* 73 */       status = serialFilter.checkInput(filterInfo);
/* 74 */       if (status != ObjectInputFilter.Status.UNDECIDED)
/*    */       {
/* 76 */         return status;
/*    */       }
/*    */     } 
/* 79 */     if (filterInfo.serialClass() != null) {
/* 80 */       String name = filterInfo.serialClass().getName();
/* 81 */       if (isAllowedByDefault(name) || isRequiredPackage(name)) {
/* 82 */         return ObjectInputFilter.Status.ALLOWED;
/*    */       }
/*    */     } 
/* 85 */     return ObjectInputFilter.Status.REJECTED;
/*    */   }
/*    */   
/*    */   private static boolean isAllowedByDefault(String name) {
/* 89 */     return (isRequiredPackage(name) || REQUIRED_JAVA_CLASSES.contains(name));
/*    */   }
/*    */   
/*    */   private static boolean isRequiredPackage(String name) {
/* 93 */     for (String packageName : REQUIRED_JAVA_PACKAGES) {
/* 94 */       if (name.startsWith(packageName)) {
/* 95 */         return true;
/*    */       }
/*    */     } 
/* 98 */     return false;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\META-INF\versions\9\org\apache\logging\log4\\util\internal\DefaultObjectInputFilter.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */