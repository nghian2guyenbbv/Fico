package vn.com.tpf.microservices.services.Automation.deReturn;

import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.*;
import vn.com.tpf.microservices.models.DEReturn.DESaleQueueDTO;
import vn.com.tpf.microservices.models.DEReturn.DESaleQueueDocumentDTO;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.is;

@Getter
public class DE_ReturnSaleQueuePage {
    private WebDriver _driver;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]//div[contains(@class,'one-col')][2]//li[1]//span[contains(text(),'Applications')]")
    @CacheLookup
    private WebElement applicationElement;

    @FindBy(how = How.ID, using = "LoanApplication_Assigned_wrapper")
    @CacheLookup
    private WebElement applicationFormElement;

    @FindBy(how = How.XPATH, using = "//ul[@id='mainChildTabs']//a[contains(text(),'Pool')]")
    @CacheLookup
    private WebElement poolElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id,'LoanApplication_Pool_wrapper')]")
    @CacheLookup
    private WebElement applicationDivPoolElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id,'LoanApplication_Pool_wrapper')]//input[contains(@class,'search-query')]")
    @CacheLookup
    private WebElement applicationPoolNumberElement;

    @FindBy(how = How.XPATH, using = "//table[@id='LoanApplication_Pool']//tbody//tr//td")
    @CacheLookup
    private List<WebElement> tbApplicationPoolElement;

    @FindBy(how = How.ID, using = "LoanApplication_Assigned_wrapper")
    @CacheLookup
    private WebElement applicationDivAssignedElement;

    @FindBy(how = How.XPATH, using = "//ul[@id='mainChildTabs']//a[contains(text(),'Assigned')]")
    @CacheLookup
    private WebElement assignedElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]//div[contains(@class,'one-col')][3]//li//a[contains(text(),'Application Manager')]")
    @CacheLookup
    private WebElement applicationManagerElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]")
    private WebElement menuApplicationElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id,'LoanApplication_Assigned_wrapper')]//div[contains(@id,'LoanApplication_Assigned_filter')]//input[contains(@type,'text')]")
    @CacheLookup
    private WebElement applicationAssignedNumberElement;

    @FindBy(how = How.XPATH, using = "//table[@id='LoanApplication_Assigned']//tbody//tr")
    @CacheLookup
    private List<WebElement> tbApplicationAssignedElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id, 'casDiv')]")
    @CacheLookup
    private WebElement applicationInformtionElement;

    @FindBy(how = How.ID, using = "applicationChildTabs")
    @CacheLookup
    private WebElement appChildTabsElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id,'casDiv')]//div[contains(@class,'tabbable application-main-tab multipleTabs')]//div[contains(@class,'sticky-info-class skip-print')]//ul//li[contains(@id,'applicationChildTabs_document')]//a[contains(text(),'Documents')]")
    @CacheLookup
    private WebElement applicationBtnDocumentElement;


    @FindBy(how = How.ID, using = "document")
    @CacheLookup
    private WebElement documentElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id,'lendingDocumentsTable_wrapper')]//input[contains(@type, 'text')]")
//    @CacheLookup
    private WebElement documentNameElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id,'lendingDocumentsTable_wrapper')]//table[contains(@id,'lendingDocumentsTable')]//tbody//tr//td[4]//select")
//    @CacheLookup
    private WebElement documentStatusElement;

    @FindBy(how = How.XPATH, using = "//table[@class='table table-bordered no-border']")
//    @CacheLookup
    private WebElement documentUploadElement;

    @FindBy(how = How.XPATH, using = "//table[contains(@class, 'table table-striped table-bordered')]//input[contains(@type, 'file')]")
//    @CacheLookup
    private WebElement documentBtnUploadElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id,'lendingDocumentsTable_wrapper')]//table[contains(@id,'lendingDocumentsTable')]//tbody[@id = 'lendingDocumentList']//tr")
    private List<WebElement> documentTableElement;

    @FindBy(how = How.XPATH, using = "//input[@id='executeDocumentIntegrationSet']")
    @CacheLookup
    private WebElement btnGetDocumentElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@class, 'txt-r actionBtnDiv')]//input[contains(@value, 'Save')]")
    @CacheLookup
    private WebElement documentBtnSaveElement;

    @FindBy(how = How.ID, using = "document_table_body_container")
    @CacheLookup
    private WebElement documentTableBodyElement;

    @FindBy(how = How.ID, using = "lendingDocumentsTable_wrapper")
    @CacheLookup
    private WebElement documentTbDocumentsElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id, 'activityDiv')]//a[contains(@id, 'loadApplicantInfo')]")
    @CacheLookup
    private WebElement documentLoadActivityElement;

    @FindBy(how = How.ID, using = "comment_button")
    @CacheLookup
    private WebElement documentBtnCommentElement;

    @FindBy(how = How.ID, using = "comment_textarea")
    @CacheLookup
    private WebElement documentTextCommentElement;

    @FindBy(how = How.ID, using = "comment_add_button")
    @CacheLookup
    private WebElement documentBtnAddCommnetElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id, 'move_to_next_stage_div')]//button[contains(@id, 'move_to_next_stage')]")
    @CacheLookup
    private WebElement btnMoveToNextStageElement;

    @FindBy(how = How.ID, using = "applicationManagerForm1")
    private WebElement applicationManagerFormElement;

    @FindBy(how = How.ID, using = "appManager_lead_application_number")
    private WebElement applicationNumberElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'applicationManagerForm1')]//input[@type='button']")
    private WebElement searchApplicationElement;

    @FindBy(how = How.XPATH, using = "//table[@id='applicationTable']//tbody//tr")
    private List<WebElement> tdApplicationElement;

    @FindBy(how = How.XPATH, using = "//table[@id='applicationTable']//tbody//tr//td[6]")
    private WebElement tdCheckStageApplicationElement;

    @FindBy(how = How.ID, using = "showTasks")
    private WebElement showTaskElement;

    @FindBy(how = How.ID, using = "taskTableDiv")
    private WebElement taskTableDivElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'edit_button0')]//input[@type='button']")
    private WebElement editElement;

    @FindBy(how = How.ID, using = "Text_selected_user0")
    private WebElement textSelectUserElement;

    @FindBy(how = How.ID, using = "holder")
    private WebElement textSelectUserContainerElement;

    @FindBy(how = How.XPATH, using = "//a[contains(@id, 'listitem_selected_user')]")
    private List<WebElement> textSelectUserOptionElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'with_branch')]//input[@type='submit']")
    private WebElement saveTaskElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'backSaveBtns')]//input[@type='button']")
    private WebElement backBtnElement;

    @FindBy(how = How.XPATH, using = "//table[@id='applicationTable']//tbody//tr[1]//td[1]")
    private WebElement applicationTableAppIDElement;


    public DE_ReturnSaleQueuePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    @SneakyThrows
    public void setData(DESaleQueueDTO deSaleQueueDTO, String user, String downLoadFileURL) {
        Instant start = Instant.now();
        String stage = "";
        ((RemoteWebDriver) _driver).setFileDetector(new LocalFileDetector());

        menuApplicationElement.click();

        applicationManagerElement.click();

        // ========== APPLICATION MANAGER =================
        stage = "APPLICATION MANAGER";

        await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(_driver::getTitle, is("Application Manager"));

        await("appManager_lead_application_number visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> applicationManagerFormElement.isDisplayed());

        applicationNumberElement.sendKeys(deSaleQueueDTO.getAppId());
        searchApplicationElement.click();

        await("Application Number Not Found!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tdApplicationElement.size() > 0);

        await("showTaskElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> showTaskElement.isDisplayed());

        if (!"SALES_QUEUE".equals(tdCheckStageApplicationElement.getText())){
            return;
        }

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
        System.out.println(stage + " => DONE" + " - TIME" + Duration.between(start, Instant.now()).toSeconds());


        // ========== APPLICATION - SALE QUEUE =================
        menuApplicationElement.click();

        applicationElement.click();

        stage = "ASSIGNED";

        await("Application timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(_driver::getTitle, is("Application Grid"));

        applicationAssignedNumberElement.clear();

        applicationAssignedNumberElement.sendKeys(deSaleQueueDTO.getAppId());

        await("tbApplicationAssignedElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tbApplicationAssignedElement.size() > 0);

        WebElement applicationIdAssignedNumberElement = _driver.findElement(new By.ByXPath("//table[@id='LoanApplication_Assigned']//tbody//tr//td[contains(@class,'tbl-left')]//a[contains(text(),'" + deSaleQueueDTO.getAppId() + "')]"));

        applicationIdAssignedNumberElement.click();

        System.out.println(stage + " => DONE" + " - TIME" + Duration.between(start, Instant.now()).toSeconds());

        stage = "SALE QUEUE";
        await("appChildTabsElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> appChildTabsElement.isDisplayed());

        applicationBtnDocumentElement.click();

        await("documentElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> documentElement.isDisplayed());

        if(documentTableElement.size() == 0){
            await("btnGetDocumentElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> btnGetDocumentElement.isDisplayed());

            btnGetDocumentElement.click();
        }

        await("documentTableElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> documentTableElement.size() > 0);

        System.out.println("LOAD TABLE DOCUMENT" + " => DONE" + " - TIME" + Duration.between(start, Instant.now()).toSeconds());

        for (DESaleQueueDocumentDTO documentList : deSaleQueueDTO.getDataDocuments()) {

            WebElement documentStatusElement2 = _driver.findElement(new By.ByXPath("//div[contains(@id,'lendingDocumentsTable_wrapper')]//table[contains(@id,'lendingDocumentsTable')]//tbody[@id = 'lendingDocumentList']//tr[contains(@data-documentcode,'" + documentList.getDocumentName() + "')]//select[starts-with(@id,'applicationDocument_receiveState')]"));

            documentStatusElement2.sendKeys("received");

            WebElement documentBtnUploadElement2 = _driver.findElement(new By.ByXPath("//div[contains(@id,'lendingDocumentsTable_wrapper')]//table[contains(@id,'lendingDocumentsTable')]//tbody[@id = 'lendingDocumentList']//tr[contains(@data-documentcode,'" + documentList.getDocumentName() + "')]//table[contains(@class, 'table table-striped table-bordered')]//input[contains(@type, 'file')]"));

            String fromFile = downLoadFileURL;
            System.out.println("URLdownload: " + fromFile);
            String docName = documentList.getFileName();
            String toFile = Constant.SCREENSHOT_PRE_PATH_DOCKER;
            toFile += UUID.randomUUID().toString() + "_" + docName;

            FileUtils.copyURLToFile(new URL(fromFile + URLEncoder.encode(docName, "UTF-8").replaceAll("\\+", "%20")), new File(toFile), 10000, 10000);
            File file = new File(toFile);
            if (file.exists()) {
                String docUrl = file.getAbsolutePath();
                System.out.println("PATH:" + docUrl);
                Thread.sleep(2000);

                documentBtnUploadElement2.sendKeys(docUrl);
                Utilities.captureScreenShot(_driver);
            }
        }

        Utilities.captureScreenShot(_driver);
        System.out.println("UPLOAD FILE" + " => DONE" + " - TIME" + Duration.between(start, Instant.now()).toSeconds());

        Utilities.captureScreenShot(_driver);

        documentBtnSaveElement.click();

        Utilities.captureScreenShot(_driver);

        await("documentElement visibale Timeout!").atMost(Constant.TIME_OUT_5_M, TimeUnit.SECONDS)
                .until(() -> documentElement.isDisplayed());

//        await("document_table_body visibale Timeout!").atMost(Constant.ACCOUNT_AVAILABLE_TIMEOUT, TimeUnit.SECONDS)
//                .until(() -> documentTableElement.size() > 2);

        System.out.println("SAVE DOCUMENT" + " => DONE" + " - TIME" + Duration.between(start, Instant.now()).toSeconds());

        Utilities.captureScreenShot(_driver);

        documentLoadActivityElement.click();

        await("documentBtnCommentElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> documentBtnCommentElement.isDisplayed());

        Utilities.captureScreenShot(_driver);

        documentBtnCommentElement.click();

        Utilities.captureScreenShot(_driver);

        await("document_text_comment visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> documentTextCommentElement.isDisplayed());

        documentTextCommentElement.sendKeys(deSaleQueueDTO.getCommentText());

        documentBtnAddCommnetElement.click();

        Utilities.captureScreenShot(_driver);

        System.out.println("ADD COMMENT" + " => DONE" + " - TIME" + Duration.between(start, Instant.now()).toSeconds());

        await("btnMoveToNextStageElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> btnMoveToNextStageElement.isDisplayed());


        // ==== Open headless ==== //
//        JavascriptExecutor jse2 = (JavascriptExecutor) _driver;
//        jse2.executeScript("arguments[0].click();", btnMoveToNextStageElement);

        btnMoveToNextStageElement.click();

        Utilities.captureScreenShot(_driver);

        await("Work flow failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(_driver::getTitle, is("Application Grid"));

        System.out.println(stage + " => DONE" + " - TIME" + Duration.between(start, Instant.now()).toSeconds());
    }

}
