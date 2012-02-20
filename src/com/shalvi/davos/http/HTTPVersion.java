package com.shalvi.davos.http;

public enum HTTPVersion {
  VERSION_1_0 ("1.0"),
  VERSION_1_1 ("1.1"),
  UNSUPPORTED ("");
  
  private String version;
  
  HTTPVersion(String version) {
    this.version = version;
  }
  
  public String toString() {
    return version;
  }
}
