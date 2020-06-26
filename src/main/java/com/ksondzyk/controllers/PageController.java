package com.ksondzyk.controllers;


import com.ksondzyk.dto.Response;
import com.ksondzyk.views.HtmlView;
import com.ksondzyk.views.View;
import com.ksondzyk.entities.Message;
import com.ksondzyk.entities.Packet;
import com.ksondzyk.network.TCP.TCPClientThread;
import com.ksondzyk.utilities.CipherMy;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class PageController implements HttpHandler {

    private static String authToken;

    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe


    private static View view=new HtmlView();
    //public static void setView(View newView) {
    //    view = newView;
    //}

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        if((httpExchange.getRequestURI().getPath().contains("hello"))){
            hello(httpExchange);
        }
       else if(httpExchange.getRequestURI().getPath().contains("signup")){
            signup(httpExchange);
        }
        else if(httpExchange.getRequestURI().getPath().contains("signin")){
            signin(httpExchange);
        }
    }



    public static void hello(HttpExchange httpExchange) throws IOException {

        Response response = new Response();
        response.setStatusCode(200);
        response.setTemplate("login");
        response.setHttpExchange(httpExchange);
        //setView(new HtmlView());
        view.view(response);
    }

    private void signup(HttpExchange httpExchange) throws IOException {
        Response response = new Response();
        StringBuilder sb = new StringBuilder();
        InputStream ios = httpExchange.getRequestBody();
        int i;
        while (true) {
            if (!((i = ios.read()) != -1)) break;
            sb.append((char) i);
        }
        Map<String, String> params = queryToMap(sb.toString());
        if (params.containsKey("login") && params.containsKey("password")){

            JSONObject jsonObject = new JSONObject();
            authToken = generateNewToken();
            jsonObject.put("login",params.get("login"));
            jsonObject.put("password",params.get("password"));
            jsonObject.put("token",authToken);
            jsonObject.put("type","user");
            jsonObject.put("cType",2);

            Packet packet = new Packet((byte) 1,new Message(1,1,jsonObject.toString(),false));
            TCPClientThread tcpClientThread = new TCPClientThread(packet);
            tcpClientThread.send();

            System.err.println(params.get("login") + ", " + params.get("password"));
            httpExchange.getResponseHeaders().add("Set-Cookie", "token="+authToken);
        }
        else{

            response.setStatusCode(400);
            response.setData("Bad Request.");
        }
        response.setStatusCode(200);
        response.setTemplate("mainpage");
        response.setHttpExchange(httpExchange);
        view.view(response);
    }
    private void signin(HttpExchange httpExchange) throws IOException {
        Response response = new Response();
        StringBuilder sb = new StringBuilder();
        InputStream ios = httpExchange.getRequestBody();
        int i;
        while (true) {
            if (!((i = ios.read()) != -1)) break;
            sb.append((char) i);
        }
        Map<String, String> params = queryToMap(sb.toString());
        if (params.containsKey("login") && params.containsKey("password")){

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("login",params.get("login"));
            jsonObject.put("password",params.get("password"));
            jsonObject.put("type","user");
            jsonObject.put("cType",1);

            Packet packet = new Packet((byte) 1,new Message(1,1,jsonObject.toString(),false));

            TCPClientThread tcpClientThread = new TCPClientThread(packet);
            Packet packetResponse = tcpClientThread.send();

            String jsonString = CipherMy.decode(packetResponse.getBMsq().getMessage());

            JSONObject responseMessage = new JSONObject(jsonString);

            if(responseMessage.get("status").equals("ok")) {
                httpExchange.getResponseHeaders().add("Set-Cookie", "token=" + authToken);
                response.setStatusCode(200);
                response.setTemplate("mainpage");
            }
            else {
                response.setStatusCode(403);
                response.setTemplate("login");
            }
        }
        else{

            response.setStatusCode(400);
            response.setData("Bad Request.");
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

    public static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }


}
