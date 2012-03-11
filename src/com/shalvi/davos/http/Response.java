package com.shalvi.davos.http;

import java.io.BufferedReader;

public class Response extends Message {

    private ResponseCode code;
    private BufferedReader reader;
    private int contentLength = -1;
    private HTTPVersion version;

    public Response() {}

    public Response(ResponseCode code) {
        this.code = code;
    }
    public Response(ResponseCode code, String body) {
        this.code = code;
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

    public void setHTTPVersion(HTTPVersion version) {
        this.version = version;
    }
    public HTTPVersion getHTTPVersion() {
        return version;
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
}
