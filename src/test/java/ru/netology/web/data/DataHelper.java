package ru.netology.web.data;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Value;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Assertions;

import java.sql.DriverManager;
import java.sql.SQLException;

import static io.restassured.RestAssured.given;

public class DataHelper {
    private DataHelper() {
    }

    public static void cleanData() throws SQLException {
        val runner = new QueryRunner();
        val codes = "DELETE FROM auth_codes";
        val cards = "DELETE FROM cards";
        val users = "DELETE FROM users";

        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                )
        ) {
            runner.update(conn, codes);
            runner.update(conn, cards);
            runner.update(conn, users);
        }
    }

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    @Value
    public static class Card {
        private String number;
    }

    public static Card getReceiverCardNumber() {
        return new Card("5559 0000 0000 0001");
    }

    public static Card getDonorCardNumber() {
        return new Card("5559 0000 0000 0002");
    }

    @Value
    public static class VerificationCode {
        private String code;
    }

    public static String getVerificationCodeForVasya() throws SQLException {
        val verificationCode = "SELECT code FROM auth_codes WHERE created = (SELECT MAX(created) FROM auth_codes);";

        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
                val countStmt = conn.createStatement();
        ) {
            try (val rs = countStmt.executeQuery(verificationCode)) {
                if (rs.next()) {
                    // выборка значения по индексу столбца (нумерация с 1)
                    val code = rs.getString("code");
                    // TODO: использовать
                    return code;
                }
            }
        }
        return null;
    }

    public static int getCurrentBalance() throws SQLException {
        val getBalance = "SELECT balance_in_kopecks/100 AS balance_in_kopecks FROM cards WHERE number LIKE '%01';";

        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
                val countStmt = conn.createStatement();
        ) {
            try (val rs = countStmt.executeQuery(getBalance)) {
                if (rs.next()) {
                    // выборка значения по индексу столбца (нумерация с 1)
                    val balance = rs.getInt("balance_in_kopecks");
                    // TODO: использовать
                    return balance;
                }
            }
        }
        return 0;
    }

    public static int getCurrentBalanceDonor() throws SQLException {
        val getBalance = "SELECT balance_in_kopecks/100 AS balance_in_kopecks FROM cards WHERE number LIKE '%02';";

        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
                val countStmt = conn.createStatement();
        ) {
            try (val rs = countStmt.executeQuery(getBalance)) {
                if (rs.next()) {
                    // выборка значения по индексу столбца (нумерация с 1)
                    val balance = rs.getInt("balance_in_kopecks");
                    // TODO: использовать
                    return balance;
                }
            }
        }
        return 0;
    }

    public static String codeRequest(String login, String password) throws SQLException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("login", login);
        requestBody.put("password", password);

        RequestSpecification request = given();
        request.header("Content-Type", "application/json");
        request.body(requestBody.toString());
        Response response = request.post("http://localhost:9999/api/auth");
        Assertions.assertEquals(200, response.getStatusCode());
        String verificationCode = DataHelper.getVerificationCodeForVasya();
        return verificationCode;
    }

    public static String tokenRequest(String login, String password) throws Exception {
        String verificationCode = codeRequest(login, password);
        JSONObject requestBody = new JSONObject();
        requestBody.put("login", login);
        requestBody.put("code", verificationCode);

        RequestSpecification request = given();
        request.header("Content-Type", "application/json");
        request.body(requestBody.toString());
        Response response = request.post("http://localhost:9999/api/auth/verification");

        Assertions.assertEquals(200, response.getStatusCode());
        System.out.print(response.getStatusCode());
        String token = response.then().extract().path("token").toString();
        return token;
    }

    public static void transferMoney(int amount, String token, String from, String to) throws Exception {
        JSONObject requestBody = new JSONObject();
        requestBody.put("from", from);
        requestBody.put("to", to);
        requestBody.put("amount", String.valueOf(amount));

        RequestSpecification request = given();
        request.header("Content-Type", "application/json");
        request.header("Authorization", "Bearer " + token);
        request.body(requestBody.toString());
        Response response = request.post("http://localhost:9999/api/transfer");

        Assertions.assertEquals(200, response.getStatusCode());
        System.out.print(response.getStatusCode());
    }
}
