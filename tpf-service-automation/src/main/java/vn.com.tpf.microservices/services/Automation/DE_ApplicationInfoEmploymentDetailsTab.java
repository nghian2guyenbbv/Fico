package vn.com.tpf.microservices.services.Automation;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.models.Automation.EmploymentDTO;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Getter
public class DE_ApplicationInfoEmploymentDetailsTab {
    private WebDriver _driver;

    @FindBy(how = How.ID, using = "occupationType_chzn")
    @CacheLookup
    private WebElement occupationTypeElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'occupationType_chzn_o_')]")
    @CacheLookup
    private List<WebElement> occupationTypeOptionElement;

    @FindBy(how = How.ID, using = "listitem_employerName0")
    @CacheLookup
    private WebElement occupationTypeOthersElement;

    @FindBy(how = How.ID, using = "salariedInfo")
    @CacheLookup
    private WebElement divSalariedInfoContentElement;

    @FindBy(how = How.ID, using = "Text_employerName")
    @CacheLookup
    private WebElement companyTaxCodeElement;

    @FindBy(how = How.ID, using = "natureOfBusiness_chzn")
    @CacheLookup
    private WebElement natureOfBusinessElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'natureOfBusiness_chzn_o_')]")
    @CacheLookup
    private List<WebElement> natureOfBusinessOptionElement;

    @FindBy(how = How.ID, using = "EmpTypeId_chzn")
    @CacheLookup
    private WebElement employmentTypeElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'EmpTypeId_chzn_o_')]")
    @CacheLookup
    private List<WebElement> employmentTypeOptionElement;

    @FindBy(how = How.ID, using = "natOfOccId_chzn")
    @CacheLookup
    private WebElement natureOfOccupationElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'natOfOccId_chzn_o_')]")
    @CacheLookup
    private List<WebElement> natureOfOccupationOptionElement;

    @FindBy(how = How.ID, using = "industrySalaried_chzn")
    @CacheLookup
    private WebElement industryElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'industrySalaried_chzn_o_')]")
    @CacheLookup
    private List<WebElement> industryOptionElement;

    @FindBy(how = How.ID, using = "departmentName")
    @CacheLookup
    private WebElement departmentNameElement;

    @FindBy(how = How.ID, using = "employeeNo")
    @CacheLookup
    private WebElement otherCompanyTaxCodeElement;

    @FindBy(how = How.ID, using = "EmpStatId_chzn")
    @CacheLookup
    private WebElement employmentStatusElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'EmpStatId_chzn_o_')]")
    @CacheLookup
    private List<WebElement> employmentStatusOptionElement;

    @FindBy(how = How.ID, using = "designation")
    @CacheLookup
    private WebElement levelElement;

    @FindBy(how = How.ID, using = "year1")
    @CacheLookup
    private WebElement durationYearsElement;

    @FindBy(how = How.ID, using = "month1")
    @CacheLookup
    private WebElement durationMonthsElement;

    @FindBy(how = How.ID, using = "employment_detail_salaried_address_check")
    @CacheLookup
    private WebElement employerAddressCheckElement;

    @FindBy(how = How.ID, using = "doneEmpButton")
    @CacheLookup
    private WebElement doneBtnElement;

    @FindBy(how = How.ID, using = "occupation_Info_Table_wrapper")
    @CacheLookup
    private WebElement tableAfterDoneElement;

    @FindBy(how = How.ID, using = "employmentsaveAndNextButton2")
    @CacheLookup
    private WebElement saveAndNextBtnElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'applicantIdHeader')]/span")
    @CacheLookup
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
    @CacheLookup
    private WebElement employerName;


    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'MajorChange')]")
    private WebElement modalMajorChangeElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'MajorChange')]//a")
    private List<WebElement> btnMajorChangeElement;

    public DE_ApplicationInfoEmploymentDetailsTab(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this._driver = driver;
    }

    public void setData(EmploymentDTO data) throws JsonParseException, JsonMappingException, IOException {

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
//		Select occupationTypeSelect = new Select(occupationTypeElement);
//		await("Occupation Type loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//				.until(() -> occupationTypeSelect.getOptions().size() > 0);
//		occupationTypeSelect.selectByVisibleText(data.getOccupationType());

        if (data.getOccupationType().equals("Others")) {
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
        } else {
            companyTaxCodeElement.sendKeys("%%%");
            await("Occupation Type option loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> occupationTypeOthersElement.isDisplayed());
            occupationTypeOthersElement.click();

            natureOfBusinessElement.click();
            await("natureOfBusinessOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> natureOfBusinessOptionElement.size() > 0);
            for (WebElement element : natureOfBusinessOptionElement) {
                if (element.getText().equals(data.getNatureOfBussiness())) {
                    element.click();
                    break;
                }
            }

//		Select natureOfBusinessSelect = new Select(natureOfBusinessElement);
//		await("Nature of business loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//				.until(() -> natureOfBusinessSelect.getOptions().size() > 0);
//		natureOfBusinessSelect.selectByVisibleText(data.getNatureOfBussiness());

            industryElement.click();
            await("industryOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> industryOptionElement.size() > 0);
            for (WebElement element : industryOptionElement) {
                if (element.getText().equals(data.getIndustry())) {
                    element.click();
                    break;
                }
            }
//		Select industrySelect = new Select(industryElement);
//		await("Industry loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//				.until(() -> industrySelect.getOptions().size() > 0);
//		industrySelect.selectByVisibleText(data.getIndustry());

            if(!data.getEmploymentType().isEmpty())
            {
                employmentTypeElement.click();
                await("employmentTypeOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> employmentTypeOptionElement.size() > 0);
                for (WebElement element : employmentTypeOptionElement) {
                    if (element.getText().equals(data.getEmploymentType())) {
                        element.click();
                        break;
                    }
                }
            }




            departmentNameElement.sendKeys(data.getDepartment());

            otherCompanyTaxCodeElement.sendKeys(data.getOtherCompanyTaxCode());

            employmentStatusElement.click();
            await("employmentStatusOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> employmentStatusOptionElement.size() > 0);
            for (WebElement element : employmentStatusOptionElement) {
                if (element.getText().equals(data.getEmploymentStatus())) {
                    element.click();
                    break;
                }
            }
//		Select employmentStatusSelect = new Select(employmentStatusElement);
//		await("Employment Status loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//				.until(() -> employmentStatusSelect.getOptions().size() > 0);
//		employmentStatusSelect.selectByVisibleText(data.getEmploymentStatus());

            levelElement.sendKeys(data.getLevel());

            Utilities.captureScreenShot(_driver);
            durationYearsElement.sendKeys(data.getDurationYears());
            durationMonthsElement.sendKeys(data.getDurationMonths());
            Utilities.captureScreenShot(_driver);
            employerName.clear();
            employerName.sendKeys(data.getRemarks());

            employerAddressCheckElement.click();
        }
    }

    public void updateData(EmploymentDTO data) throws JsonParseException, JsonMappingException, IOException {

        if(_driver.findElements(By.xpath("//*[contains(@id,'occupation_Info_Table')]//*[contains(text(),'" + data.getOccupationType() +"')]//ancestor::tr//*[contains(@id,'edit')]")).size()==0)
        {
            System.out.println("Create new employ: " + data.getOccupationType());
            setData(data);
            return;
        }

        WebElement we =_driver.findElement(By.xpath("//*[contains(@id,'occupation_Info_Table')]//*[contains(text(),'" + data.getOccupationType() +"')]//ancestor::tr//*[contains(@id,'edit')]"));
        we.click();

        companyTaxCodeElement.clear();
        companyTaxCodeElement.sendKeys("%%%");
        await("Occupation Type option loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> occupationTypeOthersElement.isDisplayed());
        occupationTypeOthersElement.click();

        natureOfBusinessElement.click();
        await("natureOfBusinessOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> natureOfBusinessOptionElement.size() > 0);
        for (WebElement element : natureOfBusinessOptionElement) {
            if (element.getText().equals(data.getNatureOfBussiness())) {
                element.click();
                break;
            }
        }

        industryElement.click();
        await("industryOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> industryOptionElement.size() > 0);
        for (WebElement element : industryOptionElement) {
            if (element.getText().equals(data.getIndustry())) {
                element.click();
                break;
            }
        }

        departmentNameElement.clear();
        departmentNameElement.sendKeys(data.getDepartment());

        otherCompanyTaxCodeElement.clear();
        otherCompanyTaxCodeElement.sendKeys(data.getOtherCompanyTaxCode());

        employmentStatusElement.click();
        await("employmentStatusOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> employmentStatusOptionElement.size() > 0);
        for (WebElement element : employmentStatusOptionElement) {
            if (element.getText().equals(data.getEmploymentStatus())) {
                element.click();
                break;
            }
        }

        levelElement.clear();
        levelElement.sendKeys(data.getLevel());
        durationYearsElement.clear();
        durationYearsElement.sendKeys(data.getDurationYears());
        durationMonthsElement.clear();
        durationMonthsElement.sendKeys(data.getDurationMonths());


        employerName.clear();
        employerName.sendKeys(data.getRemarks());
        //click nua se mat check
        //employerAddressCheckElement.click();

    }

    public void setExperienceInIndustry(EmploymentDTO data) {
        Actions actions = new Actions(_driver);
        Utilities.captureScreenShot(_driver);
        actions.moveToElement(totalYearsInOccupationIdElement).click().build().perform();
        await("cityOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> totalYearsInOccupationOptionElement.size() > 1);
        totalYearsInOccupationInputElement.sendKeys(String.valueOf(data.getTotalYearsInOccupation()));
        totalYearsInOccupationInputElement.sendKeys(Keys.ENTER);
        Utilities.captureScreenShot(_driver);
        actions.moveToElement(totalMonthsInOccupationIdElement).click().build().perform();
        await("totalMonthInOccupationOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> totalMonthsInOccupationOptionElement.size() > 1);
        totalMonthsInOccupationInputElement.sendKeys(String.valueOf(data.getTotalMonthsInOccupation()));
        totalMonthsInOccupationInputElement.sendKeys(Keys.ENTER);
        Utilities.captureScreenShot(_driver);
    }

    public void setMajorOccupation(EmploymentDTO data) {
        if(!data.getIsMajorEmployment().isEmpty())
        {
            if(_driver.findElements(By.xpath("//*[@id='occupation_Info_Table']/tbody/tr[td/*[@id='view'][contains(text(),'" + data.getIsMajorEmployment() +"')]]/td[6]/input")).size()>0)
            {
                Utilities.captureScreenShot(_driver);
                WebElement webElement=_driver.findElement(By.xpath("//*[@id='occupation_Info_Table']/tbody/tr[td/*[@id='view'][contains(text(),'" + data.getIsMajorEmployment() +"')]]/td[6]/input"));
                webElement.click();

                Utilities.captureScreenShot(_driver);
                await("modalMajorChangeElement not displayed - Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> modalMajorChangeElement.isDisplayed());
                Utilities.captureScreenShot(_driver);
                btnMajorChangeElement.get(0).click();

                Utilities.captureScreenShot(_driver);
            }
        }
    }

    // TODO: compare original data vs element data and report if not equals
    public void validInOutData(Map<String, String> mapValue, String jsonObj) throws Exception {
        EmploymentDTO data = new ObjectMapper().readValue(jsonObj, EmploymentDTO.class);
        Utilities.checkInput(mapValue, "ApplicationInfo_EmploymentDetail_OccupationType", data.getOccupationType(), occupationTypeOptionElement);
        Utilities.checkInput(mapValue, "ApplicationInfo_EmploymentDetail_TaxCode", data.getTaxCode(), companyTaxCodeElement);
        Utilities.checkInput(mapValue, "ApplicationInfo_EmploymentDetail_NatureOfBussiness", data.getNatureOfBussiness(), natureOfBusinessOptionElement);
        Utilities.checkInput(mapValue, "ApplicationInfo_EmploymentDetail_Industry", data.getIndustry(), industryOptionElement);
        Utilities.checkInput(mapValue, "ApplicationInfo_EmploymentDetail_Department", data.getDepartment(), departmentNameElement);
        Utilities.checkInput(mapValue, "ApplicationInfo_EmploymentDetail_EmploymentStatus", data.getEmploymentStatus(), employmentStatusOptionElement);
        Utilities.checkInput(mapValue, "ApplicationInfo_EmploymentDetail_DurationYears", data.getDurationYears(), durationYearsElement);
        Utilities.checkInput(mapValue, "ApplicationInfo_EmploymentDetail_DurationMonths", data.getDurationMonths(), durationMonthsElement);
    }

}
