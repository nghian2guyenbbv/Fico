package vn.com.tpf.microservices.services.Automation;

import org.awaitility.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.with;
import static org.hamcrest.Matchers.is;

public class LogoutPageV2 {
    private WebDriver _driver;

    @FindBy(how = How.ID, using = "loggedInUserPhoto")
    @CacheLookup
    private WebElement userPhotoElement;

    @FindBy(how = How.ID, using = "logout")
    @CacheLookup
    private WebElement logoutLinkElement;

    public LogoutPageV2(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    public void logout() {
        Utilities.captureScreenShot(_driver);

        with().pollInterval(Duration.FIVE_SECONDS).
        await("userPhotoElement not visiable!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> userPhotoElement.isDisplayed());

        await("userPhotoElement not visiable!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> userPhotoElement.isEnabled());

        userPhotoElement.click();

        with().pollInterval(Duration.FIVE_SECONDS).
        await("logoutLinkElement not visiable!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> logoutLinkElement.isDisplayed());

        await("logoutLinkElement not visiable!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> logoutLinkElement.isEnabled());

        logoutLinkElement.click();

        with().pollInterval(Duration.FIVE_SECONDS).
        await("Logging out failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(_driver::getTitle, is("Logging out"));


        Utilities.captureScreenShot(_driver);

        try {
            _driver.switchTo().alert().accept();
        } catch (Exception ex) {
            System.out.println("No alert");
        }
    }
}
