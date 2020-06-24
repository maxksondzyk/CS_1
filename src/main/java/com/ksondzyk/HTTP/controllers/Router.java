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

        }
    //send html page
    else if(1==1){

    }

    // api json
    else{

    }

    }
}
