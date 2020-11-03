package vn.com.tpf.microservices.services.Automation.autoCRM;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import jdk.jfr.Timespan;
import lombok.Getter;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import vn.com.tpf.microservices.models.AutoCRM.CRM_EmploymentDetailsDTO;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Getter
public class CRM_ApplicationInfoEmploymentDetailsTab {
    private WebDriver _driver;

    @FindBy(how = How.ID, using = "occupationType_chzn")
    private WebElement occupationTypeElement;

    @FindBy(how = How.ID, using = "occupationType_chznss")
    private WebElement occupationTypeElements;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'occupationType_chzn_o_')]")
    private List<WebElement> occupationTypeOptionElement;

    @FindBy(how = How.ID, using = "listitem_employerName0")
    private WebElement occupationTypeOthersElement;

    @FindBy(how = How.ID, using = "salariedInfo")
    private WebElement divSalariedInfoContentElement;

    @FindBy(how = How.ID, using = "Text_employerName")
    private WebElement companyTaxCodeElement;

    @FindBy(how = How.ID, using = "natureOfBusiness_chzn")
    private WebElement natureOfBusinessElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'natureOfBusiness_chzn_o_')]")
    private List<WebElement> natureOfBusinessOptionElement;

    @FindBy(how = How.ID, using = "EmpTypeId_chzn")
    private WebElement employmentTypeElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'EmpTypeId_chzn_o_')]")
    private List<WebElement> employmentTypeOptionElement;

    @FindBy(how = How.ID, using = "natOfOccId_chzn")
    private WebElement natureOfOccupationElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'natOfOccId_chzn_o_')]")
    private List<WebElement> natureOfOccupationOptionElement;

    @FindBy(how = How.ID, using = "industrySalaried_chzn")
    private WebElement industryElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'industrySalaried_chzn_o_')]")
    private List<WebElement> industryOptionElement;

    @FindBy(how = How.ID, using = "departmentName")
    private WebElement departmentNameElement;

    @FindBy(how = How.ID, using = "employeeNo")
    private WebElement otherCompanyTaxCodeElement;

    @FindBy(how = How.ID, using = "EmpStatId_chzn")
    private WebElement employmentStatusElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'EmpStatId_chzn_o_')]")
    private List<WebElement> employmentStatusOptionElement;

    @FindBy(how = How.ID, using = "designation")
    private WebElement levelElement;

    @FindBy(how = How.ID, using = "year1")
    private WebElement durationYearsElement;

    @FindBy(how = How.ID, using = "month1")
    private WebElement durationMonthsElement;

    @FindBy(how = How.ID, using = "employment_detail_salaried_address_check")
    private WebElement employerAddressCheckElement;

    @FindBy(how = How.ID, using = "doneEmpButton")
    private WebElement doneBtnElement;

    @FindBy(how = How.ID, using = "occupation_Info_Table_wrapper")
    private WebElement tableAfterDoneElement;

    @FindBy(how = How.ID, using = "employmentsaveAndNextButton2")
    private WebElement saveAndNextBtnElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'applicantIdHeader')]/span")
    private WebElement applicationId;

    @FindBy(how = How.ID, using = "totalYearsInOccupationId_chzn")
    private WebElement totalYearsInOccupationIdElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'totalYearsInOccupationId_chzn_o_')]")
    private List<WebElement> totalYearsInOccupationOptionElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'totalYearsInOccupationId_chzn')]//input")
    private WebElement totalYearsInOccupationInputElement;

    @FindBy(how = How.ID, using = "totalMonthsInOccupationId_chzn")
    private WebElement totalMonthsInOccupationIdElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'totalMonthsInOccupationId_chzn_o_')]")
    private List<WebElement> totalMonthsInOccupationOptionElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'totalMonthsInOccupationId_chzn')]//input")
    private WebElement totalMonthsInOccupationInputElement;

    @FindBy(how = How.ID, using = "remarks")
    private WebElement employerName;


    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'MajorChange')]")
    private WebElement modalMajorChangeElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'MajorChange')]//a")
    private List<WebElement> btnMajorChangeElement;

    public CRM_ApplicationInfoEmploymentDetailsTab(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this._driver = driver;
    }

    public void setData(CRM_EmploymentDetailsDTO data) throws JsonParseException, JsonMappingException, IOException {
        FluentWait<WebDriver> fluentWait = new FluentWait<WebDriver>(_driver).withTimeout(Duration.ofSeconds(180))
                .ignoring(NoSuchElementException.class)
                .ignoring(JavascriptException.class)
                .ignoring(ElementClickInterceptedException.class)
                .pollingEvery(Duration.ofMillis(500));

        List<WebElement> deleteOccupationTypeElements =_driver.findElements(By.xpath("//*[contains(@id,'occupation_Info_Table')]//td[3]//ancestor::tr//*[contains(@id,'delete')]"));

        if (deleteOccupationTypeElements.size() > 0){
            for (int i=0; i<deleteOccupationTypeElements.size(); i++) {
                WebElement var = deleteOccupationTypeElements.get(i);
                var.click();
            }
        }

        // Edit
//        List<WebElement> editOccupationTypeElements =_driver.findElements(By.xpath("//*[contains(@id,'occupation_Info_Table')]//td[3]//*[contains(text(),'" + data.getOccupationType() +"')]//ancestor::tr//*[contains(@id,'edit')]"));
//
//        if(editOccupationTypeElements.size()>0){
//            editOccupationTypeElements.get(0).click();
//        }

//        fluentWait.withMessage("occupation Type loading Timeout!").until(ExpectedConditions.presenceOfElementLocated(By.id("occupationType_chzn")));

//        fluentWait.withMessage("occupation Type loading Timeout!").until(ExpectedConditions.visibilityOf(occupationTypeElement));

        try {
            await("getBtnConfirmDeleteVapNextElement1 visibale Timeout!").atMost(15, TimeUnit.SECONDS)
                    .until(() -> modalMajorChangeElement.isDisplayed());
            Utilities.captureScreenShot(_driver);
            btnMajorChangeElement.get(0).click();
            Thread.sleep(15000);
        } catch (Exception e) {
            System.out.println("Confirm Delete visibale");
        }



        await("occupationTypeElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> occupationTypeElement.isDisplayed());

        await("occupationTypeElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> occupationTypeElement.isEnabled());

//        fluentWait.withMessage("occupation Type loading Timeout!").until(ExpectedConditions.elementToBeClickable(occupationTypeElement));

        occupationTypeElement.click();

        await("occupationTypeOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> occupationTypeOptionElement.size() > 0);

        for (WebElement element : occupationTypeOptionElement) {
            if (element.getText().equals(data.getOccupationType())) {
                element.click();
                break;
            }
        }

        await("natureOfOccupationElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> natureOfOccupationElement.isDisplayed() && natureOfOccupationElement.isEnabled());

        natureOfOccupationElement.click();

        await("occupationTypeOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> natureOfOccupationOptionElement.size() > 0);

        for (WebElement element : natureOfOccupationOptionElement) {
            if (element.getText().equals(data.getNatureOfOccupation())) {
                element.click();
                break;
            }
        }

        employerName.clear();
        employerName.sendKeys(data.getRemarks());

        WebElement eaCheckElement =_driver.findElement(By.id("employment_detail_" + data.getOccupationType().toLowerCase() +"_address_check"));
        eaCheckElement.click();

        Utilities.captureScreenShot(_driver);

    }

    public void setMajorOccupation(CRM_EmploymentDetailsDTO data) {
        if(_driver.findElements(By.xpath("//*[@id='occupation_Info_Table']/tbody/tr[td/*[@id='view'][contains(text(),'" + data.getIsMajorEmployment() +"')]]/td[6]/input")).size()>0) {
            Utilities.captureScreenShot(_driver);
            boolean isChecked = _driver.findElement(By.xpath("//*[@id='occupation_Info_Table']/tbody/tr[td/*[@id='view'][contains(text(),'" + data.getIsMajorEmployment() +"')]]/td[6]/input")).isSelected();
            WebElement webElement=_driver.findElement(By.xpath("//*[@id='occupation_Info_Table']/tbody/tr[td/*[@id='view'][contains(text(),'" + data.getIsMajorEmployment() +"')]]/td[6]/input"));
            webElement.click();

            Utilities.captureScreenShot(_driver);
            try {
                if (!isChecked) {
                    await("modalMajorChangeElement not displayed - Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                            .until(() -> modalMajorChangeElement.isDisplayed());
                    Utilities.captureScreenShot(_driver);
                    btnMajorChangeElement.get(0).click();
                }
            } catch (Exception e) {
                System.out.println("Confirm Delete visibale");
            }

            Utilities.captureScreenShot(_driver);
        }
    }
}
