package vn.com.tpf.microservices.services.Automation.deReturn;

import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.*;
import vn.com.tpf.microservices.models.DEReturn.DESaleQueueDTO;
import vn.com.tpf.microservices.models.DEReturn.DESaleQueueDocumentDTO;
import vn.com.tpf.microservices.services.Automation.SearchMenu;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.awaitility.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.with;
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

    @FindBy(how = How.XPATH, using = "//*[@id='LoanApplication_Assigned_wrapper']/div[1]/div/div[2]/div[1]/table/thead[1]/tr/th/input")
    @CacheLookup
    private WebElement applicationAssignedNumberElement;

    @FindBy(how = How.XPATH, using = "//table[@id='LoanApplication_Assigned']//tbody//tr//td")
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


    @FindBy(how = How.ID, using = "document_neo")
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

    @FindBy(how = How.XPATH, using = "//table[@id='applicationTable']//tbody//tr//td")
    private List<WebElement> tdApplicationElement;

    @FindBy(how = How.XPATH, using = "//table[@id='applicationTable']//tbody//tr//td[6]")
    private WebElement tdCheckStageApplicationElement;

    @FindBy(how = How.ID, using = "showTasks")
    private WebElement showTaskElement;

    @FindBy(how = How.ID, using = "taskTableDiv")
    private WebElement taskTableDivElement;

    @FindBy(how = How.XPATH, using = "//*[@id='edit_button0']/input[1]")
    private WebElement editElement;

    @FindBy(how = How.ID, using = "Text_selected_user0")
    private WebElement textSelectUserElement;

    @FindBy(how = How.ID, using = "holder")
    private WebElement textSelectUserContainerElement;

    @FindBy(how = How.XPATH, using = "//a[contains(@id,'listitem_selected_user0')]")
    private List<WebElement> textSelectUserOptionElement;

    @FindBy(how = How.XPATH, using = "//*[@id='with_branch']/input")
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
        String stage = "";
        ((RemoteWebDriver) _driver).setFileDetector(new LocalFileDetector());
        SearchMenu searchMenu = new SearchMenu(_driver);
        searchMenu.MoveToPage(Constant.MENU_NAME_LINK_APPLICATION_MANAGER);
        // ========== APPLICATION MANAGER =================
        stage = "APPLICATION MANAGER";

        await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(_driver::getTitle, is("Application Manager"));

        await("applicationManagerFormElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> applicationManagerFormElement.isDisplayed());

        applicationNumberElement.sendKeys(deSaleQueueDTO.getAppId());
        searchApplicationElement.click();

        await("Application Number Not Found!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tdApplicationElement.size() > 2);

        await("showTaskElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> showTaskElement.isDisplayed());
       System.out.println("tdCheckStageApplicationElement.getText():"+tdCheckStageApplicationElement.getText());

       /* await("Stage SALES QUEUE wrong Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> "SALES QUEUE".equals(tdCheckStageApplicationElement.getText()));*/

        await("Stage SALES QUEUE wrong Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tdCheckStageApplicationElement.getText() != null);

        if (!"SALES QUEUE".equals(tdCheckStageApplicationElement.getText())){
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

        with().pollInterval(Duration.FIVE_SECONDS).
        await("Select user visibale!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> textSelectUserContainerElement.isDisplayed());

        await("User Auto not found!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> textSelectUserOptionElement.size() > 0);

        for (WebElement e : textSelectUserOptionElement) {
            e.click();
            break;
        }
        Utilities.captureScreenShot(_driver);
        saveTaskElement.click();
        System.out.println(stage + " => DONE");
        Thread.sleep(15000);

        // ========== APPLICATION - SALE QUEUE =================
        SearchMenu searchMenuApp = new SearchMenu(_driver);
        searchMenuApp.MoveToPage(Constant.MENU_NAME_LINK_APPLICATIONS);

        stage = "ASSIGNED";

        await("Application timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(_driver::getTitle, is("Application Grid"));

        _driver.findElement(By.xpath("//*[@id='lead']/a")).click();


        await("show list data assigned Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> _driver.findElement(By.xpath("//*[@id='contentwrapper']/div/div")).isDisplayed());
        applicationAssignedNumberElement.clear();
        applicationAssignedNumberElement.sendKeys(deSaleQueueDTO.getAppId());
        applicationAssignedNumberElement.sendKeys(Keys.ENTER);
        await("tbApplicationAssignedElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tbApplicationAssignedElement.size() > 2);

        //check branch
        WebElement divDisplayBranch = _driver.findElement(By.xpath("//*[@id='displayBranch']"));
        WebElement branchUser = _driver.findElement(By.xpath("//*[@id='loggedInBranch']"));
        WebElement branchAppl = _driver.findElement(By.xpath("//*[@id='LoanApplication_Assigned']/tbody/tr/td[15]"));

        if(!branchUser.getText().equals(branchAppl.getText())){
            divDisplayBranch.click();

            WebElement divIpxBrnch = _driver.findElement(By.xpath("//*[@id='Text_headerAutoCompBranchId']"));
            await("Div input branch timeout!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> divIpxBrnch.isDisplayed());
            divIpxBrnch.clear();
            divIpxBrnch.sendKeys(branchAppl.getText());
            List<WebElement> listBranch = _driver.findElements(By.xpath("//a[contains(@id,'listitem_headerAutoCompBranchId')]"));
            await("Div input branch timeout!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> listBranch.size() > 0);
            for (WebElement e : listBranch){
                e.click();
                break;
            }
            Thread.sleep(3000);
            await("Assigned timeout!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() ->   _driver.findElement(By.xpath("//*[@id='lead']/a")).isDisplayed());

            _driver.findElement(By.xpath("//*[@id='lead']/a")).click();
            WebElement ipx = _driver.findElement(By.xpath("//*[@id='LoanApplication_Assigned_wrapper']/div[1]/div/div[2]/div[1]/table/thead[1]/tr/th/input"));
            List<WebElement> divIpx = _driver.findElements(By.xpath("//table[@id='LoanApplication_Assigned']//tbody//tr//td"));
            await("Input Application show timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> ipx.isDisplayed());
            ipx.clear();
            ipx.sendKeys(deSaleQueueDTO.getAppId());
            ipx.sendKeys(Keys.ENTER);
            with().pollInterval(Duration.FIVE_SECONDS).
                    await("Application Id Not Found!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> divIpx.size() > 2);
        }
        //check branch end


        WebElement applicationIdAssignedNumberElement = _driver.findElement(By.xpath("//*[@id='LoanApplication_Assigned_wrapper']/div[1]/div/div[2]/div[2]/div/table/tbody/tr/td/a"));

        applicationIdAssignedNumberElement.click();

        System.out.println(stage + " => DONE");

        stage = "SALE QUEUE";

        await("documentElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> documentElement.isDisplayed());

        List<String> requiredFiled = new ArrayList<>();
        if(deSaleQueueDTO.getDataDocuments().size() > 0){
            deSaleQueueDTO.getDataDocuments().stream().forEach(doc -> {
                requiredFiled.add(doc.getDocumentName());
            });
        }
        String fromFile = downLoadFileURL;
        Thread.sleep(5000);
        List<WebElement> listDocs = _driver.findElements(By.xpath("//*[contains(@data-type, 'docnode')]"));
        for (WebElement element : listDocs){
            String docName = element.getText();
            String toFile = Constant.SCREENSHOT_PRE_PATH_DOCKER;
            if (requiredFiled.contains(docName)) {
                await("Show document Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() ->  element.isDisplayed());
                element.click();
                Thread.sleep(2000);
                String finalDocName = docName;
                DESaleQueueDocumentDTO doc = deSaleQueueDTO.getDataDocuments().stream().filter(q -> q.getDocumentName().equals(finalDocName)).findAny().orElse(null);
                if(doc!=null)
                {
                    String ext = FilenameUtils.getExtension(doc.getFileName());
                    if ("TPF_Transcript".equals(docName)){
                        docName = "TPF_Tran1";
                    }
                    toFile+=UUID.randomUUID().toString()+"_"+ docName +"." + ext;
                    FileUtils.copyURLToFile(new URL(fromFile + URLEncoder.encode( doc.getFileName(), "UTF-8").replaceAll("\\+", "%20")), new File(toFile), 10000, 10000);
                    File file = new File(toFile);
                    if(file.exists()) {
                        String photoUrl = file.getAbsolutePath();
                        System.out.println("PATH:" + photoUrl);
                        Thread.sleep(2000);

                        _driver.findElement(By.xpath("//div[contains(@class,'inputBox clearfix ng-scope')]//select[@title='Status']")).click();
                        List<WebElement> lendingPhotoContainerElement  = _driver.findElements(By.xpath("//div[contains(@class,'inputBox clearfix ng-scope')]//select[@title='Status']//option"));
                        await("Load lendingPhotoContainerElement Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(() ->  lendingPhotoContainerElement.size() != 0);
                        for (WebElement e : lendingPhotoContainerElement){
                            if(e.getText().equals("Received")){
                                e.click();
                            }
                        }

                        if(!_driver.findElement(By.xpath("//li[contains(@class,'imageBox add_more_box')]")).isDisplayed()){
                            WebElement editViewDoc = _driver.findElement(By.xpath("//*[@id='dv_documentEdit']"));
                            await("Load edit ViewDoc Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                    .until(() ->  editViewDoc.isDisplayed());
                            editViewDoc.click();
                        }
                        _driver.findElement(By.xpath("//*[@id='document_viewer_mp']//input[contains(@class,'input_images')]")).sendKeys(photoUrl);

                        WebElement saveDoc = _driver.findElement(By.id("dv_documentSave"));
                        saveDoc.click();
                        await("dv_documentSave Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(() ->  !saveDoc.isDisplayed());

                        Utilities.captureScreenShot(_driver);
                    }
                }
            }
        }
        _driver.findElement(By.xpath("//*[@id='topActionBar']/button[1]")).click();
        Utilities.captureScreenShot(_driver);
        System.out.println("SAVE DOCUMENT" + " => DONE");

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
        System.out.println("ADD COMMENT" + " => DONE");

        keyActionMoveNextStage();
        await("Move next stage failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(_driver::getTitle, is("Application Grid"));
        Utilities.captureScreenShot(_driver);
        System.out.println(stage + " => DONE");
    }

    public void keyActionMoveNextStage(){
        Actions actionKey = new Actions(_driver);
        WebElement divTimeRemaining = _driver.findElement(By.xpath("//div[@id = 'heading']//div[@id = 'timer_containerappTat']"));

        actionKey.moveToElement(divTimeRemaining).perform();
        divTimeRemaining.click();
        WebElement movoToStep = _driver.findElement(By.id("move_to_next_stage"));
        JavascriptExecutor js = (JavascriptExecutor)_driver;
        js.executeScript("arguments[0].click();",movoToStep);
    }

}
