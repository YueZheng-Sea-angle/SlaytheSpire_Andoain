package org.apache.logging.log4j.core.config.builder.api;

public interface FilterableComponentBuilder<T extends ComponentBuilder<T>> extends ComponentBuilder<T> {
  T add(FilterComponentBuilder paramFilterComponentBuilder);
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\builder\api\FilterableComponentBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */