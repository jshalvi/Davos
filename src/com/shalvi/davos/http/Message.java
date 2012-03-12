package com.shalvi.davos.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class to represent an HTTP message.  Contains properties common
 * to both Request and Response.
 * 
 * @author jshalvi
 *
 */
public abstract class Message {
    protected HTTPVersion version;
    
    protected Map<HeaderFieldName,HeaderField> headerFields = 
        new HashMap<HeaderFieldName, HeaderField>();

    protected Map<String, String> cookies = 
        new HashMap<String, String>();

    /**
     * Standard constructor.
     */
    Message() {
        version = HTTPVersion.UNSUPPORTED;
    }
    
    /**
     * Copy constructor.
     * @param message to copy
     * @throws IllegalArgumentException if message is null.
     */
    Message(Message message) {
        if (message == null) {
            throw new IllegalArgumentException();
        }

        version = message.version;
        headerFields = message.getHeaderFields();
        cookies = new HashMap<String, String>(message.cookies);
    }

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
    void setHeaderField(HeaderField field) {
        if (field == null || 
                field.getKey() == HeaderFieldName.UNSPECIFIED || 
                field.getKey() == HeaderFieldName.UNSUPPORTED) {
            throw new IllegalArgumentException();
        }
        
        if (field.getKey() == HeaderFieldName.COOKIE) {
            String[] cookieFields = field.getValue().split("=");
            
            
            if (cookieFields.length == 2) {
                cookies.put(cookieFields[0], cookieFields[1].replace(';', ' ').trim());
            }
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

    /**
     * Returns a copy of this message's header fields map.
     * @return
     */
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

    public String headersToHTTPString() {
        String headers = "";

        for (Map.Entry<HeaderFieldName, HeaderField> h : headerFields.entrySet()) {
            headers += h.getValue().toString() + "\r\n";
        }

        return headers;
    }
    
    public boolean equals(Object thatObject) {
        if(!(thatObject instanceof Message) || thatObject == null) {
            return false;
        }
        
        Message that = (Message) thatObject;
        
        return version == that.getHTTPVersion() &&
            headerFields.equals(that.headerFields) &&
            cookies.equals(that.cookies);
    }
    
    /**
     * Retrieves a cookie value.
     * @param key
     * @return the desired value, or null if not available
     * @throws IllegalArgumentException if key is empty or null
     */
    public String getCookie(String key) {
        if (key == null || key.length() == 0) {
            throw new IllegalArgumentException();
        }
        
        return cookies.get(key);
    }
    
    public abstract String toString();
}
