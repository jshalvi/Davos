package com.shalvi.davos.handler;

import com.shalvi.davos.session.Session;

import junit.framework.TestCase;

public class ContextTEST extends TestCase {

    public void testGetSession() {
        Session session = new Session(1);
        Context c = new Context(null, null, session);
        
        Session copy = c.getSession();
        
        assertFalse(copy == session);
        assertTrue(copy.equals(session));
    }
}
