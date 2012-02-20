package com.shalvi.davos.http;

public enum Method {
  GET ("GET"),
  HEAD ("HEAD"),
  UNSUPPORTED ("");
  
  private String name;
  Method(String name) {
    this.name = name;
  }
  
  public String getName() {
    return name;
  }
}
