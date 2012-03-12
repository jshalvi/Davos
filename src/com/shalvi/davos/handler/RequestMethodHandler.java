package com.shalvi.davos.handler;

import java.util.EnumMap;
import java.util.Map;

import com.shalvi.davos.http.RequestMethod;
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
            public Context doMethod(Context context, RequestMethodHandler handler) {
                return handler.doGet(context);
            }
        });

        methodMap.put(RequestMethod.POST, new MethodHandler() {
            public Context doMethod(Context context, RequestMethodHandler handler) {
                return handler.doPost(context);
            }
        });

        methodMap.put(RequestMethod.HEAD, new MethodHandler() {
            public Context doMethod(Context context, RequestMethodHandler handler) {
                return handler.doHead(context);
            }
        });

        methodMap.put(RequestMethod.DELETE, new MethodHandler() {
            public Context doMethod(Context context, RequestMethodHandler handler) {
                return handler.doDelete(context);
            }
        });

        methodMap.put(RequestMethod.UNSUPPORTED, new MethodHandler() {
            public Context doMethod(Context context, RequestMethodHandler handler) {
                return handler.doUnsupported(context);
            }
        });
    }

    @Override
    public Context execute(Context context) {

        if (context == null) {
            throw new IllegalArgumentException();
        }

        MethodHandler handler = methodMap.get(context.getRequest().getMethod());

        return handler.doMethod(context, this);
    }


    public Context doGet(Context context) {
        return doUnsupported(context);
    }
    public Context doPost(Context context) {
        return doUnsupported(context);
    }
    public Context doHead(Context context) {
        return doUnsupported(context);
    }
    public Context doDelete(Context context) {
        return doUnsupported(context);
    }
    public Context doUnsupported(Context context) {
        Context cout = new Context(context.getRequest(),
                ResponseBuilder.getDefault501Response(),
                context.getSession());
        return cout;
    }
}

/**
 * Helper interface to define MethodHandlers within RequestMethodHandler
 * @author jshalvi
 *
 */
interface MethodHandler {
    public Context doMethod(Context context, RequestMethodHandler handler);
}