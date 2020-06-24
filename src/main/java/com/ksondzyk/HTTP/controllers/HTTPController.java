package com.ksondzyk.HTTP.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksondzyk.HTTP.dao.Table;
import com.ksondzyk.HTTP.dto.Response;
import com.ksondzyk.HTTP.views.View;
import com.ksondzyk.Processing.Processor;
import com.ksondzyk.entities.Message;
import com.ksondzyk.entities.Packet;
import com.ksondzyk.network.TCP.TCPClientThread;
import com.ksondzyk.utilities.CipherMy;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.SneakyThrows;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;


public class HTTPController implements HttpHandler {
    private static View view;
    public static void setView(View newView) {
        view = newView;
    }

    private static String authToken;

    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe

    public static boolean matching(String orig, String compare){
        String md5;
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(compare.getBytes());
            byte[] digest = md.digest();
            md5 = new BigInteger(1, digest).toString(16);

            return md5.equals(orig);

        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }

    public static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    public static void login(HttpExchange httpExchange) {

        Response response = new Response();

        Map<String, String> params = queryToMap(httpExchange.getRequestURI().getQuery());
        String login = "";
        String password = "";
        try {
             login = Table.selectOneByTitle("admin","Users").getString("title");
             password = Table.selectOneByTitle("admin","Users").getString("password");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (params.get("login").equals(login)&&matching(params.get("password"), password))
        {
            response.setStatusCode(200);

            authToken = generateNewToken();
            response.setData(authToken);
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

        String cleanToken = httpExchange.getRequestHeaders().get("token").toString().replaceAll("\"","").replaceAll("\\[","").replaceAll("]","");

        if(cleanToken.equals(authToken)){

            response.setStatusCode(200);

            String type = (httpExchange.getRequestURI().getPath().split("/")[2]);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("cType","1");
            jsonObject.put("type",type);

            int id = Integer.parseInt(httpExchange.getRequestURI().getPath().split("/")[3]);

            if(Processor.idPresent(id)) {

                jsonObject.put("id", id);

                Packet packet = new Packet((byte) 1,new Message(1,1,jsonObject.toString(),false));
                TCPClientThread tcpClientThread = new TCPClientThread(packet);
                Packet answer = tcpClientThread.send();

                String jsonString = CipherMy.decode(answer.getBMsq().getMessage());

                JSONObject responseMessage = new JSONObject(jsonString);

                response.setData(responseMessage.toMap());

            }
            else{
                response.setStatusCode(404);
            }
        }
        else{
            response.setStatusCode(403);
        }
        response.setHttpExchange(httpExchange);

        view.view(response);
    }

    public static void put(HttpExchange httpExchange){
        Response response = new Response();
        //InputStream cleanToken = httpExchange.getRequestBody();

        String cleanToken = httpExchange.getRequestHeaders().get("token").toString().replaceAll("\"","").replaceAll("\\[","").replaceAll("]","");

        String type = (httpExchange.getRequestURI().getPath().split("/")[2]);

        if(cleanToken.equals(authToken)){

            InputStream is = httpExchange.getRequestBody();

            ObjectMapper mapper = new ObjectMapper();

            try {

                Map<String, String> jsonMap = mapper.readValue(is,Map.class);
                jsonMap.put("cType","2");
                jsonMap.put("type",type);
                    if (type.equals("good")&&(Integer.parseInt(jsonMap.get("quantity")) < 0 || Integer.parseInt(jsonMap.get("price")) < 0))
                        response.setStatusCode(409);

                    else {
                        JSONObject jsonObject = new JSONObject(jsonMap);

                        Packet packet = new Packet((byte) 1,new Message(1,1,jsonObject.toString(),false));
                        TCPClientThread tcpClientThread = new TCPClientThread(packet);
                        Packet answer = tcpClientThread.send();

                        String jsonString = CipherMy.decode(answer.getBMsq().getMessage());

                        JSONObject responseMessage = new JSONObject(jsonString);

                        response.setData(responseMessage.toMap());

                        response.setStatusCode(201);
                        response.setData("id: " + responseMessage.get("id"));
                    }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            response.setStatusCode(403);
        }
        response.setHttpExchange(httpExchange);

        view.view(response);
    }

    public static void post(HttpExchange httpExchange) throws IOException {
        Response response = new Response();
        StringBuilder sb = new StringBuilder();
        InputStream ios = httpExchange.getRequestBody();
        int i;
        while ((i = ios.read()) != -1) {
            sb.append((char) i);
        }
        System.out.println("hm: " + sb.toString());
        String type = (httpExchange.getRequestURI().getPath().split("/")[1]);
        System.out.println("Type: " + type);
        String cleanToken = httpExchange.getRequestHeaders().get("token").toString().replaceAll("\"","").replaceAll("\\[","").replaceAll("]","");
        System.out.println(cleanToken);
        if(cleanToken.equals(authToken)){
            InputStream is = httpExchange.getRequestBody();

            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> jsonMap = null;

            try {
                jsonMap = mapper.readValue(is, Map.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(type.equals("good")&&(Integer.parseInt(jsonMap.get("quantity"))<0||Integer.parseInt(jsonMap.get("price"))<0))
                response.setStatusCode(409);
            else {
                JSONObject jsonObject = new JSONObject(jsonMap);
                jsonObject.put("cType", "3");
                jsonObject.put("type",type);

                int id = Integer.parseInt(jsonMap.get("id"));
                if (Processor.idPresent(id)) {
                    jsonObject.put("id", String.valueOf(id));
                    Processor.process(jsonObject);
                    try {
                        httpExchange.sendResponseHeaders(204,-1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                } else {
                    response.setStatusCode(404);
                }
            }
        }
        else {
            response.setStatusCode(403);
        }
        response.setHttpExchange(httpExchange);

        view.view(response);

    }

    public static void delete(HttpExchange httpExchange){
        Response response = new Response();

        String cleanToken = httpExchange.getRequestHeaders().get("token").toString().replaceAll("\"","").replaceAll("\\[","").replaceAll("]","");

        if(cleanToken.equals(authToken)){

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("cType","4");
            int id = Integer.parseInt(httpExchange.getRequestURI().getPath().split("/")[3]);
            String type = (httpExchange.getRequestURI().getPath().split("/")[2]);
            jsonObject.put("id",id);
            jsonObject.put("type",type);
            if(!Processor.idPresent(id)) {
                response.setStatusCode(404);
            }
            else {
                Processor.process(jsonObject);
                try {
                    httpExchange.sendResponseHeaders(204,-1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
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


    @SneakyThrows
    public static void hello(HttpExchange httpExchange) throws IOException {
        ResultSet resultSet = Table.selectAll("Users");
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while (resultSet.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) System.out.print(",  ");
                String columnValue = resultSet.getString(i);
                System.out.print(columnValue + " " + rsmd.getColumnName(i));
            }
            System.out.println("");
        }
        Response response = new Response();
        response.setStatusCode(200);
        response.setTemplate("list");
        response.setHttpExchange(httpExchange);
        view.view(response);
    }
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        switch (httpExchange.getRequestMethod()) {
            case "GET":
                if (httpExchange.getRequestURI().getPath().contains("login")) {
                    login(httpExchange);
                }
                else if((httpExchange.getRequestURI().getPath().contains("hello"))){
                    hello(httpExchange);
                }
                else {
                    get(httpExchange);
                }
                break;
            case "PUT":
                put(httpExchange);
                break;
            case "POST":

                if((httpExchange.getRequestURI().getPath().contains("hello"))){
                    //helloLogin(httpExchange);
                }
                else if(httpExchange.getRequestURI().getPath().contains("signup")){
                    signup(httpExchange);
                }
                post(httpExchange);
                break;
            case "DELETE":
                delete(httpExchange);
                break;
        }
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
            authToken = generateNewToken();
            Table.insertUser(params.get("login"), params.get("password"), authToken);
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

    private void helloLogin(HttpExchange httpExchange) {

        Response response = new Response();

        Map<String, String> params = queryToMap(httpExchange.getRequestURI().getQuery());
        String login = "";
        String password = "";
        System.err.println("here");
        try {
            login = Table.selectOneByTitle("admin","Users").getString("title");
            password = Table.selectOneByTitle("admin","Users").getString("password");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        System.err.println(params.get("login"));
       // System.err.println(login.);
        if (params.get("login").equals(login)&&matching(params.get("password"), password))
        {
            response.setStatusCode(200);


            authToken = generateNewToken();
            response.setData(authToken);
        }
        else{

            response.setStatusCode(401);
            response.setData("Access denied");
        }
        response.setTemplate("list");
        response.setHttpExchange(httpExchange);

        view.view(response);
    }
}