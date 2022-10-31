package vn.com.tpf.microservices.services.Automation.returnQuery;

import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.awaitility.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.springframework.util.StringUtils;
import vn.com.tpf.microservices.models.AutoReturnQuery.ResponseQueryDTO;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.with;

@Getter
public class ResponseQueryPage {

    private WebDriver _driver;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]")
    @CacheLookup
    private WebElement menuApplicationElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]//div[contains(@class,'one-col')][3]//li//a[contains(text(),'Response Query')]")
    @CacheLookup
    private WebElement responseQueryElement;

    @FindBy(how = How.ID, using = "queries_to_be_responded_wrapper")
    private List<WebElement> divAppIdResponseElement;

    @FindBy(how = How.XPATH, using = "//table[@id='queries_to_be_responded']//tbody//tr//td")
    @CacheLookup
    private List<WebElement> tableAppIdResponseElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id,'queries_to_be_responded_filter')]//input")
    @CacheLookup
    private WebElement textSearchAppIdResponseElement;

//    @FindBy(how = How.ID, using = "openApplicationOfQuery")
//    @CacheLookup
//    private WebElement idRowResponseQueryElement;

    @FindBy(how = How.XPATH, using = "//table[@id='response_grid_table']//tbody//tr//td")
    private List<WebElement> tableQueryElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'tablediv']//div[@id = 'response_grid_table_filter']//input")
    @CacheLookup
    private WebElement textSeachQueryElement;

    @FindBy(how = How.XPATH, using = "//table[@id='response_grid_table']//tbody//tr//td//a[contains(@id,'calculation')]")
    @CacheLookup
    private WebElement rowQueryCodeElement;

    @FindBy(how = How.ID, using = "queryResponse")
    @CacheLookup
    private WebElement textareaResponseElement;

    @FindBy(how = How.ID, using = "photoimg")
    @CacheLookup
    private WebElement btnUploadFileElement;

    @FindBy(how = How.ID, using = "submitResponseButton")
    @CacheLookup
    private WebElement btnSubmitResponseElement;

    public ResponseQueryPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    public void setData(ResponseQueryDTO responseQueryDTO, String downLoadFileURL) throws IOException, InterruptedException {
        ((RemoteWebDriver) _driver).setFileDetector(new LocalFileDetector());

        textSearchAppIdResponseElement.clear();

        textSearchAppIdResponseElement.sendKeys(responseQueryDTO.getApplicationId());

        with().pollInterval(Duration.FIVE_SECONDS).
        await("Search Application ID Response Query Not Found!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tableAppIdResponseElement.size() > 2);

        Utilities.captureScreenShot(_driver);

        WebElement rowAppIdResponseQuery = _driver.findElement(By.xpath("//table[@id = 'queries_to_be_responded']//*[contains(text(),'" + responseQueryDTO.getApplicationId() + "')]//ancestor::tr//td//a[@id = 'openApplicationOfQuery']"));

        rowAppIdResponseQuery.click();

        with().pollInterval(Duration.FIVE_SECONDS).
        await("tbDivResponseQueryElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tableQueryElement.size() > 2);

        Utilities.captureScreenShot(_driver);

        textSeachQueryElement.clear();

        if (Objects.isNull(responseQueryDTO.getQueryName())){
            textSeachQueryElement.sendKeys("T_RETURN");
        }else{
            textSeachQueryElement.sendKeys(responseQueryDTO.getQueryName());
        }

        int sizeTableQueryElement = tableQueryElement.size();

        with().pollInterval(Duration.FIVE_SECONDS).
        await("Raise Query Not Found!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> sizeTableQueryElement > 2);

        Utilities.captureScreenShot(_driver);

        rowQueryCodeElement.click();

        textareaResponseElement.clear();
        textareaResponseElement.sendKeys(responseQueryDTO.getCommentText());

        if(!Objects.isNull(responseQueryDTO.getDataDocument())&& !StringUtils.isEmpty(responseQueryDTO.getDataDocument().getFileName())) {
            String fromFile = downLoadFileURL;
            System.out.println("URLdownload: " + fromFile);

            String docName = responseQueryDTO.getDataDocument().getFileName();
            String toFile = Constant.SCREENSHOT_PRE_PATH_DOCKER;
            toFile += UUID.randomUUID().toString() + "_" + docName;

            FileUtils.copyURLToFile(new URL(fromFile + URLEncoder.encode(docName, "UTF-8").replaceAll("\\+", "%20")), new File(toFile), 10000, 10000);
            File file = new File(toFile);
            if (file.exists()) {
                String docUrl = file.getAbsolutePath();
                System.out.println("PATH: " + docUrl);
                Thread.sleep(2000);
                btnUploadFileElement.sendKeys(docUrl);
                Utilities.captureScreenShot(_driver);
            }

            Utilities.captureScreenShot(_driver);
        }

        btnSubmitResponseElement.click();

        Utilities.captureScreenShot(_driver);

        with().pollInterval(Duration.FIVE_SECONDS).
        await("Button Submit Respone Error!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> divAppIdResponseElement.size() > 0);

        Utilities.captureScreenShot(_driver);

    }

}
