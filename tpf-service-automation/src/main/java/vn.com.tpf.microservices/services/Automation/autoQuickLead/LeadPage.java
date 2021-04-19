package vn.com.tpf.microservices.services.Automation.autoQuickLead;

import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.awaitility.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.models.AutoQuickLead.DocumentDTO;
import vn.com.tpf.microservices.models.AutoQuickLead.QuickLeadDetails;
import vn.com.tpf.microservices.models.FileUploadDoc;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.with;

@Getter
public class LeadPage {
    private WebDriver _driver;

    @FindBy(how = How.ID, using = "simulationContentOfleadTab0")
    @CacheLookup
    private WebElement contentElement;


    @FindBy(how = How.ID, using = "dob0")
    @CacheLookup
    private WebElement dobElement;

    @FindBy(how = How.ID, using = "Text_nearestCityInd0")
    @CacheLookup
    private WebElement branchElement;

    @FindBy(how = How.ID, using = "holder")
    @CacheLookup
    private WebElement branchContainerElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'listitem_nearestCityInd0')]")
    @CacheLookup
    private List<WebElement> branchOptionElement;

    @FindBy(how = How.ID, using = "occupationType0_chzn")
    @CacheLookup
    private WebElement occupationTypeElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'occupationType0_chzn_o_')]")
    @CacheLookup
    private List<WebElement> occupationTypeOptionElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'occupationType0_chzn')]//input")
    @CacheLookup
    private WebElement occupationTypeInputElement;

    @FindBy(how = How.ID, using = "loanSchemeList_chzn")
    @CacheLookup
    private WebElement schemeElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'loanSchemeList_chzn_o')]")
    @CacheLookup
    private List<WebElement> schemeOptionElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'loanSchemeList_chzn')]//input")
    @CacheLookup
    private WebElement schemeInputElement;

    @FindBy(how = How.ID, using = "loadApplicantInfo")
    @CacheLookup
    private WebElement activityElement;

    @FindBy(how = How.ID, using = "comment_button")
    @CacheLookup
    private WebElement commentBtnElement;

    @FindBy(how = How.ID, using = "comment_textarea")
    @CacheLookup
    private WebElement commentElement;

    @FindBy(how = How.ID, using = "comment_add_button")
    @CacheLookup
    private WebElement commentAddElement;

    @FindBy(how = How.ID, using = "leadCommHist")
    @CacheLookup
    private WebElement communicationElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'collapseOnee_lead_comm')]//button[@class='btn btn-small btn-primary']")
    @CacheLookup
    private WebElement addCommunicationElement;

    @FindBy(how = How.ID, using = "lead_communication_form")
    @CacheLookup
    private WebElement formCommunicationElement;

    @FindBy(how = How.ID, using = "uniform-inperson_mode")
    @CacheLookup
    private WebElement webPortalElement;

    @FindBy(how = How.ID, using = "lead_response")
    @CacheLookup
    private WebElement leadResponseElement;

    @FindBy(how = How.ID, using = "comm_status_chzn")
    @CacheLookup
    private WebElement leadStatusElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'comm_status_chzn_o_')]")
    @CacheLookup
    private List<WebElement> leadStatusOptionElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'comm_status_chzn')]//input")
    @CacheLookup
    private WebElement leadStatusInputElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'lead_communication_form')]//input[contains(@value,'Add')]")
    @CacheLookup
    private WebElement addBtnElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'lead_communication_form')]//input[contains(@value,'Save communication details')]")
    @CacheLookup
    private WebElement saveCommunicationBtnElement;

    @FindBy(how = How.ID, using = "collapseOnee_lead_docu")
    @CacheLookup
    private WebElement leadDocElement;

    @FindBy(how = How.ID, using = "getDocumentQucikLead")
    @CacheLookup
    private WebElement getDocBtnElement;

    @FindBy(how = How.ID, using = "docu_jsp_include")
    @CacheLookup
    private WebElement documentContainerElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'applicationDocument_name')]//span[contains(@class,'fw-b fs-4')]")
    @CacheLookup
    private List<WebElement> docNameElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'photoimg')]")
    private List<WebElement> photoElement;

    @FindBy(how = How.ID, using = "saveButtonLeadToSaveDocuments")
    private WebElement saveDocBtnElement;

    @FindBy(how = How.ID, using = "moveToApplication1")
    private WebElement moveAppBtnElement;

    @FindBy(how = How.ID, using = "moveToAppConfirmDialog")
    @CacheLookup
    private WebElement modalConfirmElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'moveToAppConfirmDialog')]//a[contains(@id, 'saveAndMoveToAppConfirm')]")
    @CacheLookup
    private WebElement modalBtnConfirmElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'c_docStatus')]")
    private List<WebElement> checkBoxFileElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'docu_jsp_include']//input[contains(@id, 'photoimg')]")
    private List<WebElement> listBtnUploadDocument;

    public LeadPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    public boolean setData(QuickLeadDetails quickLead, String leadId, String downLoadFileURL) {
        boolean flag=true;
        try {
            ((RemoteWebDriver) _driver).setFileDetector(new LocalFileDetector());
            Actions actions = new Actions(_driver);
            JavascriptExecutor js= (JavascriptExecutor)_driver;
            await("contentElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> contentElement.isDisplayed());


            dobElement.sendKeys(quickLead.getDateOfBirth());
            dobElement.sendKeys(Keys.ENTER);

            branchElement.sendKeys(quickLead.getSourcingBranch());
            await("branchContainerElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> branchContainerElement.isDisplayed());

            await("branchOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> branchOptionElement.size() > 0);
            for (WebElement e : branchOptionElement) {
                if (!Objects.isNull(e.getAttribute("username")) && StringEscapeUtils.unescapeJava(e.getAttribute("username")).equals(quickLead.getSourcingBranch())) {
                    e.click();
                    break;
                }
            }

            actions.moveToElement(occupationTypeElement).click().build().perform();

            await("occupationTypeOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> occupationTypeOptionElement.size() > 0);

            occupationTypeInputElement.sendKeys(quickLead.getNatureOfOccupation());
            occupationTypeInputElement.sendKeys(Keys.ENTER);

            actions.moveToElement(schemeElement).click().build().perform();

            await("schemeOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> schemeOptionElement.size() > 0);

            schemeInputElement.sendKeys(quickLead.getSchemeCode());
            schemeInputElement.sendKeys(Keys.ENTER);
            Utilities.captureScreenShot(_driver);

            //ACTIVITY
            activityElement.click();
            await("commentBtnElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> commentBtnElement.isDisplayed());
            commentBtnElement.click();
            await("commentElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> commentElement.isDisplayed());
            commentElement.sendKeys(quickLead.getComment());
            commentAddElement.click();
            Utilities.captureScreenShot(_driver);

            //COMMUNICATION
            communicationElement.click();
            await("addCommunicationElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> addCommunicationElement.isDisplayed());
            addCommunicationElement.click();
            await("formCommunicationElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> formCommunicationElement.isDisplayed());

            await("webPortalElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> webPortalElement.isDisplayed());
            webPortalElement.click();
            Utilities.captureScreenShot(_driver);

            actions.moveToElement(leadStatusElement).click().build().perform();
            await("leadStatusOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadStatusOptionElement.size() > 0);
            leadStatusInputElement.sendKeys(quickLead.getLeadStatus());
            leadStatusInputElement.sendKeys(Keys.ENTER);
            Utilities.captureScreenShot(_driver);

            leadResponseElement.sendKeys(quickLead.getCommunicationTranscript());
            addBtnElement.click();
            saveCommunicationBtnElement.click();
            Utilities.captureScreenShot(_driver);

            await("leadDocElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadDocElement.isDisplayed());

            getDocBtnElement.click();

            Thread.sleep(30000);

            Utilities.captureScreenShot(_driver);

            with().pollInterval(Duration.FIVE_SECONDS).
                    await("documentContainerElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> documentContainerElement.isDisplayed());

            int listButtonDoc = listBtnUploadDocument.size();

            await("documentContainerElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> listButtonDoc > 0);

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
                    "TPF_Insurance_Contract","TPF_student_Card","TPF_School_Confirmation","TPF_student_ID_Card"
            );
            Utilities.captureScreenShot(_driver);

            //update count file upload
            List<FileUploadDoc> listIndexDoc=new ArrayList<FileUploadDoc>();

            for (WebElement element : docNameElement) {
                final int _tempIndex = index;
                String docName = element.getText();
                String toFile = Constant.SCREENSHOT_PRE_PATH_DOCKER;
                if (requiredFiled.contains(docName)) {

                    String finalDocName = docName;
                    DocumentDTO doc=quickLead.documents.stream().filter(q->q.getType().equals(finalDocName)).findAny().orElse(null);

                    if(doc!=null)
                    {
                        //bo sung get ext file
                        String ext= FilenameUtils.getExtension(doc.getFilename());

                        if ("TPF_Transcript".equals(docName)){
                            docName = "TPF_Tran1";
                        }

                        toFile+= UUID.randomUUID().toString()+"_"+ docName +"." + ext;

//                        FileUtils.copyURLToFile(new URL(fromFile + URLEncoder.encode( doc.getFilename(), "UTF-8").replaceAll("\\+", "%20")), new File(toFile), 10000, 10000);
                        FileUtils.copyURLToFile(new URL(fromFile + URLEncoder.encode( doc.getFilename(), "UTF-8").replaceAll("\\+", "%20")), new File(toFile));
                        File file = new File(toFile);
                        if(file.exists()) {
                            String photoUrl = file.getAbsolutePath();
                            System.out.println("PATH: " + photoUrl);
                            Thread.sleep(3000);
                            photoElement.get(_tempIndex).sendKeys(photoUrl);
                            listIndexDoc.add(FileUploadDoc.builder().index(_tempIndex).urlPhoto(photoUrl).build());

                        }
                    }
                }
                index++;
            }

            Thread.sleep(5000);
            saveDocBtnElement.click();

            await("moveAppBtnElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> moveAppBtnElement.isDisplayed());

            Utilities.captureScreenShot(_driver);

            // -------------------- update count, get nhung file da duoc upload
            List<WebElement> listCheckBoxUpload=_driver.findElements(By.xpath("//*[contains(@id, 'c_docStatus') and contains(@value,'2')]"));

            if(listCheckBoxUpload.size()!=listIndexDoc.size())
            {
                flag=false;
                return flag;
            }
            //------------------- End update

            String moveToApplicationBtnElement = _driver.findElement(By.id("moveToApplication1")).getAttribute("onclick");
            js.executeScript(moveToApplicationBtnElement);

//            moveAppBtnElement.click();

            Utilities.captureScreenShot(_driver);

            try {
                await("modalConfirmElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> _driver.findElement(By.xpath("//div[@id = 'moveToAppConfirmDialog']")).isDisplayed());
                modalBtnConfirmElement.click();
            } catch (Exception e) {
                System.out.println("modalConfirmElement visibale Timeout!");
            }

            Utilities.captureScreenShot(_driver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
}
