package com.shalvi.davos.http;

/**
 * Enum containing supported header fields.
 * 
 * @author jshalvi
 *
 */
public enum HeaderFieldName {
    HOST ("Host"),
    USER_AGENT ("User-Agent"),
    CONTENT_LENGTH ("Content-Length"),
    CONTENT_TYPE ("Content-Type"),
    COOKIE ("Cookie"),
    SET_COOKIE ("Set-Cookie"),
    UNSUPPORTED (""),
    UNSPECIFIED ("");
    
    private String name;
    
    HeaderFieldName(String name) {
        this.name = name;
    }
    
    public String toString() {
        return this.name;
    }
}
