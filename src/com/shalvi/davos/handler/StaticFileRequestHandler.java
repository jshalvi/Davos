package com.shalvi.davos.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.shalvi.davos.http.Request;
import com.shalvi.davos.http.Response;
import com.shalvi.davos.http.ResponseBuilder;
import com.shalvi.davos.http.ResponseCode;

public class StaticFileRequestHandler implements RequestHandler {
    private File rootDirectory;

    @Override
    public Response execute(Request request) {

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
            builder.setResponseCode(ResponseCode.SUCCESS_200);
            builder.setReader(reader);
        } catch (FileNotFoundException e) {
            builder.setResponseCode(ResponseCode.ERROR_404);
            // builder.setBody(ResponseCode.ERROR_404.getReasonPhrase());
        }

        return builder.toResponse();      
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
