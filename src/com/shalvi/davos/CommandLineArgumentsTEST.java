package com.shalvi.davos;

import junit.framework.Assert;
import junit.framework.TestCase;

public class CommandLineArgumentsTEST extends TestCase {

  public void testValidArgs() {
    CommandLineArguments clargs;
    
    try {
      String[] args = {"-p","1234","-l","/var/www"};
      clargs = new CommandLineArguments(args);
      Assert.assertEquals(1234, clargs.getPort());
      Assert.assertEquals("/var/www", clargs.getRootDirectory());
    } catch (CommandLineArgumentsException e) {
      Assert.fail();
    }
    
    try {
      String[] args2 = {"-l","/var/www","-p","1234"};
      clargs = new CommandLineArguments(args2);
      Assert.assertEquals(1234, clargs.getPort());
      Assert.assertEquals("/var/www", clargs.getRootDirectory());
    } catch (CommandLineArgumentsException e) {
      Assert.fail();
    }

    try {
      String[] arg3 = {};
      clargs = new CommandLineArguments(arg3);
      Assert.assertEquals(-1, clargs.getPort());
      Assert.assertNull(clargs.getRootDirectory());
    } catch (CommandLineArgumentsException e) {
      Assert.fail();
    }

  }
  
  public void testInvalidArgs() {
    String[][] args1 = {
        {"-p","hello","-l","/var/www"},
        {"-p","1234","-l"},
        {"p","hello","-l","/var/www"}
    };
    
    String[] args;
    for (int i=0; i<args1.length; i++) {
      args = args1[i];
      try {
        new CommandLineArguments(args);
        Assert.fail("args1[" + i + "] did not throw CommandLineArgumentsException.");
      } catch (CommandLineArgumentsException e) {}
    }
  }
}
