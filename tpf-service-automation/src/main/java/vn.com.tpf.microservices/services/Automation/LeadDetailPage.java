package vn.com.tpf.microservices.services.Automation;


import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
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
import vn.com.tpf.microservices.models.Document;
import vn.com.tpf.microservices.models.QuickLead.QuickLead;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Getter
public class LeadDetailPage {
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
    @CacheLookup
    private List<WebElement> photoElement;

    @FindBy(how = How.ID, using = "saveButtonLeadToSaveDocuments")
    @CacheLookup
    private WebElement saveDocBtnElement;

    @FindBy(how = How.ID, using = "moveToApplication1")
    @CacheLookup
    private WebElement moveAppBtnElement;

    @FindBy(how = How.ID, using = "moveToAppConfirmDialog")
    @CacheLookup
    private WebElement modalConfirmElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'moveToAppConfirmDialog')]//a[contains(@id, 'saveAndMoveToAppConfirm')]")
    @CacheLookup
    private WebElement modalBtnConfirmElement;

    public LeadDetailPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    public void setData(QuickLead quickLead, String leadId,String downLoadFileURL) {
        try {
            ((RemoteWebDriver) _driver).setFileDetector(new LocalFileDetector());
            Actions actions = new Actions(_driver);
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
            Utilities.captureScreenShot(_driver);

            await("documentContainerElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> documentContainerElement.isDisplayed());

            //String toFile = "D:\\FILE_TEST_HE_THONG_\\";
            //String toFile = SCREENSHOT_PRE_PATH_DOC;

           String fromFile = downLoadFileURL;
            //"http://192.168.0.203:3001/v1/file/"
            //String fromFile = "http://tpf-service-file:3001/v1/file/";
            //String toFile = Constant.SCREENSHOT_PRE_PATH_DOCKER_DOWNLOAD;
            System.out.println("URLdownload: " + fromFile);

            int index = 0;
            List<String> requiredFiled = Arrays.asList("TPF_Application cum Credit Contract (ACCA)", "TPF_ID Card", "TPF_Family Book","TPF_Notarization of Family Book",
                                                        "TPF_Customer Photograph","TPF_Health Insurance Card","TPF_Customer Signature","TPF_Health Insurance Card","TPF_Banking Statement",
                                                        "TPF_Employer Confirmation","TPF_Labor Contract","TPF_Salary slip",
                                                        "TPF_Map to Customer House","TPF_Other_Bien_Lai_TTKV_Tai_TCTD_Khac",
                                        "TPF_Electricity bills","TPF_Water bill","TPF_Internet bills","TPF_Phone bills","PF_Cable TV bills","TPF_Confirm_student_infomation"); //
            Utilities.captureScreenShot(_driver);

            for (WebElement element : docNameElement) {
                final int _tempIndex = index;
                String docName = element.getText();
                //String toFile = "D:\\FILE_TEST_HE_THONG_\\";
                String toFile = Constant.SCREENSHOT_PRE_PATH_DOCKER;
                if (requiredFiled.contains(docName)) {
                    toFile+=UUID.randomUUID().toString()+"_"+ docName +".pdf";

                    Document doc=quickLead.documents.stream().filter(q->q.getType().equals(docName)).findAny().orElse(null);

                    if(doc!=null)
                    {


                        FileUtils.copyURLToFile(new URL(fromFile + URLEncoder.encode( doc.getFilename(), "UTF-8").replaceAll("\\+", "%20")), new File(toFile), 10000, 10000);
                        File file = new File(toFile);
                        if(file.exists()) {
                            //FileUtils.copyURLToFile(new URL(fromFile), new File(toFile), 10000, 10000);
                            String photoUrl = file.getAbsolutePath();
                            System.out.println("paht;" + photoUrl);
                            // Added sleep to make you see the difference.
                            Thread.sleep(2000);

                            photoElement.get(_tempIndex).sendKeys(photoUrl);
                            Utilities.captureScreenShot(_driver);
//                        // Added sleep to make you see the difference.
//                        Thread.sleep(2000);
                        }
                    }
                }
                index++;
            }

            saveDocBtnElement.click();
            Utilities.captureScreenShot(_driver);

            await("moveAppBtnElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> moveAppBtnElement.isDisplayed());
            moveAppBtnElement.click();

            Utilities.captureScreenShot(_driver);

            await("modalConfirmElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> modalConfirmElement.isDisplayed());

            modalBtnConfirmElement.click();

            Utilities.captureScreenShot(_driver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
