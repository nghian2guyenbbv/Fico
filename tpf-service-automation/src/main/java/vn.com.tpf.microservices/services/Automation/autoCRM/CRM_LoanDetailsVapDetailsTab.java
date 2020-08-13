package vn.com.tpf.microservices.services.Automation.autoCRM;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.models.AutoCRM.CRM_VapDetailsDTO;
import vn.com.tpf.microservices.models.AutoCRM.CRM_VapDetailsListDTO;
import vn.com.tpf.microservices.models.Automation.LoanDetailsVapDTO;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Getter
public class CRM_LoanDetailsVapDetailsTab {

    private WebDriver _driver;

    @FindBy(how = How.ID, using = "vapDetailsLiId")
    private WebElement tabVapDetailsElement;

    @FindBy(how = How.ID, using = "vapDetails")
    private WebElement vapDetailsDivContainerElement;

    @FindBy(how = How.ID, using = "vapProduct_chzn")
    @CacheLookup
    private WebElement vapProductElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'vapProduct_chzn_o_')]")
    @CacheLookup
    private List<WebElement> vapProductOptionElement;

    @FindBy(how = How.ID, using = "treatmentType_chzn")
    @CacheLookup
    private WebElement vapTreatmentElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'treatmentType_chzn_o_')]")
    @CacheLookup
    private List<WebElement> vapTreatmentOptionElement;

    @FindBy(how = How.ID, using = "insuranceCompany_chzn")
    @CacheLookup
    private WebElement insuranceCompanyElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'insuranceCompany_chzn_o_')]")
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

    @FindBy(how = How.XPATH, using = "//div[@id = 'vap_details_Table_wrapper']//table[@id = 'vap_details_Table']//img[@id = 'delete']")
    private List<WebElement> deleteVapElement;

    public CRM_LoanDetailsVapDetailsTab(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    public void setData(CRM_VapDetailsDTO data) {
        //vap product
        if (deleteVapElement.size() > 0){
            for (int i=0; i<deleteVapElement.size()-1; i++) {
                WebElement var = deleteVapElement.get(i);
                var.click();
            }
        }

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
        insuranceCompanyElement.click();
        await("insuranceCompanyElementOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> insuranceCompanyOptionElement.size() > 0);
        for (WebElement element : insuranceCompanyOptionElement) {
            if (element.getText().equals(data.getInsuranceCompany())) {
                element.click();
                break;
            }
        }

        doneBtnElement.click();
    }

}
