package com.ksondzyk.HTTP.views;

import com.ksondzyk.HTTP.dto.Response;
import com.sun.net.httpserver.HttpExchange;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class HtmlView implements View {
    @Override
    public void view(Response response) {
        String responseBody = "<b>error</b>";
        Integer statusCode = 500;

        HttpExchange httpExchange = response.getHttpExchange();
        OutputStream outputStream = httpExchange.getResponseBody();

        try {
            Object data = response.getData();
            response.setTemplate("list");
            String templateName = response.getTemplate();

            Configuration configuration = getConfiguration();
            Template template = configuration.getTemplate(templateName + ".flth");

            Map root = new HashMap();
            root.put("data", data);

            StringWriter stringWriter = new StringWriter();
            template.process(root, stringWriter);

            responseBody = stringWriter.toString();
            statusCode = response.getStatusCode();
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        try {
            httpExchange.sendResponseHeaders(statusCode, responseBody.length());

            outputStream.write(responseBody.getBytes());
            outputStream.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private Configuration getConfiguration() {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);

        configuration.setClassForTemplateLoading(this.getClass(), "/templates/");

        configuration.setDefaultEncoding("UTF-8");

        return configuration;
    }
}
