package vn.com.tpf.microservices.services.Automation.deReturn;

import lombok.Getter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import vn.com.tpf.microservices.models.DEReturn.DESaleQueueDTO;
import vn.com.tpf.microservices.models.DEReturn.DESaleQueueDocumentDTO;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Getter
public class DE_ReturnSaleQueuePage {
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

    @FindBy(how = How.ID, using = "applicationChildTabs")
    @CacheLookup
    private WebElement appChildTabsElement;


    @FindBy(how = How.ID, using = "document")
    @CacheLookup
    private WebElement documentElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id,'lendingDocumentsTable_wrapper')]//input[contains(@type, 'text')]")
    @CacheLookup
    private WebElement documentNameElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id,'lendingDocumentsTable_wrapper')]//table[contains(@id,'lendingDocumentsTable')]//tbody//tr")
    @CacheLookup
    private List<WebElement> documentTableElement;

    @FindBy(how = How.XPATH, using = "//div[@id='document']//div//div[2]//div//input[contains(@id, 'executeDocumentIntegrationSet')]")
//    @FindBy(how = How.ID, using = "executeDocumentIntegrationSet")
    @CacheLookup
    private WebElement btnGetDocumentElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id,'lendingDocumentsTable_wrapper')]//table[contains(@id,'lendingDocumentsTable')]//tbody//tr//td[4]//select")
    @CacheLookup
    private WebElement documentStatusElement;

    @FindBy(how = How.XPATH, using = "//table[@class='table table-bordered no-border']")
    @CacheLookup
    private WebElement documentUploadElement;

    @FindBy(how = How.XPATH, using = "//table[contains(@class, 'table table-striped table-bordered')]//input[contains(@type, 'file')]")
    @CacheLookup
    private WebElement documentBtnUploadElement;

    @FindBys(
        @FindBy(how = How.XPATH, using = "//div[contains(@class, 'txt-r actionBtnDiv')]//input[contains(@value, 'Saves')]")
    )
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

    @FindBy(how = How.XPATH, using = "//div[contains(@id, 'activityDiv')]//div[contains(@id, 'collapse-uact')]")
    @CacheLookup
    private WebElement documentActivityElement;

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


    public DE_ReturnSaleQueuePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    public void setData(String appId, List<DESaleQueueDocumentDTO> lstDocument, String commnetText,String user) {
        try{

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
                    .until(() -> appChildTabsElement.isDisplayed());

            applicationBtnDocumentElement.click();

            await("tdApplicationElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> documentElement.isDisplayed());

            btnGetDocumentElement.click();

            lstDocument.forEach(documentList -> {

                await("document_table visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> documentTableElement.size() > 2);

                documentNameElement.clear();

                documentNameElement.sendKeys(documentList.getDocumentname());

                await("document_table visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> documentTableElement.size() > 2);

                documentStatusElement.sendKeys(documentList.getStatus());

                await("application_upload visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> documentUploadElement.isDisplayed());

                documentBtnUploadElement.sendKeys(documentList.getUrlfile());

            });

            documentNameElement.clear();

            documentNameElement.sendKeys(" ");

            await("document_table visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> documentTableElement.size() > 3);

            documentBtnSaveElement.click();

            await("document_table_body visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> documentTableElement.size() > 2);

            documentLoadActivityElement.click();

            await("document_activity visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> documentActivityElement.isDisplayed());

            documentBtnCommentElement.click();

            await("document_text_comment visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> documentTextCommentElement.isDisplayed());

            documentTextCommentElement.sendKeys(commnetText);

            documentBtnAddCommnetElement.click();

            JavascriptExecutor jse2 = (JavascriptExecutor)_driver;
            jse2.executeScript("arguments[0].click();", btnMoveToNextStageElement);

            Utilities.captureScreenShot(_driver);

        }catch (NotFoundException ex){

            System.out.println(ex.getCause());

        }
    }
}
