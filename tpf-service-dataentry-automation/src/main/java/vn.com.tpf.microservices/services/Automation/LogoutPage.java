package vn.com.tpf.microservices.services.Automation;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.utilities.Constant;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Getter
public class LogoutPage {
    private WebDriver _driver;

	@FindBy(how = How.ID, using = "loggedInUserPhoto")
	@CacheLookup
	private WebElement userPhotoElement;

    @FindBy(how = How.ID, using = "logout")
    @CacheLookup
    private WebElement logoutLinkElement;

	public LogoutPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }
	
    public void logout() {
        await("userPhotoElement not visiable!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> userPhotoElement.isDisplayed());
        userPhotoElement.click();
        await("logoutLinkElement not visiable!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> logoutLinkElement.isDisplayed());
        logoutLinkElement.click();

        try {
            _driver.switchTo().alert().accept();
        } catch (Exception ex) {
            System.out.println("No alert");
        }
    }
}
