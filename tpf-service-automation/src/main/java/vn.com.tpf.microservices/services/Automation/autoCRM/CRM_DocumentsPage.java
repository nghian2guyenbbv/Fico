package vn.com.tpf.microservices.services.Automation.autoCRM;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.awaitility.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import vn.com.tpf.microservices.models.AutoCRM.CRM_DocumentsDTO;
import vn.com.tpf.microservices.models.Automation.DocumentDTO;
import vn.com.tpf.microservices.models.DEReturn.DEResponseQueryDocumentDTO;
import vn.com.tpf.microservices.services.Automation.AutomationPage.DocumentPage;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.with;
import static org.hamcrest.Matchers.is;

@Slf4j
@Getter
public class CRM_DocumentsPage extends DocumentPage {
    private WebDriver _driver;
    @FindBy(how = How.XPATH, using = "//div[contains(@id, 'activityDiv')]//a[contains(@id, 'loadApplicantInfo')]")
    @CacheLookup
    private WebElement documentLoadActivityElement;

    @FindBy(how = How.ID, using = "document_neo")
    private WebElement loadTabDocumentElement;

    @FindBy(how = How.ID, using = "docTreeList")
    private WebElement loadTabDocumentTreeList;

    @FindBy(how = How.ID, using = "comment_add_button")
    @CacheLookup
    private WebElement documentBtnAddCommnetElement;

    @FindBy(how = How.ID, using = "comment_textarea")
    @CacheLookup
    private WebElement documentTextCommentElement;

    @FindBy(how = How.ID, using = "comment_button")
    @CacheLookup
    private WebElement documentBtnCommentElement;


    @FindBy(how = How.ID, using = "applicationChildTabs_document")
    @CacheLookup
    private WebElement tabDocumentsElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id,'documentContent')]")
    @CacheLookup
    private WebElement documentsContainerElement;

    @FindBy(how = How.ID, using = "executeDocumentIntegrationSet")
    @CacheLookup
    private WebElement btnGetDocumentElement;

    @FindBy(how = How.ID, using = "lendingDocumentList")
    @CacheLookup
    private WebElement lendingDocumentElement;

    @FindBy(how = How.XPATH, using = "//tr[contains(@id, 'row')]")
    @CacheLookup
    private List<WebElement> lendingTrElement;

//    @FindBy(how = How.XPATH, using = "//select[contains(@id, 'applicationDocument_receiveState')]")
    @FindBy(how = How.XPATH, using = "//div[contains(@class,'inputBox clearfix ng-scope')]//select[@title='Status']")
    @CacheLookup
    private WebElement lendingStatusElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@class,'inputBox clearfix ng-scope')]//select[@title='Status']//option")
    @CacheLookup
    private List<WebElement> lendingPhotoContainerElement;

    @FindBy(how = How.XPATH, using = "//input[contains(@id, 'photoimg')]")
//    @CacheLookup
    private List<WebElement> lendingPhotoElement;

    //@FindBy(how = How.ID, using = "submitDocuments")
    @FindBy(how= How.XPATH, using="//*[contains(@id, 'topActionBar')]//button[1]")
    @CacheLookup
    private WebElement btnSubmitElement;


    //------------------- UPDATE-----------------------
    @FindBy(how = How.ID, using = "lendingDocumentsTable_wrapper")
    @CacheLookup
    private WebElement lendingDocumentsTable_wrapperElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@data-type, 'docnode')]")
    @CacheLookup
    private List<WebElement> docNameElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'photoimg')]")
    @CacheLookup
    private List<WebElement> photoElement;

    private void keyActionMoveNextStage(){
        Actions actionKey = new Actions(_driver);
        WebElement divTimeRemaining = _driver.findElement(By.xpath("//div[@id = 'heading']//div[@id = 'timer_containerappTat']"));

        actionKey.moveToElement(divTimeRemaining).perform();
        divTimeRemaining.click();
        WebElement movoToStep = _driver.findElement(By.id("move_to_next_stage"));
        JavascriptExecutor js = (JavascriptExecutor)_driver;
        js.executeScript("arguments[0].click();",movoToStep);

    }


    public CRM_DocumentsPage(WebDriver driver) {
        this._driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void setDataForResponseMultiDoc(String func, List<DEResponseQueryDocumentDTO> documentDTOS, String downLoadFileURL, String documentComment)throws IOException, InterruptedException {
        boolean saveDocumentOrNotFlag = true;
        Actions actions = new Actions(_driver);
        with().pollInterval(Duration.FIVE_SECONDS).
                await("Tab Document loading Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> loadTabDocumentTreeList.isDisplayed());

        List<String> requiredFiled = new ArrayList<>();
        if (documentDTOS.size() > 0 ) {
            documentDTOS.stream().forEach(doc -> {
                requiredFiled.add(doc.getDocumentName());
            });

        }

        String fromFile = downLoadFileURL;
        Thread.sleep(5000);
        List<WebElement> listDocs = _driver.findElements(By.xpath("//*[contains(@data-type, 'docnode')]"));
        System.out.println("requiredFiled: " + requiredFiled);
        System.out.println("listDocs: "+listDocs);
        JavascriptExecutor executor = (JavascriptExecutor)_driver;
        for (WebElement element : listDocs) {
            String docName = element.getText();
            String toFile = Constant.SCREENSHOT_PRE_PATH_DOCKER;
            if (requiredFiled.contains(docName)) {
                await("Show document Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> element.isDisplayed());
                //actions.moveToElement(element).click().build().perform();
                executor.executeScript("arguments[0].click();",element);
                System.out.println("element.click(): "+docName);
                Thread.sleep(2000);
                String finalDocName = docName;
                DEResponseQueryDocumentDTO doc;
                doc = documentDTOS.stream().filter(q -> q.getDocumentName().equals(finalDocName)).findAny().orElse(null);
                if (doc != null) {
                    String ext = FilenameUtils.getExtension(doc.getFileName());
                    if ("TPF_Transcript".equals(docName)) {
                        docName = "TPF_Tran1";
                    }
                    toFile += UUID.randomUUID().toString() + "_" + docName + "." + ext;
                    FileUtils.copyURLToFile(new URL(fromFile + URLEncoder.encode(doc.getFileName(), "UTF-8").replaceAll("\\+", "%20")), new File(toFile), 10000, 10000);
                    File file = new File(toFile);
                    if("Received".equals(checkCurrrentStatus(_driver))){
                        try {
                            WebElement uploadedDocument = _driver.findElement(By.xpath("//*[contain(@class,'image-thumbs ecm-clearfix')]"));
                            // Click Edit if document was existed
                            WebElement editViewDoc = _driver.findElement(By.xpath("//*[@id='dv_documentEdit']"));
                            await("Load edit ViewDoc Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                    .until(() -> editViewDoc.isDisplayed());
                            editViewDoc.click();
                            actions.moveToElement(editViewDoc).click().perform();
                            saveDocumentOrNotFlag = false;
                            System.out.println("Click Edit document");
                        }catch(NoSuchElementException ex){
                            // No document was uploaded
                            changeDocumentStatus(_driver,"Select");
                            saveDocumentOrNotFlag = true;
                        }
                    }
                    System.out.println("file.exists(): "+file.exists());
                    if (file.exists()) {
                        String photoUrl = file.getAbsolutePath();
                        System.out.println("PATH:" + photoUrl);
                        changeDocumentStatus(_driver,"Received");
                        try {
                            Thread.sleep(10000);
                            WebElement inputDocElement = _driver.findElement(By.xpath("//*[@id='document_viewer_mp']//input[contains(@class,'input_images')]"));
                            WebElement imageAddMore = _driver.findElement(By.xpath("//li[contains(@class, 'imageBox add_more_box')]"));
                            await("Load imageAddMore Timeout!").atMost(300, TimeUnit.SECONDS)
                                    .until(() -> imageAddMore.isDisplayed());
                            inputDocElement.sendKeys(photoUrl);
                            Thread.sleep(2000);//waiting upload image
                            //go to the top page
                            _driver.findElement(By.tagName("Body")).sendKeys(Keys.HOME);
                            WebElement saveDoc = _driver.findElement(By.id("dv_documentSave"));

                            executor.executeScript("arguments[0].click();",saveDoc);
                            /*await("dv_documentSave Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                    .until(() -> !saveDoc.isDisplayed());*/
                            Thread.sleep(2000);
                            WebElement newImageActive = _driver.findElement(By.xpath("//*[@class='new_img active']"));
                            await("newImageActive Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                    .until(() -> newImageActive.isDisplayed());
                            System.out.println("Click save document button");

                        } catch (NoSuchElementException ex) {
                            changeDocumentStatus(_driver,"Select");
                            saveDocumentOrNotFlag = false;
                        }
                        Utilities.captureScreenShot(_driver);
                    }
                }
            }
        }
        if (saveDocumentOrNotFlag && !"runAutomation_Existing_Customer_Full".equals(func)&& !"handleUploadDocumentDE_ResponseQueryMultiDoc".equals(func)) { //quickLead - Existing customer doesn't have Active and Document tab
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

            documentTextCommentElement.sendKeys(documentComment);

            documentBtnAddCommnetElement.click();
            Utilities.captureScreenShot(_driver);
            System.out.println("ADD COMMENT" + " => DONE");
        }
        Utilities.captureScreenShot(_driver);
        executor.executeScript("arguments[0].click();",btnSubmitElement);
    }

    public void setData2(String func, List<CRM_DocumentsDTO> documentDTOS, String downLoadFileURL, String documentComment)throws IOException, InterruptedException {
        boolean saveDocumentOrNotFlag = true;
        Actions actions = new Actions(_driver);
        with().pollInterval(Duration.FIVE_SECONDS).
                await("Tab Document loading Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> loadTabDocumentElement.isDisplayed());

        List<String> requiredFiled = new ArrayList<>();
       /* if (documentDTOS.size() > 0) {
            documentDTOS.stream().forEach(doc -> {
                requiredFiled.add(doc.getType());
            });
        }*/

        if (documentDTOS.size() > 0 ) {
            if("runAutomation_Existing_Customer_Full".equals(func)){
                documentDTOS.stream().forEach(doc -> {
                    requiredFiled.add(doc.getType()); });
            }else{
                documentDTOS.stream().forEach(doc -> {
                    requiredFiled.add(doc.getOriginalname());
                });
            }

        }

        String fromFile = downLoadFileURL;
        Thread.sleep(5000);
        List<WebElement> listDocs = _driver.findElements(By.xpath("//*[contains(@data-type, 'docnode')]"));
        System.out.println("requiredFiled: " + requiredFiled);
        //System.out.println("listDocs: "+listDocs);
        JavascriptExecutor executor = (JavascriptExecutor)_driver;
        for (WebElement element : listDocs) {
            String docName = element.getText();
            String toFile = Constant.SCREENSHOT_PRE_PATH_DOCKER;
            if (requiredFiled.contains(docName)) {
                await("Show document Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> element.isDisplayed());
                //actions.moveToElement(element).click().build().perform();
                executor.executeScript("arguments[0].click();",element);
                System.out.println("element.click(): "+docName);
                Thread.sleep(2000);
                String finalDocName = docName;
                CRM_DocumentsDTO doc;
                if ("runAutomation_Existing_Customer_Full".equals(func)) {
                    doc = documentDTOS.stream().filter(q -> q.getType().equals(finalDocName)).findAny().orElse(null);

                } else {
                    doc = documentDTOS.stream().filter(q -> q.getOriginalname().equals(finalDocName)).findAny().orElse(null);

                }
                if (doc != null) {
                    String ext = FilenameUtils.getExtension(doc.getFilename());
                    if ("TPF_Transcript".equals(docName)) {
                        docName = "TPF_Tran1";
                    }
                    toFile += UUID.randomUUID().toString() + "_" + docName + "." + ext;
                    FileUtils.copyURLToFile(new URL(fromFile + URLEncoder.encode(doc.getFilename(), "UTF-8").replaceAll("\\+", "%20")), new File(toFile), 10000, 10000);
                    File file = new File(toFile);
                    if("Received".equals(checkCurrrentStatus(_driver))){
                        try {
                            WebElement uploadedDocument = _driver.findElement(By.xpath("//*[contain(@class,'image-thumbs ecm-clearfix')]"));
                            // Click Edit if document was existed
                            WebElement editViewDoc = _driver.findElement(By.xpath("//*[@id='dv_documentEdit']"));
                            await("Load edit ViewDoc Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                    .until(() -> editViewDoc.isDisplayed());
                            editViewDoc.click();
                            actions.moveToElement(editViewDoc).click().perform();
                            saveDocumentOrNotFlag = false;
                            System.out.println("Click Edit document");
                        }catch(NoSuchElementException ex){
                            // No document was uploaded
                            changeDocumentStatus(_driver,"Select");
                            saveDocumentOrNotFlag = true;
                        }
                    }
                    System.out.println("file.exists(): "+file.exists());
                    if (file.exists()) {
                        String photoUrl = file.getAbsolutePath();
                        System.out.println("PATH:" + photoUrl);
                        changeDocumentStatus(_driver,"Received");
                        try {
                         /*WebElement editViewDoc = _driver.findElement(By.xpath("//*[@id='dv_documentEdit']"));
                                                await("Load edit ViewDoc Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                                        .until(() -> editViewDoc.isDisplayed());
                                                editViewDoc.click();
                                                actions.moveToElement(editViewDoc).click().perform();*/


                            Thread.sleep(10000);
                            WebElement inputDocElement = _driver.findElement(By.xpath("//*[@id='document_viewer_mp']//input[contains(@class,'input_images')]"));
                            WebElement imageAddMore = _driver.findElement(By.xpath("//li[contains(@class, 'imageBox add_more_box')]"));
                            await("Load imageAddMore Timeout!").atMost(300, TimeUnit.SECONDS)
                                    .until(() -> imageAddMore.isDisplayed());
                            inputDocElement.sendKeys(photoUrl);
                            Thread.sleep(2000);//waiting upload image
                            //go to the top page
                            _driver.findElement(By.tagName("Body")).sendKeys(Keys.HOME);
                            WebElement saveDoc = _driver.findElement(By.id("dv_documentSave"));

                            executor.executeScript("arguments[0].click();",saveDoc);
                            /*await("dv_documentSave Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                    .until(() -> !saveDoc.isDisplayed());*/
                            Thread.sleep(2000);
                            WebElement newImageActive = _driver.findElement(By.xpath("//*[@class='new_img active']"));
                            await("newImageActive Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                    .until(() -> newImageActive.isDisplayed());
                            System.out.println("Click save document button");

                        } catch (NoSuchElementException ex) {
                            changeDocumentStatus(_driver,"Select");
                            saveDocumentOrNotFlag = false;
                        }
                        Utilities.captureScreenShot(_driver);
                    }
                }
            }
        }
        if (saveDocumentOrNotFlag && !"runAutomation_Existing_Customer_Full".equals(func)) { //quickLead - Existing customer doesn't have Active and Document tab
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

            documentTextCommentElement.sendKeys(documentComment);

            documentBtnAddCommnetElement.click();
            Utilities.captureScreenShot(_driver);
            System.out.println("ADD COMMENT" + " => DONE");
        }
        Utilities.captureScreenShot(_driver);
        executor.executeScript("arguments[0].click();",btnSubmitElement);
    }


    public void setData(List<CRM_DocumentsDTO> documentDTOS, String downLoadFileURL) throws IOException, InterruptedException {
        String fromFile = downLoadFileURL;
        System.out.println("URL get File: " + fromFile);

        List<String> requiredFiled = new ArrayList<>();
        if(documentDTOS.size() > 0){
            documentDTOS.stream().forEach(doc -> {
                requiredFiled.add(doc.getType());
            });
        }
        Utilities.captureScreenShot(_driver);
        Thread.sleep(5000);
        for (WebElement element : docNameElement) {
            String docName = element.getText();
            String toFile = Constant.SCREENSHOT_PRE_PATH_DOCKER;
            log.info("docName: {}",docName);
            if (requiredFiled.contains(docName)) {
                await("Show document Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() ->  element.isDisplayed());
                element.click();
                Thread.sleep(2000);

                String finalDocName  = docName;
                CRM_DocumentsDTO doc = documentDTOS.stream().filter(q->q.getType().equals(finalDocName)).findAny().orElse(null);

                if(doc!=null)
                {
                    String ext = FilenameUtils.getExtension(doc.getFilename());
                    if ("TPF_Transcript".equals(docName)){
                        docName = "TPF_Tran1";
                    }
                    toFile+=UUID.randomUUID().toString()+"_"+ docName +"." + ext;
                    FileUtils.copyURLToFile(new URL(fromFile + URLEncoder.encode( doc.getFilename(), "UTF-8").replaceAll("\\+", "%20")), new File(toFile), 10000, 10000);
                    File file = new File(toFile);
                    if(file.exists()) {
                        String photoUrl = file.getAbsolutePath();
                        System.out.println("PATH:" + photoUrl);
                        Thread.sleep(2000);

                        lendingStatusElement.click();

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

    }
}
