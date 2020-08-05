package vn.com.tpf.microservices.services.Automation.autoCRM;

import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.models.AutoCRM.CRM_SourcingDetailsDTO;
import vn.com.tpf.microservices.models.AutoCRM.CRM_SourcingDetailsListDTO;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Getter
public class CRM_LoanDetailsSourcingDetailsTab {

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

    @FindBy(how = How.ID, using = "holder")
    @CacheLookup
    private WebElement branchContainerElement;

    @FindBy(how = How.XPATH, using = "//a[contains(@id, 'listitem_branch0a')]")
    @CacheLookup
    private List<WebElement> branchOptionElement;

    @FindBy(how = How.ID, using = "channelId_chzn")
    @CacheLookup
    private WebElement channelElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'channelId_chzn_o_')]")
    @CacheLookup
    private List<WebElement> channelOptionElement;

    @FindBy(how = How.ID, using = "applicationFormNumber")
    @CacheLookup
    private WebElement applicationFormNumberElement;

    @FindBy(how = How.ID, using = "loanApplication_type_chzn")
    @CacheLookup
    private WebElement loanApplicationTypeElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'loanApplication_type_chzn_o_')]")
    @CacheLookup
    private List<WebElement> loanApplicationTypeOptionElement;

    @FindBy(how = How.ID, using = "loan_product_chzn")
    @CacheLookup
    private WebElement productNameElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'loan_product_chzn_o_')]")
    @CacheLookup
    private List<WebElement> productNameOptionElement;

    @FindBy(how = How.ID, using = "scheme_chzn")
    @CacheLookup
    private WebElement schemeElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'scheme_chzn_o_')]")
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

    @FindBy(how = How.ID, using = "sourcingRM_chzn")
    @CacheLookup
    private WebElement salesAgentCodeElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'sourcingRM_chzn_o_')]")
    @CacheLookup
    private List<WebElement> salesAgentCodeOptionElement;

    @FindBy(how = How.XPATH, using = "//button[@class='btn btn-mini btn-primary saveAndNextSourcingDetail']")
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

    public void setData(String applicationId, CRM_SourcingDetailsDTO data) {
        if (StringUtils.isNotBlank(data.getSourcingBranch())) {
            branchElement.clear();
            branchElement.sendKeys(data.getSourcingBranch());

            await("branchContainerElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> branchContainerElement.isDisplayed());
            await("branchOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> branchOptionElement.size() > 0);
            for(WebElement e: branchOptionElement) {
                if(!Objects.isNull(e.getAttribute("title")) && e.getAttribute("title").equals(data.getSourcingBranch().toUpperCase())) {
                    e.click();
                    Utilities.captureScreenShot(_driver);
                    break;
                }
            }
        }
        channelElement.click();
        await("channelOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> channelOptionElement.size() > 0);
        Utilities.chooseDropdownValue(data.getSourcingChannel().toUpperCase(), channelOptionElement);

        applicationFormNumberElement.clear();
        applicationFormNumberElement.sendKeys(applicationId);

        loanApplicationTypeElement.click();
        await("channelOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> loanApplicationTypeOptionElement.size() > 0);
        Utilities.chooseDropdownValue(data.getLoanApplicationType(), loanApplicationTypeOptionElement);

        productNameElement.click();
        await("productNameOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> productNameOptionElement.size() > 0);
        Utilities.chooseDropdownValue(data.getProductCode().toUpperCase(), productNameOptionElement);

        schemeElement.click();
        await("schemeOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> schemeOptionElement.size() > 0);
        Utilities.chooseDropdownValue(data.getSchemeCode().toUpperCase(), schemeOptionElement);

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

        //sua sang 60s , hay bi timeout cho nay
        await("rateElement loading timeout").atMost(60, TimeUnit.SECONDS)
                .until(() -> StringUtils.isNotEmpty(rateElement.getAttribute("value")));

        rateElement.clear();
        rateElement.sendKeys(data.getInterestRate());
        Utilities.captureScreenShot(_driver);

        salesAgentCodeElement.click();
        await("salesAgentCodeOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> salesAgentCodeOptionElement.size() > 0);
        Utilities.chooseDropdownValue(data.getSaleAgentCode(), salesAgentCodeOptionElement);
    }

    public void updateData(String applicationId, CRM_SourcingDetailsDTO data) {

        applicationFormNumberElement.clear();
        applicationFormNumberElement.sendKeys(applicationId);

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

        // default value is 6
        //sua sang 60s , hay bi timeout cho nay
        await("loanTermElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> StringUtils.isNotEmpty(loanTermElement.getAttribute("value")));
        loanTermElement.clear();
        loanTermElement.sendKeys(data.getRequestedTenure());
        Utilities.captureScreenShot(_driver);

        //sua sang 60s , hay bi timeout cho nay
        await("rateElement loading timeout").atMost(60, TimeUnit.SECONDS)
                .until(() -> StringUtils.isNotEmpty(rateElement.getAttribute("value")));

        Utilities.captureScreenShot(_driver);

        salesAgentCodeElement.click();
        await("salesAgentCodeOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> salesAgentCodeOptionElement.size() > 0);
        Utilities.chooseDropdownValue(data.getSaleAgentCode(), salesAgentCodeOptionElement);
    }
}
