package ru.netology.web.test;

import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;

import java.sql.DriverManager;
import java.sql.SQLException;

public class TransferTest {

    public TransferTest() throws Exception {
    }

    @AfterAll
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

    @Test
    public void shouldTransfer() throws Exception {
        int amount = 1000;
        int initialMoney = DataHelper.getCurrentBalance();
        DataHelper.transferMoney(amount);
        int expectedMoney = initialMoney + amount;
        int actualMoney = DataHelper.getCurrentBalance();
        Assertions.assertEquals(expectedMoney,actualMoney);
    }

    @Test
    public void shouldNotTransferWhenAmmountIsZero() throws Exception {
        int amount = 0;
        int initialMoney = DataHelper.getCurrentBalance();
        DataHelper.transferMoney(amount);
        int expectedMoney = initialMoney;
        int actualMoney = DataHelper.getCurrentBalance();
        Assertions.assertEquals(expectedMoney,actualMoney);
    }

    @Test
    public void shouldTransferAllMoney() throws Exception {
        int amount = DataHelper.getCurrentBalanceDonor();
        int initialMoney = DataHelper.getCurrentBalance();
        DataHelper.transferMoney(amount);
        int expectedMoney = initialMoney + amount;
        int actualMoney = DataHelper.getCurrentBalance();
        Assertions.assertEquals(expectedMoney,actualMoney);
    }

    @Test
    public void shouldNotTransferWhenNotEnoughMoney() throws Exception {
        int amount = DataHelper.getCurrentBalanceDonor()+1;
        int initialMoney = DataHelper.getCurrentBalance();
        DataHelper.transferMoney(amount);
        int expectedMoney = initialMoney;
        int actualMoney = DataHelper.getCurrentBalance();
        Assertions.assertEquals(expectedMoney,actualMoney);
    }
}


