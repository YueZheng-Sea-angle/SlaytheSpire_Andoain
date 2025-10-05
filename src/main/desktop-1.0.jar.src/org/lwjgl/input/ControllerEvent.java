/*     */ package org.lwjgl.input;
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
/*     */ class ControllerEvent
/*     */ {
/*     */   public static final int BUTTON = 1;
/*     */   public static final int AXIS = 2;
/*     */   public static final int POVX = 3;
/*     */   public static final int POVY = 4;
/*     */   private Controller source;
/*     */   private int index;
/*     */   private int type;
/*     */   private boolean buttonState;
/*     */   private boolean xaxis;
/*     */   private boolean yaxis;
/*     */   private long timeStamp;
/*     */   private float xaxisValue;
/*     */   private float yaxisValue;
/*     */   
/*     */   ControllerEvent(Controller source, long timeStamp, int type, int index, boolean xaxis, boolean yaxis) {
/*  79 */     this(source, timeStamp, type, index, false, xaxis, yaxis, 0.0F, 0.0F);
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
/*     */ 
/*     */   
/*     */   ControllerEvent(Controller source, long timeStamp, int type, int index, boolean buttonState, boolean xaxis, boolean yaxis, float xaxisValue, float yaxisValue) {
/*  96 */     this.source = source;
/*  97 */     this.timeStamp = timeStamp;
/*  98 */     this.type = type;
/*  99 */     this.index = index;
/* 100 */     this.buttonState = buttonState;
/* 101 */     this.xaxis = xaxis;
/* 102 */     this.yaxis = yaxis;
/* 103 */     this.xaxisValue = xaxisValue;
/* 104 */     this.yaxisValue = yaxisValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getTimeStamp() {
/* 114 */     return this.timeStamp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Controller getSource() {
/* 123 */     return this.source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getControlIndex() {
/* 132 */     return this.index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isButton() {
/* 141 */     return (this.type == 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getButtonState() {
/* 150 */     return this.buttonState;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAxis() {
/* 159 */     return (this.type == 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPovY() {
/* 168 */     return (this.type == 4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPovX() {
/* 177 */     return (this.type == 3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isXAxis() {
/* 186 */     return this.xaxis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isYAxis() {
/* 195 */     return this.yaxis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getXAxisValue() {
/* 204 */     return this.xaxisValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getYAxisValue() {
/* 213 */     return this.yaxisValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 220 */     return "[" + this.source + " type=" + this.type + " xaxis=" + this.xaxis + " yaxis=" + this.yaxis + "]";
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\input\ControllerEvent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */