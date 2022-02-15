package vn.com.tpf.microservices.services.Automation.autoField;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

@Getter
public class FV_CheckLoginUserConfirmation {

    private WebDriver _driver;

    @FindBy(how = How.XPATH, using = "//div[@id = 'userConfirmation']")
    private WebElement popupSignUserConfirm;

    @FindBy(how = How.XPATH, using = "//div[@id = 'userConfirmation']//button[contains(text(),'Sign In')]")
    private WebElement btnSignUserConfirm;

    public FV_CheckLoginUserConfirmation(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }
}
