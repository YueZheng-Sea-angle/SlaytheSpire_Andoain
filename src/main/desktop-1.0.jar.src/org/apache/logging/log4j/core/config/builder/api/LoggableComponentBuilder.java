package org.apache.logging.log4j.core.config.builder.api;

public interface LoggableComponentBuilder<T extends ComponentBuilder<T>> extends FilterableComponentBuilder<T> {
  T add(AppenderRefComponentBuilder paramAppenderRefComponentBuilder);
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\builder\api\LoggableComponentBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */