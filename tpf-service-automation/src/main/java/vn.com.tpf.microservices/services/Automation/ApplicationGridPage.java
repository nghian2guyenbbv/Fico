package vn.com.tpf.microservices.services.Automation;


import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.utilities.Constant;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Getter
public class ApplicationGridPage {
    private WebDriver _driver;

    @FindBy(how = How.ID, using = "mainChildTabs")
    @CacheLookup
    private WebElement mainChildTabsElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id,'mainChildTabs')]//a[contains(text(),'Pool')]")
    @CacheLookup
    private WebElement poolElement;

    @FindBy(how = How.ID, using = "LoanApplication_Pool_wrapper")
    @CacheLookup
    private WebElement poolWrapperElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'LoanApplication_Pool_filter')]//input")
    @CacheLookup
    private WebElement searchPoolQueryElement;

    @FindBy(how = How.XPATH, using = "//table[@id='LoanApplication_Pool']//tbody//tr//td")
    @CacheLookup
    private List<WebElement> tdLeadPoolElement;

    @FindBy(how = How.XPATH, using = "//table[@id='LoanApplication_Pool']//tbody//tr//td[contains(@class,'tbl-left')]//a")
    @CacheLookup
    private WebElement aLeadPoolElement;

    @FindBy(how = How.ID, using = "AssignToMe")
    @CacheLookup
    private WebElement assignToMeElement;

    @FindBy(how = How.ID, using = "LoanApplication_Assigned_wrapper")
    @CacheLookup
    private WebElement assignWrapperElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'LoanApplication_Assigned_filter')]//input")
    @CacheLookup
    private WebElement searchAssignQueryElement;

    @FindBy(how = How.XPATH, using = "//table[@id='LoanApplication_Assigned']//tbody//tr//td")
    @CacheLookup
    private List<WebElement> tdLeadAssignElement;

    @FindBy(how = How.XPATH, using = "//table[@id='LoanApplication_Assigned']//tbody//tr//td[contains(@class,'tbl-left')]//a")
    @CacheLookup
    private WebElement aLeadAssignElement;

    @FindBy(how = How.XPATH, using = "//*[@id='collectedDocs']")
    @CacheLookup
    private WebElement collectedDocs;


    public ApplicationGridPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    public void setData(String appId) {
        //Assign to me from pool
        //click tab pool
        poolElement.click();

        await("poolWrapperElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> poolWrapperElement.isDisplayed());

        searchPoolQueryElement.sendKeys(appId);

        await("tdLeadElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tdLeadPoolElement.size() > 2);

        WebElement webElement=_driver.findElement(new By.ByXPath("//table[@id='LoanApplication_Pool']//tbody//tr//td[contains(@class,'tbl-left')]//a[contains(text(),'" + appId +"')]"));

        await("aLeadElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> webElement.isDisplayed());


        await("assignToMeElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> assignToMeElement.isDisplayed());

        assignToMeElement.click();
        // end assign

        await("assignWrapperElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> assignWrapperElement.isDisplayed());

        searchAssignQueryElement.sendKeys(appId);

        await("tdLeadAssignElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tdLeadAssignElement.size() > 2);

        WebElement webAssignElement=_driver.findElement(new By.ByXPath("//table[@id='LoanApplication_Assigned']//tbody//tr//td[contains(@class,'tbl-left')]//a[contains(text(),'" + appId +"')]"));

        await("webAssignElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> webAssignElement.isDisplayed());

        webAssignElement.click();

    }

    public void updateData(String appId) {

        await("assignWrapperElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> assignWrapperElement.isDisplayed());

        searchAssignQueryElement.sendKeys(appId);

        await("tdLeadAssignElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tdLeadAssignElement.size() > 2);

        WebElement webAssignElement=_driver.findElement(new By.ByXPath("//table[@id='LoanApplication_Assigned']//tbody//tr//td[contains(@class,'tbl-left')]//a[contains(text(),'" + appId +"')]"));

        await("webAssignElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> webAssignElement.isDisplayed());

        webAssignElement.click();

    }
}
