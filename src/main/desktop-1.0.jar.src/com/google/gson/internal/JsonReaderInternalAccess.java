package com.google.gson.internal;

import com.google.gson.stream.JsonReader;
import java.io.IOException;

public abstract class JsonReaderInternalAccess {
  public static JsonReaderInternalAccess INSTANCE;
  
  public abstract void promoteNameToValue(JsonReader paramJsonReader) throws IOException;
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\com\google\gson\internal\JsonReaderInternalAccess.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */