package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {

    private SelenideElement codeField = $("[data-test-id=code] input");
    private SelenideElement verifyButton = $("[data-test-id=action-verify]");

    public VerificationPage() {
        codeField.shouldBe(Condition.visible, Duration.ofSeconds(10));



    }

    public  DashboardPage validVerify(DataHelper.VerificationCode verificationCode) {
        codeField.shouldBe(Condition.visible, Duration.ofSeconds(10));
        codeField.setValue(verificationCode.getCode());
        verifyButton.shouldBe(Condition.enabled, Duration.ofSeconds(10)).click();
        return new DashboardPage();
    }
}