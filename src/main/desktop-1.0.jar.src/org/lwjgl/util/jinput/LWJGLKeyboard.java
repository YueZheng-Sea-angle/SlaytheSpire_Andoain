/*     */ package org.lwjgl.util.jinput;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.java.games.input.AbstractComponent;
/*     */ import net.java.games.input.Component;
/*     */ import net.java.games.input.Event;
/*     */ import net.java.games.input.Keyboard;
/*     */ import org.lwjgl.input.Keyboard;
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
/*     */ final class LWJGLKeyboard
/*     */   extends Keyboard
/*     */ {
/*     */   LWJGLKeyboard() {
/*  53 */     super("LWJGLKeyboard", createComponents(), new net.java.games.input.Controller[0], new net.java.games.input.Rumbler[0]);
/*     */   }
/*     */   
/*     */   private static Component[] createComponents() {
/*  57 */     List<Key> components = new ArrayList<Key>();
/*  58 */     Field[] vkey_fields = Keyboard.class.getFields();
/*  59 */     for (Field vkey_field : vkey_fields) {
/*     */       try {
/*  61 */         if (Modifier.isStatic(vkey_field.getModifiers()) && vkey_field.getType() == int.class && vkey_field.getName().startsWith("KEY_")) {
/*     */           
/*  63 */           int vkey_code = vkey_field.getInt(null);
/*  64 */           Component.Identifier.Key key_id = KeyMap.map(vkey_code);
/*  65 */           if (key_id != Component.Identifier.Key.UNKNOWN)
/*  66 */             components.add(new Key(key_id, vkey_code)); 
/*     */         } 
/*  68 */       } catch (IllegalAccessException e) {
/*  69 */         throw new RuntimeException(e);
/*     */       } 
/*     */     } 
/*  72 */     return components.<Component>toArray(new Component[components.size()]);
/*     */   }
/*     */   
/*     */   public synchronized void pollDevice() throws IOException {
/*  76 */     if (!Keyboard.isCreated())
/*     */       return; 
/*  78 */     Keyboard.poll();
/*  79 */     for (Component component : getComponents()) {
/*  80 */       Key key = (Key)component;
/*  81 */       key.update();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected synchronized boolean getNextDeviceEvent(Event event) throws IOException {
/*  86 */     if (!Keyboard.isCreated())
/*  87 */       return false; 
/*  88 */     if (!Keyboard.next())
/*  89 */       return false; 
/*  90 */     int lwjgl_key = Keyboard.getEventKey();
/*  91 */     if (lwjgl_key == 0)
/*  92 */       return false; 
/*  93 */     Component.Identifier.Key key_id = KeyMap.map(lwjgl_key);
/*  94 */     if (key_id == null)
/*  95 */       return false; 
/*  96 */     Component key = getComponent((Component.Identifier)key_id);
/*  97 */     if (key == null)
/*  98 */       return false; 
/*  99 */     float value = Keyboard.getEventKeyState() ? 1.0F : 0.0F;
/* 100 */     event.set(key, value, Keyboard.getEventNanoseconds());
/* 101 */     return true;
/*     */   }
/*     */   
/*     */   private static final class Key
/*     */     extends AbstractComponent {
/*     */     private final int lwjgl_key;
/*     */     private float value;
/*     */     
/*     */     Key(Component.Identifier.Key key_id, int lwjgl_key) {
/* 110 */       super(key_id.getName(), (Component.Identifier)key_id);
/* 111 */       this.lwjgl_key = lwjgl_key;
/*     */     }
/*     */     
/*     */     public void update() {
/* 115 */       this.value = Keyboard.isKeyDown(this.lwjgl_key) ? 1.0F : 0.0F;
/*     */     }
/*     */     
/*     */     protected float poll() {
/* 119 */       return this.value;
/*     */     }
/*     */     
/*     */     public boolean isRelative() {
/* 123 */       return false;
/*     */     }
/*     */     
/*     */     public boolean isAnalog() {
/* 127 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjg\\util\jinput\LWJGLKeyboard.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */