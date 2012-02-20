package com.shalvi.davos.http;

public class Request {

  private String request;
  
  public Request(String request) {
    this.request = request;
  }
  
  public Method getMethod() {
    return Method.UNSUPPORTED;
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
