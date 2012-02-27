package com.shalvi.davos;

import java.io.*;
import java.net.*;

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
        Response response;
        if (request.isValid()) {
          response = new Response(ResponseCode.SUCCESS_200, "Hello, there!");
          writer.print(response.toString());
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
