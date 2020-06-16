package com.ksondzyk.HTTP.controllers;

import com.sun.net.httpserver.HttpExchange;
import com.ksondzyk.HTTP.dto.Response;
import com.ksondzyk.HTTP.models.DateTime;
import com.ksondzyk.HTTP.services.DataTimeService;
import com.ksondzyk.HTTP.views.View;

public class ExampleHelloController {
    private static View view;

    public static void setView(View newView) {
        view = newView;
    }

    public static void hello(HttpExchange httpExchange) {
        DateTime dateTime = DataTimeService.getCurrentDateTimeMinusThreeHours();

        Response response = new Response();

        response.setTemplate("datetime");
        response.setStatusCode(200);
        response.setData(dateTime);
        response.setHttpExchange(httpExchange);

        view.view(response);
    }
}
