package com.shalvi.davos.http;

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
