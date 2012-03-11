package com.shalvi.davos.handler;

import java.util.EnumMap;
import java.util.Map;

import com.shalvi.davos.http.Request;
import com.shalvi.davos.http.RequestMethod;
import com.shalvi.davos.http.Response;
import com.shalvi.davos.http.ResponseBuilder;

/**
 * An abstract base class structured around the RequestMethod enum, providing a
 * structured way to delegate requests tied to specific request methods.  Handlers which
 * need built-in method support would benefit from extending RequestMethodHandler.
 * 
 * @author jshalvi
 *
 */
abstract class RequestMethodHandler implements RequestHandler {

    private static final Map<RequestMethod, MethodHandler> methodMap = 
        new EnumMap<RequestMethod, MethodHandler>(RequestMethod.class);

    static {
        methodMap.put(RequestMethod.GET, new MethodHandler() {
            public Response doMethod(Request request, RequestMethodHandler handler) {
                return handler.doGet(request);
            }
         });
         
        methodMap.put(RequestMethod.POST, new MethodHandler() {
            public Response doMethod(Request request, RequestMethodHandler handler) {
                return handler.doPost(request);
            }
         });
         
        methodMap.put(RequestMethod.HEAD, new MethodHandler() {
            public Response doMethod(Request request, RequestMethodHandler handler) {
                return handler.doHead(request);
            }
         });
         
        methodMap.put(RequestMethod.DELETE, new MethodHandler() {
            public Response doMethod(Request request, RequestMethodHandler handler) {
                return handler.doDelete(request);
            }
         });
         
        methodMap.put(RequestMethod.UNSUPPORTED, new MethodHandler() {
            public Response doMethod(Request request, RequestMethodHandler handler) {
                return handler.doUnsupported(request);
            }
         });
    }

    @Override
    public Response execute(Request request) {
        
        if (request == null) {
            throw new IllegalArgumentException();
        }
        
        MethodHandler handler = methodMap.get(request.getMethod());

        return handler.doMethod(request, this);
    }
    

    public Response doGet(Request request) {
        return doUnsupported(request);
    }
    public Response doPost(Request request) {
        return doUnsupported(request);
    }
    public Response doHead(Request request) {
        return doUnsupported(request);
    }
    public Response doDelete(Request request) {
        return doUnsupported(request);
    }
    public Response doUnsupported(Request request) {
        return ResponseBuilder.getDefault501Response();
    }
}

/**
 * Helper interface to define MethodHandlers within RequestMethodHandler
 * @author jshalvi
 *
 */
interface MethodHandler {
    public Response doMethod(Request request, RequestMethodHandler handler);
}