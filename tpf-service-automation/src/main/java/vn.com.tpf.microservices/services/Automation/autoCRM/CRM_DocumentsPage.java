package vn.com.tpf.microservices.services.Automation.autoCRM;

import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

@Getter
public class CRM_DocumentsPage {
    private WebDriver _driver;
    @FindBy(how = How.ID, using = "applicationChildTabs_document")
    @CacheLookup
    private WebElement tabDocumentsElement;

    @FindBy(how = How.ID, using = "document")
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

    @FindBy(how = How.XPATH, using = "//select[contains(@id, 'applicationDocument_receiveState')]")
    @CacheLookup
    private List<WebElement> lendingStatusElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id, 'receivedapplicationDocument_receiveState')]")
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

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'applicationDocument_name')]")
    @CacheLookup
    private List<WebElement> docNameElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'photoimg')]")
    @CacheLookup
    private List<WebElement> photoElement;

    public CRM_DocumentsPage(WebDriver driver) {
        this._driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void setData(String photoUrl) throws IOException {

        ((RemoteWebDriver) _driver).setFileDetector(new LocalFileDetector());
        String fromFile = "https://calllogoutside.fptshop.com.vn/Uploads/FileAttachs/TPBank/20190702/CMND_20190702081038_20190702074647_25332.jpg";
        String toFile = Constant.SCREENSHOT_PRE_PATH + "download_finnone.png" ;
        FileUtils.copyURLToFile(new URL(fromFile), new File(toFile), 10000, 10000);

        File file =new File(toFile);
        photoUrl=file.getAbsolutePath();
        System.out.println("Exist:" + file.exists());

        System.out.println("Exist:" + file.getAbsolutePath());

        int index = 0;
        List<String> requiredFiled = Arrays.asList("TPF_ID Card", "TPF_Customer Photograph", "TPF_Employee_Card", "TPF_Family Book"); //
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
    }

    public void updateData(List<DocumentDTO> documentDTOS, String downLoadFileURL) throws IOException, InterruptedException {
        ((RemoteWebDriver) _driver).setFileDetector(new LocalFileDetector());
        await("lendingDocumentsTable_wrapperElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(()->lendingDocumentsTable_wrapperElement.isDisplayed());

        String fromFile = downLoadFileURL;
        String toFile = Constant.SCREENSHOT_PRE_PATH_DOCKER;
        System.out.println("update doc - URLdownload: " + fromFile);

        int index = 0;

        List<String> updateField=new ArrayList<>();
        for(DocumentDTO documentDTO:documentDTOS){
            updateField.add(FilenameUtils.removeExtension(documentDTO.originalname));
            System.out.println("PATH:" + FilenameUtils.removeExtension(documentDTO.originalname));
        }

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

                    toFile+= UUID.randomUUID().toString()+"_"+ docName +"." + ext;
                    FileUtils.copyURLToFile(new URL(fromFile + URLEncoder.encode( documentDTO.getFilename(), "UTF-8").replaceAll("\\+", "%20")), new File(toFile), 10000, 10000);
                    File file = new File(toFile);
                    if(file.exists()) {
                        String photoUrl = file.getAbsolutePath();
                        System.out.println("paht;" + photoUrl);
                        // Added sleep to make you see the difference.
                        Thread.sleep(2000);

                        photoElement.get(_tempIndex).sendKeys(photoUrl);//
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
