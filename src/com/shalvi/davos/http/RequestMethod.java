package com.shalvi.davos.http;

/**
 * Enum of supported request methods.
 * @author jshalvi
 *
 */
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
