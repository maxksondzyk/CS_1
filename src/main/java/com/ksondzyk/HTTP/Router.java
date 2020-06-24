package com.ksondzyk.HTTP;

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
        //send html page
        else if(1==1){

        }

        // api json
        else{

        }
//                if (httpExchange.getRequestHeaders().get("Content-Type").contains("application/json")) {
//                    ApiController apiController = new ApiController();
//                    apiController.handle(httpExchange);
//                }else
//                if (httpExchange.getRequestHeaders().get("Content-Type").contains("mime")) {
//                    StaticController staticController = new StaticController();
//                    staticController.handle(httpExchange);
//                }
//                else{
//                    ApiController apiController = new ApiController();
//                    apiController.handle(httpExchange);
//                }
        }
    }