package vn.com.tpf.microservices.services.Automation;

import org.awaitility.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.models.Automation.LoginDTO;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.*;
import static org.hamcrest.Matchers.is;

public class LoginV2Page {
    private WebDriver _driver;

    @FindBy(how = How.ID, using = "username_show")
//    @CacheLookup
    private WebElement userNameElement;

    @FindBy(how = How.ID, using = "password_show")
//    @CacheLookup
    private WebElement passwordElement;

    @FindBy(how = How.ID, using = "loginbutton")
//    @CacheLookup
    private WebElement loginbuttonElement;

    public LoginV2Page(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    public void loginValue(LoginDTO accountDTO) {

        with().pollInterval(Duration.FIVE_SECONDS).
        await("Textbox Username Visibale Timeout!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS).until(() -> userNameElement.isDisplayed());

        userNameElement.sendKeys(accountDTO.getUserName());

        await("Textbox Password Visibale Timeout!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS).until(() -> passwordElement.isDisplayed());

        passwordElement.sendKeys(accountDTO.getPassword());

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Utilities.captureScreenShot(_driver);

        loginbuttonElement.click();

        Utilities.captureScreenShot(_driver);

        with().pollInterval(Duration.TEN_SECONDS).
        await("Login timeout!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS).until(_driver::getTitle, is("DashBoard"));

    }


}
