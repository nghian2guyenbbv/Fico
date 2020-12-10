package vn.com.tpf.microservices.services.Automation.autoCRM;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.Getter;
import org.awaitility.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import vn.com.tpf.microservices.models.AutoCRM.CRM_EmploymentDetailsDTO;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.with;

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

    @FindBy(how = How.XPATH, using = "//table[@id = 'occupation_Info_Table']//tr//td")
    private List<WebElement> deleteIdDetailElement;

    public CRM_ApplicationInfoEmploymentDetailsTab(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this._driver = driver;
    }

    public void setData(CRM_EmploymentDetailsDTO data) throws JsonParseException, JsonMappingException, IOException, InterruptedException {

        Actions actions = new Actions(_driver);

        List<WebElement> listOccupationDelete = _driver.findElements(By.xpath("//table[@id = 'occupation_Info_Table']//tr//td[1]"));

        List<String> listStringOccupationDelete = new ArrayList<>();

        for (WebElement e: listOccupationDelete){
            listStringOccupationDelete.add(e.getAttribute("innerHTML").trim());
        }

        boolean checkListOccupationDelete = listOccupationDelete.size() != 0;

        if(checkListOccupationDelete && deleteIdDetailElement.size() > 2){
            for (String OccupationDeleteButton: listStringOccupationDelete){
//                String occupationTypeElement = _driver.findElement(By.xpath("//*[contains(@id,'occupation_Info_Table')]//*[contains(text(),'" + OccupationDeleteButton + "')]//ancestor::tr//td[3]/a[@id = 'view']")).getAttribute("innerHTML").trim();
//                if ("Salaried".equals(occupationTypeElement)){

                    Thread.sleep(5000);

                    WebElement occupationDelete = _driver.findElement(By.xpath("//*[contains(@id,'occupation_Info_Table')]//*[contains(text(),'" + OccupationDeleteButton + "')]//ancestor::tr//*[contains(@id,'delete')]"));

                    actions.moveToElement(occupationDelete).click().build().perform();

                    Thread.sleep(5000);

                    boolean checkModalMajorChange = _driver.findElements(By.xpath("//*[contains(@id, 'MajorChange')][contains(@style,'block')]")).size() != 0;

                    if (checkModalMajorChange){
                        await("Confirm Delete visibale!!!").atMost(Duration.TEN_MINUTES)
                                .until(() -> modalMajorChangeElement.isDisplayed());
                        Utilities.captureScreenShot(_driver);
                        btnMajorChangeElement.get(0).click();
                    }

//                    break;
//                }
            }
        }

//        Thread.sleep(10000);
//
//        boolean occupationEditChange = _driver.findElements(By.xpath("//*[contains(@id,'occupation_Info_Table')]//*[contains(text(),'" + data.getOccupationType() +"')]//ancestor::tr//*[contains(@id,'edit')]")).size() != 0;
//
//        if(occupationEditChange) {
//            WebElement occupationEdit = _driver.findElement(By.xpath("//*[contains(@id,'occupation_Info_Table')]//*[contains(text(),'" + data.getOccupationType() + "')]//ancestor::tr//*[contains(@id,'edit')]"));
//            actions.moveToElement(occupationEdit).click().build().perform();
//        }

        with().pollInterval(Duration.FIVE_SECONDS).
        await("occupationTypeElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> occupationTypeElement.isDisplayed() && occupationTypeElement.isEnabled());

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

    public void setMajorOccupation(CRM_EmploymentDetailsDTO data) throws InterruptedException {
        if(_driver.findElements(By.xpath("//*[@id='occupation_Info_Table']/tbody/tr[td/*[@id='view'][contains(text(),'" + data.getIsMajorEmployment() +"')]]/td[6]/input")).size()>0) {
            Utilities.captureScreenShot(_driver);
            boolean isChecked = _driver.findElement(By.xpath("//*[@id='occupation_Info_Table']/tbody/tr[td/*[@id='view'][contains(text(),'" + data.getIsMajorEmployment() +"')]]/td[6]/input")).isSelected();
            WebElement webElement=_driver.findElement(By.xpath("//*[@id='occupation_Info_Table']/tbody/tr[td/*[@id='view'][contains(text(),'" + data.getIsMajorEmployment() +"')]]/td[6]/input"));
            webElement.click();

            Utilities.captureScreenShot(_driver);

            if (!isChecked) {
                Thread.sleep(5000);
                boolean checkModalMajorChange = modalMajorChangeElement.isDisplayed();
                if (checkModalMajorChange){

                    await("modalMajorChangeElement not displayed - Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                            .until(() -> modalMajorChangeElement.isDisplayed());
                    Utilities.captureScreenShot(_driver);
                    btnMajorChangeElement.get(0).click();
                }
            }

            Utilities.captureScreenShot(_driver);
        }
    }
}
