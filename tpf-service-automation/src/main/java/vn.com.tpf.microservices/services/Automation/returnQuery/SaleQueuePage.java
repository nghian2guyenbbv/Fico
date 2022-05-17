package vn.com.tpf.microservices.services.Automation.returnQuery;

import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.awaitility.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.models.AutoReturnQuery.SaleQueueDTO;
import vn.com.tpf.microservices.models.AutoReturnQuery.SaleQueueDocumentDTO;
import vn.com.tpf.microservices.models.DEReturn.DESaleQueueDocumentDTO;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
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

    @FindBy(how = How.XPATH, using = "//*[@id='LoanApplication_Assigned_wrapper']/div[1]/div/div[2]/div[1]/table/thead[1]/tr/th/input")
    @CacheLookup
    private WebElement applicationAssignedNumberElement;

    @FindBy(how = How.XPATH, using = "//table[@id='LoanApplication_Assigned']//tbody//tr//td")
    @CacheLookup
    private List<WebElement> tbApplicationAssignedElement;

    @FindBy(how = How.ID, using = "document_neo")
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

//    @FindBy(how = How.XPATH, using = "//div[contains(@id, 'move_to_next_stage_div')]//button[contains(@id, 'move_to_next_stage')]")
//    @CacheLookup
//    private WebElement btnMoveToNextStageElement;

    @FindBy(how = How.XPATH, using = "//table[@id='LoanApplication_Assigned']")
    @CacheLookup
    private List<WebElement> checkButtonMoveNextStageElement;


    public SaleQueuePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    public void setData(SaleQueueDTO saleQueueDTO, String downLoadFileURL) throws IOException, InterruptedException {
        ((RemoteWebDriver) _driver).setFileDetector(new LocalFileDetector());
            await("Input Application show timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> applicationAssignedNumberElement.isDisplayed());

        applicationAssignedNumberElement.clear();
        applicationAssignedNumberElement.sendKeys(saleQueueDTO.getApplicationId());
        applicationAssignedNumberElement.sendKeys(Keys.ENTER);
        with().pollInterval(Duration.FIVE_SECONDS).
                await("Application Id Not Found!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
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
            ipx.sendKeys(saleQueueDTO.getApplicationId());
            ipx.sendKeys(Keys.ENTER);
            with().pollInterval(Duration.FIVE_SECONDS).
                    await("Application Id Not Found!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> divIpx.size() > 2);
        }
        //check branch end



        WebElement applicationIdAssignedNumberElement = _driver.findElement(By.xpath("//*[@id='LoanApplication_Assigned_wrapper']/div[1]/div/div[2]/div[2]/div/table/tbody/tr/td/a"));
        applicationIdAssignedNumberElement.click();


        with().pollInterval(Duration.FIVE_SECONDS).
        await("Tab Document loading Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> loadTabDocumentElement.isDisplayed());

        List<String> requiredFiled = new ArrayList<>();
        if(saleQueueDTO.getDocuments().size() > 0){
            saleQueueDTO.getDocuments().stream().forEach(doc -> {
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
                SaleQueueDocumentDTO doc = saleQueueDTO.getDocuments().stream().filter(q -> q.getDocumentName().equals(finalDocName)).findAny().orElse(null);
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

        await("Document Text Comment visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> documentTextCommentElement.isDisplayed());

        documentTextCommentElement.sendKeys(saleQueueDTO.getComments());

        documentBtnAddCommnetElement.click();

        Utilities.captureScreenShot(_driver);

        System.out.println("ADD COMMENT" + " => DONE");
        keyActionMoveNextStage();
//        Actions actions = new Actions(_driver);
//        actions.moveToElement(btnMoveToNextStageElement);
//

        await("Move next stage failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(_driver::getTitle, is("Application Grid"));
        Utilities.captureScreenShot(_driver);
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
