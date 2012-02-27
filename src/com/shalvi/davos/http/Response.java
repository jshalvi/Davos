package com.shalvi.davos.http;

import java.io.BufferedReader;

public class Response {

  private ResponseCode code;
  private String body;
  private BufferedReader reader;
  
  public Response() {}
  
  public Response(ResponseCode code) {
    this.code = code;
  }
  public Response(ResponseCode code, String body) {
    this.code = code;
    this.body = body;
  }
  public void setResponseCode(ResponseCode code) {
    this.code = code;
  }
  
  public ResponseCode getResponseCode() {
    return code;
  }
  
  public void setReader(BufferedReader reader) {
    this.reader = reader;
  }
  public BufferedReader getReader() {
    return reader;
  }
  public void setBody(String body) {
    this.body = body;
  }
  public String getBody() {
    return body;
  }
  
  
  public String toString() {
    return HTTPVersion.VERSION_1_1.toString() + " " +
      code.getStatusCode() + " " +
      code.getReasonPhrase() + "\r\n\r\n" +
      body;
  }
}
