package com.shalvi.davos.http;

import java.io.BufferedReader;
import java.io.StringReader;

import junit.framework.Assert;
import junit.framework.TestCase;

public class ResponseTEST extends TestCase {

    public void testSetContentLength() {
        Response response = new Response();
        
        assertEquals(HeaderFieldName.UNSPECIFIED, response.getHeaderField(HeaderFieldName.CONTENT_LENGTH).getKey());
        response.setContentLength(10);
        assertEquals("10", response.getHeaderField(HeaderFieldName.CONTENT_LENGTH).getValue());
        
        try {
            response.setContentLength(0);
            Assert.fail();
        } catch (IllegalArgumentException e) {}
        
        try {
            response.setContentLength(-1);
        } catch (IllegalArgumentException e) {}
    }
    
    public void testToString() {
        String markup = "<html><body>Hello there!</body></html>";
        
        Response response = new Response();
        response.setHTTPVersion(HTTPVersion.VERSION_1_1);
        response.setResponseCode(ResponseCode.SUCCESS_200);
        
        assertEquals("[HTTP/1.1 200 OK]\n" +
                "Body:\n" +
                "\t[body length: unknown]",
                response.toString());
        
        response.setContentLength(markup.length());
        response.setReader(new BufferedReader(new StringReader(markup)));
        
        assertEquals("[HTTP/1.1 200 OK]\n" +
                "Headers:\n" +
                "\tContent-Length: " + markup.length() + "\n" +
                "Body:\n" +
                "\t[body length: " + markup.length() + "]",
                response.toString());
    }
}
