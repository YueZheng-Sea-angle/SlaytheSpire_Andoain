package org.apache.logging.log4j.core.net;

import java.util.Map;

public interface Advertiser {
  Object advertise(Map<String, String> paramMap);
  
  void unadvertise(Object paramObject);
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\net\Advertiser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */