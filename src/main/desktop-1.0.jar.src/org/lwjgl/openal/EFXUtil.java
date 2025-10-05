/*     */ package org.lwjgl.openal;
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
/*     */ public final class EFXUtil
/*     */ {
/*     */   private static final int EFFECT = 1111;
/*     */   private static final int FILTER = 2222;
/*     */   
/*     */   public static boolean isEfxSupported() {
/*  65 */     if (!AL.isCreated()) {
/*  66 */       throw new OpenALException("OpenAL has not been created.");
/*     */     }
/*  68 */     return ALC10.alcIsExtensionPresent(AL.getDevice(), "ALC_EXT_EFX");
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
/*     */   public static boolean isEffectSupported(int effectType) {
/*  83 */     switch (effectType) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 0:
/*     */       case 1:
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/*     */       case 6:
/*     */       case 7:
/*     */       case 8:
/*     */       case 9:
/*     */       case 10:
/*     */       case 11:
/*     */       case 12:
/*     */       case 32768:
/* 103 */         return testSupportGeneric(1111, effectType);
/*     */     } 
/*     */     throw new IllegalArgumentException("Unknown or invalid effect type: " + effectType);
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
/*     */   public static boolean isFilterSupported(int filterType) {
/* 118 */     switch (filterType) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 0:
/*     */       case 1:
/*     */       case 2:
/*     */       case 3:
/* 128 */         return testSupportGeneric(2222, filterType);
/*     */     } 
/*     */     throw new IllegalArgumentException("Unknown or invalid filter type: " + filterType);
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
/*     */   private static boolean testSupportGeneric(int objectType, int typeValue) {
/* 142 */     switch (objectType) {
/*     */       case 1111:
/*     */       case 2222:
/*     */         break;
/*     */       default:
/* 147 */         throw new IllegalArgumentException("Invalid objectType: " + objectType);
/*     */     } 
/*     */     
/* 150 */     boolean supported = false;
/* 151 */     if (isEfxSupported())
/*     */     { int genError;
/*     */       
/* 154 */       AL10.alGetError();
/*     */       
/* 156 */       int testObject = 0;
/*     */       try {
/* 158 */         switch (objectType) {
/*     */           case 1111:
/* 160 */             testObject = EFX10.alGenEffects();
/*     */             break;
/*     */           case 2222:
/* 163 */             testObject = EFX10.alGenFilters();
/*     */             break;
/*     */           default:
/* 166 */             throw new IllegalArgumentException("Invalid objectType: " + objectType);
/*     */         } 
/* 168 */         genError = AL10.alGetError();
/* 169 */       } catch (OpenALException debugBuildException) {
/*     */ 
/*     */         
/* 172 */         if (debugBuildException.getMessage().contains("AL_OUT_OF_MEMORY")) {
/* 173 */           genError = 40965;
/*     */         } else {
/* 175 */           genError = 40964;
/*     */         } 
/*     */       } 
/*     */       
/* 179 */       if (genError == 0)
/*     */       { char c;
/* 181 */         AL10.alGetError();
/*     */         
/*     */         try {
/* 184 */           switch (objectType) {
/*     */             case 1111:
/* 186 */               EFX10.alEffecti(testObject, 32769, typeValue);
/*     */               break;
/*     */             case 2222:
/* 189 */               EFX10.alFilteri(testObject, 32769, typeValue);
/*     */               break;
/*     */             default:
/* 192 */               throw new IllegalArgumentException("Invalid objectType: " + objectType);
/*     */           } 
/* 194 */           c = AL10.alGetError();
/* 195 */         } catch (OpenALException debugBuildException) {
/*     */ 
/*     */           
/* 198 */           c = 'ꀃ';
/*     */         } 
/*     */         
/* 201 */         if (c == '\000') {
/* 202 */           supported = true;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 207 */         try { switch (objectType)
/*     */           { case 1111:
/* 209 */               EFX10.alDeleteEffects(testObject);
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
/* 226 */               return supported;case 2222: EFX10.alDeleteFilters(testObject); return supported; }  throw new IllegalArgumentException("Invalid objectType: " + objectType); } catch (OpenALException debugBuildException) {} } else if (genError == 40965) { throw new OpenALException(genError); }  }  return supported;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\openal\EFXUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */