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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

import static io.restassured.RestAssured.given;

public class TransferTest {

    public String postRequest() throws SQLException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("login", "vasya");
        requestBody.put("password", "qwerty123");

        RequestSpecification request = given();
        request.header("Content-Type", "application/json");
        request.body(requestBody.toString());
        Response response = request.post("http://localhost:9999/api/auth");
        Assertions.assertEquals(200, response.getStatusCode());
        String verificationCode = DataHelper.getVerificationCodeForVasya();
        return verificationCode;
    }

    @Test
    public void testRequest() throws Exception {
        String verificationCode = postRequest();
        JSONObject requestBody = new JSONObject();
        requestBody.put("login", "vasya");
        requestBody.put("code", verificationCode);

        RequestSpecification request = given();
        request.header("Content-Type", "application/json");
        request.body(requestBody.toString());
        Response response = request.post("http://localhost:9999/api/auth/verification");

        Assertions.assertEquals(200, response.getStatusCode());
        String status = response.then().extract().path("status");
        Assert.assertEquals(status, "ok");
        System.out.println(response.getBody().asString());
    }

}


