/*     */ package org.lwjgl.opengl;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
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
/*     */ final class WindowsKeyboard
/*     */ {
/*  47 */   private final byte[] key_down_buffer = new byte[256];
/*  48 */   private final byte[] virt_key_down_buffer = new byte[256];
/*  49 */   private final EventQueue event_queue = new EventQueue(18);
/*  50 */   private final ByteBuffer tmp_event = ByteBuffer.allocate(18);
/*     */   
/*     */   private boolean has_retained_event;
/*     */   
/*     */   private int retained_key_code;
/*     */   
/*     */   private byte retained_state;
/*     */   
/*     */   private int retained_char;
/*     */   private long retained_millis;
/*     */   private boolean retained_repeat;
/*     */   
/*     */   private static native boolean isWindowsNT();
/*     */   
/*     */   boolean isKeyDown(int lwjgl_keycode) {
/*  65 */     return (this.key_down_buffer[lwjgl_keycode] == 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void poll(ByteBuffer keyDownBuffer) {
/*  73 */     if (isKeyDown(42) && isKeyDown(54)) {
/*  74 */       if (!isKeyPressedAsync(160)) handleKey(16, 42, false, (byte)0, 0L, false); 
/*  75 */       if (!isKeyPressedAsync(161)) handleKey(16, 54, false, (byte)0, 0L, false);
/*     */     
/*     */     } 
/*  78 */     int old_position = keyDownBuffer.position();
/*  79 */     keyDownBuffer.put(this.key_down_buffer);
/*  80 */     keyDownBuffer.position(old_position);
/*     */   }
/*     */ 
/*     */   
/*     */   private static native int MapVirtualKey(int paramInt1, int paramInt2);
/*     */   
/*     */   private static native int ToUnicode(int paramInt1, int paramInt2, ByteBuffer paramByteBuffer, CharBuffer paramCharBuffer, int paramInt3, int paramInt4);
/*     */   
/*     */   private static native int ToAscii(int paramInt1, int paramInt2, ByteBuffer paramByteBuffer1, ByteBuffer paramByteBuffer2, int paramInt3);
/*     */   
/*     */   private void putEvent(int keycode, byte state, int ch, long millis, boolean repeat) {
/*  91 */     this.tmp_event.clear();
/*  92 */     this.tmp_event.putInt(keycode).put(state).putInt(ch).putLong(millis * 1000000L).put(repeat ? 1 : 0);
/*  93 */     this.tmp_event.flip();
/*  94 */     this.event_queue.putEvent(this.tmp_event);
/*     */   } private static native int GetKeyboardState(ByteBuffer paramByteBuffer); private static native short GetKeyState(int paramInt);
/*     */   private static native short GetAsyncKeyState(int paramInt);
/*     */   private static int translateExtended(int virt_key, int scan_code, boolean extended) {
/*  98 */     switch (virt_key) {
/*     */       case 16:
/* 100 */         return (scan_code == 54) ? 161 : 160;
/*     */       case 17:
/* 102 */         return extended ? 163 : 162;
/*     */       case 18:
/* 104 */         return extended ? 165 : 164;
/*     */     } 
/* 106 */     return virt_key;
/*     */   }
/*     */ 
/*     */   
/*     */   private void flushRetained() {
/* 111 */     if (this.has_retained_event) {
/* 112 */       this.has_retained_event = false;
/* 113 */       putEvent(this.retained_key_code, this.retained_state, this.retained_char, this.retained_millis, this.retained_repeat);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean isKeyPressed(int state) {
/* 118 */     return ((state & 0x1) == 1);
/*     */   }
/*     */   
/*     */   private static boolean isKeyPressedAsync(int virt_key) {
/* 122 */     return ((GetAsyncKeyState(virt_key) & 0x8000) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void releaseAll(long millis) {
/* 130 */     for (int i = 0; i < this.virt_key_down_buffer.length; i++) {
/* 131 */       if (isKeyPressed(this.virt_key_down_buffer[i])) {
/* 132 */         handleKey(i, 0, false, (byte)0, millis, false);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   void handleKey(int virt_key, int scan_code, boolean extended, byte event_state, long millis, boolean repeat) {
/* 138 */     virt_key = translateExtended(virt_key, scan_code, extended);
/* 139 */     if (!repeat && isKeyPressed(event_state) == isKeyPressed(this.virt_key_down_buffer[virt_key])) {
/*     */       return;
/*     */     }
/* 142 */     flushRetained();
/* 143 */     this.has_retained_event = true;
/* 144 */     int keycode = WindowsKeycodes.mapVirtualKeyToLWJGLCode(virt_key);
/* 145 */     if (keycode < this.key_down_buffer.length) {
/* 146 */       this.key_down_buffer[keycode] = event_state;
/* 147 */       repeat &= isKeyPressed(this.virt_key_down_buffer[virt_key]);
/* 148 */       this.virt_key_down_buffer[virt_key] = event_state;
/*     */     } 
/* 150 */     this.retained_key_code = keycode;
/* 151 */     this.retained_state = event_state;
/* 152 */     this.retained_millis = millis;
/* 153 */     this.retained_char = 0;
/* 154 */     this.retained_repeat = repeat;
/*     */   }
/*     */   
/*     */   void handleChar(int event_char, long millis, boolean repeat) {
/* 158 */     if (this.has_retained_event && this.retained_char != 0)
/* 159 */       flushRetained(); 
/* 160 */     if (!this.has_retained_event) {
/* 161 */       putEvent(0, (byte)0, event_char, millis, repeat);
/*     */     } else {
/* 163 */       this.retained_char = event_char;
/*     */     } 
/*     */   }
/*     */   void read(ByteBuffer buffer) {
/* 167 */     flushRetained();
/* 168 */     this.event_queue.copyEvents(buffer);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\WindowsKeyboard.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */