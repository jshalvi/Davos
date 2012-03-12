package com.shalvi.davos.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Manages sessions for a given Server instance.  Sessions are identified by a unique id.  There
 * should generally be one Session Manager per server instance.
 * @author jshalvi
 *
 */
public class SessionManager {

    private static int MAX_SESSIONS = 100;
    
    private Map<Integer, Session> sessionStore = new HashMap<Integer, Session>();
    private int maxSessions = MAX_SESSIONS;
    /**
     * Default constructor.
     */
    public SessionManager() {}
    

    /**
     * Construct a SessionManager with a capacity of max_sessions number of sessions.
     * @param maxSessions an int greater than zero and less than Integer.MAX_VALUE
     * @throws IllegalArgumentException if max_sessions is not within bounds
     */
    public SessionManager(int maxSessions) {
        if (maxSessions <= 0 || maxSessions > Integer.MAX_VALUE) {
            throw new IllegalArgumentException();
        }
        
        this.maxSessions = maxSessions;
    }
    /**
     * Creates a new session with a unique id and places it in the cache.
     * @return a new, unique session.
     */
    public Session newSession() throws SessionStoreFullException {

        Random rand = new Random();
        
        int i = 0;
        int maxSessionsRemaining = maxSessions - sessionStore.size();
        Integer newId = rand.nextInt(Integer.MAX_VALUE);
        while(sessionStore.containsKey(newId)) {
            newId = rand.nextInt(Integer.MAX_VALUE);
            i++;
            if(i > maxSessionsRemaining) {
                throw new SessionStoreFullException();
            }
        }
        
        Session s = new Session(newId);
        storeSession(s);
        return s;
    }
    
    /**
     * Gets an existing session with the given id
     * @param id
     * @return a session with the given id, otherwise null.
     * @throws IllegalArgumentException if id < 0
     */
    public Session getSession(Integer id) {
        if (id < 0) {
            throw new IllegalArgumentException();
        }
        
        return sessionStore.get(id);
    }
    
    /**
     * Stores the given session in the session cache under its unique id.
     * @param session
     * @throws SessionStoreFullException if attempting to store a new session when
     * the store is already full.
     */
    public void storeSession(Session session) throws SessionStoreFullException {
        if(!sessionStore.containsKey(session.getId()) && sessionStore.size() == maxSessions) {
            throw new SessionStoreFullException();
        }
        
        sessionStore.put(session.getId(), session);
    }
}
