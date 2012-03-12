package com.shalvi.davos.session;

/**
 * Thrown when a session store can no longer represent its maximum number of sessions.  Typically
 * this is set in SessionManager.MAX_SESSIONS
 * @author jshalvi
 *
 */
public class SessionStoreFullException extends Exception {

    private static final long serialVersionUID = 1L;

}
