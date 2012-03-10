package com.shalvi.davos.http;

class HeaderField {

    private HeaderFieldName key;
    private String value;
    private volatile int hashCode;
    
    HeaderField(HeaderFieldName key, String value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException();
        }
        this.key = key;
        this.value = value;
    }
    
    public HeaderFieldName getKey() {
        return key;
    }
    
    public String getValue() {
        return value;
    }
    
    public boolean equals(Object thatObject) {
        if (!(thatObject instanceof HeaderField)) {
            return false;
        }
        
        HeaderField that = (HeaderField) thatObject;
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
