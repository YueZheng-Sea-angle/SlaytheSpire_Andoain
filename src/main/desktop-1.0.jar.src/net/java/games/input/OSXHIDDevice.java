/*     */ package net.java.games.input;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class OSXHIDDevice
/*     */ {
/*  54 */   private static final Logger log = Logger.getLogger(OSXHIDDevice.class.getName());
/*     */   
/*     */   private static final int AXIS_DEFAULT_MIN_VALUE = 0;
/*     */   
/*     */   private static final int AXIS_DEFAULT_MAX_VALUE = 65536;
/*     */   
/*     */   private static final String kIOHIDTransportKey = "Transport";
/*     */   
/*     */   private static final String kIOHIDVendorIDKey = "VendorID";
/*     */   
/*     */   private static final String kIOHIDVendorIDSourceKey = "VendorIDSource";
/*     */   private static final String kIOHIDProductIDKey = "ProductID";
/*     */   private static final String kIOHIDVersionNumberKey = "VersionNumber";
/*     */   private static final String kIOHIDManufacturerKey = "Manufacturer";
/*     */   private static final String kIOHIDProductKey = "Product";
/*     */   private static final String kIOHIDSerialNumberKey = "SerialNumber";
/*     */   private static final String kIOHIDCountryCodeKey = "CountryCode";
/*     */   private static final String kIOHIDLocationIDKey = "LocationID";
/*     */   private static final String kIOHIDDeviceUsageKey = "DeviceUsage";
/*     */   private static final String kIOHIDDeviceUsagePageKey = "DeviceUsagePage";
/*     */   private static final String kIOHIDDeviceUsagePairsKey = "DeviceUsagePairs";
/*     */   private static final String kIOHIDPrimaryUsageKey = "PrimaryUsage";
/*     */   private static final String kIOHIDPrimaryUsagePageKey = "PrimaryUsagePage";
/*     */   private static final String kIOHIDMaxInputReportSizeKey = "MaxInputReportSize";
/*     */   private static final String kIOHIDMaxOutputReportSizeKey = "MaxOutputReportSize";
/*     */   private static final String kIOHIDMaxFeatureReportSizeKey = "MaxFeatureReportSize";
/*     */   private static final String kIOHIDElementKey = "Elements";
/*     */   private static final String kIOHIDElementCookieKey = "ElementCookie";
/*     */   private static final String kIOHIDElementTypeKey = "Type";
/*     */   private static final String kIOHIDElementCollectionTypeKey = "CollectionType";
/*     */   private static final String kIOHIDElementUsageKey = "Usage";
/*     */   private static final String kIOHIDElementUsagePageKey = "UsagePage";
/*     */   private static final String kIOHIDElementMinKey = "Min";
/*     */   private static final String kIOHIDElementMaxKey = "Max";
/*     */   private static final String kIOHIDElementScaledMinKey = "ScaledMin";
/*     */   private static final String kIOHIDElementScaledMaxKey = "ScaledMax";
/*     */   private static final String kIOHIDElementSizeKey = "Size";
/*     */   private static final String kIOHIDElementReportSizeKey = "ReportSize";
/*     */   private static final String kIOHIDElementReportCountKey = "ReportCount";
/*     */   private static final String kIOHIDElementReportIDKey = "ReportID";
/*     */   private static final String kIOHIDElementIsArrayKey = "IsArray";
/*     */   private static final String kIOHIDElementIsRelativeKey = "IsRelative";
/*     */   private static final String kIOHIDElementIsWrappingKey = "IsWrapping";
/*     */   private static final String kIOHIDElementIsNonLinearKey = "IsNonLinear";
/*     */   private static final String kIOHIDElementHasPreferredStateKey = "HasPreferredState";
/*     */   private static final String kIOHIDElementHasNullStateKey = "HasNullState";
/*     */   private static final String kIOHIDElementUnitKey = "Unit";
/*     */   private static final String kIOHIDElementUnitExponentKey = "UnitExponent";
/*     */   private static final String kIOHIDElementNameKey = "Name";
/*     */   private static final String kIOHIDElementValueLocationKey = "ValueLocation";
/*     */   private static final String kIOHIDElementDuplicateIndexKey = "DuplicateIndex";
/*     */   private static final String kIOHIDElementParentCollectionKey = "ParentCollection";
/*     */   private final long device_address;
/*     */   private final long device_interface_address;
/*     */   private final Map properties;
/*     */   private boolean released;
/*     */   
/*     */   public OSXHIDDevice(long device_address, long device_interface_address) throws IOException {
/* 112 */     this.device_address = device_address;
/* 113 */     this.device_interface_address = device_interface_address;
/* 114 */     this.properties = getDeviceProperties();
/* 115 */     open();
/*     */   }
/*     */   
/*     */   public final Controller.PortType getPortType() {
/* 119 */     String transport = (String)this.properties.get("Transport");
/* 120 */     if (transport == null)
/* 121 */       return Controller.PortType.UNKNOWN; 
/* 122 */     if (transport.equals("USB")) {
/* 123 */       return Controller.PortType.USB;
/*     */     }
/* 125 */     return Controller.PortType.UNKNOWN;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getProductName() {
/* 130 */     return (String)this.properties.get("Product");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final OSXHIDElement createElementFromElementProperties(Map element_properties) {
/* 138 */     long element_cookie = getLongFromProperties(element_properties, "ElementCookie");
/* 139 */     int element_type_id = getIntFromProperties(element_properties, "Type");
/* 140 */     ElementType element_type = ElementType.map(element_type_id);
/* 141 */     int min = (int)getLongFromProperties(element_properties, "Min", 0L);
/* 142 */     int max = (int)getLongFromProperties(element_properties, "Max", 65536L);
/*     */ 
/*     */     
/* 145 */     UsagePair device_usage_pair = getUsagePair();
/* 146 */     boolean default_relative = (device_usage_pair != null && (device_usage_pair.getUsage() == GenericDesktopUsage.POINTER || device_usage_pair.getUsage() == GenericDesktopUsage.MOUSE));
/*     */     
/* 148 */     boolean is_relative = getBooleanFromProperties(element_properties, "IsRelative", default_relative);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 153 */     int usage = getIntFromProperties(element_properties, "Usage");
/* 154 */     int usage_page = getIntFromProperties(element_properties, "UsagePage");
/* 155 */     UsagePair usage_pair = createUsagePair(usage_page, usage);
/* 156 */     if (usage_pair == null || (element_type != ElementType.INPUT_MISC && element_type != ElementType.INPUT_BUTTON && element_type != ElementType.INPUT_AXIS))
/*     */     {
/* 158 */       return null;
/*     */     }
/* 160 */     return new OSXHIDElement(this, usage_pair, element_cookie, element_type, min, max, is_relative);
/*     */   }
/*     */ 
/*     */   
/*     */   private final void addElements(List elements, Map properties) {
/* 165 */     Object[] elements_properties = (Object[])properties.get("Elements");
/* 166 */     if (elements_properties == null)
/*     */       return; 
/* 168 */     for (int i = 0; i < elements_properties.length; i++) {
/* 169 */       Map element_properties = (Map)elements_properties[i];
/* 170 */       OSXHIDElement element = createElementFromElementProperties(element_properties);
/* 171 */       if (element != null) {
/* 172 */         elements.add(element);
/*     */       }
/* 174 */       addElements(elements, element_properties);
/*     */     } 
/*     */   }
/*     */   
/*     */   public final List getElements() {
/* 179 */     List elements = new ArrayList();
/* 180 */     addElements(elements, this.properties);
/* 181 */     return elements;
/*     */   }
/*     */   
/*     */   private static final long getLongFromProperties(Map properties, String key, long default_value) {
/* 185 */     Long long_obj = (Long)properties.get(key);
/* 186 */     if (long_obj == null)
/* 187 */       return default_value; 
/* 188 */     return long_obj.longValue();
/*     */   }
/*     */   
/*     */   private static final boolean getBooleanFromProperties(Map properties, String key, boolean default_value) {
/* 192 */     return (getLongFromProperties(properties, key, default_value ? 1L : 0L) != 0L);
/*     */   }
/*     */   
/*     */   private static final int getIntFromProperties(Map properties, String key) {
/* 196 */     return (int)getLongFromProperties(properties, key);
/*     */   }
/*     */   
/*     */   private static final long getLongFromProperties(Map properties, String key) {
/* 200 */     Long long_obj = (Long)properties.get(key);
/* 201 */     return long_obj.longValue();
/*     */   }
/*     */   
/*     */   private static final UsagePair createUsagePair(int usage_page_id, int usage_id) {
/* 205 */     UsagePage usage_page = UsagePage.map(usage_page_id);
/* 206 */     if (usage_page != null) {
/* 207 */       Usage usage = usage_page.mapUsage(usage_id);
/* 208 */       if (usage != null)
/* 209 */         return new UsagePair(usage_page, usage); 
/*     */     } 
/* 211 */     return null;
/*     */   }
/*     */   
/*     */   public final UsagePair getUsagePair() {
/* 215 */     int usage_page_id = getIntFromProperties(this.properties, "PrimaryUsagePage");
/* 216 */     int usage_id = getIntFromProperties(this.properties, "PrimaryUsage");
/* 217 */     return createUsagePair(usage_page_id, usage_id);
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
/*     */   private final void dumpProperties() {
/* 243 */     log.info(toString());
/* 244 */     dumpMap("", this.properties);
/*     */   }
/*     */   
/*     */   private static final void dumpArray(String prefix, Object[] array) {
/* 248 */     log.info(prefix + "{");
/* 249 */     for (int i = 0; i < array.length; i++) {
/* 250 */       dumpObject(prefix + "\t", array[i]);
/* 251 */       log.info(prefix + ",");
/*     */     } 
/* 253 */     log.info(prefix + "}");
/*     */   }
/*     */   
/*     */   private static final void dumpMap(String prefix, Map map) {
/* 257 */     Iterator keys = map.keySet().iterator();
/* 258 */     while (keys.hasNext()) {
/* 259 */       Object key = keys.next();
/* 260 */       Object value = map.get(key);
/* 261 */       dumpObject(prefix, key);
/* 262 */       dumpObject(prefix + "\t", value);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static final void dumpObject(String prefix, Object obj) {
/* 267 */     if (obj instanceof Long) {
/* 268 */       Long l = (Long)obj;
/* 269 */       log.info(prefix + "0x" + Long.toHexString(l.longValue()));
/* 270 */     } else if (obj instanceof Map) {
/* 271 */       dumpMap(prefix, (Map)obj);
/* 272 */     } else if (obj.getClass().isArray()) {
/* 273 */       dumpArray(prefix, (Object[])obj);
/*     */     } else {
/* 275 */       log.info(prefix + obj);
/*     */     } 
/*     */   }
/*     */   private final Map getDeviceProperties() throws IOException {
/* 279 */     return nGetDeviceProperties(this.device_address);
/*     */   }
/*     */ 
/*     */   
/*     */   public final synchronized void release() throws IOException {
/*     */     try {
/* 285 */       close();
/*     */     } finally {
/* 287 */       this.released = true;
/* 288 */       nReleaseDevice(this.device_address, this.device_interface_address);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final synchronized void getElementValue(long element_cookie, OSXEvent event) throws IOException {
/* 294 */     checkReleased();
/* 295 */     nGetElementValue(this.device_interface_address, element_cookie, event);
/*     */   }
/*     */ 
/*     */   
/*     */   public final synchronized OSXHIDQueue createQueue(int queue_depth) throws IOException {
/* 300 */     checkReleased();
/* 301 */     long queue_address = nCreateQueue(this.device_interface_address);
/* 302 */     return new OSXHIDQueue(queue_address, queue_depth);
/*     */   }
/*     */ 
/*     */   
/*     */   private final void open() throws IOException {
/* 307 */     nOpen(this.device_interface_address);
/*     */   }
/*     */ 
/*     */   
/*     */   private final void close() throws IOException {
/* 312 */     nClose(this.device_interface_address);
/*     */   }
/*     */ 
/*     */   
/*     */   private final void checkReleased() throws IOException {
/* 317 */     if (this.released)
/* 318 */       throw new IOException(); 
/*     */   }
/*     */   
/*     */   private static final native Map nGetDeviceProperties(long paramLong) throws IOException;
/*     */   
/*     */   private static final native void nReleaseDevice(long paramLong1, long paramLong2);
/*     */   
/*     */   private static final native void nGetElementValue(long paramLong1, long paramLong2, OSXEvent paramOSXEvent) throws IOException;
/*     */   
/*     */   private static final native long nCreateQueue(long paramLong) throws IOException;
/*     */   
/*     */   private static final native void nOpen(long paramLong) throws IOException;
/*     */   
/*     */   private static final native void nClose(long paramLong) throws IOException;
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\net\java\games\input\OSXHIDDevice.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */