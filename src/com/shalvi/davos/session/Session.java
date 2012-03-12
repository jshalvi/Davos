package com.shalvi.davos.session;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Session implements Serializable {

    private static final long serialVersionUID = 1L;
    private Map<String, String> data = new HashMap<String, String>();
    private final int id;
    private volatile int hashCode;

    private boolean validKey(String key) {
        return !(key == null || key == "");
    }
    public Session(int id) {
        this.id = id;
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