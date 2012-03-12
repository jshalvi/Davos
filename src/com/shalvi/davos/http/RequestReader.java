package com.shalvi.davos.http;

import java.io.IOException;
import java.io.Reader;

/**
 * Decorates a java Reader to allow proper line-by-line reading of a request
 * stream.
 * 
 * @author jshalvi
 *
 */
public class RequestReader extends Reader {

    private Reader reader;
    
    /**
     * Constructor
     * @param reader
     * @throws IllegalArgumentException if reader is null.
     */
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
    
    /**
     * Returns a line in an HTTP request, which are terminated by CRLF rather than
     * a new line.
     * 
     * @return a line in a request or null if the end of the stream has been reached.
     *   May be an empty string such as in the case of the blank which separates 
     *   headers from the message body.  
     */
    public String readLine() {
        char currentChar;
        char prevChar = '\0';

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
