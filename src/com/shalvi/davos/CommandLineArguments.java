package com.shalvi.davos;

/**
 * Parses and retrieves command-line arguments expected for the Davos server.
 * 
 * @author jshalvi
 *
 */
public class CommandLineArguments {

    private int port = -1;
    private String rootDirectory;
    private boolean printUsage;
    /**
     * Standard constructor.  Expects an array of strings which will be parsed upon
     * construction.
     * @throws CommandLineArgumentsException if an invalid argument was provided.
     */
    public CommandLineArguments(String[] args) throws CommandLineArgumentsException {

        String arg, value;

        if (args.length == 0) {
            return;
        }

        for (int i=0, inext=1; i < args.length; i++,inext++) {
            arg = args[i];

            if ("-p".compareTo(arg) == 0) {
                if (inext >= args.length) {
                    throw new CommandLineArgumentsException();
                }

                value = args[inext];

                try {
                    int p = Integer.parseInt(value);
                    port = p;
                    i++; inext++;
                } catch (NumberFormatException e) {
                    throw new CommandLineArgumentsException();
                }

            } else if ("--help".compareTo(arg) == 0) {
                printUsage = true;
                break;

            } else if ("-l".compareTo(arg) == 0) {
                if (inext >= args.length) {
                    throw new CommandLineArgumentsException();
                }

                rootDirectory = args[inext];
                i++; inext++;
            } else {
                // Unexpected or invalid option
                throw new CommandLineArgumentsException();
            }

        }
    }

    /**
     * Returns the port parsed from the args array.
     * @return -1 if argument was not provided, otherwise returns the desired port number.
     */
    public int getPort() {
        return port;
    }

    /**
     * Returns the root directory parsed from the args array.
     * @return null if the argument was not provided, otherwise returns the desired root directory.
     */
    public String getRootDirectory() {
        return rootDirectory;
    }

    /**
     * Indicates if the program should display the program usage and exit.
     * @return
     */
    public boolean printUsage() {
        return printUsage;
    }
}
