package vn.com.tpf.microservices.services.Automation.autoAllocation;

import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringEscapeUtils;
import org.awaitility.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

@Getter
public class AutoAssignAllocationPage {
    private WebDriver _driver;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]")
    @CacheLookup
    private WebElement menuApplicationElement;

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

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'with_branch')]//input[@type='submit']")
    private WebElement saveTaskElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]//li[contains(@class,'application-column-loan')]//span[contains(text(),'Applications')]")
    private WebElement applicationElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'backSaveBtns')]//input[@type='button']")
    private WebElement backBtnElement;

    @FindBy(how = How.XPATH, using = "//table[@id='applicationTable']//tbody//tr[1]//td[1]")
    private WebElement applicationTableAppIDElement;

    @FindBy(how = How.XPATH, using = "//table[@id = 'applicationTable']//td[5]")
    private WebElement userAssignManager;

    @FindBy(how = How.XPATH, using = "//li[@class = 'buttonList']//input[@class = 'swFB']")
    private List<WebElement> listButtonSelectUserElement;

    @FindBy(how = How.XPATH, using = "//input[@id = 'paginate'][@value = '>']")
    private WebElement nextButtonElement;

    @FindBy(how = How.XPATH, using = "//input[@class = 'swFB'][@value = '>>']")
    private WebElement nextEndButtonElement;

    public AutoAssignAllocationPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    public void setData(String appId,String user) throws InterruptedException {
        await("appManager_lead_application_number visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> applicationManagerFormElement.isDisplayed());

        Utilities.captureScreenShot(_driver);
        applicationNumberElement.sendKeys(appId);
        searchApplicationElement.click();

        await("Application Id not found !!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tdApplicationElement.size() > 2);

        await("showTaskElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> showTaskElement.isDisplayed());

        Utilities.captureScreenShot(_driver);
        showTaskElement.click();

        await("taskTableDivElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> taskTableDivElement.isDisplayed());

        await("editElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> editElement.isDisplayed());

        Utilities.captureScreenShot(_driver);
        editElement.click();

        await("textSelectUserElement enable Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> textSelectUserElement.isEnabled());

        textSelectUserElement.clear();
        textSelectUserElement.sendKeys(user);
        await("textSelectUserContainerElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> textSelectUserContainerElement.isDisplayed());

        int listUserAssigned = textSelectUserOptionElement.size();

        await("No User Found!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> listUserAssigned > 0);

        boolean checkButtonPopup = _driver.findElements(By.xpath("//input[@class = 'swFB'][@value = '>>']")).size() != 0;

        if (checkButtonPopup){
            selectUserOption(user);
        }else{
            for (WebElement e : textSelectUserOptionElement) {
                if (!Objects.isNull(e.getAttribute("title")) && StringEscapeUtils.unescapeJava(e.getAttribute("title")).equals(user)) {
                    e.click();
                    break;
                }
            }
        }

        Utilities.captureScreenShot(_driver);
        saveTaskElement.click();
    }

    public void selectUserOption(String userSelect) throws InterruptedException {
        Actions actions = new Actions(_driver);

        int sizeUserSelectPage = Integer.parseInt(nextEndButtonElement.getAttribute("id"));

        for (int i = 1; i <= sizeUserSelectPage; i++){
            String pageActive = _driver.findElement(By.xpath("//input[@class = 'swFB'][@value = '" + i + "'][@type = 'button']")).getAttribute("id");
            if ("active".equals(pageActive)){
                for (WebElement e : textSelectUserOptionElement) {
                    if (!Objects.isNull(e.getAttribute("title")) && StringEscapeUtils.unescapeJava(e.getAttribute("title")).equals(userSelect)) {
                        e.click();
                        return;
                    }
                }

                int pageNum = i + 1;

                WebElement nextButtonElements =  _driver.findElement(By.xpath("//input[@class = 'swFB'][@value = '" + pageNum + "'][@type = 'button']"));

                JavascriptExecutor btnMoveToNextStage = (JavascriptExecutor)_driver;
                btnMoveToNextStage.executeScript("arguments[0].click();", nextButtonElements);

//                actions.moveToElement(nextButtonElements).click().build().perform();

            }
        }
    }
}
