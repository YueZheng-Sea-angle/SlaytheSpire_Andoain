/*    */ package org.lwjgl.util.jinput;
/*    */ 
/*    */ import net.java.games.input.Controller;
/*    */ import net.java.games.input.ControllerEnvironment;
/*    */ import net.java.games.util.plugins.Plugin;
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
/*    */ public class LWJGLEnvironmentPlugin
/*    */   extends ControllerEnvironment
/*    */   implements Plugin
/*    */ {
/* 45 */   private final Controller[] controllers = new Controller[] { (Controller)new LWJGLKeyboard(), (Controller)new LWJGLMouse() };
/*    */ 
/*    */   
/*    */   public Controller[] getControllers() {
/* 49 */     return this.controllers;
/*    */   }
/*    */   
/*    */   public boolean isSupported() {
/* 53 */     return true;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjg\\util\jinput\LWJGLEnvironmentPlugin.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */