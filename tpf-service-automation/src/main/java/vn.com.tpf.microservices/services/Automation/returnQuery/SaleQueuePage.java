package vn.com.tpf.microservices.services.Automation.returnQuery;

import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.awaitility.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.models.AutoReturnQuery.SaleQueueDTO;
import vn.com.tpf.microservices.models.AutoReturnQuery.SaleQueueDocumentDTO;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.with;
import static org.hamcrest.Matchers.is;

@Getter
public class SaleQueuePage {
    private WebDriver _driver;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]")
    @CacheLookup
    private WebElement menuApplicationElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]//div[contains(@class,'one-col')][2]//li[1]//span[contains(text(),'Applications')]")
    @CacheLookup
    private WebElement applicationElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id,'LoanApplication_Assigned_wrapper')]//div[contains(@id,'LoanApplication_Assigned_filter')]//input[contains(@type,'text')]")
    @CacheLookup
    private WebElement applicationAssignedNumberElement;

    @FindBy(how = How.XPATH, using = "//table[@id='LoanApplication_Assigned']//tbody//tr//td")
    @CacheLookup
    private List<WebElement> tbApplicationAssignedElement;

    @FindBy(how = How.XPATH, using = "//li[@id='applicationChildTabs_document']")
    private WebElement loadTabDocumentElement;

    @FindBy(how = How.XPATH, using = "//li[@id='applicationChildTabs_document']//a[@id = 'document_tab']")
    @CacheLookup
    private WebElement tabDocumentDetailsElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'lendingDocumentsTable_filter']//input")
    private WebElement textSearchDocumentElement;

    @FindBy(how = How.XPATH, using = "//input[@id='executeDocumentIntegrationSet']")
    @CacheLookup
    private WebElement btnGetDocumentElement;

    @FindBy(how = How.XPATH, using = "//table[contains(@id,'lendingDocumentsTable')]//tbody[@id = 'lendingDocumentList']//tr//td")
    private List<WebElement> documentTableElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@class, 'txt-r actionBtnDiv')]//input[contains(@value, 'Save')]")
    @CacheLookup
    private WebElement documentBtnSaveElement;

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

    @FindBy(how = How.XPATH, using = "//table[@id='LoanApplication_Assigned']")
    @CacheLookup
    private List<WebElement> checkButtonMoveNextStageElement;


    public SaleQueuePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    public void setData(SaleQueueDTO saleQueueDTO, String downLoadFileURL) throws IOException, InterruptedException {
        ((RemoteWebDriver) _driver).setFileDetector(new LocalFileDetector());

        applicationAssignedNumberElement.clear();

        applicationAssignedNumberElement.sendKeys(saleQueueDTO.getAppId());

        with().pollInterval(Duration.FIVE_SECONDS).
        await("Application Id Not Found!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tbApplicationAssignedElement.size() > 2);

        WebElement applicationIdAssignedNumberElement = _driver.findElement(new By.ByXPath("//table[@id='LoanApplication_Assigned']//tbody//tr//td[contains(@class,'tbl-left')]//a[contains(text(),'" + saleQueueDTO.getAppId() + "')]"));

        applicationIdAssignedNumberElement.click();

        with().pollInterval(Duration.FIVE_SECONDS).
        await("Tab Document loading Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> loadTabDocumentElement.isDisplayed());

        await("Tab Document loading Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tabDocumentDetailsElement.isDisplayed());

        tabDocumentDetailsElement.click();

        with().pollInterval(Duration.FIVE_SECONDS).
        await("documentElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> textSearchDocumentElement.isDisplayed());

        await("btnGetDocumentElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> btnGetDocumentElement.isDisplayed());

        btnGetDocumentElement.click();

        Thread.sleep(60000);

        await("documentElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> textSearchDocumentElement.isDisplayed());

        int listDocTableElemnet = documentTableElement.size();

        await("Document Table loading Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> listDocTableElemnet > 2);

        System.out.println("LOAD TABLE DOCUMENT" + " => DONE");

        for (SaleQueueDocumentDTO documentList : saleQueueDTO.getDataDocuments()) {

            WebElement documentStatusElement2 = _driver.findElement(new By.ByXPath("//div[contains(@id,'lendingDocumentsTable_wrapper')]//table[contains(@id,'lendingDocumentsTable')]//tbody[@id = 'lendingDocumentList']//tr[contains(@data-documentcode,'" + documentList.getDocumentName() + "')]//select[starts-with(@id,'applicationDocument_receiveState')]"));

            documentStatusElement2.sendKeys("received");

            WebElement documentBtnUploadElement2 = _driver.findElement(new By.ByXPath("//div[contains(@id,'lendingDocumentsTable_wrapper')]//table[contains(@id,'lendingDocumentsTable')]//tbody[@id = 'lendingDocumentList']//tr[contains(@data-documentcode,'" + documentList.getDocumentName() + "')]//table[contains(@class, 'table table-striped table-bordered')]//input[contains(@type, 'file')]"));

            String fromFile = downLoadFileURL;
            System.out.println("URLdownload: " + fromFile);
            String docName = documentList.getFileName();
            String toFile = Constant.SCREENSHOT_PRE_PATH_DOCKER;

            if ("TPF_Transcript".equals(docName)){
                docName = "TPF_Tran1";
            }

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
        System.out.println("UPLOAD FILE" + " => DONE");

        Utilities.captureScreenShot(_driver);

        documentBtnSaveElement.click();

        Utilities.captureScreenShot(_driver);

        await("documentElement visibale Timeout!").atMost(Constant.TIME_OUT_5_M, TimeUnit.SECONDS)
                .until(() -> textSearchDocumentElement.isDisplayed());

        System.out.println("SAVE DOCUMENT" + " => DONE");

        Utilities.captureScreenShot(_driver);

        documentLoadActivityElement.click();

        await("documentBtnCommentElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> documentBtnCommentElement.isDisplayed());

        Utilities.captureScreenShot(_driver);

        documentBtnCommentElement.click();

        Utilities.captureScreenShot(_driver);

        await("Document Text Comment visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> documentTextCommentElement.isDisplayed());

        documentTextCommentElement.sendKeys(saleQueueDTO.getCommentText());

        documentBtnAddCommnetElement.click();

        Utilities.captureScreenShot(_driver);

        System.out.println("ADD COMMENT" + " => DONE");

        await("btnMoveToNextStageElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> btnMoveToNextStageElement.isDisplayed());

        keyActionMoveNextStage();

        Utilities.captureScreenShot(_driver);
    }

    public void keyActionMoveNextStage(){
        Actions actionKey = new Actions(_driver);
        WebElement divTimeRemaining = _driver.findElement(By.xpath("//div[@id = 'heading']//div[@id = 'timer_containerappTat']"));

        actionKey.moveToElement(divTimeRemaining).click();
        actionKey.sendKeys(Keys.TAB).build().perform();
        actionKey.sendKeys(Keys.TAB).build().perform();
        actionKey.sendKeys(Keys.ENTER).build().perform();
    }
}
