/*     */ package org.apache.logging.log4j.core.config.plugins.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.JarURLConnection;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.net.URLDecoder;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.JarInputStream;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.osgi.framework.FrameworkUtil;
/*     */ import org.osgi.framework.wiring.BundleWiring;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResolverUtil
/*     */ {
/*  85 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */   
/*     */   private static final String VFSZIP = "vfszip";
/*     */   
/*     */   private static final String VFS = "vfs";
/*     */   
/*     */   private static final String JAR = "jar";
/*     */   
/*     */   private static final String BUNDLE_RESOURCE = "bundleresource";
/*     */   
/*  96 */   private final Set<Class<?>> classMatches = new HashSet<>();
/*     */ 
/*     */   
/*  99 */   private final Set<URI> resourceMatches = new HashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ClassLoader classloader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Class<?>> getClasses() {
/* 114 */     return this.classMatches;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<URI> getResources() {
/* 123 */     return this.resourceMatches;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassLoader getClassLoader() {
/* 133 */     return (this.classloader != null) ? this.classloader : (this.classloader = Loader.getClassLoader(ResolverUtil.class, null));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClassLoader(ClassLoader aClassloader) {
/* 144 */     this.classloader = aClassloader;
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
/*     */   public void find(Test test, String... packageNames) {
/* 157 */     if (packageNames == null) {
/*     */       return;
/*     */     }
/*     */     
/* 161 */     for (String pkg : packageNames) {
/* 162 */       findInPackage(test, pkg);
/*     */     }
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
/*     */   public void findInPackage(Test test, String packageName) {
/*     */     Enumeration<URL> urls;
/* 177 */     packageName = packageName.replace('.', '/');
/* 178 */     ClassLoader loader = getClassLoader();
/*     */ 
/*     */     
/*     */     try {
/* 182 */       urls = loader.getResources(packageName);
/* 183 */     } catch (IOException ioe) {
/* 184 */       LOGGER.warn("Could not read package: {}", packageName, ioe);
/*     */       
/*     */       return;
/*     */     } 
/* 188 */     while (urls.hasMoreElements()) {
/*     */       try {
/* 190 */         URL url = urls.nextElement();
/* 191 */         String urlPath = extractPath(url);
/*     */         
/* 193 */         LOGGER.info("Scanning for classes in '{}' matching criteria {}", urlPath, test);
/*     */         
/* 195 */         if ("vfszip".equals(url.getProtocol())) {
/* 196 */           String path = urlPath.substring(0, urlPath.length() - packageName.length() - 2);
/* 197 */           URL newURL = new URL(url.getProtocol(), url.getHost(), path);
/*     */           
/* 199 */           JarInputStream stream = new JarInputStream(newURL.openStream());
/*     */           try {
/* 201 */             loadImplementationsInJar(test, packageName, path, stream);
/*     */           } finally {
/* 203 */             close(stream, newURL);
/*     */           }  continue;
/* 205 */         }  if ("vfs".equals(url.getProtocol())) {
/* 206 */           String containerPath = urlPath.substring(1, urlPath.length() - packageName.length() - 2);
/* 207 */           File containerFile = new File(containerPath);
/* 208 */           if (containerFile.exists()) {
/* 209 */             if (containerFile.isDirectory()) {
/* 210 */               loadImplementationsInDirectory(test, packageName, new File(containerFile, packageName)); continue;
/*     */             } 
/* 212 */             loadImplementationsInJar(test, packageName, containerFile);
/*     */             
/*     */             continue;
/*     */           } 
/*     */           
/* 217 */           String path = urlPath.substring(0, urlPath.length() - packageName.length() - 2);
/* 218 */           URL newURL = new URL(url.getProtocol(), url.getHost(), path);
/*     */           
/* 220 */           try (InputStream is = newURL.openStream()) {
/*     */             JarInputStream jarStream;
/* 222 */             if (is instanceof JarInputStream) {
/* 223 */               jarStream = (JarInputStream)is;
/*     */             } else {
/* 225 */               jarStream = new JarInputStream(is);
/*     */             } 
/* 227 */             loadImplementationsInJar(test, packageName, path, jarStream);
/*     */           }  continue;
/*     */         } 
/* 230 */         if ("bundleresource".equals(url.getProtocol())) {
/* 231 */           loadImplementationsInBundle(test, packageName); continue;
/* 232 */         }  if ("jar".equals(url.getProtocol())) {
/* 233 */           loadImplementationsInJar(test, packageName, url); continue;
/*     */         } 
/* 235 */         File file = new File(urlPath);
/* 236 */         if (file.isDirectory()) {
/* 237 */           loadImplementationsInDirectory(test, packageName, file); continue;
/*     */         } 
/* 239 */         loadImplementationsInJar(test, packageName, file);
/*     */       
/*     */       }
/* 242 */       catch (IOException|URISyntaxException ioe) {
/* 243 */         LOGGER.warn("Could not read entries", ioe);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   String extractPath(URL url) throws UnsupportedEncodingException, URISyntaxException {
/* 249 */     String urlPath = url.getPath();
/*     */ 
/*     */ 
/*     */     
/* 253 */     if (urlPath.startsWith("jar:")) {
/* 254 */       urlPath = urlPath.substring(4);
/*     */     }
/*     */     
/* 257 */     if (urlPath.startsWith("file:")) {
/* 258 */       urlPath = urlPath.substring(5);
/*     */     }
/*     */     
/* 261 */     int bangIndex = urlPath.indexOf('!');
/* 262 */     if (bangIndex > 0) {
/* 263 */       urlPath = urlPath.substring(0, bangIndex);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 268 */     String protocol = url.getProtocol();
/* 269 */     List<String> neverDecode = Arrays.asList(new String[] { "vfs", "vfszip", "bundleresource" });
/* 270 */     if (neverDecode.contains(protocol)) {
/* 271 */       return urlPath;
/*     */     }
/* 273 */     String cleanPath = (new URI(urlPath)).getPath();
/* 274 */     if ((new File(cleanPath)).exists())
/*     */     {
/* 276 */       return cleanPath;
/*     */     }
/* 278 */     return URLDecoder.decode(urlPath, StandardCharsets.UTF_8.name());
/*     */   }
/*     */   
/*     */   private void loadImplementationsInBundle(Test test, String packageName) {
/* 282 */     BundleWiring wiring = (BundleWiring)FrameworkUtil.getBundle(ResolverUtil.class).adapt(BundleWiring.class);
/* 283 */     Collection<String> list = wiring.listResources(packageName, "*.class", 1);
/*     */     
/* 285 */     for (String name : list) {
/* 286 */       addIfMatching(test, name);
/*     */     }
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
/*     */   private void loadImplementationsInDirectory(Test test, String parent, File location) {
/* 306 */     File[] files = location.listFiles();
/* 307 */     if (files == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 312 */     for (File file : files) {
/* 313 */       StringBuilder builder = new StringBuilder();
/* 314 */       builder.append(parent).append('/').append(file.getName());
/* 315 */       String packageOrClass = (parent == null) ? file.getName() : builder.toString();
/*     */       
/* 317 */       if (file.isDirectory()) {
/* 318 */         loadImplementationsInDirectory(test, packageOrClass, file);
/* 319 */       } else if (isTestApplicable(test, file.getName())) {
/* 320 */         addIfMatching(test, packageOrClass);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isTestApplicable(Test test, String path) {
/* 326 */     return (test.doesMatchResource() || (path.endsWith(".class") && test.doesMatchClass()));
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
/*     */   private void loadImplementationsInJar(Test test, String parent, URL url) {
/* 341 */     JarURLConnection connection = null;
/*     */     try {
/* 343 */       connection = (JarURLConnection)url.openConnection();
/* 344 */       if (connection != null) {
/* 345 */         try (JarFile jarFile = connection.getJarFile()) {
/* 346 */           Enumeration<JarEntry> entries = jarFile.entries();
/* 347 */           while (entries.hasMoreElements()) {
/* 348 */             JarEntry entry = entries.nextElement();
/* 349 */             String name = entry.getName();
/* 350 */             if (!entry.isDirectory() && name.startsWith(parent) && isTestApplicable(test, name)) {
/* 351 */               addIfMatching(test, name);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } else {
/* 356 */         LOGGER.error("Could not establish connection to {}", url.toString());
/*     */       } 
/* 358 */     } catch (IOException ex) {
/* 359 */       LOGGER.error("Could not search JAR file '{}' for classes matching criteria {}, file not found", url
/* 360 */           .toString(), test, ex);
/*     */     } 
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
/*     */   private void loadImplementationsInJar(Test test, String parent, File jarFile) {
/* 376 */     JarInputStream jarStream = null;
/*     */     try {
/* 378 */       jarStream = new JarInputStream(new FileInputStream(jarFile));
/* 379 */       loadImplementationsInJar(test, parent, jarFile.getPath(), jarStream);
/* 380 */     } catch (IOException ex) {
/* 381 */       LOGGER.error("Could not search JAR file '{}' for classes matching criteria {}, file not found", jarFile, test, ex);
/*     */     } finally {
/*     */       
/* 384 */       close(jarStream, jarFile);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void close(JarInputStream jarStream, Object source) {
/* 393 */     if (jarStream != null) {
/*     */       try {
/* 395 */         jarStream.close();
/* 396 */       } catch (IOException e) {
/* 397 */         LOGGER.error("Error closing JAR file stream for {}", source, e);
/*     */       } 
/*     */     }
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
/*     */   private void loadImplementationsInJar(Test test, String parent, String path, JarInputStream stream) {
/*     */     try {
/*     */       JarEntry entry;
/* 419 */       while ((entry = stream.getNextJarEntry()) != null) {
/* 420 */         String name = entry.getName();
/* 421 */         if (!entry.isDirectory() && name.startsWith(parent) && isTestApplicable(test, name)) {
/* 422 */           addIfMatching(test, name);
/*     */         }
/*     */       } 
/* 425 */     } catch (IOException ioe) {
/* 426 */       LOGGER.error("Could not search JAR file '{}' for classes matching criteria {} due to an IOException", path, test, ioe);
/*     */     } 
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
/*     */   protected void addIfMatching(Test test, String fqn) {
/*     */     try {
/* 442 */       ClassLoader loader = getClassLoader();
/* 443 */       if (test.doesMatchClass()) {
/* 444 */         String externalName = fqn.substring(0, fqn.indexOf('.')).replace('/', '.');
/* 445 */         if (LOGGER.isDebugEnabled()) {
/* 446 */           LOGGER.debug("Checking to see if class {} matches criteria {}", externalName, test);
/*     */         }
/*     */         
/* 449 */         Class<?> type = loader.loadClass(externalName);
/* 450 */         if (test.matches(type)) {
/* 451 */           this.classMatches.add(type);
/*     */         }
/*     */       } 
/* 454 */       if (test.doesMatchResource()) {
/* 455 */         URL url = loader.getResource(fqn);
/* 456 */         if (url == null) {
/* 457 */           url = loader.getResource(fqn.substring(1));
/*     */         }
/* 459 */         if (url != null && test.matches(url.toURI())) {
/* 460 */           this.resourceMatches.add(url.toURI());
/*     */         }
/*     */       } 
/* 463 */     } catch (Throwable t) {
/* 464 */       LOGGER.warn("Could not examine class {}", fqn, t);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static interface Test {
/*     */     boolean matches(Class<?> param1Class);
/*     */     
/*     */     boolean matches(URI param1URI);
/*     */     
/*     */     boolean doesMatchClass();
/*     */     
/*     */     boolean doesMatchResource();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\plugin\\util\ResolverUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */