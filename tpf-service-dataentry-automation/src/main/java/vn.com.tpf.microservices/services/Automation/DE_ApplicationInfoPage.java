package vn.com.tpf.microservices.services.Automation;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

@Getter
public class DE_ApplicationInfoPage {

	private DE_ApplicationInfoPersonalTab applicationInfoPersonalTab;
	private DE_ApplicationInfoEmploymentDetailsTab applicationInfoEmploymentDetailsTab;
	private DE_ApplicationInfoFinancialDetailsTab applicationInfoFinancialDetailsTab;

	@FindBy(how = How.ID, using = "customerMainChildTabs_personal_tab")
	@CacheLookup
	private WebElement personalInfoTabElement;

	@FindBy(how = How.ID, using = "customerMainChildTabs_employment_tab")
	@CacheLookup
	private WebElement employmentDetailsTabElement;

	@FindBy(how = How.ID, using = "customerMainChildTabs_income_tab")
	@CacheLookup
	private WebElement financialDetailsTabElement;

	public DE_ApplicationInfoPage(WebDriver driver) {
		PageFactory.initElements(driver, this);
		applicationInfoPersonalTab = new DE_ApplicationInfoPersonalTab(driver);
		applicationInfoEmploymentDetailsTab = new DE_ApplicationInfoEmploymentDetailsTab(driver);
		applicationInfoFinancialDetailsTab = new DE_ApplicationInfoFinancialDetailsTab(driver);
	}

	public DE_ApplicationInfoPersonalTab getApplicationInfoPersonalTab() {
		return applicationInfoPersonalTab;
	}

	public DE_ApplicationInfoEmploymentDetailsTab getApplicationInfoEmploymentDetailsTab() {
		return applicationInfoEmploymentDetailsTab;
	}

	public DE_ApplicationInfoFinancialDetailsTab getApplicationInfoFinancialDetailsTab() {
		return applicationInfoFinancialDetailsTab;
	}

}
