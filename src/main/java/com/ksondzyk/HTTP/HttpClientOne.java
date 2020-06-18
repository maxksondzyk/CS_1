package com.ksondzyk.HTTP;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpClientOne {
    private static String authToken;
    // one instance, reuse
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    public static void main(String[] args) throws Exception {

        HttpClientOne obj = new HttpClientOne();

        try {
            System.out.println("Testing 1 - Send Http GET login request");
            obj.sendGetLogin();

            System.out.println("Testing 2 - Send Http GET request");
            obj.sendGet();

            obj.sendPut();
        } finally {
            obj.close();
        }
    }

    private void close() throws IOException {
        httpClient.close();
    }

    private void sendGetLogin() throws Exception {
        URIBuilder builder = new URIBuilder("http://localhost:8888/login");
        builder.setParameter("login", "me").setParameter("password", "pass");

        HttpGet request = new HttpGet(builder.build());

        // add request headers
       // request.addHeader("custom-key", "mkyong");

        try (CloseableHttpResponse response = httpClient.execute(request)) {

            // Get HttpResponse Status
            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();
            System.out.println(headers);

            if (entity != null) {
                // return it as a String
                String result = EntityUtils.toString(entity);
                authToken = result;
                System.out.println(result);
            }

        }

    }

    private void sendGet() throws Exception {

        URIBuilder builder = new URIBuilder("http://localhost:8888/api/good/1");
        builder.setParameter("login", "me").setParameter("password", "pass");

        HttpGet request = new HttpGet(builder.build());
        //add request headers
        request.addHeader("token", "authToken");

        try (CloseableHttpResponse response = httpClient.execute(request)) {

            // Get HttpResponse Status
            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();
            System.out.println(headers+"get");


            // return it as a String
            String result = EntityUtils.toString(entity);
            System.out.println(result);

        }

    }

    private void sendPut() throws Exception {

        URIBuilder builder = new URIBuilder("http://localhost:8888/api/good");
        builder.setParameter("login", "me").setParameter("password", "pass");

        HttpPut request = new HttpPut(builder.build());
        //add request headers
        request.addHeader("token", "authToken");

        try (CloseableHttpResponse response = httpClient.execute(request)) {

            // Get HttpResponse Status
            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();
            System.out.println(headers);


            // return it as a String
            String result = EntityUtils.toString(entity);
            System.out.println(result);

        }

    }

    private void sendPost() throws Exception {

        HttpPost post = new HttpPost("https://jsonplaceholder.typicode.com/posts");
        post.addHeader("accept","application/json");
        // add request parameter, form parameters
        List<NameValuePair> urlParameters = new ArrayList<>();
        //urlParameters.add(new BasicNameValuePair("userId", "1"));
//        urlParameters.add(new BasicNameValuePair("password", "123"));
//        urlParameters.add(new BasicNameValuePair("custom", "secret"));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {

            System.out.println(EntityUtils.toString(response.getEntity()));
        }
    }

}
