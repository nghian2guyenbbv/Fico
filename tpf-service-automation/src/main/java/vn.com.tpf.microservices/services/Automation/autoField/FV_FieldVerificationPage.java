package vn.com.tpf.microservices.services.Automation.autoField;

import lombok.Getter;
import org.apache.commons.lang.StringEscapeUtils;
import org.awaitility.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.models.AutoField.SubmitFieldDTO;
import vn.com.tpf.microservices.services.Automation.SearchMenu;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.with;
import static org.hamcrest.Matchers.is;

@Getter
public class FV_FieldVerificationPage {
    private WebDriver _driver;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]")
    private WebElement menuApplicationElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]//div[contains(@class,'one-col')][3]//li//a[contains(text(),'Application Manager')]")
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

    @FindBy(how = How.XPATH, using = "//*[@id='edit_button0']/input[1]")
    private WebElement editElement;

    @FindBy(how = How.ID, using = "Text_selected_user0")
    private WebElement textSelectUserElement;

    @FindBy(how = How.XPATH, using = "//a[contains(@id, 'listitem_selected_user')]")
    private List<WebElement> textSelectUserOptionElement;

    @FindBy(how = How.XPATH, using = "//*[@id='with_branch']/input")
    private WebElement saveTaskElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]//div[contains(@class,'one-col')][2]//li[1]//span[contains(text(),'Applications')]")
    private WebElement applicationElement;

    @FindBy(how = How.ID, using = "LoanApplication_Assigned_wrapper")
    private WebElement applicationFormElement;

    @FindBy(how = How.XPATH, using = "//*[@id='LoanApplication_Assigned_wrapper']/div[1]/div/div[2]/div[1]/table/thead[1]/tr/th/input")
    private WebElement applicationAssignedNumberElement;

    @FindBy(how = How.XPATH, using = "//table[@id='LoanApplication_Assigned']//tbody//tr//td")
    private List<WebElement> tbApplicationAssignedElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'fii_front_wrapper']//table[@id = 'fii_front']//tbody//tr//td")
    private List<WebElement> tbFieldInvestigationInitiationElement;

    @FindBy(how = How.XPATH, using = "//*[@id='fii_front']/tbody/tr/td[6]/button[1]")
    private WebElement btnInitiateVerificationElement;

    @FindBy(how = How.XPATH, using = "//table[contains(@id, 'initiate_Verification')]")
    private WebElement tbVerificationTypeElement;

    @FindBy(how = How.XPATH, using = "//*[@id='field_investigation_entry_verification_typeNO_0']")
    private WebElement selectVerificationTypeElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id,'initiateVerification')]//div[contains(@class,'modal-body')]//table[contains(@id, 'initiate_Verification')]//input[starts-with(@id, 'Text_field_investigation_entry_verification_agencyNO')]")
    private WebElement inputAgencyElement;

    @FindBy(how = How.ID, using = "holder")
    private WebElement textSelectUserContainerElement;

    @FindBy(how = How.XPATH, using = "//div[starts-with(@id,'content_field_investigation_entry_verification_agencyNO')]//ul[@id = 'holder']//a[starts-with(@id, 'listitem_field_investigation_entry_verification_agencyNO')]")
    private List<WebElement> liAgencyElement;

    @FindBy(how = How.XPATH, using = "//div[starts-with(@id,'content_field_investigation_entry_verification_agencyNO')]//ul[@id = 'holder']//li[starts-with(@id, 'listitem_field_investigation_entry_verification_agencyNO')]//a")
    private List<WebElement> liAgencyTagAElement;

    @FindBy(how = How.XPATH, using = "//a[contains(@id, 'listitem_field_investigation_entry_verification_agency')]")
    private List<WebElement> textInputAgencyElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id,'initiateVerification')]//div[contains(@class,'modal-footer actionBtnDiv')]//button[contains(text(), 'Initiate Verification')]")
    private WebElement btnInitiateVerificationPopupElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id, 'move_to_next_stage_div')]//button[contains(@id, 'move_to_next_stage')]")
    private WebElement btnMoveToNextStageElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'fii_front_wrapper']//table[@id = 'fii_front']//tbody//tr//td[5]")
    private WebElement tdCurrentVerificationStatus;

    @FindBy(how = How.XPATH, using = "//*[@id='viewDetailsButton']")
    private WebElement btnViewDetails;

    @FindBy(how = How.XPATH, using = "//*[@id='initiateRow0']/td")
    private List<WebElement> tableInDialogElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'dialog'][@class = 'modal-dialog']//div[@id = 'fi_modal_body']//form[@id = 'initiateVerificationForm']//input[starts-with(@class,'btn btn-inverse')]")
    private WebElement btnInverse;

    @FindBy(how = How.XPATH, using = "//div[@id = 'dialog'][@class = 'modal-dialog']//div[@id = 'fi_modal_body']//form[@id = 'initiateVerificationForm']//table[@id = 'initiate_Verification']//tbody//tr")
    private List<WebElement> tableFiiBtnAddElement;

    @FindBy(how = How.XPATH, using = "//div[starts-with(@class, 'ui-pnotify')]//div[@class ='alert ui-pnotify-container alert-error ui-pnotify-shadow']//div[@class = 'ui-pnotify-text']")
    private WebElement popupError;

    @FindBy(how = How.XPATH, using = "//table[@id = 'LoanApplication_Assigned']//tbody//tr//td")
    private List<WebElement> tableApplicationsElement;

    @FindBy(how = How.XPATH, using = "//option[@class = 'veri_option']")
    private List<WebElement> optionVerificationTypeElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id, 'move_to_next_stage_div')]//button[contains(@id, 'move_to_next_stage')]")
    private List<WebElement> checkMoveToNextStageElement;


    public FV_FieldVerificationPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    public void setData(SubmitFieldDTO submitFieldDTO, String user, String downdloadFileURL, Instant start) {
        String stage = "";

        // ========== APPLICATION MANAGER =================
        stage = "APPLICATION MANAGER";

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

        int sizeTextSelectUserOptionElement = textSelectUserOptionElement.size();

        await("textSelectUserOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> sizeTextSelectUserOptionElement > 0);

        for (WebElement e : textSelectUserOptionElement) {
            e.click();
            break;
        }
        Utilities.captureScreenShot(_driver);
        saveTaskElement.click();
        System.out.println(stage + ": DONE");
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // ========== INITIATE VERIFICATION =================

        SearchMenu goToApp = new SearchMenu(_driver);
        goToApp.MoveToPage(Constant.MENU_NAME_LINK_APPLICATIONS);
        _driver.findElement(By.xpath("//*[@id='lead']/a")).click();

        await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(_driver::getTitle, is("Application Grid"));

        applicationAssignedNumberElement.clear();
        applicationAssignedNumberElement.sendKeys(submitFieldDTO.getAppId());
        applicationAssignedNumberElement.sendKeys(Keys.ENTER);

        with().pollInterval(Duration.FIVE_SECONDS).await("Find not found AppId!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tbApplicationAssignedElement.size() > 2);

        WebElement applicationIdAssignedNumberElement =_driver.findElement(By.xpath("//*[@id='LoanApplication_Assigned_wrapper']/div[1]/div/div[2]/div[2]/div/table/tbody/tr/td/a"));

        applicationIdAssignedNumberElement.click();

        await("Table Field Investigation Initiation visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tbFieldInvestigationInitiationElement.size() > 2);

        System.out.println(tdCurrentVerificationStatus.getText());

        btnInitiateVerificationElement.click();

        await("Popup visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tableInDialogElement.size() > 2);

        with().pollInterval(Duration.FIVE_SECONDS).await("tbVerificationTypeElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tbVerificationTypeElement.isDisplayed());

        await("tbVerificationTypeElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> selectVerificationTypeElement.isDisplayed());

        await("tbVerificationTypeElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> optionVerificationTypeElement.size() > 0);

        Utilities.chooseDropdownValue("Residence Verification", optionVerificationTypeElement);

        await("inputAgencyElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> inputAgencyElement.isDisplayed());

        inputAgencyElement.sendKeys("TPAgency");

        await("textSelectUserContainerElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> textSelectUserContainerElement.isDisplayed());

        int sizeLiAgencyElement = liAgencyElement.size();

        await("liAgencyElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> sizeLiAgencyElement > 0);

        for (WebElement e : liAgencyTagAElement) {
            e.click();
            break;
        }

        btnInitiateVerificationPopupElement.click();

        await("Table Field Investigation Initiation visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> btnViewDetails.isDisplayed());

        with().pollInterval(Duration.FIVE_SECONDS).await("Button Move To Next Stage Element visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> btnMoveToNextStageElement.isDisplayed());

        with().pollInterval(Duration.FIVE_SECONDS).await("Button Move To Next Stage Element enable Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> btnMoveToNextStageElement.isEnabled());
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        btnMoveToNextStageElement.click();
//        keyActionMoveNextStage();

        Utilities.captureScreenShot(_driver);
    }

    public void keyActionMoveNextStage(){
        Actions actionKey = new Actions(_driver);
        WebElement divTimeRemaining = _driver.findElement(By.xpath("//div[@id = 'heading']//div[@id = 'timer_containerappTat']"));

        actionKey.moveToElement(divTimeRemaining).click();
        actionKey.sendKeys(Keys.TAB).build().perform();
        actionKey.sendKeys(Keys.TAB).build().perform();
        actionKey.sendKeys(Keys.ENTER).build().perform();
    }
}
