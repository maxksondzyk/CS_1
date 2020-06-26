package com.ksondzyk.HTTP.controllers;

import com.ksondzyk.HTTP.controllers.ApiController;
import com.ksondzyk.HTTP.controllers.StaticController;
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
            //має бути пейдж контролер
          //  PageController pg = new PageController();
           // pg.handle(httpExchange);
            ApiController apiController = new ApiController();
            apiController.handle(httpExchange);
        }
        }
    }