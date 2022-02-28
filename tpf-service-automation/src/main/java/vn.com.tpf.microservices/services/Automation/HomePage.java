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
public class HomePage {
	@FindBy(how = How.ID, using = "applications-li")
	@CacheLookup
	private WebElement menuApplication;
	
	@FindBy(how = How.XPATH, using = "//*[contains(text(),'Personal Loan')]")
	@CacheLookup
	private WebElement personalLoan;

	@FindBy(how = How.XPATH, using = "//*[contains(@id,'addImageId')]")
	@CacheLookup
	private WebElement quickLead;

	@FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]//li[contains(@class,'application-column-loan')]//span[contains(text(),'Applications')]")
	@CacheLookup
	private WebElement applicationElement;

	@FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]")
	@CacheLookup
	private WebElement menuApplicationElement;

	@FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]//div[contains(@class,'one-col')][3]//li//a[contains(text(),'Application Manager')]")
	@CacheLookup
	private WebElement applicationManagerElement;

	public HomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }
	
	public void menuClick() {
		this.menuApplication.click();
	}
	
	public void menuChildClick() {
		this.personalLoan.click();
	}

	public void leadQuickClick() {
		await("quickLead visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
				.until(() -> quickLead.isDisplayed());
		this.quickLead.click();
	}

}
