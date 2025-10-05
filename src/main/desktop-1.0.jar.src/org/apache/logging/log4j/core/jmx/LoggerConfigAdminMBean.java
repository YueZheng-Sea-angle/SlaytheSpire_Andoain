package org.apache.logging.log4j.core.jmx;

public interface LoggerConfigAdminMBean {
  public static final String PATTERN = "org.apache.logging.log4j2:type=%s,component=Loggers,name=%s";
  
  String getName();
  
  String getLevel();
  
  void setLevel(String paramString);
  
  boolean isAdditive();
  
  void setAdditive(boolean paramBoolean);
  
  boolean isIncludeLocation();
  
  String getFilter();
  
  String[] getAppenderRefs();
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\jmx\LoggerConfigAdminMBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */