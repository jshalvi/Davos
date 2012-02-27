package com.shalvi.davos.handler;

import com.shalvi.davos.http.Request;
import com.shalvi.davos.http.Response;

public interface RequestHandler {
  public Response execute(Request request);
}
