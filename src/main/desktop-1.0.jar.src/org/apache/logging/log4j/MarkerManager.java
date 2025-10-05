/*     */ package org.apache.logging.log4j;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
/*     */ import org.apache.logging.log4j.util.StringBuilderFormattable;
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
/*     */ public final class MarkerManager
/*     */ {
/*  31 */   private static final ConcurrentMap<String, Marker> MARKERS = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void clear() {
/*  41 */     MARKERS.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean exists(String key) {
/*  52 */     return MARKERS.containsKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Marker getMarker(String name) {
/*  63 */     Marker result = MARKERS.get(name);
/*  64 */     if (result == null) {
/*  65 */       MARKERS.putIfAbsent(name, new Log4jMarker(name));
/*  66 */       result = MARKERS.get(name);
/*     */     } 
/*  68 */     return result;
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
/*     */   @Deprecated
/*     */   public static Marker getMarker(String name, String parent) {
/*  82 */     Marker parentMarker = MARKERS.get(parent);
/*  83 */     if (parentMarker == null) {
/*  84 */       throw new IllegalArgumentException("Parent Marker " + parent + " has not been defined");
/*     */     }
/*  86 */     return getMarker(name, parentMarker);
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
/*     */   @Deprecated
/*     */   public static Marker getMarker(String name, Marker parent) {
/* 100 */     return getMarker(name).addParents(new Marker[] { parent });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Log4jMarker
/*     */     implements Marker, StringBuilderFormattable
/*     */   {
/*     */     private static final long serialVersionUID = 100L;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String name;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private volatile Marker[] parents;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Log4jMarker() {
/* 127 */       this.name = null;
/* 128 */       this.parents = null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Log4jMarker(String name) {
/* 140 */       MarkerManager.requireNonNull(name, "Marker name cannot be null.");
/* 141 */       this.name = name;
/* 142 */       this.parents = null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public synchronized Marker addParents(Marker... parentMarkers) {
/* 149 */       MarkerManager.requireNonNull(parentMarkers, "A parent marker must be specified");
/*     */ 
/*     */       
/* 152 */       Marker[] localParents = this.parents;
/*     */       
/* 154 */       int count = 0;
/* 155 */       int size = parentMarkers.length;
/* 156 */       if (localParents != null) {
/* 157 */         for (Marker parent : parentMarkers) {
/* 158 */           if (!contains(parent, localParents) && !parent.isInstanceOf(this)) {
/* 159 */             count++;
/*     */           }
/*     */         } 
/* 162 */         if (count == 0) {
/* 163 */           return this;
/*     */         }
/* 165 */         size = localParents.length + count;
/*     */       } 
/* 167 */       Marker[] markers = new Marker[size];
/* 168 */       if (localParents != null)
/*     */       {
/*     */         
/* 171 */         System.arraycopy(localParents, 0, markers, 0, localParents.length);
/*     */       }
/* 173 */       int index = (localParents == null) ? 0 : localParents.length;
/* 174 */       for (Marker parent : parentMarkers) {
/* 175 */         if (localParents == null || (!contains(parent, localParents) && !parent.isInstanceOf(this))) {
/* 176 */           markers[index++] = parent;
/*     */         }
/*     */       } 
/* 179 */       this.parents = markers;
/* 180 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public synchronized boolean remove(Marker parent) {
/* 185 */       MarkerManager.requireNonNull(parent, "A parent marker must be specified");
/* 186 */       Marker[] localParents = this.parents;
/* 187 */       if (localParents == null) {
/* 188 */         return false;
/*     */       }
/* 190 */       int localParentsLength = localParents.length;
/* 191 */       if (localParentsLength == 1) {
/* 192 */         if (localParents[0].equals(parent)) {
/* 193 */           this.parents = null;
/* 194 */           return true;
/*     */         } 
/* 196 */         return false;
/*     */       } 
/* 198 */       int index = 0;
/* 199 */       Marker[] markers = new Marker[localParentsLength - 1];
/*     */       
/* 201 */       for (int i = 0; i < localParentsLength; i++) {
/* 202 */         Marker marker = localParents[i];
/* 203 */         if (!marker.equals(parent)) {
/* 204 */           if (index == localParentsLength - 1)
/*     */           {
/* 206 */             return false;
/*     */           }
/* 208 */           markers[index++] = marker;
/*     */         } 
/*     */       } 
/* 211 */       this.parents = markers;
/* 212 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public Marker setParents(Marker... markers) {
/* 217 */       if (markers == null || markers.length == 0) {
/* 218 */         this.parents = null;
/*     */       } else {
/* 220 */         Marker[] array = new Marker[markers.length];
/* 221 */         System.arraycopy(markers, 0, array, 0, markers.length);
/* 222 */         this.parents = array;
/*     */       } 
/* 224 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getName() {
/* 229 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public Marker[] getParents() {
/* 234 */       Marker[] parentsSnapshot = this.parents;
/* 235 */       if (parentsSnapshot == null) {
/* 236 */         return null;
/*     */       }
/* 238 */       return Arrays.<Marker>copyOf(parentsSnapshot, parentsSnapshot.length);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasParents() {
/* 243 */       return (this.parents != null);
/*     */     }
/*     */ 
/*     */     
/*     */     @PerformanceSensitive({"allocation", "unrolled"})
/*     */     public boolean isInstanceOf(Marker marker) {
/* 249 */       MarkerManager.requireNonNull(marker, "A marker parameter is required");
/* 250 */       if (this == marker) {
/* 251 */         return true;
/*     */       }
/* 253 */       Marker[] localParents = this.parents;
/* 254 */       if (localParents != null) {
/*     */         
/* 256 */         int localParentsLength = localParents.length;
/* 257 */         if (localParentsLength == 1) {
/* 258 */           return checkParent(localParents[0], marker);
/*     */         }
/* 260 */         if (localParentsLength == 2) {
/* 261 */           return (checkParent(localParents[0], marker) || checkParent(localParents[1], marker));
/*     */         }
/*     */         
/* 264 */         for (int i = 0; i < localParentsLength; i++) {
/* 265 */           Marker localParent = localParents[i];
/* 266 */           if (checkParent(localParent, marker)) {
/* 267 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/* 271 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     @PerformanceSensitive({"allocation", "unrolled"})
/*     */     public boolean isInstanceOf(String markerName) {
/* 277 */       MarkerManager.requireNonNull(markerName, "A marker name is required");
/* 278 */       if (markerName.equals(getName())) {
/* 279 */         return true;
/*     */       }
/*     */       
/* 282 */       Marker marker = (Marker)MarkerManager.MARKERS.get(markerName);
/* 283 */       if (marker == null) {
/* 284 */         return false;
/*     */       }
/* 286 */       Marker[] localParents = this.parents;
/* 287 */       if (localParents != null) {
/* 288 */         int localParentsLength = localParents.length;
/* 289 */         if (localParentsLength == 1) {
/* 290 */           return checkParent(localParents[0], marker);
/*     */         }
/* 292 */         if (localParentsLength == 2) {
/* 293 */           return (checkParent(localParents[0], marker) || checkParent(localParents[1], marker));
/*     */         }
/*     */         
/* 296 */         for (int i = 0; i < localParentsLength; i++) {
/* 297 */           Marker localParent = localParents[i];
/* 298 */           if (checkParent(localParent, marker)) {
/* 299 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 304 */       return false;
/*     */     }
/*     */     
/*     */     @PerformanceSensitive({"allocation", "unrolled"})
/*     */     private static boolean checkParent(Marker parent, Marker marker) {
/* 309 */       if (parent == marker) {
/* 310 */         return true;
/*     */       }
/*     */       
/* 313 */       Marker[] localParents = (parent instanceof Log4jMarker) ? ((Log4jMarker)parent).parents : parent.getParents();
/* 314 */       if (localParents != null) {
/* 315 */         int localParentsLength = localParents.length;
/* 316 */         if (localParentsLength == 1) {
/* 317 */           return checkParent(localParents[0], marker);
/*     */         }
/* 319 */         if (localParentsLength == 2) {
/* 320 */           return (checkParent(localParents[0], marker) || checkParent(localParents[1], marker));
/*     */         }
/*     */         
/* 323 */         for (int i = 0; i < localParentsLength; i++) {
/* 324 */           Marker localParent = localParents[i];
/* 325 */           if (checkParent(localParent, marker)) {
/* 326 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/* 330 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @PerformanceSensitive({"allocation"})
/*     */     private static boolean contains(Marker parent, Marker... localParents) {
/* 340 */       for (int i = 0, localParentsLength = localParents.length; i < localParentsLength; i++) {
/* 341 */         Marker marker = localParents[i];
/* 342 */         if (marker == parent) {
/* 343 */           return true;
/*     */         }
/*     */       } 
/* 346 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 351 */       if (this == o) {
/* 352 */         return true;
/*     */       }
/* 354 */       if (o == null || !(o instanceof Marker)) {
/* 355 */         return false;
/*     */       }
/* 357 */       Marker marker = (Marker)o;
/* 358 */       return this.name.equals(marker.getName());
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 363 */       return this.name.hashCode();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 369 */       StringBuilder sb = new StringBuilder();
/* 370 */       formatTo(sb);
/* 371 */       return sb.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public void formatTo(StringBuilder sb) {
/* 376 */       sb.append(this.name);
/* 377 */       Marker[] localParents = this.parents;
/* 378 */       if (localParents != null) {
/* 379 */         addParentInfo(sb, localParents);
/*     */       }
/*     */     }
/*     */     
/*     */     @PerformanceSensitive({"allocation"})
/*     */     private static void addParentInfo(StringBuilder sb, Marker... parents) {
/* 385 */       sb.append("[ ");
/* 386 */       boolean first = true;
/*     */       
/* 388 */       for (int i = 0, parentsLength = parents.length; i < parentsLength; i++) {
/* 389 */         Marker marker = parents[i];
/* 390 */         if (!first) {
/* 391 */           sb.append(", ");
/*     */         }
/* 393 */         first = false;
/* 394 */         sb.append(marker.getName());
/* 395 */         Marker[] p = (marker instanceof Log4jMarker) ? ((Log4jMarker)marker).parents : marker.getParents();
/* 396 */         if (p != null) {
/* 397 */           addParentInfo(sb, p);
/*     */         }
/*     */       } 
/* 400 */       sb.append(" ]");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static void requireNonNull(Object obj, String message) {
/* 406 */     if (obj == null)
/* 407 */       throw new IllegalArgumentException(message); 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\MarkerManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */