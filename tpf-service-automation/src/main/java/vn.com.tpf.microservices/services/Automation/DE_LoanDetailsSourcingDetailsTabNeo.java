package vn.com.tpf.microservices.services.Automation;

import lombok.Getter;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.models.Automation.LoanDetailsDTO;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Getter
public class DE_LoanDetailsSourcingDetailsTabNeo {
    private WebDriver _driver;

    @FindBy(how = How.ID, using = "sourcingDetailsLiId")
    @CacheLookup
    private WebElement tabSourcingDetailsElement;

    @FindBy(how = How.ID, using = "sourcing")
    @CacheLookup
    private WebElement sourcingDetailsDivContainerElement;

    @FindBy(how = How.ID, using = "Text_branch")
    @CacheLookup
    private WebElement branchElement;

    @FindBy(how = How.ID, using = "auto-container")
    @CacheLookup
    private WebElement branchContainerElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'listitem_branch')]")
    @CacheLookup
    private List<WebElement> branchOptionElement;

    @FindBy(how = How.ID, using = "channelId_chosen")
    @CacheLookup
    private WebElement channelElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'channelId_chosen_o_')]")
    @CacheLookup
    private List<WebElement> channelOptionElement;

    @FindBy(how = How.ID, using = "applicationFormNumber")
    @CacheLookup
    private WebElement applicationFormNumberElement;

    @FindBy(how = How.ID, using = "loanApplication_type_chosen")
    @CacheLookup
    private WebElement loanApplicationTypeElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'loanApplication_type_chosen_o_')]")
    @CacheLookup
    private List<WebElement> loanApplicationTypeOptionElement;

    @FindBy(how = How.ID, using = "Text_loan_product")
    @CacheLookup
    private WebElement productNameElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'listitem_loan_product')]")
    @CacheLookup
    private List<WebElement> productNameOptionElement;

    @FindBy(how = How.ID, using = "Text_scheme")
    @CacheLookup
    private WebElement schemeElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'listitem_scheme')]")
    @CacheLookup
    private List<WebElement> schemeOptionElement;

    @FindBy(how = How.ID, using = "amount_loanAmount_requested")
    @CacheLookup
    private WebElement loanAmountElement;

    @FindBy(how = How.ID, using = "source_tenure")
    @CacheLookup
    private WebElement loanTermElement;

    @FindBy(how = How.ID, using = "source_rateId")
    @CacheLookup
    private WebElement rateElement;

    @FindBy(how = How.ID, using = "Text_sourcingRM")
    @CacheLookup
    private WebElement salesAgentCodeElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'listitem_sourcingRM')]")
    @CacheLookup
    private List<WebElement> salesAgentCodeOptionElement;

    @FindBy(how = How.XPATH, using = "//button[@onclick='saveAndNextSourcingDetail();']")
    @CacheLookup
    private WebElement btnSaveAndNextElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id, 'confirmDeleteVapNext')]")
    private WebElement dialogConfirmDeleteVapNextElements;

    @FindBy(how = How.XPATH, using = "//div[@class = 'modal-scrollable']//div[starts-with(@id,'confirmDeleteVap')]//a[contains(text(),'Confirm')]")
    private WebElement btnConfirmDeleteVapNextElements;


    //------------------------- UPDATE ---------------------------------

    @FindBy(how = How.XPATH, using = "//div[contains(@id, 'confirmDeleteVapNext')]")
    @CacheLookup
    private List<WebElement> modalConfirmDeleteVapNextElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'confirmDeleteVapNext')]//a[contains(@id, 'confirmDeleteVapNext')]")
    @CacheLookup
    private List<WebElement> btnConfirmDeleteVapNextElement;

    @FindBy(how = How.XPATH, using = "//div[@class='modal-scrollable']//a[contains(@id, 'confirmDeleteVapNext')]")
    private WebElement btnConfirmDeleteVapNextElement1;


    //------------------------- UPDATE 4-3-2020 ---------------------------------
    @FindBy(how = How.ID, using = "loan_Info_loanPurposeDescription")
    @CacheLookup
    private WebElement loanDetailPurposeElement;

    public DE_LoanDetailsSourcingDetailsTabNeo(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    public void setData(LoanDetailsDTO data) {
    	//default branch HO CHI MINH
//        if (StringUtils.isNotBlank(data.getBranch())) {
//            branchElement.clear();
//            // TODO: should select value from dropdown autocomplete
        branchElement.clear();
            branchElement.sendKeys("HO CHI MINH");
//
//            await("branchContainerElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> branchContainerElement.isDisplayed());
////        branchOptionElement.get(0).click();
            await("branchOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> branchOptionElement.size() > 0);
            for(WebElement e: branchOptionElement) {
                if(!Objects.isNull(e.getAttribute("username")) && e.getAttribute("username").toUpperCase().equals("HO CHI MINH")) {
                    e.click();
                    Utilities.captureScreenShot(_driver);
                    break;
                }
            }
//        }
        channelElement.click();
        await("channelOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> channelOptionElement.size() > 0);
        Utilities.chooseDropdownValue(data.getChannel(), channelOptionElement);
        applicationFormNumberElement.clear();
        applicationFormNumberElement.sendKeys(data.getApplicationNumber());

        loanApplicationTypeElement.click();
        await("channelOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> loanApplicationTypeOptionElement.size() > 0);
        Utilities.chooseDropdownValue(data.getLoanAppType(), loanApplicationTypeOptionElement);
        productNameElement.clear();
        productNameElement.sendKeys(data.getProductName());
        await("productNameOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> productNameOptionElement.size() > 0);
        for(WebElement e: productNameOptionElement) {
            if(!Objects.isNull(e.getAttribute("username")) && e.getAttribute("username").toUpperCase().equals(data.getProductName().toUpperCase())) {
                e.click();
                Utilities.captureScreenShot(_driver);
                break;
            }
        }
//
//        schemeElement.click();
        schemeElement.clear();
        schemeElement.sendKeys(data.getScheme());
        await("schemeOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> schemeOptionElement.size() > 0);
//        Utilities.chooseDropdownValue(data.getScheme(), schemeOptionElement);
        for (WebElement e : schemeOptionElement) {
            if (!Objects.isNull(e.getAttribute("username")) && StringEscapeUtils.unescapeJava(e.getAttribute("username")).toUpperCase().equals(data.getScheme().toUpperCase())) {
                e.click();
                break;
            }
        }

//        //update them scheme
//        schemeElement.click();
//        await("schemeOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                .until(() -> schemeOptionElement.size() > 0);
//        Utilities.chooseDropdownValue(data.getScheme(), schemeOptionElement);


//        loanAmountElement.sendKeys("");
//        loanAmountElement.sendKeys(data.getLoanAmount());
        Utilities.checkValueSendkey(data.getLoanAmount(),loanAmountElement);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // default value is 6
        //sua sang 60s , hay bi timeout cho nay
        await("loanTermElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> StringUtils.isNotEmpty(loanTermElement.getAttribute("value")));
        loanTermElement.clear();
        loanTermElement.sendKeys(data.getLoanTerm());
        Utilities.captureScreenShot(_driver);
        // temporarily set value
//        rateElement.clear();
//        rateElement.sendKeys("29.95");

        //sua sang 60s , hay bi timeout cho nay
        await("rateElement loading timeout").atMost(60, TimeUnit.SECONDS)
                .until(() -> StringUtils.isNotEmpty(rateElement.getAttribute("value")));

        Utilities.captureScreenShot(_driver);

        //update nhap leaddeatail
        loanDetailPurposeElement.clear();
        loanDetailPurposeElement.sendKeys(data.getLoadPurpose());

        salesAgentCodeElement.click();
        salesAgentCodeElement.clear();
        salesAgentCodeElement.sendKeys(data.getSaleAgentCode());
        await("salesAgentCodeOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> salesAgentCodeOptionElement.size() > 0);
        for (WebElement e : salesAgentCodeOptionElement) {
            if (!Objects.isNull(e.getAttribute("username")) && StringEscapeUtils.unescapeJava(e.getAttribute("username")).toUpperCase().equals(data.getSaleAgentCode().toUpperCase())) {
                e.click();
                break;
            }
        }
    }

    public void updateData(LoanDetailsDTO data) {
        applicationFormNumberElement.clear();
        applicationFormNumberElement.sendKeys(data.getApplicationNumber());

        List<WebElement> deleteLoanType = _driver.findElements(By.xpath("//*[contains(@id, 'loanApplication_type_chosen')]//*[contains(@class, 'search-choice-close')]"));
        if (deleteLoanType != null && deleteLoanType.size() > 0){
            deleteLoanType.get(0).click();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        loanApplicationTypeElement.click();
        await("channelOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> loanApplicationTypeOptionElement.size() > 0);
        Utilities.chooseDropdownValue(data.getLoanAppType(), loanApplicationTypeOptionElement);

        if (data.getChannel() != null && !data.getChannel().isEmpty()) {
            channelElement.click();
            await("channelOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> channelOptionElement.size() > 0);
            Utilities.chooseDropdownValue(data.getChannel(), channelOptionElement);
        }
        productNameElement.clear();
        productNameElement.sendKeys(data.getProductName());
        await("productNameOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> productNameOptionElement.size() > 0);
        for(WebElement e: productNameOptionElement) {
            if(!Objects.isNull(e.getAttribute("username")) && e.getAttribute("username").toUpperCase().equals(data.getProductName().toUpperCase())) {
                e.click();
                Utilities.captureScreenShot(_driver);
                break;
            }
        }
        schemeElement.clear();
        schemeElement.sendKeys(data.getScheme());
        await("schemeOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> schemeOptionElement.size() > 0);
//        Utilities.chooseDropdownValue(data.getScheme(), schemeOptionElement);
        for (WebElement e : schemeOptionElement) {
            //if (!Objects.isNull(e.getAttribute("username")) && StringEscapeUtils.unescapeJava(e.getAttribute("username")).toUpperCase().equals(data.getScheme().toUpperCase())) {
            if (!Objects.isNull(e.getAttribute("username")) && e.getAttribute("username").toUpperCase().equals(data.getScheme().toUpperCase())) {
                e.click();
                Utilities.captureSreenShotWithStage("SOURCING TAB","Click Scheme",_driver);
                
                break;
            }
        }

//        //update them scheme
//        schemeElement.click();
//        await("schemeOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                .until(() -> schemeOptionElement.size() > 0);
//        Utilities.chooseDropdownValue(data.getScheme(), schemeOptionElement);

        loanAmountElement.clear();
        Utilities.checkValueSendkey(data.getLoanAmount(),loanAmountElement);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // default value is 6
        //sua sang 60s , hay bi timeout cho nay
        await("loanTermElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> StringUtils.isNotEmpty(loanTermElement.getAttribute("value")));
        loanTermElement.clear();
        loanTermElement.sendKeys(data.getLoanTerm());
        Utilities.captureScreenShot(_driver);
        // temporarily set value
//        rateElement.clear();
//        rateElement.sendKeys("29.95");

        //sua sang 60s , hay bi timeout cho nay
        await("rateElement loading timeout").atMost(60, TimeUnit.SECONDS)
                .until(() -> StringUtils.isNotEmpty(rateElement.getAttribute("value")));

        Utilities.captureScreenShot(_driver);

        //update nhap leaddeatail
        loanDetailPurposeElement.clear();
        loanDetailPurposeElement.sendKeys(data.getLoadPurpose());

        salesAgentCodeElement.click();
        salesAgentCodeElement.clear();
        salesAgentCodeElement.sendKeys(data.getSaleAgentCode());
        await("salesAgentCodeOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> salesAgentCodeOptionElement.size() > 0);
        for (WebElement e : salesAgentCodeOptionElement) {
            if (!Objects.isNull(e.getAttribute("username")) && StringEscapeUtils.unescapeJava(e.getAttribute("username")).toUpperCase().equals(data.getSaleAgentCode().toUpperCase())) {
                e.click();
                break;
            }
        }
    }

    public void validInOutData(Map<String, String> mapValue, String branch, String channel, String applicationNumber, String loanAppType,
                               String productName, String scheme, String loanAmount, String loanTerm, String saleAgentCode) throws Exception {
        Utilities.checkInput(mapValue, "LoanDetails_SourcingDetails_Branch", branch, branchElement);
        Utilities.checkInput(mapValue, "LoanDetails_SourcingDetails_Channel", channel, channelOptionElement);
        Utilities.checkInput(mapValue, "LoanDetails_SourcingDetails_ApplicationFormNumber", applicationNumber, applicationFormNumberElement);
        Utilities.checkInput(mapValue, "LoanDetails_SourcingDetails_LoanAppType", loanAppType, loanApplicationTypeOptionElement);
        Utilities.checkInput(mapValue, "LoanDetails_SourcingDetails_ProductName", productName, productNameOptionElement );
        Utilities.checkInput(mapValue, "LoanDetails_SourcingDetails_Scheme", scheme, schemeOptionElement);
        Utilities.checkInput(mapValue, "LoanDetails_SourcingDetails_LoanAmount", loanAmount, loanAmountElement);
        Utilities.checkInput(mapValue, "LoanDetails_SourcingDetails_LoanTerm", loanTerm, loanTermElement);
        Utilities.checkInput(mapValue, "LoanDetails_SourcingDetails_SaleAgentCode", saleAgentCode, salesAgentCodeOptionElement);
    }

}
