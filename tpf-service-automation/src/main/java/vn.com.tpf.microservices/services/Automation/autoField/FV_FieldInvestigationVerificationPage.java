package vn.com.tpf.microservices.services.Automation.autoField;

import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.models.AutoField.SubmitFieldAttachmentDTO;
import vn.com.tpf.microservices.models.AutoField.SubmitFieldDTO;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.is;


@Getter
public class FV_FieldInvestigationVerificationPage {
    private WebDriver _driver;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]")
    private WebElement menuApplicationElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]//div[contains(@class,'one-col')][3]//li//a[contains(text(),'FI Allocation Grid')]")
    @CacheLookup
    private WebElement fiAllocationGridElement;

    @FindBy(how = How.ID, using = "fiSearchForm")
    private WebElement fiAllocationGridFormElement;

    @FindBy(how = How.ID, using = "applicationId")
    private WebElement fiAllocationGridApplicationNumberElement;

    @FindBy(how = How.XPATH, using = "//div[@class = 'row-fluid']//button[@id = 'searchApplication']")
    private WebElement searchFIAllocationGridElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'container-no-tpl']//table[@id = 'FiSearch']//tbody//tr//td")
    @CacheLookup
    private List<WebElement> fiAllocationGridTable;

    @FindBy(how = How.XPATH, using = "//div[@id = 'container-no-tpl']//table[@id = 'FiSearch']//tbody//tr//td[20]//img[@id = 'AssignTo']")
    private WebElement assignToFIAllocationGridElement;

    @FindBy(how = How.XPATH, using = "//div[@class = 'modal-scrollable']")
    private WebElement popupFIAllocationGridElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'agents-modal']//div[@id = '-control-group']//input[contains(@class, 'agent_search_input')]")
    private WebElement inputSearchFIAllocationGridElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'agents-modal']//div[contains(@class, 'modal-body')]//table//tbody[@id = 'agent-table-tbody']//tr[not(starts-with(@style, 'display: none;'))]//td")
    @CacheLookup
    private List<WebElement> tableSearchFIAllocationGridElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'agents-modal']//div[contains(@class, 'modal-body')]//table//tbody[@id = 'agent-table-tbody']//tr[not(starts-with(@style, 'display: none;'))]//td//input")
    private WebElement selectFIAllocationGridElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'agents-modal']//div[@id = 'pill-div']//span[contains(@class, 'pill-bread')]")
    private WebElement spanSelectUserElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'agents-modal']//button[text() = 'Done']")
    private WebElement doneButtonFIAllocationGridElement;




    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]//div[contains(@class,'one-col')][3]//li//a[contains(text(),'Field Investigation Verification')]")
    @CacheLookup
    private WebElement fieldInvestigationVerificationElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'tableParentDiv']//div[@id='fieldInvestigationEntryTable_wrapper']")
    @CacheLookup
    private WebElement fieldInvestigationEntryElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'tableParentDiv']//div[@id='fieldInvestigationEntryTable_wrapper']//table[@id = 'fieldInvestigationEntryTable']//tbody//tr")
    @CacheLookup
    private List<WebElement> fieldInvestigationEntryTable;

    @FindBy(how = How.XPATH, using = "//div[@id = 'fieldInvestigationEntryTable_filter']//input[@type = 'text']")
    @CacheLookup
    private WebElement appIdSearchInput;

    @FindBy(how = How.XPATH, using = "//div[@id = 'tableParentDiv']//div[@id='fieldInvestigationEntryTable_wrapper']//table[@id ='fieldInvestigationEntryTable']//tbody//tr[1]//td[2]//a[contains(text(),'Residence Verification')]")
    @CacheLookup
    private WebElement residenceVerificationTd;

    @FindBy(how = How.XPATH, using = "//div[starts-with(@id, 'miscDynamicForm')]//div[starts-with(@id, 'loanInfo_miscDynamicForm_')]//div[@id = 'accordion_advSearch']")
    @CacheLookup
    private WebElement fieldVerificationElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'genericForm_Residence_Verification-field-form']//div[starts-with(@id, 'Result_home_visit_Residence_Verification_')]//div[@class = 'chzn-search']//input[@type = 'text']")
    @CacheLookup
    private WebElement resultHomeVisitInput;

    @FindBy(how = How.XPATH, using = "//div[@id = 'genericForm_Residence_Verification-field-form']//div[starts-with(@id, 'Result_home_visit_Residence_Verification_')]//ul[@class = 'chzn-results']")
    @CacheLookup
    private WebElement resultHomeVisitUl;

    @FindBy(how = How.XPATH, using = "//div[@id = 'genericForm_Residence_Verification-field-form']//div[starts-with(@id, 'Result_home_visit_Residence_Verification_')]//ul[@class = 'chzn-results']//li[starts-with(@id, 'Result_home_visit_Residence_Verification_1_chzn_o_')]")
    @CacheLookup
    private List<WebElement> resultHomeVisitSelect;

    @FindBy(how = How.XPATH, using = "//div[@id = 'genericForm_Residence_Verification-field-form']//div[starts-with(@id, 'Result_office_visit_Residence_Verification_')]//div[@class = 'chzn-search']//input[@type = 'text']")
    @CacheLookup
    private WebElement resultOfficeVisitInput;

    @FindBy(how = How.XPATH, using = "//div[@id = 'genericForm_Residence_Verification-field-form']//div[starts-with(@id, 'Result_office_visit_Residence_Verification_')]//ul[@class = 'chzn-results']")
    @CacheLookup
    private WebElement resultOfficeVisitUl;

    @FindBy(how = How.XPATH, using = "//div[@id = 'genericForm_Residence_Verification-field-form']//div[starts-with(@id, 'Result_office_visit_Residence_Verification_')]//ul[@class = 'chzn-results']//li[starts-with(@id, 'Result_office_visit_Residence_Verification_2_chzn_o_')]")
    @CacheLookup
    private List<WebElement> resultOfficeVisitSelect;

    @FindBy(how = How.XPATH, using = "//div[@id = 'genericForm_Residence_Verification-field-form']//div[starts-with(@id, 'Result_home_visit_2_Residence_Verification_')]//div[@class = 'chzn-search']//input[@type = 'text']")
    @CacheLookup
    private WebElement resultHomeVisit2Input;

    @FindBy(how = How.XPATH, using = "//div[@id = 'genericForm_Residence_Verification-field-form']//div[starts-with(@id, 'Result_home_visit_2_Residence_Verification_')]//ul[@class = 'chzn-results']")
    @CacheLookup
    private WebElement resultHomeVisit2Ul;

    @FindBy(how = How.XPATH, using = "//div[@id = 'genericForm_Residence_Verification-field-form']//div[starts-with(@id, 'Result_home_visit_2_Residence_Verification_')]//ul[@class = 'chzn-results']//li[starts-with(@id, 'Result_home_visit_2_Residence_Verification_3_chzn_o_')]")
    @CacheLookup
    private List<WebElement> resultHomeVisit2Select;

    @FindBy(how = How.XPATH, using = "//a[@id = 'addAttachment']")
    @CacheLookup
    private WebElement attachmentsButtonUploadA;

    @FindBy(how = How.XPATH, using = "//div[@id = 'noOfAttempts-control-group']//input[@id = 'noOfAttempts']")
    @CacheLookup
    private WebElement noOfAttemptsInput;

    @FindBy(how = How.XPATH, using = "//form[@id = 'field_investigation_entry']//div[1]//div[2]//div[@id = 'field_investigation_entry_verification_agent-control-group']//div[@id = 'field_investigation_entry_verification_agent_chzn']//input[@type = 'text']")
    @CacheLookup
    private WebElement verificationAgentInputElement;

    @FindBy(how = How.XPATH, using = "//div[@id='field_investigation_entry_verification_agent_chzn']//ul[@class='chzn-results']")
    @CacheLookup
    private WebElement verificationAgentUlElement;

    @FindBy(how = How.XPATH, using = "//li[starts-with(@id, 'field_investigation_entry_verification_agent_chzn_o_')]")
    @CacheLookup
    private List<WebElement> verificationAgentLiElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'field_investigation_entry_verification_agent_chzn']//li[starts-with(@class, 'active-result')]")
    @CacheLookup
    private List<WebElement> verificationAgentLiAndElement;

    @FindBy(how = How.XPATH, using = "//select[@id = 'field_investigation_entry_vo_verificationResult']")
    @CacheLookup
    private WebElement resultDescriptionSelect;

    @FindBy(how = How.XPATH, using = "//div[@id = 'field_investigation_entry_remarks-control-group']//textarea[@id = 'field_investigation_entry_remarks']")
    @CacheLookup
    private WebElement remarksDescriptionTextArea;

    @FindBy(how = How.XPATH, using = "//div[@class = 'txt-r']//input[@id = 'move_to_next_stage']")
    @CacheLookup
    private WebElement buttonSaveAndProceed;

    @FindBy(how = How.XPATH, using = "//input[@name = 'timeOfVisit']")
    @CacheLookup
    private WebElement timeOfVisitElement;

    @FindBy(how = How.XPATH, using = "//input[@id = 'field_investigation_entry_fDate']")
    @CacheLookup
    private WebElement verificationDateElement;

    public FV_FieldInvestigationVerificationPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    @SneakyThrows
    public void setData(SubmitFieldDTO submitFieldDTO, String downLoadFileURL, String accountAuto) {
        String stage = "";
        ((RemoteWebDriver) _driver).setFileDetector(new LocalFileDetector());

        stage = "FI ALLOCATION GRID";
        menuApplicationElement.click();

        fiAllocationGridElement.click();

        await("FI Entries Grid timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(_driver::getTitle, is("FI Entries Grid"));

        await("appManager_lead_application_number visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> fiAllocationGridFormElement.isDisplayed());

        fiAllocationGridApplicationNumberElement.sendKeys(submitFieldDTO.getAppId());
        searchFIAllocationGridElement.click();

        await("Application FIAllocationGrid not found!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> fiAllocationGridTable.size() > 2);

        assignToFIAllocationGridElement.click();

        await("Popup FIAllocationGrid visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> popupFIAllocationGridElement.isDisplayed());

        inputSearchFIAllocationGridElement.sendKeys(accountAuto);

        await("Account not found!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tableSearchFIAllocationGridElement.size() > 2);

        selectFIAllocationGridElement.click();

        await("Account not found!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> spanSelectUserElement.isDisplayed());

//        doneButtonFIAllocationGridElement.click();

        JavascriptExecutor jseBTNDone = (JavascriptExecutor)_driver;
        jseBTNDone.executeScript("arguments[0].click();", doneButtonFIAllocationGridElement);

        await("FI Entries Grid done timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(_driver::getTitle, is("FI Entries Grid"));

        menuApplicationElement.click();

        fieldInvestigationVerificationElement.click();
        await("fieldInvestigationEntryTable visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> fieldInvestigationEntryElement.isDisplayed());

        appIdSearchInput.clear();
        appIdSearchInput.sendKeys(submitFieldDTO.getAppId());

        if(fieldInvestigationEntryTable.size()!=0) {
            List<WebElement> residenceVerificationTds =_driver.findElements(new By.ByXPath("//table[@id='fieldInvestigationEntryTable']//tbody//tr//td[3][contains(text(),'" + submitFieldDTO.getAppId() + "')]"));
            await("Find not found AppId!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> residenceVerificationTds.size() > 0);
            if (residenceVerificationTds.size()!=0){
                residenceVerificationTd.click();
            }
        }

        await("fieldVerificationElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> fieldVerificationElement.isDisplayed());

        WebElement radioPhoneConfirmedElement = _driver.findElement(new By.ByXPath("//div[@id = 'genericForm_Residence_Verification-field-form']//input[@value = '" + submitFieldDTO.getPhoneConfirmed() + "']"));
        radioPhoneConfirmedElement.click();

        resultHomeVisitInput.sendKeys(submitFieldDTO.getResultHomeVisit());

        await("resultHomeVisitUl displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> resultHomeVisitUl.isDisplayed());

        await("resultHomeVisitSelect loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> resultHomeVisitSelect.size() > 0);

        for (WebElement e : resultHomeVisitSelect) {
            if (!Objects.isNull(e.getText()) && StringEscapeUtils.unescapeJava(e.getText()).equals(submitFieldDTO.getResultHomeVisit())) {
                e.click();
                break;
            }
        }

        resultOfficeVisitInput.sendKeys(submitFieldDTO.getResultOfficeVisit());

        await("resultOfficeVisitUl displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> resultOfficeVisitUl.isDisplayed());

        await("resultOfficeVisitSelect loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> resultOfficeVisitSelect.size() > 0);

        for (WebElement e : resultOfficeVisitSelect) {
            if (!Objects.isNull(e.getText()) && StringEscapeUtils.unescapeJava(e.getText()).equals(submitFieldDTO.getResultOfficeVisit())) {
                e.click();
                break;
            }
        }

        resultHomeVisit2Input.sendKeys(submitFieldDTO.getResult2ndHomeVisit());

        await("resultHomeVisit2Ul displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> resultHomeVisit2Ul.isDisplayed());

        await("resultHomeVisit2Select loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> resultHomeVisit2Select.size() > 0);

        for (WebElement e : resultHomeVisit2Select) {
            if (!Objects.isNull(e.getText()) && StringEscapeUtils.unescapeJava(e.getText()).equals(submitFieldDTO.getResult2ndHomeVisit())) {
                e.click();
                break;
            }
        }

        //====================================
        System.setProperty("java.awt.headless", "false");
        int i = 0;
        stage = "UPLOAD FILE";
        System.out.println(stage + ": START");
        for (SubmitFieldAttachmentDTO attachmentFileList : submitFieldDTO.getAttachmentField()) {
            attachmentsButtonUploadA.click();

            Thread.sleep(1000);
            Robot robotObj = new Robot();
            this.pressEscapeKey(robotObj);
            Thread.sleep(3000);

            WebElement attachmentsButtonUpload = _driver.findElement(new By.ByXPath("//div[@id = 'fileupload']//input[@id = 'single_file_" + i + "']"));

            String fromFile = downLoadFileURL;
            System.out.println("URLdownload: " + fromFile);
            String docName = attachmentFileList.getFileName();
            String toFile = Constant.SCREENSHOT_PRE_PATH_DOCKER;
            toFile += UUID.randomUUID().toString() + "_" + docName;


            FileUtils.copyURLToFile(new URL(fromFile + URLEncoder.encode(docName, "UTF-8").replaceAll("\\+", "%20")), new File(toFile), 10000, 10000);
            File file = new File(toFile);

            if (file.exists()) {
                String docUrl = file.getAbsolutePath();
                System.out.println("paht;" + docUrl);
                Thread.sleep(2000);

                attachmentsButtonUpload.sendKeys(docUrl);

                Utilities.captureScreenShot(_driver);
            }

            i++;

        }
        System.out.println(stage + ": DONE");
        //========================

//        noOfAttemptsInput.sendKeys(fieldList.getNoOfAttempts());
        noOfAttemptsInput.sendKeys("1");

        if (StringUtils.isNotEmpty(submitFieldDTO.getVerificationAgent())){
            verificationAgentInputElement.sendKeys(submitFieldDTO.getVerificationAgent());
//            verificationAgentInputElement.sendKeys("TPF Agent");
            await("verificationAgentUlElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> verificationAgentUlElement.isDisplayed());

            await("verificationAgentLiElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> verificationAgentLiElement.size() > 0);

            await("Find Verification Agent not found").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> verificationAgentLiAndElement.size() > 0);

            for (WebElement e : verificationAgentLiElement) {
//                if (!Objects.isNull(e.getText()) && StringEscapeUtils.unescapeJava(e.getText()).equals("TPF Agent")) {
                if (!Objects.isNull(e.getText()) && StringEscapeUtils.unescapeJava(e.getText()).equals(submitFieldDTO.getVerificationAgent())) {
                    e.click();
                    break;
                }
            }
        }
//

//        resultDescriptionSelect.sendKeys(fieldList.getResultDecisionFiv());
        resultDescriptionSelect.sendKeys("Positive");

//        if ("PRE-APPROVED".equals(fieldList.getStatusField())){
        if(!Objects.isNull(submitFieldDTO.getRemarksDecisionFiv())){
            remarksDescriptionTextArea.sendKeys(submitFieldDTO.getRemarksDecisionFiv());
        }
        timeOfVisitElement.clear();
        timeOfVisitElement.sendKeys(submitFieldDTO.getTimeOfVisit());
        verificationDateElement.clear();
        verificationDateElement.sendKeys(submitFieldDTO.getVerificationDate());
//        }

        buttonSaveAndProceed.click();

        Utilities.captureScreenShot(_driver);
    }

    private void pressEscapeKey(Robot robot) throws InterruptedException {
        robot.keyPress(KeyEvent.VK_ESCAPE);
        robot.keyRelease(KeyEvent.VK_ESCAPE);
        Thread.sleep(1000);
    }
}
