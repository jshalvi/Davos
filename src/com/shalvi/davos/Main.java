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

  public static void main(String[] args) {
    System.out.println("Starting...");
    
    BufferedReader reader;
    PrintWriter writer;
    

    try {
      ServerSocket serverSocket = new ServerSocket(1948);

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
