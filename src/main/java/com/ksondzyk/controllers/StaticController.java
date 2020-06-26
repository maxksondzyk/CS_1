package com.ksondzyk.controllers;

import com.ksondzyk.dto.Response;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class StaticController implements HttpHandler {


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String root = "src/main/resources/templates/";
        URI uri = httpExchange.getRequestURI();
        System.out.println("looking for: "+ root + uri.getPath());
        String path = uri.getPath();
        File file = new File(root + path).getCanonicalFile();


        Response response  = new Response();
        response.setTemplate(root+path);
        response.setHttpExchange(httpExchange);

        if (!file.isFile()) {
            // Object does not exist or is not a file: reject with 404 error.
            response.setStatusCode(404) ;
           // view.view(response);
            String response_ = "404 (Not Found)\n";
           httpExchange.sendResponseHeaders(404, response_.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response_.getBytes());
            os.close();

        } else {
            // Object exists and is a file: accept with response code 200.
            String mime = "text/html";
            if(path.substring(path.length()-3).equals(".js")) mime = "application/javascript";
            if(path.substring(path.length()-3).equals("css")) mime = "text/css";

            Headers h = httpExchange.getResponseHeaders();
            h.set("Content-Type", mime);
            httpExchange.sendResponseHeaders(200, 0);

            OutputStream os = httpExchange.getResponseBody();
            FileInputStream fs = new FileInputStream(file);
            final byte[] buffer = new byte[0x10000];
            int count = 0;
            while ((count = fs.read(buffer)) >= 0) {
                os.write(buffer,0,count);
            }
            fs.close();
            os.close();
        }

    }
}
