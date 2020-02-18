package vn.com.tpf.microservices.services.Automation.deReturn;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.models.DEReturn.DEResponseQueryDocumentDTO;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Getter
public class DE_ReturnRaiseQueryPage {
    private WebDriver _driver;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]//div[contains(@class,'one-col')][3]//li//a[contains(text(),'Response Query')]")
    @CacheLookup
    private WebElement responseQueryElement;

    @FindBy(how = How.ID, using = "queries_to_be_responded_wrapper")
    @CacheLookup
    private WebElement responseQueryFormElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id,'queries_to_be_responded_filter')]//input[contains(@type, 'text')]")
    @CacheLookup
    private WebElement applicationNumberElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'applicationManagerForm1')]//input[@type='button']")
    @CacheLookup
    private WebElement searchApplicationElement;

    @FindBy(how = How.XPATH, using = "//table[@id='queries_to_be_responded']//tbody//tr")
    @CacheLookup
    private List<WebElement> tdResponseQueryElement;

    @FindBy(how = How.ID, using = "openApplicationOfQuery")
    @CacheLookup
    private WebElement idRowResponseQueryElement;

    @FindBy(how = How.XPATH, using = "//table[@id='response_grid_table']//tbody//tr")
    @CacheLookup
    private List<WebElement> tbDivResponseQueryElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id,'tablediv')]//div[contains(@id,'response_grid_table_wrapper')]//table[@id='response_grid_table']//tbody//tr//td//a[contains(@id,'calculation')]")
    @CacheLookup
    private WebElement idRowCalculationElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id,'queryResponse_forward')]//div[contains(@id,'queryResponse-control-group')]//textarea[contains(@id, 'queryResponse')]")
    @CacheLookup
    private WebElement textResponseElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id, tablediv)]//div[contains(@id,'response_grid_table_filter')]//input[@type='text']")
    @CacheLookup
    private WebElement textSeachResponseQueryElement;

//    @FindBy(how = How.XPATH, using = "//div[contains(@id, 'uploadGrid')]//input[@id='photoimg']")
    @FindBy(how = How.ID, using = "photoimg")
    @CacheLookup
    private WebElement btnUploadFileElement;

    @FindBy(how = How.ID, using = "submitResponseButton")
    @CacheLookup
    private WebElement btnSubmitResponseElement;

    public DE_ReturnRaiseQueryPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    public void setData(String appId, List<DEResponseQueryDocumentDTO> lstDocument, String user) {
        await("appManager_lead_application_number visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> responseQueryFormElement.isDisplayed());

        applicationNumberElement.clear();
        applicationNumberElement.sendKeys(appId);

        await("tdApplicationElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tdResponseQueryElement.size() > 0);

        idRowResponseQueryElement.click();

        await("tdApplicationElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tbDivResponseQueryElement.size() > 0);

        lstDocument.forEach(documentList -> {
            textSeachResponseQueryElement.clear();
            textSeachResponseQueryElement.sendKeys(documentList.getQuerycode());
            idRowCalculationElement.click();
            await("tdApplicationElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> tbDivResponseQueryElement.size() > 0);
            textResponseElement.clear();
            textResponseElement.sendKeys(documentList.getComment());
            btnUploadFileElement.sendKeys(documentList.getUrlfile());
        });

        Utilities.captureScreenShot(_driver);
//        btnSubmitResponseElement.click();
    }
}
