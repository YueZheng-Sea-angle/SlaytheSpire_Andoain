/*     */ package org.apache.commons.net.ftp;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import org.apache.commons.net.util.Charsets;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FTPListParseEngine
/*     */ {
/*  79 */   private List<String> entries = new LinkedList<String>();
/*  80 */   private ListIterator<String> _internalIterator = this.entries.listIterator();
/*     */   
/*     */   private final FTPFileEntryParser parser;
/*     */   
/*     */   private final boolean saveUnparseableEntries;
/*     */   
/*     */   public FTPListParseEngine(FTPFileEntryParser parser) {
/*  87 */     this(parser, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   FTPListParseEngine(FTPFileEntryParser parser, FTPClientConfig configuration) {
/*  95 */     this.parser = parser;
/*  96 */     if (configuration != null) {
/*  97 */       this.saveUnparseableEntries = configuration.getUnparseableEntries();
/*     */     } else {
/*  99 */       this.saveUnparseableEntries = false;
/*     */     } 
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
/*     */   
/*     */   public void readServerList(InputStream stream, String encoding) throws IOException {
/* 118 */     this.entries = new LinkedList<String>();
/* 119 */     readStream(stream, encoding);
/* 120 */     this.parser.preParse(this.entries);
/* 121 */     resetIterator();
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
/*     */ 
/*     */   
/*     */   private void readStream(InputStream stream, String encoding) throws IOException {
/* 140 */     BufferedReader reader = new BufferedReader(new InputStreamReader(stream, Charsets.toCharset(encoding)));
/*     */ 
/*     */     
/* 143 */     String line = this.parser.readNextEntry(reader);
/*     */     
/* 145 */     while (line != null) {
/*     */       
/* 147 */       this.entries.add(line);
/* 148 */       line = this.parser.readNextEntry(reader);
/*     */     } 
/* 150 */     reader.close();
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
/*     */   public FTPFile[] getNext(int quantityRequested) {
/* 177 */     List<FTPFile> tmpResults = new LinkedList<FTPFile>();
/* 178 */     int count = quantityRequested;
/* 179 */     while (count > 0 && this._internalIterator.hasNext()) {
/* 180 */       String entry = this._internalIterator.next();
/* 181 */       FTPFile temp = this.parser.parseFTPEntry(entry);
/* 182 */       if (temp == null && this.saveUnparseableEntries) {
/* 183 */         temp = new FTPFile(entry);
/*     */       }
/* 185 */       tmpResults.add(temp);
/* 186 */       count--;
/*     */     } 
/* 188 */     return tmpResults.<FTPFile>toArray(new FTPFile[tmpResults.size()]);
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
/*     */   public FTPFile[] getPrevious(int quantityRequested) {
/* 219 */     List<FTPFile> tmpResults = new LinkedList<FTPFile>();
/* 220 */     int count = quantityRequested;
/* 221 */     while (count > 0 && this._internalIterator.hasPrevious()) {
/* 222 */       String entry = this._internalIterator.previous();
/* 223 */       FTPFile temp = this.parser.parseFTPEntry(entry);
/* 224 */       if (temp == null && this.saveUnparseableEntries) {
/* 225 */         temp = new FTPFile(entry);
/*     */       }
/* 227 */       tmpResults.add(0, temp);
/* 228 */       count--;
/*     */     } 
/* 230 */     return tmpResults.<FTPFile>toArray(new FTPFile[tmpResults.size()]);
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
/*     */   public FTPFile[] getFiles() throws IOException {
/* 245 */     return getFiles(FTPFileFilters.NON_NULL);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTPFile[] getFiles(FTPFileFilter filter) throws IOException {
/* 269 */     List<FTPFile> tmpResults = new ArrayList<FTPFile>();
/* 270 */     Iterator<String> iter = this.entries.iterator();
/* 271 */     while (iter.hasNext()) {
/* 272 */       String entry = iter.next();
/* 273 */       FTPFile temp = this.parser.parseFTPEntry(entry);
/* 274 */       if (temp == null && this.saveUnparseableEntries) {
/* 275 */         temp = new FTPFile(entry);
/*     */       }
/* 277 */       if (filter.accept(temp)) {
/* 278 */         tmpResults.add(temp);
/*     */       }
/*     */     } 
/* 281 */     return tmpResults.<FTPFile>toArray(new FTPFile[tmpResults.size()]);
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
/*     */   public boolean hasNext() {
/* 293 */     return this._internalIterator.hasNext();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasPrevious() {
/* 304 */     return this._internalIterator.hasPrevious();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetIterator() {
/* 311 */     this._internalIterator = this.entries.listIterator();
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
/*     */   @Deprecated
/*     */   public void readServerList(InputStream stream) throws IOException {
/* 326 */     readServerList(stream, null);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\ftp\FTPListParseEngine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */