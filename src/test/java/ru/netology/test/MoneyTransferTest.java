package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.*;
import static ru.netology.page.DashboardPage.pushFirstCardButton;
import static ru.netology.page.DashboardPage.pushSecondCardButton;

public class MoneyTransferTest {

    @BeforeEach
    public void openPage() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    public void shouldTransferMoneyFromFirstCardToSecondCard() {
        val dashboardPage = new DashboardPage();
        val firstCardBalanceStart = dashboardPage.getFirstCardBalance();
        val secondCardBalanceStart = dashboardPage.getSecondCardBalance();
        int amount = firstCardBalanceStart / 2; // Сумма определяется автоматически

        val transactionPage = pushSecondCardButton();
        transactionPage.transferMoney(amount, getFirstCardNumber());
        val firstCardBalanceResult = firstCardBalanceStart - amount;
        val secondCardBalanceResult = secondCardBalanceStart + amount;

        assertEquals(firstCardBalanceResult, dashboardPage.getFirstCardBalance());
        assertEquals(secondCardBalanceResult, dashboardPage.getSecondCardBalance());
    }

    @Test
    public void shouldTransferMoneyFromSecondCardToFirstCard() {
        val dashboardPage = new DashboardPage();
        val firstCardBalanceStart = dashboardPage.getFirstCardBalance();
        val secondCardBalanceStart = dashboardPage.getSecondCardBalance();
        int amount = secondCardBalanceStart / 3; // Сумма определяется автоматически

        val transactionPage = pushFirstCardButton();
        transactionPage.transferMoney(amount, getSecondCardNumber());
        val firstCardBalanceResult = firstCardBalanceStart + amount;
        val secondCardBalanceResult = secondCardBalanceStart - amount;

        assertEquals(firstCardBalanceResult, dashboardPage.getFirstCardBalance());
        assertEquals(secondCardBalanceResult, dashboardPage.getSecondCardBalance());
    }

    @Test
    public void shouldNotTransferMoneyIfAmountExceedsCardBalance() {
        val dashboardPage = new DashboardPage();
        val firstCardBalanceStart = dashboardPage.getFirstCardBalance();
        int amount = firstCardBalanceStart + 10_000; // Сумма превышает остаток

        val transactionPage = pushSecondCardButton();
        transactionPage.transferMoney(amount, getFirstCardNumber());
        transactionPage.getErrorLimit();
    }

    @Test
    public void shouldBeGetErrorIfSameCard() {
        val dashboardPage = new DashboardPage();
        val firstCardBalanceStart = dashboardPage.getFirstCardBalance();
        int amount = firstCardBalanceStart / 4; // Сумма определяется автоматически

        val transactionPage = pushFirstCardButton();
        transactionPage.transferMoney(amount, getFirstCardNumber());
        transactionPage.getErrorInvalidCard();
    }
}
