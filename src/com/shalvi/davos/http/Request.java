package com.shalvi.davos.http;

/**
 * Represents an HTTP Request, providing information such as the Request Method (GET, POST, 
 * HEAD, DELETE), the request URI specifying the resource, and the HTTP version.  Request instances
 * are immutable value objects, and do not provide any sort of validation.
 * 
 * @author jshalvi
 *
 */
public class Request {

  private RequestMethod method;
  private String uri;
  private HTTPVersion version;
  private boolean valid;
  
  /**
   * Standard constructor.  Initializes fields to empty, false or unsupported.
   */
  public Request() {
    method = RequestMethod.UNSUPPORTED;
    uri = "";
    version = HTTPVersion.UNSUPPORTED;
    valid = false;
  }
  
  /**
   * Copy constructor.  Copies fields from specified Request object.
   * @param r object to copy.
   */
  public Request(Request r) {
    method = r.getMethod();
    uri = r.getRequestURI();
    version = r.getHTTPVersion();
    valid = r.isValid();
  }
  
  /**
   * Sets the RequestMethod of the Request.
   * @param m
   */
  void setMethod(RequestMethod m) {
    method = m;
  }
  
  /**
   * Returns the RequestMethod.
   * @return RequestMethod of this Request.
   */
  public RequestMethod getMethod() {
    return method;
  }
  
  /**
   * Sets the request URI.
   * @param s
   */
  void setRequestURI(String s) {
    uri = s;
  }
  
  /**
   * Returns the request URI.
   * @return
   */
  public String getRequestURI() {
    return uri;
  }
  
  /**
   * Sets the HTTP version of the request.
   * @param v
   */
  void setHTTPVersion(HTTPVersion v) {
    version = v;
  }
  
  /**
   * Returns the HTTP version of the request.
   * @return
   */
  public HTTPVersion getHTTPVersion() {
    return version;
  }
  
  
  void setValid(boolean v) {
    valid = v;
  }
  
  /**
   * Set to true if the request has a valid Method, URI, and HTTP Version.  Otherwise false.
   * @return
   */
  public boolean isValid() {
    return valid;
  }
  
  public String toString() {
    return "[" + method.toString() + " " + uri + "]";
  }
}
