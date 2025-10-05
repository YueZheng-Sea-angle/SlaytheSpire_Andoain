/*    */ package net.java.games.input;
/*    */ 
/*    */ import java.io.IOException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class LinuxComponent
/*    */   extends AbstractComponent
/*    */ {
/*    */   private final LinuxEventComponent component;
/*    */   
/*    */   public LinuxComponent(LinuxEventComponent component) {
/* 51 */     super(component.getIdentifier().getName(), component.getIdentifier());
/* 52 */     this.component = component;
/*    */   }
/*    */   
/*    */   public final boolean isRelative() {
/* 56 */     return this.component.isRelative();
/*    */   }
/*    */   
/*    */   public final boolean isAnalog() {
/* 60 */     return this.component.isAnalog();
/*    */   }
/*    */   
/*    */   protected float poll() throws IOException {
/* 64 */     return convertValue(LinuxControllers.poll(this.component), this.component.getDescriptor());
/*    */   }
/*    */   
/*    */   float convertValue(float value, LinuxAxisDescriptor descriptor) {
/* 68 */     return getComponent().convertValue(value);
/*    */   }
/*    */   
/*    */   public final float getDeadZone() {
/* 72 */     return this.component.getDeadZone();
/*    */   }
/*    */   
/*    */   public final LinuxEventComponent getComponent() {
/* 76 */     return this.component;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\net\java\games\input\LinuxComponent.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */