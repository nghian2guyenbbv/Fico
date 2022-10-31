package vn.com.tpf.microservices.services.Automation.autoCRM;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

@Getter
public class CRM_ApplicationInfoBankDetailsTab {
    private WebDriver _driver;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'applicantIdHeader')]/span")
    @CacheLookup
    private WebElement applicationId;

    @FindBy(how = How.ID, using = "bankDetailsGrid")
    @CacheLookup
    private WebElement bankDetailsGridElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'tableBank']//table[@id = 'bank_details_Table']//tbody//tr//td")
    @CacheLookup
    private List<WebElement> bankDetailsTableElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'tableBank']//table[@id = 'bank_details_Table']//tbody//tr//td//*[contains(@id, 'deleteTag')]")
    @CacheLookup
    private List<WebElement> deleteIdDetailElement;

    @FindBy(how = How.ID, using = "bankSaveAndNextButton2")
    @CacheLookup
    private WebElement btnSaveAndNextElement;



    public CRM_ApplicationInfoBankDetailsTab(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this._driver = driver;
    }

    public void saveAndNext() {
        this.btnSaveAndNextElement.click();
    }
}
