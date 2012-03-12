package com.shalvi.davos.session;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a unique session during the server instance.  Each session contains a
 * unique ID, and contains a map of key/value String pairs which may be set during
 * the course of handling a request.  If modified within a method, a copy of the session
 * should be returned using the copy constructor. 
 * @author jshalvi
 *
 */
public class Session implements Serializable {

    private static final long serialVersionUID = 1L;
    private Map<String, String> data = new HashMap<String, String>();
    private final int id;
    private volatile int hashCode;
    
    public static String SESSION_KEY = "davossession";

    private boolean validKey(String key) {
        return !(key == null || key == "");
    }
    
    /**
     * Constructor.
     * @param id of the session, must be zero or greater
     * @throws IllegalArgumentException if id < 0
     */
    public Session(int id) {
        if (id < 0) {
            throw new IllegalArgumentException();
        }
        this.id = id;
    }
    
    /** 
     * Copy constructor.
     * @param session
     * @throws IllegalArgumentException if session is null
     */
    public Session(Session session) {
        if (session == null) {
            throw new IllegalArgumentException();
        }
        
        id = session.id;
        data = new HashMap<String, String>(session.data);
    }

    public int getId() {
        return id;
    }

    public void setData(String key, String val) {
        if (!validKey(key) || val == null) {
            throw new IllegalArgumentException();
        }

        data.put(key, val);
    }

    public String getData(String key) {
        if (!validKey(key)) {
            throw new IllegalArgumentException();
        }

        return data.get(key);
    }

    public void clearData(String key) {
        if (!validKey(key)) {
            throw new IllegalArgumentException();
        }

        data.remove(key);
    }

    public String toString() {
        String out = "[Session id: " + id;
        for(Map.Entry<String, String> entry : data.entrySet()) {
            out += ", " + entry.getKey() + "=" + entry.getValue();
        }
        return out + "]";
    }

    public boolean equals(Object thatObject) {
        Session that;
        if (!(thatObject instanceof Session)) {
            return false;
        }
        that = (Session) thatObject;
        
        return (this == that) ||
            (id == that.id && data.equals(that.data));
    }

    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = 17;
            result = 31 * result + id;
            result = 31 * result + data.hashCode();
        }
        return result;
    }
}
