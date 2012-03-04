package com.shalvi.davos;

/**
 * Parses and retrieves command-line arguments expected for the Davos server.
 * 
 * @author jshalvi
 *
 */
public class CommandLineArguments {

  /**
   * Standard constructor.  Expects an array of strings which will be parsed upon
   * construction.
   * @throws CommandLineArgumentsException if an invalid argument was provided.
   */
  public CommandLineArguments(String[] args) throws CommandLineArgumentsException {
    throw new CommandLineArgumentsException();
  }
  
  /**
   * Returns the port parsed from the args array.
   * @return -1 if argument was not provided, otherwise returns the desired port number.
   */
  public int getPort() {
    return -1;
  }
  
  /**
   * Returns the root directory parsed from the args array.
   * @return null if the argument was not provided, otherwise returns the desired root directory.
   */
  public String getRootDirectory() {
    return null;
  }
}
