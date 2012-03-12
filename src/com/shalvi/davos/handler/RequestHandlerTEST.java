package com.shalvi.davos.handler;

import java.io.BufferedReader;
import java.io.StringReader;

import com.shalvi.davos.http.Request;
import com.shalvi.davos.http.Response;
import com.shalvi.davos.http.MockRequestBuilder;

import junit.framework.Assert;
import junit.framework.TestCase;

public class RequestHandlerTEST extends TestCase {

    public void testStaticFileRequestHandler() {
        RequestHandler handler = new StaticFileRequestHandler();
        Request request = null;
        Response response;
        MockRequestBuilder builder = new MockRequestBuilder();
        builder.initializeDefaults();
        builder.setValid(false);
        request = builder.toNewRequest();
        Context cin = new Context(request, null, null);
        Context cout;
        // Null request
        try {
            cout = handler.execute(null);
            Assert.fail();
        } catch (IllegalArgumentException e ) {}

        // Invalid request
        try {
            cout = handler.execute(cin);
            Assert.fail();
        } catch (IllegalArgumentException e ) {}
    }
    
    public void testContentLength() {
        String text = "<html><body>Hello there!</body></html>";
        BufferedReader reader = new BufferedReader(new StringReader(text));
        StaticFileRequestHandler handler = new StaticFileRequestHandler();
        
        // Run twice to ensure reader is consumed
        assertEquals(text.length(), handler.determineContentLength(reader));
        assertEquals(-1, handler.determineContentLength(reader));
        
    }

}
