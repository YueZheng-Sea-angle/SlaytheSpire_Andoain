/*     */ package org.lwjgl.opengl;
/*     */ 
/*     */ import java.nio.IntBuffer;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import org.lwjgl.BufferUtils;
/*     */ import org.lwjgl.LWJGLUtil;
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
/*     */ public final class ContextAttribs
/*     */ {
/*     */   public static final int CONTEXT_MAJOR_VERSION_ARB = 8337;
/*     */   public static final int CONTEXT_MINOR_VERSION_ARB = 8338;
/*     */   public static final int CONTEXT_PROFILE_MASK_ARB = 37158;
/*     */   public static final int CONTEXT_CORE_PROFILE_BIT_ARB = 1;
/*     */   public static final int CONTEXT_COMPATIBILITY_PROFILE_BIT_ARB = 2;
/*     */   public static final int CONTEXT_ES2_PROFILE_BIT_EXT = 4;
/*     */   public static final int CONTEXT_FLAGS_ARB = 8340;
/*     */   public static final int CONTEXT_DEBUG_BIT_ARB = 1;
/*     */   public static final int CONTEXT_FORWARD_COMPATIBLE_BIT_ARB = 2;
/*     */   public static final int CONTEXT_ROBUST_ACCESS_BIT_ARB = 4;
/*     */   public static final int CONTEXT_RESET_ISOLATION_BIT_ARB = 8;
/*     */   public static final int CONTEXT_RESET_NOTIFICATION_STRATEGY_ARB = 33366;
/*     */   public static final int NO_RESET_NOTIFICATION_ARB = 33377;
/*     */   public static final int LOSE_CONTEXT_ON_RESET_ARB = 33362;
/*     */   public static final int CONTEXT_RELEASE_BEHABIOR_ARB = 8343;
/*     */   public static final int CONTEXT_RELEASE_BEHAVIOR_NONE_ARB = 0;
/*     */   public static final int CONTEXT_RELEASE_BEHAVIOR_FLUSH_ARB = 8344;
/*     */   public static final int CONTEXT_LAYER_PLANE_ARB = 8339;
/*     */   private int majorVersion;
/*     */   private int minorVersion;
/*     */   private int profileMask;
/*     */   private int contextFlags;
/* 102 */   private int contextResetNotificationStrategy = 33377;
/* 103 */   private int contextReleaseBehavior = 8344;
/*     */ 
/*     */   
/*     */   private int layerPlane;
/*     */ 
/*     */ 
/*     */   
/*     */   public ContextAttribs() {
/* 111 */     this(1, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public ContextAttribs(int majorVersion, int minorVersion) {
/* 116 */     this(majorVersion, minorVersion, 0, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContextAttribs(int majorVersion, int minorVersion, int profileMask) {
/* 127 */     this(majorVersion, minorVersion, 0, profileMask);
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
/*     */   public ContextAttribs(int majorVersion, int minorVersion, int profileMask, int contextFlags) {
/* 139 */     if (majorVersion < 0 || 4 < majorVersion || minorVersion < 0 || (majorVersion == 4 && 5 < minorVersion) || (majorVersion == 3 && 3 < minorVersion) || (majorVersion == 2 && 1 < minorVersion) || (majorVersion == 1 && 5 < minorVersion))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 145 */       throw new IllegalArgumentException("Invalid OpenGL version specified: " + majorVersion + '.' + minorVersion);
/*     */     }
/* 147 */     if (LWJGLUtil.CHECKS) {
/* 148 */       if (1 < Integer.bitCount(profileMask) || 4 < profileMask) {
/* 149 */         throw new IllegalArgumentException("Invalid profile mask specified: " + Integer.toBinaryString(profileMask));
/*     */       }
/* 151 */       if (15 < contextFlags) {
/* 152 */         throw new IllegalArgumentException("Invalid context flags specified: " + Integer.toBinaryString(profileMask));
/*     */       }
/*     */     } 
/* 155 */     this.majorVersion = majorVersion;
/* 156 */     this.minorVersion = minorVersion;
/*     */     
/* 158 */     this.profileMask = profileMask;
/* 159 */     this.contextFlags = contextFlags;
/*     */   }
/*     */ 
/*     */   
/*     */   private ContextAttribs(ContextAttribs other) {
/* 164 */     this.majorVersion = other.majorVersion;
/* 165 */     this.minorVersion = other.minorVersion;
/*     */     
/* 167 */     this.profileMask = other.profileMask;
/* 168 */     this.contextFlags = other.contextFlags;
/*     */     
/* 170 */     this.contextResetNotificationStrategy = other.contextResetNotificationStrategy;
/* 171 */     this.contextReleaseBehavior = other.contextReleaseBehavior;
/*     */     
/* 173 */     this.layerPlane = other.layerPlane;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMajorVersion() {
/* 180 */     return this.majorVersion;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMinorVersion() {
/* 185 */     return this.minorVersion;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getProfileMask() {
/* 190 */     return this.profileMask;
/*     */   }
/*     */   
/*     */   private boolean hasMask(int mask) {
/* 194 */     return (this.profileMask == mask);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isProfileCore() {
/* 199 */     return hasMask(1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isProfileCompatibility() {
/* 204 */     return hasMask(2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isProfileES() {
/* 209 */     return hasMask(4);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getContextFlags() {
/* 214 */     return this.contextFlags;
/*     */   }
/*     */   
/*     */   private boolean hasFlag(int flag) {
/* 218 */     return ((this.contextFlags & flag) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDebug() {
/* 223 */     return hasFlag(1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isForwardCompatible() {
/* 228 */     return hasFlag(2);
/*     */   }
/*     */   
/*     */   public boolean isRobustAccess() {
/* 232 */     return hasFlag(4);
/*     */   }
/*     */   
/*     */   public boolean isContextResetIsolation() {
/* 236 */     return hasFlag(8);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getContextResetNotificationStrategy() {
/* 241 */     return this.contextResetNotificationStrategy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLoseContextOnReset() {
/* 249 */     return (this.contextResetNotificationStrategy == 33362);
/*     */   }
/*     */   
/*     */   public int getContextReleaseBehavior() {
/* 253 */     return this.contextReleaseBehavior;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLayerPlane() {
/* 258 */     return this.layerPlane;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ContextAttribs toggleMask(int mask, boolean value) {
/* 264 */     if (value == hasMask(mask)) {
/* 265 */       return this;
/*     */     }
/* 267 */     ContextAttribs attribs = new ContextAttribs(this);
/* 268 */     attribs.profileMask = value ? mask : 0;
/* 269 */     return attribs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContextAttribs withProfileCore(boolean profileCore) {
/* 277 */     if (this.majorVersion < 3 || (this.majorVersion == 3 && this.minorVersion < 2)) {
/* 278 */       throw new IllegalArgumentException("Profiles are only supported on OpenGL version 3.2 or higher.");
/*     */     }
/* 280 */     return toggleMask(1, profileCore);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContextAttribs withProfileCompatibility(boolean profileCompatibility) {
/* 288 */     if (this.majorVersion < 3 || (this.majorVersion == 3 && this.minorVersion < 2)) {
/* 289 */       throw new IllegalArgumentException("Profiles are only supported on OpenGL version 3.2 or higher.");
/*     */     }
/* 291 */     return toggleMask(2, profileCompatibility);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContextAttribs withProfileES(boolean profileES) {
/* 299 */     if (this.majorVersion != 2 || this.minorVersion != 0) {
/* 300 */       throw new IllegalArgumentException("The OpenGL ES profile is only supported on OpenGL version 2.0.");
/*     */     }
/* 302 */     return toggleMask(4, profileES);
/*     */   }
/*     */   
/*     */   private ContextAttribs toggleFlag(int flag, boolean value) {
/* 306 */     if (value == hasFlag(flag)) {
/* 307 */       return this;
/*     */     }
/* 309 */     ContextAttribs attribs = new ContextAttribs(this);
/* 310 */     attribs.contextFlags ^= flag;
/* 311 */     return attribs;
/*     */   }
/*     */   
/*     */   public ContextAttribs withDebug(boolean debug) {
/* 315 */     return toggleFlag(1, debug);
/*     */   }
/*     */   public ContextAttribs withForwardCompatible(boolean forwardCompatible) {
/* 318 */     return toggleFlag(2, forwardCompatible);
/*     */   }
/*     */   public ContextAttribs withRobustAccess(boolean robustAccess) {
/* 321 */     return toggleFlag(4, robustAccess);
/*     */   }
/*     */   public ContextAttribs withContextResetIsolation(boolean contextResetIsolation) {
/* 324 */     return toggleFlag(8, contextResetIsolation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContextAttribs withResetNotificationStrategy(int strategy) {
/* 335 */     if (strategy == this.contextResetNotificationStrategy) {
/* 336 */       return this;
/*     */     }
/* 338 */     if (LWJGLUtil.CHECKS && strategy != 33377 && strategy != 33362) {
/* 339 */       throw new IllegalArgumentException("Invalid context reset notification strategy specified: 0x" + LWJGLUtil.toHexString(strategy));
/*     */     }
/* 341 */     ContextAttribs attribs = new ContextAttribs(this);
/* 342 */     attribs.contextResetNotificationStrategy = strategy;
/* 343 */     return attribs;
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
/*     */   public ContextAttribs withLoseContextOnReset(boolean loseContextOnReset) {
/* 357 */     return withResetNotificationStrategy(loseContextOnReset ? 33362 : 33377);
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
/*     */   public ContextAttribs withContextReleaseBehavior(int behavior) {
/* 369 */     if (behavior == this.contextReleaseBehavior) {
/* 370 */       return this;
/*     */     }
/* 372 */     if (LWJGLUtil.CHECKS && behavior != 8344 && behavior != 0) {
/* 373 */       throw new IllegalArgumentException("Invalid context release behavior specified: 0x" + LWJGLUtil.toHexString(behavior));
/*     */     }
/* 375 */     ContextAttribs attribs = new ContextAttribs(this);
/* 376 */     attribs.contextReleaseBehavior = behavior;
/* 377 */     return attribs;
/*     */   }
/*     */ 
/*     */   
/*     */   public ContextAttribs withLayer(int layerPlane) {
/* 382 */     if (LWJGLUtil.getPlatform() != 3) {
/* 383 */       throw new IllegalArgumentException("The CONTEXT_LAYER_PLANE_ARB attribute is supported only on the Windows platform.");
/*     */     }
/* 385 */     if (layerPlane == this.layerPlane) {
/* 386 */       return this;
/*     */     }
/* 388 */     if (layerPlane < 0) {
/* 389 */       throw new IllegalArgumentException("Invalid layer plane specified: " + layerPlane);
/*     */     }
/* 391 */     ContextAttribs attribs = new ContextAttribs(this);
/* 392 */     attribs.layerPlane = layerPlane;
/* 393 */     return attribs;
/*     */   }
/*     */   
/*     */   IntBuffer getAttribList() {
/* 397 */     if (LWJGLUtil.getPlatform() == 2) {
/* 398 */       return null;
/*     */     }
/* 400 */     LinkedHashMap<Integer, Integer> map = new LinkedHashMap<Integer, Integer>(8);
/*     */     
/* 402 */     if (this.majorVersion != 1 || this.minorVersion != 0) {
/* 403 */       map.put(Integer.valueOf(8337), Integer.valueOf(this.majorVersion));
/* 404 */       map.put(Integer.valueOf(8338), Integer.valueOf(this.minorVersion));
/*     */     } 
/*     */     
/* 407 */     if (this.contextFlags != 0) {
/* 408 */       map.put(Integer.valueOf(8340), Integer.valueOf(this.contextFlags));
/*     */     }
/* 410 */     if (this.profileMask != 0) {
/* 411 */       map.put(Integer.valueOf(37158), Integer.valueOf(this.profileMask));
/*     */     }
/* 413 */     if (this.contextResetNotificationStrategy != 33377) {
/* 414 */       map.put(Integer.valueOf(33366), Integer.valueOf(this.contextResetNotificationStrategy));
/*     */     }
/* 416 */     if (this.contextReleaseBehavior != 8344) {
/* 417 */       map.put(Integer.valueOf(8343), Integer.valueOf(this.contextReleaseBehavior));
/*     */     }
/* 419 */     if (this.layerPlane != 0) {
/* 420 */       map.put(Integer.valueOf(8339), Integer.valueOf(this.layerPlane));
/*     */     }
/* 422 */     if (map.isEmpty()) {
/* 423 */       return null;
/*     */     }
/* 425 */     IntBuffer attribs = BufferUtils.createIntBuffer(map.size() * 2 + 1);
/* 426 */     for (Map.Entry<Integer, Integer> attrib : map.entrySet()) {
/* 427 */       attribs.put(((Integer)attrib.getKey()).intValue()).put(((Integer)attrib.getValue()).intValue());
/*     */     }
/*     */ 
/*     */     
/* 431 */     attribs.put(0);
/* 432 */     attribs.rewind();
/* 433 */     return attribs;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 437 */     StringBuilder sb = new StringBuilder(32);
/*     */     
/* 439 */     sb.append("ContextAttribs:");
/* 440 */     sb.append(" Version=").append(this.majorVersion).append('.').append(this.minorVersion);
/*     */     
/* 442 */     if (this.profileMask != 0) {
/* 443 */       sb.append(", Profile=");
/* 444 */       if (hasMask(1)) {
/* 445 */         sb.append("CORE");
/* 446 */       } else if (hasMask(2)) {
/* 447 */         sb.append("COMPATIBLITY");
/* 448 */       } else if (hasMask(4)) {
/* 449 */         sb.append("ES2");
/*     */       } else {
/* 451 */         sb.append("*unknown*");
/*     */       } 
/*     */     } 
/* 454 */     if (this.contextFlags != 0) {
/* 455 */       if (hasFlag(1))
/* 456 */         sb.append(", DEBUG"); 
/* 457 */       if (hasFlag(2))
/* 458 */         sb.append(", FORWARD_COMPATIBLE"); 
/* 459 */       if (hasFlag(4))
/* 460 */         sb.append(", ROBUST_ACCESS"); 
/* 461 */       if (hasFlag(8)) {
/* 462 */         sb.append(", RESET_ISOLATION");
/*     */       }
/*     */     } 
/* 465 */     if (this.contextResetNotificationStrategy != 33377)
/* 466 */       sb.append(", LOSE_CONTEXT_ON_RESET"); 
/* 467 */     if (this.contextReleaseBehavior != 8344) {
/* 468 */       sb.append(", RELEASE_BEHAVIOR_NONE");
/*     */     }
/* 470 */     if (this.layerPlane != 0) {
/* 471 */       sb.append(", Layer=").append(this.layerPlane);
/*     */     }
/* 473 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\ContextAttribs.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */