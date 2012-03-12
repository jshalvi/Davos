package com.shalvi.davos.http;

import java.io.BufferedReader;

public class Response extends Message {

    private ResponseCode code;
    private BufferedReader reader;
    private int contentLength = -1;

    public Response() {}

    public Response(ResponseCode code) {
        this.code = code;
    }
    public Response(ResponseCode code, String body) {
        this.code = code;
    }
    
    /**
     * Default copy constructor.
     * @param response
     * @throws IllegalArgumentException if response is null
     */
    public Response(Response response) {
        super(response);
        
        code = response.getResponseCode();
        reader = response.getReader();
        contentLength = response.getContentLength();
    }
    public void setResponseCode(ResponseCode code) {
        this.code = code;
    }

    public ResponseCode getResponseCode() {
        return code;
    }

    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }
    public BufferedReader getReader() {
        return reader;
    }

    /**
     * Sets the content length of the response of the response.  This will set the Content-Length header
     * accordingly.  If length <= 0, the header field will be cleared.
     * @param length
     */
    public void setContentLength(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException();
        }

        setHeaderField(new HeaderField(HeaderFieldName.CONTENT_LENGTH, "" + length));
        this.contentLength = length;
    }
    public int getContentLength() {
        return contentLength;
    }
    
    /**
     * Adds a cookie to the cookie map, and sets the header accordingly.  NOTE at the moment
     * only one cookie is supported at a time.
     * @param key
     * @param val
     * @throws IllegalArgumentException if key or val are empty or null
     */
    public void setCookie(String key, String val) {
        if (key == null || key.length() == 0 ||
                val == null || val.length() == 0) {
            throw new IllegalArgumentException();
        }
        setHeaderField(new HeaderField(HeaderFieldName.SET_COOKIE, key + "=" + val + ";"));
        cookies.put(key, val);
    }


    public String toString() {
        String out = "[" + version.toString() + " " +
        code.getStatusCode() + " " +
        code.getReasonPhrase() + "]\n";

        out += headersToString();

        out += "Body:\n\t[body length: ";

        if (contentLength > 0) {
            out += contentLength;
        } else {
            out += "unknown";
        }

        out += "]";


        return out;
    }
    
    /**
     * Compares two Responses for equality.  Note, this does not take equality of the
     * reader into account.
     */
    public boolean equals(Object thatObect) {
        if(!(thatObect instanceof Response) || thatObect == null) {
            return false;
        }
        Response that = (Response) thatObect;

        
        return super.equals(that) &&
            this.code == that.getResponseCode() &&
            this.contentLength == that.getContentLength();
    }
}
