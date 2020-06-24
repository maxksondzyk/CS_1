//package com.ksondzyk.HTTP;
//
//import com.sun.net.httpserver.HttpExchange;
//import com.sun.net.httpserver.HttpHandler;
//
//import java.io.IOException;
//
//public class Router implements HttpHandler {
//
//    @Override
//    public void handle(HttpExchange httpExchange) throws IOException {
//        switch (httpExchange.getRequestMethod()) {
//            case "GET":
//                if (httpExchange.getRequestURI().getPath().contains(".")) {
//                    login(httpExchange);
//                }
//                else if((httpExchange.getRequestURI().getPath().contains("hello"))){
//                    hello(httpExchange);
//                }
//                else {
//                    get(httpExchange);
//                }
//                break;
//            case "PUT":
//                put(httpExchange);
//                break;
//            case "POST":
//
//                if((httpExchange.getRequestURI().getPath().contains("hello"))){
//                    helloLogin(httpExchange);
//                }else
//                    post(httpExchange);
//                break;
//            case "DELETE":
//                delete(httpExchange);
//                break;
//        }
//    }
//}
