package vn.com.tpf.microservices.services.Automation.autoCRM;

import lombok.Getter;
import org.apache.commons.lang.StringEscapeUtils;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.models.AutoCRM.CRM_QuickLeadDTO;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Getter
public class CRM_QuickLeadPage {
    private WebDriver _driver;

    @FindBy(how = How.ID, using = "productTypeIdd1_chzn")
    @CacheLookup
    private WebElement productTypeElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'productTypeIdd1_chzn_o_')]")
    @CacheLookup
    private List<WebElement> productTypeOptionElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'productTypeIdd1_chzn')]//input")
    private WebElement productTypeInputElement;

    @FindBy(how = How.ID, using = "individual")
    @CacheLookup
    private WebElement individualElement;

    @FindBy(how = How.ID, using = "create_lead")
    @CacheLookup
    private WebElement createLeadElement;

    @FindBy(how = How.ID, using = "quickLeadForm")
    @CacheLookup
    private WebElement quickLeadDefailsFormElement;


    @FindBy(how = How.ID, using = "loan_product_chzn")
    @CacheLookup
    private WebElement loanProductElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'loan_product_chzn_o_')]")
    @CacheLookup
    private List<WebElement> loanProductOptionElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'loan_product_chzn')]//input")
    @CacheLookup
    private WebElement loanProductInputElement;

    @FindBy(how = How.ID, using = "amount_loanAmount1")
    @CacheLookup
    private WebElement amountLoanElement;

    @FindBy(how = How.ID, using = "firstName")
    @CacheLookup
    private WebElement firstNameElement;

    @FindBy(how = How.ID, using = "lastname")
    @CacheLookup
    private WebElement lastNameElement;

    @FindBy(how = How.ID, using = "Text_city")
    @CacheLookup
    private WebElement cityElement;

    @FindBy(how = How.ID, using = "auto-container")
    @CacheLookup
    private WebElement cityContainerElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'listitem_city')]")
    @CacheLookup
    private List<WebElement> cityOptionElement;

    @FindBy(how = How.ID, using = "sorcingChannel_chzn")
    @CacheLookup
    private WebElement sourcingChannelElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'sorcingChannel_chzn_o_')]")
    @CacheLookup
    private List<WebElement> sourcingChannelOptionElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'sorcingChannel_chzn')]//input")
    @CacheLookup
    private WebElement sourcingChanneInputElement;

    @FindBy(how = How.ID, using = "alternateChannelMode_chzn")
    @CacheLookup
    private WebElement alternateChannelModeElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'alternateChannelMode_chzn_o_')]")
    @CacheLookup
    private List<WebElement> alternateChannelModeOptionElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'alternateChannelMode_chzn')]//input")
    @CacheLookup
    private WebElement alternateChannelModeInputElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@class, 'employee')]")
    @CacheLookup
    private WebElement employeeElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'Text_employeeName')]")
    @CacheLookup
    private WebElement employeeNameElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'Text_employeeNumber')]")
    @CacheLookup
    private WebElement employeeNumberElement;

    @FindBy(how = How.ID, using = "saveButtonLeadBottom")
    @CacheLookup
    private WebElement saveButtonleadElement;


    public CRM_QuickLeadPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    public void setData(CRM_QuickLeadDTO quickLead) {
        Actions actions = new Actions(_driver);
        actions.moveToElement(productTypeElement).click().build().perform();

        await("productTypeOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> productTypeOptionElement.size() >0);

        productTypeInputElement.sendKeys(quickLead.getProductTypeCode());
        productTypeInputElement.sendKeys(Keys.ENTER);

        individualElement.click();
        createLeadElement.click();

        await("quickLeadDefailsFormElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> quickLeadDefailsFormElement.isDisplayed());

        actions.moveToElement(loanProductElement).click().build().perform();

        await("productTypeOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> loanProductOptionElement.size() >0);

        loanProductInputElement.sendKeys(quickLead.getProductCode());
        loanProductInputElement.sendKeys(Keys.ENTER);

        amountLoanElement.sendKeys(quickLead.getLoanAmountRequested());
        firstNameElement.sendKeys(quickLead.getFirstName());
        lastNameElement.sendKeys(quickLead.getLastName());


        cityElement.sendKeys(quickLead.getCity());
        await("cityContainerElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> cityContainerElement.isDisplayed());

        await("cityOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> cityOptionElement.size() > 0);

        for(WebElement e: cityOptionElement) {
            if(!Objects.isNull(e.getAttribute("username")) && StringEscapeUtils.unescapeJava(e.getAttribute("username")).equals(Utilities.convertToTitleCaseSplitting(quickLead.getCity()))) {
                e.click();
                break;
            }
        }

        actions.moveToElement(sourcingChannelElement).click().build().perform();

        await("sourcingChannelElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> sourcingChannelOptionElement.size() >0);

        sourcingChanneInputElement.sendKeys(quickLead.getSourcingChannel());
        sourcingChanneInputElement.sendKeys(Keys.ENTER);

        if ("ALTERNATE_CHANNEL".equals(quickLead.getSourcingChannel())){
            actions.moveToElement(alternateChannelModeElement).click().build().perform();

            await("alternateChannelModeElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> alternateChannelModeOptionElement.size() >0);

            alternateChannelModeInputElement.sendKeys(quickLead.getAlternateChannelMode());
            alternateChannelModeInputElement.sendKeys(Keys.ENTER);
        }

        if ("CALL_CENTER".equals(quickLead.getSourcingChannel())){

            await("Employee Element loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> employeeElement.isDisplayed());

            employeeNameElement.clear();
            employeeNameElement.sendKeys(quickLead.getEmemployeeName());

            employeeNumberElement.clear();
            employeeNumberElement.sendKeys(quickLead.getEmemployeeNumber());
        }

        saveButtonleadElement.click();
    }
}
