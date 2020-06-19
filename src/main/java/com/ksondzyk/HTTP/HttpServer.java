package com.ksondzyk.HTTP;

import com.ksondzyk.DataBase.DB;
import com.ksondzyk.HTTP.dao.Table;
import com.ksondzyk.HTTP.controllers.HTTPController;
import com.ksondzyk.HTTP.views.JsonView;
import com.ksondzyk.HTTP.views.View;
import com.sun.net.httpserver.HttpContext;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpServer {

    final static int HTTP_SERVER_PORT = 8888;

    final static View VIEW = new JsonView();

    public static void main(String[] args) {
        try {
            DB.connect();
            Table.createTable();
            HTTPController.setView(VIEW);

            com.sun.net.httpserver.HttpServer server = com.sun.net.httpserver.HttpServer.create();

            server.bind(new InetSocketAddress(HTTP_SERVER_PORT), 0);

            HttpContext context = server.createContext("/"); // http://localhost:8888/
            context.setHandler(HTTPController::handler);
            server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}