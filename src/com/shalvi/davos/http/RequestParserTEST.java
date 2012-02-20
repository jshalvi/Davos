package com.shalvi.davos.http;

import junit.framework.Assert;
import junit.framework.TestCase;


public class RequestParserTEST extends TestCase {

  public void testParseMethod() {
    
    Assert.assertEquals(Method.UNSUPPORTED, RequestParser.parseMethod(null));
    Assert.assertEquals(Method.UNSUPPORTED, RequestParser.parseMethod(""));
    Assert.assertEquals(Method.UNSUPPORTED, RequestParser.parseMethod("/pub/WWW/TheProject.html HTTP/1.1"));
    Assert.assertEquals(Method.UNSUPPORTED, RequestParser.parseMethod("GOT /pub/WWW/TheProject.html HTTP/1.1"));
    Assert.assertEquals(Method.UNSUPPORTED, RequestParser.parseMethod("/pub/WWW/TheProject.html GOT HTTP/1.1"));
    
    Assert.assertEquals(Method.UNSUPPORTED, RequestParser.parseMethod(" GET /pub/WWW/TheProject.html HTTP/1.1"));    
    Assert.assertEquals(Method.UNSUPPORTED, RequestParser.parseMethod("GETTER /pub/WWW/TheProject.html HTTP/1.1"));
    Assert.assertEquals(Method.UNSUPPORTED, RequestParser.parseMethod("HEADER /pub/WWW/TheProject.html HTTP/1.1"));
    Assert.assertEquals(Method.UNSUPPORTED, RequestParser.parseMethod("DELETER /pub/WWW/TheProject.html HTTP/1.1"));
    Assert.assertEquals(Method.UNSUPPORTED, RequestParser.parseMethod("POSTER /pub/WWW/TheProject.html HTTP/1.1"));

    Assert.assertEquals(Method.UNSUPPORTED, RequestParser.parseMethod("get /pub/WWW/TheProject.html HTTP/1.1"));
    Assert.assertEquals(Method.UNSUPPORTED, RequestParser.parseMethod("head /pub/WWW/TheProject.html HTTP/1.1"));
    Assert.assertEquals(Method.UNSUPPORTED, RequestParser.parseMethod("delete /pub/WWW/TheProject.html HTTP/1.1"));
    Assert.assertEquals(Method.UNSUPPORTED, RequestParser.parseMethod("post /pub/WWW/TheProject.html HTTP/1.1"));
    
    Assert.assertEquals(Method.GET, RequestParser.parseMethod("GET /pub/WWW/TheProject.html HTTP/1.1"));
    Assert.assertEquals(Method.HEAD, RequestParser.parseMethod("HEAD /pub/WWW/TheProject.html HTTP/1.1"));
    Assert.assertEquals(Method.DELETE, RequestParser.parseMethod("DELETE /pub/WWW/TheProject.html HTTP/1.1"));
    Assert.assertEquals(Method.POST, RequestParser.parseMethod("POST /pub/WWW/TheProject.html HTTP/1.1"));
  }
  
  public void testParseURILocator() {
    Assert.assertEquals("", RequestParser.parseURILocator(null));
    Assert.assertEquals("", RequestParser.parseURILocator(""));
    Assert.assertEquals("", RequestParser.parseURILocator("/pub/WWW/TheProject.html"));
    Assert.assertEquals("/pub/WWW/TheProject.html", RequestParser.parseURILocator("GET /pub/WWW/TheProject.html"));
    Assert.assertEquals("GET", RequestParser.parseURILocator("/pub/WWW/TheProject.html GET HTTP/1.1"));
    Assert.assertEquals("/pub/WWW/TheProject.html", RequestParser.parseURILocator("GET /pub/WWW/TheProject.html HTTP/1.1"));
    Assert.assertEquals("/pub/WWW/TheProject.html", RequestParser.parseURILocator("GET    /pub/WWW/TheProject.html   HTTP/1.1"));
  }
  
  public void testParseHTTPVersion() {
    Assert.assertEquals(HTTPVersion.UNSUPPORTED, RequestParser.parseHTTPVersion(null));
    Assert.assertEquals(HTTPVersion.UNSUPPORTED, RequestParser.parseHTTPVersion(""));
    Assert.assertEquals(HTTPVersion.UNSUPPORTED, RequestParser.parseHTTPVersion("GET /pub/WWW/TheProject.html"));
    Assert.assertEquals(HTTPVersion.UNSUPPORTED, RequestParser.parseHTTPVersion("GET /pub/WWW/TheProject.html HTTP/0.1"));
    Assert.assertEquals(HTTPVersion.VERSION_1_0, RequestParser.parseHTTPVersion("GET /pub/WWW/TheProject.html HTTP/1.0"));
    Assert.assertEquals(HTTPVersion.VERSION_1_1, RequestParser.parseHTTPVersion("GET /pub/WWW/TheProject.html HTTP/1.1"));
  }
}
