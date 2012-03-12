package com.shalvi.davos.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.shalvi.davos.handler.Context;
import com.shalvi.davos.handler.NoHandlerFoundException;
import com.shalvi.davos.handler.RequestDispatcher;
import com.shalvi.davos.handler.RequestHandler;
import com.shalvi.davos.handler.StaticFileRequestHandler;
import com.shalvi.davos.http.Request;
import com.shalvi.davos.http.RequestParser;
import com.shalvi.davos.http.RequestReader;
import com.shalvi.davos.http.Response;
import com.shalvi.davos.http.ResponseBuilder;
import com.shalvi.davos.http.ResponseReader;
import com.shalvi.davos.session.Session;
import com.shalvi.davos.session.SessionManager;
import com.shalvi.davos.session.SessionStoreFullException;

public class Server {
    private static int DEFAULT_PORT = 8888;
    private int port = DEFAULT_PORT;
    private String rootDirectory = ".";
    private RequestDispatcher dispatcher = new RequestDispatcher();

    /**
     * Constructor.
     */
    public Server() {
        initializeDefaultHandlers();
    }

    /**
     * Constructor
     * @param port on which the server should listen.
     * @param rootDirectory for static files.
     */
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

    /**
     * Registers a request handler to a URI pattern.  Patterns are checked in the order in which
     * they were added, with older patterns taking priority over newly added patterns.
     * @param pattern to be used as a regex pattern against request URI's.
     * @param handler to be executed.
     */
    public void addHandler(String pattern, RequestHandler handler) {
        dispatcher.addHandler(pattern, handler);
    }

    /**
     * Registers a request handler to multiple URI patterns.  This is a convenience method to 
     * use a single handler for multiple patterns.  Patterns are checked in the order in which
     * they were added, with older patterns taking priority over newly added patterns.

     * @param patterns to be used as regex patterns against request URI's.
     * @param handler to be executed.
     */
    public void addHandler(String[] patterns, RequestHandler handler) {
        dispatcher.addHandler(patterns, handler);
    }

    /**
     * Sets the port on which the server should listen.
     * @param port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Sets the root directory to use for serving static files.
     * @param rootDirectory
     */
    public void setRootDirectory(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    private void log(String s) {
        System.out.println(s);
    }

    /**
     * Starts the server.
     */
    public void run() {
        RequestReader reader;
        PrintWriter writer;
        RequestHandler handler;
        Request request;
        Response response;
        Session session;
        SessionManager sessionManager = new SessionManager();

        log("Starting on port " + port + "...");


        try {
            ServerSocket serverSocket = new ServerSocket(port);

            while(true) {
                Socket socket = serverSocket.accept();

                writer = new PrintWriter(socket.getOutputStream());
                reader = new RequestReader(
                        new BufferedReader(new InputStreamReader(socket.getInputStream())));

                request = RequestParser.parseRequest(reader);

                String sessionIdStr = request.getCookie(Session.SESSION_KEY);
                session = null;
                try {
                    if (sessionIdStr != null && sessionIdStr.length() > 0) {
                        int sessionId = Integer.parseInt(sessionIdStr);
                        session = sessionManager.getSession(sessionId);
                        session = session == null ? sessionManager.newSession() : session;
                    }
                    if (sessionIdStr == null) {
                        session = sessionManager.newSession();
                    }
                } catch (SessionStoreFullException e) {
                    log("WARNING:  Session store is full.  No new sessions will be created");
                }

                log(request.toString());

                if (request.isValid()) {

                    try {
                        handler = dispatcher.dispatch(request.getRequestURI());
                        response = handler.execute(new Context(request, null, session)).getResponse();
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
