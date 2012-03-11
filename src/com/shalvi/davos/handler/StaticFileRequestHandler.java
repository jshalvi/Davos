package com.shalvi.davos.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.shalvi.davos.http.Request;
import com.shalvi.davos.http.Response;
import com.shalvi.davos.http.ResponseBuilder;
import com.shalvi.davos.http.ResponseCode;

public class StaticFileRequestHandler extends RequestMethodHandler {
    private File rootDirectory;

    @Override
    public Response doGet(Request request) {
        Response response;

        if (request == null) {
            throw new IllegalArgumentException("Null request object");
        }

        if (!request.isValid()) {
            throw new IllegalArgumentException("Invalid request object.");
        }

        File f = new File(rootDirectory.getAbsolutePath() + request.getRequestURI());
        ResponseBuilder builder = new ResponseBuilder();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(f));
            
            /*
             * Right now I don't know of a good way to re-use this reader, creating
             * a second one just to calculate length.
             */
            BufferedReader reader2 = new BufferedReader(new FileReader(f));
            
            int contentLength = determineContentLength(reader2);
            
            if (contentLength > 0) {
                builder.setResponseCode(ResponseCode.SUCCESSFUL_200);
                builder.setReader(reader);
                builder.setContentLength(contentLength);
                response = builder.toResponse();
            } else {
                response = ResponseBuilder.getDefault404Response();
            }
            
        } catch (FileNotFoundException e) {
            response = ResponseBuilder.getDefault404Response();
        }

        return response;      
    }
    
    @Override
    public Response doPost(Request request) {
        return doGet(request);
    }

    @Override
    public Response doHead(Request request) {
        return ResponseBuilder.getHeadersOnlyResponse(doGet(request));
    }
    
    int determineContentLength(BufferedReader reader) {
        int len = -1;
        char c = ' ';
        try {
            while (c != (char) -1) {
                c = (char) reader.read();
                len++;
            }
            if (len == 0) len = -1;

        } catch (IOException e) {
            e.printStackTrace();
            len = -1;
        }
        
        return len;
    }

    public void setRootDirectory(String path) throws IllegalArgumentException {

        if (path == null) {
            throw new IllegalArgumentException();
        }

        File f = new File(path);

        if (!f.isDirectory()) {
            throw new IllegalArgumentException(
                    "Specified path is not a directory: " + path);
        }

        rootDirectory = f;
    }
}
