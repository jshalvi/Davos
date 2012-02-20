package com.shalvi.davos.http;

import junit.framework.Assert;
import junit.framework.TestCase;

public class RequestTEST extends TestCase {

  public void testRequestLine() {
    Request r;
    
    // Bad Method
    r = new Request("/pub/WWW/TheProject.html HTTP/1.1");
    Assert.assertFalse(r.isValid());

    // Bad URI
    r = new Request("GET HTTP/1.1");
    Assert.assertFalse(r.isValid());

    // Bad HTTP version
    r = new Request("GOT /pub/WWW/TheProject.html HTTP/1.1");
    Assert.assertFalse(r.isValid());
    
    r = new Request("GET /pub/WWW/TheProject.html HTTP/0.1");
    Assert.assertFalse(r.isValid());

    r = new Request("GET /pub/WWW/TheProject.html HTTP/");
    Assert.assertFalse(r.isValid());

    r = new Request("GET /pub/WWW/TheProject.html /0.1");
    Assert.assertFalse(r.isValid());

    r = new Request("GET /pub/WWW/TheProject.html /");
    Assert.assertFalse(r.isValid());
  }
  
  public void testGETRequest() {
    Request r;
    
    r = new Request("GET /pub/WWW/TheProject.html HTTP/1.1");
    Assert.assertEquals(Method.GET, r.getMethod());
    Assert.assertTrue(r.isValid());
  }
  
  public void testPOSTRequest() {
    Assert.fail();
  }
  
  public void testHEADRequest() {
    Assert.fail();
  }
  
  public void testDELETERequest() {
    Assert.fail();
  }// bob
}
