package com.shalvi.davos.http;

import junit.framework.Assert;
import junit.framework.TestCase;

public class RequestTEST extends TestCase {

    public void testEquals() {

        Request request1 = new Request(), request2 = new Request(), request3 = new Request();
        
        MockRequestBuilder builder = new MockRequestBuilder();

        request1.equals(new Object());
        
        request1.setMethod(RequestMethod.GET);
        request1.setRequestURI("/");
        request1.setHTTPVersion(HTTPVersion.VERSION_1_1);
        request1.setValid(true);
        request1.setHeaderField(new RequestHeaderField(RequestHeaderFieldName.HOST, "www.test.com"));

        request2.setMethod(RequestMethod.GET);
        request2.setRequestURI("/");
        request2.setHTTPVersion(HTTPVersion.VERSION_1_1);
        request2.setValid(true);
        request2.setHeaderField(new RequestHeaderField(RequestHeaderFieldName.HOST, "www.test.com"));

        request3.setMethod(RequestMethod.GET);
        request3.setRequestURI("/");
        request3.setHTTPVersion(HTTPVersion.VERSION_1_1);
        request3.setValid(true);
        request3.setHeaderField(new RequestHeaderField(RequestHeaderFieldName.HOST, "www.test2.com"));

        Assert.assertFalse(request1 == request2);
        Assert.assertTrue(request1.equals(request2));
        Assert.assertTrue(request2.equals(request1));
        Assert.assertFalse(request1.equals(request3));
        
        // Test copy constructor
        Request request4 = new Request(request1);
        Assert.assertFalse(request1 == request4);
        Assert.assertTrue(request1.equals(request4));
        
        builder.initializeDefaults();
        request1 = builder.toNewRequest();
        builder.setMethod(RequestMethod.POST);
        Assert.assertFalse(request1.equals(builder.toNewRequest()));
        
        builder.initializeDefaults();
        builder.setHTTPVersion(HTTPVersion.VERSION_1_0);
        Assert.assertFalse(request1.equals(builder.toNewRequest()));
        
        builder.initializeDefaults();
        builder.setRequestURI("/test.html");
        Assert.assertFalse(request1.equals(builder.toNewRequest()));
        
        builder.initializeDefaults();
        builder.setValid(false);
        Assert.assertFalse(request1.equals(builder.toNewRequest()));
        
        builder.initializeDefaults();
        builder.setRequestHeaderField(new RequestHeaderField(RequestHeaderFieldName.USER_AGENT, "Chrome/10.0"));
        Assert.assertFalse(request1.equals(builder.toNewRequest()));
    }
    
    public void testHeaderField() {
        Request request = new Request();
        RequestHeaderField field;
        
        try {
            request.setHeaderField(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {}

        request.setHeaderField(new RequestHeaderField(RequestHeaderFieldName.HOST, "www.test.com"));
        field = request.getHeaderField(RequestHeaderFieldName.HOST);
        Assert.assertEquals(RequestHeaderFieldName.HOST, field.getKey());
        Assert.assertEquals("www.test.com", field.getValue());
        
        request.setHeaderField(new RequestHeaderField(RequestHeaderFieldName.HOST, "www.test2.com"));
        field = request.getHeaderField(RequestHeaderFieldName.HOST);
        Assert.assertEquals("www.test2.com", field.getValue());
        
        try {
            request.getHeaderField(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {}
        
        try {
            request.setHeaderField(new RequestHeaderField(RequestHeaderFieldName.UNSPECIFIED, ""));
            Assert.fail();
        } catch (IllegalArgumentException e) {}
        
        try {
            request.setHeaderField(new RequestHeaderField(RequestHeaderFieldName.UNSUPPORTED, ""));
            Assert.fail();
        } catch (IllegalArgumentException e) {}
        
        Assert.assertEquals(
                RequestHeaderFieldName.UNSPECIFIED, 
                request.getHeaderField(RequestHeaderFieldName.CONTENT_TYPE).getKey());
    }
    
}
