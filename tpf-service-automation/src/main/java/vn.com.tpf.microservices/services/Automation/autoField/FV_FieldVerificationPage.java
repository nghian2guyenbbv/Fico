package vn.com.tpf.microservices.services.Automation.autoField;

import lombok.Getter;
import org.apache.commons.lang.StringEscapeUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.models.AutoField.SubmitFieldDTO;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.is;

@Getter
public class FV_FieldVerificationPage {
    private WebDriver _driver;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]")
    private WebElement menuApplicationElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]//div[contains(@class,'one-col')][3]//li//a[contains(text(),'Application Manager')]")
    @CacheLookup
    private WebElement applicationManagerElement;

    @FindBy(how = How.ID, using = "applicationManagerForm1")
    private WebElement applicationManagerFormElement;

    @FindBy(how = How.ID, using = "appManager_lead_application_number")
    private WebElement applicationNumberElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'applicationManagerForm1')]//input[@type='button']")
    private WebElement searchApplicationElement;

    @FindBy(how = How.XPATH, using = "//table[@id='applicationTable']//tbody//tr//td")
    private List<WebElement> tdApplicationElement;

    @FindBy(how = How.ID, using = "showTasks")
    private WebElement showTaskElement;

    @FindBy(how = How.XPATH, using = "//table[@id='applicationTable']//tbody//tr//td[6]")
    private WebElement tdCheckStageApplicationElement;

    @FindBy(how = How.ID, using = "taskTableDiv")
    private WebElement taskTableDivElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'edit_button0')]//input[@type='button']")
    private WebElement editElement;

    @FindBy(how = How.ID, using = "Text_selected_user0")
    private WebElement textSelectUserElement;

    @FindBy(how = How.XPATH, using = "//a[contains(@id, 'listitem_selected_user')]")
    private List<WebElement> textSelectUserOptionElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'with_branch')]//input[@type='submit']")
    private WebElement saveTaskElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]//div[contains(@class,'one-col')][2]//li[1]//span[contains(text(),'Applications')]")
    @CacheLookup
    private WebElement applicationElement;

    @FindBy(how = How.ID, using = "LoanApplication_Assigned_wrapper")
    @CacheLookup
    private WebElement applicationFormElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id,'LoanApplication_Assigned_wrapper')]//div[contains(@id,'LoanApplication_Assigned_filter')]//input[contains(@type,'text')]")
    @CacheLookup
    private WebElement applicationAssignedNumberElement;

    @FindBy(how = How.XPATH, using = "//table[@id='LoanApplication_Assigned']//tbody//tr//td")
    @CacheLookup
    private List<WebElement> tbApplicationAssignedElement;

    @FindBy(how = How.XPATH, using = "//table[contains(@id,'fii_front')]//tbody//tr//td")
    @CacheLookup
    private List<WebElement> tbFieldInvestigationInitiationElement;

    @FindBy(how = How.XPATH, using = "//button[contains(text(), 'Initiate Verification')]")
    @CacheLookup
    private WebElement btnInitiateVerificationElement;

    @FindBy(how = How.XPATH, using = "//table[contains(@id, 'initiate_Verification')]")
    @CacheLookup
    private WebElement tbVerificationTypeElement;

    @FindBy(how = How.XPATH, using = "//table[contains(@id, 'initiate_Verification')]//select")
    @CacheLookup
    private WebElement selectVerificationTypeElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id,'initiateVerification')]//div[contains(@class,'modal-body')]//table[contains(@id, 'initiate_Verification')]//input[starts-with(@id, 'Text_field_investigation_entry_verification_agencyNO')]")
    @CacheLookup
    private WebElement inputAgencyElement;

    @FindBy(how = How.ID, using = "holder")
    private WebElement textSelectUserContainerElement;

    @FindBy(how = How.XPATH, using = "//div[starts-with(@id,'content_field_investigation_entry_verification_agencyNO')]//ul[@id = 'holder']//li[starts-with(@id, 'listitem_field_investigation_entry_verification_agencyNO0')]")
    @CacheLookup
    private List<WebElement> liAgencyElement;

    @FindBy(how = How.XPATH, using = "//a[contains(@id, 'listitem_field_investigation_entry_verification_agency')]")
    @CacheLookup
    private List<WebElement> textInputAgencyElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id,'initiateVerification')]//div[contains(@class,'modal-footer actionBtnDiv')]//button[contains(text(), 'Initiate Verification')]")
    @CacheLookup
    private WebElement btnInitiateVerificationPopupElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id, 'move_to_next_stage_div')]//button[contains(@id, 'move_to_next_stage')]")
    @CacheLookup
    private WebElement btnMoveToNextStageElement;


    public FV_FieldVerificationPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    public void setData(SubmitFieldDTO submitFieldDTO, String user, String downdloadFileURL, Instant start) {
        String stage = "";

        /*menuApplicationElement.click();

        applicationManagerElement.click();*/

        // ========== APPLICATION MANAGER =================
        stage = "APPLICATION MANAGER";

        /*await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(_driver::getTitle, is("Application Manager"));

        await("appManager_lead_application_number visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> applicationManagerFormElement.isDisplayed());

        applicationNumberElement.sendKeys(submitFieldDTO.getAppId());
        searchApplicationElement.click();

        await("tdApplicationElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tdApplicationElement.size() > 2);

        await("showTaskElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> showTaskElement.isDisplayed());

        await("Stage wrong Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> "FII".equals(tdCheckStageApplicationElement.getText()));*/

        showTaskElement.click();

        await("taskTableDivElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> taskTableDivElement.isDisplayed());

        await("editElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> editElement.isDisplayed());

        editElement.click();

        await("textSelectUserElement enable Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> textSelectUserElement.isEnabled());

        textSelectUserElement.clear();
        textSelectUserElement.sendKeys(user);
        await("textSelectUserContainerElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> textSelectUserContainerElement.isDisplayed());

        await("textSelectUserOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> textSelectUserOptionElement.size() > 0);

        for (WebElement e : textSelectUserOptionElement) {
            if (!Objects.isNull(e.getAttribute("title")) && StringEscapeUtils.unescapeJava(e.getAttribute("title")).equals(user)) {
                e.click();
                break;
            }
        }
        Utilities.captureScreenShot(_driver);
        saveTaskElement.click();
        System.out.println(stage + ": DONE");

        // ========== INITIATE VERIFICATION =================

        menuApplicationElement.click();

        applicationElement.click();

        await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(_driver::getTitle, is("Application Grid"));

        applicationAssignedNumberElement.clear();

        applicationAssignedNumberElement.sendKeys(submitFieldDTO.getAppId());

        await("Find not found AppId!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tbApplicationAssignedElement.size() > 2);

        WebElement applicationIdAssignedNumberElement =_driver.findElement(new By.ByXPath("//table[@id='LoanApplication_Assigned']//tbody//tr//td[contains(@class,'tbl-left')]//a[contains(text(),'" + submitFieldDTO.getAppId() +"')]"));

        applicationIdAssignedNumberElement.click();

        await("tbFieldInvestigationInitiationElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tbFieldInvestigationInitiationElement.size() > 2);

        btnInitiateVerificationElement.click();

        await("tbVerificationTypeElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tbVerificationTypeElement.isDisplayed());

        selectVerificationTypeElement.sendKeys("Residence Verification");

        await("inputAgencyElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> inputAgencyElement.isDisplayed());

        inputAgencyElement.sendKeys("TPF Agency");

        await("textSelectUserContainerElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> textSelectUserContainerElement.isDisplayed());

        await("liAgencyElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> liAgencyElement.size() > 0);

        for (WebElement e : liAgencyElement) {
            if (!Objects.isNull(e.getAttribute("username")) && StringEscapeUtils.unescapeJava(e.getAttribute("username")).equals("TPF Agency")) {
                e.click();
                break;
            }
        }

        btnInitiateVerificationPopupElement.click();

        await("btnMoveToNextStageElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> btnMoveToNextStageElement.isDisplayed());

        /*JavascriptExecutor jse2 = (JavascriptExecutor)_driver;
        jse2.executeScript("arguments[0].click();", btnMoveToNextStageElement);*/

        btnMoveToNextStageElement.click();

        await("Work flow failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(_driver::getTitle, is("Application Grid"));

        Utilities.captureScreenShot(_driver);

        // ========== FIELD INVESTIGATION VERIFICATION =================
        stage = "FIELD INVESTIGATION VERIFICATION";
        FV_FieldInvestigationVerificationPage fv_FieldInvestigationVerificationPage = new FV_FieldInvestigationVerificationPage(_driver);
        fv_FieldInvestigationVerificationPage.setData(submitFieldDTO, downdloadFileURL);
        System.out.println(stage + ": DONE" + " - Time " + Duration.between(start, Instant.now()).toSeconds());

    }
}
