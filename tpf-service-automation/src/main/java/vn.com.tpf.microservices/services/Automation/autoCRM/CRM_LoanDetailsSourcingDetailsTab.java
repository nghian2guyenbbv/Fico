package vn.com.tpf.microservices.services.Automation.autoCRM;

import lombok.Getter;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.models.AutoCRM.CRM_SourcingDetailsDTO;
import vn.com.tpf.microservices.models.AutoCRM.CRM_SourcingDetailsListDTO;
import vn.com.tpf.microservices.models.Automation.LoanDetailsDTO;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Getter
public class CRM_LoanDetailsSourcingDetailsTab {

    private WebDriver _driver;

    @FindBy(how = How.XPATH, using = "//button[@onclick='saveAndNextSourcingDetail();']")
    @CacheLookup
    private WebElement saveButton;

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

    /*@FindBy(how = How.XPATH, using = "//button[@class='btn btn-xs btn-primary saveAndNextSourcingDetail']")
    @CacheLookup
    private WebElement btnSaveAndNextElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id, 'confirmDeleteVapNext')]")
    private WebElement dialogConfirmDeleteVapNextElements;

    @FindBy(how = How.XPATH, using = "//div[@class = 'modal-scrollable']//div[starts-with(@id,'confirmDeleteVap')]//a[contains(text(),'Confirm')]")
    private WebElement btnConfirmDeleteVapNextElements;*/
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

    public CRM_LoanDetailsSourcingDetailsTab(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    public void setData(CRM_SourcingDetailsDTO data)throws InterruptedException {
        if (StringUtils.isNotBlank(data.getSourcingBranch())) {
            Thread.sleep(300);
            branchElement.clear();
            Thread.sleep(300);
            branchElement.sendKeys(data.getSourcingBranch());

//            await("branchContainerElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> branchContainerElement.isDisplayed());

            await("branchOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> branchOptionElement.size() > 0);

            for(WebElement e: branchOptionElement) {
                e.click();
                Utilities.captureScreenShot(_driver);
                break;
            }
        }
        channelElement.click();
        await("channelOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> channelOptionElement.size() > 0);
        Utilities.chooseDropdownValue(data.getSourcingChannel().toUpperCase(), channelOptionElement);

//        applicationFormNumberElement.clear();
//        applicationFormNumberElement.sendKeys(data.getApplicationNumber());

        loanApplicationTypeElement.click();
        await("channelOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> loanApplicationTypeOptionElement.size() > 0);
        Utilities.chooseDropdownValue(data.getLoanApplicationType(), loanApplicationTypeOptionElement);

        productNameElement.clear();
        productNameElement.sendKeys(data.getProductCode().toUpperCase());
        await("productNameOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> productNameOptionElement.size() > 0);
        for (WebElement e : productNameOptionElement) {
            e.click();
            break;
        }
        schemeElement.clear();
        schemeElement.sendKeys(data.getSchemeCode().toUpperCase());
        await("schemeOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> schemeOptionElement.size() > 0);
        for (WebElement e : schemeOptionElement) {
            e.click();
            break;
        }

        Utilities.checkValueSendkey(data.getLoanAmountRequested(),loanAmountElement);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // default value is 6
        //sua sang 60s , hay bi timeout cho nay
//        await("loanTermElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                .until(() -> StringUtils.isNotEmpty(loanTermElement.getAttribute("value")));

//        loanTermElement.clear();
//
//        loanTermElement.sendKeys(data.getRequestedTenure());
//
//        Utilities.captureScreenShot(_driver);

        //sua sang 60s , hay bi timeout cho nay
//        await("rateElement loading timeout").atMost(60, TimeUnit.SECONDS)
//                .until(() -> StringUtils.isNotEmpty(rateElement.getAttribute("value")));


//        rateElement.clear();
//        rateElement.sendKeys(data.getInterestRate());

        Utilities.captureScreenShot(_driver);

        salesAgentCodeElement.clear();
        salesAgentCodeElement.sendKeys(data.getSaleAgentCode());
        await("salesAgentCodeOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> salesAgentCodeOptionElement.size() > 0);
        for (WebElement e : salesAgentCodeOptionElement) {
            e.click();
            break;
        }
    }

    public void updateData2(CRM_SourcingDetailsDTO data) throws InterruptedException {
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
        Utilities.chooseDropdownValue(data.getLoanApplicationType(), loanApplicationTypeOptionElement);

        if (data.getSourcingChannel() != null && !data.getSourcingChannel().isEmpty()) {
            channelElement.click();
            await("channelOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> channelOptionElement.size() > 0);
            Utilities.chooseDropdownValue(data.getSourcingChannel(), channelOptionElement);
        }
        productNameElement.clear();
        productNameElement.sendKeys(data.getProductCode());
        await("productNameOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> productNameOptionElement.size() > 0);
        for(WebElement e: productNameOptionElement) {
            if(!Objects.isNull(e.getAttribute("username")) && e.getAttribute("username").toUpperCase().equals(data.getProductCode().toUpperCase())) {
                e.click();
                Utilities.captureScreenShot(_driver);
                break;
            }
        }
        schemeElement.clear();
        schemeElement.sendKeys(data.getSchemeCode());
        await("schemeOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> schemeOptionElement.size() > 0);
//        Utilities.chooseDropdownValue(data.getScheme(), schemeOptionElement);
        Thread.sleep(3000);
        for (WebElement e : schemeOptionElement) {
            //if (!Objects.isNull(e.getAttribute("username")) && StringEscapeUtils.unescapeJava(e.getAttribute("username")).toUpperCase().equals(data.getScheme().toUpperCase())) {
            if (!Objects.isNull(e.getAttribute("username")) && e.getAttribute("username").toUpperCase().equals(data.getSchemeCode().toUpperCase())) {
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
        Utilities.checkValueSendkey(data.getLoanAmountRequested(),loanAmountElement);

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
        loanTermElement.sendKeys(data.getRequestedTenure());
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
        loanDetailPurposeElement.sendKeys(data.getLoanPurposeDesc());

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

    public void updateData(CRM_SourcingDetailsDTO data) {

//        applicationFormNumberElement.clear();
//        applicationFormNumberElement.sendKeys(data.getApplicationNumber());

        loanApplicationTypeElement.click();
        await("channelOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> loanApplicationTypeOptionElement.size() > 0);
        Utilities.chooseDropdownValue(data.getLoanApplicationType(), loanApplicationTypeOptionElement);

        schemeElement.click();
        await("schemeOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> schemeOptionElement.size() > 0);
        Utilities.chooseDropdownValue(data.getSchemeCode().toUpperCase(), schemeOptionElement);

        loanAmountElement.clear();
        Utilities.checkValueSendkey(data.getLoanAmountRequested(),loanAmountElement);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        // default value is 6
//        //sua sang 60s , hay bi timeout cho nay
//        await("loanTermElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                .until(() -> StringUtils.isNotEmpty(loanTermElement.getAttribute("value")));
//
//        loanTermElement.clear();
//
//        loanTermElement.sendKeys(data.getRequestedTenure());
//
//        Utilities.captureScreenShot(_driver);
//
//        //sua sang 60s , hay bi timeout cho nay
//        await("rateElement loading timeout").atMost(60, TimeUnit.SECONDS)
//                .until(() -> StringUtils.isNotEmpty(rateElement.getAttribute("value")));
//
//        rateElement.clear();
//
//        rateElement.sendKeys(data.getInterestRate());
//
//        Utilities.captureScreenShot(_driver);

        salesAgentCodeElement.click();

        await("salesAgentCodeOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> salesAgentCodeOptionElement.size() > 0);

        Utilities.chooseDropdownValue(data.getSaleAgentCode(), salesAgentCodeOptionElement);
    }
}
