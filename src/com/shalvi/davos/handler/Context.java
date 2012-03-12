package com.shalvi.davos.handler;

import com.shalvi.davos.http.Request;
import com.shalvi.davos.http.Response;
import com.shalvi.davos.session.Session;

/**
 * Holds the request, response, and session of any given request.  Any of these
 * may be null at any time.  Since the session is the only object which may mutate
 * during the handling of a request, a copy is always return in a call to getSession().
 * 
 * @author jshalvi
 *
 */
public class Context {
    private final Session session;
    private final Request request;
    private final Response response;

    /**
     * Constructor
     * @param request
     * @param response
     * @param session
     */
    public Context(Request request, Response response, Session session) {
        this.request = request;
        this.response = response;
        this.session = session;
    }

    /**
     * Gets the current request.
     * @return the request if available, otherwise null.
     */
    public Request getRequest() {
        return request;
    }

    /**
     * Gets the current response
     * @return the response if available, otherwise null.
     */
    public Response getResponse() {
        return response;
    }

    /**
     * Returns a copy of the session.
     * @return null if no session available, otherwise returns a copy of the session.
     */
    public Session getSession() {
        Session s = null;

        if ( session != null) {
            s = new Session(session);
        }

        return s;
    }
}
