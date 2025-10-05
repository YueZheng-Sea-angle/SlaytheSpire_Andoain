package com.google.gson;

import java.lang.reflect.Type;

public interface JsonDeserializer<T> {
  T deserialize(JsonElement paramJsonElement, Type paramType, JsonDeserializationContext paramJsonDeserializationContext) throws JsonParseException;
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\com\google\gson\JsonDeserializer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */