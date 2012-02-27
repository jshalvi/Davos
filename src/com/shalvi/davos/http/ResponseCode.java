package com.shalvi.davos.http;

public enum ResponseCode {
  SUCCESS_200 ("200", "OK"),
  ERROR_404 ("404", "Not found");
  
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
}
