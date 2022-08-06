package vn.com.tpf.microservices.services.Automation.autoCRM;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.is;


@Getter
public class CRM_ExistingCustomerPage  {
    private WebDriver _driver;
    @FindBy(how = How.XPATH, using = "//*[contains(@id,'customerDataFetchType3')]")
    private WebElement bothCheckBoxElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]")
    private WebElement menuApplicationElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]//div[contains(@class,'one-col')][2]//li//a[contains(text(),'Personal Loan  ')]")
    @CacheLookup
    private WebElement personalLoanElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]//div[contains(@class,'one-col')][2]//li//span[contains(text(),'Applications')]")
    @CacheLookup
    private WebElement applicationElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'createCustDiv']//div[contains(@class,'applicant-search-screen')]")
    @CacheLookup
    private WebElement createCustomerElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'uniform-isExistCustomer']//input[contains(@id,'isExistCustomer')]")
    @CacheLookup
    private WebElement isExistCustomerRadioElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'existingCustomerSearch']//form[starts-with(@id, 'applicantSearchVoForm')]")
    @CacheLookup
    private WebElement existingCustomerSearchFormElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'existingCustomerSearch']//form[starts-with(@id, 'applicantSearchVoForm')]//div[@id = 'customerNumber-control-group']//input[@id = 'customerNumber']")
    @CacheLookup
    private WebElement neoCustIDInputElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'existingCustomerSearch']//form[starts-with(@id, 'applicantSearchVoForm')]//div[@id = 'cifNumber-control-group']//input[@id='cifNumber']")
    @CacheLookup
    private WebElement cifNumberInputElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'existingCustomerSearch']//form[starts-with(@id, 'applicantSearchVoForm')]//div[@id = 'uniqueIdNumber-control-group']//input[@id='uniqueIdNumber']")
    @CacheLookup
    private WebElement idNumberInputElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'existingCustomerSearch']//form[starts-with(@id, 'applicantSearchVoForm')]//div[@id = 'idType-control-group']//div[@id = 'idType_chosen']//div[contains(@class, 'chosen-drop')]//div[contains(@class, 'chosen-search')]//input[@type = 'text']")
    @CacheLookup
    private WebElement identificationTypeInputElement;

        @FindBy(how = How.XPATH, using = "//div[@id = 'existingCustomerSearch']//form[starts-with(@id, 'applicantSearchVoForm')]//div[@id = 'idType-control-group']//div[@id = 'idType_chosen']//div[contains(@class, 'chosen-drop')]//ul[contains(@class, 'chosen-results')]")
    @CacheLookup
    private WebElement identificationTypeUlElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'existingCustomerSearch']//form[starts-with(@id, 'applicantSearchVoForm')]//div[@id = 'idType-control-group']//div[@id = 'idType_chosen']//div[contains(@class, 'chosen-drop')]//ul[contains(@class, 'chosen-results')]//li[starts-with(@id, 'idType_chosen_o_')]")
    @CacheLookup
    private List<WebElement> identificationTypeLiElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'existingCustomerSearch']//form[starts-with(@id, 'applicantSearchVoForm')]//input[@id = 'searchCustomer']")
    @CacheLookup
    private WebElement searchCustomerButtonElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'existingCustomerSearch']//form[starts-with(@id, 'applicantSearchVoForm')]//div[@id = 'example']//table[@id = 'searchData_IndividualCustomerTable']//tbody//tr//td")
    @CacheLookup
    private List<WebElement> searchCustomerTableElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'existingCustomerSearch']//form[starts-with(@id, 'applicantSearchVoForm')]//div[@id = 'example']//table[@id = 'searchData_IndividualCustomerTable']//tbody//tr")
    @CacheLookup
    private List<WebElement> searchCustomerTableSizeElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'existingCustomerSearch']//form[starts-with(@id, 'applicantSearchVoForm')]//div[@id = 'example']//table[@id = 'searchData_IndividualCustomerTable']//tbody//tr//td[contains(@class, 'select_individual')]//input[contains(@value,'Select')]")
    @CacheLookup
    private WebElement searchCustomerSelectElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'customerInfo']//form[@id = 'applicantSearchVoFormdetail']")
    @CacheLookup
    private WebElement customerInformationFormElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'customerInfo']//button[contains(text(),'Save')]")
    @CacheLookup
    private WebElement customerInformationSaveElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'container-no-tpl']//div[starts-with(@id,'applicationParty')]")
    @CacheLookup
    private WebElement primaryApplicantElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'container-no-tpl']//div[starts-with(@id,'applicationParty')]//div[@id = 'applicationCustomer_id-control-group']//input[@id = 'applicationCustomer_id']")
    @CacheLookup
    private WebElement primaryApplicantNeoCustIDInputElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'container-no-tpl']//div[starts-with(@id,'applicationParty')]//div[@id = 'applicationCustomer_idNo-control-group']//input[@id = 'applicationCustomer_idNo']")
    @CacheLookup
    private WebElement primaryApplicantIdNumberInputElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'container-no-tpl']//div[starts-with(@id,'applicationParty')]//div[@id = 'applicationCustomer_cifNumber-control-group']//input[@id = 'applicationCustomer_cifNumber']")
    @CacheLookup
    private WebElement primaryApplicantNeoCifNumberInputElement;

    @FindBy(how = How.XPATH, using = "//div[@id ='container-no-tpl']//div[contains(@class,'float-l m-r5')]//h4[@id='applicantIdHeader']//span")
    @CacheLookup
    private WebElement applicantIdHeaderElement;

    @FindBy(how = How.ID, using = "edit-customer-inDetail_0")
    @CacheLookup
    private WebElement editCustomerExistCustomerElement;


    public CRM_ExistingCustomerPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }
    @FindBy(how = How.ID, using = "menuClick")
    @CacheLookup
    private WebElement applicationMenu;

    @FindBy(how = How.ID, using = "menuidsapplication.grid")
    @CacheLookup
    private WebElement applicationMenuGrid;

    @FindBy(how = How.ID, using = "menuidsapplications")
    @CacheLookup
    private WebElement applicationMainMenu;

    @FindBy(how = How.ID, using = "menuidsapplicationGridView")
    @CacheLookup
    private WebElement applicationMenuGridView;

    @FindBy(how = How.CLASS_NAME, using = "innerMenuContainer")
    @CacheLookup
    private WebElement quickLead;

    @FindBy(how = How.ID, using = "appForm")
    @CacheLookup
    private WebElement createApplication;

    @FindBy(how = How.XPATH, using = "//*[@id=\"menuidsPersonalloan  \"]")
    @CacheLookup
    private WebElement applicationMenuGridPersonalLoan;

    @FindBy(how = How.ID, using = "applicationChildTabs_customerPersonal")
    @CacheLookup
    private WebElement customerPersonal;

    public void application_personal_Click() {
        this.applicationMenu.click();
//        await("quickLead visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                .until(() -> quickLead.isDisplayed());
        this.applicationMainMenu.click();
        this.applicationMenuGrid.click();
        this.applicationMenuGridPersonalLoan.click();

    }
    public void applications_Click() {
        this.applicationMenu.click();
//        await("quickLead visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                .until(() -> quickLead.isDisplayed());
        this.applicationMainMenu.click();
        this.applicationMenuGrid.click();
        this.applicationMenuGridView.click();

    }

}
