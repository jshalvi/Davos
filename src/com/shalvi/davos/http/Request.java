package com.shalvi.davos.http;

public class Request {

  private RequestMethod method;
  private String uri;
  private HTTPVersion version;
  private boolean valid;
  
  public Request() {
    method = RequestMethod.UNSUPPORTED;
    uri = "";
    version = HTTPVersion.UNSUPPORTED;
    valid = false;
  }
  
  public Request(Request r) {
    method = r.getMethod();
    uri = r.getRequestURI();
    version = r.getHTTPVersion();
    valid = r.isValid();
  }
  
  void setMethod(RequestMethod m) {
    method = m;
  }
  
  public RequestMethod getMethod() {
    return method;
  }
  
  void setRequestURI(String s) {
    uri = s;
  }
  
  public String getRequestURI() {
    return uri;
  }
  
  void setHTTPVersion(HTTPVersion v) {
    version = v;
  }
  
  public HTTPVersion getHTTPVersion() {
    return version;
  }
  
  void setValid(boolean v) {
    valid = v;
  }
  
  public boolean isValid() {
    return valid;
  }
  
  public String toString() {
    return "[" + method.toString() + " " + uri + "]";
  }
}
