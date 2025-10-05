/*     */ package org.apache.commons.net.nntp;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class Threader
/*     */ {
/*     */   public Threadable thread(List<? extends Threadable> messages) {
/*  45 */     return thread(messages);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Threadable thread(Iterable<? extends Threadable> messages) {
/*  56 */     if (messages == null) {
/*  57 */       return null;
/*     */     }
/*     */     
/*  60 */     HashMap<String, ThreadContainer> idTable = new HashMap<String, ThreadContainer>();
/*     */ 
/*     */     
/*  63 */     for (Threadable t : messages) {
/*  64 */       if (!t.isDummy()) {
/*  65 */         buildContainer(t, idTable);
/*     */       }
/*     */     } 
/*     */     
/*  69 */     if (idTable.isEmpty()) {
/*  70 */       return null;
/*     */     }
/*     */     
/*  73 */     ThreadContainer root = findRootSet(idTable);
/*  74 */     idTable.clear();
/*  75 */     idTable = null;
/*     */     
/*  77 */     pruneEmptyContainers(root);
/*     */     
/*  79 */     root.reverseChildren();
/*  80 */     gatherSubjects(root);
/*     */     
/*  82 */     if (root.next != null) {
/*  83 */       throw new RuntimeException("root node has a next:" + root);
/*     */     }
/*     */     
/*  86 */     for (ThreadContainer r = root.child; r != null; r = r.next) {
/*  87 */       if (r.threadable == null) {
/*  88 */         r.threadable = r.child.threadable.makeDummy();
/*     */       }
/*     */     } 
/*     */     
/*  92 */     Threadable result = (root.child == null) ? null : root.child.threadable;
/*  93 */     root.flush();
/*     */     
/*  95 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void buildContainer(Threadable threadable, HashMap<String, ThreadContainer> idTable) {
/* 104 */     String id = threadable.messageThreadId();
/* 105 */     ThreadContainer container = idTable.get(id);
/* 106 */     int bogusIdCount = 0;
/*     */ 
/*     */ 
/*     */     
/* 110 */     if (container != null) {
/* 111 */       if (container.threadable != null) {
/* 112 */         bogusIdCount++;
/* 113 */         id = "<Bogus-id:" + bogusIdCount + ">";
/* 114 */         container = null;
/*     */       }
/*     */       else {
/*     */         
/* 118 */         container.threadable = threadable;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 123 */     if (container == null) {
/* 124 */       container = new ThreadContainer();
/* 125 */       container.threadable = threadable;
/* 126 */       idTable.put(id, container);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 131 */     ThreadContainer parentRef = null;
/*     */     
/* 133 */     String[] references = threadable.messageThreadReferences();
/* 134 */     for (String refString : references) {
/*     */       
/* 136 */       ThreadContainer ref = idTable.get(refString);
/*     */ 
/*     */       
/* 139 */       if (ref == null) {
/* 140 */         ref = new ThreadContainer();
/* 141 */         idTable.put(refString, ref);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 147 */       if (parentRef != null && ref.parent == null && parentRef != ref && !ref.findChild(parentRef)) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 152 */         ref.parent = parentRef;
/* 153 */         ref.next = parentRef.child;
/* 154 */         parentRef.child = ref;
/*     */       } 
/* 156 */       parentRef = ref;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 162 */     if (parentRef != null && (parentRef == container || container.findChild(parentRef)))
/*     */     {
/*     */       
/* 165 */       parentRef = null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 171 */     if (container.parent != null) {
/*     */ 
/*     */       
/* 174 */       ThreadContainer prev = null, rest = container.parent.child;
/* 175 */       for (; rest != null; 
/* 176 */         prev = rest, rest = rest.next) {
/* 177 */         if (rest == container) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */       
/* 182 */       if (rest == null) {
/* 183 */         throw new RuntimeException("Didnt find " + container + " in parent" + container.parent);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 191 */       if (prev == null) {
/* 192 */         container.parent.child = container.next;
/*     */       } else {
/* 194 */         prev.next = container.next;
/*     */       } 
/*     */       
/* 197 */       container.next = null;
/* 198 */       container.parent = null;
/*     */     } 
/*     */ 
/*     */     
/* 202 */     if (parentRef != null) {
/* 203 */       container.parent = parentRef;
/* 204 */       container.next = parentRef.child;
/* 205 */       parentRef.child = container;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ThreadContainer findRootSet(HashMap<String, ThreadContainer> idTable) {
/* 215 */     ThreadContainer root = new ThreadContainer();
/* 216 */     Iterator<Map.Entry<String, ThreadContainer>> iter = idTable.entrySet().iterator();
/*     */     
/* 218 */     while (iter.hasNext()) {
/* 219 */       Map.Entry<String, ThreadContainer> entry = iter.next();
/* 220 */       ThreadContainer c = entry.getValue();
/* 221 */       if (c.parent == null) {
/* 222 */         if (c.next != null) {
/* 223 */           throw new RuntimeException("c.next is " + c.next.toString());
/*     */         }
/*     */         
/* 226 */         c.next = root.child;
/* 227 */         root.child = c;
/*     */       } 
/*     */     } 
/* 230 */     return root;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void pruneEmptyContainers(ThreadContainer parent) {
/* 239 */     ThreadContainer prev = null, container = parent.child, next = container.next;
/* 240 */     for (; container != null; 
/* 241 */       prev = container, 
/* 242 */       container = next, 
/* 243 */       next = (container == null) ? null : container.next) {
/*     */ 
/*     */       
/* 246 */       if (container.threadable == null && container.child == null) {
/* 247 */         if (prev == null) {
/* 248 */           parent.child = container.next;
/*     */         } else {
/* 250 */           prev.next = container.next;
/*     */         } 
/*     */ 
/*     */         
/* 254 */         container = prev;
/*     */ 
/*     */       
/*     */       }
/* 258 */       else if (container.threadable == null && container.child != null && (container.parent != null || container.child.next == null)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 265 */         ThreadContainer kids = container.child;
/*     */ 
/*     */         
/* 268 */         if (prev == null) {
/* 269 */           parent.child = kids;
/*     */         } else {
/* 271 */           prev.next = kids;
/*     */         } 
/*     */ 
/*     */         
/*     */         ThreadContainer tail;
/*     */         
/* 277 */         for (tail = kids; tail.next != null; tail = tail.next) {
/* 278 */           tail.parent = container.parent;
/*     */         }
/*     */         
/* 281 */         tail.parent = container.parent;
/* 282 */         tail.next = container.next;
/*     */ 
/*     */ 
/*     */         
/* 286 */         next = kids;
/*     */ 
/*     */         
/* 289 */         container = prev;
/* 290 */       } else if (container.child != null) {
/*     */ 
/*     */         
/* 293 */         pruneEmptyContainers(container);
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
/*     */   private void gatherSubjects(ThreadContainer root) {
/* 305 */     int count = 0;
/*     */     
/* 307 */     for (ThreadContainer c = root.child; c != null; c = c.next) {
/* 308 */       count++;
/*     */     }
/*     */ 
/*     */     
/* 312 */     HashMap<String, ThreadContainer> subjectTable = new HashMap<String, ThreadContainer>((int)(count * 1.2D), 0.9F);
/* 313 */     count = 0;
/*     */     
/* 315 */     for (ThreadContainer threadContainer1 = root.child; threadContainer1 != null; threadContainer1 = threadContainer1.next) {
/* 316 */       Threadable threadable = threadContainer1.threadable;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 321 */       if (threadable == null) {
/* 322 */         threadable = threadContainer1.child.threadable;
/*     */       }
/*     */       
/* 325 */       String subj = threadable.simplifiedSubject();
/*     */       
/* 327 */       if (subj != null && subj.length() != 0) {
/*     */ 
/*     */ 
/*     */         
/* 331 */         ThreadContainer old = subjectTable.get(subj);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 340 */         if (old == null || (threadContainer1.threadable == null && old.threadable != null) || (old.threadable != null && old.threadable.subjectIsReply() && threadContainer1.threadable != null && !threadContainer1.threadable.subjectIsReply())) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 346 */           subjectTable.put(subj, threadContainer1);
/* 347 */           count++;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 352 */     if (count == 0) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 359 */     ThreadContainer prev = null, threadContainer2 = root.child, rest = threadContainer2.next;
/* 360 */     for (; threadContainer2 != null; 
/* 361 */       prev = threadContainer2, threadContainer2 = rest, rest = (rest == null) ? null : rest.next) {
/* 362 */       Threadable threadable = threadContainer2.threadable;
/*     */ 
/*     */       
/* 365 */       if (threadable == null) {
/* 366 */         threadable = threadContainer2.child.threadable;
/*     */       }
/*     */       
/* 369 */       String subj = threadable.simplifiedSubject();
/*     */ 
/*     */       
/* 372 */       if (subj != null && subj.length() != 0) {
/*     */ 
/*     */ 
/*     */         
/* 376 */         ThreadContainer old = subjectTable.get(subj);
/*     */         
/* 378 */         if (old != threadContainer2) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 384 */           if (prev == null) {
/* 385 */             root.child = threadContainer2.next;
/*     */           } else {
/* 387 */             prev.next = threadContainer2.next;
/*     */           } 
/* 389 */           threadContainer2.next = null;
/*     */           
/* 391 */           if (old.threadable == null && threadContainer2.threadable == null) {
/*     */ 
/*     */             
/* 394 */             ThreadContainer tail = old.child;
/* 395 */             while (tail != null && tail.next != null) {
/* 396 */               tail = tail.next;
/*     */             }
/*     */ 
/*     */             
/* 400 */             if (tail != null) {
/* 401 */               tail.next = threadContainer2.child;
/*     */             }
/*     */             
/* 404 */             for (tail = threadContainer2.child; tail != null; tail = tail.next) {
/* 405 */               tail.parent = old;
/*     */             }
/*     */             
/* 408 */             threadContainer2.child = null;
/* 409 */           } else if (old.threadable == null || (threadContainer2.threadable != null && threadContainer2.threadable.subjectIsReply() && !old.threadable.subjectIsReply())) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 415 */             threadContainer2.parent = old;
/* 416 */             threadContainer2.next = old.child;
/* 417 */             old.child = threadContainer2;
/*     */           }
/*     */           else {
/*     */             
/* 421 */             ThreadContainer newc = new ThreadContainer();
/* 422 */             newc.threadable = old.threadable;
/* 423 */             newc.child = old.child;
/*     */             
/* 425 */             ThreadContainer tail = newc.child;
/* 426 */             for (; tail != null; 
/* 427 */               tail = tail.next)
/*     */             {
/* 429 */               tail.parent = newc;
/*     */             }
/*     */             
/* 432 */             old.threadable = null;
/* 433 */             old.child = null;
/*     */             
/* 435 */             threadContainer2.parent = old;
/* 436 */             newc.parent = old;
/*     */ 
/*     */             
/* 439 */             old.child = threadContainer2;
/* 440 */             threadContainer2.next = newc;
/*     */           } 
/*     */           
/* 443 */           threadContainer2 = prev;
/*     */         } 
/*     */       } 
/* 446 */     }  subjectTable.clear();
/* 447 */     subjectTable = null;
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
/*     */   @Deprecated
/*     */   public Threadable thread(Threadable[] messages) {
/* 463 */     if (messages == null) {
/* 464 */       return null;
/*     */     }
/* 466 */     return thread(Arrays.asList(messages));
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\nntp\Threader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */