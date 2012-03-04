package com.shalvi.davos.http;

public enum RequestParameterKey {
    HOST ("Host"),
    USER_AGENT ("User-Agent"),
    CONTENT_LENGTH ("Content-Length"),
    CONTENT_TYPE ("Content-Type"),
    UNSUPPORTED ("");
    
    private String name;
    
    RequestParameterKey(String name) {
        this.name = name;
    }
    
    public String toString() {
        return this.name;
    }
}
