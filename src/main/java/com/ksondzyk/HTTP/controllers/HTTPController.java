package com.ksondzyk.HTTP.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksondzyk.HTTP.dto.Response;
import com.ksondzyk.HTTP.views.View;
import com.sun.net.httpserver.HttpExchange;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HTTPController {
    private static View view;
    public static void setView(View newView) {
        view = newView;
    }
    static Random random;

    private static String authToken;

    public static String generateToken() {
        long longToken = Math.abs( random.nextInt() );
        String random = Long.toString( longToken, 8 );
        return random;
    }
    public static void handler(HttpExchange httpExchange){

        if(httpExchange.getRequestMethod().equals("GET")) {
            if (httpExchange.getRequestURI().getPath().contains("login")) {
                login(httpExchange);
            } else {
                get(httpExchange);
            }
        } else if(httpExchange.getRequestMethod().equals("PUT")){
            put(httpExchange);
        } else if(httpExchange.getRequestMethod().equals("POST")){
            post(httpExchange);
        } else if(httpExchange.getRequestMethod().equals("DELETE")){
            delete(httpExchange);
        }
    }
    public static void login(HttpExchange httpExchange) {
        Response response = new Response();

        //response.setTemplate("datetime");
        Map<String, String> params = queryToMap(httpExchange.getRequestURI().getQuery());
        if (params.get("login").equals("me")&&params.get("password").equals("pass")) {
            response.setStatusCode(200);
           // authToken = generateToken();
            response.setData("authToken");
        }
        else{
            response.setStatusCode(401);
            response.setData("Access denied");
        }
            response.setHttpExchange(httpExchange);

        view.view(response);
    }

    public static void get(HttpExchange httpExchange) {
        Response response = new Response();
        String tok = httpExchange.getRequestHeaders().get("token").toString().replaceAll("\"","").replaceAll("\\[","").replaceAll("]","");
        if(tok.equals("authToken")){
            response.setStatusCode(200);
            //if(httpExchange.getRequestURI().getPath().("(^api\/product\/(\d)+$)"))
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name","apples");
            jsonObject.put("id","1");
            int id = Integer.parseInt(httpExchange.getRequestURI().getPath().split("/")[3]);
            response.setData(jsonObject.toMap());
        }
        else{
            response.setStatusCode(403);
        }
        response.setHttpExchange(httpExchange);

        view.view(response);
    }
    public static void put(HttpExchange httpExchange){
        Response response = new Response();

        String tok = httpExchange.getRequestHeaders().get("token").toString().replaceAll("\"","").replaceAll("\\[","").replaceAll("]","");
        if(tok.equals("authToken")){
            InputStream is = httpExchange.getRequestBody();
            ObjectMapper mapper = new ObjectMapper();
            try {
                Map<String, String> jsonMap = mapper.readValue(is,Map.class);
                System.out.println(jsonMap.get("name"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            response.setStatusCode(201);
            //if(httpExchange.getRequestURI().getPath().("(^api\/product\/(\d)+$)"))
            response.setData("good is created");
        }
        else {

            response.setStatusCode(403);
        }
        response.setHttpExchange(httpExchange);

        view.view(response);
    }

    public static void post(HttpExchange httpExchange){
        Response response = new Response();

        String tok = httpExchange.getRequestHeaders().get("token").toString().replaceAll("\"","").replaceAll("\\[","").replaceAll("]","");
        if(tok.equals("authToken")){
            InputStream is = httpExchange.getRequestBody();
            ObjectMapper mapper = new ObjectMapper();
            try {
                Map<String, String> jsonMap = mapper.readValue(is,Map.class);
                System.out.println(jsonMap.get("name"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            response.setStatusCode(201);
            //if(httpExchange.getRequestURI().getPath().("(^api\/product\/(\d)+$)"))
            response.setData("good was changed");
        }
        else {

            response.setStatusCode(403);
        }
        response.setHttpExchange(httpExchange);

        view.view(response);
    }
    public static void delete(HttpExchange httpExchange){
        Response response = new Response();

        String tok = httpExchange.getRequestHeaders().get("token").toString().replaceAll("\"","").replaceAll("\\[","").replaceAll("]","");
        if(tok.equals("authToken")){
            response.setStatusCode(200);
            //int id = Integer.parseInt(httpExchange.getRequestURI().getPath().split("/")[3]);
            //if(httpExchange.getRequestURI().getPath().("(^api\/product\/(\d)+$)"))
            response.setData("good was deleted");
        }
        else {
            response.setStatusCode(403);
        }
        response.setHttpExchange(httpExchange);

        view.view(response);
    }

    public static Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            } else {
                result.put(entry[0], "");
            }
        }
        return result;
    }
}