/*     */ package org.apache.logging.log4j.core.config.plugins.visitors;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.Node;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
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
/*     */ public class PluginElementVisitor
/*     */   extends AbstractPluginVisitor<PluginElement>
/*     */ {
/*     */   public PluginElementVisitor() {
/*  37 */     super(PluginElement.class);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object visit(Configuration configuration, Node node, LogEvent event, StringBuilder log) {
/*  43 */     String name = this.annotation.value();
/*  44 */     if (this.conversionType.isArray()) {
/*  45 */       setConversionType(this.conversionType.getComponentType());
/*  46 */       List<Object> values = new ArrayList();
/*  47 */       Collection<Node> used = new ArrayList<>();
/*  48 */       log.append("={");
/*  49 */       boolean first = true;
/*  50 */       for (Node child : node.getChildren()) {
/*  51 */         PluginType<?> childType = child.getType();
/*  52 */         if (name.equalsIgnoreCase(childType.getElementName()) || this.conversionType
/*  53 */           .isAssignableFrom(childType.getPluginClass())) {
/*  54 */           if (!first) {
/*  55 */             log.append(", ");
/*     */           }
/*  57 */           first = false;
/*  58 */           used.add(child);
/*  59 */           Object childObject = child.getObject();
/*  60 */           if (childObject == null) {
/*  61 */             LOGGER.error("Null object returned for {} in {}.", child.getName(), node.getName());
/*     */             continue;
/*     */           } 
/*  64 */           if (childObject.getClass().isArray()) {
/*  65 */             log.append(Arrays.toString((Object[])childObject)).append('}');
/*  66 */             node.getChildren().removeAll(used);
/*  67 */             return childObject;
/*     */           } 
/*  69 */           log.append(child.toString());
/*  70 */           values.add(childObject);
/*     */         } 
/*     */       } 
/*  73 */       log.append('}');
/*     */       
/*  75 */       if (!values.isEmpty() && !this.conversionType.isAssignableFrom(values.get(0).getClass())) {
/*  76 */         LOGGER.error("Attempted to assign attribute {} to list of type {} which is incompatible with {}.", name, values
/*  77 */             .get(0).getClass(), this.conversionType);
/*  78 */         return null;
/*     */       } 
/*  80 */       node.getChildren().removeAll(used);
/*     */       
/*  82 */       Object[] array = (Object[])Array.newInstance(this.conversionType, values.size());
/*  83 */       for (int i = 0; i < array.length; i++) {
/*  84 */         array[i] = values.get(i);
/*     */       }
/*  86 */       return array;
/*     */     } 
/*  88 */     Node namedNode = findNamedNode(name, node.getChildren());
/*  89 */     if (namedNode == null) {
/*  90 */       log.append(name).append("=null");
/*  91 */       return null;
/*     */     } 
/*  93 */     log.append(namedNode.getName()).append('(').append(namedNode.toString()).append(')');
/*  94 */     node.getChildren().remove(namedNode);
/*  95 */     return namedNode.getObject();
/*     */   }
/*     */   
/*     */   private Node findNamedNode(String name, Iterable<Node> children) {
/*  99 */     for (Node child : children) {
/* 100 */       PluginType<?> childType = child.getType();
/* 101 */       if (childType == null);
/*     */ 
/*     */       
/* 104 */       if (name.equalsIgnoreCase(childType.getElementName()) || this.conversionType
/* 105 */         .isAssignableFrom(childType.getPluginClass()))
/*     */       {
/*     */         
/* 108 */         return child;
/*     */       }
/*     */     } 
/* 111 */     return null;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\plugins\visitors\PluginElementVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */