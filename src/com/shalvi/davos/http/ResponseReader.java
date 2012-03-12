package com.shalvi.davos.http;

import java.io.IOException;

/**
 * Reader to be used for writing to the socket's output stream.  Currently, streaming
 * is not supported.  Use toString().
 * @author jshalvi
 *
 */
public class ResponseReader {

    private Response response;
    private static String CRLF = "\r\n";

    public ResponseReader(Response response) {
        if (response == null) {
            throw new IllegalArgumentException();
        }
        this.response = response;
    }

    /**
     * Returns the response text as a string.  This string may then be written
     * to the socket's output stream.
     */
    public String toString() {
        /*
         * TODO:  Not the best way to do this.  Need to figure out a way to stream this back
         * to Server.  This will do for now.
         */
        String out = "";

        out += response.getHTTPVersion().toString() + " ";
        out += response.getResponseCode().toString() + CRLF;
        out += response.headersToHTTPString() + CRLF;
        if (response.getReader() != null) {
            char[] cbuf = new char[response.getContentLength()];

            try {
                response.getReader().read(cbuf, 0, response.getContentLength());
            } catch (IOException e) {
                e.printStackTrace();
            }

            out += new String(cbuf);
        }
        return out;
    }
}
