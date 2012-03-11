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
        request1.setHeaderField(new HeaderField(HeaderFieldName.HOST, "www.test.com"));

        request2.setMethod(RequestMethod.GET);
        request2.setRequestURI("/");
        request2.setHTTPVersion(HTTPVersion.VERSION_1_1);
        request2.setValid(true);
        request2.setHeaderField(new HeaderField(HeaderFieldName.HOST, "www.test.com"));

        request3.setMethod(RequestMethod.GET);
        request3.setRequestURI("/");
        request3.setHTTPVersion(HTTPVersion.VERSION_1_1);
        request3.setValid(true);
        request3.setHeaderField(new HeaderField(HeaderFieldName.HOST, "www.test2.com"));

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
        Request tmp = builder.toNewRequest();
        Assert.assertFalse(request1.equals(tmp));
        
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
        builder.setHeaderField(new HeaderField(HeaderFieldName.USER_AGENT, "Chrome/10.0"));
        Assert.assertFalse(request1.equals(builder.toNewRequest()));
        
        // Test POSTDATA equality
        builder.initializeDefaults();
        request1 = builder.toNewRequest();
        request2 = builder.toNewRequest();
        request2.setPostdata("testKey", "testVal2");
        assertFalse(request1.equals(request2));
        request1.setPostdata("testKey", "testVal");
        request2.setPostdata("testKey", "testVal");
        assertTrue(request1.equals(request2));
        request2.setPostdata("testKey2", "testVal");
        assertFalse(request1.equals(request2));
    }
    
    public void testHeaderField() {
        Request request = new Request();
        HeaderField field;
        
        try {
            request.setHeaderField(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {}

        request.setHeaderField(new HeaderField(HeaderFieldName.HOST, "www.test.com"));
        field = request.getHeaderField(HeaderFieldName.HOST);
        Assert.assertEquals(HeaderFieldName.HOST, field.getKey());
        Assert.assertEquals("www.test.com", field.getValue());
        
        request.setHeaderField(new HeaderField(HeaderFieldName.HOST, "www.test2.com"));
        field = request.getHeaderField(HeaderFieldName.HOST);
        Assert.assertEquals("www.test2.com", field.getValue());
        
        try {
            request.getHeaderField(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {}
        
        try {
            request.setHeaderField(new HeaderField(HeaderFieldName.UNSPECIFIED, ""));
            Assert.fail();
        } catch (IllegalArgumentException e) {}
        
        try {
            request.setHeaderField(new HeaderField(HeaderFieldName.UNSUPPORTED, ""));
            Assert.fail();
        } catch (IllegalArgumentException e) {}
        
        Assert.assertEquals(
                HeaderFieldName.UNSPECIFIED, 
                request.getHeaderField(HeaderFieldName.CONTENT_TYPE).getKey());
    }
    
    private void assertPostdataException(String key, String val) {
        Request r = new Request();
        try {
            r.setPostdata(key, val);
            Assert.fail();
        } catch( IllegalArgumentException e) {}
    }
    public void testPostdata() {
        
        assertPostdataException("", "test");
        assertPostdataException(null, "test");
        assertPostdataException("test", null);
        
        Request r = new Request();
        assertNull(r.getPostdata("sdflksjdlfk"));
        r.setPostdata("fox", "snow");
        assertEquals("snow", r.getPostdata("fox"));
        r.setPostdata("fox", "fire");
        assertEquals("fire", r.getPostdata("fox"));
    }
    
    public void testGetContentLength() {
        
        Request request = new Request();
        assertEquals(0, request.getContentLength());
        
        request.setHeaderField(new HeaderField(HeaderFieldName.CONTENT_LENGTH, "0"));
        assertEquals(0, request.getContentLength());
        
        request.setHeaderField(new HeaderField(HeaderFieldName.CONTENT_LENGTH, "5"));
        assertEquals(5, request.getContentLength());
        
        request.setHeaderField(new HeaderField(HeaderFieldName.CONTENT_LENGTH, ""));
        assertEquals(0, request.getContentLength());

        request.setHeaderField(new HeaderField(HeaderFieldName.CONTENT_LENGTH, "notanumber"));
        assertEquals(0, request.getContentLength());
    }
    
    public void testToString() {
        
        String HEADERS = "Headers:\n\tHost: www.test.com\n";
        String POSTDATA = "Postdata:\n\ttestkey=testval\n";

        // GET
        MockRequestBuilder builder = new MockRequestBuilder();
        builder.initializeDefaults();
        builder.setMethod(RequestMethod.GET);
        builder.setRequestURI("/test.html");
        assertEquals("[GET /test.html HTTP/1.1]\n" + HEADERS, builder.toNewRequest().toString());
        
        // HEAD
        builder.setMethod(RequestMethod.HEAD);
        assertEquals("[HEAD /test.html HTTP/1.1]\n" + HEADERS, builder.toNewRequest().toString());

        // DELETE
        builder.setMethod(RequestMethod.DELETE);
        assertEquals("[DELETE /test.html HTTP/1.1]\n" + HEADERS, builder.toNewRequest().toString());
        
        // POST w/data
        builder.setMethod(RequestMethod.POST);
        builder.setPostdata("testkey", "testval");
        assertEquals("[POST /test.html HTTP/1.1]\n" + HEADERS + POSTDATA,
            builder.toNewRequest().toString());
    }
}
