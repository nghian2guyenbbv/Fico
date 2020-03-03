package vn.com.tpf.microservices.services.Automation.deReturn;

import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.models.DEReturn.DEResponseQueryDTO;
import vn.com.tpf.microservices.models.DEReturn.DEResponseQueryDocumentDTO;
import vn.com.tpf.microservices.models.Document;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
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

    public void setData(DEResponseQueryDTO deResponseQueryDTO, String downLoadFileURL) {
        try{
            await("appManager_lead_application_number visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> responseQueryFormElement.isDisplayed());

            applicationNumberElement.clear();
            applicationNumberElement.sendKeys(deResponseQueryDTO.getAppId());

            await("tdApplicationElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> tdResponseQueryElement.size() > 0);

            idRowResponseQueryElement.click();

            await("tdApplicationElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> tbDivResponseQueryElement.size() > 0);


            textSeachResponseQueryElement.clear();
            textSeachResponseQueryElement.sendKeys("T_RETURN");
            idRowCalculationElement.click();
            await("tdApplicationElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> tbDivResponseQueryElement.size() > 0);

            textResponseElement.clear();
            textResponseElement.sendKeys(deResponseQueryDTO.getDocument().getComments());

            String fromFile = downLoadFileURL;
            System.out.println("URLdownload: " + fromFile);

            String docName = deResponseQueryDTO.getDocument().getFileName();
            String toFile = Constant.SCREENSHOT_PRE_PATH_DOCKER;

            toFile+= UUID.randomUUID().toString()+"_"+ docName;
            FileUtils.copyURLToFile(new URL(fromFile + URLEncoder.encode(docName, "UTF-8").replaceAll("\\+", "%20")), new File(toFile), 10000, 10000);
            File file = new File(toFile);
            if(file.exists()) {
                String docUrl = file.getAbsolutePath();
                System.out.println("paht;" + docUrl);
                Thread.sleep(2000);

                btnUploadFileElement.sendKeys(docUrl);

                Utilities.captureScreenShot(_driver);
            }

            Utilities.captureScreenShot(_driver);

            btnSubmitResponseElement.click();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}