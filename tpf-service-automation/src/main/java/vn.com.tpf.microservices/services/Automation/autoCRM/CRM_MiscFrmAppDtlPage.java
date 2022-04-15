package vn.com.tpf.microservices.services.Automation.autoCRM;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.Getter;
import org.apache.commons.lang.StringEscapeUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.models.AutoCRM.CRM_DynamicFormDTO;
import vn.com.tpf.microservices.models.AutoCRM.CRM_MiscFrmAppDtlDTO;
import vn.com.tpf.microservices.models.Automation.MiscFrmAppDtlDTO;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Getter
public class CRM_MiscFrmAppDtlPage {

    private WebDriver _driver;
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

//    //UAT
//    @FindBy(how = How.ID, using = "Loan_purpose_1_frmAppDtl_0_chzn")
//    @CacheLookup
//    private WebElement loanPurposeElement;
//
//    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'Loan_purpose_1_frmAppDtl_0_chzn_o_')]")
//    @CacheLookup
//    private List<WebElement> loanPurposeOptionElement;
//
//    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'Loan_purpose_1_frmAppDtl_0_chzn')]//input")
//    @CacheLookup
//    private WebElement loanPurposeInputElement;
//    //Update
//
//    //update loanpurpose
//    @FindBy(how = How.XPATH, using = "//*[contains(@id,'Loan_purpose_1_frmAppDtl_0_chzn')]//*[contains(@class,'search-choice-close')]")
//    @CacheLookup
//    private List<WebElement> loanPurposeCloseElement;
//
//    @FindBy(how = How.ID, using = "insurance_company_contract_frmAppDtl_2")
//    private WebElement contractNumberElement;
//
//    @FindBy(how = How.ID, using = "insurance_company_frmAppDtl_2")
//    private WebElement companyNameElement;
//
//    //------------- END UAT----------------//

    //PRO
    //production khac id
    @FindBy(how = How.ID, using = "loanpurpose_frmAppDtl_0_chosen")
    @CacheLookup
    private WebElement loanPurposeElement;

    //production khac
    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'loanpurpose_frmAppDtl_0_chosen_o_')]")
    @CacheLookup
    private List<WebElement> loanPurposeOptionElement;

    //update loanpurpose PRO
    @FindBy(how = How.XPATH, using = "//*[contains(@id,'loanpurpose_frmAppDtl_0_chzn')]//*[contains(@class,'search-choice-close')]")
    @CacheLookup
    private List<WebElement> loanPurposeCloseElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'loanpurpose_frmAppDtl_0_chosen')]//input")
    @CacheLookup
    private WebElement loanPurposeInputElement;

    @FindBy(how = How.ID, using = "tpf_insurance_company_contract_frmAppDtl_2")
    private WebElement contractNumberElement;

    @FindBy(how = How.ID, using = "tpf_insurance_company_frmAppDtl_2")
    private WebElement companyNameElement;

    //------------- END PRO----------------//

    @FindBy(how = How.ID, using = "householdmembers_frmAppDtl_1")
    @CacheLookup
    private WebElement numberOfDependentsElement;

    @FindBy(how = How.ID, using = "house_ownership_frmAppDtl_1_chosen")
    @CacheLookup
    private WebElement houseOwnerShipElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'house_ownership_frmAppDtl_1_chosen_o_')]")
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

    @FindBy(how = How.ID, using = "amount_Total_Monthly_Payable_frmAppDtl_5")
    @CacheLookup
    private WebElement totalMonthlyPayableElement;

    @FindBy(how = How.ID, using = "Remark_frmAppDtl_3")
    @CacheLookup
    private WebElement remarkElement;

    @FindBy(how = How.ID, using = "dynSave")
    @CacheLookup
    private List<WebElement> btnSaveElement;

    @FindBy(how = How.ID, using = "move_to_next_stage")
    private WebElement btnMoveToNextStageElement;

    @FindBy(how = How.ID, using = "uniform-loan_at_work_frmAppDtl_51")
    private WebElement loanOfWorkNoElememt;

    @FindBy(how = How.ID, using = "uniform-loan_at_work_frmAppDtl_52")
    private WebElement loanOfWorkYesElememt;

    @FindBy(how = How.ID, using = "T_Courier_Code_frmAppDtl_4")
    private WebElement courierCodeElement;

    @FindBy(how = How.ID, using = "uniform-Old_Contract_Loan_Amount_frmAppDtl_51")
    private WebElement oldContractLoanAmountLess20Elememt;

    @FindBy(how = How.ID, using = "uniform-Old_Contract_Loan_Amount_frmAppDtl_52")
    private WebElement oldContractLoanAmountMore20Elememt;

    @FindBy(how = How.ID, using = "uniform-Old_Contract_Loan_Amount_frmAppDtl_53")
    private WebElement oldContractLoanAmountOtherElememt;

    @FindBy(how = How.ID, using = "ZALO_DYN_frmAppDtl_5")
    private WebElement zaloElement;

    @FindBy(how = How.ID, using = "tpf_insurance_fee_frmAppDtl_2")
    private WebElement monthlyFeeElement;

    @FindBy(how = How.ID, using = "leadCommHist")
    @CacheLookup
    private WebElement communicationElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'collapseOnee_lead_comm')]//button[@class='btn btn-sm btn-primary']")
    @CacheLookup
    private WebElement addCommunicationElement;

    @FindBy(how = How.ID, using = "lead_communication_form")
    @CacheLookup
    private WebElement formCommunicationElement;

    @FindBy(how = How.ID, using = "uniform-inperson_mode")
    @CacheLookup
    private WebElement webPortalElement;

    @FindBy(how = How.ID, using = "comm_status_chosen")
    @CacheLookup
    private WebElement leadStatusElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'comm_status_chosen_o_')]")
    @CacheLookup
    private List<WebElement> leadStatusOptionElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'comm_status_chosen')]//input")
    @CacheLookup
    private WebElement leadStatusInputElement;

    @FindBy(how = How.ID, using = "lead_response")
    @CacheLookup
    private WebElement leadResponseElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'lead_communication_form')]//input[contains(@value,'Submit')]")
    @CacheLookup
    private WebElement saveCommunicationBtnElement;

    @FindBy(how = How.ID, using = "lead_addedBy")
    @CacheLookup
    private WebElement addedByElement;

    @FindBy(how = How.ID, using = "Text_lead_contactedBy")
    @CacheLookup
    private WebElement contactedByElement;

    @FindBy(how = How.ID, using = "holder")
    private WebElement textSelectContactedByContainerElement;

    @FindBy(how = How.XPATH, using = "//a[contains(@id, 'listitem_lead_contactedBy0a')]")
    private List<WebElement> textSelectContactedByOptionElement;


    public WebElement _getBtnSaveElement() {
        for(WebElement e: btnSaveElement) {
            if(e.isDisplayed() && e.isEnabled()){
                return e;
            }
        }
        return btnSaveElement.get(1);
    }


    public CRM_MiscFrmAppDtlPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this._driver=driver;
    }

    public void setData(CRM_DynamicFormDTO data) {
        await("loanPurposeElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> loanPurposeElement.isDisplayed() && loanPurposeElement.isEnabled());
        loanPurposeElement.click();
        await("loanPurposeOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> loanPurposeOptionElement.size() > 0);
//        String[] listPurpose= data.getLoanPurpose().split(";");
        loanPurposeInputElement.sendKeys(data.getLoanPurpose() + Keys.ENTER);
        Utilities.captureScreenShot(_driver);
//        if(listPurpose.length>0)
//        {
//
//            for(String loanPurpose : listPurpose){
//                loanPurposeInputElement.sendKeys(loanPurpose);
//                loanPurposeInputElement.sendKeys(Keys.ENTER);
//                Utilities.captureScreenShot(_driver);
//            }
//
//        }
        numberOfDependentsElement.clear();
        numberOfDependentsElement.sendKeys(data.getNumberOfDependents());

        houseOwnerShipElement.click();
        await("houseOwnerShipOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> houseOwnerShipOptionElement.size() > 0);
        Utilities.chooseDropdownValue(data.getHouseOwnership(), houseOwnerShipOptionElement);
        Utilities.captureScreenShot(_driver);
        mortgagePaymentCostElement.clear();
        mortgagePaymentCostElement.sendKeys(data.getMonthlyRental());

        monthlyFeeElement.clear();
//        monthlyFeeElement.sendKeys(data.getMonthlyFee());

        newBankCardNumberElement.clear();
        newBankCardNumberElement.sendKeys(data.getNewBankCardNumber());

        courierCodeElement.clear();
        courierCodeElement.sendKeys(data.getCourierCode());


        salesAgentCodeElement.clear();
        salesAgentCodeElement.sendKeys(data.getSaleAgentCode());

        maxRequestRateElement.clear();
        maxRequestRateElement.sendKeys(data.getMaximumInterestedRate());
        Utilities.captureScreenShot(_driver);

    }

    public void updateData(CRM_DynamicFormDTO data) {
        await("loanPurposeElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> loanPurposeElement.isDisplayed() && loanPurposeElement.isEnabled());
        loanPurposeElement.click();

        //xoa loanpurpose
        if(loanPurposeCloseElement.size()>0)
        {
            for (WebElement we:loanPurposeCloseElement)
            {
                we.click();
            }
        }

        loanPurposeElement.click();
        loanPurposeElement.click();
        await("loanPurposeOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> loanPurposeOptionElement.size() > 0);
        //Utilities.chooseDropdownValue(data.getLoanPurpose(), loanPurposeOptionElement);

        String[] listPurpose= data.getLoanPurpose().split(";");

        if(listPurpose.length>0)
        {

            for(String loanPurpose : listPurpose){
                loanPurposeInputElement.sendKeys(loanPurpose);
                loanPurposeInputElement.sendKeys(Keys.ENTER);
                Utilities.captureScreenShot(_driver);
            }

        }


        numberOfDependentsElement.clear();
        numberOfDependentsElement.sendKeys(data.getNumberOfDependents());

        houseOwnerShipElement.click();
        await("houseOwnerShipOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> houseOwnerShipOptionElement.size() > 0);
        Utilities.chooseDropdownValue(data.getHouseOwnership(), houseOwnerShipOptionElement);
        Utilities.captureScreenShot(_driver);
        mortgagePaymentCostElement.clear();
        mortgagePaymentCostElement.sendKeys(data.getMonthlyRental());

        monthlyFeeElement.clear();
//        monthlyFeeElement.sendKeys(data.getMonthlyFee());

        newBankCardNumberElement.clear();
        newBankCardNumberElement.sendKeys(data.getNewBankCardNumber());

        courierCodeElement.clear();
        courierCodeElement.sendKeys(data.getCourierCode());


        salesAgentCodeElement.clear();
        salesAgentCodeElement.sendKeys(data.getSaleAgentCode());

        maxRequestRateElement.clear();
        maxRequestRateElement.sendKeys(data.getMaximumInterestedRate());
        Utilities.captureScreenShot(_driver);
    }

    public void updateCommunicationValue(String communication) {
        //COMMUNICATION
        String stage = "CUSTOMER COMMUNICATION HISTORY";
        Actions actions = new Actions(_driver);
        communicationElement.click();
        await("addCommunicationElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> addCommunicationElement.isDisplayed());
        addCommunicationElement.click();
        await("formCommunicationElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> formCommunicationElement.isDisplayed());

        await("webPortalElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> webPortalElement.isDisplayed());
        webPortalElement.click();
        Utilities.captureScreenShot(_driver);

        contactedByElement.clear();
        contactedByElement.sendKeys(addedByElement.getAttribute("value"));

        await("textSelectUserContainerElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> textSelectContactedByContainerElement.isDisplayed());

        await("textSelectUserOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> textSelectContactedByOptionElement.size() > 0);

        for (WebElement e : textSelectContactedByOptionElement) {
            e.click();
        }

        actions.moveToElement(leadStatusElement).click().build().perform();
        await("leadStatusOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> leadStatusOptionElement.size() > 0);
        Utilities.chooseDropdownValue("NA", leadStatusOptionElement);
        Utilities.captureScreenShot(_driver);

        leadResponseElement.sendKeys(communication);
        saveCommunicationBtnElement.click();
        Utilities.captureScreenShot(_driver);
        System.out.println(stage + ": DONE");
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
