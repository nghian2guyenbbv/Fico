package vn.com.tpf.microservices.services.Automation;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.models.Automation.LoanDetailsVapDTO;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Getter
public class DE_LoanDetailsVapDetailsTabNeo {
    private WebDriver _driver;

    @FindBy(how = How.ID, using = "vapDetailsLiId")
    private WebElement tabVapDetailsElement;

    @FindBy(how = How.ID, using = "vapDetails")
    private WebElement vapDetailsDivContainerElement;

    @FindBy(how = How.ID, using = "vapProduct_chosen")
    @CacheLookup
    private WebElement vapProductElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'vapProduct_chosen_o_')]")
    @CacheLookup
    private List<WebElement> vapProductOptionElement;

    @FindBy(how = How.ID, using = "treatmentType_chosen")
    @CacheLookup
    private WebElement vapTreatmentElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'treatmentType_chosen_o_')]")
    @CacheLookup
    private List<WebElement> vapTreatmentOptionElement;

    @FindBy(how = How.ID, using = "Text_disburseToBP")
    @CacheLookup
    private WebElement insuranceCompanyElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'listitem_disburseToBP')]")
    @CacheLookup
    private List<WebElement> insuranceCompanyOptionElement;

    @FindBy(how = How.ID, using = "doneButton")
    @CacheLookup
    private WebElement doneBtnElement;

    @FindBy(how = How.ID, using = "employmentsaveAndNextButton1")
    @CacheLookup
    private WebElement btnSaveAndNextElement;

    //-------------------- UPDATE -------------------
    @FindBy(how = How.XPATH, using = "//*[contains(@id,'vap_details_Table')]//*[contains(@id,'edit')]")
    private WebElement editVapElement;

    @FindBy(how = How.ID, using = "applicantList_chosen")
    @CacheLookup
    private WebElement applicationNameElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'applicantList_chosen_o_')]")
    @CacheLookup
    private List<WebElement> applicationNameOptionElement;

    public DE_LoanDetailsVapDetailsTabNeo(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }


    public void setData(LoanDetailsVapDTO data) {
        //vap product
//        await("vapProductElement loading timeout").atMost(Constant.TIME_OUT_S,TimeUnit.SECONDS)
//                .until(() -> vapProductElement.isDisplayed());
        vapProductElement.click();
        Utilities.captureScreenShot(_driver);
        await("vapProductOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> vapProductOptionElement.size() > 0);
        for (WebElement element : vapProductOptionElement) {
            if (element.getText().equals(data.getVapProduct())) {
                element.click();
                Utilities.captureScreenShot(_driver);
                break;
            }
        }
        Utilities.captureScreenShot(_driver);
        await("vapTreatmentElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> applicationNameElement.isDisplayed());
        applicationNameElement.click();
        Utilities.captureScreenShot(_driver);
        await("vapTreatmentElementOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> applicationNameOptionElement.size() > 0);
        for (WebElement element : applicationNameOptionElement) {
                element.click();
                Utilities.captureScreenShot(_driver);
                break;
        }

        //vap treatment
        await("vapTreatmentElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> vapTreatmentElement.isDisplayed());
        vapTreatmentElement.click();
        Utilities.captureScreenShot(_driver);
        await("vapTreatmentElementOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> vapTreatmentOptionElement.size() > 0);
        for (WebElement element : vapTreatmentOptionElement) {
            if (element.getText().equals(data.getVapTreatment())) {
                element.click();
                Utilities.captureScreenShot(_driver);
                break;
            }
        }
        Utilities.captureScreenShot(_driver);
        //insurance company
        await("insuranceCompanyElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> insuranceCompanyElement.isDisplayed());
        insuranceCompanyElement.clear();
        insuranceCompanyElement.sendKeys(data.getInsuranceCompany());
        await("insuranceCompanyElementOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> insuranceCompanyOptionElement.size() > 0);
        for (WebElement element : insuranceCompanyOptionElement) {
            if (!Objects.isNull(element.getAttribute("username")) && element.getAttribute("username").equals(data.getInsuranceCompany())) {
                element.click();
                break;
            }
        }


        doneBtnElement.click();
    }

    public void updateData(LoanDetailsVapDTO data) {

        //kiem tra neu chua co thi tao, cho update FULL
        if(_driver.findElements(By.xpath("//*[contains(@id,'vap_details_Table')]//*[contains(@id,'edit')]")).size()==0){
            setData(data);
            return;
        }

        await("editVapElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> editVapElement.isDisplayed());

        editVapElement.click();

        Utilities.captureScreenShot(_driver);
        await("vapProductOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> vapProductOptionElement.size() > 0);
        for (WebElement element : vapProductOptionElement) {
            if (element.getText().equals(data.getVapProduct())) {
                element.click();
                Utilities.captureScreenShot(_driver);
                break;
            }
        }
        Utilities.captureScreenShot(_driver);
        //vap treatment
        await("vapTreatmentElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> vapTreatmentElement.isDisplayed());
        vapTreatmentElement.click();
        Utilities.captureScreenShot(_driver);
        await("vapTreatmentElementOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> vapTreatmentOptionElement.size() > 0);
        for (WebElement element : vapTreatmentOptionElement) {
            if (element.getText().equals(data.getVapTreatment())) {
                element.click();
                Utilities.captureScreenShot(_driver);
                break;
            }
        }
        Utilities.captureScreenShot(_driver);
        //insurance company
        await("insuranceCompanyElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> insuranceCompanyElement.isDisplayed());
        insuranceCompanyElement.clear();
        insuranceCompanyElement.sendKeys(data.getInsuranceCompany());
        await("insuranceCompanyElementOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> insuranceCompanyOptionElement.size() > 0);
        for (WebElement element : insuranceCompanyOptionElement) {
            if (!Objects.isNull(element.getAttribute("username")) && element.getAttribute("username").equals(data.getInsuranceCompany())) {
                element.click();
                break;
            }
        }

        doneBtnElement.click();
    }
}
