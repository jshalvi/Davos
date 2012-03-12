package com.shalvi.davos.http;

import java.io.IOException;
import java.io.Reader;

public class RequestReader extends Reader {

    private Reader reader;
    
    public RequestReader(Reader reader) {
        if (reader == null) {
            throw new IllegalArgumentException();
        }
        this.reader = reader;
    }
    
    @Override
    public void close() throws IOException {
        reader.close();
    }

    @Override
    public int read(char[] arg0, int arg1, int arg2) throws IOException {
        return reader.read(arg0, arg1, arg2);
    }
    
    public String readLine() {
        char currentChar;
        char prevChar = '\0';
        // boolean isEmptyLine = true;
        String out = "";
        
        try {
          
          while (true) {
            currentChar = (char) reader.read();
            
            if (currentChar == (char) -1) {
              if (prevChar == '\0') {
                return null;
              } else {
                break;
              }
            }
            
            if (prevChar == '\r' && currentChar == '\n') {
              if (prevChar == '\0') {
                return null;
              } else {
                break;
              }
            }
            
            if (currentChar != '\n' && currentChar != '\r') {

              out += currentChar;
            }
            prevChar = currentChar;
          }
          
        } catch (IOException e) {
          e.printStackTrace();
        }
        return out;
      }

}
