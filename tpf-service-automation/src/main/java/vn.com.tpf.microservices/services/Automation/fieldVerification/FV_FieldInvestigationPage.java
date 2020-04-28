package vn.com.tpf.microservices.services.Automation.fieldVerification;

import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.*;

import static org.awaitility.Awaitility.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

import vn.com.tpf.microservices.models.FieldVerification.FieldInvestigationAttachmentDTO;
import vn.com.tpf.microservices.models.FieldVerification.FieldInvestigationDTO;
import vn.com.tpf.microservices.utilities.*;


@Getter
public class FV_FieldInvestigationPage {
    private WebDriver _driver;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]")
    private WebElement menuApplicationElement;

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

    public FV_FieldInvestigationPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    @SneakyThrows
    public void setData(FieldInvestigationDTO fieldList, String downLoadFileURL) {
        String stage = "";
        ((RemoteWebDriver) _driver).setFileDetector(new LocalFileDetector());

        menuApplicationElement.click();
        fieldInvestigationVerificationElement.click();
        await("fieldInvestigationEntryTable visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> fieldInvestigationEntryElement.isDisplayed());

        appIdSearchInput.clear();
        appIdSearchInput.sendKeys(fieldList.getAppId());

        if(fieldInvestigationEntryTable.size()!=0) {
            List<WebElement> residenceVerificationTds =_driver.findElements(new By.ByXPath("//table[@id='fieldInvestigationEntryTable']//tbody//tr//td[3][contains(text(),'" + fieldList.getAppId() + "')]"));
            await("Find not found AppId!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> residenceVerificationTds.size() > 0);
            if (residenceVerificationTds.size()!=0){
                residenceVerificationTd.click();
            }
        }

        await("fieldVerificationElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> fieldVerificationElement.isDisplayed());

        WebElement radioPhoneConfirmedElement = _driver.findElement(new By.ByXPath("//div[@id = 'genericForm_Residence_Verification-field-form']//input[@value = '" + fieldList.getPhoneConfirmed() + "']"));
        radioPhoneConfirmedElement.click();

        resultHomeVisitInput.sendKeys(fieldList.getResultHomeVisit());

        await("resultHomeVisitUl displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> resultHomeVisitUl.isDisplayed());

        await("resultHomeVisitSelect loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> resultHomeVisitSelect.size() > 0);

        for (WebElement e : resultHomeVisitSelect) {
            if (!Objects.isNull(e.getText()) && StringEscapeUtils.unescapeJava(e.getText()).equals(fieldList.getResultHomeVisit())) {
                e.click();
                break;
            }
        }

        resultOfficeVisitInput.sendKeys(fieldList.getResultOfficeVisit());

        await("resultOfficeVisitUl displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> resultOfficeVisitUl.isDisplayed());

        await("resultOfficeVisitSelect loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> resultOfficeVisitSelect.size() > 0);

        for (WebElement e : resultOfficeVisitSelect) {
            if (!Objects.isNull(e.getText()) && StringEscapeUtils.unescapeJava(e.getText()).equals(fieldList.getResultOfficeVisit())) {
                e.click();
                break;
            }
        }

        resultHomeVisit2Input.sendKeys(fieldList.getResult2ndHomeVisit());

        await("resultHomeVisit2Ul displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> resultHomeVisit2Ul.isDisplayed());

        await("resultHomeVisit2Select loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> resultHomeVisit2Select.size() > 0);

        for (WebElement e : resultHomeVisit2Select) {
            if (!Objects.isNull(e.getText()) && StringEscapeUtils.unescapeJava(e.getText()).equals(fieldList.getResult2ndHomeVisit())) {
                e.click();
                break;
            }
        }

        //====================================
        System.setProperty("java.awt.headless", "false");
        int i = 0;
        stage = "UPLOAD FILE";
        System.out.println(stage + ": START");
        for (FieldInvestigationAttachmentDTO attachmentFileList : fieldList.getAttachmentField()) {
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

//        verificationAgentInputElement.sendKeys(fieldList.getVerificationAgent());
        verificationAgentInputElement.sendKeys("TPF Agent");

        await("verificationAgentUlElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> verificationAgentUlElement.isDisplayed());

        await("verificationAgentLiElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> verificationAgentLiElement.size() > 0);

        await("Find Verification Agent not found").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> verificationAgentLiAndElement.size() > 0);

        for (WebElement e : verificationAgentLiElement) {
            if (!Objects.isNull(e.getText()) && StringEscapeUtils.unescapeJava(e.getText()).equals(fieldList.getVerificationAgent())) {
                e.click();
                break;
            }
        }

//        resultDescriptionSelect.sendKeys(fieldList.getResultDecisionFiv());
        resultDescriptionSelect.sendKeys("Positive");

//        if ("PRE-APPROVED".equals(fieldList.getStatusField())){
            if(!Objects.isNull(fieldList.getRemarksDecisionFiv())){
                remarksDescriptionTextArea.sendKeys(fieldList.getRemarksDecisionFiv());
            }
            timeOfVisitElement.sendKeys(fieldList.getTimeOfVisit());
            verificationDateElement.sendKeys(fieldList.getVerificationDate());
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
