package com.ksondzyk.HTTP.views;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.ksondzyk.HTTP.dto.Response;

import java.io.IOException;
import java.io.OutputStream;

public class JsonView implements View {
    @Override
    public void view(Response response) {
        String responseBody = "{\"error\": \"response encoding error\"}";
        Integer statusCode = 500;

        try {
            Object data = response.getData();
            ObjectMapper objectMapper = new ObjectMapper();
            responseBody = objectMapper.writeValueAsString(data);

            statusCode = response.getStatusCode();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        HttpExchange httpExchange = response.getHttpExchange();

        try {
            Headers responseHeaders = httpExchange.getResponseHeaders();
            responseHeaders.set("Content-Type", "application/json");

            httpExchange.sendResponseHeaders(statusCode, responseBody.length());

            OutputStream outputStream = httpExchange.getResponseBody();

            outputStream.write(responseBody.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
