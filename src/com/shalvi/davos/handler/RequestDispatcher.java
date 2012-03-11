package com.shalvi.davos.handler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Dispatches a request URI to its registered handler.  A handler is registered to the dispatcher
 * with a give pattern or set of patterns.
 * @author jshalvi
 *
 */
public class RequestDispatcher {
    
    private Map<Pattern, RequestHandler> handlers = new LinkedHashMap<Pattern, RequestHandler>();
    
    /**
     * Register a request handler to the dispatcher.
     * @param pattern any non-empty string to be used as a regex against incoming URI's.
     * @param handler 
     * @throws IllegalArgumentException if uri is empty or null, or if handler is null.
     */
    public void addHandler(String pattern, RequestHandler handler) throws IllegalArgumentException {
        if (pattern == null || pattern.length() == 0 || handler == null) {
            throw new IllegalArgumentException();
        }
        
        String[] patterns = new String[1];
        patterns[0] = pattern;
        addHandler(patterns, handler);
    }
    
    /**
     * Register a request handler to the dispatcher with an array of uri patterns.  This
     * is a convenience method to allow the registration of multiple uri patterns to one
     * handler.
     * @param patterns
     * @param handler
     * @throws IllegalArgumentException if any uri is empty or null, or if handler is null.
     */
    public void addHandler(String[] patterns, RequestHandler handler) throws IllegalArgumentException {
        
        Map<Pattern, RequestHandler> m = new LinkedHashMap<Pattern, RequestHandler>();
        
        if (patterns == null || patterns.length == 0 || handler == null) {
            throw new IllegalArgumentException();
        }

        for (String s : patterns) {
            if (s == null || s.length() == 0) {
                throw new IllegalArgumentException();
            }
            
            try {
                m.put(Pattern.compile(s), handler);
            } catch(PatternSyntaxException e) {
                throw new IllegalArgumentException("Bad pattern: " + s);
            }            
        }
        
        handlers.putAll(m);        
    }
    
    /**
     * Attempts to find a handler whose pattern matches the given URI.
     * @param uri
     * @return RequestHandler to execute.
     * @throws NoHandlerFoundException if a handler is not found.
     */
    public RequestHandler dispatch(String uri) throws NoHandlerFoundException {
        Matcher m;
        RequestHandler handler = null;
        
        for (Pattern pattern : handlers.keySet()) {
            m = pattern.matcher(uri);
            if (m.matches()) {
                handler = handlers.get(pattern);
                break;
            }
        }
        
        if (handler == null) {
            throw new NoHandlerFoundException();
        }
        
        return handler;
    }
}
