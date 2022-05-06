package vn.com.tpf.microservices.services.Automation.returnQuery;

import lombok.Getter;
import org.apache.commons.lang.StringEscapeUtils;
import org.awaitility.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.with;
import static org.hamcrest.Matchers.is;

@Getter
public class ApplicationManagerPage {
    private WebDriver _driver;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]")
    private WebElement menuApplicationElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]//div[contains(@class,'one-col')][3]//li//a[contains(text(),'Application Manager')]")
    @CacheLookup
    private WebElement applicationManagerElement;

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

    @FindBy(how = How.ID, using = "holder")
    private WebElement textSelectUserContainerElement;

    @FindBy(how = How.XPATH, using = "//a[contains(@id, 'listitem_selected_user')]")
    private List<WebElement> textSelectUserOptionElement;

    @FindBy(how = How.XPATH, using = "//*[@id='with_branch']/input")
    private WebElement saveTaskElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]//li[contains(@class,'application-column-loan')]//span[contains(text(),'Applications')]")
    private WebElement applicationElement;

    @FindBy(how = How.XPATH, using = "//*[@id='LoanApplication_Assigned_wrapper']/div[1]/div/div[2]/div[1]/table/thead[1]/tr/th/input")
    private WebElement applicationAssignedNumberElement;

    @FindBy(how = How.XPATH, using = "//table[@id='LoanApplication_Assigned']//tbody//tr//td")
    private List<WebElement> tbApplicationAssignedElement;

    public ApplicationManagerPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    public void setData(String appId,String user) {

        applicationNumberElement.clear();
        applicationNumberElement.sendKeys(appId);

        searchApplicationElement.click();

        with().pollInterval(Duration.FIVE_SECONDS).
        await("Application Number Not Found!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tdApplicationElement.size() > 2);

        await("showTaskElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> showTaskElement.isDisplayed());

        await("Stage SALES_QUEUE wrong Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> "SALES QUEUE".equals(tdCheckStageApplicationElement.getText()));

        if (!"SALES QUEUE".equals(tdCheckStageApplicationElement.getText())){
            return;
        }

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

        with().pollInterval(Duration.FIVE_SECONDS).
        await("Select user visibale!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> textSelectUserContainerElement.isDisplayed());


        await("User Auto not found!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> textSelectUserOptionElement.size() > 0);

        for (WebElement e : textSelectUserOptionElement) {
            e.click();
            break;
        }
        Utilities.captureScreenShot(_driver);
        saveTaskElement.click();

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void assignNotACCA(String appId,String user) {

        applicationNumberElement.sendKeys(appId);
        searchApplicationElement.click();
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

        await("textSelectUserElement enable Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> textSelectUserElement.isEnabled());

        textSelectUserElement.clear();
        textSelectUserElement.sendKeys(user);

        await("Select user visibale!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> textSelectUserContainerElement.isDisplayed());

        await("User Auto not found!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> textSelectUserOptionElement.size() > 0);

        for (WebElement e : textSelectUserOptionElement) {
            e.click();
            break;
        }

        Utilities.captureScreenShot(_driver);

        saveTaskElement.click();
    }

    public void setDataACCA(String user) {

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
        await("Select user visibale!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> textSelectUserContainerElement.isDisplayed());


        await("User Auto not found!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> textSelectUserOptionElement.size() > 0);

        for (WebElement e : textSelectUserOptionElement) {
            e.click();
            break;
        }
        Utilities.captureScreenShot(_driver);
        saveTaskElement.click();
    }

    public void keyActionMoveNextStage(){
        Actions actionKey = new Actions(_driver);
        WebElement divTimeRemaining = _driver.findElement(By.xpath("//div[@id = 'heading']//div[@id = 'timer_containerappTat']"));

        actionKey.moveToElement(divTimeRemaining).click();
        actionKey.sendKeys(Keys.TAB).build().perform();
        actionKey.sendKeys(Keys.TAB).build().perform();
        actionKey.sendKeys(Keys.TAB).build().perform();
        actionKey.sendKeys(Keys.ENTER).build().perform();
    }

}
