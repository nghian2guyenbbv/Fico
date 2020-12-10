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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import vn.com.tpf.microservices.models.AutoField.WaiveFieldDTO;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.with;
import static org.hamcrest.Matchers.is;

@Getter
public class FV_WaiveFieldPage {
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

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]//div[contains(@class,'one-col')][2]//li//span[contains(text(),'Applications')]")
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

    @FindBy(how = How.XPATH, using = "//table[contains(@id,'fii_front')]//tbody//tr//td")
    @CacheLookup
    private List<WebElement> tbFieldInvestigationInitiationElement;

    @FindBy(how = How.ID, using = "waiveOffAll")
    @CacheLookup
    private WebElement btnWaiveOffAllElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id,'waiveOffVerification')]//div[contains(@class,'modal-body')]//form[@id = 'waiveOffVerificationForm']")
    private WebElement popupFiiWaiveOffElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id,'waiveOffVerification')]//div[contains(@class,'modal-body')]//form[@id = 'waiveOffVerificationForm']//table[contains(@id,'fii_waiveOff')]//select[starts-with(@id, 'field_investigation_waiveOff_reasonCodeNO')]")
    @CacheLookup
    private WebElement selectReasonCodeElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@class,'modal-body')]//div[contains(@class,'modal-footer')]//button[contains(text(), 'Waive Off Verification')]")
    @CacheLookup
    private WebElement btnWaiveOffVerificationPopupElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id, 'move_to_next_stage_div')]//button[contains(@id, 'move_to_next_stage')]")
    @CacheLookup
    private WebElement btnMoveToNextStageElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'fii_front_wrapper']//table[@id = 'fii_front']//tbody//tr//td[5]")
    private WebElement tdCurrentVerificationStatus;

    @FindBy(how = How.XPATH, using = "//div[@id = 'dialog'][@class = 'modal-dialog']//div[@class = 'modal-body']//form[@id = 'waiveOffVerificationForm']//table[@id = 'fii_waiveOff']//tbody//tr//td")
    private List<WebElement> tableInDialogElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'dialog'][@class = 'modal-dialog']//div[@class = 'modal-body']//form[@id = 'waiveOffVerificationForm']//table[@id = 'fii_waiveOff']//tbody//tr//td")
    private WebElement tableInDialogElement2;

    @FindBy(how = How.XPATH, using = "//table[contains(@id,'fii_front')]//tbody//tr//td//a[contains(text(), 'View Details')]")
    @CacheLookup
    private WebElement btnViewDetails;

    @FindBy(how = How.XPATH, using = "//div[@id = 'dialog'][@class = 'modal-dialog']//div[@class = 'modal-body']//form[@id = 'waiveOffVerificationForm']//input[starts-with(@class,'btn btn-inverse')]")
    @CacheLookup
    private WebElement btnInverse;

    @FindBy(how = How.XPATH, using = "//div[@id = 'dialog'][@class = 'modal-dialog']//div[@class = 'modal-body']//form[@id = 'waiveOffVerificationForm']//table[@id = 'fii_waiveOff']//tbody//tr")
    private List<WebElement> tableFiiBtnAddElement;

    @FindBy(how = How.XPATH, using = "//div[starts-with(@class, 'ui-pnotify')]//div[@class ='alert ui-pnotify-container alert-error ui-pnotify-shadow']//div[@class = 'ui-pnotify-text']")
    @CacheLookup
    private WebElement popupError;


    public FV_WaiveFieldPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    public void setData(WaiveFieldDTO waiveFieldDTO, String user) {
//        WebDriverWait wait = new WebDriverWait(_driver, 10);
        Actions actions = new Actions(_driver);
        String stage = "";

        // ========== APPLICATION MANAGER =================
        stage = "APPLICATION MANAGER";

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

        // ========== WAIVE OFF ALL =================

        menuApplicationElement.click();

        applicationElement.click();

        await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(_driver::getTitle, is("Application Grid"));

        applicationAssignedNumberElement.clear();

        applicationAssignedNumberElement.sendKeys(waiveFieldDTO.getAppId());

        await("Find not found AppId!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tbApplicationAssignedElement.size() > 2);

        WebElement applicationIdAssignedNumberElement =_driver.findElement(new By.ByXPath("//table[@id='LoanApplication_Assigned']//tbody//tr//td[contains(@class,'tbl-left')]//a[contains(text(),'" + waiveFieldDTO.getAppId() +"')]"));

        applicationIdAssignedNumberElement.click();

        await("tbFieldInvestigationInitiationElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tbFieldInvestigationInitiationElement.size() > 2);

        System.out.println(tdCurrentVerificationStatus.getText());

        with().pollInterval(Duration.FIVE_SECONDS).
                await("button Waive Off All Element visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> btnWaiveOffAllElement.isDisplayed() && btnWaiveOffAllElement.isEnabled());

        btnWaiveOffAllElement.click();

        with().pollInterval(Duration.FIVE_SECONDS).
        await("Popup visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tableInDialogElement.size() > 2);

//        if ("Verification Not Initiated".equals(tdCurrentVerificationStatus.getText())){
//
//            btnWaiveOffAllElement.click();
//
//            await("Popup visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> tableInDialogElement.size() > 2);
//
//        }else{
//
//            btnViewDetails.click();
//
//            await("Popup visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> tableInDialogElement2.isDisplayed());
//
//            await("btnInverse visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> btnInverse.isDisplayed());
//
//            if(tableFiiBtnAddElement.size() < 2){
//
//                btnInverse.click();
//
//                await("Line waive visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                        .until(() -> tableFiiBtnAddElement.size() > 1);
//            }
//
//        }

        await("selectReasonCodeElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> selectReasonCodeElement.isDisplayed());

        selectReasonCodeElement.sendKeys("F.1.1 Bỏ qua bước FI");

        await("btnWaiveOffVerificationPopupElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> btnWaiveOffVerificationPopupElement.isDisplayed());

//        wait.until(ExpectedConditions.elementToBeClickable(btnWaiveOffVerificationPopupElement));
//        btnWaiveOffVerificationPopupElement.click();

        actions.moveToElement(btnWaiveOffVerificationPopupElement).sendKeys(btnWaiveOffVerificationPopupElement, Keys.ENTER).perform();

//        JavascriptExecutor btnWaiveOffVerificationPopup = (JavascriptExecutor)_driver;
//        btnWaiveOffVerificationPopup.executeScript("arguments[0].click();", btnWaiveOffVerificationPopupElement);

//        await(popupError.getText()).atMost(5, TimeUnit.SECONDS)
//                .until(() -> "false".equals(popupError.isDisplayed()));

        with().pollInterval(Duration.FIVE_SECONDS).
                await("btnMoveToNextStageElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> btnMoveToNextStageElement.isDisplayed());

        with().pollInterval(Duration.FIVE_SECONDS).
                await("btnMoveToNextStageElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> btnMoveToNextStageElement.isEnabled());


//        wait.until(ExpectedConditions.elementToBeClickable(btnMoveToNextStageElement));
//        btnMoveToNextStageElement.click();

        actions.moveToElement(btnMoveToNextStageElement).sendKeys(btnMoveToNextStageElement, Keys.ENTER).perform();

        await("Work flow failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(_driver::getTitle, is("Application Grid"));

        Utilities.captureScreenShot(_driver);

    }
}
