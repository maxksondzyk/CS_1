package com.ksondzyk.HTTP;

import com.ksondzyk.HTTP.controllers.ApiController;
import com.ksondzyk.HTTP.controllers.Router;
import com.ksondzyk.HTTP.views.HtmlView;
import com.ksondzyk.HTTP.views.View;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpServer {

    final static int HTTP_SERVER_PORT = 8080;

    final static View VIEW = new HtmlView();

    public static void main(String[] args) {
        try {
           // DB.connect();
//            Table.createTable();
//            Table.createUsersTable();
//            Table.insertUser("admin","pass", "123");
            ApiController.setView(VIEW);

            com.sun.net.httpserver.HttpServer server = com.sun.net.httpserver.HttpServer.create();

            server.bind(new InetSocketAddress(HTTP_SERVER_PORT), 0);

            Router router = new Router();
            ApiController apiController = new ApiController();
            server.createContext("/", router); // http://localhost:8888/

            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}