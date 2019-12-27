package vn.com.tpf.microservices.services.Automation.deResponseQuery;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.models.DEResponseQuery.DEResponseQueryDocumentDTO;
import vn.com.tpf.microservices.models.DEResponseQuery.DESaleQueueDocumentDTO;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Getter
public class DE_SaleApplicationManagerPage {
    private WebDriver _driver;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]//div[contains(@class,'one-col')][2]//li//span[contains(text(),'Applications')]")
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

    @FindBy(how = How.XPATH, using = "//div[contains(@id,'LoanApplication_Assigned_wrapper')]//div[contains(@id,'LoanApplication_Assigned_filter')]//input[contains(@type,'text')]")
    @CacheLookup
    private WebElement applicationAssignedNumberElement;

    @FindBy(how = How.XPATH, using = "//table[@id='LoanApplication_Assigned']//tbody//tr")
    @CacheLookup
    private List<WebElement> tbApplicationAssignedElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id, 'casDiv')]")
    @CacheLookup
    private WebElement applicationInformtionElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id,'casDiv')]//div[contains(@class,'tabbable application-main-tab multipleTabs')]//div[contains(@class,'sticky-info-class skip-print')]//ul//li[contains(@id,'applicationChildTabs_document')]//a[contains(text(),'Documents')]")
    @CacheLookup
    private WebElement applicationBtnDocumentElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@class,'tab-content m-b5 m-t5 o-visi p-l5 p-r5')]//div[contains(@id,'lendingDocumentsTable_wrapper')]")
    @CacheLookup
    private WebElement documentElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@class,'tab-content m-b5 m-t5 o-visi p-l5 p-r5')]//div[contains(@id,'lendingDocumentsTable_wrapper')]//label[contains(text(),'Search:')]//input[contains(@type, 'text')]")
    @CacheLookup
    private WebElement documentNameElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@class,'tab-content m-b5 m-t5 o-visi p-l5 p-r5')]//div[contains(@id,'lendingDocumentsTable_wrapper')]//table[contains(@id,'lendingDocumentsTable')]//tbody//tr")
    @CacheLookup
    private List<WebElement> documentTableElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@class,'tab-content m-b5 m-t5 o-visi p-l5 p-r5')]//div[contains(@id,'lendingDocumentsTable_wrapper')]//table[contains(@id,'lendingDocumentsTable')]//tbody//tr//select[contains(@id, 'applicationDocument_receiveState2')]")
    @CacheLookup
    private WebElement documentStatusElement;

    @FindBy(how = How.ID, using = "receivedapplicationDocument_receiveState2")
    @CacheLookup
    private WebElement documentUploadElement;

    @FindBy(how = How.ID, using = "photoimg2")
    @CacheLookup
    private WebElement documentBtnUploadElement;

    @FindBy(how = How.ID, using = "submitDocuments")
    @CacheLookup
    private WebElement documentBtnSaveElement;

    @FindBy(how = How.ID, using = "lendingDocumentsTable_wrapper")
    @CacheLookup
    private WebElement documentTbDocumentsElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id, 'activityDiv')]//a[contains(@id, 'loadApplicantInfo')]")
    @CacheLookup
    private WebElement documentLoadActivityElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id, 'activityDiv')]//div[contains(@id, 'collapse-uact')]")
    @CacheLookup
    private WebElement documentActivityElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id, 'activityDiv')]//div[contains(@id, 'collapse-uact')]//input[contains(@id,'comment_button')]")
    @CacheLookup
    private WebElement documentBtnCommnetElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id, 'activityDiv')]//div[contains(@id, 'collapse-uact')]//textarea[contains(@id,'comment_textarea')]")
    @CacheLookup
    private WebElement documentTextCommnetElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id, 'activityDiv')]//div[contains(@id, 'collapse-uact')]//input[contains(@id,'comment_add_button')]")
    @CacheLookup
    private WebElement documentBtnAddCommnetElement;

    @FindBy(how = How.ID, using = "move_to_next_stage")
    @CacheLookup
    private WebElement btnMoveToNextStageElement;


    /*@FindBy(how = How.XPATH, using = "//table[@id='LoanApplication_Pool']//tbody//tr[1]//td[17]//img[contains(@id, 'AssignToMe')]")
    @CacheLookup
    private WebElement applicationPoolAssignToMeNumberElement;*/





    public DE_SaleApplicationManagerPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    public void setData(String appId, List<DESaleQueueDocumentDTO> lstDocument, String commnetText,String user) {
        await("appManager_lead_application_number visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> applicationDivAssignedElement.isDisplayed());

        poolElement.click();

        await("appManager_lead_application_number visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> applicationDivPoolElement.isDisplayed());

        applicationPoolNumberElement.clear();
        applicationPoolNumberElement.sendKeys(appId);

        await("tdApplicationElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tbApplicationPoolElement.size() > 2);

        WebElement applicationPoolAssignToMeNumberElement =_driver.findElement(new By.ByXPath("//table[@id='LoanApplication_Pool']//tbody//tr[1]//td[contains(/,'"+ appId +"')]//img[contains(@id, 'AssignToMe')]"));
        applicationPoolAssignToMeNumberElement.click();


        //Assigned
        await("appManager_lead_application_number visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> applicationFormElement.isDisplayed());


        applicationAssignedNumberElement.clear();
        applicationAssignedNumberElement.sendKeys(appId);

        await("tdApplicationElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tbApplicationAssignedElement.size() > 2);

        WebElement applicationIdAssignedNumberElement =_driver.findElement(new By.ByXPath("//table[@id='LoanApplication_Assigned']//tbody//tr//td[contains(@class,'tbl-left')]//a[contains(text(),'" + appId +"')]"));
        applicationIdAssignedNumberElement.click();

        await("tdApplicationElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> applicationInformtionElement.isDisplayed());

        applicationBtnDocumentElement.click();

        await("tdApplicationElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> documentElement.isDisplayed());

        lstDocument.forEach(documentList -> {
            documentNameElement.clear();
            documentNameElement.sendKeys(documentList.getDocumentname());
            await("tdApplicationElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> documentTableElement.size() > 0);
            documentStatusElement.sendKeys(documentList.getStatus());
            await("appManager_lead_application_number visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> documentUploadElement.isDisplayed());
            documentBtnUploadElement.sendKeys(documentList.getUrlfile());

            await("appManager_lead_application_number visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> applicationInformtionElement.isDisplayed());
        });

        documentBtnSaveElement.click();

        await("appManager_lead_application_number visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> documentTbDocumentsElement.isDisplayed());

        documentLoadActivityElement.click();

        await("appManager_lead_application_number visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> documentActivityElement.isDisplayed());

        documentBtnCommnetElement.click();

        await("appManager_lead_application_number visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> documentTextCommnetElement.isDisplayed());

        documentTextCommnetElement.sendKeys(commnetText);

        documentBtnAddCommnetElement.click();

        btnMoveToNextStageElement.click();

        Utilities.captureScreenShot(_driver);

    }
}
