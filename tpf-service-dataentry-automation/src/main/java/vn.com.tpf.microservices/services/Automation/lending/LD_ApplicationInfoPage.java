package vn.com.tpf.microservices.services.Automation.lending;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

@Getter
public class LD_ApplicationInfoPage {

	private LD_ApplicationInfoPersonalTab applicationInfoPersonalTab;
	private LD_ApplicationInfoEmploymentDetailsTab applicationInfoEmploymentDetailsTab;
	private LD_ApplicationInfoFinancialDetailsTab applicationInfoFinancialDetailsTab;

	@FindBy(how = How.ID, using = "customerMainChildTabs_personal_tab")
	@CacheLookup
	private WebElement personalInfoTabElement;

	@FindBy(how = How.ID, using = "customerMainChildTabs_employment_tab")
	@CacheLookup
	private WebElement employmentDetailsTabElement;

	@FindBy(how = How.ID, using = "customerMainChildTabs_income_tab")
	@CacheLookup
	private WebElement financialDetailsTabElement;

	public LD_ApplicationInfoPage(WebDriver driver) {
		PageFactory.initElements(driver, this);
		applicationInfoPersonalTab = new LD_ApplicationInfoPersonalTab(driver);
		applicationInfoEmploymentDetailsTab = new LD_ApplicationInfoEmploymentDetailsTab(driver);
		applicationInfoFinancialDetailsTab = new LD_ApplicationInfoFinancialDetailsTab(driver);
	}

	public LD_ApplicationInfoPersonalTab getApplicationInfoPersonalTab() {
		return applicationInfoPersonalTab;
	}

	public LD_ApplicationInfoEmploymentDetailsTab getApplicationInfoEmploymentDetailsTab() {
		return applicationInfoEmploymentDetailsTab;
	}

	public LD_ApplicationInfoFinancialDetailsTab getApplicationInfoFinancialDetailsTab() {
		return applicationInfoFinancialDetailsTab;
	}

}
