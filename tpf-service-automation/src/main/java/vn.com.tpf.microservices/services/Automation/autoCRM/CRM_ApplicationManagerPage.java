package vn.com.tpf.microservices.services.Automation.autoCRM;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringEscapeUtils;
import org.awaitility.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.with;

@Getter
public class CRM_ApplicationManagerPage {
    private WebDriver _driver;

    @FindBy(how = How.ID, using = "applicationManagerForm1")
    private WebElement applicationManagerFormElement;

    @FindBy(how = How.ID, using = "appManager_lead_application_number")
    private WebElement applicationNumberElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'applicationManagerForm1')]//input[@type='button']")
    private WebElement searchApplicationElement;

    @FindBy(how = How.XPATH, using = "//table[@id='applicationTable']//tbody//tr")
    private List<WebElement> tdApplicationElement;

    @FindBy(how = How.ID, using = "showTasks")
    private WebElement showTaskElement;

    @FindBy(how = How.ID, using = "taskTableDiv")
    private WebElement taskTableDivElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'edit_button0')]//input[@type='button']")
    private WebElement editElement;

    @FindBy(how = How.ID, using = "Text_selected_user0")
    private WebElement textSelectUserElement;

//    @FindBy(how = How.ID, using = "holder")
    @FindBy(how = How.XPATH, using = "//div[@id = 'content_selected_user0']//ul[@id = 'holder']")
    private WebElement textSelectUserContainerElement;

    @FindBy(how = How.XPATH, using = "//a[contains(@id, 'listitem_selected_user')]")
    private List<WebElement> textSelectUserOptionElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'with_branch')]//input[@type='submit']")
    private WebElement saveTaskElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]//li[contains(@class,'application-column-loan')]//span[contains(text(),'Applications')]")
    private WebElement applicationElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]")
    private WebElement menuApplicationElement;


    @FindBy(how = How.XPATH, using = "//*[contains(@class,'backSaveBtns')]//input[@type='button']")
    private WebElement backBtnElement;

    @FindBy(how = How.XPATH, using = "//table[@id='applicationTable']//tbody//tr[1]//td[1]")
    private WebElement applicationTableAppIDElement;

    //===Application Grid

    @FindBy(how = How.ID, using = "LoanApplication_Assigned_wrapper")
    @CacheLookup
    private WebElement assignWrapperElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'LoanApplication_Assigned_filter')]//input")
    @CacheLookup
    private WebElement searchAssignQueryElement;

    @FindBy(how = How.XPATH, using = "//table[@id='LoanApplication_Assigned']//tbody//tr//td")
    @CacheLookup
    private List<WebElement> tdAssignElement;

    //Team

    @FindBy(how = How.ID, using = "Text_team_Branch0")
    private WebElement textSelectTeamNameElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'content_team_Branch0']//ul[@id = 'holder']")
    private WebElement textSelectTeamNameContainerElement;

    @FindBy(how = How.XPATH, using = "//li[contains(@id, 'listitem_team_Branch')]")
    private List<WebElement> liSelectTeamNameOptionElement;

    @FindBy(how = How.XPATH, using = "//a[contains(@id, 'listitem_team_Branch')]")
    private List<WebElement> textSelectTeamNameOptionElement;


    public CRM_ApplicationManagerPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    @SneakyThrows
    public void setData(String appId, String user) {
        String strTeamName = "TPF DE TOPUP_XSELL";

        await("appManager_lead_application_number visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> applicationManagerFormElement.isDisplayed());

        applicationNumberElement.sendKeys(appId);
        searchApplicationElement.click();

        with().pollInterval(Duration.FIVE_SECONDS).
        await("tdApplicationElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tdApplicationElement.size() > 0);

        await("showTaskElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> showTaskElement.isDisplayed());

        showTaskElement.click();

        await("taskTableDivElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> taskTableDivElement.isDisplayed());

        await("editElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> editElement.isDisplayed());

        editElement.click();

        await("Textbox select Team enable Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> textSelectTeamNameElement.isEnabled());

        textSelectTeamNameElement.clear();
        textSelectTeamNameElement.sendKeys(strTeamName);

        with().pollInterval(Duration.FIVE_SECONDS).
        await("Select Team Name Container displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> textSelectTeamNameContainerElement.isDisplayed());

        int listTeamNameOption = liSelectTeamNameOptionElement.size();

        await("No Team Name Found timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> listTeamNameOption > 0);

        for (WebElement eTeamName : liSelectTeamNameOptionElement) {
            if (!Objects.isNull(eTeamName.getAttribute("username")) && StringEscapeUtils.unescapeJava(eTeamName.getAttribute("username")).equals(strTeamName)) {
                eTeamName.click();
                break;
            }
        }

        Utilities.captureScreenShot(_driver);

        await("textSelectUserElement enable Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> textSelectUserElement.isEnabled());

        textSelectUserElement.clear();
        textSelectUserElement.sendKeys(user);

        with().pollInterval(Duration.FIVE_SECONDS).
        await("textSelectUserContainerElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> textSelectUserContainerElement.isDisplayed());

        int listUserOption = textSelectUserOptionElement.size();

        await("No User Auto Found timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> listUserOption > 0);

        for (WebElement eUserName : textSelectUserOptionElement) {
            if (!Objects.isNull(eUserName.getAttribute("title")) && StringEscapeUtils.unescapeJava(eUserName.getAttribute("title")).equals(user)) {
                eUserName.click();
                break;
            }
        }
        Utilities.captureScreenShot(_driver);
        saveTaskElement.click();
    }

    public void applicationGrid(String appId) {

        await("assignWrapperElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> assignWrapperElement.isDisplayed());

        searchAssignQueryElement.sendKeys(appId);

        await("tdLeadAssignElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tdAssignElement.size() > 2);

        WebElement applicationIdAssignedNumberElement=_driver.findElement(new By.ByXPath("//table[@id='LoanApplication_Assigned']//tbody//tr//td[contains(@class,'tbl-left')]//a[contains(text(),'" + appId +"')]"));

        await("applicationIdAssignedNumberElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> applicationIdAssignedNumberElement.isDisplayed());

        applicationIdAssignedNumberElement.click();

    }
}