package ru.netology.web.data;

import lombok.Value;

public class DataHelper {
    private DataHelper() {}

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

    public static VerificationCode getVerificationCodeFor(AuthInfo authInfo) {
        return new VerificationCode("12345");
    }

    @Value
    public static class TransferInfo {
        private String amount;
        private String card;
    }

    public static TransferInfo getTransferInfo(String amount) {
        return new TransferInfo(amount,"5559000000000002");
    }

    public static TransferInfo getReturnTransferInfo(TransferInfo original) {
        return new TransferInfo(original.amount,"5559000000000001");
    }
}
