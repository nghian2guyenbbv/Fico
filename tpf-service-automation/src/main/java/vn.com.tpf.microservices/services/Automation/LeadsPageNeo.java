package vn.com.tpf.microservices.services.Automation;

import lombok.Getter;
import org.awaitility.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.utilities.Constant;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.with;

@Getter
public class LeadsPageNeo {
    private WebDriver _driver;

    @FindBy(how = How.ID, using = "container-no-tpl")
    @CacheLookup
    private WebElement leadWrapperElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'lead_pool_wrapper')]//input")
    @CacheLookup
    private WebElement searchQueryElement;

    @FindBy(how = How.XPATH, using = "//table[@id='lead']//tbody//tr//td")
    @CacheLookup
    private List<WebElement> tdLeadElement;

    @FindBy(how = How.XPATH, using = "//table[@id='lead']//tbody//tr//td[contains(@class,'tbl-left')]//a")
    @CacheLookup
    private WebElement aLeadElement;

    @FindBy(how = How.CLASS_NAME, using="ui-pnotify")
    @CacheLookup
    private WebElement notifyElement;

    @FindBy(how = How.XPATH, using="//div[@class='ui-pnotify-text']")
    private WebElement notifyTextElement;

    @FindBy(how = How.XPATH, using="//span[@class='ui-pnotify-history-pulldown glyphicon glyphicon-chevron-down']")
    @CacheLookup
    private WebElement spanAllNotifyElement;

    @FindBy(how = How.XPATH, using="//div[@class='ui-pnotify-history-container well ui-pnotify-history-fixed']")
    @CacheLookup
    private WebElement divAllNotifyElement;

    @FindBy(how = How.XPATH, using="//button[@class='ui-pnotify-history-all btn btn-default']")
    @CacheLookup
    private WebElement btnAllNotifyElement;

    @FindBy(how = How.XPATH, using="//div[@class='alert ui-pnotify-container alert-success ui-pnotify-shadow']//div[@class='ui-pnotify-text']")
    @CacheLookup
    private List<WebElement> notifyTextSuccessElement;


    public LeadsPageNeo(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    public void setData(String leadId) {
        Actions actions = new Actions(_driver);
        await("leadWrapperElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> leadWrapperElement.isDisplayed());

        searchQueryElement.sendKeys(leadId);

        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        with().pollInterval(Duration.FIVE_SECONDS).
        await("tdLeadElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tdLeadElement.size() > 2);

        WebElement webElement=_driver.findElement(new By.ByXPath("//table[@id='lead']//tbody//tr//td[contains(@class,'tbl-left')]//a[contains(text(),'" + leadId +"')]"));

        with().pollInterval(Duration.FIVE_SECONDS).
        await("aLeadElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> webElement.isDisplayed());

        webElement.click();

    }

}
