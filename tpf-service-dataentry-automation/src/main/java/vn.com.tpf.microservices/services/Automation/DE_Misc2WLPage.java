package vn.com.tpf.microservices.services.Automation;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.models.Automation.MiscVinIdDTO;

@Getter
public class DE_Misc2WLPage {
    @FindBy(how = How.ID, using = "applicationChildTabs_miscDynamicForm_2")
    @CacheLookup
    private WebElement tabMisc2WLElement;

    @FindBy(how = How.XPATH, using = "//ul[@id='applicationChildTabs']/li/a[contains(@onclick,'2WL')]")
    @CacheLookup
    private WebElement tabMisc2WLElementByName;

    @FindBy(how = How.ID, using = "amount_T_2WL_Goods_Price_2WL_0")
    @CacheLookup
    private WebElement goodPriceElement;

    @FindBy(how = How.ID, using = "T_2WL_Goods_Code_2WL_0")
    @CacheLookup
    private WebElement goodCodeElement;

    @FindBy(how = How.ID, using = "T_VIN_Code_2WL_0")
    @CacheLookup
    private WebElement vinCodeElement;

    @FindBy(how = How.ID, using = "T_2WL_VEN_Code_2WL_0")
    @CacheLookup
    private WebElement venCodeElement;

    @FindBy(how = How.ID, using = "T_2WL_Product_Name_2WL_0")
    @CacheLookup
    private WebElement productName;


    @FindBy(how = How.ID, using = "T_2WL_Sales_Channel_2WL_1")
    @CacheLookup
    private WebElement saleChannelElement;

    @FindBy(how = How.ID, using = "T_2WL_Dealer_Code_2WL_1")
    @CacheLookup
    private WebElement dealerCodeElement;

    @FindBy(how = How.ID, using = "amount_T_2WL_Down_Payment_2WL_1")
    @CacheLookup
    private WebElement downPaymentElement;

    @FindBy(how = How.ID, using = "dynSave")
    @CacheLookup
    private WebElement btnSaveElement;

    public DE_Misc2WLPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void setData(MiscVinIdDTO data) {
        goodCodeElement.sendKeys(data.getGoodCode());
        goodPriceElement.sendKeys(data.getGoodPrice());
        vinCodeElement.sendKeys(data.getVinCode());
        venCodeElement.sendKeys(data.getVenCode());
        productName.sendKeys(data.getProductName());
        saleChannelElement.sendKeys(data.getSaleChannel());
        dealerCodeElement.sendKeys(data.getDealerCode());
        downPaymentElement.sendKeys(data.getDownPayment());
    }
}
