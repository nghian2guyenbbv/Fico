package vn.com.tpf.microservices.services.Automation.autoCRM;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

@Getter
public class CRM_ApplicationInfoFinancialDetailsTab {
    private WebDriver _driver;

    @FindBy(how = How.ID, using = "customerMainChildTabs_income_tab")
    @CacheLookup
    private WebElement customerMainChildTabs_income_tabElement;

    // Income Details
    @FindBy(how = How.ID, using = "finInfo_IncomeInlineGrid_link")
    @CacheLookup
    private WebElement loadIncomeDetailElement;

    @FindBy(how = How.ID, using = "finInfo_IncomeInlineGrid_AccDiv")
    @CacheLookup
    private WebElement incomeDetailDivElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'addMoreIncDetails')]")
    @CacheLookup
    private WebElement btnAddMoreIncomeDtlElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'rowIncDetails')]")
    private List<WebElement> trElements;

    @FindBy(how = How.ID, using = "financialSaveAndNextButton2")
    @CacheLookup
    private WebElement btnSaveAndNextElement;

    @FindBy(how = How.ID, using = "childModalIncomeDetailInlineGrid")
    @CacheLookup
    private WebElement childModalIncomeDetailInlineGridElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'incomeDetailForm_incomeSource_chzn')]")
    private WebElement incomeDetailForm_incomeSourceElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'incomeDetailForm_incomeSource_chzn_o_')]")
    private List<WebElement> incomeDetailForm_incomeSourceOptionElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'incomeDetailForm_incomeSource_chzn')]//input")
    private WebElement incomeDetailForm_incomeSourceInputElement;

    @FindBy(how = How.ID, using = "incomeDetailForm_paymentMode_chzn")
    private WebElement incomeDetailForm_paymentModeElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'incomeDetailForm_paymentMode_chzn_o_')]")
    private List<WebElement> incomeDetailForm_paymentModeOptionElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'incomeDetailForm_paymentMode_chzn')]//input")
    private WebElement incomeDetailForm_paymentModeInputElement;

    @FindBy(how = How.ID, using = "childModalWindowDoneButtonIncomeDetailInlineGrid")
    private WebElement childModalWindowDoneButtonIncomeDetailInlineGridElement;

    //----------------------------UPDATE---------------------------------
    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'DeleteIncDetails')]")
    @CacheLookup
    private List<WebElement> deleteIncDetailsElement;


    public CRM_ApplicationInfoFinancialDetailsTab(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this._driver = driver;
    }
}
