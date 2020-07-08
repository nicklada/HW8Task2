package ru.netology.web.test;

import org.junit.jupiter.api.*;
import ru.netology.web.data.DataHelper;

import java.sql.SQLException;

import static ru.netology.web.data.DataHelper.tokenRequest;

public class TransferTest {

    private static String Token;

    @AfterAll
    public static void cleanData() throws SQLException {
        DataHelper.cleanData();
    }

    @BeforeAll
    public static void setUp() throws Exception {
        Token = tokenRequest();
    }

    @Test
    public void shouldTransfer() throws Exception {
        int amount = 1000;
        int initialMoney = DataHelper.getCurrentBalance();
        DataHelper.transferMoney(amount,Token);
        int expectedMoney = initialMoney + amount;
        int actualMoney = DataHelper.getCurrentBalance();
        Assertions.assertEquals(expectedMoney, actualMoney);
    }

    @Test
    public void shouldNotTransferWhenAmmountIsZero() throws Exception {
        int amount = 0;
        int initialMoney = DataHelper.getCurrentBalance();
        DataHelper.transferMoney(amount,Token);
        int expectedMoney = initialMoney;
        int actualMoney = DataHelper.getCurrentBalance();
        Assertions.assertEquals(expectedMoney, actualMoney);
    }

    @Test
    public void shouldTransferAllMoney() throws Exception {
        int amount = DataHelper.getCurrentBalanceDonor();
        int initialMoney = DataHelper.getCurrentBalance();
        DataHelper.transferMoney(amount,Token);
        int expectedMoney = initialMoney + amount;
        int actualMoney = DataHelper.getCurrentBalance();
        Assertions.assertEquals(expectedMoney, actualMoney);
    }

    @Test
    public void shouldNotTransferWhenNotEnoughMoney() throws Exception {
        int amount = DataHelper.getCurrentBalanceDonor() + 1;
        int initialMoney = DataHelper.getCurrentBalance();
        DataHelper.transferMoney(amount,Token);
        int expectedMoney = initialMoney;
        int actualMoney = DataHelper.getCurrentBalance();
        Assertions.assertEquals(expectedMoney, actualMoney);
    }
}


