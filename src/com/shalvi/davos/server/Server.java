package com.shalvi.davos.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.shalvi.davos.handler.RequestHandler;
import com.shalvi.davos.handler.StaticFileRequestHandler;
import com.shalvi.davos.http.Request;
import com.shalvi.davos.http.RequestParser;
import com.shalvi.davos.http.Response;
import com.shalvi.davos.http.ResponseCode;

public class Server {
// set port
    private static int DEFAULT_PORT = 8888;
    private int port;
    private String rootDirectory;
    
    public Server() {
        port = DEFAULT_PORT;
        rootDirectory = ".";
    }
    
    public void setPort(int port) {
        this.port = port;
    }
    
    public void setRootDirectory(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }
    
    private void log(String s) {
        System.out.println(s);
    }
    
    public void run() {
        BufferedReader reader;
        PrintWriter writer;
        
        log("Starting on port " + port + "...");

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
