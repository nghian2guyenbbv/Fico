package vn.com.tpf.microservices.services.Automation.autoCRM;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import vn.com.tpf.microservices.models.AutoCRM.CRM_DocumentsDTO;
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

@Slf4j
@Getter
public class CRM_DocumentsPage {
    private WebDriver _driver;
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

    @FindBy(how = How.ID, using = "submitDocuments")
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

    public CRM_DocumentsPage(WebDriver driver) {
        this._driver = driver;
        PageFactory.initElements(driver, this);
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
        System.out.println(requiredFiled.toString());

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
                    toFile+=UUID.randomUUID().toString()+"_"+ docName +"." + ext;;
//                    FileUtils.copyURLToFile(new URL(fromFile + URLEncoder.encode( doc.getFilename(), "UTF-8").replaceAll("\\+", "%20")), new File(toFile), 10000, 10000);
                    FileUtils.copyURLToFile(new URL(fromFile + doc.getFilename()), new File(toFile), 10000, 10000);
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
                            System.out.println("EXISTS");
                        }else{
                            System.out.println("NEW");
                        }
                        WebElement ipxUploadFile = _driver.findElement(By.xpath("//*[@id='document_viewer_mp']//input[contains(@class,'input_images')]"));

                        ipxUploadFile.sendKeys(photoUrl);
                        WebElement saveDoc = _driver.findElement(By.id("dv_documentSave"));
                        saveDoc.click();
                        await("dv_documentSave Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(() ->  !saveDoc.isDisplayed());
                        Utilities.captureScreenShot(_driver);
                    }
                }
            }
        }
        WebElement submitFile = _driver.findElement(By.xpath("//*[@id='topActionBar']/button[1]"));
        submitFile.click();
        Utilities.captureScreenShot(_driver);

    }
}
