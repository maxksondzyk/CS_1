package com.ksondzyk.controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class Router implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();

        //send static file binary
        if(path.endsWith(".js") || path.endsWith(".css") || path.endsWith(".png")){
            StaticController staticController = new StaticController();
            staticController.handle(httpExchange);
        }

        // api json
        else if(path.startsWith("api/")){
            ApiController apiController = new ApiController();
            apiController.handle(httpExchange);
        }
        else{
            ApiController apiController = new ApiController();
            apiController.handle(httpExchange);
        }
        }
    }