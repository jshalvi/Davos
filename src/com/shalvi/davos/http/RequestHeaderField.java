package com.shalvi.davos.http;

class RequestHeaderField {

    private RequestHeaderFieldName key;
    private String value;
    private volatile int hashCode;
    
    RequestHeaderField(RequestHeaderFieldName key, String value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException();
        }
        this.key = key;
        this.value = value;
    }
    
    public RequestHeaderFieldName getKey() {
        return key;
    }
    
    public String getValue() {
        return value;
    }
    
    public boolean equals(Object thatObject) {
        if (!(thatObject instanceof RequestHeaderField)) {
            return false;
        }
        
        RequestHeaderField that = (RequestHeaderField) thatObject;
        return key == that.getKey() && value.compareTo(that.getValue()) == 0;
    }
    
    public int hashCode() {
        
        int result = hashCode;
        if (result == 0) {
            result = 17;
            result = 31 * result + key.hashCode();
            result = 31 * result + value.hashCode();
            
        }
        return result;
    }
}
