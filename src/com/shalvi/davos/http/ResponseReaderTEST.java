package com.shalvi.davos.http;

import com.shalvi.davos.handler.http.ResponseReader;

import junit.framework.Assert;
import junit.framework.TestCase;

public class ResponseReaderTEST extends TestCase {

    private static String CRLF = "\r\n";
    public void testResponseRead() {
        Response response = ResponseBuilder.getMock200Response();
        ResponseReader reader;
        try {
            reader = new ResponseReader(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {}
        
        String line, out = "";
        
        reader = new ResponseReader(response);
        out = reader.toString();
        /*
        while((line = reader.readLine()) != null) {
            out += line;
        }
        */
        assertEquals(
                "HTTP/1.1 200 OK" + CRLF +
                "Content-Length: " + ResponseBuilder.MOCK_200_RESPONSE_TEXT.length() + CRLF +
                CRLF +
                ResponseBuilder.MOCK_200_RESPONSE_TEXT, out);
    }
}
