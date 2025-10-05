/*     */ package org.lwjgl.input;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import net.java.games.input.Controller;
/*     */ import net.java.games.input.ControllerEnvironment;
/*     */ import org.lwjgl.LWJGLException;
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
/*     */ public class Controllers
/*     */ {
/*  47 */   private static ArrayList<JInputController> controllers = new ArrayList<JInputController>();
/*     */ 
/*     */   
/*     */   private static int controllerCount;
/*     */   
/*  52 */   private static ArrayList<ControllerEvent> events = new ArrayList<ControllerEvent>();
/*     */ 
/*     */ 
/*     */   
/*     */   private static ControllerEvent event;
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean created;
/*     */ 
/*     */ 
/*     */   
/*     */   public static void create() throws LWJGLException {
/*  65 */     if (created) {
/*     */       return;
/*     */     }
/*     */     try {
/*  69 */       ControllerEnvironment env = ControllerEnvironment.getDefaultEnvironment();
/*     */       
/*  71 */       Controller[] found = env.getControllers();
/*  72 */       ArrayList<Controller> lollers = new ArrayList<Controller>();
/*  73 */       for (Controller c : found) {
/*  74 */         if (!c.getType().equals(Controller.Type.KEYBOARD) && !c.getType().equals(Controller.Type.MOUSE))
/*     */         {
/*  76 */           lollers.add(c);
/*     */         }
/*     */       } 
/*     */       
/*  80 */       for (Controller c : lollers) {
/*  81 */         createController(c);
/*     */       }
/*     */       
/*  84 */       created = true;
/*  85 */     } catch (Throwable e) {
/*  86 */       throw new LWJGLException("Failed to initialise controllers", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void createController(Controller c) {
/*  96 */     Controller[] subControllers = c.getControllers();
/*  97 */     if (subControllers.length == 0) {
/*  98 */       JInputController controller = new JInputController(controllerCount, c);
/*     */       
/* 100 */       controllers.add(controller);
/* 101 */       controllerCount++;
/*     */     } else {
/* 103 */       for (Controller sub : subControllers) {
/* 104 */         createController(sub);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Controller getController(int index) {
/* 116 */     return controllers.get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getControllerCount() {
/* 125 */     return controllers.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void poll() {
/* 133 */     for (int i = 0; i < controllers.size(); i++) {
/* 134 */       getController(i).poll();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void clearEvents() {
/* 142 */     events.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean next() {
/* 151 */     if (events.size() == 0) {
/* 152 */       event = null;
/* 153 */       return false;
/*     */     } 
/*     */     
/* 156 */     event = events.remove(0);
/*     */     
/* 158 */     return (event != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isCreated() {
/* 165 */     return created;
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
/*     */   public static void destroy() {}
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
/*     */   public static Controller getEventSource() {
/* 196 */     return event.getSource();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getEventControlIndex() {
/* 205 */     return event.getControlIndex();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isEventButton() {
/* 214 */     return event.isButton();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isEventAxis() {
/* 223 */     return event.isAxis();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isEventXAxis() {
/* 232 */     return event.isXAxis();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isEventYAxis() {
/* 241 */     return event.isYAxis();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isEventPovX() {
/* 250 */     return event.isPovX();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isEventPovY() {
/* 259 */     return event.isPovY();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long getEventNanoseconds() {
/* 268 */     return event.getTimeStamp();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean getEventButtonState() {
/* 277 */     return event.getButtonState();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float getEventXAxisValue() {
/* 286 */     return event.getXAxisValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float getEventYAxisValue() {
/* 295 */     return event.getYAxisValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void addEvent(ControllerEvent event) {
/* 304 */     if (event != null)
/* 305 */       events.add(event); 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\input\Controllers.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */