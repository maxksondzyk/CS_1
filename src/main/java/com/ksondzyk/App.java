package com.ksondzyk;

import com.ksondzyk.DataBase.DB;
import com.ksondzyk.HTTP.controllers.HTTPController;
import com.ksondzyk.HTTP.dao.Table;
import com.ksondzyk.HTTP.views.JsonView;
import com.ksondzyk.HTTP.views.View;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.InetSocketAddress;

@SpringBootApplication
public class App {
    final static int HTTP_SERVER_PORT = 8888;

    final static View VIEW = new JsonView();
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
        try {
            DB.connect();
            Table.createTable();
           // Table.createCategoriesTable();
            Table.createUsersTable();
            Table.insertUser("admin","pass");
            HTTPController.setView(VIEW);

            com.sun.net.httpserver.HttpServer server = com.sun.net.httpserver.HttpServer.create();

            server.bind(new InetSocketAddress(HTTP_SERVER_PORT), 0);

            HTTPController httpController = new HTTPController();
            server.createContext("/",httpController); // http://localhost:8888/

            //server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }

}
