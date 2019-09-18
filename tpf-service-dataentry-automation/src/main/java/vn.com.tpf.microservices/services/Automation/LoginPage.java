package vn.com.tpf.microservices.services.Automation;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

@Getter
public class LoginPage {
	@FindBy(how = How.ID, using = "username_show")
	@CacheLookup
	private WebElement userNameElement;
	
	@FindBy(how = How.ID, using = "password_show")
	@CacheLookup
	private WebElement passwordElement;
	
	@FindBy(how = How.ID, using = "loginbutton")
	@CacheLookup
	private WebElement btnElement;
	
	public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void setLoginValue(String userName, String password) {
        this.setUserName(userName);
        this.setPassword(password);
    }
    
    public void setUserName(String userName) {
        userNameElement.sendKeys(userName);
    }

    public void setPassword(String password) {
        passwordElement.sendKeys(password);
    }

    public void clickLogin() {
        btnElement.click();
    }
    
    public void setValue() {
    	
    }

}
