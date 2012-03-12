package com.shalvi.davos.http;

/**
 * Enum containing supported response codes.
 * @author jshalvi
 *
 */
public enum ResponseCode {
  SUCCESSFUL_200 ("200", "OK"),
  CLIENT_ERROR_404 ("404", "Not found"),
  SERVER_ERROR_501 ("501", "Not implemented");
  
  private String code;
  private String message;
  
  ResponseCode(String code, String message) {
    this.code = code;
    this.message = message;
  }
  
  public String getStatusCode() {
    return code;
  }
  
  public String getReasonPhrase() {
    return message;
  }
  
  public String toString() {
      return code + " " + message;
  }
}
