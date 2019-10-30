package vn.com.tpf.microservices.services.Automation;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.models.Automation.MiscFptDTO;
import vn.com.tpf.microservices.utilities.Utilities;

import java.util.List;
import java.util.Map;

@Getter
public class DE_MiscFptPage {
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

    @FindBy(how = How.ID, using = "dynSave")
    @CacheLookup
    private WebElement btnSaveElement;

    public DE_MiscFptPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
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
        downPaymentElement.sendKeys(data.getDownPayment());
        employeeCardNumElement.sendKeys(data.getEmployeeCardNum());
    }

    public void validInOutData(Map<String, String> mapValue, String model, String goodCode, String goodType, String quantity,
                               String goodPrice, String downPayment, String employeeCardNum) throws Exception {
        Utilities.checkInput(mapValue, "MiscFptPage_Model", model, modelElement);
        Utilities.checkInput(mapValue, "MiscFptPage_GoodCode", goodCode, goodCodeElement);
        Utilities.checkInput(mapValue, "MiscFptPage_GoodType", goodType, goodTypeOptionElement);
        Utilities.checkInput(mapValue, "MiscFptPage_Quantity", quantity, quantityElement);
        Utilities.checkInput(mapValue, "MiscFptPage_GoodPrice", goodPrice, goodPriceElement);
        Utilities.checkInput(mapValue, "MiscFptPage_DownPayment", downPayment, downPaymentElement);
        Utilities.checkInput(mapValue, "MiscFptPage_EmployeeCardNum", employeeCardNum, employeeCardNumElement);
    }
}
