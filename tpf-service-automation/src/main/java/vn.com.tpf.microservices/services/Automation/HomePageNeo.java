package vn.com.tpf.microservices.services.Automation;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.utilities.Constant;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Getter
public class HomePageNeo {
	@FindBy(how = How.ID, using = "menuClick")
	@CacheLookup
	private WebElement menuApplication;

	@FindBy(how = How.XPATH, using = "//*[contains(text(),'Personal Loan')]")
	@CacheLookup
	private WebElement personalLoan;

	@FindBy(how = How.CLASS_NAME, using = "linkfunIcoLink")
	@CacheLookup
	private WebElement quickLead;

	@FindBy(how = How.ID, using = "menuidslead")
	@CacheLookup
	private WebElement quickLeadSearch;

	@FindBy(how = How.ID, using = "menuidsapplications")
	@CacheLookup
	private WebElement applicationMenu;

	@FindBy(how = How.ID, using = "menuidsapplication.grid")
	@CacheLookup
	private WebElement applicationMenuGrid;

	@FindBy(how = How.ID, using = "menuidsapplicationGridView")
	@CacheLookup
	private WebElement applicationMenuGridView;

	@FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]//li[contains(@class,'application-column-loan')]//span[contains(text(),'Applications')]")
	@CacheLookup
	private WebElement applicationElement;

	@FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]")
	@CacheLookup
	private WebElement menuApplicationElement;

	@FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]//div[contains(@class,'one-col')][3]//li//a[contains(text(),'Application Manager')]")
	@CacheLookup
	private WebElement applicationManagerElement;

	@FindBy(how = How.ID, using = "assigned")
	@CacheLookup
	private WebElement assigned;

	@FindBy(how = How.XPATH, using = "//div[contains(@class,'DTFC_LeftHeadWrapper')]//input[@data-id='applicationNumber']")
	@CacheLookup
	private WebElement applicationNumber;

	@FindBy(how = How.XPATH, using = "//*[contains(@id, 'lead_assigned_wrapper')]//input")
	@CacheLookup
	private WebElement searchQueryElement;

	@FindBy(how = How.XPATH, using = "//*[contains(@id, 'lead_assigned_wrapper')]//a")
	@CacheLookup
	private WebElement LeadQueryElement;

	@FindBy(how = How.XPATH, using = "//*[contains(@class, 'DTFC_LeftBodyWrapper')]//a[contains(@class,'IDnumber')]")
	@CacheLookup
	private WebElement appQueryElement;

	@FindBy(how = How.ID, using = "menuidsapplicationManager")
	@CacheLookup
	private WebElement applicationManager;

	@FindBy(how = How.ID, using = "appManager_lead_application_number")
	@CacheLookup
	private WebElement leadNumber;

	@FindBy(how = How.XPATH, using = "//*[contains(@onclick, 'searchApplication')]")
	@CacheLookup
	private WebElement searchApplication;

	@FindBy(how = How.ID, using = "showTasks")
	@CacheLookup
	private WebElement showTasks;

	@FindBy(how = How.XPATH, using = "//*[contains(@id, 'edit_button0') ]//input[contains(@value,'Edit')]")
	@CacheLookup
	private WebElement editAssigned;

	@FindBy(how = How.ID, using = "Text_team_Branch0")
	@CacheLookup
	private WebElement teamName;

	@FindBy(how = How.ID, using = "Text_selected_user0")
	@CacheLookup
	private WebElement nameAccount;

	@FindBy(how = How.XPATH, using = "//*[@id='listitem_team_Branch00']")
	@CacheLookup
	private List<WebElement> teamOptionElement;

	@FindBy(how = How.XPATH, using = "//*[@id='listitem_selected_user00']")
	@CacheLookup
	private List<WebElement> accountOptionElement;

	@FindBy(how = How.XPATH, using = "//*[contains(@id, 'with_branch')]//input")
	@CacheLookup
	private WebElement saveAssigned;

	@FindBy(how = How.XPATH, using = "//table[@id='applicationTable']//tbody//tr[1]//td[1]")
	private WebElement applicationTableAppIDElement;

	@FindBy(how = How.ID, using = "lead")
	@CacheLookup
	private WebElement assignedApp;
	public HomePageNeo(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}

	public void menuClick() {
		this.menuApplication.click();
	}

	public void menuChildClick() {
		this.personalLoan.click();
	}

	public void leadQuickClick() {
		this.applicationMenu.click();
		await("quickLead visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
				.until(() -> quickLead.isDisplayed());
		this.quickLead.click();
	}

	public void leadQuickClickSearch() {
		this.applicationMenu.click();
		await("quickLead visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
				.until(() -> quickLead.isDisplayed());
		this.quickLeadSearch.click();
	}

	public void applicationManagerClick() {
		this.applicationMenu.click();
		await("quickLead visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
				.until(() -> quickLead.isDisplayed());
		this.applicationManager.click();
	}

	public void applicationClick() {
		this.applicationMenu.click();
		await("quickLead visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
				.until(() -> quickLead.isDisplayed());
		this.applicationMenuGrid.click();
		this.applicationMenuGridView.click();
	}

}
