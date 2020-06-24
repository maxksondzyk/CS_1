package com.ksondzyk.HTTP;

import com.ksondzyk.HTTP.controllers.ApiController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class Router implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
                if (httpExchange.getRequestHeaders().get("Content-Type").contains("api")) {
                    ApiController apiController = new ApiController();
                    apiController.handle(httpExchange);
                }

                if (httpExchange.getRequestHeaders().get("Content-Type").contains("mime")) {
                    ApiController apiController = new ApiController();
                    apiController.handle(httpExchange);
                }
        }
    }
}