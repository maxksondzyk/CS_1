package com.ksondzyk.HTTP;

import com.ksondzyk.DataBase.DB;
import com.ksondzyk.HTTP.controllers.HTTPController;
import com.ksondzyk.HTTP.dao.Table;
import com.ksondzyk.HTTP.views.HtmlView;
import com.ksondzyk.HTTP.views.JsonView;
import com.ksondzyk.HTTP.views.View;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpServer {

    final static int HTTP_SERVER_PORT = 8080;

    final static View VIEW = new HtmlView();

    public static void main(String[] args) {
        try {
            DB.connect();
            Table.createTable();
            Table.createUsersTable();
            Table.insertUser("admin","pass");
            HTTPController.setView(VIEW);

            com.sun.net.httpserver.HttpServer server = com.sun.net.httpserver.HttpServer.create();

            server.bind(new InetSocketAddress(HTTP_SERVER_PORT), 0);

            HTTPController httpController = new HTTPController();
            server.createContext("/",httpController); // http://localhost:8888/

            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}