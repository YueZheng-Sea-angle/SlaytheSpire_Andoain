/*    */ package com.gikk.twirk.types.users;
/*    */ 
/*    */ import com.gikk.twirk.types.twitchMessage.TwitchMessage;
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
/*    */ public interface TwitchUserBuilder
/*    */ {
/*    */   TwitchUser build(TwitchMessage paramTwitchMessage);
/*    */   
/*    */   static TwitchUserBuilder getDefault() {
/* 20 */     return new DefaultTwitchUserBuilder();
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\com\gikk\twirk\type\\users\TwitchUserBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */