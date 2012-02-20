package com.shalvi.davos.http;

public class RequestParser {
  
  static boolean validateRequestLine(String requestLine) {
    return  !(requestLine == null || requestLine.length() == 0 || requestLine.startsWith(" "));

  }
  static Method parseMethod(String requestLine) {
    Method method = Method.UNSUPPORTED;
    String[] tokens;
    String methodToken;
    
    if (!validateRequestLine(requestLine)) {
      
      return Method.UNSUPPORTED;
    }

    tokens = requestLine.split(" ");    
    if (tokens.length == 0) {
      return Method.UNSUPPORTED;
    }
    methodToken = tokens[0];
    
    for (Method m : Method.values()) {
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
  
  public static Request parseRequest(String request) {
    Request r = new Request();
    
    return r;
  }

}
