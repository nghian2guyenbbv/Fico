package vn.com.tpf.microservices.services.Automation.lending;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.models.Automation.MiscFptDTO;

import java.util.List;

@Getter
public class LD_MiscFptPage {
    private WebDriver _driver;
    @FindBy(how = How.ID, using = "applicationChildTabs_miscDynamicForm_1")
    @CacheLookup
    private WebElement tabMiscFptElement;

    @FindBy(how = How.ID, using = "genericForm_FPT-panel-form")
    @CacheLookup
    private WebElement tabMiscFptContainerElement;

    @FindBy(how = How.ID, using = "FPT_MODEL_1_FPT_0")
    @CacheLookup
    private WebElement modelElement;

    @FindBy(how = How.ID, using = "FPT_GOOD_CODE_1_FPT_0")
    @CacheLookup
    private WebElement goodCodeElement;

    @FindBy(how = How.ID, using = "FPT_GOOD_TYPE_1_FPT_0_chzn")
    @CacheLookup
    private WebElement goodTypeElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'FPT_GOOD_TYPE_1_FPT_0_chzn_o_')]")
    @CacheLookup
    private List<WebElement> goodTypeOptionElement;

    @FindBy(how = How.ID, using = "FPT_PRODUCT_QUANTITY_1_FPT_0")
    @CacheLookup
    private WebElement quantityElement;

    @FindBy(how = How.ID, using = "amount_FPT_GOOD_PRICE_1_FPT_0")
    @CacheLookup
    private WebElement goodPriceElement;

    @FindBy(how = How.ID, using = "FPT_Down_Payment_3_FPT_3")
    @CacheLookup
    private WebElement downPaymentElement;

    @FindBy(how = How.ID, using = "Employee_card_no_FPT_3")
    @CacheLookup
    private WebElement employeeCardNumElement;

    @FindBy(how = How.ID, using = "amount_FPT_SAMSUNG_CREDIT_LIMIT_3_FPT_3")
    @CacheLookup
    private WebElement creditLimitElement;

    @FindBy(how = How.ID, using = "dynSave")
    @CacheLookup
    private WebElement btnSaveElement;

    public LD_MiscFptPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver=driver;
    }

    public void setData(MiscFptDTO data) {
//        modelElement.sendKeys(data.getModel());
//        goodCodeElement.sendKeys(data.getGoodCode());
//        goodTypeElement.click();
//        await("channelOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                .until(() -> goodTypeOptionElement.size() > 0);
//        Utilities.chooseDropdownValue(data.getGoodType(), goodTypeOptionElement);
//        quantityElement.sendKeys(data.getQuantity());
//        goodPriceElement.sendKeys(data.getGoodPrice());

        for(int i= 0; i < data.getProductDetails().size(); i++) {
            int j=i+1;
            _driver.findElement(By.id("FPT_MODEL_"+j+"_FPT_"+i)).sendKeys(data.getProductDetails().get(i).getModel());
            _driver.findElement(By.id("FPT_GOOD_CODE_"+j+"_FPT_"+i)).sendKeys(data.getProductDetails().get(i).getGoodCode());
            _driver.findElement(By.id("FPT_GOOD_TYPE_"+j+"_FPT_"+i+"_chzn")).click();

            WebElement we=_driver.findElement(By.xpath("//*[@id='FPT_GOOD_TYPE_"+j+"_FPT_"+i+"_chzn']//li[contains(@class, 'active-result') and text() = '" + data.getProductDetails().get(i).getGoodType() + "']"));
            we.click();

            //SeleniumUtils.findByXpath(driver,customerErrorResponse,"//*[@id='FPT_GOOD_TYPE_"+j+"_FPT_"+i+"_chzn']//li[contains(@class, 'active-result') and text() = '" + test.get(i).get(2).toString() + "']",stage ,test.get(i).get(2).toString()).click();

            _driver.findElement(By.id("FPT_PRODUCT_QUANTITY_"+j+"_FPT_"+i)).sendKeys(data.getProductDetails().get(i).getQuantity());
            _driver.findElement(By.id("amount_FPT_GOOD_PRICE_"+j+"_FPT_"+i)).sendKeys(data.getProductDetails().get(i).getGoodPrice());
        }

        creditLimitElement.sendKeys("0");
        downPaymentElement.sendKeys(data.getDownPayment());
        employeeCardNumElement.sendKeys(data.getEmployeeCardNum());
    }

}
