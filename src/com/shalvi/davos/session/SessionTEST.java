package com.shalvi.davos.session;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Before;

import junit.framework.Assert;
import junit.framework.TestCase;

public class SessionTEST extends TestCase {

    private Session session1;
    private Session session1b;
    private Session session2;
    private Session session3;

    @Before
    public void setUp() {
        session1 = new Session(1);
        session1.setData("testkey", "testval");
        
        session1b = new Session(1);
        session1b.setData("testkey", "testval");
        
        session2 = new Session(2);
        session2.setData("testkey", "testval2");

        session3 = new Session(3);
        session3.setData("testkey2", "testval2");
    }

    public void testSerialization()  {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream out = new ObjectOutputStream(baos);
            out.writeObject(session1);
            String serialized = baos.toString();

            InputStream is = new ByteArrayInputStream(serialized.getBytes());
            
            ObjectInputStream ois = new ObjectInputStream(is);
            
            Session unserializedSession = (Session)ois.readObject();
            
            assertEquals(session1.getId(), unserializedSession.getId());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void testSession() {
        
        try {
            session1.setData(null, "testkey");
            Assert.fail();
        } catch(IllegalArgumentException e) {}
        
        try {
            session1.setData("", "testkey");
            Assert.fail();
        } catch(IllegalArgumentException e) {}
        
        try {
            session1.setData("testkey", null);
            Assert.fail();
        } catch(IllegalArgumentException e) {}
        
      
        session1.setData("testkey", "testval5");
        assertEquals("testval5", session1.getData("testkey"));
        
        session1.setData("testkey", "");
        assertEquals("", session1.getData("testkey"));
        
        assertNull(session1.getData("missingKey"));
    }

    public void testHashCode() {
        assertEquals(session1.hashCode(), session1b.hashCode());
        assertEquals(session1.hashCode(), session1.hashCode());
        assertFalse(session2.hashCode() == session1.hashCode());
    }
    
    public void testClearData() {
        session1.clearData("testkey");
        assertNull(session1.getData("testkey"));
    }

    public void testEquals() {
        
        assertTrue(session1.equals(session1));
        assertTrue(session1.equals(session1b));
        assertTrue(session1b.equals(session1));
        assertFalse(session1.equals(new Object()));
        assertFalse(session1.equals(null));
        assertFalse(session1.equals(session2));
        assertFalse(session2.equals(session1));
    }
    
    public void testToString() {
        assertEquals("[Session id: 1, testkey=testval]", session1.toString());
    }
}
