package com.shalvi.davos.handler;

import org.junit.Before;

import com.shalvi.davos.http.Request;
import com.shalvi.davos.http.Response;

import junit.framework.Assert;
import junit.framework.TestCase;

public class RequestDispatcherTEST extends TestCase {
    
    RequestDispatcher dispatcher;
    RequestHandler handler1 = new RequestHandler() {
        public Response execute(Request request) {
            return new Response();
        }
    };
    
    RequestHandler handler2 = new RequestHandler() {
        public Response execute(Request request) {
            return new Response();
        }
    };
    
    
    @Before
    public void setUp() {
        dispatcher = new RequestDispatcher();
    }
    
    public void testAddHandler() {
        try {
            dispatcher.addHandler((String) null, handler1);
            Assert.fail();
        } catch (IllegalArgumentException e) {}
        
        try {
            dispatcher.addHandler("", handler1);
            Assert.fail();
        } catch (IllegalArgumentException e) {}
        
        try {
            dispatcher.addHandler("/", null);
            Assert.fail();
        } catch (IllegalArgumentException e) {}
        
        dispatcher.addHandler("/", handler2);       
    }
    
    public void testAddMultiURLHandler() {
        String[] invalidPatterns = {
                ""
        };
        String[] invalidPatterns2 = {
                "/",
                ".*",
                ""
        };
        String[] invalidPatterns3 = {
                null
        };
        String[] validPatterns = {
                "/",
                ".*",
                "/home"
        };
        try {
            dispatcher.addHandler((String[]) null, handler1);
            Assert.fail();
        } catch (IllegalArgumentException e) {}
        
        try {
            dispatcher.addHandler(invalidPatterns, handler1);
            Assert.fail();
        } catch (IllegalArgumentException e) {}
        
        try {
            dispatcher.addHandler(invalidPatterns2, null);
            Assert.fail();
        } catch (IllegalArgumentException e) {}
        
        try {
            dispatcher.addHandler(invalidPatterns3, null);
            Assert.fail();
        } catch (IllegalArgumentException e) {}
        
        // Ensure none of the handlers in the invalid patterns array
        // weren't added
        try {
            dispatcher.dispatch("/");
            Assert.fail();
        } catch(NoHandlerFoundException e) {}
        
        dispatcher.addHandler(validPatterns, handler2);       
    }
    
    public void testDispatch() {
        String[] request1Patterns = {
                "/",
                "/test",
                ".*ajax$"
        };
        
        dispatcher.addHandler(request1Patterns, handler1);
        dispatcher.addHandler(".*\\.html", handler2);
        dispatcher.addHandler(".*\\.js", handler2);
        dispatcher.addHandler(".*", handler2);
        
        try {
            assertEquals(handler1, dispatcher.dispatch("/")); 
            assertEquals(handler1, dispatcher.dispatch("/test")); 
            assertEquals(handler1, dispatcher.dispatch(".*ajax")); 
            assertEquals(handler2, dispatcher.dispatch("/test.html"));
            assertEquals(handler2, dispatcher.dispatch("/test.js"));
            assertEquals(handler2, dispatcher.dispatch("/test8.html"));
        } catch (NoHandlerFoundException e) {
            Assert.fail();
        }

    }
    
    public void testInsertionOrder() {
        dispatcher.addHandler(".*", handler2);
        dispatcher.addHandler("/", handler1);
        
        try {
            assertEquals(handler2, dispatcher.dispatch("/"));
        } catch (NoHandlerFoundException e) {
            Assert.fail();
        }
    }
    
    public void testMultiInsertionOrder() {
        String[] pattern1 = {
                "/",
                ".*"
        };
        String[] pattern2 = {
                "test\\.html",
                "test\\.js"
        };
        
        dispatcher.addHandler(pattern1, handler1);
        dispatcher.addHandler(pattern2, handler2);
        
        try {
            assertEquals(handler1, dispatcher.dispatch("/"));
            assertEquals(handler1, dispatcher.dispatch("test.html"));
            assertEquals(handler1, dispatcher.dispatch("test.js"));
        } catch(NoHandlerFoundException e) {
            Assert.fail();
        }
    }
    
    public void testHandlerNotFound() {
        dispatcher.addHandler("/", handler1);
        
        try {
            dispatcher.dispatch("test.html");
            Assert.fail();
        } catch(NoHandlerFoundException e) {}
    }

}
