package vn.com.tpf.microservices.services.Automation.autoField;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.models.AutoField.WaiveFieldDTO;

public class FV_ExitingCustomerPage {
    private WebDriver _driver;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]")
    private WebElement menuApplicationElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]//div[contains(@class,'one-col')][2]//li//span[contains(text(),'Personal Loan  ')]")
    @CacheLookup
    private WebElement applicationElement;

    @FindBy(how = How.XPATH, using = "//input[@id='isExistCustomer']")
    @CacheLookup
    private WebElement isExistCustomerRadioElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'existingCustomerSearch']//form[starts-with(@id, 'applicantSearchVoForm')]")
    @CacheLookup
    private WebElement existingCustomerSearchFormElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'existingCustomerSearch']//form[starts-with(@id, 'applicantSearchVoForm')]//input[@id = 'customerNumber']")
    @CacheLookup
    private WebElement neoCustIDInputElement;



    public FV_ExitingCustomerPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    public void setData(WaiveFieldDTO waiveFieldDTO, String user) {
    }

}
