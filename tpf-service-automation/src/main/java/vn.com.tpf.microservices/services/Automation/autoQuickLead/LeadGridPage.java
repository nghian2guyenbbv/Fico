package vn.com.tpf.microservices.services.Automation.autoQuickLead;

import lombok.Getter;
import org.awaitility.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.utilities.Constant;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.with;
import static org.hamcrest.Matchers.is;

@Getter
public class LeadGridPage {
    private WebDriver _driver;

    @FindBy(how = How.ID, using = "leadData")
    @CacheLookup
    private WebElement leadWrapperElement;

    @FindBy(how = How.XPATH, using = "//*[@id='lead_assigned_wrapper']/div[1]/div/div[1]/div[1]/div/table/thead[1]/tr/th/input")
    @CacheLookup
    private WebElement searchQueryElement;

    @FindBy(how = How.XPATH, using = "//table[@id='lead_assigned']//tbody//tr//td//a")
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

    @FindBy(how = How.XPATH, using="//span[@class='ui-pnotify-history-pulldown icon-chevron-down']")
    @CacheLookup
    private WebElement spanAllNotifyElement;

    @FindBy(how = How.XPATH, using="//div[@class='ui-pnotify-history-container well']")
    @CacheLookup
    private WebElement divAllNotifyElement;

    @FindBy(how = How.XPATH, using="//button[@class='ui-pnotify-history-all btn']")
    @CacheLookup
    private WebElement btnAllNotifyElement;

    @FindBy(how = How.XPATH, using="//div[@class='alert ui-pnotify-container alert-success ui-pnotify-shadow']//div[@class='ui-pnotify-text']")
    @CacheLookup
    private List<WebElement> notifyTextSuccessElement;


    public LeadGridPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    public void setData(String leadId) {
        await("leadWrapperElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> leadWrapperElement.isDisplayed());

        WebElement assigned = _driver.findElement(By.xpath("//*[@id='assigned']/a"));
        assigned.click();

        searchQueryElement.sendKeys(leadId);
        searchQueryElement.sendKeys(Keys.ENTER);

        with().pollInterval(Duration.FIVE_SECONDS).
                await("tdLeadElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tdLeadElement.size() > 0);

        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement webElement=_driver.findElement(new By.ByXPath("//*[@id='lead_assigned']/tbody/tr/td/a[contains(text(),'" + leadId +"')]"));

        with().pollInterval(Duration.FIVE_SECONDS).await("aLeadElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> webElement.isDisplayed());

        webElement.click();

        with().pollInterval(Duration.FIVE_SECONDS).await("Lead Page Visibale Loading").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(_driver::getTitle, is("Lead"));

    }
}
