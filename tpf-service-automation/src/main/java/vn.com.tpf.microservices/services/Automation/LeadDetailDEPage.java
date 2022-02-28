package vn.com.tpf.microservices.services.Automation;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Getter
public class LeadDetailDEPage {
    private WebDriver _driver;

    @FindBy(how = How.ID, using = "loanApplicationExistingParties")
    @CacheLookup
    private WebElement loanApplicationExistingElement;

    @FindBy(how = How.ID, using = "edit-customer-inDetail_0")
    @CacheLookup
    private WebElement editCustomerElement;

    @FindBy(how = How.ID, using = "applicationChildTabs_customerPersonal")
    @CacheLookup
    private WebElement applicationChildTabs;

    public LeadDetailDEPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    public void setData(String appId) {
        await("loanApplicationExistingElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> loanApplicationExistingElement.isDisplayed());

        Utilities.captureScreenShot(_driver);

        editCustomerElement.click();
    }

    public void applicationInformation() {
        await("loanApplicationExistingElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> applicationChildTabs.isDisplayed());
        applicationChildTabs.click();
        await("editCustomerElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> editCustomerElement.isDisplayed());

        Utilities.captureScreenShot(_driver);

        editCustomerElement.click();
    }
}
