package com.shalvi.davos.http;

import java.io.BufferedReader;

public class ResponseBuilder {

  private Response response;
  
  public ResponseBuilder() {
    response = new Response();
  }
  
  public void setResponseCode(ResponseCode code) {
    response.setResponseCode(code);
  }
  public void setBody(String body) {
    response.setBody(body);
  }
  public void setReader(BufferedReader reader) {
    response.setReader(reader);
  }
  public Response toResponse() {
    return response;
  }
}
