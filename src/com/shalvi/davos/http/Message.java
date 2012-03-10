package com.shalvi.davos.http;

import java.util.HashMap;
import java.util.Map;

public abstract class Message {
    protected String uri;
    protected HTTPVersion version;
    protected Map<HeaderFieldName,HeaderField> headerFields = 
        new HashMap<HeaderFieldName, HeaderField>();
    

    /**
     * Sets the HTTP version of the request.
     * @param v
     */
    void setHTTPVersion(HTTPVersion v) {
      version = v;
    }
    
    /**
     * Returns the HTTP version of the request.
     * @return
     */
    public HTTPVersion getHTTPVersion() {
      return version;
    }
    
    /**
     * Sets a header field.  If the field is already set, the old value will be overwritten.
     * @param field
     * @throws IllegalArgumentException if field is null
     */
    public void setHeaderField(HeaderField field) {
        if (field == null || 
                field.getKey() == HeaderFieldName.UNSPECIFIED || 
                field.getKey() == HeaderFieldName.UNSUPPORTED) {
            throw new IllegalArgumentException();
        }
        
        headerFields.put(field.getKey(), field);
    }
    
    public HeaderField getHeaderField(HeaderFieldName key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        
        if (headerFields.containsKey(key)) {
            return headerFields.get(key);
        }
        
        return new HeaderField(HeaderFieldName.UNSPECIFIED, "");
    }
    
    Map<HeaderFieldName, HeaderField> getHeaderFields() {
        return new HashMap<HeaderFieldName, HeaderField>(headerFields);
    }
    
    String headersToString() {
        String headers = "";
        
        for (Map.Entry<HeaderFieldName, HeaderField> h : headerFields.entrySet()) {
            headers += "\t" + h.getValue().toString() + "\n";
        }
        
        return headers.length() > 0 ? "Headers:\n" + headers : "";
    }
    
    public abstract String toString();
}
