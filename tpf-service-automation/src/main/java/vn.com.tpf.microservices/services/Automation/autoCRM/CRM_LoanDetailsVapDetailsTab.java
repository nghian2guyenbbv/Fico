package vn.com.tpf.microservices.services.Automation.autoCRM;

import lombok.Getter;
import org.openqa.selenium.By;
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

    @FindBy(how = How.XPATH, using = "//a[contains(@id, 'listitem_disburseToBP')]")
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
        //Delete row VAP product
        //comment - have not in video topup
//        Utilities.captureScreenShot(_driver);
//        int count = deleteVapElement.size();
//        if (count > 0) {
//            Utilities.captureScreenShot(_driver);
//            for(WebElement we : deleteVapElement.subList(0, count))
//            {
//                we.click();
//            }
//        }

        Utilities.captureScreenShot(_driver);
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
        //application name
        WebElement applName = _driver.findElement(By.xpath("//*[@id=\"applicantList_chosen\"]"));
        List<WebElement> applNameOption = _driver.findElements(By.xpath("//*[contains(@id, 'applicantList_chosen_o_')]"));
        await("applName loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> applName.isDisplayed());

        applName.click();

        await("applNameOption name loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> applNameOption.size() > 0);
        for (WebElement element : applNameOption) {
            element.click();
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
            element.click();
        }

        doneBtnElement.click();
    }

}
