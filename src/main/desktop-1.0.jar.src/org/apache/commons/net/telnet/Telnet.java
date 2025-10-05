/*      */ package org.apache.commons.net.telnet;
/*      */ 
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import org.apache.commons.net.SocketClient;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class Telnet
/*      */   extends SocketClient
/*      */ {
/*      */   static final boolean debug = false;
/*      */   static final boolean debugoptions = false;
/*   34 */   static final byte[] _COMMAND_DO = new byte[] { -1, -3 };
/*      */ 
/*      */ 
/*      */   
/*   38 */   static final byte[] _COMMAND_DONT = new byte[] { -1, -2 };
/*      */ 
/*      */ 
/*      */   
/*   42 */   static final byte[] _COMMAND_WILL = new byte[] { -1, -5 };
/*      */ 
/*      */ 
/*      */   
/*   46 */   static final byte[] _COMMAND_WONT = new byte[] { -1, -4 };
/*      */ 
/*      */ 
/*      */   
/*   50 */   static final byte[] _COMMAND_SB = new byte[] { -1, -6 };
/*      */ 
/*      */ 
/*      */   
/*   54 */   static final byte[] _COMMAND_SE = new byte[] { -1, -16 };
/*      */ 
/*      */   
/*      */   static final int _WILL_MASK = 1;
/*      */ 
/*      */   
/*      */   static final int _DO_MASK = 2;
/*      */ 
/*      */   
/*      */   static final int _REQUESTED_WILL_MASK = 4;
/*      */ 
/*      */   
/*      */   static final int _REQUESTED_DO_MASK = 8;
/*      */ 
/*      */   
/*      */   static final int DEFAULT_PORT = 23;
/*      */ 
/*      */   
/*      */   int[] _doResponse;
/*      */ 
/*      */   
/*      */   int[] _willResponse;
/*      */   
/*      */   int[] _options;
/*      */   
/*      */   protected static final int TERMINAL_TYPE = 24;
/*      */   
/*      */   protected static final int TERMINAL_TYPE_SEND = 1;
/*      */   
/*      */   protected static final int TERMINAL_TYPE_IS = 0;
/*      */   
/*   85 */   static final byte[] _COMMAND_IS = new byte[] { 24, 0 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   92 */   private String terminalType = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final TelnetOptionHandler[] optionHandlers;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  107 */   static final byte[] _COMMAND_AYT = new byte[] { -1, -10 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  114 */   private final Object aytMonitor = new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private volatile boolean aytFlag = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  125 */   private volatile OutputStream spyStream = null;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  130 */   private TelnetNotificationHandler __notifhand = null;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Telnet() {
/*  136 */     setDefaultPort(23);
/*  137 */     this._doResponse = new int[256];
/*  138 */     this._willResponse = new int[256];
/*  139 */     this._options = new int[256];
/*  140 */     this.optionHandlers = new TelnetOptionHandler[256];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Telnet(String termtype) {
/*  152 */     setDefaultPort(23);
/*  153 */     this._doResponse = new int[256];
/*  154 */     this._willResponse = new int[256];
/*  155 */     this._options = new int[256];
/*  156 */     this.terminalType = termtype;
/*  157 */     this.optionHandlers = new TelnetOptionHandler[256];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean _stateIsWill(int option) {
/*  171 */     return ((this._options[option] & 0x1) != 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean _stateIsWont(int option) {
/*  183 */     return !_stateIsWill(option);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean _stateIsDo(int option) {
/*  195 */     return ((this._options[option] & 0x2) != 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean _stateIsDont(int option) {
/*  207 */     return !_stateIsDo(option);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean _requestedWill(int option) {
/*  219 */     return ((this._options[option] & 0x4) != 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean _requestedWont(int option) {
/*  231 */     return !_requestedWill(option);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean _requestedDo(int option) {
/*  243 */     return ((this._options[option] & 0x8) != 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean _requestedDont(int option) {
/*  255 */     return !_requestedDo(option);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void _setWill(int option) throws IOException {
/*  266 */     this._options[option] = this._options[option] | 0x1;
/*      */ 
/*      */     
/*  269 */     if (_requestedWill(option))
/*      */     {
/*  271 */       if (this.optionHandlers[option] != null) {
/*      */         
/*  273 */         this.optionHandlers[option].setWill(true);
/*      */         
/*  275 */         int[] subneg = this.optionHandlers[option].startSubnegotiationLocal();
/*      */ 
/*      */         
/*  278 */         if (subneg != null)
/*      */         {
/*  280 */           _sendSubnegotiation(subneg);
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void _setDo(int option) throws IOException {
/*  295 */     this._options[option] = this._options[option] | 0x2;
/*      */ 
/*      */     
/*  298 */     if (_requestedDo(option))
/*      */     {
/*  300 */       if (this.optionHandlers[option] != null) {
/*      */         
/*  302 */         this.optionHandlers[option].setDo(true);
/*      */         
/*  304 */         int[] subneg = this.optionHandlers[option].startSubnegotiationRemote();
/*      */ 
/*      */         
/*  307 */         if (subneg != null)
/*      */         {
/*  309 */           _sendSubnegotiation(subneg);
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void _setWantWill(int option) {
/*  323 */     this._options[option] = this._options[option] | 0x4;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void _setWantDo(int option) {
/*  333 */     this._options[option] = this._options[option] | 0x8;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void _setWont(int option) {
/*  343 */     this._options[option] = this._options[option] & 0xFFFFFFFE;
/*      */ 
/*      */     
/*  346 */     if (this.optionHandlers[option] != null)
/*      */     {
/*  348 */       this.optionHandlers[option].setWill(false);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void _setDont(int option) {
/*  360 */     this._options[option] = this._options[option] & 0xFFFFFFFD;
/*      */ 
/*      */     
/*  363 */     if (this.optionHandlers[option] != null)
/*      */     {
/*  365 */       this.optionHandlers[option].setDo(false);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void _setWantWont(int option) {
/*  377 */     this._options[option] = this._options[option] & 0xFFFFFFFB;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void _setWantDont(int option) {
/*  387 */     this._options[option] = this._options[option] & 0xFFFFFFF7;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void _processCommand(int command) {
/*  402 */     if (this.__notifhand != null)
/*      */     {
/*  404 */       this.__notifhand.receivedNegotiation(5, command);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void _processDo(int option) throws IOException {
/*  423 */     if (this.__notifhand != null)
/*      */     {
/*  425 */       this.__notifhand.receivedNegotiation(1, option);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  430 */     boolean acceptNewState = false;
/*      */ 
/*      */ 
/*      */     
/*  434 */     if (this.optionHandlers[option] != null) {
/*      */       
/*  436 */       acceptNewState = this.optionHandlers[option].getAcceptLocal();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*  442 */     else if (option == 24) {
/*      */       
/*  444 */       if (this.terminalType != null && this.terminalType.length() > 0)
/*      */       {
/*  446 */         acceptNewState = true;
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  454 */     if (this._willResponse[option] > 0) {
/*      */       
/*  456 */       this._willResponse[option] = this._willResponse[option] - 1;
/*  457 */       if (this._willResponse[option] > 0 && _stateIsWill(option))
/*      */       {
/*  459 */         this._willResponse[option] = this._willResponse[option] - 1;
/*      */       }
/*      */     } 
/*      */     
/*  463 */     if (this._willResponse[option] == 0)
/*      */     {
/*  465 */       if (_requestedWont(option)) {
/*      */ 
/*      */         
/*  468 */         switch (option) {
/*      */         
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  477 */         if (acceptNewState)
/*      */         {
/*  479 */           _setWantWill(option);
/*  480 */           _sendWill(option);
/*      */         }
/*      */         else
/*      */         {
/*  484 */           this._willResponse[option] = this._willResponse[option] + 1;
/*  485 */           _sendWont(option);
/*      */         
/*      */         }
/*      */       
/*      */       }
/*      */       else {
/*      */         
/*  492 */         switch (option) {
/*      */         
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       } 
/*      */     }
/*  503 */     _setWill(option);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void _processDont(int option) throws IOException {
/*  519 */     if (this.__notifhand != null)
/*      */     {
/*  521 */       this.__notifhand.receivedNegotiation(2, option);
/*      */     }
/*      */ 
/*      */     
/*  525 */     if (this._willResponse[option] > 0) {
/*      */       
/*  527 */       this._willResponse[option] = this._willResponse[option] - 1;
/*  528 */       if (this._willResponse[option] > 0 && _stateIsWont(option))
/*      */       {
/*  530 */         this._willResponse[option] = this._willResponse[option] - 1;
/*      */       }
/*      */     } 
/*      */     
/*  534 */     if (this._willResponse[option] == 0 && _requestedWill(option)) {
/*      */ 
/*      */       
/*  537 */       switch (option) {
/*      */       
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  546 */       if (_stateIsWill(option) || _requestedWill(option))
/*      */       {
/*  548 */         _sendWont(option);
/*      */       }
/*      */       
/*  551 */       _setWantWont(option);
/*      */     } 
/*      */ 
/*      */     
/*  555 */     _setWont(option);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void _processWill(int option) throws IOException {
/*  573 */     if (this.__notifhand != null)
/*      */     {
/*  575 */       this.__notifhand.receivedNegotiation(3, option);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  580 */     boolean acceptNewState = false;
/*      */ 
/*      */     
/*  583 */     if (this.optionHandlers[option] != null)
/*      */     {
/*  585 */       acceptNewState = this.optionHandlers[option].getAcceptRemote();
/*      */     }
/*      */ 
/*      */     
/*  589 */     if (this._doResponse[option] > 0) {
/*      */       
/*  591 */       this._doResponse[option] = this._doResponse[option] - 1;
/*  592 */       if (this._doResponse[option] > 0 && _stateIsDo(option))
/*      */       {
/*  594 */         this._doResponse[option] = this._doResponse[option] - 1;
/*      */       }
/*      */     } 
/*      */     
/*  598 */     if (this._doResponse[option] == 0 && _requestedDont(option)) {
/*      */ 
/*      */       
/*  601 */       switch (option) {
/*      */       
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  610 */       if (acceptNewState) {
/*      */         
/*  612 */         _setWantDo(option);
/*  613 */         _sendDo(option);
/*      */       }
/*      */       else {
/*      */         
/*  617 */         this._doResponse[option] = this._doResponse[option] + 1;
/*  618 */         _sendDont(option);
/*      */       } 
/*      */     } 
/*      */     
/*  622 */     _setDo(option);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void _processWont(int option) throws IOException {
/*  639 */     if (this.__notifhand != null)
/*      */     {
/*  641 */       this.__notifhand.receivedNegotiation(4, option);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  646 */     if (this._doResponse[option] > 0) {
/*      */       
/*  648 */       this._doResponse[option] = this._doResponse[option] - 1;
/*  649 */       if (this._doResponse[option] > 0 && _stateIsDont(option))
/*      */       {
/*  651 */         this._doResponse[option] = this._doResponse[option] - 1;
/*      */       }
/*      */     } 
/*      */     
/*  655 */     if (this._doResponse[option] == 0 && _requestedDo(option)) {
/*      */ 
/*      */       
/*  658 */       switch (option) {
/*      */       
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  667 */       if (_stateIsDo(option) || _requestedDo(option))
/*      */       {
/*  669 */         _sendDont(option);
/*      */       }
/*      */       
/*  672 */       _setWantDont(option);
/*      */     } 
/*      */ 
/*      */     
/*  676 */     _setDont(option);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void _processSuboption(int[] suboption, int suboptionLength) throws IOException {
/*  696 */     if (suboptionLength > 0)
/*      */     {
/*  698 */       if (this.optionHandlers[suboption[0]] != null) {
/*      */         
/*  700 */         int[] responseSuboption = this.optionHandlers[suboption[0]].answerSubnegotiation(suboption, suboptionLength);
/*      */ 
/*      */         
/*  703 */         _sendSubnegotiation(responseSuboption);
/*      */ 
/*      */       
/*      */       }
/*  707 */       else if (suboptionLength > 1) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  717 */         if (suboption[0] == 24 && suboption[1] == 1)
/*      */         {
/*      */           
/*  720 */           _sendTerminalType();
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final synchronized void _sendTerminalType() throws IOException {
/*  740 */     if (this.terminalType != null) {
/*      */       
/*  742 */       this._output_.write(_COMMAND_SB);
/*  743 */       this._output_.write(_COMMAND_IS);
/*  744 */       this._output_.write(this.terminalType.getBytes(getCharset()));
/*  745 */       this._output_.write(_COMMAND_SE);
/*  746 */       this._output_.flush();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final synchronized void _sendSubnegotiation(int[] subn) throws IOException {
/*  770 */     if (subn != null) {
/*      */       
/*  772 */       this._output_.write(_COMMAND_SB);
/*      */       
/*  774 */       for (int element : subn) {
/*      */         
/*  776 */         byte b = (byte)element;
/*  777 */         if (b == -1) {
/*  778 */           this._output_.write(b);
/*      */         }
/*  780 */         this._output_.write(b);
/*      */       } 
/*  782 */       this._output_.write(_COMMAND_SE);
/*      */ 
/*      */       
/*  785 */       this._output_.flush();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final synchronized void _sendCommand(byte cmd) throws IOException {
/*  800 */     this._output_.write(255);
/*  801 */     this._output_.write(cmd);
/*  802 */     this._output_.flush();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final synchronized void _processAYTResponse() {
/*  811 */     if (!this.aytFlag)
/*      */     {
/*  813 */       synchronized (this.aytMonitor) {
/*      */         
/*  815 */         this.aytFlag = true;
/*  816 */         this.aytMonitor.notifyAll();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _connectAction_() throws IOException {
/*      */     int ii;
/*  831 */     for (ii = 0; ii < 256; ii++) {
/*      */       
/*  833 */       this._doResponse[ii] = 0;
/*  834 */       this._willResponse[ii] = 0;
/*  835 */       this._options[ii] = 0;
/*  836 */       if (this.optionHandlers[ii] != null) {
/*      */         
/*  838 */         this.optionHandlers[ii].setDo(false);
/*  839 */         this.optionHandlers[ii].setWill(false);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  844 */     super._connectAction_();
/*  845 */     this._input_ = new BufferedInputStream(this._input_);
/*  846 */     this._output_ = new BufferedOutputStream(this._output_);
/*      */ 
/*      */     
/*  849 */     for (ii = 0; ii < 256; ii++) {
/*      */       
/*  851 */       if (this.optionHandlers[ii] != null) {
/*      */         
/*  853 */         if (this.optionHandlers[ii].getInitLocal())
/*      */         {
/*  855 */           _requestWill(this.optionHandlers[ii].getOptionCode());
/*      */         }
/*      */         
/*  858 */         if (this.optionHandlers[ii].getInitRemote())
/*      */         {
/*  860 */           _requestDo(this.optionHandlers[ii].getOptionCode());
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final synchronized void _sendDo(int option) throws IOException {
/*  880 */     this._output_.write(_COMMAND_DO);
/*  881 */     this._output_.write(option);
/*      */ 
/*      */     
/*  884 */     this._output_.flush();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final synchronized void _requestDo(int option) throws IOException {
/*  897 */     if ((this._doResponse[option] == 0 && _stateIsDo(option)) || _requestedDo(option)) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  902 */     _setWantDo(option);
/*  903 */     this._doResponse[option] = this._doResponse[option] + 1;
/*  904 */     _sendDo(option);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final synchronized void _sendDont(int option) throws IOException {
/*  920 */     this._output_.write(_COMMAND_DONT);
/*  921 */     this._output_.write(option);
/*      */ 
/*      */     
/*  924 */     this._output_.flush();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final synchronized void _requestDont(int option) throws IOException {
/*  937 */     if ((this._doResponse[option] == 0 && _stateIsDont(option)) || _requestedDont(option)) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  942 */     _setWantDont(option);
/*  943 */     this._doResponse[option] = this._doResponse[option] + 1;
/*  944 */     _sendDont(option);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final synchronized void _sendWill(int option) throws IOException {
/*  961 */     this._output_.write(_COMMAND_WILL);
/*  962 */     this._output_.write(option);
/*      */ 
/*      */     
/*  965 */     this._output_.flush();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final synchronized void _requestWill(int option) throws IOException {
/*  978 */     if ((this._willResponse[option] == 0 && _stateIsWill(option)) || _requestedWill(option)) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  983 */     _setWantWill(option);
/*  984 */     this._doResponse[option] = this._doResponse[option] + 1;
/*  985 */     _sendWill(option);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final synchronized void _sendWont(int option) throws IOException {
/* 1001 */     this._output_.write(_COMMAND_WONT);
/* 1002 */     this._output_.write(option);
/*      */ 
/*      */     
/* 1005 */     this._output_.flush();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final synchronized void _requestWont(int option) throws IOException {
/* 1018 */     if ((this._willResponse[option] == 0 && _stateIsWont(option)) || _requestedWont(option)) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/* 1023 */     _setWantWont(option);
/* 1024 */     this._doResponse[option] = this._doResponse[option] + 1;
/* 1025 */     _sendWont(option);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final synchronized void _sendByte(int b) throws IOException {
/* 1037 */     this._output_.write(b);
/*      */ 
/*      */     
/* 1040 */     _spyWrite(b);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final boolean _sendAYT(long timeout) throws IOException, IllegalArgumentException, InterruptedException {
/* 1058 */     boolean retValue = false;
/* 1059 */     synchronized (this.aytMonitor) {
/*      */       
/* 1061 */       synchronized (this) {
/*      */         
/* 1063 */         this.aytFlag = false;
/* 1064 */         this._output_.write(_COMMAND_AYT);
/* 1065 */         this._output_.flush();
/*      */       } 
/* 1067 */       this.aytMonitor.wait(timeout);
/* 1068 */       if (!this.aytFlag) {
/*      */         
/* 1070 */         retValue = false;
/* 1071 */         this.aytFlag = true;
/*      */       }
/*      */       else {
/*      */         
/* 1075 */         retValue = true;
/*      */       } 
/*      */     } 
/*      */     
/* 1079 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void addOptionHandler(TelnetOptionHandler opthand) throws InvalidTelnetOptionException, IOException {
/* 1095 */     int optcode = opthand.getOptionCode();
/* 1096 */     if (TelnetOption.isValidOption(optcode)) {
/*      */       
/* 1098 */       if (this.optionHandlers[optcode] == null) {
/*      */         
/* 1100 */         this.optionHandlers[optcode] = opthand;
/* 1101 */         if (isConnected())
/*      */         {
/* 1103 */           if (opthand.getInitLocal())
/*      */           {
/* 1105 */             _requestWill(optcode);
/*      */           }
/*      */           
/* 1108 */           if (opthand.getInitRemote())
/*      */           {
/* 1110 */             _requestDo(optcode);
/*      */           }
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 1116 */         throw new InvalidTelnetOptionException("Already registered option", optcode);
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1122 */       throw new InvalidTelnetOptionException("Invalid Option Code", optcode);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void deleteOptionHandler(int optcode) throws InvalidTelnetOptionException, IOException {
/* 1137 */     if (TelnetOption.isValidOption(optcode)) {
/*      */       
/* 1139 */       if (this.optionHandlers[optcode] == null)
/*      */       {
/* 1141 */         throw new InvalidTelnetOptionException("Unregistered option", optcode);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1146 */       TelnetOptionHandler opthand = this.optionHandlers[optcode];
/* 1147 */       this.optionHandlers[optcode] = null;
/*      */       
/* 1149 */       if (opthand.getWill())
/*      */       {
/* 1151 */         _requestWont(optcode);
/*      */       }
/*      */       
/* 1154 */       if (opthand.getDo())
/*      */       {
/* 1156 */         _requestDont(optcode);
/*      */       
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/* 1162 */       throw new InvalidTelnetOptionException("Invalid Option Code", optcode);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void _registerSpyStream(OutputStream spystream) {
/* 1178 */     this.spyStream = spystream;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void _stopSpyStream() {
/* 1187 */     this.spyStream = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void _spyRead(int ch) {
/* 1197 */     OutputStream spy = this.spyStream;
/* 1198 */     if (spy != null) {
/*      */       
/*      */       try {
/*      */         
/* 1202 */         if (ch != 13)
/*      */         {
/* 1204 */           if (ch == 10)
/*      */           {
/* 1206 */             spy.write(13);
/*      */           }
/* 1208 */           spy.write(ch);
/* 1209 */           spy.flush();
/*      */         }
/*      */       
/* 1212 */       } catch (IOException e) {
/*      */         
/* 1214 */         this.spyStream = null;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void _spyWrite(int ch) {
/* 1226 */     if (!_stateIsDo(1) || !_requestedDo(1)) {
/*      */ 
/*      */       
/* 1229 */       OutputStream spy = this.spyStream;
/* 1230 */       if (spy != null) {
/*      */         
/*      */         try {
/*      */           
/* 1234 */           spy.write(ch);
/* 1235 */           spy.flush();
/*      */         }
/* 1237 */         catch (IOException e) {
/*      */           
/* 1239 */           this.spyStream = null;
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void registerNotifHandler(TelnetNotificationHandler notifhand) {
/* 1254 */     this.__notifhand = notifhand;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void unregisterNotifHandler() {
/* 1263 */     this.__notifhand = null;
/*      */   }
/*      */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\telnet\Telnet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */