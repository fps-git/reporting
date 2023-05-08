package ru.netology.carddeliverypatterns.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;

import ru.netology.carddeliverypatterns.data.DataGenerator;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byTagAndText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

class DeliveryTest {


    void clean(String locator) {
        $(locator).sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);

        // ---------------------- Проверка первого планирования встречи ---------------------

        $("[data-test-id='city'] input").setValue(validUser.getCity());
        clean("[data-test-id='date'] input");
        $("[data-test-id='date'] input").setValue(firstMeetingDate);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $(withText("Запланировать")).click();
        $("[data-test-id='success-notification'] .notification__content").should(appear)
                .shouldHave(text("Встреча успешно запланирована на " + firstMeetingDate));
        $("[data-test-id='success-notification'] .notification__closer").click();

        // ---------------------- Проверка перепланирования встречи ---------------------

        clean("[data-test-id='date'] input");
        $("[data-test-id='date'] input").setValue(secondMeetingDate);
        $(withText("Запланировать")).click();
        $("[data-test-id='replan-notification'] .notification__content").should(appear)
                .shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $(byTagAndText("span","Перепланировать")).click();
        $("[data-test-id='success-notification'] .notification__content").should(appear)
                .shouldHave(text("Встреча успешно запланирована на " + secondMeetingDate));
    }
}
