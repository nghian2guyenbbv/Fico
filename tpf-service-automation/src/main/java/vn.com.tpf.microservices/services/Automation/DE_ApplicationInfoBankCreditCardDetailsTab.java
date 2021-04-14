package vn.com.tpf.microservices.services.Automation;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.awaitility.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.models.Automation.BankCreditCardDetailsDTO;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.*;

public class DE_ApplicationInfoBankCreditCardDetailsTab {
    private WebDriver _driver;

    @FindBy(how = How.XPATH, using = "//table[@id = 'bank_details_Table']//tr//td")
    @CacheLookup
    private List<WebElement> tableBankDetails;

    @FindBy(how = How.XPATH, using = "//div[@id = 'bankDetailsGrid']//input[@id = 'create_new']")
    private WebElement createNewBankDetails;

    @FindBy(how = How.XPATH, using = "//button[@id = 'fetchBank']")
    @CacheLookup
    private WebElement fetchBankButton;

    @FindBy(how = How.XPATH, using = "//button[@id = 'fetchBank'][contains(text(), 'Wait...')]")
    @CacheLookup
    private WebElement fetchBankWaitButton;

    @FindBy(how = How.XPATH, using = "//div[contains(@id, 'content_bank_details_bankName')]//ul[@id = 'holder']")
    private WebElement popupBankName;

    @FindBy(how = How.XPATH, using = "//li[contains(@id, 'listitem_bank_details_bankName_0')]")
    private List<WebElement> selectBankNameOptionElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id, 'content_bank_detail_bankBranch')]//ul[@id = 'holder']")
    private WebElement popupBrandName;

    @FindBy(how = How.XPATH, using = "//li[contains(@id, 'listitem_bank_detail_bankBranch_0')]")
    private List<WebElement> selectBranchNameOptionElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'deleteTag')]")
    @CacheLookup
    private List<WebElement> deleteBankDetailsElement;

    @FindBy(how = How.ID, using = "bankSaveAndNextButton2")
    @CacheLookup
    private WebElement btnSaveAndNextElement;

    @FindBy(how = How.ID, using = "addviewManualBankDetails")
    private WebElement popupAddViewBankDetails;

    @FindBy(how = How.ID, using = "bank_eval_period_manual_chzn")
    private WebElement bankingEvaluationPeriodElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'bank_eval_period_manual_chzn_o_')]")
    private List<WebElement> bankingEvaluationPeriodOptionElement;

    @FindBy(how = How.XPATH, using = "//input[@id = 'amount_avgBalanceManual']")
    private WebElement averageBalanceElement;

    @FindBy(how = How.XPATH, using = "//a[@id = 'okFetch']")
    private WebElement buttonOkAddViewBankDetails;



    public DE_ApplicationInfoBankCreditCardDetailsTab(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    public void setBankDetailsData(List<BankCreditCardDetailsDTO> bankCreditCardDetailsDTO) {
        with().pollInterval(Duration.FIVE_SECONDS).await("Button create new bank details visible timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> createNewBankDetails.isDisplayed());

        createNewBankDetails.click();

        int index = 0;

        int sizeTableBanks = tableBankDetails.size();

        with().pollInterval(Duration.FIVE_SECONDS).await("Table create new bank details visible timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> sizeTableBanks > 2);

        Utilities.captureScreenShot(_driver);

        for (BankCreditCardDetailsDTO data : bankCreditCardDetailsDTO) {

            System.out.println("BANK NAME: " + data.getBankName());

            WebElement textBankName = _driver.findElement(By.id("Text_bank_details_bankName_" + index));

            textBankName.clear();

            textBankName.sendKeys(data.getBankName());

            await("Select BankName Element displayed timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> popupBankName.isDisplayed());

            int sizeSelectBankName = selectBankNameOptionElement.size();

            await("List BankName Element displayed timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> sizeSelectBankName > 0);

            for (WebElement e : selectBankNameOptionElement) {
                if (!Objects.isNull(e.getAttribute("username")) && StringEscapeUtils.unescapeJava(e.getAttribute("username")).equals(data.getBankName())) {
                    e.click();
                    break;
                }
            }

            Utilities.captureScreenShot(_driver);

            System.out.println("BRANCH NAME: " + data.getBranchName());

            WebElement textBrandName = _driver.findElement(By.id("Text_bank_detail_bankBranch_" + index));

            textBrandName.clear();

            textBrandName.sendKeys(data.getBranchName());

            await("Select BranchName Element displayed timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> popupBrandName.isDisplayed());

            Utilities.captureScreenShot(_driver);

            int sizeSelectBranchName = selectBranchNameOptionElement.size();

            await("List BranchName Element displayed timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> sizeSelectBranchName > 0);

            for (WebElement e : selectBranchNameOptionElement) {
                if (!Objects.isNull(e.getAttribute("username")) && StringEscapeUtils.unescapeJava(e.getAttribute("username")).equals(data.getBranchName())) {
                    e.click();
                    break;
                }
            }

            Utilities.captureScreenShot(_driver);

            if (data.getTypeOfAccount() != null || StringUtils.isNotEmpty(data.getTypeOfAccount())){

                System.out.println("TYPE OF ACCOUNT: " + data.getTypeOfAccount());

                WebElement typeofAccountElement = _driver.findElement(By.xpath("//div[@id = 'accountTypeId_" + index + "_chzn']"));

                await("Type Of Account Element not enabled!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> typeofAccountElement.isEnabled());

                List<WebElement> listTypeofAccountElement = _driver.findElements(By.xpath("//*[contains(@id, 'accountTypeId_" + index + "_chzn_o_')]"));

                await("list Type Of Account Element loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> listTypeofAccountElement.size() > 0);

                Utilities.chooseDropdownValue(data.getTypeOfAccount(), listTypeofAccountElement);

                Utilities.captureScreenShot(_driver);

            }

            System.out.println("CURRENT ACCOUNT NUMBER IN TP BANK: " + data.getAccountNumber());

            WebElement currentAccountNumber = _driver.findElement(By.id("bank_Detail_accountNumber_" + index));

            currentAccountNumber.clear();

            currentAccountNumber.sendKeys(data.getAccountNumber());

            Utilities.captureScreenShot(_driver);

            WebElement accountOpeningYearElement = _driver.findElement(By.id("bank_detail_accountOpeningYear_" + index));

            Calendar now = Calendar.getInstance();

            String currentYear = String.valueOf(now.get(Calendar.YEAR));

            System.out.println("YEAR TEST = " + currentYear);

            accountOpeningYearElement.clear();

            accountOpeningYearElement.sendKeys(currentYear);

            WebElement natureOfBankAccount = _driver.findElement(By.id("selectedAccountType_" + index));

            natureOfBankAccount.click();

            WebElement bankDetailsUpload = _driver.findElement(By.id("manualBankDetailsUpload_" + index));

            bankDetailsUpload.click();

            with().pollInterval(Duration.FIVE_SECONDS).await("Popup Add/View Bank Details displayed timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> popupAddViewBankDetails.isDisplayed());

            await("Banking Evaluation Period loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> bankingEvaluationPeriodElement.isDisplayed());

            bankingEvaluationPeriodElement.click();

            await("Banking Evaluation Period List loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> bankingEvaluationPeriodOptionElement.size() > 0);

            Utilities.chooseDropdownValue("3", bankingEvaluationPeriodOptionElement);

            await("Average BalanceElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> averageBalanceElement.isDisplayed());

            averageBalanceElement.clear();
            averageBalanceElement.sendKeys("123");

            await("Average BalanceElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> buttonOkAddViewBankDetails.isDisplayed());

            buttonOkAddViewBankDetails.click();


            if (index < bankCreditCardDetailsDTO.size() - 1) {
                await("Button create new bank details visible timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> createNewBankDetails.isEnabled());
                createNewBankDetails.click();
            }

            index++;

        }

    }

    public void updateBankDetailsData(List<BankCreditCardDetailsDTO> bankCreditCardDetailsDTO) {

        await("Load deleteIdDetailElement Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> deleteBankDetailsElement.size() > 0);

        System.out.println("Size Delete => " + deleteBankDetailsElement.size());

        for (int i=0; i<deleteBankDetailsElement.size(); i++)
        {
            WebElement var = deleteBankDetailsElement.get(i);
            var.click();
        }

        with().pollInterval(Duration.FIVE_SECONDS).await("Button create new bank details visible timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> createNewBankDetails.isDisplayed());

        createNewBankDetails.click();

        int index = 0;

        int sizeTableBanks = tableBankDetails.size();

        with().pollInterval(Duration.FIVE_SECONDS).await("Table create new bank details visible timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> sizeTableBanks > 2);

        Utilities.captureScreenShot(_driver);

        for (BankCreditCardDetailsDTO data : bankCreditCardDetailsDTO) {

            System.out.println("BANK NAME: " + data.getBankName());

            WebElement textBankName = _driver.findElement(By.id("Text_bank_details_bankName_" + index));

            textBankName.clear();

            textBankName.sendKeys(data.getBankName());

            await("Select BankName Element displayed timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> popupBankName.isDisplayed());

            int sizeSelectBankName = selectBankNameOptionElement.size();

            await("List BankName Element displayed timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> sizeSelectBankName > 0);

            for (WebElement e : selectBankNameOptionElement) {
                if (!Objects.isNull(e.getAttribute("username")) && StringEscapeUtils.unescapeJava(e.getAttribute("username")).equals(data.getBankName())) {
                    e.click();
                    break;
                }
            }

            Utilities.captureScreenShot(_driver);

            System.out.println("BRANCH NAME: " + data.getBranchName());

            WebElement textBrandName = _driver.findElement(By.id("Text_bank_detail_bankBranch_" + index));

            textBrandName.clear();

            textBrandName.sendKeys(data.getBranchName());

            await("Select BranchName Element displayed timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> popupBrandName.isDisplayed());

            Utilities.captureScreenShot(_driver);

            int sizeSelectBranchName = selectBranchNameOptionElement.size();

            await("List BranchName Element displayed timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> sizeSelectBranchName > 0);

            for (WebElement e : selectBranchNameOptionElement) {
                if (!Objects.isNull(e.getAttribute("username")) && StringEscapeUtils.unescapeJava(e.getAttribute("username")).equals(data.getBranchName())) {
                    e.click();
                    break;
                }
            }

            Utilities.captureScreenShot(_driver);

            if (data.getTypeOfAccount() != null || StringUtils.isNotEmpty(data.getTypeOfAccount())){

                System.out.println("TYPE OF ACCOUNT: " + data.getTypeOfAccount());

                WebElement typeofAccountElement = _driver.findElement(By.xpath("//div[@id = 'accountTypeId_" + index + "_chzn']"));

                await("Type Of Account Element not enabled!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> typeofAccountElement.isEnabled());

                List<WebElement> listTypeofAccountElement = _driver.findElements(By.xpath("//*[contains(@id, 'accountTypeId_" + index + "_chzn_o_')]"));

                await("list Type Of Account Element loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> listTypeofAccountElement.size() > 0);

                Utilities.chooseDropdownValue(data.getTypeOfAccount(), listTypeofAccountElement);

                Utilities.captureScreenShot(_driver);

            }

            System.out.println("CURRENT ACCOUNT NUMBER IN TP BANK: " + data.getAccountNumber());

            WebElement currentAccountNumber = _driver.findElement(By.id("bank_Detail_accountNumber_" + index));

            currentAccountNumber.clear();

            currentAccountNumber.sendKeys(data.getAccountNumber());

            Utilities.captureScreenShot(_driver);

            WebElement accountOpeningYearElement = _driver.findElement(By.id("bank_detail_accountOpeningYear_" + index));

            Calendar now = Calendar.getInstance();

            String currentYear = String.valueOf(now.get(Calendar.YEAR));

            System.out.println("YEAR TEST = " + currentYear);

            accountOpeningYearElement.clear();

            accountOpeningYearElement.sendKeys(currentYear);

            WebElement natureOfBankAccount = _driver.findElement(By.id("selectedAccountType_" + index));

            natureOfBankAccount.click();

            WebElement bankDetailsUpload = _driver.findElement(By.id("manualBankDetailsUpload_" + index));

            bankDetailsUpload.click();

            with().pollInterval(Duration.FIVE_SECONDS).await("Popup Add/View Bank Details displayed timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> popupAddViewBankDetails.isDisplayed());

            await("Banking Evaluation Period loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> bankingEvaluationPeriodElement.isDisplayed());

            bankingEvaluationPeriodElement.click();

            await("Banking Evaluation Period loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> bankingEvaluationPeriodOptionElement.size() > 0);

            Utilities.chooseDropdownValue("3", bankingEvaluationPeriodOptionElement);

            await("Average BalanceElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> averageBalanceElement.isDisplayed());

            averageBalanceElement.clear();
            averageBalanceElement.sendKeys("123");

            await("Average BalanceElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> buttonOkAddViewBankDetails.isDisplayed());

            buttonOkAddViewBankDetails.click();


            if (index < bankCreditCardDetailsDTO.size() - 1) {
                await("Button create new bank details visible timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> createNewBankDetails.isEnabled());
                createNewBankDetails.click();
            }

            index++;

        }

    }

    public void saveAndNext() {
        this.btnSaveAndNextElement.click();
    }

}
