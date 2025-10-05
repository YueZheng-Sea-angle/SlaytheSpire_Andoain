/*    */ package com.badlogic.gdx.backends.headless.mock.audio;
/*    */ 
/*    */ import com.badlogic.gdx.audio.AudioDevice;
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
/*    */ public class MockAudioDevice
/*    */   implements AudioDevice
/*    */ {
/*    */   public boolean isMono() {
/* 28 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void writeSamples(short[] samples, int offset, int numSamples) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void writeSamples(float[] samples, int offset, int numSamples) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public int getLatency() {
/* 43 */     return 0;
/*    */   }
/*    */   
/*    */   public void dispose() {}
/*    */   
/*    */   public void setVolume(float volume) {}
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\com\badlogic\gdx\backends\headless\mock\audio\MockAudioDevice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */