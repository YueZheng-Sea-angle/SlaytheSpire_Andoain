/*    */ package com.gikk.twirk.types.users;
/*    */ 
/*    */ import com.gikk.twirk.types.twitchMessage.TwitchMessage;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface UserstateBuilder
/*    */ {
/*    */   static UserstateBuilder getDefault() {
/* 13 */     return new DefaultUserstateBuilder();
/*    */   }
/*    */   
/*    */   Userstate build(TwitchMessage paramTwitchMessage);
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\com\gikk\twirk\type\\users\UserstateBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */