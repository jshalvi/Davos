package com.shalvi.davos.http;

public class RequestHeaderField {

    private RequestHeaderFieldName key;
    private String value;
    
    RequestHeaderField(RequestHeaderFieldName key, String value) {
        this.key = key;
        this.value = value;
    }
    
    public RequestHeaderFieldName getKey() {
        return key;
    }
    
    public String getValue() {
        return value;
    }
}
