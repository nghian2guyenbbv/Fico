package vn.com.tpf.microservices.services.Automation.autoField;

import lombok.Getter;
import org.apache.commons.lang.StringEscapeUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.models.AutoField.ExistingCustomerDTO;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.is;


@Getter
public class FV_ExistingCustomerPage {
    private WebDriver _driver;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]")
    private WebElement menuApplicationElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]//div[contains(@class,'one-col')][2]//li//a[contains(text(),'Personal Loan  ')]")
    @CacheLookup
    private WebElement personalLoanElement;

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

    @FindBy(how = How.XPATH, using = "//div[@id = 'existingCustomerSearch']//form[starts-with(@id, 'applicantSearchVoForm')]//div[@id = 'idType-control-group']//div[@id = 'idType_chzn']//div[contains(@class, 'chzn-drop')]//div[contains(@class, 'chzn-search')]//input[@type = 'text']")
    @CacheLookup
    private WebElement identificationTypeInputElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'existingCustomerSearch']//form[starts-with(@id, 'applicantSearchVoForm')]//div[@id = 'idType-control-group']//div[@id = 'idType_chzn']//div[contains(@class, 'chzn-drop')]//ul[contains(@class, 'chzn-results')]")
    @CacheLookup
    private WebElement identificationTypeUlElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'existingCustomerSearch']//form[starts-with(@id, 'applicantSearchVoForm')]//div[@id = 'idType-control-group']//div[@id = 'idType_chzn']//div[contains(@class, 'chzn-drop')]//ul[contains(@class, 'chzn-results')]//li[starts-with(@id, 'idType_chzn_o_')]")
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


    public FV_ExistingCustomerPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }


}
