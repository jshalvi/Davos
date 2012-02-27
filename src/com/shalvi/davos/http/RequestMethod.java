package com.shalvi.davos.http;

public enum RequestMethod {
  GET ("GET"),
  HEAD ("HEAD"),
  POST ("POST"),
  DELETE ("DELETE"),
  UNSUPPORTED ("");
  
  private String name;
  RequestMethod(String name) {
    this.name = name;
  }
  
  public String toString() {
    return name;
  }
}
