package vn.com.tpf.microservices.services.Automation.autoCRM;

import lombok.Getter;
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

    public void setData(List<CRM_DocumentsDTO> documentDTOS, String downLoadFileURL) throws IOException, InterruptedException {
        ((RemoteWebDriver) _driver).setFileDetector(new LocalFileDetector());
        await("lendingDocumentsTable_wrapperElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(()->lendingDocumentsTable_wrapperElement.isDisplayed());

        String fromFile = downLoadFileURL;
        System.out.println("URLdownload: " + fromFile);

        int index = 0;
        List<String> requiredFiled = Arrays.asList(
                "TPF_Application cum Credit Contract (ACCA)","TPF_ID Card","TPF_Notarization of ID card","TPF_Family Book",
                "TPF_Notarization of Family Book","TPF_Customer Photograph","TPF_Customer Signature",
                "TPF_Health Insurance Card","TPF_Banking Statement","TPF_Employer Confirmation","TPF_Labor Contract",
                "TPF_Salary slip","TPF_Appointment decision","TPF_Residence Confirmation","TPF_KT3","TPF_Electricity bills",
                "TPF_Water bill","TPF_Map to Customer House","TPF_Driving_License","TPF_Giay_xac_nhan_nhan_than",
                "TPF_Salary_SMS_Banking","TPF_E-Banking_photo","TPF_Others","TPF_Details of Stock","TPF_Employee_Card",
                "TPF_Collab document","TPF_Internet bills","TPF_Phone bills","TPF_Cable TV bills","TPF_Credit contract documentations",
                "TPF_Other_Bien_Lai_TTKV_Tai_TCTD_Khac","TPF_Other_Bao_Hiem_Xa_Hoi_Online","TPF_Other_Chung_Nhan_Ket_Hon",
                "TPF_Other_Mail_Xin_Deviation","TPF_Other_Tra_Cuu_Tren_Tong_Cuc_Thue","TPF_Xac_Nhan_Tam_Tru",
                "TPF_ Confirm_student_Information","TPF_ Confirm_student_Infomation","TPF_Confirm_student_infomation",
                "TPF_Confirm_student_Infomation","TPF_Confirm_student_information","TPF_Confirm_student_Information",
                "TPF_Offer_Letter","TPF_Diploma","TPF_Transcript","TPF_Representatives","TPF_Representatives ",
                "TPF_Student_Portal_Login_Photo","TPF_Insurance Bill","TPF_Insurance_Bill","TPF_Insurance Contract",
                "TPF_Insurance_Contract","TPF_student_Card","TPF_School_Confirmation","TPF_student_ID_Card",
                "TPF_Screenshot_Customer_Info_TPBank_Mobile_App"
        );
        Utilities.captureScreenShot(_driver);

        for (WebElement element : docNameElement) {
            final int _tempIndex = index;
            String docName = element.getText();
            String toFile = Constant.SCREENSHOT_PRE_PATH_DOCKER;
            if (requiredFiled.contains(docName)) {

                String finalDocName = docName;
                CRM_DocumentsDTO doc=documentDTOS.stream().filter(q->q.getType().equals(finalDocName)).findAny().orElse(null);

                if(doc!=null)
                {
                    //bo sung get ext file
                    String ext=FilenameUtils.getExtension(doc.getFilename());

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
                        new Select(lendingStatusElement.get(index)).selectByVisibleText("Received");
                        await("Load lendingPhotoContainerElement Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(() ->  lendingPhotoContainerElement.get(_tempIndex).isDisplayed());
                        photoElement.get(_tempIndex).sendKeys(photoUrl);
                        Utilities.captureScreenShot(_driver);
                    }
                }
            }
            index++;
        }

        Utilities.captureScreenShot(_driver);

    }
}
