package vn.com.tpf.microservices.services.Automation.autoCRM;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

@Getter
public class CRM_ApplicationInfoPage {

    private CRM_ApplicationInfoPersonalTab applicationInfoPersonalTab;
    private CRM_ApplicationInfoEmploymentDetailsTab applicationInfoEmploymentDetailsTab;
    private CRM_ApplicationInfoFinancialDetailsTab applicationInfoFinancialDetailsTab;
    private CRM_ApplicationInfoBankDetailsTab applicationInfoBankDetailsTab;

    @FindBy(how = How.ID, using = "customerMainChildTabs_personal_tab")
    @CacheLookup
    private WebElement personalInfoTabElement;

    @FindBy(how = How.ID, using = "customerMainChildTabs_employment_tab")
    @CacheLookup
    private WebElement employmentDetailsTabElement;

    @FindBy(how = How.ID, using = "customerMainChildTabs_bankDetails_tab")
    @CacheLookup
    private WebElement bankDetailsTabElement;

    @FindBy(how = How.ID, using = "customerMainChildTabs_income_tab")
    @CacheLookup
    private WebElement financialDetailsTabElement;

    public CRM_ApplicationInfoPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        applicationInfoPersonalTab = new CRM_ApplicationInfoPersonalTab(driver);
        applicationInfoEmploymentDetailsTab = new CRM_ApplicationInfoEmploymentDetailsTab(driver);
        applicationInfoFinancialDetailsTab = new CRM_ApplicationInfoFinancialDetailsTab(driver);
        applicationInfoBankDetailsTab = new CRM_ApplicationInfoBankDetailsTab(driver);
    }

    public CRM_ApplicationInfoPersonalTab getApplicationInfoPersonalTab() {
        return applicationInfoPersonalTab;
    }

    public CRM_ApplicationInfoEmploymentDetailsTab getApplicationInfoEmploymentDetailsTab() {
        return applicationInfoEmploymentDetailsTab;
    }

    public CRM_ApplicationInfoFinancialDetailsTab getApplicationInfoFinancialDetailsTab() {
        return applicationInfoFinancialDetailsTab;
    }

    public CRM_ApplicationInfoBankDetailsTab getApplicationInfoBankDetailsTab() {
        return applicationInfoBankDetailsTab;
    }

}
