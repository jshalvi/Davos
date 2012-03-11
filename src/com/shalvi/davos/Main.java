package com.shalvi.davos;

import com.shalvi.davos.server.Server;

public class Main {

  private static int DEFAULT_PORT = 1234;
  private static String USAGE = "Usage: [-p port] [-l location_root]";
  public static void main(String[] args) {
    int port = DEFAULT_PORT;
    CommandLineArguments cargs;
    
    try {
      cargs = new CommandLineArguments(args);
    } catch (CommandLineArgumentsException e) {
      System.out.println(USAGE);
      return;
    }
    
    if (cargs.getPort() > 0) {
      port = cargs.getPort();
    }
    
    Server server = new Server();
    server.setPort(port);
    server.run();
  }
}
