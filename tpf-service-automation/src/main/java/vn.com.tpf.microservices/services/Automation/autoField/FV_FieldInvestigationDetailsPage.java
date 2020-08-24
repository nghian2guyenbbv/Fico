package vn.com.tpf.microservices.services.Automation.autoField;

import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringEscapeUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.models.AutoField.SubmitFieldDTO;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.is;

@Getter
public class FV_FieldInvestigationDetailsPage {
    private WebDriver _driver;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]")
    private WebElement menuApplicationElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]//div[contains(@class,'one-col')][3]//li//a[contains(text(),'Application Manager')]")
    @CacheLookup
    private WebElement applicationManagerElement;

    @FindBy(how = How.ID, using = "applicationManagerForm1")
    private WebElement applicationManagerFormElement;

    @FindBy(how = How.ID, using = "appManager_lead_application_number")
    private WebElement applicationNumberElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'applicationManagerForm1')]//input[@type='button']")
    private WebElement searchApplicationElement;

    @FindBy(how = How.XPATH, using = "//table[@id='applicationTable']//tbody//tr//td")
    private List<WebElement> tdApplicationElement;

    @FindBy(how = How.ID, using = "showTasks")
    private WebElement showTaskElement;

    @FindBy(how = How.XPATH, using = "//table[@id='applicationTable']//tbody//tr//td[6]")
    private WebElement tdCheckStageApplicationElement;

    @FindBy(how = How.ID, using = "taskTableDiv")
    private WebElement taskTableDivElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'edit_button0')]//input[@type='button']")
    private WebElement editElement;

    @FindBy(how = How.ID, using = "Text_selected_user0")
    private WebElement textSelectUserElement;

    @FindBy(how = How.XPATH, using = "//a[contains(@id, 'listitem_selected_user')]")
    private List<WebElement> textSelectUserOptionElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'with_branch')]//input[@type='submit']")
    private WebElement saveTaskElement;

    @FindBy(how = How.ID, using = "holder")
    @CacheLookup
    private WebElement textSelectUserContainerElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]//div[contains(@class,'one-col')][2]//li[1]//span[contains(text(),'Applications')]")
    @CacheLookup
    private WebElement applicationElement;

    @FindBy(how = How.ID, using = "LoanApplication_Assigned_wrapper")
    @CacheLookup
    private WebElement applicationFormElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id,'LoanApplication_Assigned_wrapper')]//div[contains(@id,'LoanApplication_Assigned_filter')]//input[contains(@type,'text')]")
    @CacheLookup
    private WebElement applicationAssignedNumberElement;

    @FindBy(how = How.XPATH, using = "//table[@id='LoanApplication_Assigned']//tbody//tr//td")
    @CacheLookup
    private List<WebElement> tbApplicationAssignedElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'fieldInvestigationEntryTable_wrapper']//table[@id = 'fieldInvestigationEntryTable']//tbody//tr//td")
    private List<WebElement> tbFieldInvestigationEntryTable;

    @FindBy(how = How.XPATH, using = "//form[@id = 'application_fi_verdict']//div[@id = 'application_fi_verdict_decision-control-group']//div[@id = 'application_fi_verdict_decision_chzn']//div[@class = 'chzn-drop']//div[@class = 'chzn-search']//input[@type = 'text']")
    @CacheLookup
    private WebElement decisionInputElement;

    @FindBy(how = How.XPATH, using = "//form[@id = 'application_fi_verdict']//div[@id = 'application_fi_verdict_decision-control-group']//div[@id = 'application_fi_verdict_decision_chzn']//div[@class = 'chzn-drop']//ul[@class = 'chzn-results']")
    @CacheLookup
    private WebElement decisionUlElement;

    @FindBy(how = How.XPATH, using = "//form[@id = 'application_fi_verdict']//div[@id = 'application_fi_verdict_decision-control-group']//div[@id = 'application_fi_verdict_decision_chzn']//div[@class = 'chzn-drop']//ul[@class = 'chzn-results']//li[starts-with(@id, 'application_fi_verdict_decision_chzn_o_')]")
    @CacheLookup
    private List<WebElement> decisionSelectElement;

    @FindBy(how = How.XPATH, using = "//form[@id = 'application_fi_verdict']//div[@id = 'application_fi_verdict_reason0-control-group']//div[@id = 'application_fi_verdict_reason0_chzn']//div[@class = 'chzn-drop']//div[@class = 'chzn-search']//input[@type = 'text']")
    @CacheLookup
    private WebElement reasonInputElement;

    @FindBy(how = How.XPATH, using = "//form[@id = 'application_fi_verdict']//div[@id = 'application_fi_verdict_reason0-control-group']//div[@id = 'application_fi_verdict_reason0_chzn']//div[@class = 'chzn-drop']//ul[@class = 'chzn-results']")
    @CacheLookup
    private WebElement reasonUlElement;

    @FindBy(how = How.XPATH, using = "//form[@id = 'application_fi_verdict']//div[@id = 'application_fi_verdict_reason0-control-group']//div[@id = 'application_fi_verdict_reason0_chzn']//div[@class = 'chzn-drop']//ul[@class = 'chzn-results']//li[starts-with(@id, 'application_fi_verdict_reason0_chzn_o_')]")
    @CacheLookup
    private List<WebElement> reasonSelectElement;

    @FindBy(how = How.XPATH, using = "//textarea[@id = 'application_fi_verdict_decision_comments']")
    @CacheLookup
    private WebElement decisionCommentsElement;

    @FindBy(how = How.XPATH, using = "//input[@id = 'field_investigation_form_waive_field_button']")
    @CacheLookup
    private WebElement btnSaveDecisionElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id, 'move_to_next_stage_div')]//button[contains(@id, 'move_to_next_stage')]")
    @CacheLookup
    private WebElement btnMoveToNextStageElement;


    public FV_FieldInvestigationDetailsPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    @SneakyThrows
    public void setData(SubmitFieldDTO submitFieldDTO, String user) {
        String stage = "";

        menuApplicationElement.click();

        applicationManagerElement.click();

        // ========== APPLICATION MANAGER =================
        stage = "APPLICATION MANAGER";

        await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(_driver::getTitle, is("Application Manager"));

        await("appManager_lead_application_number visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> applicationManagerFormElement.isDisplayed());

        applicationNumberElement.sendKeys(submitFieldDTO.getAppId());
        searchApplicationElement.click();

        await("tdApplicationElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tdApplicationElement.size() > 2);

        await("showTaskElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> showTaskElement.isDisplayed());

        await("Stage wrong Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> "FIC".equals(tdCheckStageApplicationElement.getText()));

        showTaskElement.click();

        await("taskTableDivElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> taskTableDivElement.isDisplayed());

        await("editElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> editElement.isDisplayed());

        editElement.click();

        await("textSelectUserElement enable Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> textSelectUserElement.isEnabled());

        textSelectUserElement.clear();
        textSelectUserElement.sendKeys(user);
        await("textSelectUserContainerElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> textSelectUserContainerElement.isDisplayed());

        await("textSelectUserOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> textSelectUserOptionElement.size() > 0);

        for (WebElement e : textSelectUserOptionElement) {
            if (!Objects.isNull(e.getAttribute("title")) && StringEscapeUtils.unescapeJava(e.getAttribute("title")).equals(user)) {
                e.click();
                break;
            }
        }
        Utilities.captureScreenShot(_driver);
        saveTaskElement.click();
        System.out.println(stage + ": DONE");


        // ========== FIELD INITIATION COMPLETION =================

        menuApplicationElement.click();

        applicationElement.click();

        await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(_driver::getTitle, is("Application Grid"));

        applicationAssignedNumberElement.clear();

        applicationAssignedNumberElement.sendKeys(submitFieldDTO.getAppId());

        await("Find not found AppId!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tbApplicationAssignedElement.size() > 2);

        WebElement applicationIdAssignedNumberElement = _driver.findElement(new By.ByXPath("//table[@id='LoanApplication_Assigned']//tbody//tr//td[contains(@class,'tbl-left')]//a[contains(text(),'" + submitFieldDTO.getAppId() + "')]"));

        applicationIdAssignedNumberElement.click();

        await("tbFieldInvestigationEntryTable visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tbFieldInvestigationEntryTable.size() > 2);

//        decisionInputElement.sendKeys(fieldList.getDecisionFic());
        decisionInputElement.sendKeys("Approve");

        await("decisionUlElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> decisionUlElement.isDisplayed());

        await("decisionSelectElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> decisionSelectElement.size() > 0);

        for (WebElement e : decisionSelectElement) {
            if (!Objects.isNull(e.getText()) && StringEscapeUtils.unescapeJava(e.getText()).equals("Approve")) {
                e.click();
                break;
            }
        }

        await("Reason Select loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> reasonSelectElement.size() > 0);

        reasonInputElement.sendKeys(submitFieldDTO.getResonDecisionFic());


        await("reasonUlElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> reasonUlElement.isDisplayed());

        await("reasonSelectElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> reasonSelectElement.size() > 0);

        for (WebElement e : reasonSelectElement) {
            if (!Objects.isNull(e.getText()) && StringEscapeUtils.unescapeJava(e.getText()).equals(submitFieldDTO.getResonDecisionFic())) {
                e.click();
                break;
            }
        }

        if(!Objects.isNull(submitFieldDTO.getRemarksDecisionFic())){
            decisionCommentsElement.sendKeys(submitFieldDTO.getRemarksDecisionFic());
        }

        btnSaveDecisionElement.click();

        Thread.sleep(40000);

//        JavascriptExecutor jseMoveNextStage = (JavascriptExecutor) _driver;
//        jseMoveNextStage.executeScript("arguments[0].click();", btnMoveToNextStageElement);

        btnMoveToNextStageElement.click();

        Utilities.captureScreenShot(_driver);

        await("Work flow failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(_driver::getTitle, is("Application Grid"));

    }
}
