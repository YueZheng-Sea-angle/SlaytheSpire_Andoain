/*     */ package org.apache.commons.net.ftp;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum FTPCmd
/*     */ {
/*     */   public static final FTPCmd ABORT;
/*     */   public static final FTPCmd ACCOUNT;
/*     */   public static final FTPCmd ALLOCATE;
/*     */   public static final FTPCmd APPEND;
/*     */   public static final FTPCmd CHANGE_TO_PARENT_DIRECTORY;
/*     */   public static final FTPCmd CHANGE_WORKING_DIRECTORY;
/*     */   public static final FTPCmd DATA_PORT;
/*     */   public static final FTPCmd DELETE;
/*     */   public static final FTPCmd FEATURES;
/*     */   public static final FTPCmd FILE_STRUCTURE;
/*     */   public static final FTPCmd GET_MOD_TIME;
/*     */   public static final FTPCmd LOGOUT;
/*     */   public static final FTPCmd MAKE_DIRECTORY;
/*     */   public static final FTPCmd MOD_TIME;
/*     */   public static final FTPCmd NAME_LIST;
/*  25 */   ABOR,
/*  26 */   ACCT,
/*  27 */   ALLO,
/*  28 */   APPE,
/*  29 */   CDUP,
/*  30 */   CWD,
/*  31 */   DELE,
/*  32 */   EPRT,
/*  33 */   EPSV,
/*  34 */   FEAT,
/*  35 */   HELP,
/*  36 */   LIST,
/*  37 */   MDTM,
/*  38 */   MFMT,
/*  39 */   MKD,
/*  40 */   MLSD,
/*  41 */   MLST,
/*  42 */   MODE,
/*  43 */   NLST,
/*  44 */   NOOP,
/*  45 */   PASS,
/*  46 */   PASV,
/*  47 */   PORT,
/*  48 */   PWD,
/*  49 */   QUIT,
/*  50 */   REIN,
/*  51 */   REST,
/*  52 */   RETR,
/*  53 */   RMD,
/*  54 */   RNFR,
/*  55 */   RNTO,
/*  56 */   SITE,
/*  57 */   SMNT,
/*  58 */   STAT,
/*  59 */   STOR,
/*  60 */   STOU,
/*  61 */   STRU,
/*  62 */   SYST,
/*  63 */   TYPE,
/*  64 */   USER;
/*     */   public static final FTPCmd PASSIVE;
/*     */   public static final FTPCmd PASSWORD;
/*     */   
/*     */   static {
/*  69 */     ABORT = ABOR;
/*  70 */     ACCOUNT = ACCT;
/*  71 */     ALLOCATE = ALLO;
/*  72 */     APPEND = APPE;
/*  73 */     CHANGE_TO_PARENT_DIRECTORY = CDUP;
/*  74 */     CHANGE_WORKING_DIRECTORY = CWD;
/*  75 */     DATA_PORT = PORT;
/*  76 */     DELETE = DELE;
/*  77 */     FEATURES = FEAT;
/*  78 */     FILE_STRUCTURE = STRU;
/*  79 */     GET_MOD_TIME = MDTM;
/*  80 */     LOGOUT = QUIT;
/*  81 */     MAKE_DIRECTORY = MKD;
/*  82 */     MOD_TIME = MDTM;
/*  83 */     NAME_LIST = NLST;
/*  84 */     PASSIVE = PASV;
/*  85 */     PASSWORD = PASS;
/*  86 */     PRINT_WORKING_DIRECTORY = PWD;
/*  87 */     REINITIALIZE = REIN;
/*  88 */     REMOVE_DIRECTORY = RMD;
/*  89 */     RENAME_FROM = RNFR;
/*  90 */     RENAME_TO = RNTO;
/*  91 */     REPRESENTATION_TYPE = TYPE;
/*  92 */     RESTART = REST;
/*  93 */     RETRIEVE = RETR;
/*  94 */     SET_MOD_TIME = MFMT;
/*  95 */     SITE_PARAMETERS = SITE;
/*  96 */     STATUS = STAT;
/*  97 */     STORE = STOR;
/*  98 */     STORE_UNIQUE = STOU;
/*  99 */     STRUCTURE_MOUNT = SMNT;
/* 100 */     SYSTEM = SYST;
/* 101 */     TRANSFER_MODE = MODE;
/* 102 */     USERNAME = USER;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final FTPCmd PRINT_WORKING_DIRECTORY;
/*     */   
/*     */   public static final FTPCmd REINITIALIZE;
/*     */   public static final FTPCmd REMOVE_DIRECTORY;
/*     */   public static final FTPCmd RENAME_FROM;
/*     */   
/*     */   public final String getCommand() {
/* 113 */     return name();
/*     */   }
/*     */   
/*     */   public static final FTPCmd RENAME_TO;
/*     */   public static final FTPCmd REPRESENTATION_TYPE;
/*     */   public static final FTPCmd RESTART;
/*     */   public static final FTPCmd RETRIEVE;
/*     */   public static final FTPCmd SET_MOD_TIME;
/*     */   public static final FTPCmd SITE_PARAMETERS;
/*     */   public static final FTPCmd STATUS;
/*     */   public static final FTPCmd STORE;
/*     */   public static final FTPCmd STORE_UNIQUE;
/*     */   public static final FTPCmd STRUCTURE_MOUNT;
/*     */   public static final FTPCmd SYSTEM;
/*     */   public static final FTPCmd TRANSFER_MODE;
/*     */   public static final FTPCmd USERNAME;
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\ftp\FTPCmd.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */