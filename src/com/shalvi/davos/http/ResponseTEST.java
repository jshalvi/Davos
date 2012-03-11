package com.shalvi.davos.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import junit.framework.Assert;
import junit.framework.TestCase;

public class ResponseTEST extends TestCase {

    public void testCopyConstructor() {
        Response response1 = ResponseBuilder.getMock200Response();
        Response response2 = new Response(response1);
        
        assertFalse(response1 == response2);
        assertTrue(response1.equals(response2));
        response2.setResponseCode(ResponseCode.CLIENT_ERROR_404);
        assertFalse(response1.equals(response2));
    }
    
    public void testEquals() {
        Response response1 = ResponseBuilder.getMock200Response();
        Response response2 = ResponseBuilder.getMock200Response();
        Response response3 = ResponseBuilder.getMock200Response();
        
        assertTrue(response1.equals(response2));
        assertTrue(response2.equals(response1));
        assertTrue(response1.equals(response3));
        assertTrue(response2.equals(response3));
        
        assertTrue(response1.equals(response1));
        
        assertFalse(response1.equals(new Object()));
        assertFalse(response1.equals(null));
        
        response3.setResponseCode(ResponseCode.CLIENT_ERROR_404);
        assertFalse(response1.equals(response3));
        assertFalse(response3.equals(response1));
    }
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
        response.setResponseCode(ResponseCode.SUCCESSFUL_200);
        
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
    
    public void testDefault404() {
        Response response = ResponseBuilder.getDefault404Response();
        String responseBody = "";
        
        assertEquals(ResponseCode.CLIENT_ERROR_404, response.getResponseCode());
        
        try {
            responseBody = response.getReader().readLine();
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
        
        assertEquals(ResponseBuilder.DEFAULT_404_RESPONSE_TEXT, responseBody);
    }
    
    public void testDefault501() {
        Response response = ResponseBuilder.getDefault501Response();
        String responseBody = "";
        
        assertEquals(ResponseCode.SERVER_ERROR_501, response.getResponseCode());
        
        try {
            responseBody = response.getReader().readLine();
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
        
        assertEquals(ResponseBuilder.DEFAULT_501_RESPONSE_TEXT, responseBody);
    }
    
    public void testHeadersOnly() {
        Response response = ResponseBuilder.getDefault501Response();
        Response response2 = ResponseBuilder.getHeadersOnlyResponse(response);
        
        assertFalse(response == response2);
        assertTrue(response2.getHeaderFields().isEmpty());
        assertTrue(response2.getReader() == null);
        
        try {
            ResponseBuilder.getHeadersOnlyResponse(null);
            Assert.fail();
        } catch(IllegalArgumentException e) {}
    }
}
