package vn.com.tpf.microservices.services.Automation;

import net.bytebuddy.asm.Advice;
import org.awaitility.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.remote.server.handler.FindElements;
import org.openqa.selenium.support.*;
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

        Actions actions = new Actions(_driver);

        with().pollInterval(Duration.FIVE_SECONDS).
        await("Textbox Username Visibale Timeout!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS).until(() -> userNameElement.isDisplayed());

        userNameElement.sendKeys(accountDTO.getUserName());

        await("Textbox Password Visibale Timeout!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS).until(() -> passwordElement.isDisplayed());

        passwordElement.sendKeys(accountDTO.getPassword());

        Utilities.captureScreenShot(_driver);

        with().pollInterval(Duration.FIVE_SECONDS).
        await("Button login cannot click Timeout!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS).until(() -> loginbuttonElement.isDisplayed());

        String onclickValue = _driver.findElement(By.xpath("//button[@id = 'loginbutton']")).getAttribute("onclick");

        JavascriptExecutor js= (JavascriptExecutor)_driver;
        js.executeScript(onclickValue);

        Utilities.captureScreenShot(_driver);

        try{

            with().pollInterval(Duration.FIVE_SECONDS).
            await("Login timeout!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS).until(_driver::getTitle, is("DashBoard"));

        }catch(Exception e){

            WebElement buttonLoginElement = _driver.findElement(By.xpath("//button[@id = 'loginbutton']"));

            actions.moveToElement(buttonLoginElement).click().build().perform();

            with().pollInterval(Duration.FIVE_SECONDS).
            await("Login timeout!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS).until(_driver::getTitle, is("DashBoard"));

        }

    }


}
