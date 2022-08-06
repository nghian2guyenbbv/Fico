package vn.com.tpf.microservices.services.Automation;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
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
import org.openqa.selenium.support.ui.Select;
import vn.com.tpf.microservices.models.Automation.DocumentDTO;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.with;

@Slf4j
@Getter
public class DE_DocumentsPage {
    private WebDriver _driver;

    @FindBy(how = How.ID, using = "comment_add_button")
    @CacheLookup
    private WebElement documentBtnAddCommnetElement;

    @FindBy(how = How.ID, using = "comment_textarea")
    @CacheLookup
    private WebElement documentTextCommentElement;

    @FindBy(how = How.ID, using = "comment_button")
    @CacheLookup
    private WebElement documentBtnCommentElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id, 'activityDiv')]//a[contains(@id, 'loadApplicantInfo')]")
    @CacheLookup
    private WebElement documentLoadActivityElement;

    @FindBy(how = How.ID, using = "document_neo")
    private WebElement loadTabDocumentElement;

    @FindBy(how = How.ID, using = "applicationChildTabs_document")
    @CacheLookup
    private WebElement tabDocumentsElement;

    //@FindBy(how = How.ID, using = "document")
    //NghiaNVT Update document container
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

    //@FindBy(how = How.XPATH, using = "//select[contains(@id, 'applicationDocument_receiveState')]")
    @FindBy(how = How.XPATH, using = "//div[contains(@class,'inputBox clearfix ng-scope')]//select[@title='Status']")
    @CacheLookup
    private WebElement lendingStatusElement2;

    @FindBy(how = How.XPATH, using = "//select[contains(@id, 'applicationDocument_receiveState')]")
    @CacheLookup
    private List<WebElement> lendingStatusElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id, 'receivedapplicationDocument_receiveState')]")
    @CacheLookup
    private List<WebElement> lendingPhotoContainerElement;

    @FindBy(how = How.XPATH, using = "//input[contains(@id, 'photoimg')]")
//    @CacheLookup
    private List<WebElement> lendingPhotoElement;

    //@FindBy(how = How.ID, using = "submitDocuments")
    @FindBy(how = How.XPATH, using = "//ul[@class='mainActions clearfix ng-scope']//button[1]")
    @CacheLookup
    private WebElement btnSubmitElement;

    //------------------- UPDATE-----------------------
    @FindBy(how = How.ID, using = "lendingDocumentsTable_wrapper")
    @CacheLookup
    private WebElement lendingDocumentsTable_wrapperElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'applicationDocument_name')]")
    @CacheLookup
    private List<WebElement> docNameElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'photoimg')]")
    @CacheLookup
    private List<WebElement> photoElement;

    public DE_DocumentsPage(WebDriver driver) {
        this._driver = driver;
        PageFactory.initElements(driver, this);
    }
    private String checkCurrrentStatus(){
        WebElement editViewDoc = _driver.findElement(By.xpath("//div[contains(@class,'inputBox clearfix ng-scope')]//select[@title='Status']"));
        Select selectStatus = new Select(editViewDoc);
        WebElement e = selectStatus.getFirstSelectedOption();
        System.out.println("checkCurrrentStatus: "+e.getText());
        return e.getText();
    }

    private void changeDocumentStatus(String status) throws InterruptedException {
        _driver.findElement(By.xpath("//div[contains(@class,'inputBox clearfix ng-scope')]//select[@title='Status']")).click();
        List<WebElement> lendingPhotoContainerElement = _driver.findElements(By.xpath("//div[contains(@class,'inputBox clearfix ng-scope')]//select[@title='Status']//option"));
        await("Load lendingPhotoContainerElement Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> lendingPhotoContainerElement.size() != 0);

        {
            Utilities.chooseDropdownValue(status, lendingPhotoContainerElement);
            System.out.println("change Status to: " + status);
        }


    }

    public void setData2(List<DocumentDTO> documentDTOS, String downLoadFileURL, String documentComment) throws IOException, InterruptedException {
        boolean saveDocumentOrNotFlag = true;
        Actions actions = new Actions(_driver);
        with().pollInterval(Duration.FIVE_SECONDS).
                await("Tab Document loading Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> loadTabDocumentElement.isDisplayed());

        List<String> requiredFiled = new ArrayList<>();
        if (documentDTOS.size() > 0) {
            documentDTOS.stream().forEach(doc -> {
                requiredFiled.add(doc.getOriginalname());
            });
        }
        String fromFile = downLoadFileURL;
        Thread.sleep(5000);
        List<WebElement> listDocs = _driver.findElements(By.xpath("//*[contains(@data-type, 'docnode')]"));
        System.out.println("requiredFiled: " + requiredFiled);
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
                DocumentDTO doc = documentDTOS.stream().filter(q -> q.getOriginalname().equals(finalDocName)).findAny().orElse(null);
                if (doc != null) {
                    String ext = FilenameUtils.getExtension(doc.getFilename());
                    if ("TPF_Transcript".equals(docName)) {
                        docName = "TPF_Tran1";
                    }
                    toFile += UUID.randomUUID().toString() + "_" + docName + "." + ext;
                    FileUtils.copyURLToFile(new URL(fromFile + URLEncoder.encode(doc.getFilename(), "UTF-8").replaceAll("\\+", "%20")), new File(toFile), 10000, 10000);
                    File file = new File(toFile);
                    /*if("Received".equals(checkCurrrentStatus())){
                        System.out.println("Edit document");
                        WebElement editViewDoc = _driver.findElement(By.xpath("//*[@id='dv_documentEdit']"));
                        await("Load edit ViewDoc Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                                        .until(() -> editViewDoc.isDisplayed());
                                                //editViewDoc.click();
                        actions.moveToElement(editViewDoc).click().perform();

                    }*/
                    if (file.exists() && !"Received".equals(checkCurrrentStatus())) {
                    /*if (file.exists()) {*/
                        String photoUrl = file.getAbsolutePath();
                        System.out.println("PATH:" + photoUrl);
                        changeDocumentStatus("Received");
                        /*_driver.findElement(By.xpath("//div[contains(@class,'inputBox clearfix ng-scope')]//select[@title='Status']")).click();
                        List<WebElement> lendingPhotoContainerElement = _driver.findElements(By.xpath("//div[contains(@class,'inputBox clearfix ng-scope')]//select[@title='Status']//option"));
                        await("Load lendingPhotoContainerElement Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(() -> lendingPhotoContainerElement.size() != 0);
                        for (WebElement e : lendingPhotoContainerElement) {
                            if (e.getText().equals("Received")) {
                                e.click();
                                System.out.println("click Received");
                            }
                        }

*/
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
                            changeDocumentStatus("Select");
                            saveDocumentOrNotFlag = false;
                        }
                        Utilities.captureScreenShot(_driver);
                    }
                }
            }
        }
        if (saveDocumentOrNotFlag) {
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
       /* keyActionMoveNextStage();
        await("Move next stage failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(_driver::getTitle, is("Application Grid"));
        Utilities.captureScreenShot(_driver);*/
        executor.executeScript("arguments[0].click();",btnSubmitElement);
    }

    public void setData(String photoUrl) throws IOException {

        //dang ki local file
        ((RemoteWebDriver) _driver).setFileDetector(new LocalFileDetector());
        //download file
        String fromFile = "https://calllogoutside.fptshop.com.vn/Uploads/FileAttachs/TPBank/20190702/CMND_20190702081038_20190702074647_25332.jpg";
        String toFile = Constant.SCREENSHOT_PRE_PATH + "download_finnone.png" ;
        FileUtils.copyURLToFile(new URL(fromFile), new File(toFile), 10000, 10000);
//        byte[] fileContent = FileUtils.readFileToByteArray(new File(toFile));
//        String encodedString = Base64.getEncoder().encodeToString(fileContent);
//        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
//        FileUtils.writeByteArrayToFile(new File(Constant.SCREENSHOT_PRE_PATH_DOCKER +"/donwload_vinid.png"), decodedBytes);

        File file =new File(toFile);
        photoUrl=file.getAbsolutePath();
        System.out.println("Exist:" + file.exists());

        System.out.println("Exist:" + file.getAbsolutePath());

        int index = 0;
        List<String> requiredFiled = Arrays.asList("TPF_ID Card", "TPF_Customer Photograph", "TPF_Employee_Card", "TPF_Family Book"); //
        // TPF_Family Book is not required
//        List<String> requiredFiled = Arrays.asList("TPF_Application cum Credit Contract (ACCA)", "TPF_ID Card", "TPF_Customer Photograph",
//                "TPF_Customer Signature", "TPF_Income Proof");
        for(WebElement element : lendingTrElement) {
            final int _tempIndex = index;
            if (requiredFiled.contains(element.getAttribute("data-documentcode"))) {
                new Select(lendingStatusElement.get(index)).selectByVisibleText("Received");
                await("Load lendingPhotoContainerElement Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() ->  lendingPhotoContainerElement.get(_tempIndex).isDisplayed());
                lendingPhotoElement.get(_tempIndex).sendKeys(photoUrl);
                Utilities.captureScreenShot(_driver);
            }
            index++;
        }

      //System.out.println(file.delete());

    }

    public void updateData(List<DocumentDTO> documentDTOS,String downLoadFileURL) throws IOException, InterruptedException {
        ((RemoteWebDriver) _driver).setFileDetector(new LocalFileDetector());
        await("lendingDocumentsTable_wrapperElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(()->lendingDocumentsTable_wrapperElement.isDisplayed());

       //String fromFile = "http://192.168.0.203:3001/v1/file/";
        String fromFile = downLoadFileURL;

        //do index=0 la 1 element khac
        System.out.println("update doc - URLdownload: " + fromFile);

        int index = 0;

        List<String> updateField=new ArrayList<>();
        for(DocumentDTO documentDTO:documentDTOS){
            updateField.add(FilenameUtils.removeExtension(documentDTO.originalname));

            System.out.println("NAME:" + FilenameUtils.removeExtension(documentDTO.originalname));
        }

//        List<String> requiredFiled = Arrays.asList("TPF_Application cum Credit Contract (ACCA)", "TPF_ID Card", "TPF_Family Book",
//                "TPF_Customer Photograph","TPF_Customer Signature","TPF_Health Insurance Card","TPF_Banking Statement",
//                "TPF_Map to Customer House","TPF_Other_Bien_Lai_TTKV_Tai_TCTD_Khac"); //
        Utilities.captureScreenShot(_driver);

        for (WebElement element : docNameElement) {
            final int _tempIndex = index;
            String docName = element.getText();
            if (updateField.contains(docName)) {

                //do type truyen sang null
                DocumentDTO documentDTO=documentDTOS.stream().filter(documentdto -> documentdto.originalname.contains(docName)).findAny().orElse(null);
                if(documentDTO!=null){

                    //bo sung get ext file
                    String ext= FilenameUtils.getExtension(documentDTO.getFilename());
                    String toFile = Constant.SCREENSHOT_PRE_PATH_DOCKER;
                    toFile+= UUID.randomUUID().toString()+"_"+ docName +"." + ext;
                    //File file = new File(toFile + documentDTO.getFilename());
                    FileUtils.copyURLToFile(new URL(fromFile + URLEncoder.encode( documentDTO.getFilename(), "UTF-8").replaceAll("\\+", "%20")), new File(toFile), 10000, 10000);
                    File file = new File(toFile);
                    if(file.exists()) {
                        String photoUrl = file.getAbsolutePath();
                        System.out.println("PATH:" + photoUrl);
                        // Added sleep to make you see the difference.
                        Thread.sleep(2000);

                        photoElement.get(_tempIndex).sendKeys(photoUrl);//
                        //System.out.println("up oki");
                        // Added sleep to make you see the difference.
                        Thread.sleep(2000);

                        Utilities.captureScreenShot(_driver);
                        System.out.println(photoElement.get(_tempIndex).getText());
                    }
                }

            }
            index++;
        }

    }

}
