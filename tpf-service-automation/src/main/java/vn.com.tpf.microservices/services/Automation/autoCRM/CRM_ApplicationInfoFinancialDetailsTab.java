package vn.com.tpf.microservices.services.Automation.autoCRM;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.Getter;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.models.AutoCRM.CRM_FinancialDetailsDTO;
import vn.com.tpf.microservices.models.Automation.IncomeDetailDTO;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Getter
public class CRM_ApplicationInfoFinancialDetailsTab {
    private WebDriver _driver;

    @FindBy(how = How.ID, using = "customerMainChildTabs_income_tab")
    @CacheLookup
    private WebElement customerMainChildTabs_income_tabElement;

    // Income Details
    @FindBy(how = How.ID, using = "finInfo_IncomeInlineGrid_link")
    @CacheLookup
    private WebElement loadIncomeDetailElement;

    @FindBy(how = How.ID, using = "finInfo_IncomeInlineGrid_AccDiv")
    @CacheLookup
    private WebElement incomeDetailDivElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'addMoreIncDetails')]")
    @CacheLookup
    private WebElement btnAddMoreIncomeDtlElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'rowIncDetails')]//td")
    private List<WebElement> trElements;

    // @FindBy(how = How.ID, using = "financialSaveAndNextButton2")
    @FindBy(how = How.ID, using = "financialSaveAndNextButton1")
    @CacheLookup
    private WebElement btnSaveAndNextElement;

    @FindBy(how = How.ID, using = "childModalIncomeDetailInlineGrid")
    @CacheLookup
    private WebElement childModalIncomeDetailInlineGridElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'incomeDetailForm_paymentMode_chosen')]")
    private WebElement incomeDetailForm_incomeSourceElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'incomeDetailForm_paymentMode_chosen_o_')]")
    private List<WebElement> incomeDetailForm_incomeSourceOptionElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'incomeDetailForm_paymentMode_chosen')]//input")
    private WebElement incomeDetailForm_incomeSourceInputElement;

    @FindBy(how = How.ID, using = "incomeDetailForm_paymentMode_chosen")
    private WebElement incomeDetailForm_paymentModeElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'incomeDetailForm_paymentMode_chosen_o_')]")
    private List<WebElement> incomeDetailForm_paymentModeOptionElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'incomeDetailForm_paymentMode_chosen')]//input")
    private WebElement incomeDetailForm_paymentModeInputElement;

    @FindBy(how = How.ID, using = "childModalWindowDoneButtonIncomeDetailInlineGrid")
    private WebElement childModalWindowDoneButtonIncomeDetailInlineGridElement;

    //----------------------------UPDATE---------------------------------
    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'DeleteIncDetails')]")
    @CacheLookup
    private List<WebElement> deleteIncDetailsElement;


    public CRM_ApplicationInfoFinancialDetailsTab(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this._driver = driver;
    }

    public void openIncomeDetailSection() {
        this.loadIncomeDetailElement.click();
    }

    public void openFinancialDetailsTabSection() {
        this.customerMainChildTabs_income_tabElement.click();
    }

    public void setIncomeDetailsData(CRM_FinancialDetailsDTO data) throws JsonParseException, JsonMappingException, IOException, InterruptedException {
        int index = 0;
        final int _index = index;
        int size= trElements.size();
        await("IncomeDetails Tr container not displayed - Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> trElements.size() > 2);

        WebElement incomeHead = _driver.findElement(By.id("incomeDetailForm_incomeHead_" + index + "_chosen"));
        incomeHead.click();
        List<WebElement> incomeHeads = _driver.findElements(By.xpath("//*[contains(@id, 'incomeDetailForm_incomeHead_" + index + "_chosen_o_')]"));
        for (WebElement element : incomeHeads) {
            if (element.getText().equals(data.getIncomeExpense())) {
                element.click();
                break;
            }
        }

        if(data.getIncomeExpense().equals("Main Personal Income")) {
            //add income detail
            _driver.findElement(By.xpath("//*[contains(@id, 'rowIncDetails" + index + "')]//a[contains(@class,'fs-10')]")).click();
            await("childModalIncomeDetailInlineGridElement not displayed - Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> childModalIncomeDetailInlineGridElement.isDisplayed());
            Actions actions = new Actions(_driver);

            await("incomeDetailForm_incomeSourceElement not enabled - Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> incomeDetailForm_incomeSourceElement.isEnabled());

            actions.moveToElement(incomeDetailForm_incomeSourceElement).click().build().perform();
            await("incomeDetailForm_incomeSourceOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> incomeDetailForm_incomeSourceOptionElement.size() > 1);

            Utilities.chooseDropdownValue(data.getDayOfSalaryPayment(),incomeDetailForm_incomeSourceOptionElement);

            incomeDetailForm_incomeSourceInputElement.sendKeys(data.getDayOfSalaryPayment());
            incomeDetailForm_incomeSourceInputElement.sendKeys(Keys.ENTER);
            Utilities.captureScreenShot(_driver);
            incomeDetailForm_paymentModeElement.click();
            await("incomeDetailForm_paymentModeOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)

                    .until(() -> incomeDetailForm_paymentModeOptionElement.size() > 1);
            incomeDetailForm_paymentModeInputElement.sendKeys(data.getModeOfPayment());
            incomeDetailForm_paymentModeInputElement.sendKeys(Keys.ENTER);

            Utilities.captureScreenShot(_driver);

            childModalWindowDoneButtonIncomeDetailInlineGridElement.click();
            Thread.sleep(3000);
        }

        WebElement frequency = _driver.findElement(By.id("incomeDetailForm_frequency_" + index + "_chosen"));
        frequency.click();
        List<WebElement> frequencys = _driver.findElements(By.xpath("//*[contains(@id, 'incomeDetailForm_frequency_" + index + "_chosen_o_')]"));
        for (WebElement element : frequencys) {
            if (element.getText().equals(data.getFrequency())) {
                element.click();
                break;
            }
        }

        Utilities.captureScreenShot(_driver);

        WebElement we =_driver.findElement(By.id("amount_incomeDetailForm_amount_" + index));
        Utilities.checkValueSendkeyAmount(data.getAmount(),we);

        Utilities.captureScreenShot(_driver);



//        if (index < datas.size() - 1) {
//            await("Btn Add IncomeDetail not enabled - Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> btnAddMoreIncomeDtlElement.isEnabled());
//            btnAddMoreIncomeDtlElement.click();
//        }
//        index++;
    }

    /**
     * Main Income need to add more infomation and can be make an Issue
     * @param datas
     * @return
     */
    private List<IncomeDetailDTO> switchMainIncometoLast(List<IncomeDetailDTO> datas) {
        List<IncomeDetailDTO> newDatas = datas;
        if ("Main Personal Income".equals(datas.get(0).getIncomeHead()) && datas.size() > 1) {
            IncomeDetailDTO mainDeatailIncome = datas.get(0);
            newDatas.set(0, datas.get(datas.size() - 1));
            newDatas.set(datas.size() - 1, mainDeatailIncome);

        }
        return newDatas;
    }

    public void updateIncomeDetailsData2( CRM_FinancialDetailsDTO datas) throws JsonParseException, JsonMappingException, IOException, InterruptedException {
        Actions actions = new Actions(_driver);
        await("Load deleteIdDetailElement Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> deleteIncDetailsElement.size() > 0);

        for (int i=0; i<deleteIncDetailsElement.size(); i++)
        {
            WebElement var = deleteIncDetailsElement.get(i);
            var.click();
        }

        btnAddMoreIncomeDtlElement.click();
        int indexRow=deleteIncDetailsElement.size();
        int index = 0;
        //List<IncomeDetailDTO> newDatas = switchMainIncometoLast(datas);
        //for (IncomeDetailDTO data : newDatas) {


            final int _index = index;
            await("IncomeDetails Tr container not displayed - Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> trElements.size() > _index);
            Thread.sleep(5000);//wait to insert income row
            WebElement frequency = _driver.findElement(By.id("incomeDetailForm_frequency_" + indexRow + "_chosen"));
            frequency.click();
            List<WebElement> frequencies = _driver.findElements(By.xpath("//*[contains(@id, 'incomeDetailForm_frequency_" + indexRow + "_chosen_o_')]"));
            for (WebElement element : frequencies) {
                if (element.getText().equals(datas.getFrequency())) {
                    element.click();
                    break;
                }
            }


            //WebElement incomeHead = _driver.findElement(By.id("incomeDetailForm_incomeHead_" + indexRow + "_chosen"));
            WebElement incomeHead = _driver.findElement(By.xpath("//*[contains(@id, 'incomeDetailForm_incomeHead_"+indexRow+"_chosen')]//*[contains(text(),'Select')]"));
            actions.moveToElement(incomeHead).click().perform();
            WebElement incomeHeadInput = _driver.findElement(By.xpath("//*[contains(@id, 'incomeDetailForm_incomeHead_"+indexRow+"_chosen')]//input"));
            incomeHeadInput.sendKeys(datas.getIncomeExpense());
            //Thread.sleep(5000);//wait to show income list
            System.out.println("click incomeHead: " + indexRow);
            List<WebElement> incomeHeads = _driver.findElements(By.xpath("//*[contains(@id, 'incomeDetailForm_incomeHead_" + indexRow + "_chosen_o_')]"));
            await("IncomeList is not display - Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> incomeHeads.size() > 0);
            for (WebElement element : incomeHeads) {
                if (element.getText().equals(datas.getIncomeExpense())) {
                    actions.moveToElement(element).click().perform();
                    //element.click();
                    break;
                }
            }

            _driver.findElement(By.id("amount_incomeDetailForm_amount_" + indexRow)).clear();
            _driver.findElement(By.id("amount_incomeDetailForm_amount_" + indexRow)).sendKeys(datas.getAmount());

            if(datas.getIncomeExpense().equals("Main Personal Income")) {
                //add income detail
//                _driver.findElement(By.xpath("//*[contains(@id, 'rowIncDetails" + indexRow + "')]//a[contains(@class,'fs-10')]")).click();
                String onlickMoreDetail = _driver.findElement(By.xpath("//*[contains(@id, 'rowIncDetails" + indexRow + "')]//a[contains(@class,'fs-10')]")).getAttribute("onclick");
                JavascriptExecutor js= (JavascriptExecutor)_driver;
                js.executeScript(onlickMoreDetail);
                await("childModalIncomeDetailInlineGridElement not displayed - Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> childModalIncomeDetailInlineGridElement.isDisplayed());

//                await("incomeDetailForm_incomeSourceElement not enabled - Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                        .until(() -> incomeDetailForm_incomeSourceElement.isEnabled());
//
//                actions.moveToElement(incomeDetailForm_incomeSourceElement).click().build().perform();
//                await("cityOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                        .until(() -> incomeDetailForm_incomeSourceOptionElement.size() > 1);
//                incomeDetailForm_incomeSourceInputElement.sendKeys(data.getDayOfSalaryPayment());
//                incomeDetailForm_incomeSourceInputElement.sendKeys(Keys.ENTER);
                Thread.sleep(2000);//wait to load income mode
                incomeDetailForm_paymentModeElement.click();
                await("cityOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> incomeDetailForm_paymentModeOptionElement.size() > 1);
                incomeDetailForm_paymentModeInputElement.sendKeys(datas.getModeOfPayment());
                incomeDetailForm_paymentModeInputElement.sendKeys(Keys.ENTER);

                childModalWindowDoneButtonIncomeDetailInlineGridElement.click();
                Thread.sleep(3000);
            }

           /* if (index < datas.size() - 1) {
                await("Btn Add IncomeDetail not enabled - Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> btnAddMoreIncomeDtlElement.isEnabled());
                btnAddMoreIncomeDtlElement.click();
            }*/
            index++;
            indexRow++;
    }



    public void updateIncomeDetailsData(CRM_FinancialDetailsDTO data) throws JsonParseException, JsonMappingException, IOException, InterruptedException {

        await("Load deleteIdDetailElement Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> deleteIncDetailsElement.size() > 0);

        for (int i=0; i<deleteIncDetailsElement.size(); i++)
        {
            WebElement var = deleteIncDetailsElement.get(i);
            var.click();
        }

        btnAddMoreIncomeDtlElement.click();

        int indexRow=deleteIncDetailsElement.size();
        int index = 0;
//        for (IncomeDetailDTO data : datas) {

            System.out.println(data.getIncomeExpense());

            final int _index = index;
            int size=trElements.size();
            await("IncomeDetails Tr container not displayed - Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> trElements.size() > _index);

            WebElement incomeHead = _driver.findElement(By.id("incomeDetailForm_incomeHead_" + indexRow + "_chzn"));
            incomeHead.click();
            List<WebElement> incomeHeads = _driver.findElements(By.xpath("//*[contains(@id, 'incomeDetailForm_incomeHead_" + indexRow + "_chzn_o_')]"));
            for (WebElement element : incomeHeads) {
                if (element.getText().equals(data.getIncomeExpense())) {
                    element.click();
                    break;
                }
            }

            WebElement frequency = _driver.findElement(By.id("incomeDetailForm_frequency_" + indexRow + "_chzn"));
            frequency.click();
            List<WebElement> frequencys = _driver.findElements(By.xpath("//*[contains(@id, 'incomeDetailForm_frequency_" + indexRow + "_chzn_o_')]"));
            for (WebElement element : frequencys) {
                if (element.getText().equals(data.getFrequency())) {
                    element.click();
                    break;
                }
            }
            _driver.findElement(By.id("amount_incomeDetailForm_amount_" + indexRow)).clear();
            _driver.findElement(By.id("amount_incomeDetailForm_amount_" + indexRow)).sendKeys(data.getAmount());

            if(data.getIncomeExpense().equals("Main Personal Income")) {
                //add income detail
                _driver.findElement(By.xpath("//*[contains(@id, 'rowIncDetails" + indexRow + "')]//a[contains(@class,'fs-10')]")).click();
                await("childModalIncomeDetailInlineGridElement not displayed - Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> childModalIncomeDetailInlineGridElement.isDisplayed());
                Actions actions = new Actions(_driver);

                await("incomeDetailForm_incomeSourceElement not enabled - Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> incomeDetailForm_incomeSourceElement.isEnabled());

                actions.moveToElement(incomeDetailForm_incomeSourceElement).click().build().perform();
                await("cityOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> incomeDetailForm_incomeSourceOptionElement.size() > 1);
                incomeDetailForm_incomeSourceInputElement.sendKeys(data.getDayOfSalaryPayment());
                incomeDetailForm_incomeSourceInputElement.sendKeys(Keys.ENTER);

                incomeDetailForm_paymentModeElement.click();
                await("cityOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> incomeDetailForm_paymentModeOptionElement.size() > 1);
                incomeDetailForm_paymentModeInputElement.sendKeys(data.getModeOfPayment());
                incomeDetailForm_paymentModeInputElement.sendKeys(Keys.ENTER);

                childModalWindowDoneButtonIncomeDetailInlineGridElement.click();
                Thread.sleep(3000);
            }

//            if (index < datas.size() - 1) {
//                await("Btn Add IncomeDetail not enabled - Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                        .until(() -> btnAddMoreIncomeDtlElement.isEnabled());
//                btnAddMoreIncomeDtlElement.click();
//            }
//            index++;
//            indexRow++;
//        }
    }

    public void saveAndNext() {
        this.btnSaveAndNextElement.click();
    }
}
