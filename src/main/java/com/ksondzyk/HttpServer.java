package com.ksondzyk;

import com.ksondzyk.controllers.ApiController;
import com.ksondzyk.controllers.Router;
import com.ksondzyk.views.HtmlView;
import com.ksondzyk.views.View;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpServer {

    final static int HTTP_SERVER_PORT = 8080;

    final static View VIEW = new HtmlView();

    public static void main(String[] args) {
        try {

            ApiController.setView(VIEW);

            com.sun.net.httpserver.HttpServer server = com.sun.net.httpserver.HttpServer.create();

            server.bind(new InetSocketAddress(HTTP_SERVER_PORT), 0);

            Router router = new Router();
            server.createContext("/", router); // http://localhost:8888/

            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}