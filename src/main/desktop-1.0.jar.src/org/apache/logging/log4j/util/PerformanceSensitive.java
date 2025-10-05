package org.apache.logging.log4j.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
public @interface PerformanceSensitive {
  String[] value() default {""};
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4\\util\PerformanceSensitive.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */