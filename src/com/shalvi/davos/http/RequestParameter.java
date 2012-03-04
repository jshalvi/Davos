package com.shalvi.davos.http;

public class RequestParameter {

    private RequestParameterKey key;
    private String value;
    
    RequestParameter(RequestParameterKey key, String value) {
        this.key = key;
        this.value = value;
    }
    
    public RequestParameterKey getKey() {
        return key;
    }
    
    public String getValue() {
        return value;
    }
}
