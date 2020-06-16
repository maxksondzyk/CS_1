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

            HttpContext context = server.createContext("/login"); // http://localhost:8888/hello
            context.setHandler(ExampleHelloController::login);
            HttpContext contextGet = server.createContext("/api/good"); // http://localhost:8888/hello
            contextGet.setHandler(ExampleHelloController::get);

            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}