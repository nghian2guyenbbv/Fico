package vn.com.tpf.microservices.services.Automation.lending;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.models.Automation.IncomeDetailDTO;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Getter
public class LD_ApplicationInfoFinancialDetailsTab {
    private WebDriver _driver;

    // Income Details
    @FindBy(how = How.ID, using = "finInfo_IncomeInlineGrid_link")
    @CacheLookup
    private WebElement loadIncomeDetailElement;

    @FindBy(how = How.ID, using = "finInfo_IncomeInlineGrid_AccDiv")
    @CacheLookup
    private WebElement incomeDetailDivElement;

    @FindBy(how = How.ID, using = "addMoreIncDetails")
    @CacheLookup
    private WebElement btnAddMoreIncomeDtlElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'rowIncDetails')]")
    @CacheLookup
    private List<WebElement> trElements;

    @FindBy(how = How.ID, using = "financialSaveAndNextButton2")
    @CacheLookup
    private WebElement btnSaveAndNextElement;

    public LD_ApplicationInfoFinancialDetailsTab(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this._driver = driver;
    }

    public void setIncomeDetailsData(List<IncomeDetailDTO> datas) throws JsonParseException, JsonMappingException, IOException {
        int index = 0;
        for (IncomeDetailDTO data : datas) {
        	final int _index = index;
        	await("IncomeDetails Tr container not displayed - Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
            .until(() -> trElements.size() > _index);
        	
//			new Select(_driver.findElement(By.id("incomeDetailForm_incomeHead_" + index)))
//					.selectByVisibleText(data.getIncomeHead());
//			new Select(_driver.findElement(By.id("incomeDetailForm_frequency_" + index)))
//					.selectByVisibleText(data.getFrequency());

            WebElement incomeHead = _driver.findElement(By.id("incomeDetailForm_incomeHead_" + index + "_chzn"));
            incomeHead.click();
            List<WebElement> incomeHeads = _driver.findElements(By.xpath("//*[contains(@id, 'incomeDetailForm_incomeHead_" + index + "_chzn_o_')]"));
            for (WebElement element : incomeHeads) {
                if (element.getText().equals(data.getIncomeHead())) {
                    element.click();
                    break;
                }
            }

            WebElement frequency = _driver.findElement(By.id("incomeDetailForm_frequency_" + index + "_chzn"));
            frequency.click();
            List<WebElement> frequencys = _driver.findElements(By.xpath("//*[contains(@id, 'incomeDetailForm_frequency_" + index + "_chzn_o_')]"));
            for (WebElement element : frequencys) {
                if (element.getText().equals(data.getFrequency())) {
                    element.click();
                    break;
                }
            }
            _driver.findElement(By.id("amount_incomeDetailForm_amount_" + index)).sendKeys(data.getAmount());
//            _driver.findElement(By.id("incomeDetailForm_percentageToConsider_" + index)).sendKeys(data.getPercentage());
            if (index < datas.size() - 1) {
                await("Btn Add IncomeDetail not enabled - Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> btnAddMoreIncomeDtlElement.isEnabled());
                btnAddMoreIncomeDtlElement.click();
            }
            index++;
        }
    }

    public void openIncomeDetailSection() {
        this.loadIncomeDetailElement.click();
    }

    public void saveAndNext() {
        this.btnSaveAndNextElement.click();
    }

    // TODO: compare original data vs element data and report if not equals
    public void validInOutData(Map<String, String> mapValue, String jsonObj) throws Exception {
        IncomeDetailDTO[] datas = new ObjectMapper().readValue(jsonObj, IncomeDetailDTO[].class);
        int index = 0;
        for (IncomeDetailDTO data : datas) {
            List<WebElement> incomeHeads = _driver.findElements(By.xpath("//*[contains(@id, 'incomeDetailForm_incomeHead_" + index + "_chzn_o_')]"));
            Utilities.checkInput(mapValue, "ApplicationInfo_FinancialDetail_IncomeHead_" + index, data.getIncomeHead(), incomeHeads);
            List<WebElement> frequencys = _driver.findElements(By.xpath("//*[contains(@id, 'incomeDetailForm_frequency_" + index + "_chzn_o_')]"));
            Utilities.checkInput(mapValue, "ApplicationInfo_FinancialDetail_Frequency_" + index, data.getFrequency(), frequencys);
            Utilities.checkInput(mapValue, "ApplicationInfo_FinancialDetail_Amount_" + index, data.getAmount(), _driver.findElement(By.id("amount_incomeDetailForm_amount_" + index)));
            Utilities.checkInput(mapValue, "ApplicationInfo_FinancialDetail_Percentage_" + index, data.getPercentage(), _driver.findElement(By.id("incomeDetailForm_percentageToConsider_" + index)));
            index++;
        }
    }
}
