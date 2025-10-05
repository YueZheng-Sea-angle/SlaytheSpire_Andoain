package org.apache.commons.net.ftp;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public interface FTPFileEntryParser {
  FTPFile parseFTPEntry(String paramString);
  
  String readNextEntry(BufferedReader paramBufferedReader) throws IOException;
  
  List<String> preParse(List<String> paramList);
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\ftp\FTPFileEntryParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */