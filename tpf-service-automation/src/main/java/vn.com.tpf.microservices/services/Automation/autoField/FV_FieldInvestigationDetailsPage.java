package vn.com.tpf.microservices.services.Automation.autoField;

import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringEscapeUtils;
import org.awaitility.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
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
import static org.awaitility.Awaitility.with;
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

    @FindBy(how = How.ID, using = "application_fi_verdict_decision_chzn")
    private WebElement decisionOptionDivElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'application_fi_verdict_decision_chzn_o_')]")
    private List<WebElement> decisionOptionElement;

    @FindBy(how = How.ID, using = "application_fi_verdict_reason0_chzn")
    private WebElement reasonOptionDivElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'application_fi_verdict_reason0_chzn_o_')]")
    private List<WebElement> reasonOptionElement;

    @FindBy(how = How.XPATH, using = "//textarea[@id = 'application_fi_verdict_decision_comments']")
    @CacheLookup
    private WebElement decisionCommentsElement;

    @FindBy(how = How.XPATH, using = "//input[@id = 'field_investigation_form_waive_field_button']")
    @CacheLookup
    private WebElement btnSaveDecisionElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id, 'move_to_next_stage_div')]//button[contains(@id, 'move_to_next_stage')]")
    @CacheLookup
    private WebElement btnMoveToNextStageElement;

    @FindBy(how = How.XPATH, using = "//table[@id = 'LoanApplication_Assigned']//tbody//tr//td")
    private List<WebElement> tableApplicationsElement;



    public FV_FieldInvestigationDetailsPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    public void setData(SubmitFieldDTO submitFieldDTO, String user) {
        String stage = "";

        Actions actions = new Actions(_driver);

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

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        with().pollInterval(Duration.FIVE_SECONDS).
        await("Find not found AppId!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tbApplicationAssignedElement.size() > 2);

        WebElement applicationIdAssignedNumberElement = _driver.findElement(new By.ByXPath("//table[@id='LoanApplication_Assigned']//tbody//tr//td[contains(@class,'tbl-left')]//a[contains(text(),'" + submitFieldDTO.getAppId() + "')]"));

        applicationIdAssignedNumberElement.click();

        System.out.println("Application Id Assigned" + ": DONE");

        await("tbFieldInvestigationEntryTable visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tbFieldInvestigationEntryTable.size() > 2);

        decisionOptionDivElement.click();

        await("decisionSelectElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> decisionOptionElement.size() > 0);

        Utilities.chooseDropdownValue("Approve", decisionOptionElement);

        System.out.println("Decision Option" + ": DONE");

        reasonOptionDivElement.click();

        await("decisionSelectElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> reasonOptionElement.size() > 0);

        Utilities.chooseDropdownValue(submitFieldDTO.getResonDecisionFic(), reasonOptionElement);

        System.out.println("Reason Option" + ": DONE");

        if(!Objects.isNull(submitFieldDTO.getRemarksDecisionFic())){
            decisionCommentsElement.sendKeys(submitFieldDTO.getRemarksDecisionFic());
        }

        System.out.println("Decision Comments" + ": DONE");

        with().pollInterval(Duration.FIVE_SECONDS).
        await("Button Save Decision Element loading timeout!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> btnSaveDecisionElement.isDisplayed());

        with().pollInterval(Duration.FIVE_SECONDS).
        await("Button Save Decision Element loading timeout!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> btnSaveDecisionElement.isEnabled());

        btnSaveDecisionElement.click();

        System.out.println("Button Save" + ": DONE");

        with().pollInterval(Duration.FIVE_SECONDS).
        await("Button Move Next Stage loading timeout!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> btnMoveToNextStageElement.isDisplayed());

        with().pollInterval(Duration.FIVE_SECONDS).
        await("Button Move Next Stage loading timeout!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> btnMoveToNextStageElement.isEnabled());

//        btnMoveToNextStageElement.click();
        actions.moveToElement(btnMoveToNextStageElement).sendKeys(btnMoveToNextStageElement, Keys.ENTER).perform();

        System.out.println("Button Move To Next Stage" + ": DONE");

        boolean checkMoveNextStage = tableApplicationsElement.size() != 0;

        await("FIELD INITIATION COMPLETION failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> checkMoveNextStage);

        Utilities.captureScreenShot(_driver);

    }
}
