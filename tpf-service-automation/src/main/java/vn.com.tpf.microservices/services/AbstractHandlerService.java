package vn.com.tpf.microservices.services;

import org.awaitility.Awaitility;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import vn.com.tpf.microservices.services.Automation.HomePageNeo;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.util.concurrent.TimeUnit;

public class AbstractHandlerService {
    public void searchAppInapplicationTab(WebDriver driver, String leadAppID, String stage) throws InterruptedException{
        HomePageNeo homePage = new HomePageNeo(driver);
        homePage.menuClick();
        homePage.applicationClick();
        Thread.sleep(Constant.WAIT_ASSIGN_TIMEOUT);
        homePage.getAssignedApp().click();
        Awaitility.await("branchOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> homePage.getApplicationNumber().isDisplayed());
        homePage.getApplicationNumber().sendKeys(leadAppID);
        homePage.getApplicationNumber().sendKeys(Keys.ENTER);
        Thread.sleep(Constant.WAIT_ACCOUNT_GET_NULL);
        homePage.getAppQueryElement().click();
        System.out.println(stage + ": DONE");
        Utilities.captureSreenShotWithStage(stage,"assingeAppToUser",driver);

    }
    public void searchAppInResponseQuery(WebDriver driver, String leadAppID, String stage) throws InterruptedException{
        HomePageNeo homePage = new HomePageNeo(driver);
        homePage.menuClick();
        homePage.applicationClick();
        Thread.sleep(Constant.WAIT_ASSIGN_TIMEOUT);
        homePage.getSearchQueryElement().click();

    }
}
