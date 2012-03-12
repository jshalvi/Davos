package com.shalvi.davos.http;

/**
 * Builder used to create Request objects for testing purposes only.
 * 
 * @author jshalvi
 *
 */
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
  public void setHeaderField(HeaderField field) {
      request.setHeaderField(field);
  }
  public void setPostdata(String key, String val) {
      request.setPostdata(key, val);
  }
  
  public void initializeDefaults() {
    setMethod(RequestMethod.GET);
    setRequestURI("/");
    setHTTPVersion(HTTPVersion.VERSION_1_1);
    setValid(true);
    request.setHeaderField(new HeaderField(HeaderFieldName.HOST, "www.test.com"));
  }
  public Request toNewRequest() {
    return new Request(request);
  }
}
