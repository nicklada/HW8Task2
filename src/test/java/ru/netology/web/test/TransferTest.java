package ru.netology.web.test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class TransferTest {

    @Test
    public void testReq() throws Exception {
        String payload = "data={" +
                "\"login\": \"vasya\", " +
                "\"password\": \"qwerty123\", " +
                "}";
        StringEntity entity = new StringEntity(payload);

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost("http://localhost:9999/api/auth");
        request.setEntity(entity);

        HttpResponse response = httpClient.execute(request);
        System.out.println(response.getStatusLine().getStatusCode());
        InputStream inputStream = response.getEntity().getContent();
        String json = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        System.out.println(json);
    }

    @Test
    public void postRequestExampleTest() {
        JSONObject requestBody = new JSONObject();
        requestBody.put("login", "vasya");
        requestBody.put("password", "qwerty123");

        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.body(requestBody.toString());
        Response response = request.post("http://localhost:9999/api/auth");
        Assertions.assertEquals(200, response.getStatusCode());
        String status = response.then().extract().path("status");
        Assert.assertEquals(status, "ok");
        System.out.println(response.getBody().asString());
    }

}


