package com.ksondzyk.HTTP;

import com.sun.net.httpserver.HttpServer;
import com.ksondzyk.HTTP.controllers.ExampleMultiactionController;
import com.ksondzyk.HTTP.views.HtmlView;
import com.ksondzyk.HTTP.views.View;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpServerReflection {

    final static int HTTP_SERVER_PORT = 8888;

    final static View VIEW = new HtmlView();

    public static void main(String[] args) {
        try {
            ExampleMultiactionController.setView(VIEW);

            HttpServer server = HttpServer.create();

            server.bind(new InetSocketAddress(HTTP_SERVER_PORT), 0);

            ExampleMultiactionController exampleMultiactionController = new ExampleMultiactionController();
            server.createContext("/", exampleMultiactionController);

            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
