package vn.com.tpf.microservices.services.Automation;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.utilities.Constant;

@Getter
public class LoginPageNeo {
	@FindBy(how = How.ID, using = "username")
	@CacheLookup
	private WebElement userNameElement;

	@FindBy(how = How.ID, using = "password-entry")
	@CacheLookup
	private WebElement passwordElement;

    @FindBy(how = How.ID, using = "selectedModule")
    @CacheLookup
    private WebElement selectModule;

	@FindBy(how = How.ID, using = "loginbutton")
	@CacheLookup
	private WebElement btnElement;



	public LoginPageNeo(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void setLoginValue(String userName, String password, String module) throws InterruptedException {
        this.setUserName(userName);
        this.setPassword(password);
        Thread.sleep(Constant.WAIT_ACCOUNT_GET_NULL);
        this.setModule(module);
    }
    
    public void setUserName(String userName) {
        userNameElement.sendKeys(userName);
    }

    public void setPassword(String password) {
        passwordElement.sendKeys(password);
    }

    public void setModule(String module) {
        selectModule.sendKeys(module);
    }

    public void clickLogin() {
        btnElement.click();
    }
    
    public void setValue() {
    	
    }

}
