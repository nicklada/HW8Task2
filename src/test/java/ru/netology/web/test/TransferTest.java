package ru.netology.web.test;

import org.junit.jupiter.api.*;
import ru.netology.web.data.DataHelper;

import java.sql.SQLException;

import static ru.netology.web.data.DataHelper.tokenRequest;

public class TransferTest {

    private static String Token;

    @BeforeAll
    public static void setUp() throws Exception {
        String login = DataHelper.getAuthInfo().getLogin();
        String password = DataHelper.getAuthInfo().getPassword();
        Token = tokenRequest(login, password);
    }

    @AfterAll
    public static void cleanData() throws SQLException {
        DataHelper.cleanData();
    }

    @Test
    public void shouldTransfer() throws Exception {
        int amount = 1000;
        String from = DataHelper.getDonorCardNumber().getNumber();
        String to = DataHelper.getReceiverCardNumber().getNumber();
        int initialMoney = DataHelper.getCurrentBalance();
        DataHelper.transferMoney(amount, Token, from, to);
        int expectedMoney = initialMoney + amount;
        int actualMoney = DataHelper.getCurrentBalance();
        Assertions.assertEquals(expectedMoney, actualMoney);
    }

    @Test
    public void shouldNotTransferWhenAmmountIsZero() throws Exception {
        int amount = 0;
        String from = DataHelper.getDonorCardNumber().getNumber();
        String to = DataHelper.getReceiverCardNumber().getNumber();
        int initialMoney = DataHelper.getCurrentBalance();
        DataHelper.transferMoney(amount, Token, from, to);
        int expectedMoney = initialMoney;
        int actualMoney = DataHelper.getCurrentBalance();
        Assertions.assertEquals(expectedMoney, actualMoney);
    }

    @Test
    public void shouldTransferAllMoney() throws Exception {
        int amount = DataHelper.getCurrentBalanceDonor();
        String from = DataHelper.getDonorCardNumber().getNumber();
        String to = DataHelper.getReceiverCardNumber().getNumber();
        int initialMoney = DataHelper.getCurrentBalance();
        DataHelper.transferMoney(amount, Token, from, to);
        int expectedMoney = initialMoney + amount;
        int actualMoney = DataHelper.getCurrentBalance();
        Assertions.assertEquals(expectedMoney, actualMoney);
    }

    @Test
    public void shouldNotTransferWhenNotEnoughMoney() throws Exception {
        int amount = DataHelper.getCurrentBalanceDonor() + 1;
        String from = DataHelper.getDonorCardNumber().getNumber();
        String to = DataHelper.getReceiverCardNumber().getNumber();
        int initialMoney = DataHelper.getCurrentBalance();
        DataHelper.transferMoney(amount, Token, from, to);
        int expectedMoney = initialMoney;
        int actualMoney = DataHelper.getCurrentBalance();
        Assertions.assertEquals(expectedMoney, actualMoney);
    }
}


