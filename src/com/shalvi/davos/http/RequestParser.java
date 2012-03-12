package com.shalvi.davos.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



enum ParserState {
  START_LINE() {
    public Request parse(RequestReader reader, Request request) {
      Request r = new Request(request);
      Request rParsed;
      String requestLine = reader.readLine();
      
      rParsed = RequestParser.parseRequestLine(requestLine);
      r.setMethod(rParsed.getMethod());
      r.setRequestURI(rParsed.getRequestURI());
      r.setHTTPVersion(rParsed.getHTTPVersion());
      
      r.setValid(
          !(r.getMethod() == RequestMethod.UNSUPPORTED || 
          r.getRequestURI() == "" ||
          r.getHTTPVersion() == HTTPVersion.UNSUPPORTED));

      return r;
    }
    
    public ParserState getNext(Request request) {
      return REQUEST_HEADERS;
    }
  },
  REQUEST_HEADERS() {
    public Request parse(RequestReader reader, Request request) {
      Request r = new Request(request);
      String line = reader.readLine();
      HeaderField field;
      
      if (line == null) {
          
        // We prematurely reached the end of a request
        r.setValid(false);
      } else {
        while (line != null) {
          if (line == "") {
            break;
          }
          field = RequestParser.parseHeaderField(line);
          
          if (HeaderFieldName.UNSPECIFIED != r.getHeaderField(field.getKey()).getKey()) {
              r.setValid(false);
              break;
          }
          if (HeaderFieldName.UNSUPPORTED != field.getKey()) {
              r.setHeaderField(RequestParser.parseHeaderField(line));
          }
          
          line = reader.readLine();
        }
      }
      return r;
    }
            
    public ParserState getNext(Request request) {
        int len = request.getContentLength();
      if (request.getMethod().equals(RequestMethod.POST) && len > 0) {
          return REQUEST_POSTDATA;
      }
      return END;
    }
  },
  REQUEST_POSTDATA() {
    public Request parse(RequestReader reader, Request request) {
        Request r = new Request(request);
        Map<String, String> postData;

        char[] cbuf = new char[request.getContentLength()];
        int rStatus;
        
        try {
            rStatus = reader.read(cbuf, 0, request.getContentLength());
        } catch (IOException e) {
            r.setValid(false);
            return r;
        }
        
        
        if (rStatus <= 0 || rStatus != r.getContentLength()) {
            r.setValid(false);
            return r;
        }
        
        try {
            String line = new String(cbuf);
            postData = RequestParser.parseUrlEncodedPostdata(line);
        } catch (IllegalArgumentException e) {
            r.setValid(false);
            return r;
        }
        
        for (String key : postData.keySet()) {
            r.setPostdata(key, postData.get(key));
        }
        
        return r;
    }
    
    public ParserState getNext(Request request) {
      return END;
    }
    
  },
  END() {
    public Request parse(RequestReader reader, Request request) {
      Request r = new Request(request);
      return r;
    }
    
    public ParserState getNext(Request request) {
      return null;
    }
  };
  
  public abstract Request parse(RequestReader reader, Request request);
  public abstract ParserState getNext(Request request);
}

public class RequestParser {
  
  /*
   *
   * parsing states
   * 
   */
  static boolean validateRequestLine(String requestLine) {
    return  !(requestLine == null || requestLine.length() == 0 || requestLine.startsWith(" "));

  }
  
  static RequestMethod parseMethod(String requestLine) {
    RequestMethod method = RequestMethod.UNSUPPORTED;
    String[] tokens;
    String methodToken;
    
    if (!validateRequestLine(requestLine)) {
      
      return RequestMethod.UNSUPPORTED;
    }

    tokens = requestLine.split(" ");    
    if (tokens.length == 0) {
      return RequestMethod.UNSUPPORTED;
    }
    methodToken = tokens[0];
    
    for (RequestMethod m : RequestMethod.values()) {
      if (m.toString().equals(methodToken)) {
        method = m;
        break;
      }
    }
    return method;
  }
  
  static String parseURILocator(String requestLine) {
    String[] tokens;
    
    if (!validateRequestLine(requestLine)) {
      return "";
    }
    
    tokens = requestLine.split("\\s+");
    if (tokens.length < 2) {
      return "";
    }
    
    return tokens[1];
  }
  
  static HTTPVersion parseHTTPVersion(String requestLine) {
    String[] tokens;
    String httpVersionToken;
    HTTPVersion httpVersion = HTTPVersion.UNSUPPORTED;

    if (!validateRequestLine(requestLine)) {
      return HTTPVersion.UNSUPPORTED;
    }
    
    tokens = requestLine.split("\\s+");    
    if (tokens.length < 3) {
      return HTTPVersion.UNSUPPORTED;
    }
    httpVersionToken = tokens[2];

    for (HTTPVersion v : HTTPVersion.values()) {
      if (v.toString().equals(httpVersionToken)) {
        httpVersion = v;
        break;
      }
    }
    
    return httpVersion;
  }
  
  static Request parseRequestLine(String requestLine) {
    Request request = new Request();
    
    if (!validateRequestLine(requestLine)) {
      return request;
    }
    
    request.setMethod(parseMethod(requestLine));
    request.setRequestURI(parseURILocator(requestLine));
    request.setHTTPVersion(parseHTTPVersion(requestLine));
    if (
        request.getMethod() == RequestMethod.UNSUPPORTED || 
        request.getRequestURI() == "" ||
        request.getHTTPVersion() == HTTPVersion.UNSUPPORTED) {
      
      request = new Request();
    }

    return request;
  }
  
  /**
   * Parses url-encoded postdata.
   * @param line
   * @return a new map whose postdata contains the parsed items, or null if the postdata was invalid.
   */
  static Map<String, String> parseUrlEncodedPostdata(String line) {
      Map<String,String> m = new HashMap<String, String>();
      
      if (line == null) {
          throw new IllegalArgumentException();
      }
      
      if (line.length() ==0) {
          return m;
      }
      
      String[] entities = line.split("&"); // are html entities used?  probably not...
      if (entities.length == 0) {
          return null;
      }
      
      for (String entity : entities) {
          if (!entity.contains("=")) {
              return null;
          }
          
          String[] pair = entity.split("=");
          if (pair.length > 2 || pair[0].length() == 0) {
              return null;
          }
          m.put(pair[0], pair.length == 2 ? pair[1] : "");
      }
      
      return m;
  }
  
  static HeaderField parseHeaderField(String paramLine) {
      HeaderField param = new HeaderField(HeaderFieldName.UNSUPPORTED, "");
      
      if (paramLine == null || paramLine.compareTo("") == 0) {
          return param;
      }
      
      String[] paramLineArr = paramLine.split(":");
      if (paramLineArr.length != 2) {
          return param;
      }
      
      String paramLineKey = paramLineArr[0];
      String paramLineVal = paramLineArr[1].trim();
      
      for (HeaderFieldName key : HeaderFieldName.values()) {
          if (paramLineKey.compareToIgnoreCase(key.toString()) == 0) {
              param = new HeaderField(key, paramLineVal);
              break;
          }
      }
      
      return param;
  }
  
  public static Request parseRequest(RequestReader reader) {
    Request r = new Request();
    ParserState state = ParserState.START_LINE;
    
    while (state != ParserState.END) {
      r = state.parse(reader, r);
      if (r.isValid()) {
        state = state.getNext(r);
      } else {
        break;
      }
    }
    
    return r;
  }
}
