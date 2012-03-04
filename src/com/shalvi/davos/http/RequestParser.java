package com.shalvi.davos.http;

import java.io.BufferedReader;
import java.io.IOException;

enum ParserState {
  REQUEST_LINE() {
    public Request parse(BufferedReader reader, Request request) {
      Request r = new Request(request);
      Request rParsed;
      String requestLine = RequestParser.readLine(reader);
      
      rParsed = RequestParser.parseRequestLine(requestLine);
      r.setMethod(rParsed.getMethod());
      r.setRequestURI(rParsed.getRequestURI());
      r.setHTTPVersion(rParsed.getHTTPVersion());
      
      r.setValid(
          !(r.getMethod() == RequestMethod.UNSUPPORTED || 
          r.getRequestURI() == "" ||
          r.getHTTPVersion() == HTTPVersion.UNSUPPORTED));

      return r;
    }
    
    public ParserState getNext(Request request) {
      return REQUEST_PARAMS;
    }
  },
  REQUEST_PARAMS() {
    public Request parse(BufferedReader reader, Request request) {
      Request r = new Request(request);
      String line = RequestParser.readLine(reader);
      
      if (line == null) {
        r.setValid(false);
      } else {
        while (line != null) {
          if (line == "") {
            break;
          }
          line = RequestParser.readLine(reader);
        }
      }
      return r;
    }
            
    public ParserState getNext(Request request) {
      return null;
    }
  },
  REQUEST_POSTDATA() {
    public Request parse(BufferedReader reader, Request request) {
      return null;
    }
    
    public ParserState getNext(Request request) {
      return null;
    }
    
  },
  END() {
    public Request parse(BufferedReader reader, Request request) {
      Request r = new Request(request);
      String line = RequestParser.readLine(reader);

      r.setValid(line == "");
      return r;
    }
    
    public ParserState getNext(Request request) {
      return null;
    }
  };
  
  public abstract Request parse(BufferedReader reader, Request request);
  public abstract ParserState getNext(Request request);
}

public class RequestParser {
  
  /*
   *
   * parsing states
   * 
   */
  static boolean validateRequestLine(String requestLine) {
    return  !(requestLine == null || requestLine.length() == 0 || requestLine.startsWith(" "));

  }
  
  static String readLine(BufferedReader reader) {
    char currentChar;
    char prevChar = '\0';
    // boolean isEmptyLine = true;
    String out = "";
    
    if (reader == null) {
      throw new IllegalArgumentException("BufferedReader must not be null");
    }
    
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
  static RequestMethod parseMethod(String requestLine) {
    RequestMethod method = RequestMethod.UNSUPPORTED;
    String[] tokens;
    String methodToken;
    
    if (!validateRequestLine(requestLine)) {
      
      return RequestMethod.UNSUPPORTED;
    }

    tokens = requestLine.split(" ");    
    if (tokens.length == 0) {
      return RequestMethod.UNSUPPORTED;
    }
    methodToken = tokens[0];
    
    for (RequestMethod m : RequestMethod.values()) {
      if (m.toString().equals(methodToken)) {
        method = m;
        break;
      }
    }
    return method;
  }
  
  static String parseURILocator(String requestLine) {
    String[] tokens;
    
    if (!validateRequestLine(requestLine)) {
      return "";
    }
    
    tokens = requestLine.split("\\s+");
    if (tokens.length < 2) {
      return "";
    }
    
    return tokens[1];
  }
  
  static HTTPVersion parseHTTPVersion(String requestLine) {
    String[] tokens;
    String httpVersionToken;
    HTTPVersion httpVersion = HTTPVersion.UNSUPPORTED;

    if (!validateRequestLine(requestLine)) {
      return HTTPVersion.UNSUPPORTED;
    }
    
    tokens = requestLine.split("\\s+");    
    if (tokens.length < 3) {
      return HTTPVersion.UNSUPPORTED;
    }
    httpVersionToken = tokens[2];

    for (HTTPVersion v : HTTPVersion.values()) {
      if (v.toString().equals(httpVersionToken)) {
        httpVersion = v;
        break;
      }
    }
    
    return httpVersion;
  }
  
  static Request parseRequestLine(String requestLine) {
    Request request = new Request();
    
    if (!validateRequestLine(requestLine)) {
      return request;
    }
    
    request.setMethod(parseMethod(requestLine));
    request.setRequestURI(parseURILocator(requestLine));
    request.setHTTPVersion(parseHTTPVersion(requestLine));
    if (
        request.getMethod() == RequestMethod.UNSUPPORTED || 
        request.getRequestURI() == "" ||
        request.getHTTPVersion() == HTTPVersion.UNSUPPORTED) {
      
      request = new Request();
    }

    return request;
  }
  
  static RequestParameter parseRequestParameter(String paramLine) {
      RequestParameter param = new RequestParameter(RequestParameterKey.UNSUPPORTED, null);
      
      if (paramLine == null || paramLine.compareTo("") == 0) {
          return param;
      }
      
      String[] paramLineArr = paramLine.split(":");
      if (paramLineArr.length != 2) {
          return param;
      }
      
      String paramLineKey = paramLineArr[0];
      String paramLineVal = paramLineArr[1].trim();
      
      for (RequestParameterKey key : RequestParameterKey.values()) {
          if (paramLineKey.compareToIgnoreCase(key.toString()) == 0) {
              param = new RequestParameter(key, paramLineVal);
              break;
          }
      }
      
      return param;
  }
  
  public static Request parseRequest(BufferedReader reader) {
    Request r = new Request();
    ParserState state = ParserState.REQUEST_LINE;
    
    while (state != null) {
      r = state.parse(reader, r);
      if (r.isValid()) {
        state = state.getNext(r);
      } else {
        break;
      }
    }
    
    return r;
  }
}
