package com.shalvi.davos.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an HTTP Request, providing information such as the Request Method (GET, POST, 
 * HEAD, DELETE), the request URI specifying the resource, and the HTTP version.  Request instances
 * are immutable value objects, and do not provide any sort of validation.
 * 
 * @author jshalvi
 *
 */
public class Request extends Message {

  private RequestMethod method;
  private boolean valid;
  
  private Map<String, String> postData = new HashMap<String, String>();
  
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
   * @throws IllegalArgumentException if request is null
   */
  public Request(Request r) {
      if (r == null) {
          throw new IllegalArgumentException();
      }
      
      method = r.getMethod();
      uri = r.getRequestURI();
      version = r.getHTTPVersion();
      valid = r.isValid();
      headerFields = r.getHeaderFields();
      postData = new HashMap<String, String>(r.postData);
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
  
  /**
   * Adds a postdata field to the request.
   * @param key
   * @param val
   * @throws IllegalArgumentException if the key is empty or null, or the value is null
   */
  public void setPostdata(String key, String val) {
      if (key == "" || key == null || val == null) {
          throw new IllegalArgumentException();
      }
      
      postData.put(key, val);
  }
  
  /**
   * Gets a postdata field from the request.
   * @param key
   * @return null if the key is not found, otherwise a string containing the postdata value.
   * @throws IllegalArgumentException if key is null or empty.
   */
  public String getPostdata(String key) {
      if (key == "" || key == null) {
          throw new IllegalArgumentException();
      }
      
      return postData.get(key);
  }
  
  /**
   * Returns the value of the Content-Length header.
   * @return content-length, or zero if Content-Length was not specified.
   */
  public int getContentLength() {
      int contentLen = 0;
      
      try {
          contentLen = Integer.parseInt(getHeaderField(HeaderFieldName.CONTENT_LENGTH).getValue());
      } catch (NumberFormatException e) {}
      
      return contentLen;
  }
  
  public String toString() {
      String startLine="", headers = "", postdata="";
      
      startLine =  "[" + method.toString() + " " + uri + " " + version.toString() + "]\n";
      
      for (Map.Entry<String, String> e : postData.entrySet()) {
          postdata += "\t" + e.getKey() + "=" + e.getValue() + "\n";
      }
      
      for (Map.Entry<HeaderFieldName, HeaderField> h : headerFields.entrySet()) {
          headers += "\t" + h.getValue().toString() + "\n";
      }
      
      String out = startLine;
      out += headers.length() > 0 ? "Headers:\n" + headers : "";
      out += postdata.length() > 0 ? "Postdata:\n" + postdata : "";
      return out;
  }
    
  public boolean equals(Object thatObject) {
      if(!(thatObject instanceof Request)) {
          return false;
      }
      
      Request that = (Request) thatObject;
      
      return method == that.getMethod() &&
          uri.compareTo(that.getRequestURI()) == 0 &&
          version == that.getHTTPVersion() &&
          valid == that.isValid() &&
          headerFields.equals(that.getHeaderFields()) &&
          postData.equals(that.postData);
  }
}
