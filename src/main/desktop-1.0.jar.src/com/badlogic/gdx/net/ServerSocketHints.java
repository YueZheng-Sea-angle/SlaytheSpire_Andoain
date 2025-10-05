/*    */ package com.badlogic.gdx.net;
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
/*    */ public class ServerSocketHints
/*    */ {
/* 28 */   public int backlog = 16;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   public int performancePrefConnectionTime = 0;
/*    */   
/* 38 */   public int performancePrefLatency = 1;
/*    */   
/* 40 */   public int performancePrefBandwidth = 0;
/*    */   
/*    */   public boolean reuseAddress = true;
/*    */   
/* 44 */   public int acceptTimeout = 5000;
/*    */   
/* 46 */   public int receiveBufferSize = 4096;
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\com\badlogic\gdx\net\ServerSocketHints.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */