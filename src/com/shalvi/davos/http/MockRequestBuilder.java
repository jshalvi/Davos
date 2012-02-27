package com.shalvi.davos.http;

public class MockRequestBuilder {

  private Request request;
  
  public MockRequestBuilder() {
    request = new Request();
  }
  public void setRequestURI(String s) {
    request.setRequestURI(s);
  }
  public void setHTTPVersion(HTTPVersion version) {
    request.setHTTPVersion(version);
  }
  public void setMethod(RequestMethod method) {
    request.setMethod(method);
  }
  public void setValid(boolean valid) {
    request.setValid(valid);
  }
  public void initializeDefaults() {
    setMethod(RequestMethod.GET);
    setRequestURI("/");
    setHTTPVersion(HTTPVersion.VERSION_1_1);
    setValid(true);
  }
  public Request toNewRequest() {
    return new Request(request);
  }
}
