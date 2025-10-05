/*     */ package org.apache.logging.log4j.core.config;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginType;
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
/*     */ public class Node
/*     */ {
/*     */   public static final String CATEGORY = "Core";
/*     */   private Node parent;
/*     */   private final String name;
/*     */   private String value;
/*     */   private final PluginType<?> type;
/*  43 */   private final Map<String, String> attributes = new HashMap<>();
/*  44 */   private final List<Node> children = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object object;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node(Node parent, String name, PluginType<?> type) {
/*  57 */     this.parent = parent;
/*  58 */     this.name = name;
/*  59 */     this.type = type;
/*     */   }
/*     */   
/*     */   public Node() {
/*  63 */     this.parent = null;
/*  64 */     this.name = null;
/*  65 */     this.type = null;
/*     */   }
/*     */   
/*     */   public Node(Node node) {
/*  69 */     this.parent = node.parent;
/*  70 */     this.name = node.name;
/*  71 */     this.type = node.type;
/*  72 */     this.attributes.putAll(node.getAttributes());
/*  73 */     this.value = node.getValue();
/*  74 */     for (Node child : node.getChildren()) {
/*  75 */       this.children.add(new Node(child));
/*     */     }
/*  77 */     this.object = node.object;
/*     */   }
/*     */   
/*     */   public void setParent(Node parent) {
/*  81 */     this.parent = parent;
/*     */   }
/*     */   
/*     */   public Map<String, String> getAttributes() {
/*  85 */     return this.attributes;
/*     */   }
/*     */   
/*     */   public List<Node> getChildren() {
/*  89 */     return this.children;
/*     */   }
/*     */   
/*     */   public boolean hasChildren() {
/*  93 */     return !this.children.isEmpty();
/*     */   }
/*     */   
/*     */   public String getValue() {
/*  97 */     return this.value;
/*     */   }
/*     */   
/*     */   public void setValue(String value) {
/* 101 */     this.value = value;
/*     */   }
/*     */   
/*     */   public Node getParent() {
/* 105 */     return this.parent;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 109 */     return this.name;
/*     */   }
/*     */   
/*     */   public boolean isRoot() {
/* 113 */     return (this.parent == null);
/*     */   }
/*     */   
/*     */   public void setObject(Object obj) {
/* 117 */     this.object = obj;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getObject() {
/* 122 */     return (T)this.object;
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
/*     */   public <T> T getObject(Class<T> clazz) {
/* 134 */     return clazz.cast(this.object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInstanceOf(Class<?> clazz) {
/* 145 */     return clazz.isInstance(this.object);
/*     */   }
/*     */   
/*     */   public PluginType<?> getType() {
/* 149 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 154 */     if (this.object == null) {
/* 155 */       return "null";
/*     */     }
/* 157 */     return this.type.isObjectPrintable() ? this.object.toString() : (this.type
/* 158 */       .getPluginClass().getName() + " with name " + this.name);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\Node.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */