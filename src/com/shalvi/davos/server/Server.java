package com.shalvi.davos.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.shalvi.davos.handler.NoHandlerFoundException;
import com.shalvi.davos.handler.RequestDispatcher;
import com.shalvi.davos.handler.RequestHandler;
import com.shalvi.davos.handler.StaticFileRequestHandler;
import com.shalvi.davos.http.Request;
import com.shalvi.davos.http.RequestParser;
import com.shalvi.davos.http.Response;
import com.shalvi.davos.http.ResponseBuilder;
import com.shalvi.davos.http.ResponseReader;

public class Server {
    private static int DEFAULT_PORT = 8888;
    private int port = DEFAULT_PORT;
    private String rootDirectory = ".";
    private RequestDispatcher dispatcher = new RequestDispatcher();

    public Server() {
        initializeDefaultHandlers();
    }
    
    public Server(int port, String rootDirectory) {
        setPort(port);
        setRootDirectory(rootDirectory);
        initializeDefaultHandlers();
    }

    private void initializeDefaultHandlers() {
        StaticFileRequestHandler staticFileHandler = new StaticFileRequestHandler();
        staticFileHandler.setRootDirectory(rootDirectory);
        dispatcher.addHandler(".*", staticFileHandler);
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
        RequestHandler handler;
        Request request;
        Response response;

        log("Starting on port " + port + "...");


        try {
            ServerSocket serverSocket = new ServerSocket(port);

            while(true) {
                Socket socket = serverSocket.accept();

                writer = new PrintWriter(socket.getOutputStream());
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                request = RequestParser.parseRequest(reader);
                log(request.toString());

                if (request.isValid()) {

                    try {
                        handler = dispatcher.dispatch(request.getRequestURI());
                        response = handler.execute(request);
                    } catch (NoHandlerFoundException e) {
                        response = ResponseBuilder.getDefault404Response();
                    }
                    
                } else {
                    /*
                     * TODO: check to see if a more appropriate "Invalid request" 
                     * response exists.
                     */
                    response = ResponseBuilder.getDefault404Response();
                }
                
                ResponseReader responseReader = new ResponseReader(response);
                String responseText = responseReader.toString();
                // System.out.println(responseText);
                writer.print(responseText);

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
