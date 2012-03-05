package com.shalvi.davos;

import java.io.*;
import java.net.*;

import com.shalvi.davos.handler.RequestHandler;
import com.shalvi.davos.handler.StaticFileRequestHandler;
import com.shalvi.davos.http.Request;
import com.shalvi.davos.http.Response;
import com.shalvi.davos.http.RequestParser;
import com.shalvi.davos.http.ResponseCode;

public class Main {

  private static int DEFAULT_PORT = 1234;
  private static String DEFAULT_ROOT_DIRECTORY = ".";
  private static String USAGE = "Usage: [-p port] [-l location_root]";
  public static void main(String[] args) {
    int port = DEFAULT_PORT;
    String root_directory = DEFAULT_ROOT_DIRECTORY;
    CommandLineArguments cargs;
    
    try {
      cargs = new CommandLineArguments(args);
    } catch (CommandLineArgumentsException e) {
      System.out.println("1" + USAGE);
      return;
    }
    
    if (cargs.getPort() > 0) {
      port = cargs.getPort();
    }
    
    System.out.println("Starting on port " + port + "...");
    
    BufferedReader reader;
    PrintWriter writer;
    

    try {
      ServerSocket serverSocket = new ServerSocket(port);

      while(true) {
        Socket socket = serverSocket.accept();
        
        writer = new PrintWriter(socket.getOutputStream());
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        Request request = RequestParser.parseRequest(reader);
        System.out.println(request.toString());
        Response response;
        
        if (request.isValid()) {
          
          RequestHandler handler = new StaticFileRequestHandler();
          response = handler.execute(request);
          BufferedReader responseReader = response.getReader();
          String line;
          if (responseReader != null) {
            while ((line = responseReader.readLine()) != null) {
              writer.print(line);
            }
          }
        } else {
          response = new Response(ResponseCode.ERROR_404, "File not found, bro");
          writer.print(response.toString());
        }

        writer.flush();
        writer.close();
        reader.close();
        socket.close();
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
