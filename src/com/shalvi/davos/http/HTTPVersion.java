package com.shalvi.davos.http;

/**
 * Enum containing supported HTTPVersions.
 * @author jshalvi
 *
 */
public enum HTTPVersion {
  VERSION_1_0 ("HTTP/1.0"),
  VERSION_1_1 ("HTTP/1.1"),
  UNSUPPORTED ("");
  
  private String version;
  
  HTTPVersion(String version) {
    this.version = version;
  }
  
  public String toString() {
    return version;
  }
}
