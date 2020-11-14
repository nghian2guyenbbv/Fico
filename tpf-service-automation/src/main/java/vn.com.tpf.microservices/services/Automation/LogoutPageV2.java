package vn.com.tpf.microservices.services.Automation;

import org.awaitility.Duration;
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
        Actions actions = new Actions(_driver);

        Utilities.captureScreenShot(_driver);

        with().pollInterval(Duration.TEN_SECONDS).
        await("userPhotoElement not visiable!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> userPhotoElement.isDisplayed());

        actions.moveToElement(userPhotoElement).click().build().perform();

        await("logoutLinkElement not visiable!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> logoutLinkElement.isDisplayed());

        logoutLinkElement.click();

        Utilities.captureScreenShot(_driver);

        try {
            _driver.switchTo().alert().accept();
        } catch (Exception ex) {
            System.out.println("No alert");
        }
    }
}
