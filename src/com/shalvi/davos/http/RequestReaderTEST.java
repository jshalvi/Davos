package com.shalvi.davos.http;

import java.io.BufferedReader;
import java.io.StringReader;

import junit.framework.Assert;
import junit.framework.TestCase;

public class RequestReaderTEST extends TestCase {
    
    public void testNullReader() {
        try {
            new RequestReader(null);
            Assert.fail();
        } catch(IllegalArgumentException e) {}
    }
    public void testReadLine() {
        
        String LINE1 = "GET /pub/WWW/TheProject.html HTTP/1.1",
            SEPARATOR = "\r\n",
            LINE2 = "test";
        
        // test line with \r or \n alone
        
        RequestReader reader = new RequestReader(
                new BufferedReader(new StringReader(LINE1 + SEPARATOR + LINE2)));
        
        Assert.assertEquals(LINE1, reader.readLine());
        Assert.assertEquals(LINE2, reader.readLine());
        Assert.assertNull(reader.readLine());
        
        reader = new RequestReader(
                new BufferedReader(new StringReader(SEPARATOR)));
        Assert.assertEquals("", reader.readLine());
        Assert.assertNull(reader.readLine());
        
        reader = new RequestReader(
                new BufferedReader(new StringReader("")));
        Assert.assertNull(reader.readLine());
      }
}
