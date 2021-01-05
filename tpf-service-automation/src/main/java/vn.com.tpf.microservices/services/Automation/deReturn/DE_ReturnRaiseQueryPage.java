package vn.com.tpf.microservices.services.Automation.deReturn;

import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.springframework.util.StringUtils;
import vn.com.tpf.microservices.models.DEReturn.DEResponseQueryDTO;
import vn.com.tpf.microservices.models.DEReturn.DEResponseQueryDocumentDTO;
import vn.com.tpf.microservices.models.Document;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
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

    @FindBy(how = How.XPATH, using = "//table[@id='queries_to_be_responded']//tbody//tr//td")
    @CacheLookup
    private List<WebElement> tdResponseQueryElement;

    @FindBy(how = How.ID, using = "openApplicationOfQuery")
    @CacheLookup
    private WebElement idRowResponseQueryElement;

    @FindBy(how = How.XPATH, using = "//table[@id='response_grid_table']//tbody//tr//td")
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
//    private List<WebElement> btnUploadFileElement;

    @FindBy(how = How.ID, using = "submitResponseButton")
    @CacheLookup
    private WebElement btnSubmitResponseElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'applicationDocument_name')]//span[contains(@class,'fw-b fs-4')]")
    @CacheLookup
    private List<WebElement> docNameElement;

    public DE_ReturnRaiseQueryPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    @SneakyThrows
    public void setData(DEResponseQueryDTO deResponseQueryDTO, String downLoadFileURL) {
        ((RemoteWebDriver) _driver).setFileDetector(new LocalFileDetector());
        await("responseQueryFormElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> responseQueryFormElement.isDisplayed());

        applicationNumberElement.clear();
        applicationNumberElement.sendKeys(deResponseQueryDTO.getAppId());

        await("tdResponseQueryElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tdResponseQueryElement.size() > 2);

        idRowResponseQueryElement.click();

        await("tbDivResponseQueryElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tbDivResponseQueryElement.size() > 2);


        textSeachResponseQueryElement.clear();
        textSeachResponseQueryElement.sendKeys("T_RETURN");
        idRowCalculationElement.click();
        await("tbDivResponseQueryElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tbDivResponseQueryElement.size() > 2);

        textResponseElement.clear();
        textResponseElement.sendKeys(deResponseQueryDTO.getCommentText());

        if(!Objects.isNull(deResponseQueryDTO.getDataDocument())&& !StringUtils.isEmpty(deResponseQueryDTO.getDataDocument().getFileName())) {
            String fromFile = downLoadFileURL;
            System.out.println("URLdownload: " + fromFile);

            String docName = deResponseQueryDTO.getDataDocument().getFileName();
            String toFile = Constant.SCREENSHOT_PRE_PATH_DOCKER;
            toFile += UUID.randomUUID().toString() + "_" + docName;

            FileUtils.copyURLToFile(new URL(fromFile + URLEncoder.encode(docName, "UTF-8").replaceAll("\\+", "%20")), new File(toFile), 10000, 10000);
            File file = new File(toFile);
            if (file.exists()) {
                String docUrl = file.getAbsolutePath();
                System.out.println("Path;" + docUrl);
                Thread.sleep(2000);

                btnUploadFileElement.sendKeys(docUrl);

                Utilities.captureScreenShot(_driver);
            }

            Utilities.captureScreenShot(_driver);
        }
        btnSubmitResponseElement.click();

    }
}
