package vn.com.tpf.microservices.services.Automation.lending;


import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.models.Automation.MiscFrmAppDtlDTO;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Getter
public class LD_MiscFrmAppDtlPage {
    @FindBy(how = How.ID, using = "applicationChildTabs_miscDynamicForm_0")
    @CacheLookup
    private WebElement tabMiscFrmAppDtlElement;

    @FindBy(how = How.XPATH, using = "//ul[@id='applicationChildTabs']/li/a[contains(@onclick,'frmAppDtl')]")
    @CacheLookup
    private WebElement tabMiscFrmAppDtlElementByName;

    //dynamic-form
    @FindBy(how = How.ID, using = "accordion_advSearch")
    @CacheLookup
    private WebElement tabMiscFrmAppDtlContainerElement;

    //UAT
//    @FindBy(how = How.ID, using = "Loan_purpose_1_frmAppDtl_0_chzn")
//    @CacheLookup
//    private WebElement loanPurposeElement;

//    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'Loan_purpose_1_frmAppDtl_0_chzn_o_')]")
//    @CacheLookup
//    private List<WebElement> loanPurposeOptionElement;
//
    //PRO
    //production khac id
    @FindBy(how = How.ID, using = "loanpurpose_frmAppDtl_0_chzn")
    @CacheLookup
    private WebElement loanPurposeElement;

    //production khac
    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'loanpurpose_frmAppDtl_0_chzn_o_')]")
    @CacheLookup
    private List<WebElement> loanPurposeOptionElement;


    @FindBy(how = How.ID, using = "householdmembers_frmAppDtl_1")
    @CacheLookup
    private WebElement numberOfDependentsElement;

    @FindBy(how = How.ID, using = "house_ownership_frmAppDtl_1_chzn")
    @CacheLookup
    private WebElement houseOwnerShipElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'house_ownership_frmAppDtl_1_chzn_o_')]")
    @CacheLookup
    private List<WebElement> houseOwnerShipOptionElement;

    @FindBy(how = How.ID, using = "amount_mortgage_payment_cost_frmAppDtl_1")
    @CacheLookup
    private WebElement mortgagePaymentCostElement;

    @FindBy(how = How.ID, using = "newbankcardnumber_frmAppDtl_4")
    @CacheLookup
    private WebElement newBankCardNumberElement;

    @FindBy(how = How.ID, using = "salesagentcode_frmAppDtl_4")
    @CacheLookup
    private WebElement salesAgentCodeElement;

    @FindBy(how = How.ID, using = "Max_req_EIR_frmAppDtl_5")
    @CacheLookup
    private WebElement maxRequestRateElement;

    @FindBy(how = How.ID, using = "dynSave")
    @CacheLookup
    private List<WebElement> btnSaveElement;

    @FindBy(how = How.ID, using = "move_to_next_stage")
    @CacheLookup
    private WebElement btnMoveToNextStageElement;

    public LD_MiscFrmAppDtlPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void setData(MiscFrmAppDtlDTO data) {
        await("loanPurposeElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> loanPurposeElement.isDisplayed() && loanPurposeElement.isEnabled());
        loanPurposeElement.click();
        await("loanPurposeOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> loanPurposeOptionElement.size() > 0);
        Utilities.chooseDropdownValue(data.getLoanPurpose(), loanPurposeOptionElement);

        numberOfDependentsElement.sendKeys(data.getNumOfDependents());

        houseOwnerShipElement.click();
        await("houseOwnerShipOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> houseOwnerShipOptionElement.size() > 0);
        Utilities.chooseDropdownValue(data.getHouseOwnership(), houseOwnerShipOptionElement);

        mortgagePaymentCostElement.sendKeys(data.getMortgagePaymentCost());
        newBankCardNumberElement.sendKeys(data.getNewBankCardNumber());
        salesAgentCodeElement.sendKeys(data.getSalesAgentCode());
        maxRequestRateElement.sendKeys(data.getMaxRequestRate());
    }

    public WebElement _getBtnSaveElement() {
    	for(WebElement e: btnSaveElement) {
    		if(e.isDisplayed() && e.isEnabled()){
    			return e;
    		}
		}
    	return btnSaveElement.get(1);
    }


    public void validInOutData(Map<String, String> mapValue, String loanPurpose, String numOfDependents, String houseOwnership, String mortgagePaymentCost,
                               String newBankCardNumber, String salesAgentCode, String maxRequestRate) throws Exception {
        Utilities.checkInput(mapValue, "MiscFrmAppDtl_loanPurpose", loanPurpose, loanPurposeOptionElement);
        Utilities.checkInput(mapValue, "MiscFrmAppDtl_numOfDependents", numOfDependents, numberOfDependentsElement);
        Utilities.checkInput(mapValue, "MiscFrmAppDtl_houseOwnership", houseOwnership, houseOwnerShipOptionElement);
        Utilities.checkInput(mapValue, "MiscFrmAppDtl_mortgagePaymentCost", mortgagePaymentCost, mortgagePaymentCostElement);
        Utilities.checkInput(mapValue, "MiscFrmAppDtl_newBankCardNumber", newBankCardNumber, newBankCardNumberElement);
        Utilities.checkInput(mapValue, "MiscFrmAppDtl_salesAgentCode", salesAgentCode, salesAgentCodeElement);
        Utilities.checkInput(mapValue, "MiscFrmAppDtl_maxRequestRate", maxRequestRate, maxRequestRateElement);
    }
}
