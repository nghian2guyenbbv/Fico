package vn.com.tpf.microservices.services.Automation;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.awaitility.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.models.Automation.BankCreditCardDetailsDTO;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.with;

public class DE_ApplicationInfoBankCreditCardDetailsTabNeo {
    private WebDriver _driver;

    @FindBy(how = How.XPATH, using = "//table[@id = 'bank_details_Table']//tr//td")
    @CacheLookup
    private List<WebElement> tableBankDetails;

    @FindBy(how = How.XPATH, using = "//div[@id = 'bank_branch_InformationDiv']//input[@id = 'create_new']")
    private WebElement createNewBankDetails;

    @FindBy(how = How.XPATH, using = "//button[@id = 'fetchBank']")
    @CacheLookup
    private WebElement fetchBankButton;

    @FindBy(how = How.XPATH, using = "//button[@id = 'fetchBank'][contains(text(), 'Wait...')]")
    @CacheLookup
    private WebElement fetchBankWaitButton;

    @FindBy(how = How.XPATH, using = "//div[contains(@id, 'content_bank_details_bankName')]//ul[@id = 'holder']")
    private WebElement popupBankName;

    @FindBy(how = How.XPATH, using = "//li[contains(@id, 'listitem_bank_details_bankName_')]")
    private List<WebElement> selectBankNameOptionElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id, 'content_bank_detail_bankBranch')]//ul[@id = 'holder']")
    private WebElement popupBrandName;

    @FindBy(how = How.XPATH, using = "//li[contains(@id, 'listitem_bank_detail_bankBranch_')]")
    private List<WebElement> selectBranchNameOptionElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'deleteTag')]")
    @CacheLookup
    private List<WebElement> deleteBankDetailsElement;

    @FindBy(how = How.ID, using = "bankSaveAndNextButton2")
    @CacheLookup
    private WebElement btnSaveAndNextElement;

    public DE_ApplicationInfoBankCreditCardDetailsTabNeo(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    public void setBankDetailsData(List<BankCreditCardDetailsDTO> bankCreditCardDetailsDTO) {
        with().pollInterval(Duration.FIVE_SECONDS).await("Button create new bank details visible timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> createNewBankDetails.isDisplayed());

        createNewBankDetails.click();

        int index = 1;

        int sizeTableBanks = tableBankDetails.size();

        with().pollInterval(Duration.FIVE_SECONDS).await("Table create new bank details visible timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> sizeTableBanks > 2);

//        with().pollInterval(Duration.FIVE_SECONDS).await("Button Fetch Bank visible timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                .until(() -> fetchBankButton.isDisplayed());
//        fetchBankButton.click();

        Utilities.captureScreenShot(_driver);

        for (BankCreditCardDetailsDTO data : bankCreditCardDetailsDTO) {

            WebElement currentAccountNumber = _driver.findElement(By.id("bank_Detail_accountNumber_" + index));

            currentAccountNumber.clear();

            System.out.println("BANK NAME: " + data.getBankName());

            WebElement textBankName = _driver.findElement(By.id("Text_bank_details_bankName_" + index));

            textBankName.clear();

            textBankName.sendKeys(data.getBankName());

            await("Select BankName Element displayed timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> popupBankName.isDisplayed());

            int sizeSelectBankName = selectBankNameOptionElement.size();

            await("List BankName Element displayed timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> sizeSelectBankName > 0);
            JavascriptExecutor js = (JavascriptExecutor)_driver;
            for (WebElement e : selectBankNameOptionElement) {
                if (!Objects.isNull(e.getAttribute("username")) && StringEscapeUtils.unescapeJava(e.getAttribute("username")).toUpperCase().equals(data.getBankName().toUpperCase())) {
                    js.executeScript(e.findElement(By.xpath("//*[@id='"+e.getAttribute("id")+"']/a")).getAttribute("onclick"));
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
                if (!Objects.isNull(e.getAttribute("username")) && StringEscapeUtils.unescapeJava(e.getAttribute("username")).toUpperCase().equals(data.getBranchName().toUpperCase())) {
                    js.executeScript(e.findElement(By.xpath("//*[@id='"+e.getAttribute("id")+"']/a")).getAttribute("onclick"));
                    break;
                }
            }

            Utilities.captureScreenShot(_driver);

            if (data.getTypeOfAccount() != null || StringUtils.isNotEmpty(data.getTypeOfAccount())){

                System.out.println("TYPE OF ACCOUNT: " + data.getTypeOfAccount());

                WebElement typeofAccountElement = _driver.findElement(By.xpath("//div[@id = 'accountTypeId_" + index + "_chosen']"));

                await("Type Of Account Element not enabled!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> typeofAccountElement.isEnabled());

                List<WebElement> listTypeofAccountElement = _driver.findElements(By.xpath("//*[contains(@id, 'accountTypeId_" + index + "_chosen_o_')]"));

                await("list Type Of Account Element loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> listTypeofAccountElement.size() > 0);

                Utilities.chooseDropdownValue(data.getTypeOfAccount(), listTypeofAccountElement);

                Utilities.captureScreenShot(_driver);

            }

            System.out.println("CURRENT ACCOUNT NUMBER IN TP BANK: " + data.getAccountNumber());

            currentAccountNumber.clear();

            currentAccountNumber.sendKeys(data.getAccountNumber());

            Utilities.captureScreenShot(_driver);

            //Default Account
            WebElement defaultAccount = _driver.findElement(By.xpath("//input[@id='selectedAccountType_" + index + "']"));
            if (defaultAccount != null) {
                defaultAccount.click();
                System.out.println("AUTOMATION TICKED DEFAULT ACCOUNT");
            }else
                System.out.println("NOT FOUND ELEMENT selectedAccountType_" + index);

            Utilities.captureScreenShot(_driver);

            if (index < bankCreditCardDetailsDTO.size() - 1) {
                await("Button create new bank details visible timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> createNewBankDetails.isEnabled());
                createNewBankDetails.click();
            }

            index++;

        }
    }

    public void updateBankDetailsData(List<BankCreditCardDetailsDTO> bankCreditCardDetailsDTO) {
        int index = 0;
        if(_driver.findElements(By.xpath("//*[contains(@id, 'deleteTag')]")).size()!=0){
            await("Load deleteIdDetailElement Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> deleteBankDetailsElement.size() > 0);

            System.out.println("Size Delete => " + deleteBankDetailsElement.size());

            for (int i=0; i<deleteBankDetailsElement.size(); i++)
            {
                WebElement var = deleteBankDetailsElement.get(i);
                var.click();
            }
            index = 1;
        }
        if (bankCreditCardDetailsDTO == null || bankCreditCardDetailsDTO.size() < 1){
            return;
        }

        with().pollInterval(Duration.FIVE_SECONDS).await("Button create new bank details visible timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> createNewBankDetails.isDisplayed());

        createNewBankDetails.click();

        int sizeTableBanks = tableBankDetails.size();

        with().pollInterval(Duration.FIVE_SECONDS).await("Table create new bank details visible timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> sizeTableBanks > 2);

//        with().pollInterval(Duration.FIVE_SECONDS).await("Button Fetch Bank visible timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                .until(() -> fetchBankButton.isDisplayed());

//        fetchBankButton.click();

        Utilities.captureScreenShot(_driver);

        for (BankCreditCardDetailsDTO data : bankCreditCardDetailsDTO) {

            WebElement currentAccountNumber = _driver.findElement(By.id("bank_Detail_accountNumber_" + index));

            currentAccountNumber.clear();

            System.out.println("BANK NAME: " + data.getBankName());

            WebElement textBankName = _driver.findElement(By.id("Text_bank_details_bankName_" + index));

            textBankName.clear();

            textBankName.sendKeys(data.getBankName());

            await("Select BankName Element displayed timeout!").atMost(Constant.TIME_OUT_5_M, TimeUnit.SECONDS)
                    .until(() -> popupBankName.isDisplayed());

            int sizeSelectBankName = selectBankNameOptionElement.size();

            await("List BankName Element displayed timeout!").atMost(Constant.TIME_OUT_5_M, TimeUnit.SECONDS)
                    .until(() -> sizeSelectBankName > 0);

            for (WebElement e : selectBankNameOptionElement) {
                if (!Objects.isNull(e.getAttribute("username")) && StringEscapeUtils.unescapeJava(e.getAttribute("username")).toUpperCase().equals(data.getBankName().toUpperCase())) {
                    e.click();
                    break;
                }
            }

            Utilities.captureScreenShot(_driver);

            System.out.println("BRANCH NAME: " + data.getBranchName());

            WebElement textBrandName = _driver.findElement(By.id("Text_bank_detail_bankBranch_" + index));

            textBrandName.clear();

            textBrandName.sendKeys(data.getBranchName());

            await("Select BranchName Element displayed timeout!").atMost(Constant.TIME_OUT_5_M, TimeUnit.SECONDS)
                    .until(() -> popupBrandName.isDisplayed());

            Utilities.captureScreenShot(_driver);

            int sizeSelectBranchName = selectBranchNameOptionElement.size();

            await("List BranchName Element displayed timeout!").atMost(Constant.TIME_OUT_5_M, TimeUnit.SECONDS)
                    .until(() -> sizeSelectBranchName > 0);

            for (WebElement e : selectBranchNameOptionElement) {
                if (!Objects.isNull(e.getAttribute("username")) && StringEscapeUtils.unescapeJava(e.getAttribute("username")).toUpperCase().equals(data.getBranchName().toUpperCase())) {
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

            currentAccountNumber.clear();

            currentAccountNumber.sendKeys(data.getAccountNumber());

            Utilities.captureScreenShot(_driver);

            //Default Account
            WebElement defaultAccount = _driver.findElement(By.xpath("//input[@id='selectedAccountType_" + index + "']"));
            if (defaultAccount != null) {
                defaultAccount.click();
                System.out.println("AUTOMATION TICKED DEFAULT ACCOUNT");
            }else
                System.out.println("NOT FOUND ELEMENT selectedAccountType_" + index);

            Utilities.captureScreenShot(_driver);

            if ((index == 1 && index < bankCreditCardDetailsDTO.size())
                    || (index == 0 && index < bankCreditCardDetailsDTO.size() - 1)) {
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
