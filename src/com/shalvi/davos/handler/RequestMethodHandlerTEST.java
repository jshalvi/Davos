package com.shalvi.davos.handler;

import com.shalvi.davos.http.MockRequestBuilder;
import com.shalvi.davos.http.Response;
import com.shalvi.davos.http.Request;
import com.shalvi.davos.http.RequestMethod;
import com.shalvi.davos.http.ResponseCode;

import junit.framework.TestCase;

public class RequestMethodHandlerTEST extends TestCase {

    public void testSupportedMethods() {
        RequestHandler handler = new RequestMethodHandler() {};
        
        MockRequestBuilder builder = new MockRequestBuilder();
        builder.initializeDefaults();

        for (RequestMethod m : RequestMethod.values()) {
            builder.setMethod(m);
            Request r = builder.toNewRequest();
            
            Response response = handler.execute(new Context(r, null, null)).getResponse();
            assertEquals(ResponseCode.SERVER_ERROR_501.toString(), response.getResponseCode().toString());
        }
    }    
}
