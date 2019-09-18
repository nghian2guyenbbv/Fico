package vn.com.tpf.microservices.services;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.tpf.microservices.driver.SeleniumGridDriver;
import vn.com.tpf.microservices.models.Automation.LoginDTO;
import vn.com.tpf.microservices.models.QuickLead.Application;
import vn.com.tpf.microservices.models.QuickLead.QuickLead;
import vn.com.tpf.microservices.services.Automation.*;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import static org.hamcrest.Matchers.is;
import static org.awaitility.Awaitility.await;

@Service
public class AutomationHandlerService {

    @Autowired
    private RabbitMQService rabbitMQService;

    private LoginDTO pollAccountFromQueue(Queue<LoginDTO> accounts) throws Exception {
        LoginDTO accountDTO = null;
        while (Objects.isNull(accountDTO)) {
            System.out.println("Wait to get account...");

            accountDTO = accounts.poll();
            if (!Objects.isNull(accountDTO)) {
                System.out.println("Get it:" + accountDTO.toString());
                System.out.println("Exist:" + accounts.size());
            } else
                Thread.sleep(Constant.WAIT_ACCOUNT_TIMEOUT);
        }

        return accountDTO;
    }

    private void pushAccountToQueue(Queue<LoginDTO> accounts, LoginDTO accountDTO) {
        synchronized (accounts) {
            if (!Objects.isNull(accountDTO)) {
                System.out.println("push to queue... : " + accountDTO.toString());
                accounts.add(accountDTO);
            }
        }
    }

    public void logout(WebDriver driver) {
        System.out.println("Logout");
        LogoutPage logoutPage = new LogoutPage(driver);
        logoutPage.logout();
    }

    public void executor(Queue<LoginDTO> accounts, String browser, Map<String, Object> mapValue, String function) {
        WebDriver driver = null;
        LoginDTO accountDTO = null;
        try {
            accountDTO = pollAccountFromQueue(accounts);
            SeleniumGridDriver setupTestDriver = new SeleniumGridDriver(null, browser, Constant.FINNONE_LOGIN_URL, null);
            driver = setupTestDriver.getDriver();

            switch (function){
                case "runAutomation_DE":
                    //runAutomation_DE(driver, mapValue, accountDTO);
                    break;
                case "updateAutomation_DE":
                    //updateAutomation_DE(driver,mapValue,accountDTO,"APPL00089328");
                    break;
                case "quickLead":
                    runAutomation_QuickLead(driver, mapValue, accountDTO);
                    break;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            Utilities.captureScreenShot(driver);
        } finally {
            //logout(driver);
            pushAccountToQueue(accounts, accountDTO);
            if (driver != null) {
                driver.close();
                driver.quit();
            }
        }
    }


    //QUICK LEAD
    public void runAutomation_QuickLead(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO) throws Exception {
        String log ="";
        String vinId = "";
        String appId = "";
        String stage= "";
        try {
            stage="INIT DATA";
            //*************************** GET DATA *********************//
            Application application = (Application) mapValue.get("ApplicationDTO");
            QuickLead quickLead=application.getQuickLead();

            //*************************** END GET DATA *********************//

            System.out.println(stage + ": DONE" );
            stage="LOGIN FINONE";
            HashMap<String, String> dataControl = new HashMap<>();
            LoginPage loginPage = new LoginPage(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
            loginPage.clickLogin();

            Utilities.captureScreenShot(driver);

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            log="HOME PAGE";
            HomePage homePage = new HomePage(driver);

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            stage="QUICK LEAD";
            // ========== QUICK LEAD =================
            homePage.menuClick();
            Utilities.captureScreenShot(driver);
            homePage.leadQuickClick();

            Utilities.captureScreenShot(driver);
            await("Quick Lead Entry timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Quick Lead Entry"));

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);
            stage="QUICK LEAD (DOCUMENT UPLOAD & APPLICATION CREATION)";
            QuickLeadPage quickLeadPage=new QuickLeadPage(driver);
            quickLeadPage.setData(quickLead);

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);
            stage="LEADS GRID";
            // ========== LEAD PAGE =================
            LeadsPage leadsPage=new LeadsPage(driver);
            await("Quick Lead Entry timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Lead Grid"));

            await("notifyTextElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(()->leadsPage.getNotifyTextElement().isEnabled() && leadsPage.getNotifyTextElement().isDisplayed());

            String notify=leadsPage.getNotifyTextElement().getText();
            String leadApp="";
            if(notify.contains("LEAD")){
                leadApp=notify.substring(notify.indexOf("LEAD"),notify.length());
            }

            System.out.println("LEAD APP: =>" + leadApp);
            leadsPage.setData(leadApp);

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);
            stage="LEAD STAGE";
            // ========== LEAD STAGE =================
            LeadDetailPage leadDetailPage=new LeadDetailPage(driver);
            await("contentElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadDetailPage.getContentElement().isDisplayed());

            leadDetailPage.setData(quickLead,leadApp);

            Utilities.captureScreenShot(driver);

            await("Lead Page timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Lead Grid"));

            leadsPage.getSpanAllNotifyElement().click();
            await("getDivAllNotifyElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(()->leadsPage.getDivAllNotifyElement().isEnabled() && leadsPage.getDivAllNotifyElement().isDisplayed());

            await("getBtnAllNotifyElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(()->leadsPage.getBtnAllNotifyElement().isEnabled() && leadsPage.getBtnAllNotifyElement().isDisplayed());
            leadsPage.getBtnAllNotifyElement().click();

            Utilities.captureScreenShot(driver);
            await("getBtnAllNotifyElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(()->leadsPage.getNotifyTextSuccessElement().size()>0);

            String leadAppID="";
            for (WebElement e: leadsPage.getNotifyTextSuccessElement())
            {
                System.out.println(e.getText());
                if(e.getText().contains("APPL")){
                    leadAppID=e.getText().substring(e.getText().indexOf("APPL"));
                }
            }
            System.out.println("APPID: => " + leadAppID);

            Utilities.captureScreenShot(driver);

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            application.setApplicationId(leadAppID);

            rabbitMQService.send("tpf-service-dataentry",
                    Map.of("func", "updateAutomation", "token",
                            String.format("Bearer %s", rabbitMQService.getToken().path("access_token").asText()),"body", application));

            homePage.getMenuApplicationElement().click();
            homePage.getApplicationElement().click();
            logout(driver);

        } catch (Exception e) {
            System.out.println(stage + "=> " + e.getMessage());
            if (e.getMessage().contains("Work flow failed!!!")) {
                log="END OF LEAD DETAIL";

                await("Get error fail!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> driver.findElements(By.id("error-message")).size() > 0);

                if (driver.findElements(By.id("error-message")) != null && driver.findElements(By.id("error-message")).size() > 0) {
                    String error = "Error: ";
                    for (WebElement we : driver.findElements(By.id("error-message"))) {
                        error += " - " + we.getText();
                    }
                    System.out.println(error);
                }
            }
            e.printStackTrace();
            Utilities.captureScreenShot(driver);

        } finally {
            System.out.println(stage);
            logout(driver);
        }
    }

}
