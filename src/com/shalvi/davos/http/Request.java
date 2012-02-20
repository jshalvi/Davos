package com.shalvi.davos.http;

public class Request {

  private Method method;
  
  public Request() {
  }
  
  void setMethod(Method m) {
    this.method = m;
  }
  
  public Method getMethod() {
    return method;
  }
  
  public String getRequestURI() {
    return null;
  }
  
  public HTTPVersion getHTTPVersion() {
    return HTTPVersion.UNSUPPORTED;
  }
  
  public boolean parse(String request) {
    return false;
  }
  
  public boolean isValid() {
    return false;
  }
}
