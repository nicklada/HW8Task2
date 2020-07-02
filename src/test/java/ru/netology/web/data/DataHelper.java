package ru.netology.web.data;

import lombok.Value;
import lombok.val;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DataHelper {
    private DataHelper() {
    }

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static AuthInfo getOtherAuthInfo(AuthInfo original) {
        return new AuthInfo("petya", "123qwerty");
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
}
