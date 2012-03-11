package com.shalvi.davos;

import java.io.File;

import com.shalvi.davos.server.Server;

public class Main {

    private static int DEFAULT_PORT = 1234;
    private static String USAGE = "Usage: [-p port] [-l location_root]";

    private static void printUsage() {
        System.out.println(USAGE);
    }
    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        String rootDirectory = "";
        
        CommandLineArguments cargs;

        try {
            cargs = new CommandLineArguments(args);
        } catch (CommandLineArgumentsException e) {
            printUsage();
            return;
        }

        if (cargs.getPort() > 0) {
            port = cargs.getPort();
        }

        rootDirectory = cargs.getRootDirectory() == null ? "." : cargs.getRootDirectory();
        Server server = new Server();
        server.setPort(port);
        server.setRootDirectory(rootDirectory);
        server.run();
    }
}
