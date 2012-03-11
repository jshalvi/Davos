package com.shalvi.davos.http;

import java.io.BufferedReader;
import java.io.StringReader;

public class ResponseBuilder {

    private Response response;
    public static String DEFAULT_404_RESPONSE_TEXT = 
        "<html><body>Error 404 - File not found.</body></html>";
        
    public static String DEFAULT_501_RESPONSE_TEXT = 
        "<html><body>Error 501 - Not implemented.</body></html>";
        
    public static String MOCK_200_RESPONSE_TEXT = 
        "<html><body>Hello, there.</body></html>";
        
    public ResponseBuilder() {
        response = new Response();
        response.setHTTPVersion(HTTPVersion.VERSION_1_1);
    }

    public void setResponseCode(ResponseCode code) {
        response.setResponseCode(code);
    }
    public void setReader(BufferedReader reader) {
        response.setReader(reader);
    }
    public Response toResponse() {
        return response;
    }
    public void setHeaderField(HeaderField field) {
        response.setHeaderField(field);
    }
    public void setContentLength(int length) {
        response.setContentLength(length);
    }
    public static Response getDefault404Response() {
        Response r = new Response();
        r.setHeaderField(new HeaderField(HeaderFieldName.CONTENT_TYPE, "text/html"));
        r.setHTTPVersion(HTTPVersion.VERSION_1_1);
        r.setResponseCode(ResponseCode.CLIENT_ERROR_404);
        r.setContentLength(DEFAULT_404_RESPONSE_TEXT.length());
        
        r.setReader(new BufferedReader(new StringReader(DEFAULT_404_RESPONSE_TEXT)));
        
        return r;
    }
    
    public static Response getDefault501Response() {
        Response r = new Response();
        r.setHeaderField(new HeaderField(HeaderFieldName.CONTENT_TYPE, "text/html"));
        r.setHTTPVersion(HTTPVersion.VERSION_1_1);
        r.setResponseCode(ResponseCode.SERVER_ERROR_501);
        r.setContentLength(DEFAULT_501_RESPONSE_TEXT.length());
        
        r.setReader(new BufferedReader(new StringReader(DEFAULT_501_RESPONSE_TEXT)));
        
        return r;
    }

    public static Response getMock200Response() {
        Response r = new Response();
        r.setContentLength(MOCK_200_RESPONSE_TEXT.length());
        r.setHTTPVersion(HTTPVersion.VERSION_1_1);
        r.setResponseCode(ResponseCode.SUCCESSFUL_200);
        
        r.setReader(new BufferedReader(new StringReader(MOCK_200_RESPONSE_TEXT)));
        
        return r;
    }
    
    public static Response getHeadersOnlyResponse(Response response) {
        if (response == null) {
            throw new IllegalArgumentException();
        }
        
        Response r = new Response(response);
        r.headerFields.clear();
        r.setReader(null);
        
        return r;
    }

}
