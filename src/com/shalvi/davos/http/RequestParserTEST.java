package com.shalvi.davos.http;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Map;

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
  private static String HEADER_LINE = "Host: www.test.com";
  
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
  
  public void testParseRequest() {
    RequestReader reader;
    Request request;
    
    // Test valid requests
    reader = new RequestReader(
        new BufferedReader(new StringReader(RL_VALID_GET + CRLF + CRLF)));
    request = RequestParser.parseRequest(reader);
    Assert.assertTrue(request.isValid());
    Assert.assertEquals(RequestMethod.GET, request.getMethod());

    reader = new RequestReader(
            new BufferedReader(new StringReader(
        RL_VALID_GET + CRLF + 
        HEADER_LINE + CRLF +
        CRLF)));
    
    request = RequestParser.parseRequest(reader);
    Assert.assertTrue(request.isValid());
    Assert.assertEquals(RequestMethod.GET, request.getMethod());

    reader = new RequestReader(
            new BufferedReader(new StringReader(RL_VALID_HEAD + CRLF + CRLF)));
    request = RequestParser.parseRequest(reader);
    Assert.assertTrue(request.isValid());
    Assert.assertEquals(RequestMethod.HEAD, request.getMethod());

    reader = new RequestReader(
            new BufferedReader(new StringReader(RL_VALID_DELETE + CRLF + CRLF)));
    request = RequestParser.parseRequest(reader);
    Assert.assertTrue(request.isValid());
    Assert.assertEquals(RequestMethod.DELETE, request.getMethod());

    reader = new RequestReader(
            new BufferedReader(new StringReader(RL_VALID_POST + CRLF + CRLF)));
    request = RequestParser.parseRequest(reader);
    Assert.assertTrue(request.isValid());
    Assert.assertEquals(RequestMethod.POST, request.getMethod());

    // Test bad request lines
    reader = new RequestReader(
            new BufferedReader(new StringReader(RL_BAD_METHOD + CRLF + CRLF)));
    request = RequestParser.parseRequest(reader);
    Assert.assertFalse(request.isValid());

    reader = new RequestReader(
            new BufferedReader(new StringReader(RL_MISSING_METHOD + CRLF + CRLF)));
    request = RequestParser.parseRequest(reader);
    Assert.assertFalse(request.isValid());

    reader = new RequestReader(
            new BufferedReader(new StringReader(RL_BAD_HTTP_VERSION + CRLF + CRLF)));
    request = RequestParser.parseRequest(reader);
    Assert.assertFalse(request.isValid());

    reader = new RequestReader(
            new BufferedReader(new StringReader(RL_MISSING_HTTP_VERSION + CRLF + CRLF)));
    request = RequestParser.parseRequest(reader);
    Assert.assertFalse(request.isValid());

    // Test incomplete request
    reader = new RequestReader(
            new BufferedReader(new StringReader(RL_VALID_HEAD)));
    request = RequestParser.parseRequest(reader);
    Assert.assertFalse(request.isValid());
  }
  
  private boolean validParameter(HeaderFieldName key, String value, String line) {
      
      HeaderField param = RequestParser.parseHeaderField(line);
      
      if (param.getKey() == key && param.getValue().compareTo(value) == 0) {
          return true;
      }

      return false;
  }
  public void testParseParameter() {
      
      Assert.assertTrue(validParameter(
              HeaderFieldName.HOST,
              "www.mysite.com",
              "Host: www.mysite.com"));
      
      Assert.assertTrue(validParameter(
              HeaderFieldName.HOST,
              "www.mysite.com",
              "Host:www.mysite.com"));
      Assert.assertTrue(validParameter(
              HeaderFieldName.HOST,
              "www.mysite.com",
              "HOST: www.mysite.com"));
      
      Assert.assertTrue(validParameter(
              HeaderFieldName.USER_AGENT, 
              "Mozilla/4.0",
              "User-Agent: Mozilla/4.0"));
      
      Assert.assertTrue(validParameter(
              HeaderFieldName.CONTENT_LENGTH, 
              "27",
              "Content-Length: 27"));
      
      Assert.assertTrue(validParameter(
              HeaderFieldName.CONTENT_TYPE,
              "application/x-www-form-urlencoded",
              "Content-Type: application/x-www-form-urlencoded"));
      
      HeaderFieldName unsupported = HeaderFieldName.UNSUPPORTED;
      Assert.assertEquals(unsupported, RequestParser.parseHeaderField("Force: strong").getKey());
      Assert.assertEquals(unsupported, RequestParser.parseHeaderField("Host:").getKey());
      Assert.assertEquals(unsupported, RequestParser.parseHeaderField("Host www.content-length").getKey());
      Assert.assertEquals(unsupported, RequestParser.parseHeaderField("").getKey());
      Assert.assertEquals(unsupported, RequestParser.parseHeaderField(null).getKey());
  }
  
  public void testRequestHeaderField() {
      
      try {
          new HeaderField(null, "TEST");
          Assert.fail();
      } catch (IllegalArgumentException e) {}
      
      try {
          new HeaderField(HeaderFieldName.HOST, null);
          Assert.fail();
      } catch (IllegalArgumentException e) {}
      
      
      String userAgentVal = "Mozilla/4.0";
      HeaderFieldName userAgentKey = HeaderFieldName.USER_AGENT;
      String hostVal = "www.test.com";
      HeaderFieldName hostKey = HeaderFieldName.HOST;

      HeaderField field = new HeaderField(HeaderFieldName.HOST, "www.test.com"),
          field2 = new HeaderField(hostKey, hostVal),
          field3 = new HeaderField(hostKey, new String(hostVal));
      
      Assert.assertEquals(HeaderFieldName.HOST, field.getKey());
      Assert.assertEquals("www.test.com", field.getValue());
            
      Assert.assertTrue(field.equals(field));
      Assert.assertTrue(field.equals(field2));
      Assert.assertTrue(field.equals(field3));
      
      Assert.assertFalse(field.equals(new HeaderField(hostKey, userAgentVal)));
      Assert.assertFalse(field.equals(new HeaderField(userAgentKey, hostVal)));
      Assert.assertFalse(field.equals(new Object()));
      Assert.assertFalse(field.equals(null));
      
      Assert.assertEquals(field.hashCode(), field.hashCode());
      Assert.assertEquals(field.hashCode(), field2.hashCode());
      Assert.assertEquals(field.hashCode(), field3.hashCode());
      
      // test hashCode
  }
  
  private Request parseRequestText(String requestText) {
      RequestReader reader = new RequestReader(
              new BufferedReader(new StringReader(requestText)));
      return RequestParser.parseRequest(reader);
  }
  
  private void verifyHeaderField(Request request, HeaderFieldName key, String value) {
      HeaderField field = request.getHeaderField(key);
      Assert.assertEquals(key, field.getKey());
      Assert.assertEquals(value, field.getValue());
  }
  
  public void testParseHeaders() {
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
    String requestText = RL_VALID_POST + CRLF +
      "Host: www.mysite.com" + CRLF +
      "User-Agent: Mozilla/4.0" + CRLF +
      "Content-Length: 30" + CRLF +
      "Content-Type: application/x-www-form-urlencoded" + CRLF +
      CRLF;
    
    Request request = parseRequestText(requestText);
    
    Assert.assertEquals(RequestMethod.POST, request.getMethod());
    verifyHeaderField(request, HeaderFieldName.HOST, "www.mysite.com");
    verifyHeaderField(request, HeaderFieldName.USER_AGENT, "Mozilla/4.0");
    verifyHeaderField(request, HeaderFieldName.CONTENT_LENGTH, "30");
    verifyHeaderField(request, HeaderFieldName.CONTENT_TYPE, "application/x-www-form-urlencoded");   
     
    requestText = RL_VALID_POST + CRLF +
      "Host: www.mysite.com" + CRLF +
      "User-Agent: Mozilla/4.0" + CRLF +
      "User-Agent: Mozilla/4.0" + CRLF +
      "Content-Length: 30" + CRLF +
      "Content-Type: application/x-www-form-urlencoded" + CRLF +
      CRLF;
  
    request = parseRequestText(requestText);
    Assert.assertFalse(request.isValid());
    
    requestText = RL_VALID_GET + CRLF +
      CRLF;

    request = parseRequestText(requestText);
    Assert.assertTrue(request.isValid());
    Assert.assertEquals(RequestMethod.GET, request.getMethod());

    requestText = RL_VALID_GET + CRLF +
        "Nonsense: blah/0.0" + CRLF +
        CRLF;

    request = parseRequestText(requestText);
    Assert.assertTrue(request.isValid());
  }
  
  public void testParsePostdata() {
      Map<String, String> m;
      
      try {
          RequestParser.parseUrlEncodedPostdata(null);
          Assert.fail();
          
      } catch (IllegalArgumentException e) {}
      
      String[] INVALID_POSTDATA = {
              "=sforel&password=not2day",
              "username=password=not2day",
              "username&password",
              "&password=not2day",
              "&"
      };
      
      for (String s : INVALID_POSTDATA) {
          m = RequestParser.parseUrlEncodedPostdata(s);
          assertNull(s + " should be invalid.", m);
      }
      
      m = RequestParser.parseUrlEncodedPostdata("userid=sforel&password=not2day");
      assertEquals("sforel", m.get("userid"));
      assertEquals("not2day", m.get("password"));
      
      m = RequestParser.parseUrlEncodedPostdata("username=sforel");
      assertEquals("sforel", m.get("username"));
      assertNull(m.get("password"));

      m = RequestParser.parseUrlEncodedPostdata("username=&password=");
      assertEquals("", m.get("username"));
      assertEquals("", m.get("password"));
  }
  
  public void testParsePost() {
      String requestText = RL_VALID_POST + CRLF +
          "Host: www.mysite.com" + CRLF +
          "User-Agent: Mozilla/4.0" + CRLF +
          "Content-Length: 30" + CRLF +
          "Content-Type: application/x-www-form-urlencoded" + CRLF +
          CRLF +
          "userid=sforel&password=not2day";

      Request request = parseRequestText(requestText);
      assertEquals("sforel", request.getPostdata("userid"));
      assertEquals("not2day", request.getPostdata("password"));
      assertTrue(request.isValid());
      
      // Content-length over
      requestText = RL_VALID_POST + CRLF +
          "Host: www.mysite.com" + CRLF +
          "User-Agent: Mozilla/4.0" + CRLF +
          "Content-Length: 31" + CRLF +
          "Content-Type: application/x-www-form-urlencoded" + CRLF +
          CRLF +
          "userid=sforel&password=not2day";
      request = parseRequestText(requestText);
      assertFalse(request.isValid());

      // Missing CRLF between headers and body
      requestText = RL_VALID_POST + CRLF +
          "Host: www.mysite.com" + CRLF +
          "User-Agent: Mozilla/4.0" + CRLF +
          "Content-Length: 31" + CRLF +
          "Content-Type: application/x-www-form-urlencoded" + CRLF +
          "userid=sforel&password=not2day";
      request = parseRequestText(requestText);
      assertFalse(request.isValid());


      
  }
}
