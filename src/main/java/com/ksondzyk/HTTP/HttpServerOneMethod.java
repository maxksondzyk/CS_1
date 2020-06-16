package com.ksondzyk.HTTP;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import com.ksondzyk.HTTP.controllers.ExampleHelloController;
import com.ksondzyk.HTTP.views.JsonView;
import com.ksondzyk.HTTP.views.View;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpServerOneMethod {

    final static int HTTP_SERVER_PORT = 8888;

    final static View VIEW = new JsonView();

    public static void main(String[] args) {
        try {
            ExampleHelloController.setView(VIEW);

            HttpServer server = HttpServer.create();

            server.bind(new InetSocketAddress(HTTP_SERVER_PORT), 0);

            HttpContext context = server.createContext("/hello"); // http://localhost:8888/hello
            context.setHandler(ExampleHelloController::hello);

            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}