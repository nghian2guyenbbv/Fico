package vn.com.tpf.microservices.services.Automation;

import lombok.Getter;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

@Getter
public class DE_ApplicationInfoPage {
    private WebDriver _driver;

    private DE_ApplicationInfoPersonalTab applicationInfoPersonalTab;
    private DE_ApplicationInfoPersonalTabNeo applicationInfoPersonalTabNeo;
    private DE_ApplicationInfoEmploymentDetailsTab applicationInfoEmploymentDetailsTab;

    private DE_ApplicationInfoEmploymentDetailsTabNeo applicationInfoEmploymentDetailsTabNeo;
    private DE_ApplicationInfoFinancialDetailsTab applicationInfoFinancialDetailsTab;
    private DE_ApplicationInfoFinancialDetailsTabNeo applicationInfoFinancialDetailsTabNeo;
    private DE_ApplicationInfoBankCreditCardDetailsTab applicationInfoBankCreditCardDetailsTab;
    private DE_ApplicationInfoBankCreditCardDetailsTabNeo applicationInfoBankCreditCardDetailsTabNeo;

    @FindBy(how = How.ID, using = "customerMainChildTabs_personal_tab")
    @CacheLookup
    private WebElement personalInfoTabElement;

    @FindBy(how = How.ID, using = "customerMainChildTabs_employment_tab")
    @CacheLookup
    private WebElement employmentDetailsTabElement;

    @FindBy(how = How.ID, using = "customerMainChildTabs_income_tab")
    @CacheLookup
    private WebElement financialDetailsTabElement;

    @FindBy(how = How.ID, using = "customerMainChildTabs_bankDetails_tab")
    @CacheLookup
    private WebElement bankCreditCardDetailsDetailsTabElement;

    public DE_ApplicationInfoPage(WebDriver driver) {
        _driver = driver;
        PageFactory.initElements(driver, this);
        applicationInfoPersonalTab = new DE_ApplicationInfoPersonalTab(driver);
        applicationInfoPersonalTabNeo = new DE_ApplicationInfoPersonalTabNeo(driver);
        applicationInfoEmploymentDetailsTab = new DE_ApplicationInfoEmploymentDetailsTab(driver);
        applicationInfoEmploymentDetailsTabNeo = new DE_ApplicationInfoEmploymentDetailsTabNeo(driver);
        applicationInfoFinancialDetailsTab = new DE_ApplicationInfoFinancialDetailsTab(driver);
        applicationInfoFinancialDetailsTabNeo = new DE_ApplicationInfoFinancialDetailsTabNeo(driver);
        applicationInfoBankCreditCardDetailsTab = new DE_ApplicationInfoBankCreditCardDetailsTab(driver);
        applicationInfoBankCreditCardDetailsTabNeo = new DE_ApplicationInfoBankCreditCardDetailsTabNeo(driver);
    }

    public DE_ApplicationInfoPersonalTab getApplicationInfoPersonalTab() {
        return applicationInfoPersonalTab;
    }

    public DE_ApplicationInfoPersonalTabNeo getApplicationInfoPersonalTabNeo() {
        return applicationInfoPersonalTabNeo;
    }

    public DE_ApplicationInfoEmploymentDetailsTab getApplicationInfoEmploymentDetailsTab() {
        return applicationInfoEmploymentDetailsTab;
    }

    public DE_ApplicationInfoEmploymentDetailsTabNeo getApplicationInfoEmploymentDetailsTabNeo() {
        return applicationInfoEmploymentDetailsTabNeo;
    }

    public DE_ApplicationInfoFinancialDetailsTab getApplicationInfoFinancialDetailsTab() {
        return applicationInfoFinancialDetailsTab;
    }

    public DE_ApplicationInfoFinancialDetailsTabNeo getApplicationInfoFinancialDetailsTabNeo() {
        return applicationInfoFinancialDetailsTabNeo;
    }

    public DE_ApplicationInfoBankCreditCardDetailsTab getApplicationInfoBankCreditCardDetailsTab() {
        return applicationInfoBankCreditCardDetailsTab;
    }

    public DE_ApplicationInfoBankCreditCardDetailsTabNeo getApplicationInfoBankCreditCardDetailsTabNeo() {
        return applicationInfoBankCreditCardDetailsTabNeo;
    }
    public void loadEmloymentTab() {
        JavascriptExecutor jse2 = (JavascriptExecutor) _driver;
        jse2.executeScript("arguments[0].click();", employmentDetailsTabElement);
    }
}
