package com.shalvi.davos.session;

import org.junit.Before;

import junit.framework.Assert;
import junit.framework.TestCase;

public class SessionManagerTEST extends TestCase {

    private SessionManager manager;

    @Before
    public void setUp() {
        manager = new SessionManager();
    }

    public void testNewSession() {
        try {
            Session s = manager.newSession();
            assertNotNull(s);
            assertTrue(s instanceof Session);

        } catch(SessionStoreFullException e) {
            Assert.fail();
        }

    }

    public void testGetSession() {
        try {
            Session s1 = manager.newSession();
            Session s2 = manager.newSession();

            assertFalse(s1.equals(s2));
            assertFalse(s1 == s2);

            assertTrue(s1.equals(manager.getSession(s1.getId())));
            assertTrue(s2.equals(manager.getSession(s2.getId())));

            try {
                manager.getSession(-1);
                Assert.fail();
            } catch(IllegalArgumentException e) {}


        } catch(SessionStoreFullException e) {
            Assert.fail();
        }

    }
    
    public void testGetMissingSession() {
        try {
            Session s = manager.newSession();
            int newId = s.getId() == 1 ? 2 : 1;
            assertNull(manager.getSession(newId));
        } catch (SessionStoreFullException e) {
            Assert.fail();
        }
    }

    public void testStoreSession() {
        try {
            Session s = manager.newSession();
            manager.storeSession(s);
            assertTrue(s.equals(manager.getSession(s.getId())));
        } catch (SessionStoreFullException e) {
            Assert.fail();
        }
    }

    public void testMaxSessions() {
        int i = 0, max = 10;
        SessionManager manager = new SessionManager(max);

        try {
            for (i = 0; i < max+1; i++) {
                manager.newSession();
            }
            Assert.fail();
        } catch (SessionStoreFullException e) {}
        
        try {
            manager = new SessionManager(1);
            Session s = manager.newSession();
            
            int newId = s.getId() == 1 ? 2 : 1;
            
            manager.storeSession(new Session(newId));
            Assert.fail();
        } catch (SessionStoreFullException e) {}
        
        try {
            new SessionManager(-1);
            Assert.fail();
        } catch (IllegalArgumentException e) {}
        
        try {
            // Kind of meaningless...
            new SessionManager(Integer.MAX_VALUE + 1);
        } catch (IllegalArgumentException e) {}
        
        try {
            new SessionManager(0);
            Assert.fail();
        } catch (IllegalArgumentException e) {}
        
        new SessionManager(Integer.MAX_VALUE);
    }
}
