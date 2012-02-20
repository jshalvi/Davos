package com.shalvi.davos.http;

public enum Method {
  GET ("GET"),
  HEAD ("HEAD"),
  POST ("POST"),
  DELETE ("DELETE"),
  UNSUPPORTED ("");
  
  private String name;
  Method(String name) {
    this.name = name;
  }
  
  public String toString() {
    return name;
  }
}
