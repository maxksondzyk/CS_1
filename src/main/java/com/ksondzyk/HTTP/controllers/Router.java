package com.ksondzyk.HTTP.controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class Router implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
    String path = httpExchange.getRequestURI().getPath();

    //send static file binary
    if(path.endsWith(".js") || path.endsWith(".css") || path.endsWith(".png")){
        StaticController controller = new StaticController();
            controller.handle(httpExchange);
        }

    // api json
    else{
        ApiController apiController = new ApiController();
        apiController.handle(httpExchange);
    }

    }
}
