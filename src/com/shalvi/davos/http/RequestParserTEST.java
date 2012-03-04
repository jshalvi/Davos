package com.shalvi.davos.http;

import java.io.BufferedReader;
import java.io.StringReader;

import junit.framework.Assert;
import junit.framework.TestCase;


public class RequestParserTEST extends TestCase {

  private static String CRLF = "\r\n";
  private static String RL_VALID_GET = "GET /pub/WWW/TheProject.html HTTP/1.1";
  private static String RL_VALID_HEAD = "HEAD /pub/WWW/TheProject.html HTTP/1.1";
  private static String RL_VALID_DELETE = "DELETE /pub/WWW/TheProject.html HTTP/1.1";
  private static String RL_VALID_POST = "POST /pub/WWW/TheProject.html HTTP/1.1";
  
  private static String RL_BAD_METHOD = "GOT /pub/WWW/TheProject.html HTTP/1.1";
  private static String RL_MISSING_METHOD = "/pub/WWW/TheProject.html HTTP/1.1";
  
  
  private static String RL_BAD_HTTP_VERSION = "GET /pub/WWW/TheProject.html HTTP/0.1";
  private static String RL_MISSING_HTTP_VERSION = "GET /pub/WWW/TheProject.html";
  private static String HEADER_LINE = "Content: all";
  
  public void testParseMethod() {
    
    Assert.assertEquals(RequestMethod.UNSUPPORTED, RequestParser.parseMethod(null));
    Assert.assertEquals(RequestMethod.UNSUPPORTED, RequestParser.parseMethod(""));
    Assert.assertEquals(RequestMethod.UNSUPPORTED, RequestParser.parseMethod("/pub/WWW/TheProject.html HTTP/1.1"));
    Assert.assertEquals(RequestMethod.UNSUPPORTED, RequestParser.parseMethod("GOT /pub/WWW/TheProject.html HTTP/1.1"));
    Assert.assertEquals(RequestMethod.UNSUPPORTED, RequestParser.parseMethod("/pub/WWW/TheProject.html GOT HTTP/1.1"));
    
    Assert.assertEquals(RequestMethod.UNSUPPORTED, RequestParser.parseMethod(" GET /pub/WWW/TheProject.html HTTP/1.1"));    
    Assert.assertEquals(RequestMethod.UNSUPPORTED, RequestParser.parseMethod("GETTER /pub/WWW/TheProject.html HTTP/1.1"));
    Assert.assertEquals(RequestMethod.UNSUPPORTED, RequestParser.parseMethod("HEADER /pub/WWW/TheProject.html HTTP/1.1"));
    Assert.assertEquals(RequestMethod.UNSUPPORTED, RequestParser.parseMethod("DELETER /pub/WWW/TheProject.html HTTP/1.1"));
    Assert.assertEquals(RequestMethod.UNSUPPORTED, RequestParser.parseMethod("POSTER /pub/WWW/TheProject.html HTTP/1.1"));

    Assert.assertEquals(RequestMethod.UNSUPPORTED, RequestParser.parseMethod("get /pub/WWW/TheProject.html HTTP/1.1"));
    Assert.assertEquals(RequestMethod.UNSUPPORTED, RequestParser.parseMethod("head /pub/WWW/TheProject.html HTTP/1.1"));
    Assert.assertEquals(RequestMethod.UNSUPPORTED, RequestParser.parseMethod("delete /pub/WWW/TheProject.html HTTP/1.1"));
    Assert.assertEquals(RequestMethod.UNSUPPORTED, RequestParser.parseMethod("post /pub/WWW/TheProject.html HTTP/1.1"));
    
    Assert.assertEquals(RequestMethod.GET, RequestParser.parseMethod("GET /pub/WWW/TheProject.html HTTP/1.1"));
    Assert.assertEquals(RequestMethod.HEAD, RequestParser.parseMethod("HEAD /pub/WWW/TheProject.html HTTP/1.1"));
    Assert.assertEquals(RequestMethod.DELETE, RequestParser.parseMethod("DELETE /pub/WWW/TheProject.html HTTP/1.1"));
    Assert.assertEquals(RequestMethod.POST, RequestParser.parseMethod("POST /pub/WWW/TheProject.html HTTP/1.1"));
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
  
  public void testParseRequestLine() {
    Request r;
    
    r = RequestParser.parseRequestLine("GET /pub/WWW/TheProject.html HTTP/1.1");
    Assert.assertEquals(RequestMethod.GET, r.getMethod());
    Assert.assertEquals("/pub/WWW/TheProject.html", r.getRequestURI());
    Assert.assertEquals(HTTPVersion.VERSION_1_1, r.getHTTPVersion());
    
    r = RequestParser.parseRequestLine("POST / HTTP/1.0");
    Assert.assertEquals(RequestMethod.POST, r.getMethod());
    Assert.assertEquals("/", r.getRequestURI());
    Assert.assertEquals(HTTPVersion.VERSION_1_0, r.getHTTPVersion());
    
    r = RequestParser.parseRequestLine("HEAD /pub/WWW/TheProject.html HTTP/1.1");
    Assert.assertEquals(RequestMethod.HEAD, r.getMethod());
    Assert.assertEquals("/pub/WWW/TheProject.html", r.getRequestURI());
    Assert.assertEquals(HTTPVersion.VERSION_1_1, r.getHTTPVersion());
    
    r = RequestParser.parseRequestLine("DELETE /pub/WWW/TheProject.html HTTP/1.1");
    Assert.assertEquals(RequestMethod.DELETE, r.getMethod());
    Assert.assertEquals("/pub/WWW/TheProject.html", r.getRequestURI());
    Assert.assertEquals(HTTPVersion.VERSION_1_1, r.getHTTPVersion());
    
    r = RequestParser.parseRequestLine("INVALID /pub/WWW/TheProject.html HTTP/1.1");
    assertInvalidRequestLine(r);
    
    r = RequestParser.parseRequestLine("/pub/WWW/TheProject.html HTTP/1.1");
    assertInvalidRequestLine(r);

    r = RequestParser.parseRequestLine("GET HTTP/1.1");
    assertInvalidRequestLine(r);
    
    r = RequestParser.parseRequestLine("GET /pub/WWW/TheProject.html HTTP/2.1");
    assertInvalidRequestLine(r);
    
    r = RequestParser.parseRequestLine("GET /pub/WWW/TheProject.html");
    assertInvalidRequestLine(r);
    
    r = RequestParser.parseRequestLine("");
    assertInvalidRequestLine(r);
    
    r = RequestParser.parseRequestLine(null);
    assertInvalidRequestLine(r);    
  }
  
  private void assertInvalidRequestLine(Request r) {
    Assert.assertEquals(RequestMethod.UNSUPPORTED, r.getMethod());
    Assert.assertEquals("", r.getRequestURI());
    Assert.assertEquals(HTTPVersion.UNSUPPORTED, r.getHTTPVersion());
  }
  
  public void testReadLine() {
    
    String LINE1 = "GET /pub/WWW/TheProject.html HTTP/1.1",
        SEPARATOR = "\r\n",
        LINE2 = "test";
    
    // test line with \r or \n alone
    
    BufferedReader reader = new BufferedReader(new StringReader(LINE1 + SEPARATOR + LINE2));
    Assert.assertEquals(LINE1, RequestParser.readLine(reader));
    Assert.assertEquals(LINE2, RequestParser.readLine(reader));
    Assert.assertNull(RequestParser.readLine(reader));
    
    reader = new BufferedReader(new StringReader(SEPARATOR));
    Assert.assertEquals("", RequestParser.readLine(reader));
    Assert.assertNull(RequestParser.readLine(reader));
    
    reader = new BufferedReader(new StringReader(""));
    Assert.assertNull(RequestParser.readLine(reader));
    
    try {
      RequestParser.readLine(null);
      Assert.fail();
    } catch (IllegalArgumentException e) {}
  }
  
  public void testParseRequest() {
    BufferedReader reader;
    Request request;
    
    // Test valid requests
    reader = new BufferedReader(new StringReader(RL_VALID_GET + CRLF + CRLF));
    request = RequestParser.parseRequest(reader);
    Assert.assertTrue(request.isValid());
    Assert.assertEquals(RequestMethod.GET, request.getMethod());

    reader = new BufferedReader(new StringReader(
        RL_VALID_GET + CRLF + 
        HEADER_LINE + CRLF +
        CRLF));
    request = RequestParser.parseRequest(reader);
    Assert.assertTrue(request.isValid());
    Assert.assertEquals(RequestMethod.GET, request.getMethod());

    reader = new BufferedReader(new StringReader(RL_VALID_HEAD + CRLF + CRLF));
    request = RequestParser.parseRequest(reader);
    Assert.assertTrue(request.isValid());
    Assert.assertEquals(RequestMethod.HEAD, request.getMethod());

    reader = new BufferedReader(new StringReader(RL_VALID_DELETE + CRLF + CRLF));
    request = RequestParser.parseRequest(reader);
    Assert.assertTrue(request.isValid());
    Assert.assertEquals(RequestMethod.DELETE, request.getMethod());

    reader = new BufferedReader(new StringReader(RL_VALID_POST + CRLF + CRLF));
    request = RequestParser.parseRequest(reader);
    Assert.assertTrue(request.isValid());
    Assert.assertEquals(RequestMethod.POST, request.getMethod());

    // Test bad request lines
    reader = new BufferedReader(new StringReader(RL_BAD_METHOD + CRLF + CRLF));
    request = RequestParser.parseRequest(reader);
    Assert.assertFalse(request.isValid());

    reader = new BufferedReader(new StringReader(RL_MISSING_METHOD + CRLF + CRLF));
    request = RequestParser.parseRequest(reader);
    Assert.assertFalse(request.isValid());

    reader = new BufferedReader(new StringReader(RL_BAD_HTTP_VERSION + CRLF + CRLF));
    request = RequestParser.parseRequest(reader);
    Assert.assertFalse(request.isValid());

    reader = new BufferedReader(new StringReader(RL_MISSING_HTTP_VERSION + CRLF + CRLF));
    request = RequestParser.parseRequest(reader);
    Assert.assertFalse(request.isValid());

    // Test incomplete request
    reader = new BufferedReader(new StringReader(RL_VALID_HEAD));
    request = RequestParser.parseRequest(reader);
    Assert.assertFalse(request.isValid());
  }
  
  private boolean validParameter(RequestHeaderFieldName key, String value, String line) {
      
      RequestHeaderField param = RequestParser.parseRequestParameter(line);
      
      if (param.getKey() == key && param.getValue().compareTo(value) == 0) {
          return true;
      }

      return false;
  }
  public void testParseParameter() {
      
      Assert.assertTrue(validParameter(
              RequestHeaderFieldName.HOST,
              "www.mysite.com",
              "Host: www.mysite.com"));
      
      Assert.assertTrue(validParameter(
              RequestHeaderFieldName.HOST,
              "www.mysite.com",
              "Host:www.mysite.com"));
      Assert.assertTrue(validParameter(
              RequestHeaderFieldName.HOST,
              "www.mysite.com",
              "HOST: www.mysite.com"));
      
      Assert.assertTrue(validParameter(
              RequestHeaderFieldName.USER_AGENT, 
              "Mozilla/4.0",
              "User-Agent: Mozilla/4.0"));
      
      Assert.assertTrue(validParameter(
              RequestHeaderFieldName.CONTENT_LENGTH, 
              "27",
              "Content-Length: 27"));
      
      Assert.assertTrue(validParameter(
              RequestHeaderFieldName.CONTENT_TYPE,
              "application/x-www-form-urlencoded",
              "Content-Type: application/x-www-form-urlencoded"));
      
      RequestHeaderFieldName unsupported = RequestHeaderFieldName.UNSUPPORTED;
      Assert.assertEquals(unsupported, RequestParser.parseRequestParameter("Force: strong").getKey());
      Assert.assertEquals(unsupported, RequestParser.parseRequestParameter("Host:").getKey());
      Assert.assertEquals(unsupported, RequestParser.parseRequestParameter("Host www.content-length").getKey());
      Assert.assertEquals(unsupported, RequestParser.parseRequestParameter("").getKey());
      Assert.assertEquals(unsupported, RequestParser.parseRequestParameter(null).getKey());
  }
  
  public void testParsePost() {
    /*
     * 
     * Sample request:
     * 
     * POST /login.jsp HTTP/1.1
     * Host: www.mysite.com
     * User-Agent: Mozilla/4.0
     * Content-Length: 27
     * Content-Type: application/x-www-form-urlencoded
     * 
     * userid=sforel&password=not2day
     *
     */
      /*
    String requestText = RL_VALID_POST + CRLF +
      "Host: www.mysite.com" + CRLF +
      "User-Agent: Mozilla/4.0" + CRLF +
      "Content-Length: 27" + CRLF +
      "Content-Type: application/x-www-form-urlencoded" + CRLF +
      CRLF +
      "userid=sforel&password=not2day" + CRLF +
      CRLF;
    
    BufferedReader reader = new BufferedReader(new StringReader(requestText));
    Request request = RequestParser.parseRequest(reader);
    */
    
  }
}
