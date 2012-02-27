package com.shalvi.davos.handler;

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
    
    // Null request
    try {
      response = handler.execute(null);
      Assert.fail();
    } catch (IllegalArgumentException e ) {}
    
    // Invalid request
    try {
      response = handler.execute(request);
      Assert.fail();
    } catch (IllegalArgumentException e ) {}
    
    
    // Default resource found
    // builder.setRequestURI("/");
    // response = handler.execute(builder.toNewRequest());
    
    // Default resource found
    // TODO: re-enable
    /*
    builder.initializeDefaults();
    builder.setRequestURI("/found.html");
    response = handler.execute(builder.toNewRequest());
    Assert.assertEquals(ResponseCode.SUCCESS_200, response.getResponseCode());
    // Resource not found
    builder.setRequestURI("/notfound.html");
    response = handler.execute(builder.toNewRequest());
    Assert.assertEquals(ResponseCode.ERROR_404, response.getResponseCode());
        */

  }

}
