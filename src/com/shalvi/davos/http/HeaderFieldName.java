package com.shalvi.davos.http;

public enum HeaderFieldName {
    HOST ("Host"),
    USER_AGENT ("User-Agent"),
    CONTENT_LENGTH ("Content-Length"),
    CONTENT_TYPE ("Content-Type"),
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
