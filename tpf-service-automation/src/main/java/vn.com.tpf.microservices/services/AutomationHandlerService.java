package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import vn.com.tpf.microservices.driver.SeleniumGridDriver;
import vn.com.tpf.microservices.models.AutoAllocation.AutoAssignAllocationDTO;
import vn.com.tpf.microservices.models.AutoAllocation.AutoReassignUserDTO;
import vn.com.tpf.microservices.models.AutoAssign.AutoAssignDTO;
import vn.com.tpf.microservices.models.AutoCRM.*;
import vn.com.tpf.microservices.models.AutoField.*;
import vn.com.tpf.microservices.models.Automation.*;
import vn.com.tpf.microservices.models.DEReturn.DEResponseQueryDTO;
import vn.com.tpf.microservices.models.DEReturn.DESaleQueueDTO;
import vn.com.tpf.microservices.models.QuickLead.Application;
import vn.com.tpf.microservices.models.QuickLead.QuickLead;
import vn.com.tpf.microservices.models.ResponseAutomationModel;
import vn.com.tpf.microservices.services.Automation.*;
import vn.com.tpf.microservices.services.Automation.autoAllocation.AutoAllocationReassignPage;
import vn.com.tpf.microservices.services.Automation.autoAllocation.AutoAssignAllocationPage;
import vn.com.tpf.microservices.services.Automation.autoCRM.*;
import vn.com.tpf.microservices.services.Automation.autoField.*;
import vn.com.tpf.microservices.services.Automation.deReturn.AssignManagerSaleQueuePage;
import vn.com.tpf.microservices.services.Automation.deReturn.DE_ReturnRaiseQueryPage;
import vn.com.tpf.microservices.services.Automation.deReturn.DE_ReturnSaleQueuePage;
import vn.com.tpf.microservices.services.Automation.lending.*;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.is;

@Service
public class AutomationHandlerService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RabbitMQService rabbitMQService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Value("${spring.url.finone}")
    private String fin1URL;

    @Value("${spring.url.seleHost}")
    private String seleHost;

    @Value("${spring.url.selePort}")
    private String selePort;


    @Value("${spring.url.downloadFile}")
    private String downdloadFileURL;

    @Value("${spring.url.rabbitIdRes}")
    private String rabbitIdRes;

    private LoginDTO pollAccountFromQueue_OLD(Queue<LoginDTO> accounts, String project) throws Exception {
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

    private LoginDTO pollAccountFromQueue(Queue<LoginDTO> accounts, String project) throws Exception {
        LoginDTO accountDTO = null;
//        while (Objects.isNull(accountDTO)) {
//            System.out.println("Wait to get account...");
//
//            accountDTO = accounts.poll();
//            if (!Objects.isNull(accountDTO)) {
//                System.out.println("Get it:" + accountDTO.toString());
//                System.out.println("Exist:" + accounts.size());
//            } else
//                Thread.sleep(Constant.WAIT_ACCOUNT_TIMEOUT);
//        }


        while (Objects.isNull(accountDTO)) {
            System.out.println("Wait to get account...");

            //get list account finone available
            Query query = new Query();
            query.addCriteria(Criteria.where("active").is(0).and("project").is(project));
            AccountFinOneDTO accountFinOneDTO = mongoTemplate.findOne(query, AccountFinOneDTO.class);
            if (!Objects.isNull(accountFinOneDTO)) {
                accountDTO = new LoginDTO().builder().userName(accountFinOneDTO.getUsername()).password(accountFinOneDTO.getPassword()).build();

                Query queryUpdate = new Query();
                queryUpdate.addCriteria(Criteria.where("active").is(0).and("username").is(accountFinOneDTO.getUsername()).and("project").is(project));
                Update update = new Update();
                update.set("active", 1);
                AccountFinOneDTO resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, AccountFinOneDTO.class);

                if (resultUpdate == null) {
                    Thread.sleep(Constant.WAIT_ACCOUNT_GET_NULL);
                    accountDTO = null;
                } else {
                    System.out.println("Get it:" + accountDTO.toString());
                    System.out.println("Exist:" + accounts.size());
                }
            } else
                Thread.sleep(Constant.WAIT_ACCOUNT_TIMEOUT);
        }

        return accountDTO;
    }

    private LoginDTO retry_pollAccountFromQueue(String project) throws Exception {
        LoginDTO accountDTO = null;

        while (Objects.isNull(accountDTO)) {
            System.out.println("Wait to get account...");

            //get list account finone available
            Query query = new Query();
            query.addCriteria(Criteria.where("active").is(0).and("project").is(project));
            AccountFinOneDTO accountFinOneDTO = mongoTemplate.findOne(query, AccountFinOneDTO.class);
            if (!Objects.isNull(accountFinOneDTO)) {
                accountDTO = new LoginDTO().builder().userName(accountFinOneDTO.getUsername()).password(accountFinOneDTO.getPassword()).build();

                Query queryUpdate = new Query();
                queryUpdate.addCriteria(Criteria.where("active").is(0).and("username").is(accountFinOneDTO.getUsername()).and("project").is(project));
                Update update = new Update();
                update.set("active", 1);
                AccountFinOneDTO resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, AccountFinOneDTO.class);

                if (resultUpdate == null) {
                    Thread.sleep(Constant.WAIT_ACCOUNT_GET_NULL);
                    accountDTO = null;
                }
            } else
                Thread.sleep(Constant.WAIT_ACCOUNT_TIMEOUT);
        }


        return accountDTO;
    }


    private void pushAccountToQueue_OLD(Queue<LoginDTO> accounts, LoginDTO accountDTO) {
        synchronized (accounts) {
            if (!Objects.isNull(accountDTO)) {
                System.out.println("push to queue... : " + accountDTO.toString());
                accounts.add(accountDTO);
            }
        }
    }

    private void pushAccountToQueue(LoginDTO accountDTO, String project) {
        //synchronized (accounts) {
        if (!Objects.isNull(accountDTO)) {
//                System.out.println("push to queue... : " + accountDTO.toString());
//                accounts.add(accountDTO);
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Query queryUpdate = new Query();
            queryUpdate.addCriteria(Criteria.where("username").is(accountDTO.getUserName()).and("project").is(project));
            Update update = new Update();
            update.set("active", 0);
            AccountFinOneDTO resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, AccountFinOneDTO.class);
            System.out.println("Update it:" + accountDTO.toString());
        }
        // }
    }

    public void logout(WebDriver driver,String accountAuto) {
        try {

            System.out.println("Logout");
            LogoutPage logoutPage = new LogoutPage(driver);
            logoutPage.logout();
            log.info("Logout: Done => " + accountAuto);
        } catch (Exception e) {
            System.out.println("LOGOUT: =>" + accountAuto +" - " + e.toString());
        }
    }

    public void executor(Queue<LoginDTO> accounts, String browser, Map<String, Object> mapValue, String function, String project) {
        WebDriver driver = null;
        LoginDTO accountDTO = null;
        try {

            SeleniumGridDriver setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
            driver = setupTestDriver.getDriver();

            switch (function) {
                case "runAutomation_UpdateInfo":
                    accountDTO = pollAccountFromQueue(accounts, project);
                    runAutomation_UpdateInfo(driver, mapValue, accountDTO);
                    break;
                case "runAutomation_UpdateAppError":
                    accountDTO = pollAccountFromQueue(accounts, project);
                    runAutomation_UpdateAppError(driver, mapValue, accountDTO);
                    break;
                case "quickLead":
                    accountDTO = pollAccountFromQueue(accounts, project);
                    runAutomation_QuickLead(driver, mapValue, accountDTO);
                    break;
                case "momoCreateApp":
                    accountDTO = pollAccountFromQueue(accounts, project);
                    runAutomation_momoCreateApp(driver, mapValue, accountDTO);
                    break;
                case "fptCreateApp":
                    accountDTO = pollAccountFromQueue(accounts, project);
                    runAutomation_fptCreateApp(driver, mapValue, accountDTO);
                    break;
                case "runAutomationDE_AutoAssign":
                    //chay get account trong function
                    runAutomationDE_autoAssign(driver, mapValue, project, browser);
                    break;
                case "runAutomationDE_ResponseQuery":
                    accountDTO = return_pollAccountFromQueue(accounts, project.toUpperCase(), mapValue);
                    runAutomationDE_responseQuery(driver, mapValue, accountDTO);
                    break;
                case "runAutomationDE_SaleQueue":
                    accountDTO = pollAccountFromQueue(accounts, project.toUpperCase());
                    runAutomationDE_saleQueue(driver, mapValue, accountDTO);
                    break;
                case "SN_quickLead":
                    accountDTO = pollAccountFromQueue(accounts, project);
                    SN_runAutomation_QuickLead(driver, mapValue, accountDTO);
                    break;
                case "runAutomation_Waive_Field":
                    String funcWaiveField = "Waive_Field";
//                    runAutomation_Field_Old(driver, mapValue, project, browser, funcWaiveField);
                    runAutomation_Field(driver, mapValue, project, browser, funcWaiveField);
                    break;
                case "runAutomation_Submit_Field":
                    String funcSubmitField = "Submit_Field";
//                    runAutomation_Field_Old(driver, mapValue, project, browser, funcSubmitField);
                    runAutomation_Field(driver, mapValue, project, browser, funcSubmitField);
                    break;
                case "MOBILITY_quickLead":
                    accountDTO = pollAccountFromQueue(accounts, project);
                    MOBILITY_runAutomation_QuickLead(driver, mapValue, accountDTO);
                    break;
                case "runAutomation_Existing_Customer":
                    accountDTO = pollAccountFromQueue(accounts, project.toUpperCase());
                    runAutomation_Existing_Customer(driver, mapValue, accountDTO);
                    break;
                case "CRM_quickLead_With_CustID":
                    accountDTO = pollAccountFromQueue(accounts, project.toUpperCase());
                    CRM_runAutomation_QuickLead_With_CustID(driver, mapValue, accountDTO);
                    break;
                case "CRM_quickLead":
                    accountDTO = pollAccountFromQueue(accounts, project.toUpperCase());
                    CRM_runAutomation_QuickLead(driver, mapValue, accountDTO, project);
                    break;
                case "runAutomation_ResponseQuery":
                    accountDTO = return_pollAccountFromQueue(accounts, project, mapValue);
                    runAutomationDE_responseQuery(driver, mapValue, accountDTO);
                    break;
                case "runAutomation_SaleQueue":
                    accountDTO = pollAccountFromQueue(accounts, project);
                    runAutomationDE_saleQueue(driver, mapValue, accountDTO);
                    break;
                case "quickLead_Assign_Pool":
                    accountDTO = pollAccountFromQueue(accounts, project);
                    runAutomation_QuickLead_Assign_Pool(driver, mapValue, accountDTO);
                    break;
                case "runAutomation_Sale_Queue_With_FullInfo":
                    accountDTO = pollAccountFromQueue(accounts, project.toUpperCase());
                    runAutomation_Sale_Queue_With_FullInfo(driver, mapValue, accountDTO);
                    break;
                case "runAutomation_Auto_Allocation":
                    runAutomation_autoAllocation(driver, mapValue, project, browser);
                    break;
            }

        } catch (Exception e) {
            System.out.println("executor:" + e.toString());
            Utilities.captureScreenShot(driver);
        } finally {
            //logout(driver);
            if (project.equals("MOMO")) {
                pushAccountToQueue(accountDTO, project);
            } else {
                pushAccountToQueue(accountDTO, project);
            }

            if (driver != null) {
                driver.close();
                driver.quit();
            }
        }
    }

    //------------------------ DATA ENTRY -------------------------------------------
    public void runAutomation_QuickLead(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO) throws Exception {
        Instant start = Instant.now();
        String appId = "";
        String stage = "";
        Application application = Application.builder().build();
        log.info("{}", application);
        try {
            stage = "INIT DATA";
            //*************************** GET DATA *********************//
            application = (Application) mapValue.get("ApplicationDTO");
            application.setAutomationAcc(accountDTO.getUserName());
            QuickLead quickLead = application.getQuickLead();
            mongoTemplate.save(application);


            //*************************** END GET DATA *********************//
            Actions actions = new Actions(driver);
            System.out.println(stage + ": DONE");
            stage = "LOGIN FINONE";
            HashMap<String, String> dataControl = new HashMap<>();
            LoginPage loginPage = new LoginPage(driver);
            Utilities.captureScreenShot(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
            loginPage.clickLogin();
            Utilities.captureScreenShot(driver);
            //actions.moveToElement(loginPage.getBtnElement()).click().build().perform();

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));


            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "HOME PAGE";
            HomePage homePage = new HomePage(driver);


            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "QUICK LEAD";
            // ========== QUICK LEAD =================
            homePage.menuClick();
            homePage.leadQuickClick();

            await("Quick Lead Entry timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Quick Lead Entry"));

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "QUICK LEAD (DOCUMENT UPLOAD & APPLICATION CREATION)";
            QuickLeadPage quickLeadPage = new QuickLeadPage(driver);
            quickLeadPage.setData(quickLead);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "LEADS GRID";
            // ========== LEAD PAGE =================
            LeadsPage leadsPage = new LeadsPage(driver);
            await("Quick Lead Entry timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Lead Grid"));

            await("notifyTextElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadsPage.getNotifyTextElement().isEnabled() && leadsPage.getNotifyTextElement().isDisplayed());

            String notify = leadsPage.getNotifyTextElement().getText();
            System.out.println("notify: =>" + notify);
            String leadApp = "";
            if (notify.contains("LEAD")) {
                leadApp = notify.substring(notify.indexOf("LEAD"), notify.length());
            }
            
            System.out.println("LEAD APP: =>" + leadApp);
            leadsPage.setData(leadApp);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "LEAD STAGE";
            // ========== LEAD STAGE =================
            LeadDetailPage leadDetailPage = new LeadDetailPage(driver);
            await("contentElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadDetailPage.getContentElement().isDisplayed());

            leadDetailPage.setData(quickLead, leadApp, downdloadFileURL);

            Utilities.captureScreenShot(driver);

            await("Lead Page timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Lead Grid"));

            leadsPage.getSpanAllNotifyElement().click();
            await("getDivAllNotifyElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadsPage.getDivAllNotifyElement().isEnabled() && leadsPage.getDivAllNotifyElement().isDisplayed());

            await("getBtnAllNotifyElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadsPage.getBtnAllNotifyElement().isEnabled() && leadsPage.getBtnAllNotifyElement().isDisplayed());
            leadsPage.getBtnAllNotifyElement().click();

            Utilities.captureScreenShot(driver);
            await("getBtnAllNotifyElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadsPage.getNotifyTextSuccessElement().size() > 0);

            String leadAppID = "";
//            for (WebElement e: leadsPage.getNotifyTextSuccessElement())
//            {
//                System.out.println(e.getText());
//                if(e.getText().contains("APPL")){
//                    leadAppID=e.getText().substring(e.getText().indexOf("APPL"),e.getText().indexOf("APPL") + 12);
//                }
//            }
//            System.out.println("APPID: => " + leadAppID);
//
//            Utilities.captureScreenShot(driver);
//            System.out.println(stage + ": DONE" );

            //update thêm phần assign về acc tạo app để tranh rơi vào pool
            stage = "APPLICATION MANAGER";
            // ========== APPLICATION MANAGER =================
            homePage.getMenuApplicationElement().click();
            homePage.getApplicationManagerElement().click();
            await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Manager"));

            DE_ApplicationManagerPage de_applicationManagerPage = new DE_ApplicationManagerPage(driver);

            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> de_applicationManagerPage.getApplicationManagerFormElement().isDisplayed());

            //get appID moi o day
            leadAppID = de_applicationManagerPage.getAppID(leadApp);

            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> de_applicationManagerPage.getBackBtnElement().isDisplayed());

            de_applicationManagerPage.getBackBtnElement().click();

            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> de_applicationManagerPage.getApplicationManagerFormElement().isDisplayed());

            de_applicationManagerPage.setData(leadAppID, accountDTO.getUserName());
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            //-------------------- END ---------------------------

            application.setApplicationId(leadAppID);
            application.setLeadApp(leadApp);

            //UPDATE STATUS
            application.setStatus("OK");
            application.setDescription("Thanh cong");

//            homePage.getMenuApplicationElement().click();
//            homePage.getApplicationElement().click();
            Utilities.captureScreenShot(driver);
            //logout(driver);

        } catch (Exception e) {
            //UPDATE STATUS
            application.setStatus("ERROR");
            application.setStage(stage);
            application.setDescription(e.getMessage());

            System.out.println(stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);

            if (e.getMessage().contains("Work flow failed!!!")) {
                stage = "END OF LEAD DETAIL";

                await("Get error fail!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> driver.findElements(By.id("error-message")).size() > 0);

                if (driver.findElements(By.id("error-message")) != null && driver.findElements(By.id("error-message")).size() > 0) {
                    String error = "Error: ";
                    for (WebElement we : driver.findElements(By.id("error-message"))) {
                        error += " - " + we.getText();
                    }
                    System.out.println(stage + "=>" + error);
                }
            }
        } finally {
            if (application.getApplicationId() == null || application.getApplicationId().isEmpty() || application.getApplicationId().indexOf("LEAD") > 0 || application.getApplicationId().indexOf("APPL") < 0) {
                application.setApplicationId("UNKNOW");
            }

            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            try {
                updateStatusRabbit(application, "updateAutomation");
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            logout(driver,accountDTO.getUserName());
        }
    }

    public void runAutomation_UpdateInfo(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO) throws Exception {
        String stage = "";
        Instant start = Instant.now();
        Application application = Application.builder().build();
        try {
            stage = "INIT DATA";
            //*************************** GET DATA *********************//
            application = (Application) mapValue.get("ApplicationDTO");
            ApplicationInfoDTO applicationInfoDTO = (ApplicationInfoDTO) mapValue.get("ApplicationInfoDTO");
            LoanDetailsDTO loanDetailsDTO = (LoanDetailsDTO) mapValue.get("LoanDetailsDTO");
            LoanDetailsVapDTO loanDetailsVapDTO = (LoanDetailsVapDTO) mapValue.get("LoanDetailsVapDTO");
            List<ReferenceDTO> referenceDTO = (List<ReferenceDTO>) mapValue.get("ReferenceDTO");
            List<DocumentDTO> documentDTOS = (List<DocumentDTO>) mapValue.get("DocumentDTO");
            MiscFptDTO miscFptDTO = (MiscFptDTO) mapValue.get("MiscFptDTO");
            MiscFrmAppDtlDTO miscFrmAppDtlDTO = (MiscFrmAppDtlDTO) mapValue.get("MiscFrmAppDtlDTO");
            MiscVinIdDTO miscVinIdDTO = (MiscVinIdDTO) mapValue.get("MiscVinIdDTO");

            application.setAutomationAcc(accountDTO.getUserName());

            //vinId = loanDetailsDTO.getApplicationNumber();
            //*************************** END GET DATA *********************//

            System.out.println(stage + ": DONE");
            stage = "LOGIN FINONE";
            HashMap<String, String> dataControl = new HashMap<>();
            LoginPage loginPage = new LoginPage(driver);
            Utilities.captureScreenShot(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
            loginPage.clickLogin();
            Utilities.captureScreenShot(driver);
            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "HOME PAGE";
            HomePage homePage = new HomePage(driver);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ========== APPLICATIONS =================
            String leadAppID = application.getApplicationId();
            homePage.getMenuApplicationElement().click();

            // update lai flow assign app moi

//            homePage.getApplicationElement().click();
//
//            await("Application Grid timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(driver::getTitle, is("Application Grid"));
//
//            ApplicationGridPage applicationGridPage=new ApplicationGridPage(driver);
//            applicationGridPage.setData(leadAppID);
//            Utilities.captureScreenShot(driver);
//
//            System.out.println(stage + ": DONE" );
//            Utilities.captureScreenShot(driver);
            stage = "APPLICATION MANAGER";
            // ========== APPLICATION MANAGER =================
            homePage.getApplicationManagerElement().click();
            await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Manager"));

            DE_ApplicationManagerPage de_applicationManagerPage = new DE_ApplicationManagerPage(driver);

            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> de_applicationManagerPage.getApplicationManagerFormElement().isDisplayed());
            de_applicationManagerPage.setData(leadAppID, accountDTO.getUserName());
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "APPLICATION GRID";
            // ========== APPLICATION GRID =================
            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> de_applicationManagerPage.getMenuApplicationElement().isDisplayed());

            de_applicationManagerPage.getMenuApplicationElement().click();
            Utilities.captureScreenShot(driver);
            de_applicationManagerPage.getApplicationElement().click();
            Utilities.captureScreenShot(driver);
            await("Application Grid timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Grid"));
            Utilities.captureScreenShot(driver);
            ApplicationGridPage applicationGridPage = new ApplicationGridPage(driver);
            applicationGridPage.updateData(leadAppID);
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "LEAD DETAILS (DATA ENTRY)";
            // ========== LEAD DETAILS (DATA ENTRY) =================
            LeadDetailDEPage leadDetailDEPage = new LeadDetailDEPage(driver);
            leadDetailDEPage.setData(leadAppID);
            Utilities.captureScreenShot(driver);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "PERSONAL INFORMATION";
            // ========== PERSONAL INFORMATION =================
            DE_ApplicationInfoPage appInfoPage = new DE_ApplicationInfoPage(driver);
            DE_ApplicationInfoPersonalTab personalTab = appInfoPage.getApplicationInfoPersonalTab();

            await("getPersonalCustomerDetailsElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> personalTab.getPersonalCustomerDetailsElement().isDisplayed());

            personalTab.setValue(applicationInfoDTO);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "EMPLOYMENT DETAILS";
            // ========== EMPLOYMENT DETAILS =================
            await("Load employment details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> appInfoPage.getEmploymentDetailsTabElement().getAttribute("class").contains("active"));

            DE_ApplicationInfoEmploymentDetailsTab employmentDetailsTab = appInfoPage.getApplicationInfoEmploymentDetailsTab();
            await("Span Application Id not enabled Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> employmentDetailsTab.getApplicationId().isEnabled());

            employmentDetailsTab.setData(applicationInfoDTO.getEmploymentDetails());
            employmentDetailsTab.getDoneBtnElement().click();
            await("Employment Status loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> employmentDetailsTab.getTableAfterDoneElement().isDisplayed());
            employmentDetailsTab.setExperienceInIndustry(applicationInfoDTO.getEmploymentDetails());
            employmentDetailsTab.setMajorOccupation(applicationInfoDTO.getEmploymentDetails());
            employmentDetailsTab.getSaveAndNextBtnElement().click();
            Utilities.captureScreenShot(driver);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "EMPLOYMENT DETAILS - FINANCIAL";
            // ==========FINANCIAL DETAILS =================
            if (applicationInfoDTO.getIncomeDetails() != null && applicationInfoDTO.getIncomeDetails().size() > 0) {
                DE_ApplicationInfoFinancialDetailsTab financialDetailsTab = appInfoPage.getApplicationInfoFinancialDetailsTab();

                financialDetailsTab.openIncomeDetailSection();
                await("Load financial details - income details Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> financialDetailsTab.getIncomeDetailDivElement().isDisplayed());
                financialDetailsTab.setIncomeDetailsData(applicationInfoDTO.getIncomeDetails());
                financialDetailsTab.saveAndNext();
            }

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ==========LOAN DETAILS=================
            stage = "LOAN DETAIL PAGE - SOURCING DETAIL TAB";
            DE_LoanDetailsPage loanDetailsPage = new DE_LoanDetailsPage(driver);
            Utilities.captureScreenShot(driver);
            loanDetailsPage.getTabLoanDetailsElement().click();

            DE_LoanDetailsSourcingDetailsTab loanDetailsSourcingDetailsTab = new DE_LoanDetailsSourcingDetailsTab(driver);
            await("Load loan details - sourcing details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getTabSourcingDetailsElement().getAttribute("class").contains("active"));
            await("Load loan details - sourcing details container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getSourcingDetailsDivContainerElement().isDisplayed());
            loanDetailsSourcingDetailsTab.setData(loanDetailsDTO);
            Utilities.captureScreenShot(driver);
            loanDetailsSourcingDetailsTab.getBtnSaveAndNextElement().click();

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ==========VAP DETAILS=======================
            if (loanDetailsVapDTO != null && loanDetailsVapDTO.getVapProduct() != null && !loanDetailsVapDTO.getVapProduct().equals("")) {
                stage = "LOAN DETAIL PAGE - VAP DETAIL TAB";
                Utilities.captureScreenShot(driver);
                DE_LoanDetailsVapDetailsTab loanDetailsVapDetailsTab = new DE_LoanDetailsVapDetailsTab(driver);

                await("Load loan details - sourcing details container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> loanDetailsVapDetailsTab.getVapDetailsDivContainerElement().isDisplayed());
                loanDetailsVapDetailsTab.setData(loanDetailsVapDTO);
                Utilities.captureScreenShot(driver);
                loanDetailsVapDetailsTab.getBtnSaveAndNextElement().click();

                System.out.println("LOAN DETAILS - VAP: DONE");

            }

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "DOCUMENTS";
            // ==========DOCUMENTS=================
            if (documentDTOS.size() > 0) {
                DE_DocumentsPage documentsPage = new DE_DocumentsPage(driver);
                await("Load document tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> documentsPage.getTabDocumentsElement().isDisplayed() && documentsPage.getTabDocumentsElement().isEnabled());
                documentsPage.getTabDocumentsElement().click();
                await("Load document container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> documentsPage.getDocumentsContainerElement().isDisplayed());
                documentsPage.getBtnGetDocumentElement().click();
                await("Load document table Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> documentsPage.getLendingTrElement().size() > 0);

                documentsPage.updateData(documentDTOS, downdloadFileURL);
                Utilities.captureScreenShot(driver);
                documentsPage.getBtnSubmitElement().click();
            }

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "REFERENCES";
            // ==========REFERENCES=================
            stage = "REFERENCES PAGE";
            DE_ReferencesPage referencesPage = new DE_ReferencesPage(driver);
            await("Load references tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> referencesPage.getTabReferencesElement().isDisplayed() && referencesPage.getTabReferencesElement().isEnabled());
            referencesPage.getTabReferencesElement().click();
            referencesPage.setData(referenceDTO);
            referencesPage.getSaveBtnElement().click();

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ==========MISC FRM APP DTL=================
            stage = "MISC FRM APPDTL PAGE";
            DE_MiscFrmAppDtlPage miscFrmAppDtlPage = new DE_MiscFrmAppDtlPage(driver);
            miscFrmAppDtlPage.getTabMiscFrmAppDtlElementByName().click();
            miscFrmAppDtlPage.setData(miscFrmAppDtlDTO);
            Utilities.captureScreenShot(driver);
            await("getBtnSaveElement end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFrmAppDtlPage._getBtnSaveElement().isEnabled());
            miscFrmAppDtlPage._getBtnSaveElement().click();
            Utilities.captureScreenShot(driver);
            await("getBtnMoveToNextStageElement end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFrmAppDtlPage.getBtnMoveToNextStageElement().isEnabled());
            Utilities.captureScreenShot(driver);

            //tam thoi cho sleep de an notification moi click dc button movetonextstage
            Thread.sleep(15000);

            miscFrmAppDtlPage.getBtnMoveToNextStageElement().click();
            Utilities.captureScreenShot(driver);
            System.out.println("MISC FRM APP DTL: DONE");

            await("Work flow failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Grid"));

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "COMPLETE";
            System.out.println("AUTO OK: user =>" + accountDTO.getUserName());

            //UPDATE STATUS
            application.setStatus("OK");
            application.setDescription("Thanh cong");

            //logout(driver);

        } catch (Exception e) {
            //UPDATE STATUS
            application.setStatus("ERROR");
            application.setStage(stage);
            application.setDescription(e.getMessage() + "- toString: " + e.toString());

            System.out.println(stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);

            if (e.getMessage().contains("Work flow failed!!!")) {
                stage = "END OF LEAD DETAIL";
                application.setStage(stage);
                await("Get error fail!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> driver.findElements(By.id("error-message")).size() > 0);

                if (driver.findElements(By.id("error-message")) != null && driver.findElements(By.id("error-message")).size() > 0) {
                    String error = "Error: ";
                    for (WebElement we : driver.findElements(By.id("error-message"))) {
                        error += " - " + we.getText();
                    }
                    application.setError(error);
                    System.out.println(stage + "=>" + error);
                }
            }
        } finally {
            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());

            logout(driver,accountDTO.getUserName());

            updateStatusRabbit(application, "updateFullApp");

        }
    }

    public void runAutomation_UpdateAppError(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO) throws Exception {
        Instant start = Instant.now();
        String stage = "";
        String stageError = "";
        Application application = Application.builder().build();

        String leadAppID = "";
        try {
            stage = "INIT DATA";
            //*************************** GET DATA *********************//
            application = (Application) mapValue.get("ApplicationDTO");
            leadAppID = application.getApplicationId();
            ApplicationInfoDTO applicationInfoDTO = (ApplicationInfoDTO) mapValue.get("ApplicationInfoDTO");
            LoanDetailsDTO loanDetailsDTO = (LoanDetailsDTO) mapValue.get("LoanDetailsDTO");
            LoanDetailsVapDTO loanDetailsVapDTO = (LoanDetailsVapDTO) mapValue.get("LoanDetailsVapDTO");
            List<ReferenceDTO> referenceDTO = (List<ReferenceDTO>) mapValue.get("ReferenceDTO");
            MiscFptDTO miscFptDTO = (MiscFptDTO) mapValue.get("MiscFptDTO");
            MiscFrmAppDtlDTO miscFrmAppDtlDTO = (MiscFrmAppDtlDTO) mapValue.get("MiscFrmAppDtlDTO");
            MiscVinIdDTO miscVinIdDTO = (MiscVinIdDTO) mapValue.get("MiscVinIdDTO");

            application.setAutomationAcc(accountDTO.getUserName());

            //vinId = loanDetailsDTO.getApplicationNumber();
            //*************************** END GET DATA *********************//

            //---------------- GET STAGE -------------------------//
            stageError = application.getStage();

            switch (stageError) {
                case "END OF LEAD DETAIL":
                    if (application.getError() != null && application.getError().contains("Address")) {
                        updateAppError_EndLeadDetailWithAddress(driver, stageError, accountDTO, leadAppID, mapValue);
                    } else {
                        updateAppError_EndLeadDetail(driver, stageError, accountDTO, leadAppID, mapValue);
                    }
                    break;
                case "END OF LEAD DETAILV1":
                    updateAppError_EndLeadDetailV1(driver, stageError, accountDTO, leadAppID, mapValue);
                    break;
                case "PERSONAL INFORMATION":
                    updateAppError_PersonalInformation(driver, stageError, accountDTO, leadAppID, mapValue);
                    break;
                case "EMPLOYMENT DETAILS":
                    updateAppError_PersonalInformation(driver, stageError, accountDTO, leadAppID, mapValue);
                    break;
                case "LOGIN FINONE":
                    updateAppError_PersonalInformation(driver, stageError, accountDTO, leadAppID, mapValue);
                    break;
                case "INIT DATA":
                    updateAppError_PersonalInformation(driver, stageError, accountDTO, leadAppID, mapValue);
                    break;
                case "FULL":
                    updateAppError_Full(driver, stageError, accountDTO, leadAppID, mapValue);
                    break;
                default:
                    updateAppError_Full(driver, stageError, accountDTO, leadAppID, mapValue);
                    break;
            }

            return;
//
//            System.out.println(stage + ": DONE" );
//            stage="LOGIN FINONE";
//            HashMap<String, String> dataControl = new HashMap<>();
//            LoginPage loginPage = new LoginPage(driver);
//            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
//            loginPage.clickLogin();
//
//            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(driver::getTitle, is("DashBoard"));
//            Utilities.captureScreenShot(driver);
//
//            stage="HOME PAGE";
//            HomePage homePage = new HomePage(driver);
//
//            //DATA INPUT UPDATE
//            //String leadAppID="APPL00086527";
//            System.out.println(stage + ": DONE" );
//            Utilities.captureScreenShot(driver);
//            stage="APPLICATIONS";
//            // ========== APPLICATIONS =================
//            homePage.getMenuApplicationElement().click();
//            homePage.getApplicationElement().click();
//
//            await("Application Grid timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(driver::getTitle, is("Application Grid"));
//
//            ApplicationGridPage applicationGridPage=new ApplicationGridPage(driver);
//            applicationGridPage.updateData(leadAppID);
//            Utilities.captureScreenShot(driver);
//
//            System.out.println(stage + ": DONE" );
//            Utilities.captureScreenShot(driver);
//            stage="LEAD DETAILS (DATA ENTRY)";
//            // ========== LEAD DETAILS (DATA ENTRY) =================
//            LeadDetailDEPage leadDetailDEPage= new LeadDetailDEPage(driver);
//            leadDetailDEPage.setData(leadAppID);
//            Utilities.captureScreenShot(driver);
//
//            System.out.println(stage + ": DONE" );
//            Utilities.captureScreenShot(driver);
//            stage="PERSONAL INFORMATION";
//            // ========== PERSONAL INFORMATION =================
//            DE_ApplicationInfoPage appInfoPage = new DE_ApplicationInfoPage(driver);
//            DE_ApplicationInfoPersonalTab personalTab = appInfoPage.getApplicationInfoPersonalTab();
//
//            await("getPersonalCustomerDetailsElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(()->personalTab.getPersonalCustomerDetailsElement().isDisplayed());
//
//            personalTab.updateValue(applicationInfoDTO);
//
//            System.out.println(stage + ": DONE" );
//            Utilities.captureScreenShot(driver);
//            stage="EMPLOYMENT DETAILS";
//            // ========== EMPLOYMENT DETAILS =================
//            await("Load employment details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> appInfoPage.getEmploymentDetailsTabElement().getAttribute("class").contains("active"));
//
//            DE_ApplicationInfoEmploymentDetailsTab employmentDetailsTab = appInfoPage.getApplicationInfoEmploymentDetailsTab();
//            await("Span Application Id not enabled Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> employmentDetailsTab.getApplicationId().isEnabled());
//
//            employmentDetailsTab.updateData(applicationInfoDTO.getEmploymentDetails());
//            employmentDetailsTab.getDoneBtnElement().click();
//            await("Employment Status loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> employmentDetailsTab.getTableAfterDoneElement().isDisplayed());
//            employmentDetailsTab.setExperienceInIndustry(applicationInfoDTO.getEmploymentDetails());
//            employmentDetailsTab.getSaveAndNextBtnElement().click();
//
//            System.out.println(stage + ": DONE" );
//            Utilities.captureScreenShot(driver);
//            stage="FINANCIAL DETAILS";
//            // ==========FINANCIAL DETAILS =================
//            if (applicationInfoDTO.getIncomeDetails() != null && applicationInfoDTO.getIncomeDetails().size() > 0) {
//                DE_ApplicationInfoFinancialDetailsTab financialDetailsTab = appInfoPage.getApplicationInfoFinancialDetailsTab();
//
//                financialDetailsTab.openIncomeDetailSection();
//                await("Load financial details - income details Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                        .until(() -> financialDetailsTab.getIncomeDetailDivElement().isDisplayed());
//                financialDetailsTab.updateIncomeDetailsData(applicationInfoDTO.getIncomeDetails());
//                financialDetailsTab.saveAndNext();
//            }
//
//            System.out.println(stage + ": DONE" );
//            Utilities.captureScreenShot(driver);
//
//            // ==========LOAN DETAILS=================
//            stage="LOAN DETAIL - SOURCING DETAIL TAB";
//            DE_LoanDetailsPage loanDetailsPage = new DE_LoanDetailsPage(driver);
//            Utilities.captureScreenShot(driver);
//            loanDetailsPage.getTabLoanDetailsElement().click();
//
//            DE_LoanDetailsSourcingDetailsTab loanDetailsSourcingDetailsTab = new DE_LoanDetailsSourcingDetailsTab(driver);
//            await("Load loan details - sourcing details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> loanDetailsSourcingDetailsTab.getTabSourcingDetailsElement().getAttribute("class").contains("active"));
//            await("Load loan details - sourcing details container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> loanDetailsSourcingDetailsTab.getSourcingDetailsDivContainerElement().isDisplayed());
//            loanDetailsSourcingDetailsTab.updateData(loanDetailsDTO);
//            Utilities.captureScreenShot(driver);
//            loanDetailsSourcingDetailsTab.getBtnSaveAndNextElement().click();
//
//            //update
//            await("modalConfirmElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> loanDetailsSourcingDetailsTab.getModalConfirmDeleteVapNextElement().size()>0);
//
//            await("modalBtnConfirmElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> loanDetailsSourcingDetailsTab.getModalConfirmDeleteVapNextElement().get(1).findElement(By.id("confirmDeleteVapNext")).isDisplayed());
//
//            System.out.println(loanDetailsSourcingDetailsTab.getModalConfirmDeleteVapNextElement().size());
//
//            Actions actions=new Actions(driver);
//
//            await("getBtnConfirmDeleteVapNextElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElement().size()>0);
//
//            System.out.println(loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElement().size());
//
//            await("getBtnConfirmDeleteVapNextElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElement().get(1).isDisplayed());
//
//            loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElement().get(1).click();
//
//            System.out.println("LOAN DETAILS: DONE");
//
//            System.out.println(stage + ": DONE" );
//            Utilities.captureScreenShot(driver);
//
//            // ========== VAP DETAILS =======================
//            if (loanDetailsVapDTO != null && !loanDetailsVapDTO.getVapProduct().equals("")) {
//                stage="LOAN DETAIL - VAP DETAIL TAB";
//                Utilities.captureScreenShot(driver);
//                DE_LoanDetailsVapDetailsTab loanDetailsVapDetailsTab = new DE_LoanDetailsVapDetailsTab(driver);
//
//                await("Load loan details - sourcing details container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                        .until(() -> loanDetailsVapDetailsTab.getVapDetailsDivContainerElement().isDisplayed());
//
//                loanDetailsVapDetailsTab.updateData(loanDetailsVapDTO);
//                Utilities.captureScreenShot(driver);
//                loanDetailsVapDetailsTab.getBtnSaveAndNextElement().click();
//
//                System.out.println("LOAN DETAILS - VAP: DONE");
//
//            }
//
//            System.out.println(stage + ": DONE" );
//            Utilities.captureScreenShot(driver);
//
//            // ==========REFERENCES=================
//            stage="REFERENCES";
//            DE_ReferencesPage referencesPage = new DE_ReferencesPage(driver);
//            await("Load references tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> referencesPage.getTabReferencesElement().isDisplayed() && referencesPage.getTabReferencesElement().isEnabled());
//            referencesPage.getTabReferencesElement().click();
//
//            referencesPage.updateData(referenceDTO);
//            Utilities.captureScreenShot(driver);
//            referencesPage.getSaveBtnElement().click();
//
//            System.out.println("REFERENCES DETAILS: DONE");
//
//            System.out.println(stage + ": DONE" );
//            Utilities.captureScreenShot(driver);
//
//            // ==========MISC FRM APP DTL=================
//            stage="MISC FRM APPDTL";
//            DE_MiscFrmAppDtlPage miscFrmAppDtlPage = new DE_MiscFrmAppDtlPage(driver);
//            miscFrmAppDtlPage.getTabMiscFrmAppDtlElementByName().click();
//
//            miscFrmAppDtlPage.updateData(miscFrmAppDtlDTO);
//
//            await("getBtnSaveElement end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> miscFrmAppDtlPage._getBtnSaveElement().isEnabled());
//
//            Utilities.captureScreenShot(driver);
//            miscFrmAppDtlPage._getBtnSaveElement().click();
//
//            await("getBtnMoveToNextStageElement end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> miscFrmAppDtlPage.getBtnMoveToNextStageElement().isEnabled());
//
//            miscFrmAppDtlPage.getBtnMoveToNextStageElement().click();
//            System.out.println("MISC FRM APP DTL: DONE");
//
//            await("Work flow failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(driver::getTitle, is("Application Grid"));
//
//            System.out.println(stage + ": DONE" );
//            Utilities.captureScreenShot(driver);
//            stage="COMPLETE";
//            System.out.println("AUTO OK: user =>" + accountDTO.getUserName());
//
//            //UPDATE STATUS
//            application.setStatus("OK");
//            application.setDescription("Update thanh cong");
//

        } catch (Exception e) {
            //UPDATE STATUS
            application.setStatus("ERROR");
            application.setStage(stage);
            application.setDescription(e.getMessage());

            System.out.println(stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);

            if (e.getMessage().contains("Work flow failed!!!")) {
                stage = "END OF LEAD DETAIL";
                application.setStage(stage);
                await("Get error fail!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> driver.findElements(By.id("error-message")).size() > 0);

                if (driver.findElements(By.id("error-message")) != null && driver.findElements(By.id("error-message")).size() > 0) {
                    String error = "Error: ";
                    for (WebElement we : driver.findElements(By.id("error-message"))) {
                        error += " - " + we.getText();
                    }
                    application.setError(error);
                    System.out.println(stage + "=>" + error);
                }
            }
        } finally {
            //updateStatusRabbit(application,"updateAppError");
            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            System.out.println(stage);
            logout(driver,accountDTO.getUserName());
        }
    }

    private void updateStatusRabbit(Application application, String func) throws Exception {
//        JsonNode jsonNode= rabbitMQService.sendAndReceive("tpf-service-dataentry",
//                Map.of("func", func, "token",
//                        String.format("Bearer %s", rabbitMQService.getToken().path("access_token").asText()),"body", application));

        JsonNode jsonNode = rabbitMQService.sendAndReceive(rabbitIdRes,
                Map.of("func", func, "body", application));
        System.out.println("rabit:=>" + jsonNode.toString());


    }

    private void updateAppError_EndLeadDetail(WebDriver driver, String stage, LoginDTO accountDTO, String leadAppID, Map<String, Object> mapValue) throws Exception {
        Application application = Application.builder().build();
        application.setAutomationAcc(accountDTO.getUserName());
        try {
            application = (Application) mapValue.get("ApplicationDTO");
            ApplicationInfoDTO applicationInfoDTO = (ApplicationInfoDTO) mapValue.get("ApplicationInfoDTO");
            LoanDetailsDTO loanDetailsDTO = (LoanDetailsDTO) mapValue.get("LoanDetailsDTO");
            LoanDetailsVapDTO loanDetailsVapDTO = (LoanDetailsVapDTO) mapValue.get("LoanDetailsVapDTO");
            List<ReferenceDTO> referenceDTO = (List<ReferenceDTO>) mapValue.get("ReferenceDTO");
            List<DocumentDTO> documentDTOS = (List<DocumentDTO>) mapValue.get("DocumentDTO");
            MiscFptDTO miscFptDTO = (MiscFptDTO) mapValue.get("MiscFptDTO");
            MiscFrmAppDtlDTO miscFrmAppDtlDTO = (MiscFrmAppDtlDTO) mapValue.get("MiscFrmAppDtlDTO");
            MiscVinIdDTO miscVinIdDTO = (MiscVinIdDTO) mapValue.get("MiscVinIdDTO");

            application.setAutomationAcc(accountDTO.getUserName());

            stage = "LOGIN FINONE";
            HashMap<String, String> dataControl = new HashMap<>();
            LoginPage loginPage = new LoginPage(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
            loginPage.clickLogin();

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));
            Utilities.captureScreenShot(driver);

            stage = "HOME PAGE";
            HomePage homePage = new HomePage(driver);

            //DATA INPUT UPDATE
            //String leadAppID="APPL00086527";
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "APPLICATIONS";
//            // ========== APPLICATIONS =================
//            homePage.getMenuApplicationElement().click();
//            homePage.getApplicationElement().click();
//
//            await("Application Grid timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(driver::getTitle, is("Application Grid"));
//
//            ApplicationGridPage applicationGridPage = new ApplicationGridPage(driver);
//            applicationGridPage.updateData(leadAppID);
//            Utilities.captureScreenShot(driver);


            // ========== APPLICATIONS =================
            homePage.getMenuApplicationElement().click();

            // update lai flow assign app moi

//            homePage.getApplicationElement().click();
//
//            await("Application Grid timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(driver::getTitle, is("Application Grid"));
//
//            ApplicationGridPage applicationGridPage=new ApplicationGridPage(driver);
//            applicationGridPage.setData(leadAppID);
//            Utilities.captureScreenShot(driver);
//
//            System.out.println(stage + ": DONE" );
//            Utilities.captureScreenShot(driver);
            stage = "APPLICATION MANAGER";
            // ========== APPLICATION MANAGER =================
            homePage.getApplicationManagerElement().click();
            await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Manager"));

            DE_ApplicationManagerPage de_applicationManagerPage = new DE_ApplicationManagerPage(driver);

            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> de_applicationManagerPage.getApplicationManagerFormElement().isDisplayed());
            de_applicationManagerPage.setData(leadAppID, accountDTO.getUserName());
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "APPLICATION GRID";
            // ========== APPLICATION GRID =================
            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> de_applicationManagerPage.getMenuApplicationElement().isDisplayed());

            de_applicationManagerPage.getMenuApplicationElement().click();
            Utilities.captureScreenShot(driver);
            de_applicationManagerPage.getApplicationElement().click();
            Utilities.captureScreenShot(driver);
            await("Application Grid timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Grid"));
            Utilities.captureScreenShot(driver);
            ApplicationGridPage applicationGridPage = new ApplicationGridPage(driver);
            applicationGridPage.updateData(leadAppID);
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ========== LEAD DETAILS (DATA ENTRY) =================
            LeadDetailDEPage leadDetailDEPage = new LeadDetailDEPage(driver);
            leadDetailDEPage.setData(leadAppID);
            Utilities.captureScreenShot(driver);


            //tam thoi commnet lai
//            // ==========LOAN DETAILS=================
//            stage = "LOAN DETAIL - SOURCING DETAIL TAB";
//            DE_LoanDetailsPage loanDetailsPage = new DE_LoanDetailsPage(driver);
//            Utilities.captureScreenShot(driver);
//            loanDetailsPage.getTabLoanDetailsElement().click();
//
//            DE_LoanDetailsSourcingDetailsTab loanDetailsSourcingDetailsTab = new DE_LoanDetailsSourcingDetailsTab(driver);
//            await("Load loan details - sourcing details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> loanDetailsSourcingDetailsTab.getTabSourcingDetailsElement().getAttribute("class").contains("active"));
//            await("Load loan details - sourcing details container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> loanDetailsSourcingDetailsTab.getSourcingDetailsDivContainerElement().isDisplayed());
//            loanDetailsSourcingDetailsTab.updateData(loanDetailsDTO);
//            Utilities.captureScreenShot(driver);
//            loanDetailsSourcingDetailsTab.getBtnSaveAndNextElement().click();
//
////            //update
////            await("modalConfirmElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
////                    .until(() -> loanDetailsSourcingDetailsTab.getModalConfirmDeleteVapNextElement().size() > 0);
////
////            await("modalBtnConfirmElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
////                    .until(() -> loanDetailsSourcingDetailsTab.getModalConfirmDeleteVapNextElement().get(1).findElement(By.id("confirmDeleteVapNext")).isDisplayed());
////
////            System.out.println(loanDetailsSourcingDetailsTab.getModalConfirmDeleteVapNextElement().size());
////
////            Actions actions = new Actions(driver);
////
////            await("getBtnConfirmDeleteVapNextElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
////                    .until(() -> loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElement().size() > 0);
////
////            System.out.println(loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElement().size());
////
////            await("getBtnConfirmDeleteVapNextElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
////                    .until(() -> loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElement().get(1).isDisplayed());
////
////            loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElement().get(1).click();
//
//            try {
//                await("getBtnConfirmDeleteVapNextElement1 visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                        .until(() -> driver.findElement(By.xpath("//div[@class='modal-scrollable']//a[contains(@id, 'confirmDeleteVapNext')]")).isDisplayed());
//                //loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElement1().click();
//
//                driver.findElement(By.xpath("//div[@class='modal-scrollable']//a[contains(@id, 'confirmDeleteVapNext')]")).click();
//
//                System.out.println("test");
//            } catch (Exception e) {
//                System.out.println("VAP:" + e.toString());
//            }
//
//            System.out.println("LOAN DETAILS: DONE");
//
//            System.out.println(stage + ": DONE");
//            Utilities.captureScreenShot(driver);


            // ==========MISC FRM APP DTL=================
            stage = "MISC FRM APPDTL";
            DE_MiscFrmAppDtlPage miscFrmAppDtlPage = new DE_MiscFrmAppDtlPage(driver);
            miscFrmAppDtlPage.getTabMiscFrmAppDtlElementByName().click();

            miscFrmAppDtlPage.updateData(miscFrmAppDtlDTO);

            await("getBtnSaveElement end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFrmAppDtlPage._getBtnSaveElement().isEnabled());

            Utilities.captureScreenShot(driver);

            miscFrmAppDtlPage._getBtnSaveElement().click();

            await("getBtnMoveToNextStageElement end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFrmAppDtlPage.getBtnMoveToNextStageElement().isEnabled());

            //tam thoi cho sleep de an notification moi click dc button movetonextstage
            Thread.sleep(15000);

            miscFrmAppDtlPage.getBtnMoveToNextStageElement().click();
            System.out.println("MISC FRM APP DTL: DONE");

            await("Work flow failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Grid"));

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "COMPLETE";
            System.out.println("AUTO OK: user =>" + accountDTO.getUserName());

            //UPDATE STATUS
            application.setStatus("OK");
            application.setDescription("Thanh cong");
            application.setStage(stage);
        } catch (Exception e) {
            //UPDATE STATUS
            application.setStatus("ERROR");
            application.setStage(stage);
            application.setDescription(e.getMessage() + "- toString: " + e.toString());

            System.out.println(stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);

            if (e.getMessage().contains("Work flow failed!!!")) {
                stage = "END OF LEAD DETAIL";
                application.setStage(stage);
                await("Get error fail!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> driver.findElements(By.id("error-message")).size() > 0);

                if (driver.findElements(By.id("error-message")) != null && driver.findElements(By.id("error-message")).size() > 0) {
                    String error = "Error: ";
                    for (WebElement we : driver.findElements(By.id("error-message"))) {
                        error += " - " + we.getText();
                    }
                    application.setError(error);
                    System.out.println(stage + "=>" + error);
                }
            }
        } finally {
            updateStatusRabbit(application, "updateAppError");
            System.out.println(stage);
            logout(driver,accountDTO.getUserName());
        }
    }

    private void updateAppError_EndLeadDetailV1(WebDriver driver, String stage, LoginDTO accountDTO, String leadAppID, Map<String, Object> mapValue) throws Exception {
        Application application = Application.builder().build();

        try {
            application = (Application) mapValue.get("ApplicationDTO");
            ApplicationInfoDTO applicationInfoDTO = (ApplicationInfoDTO) mapValue.get("ApplicationInfoDTO");
            LoanDetailsDTO loanDetailsDTO = (LoanDetailsDTO) mapValue.get("LoanDetailsDTO");
            LoanDetailsVapDTO loanDetailsVapDTO = (LoanDetailsVapDTO) mapValue.get("LoanDetailsVapDTO");
            List<ReferenceDTO> referenceDTO = (List<ReferenceDTO>) mapValue.get("ReferenceDTO");
            List<DocumentDTO> documentDTOS = (List<DocumentDTO>) mapValue.get("DocumentDTO");
            MiscFptDTO miscFptDTO = (MiscFptDTO) mapValue.get("MiscFptDTO");
            MiscFrmAppDtlDTO miscFrmAppDtlDTO = (MiscFrmAppDtlDTO) mapValue.get("MiscFrmAppDtlDTO");
            MiscVinIdDTO miscVinIdDTO = (MiscVinIdDTO) mapValue.get("MiscVinIdDTO");

            application.setAutomationAcc(accountDTO.getUserName());

            stage = "LOGIN FINONE";
            HashMap<String, String> dataControl = new HashMap<>();
            LoginPage loginPage = new LoginPage(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
            loginPage.clickLogin();

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));
            Utilities.captureScreenShot(driver);

            stage = "HOME PAGE";
            HomePage homePage = new HomePage(driver);

            //DATA INPUT UPDATE
            //String leadAppID="APPL00086527";
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "APPLICATIONS";
//            // ========== APPLICATIONS =================
//            homePage.getMenuApplicationElement().click();
//            homePage.getApplicationElement().click();
//
//            await("Application Grid timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(driver::getTitle, is("Application Grid"));
//
//            ApplicationGridPage applicationGridPage = new ApplicationGridPage(driver);
//            applicationGridPage.updateData(leadAppID);
//            Utilities.captureScreenShot(driver);


            // ========== APPLICATIONS =================
            homePage.getMenuApplicationElement().click();

            // update lai flow assign app moi

//            homePage.getApplicationElement().click();
//
//            await("Application Grid timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(driver::getTitle, is("Application Grid"));
//
//            ApplicationGridPage applicationGridPage=new ApplicationGridPage(driver);
//            applicationGridPage.setData(leadAppID);
//            Utilities.captureScreenShot(driver);
//
//            System.out.println(stage + ": DONE" );
//            Utilities.captureScreenShot(driver);
            stage = "APPLICATION MANAGER";
            // ========== APPLICATION MANAGER =================
            homePage.getApplicationManagerElement().click();
            await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Manager"));

            DE_ApplicationManagerPage de_applicationManagerPage = new DE_ApplicationManagerPage(driver);

            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> de_applicationManagerPage.getApplicationManagerFormElement().isDisplayed());
            de_applicationManagerPage.setData(leadAppID, accountDTO.getUserName());
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "APPLICATION GRID";
            // ========== APPLICATION GRID =================
            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> de_applicationManagerPage.getMenuApplicationElement().isDisplayed());

            de_applicationManagerPage.getMenuApplicationElement().click();
            Utilities.captureScreenShot(driver);
            de_applicationManagerPage.getApplicationElement().click();
            Utilities.captureScreenShot(driver);
            await("Application Grid timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Grid"));
            Utilities.captureScreenShot(driver);
            ApplicationGridPage applicationGridPage = new ApplicationGridPage(driver);
            applicationGridPage.updateData(leadAppID);
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ========== LEAD DETAILS (DATA ENTRY) =================
            LeadDetailDEPage leadDetailDEPage = new LeadDetailDEPage(driver);
            leadDetailDEPage.setData(leadAppID);
            Utilities.captureScreenShot(driver);

            // ==========MISC FRM APP DTL=================
            stage = "MISC FRM APPDTL";
            DE_MiscFrmAppDtlPage miscFrmAppDtlPage = new DE_MiscFrmAppDtlPage(driver);
            miscFrmAppDtlPage.getTabMiscFrmAppDtlElementByName().click();

            miscFrmAppDtlPage.updateData(miscFrmAppDtlDTO);

            await("getBtnSaveElement end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFrmAppDtlPage._getBtnSaveElement().isEnabled());

            Utilities.captureScreenShot(driver);

            miscFrmAppDtlPage._getBtnSaveElement().click();

            await("getBtnMoveToNextStageElement end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFrmAppDtlPage.getBtnMoveToNextStageElement().isEnabled());

            //tam thoi cho sleep de an notification moi click dc button movetonextstage
            Thread.sleep(15000);

            miscFrmAppDtlPage.getBtnMoveToNextStageElement().click();
            System.out.println("MISC FRM APP DTL: DONE");

            await("Work flow failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Grid"));

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "COMPLETE";
            System.out.println("AUTO OK: user =>" + accountDTO.getUserName());

            //UPDATE STATUS
            application.setStatus("OK");
            application.setDescription("Thanh cong");
            application.setStage(stage);
        } catch (Exception e) {
            //UPDATE STATUS
            application.setStatus("ERROR");
            application.setStage(stage);
            application.setDescription(e.getMessage());

            System.out.println(stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);

            if (e.getMessage().contains("Work flow failed!!!")) {
                stage = "END OF LEAD DETAIL";
                application.setStage(stage);
                await("Get error fail!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> driver.findElements(By.id("error-message")).size() > 0);

                if (driver.findElements(By.id("error-message")) != null && driver.findElements(By.id("error-message")).size() > 0) {
                    String error = "Error: ";
                    for (WebElement we : driver.findElements(By.id("error-message"))) {
                        error += " - " + we.getText();
                    }
                    application.setError(error);
                    System.out.println(stage + "=>" + error);
                }
            }
        } finally {
            updateStatusRabbit(application, "updateAppError");
            System.out.println(stage);
            logout(driver,accountDTO.getUserName());
        }
    }

    private void updateAppError_PersonalInformation(WebDriver driver, String stage, LoginDTO accountDTO, String leadAppID, Map<String, Object> mapValue) throws Exception {
        Application application = Application.builder().build();

        try {
            application = (Application) mapValue.get("ApplicationDTO");
            ApplicationInfoDTO applicationInfoDTO = (ApplicationInfoDTO) mapValue.get("ApplicationInfoDTO");
            LoanDetailsDTO loanDetailsDTO = (LoanDetailsDTO) mapValue.get("LoanDetailsDTO");
            LoanDetailsVapDTO loanDetailsVapDTO = (LoanDetailsVapDTO) mapValue.get("LoanDetailsVapDTO");
            List<ReferenceDTO> referenceDTO = (List<ReferenceDTO>) mapValue.get("ReferenceDTO");
            List<DocumentDTO> documentDTOS = (List<DocumentDTO>) mapValue.get("DocumentDTO");
            MiscFptDTO miscFptDTO = (MiscFptDTO) mapValue.get("MiscFptDTO");
            MiscFrmAppDtlDTO miscFrmAppDtlDTO = (MiscFrmAppDtlDTO) mapValue.get("MiscFrmAppDtlDTO");
            MiscVinIdDTO miscVinIdDTO = (MiscVinIdDTO) mapValue.get("MiscVinIdDTO");

            application.setAutomationAcc(accountDTO.getUserName());

            stage = "LOGIN FINONE";
            HashMap<String, String> dataControl = new HashMap<>();
            LoginPage loginPage = new LoginPage(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
            loginPage.clickLogin();

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));
            Utilities.captureScreenShot(driver);

            stage = "HOME PAGE";
            HomePage homePage = new HomePage(driver);

            //DATA INPUT UPDATE
            //String leadAppID="APPL00086527";
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "APPLICATIONS";
//            // ========== APPLICATIONS =================
//            homePage.getMenuApplicationElement().click();
//            homePage.getApplicationElement().click();
//
//            await("Application Grid timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(driver::getTitle, is("Application Grid"));
//
//            ApplicationGridPage applicationGridPage = new ApplicationGridPage(driver);
//            applicationGridPage.updateData(leadAppID);
//            Utilities.captureScreenShot(driver);

            // ========== APPLICATIONS =================
            homePage.getMenuApplicationElement().click();

            // update lai flow assign app moi

//            homePage.getApplicationElement().click();
//
//            await("Application Grid timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(driver::getTitle, is("Application Grid"));
//
//            ApplicationGridPage applicationGridPage=new ApplicationGridPage(driver);
//            applicationGridPage.setData(leadAppID);
//            Utilities.captureScreenShot(driver);
//
//            System.out.println(stage + ": DONE" );
//            Utilities.captureScreenShot(driver);
            stage = "APPLICATION MANAGER";
            // ========== APPLICATION MANAGER =================
            homePage.getApplicationManagerElement().click();
            await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Manager"));

            DE_ApplicationManagerPage de_applicationManagerPage = new DE_ApplicationManagerPage(driver);

            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> de_applicationManagerPage.getApplicationManagerFormElement().isDisplayed());
            de_applicationManagerPage.setData(leadAppID, accountDTO.getUserName());
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "APPLICATION GRID";
            // ========== APPLICATION GRID =================
            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> de_applicationManagerPage.getMenuApplicationElement().isDisplayed());

            de_applicationManagerPage.getMenuApplicationElement().click();
            Utilities.captureScreenShot(driver);
            de_applicationManagerPage.getApplicationElement().click();
            Utilities.captureScreenShot(driver);
            await("Application Grid timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Grid"));
            Utilities.captureScreenShot(driver);
            ApplicationGridPage applicationGridPage = new ApplicationGridPage(driver);
            applicationGridPage.updateData(leadAppID);
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ========== LEAD DETAILS (DATA ENTRY) =================
            LeadDetailDEPage leadDetailDEPage = new LeadDetailDEPage(driver);
            leadDetailDEPage.setData(leadAppID);
            Utilities.captureScreenShot(driver);

            stage = "PERSONAL INFORMATION";
            // ========== PERSONAL INFORMATION =================
            DE_ApplicationInfoPage appInfoPage = new DE_ApplicationInfoPage(driver);
            DE_ApplicationInfoPersonalTab personalTab = appInfoPage.getApplicationInfoPersonalTab();

            await("getPersonalCustomerDetailsElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> personalTab.getPersonalCustomerDetailsElement().isDisplayed());

            personalTab.updateValue(applicationInfoDTO);

            System.out.println(stage + ": DONE");
            stage = "EMPLOYMENT DETAILS";
            // ========== EMPLOYMENT DETAILS =================

            //appInfoPage.getEmploymentDetailsTabElement().click();

            await("Load employment details tab Timeout!").atMost(60, TimeUnit.SECONDS)
                    .until(() -> appInfoPage.getEmploymentDetailsTabElement().getAttribute("class").contains("active"));

            DE_ApplicationInfoEmploymentDetailsTab employmentDetailsTab = appInfoPage.getApplicationInfoEmploymentDetailsTab();
            await("Span Application Id not enabled Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> employmentDetailsTab.getApplicationId().isEnabled());

            employmentDetailsTab.setData(applicationInfoDTO.getEmploymentDetails());
            employmentDetailsTab.getDoneBtnElement().click();
            await("Employment Status loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> employmentDetailsTab.getTableAfterDoneElement().isDisplayed());
            employmentDetailsTab.setExperienceInIndustry(applicationInfoDTO.getEmploymentDetails());
            employmentDetailsTab.setMajorOccupation(applicationInfoDTO.getEmploymentDetails());
            employmentDetailsTab.getSaveAndNextBtnElement().click();
            Utilities.captureScreenShot(driver);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "EMPLOYMENT DETAILS - FINANCIAL";
            // ==========FINANCIAL DETAILS =================
            if (applicationInfoDTO.getIncomeDetails() != null && applicationInfoDTO.getIncomeDetails().size() > 0) {
                DE_ApplicationInfoFinancialDetailsTab financialDetailsTab = appInfoPage.getApplicationInfoFinancialDetailsTab();

                financialDetailsTab.openIncomeDetailSection();
                await("Load financial details - income details Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> financialDetailsTab.getIncomeDetailDivElement().isDisplayed());
                financialDetailsTab.setIncomeDetailsData(applicationInfoDTO.getIncomeDetails());
                financialDetailsTab.saveAndNext();
            }

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ==========LOAN DETAILS=================
            stage = "LOAN DETAIL PAGE - SOURCING DETAIL TAB";
            DE_LoanDetailsPage loanDetailsPage = new DE_LoanDetailsPage(driver);
            Utilities.captureScreenShot(driver);
            loanDetailsPage.getTabLoanDetailsElement().click();

            DE_LoanDetailsSourcingDetailsTab loanDetailsSourcingDetailsTab = new DE_LoanDetailsSourcingDetailsTab(driver);
            await("Load loan details - sourcing details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getTabSourcingDetailsElement().getAttribute("class").contains("active"));
            await("Load loan details - sourcing details container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getSourcingDetailsDivContainerElement().isDisplayed());
            loanDetailsSourcingDetailsTab.setData(loanDetailsDTO);
            Utilities.captureScreenShot(driver);
            loanDetailsSourcingDetailsTab.getBtnSaveAndNextElement().click();

//            if(!"none".equals(loanDetailsSourcingDetailsTab.getDialogConfirmDeleteVapNextElements().isDisplayed()))
//            {
//                JavascriptExecutor jse = (JavascriptExecutor)driver;
//                jse.executeScript("arguments[0].click();", loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElements());
//                Thread.sleep(2000);
//            }

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ==========VAP DETAILS=======================
            if (loanDetailsVapDTO != null && loanDetailsVapDTO.getVapProduct() != null && !loanDetailsVapDTO.getVapProduct().equals("")) {
                stage = "LOAN DETAIL PAGE - VAP DETAIL TAB";
                Utilities.captureScreenShot(driver);
                DE_LoanDetailsVapDetailsTab loanDetailsVapDetailsTab = new DE_LoanDetailsVapDetailsTab(driver);

                await("Load loan details - sourcing details container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> loanDetailsVapDetailsTab.getVapDetailsDivContainerElement().isDisplayed());
                loanDetailsVapDetailsTab.setData(loanDetailsVapDTO);
                Utilities.captureScreenShot(driver);
                loanDetailsVapDetailsTab.getBtnSaveAndNextElement().click();

                System.out.println("LOAN DETAILS - VAP: DONE");

            }

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "DOCUMENTS";
            // ==========DOCUMENTS=================
            if (documentDTOS.size() > 0) {
                DE_DocumentsPage documentsPage = new DE_DocumentsPage(driver);
                await("Load document tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> documentsPage.getTabDocumentsElement().isDisplayed() && documentsPage.getTabDocumentsElement().isEnabled());
                documentsPage.getTabDocumentsElement().click();
                await("Load document container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> documentsPage.getDocumentsContainerElement().isDisplayed());
                documentsPage.getBtnGetDocumentElement().click();
                await("Load document table Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> documentsPage.getLendingTrElement().size() > 0);

                documentsPage.updateData(documentDTOS, downdloadFileURL);
                Utilities.captureScreenShot(driver);
                documentsPage.getBtnSubmitElement().click();
            }

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "REFERENCES";
            // ==========REFERENCES=================
            stage = "REFERENCES PAGE";
            DE_ReferencesPage referencesPage = new DE_ReferencesPage(driver);
            await("Load references tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> referencesPage.getTabReferencesElement().isDisplayed() && referencesPage.getTabReferencesElement().isEnabled());
            referencesPage.getTabReferencesElement().click();
            referencesPage.setData(referenceDTO);
            referencesPage.getSaveBtnElement().click();

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ==========MISC FRM APP DTL=================
            stage = "MISC FRM APPDTL PAGE";
            DE_MiscFrmAppDtlPage miscFrmAppDtlPage = new DE_MiscFrmAppDtlPage(driver);
            miscFrmAppDtlPage.getTabMiscFrmAppDtlElementByName().click();
            miscFrmAppDtlPage.setData(miscFrmAppDtlDTO);
            Utilities.captureScreenShot(driver);
            await("getBtnSaveElement end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFrmAppDtlPage._getBtnSaveElement().isEnabled());
            miscFrmAppDtlPage._getBtnSaveElement().click();
            Utilities.captureScreenShot(driver);
            await("getBtnMoveToNextStageElement end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFrmAppDtlPage.getBtnMoveToNextStageElement().isEnabled());
            Utilities.captureScreenShot(driver);

            //tam thoi cho sleep de an notification moi click dc button movetonextstage
            Thread.sleep(15000);

            miscFrmAppDtlPage.getBtnMoveToNextStageElement().click();
            Utilities.captureScreenShot(driver);
            System.out.println("MISC FRM APP DTL: DONE");

            await("Work flow failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Grid"));

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "COMPLETE";
            System.out.println("AUTO OK: user =>" + accountDTO.getUserName());

            //UPDATE STATUS
            application.setStatus("OK");
            application.setStage(stage);
            application.setDescription("Thanh cong");

        } catch (Exception e) {
            //UPDATE STATUS
            application.setStatus("ERROR");
            application.setStage(stage);
            application.setDescription(e.getMessage() + "- toString: " + e.toString());

            System.out.println(stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);

            if (e.getMessage().contains("Work flow failed!!!")) {
                stage = "END OF LEAD DETAIL";
                application.setStage(stage);
                await("Get error fail!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> driver.findElements(By.id("error-message")).size() > 0);

                if (driver.findElements(By.id("error-message")) != null && driver.findElements(By.id("error-message")).size() > 0) {
                    String error = "Error: ";
                    for (WebElement we : driver.findElements(By.id("error-message"))) {
                        error += " - " + we.getText();
                    }
                    application.setError(error);
                    System.out.println(stage + "=>" + error);
                }
            }
        } finally {
            updateStatusRabbit(application, "updateAppError");
            System.out.println(stage);
            logout(driver,accountDTO.getUserName());
        }
    }

    private void updateAppError_EndLeadDetailWithAddress(WebDriver driver, String stage, LoginDTO accountDTO, String leadAppID, Map<String, Object> mapValue) throws Exception {
        Application application = Application.builder().build();

        try {
            application = (Application) mapValue.get("ApplicationDTO");
            ApplicationInfoDTO applicationInfoDTO = (ApplicationInfoDTO) mapValue.get("ApplicationInfoDTO");
            LoanDetailsDTO loanDetailsDTO = (LoanDetailsDTO) mapValue.get("LoanDetailsDTO");
            LoanDetailsVapDTO loanDetailsVapDTO = (LoanDetailsVapDTO) mapValue.get("LoanDetailsVapDTO");
            List<ReferenceDTO> referenceDTO = (List<ReferenceDTO>) mapValue.get("ReferenceDTO");
            MiscFptDTO miscFptDTO = (MiscFptDTO) mapValue.get("MiscFptDTO");
            MiscFrmAppDtlDTO miscFrmAppDtlDTO = (MiscFrmAppDtlDTO) mapValue.get("MiscFrmAppDtlDTO");
            MiscVinIdDTO miscVinIdDTO = (MiscVinIdDTO) mapValue.get("MiscVinIdDTO");

            application.setAutomationAcc(accountDTO.getUserName());

            stage = "LOGIN FINONE";
            HashMap<String, String> dataControl = new HashMap<>();
            LoginPage loginPage = new LoginPage(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
            loginPage.clickLogin();

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));
            Utilities.captureScreenShot(driver);

            stage = "HOME PAGE";
            HomePage homePage = new HomePage(driver);

            //DATA INPUT UPDATE
            //String leadAppID="APPL00086527";
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "APPLICATIONS";
//            // ========== APPLICATIONS =================
//            homePage.getMenuApplicationElement().click();
//            homePage.getApplicationElement().click();
//
//            await("Application Grid timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(driver::getTitle, is("Application Grid"));
//
//            ApplicationGridPage applicationGridPage = new ApplicationGridPage(driver);
//            applicationGridPage.updateData(leadAppID);
//            Utilities.captureScreenShot(driver);

            // ========== APPLICATIONS =================
            homePage.getMenuApplicationElement().click();

            // update lai flow assign app moi

//            homePage.getApplicationElement().click();
//
//            await("Application Grid timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(driver::getTitle, is("Application Grid"));
//
//            ApplicationGridPage applicationGridPage=new ApplicationGridPage(driver);
//            applicationGridPage.setData(leadAppID);
//            Utilities.captureScreenShot(driver);
//
//            System.out.println(stage + ": DONE" );
//            Utilities.captureScreenShot(driver);
            stage = "APPLICATION MANAGER";
            // ========== APPLICATION MANAGER =================
            homePage.getApplicationManagerElement().click();
            await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Manager"));

            DE_ApplicationManagerPage de_applicationManagerPage = new DE_ApplicationManagerPage(driver);

            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> de_applicationManagerPage.getApplicationManagerFormElement().isDisplayed());
            de_applicationManagerPage.setData(leadAppID, accountDTO.getUserName());
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);


            stage = "APPLICATION GRID";
            // ========== APPLICATION GRID =================
            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> de_applicationManagerPage.getMenuApplicationElement().isDisplayed());

            de_applicationManagerPage.getMenuApplicationElement().click();
            Utilities.captureScreenShot(driver);
            de_applicationManagerPage.getApplicationElement().click();
            Utilities.captureScreenShot(driver);
            await("Application Grid timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Grid"));
            Utilities.captureScreenShot(driver);
            ApplicationGridPage applicationGridPage = new ApplicationGridPage(driver);
            applicationGridPage.updateData(leadAppID);
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "LEAD DETAILS (DATA ENTRY)";
            // ========== LEAD DETAILS (DATA ENTRY) =================
            LeadDetailDEPage leadDetailDEPage = new LeadDetailDEPage(driver);
            leadDetailDEPage.setData(leadAppID);
            Utilities.captureScreenShot(driver);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "PERSONAL INFORMATION";
            // ========== PERSONAL INFORMATION =================
            DE_ApplicationInfoPage appInfoPage = new DE_ApplicationInfoPage(driver);
            DE_ApplicationInfoPersonalTab personalTab = appInfoPage.getApplicationInfoPersonalTab();

            await("getPersonalCustomerDetailsElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> personalTab.getPersonalCustomerDetailsElement().isDisplayed());

            personalTab.updateValue(applicationInfoDTO);

            System.out.println(stage + ": DONE");
            stage = "EMPLOYMENT DETAILS";
            // ========== EMPLOYMENT DETAILS =================

            appInfoPage.getEmploymentDetailsTabElement().click();

            await("Load employment details tab Timeout!").atMost(60, TimeUnit.SECONDS)
                    .until(() -> appInfoPage.getEmploymentDetailsTabElement().getAttribute("class").contains("active"));

            DE_ApplicationInfoEmploymentDetailsTab employmentDetailsTab = appInfoPage.getApplicationInfoEmploymentDetailsTab();
            await("Span Application Id not enabled Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> employmentDetailsTab.getApplicationId().isEnabled());

            employmentDetailsTab.updateData(applicationInfoDTO.getEmploymentDetails());
            employmentDetailsTab.getDoneBtnElement().click();
            await("Employment Status loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> employmentDetailsTab.getTableAfterDoneElement().isDisplayed());
            employmentDetailsTab.setExperienceInIndustry(applicationInfoDTO.getEmploymentDetails());
            employmentDetailsTab.getSaveAndNextBtnElement().click();

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "FINANCIAL DETAILS";
            // ==========FINANCIAL DETAILS =================
            if (applicationInfoDTO.getIncomeDetails() != null && applicationInfoDTO.getIncomeDetails().size() > 0) {
                DE_ApplicationInfoFinancialDetailsTab financialDetailsTab = appInfoPage.getApplicationInfoFinancialDetailsTab();

                financialDetailsTab.openIncomeDetailSection();
                await("Load financial details - income details Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> financialDetailsTab.getIncomeDetailDivElement().isDisplayed());
                financialDetailsTab.updateIncomeDetailsData(applicationInfoDTO.getIncomeDetails());
                financialDetailsTab.saveAndNext();
            }

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);


            // ==========LOAN DETAILS=================
            stage = "LOAN DETAIL - SOURCING DETAIL TAB";
            DE_LoanDetailsPage loanDetailsPage = new DE_LoanDetailsPage(driver);
            Utilities.captureScreenShot(driver);
            loanDetailsPage.getTabLoanDetailsElement().click();

            DE_LoanDetailsSourcingDetailsTab loanDetailsSourcingDetailsTab = new DE_LoanDetailsSourcingDetailsTab(driver);
            await("Load loan details - sourcing details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getTabSourcingDetailsElement().getAttribute("class").contains("active"));
            await("Load loan details - sourcing details container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getSourcingDetailsDivContainerElement().isDisplayed());
            loanDetailsSourcingDetailsTab.updateData(loanDetailsDTO);
            Utilities.captureScreenShot(driver);
            loanDetailsSourcingDetailsTab.getBtnSaveAndNextElement().click();

//            //update
//            await("modalConfirmElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> loanDetailsSourcingDetailsTab.getModalConfirmDeleteVapNextElement().size() > 0);
//
//            await("modalBtnConfirmElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> loanDetailsSourcingDetailsTab.getModalConfirmDeleteVapNextElement().get(1).findElement(By.id("confirmDeleteVapNext")).isDisplayed());
//
//            System.out.println(loanDetailsSourcingDetailsTab.getModalConfirmDeleteVapNextElement().size());
//
//            Actions actions = new Actions(driver);
//
//            await("getBtnConfirmDeleteVapNextElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElement().size() > 0);
//
//            System.out.println(loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElement().size());
//
//            await("getBtnConfirmDeleteVapNextElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElement().get(1).isDisplayed());
//
//            loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElement().get(1).click();


            try {
                await("getBtnConfirmDeleteVapNextElement1 visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> driver.findElement(By.xpath("//div[@class='modal-scrollable']//a[contains(@id, 'confirmDeleteVapNext')]")).isDisplayed());
                //loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElement1().click();

                driver.findElement(By.xpath("//div[@class='modal-scrollable']//a[contains(@id, 'confirmDeleteVapNext')]")).click();
            } catch (Exception e) {
            }

            System.out.println("LOAN DETAILS: DONE");

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ==========MISC FRM APP DTL=================
            stage = "MISC FRM APPDTL";
            DE_MiscFrmAppDtlPage miscFrmAppDtlPage = new DE_MiscFrmAppDtlPage(driver);
            miscFrmAppDtlPage.getTabMiscFrmAppDtlElementByName().click();

            miscFrmAppDtlPage.updateData(miscFrmAppDtlDTO);

            await("getBtnSaveElement end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFrmAppDtlPage._getBtnSaveElement().isEnabled());

            Utilities.captureScreenShot(driver);
            miscFrmAppDtlPage._getBtnSaveElement().click();

            await("getBtnMoveToNextStageElement end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFrmAppDtlPage.getBtnMoveToNextStageElement().isEnabled());

            //tam thoi cho sleep de an notification moi click dc button movetonextstage
            Thread.sleep(15000);

            miscFrmAppDtlPage.getBtnMoveToNextStageElement().click();
            System.out.println("MISC FRM APP DTL: DONE");

            await("Work flow failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Grid"));

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "COMPLETE";
            System.out.println("AUTO OK: user =>" + accountDTO.getUserName());

            //UPDATE STATUS
            application.setStatus("OK");
            application.setDescription("Thanh cong");
            application.setStage(stage);
        } catch (Exception e) {
            //UPDATE STATUS
            application.setStatus("ERROR");
            application.setStage(stage);
            application.setDescription(e.getMessage() + "- toString: " + e.toString());

            System.out.println(stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);

            if (e.getMessage().contains("Work flow failed!!!")) {
                stage = "END OF LEAD DETAIL";
                application.setStage(stage);
                await("Get error fail!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> driver.findElements(By.id("error-message")).size() > 0);

                if (driver.findElements(By.id("error-message")) != null && driver.findElements(By.id("error-message")).size() > 0) {
                    String error = "Error: ";
                    for (WebElement we : driver.findElements(By.id("error-message"))) {
                        error += " - " + we.getText();
                    }
                    application.setError(error);
                    System.out.println(stage + "=>" + error);
                }
            }
        } finally {
            updateStatusRabbit(application, "updateAppError");
            System.out.println(stage);
            logout(driver,accountDTO.getUserName());
        }
    }

    private void updateAppError_Full(WebDriver driver, String stage, LoginDTO accountDTO, String leadAppID, Map<String, Object> mapValue) throws Exception {
        Application application = Application.builder().build();

        try {
            application = (Application) mapValue.get("ApplicationDTO");
            ApplicationInfoDTO applicationInfoDTO = (ApplicationInfoDTO) mapValue.get("ApplicationInfoDTO");
            LoanDetailsDTO loanDetailsDTO = (LoanDetailsDTO) mapValue.get("LoanDetailsDTO");
            LoanDetailsVapDTO loanDetailsVapDTO = (LoanDetailsVapDTO) mapValue.get("LoanDetailsVapDTO");
            List<ReferenceDTO> referenceDTO = (List<ReferenceDTO>) mapValue.get("ReferenceDTO");
            List<DocumentDTO> documentDTOS = (List<DocumentDTO>) mapValue.get("DocumentDTO");
            MiscFptDTO miscFptDTO = (MiscFptDTO) mapValue.get("MiscFptDTO");
            MiscFrmAppDtlDTO miscFrmAppDtlDTO = (MiscFrmAppDtlDTO) mapValue.get("MiscFrmAppDtlDTO");
            MiscVinIdDTO miscVinIdDTO = (MiscVinIdDTO) mapValue.get("MiscVinIdDTO");

            application.setAutomationAcc(accountDTO.getUserName());

            stage = "LOGIN FINONE";
            HashMap<String, String> dataControl = new HashMap<>();
            LoginPage loginPage = new LoginPage(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
            loginPage.clickLogin();

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));
            Utilities.captureScreenShot(driver);

            stage = "HOME PAGE";
            HomePage homePage = new HomePage(driver);

            //DATA INPUT UPDATE
            //String leadAppID="APPL00086527";
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "APPLICATIONS";
//            // ========== APPLICATIONS =================
//            homePage.getMenuApplicationElement().click();
//            homePage.getApplicationElement().click();
//
//            await("Application Grid timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(driver::getTitle, is("Application Grid"));
//
//            ApplicationGridPage applicationGridPage = new ApplicationGridPage(driver);
//            applicationGridPage.updateData(leadAppID);
//            Utilities.captureScreenShot(driver);


            // ========== APPLICATIONS =================
            homePage.getMenuApplicationElement().click();

            // update lai flow assign app moi

//            homePage.getApplicationElement().click();
//
//            await("Application Grid timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(driver::getTitle, is("Application Grid"));
//
//            ApplicationGridPage applicationGridPage=new ApplicationGridPage(driver);
//            applicationGridPage.setData(leadAppID);
//            Utilities.captureScreenShot(driver);
//
//            System.out.println(stage + ": DONE" );
//            Utilities.captureScreenShot(driver);
            stage = "APPLICATION MANAGER";
            // ========== APPLICATION MANAGER =================
            homePage.getApplicationManagerElement().click();
            await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Manager"));

            DE_ApplicationManagerPage de_applicationManagerPage = new DE_ApplicationManagerPage(driver);

            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> de_applicationManagerPage.getApplicationManagerFormElement().isDisplayed());
            de_applicationManagerPage.setData(leadAppID, accountDTO.getUserName());
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "APPLICATION GRID";
            // ========== APPLICATION GRID =================
            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> de_applicationManagerPage.getMenuApplicationElement().isDisplayed());

            de_applicationManagerPage.getMenuApplicationElement().click();
            Utilities.captureScreenShot(driver);
            de_applicationManagerPage.getApplicationElement().click();
            Utilities.captureScreenShot(driver);
            await("Application Grid timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Grid"));
            Utilities.captureScreenShot(driver);
            ApplicationGridPage applicationGridPage = new ApplicationGridPage(driver);
            applicationGridPage.updateData(leadAppID);
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "LEAD DETAILS (DATA ENTRY)";
            // ========== LEAD DETAILS (DATA ENTRY) =================
            LeadDetailDEPage leadDetailDEPage = new LeadDetailDEPage(driver);
            leadDetailDEPage.setData(leadAppID);
            Utilities.captureScreenShot(driver);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "PERSONAL INFORMATION";
            // ========== PERSONAL INFORMATION =================
            DE_ApplicationInfoPage appInfoPage = new DE_ApplicationInfoPage(driver);
            DE_ApplicationInfoPersonalTab personalTab = appInfoPage.getApplicationInfoPersonalTab();

            await("getPersonalCustomerDetailsElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> personalTab.getPersonalCustomerDetailsElement().isDisplayed());

            personalTab.updateValue(applicationInfoDTO);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "EMPLOYMENT DETAILS";
            // ========== EMPLOYMENT DETAILS =================
            await("Load employment details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> appInfoPage.getEmploymentDetailsTabElement().getAttribute("class").contains("active"));

            DE_ApplicationInfoEmploymentDetailsTab employmentDetailsTab = appInfoPage.getApplicationInfoEmploymentDetailsTab();
            await("Span Application Id not enabled Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> employmentDetailsTab.getApplicationId().isEnabled());

            employmentDetailsTab.updateData(applicationInfoDTO.getEmploymentDetails());
            employmentDetailsTab.getDoneBtnElement().click();
            await("Employment Status loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> employmentDetailsTab.getTableAfterDoneElement().isDisplayed());
            employmentDetailsTab.setExperienceInIndustry(applicationInfoDTO.getEmploymentDetails());
            employmentDetailsTab.getSaveAndNextBtnElement().click();

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "FINANCIAL DETAILS";
            // ==========FINANCIAL DETAILS =================
            if (applicationInfoDTO.getIncomeDetails() != null && applicationInfoDTO.getIncomeDetails().size() > 0) {
                DE_ApplicationInfoFinancialDetailsTab financialDetailsTab = appInfoPage.getApplicationInfoFinancialDetailsTab();

                financialDetailsTab.getCustomerMainChildTabs_income_tabElement().click();

                financialDetailsTab.openIncomeDetailSection();
                await("Load financial details - income details Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> financialDetailsTab.getIncomeDetailDivElement().isDisplayed());
                financialDetailsTab.updateIncomeDetailsData(applicationInfoDTO.getIncomeDetails());
                financialDetailsTab.saveAndNext();
            }

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ==========LOAN DETAILS=================
            stage = "LOAN DETAIL - SOURCING DETAIL TAB";
            DE_LoanDetailsPage loanDetailsPage = new DE_LoanDetailsPage(driver);
            Utilities.captureScreenShot(driver);
            loanDetailsPage.getTabLoanDetailsElement().click();

            DE_LoanDetailsSourcingDetailsTab loanDetailsSourcingDetailsTab = new DE_LoanDetailsSourcingDetailsTab(driver);
            await("Load loan details - sourcing details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getTabSourcingDetailsElement().getAttribute("class").contains("active"));
            await("Load loan details - sourcing details container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getSourcingDetailsDivContainerElement().isDisplayed());
            loanDetailsSourcingDetailsTab.updateData(loanDetailsDTO);
            Utilities.captureScreenShot(driver);
            loanDetailsSourcingDetailsTab.getBtnSaveAndNextElement().click();

            //update
//            await("modalConfirmElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> loanDetailsSourcingDetailsTab.getModalConfirmDeleteVapNextElement().size()>0);
//
//            await("modalBtnConfirmElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> loanDetailsSourcingDetailsTab.getModalConfirmDeleteVapNextElement().get(1).findElement(By.id("confirmDeleteVapNext")).isDisplayed());
//
//            System.out.println(loanDetailsSourcingDetailsTab.getModalConfirmDeleteVapNextElement().size());
//
//            Actions actions=new Actions(driver);
//
//            await("getBtnConfirmDeleteVapNextElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> loanDetailsSourcingDetailsTab.getModalConfirmDeleteVapNextElement().size()>0);
//
//            System.out.println(loanDetailsSourcingDetailsTab.getModalConfirmDeleteVapNextElement().size());
//
//            await("getBtnConfirmDeleteVapNextElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> loanDetailsSourcingDetailsTab.getModalConfirmDeleteVapNextElement().get(1).isDisplayed());
//
//
//            System.out.println("BTN :" + loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElement().size());
//
//            //actions.moveToElement(loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElement().get(1)).click().build().perform();
//
//           loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElement().get(1).click();

            //kiem tra da co VAP Hay chua, neu co moi hien popup de close
//            if(driver.findElements(By.xpath("//div[@class='modal-scrollable']//a[contains(@id, 'confirmDeleteVapNext')]")).size()>0) {

            try {

                await("getBtnConfirmDeleteVapNextElement1 visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> driver.findElement(By.xpath("//div[@class='modal-scrollable']//a[contains(@id, 'confirmDeleteVapNext')]")).isDisplayed());
                //loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElement1().click();

                driver.findElement(By.xpath("//div[@class='modal-scrollable']//a[contains(@id, 'confirmDeleteVapNext')]")).click();
            } catch (Exception e) {
                log.info("Vap:" + e.toString());
            }

            //}

            System.out.println("LOAN DETAILS: DONE");

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ========== VAP DETAILS =======================
            if (loanDetailsVapDTO != null && loanDetailsVapDTO.getVapProduct() != null && !loanDetailsVapDTO.getVapProduct().equals("")) {
                stage = "LOAN DETAIL - VAP DETAIL TAB";
                Utilities.captureScreenShot(driver);
                DE_LoanDetailsVapDetailsTab loanDetailsVapDetailsTab = new DE_LoanDetailsVapDetailsTab(driver);

                await("Load loanDetailsVapDetailsTab container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> loanDetailsVapDetailsTab.getVapDetailsDivContainerElement().isDisplayed());

                loanDetailsVapDetailsTab.updateData(loanDetailsVapDTO);
                Utilities.captureScreenShot(driver);
                loanDetailsVapDetailsTab.getBtnSaveAndNextElement().click();

                System.out.println("LOAN DETAILS - VAP: DONE");

            }

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "DOCUMENTS";
            // ==========DOCUMENTS=================
            if (documentDTOS.size() > 0) {
                DE_DocumentsPage documentsPage = new DE_DocumentsPage(driver);
                await("Load document tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> documentsPage.getTabDocumentsElement().isDisplayed() && documentsPage.getTabDocumentsElement().isEnabled());
                documentsPage.getTabDocumentsElement().click();
                await("Load document container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> documentsPage.getDocumentsContainerElement().isDisplayed());
                documentsPage.getBtnGetDocumentElement().click();
                await("Load document table Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> documentsPage.getLendingTrElement().size() > 0);

                documentsPage.updateData(documentDTOS, downdloadFileURL);
                Utilities.captureScreenShot(driver);
                documentsPage.getBtnSubmitElement().click();
            }


            // ==========REFERENCES=================
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "REFERENCES";
            DE_ReferencesPage referencesPage = new DE_ReferencesPage(driver);
            await("Load references tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> referencesPage.getTabReferencesElement().isDisplayed() && referencesPage.getTabReferencesElement().isEnabled());
            referencesPage.getTabReferencesElement().click();

            referencesPage.updateData(referenceDTO);
            Utilities.captureScreenShot(driver);
            referencesPage.getSaveBtnElement().click();

            System.out.println("REFERENCES DETAILS: DONE");

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ==========MISC FRM APP DTL=================
            stage = "MISC FRM APPDTL";
            DE_MiscFrmAppDtlPage miscFrmAppDtlPage = new DE_MiscFrmAppDtlPage(driver);
            miscFrmAppDtlPage.getTabMiscFrmAppDtlElementByName().click();

            miscFrmAppDtlPage.updateData(miscFrmAppDtlDTO);

            await("getBtnSaveElement end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFrmAppDtlPage._getBtnSaveElement().isEnabled());

            Utilities.captureScreenShot(driver);
            miscFrmAppDtlPage._getBtnSaveElement().click();

            await("getBtnMoveToNextStageElement end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFrmAppDtlPage.getBtnMoveToNextStageElement().isEnabled());

            //tam thoi cho sleep de an notification moi click dc button movetonextstage
            Thread.sleep(15000);

            miscFrmAppDtlPage.getBtnMoveToNextStageElement().click();
            System.out.println("MISC FRM APP DTL: DONE");

            await("Work flow failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Grid"));

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "COMPLETE";
            System.out.println("AUTO OK: user =>" + accountDTO.getUserName());


            //UPDATE STATUS
            application.setStatus("OK");
            application.setDescription("Thanh cong");
            application.setStage(stage);
        } catch (Exception e) {
            //UPDATE STATUS
            application.setStatus("ERROR");
            application.setStage(stage);
            application.setDescription(e.getMessage());

            System.out.println(stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);

            if (e.getMessage().contains("Work flow failed!!!")) {
                stage = "END OF LEAD DETAIL";
                application.setStage(stage);
                await("Get error fail!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> driver.findElements(By.id("error-message")).size() > 0);

                if (driver.findElements(By.id("error-message")) != null && driver.findElements(By.id("error-message")).size() > 0) {
                    String error = "Error: ";
                    for (WebElement we : driver.findElements(By.id("error-message"))) {
                        error += " - " + we.getText();
                    }
                    application.setError(error);
                    System.out.println(stage + "=>" + error);
                }
            }
        } finally {
            updateStatusRabbit(application, "updateAppError");
            System.out.println(stage);
            logout(driver,accountDTO.getUserName());
        }
    }

    private void updateAppError_FullAll(WebDriver driver, String stage, LoginDTO accountDTO, String leadAppID, Map<String, Object> mapValue) throws Exception {
        Application application = Application.builder().build();
        try {
            application = (Application) mapValue.get("ApplicationDTO");
            ApplicationInfoDTO applicationInfoDTO = (ApplicationInfoDTO) mapValue.get("ApplicationInfoDTO");
            LoanDetailsDTO loanDetailsDTO = (LoanDetailsDTO) mapValue.get("LoanDetailsDTO");
            LoanDetailsVapDTO loanDetailsVapDTO = (LoanDetailsVapDTO) mapValue.get("LoanDetailsVapDTO");
            List<ReferenceDTO> referenceDTO = (List<ReferenceDTO>) mapValue.get("ReferenceDTO");
            List<DocumentDTO> documentDTOS = (List<DocumentDTO>) mapValue.get("DocumentDTO");
            MiscFptDTO miscFptDTO = (MiscFptDTO) mapValue.get("MiscFptDTO");
            MiscFrmAppDtlDTO miscFrmAppDtlDTO = (MiscFrmAppDtlDTO) mapValue.get("MiscFrmAppDtlDTO");
            MiscVinIdDTO miscVinIdDTO = (MiscVinIdDTO) mapValue.get("MiscVinIdDTO");

            stage = "LOGIN FINONE";
            HashMap<String, String> dataControl = new HashMap<>();
            LoginPage loginPage = new LoginPage(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
            loginPage.clickLogin();

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));
            Utilities.captureScreenShot(driver);

            stage = "HOME PAGE";
            HomePage homePage = new HomePage(driver);

            //DATA INPUT UPDATE
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "APPLICATION MANAGER";
            // ========== APPLICATION MANAGER =================
            homePage.getMenuApplicationElement().click();
            homePage.getApplicationManagerElement().click();
            await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Manager"));

            DE_ApplicationManagerPage de_applicationManagerPage = new DE_ApplicationManagerPage(driver);

            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> de_applicationManagerPage.getApplicationManagerFormElement().isDisplayed());
            de_applicationManagerPage.setData(leadAppID, accountDTO.getUserName());
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "APPLICATION GRID";
            // ========== APPLICATION GRID =================
            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> de_applicationManagerPage.getMenuApplicationElement().isDisplayed());

            de_applicationManagerPage.getMenuApplicationElement().click();
            Utilities.captureScreenShot(driver);
            de_applicationManagerPage.getApplicationElement().click();
            Utilities.captureScreenShot(driver);
            await("Application Grid timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Grid"));
            Utilities.captureScreenShot(driver);
            ApplicationGridPage applicationGridPage = new ApplicationGridPage(driver);
            applicationGridPage.updateData(leadAppID);
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "LEAD DETAILS (DATA ENTRY)";
            // ========== LEAD DETAILS (DATA ENTRY) =================
            LeadDetailDEPage leadDetailDEPage = new LeadDetailDEPage(driver);
            leadDetailDEPage.setData(leadAppID);
            Utilities.captureScreenShot(driver);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "PERSONAL INFORMATION";
            // ========== PERSONAL INFORMATION =================
            DE_ApplicationInfoPage appInfoPage = new DE_ApplicationInfoPage(driver);
            DE_ApplicationInfoPersonalTab personalTab = appInfoPage.getApplicationInfoPersonalTab();

            await("getPersonalCustomerDetailsElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> personalTab.getPersonalCustomerDetailsElement().isDisplayed());

            personalTab.updateValue(applicationInfoDTO);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "EMPLOYMENT DETAILS";
            // ========== EMPLOYMENT DETAILS =================
            await("Load employment details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> appInfoPage.getEmploymentDetailsTabElement().getAttribute("class").contains("active"));

            DE_ApplicationInfoEmploymentDetailsTab employmentDetailsTab = appInfoPage.getApplicationInfoEmploymentDetailsTab();
            await("Span Application Id not enabled Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> employmentDetailsTab.getApplicationId().isEnabled());

            employmentDetailsTab.updateData(applicationInfoDTO.getEmploymentDetails());
            employmentDetailsTab.getDoneBtnElement().click();
            await("Employment Status loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> employmentDetailsTab.getTableAfterDoneElement().isDisplayed());
            employmentDetailsTab.setExperienceInIndustry(applicationInfoDTO.getEmploymentDetails());
            employmentDetailsTab.getSaveAndNextBtnElement().click();

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "FINANCIAL DETAILS";
            // ==========FINANCIAL DETAILS =================
            if (applicationInfoDTO.getIncomeDetails() != null && applicationInfoDTO.getIncomeDetails().size() > 0) {
                DE_ApplicationInfoFinancialDetailsTab financialDetailsTab = appInfoPage.getApplicationInfoFinancialDetailsTab();

                financialDetailsTab.getCustomerMainChildTabs_income_tabElement().click();

                financialDetailsTab.openIncomeDetailSection();
                await("Load financial details - income details Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> financialDetailsTab.getIncomeDetailDivElement().isDisplayed());
                financialDetailsTab.updateIncomeDetailsData(applicationInfoDTO.getIncomeDetails());
                financialDetailsTab.saveAndNext();
            }

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ==========LOAN DETAILS=================
            stage = "LOAN DETAIL - SOURCING DETAIL TAB";
            DE_LoanDetailsPage loanDetailsPage = new DE_LoanDetailsPage(driver);
            Utilities.captureScreenShot(driver);
            loanDetailsPage.getTabLoanDetailsElement().click();

            DE_LoanDetailsSourcingDetailsTab loanDetailsSourcingDetailsTab = new DE_LoanDetailsSourcingDetailsTab(driver);
            await("Load loan details - sourcing details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getTabSourcingDetailsElement().getAttribute("class").contains("active"));
            await("Load loan details - sourcing details container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getSourcingDetailsDivContainerElement().isDisplayed());
            loanDetailsSourcingDetailsTab.updateData(loanDetailsDTO);
            Utilities.captureScreenShot(driver);
            loanDetailsSourcingDetailsTab.getBtnSaveAndNextElement().click();

            //update
//            await("modalConfirmElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> loanDetailsSourcingDetailsTab.getModalConfirmDeleteVapNextElement().size()>0);
//
//            await("modalBtnConfirmElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> loanDetailsSourcingDetailsTab.getModalConfirmDeleteVapNextElement().get(1).findElement(By.id("confirmDeleteVapNext")).isDisplayed());
//
//            System.out.println(loanDetailsSourcingDetailsTab.getModalConfirmDeleteVapNextElement().size());
//
//            Actions actions=new Actions(driver);
//
//            await("getBtnConfirmDeleteVapNextElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> loanDetailsSourcingDetailsTab.getModalConfirmDeleteVapNextElement().size()>0);
//
//            System.out.println(loanDetailsSourcingDetailsTab.getModalConfirmDeleteVapNextElement().size());
//
//            await("getBtnConfirmDeleteVapNextElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> loanDetailsSourcingDetailsTab.getModalConfirmDeleteVapNextElement().get(1).isDisplayed());
//
//
//            System.out.println("BTN :" + loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElement().size());
//
//            //actions.moveToElement(loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElement().get(1)).click().build().perform();
//
//           loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElement().get(1).click();

            //kiem tra da co VAP Hay chua, neu co moi hien popup de close
//            if(driver.findElements(By.xpath("//div[@class='modal-scrollable']//a[contains(@id, 'confirmDeleteVapNext')]")).size()>0) {

            try {

                await("getBtnConfirmDeleteVapNextElement1 visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> driver.findElement(By.xpath("//div[@class='modal-scrollable']//a[contains(@id, 'confirmDeleteVapNext')]")).isDisplayed());
                //loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElement1().click();

                driver.findElement(By.xpath("//div[@class='modal-scrollable']//a[contains(@id, 'confirmDeleteVapNext')]")).click();
            } catch (Exception e) {
                log.info("Vap:" + e.toString());
            }

            //}

            System.out.println("LOAN DETAILS: DONE");

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ========== VAP DETAILS =======================
            if (loanDetailsVapDTO != null && loanDetailsVapDTO.getVapProduct() != null && !loanDetailsVapDTO.getVapProduct().equals("")) {
                stage = "LOAN DETAIL - VAP DETAIL TAB";
                Utilities.captureScreenShot(driver);
                DE_LoanDetailsVapDetailsTab loanDetailsVapDetailsTab = new DE_LoanDetailsVapDetailsTab(driver);

                await("Load loanDetailsVapDetailsTab container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> loanDetailsVapDetailsTab.getVapDetailsDivContainerElement().isDisplayed());

                loanDetailsVapDetailsTab.updateData(loanDetailsVapDTO);
                Utilities.captureScreenShot(driver);
                loanDetailsVapDetailsTab.getBtnSaveAndNextElement().click();

                System.out.println("LOAN DETAILS - VAP: DONE");

            }

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "DOCUMENTS";
            // ==========DOCUMENTS=================
            if (documentDTOS.size() > 0) {
                DE_DocumentsPage documentsPage = new DE_DocumentsPage(driver);
                await("Load document tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> documentsPage.getTabDocumentsElement().isDisplayed() && documentsPage.getTabDocumentsElement().isEnabled());
                documentsPage.getTabDocumentsElement().click();
                await("Load document container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> documentsPage.getDocumentsContainerElement().isDisplayed());
                documentsPage.getBtnGetDocumentElement().click();
                await("Load document table Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> documentsPage.getLendingTrElement().size() > 0);

                documentsPage.updateData(documentDTOS, downdloadFileURL);
                Utilities.captureScreenShot(driver);
                documentsPage.getBtnSubmitElement().click();
            }


            // ==========REFERENCES=================
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "REFERENCES";
            DE_ReferencesPage referencesPage = new DE_ReferencesPage(driver);
            await("Load references tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> referencesPage.getTabReferencesElement().isDisplayed() && referencesPage.getTabReferencesElement().isEnabled());
            referencesPage.getTabReferencesElement().click();

            referencesPage.updateData(referenceDTO);
            Utilities.captureScreenShot(driver);
            referencesPage.getSaveBtnElement().click();

            System.out.println("REFERENCES DETAILS: DONE");

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ==========MISC FRM APP DTL=================
            stage = "MISC FRM APPDTL";
            DE_MiscFrmAppDtlPage miscFrmAppDtlPage = new DE_MiscFrmAppDtlPage(driver);
            miscFrmAppDtlPage.getTabMiscFrmAppDtlElementByName().click();

            miscFrmAppDtlPage.updateData(miscFrmAppDtlDTO);

            await("getBtnSaveElement end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFrmAppDtlPage._getBtnSaveElement().isEnabled());

            Utilities.captureScreenShot(driver);
            miscFrmAppDtlPage._getBtnSaveElement().click();

            await("getBtnMoveToNextStageElement end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFrmAppDtlPage.getBtnMoveToNextStageElement().isEnabled());

            miscFrmAppDtlPage.getBtnMoveToNextStageElement().click();
            System.out.println("MISC FRM APP DTL: DONE");

            await("Work flow failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Grid"));

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "COMPLETE";
            System.out.println("AUTO OK: user =>" + accountDTO.getUserName());


            //UPDATE STATUS
            application.setStatus("OK");
            application.setDescription("Thanh cong");
            application.setStage(stage);
        } catch (Exception e) {
            //UPDATE STATUS
            application.setStatus("ERROR");
            application.setStage(stage);
            application.setDescription(e.getMessage());

            System.out.println(stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);

            if (e.getMessage().contains("Work flow failed!!!")) {
                stage = "END OF LEAD DETAIL";
                application.setStage(stage);
                await("Get error fail!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> driver.findElements(By.id("error-message")).size() > 0);

                if (driver.findElements(By.id("error-message")) != null && driver.findElements(By.id("error-message")).size() > 0) {
                    String error = "Error: ";
                    for (WebElement we : driver.findElements(By.id("error-message"))) {
                        error += " - " + we.getText();
                    }
                    application.setError(error);
                    System.out.println(stage + "=>" + error);
                }
            }
        } finally {
            updateStatusRabbit(application, "updateAppError");
            System.out.println(stage);
            logout(driver,accountDTO.getUserName());
        }
    }

    public void retry_runAutomation_UpdateInfo(Map<String, Object> mapValue) throws Exception {
        SeleniumGridDriver setupTestDriver = new SeleniumGridDriver(null, "chrome", fin1URL, null, seleHost, selePort);
        WebDriver driver = setupTestDriver.getDriver();

        LoginDTO accountDTO = null;
        accountDTO = retry_pollAccountFromQueue("DATAENTRY");

        String stage = "";
        Instant start = Instant.now();
        Application application = Application.builder().build();
        try {


            stage = "INIT DATA";
            //*************************** GET DATA *********************//
            application = (Application) mapValue.get("ApplicationDTO");
            ApplicationInfoDTO applicationInfoDTO = (ApplicationInfoDTO) mapValue.get("ApplicationInfoDTO");
            LoanDetailsDTO loanDetailsDTO = (LoanDetailsDTO) mapValue.get("LoanDetailsDTO");
            LoanDetailsVapDTO loanDetailsVapDTO = (LoanDetailsVapDTO) mapValue.get("LoanDetailsVapDTO");
            List<ReferenceDTO> referenceDTO = (List<ReferenceDTO>) mapValue.get("ReferenceDTO");
            List<DocumentDTO> documentDTOS = (List<DocumentDTO>) mapValue.get("DocumentDTO");
            MiscFptDTO miscFptDTO = (MiscFptDTO) mapValue.get("MiscFptDTO");
            MiscFrmAppDtlDTO miscFrmAppDtlDTO = (MiscFrmAppDtlDTO) mapValue.get("MiscFrmAppDtlDTO");
            MiscVinIdDTO miscVinIdDTO = (MiscVinIdDTO) mapValue.get("MiscVinIdDTO");

            application.setAutomationAcc(accountDTO.getUserName());

            //vinId = loanDetailsDTO.getApplicationNumber();
            //*************************** END GET DATA *********************//

            System.out.println(stage + ": DONE");
            stage = "LOGIN FINONE";
            HashMap<String, String> dataControl = new HashMap<>();
            LoginPage loginPage = new LoginPage(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
            loginPage.clickLogin();

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "HOME PAGE";
            HomePage homePage = new HomePage(driver);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ========== APPLICATIONS =================
            String leadAppID = application.getApplicationId();
            homePage.getMenuApplicationElement().click();

            // update lai flow assign app moi

//            homePage.getApplicationElement().click();
//
//            await("Application Grid timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(driver::getTitle, is("Application Grid"));
//
//            ApplicationGridPage applicationGridPage=new ApplicationGridPage(driver);
//            applicationGridPage.setData(leadAppID);
//            Utilities.captureScreenShot(driver);
//
//            System.out.println(stage + ": DONE" );
//            Utilities.captureScreenShot(driver);
            stage = "APPLICATION MANAGER";
            // ========== APPLICATION MANAGER =================
            homePage.getApplicationManagerElement().click();
            await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Manager"));

            DE_ApplicationManagerPage de_applicationManagerPage = new DE_ApplicationManagerPage(driver);

            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> de_applicationManagerPage.getApplicationManagerFormElement().isDisplayed());
            de_applicationManagerPage.setData(leadAppID, accountDTO.getUserName());
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "APPLICATION GRID";
            // ========== APPLICATION GRID =================
            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> de_applicationManagerPage.getMenuApplicationElement().isDisplayed());

            de_applicationManagerPage.getMenuApplicationElement().click();
            Utilities.captureScreenShot(driver);
            de_applicationManagerPage.getApplicationElement().click();
            Utilities.captureScreenShot(driver);
            await("Application Grid timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Grid"));
            Utilities.captureScreenShot(driver);
            ApplicationGridPage applicationGridPage = new ApplicationGridPage(driver);
            applicationGridPage.updateData(leadAppID);
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "LEAD DETAILS (DATA ENTRY)";
            // ========== LEAD DETAILS (DATA ENTRY) =================
            LeadDetailDEPage leadDetailDEPage = new LeadDetailDEPage(driver);
            leadDetailDEPage.setData(leadAppID);
            Utilities.captureScreenShot(driver);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "PERSONAL INFORMATION";
            // ========== PERSONAL INFORMATION =================
            DE_ApplicationInfoPage appInfoPage = new DE_ApplicationInfoPage(driver);
            DE_ApplicationInfoPersonalTab personalTab = appInfoPage.getApplicationInfoPersonalTab();

            await("getPersonalCustomerDetailsElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> personalTab.getPersonalCustomerDetailsElement().isDisplayed());

            personalTab.setValue(applicationInfoDTO);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "EMPLOYMENT DETAILS";
            // ========== EMPLOYMENT DETAILS =================
            await("Load employment details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> appInfoPage.getEmploymentDetailsTabElement().getAttribute("class").contains("active"));

            DE_ApplicationInfoEmploymentDetailsTab employmentDetailsTab = appInfoPage.getApplicationInfoEmploymentDetailsTab();
            await("Span Application Id not enabled Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> employmentDetailsTab.getApplicationId().isEnabled());

            employmentDetailsTab.setData(applicationInfoDTO.getEmploymentDetails());
            employmentDetailsTab.getDoneBtnElement().click();
            await("Employment Status loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> employmentDetailsTab.getTableAfterDoneElement().isDisplayed());
            employmentDetailsTab.setExperienceInIndustry(applicationInfoDTO.getEmploymentDetails());
            employmentDetailsTab.setMajorOccupation(applicationInfoDTO.getEmploymentDetails());
            employmentDetailsTab.getSaveAndNextBtnElement().click();
            Utilities.captureScreenShot(driver);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "EMPLOYMENT DETAILS - FINANCIAL";
            // ==========FINANCIAL DETAILS =================
            if (applicationInfoDTO.getIncomeDetails() != null && applicationInfoDTO.getIncomeDetails().size() > 0) {
                DE_ApplicationInfoFinancialDetailsTab financialDetailsTab = appInfoPage.getApplicationInfoFinancialDetailsTab();

                financialDetailsTab.openIncomeDetailSection();
                await("Load financial details - income details Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> financialDetailsTab.getIncomeDetailDivElement().isDisplayed());
                financialDetailsTab.setIncomeDetailsData(applicationInfoDTO.getIncomeDetails());
                financialDetailsTab.saveAndNext();
            }

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ==========LOAN DETAILS=================
            stage = "LOAN DETAIL PAGE - SOURCING DETAIL TAB";
            DE_LoanDetailsPage loanDetailsPage = new DE_LoanDetailsPage(driver);
            Utilities.captureScreenShot(driver);
            loanDetailsPage.getTabLoanDetailsElement().click();

            DE_LoanDetailsSourcingDetailsTab loanDetailsSourcingDetailsTab = new DE_LoanDetailsSourcingDetailsTab(driver);
            await("Load loan details - sourcing details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getTabSourcingDetailsElement().getAttribute("class").contains("active"));
            await("Load loan details - sourcing details container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getSourcingDetailsDivContainerElement().isDisplayed());
            loanDetailsSourcingDetailsTab.setData(loanDetailsDTO);
            Utilities.captureScreenShot(driver);
            loanDetailsSourcingDetailsTab.getBtnSaveAndNextElement().click();

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ==========VAP DETAILS=======================
            if (loanDetailsVapDTO != null && loanDetailsVapDTO.getVapProduct() != null && !loanDetailsVapDTO.getVapProduct().equals("")) {
                stage = "LOAN DETAIL PAGE - VAP DETAIL TAB";
                Utilities.captureScreenShot(driver);
                DE_LoanDetailsVapDetailsTab loanDetailsVapDetailsTab = new DE_LoanDetailsVapDetailsTab(driver);

                await("Load loan details - sourcing details container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> loanDetailsVapDetailsTab.getVapDetailsDivContainerElement().isDisplayed());
                loanDetailsVapDetailsTab.setData(loanDetailsVapDTO);
                Utilities.captureScreenShot(driver);
                loanDetailsVapDetailsTab.getBtnSaveAndNextElement().click();

                System.out.println("LOAN DETAILS - VAP: DONE");

            }

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "DOCUMENTS";
            // ==========DOCUMENTS=================
            if (documentDTOS.size() > 0) {
                DE_DocumentsPage documentsPage = new DE_DocumentsPage(driver);
                await("Load document tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> documentsPage.getTabDocumentsElement().isDisplayed() && documentsPage.getTabDocumentsElement().isEnabled());
                documentsPage.getTabDocumentsElement().click();
                await("Load document container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> documentsPage.getDocumentsContainerElement().isDisplayed());
                documentsPage.getBtnGetDocumentElement().click();
                await("Load document table Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> documentsPage.getLendingTrElement().size() > 0);

                documentsPage.updateData(documentDTOS, downdloadFileURL);
                Utilities.captureScreenShot(driver);
                documentsPage.getBtnSubmitElement().click();
            }

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "REFERENCES";
            // ==========REFERENCES=================
            stage = "REFERENCES PAGE";
            DE_ReferencesPage referencesPage = new DE_ReferencesPage(driver);
            await("Load references tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> referencesPage.getTabReferencesElement().isDisplayed() && referencesPage.getTabReferencesElement().isEnabled());
            referencesPage.getTabReferencesElement().click();
            referencesPage.setData(referenceDTO);
            referencesPage.getSaveBtnElement().click();

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ==========MISC FRM APP DTL=================
            stage = "MISC FRM APPDTL PAGE";
            DE_MiscFrmAppDtlPage miscFrmAppDtlPage = new DE_MiscFrmAppDtlPage(driver);
            miscFrmAppDtlPage.getTabMiscFrmAppDtlElementByName().click();
            miscFrmAppDtlPage.setData(miscFrmAppDtlDTO);
            Utilities.captureScreenShot(driver);
            await("getBtnSaveElement end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFrmAppDtlPage._getBtnSaveElement().isEnabled());
            miscFrmAppDtlPage._getBtnSaveElement().click();
            Utilities.captureScreenShot(driver);
            await("getBtnMoveToNextStageElement end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFrmAppDtlPage.getBtnMoveToNextStageElement().isEnabled());
            Utilities.captureScreenShot(driver);

            //tam thoi cho sleep de an notification moi click dc button movetonextstage
            Thread.sleep(15000);

            miscFrmAppDtlPage.getBtnMoveToNextStageElement().click();
            Utilities.captureScreenShot(driver);
            System.out.println("MISC FRM APP DTL: DONE");

            await("Work flow failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Grid"));

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "COMPLETE";
            System.out.println("AUTO OK: user =>" + accountDTO.getUserName());

            //UPDATE STATUS
            application.setStatus("OK");
            application.setDescription("Retry Thanh cong");

            //logout(driver);

        } catch (Exception e) {
            //UPDATE STATUS
            application.setStatus("ERROR");
            application.setStage(stage);
            application.setDescription("Retry:" + e.getMessage());

            System.out.println(stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);

            if (e.getMessage().contains("Work flow failed!!!")) {
                stage = "END OF LEAD DETAIL";
                application.setStage(stage);
                await("Get error fail!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> driver.findElements(By.id("error-message")).size() > 0);

                if (driver.findElements(By.id("error-message")) != null && driver.findElements(By.id("error-message")).size() > 0) {
                    String error = "Error: ";
                    for (WebElement we : driver.findElements(By.id("error-message"))) {
                        error += " - " + we.getText();
                    }
                    application.setError(error);
                    System.out.println(stage + "=>" + error);
                }
            }
        } finally {
            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            logout(driver,accountDTO.getUserName());
            updateStatusRabbit(application, "updateFullApp");
            pushAccountToQueue(accountDTO, "DATAENTRY");
            if (driver != null) {
                driver.close();
                driver.quit();
            }
        }
    }

    //------------------------ END DATA ENTRY -------------------------------------------

    //------------------------ MOMO -----------------------------------------------------

    public void runAutomation_momoCreateApp(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO) throws Exception {
        String stage = "";
        Application application = Application.builder().build();

        try {
            stage = "INIT DATA";
            String appId = "";
            //*************************** GET DATA *********************//
            application = (Application) mapValue.get("ApplicationDTO");
            ApplicationInfoDTO applicationInfoDTO = (ApplicationInfoDTO) mapValue.get("ApplicationInfoDTO");
            LoanDetailsDTO loanDetailsDTO = (LoanDetailsDTO) mapValue.get("LoanDetailsDTO");
            LoanDetailsVapDTO loanDetailsVapDTO = (LoanDetailsVapDTO) mapValue.get("LoanDetailsVapDTO");
            List<ReferenceDTO> referenceDTO = (List<ReferenceDTO>) mapValue.get("ReferenceDTO");
            List<DocumentDTO> documentDTOS = (List<DocumentDTO>) mapValue.get("DocumentDTO");
            MiscFrmAppDtlDTO miscFrmAppDtlDTO = (MiscFrmAppDtlDTO) mapValue.get("MiscFrmAppDtlDTO");
            //*************************** END GET DATA *********************//

            System.out.println(stage + ": DONE");
            stage = "LOGIN FINONE";
            HashMap<String, String> dataControl = new HashMap<>();
            LoginPage loginPage = new LoginPage(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
            loginPage.clickLogin();

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "HOME PAGE";
            HomePage homePage = new HomePage(driver);
            homePage.menuClick();
            homePage.menuChildClick();

            await("Creation Misc2WLPage timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Create Application"));
            LD_ApplicationInfoPage appInfoPage = new LD_ApplicationInfoPage(driver);

            //========== PERSONAL INFORMATION=================
            stage = "APPLICATION INFORMATION PAGE - PERSONAL INFORMATION";
            LD_ApplicationInfoPersonalTab personalTab = appInfoPage.getApplicationInfoPersonalTab();
            personalTab.btnCreateClick();

            personalTab.setValue(applicationInfoDTO.getGender(), applicationInfoDTO.getFirstName(),
                    applicationInfoDTO.getMiddleName(), applicationInfoDTO.getLastName(), applicationInfoDTO.getDateOfBirth(),
                    applicationInfoDTO.getPlaceOfIssue(), applicationInfoDTO.getMaritalStatus(), applicationInfoDTO.getNational(), applicationInfoDTO.getEducation());
            Utilities.captureScreenShot(driver);
            personalTab.btnPersonInfoProcessClick();

            System.out.println("PERSONAL BASIC: DONE");

            // personal infomation tab - handle identification section
            stage = "APPLICATION INFORMATION PAGE - IDENTIFICATION";
            personalTab.loadIdentificationSection();
            await("Load Identification Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> personalTab.getIdentificationDivElement().isDisplayed());
            personalTab.setIdentificationValue(applicationInfoDTO.getIdentification());
            Utilities.captureScreenShot(driver);
            System.out.println("PERSONAL IDENTIFICATION: DONE");

            // personal infomation tab - handle address section
            stage = "APPLICATION INFORMATION PAGE - ADDRESS";
            personalTab.loadAddressSection();
            await("Load Identification Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> personalTab.getAddressDivElement().isDisplayed());

            personalTab.setAddressValue(applicationInfoDTO.getAddress());
            personalTab.loadAddressSection(); // close section after complete input

            Utilities.captureScreenShot(driver);
            System.out.println("PERSONAL ADDRESS: DONE");

            // personal infomation tab - handle family
            if (applicationInfoDTO.getFamily().size() > 0) {
                stage = "APPLICATION INFORMATION PAGE - FAMILY";

                personalTab.loadFamilySection();
                await("Load Family Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> personalTab.getFamilyDivElement().isDisplayed());
                personalTab.setFamilyValue(applicationInfoDTO.getFamily());
                Utilities.captureScreenShot(driver);
                System.out.println("PERSONAL FAMILY: DONE");
            }

            // personal infomation tab- handle communication details
            stage = "APPLICATION INFORMATION PAGE - COMMUNICATION";
            personalTab.loadCommunicationSection();
            await("Load Communication details Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> personalTab.getCommunicationDetailDivElement().isDisplayed());
            personalTab.selectPrimaryAddress(0);
            await("emailPrimary loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> personalTab.getPrimaryEmailElement().isDisplayed());
            personalTab.getPrimaryEmailElement().sendKeys(applicationInfoDTO.getEmail());
            Utilities.captureScreenShot(driver);
            personalTab.loadCommunicationSection(); // close section after complete input
            System.out.println("PERSONAL COMMUNICATION: DONE");

            // personal infomation tab - button check duplicate
            stage = "APPLICATION INFORMATION PAGE - CHECK DUPLICATE";
            await("Button check address duplicate not enabled").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> personalTab.getBtnCheckDuplicateElement().isEnabled());

            personalTab.getBtnCheckDuplicateElement().click();
            await("Button check address duplicate not enabled").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> StringUtils.isNotEmpty(personalTab.getNumDuplicateElement().getText()));
            Utilities.captureScreenShot(driver);
            System.out.println("PERSONAL CHECK DUPLICATE: DONE");
            Utilities.captureScreenShot(driver);
            personalTab.saveAndNext();
            System.out.println("PERSONAL INFORMATION: DONE");

            // == employment details tab ==
            stage = "APPLICATION INFORMATION PAGE - EMPLOYMENT DETAIL TAB";
            await("Load employment details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> appInfoPage.getEmploymentDetailsTabElement().getAttribute("class").contains("active"));
            LD_ApplicationInfoEmploymentDetailsTab employmentDetailsTab = appInfoPage.getApplicationInfoEmploymentDetailsTab();

            //print application Id
            await("Span Application Id not enabled Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> employmentDetailsTab.getApplicationId().isEnabled());
            appId = employmentDetailsTab.getApplicationId().getText();
            System.out.println("APPID==>" + appId);
            System.out.println("PERSONAL: DONE");

            application.setApplicationId(appId);

            Utilities.captureScreenShot(driver);
            //--------------------END-----------------------
            employmentDetailsTab.setData(applicationInfoDTO.getEmploymentDetails());
            Utilities.captureScreenShot(driver);
            employmentDetailsTab.getDoneBtnElement().click();
            await("Employment Status loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> employmentDetailsTab.getTableAfterDoneElement().isDisplayed());
            Utilities.captureScreenShot(driver);
            employmentDetailsTab.getSaveAndNextBtnElement().click();
            System.out.println("EMPLOYTMENT DETAILS TAB: DONE");

            // == financial details tab ==
            if (applicationInfoDTO.getIncomeDetails() != null && applicationInfoDTO.getIncomeDetails().size() > 0) {
                stage = "APPLICATION INFORMATION PAGE - FINANCIAL TAB";
                LD_ApplicationInfoFinancialDetailsTab financialDetailsTab = appInfoPage.getApplicationInfoFinancialDetailsTab();
                financialDetailsTab.openIncomeDetailSection();
                await("Load financial details - income details Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> financialDetailsTab.getIncomeDetailDivElement().isDisplayed());
                financialDetailsTab.setIncomeDetailsData(applicationInfoDTO.getIncomeDetails());
                Utilities.captureScreenShot(driver);
                financialDetailsTab.saveAndNext();

                System.out.println("EMPLOYTMENT DETAILS TAB - INCOME: DONE");
            }
            System.out.println("PERSONAL EMPLOYEE DETAILS: DONE");

            // ==========LOAN DETAILS=================
            stage = "LOAN DETAIL PAGE - SOURCING DETAIL TAB";
            LD_LoanDetailsPage loanDetailsPage = new LD_LoanDetailsPage(driver);
            Utilities.captureScreenShot(driver);
            loanDetailsPage.getTabLoanDetailsElement().click();


            LoanDetailsSourcingDetailsTab loanDetailsSourcingDetailsTab = new LoanDetailsSourcingDetailsTab(driver);
            await("Load loan details - sourcing details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getTabSourcingDetailsElement().getAttribute("class").contains("active"));
            await("Load loan details - sourcing details container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getSourcingDetailsDivContainerElement().isDisplayed());
            loanDetailsSourcingDetailsTab.setData(loanDetailsDTO);
            Utilities.captureScreenShot(driver);
            loanDetailsSourcingDetailsTab.getBtnSaveAndNextElement().click();
            System.out.println("LOAN DETAILS: DONE");

            // ==========VAP=======================
            if (loanDetailsVapDTO != null && loanDetailsVapDTO.getVapProduct() != null && !loanDetailsVapDTO.getVapProduct().equals("")) {
                stage = "LOAN DETAIL PAGE - VAP DETAIL TAB";
                Utilities.captureScreenShot(driver);
                LD_LoanDetailsVapDetailsTab loanDetailsVapDetailsTab = new LD_LoanDetailsVapDetailsTab(driver);
                await("Load loan details - vap details container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> loanDetailsVapDetailsTab.getTabVapDetailsElement().getAttribute("class").contains("active"));
                await("Load loan details - sourcing details container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> loanDetailsVapDetailsTab.getVapDetailsDivContainerElement().isDisplayed());
                loanDetailsVapDetailsTab.setData(loanDetailsVapDTO);
                Utilities.captureScreenShot(driver);
                loanDetailsVapDetailsTab.getBtnSaveAndNextElement().click();

                System.out.println("LOAN DETAILS - VAP: DONE");

            }


            // ==========REFERENCES=================
            stage = "REFERENCES PAGE";
            LD_ReferencesPage referencesPage = new LD_ReferencesPage(driver);
            await("Load references tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> referencesPage.getTabReferencesElement().isDisplayed() && referencesPage.getTabReferencesElement().isEnabled());
            referencesPage.getTabReferencesElement().click();
            referencesPage.setData(referenceDTO);
            Utilities.captureScreenShot(driver);
            referencesPage.getSaveBtnElement().click();

            System.out.println("REFERENCES DETAILS: DONE");


            // ==========MISC FRM APP DTL=================
            stage = "MISC FRM APPDTL PAGE";
            LD_MiscFrmAppDtlPage miscFrmAppDtlPage = new LD_MiscFrmAppDtlPage(driver);
            miscFrmAppDtlPage.getTabMiscFrmAppDtlElementByName().click();
            miscFrmAppDtlPage.setData(miscFrmAppDtlDTO);

            await("getBtnSaveElement end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFrmAppDtlPage._getBtnSaveElement().isEnabled());

            Utilities.captureScreenShot(driver);
            miscFrmAppDtlPage._getBtnSaveElement().click();

            await("getBtnMoveToNextStageElement end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFrmAppDtlPage.getBtnMoveToNextStageElement().isEnabled());

            Utilities.captureScreenShot(driver);

            miscFrmAppDtlPage.getBtnMoveToNextStageElement().click();
            System.out.println("MISC FRM APP DTL: DONE");

            await("Work flow failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Grid"));

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "COMPLETE";
            System.out.println("AUTO OK: user =>" + accountDTO.getUserName());

            //UPDATE STATUS
            application.setStatus("Pass");
            application.setDescription("Thanh cong");

            //logout(driver);

        } catch (Exception e) {
            //UPDATE STATUS
            application.setStatus("ERROR - " + stage);
            application.setStage(stage);
            application.setDescription(e.toString());

            System.out.println(stage + "=> MESSAGE: " + e.toString() + "\n TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);

            if (e.getMessage().contains("Work flow failed!!!")) {
                stage = "END OF LEAD DETAIL";

                await("Get error fail!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> driver.findElements(By.id("error-message")).size() > 0);

                if (driver.findElements(By.id("error-message")) != null && driver.findElements(By.id("error-message")).size() > 0) {
                    String error = "Error: ";
                    for (WebElement we : driver.findElements(By.id("error-message"))) {
                        error += " - " + we.getText();
                    }
                    application.setError(error);
                    System.out.println(stage + "=>" + error);
                }
            }
        } finally {
            if (application.getApplicationId() == null || application.getApplicationId().isEmpty()) {
                application.setApplicationId("UNKNOW");
            }

            logout(driver,accountDTO.getUserName());
            LD_updateStatusRabbit(application,"updateAutomation","momo");

        }
    }

    private void LD_updateStatusRabbit(Application application, String func, String project) throws Exception {
//        JsonNode jsonNode= rabbitMQService.sendAndReceive("tpf-service-esb",
//                Map.of("func", "updateAutomation", "token",
//                        String.format("Bearer %s", rabbitMQService.getToken().path("access_token").asText()),"body", Map.of("app_id",application.getApplicationId()!=null ? application.getApplicationId():"",
//                                "project",project,
//                                "automation_result",application.getStatus(),
//                                "reference_id",UUID.randomUUID().toString(),
//                                "description",application.getDescription()!=null?application.getDescription():"",
//                                "transaction_id", application.getLoanDetails().getSourcingDetails().getChassisApplicationNum())));

        JsonNode jsonNode = rabbitMQService.sendAndReceive("tpf-service-esb",
                Map.of("func", "updateAutomation", "reference_id", application.getReference_id(), "body", Map.of("app_id", application.getApplicationId() != null ? application.getApplicationId() : "",
                        "project", project,
                        "automation_result", application.getStatus(),
                        "description", application.getDescription() != null ? application.getDescription() : "",
                        "transaction_id", application.getLoanDetails().getSourcingDetails().getChassisApplicationNum())));
        System.out.println("rabit:=>" + jsonNode.toString());

    }

    //------------------------ END MOMO -------------------------------------------------

    //------------------------ FPT -----------------------------------------------------
    public void runAutomation_fptCreateApp(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO) throws Exception {
        String stage = "";
        Application application = Application.builder().build();

        try {
            stage = "INIT DATA";
            String appId = "";
            //*************************** GET DATA *********************//
            application = (Application) mapValue.get("ApplicationDTO");
            ApplicationInfoDTO applicationInfoDTO = (ApplicationInfoDTO) mapValue.get("ApplicationInfoDTO");
            LoanDetailsDTO loanDetailsDTO = (LoanDetailsDTO) mapValue.get("LoanDetailsDTO");
            LoanDetailsVapDTO loanDetailsVapDTO = (LoanDetailsVapDTO) mapValue.get("LoanDetailsVapDTO");
            List<ReferenceDTO> referenceDTO = (List<ReferenceDTO>) mapValue.get("ReferenceDTO");
            List<DocumentDTO> documentDTOS = (List<DocumentDTO>) mapValue.get("DocumentDTO");
            MiscFrmAppDtlDTO miscFrmAppDtlDTO = (MiscFrmAppDtlDTO) mapValue.get("MiscFrmAppDtlDTO");
            MiscFptDTO miscFptDTO = (MiscFptDTO) mapValue.get("MiscFptDTO");
            //*************************** END GET DATA *********************//

            System.out.println(stage + ": DONE");
            stage = "LOGIN FINONE";
            HashMap<String, String> dataControl = new HashMap<>();
            LoginPage loginPage = new LoginPage(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
            loginPage.clickLogin();

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "HOME PAGE";
            HomePage homePage = new HomePage(driver);
            homePage.menuClick();
            homePage.menuChildClick();

            await("Creation Misc2WLPage timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Create Application"));
            LD_ApplicationInfoPage appInfoPage = new LD_ApplicationInfoPage(driver);

            //========== PERSONAL INFORMATION=================
            stage = "APPLICATION INFORMATION PAGE - PERSONAL INFORMATION";
            LD_ApplicationInfoPersonalTab personalTab = appInfoPage.getApplicationInfoPersonalTab();
            personalTab.btnCreateClick();

            personalTab.setValue(applicationInfoDTO.getGender(), applicationInfoDTO.getFirstName(),
                    applicationInfoDTO.getMiddleName(), applicationInfoDTO.getLastName(), applicationInfoDTO.getDateOfBirth(),
                    applicationInfoDTO.getPlaceOfIssue(), applicationInfoDTO.getMaritalStatus(), applicationInfoDTO.getNational(), applicationInfoDTO.getEducation());
            Utilities.captureScreenShot(driver);
            personalTab.btnPersonInfoProcessClick();

            System.out.println("PERSONAL BASIC: DONE");

            // personal infomation tab - handle identification section
            stage = "APPLICATION INFORMATION PAGE - IDENTIFICATION";
            personalTab.loadIdentificationSection();
            await("Load Identification Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> personalTab.getIdentificationDivElement().isDisplayed());
            personalTab.setIdentificationValue(applicationInfoDTO.getIdentification());
            Utilities.captureScreenShot(driver);
            System.out.println("PERSONAL IDENTIFICATION: DONE");

            // personal infomation tab - handle address section
            stage = "APPLICATION INFORMATION PAGE - ADDRESS";
            personalTab.loadAddressSection();
            await("Load Identification Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> personalTab.getAddressDivElement().isDisplayed());

            personalTab.setAddressValue(applicationInfoDTO.getAddress());
            personalTab.loadAddressSection(); // close section after complete input

            Utilities.captureScreenShot(driver);
            System.out.println("PERSONAL ADDRESS: DONE");

            // personal infomation tab - handle family
            if (applicationInfoDTO.getFamily().size() > 0) {
                stage = "APPLICATION INFORMATION PAGE - FAMILY";

                personalTab.loadFamilySection();
                await("Load Family Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> personalTab.getFamilyDivElement().isDisplayed());
                personalTab.setFamilyValue(applicationInfoDTO.getFamily());
                Utilities.captureScreenShot(driver);
                System.out.println("PERSONAL FAMILY: DONE");
            }

            // personal infomation tab- handle communication details
            stage = "APPLICATION INFORMATION PAGE - COMMUNICATION";
            personalTab.loadCommunicationSection();
            await("Load Communication details Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> personalTab.getCommunicationDetailDivElement().isDisplayed());
            personalTab.selectPrimaryAddress(0);
            await("emailPrimary loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> personalTab.getPrimaryEmailElement().isDisplayed());
            personalTab.getPrimaryEmailElement().sendKeys(applicationInfoDTO.getEmail());
            Utilities.captureScreenShot(driver);
            personalTab.loadCommunicationSection(); // close section after complete input
            System.out.println("PERSONAL COMMUNICATION: DONE");

            // personal infomation tab - button check duplicate
            stage = "APPLICATION INFORMATION PAGE - CHECK DUPLICATE";
            await("Button check address duplicate not enabled").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> personalTab.getBtnCheckDuplicateElement().isEnabled());

            personalTab.getBtnCheckDuplicateElement().click();
            await("Button check address duplicate not enabled").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> StringUtils.isNotEmpty(personalTab.getNumDuplicateElement().getText()));
            Utilities.captureScreenShot(driver);
            System.out.println("PERSONAL CHECK DUPLICATE: DONE");
            Utilities.captureScreenShot(driver);
            personalTab.saveAndNext();
            System.out.println("PERSONAL INFORMATION: DONE");
            Utilities.captureScreenShot(driver);

            // == employment details tab ==
            stage = "APPLICATION INFORMATION PAGE - EMPLOYMENT DETAIL TAB";
            appInfoPage.getEmploymentDetailsTabElement().click();

            await("Load employment details tab Timeout!").atMost(60, TimeUnit.SECONDS)
                    .until(() -> appInfoPage.getEmploymentDetailsTabElement().getAttribute("class").contains("active"));
            LD_ApplicationInfoEmploymentDetailsTab employmentDetailsTab = appInfoPage.getApplicationInfoEmploymentDetailsTab();

            //print application Id
            await("Span Application Id not enabled Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> employmentDetailsTab.getApplicationId().isEnabled());
            appId = employmentDetailsTab.getApplicationId().getText();
            System.out.println("APPID==>" + appId);
            System.out.println("PERSONAL: DONE");

            application.setApplicationId(appId);

            Utilities.captureScreenShot(driver);
            //--------------------END-----------------------
            employmentDetailsTab.setData(applicationInfoDTO.getEmploymentDetails());
            Utilities.captureScreenShot(driver);
            employmentDetailsTab.getDoneBtnElement().click();
            await("Employment Status loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> employmentDetailsTab.getTableAfterDoneElement().isDisplayed());
            Utilities.captureScreenShot(driver);
            employmentDetailsTab.getSaveAndNextBtnElement().click();
            System.out.println("EMPLOYTMENT DETAILS TAB: DONE");

            // == financial details tab ==
            if (applicationInfoDTO.getIncomeDetails() != null && applicationInfoDTO.getIncomeDetails().size() > 0) {
                stage = "APPLICATION INFORMATION PAGE - FINANCIAL TAB";
                LD_ApplicationInfoFinancialDetailsTab financialDetailsTab = appInfoPage.getApplicationInfoFinancialDetailsTab();
                financialDetailsTab.openIncomeDetailSection();
                await("Load financial details - income details Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> financialDetailsTab.getIncomeDetailDivElement().isDisplayed());
                financialDetailsTab.setIncomeDetailsData(applicationInfoDTO.getIncomeDetails());
                Utilities.captureScreenShot(driver);
                financialDetailsTab.saveAndNext();

                System.out.println("EMPLOYTMENT DETAILS TAB - INCOME: DONE");
            }
            System.out.println("PERSONAL EMPLOYEE DETAILS: DONE");

            // ==========LOAN DETAILS=================
            stage = "LOAN DETAIL PAGE - SOURCING DETAIL TAB";
            LD_LoanDetailsPage loanDetailsPage = new LD_LoanDetailsPage(driver);
            Utilities.captureScreenShot(driver);
            loanDetailsPage.getTabLoanDetailsElement().click();


            LoanDetailsSourcingDetailsTab loanDetailsSourcingDetailsTab = new LoanDetailsSourcingDetailsTab(driver);
            await("Load loan details - sourcing details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getTabSourcingDetailsElement().getAttribute("class").contains("active"));
            await("Load loan details - sourcing details container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getSourcingDetailsDivContainerElement().isDisplayed());
            loanDetailsSourcingDetailsTab.setData(loanDetailsDTO);
            Utilities.captureScreenShot(driver);
            loanDetailsSourcingDetailsTab.getBtnSaveAndNextElement().click();
            System.out.println("LOAN DETAILS: DONE");

            // ==========VAP=======================
            if (loanDetailsVapDTO != null && loanDetailsVapDTO.getVapProduct() != null && !loanDetailsVapDTO.getVapProduct().equals("")) {
                stage = "LOAN DETAIL PAGE - VAP DETAIL TAB";
                Utilities.captureScreenShot(driver);
                LD_LoanDetailsVapDetailsTab loanDetailsVapDetailsTab = new LD_LoanDetailsVapDetailsTab(driver);
                await("Load loan details - vap details container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> loanDetailsVapDetailsTab.getTabVapDetailsElement().getAttribute("class").contains("active"));
                await("Load loan details - sourcing details container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> loanDetailsVapDetailsTab.getVapDetailsDivContainerElement().isDisplayed());
                loanDetailsVapDetailsTab.setData(loanDetailsVapDTO);
                Utilities.captureScreenShot(driver);
                loanDetailsVapDetailsTab.getBtnSaveAndNextElement().click();

                System.out.println("LOAN DETAILS - VAP: DONE");

            }

            // ==========REFERENCES=================
            stage = "REFERENCES PAGE";
            LD_ReferencesPage referencesPage = new LD_ReferencesPage(driver);
            await("Load references tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> referencesPage.getTabReferencesElement().isDisplayed() && referencesPage.getTabReferencesElement().isEnabled());
            referencesPage.getTabReferencesElement().click();
            referencesPage.setData(referenceDTO);
            Utilities.captureScreenShot(driver);
            referencesPage.getSaveBtnElement().click();

            System.out.println("REFERENCES DETAILS: DONE");

            // ==========MISC FRM FPT=====================
            stage = "MISC FRM FPT";
            LD_MiscFptPage miscFptPage = new LD_MiscFptPage(driver);
            miscFptPage.getTabMiscFptElement().click();
            await("MISC FPT container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFptPage.getTabMiscFptContainerElement().isDisplayed());
            miscFptPage.setData(miscFptDTO);
            Utilities.captureScreenShot(driver);
            miscFptPage.getBtnSaveElement().click();

            System.out.println("MISC FRM FPT: DONE");

            // ==========MISC FRM APP DTL=================
            stage = "MISC FRM APPDTL PAGE";
            LD_MiscFrmAppDtlPage miscFrmAppDtlPage = new LD_MiscFrmAppDtlPage(driver);
            miscFrmAppDtlPage.getTabMiscFrmAppDtlElementByName().click();
            miscFrmAppDtlPage.setData(miscFrmAppDtlDTO);

            await("getBtnSaveElement end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFrmAppDtlPage._getBtnSaveElement().isEnabled());

            Utilities.captureScreenShot(driver);
            miscFrmAppDtlPage._getBtnSaveElement().click();

            await("getBtnMoveToNextStageElement end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFrmAppDtlPage.getBtnMoveToNextStageElement().isEnabled());

            Utilities.captureScreenShot(driver);

            miscFrmAppDtlPage.getBtnMoveToNextStageElement().click();
            System.out.println("MISC FRM APP DTL: DONE");

            await("Work flow failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Grid"));

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "COMPLETE";
            System.out.println("AUTO OK: user =>" + accountDTO.getUserName());

            //UPDATE STATUS
            application.setStatus("Pass");
            application.setDescription("Thanh cong");

            //logout(driver);

        } catch (Exception e) {
            //UPDATE STATUS
            application.setStatus("ERROR - " + stage);
            application.setStage(stage);
            application.setDescription(e.toString());

            System.out.println(stage + "=> MESSAGE: " + e.toString() + "\n TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);

            if (e.getMessage().contains("Work flow failed!!!")) {
                stage = "END OF LEAD DETAIL";

                await("Get error fail!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> driver.findElements(By.id("error-message")).size() > 0);

                if (driver.findElements(By.id("error-message")) != null && driver.findElements(By.id("error-message")).size() > 0) {
                    String error = "Error: ";
                    for (WebElement we : driver.findElements(By.id("error-message"))) {
                        error += " - " + we.getText();
                    }
                    application.setError(error);
                    System.out.println(stage + "=>" + error);
                }
            }
        } finally {

            logout(driver,accountDTO.getUserName());
            LD_updateStatusRabbit(application,"updateAutomation","fpt");

        }
    }
    //------------------------ END FPT -----------------------------------------------------

    //------------------------ AUTO ASSIGN -----------------------------------------------------
    public void runAutomationDE_autoAssign(WebDriver driver, Map<String, Object> mapValue, String project, String browser) throws Exception {
        String stage = "";
        try {
            stage = "INIT DATA";
            //*************************** GET DATA *********************//
            List<AutoAssignDTO> autoAssignDTOList = (List<AutoAssignDTO>) mapValue.get("AutoAssignList");
            //*************************** END GET DATA *********************//
            List<LoginDTO> loginDTOList = new ArrayList<LoginDTO>();

            LoginDTO accountDTONew = null;
            do {
                //get list account finone available
                Query query = new Query();
                query.addCriteria(Criteria.where("active").is(0).and("project").is(project));
                AccountFinOneDTO accountFinOneDTO = mongoTemplate.findOne(query, AccountFinOneDTO.class);
                if (!Objects.isNull(accountFinOneDTO)) {
                    accountDTONew = new LoginDTO().builder().userName(accountFinOneDTO.getUsername()).password(accountFinOneDTO.getPassword()).build();

                    Query queryUpdate = new Query();
                    queryUpdate.addCriteria(Criteria.where("active").is(0).and("username").is(accountFinOneDTO.getUsername()).and("project").is(project));
                    Update update = new Update();
                    update.set("active", 1);
                    AccountFinOneDTO resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, AccountFinOneDTO.class);

                    if (resultUpdate == null) {
                        Thread.sleep(Constant.WAIT_ACCOUNT_GET_NULL);
                        accountDTONew = null;
                    } else {
                        loginDTOList.add(accountDTONew);
                        System.out.println("Get it:" + accountDTONew.toString());
                    }
                } else
                    accountDTONew = null;
            } while (!Objects.isNull(accountDTONew));

            //insert data
            mongoTemplate.insert(autoAssignDTOList, AutoAssignDTO.class);

            if (loginDTOList.size() > 0) {
                ExecutorService workerThreadPoolDE = Executors.newFixedThreadPool(loginDTOList.size());

                for (LoginDTO loginDTO : loginDTOList) {
                    workerThreadPoolDE.execute(new Runnable() {
                        @Override
                        public void run() {
//                            runAutomationDE_autoAssign_run(loginDTO, browser);
                            runAutomationDE_autoAssign_run(loginDTO, browser, project);
                        }
                    });
                }

            }
        } catch (Exception e) {
            System.out.println(stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();
            Utilities.captureScreenShot(driver);
        }
    }

    private void runAutomationDE_autoAssign_run(LoginDTO accountDTO, String browser, String project) {
        WebDriver driver = null;
        Instant start = Instant.now();
        String stage = "";
        System.out.println("START - Auto: " + accountDTO.getUserName() + " - Time: " + Duration.between(start, Instant.now()).toSeconds());
        try {
            SeleniumGridDriver setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
            driver = setupTestDriver.getDriver();
            //get account run
            stage = "LOGIN FINONE";
            HashMap<String, String> dataControl = new HashMap<>();
            LoginPage loginPage = new LoginPage(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
            loginPage.clickLogin();

//            //check da login
//            if(driver.findElements(By.xpath("//*[contains(@id,'userConfirmation')]//*[contains(text(),'Sign In')]")).size()!=0){
//                WebElement we=driver.findElement(By.xpath("//*[contains(@id,'userConfirmation')]//*[contains(text(),'Sign In')]"));
//                we.click();
//            }

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));
            System.out.println("Auto: " + accountDTO.getUserName() + " - " + stage + ": DONE" + " - Time: " + Duration.between(start, Instant.now()).toSeconds());
            Utilities.captureScreenShot(driver);

            AutoAssignDTO autoAssignDTO = null;
            do {
                try {
                    Instant startIn = Instant.now();


                    System.out.println("Auto:" + accountDTO.getUserName() + " - BEGIN " + " - Time: " + Duration.between(startIn, Instant.now()).toSeconds());
                    Query query = new Query();
                    query.addCriteria(Criteria.where("status").is(0));
                    autoAssignDTO = mongoTemplate.findOne(query, AutoAssignDTO.class);

                    if (!Objects.isNull(autoAssignDTO)) {

                        //update app
                        Query queryUpdate = new Query();
                        queryUpdate.addCriteria(Criteria.where("status").is(0).and("appid").is(autoAssignDTO.getAppid()).and("username").is(autoAssignDTO.getUsername()));
                        Update update = new Update();
                        update.set("userauto", accountDTO.getUserName());
                        update.set("status", 2);
                        AutoAssignDTO resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, AutoAssignDTO.class);

                        if (resultUpdate == null) {
                            continue;
                        }

                        System.out.println("Auto:" + accountDTO.getUserName() + " - GET DONE " + " - " + " App: " + autoAssignDTO.getAppid() + " - User: " + autoAssignDTO.getUsername() + " - Time: " + Duration.between(startIn, Instant.now()).toSeconds());

                        stage = "HOME PAGE";
                        HomePage homePage = new HomePage(driver);
                        //System.out.println("Acc: " + accountDTO.getUserName() + "-" + stage + ": DONE");
                        // ========== APPLICATIONS =================
                        String appID = autoAssignDTO.getAppid();
                        homePage.getMenuApplicationElement().click();

                        stage = "APPLICATION MANAGER";
                        // ========== APPLICATION MANAGER =================
                        homePage.getApplicationManagerElement().click();
                        await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(driver::getTitle, is("Application Manager"));

                        DE_ApplicationManagerPage de_applicationManagerPage = new DE_ApplicationManagerPage(driver);
                        await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(() -> de_applicationManagerPage.getApplicationManagerFormElement().isDisplayed());
                        de_applicationManagerPage.setData(appID, autoAssignDTO.getUsername().toLowerCase());
                        //System.out.println(stage + ": DONE");
                        //Utilities.captureScreenShot(driver);

                        System.out.println("Auto: " + accountDTO.getUserName() + " - FINISH " + " - " + " App: " + autoAssignDTO.getAppid() + " - User: " + autoAssignDTO.getUsername() + " - Time: " + Duration.between(startIn, Instant.now()).toSeconds());

                        // ========= UPDATE DB ============================
                        Query queryUpdate1 = new Query();
                        queryUpdate1.addCriteria(Criteria.where("status").is(2).and("appid").is(autoAssignDTO.getAppid()).and("username").is(autoAssignDTO.getUsername()));
                        Update update1 = new Update();
                        update1.set("userauto", accountDTO.getUserName());
                        update1.set("status", 1);
                        AutoAssignDTO resultUpdate1 = mongoTemplate.findAndModify(queryUpdate1, update1, AutoAssignDTO.class);
                        System.out.println("Auto: " + accountDTO.getUserName() + " - UPDATE STATUS " + " - " + " App: " + autoAssignDTO.getAppid() + " - User: " + autoAssignDTO.getUsername() + " - Time: " + Duration.between(startIn, Instant.now()).toSeconds());
                    }
                } catch (Exception ex) {
                    Query queryUpdate = new Query();
                    queryUpdate.addCriteria(Criteria.where("status").is(0).and("appid").is(autoAssignDTO.getAppid()).and("username").is(autoAssignDTO.getUsername()));
                    Update update = new Update();
                    update.set("userauto", accountDTO.getUserName());
                    update.set("status", 3);
                    AutoAssignDTO resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, AutoAssignDTO.class);

                    System.out.println(ex.getMessage());
                }
            } while (!Objects.isNull(autoAssignDTO));
        } catch (Exception e) {
            System.out.println("User Auto:" + accountDTO.getUserName() + " - " + stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);
        } finally {
            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            logout(driver,accountDTO.getUserName());
            pushAccountToQueue(accountDTO, project);
        }
    }
    //------------------------ END AUTO ASSIGN -----------------------------------------------------

    //------------------------ RESPONSE_QUERY-----------------------------------------------------
    private LoginDTO return_pollAccountFromQueue(Queue<LoginDTO> accounts, String project, Map<String, Object> mapValue) throws Exception {
        LoginDTO accountDTO = null;

        while (Objects.isNull(accountDTO)) {
            System.out.println("Wait to get account...");
            DEResponseQueryDTO deResponseQueryDTO = (DEResponseQueryDTO) mapValue.get("DEResponseQueryList");
            Query query = new Query();
            query.addCriteria(Criteria.where("applicationId").is(deResponseQueryDTO.getAppId()));
            Application applicationDTO = mongoTemplate.findOne(query, Application.class);
            AccountFinOneDTO accountFinOneDTO = null;
            if (!Objects.isNull(applicationDTO)) {
                query = new Query();
                query.addCriteria(Criteria.where("active").is(0).and("project").is(project).and("username").is(applicationDTO.getAutomationAcc()));
                accountFinOneDTO = mongoTemplate.findOne(query, AccountFinOneDTO.class);
            }
            if (!Objects.isNull(accountFinOneDTO)) {
                accountDTO = new LoginDTO().builder().userName(accountFinOneDTO.getUsername()).password(accountFinOneDTO.getPassword()).build();

                Query queryUpdate = new Query();
                queryUpdate.addCriteria(Criteria.where("active").is(0).and("username").is(accountFinOneDTO.getUsername()).and("project").is(project));
                Update update = new Update();
                update.set("active", 1);
                AccountFinOneDTO resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, AccountFinOneDTO.class);

                if (resultUpdate == null) {
                    Thread.sleep(Constant.WAIT_ACCOUNT_GET_NULL);
                    accountDTO = null;
                } else {
                    System.out.println("Get it:" + accountDTO.toString());
                    System.out.println("Exist:" + accounts.size());
                }
            } else
                Thread.sleep(Constant.WAIT_ACCOUNT_TIMEOUT);
        }

        return accountDTO;
    }

    public void runAutomationDE_responseQuery(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO) throws Exception {
        ResponseAutomationModel responseModel = new ResponseAutomationModel();
        Instant start = Instant.now();
        String stage = "";
        DEResponseQueryDTO deResponseQueryDTO = DEResponseQueryDTO.builder().build();
        log.info("{}", deResponseQueryDTO);
        try {
            stage = "INIT DATA";
            //*************************** GET DATA *********************//
            deResponseQueryDTO = (DEResponseQueryDTO) mapValue.get("DEResponseQueryList");
            //*************************** END GET DATA *********************//
            System.out.println(stage + ": DONE");
            stage = "LOGIN FINONE";
            LoginPage loginPage = new LoginPage(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
            loginPage.clickLogin();

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            System.out.println("Auto:" + accountDTO.getUserName() + " - GET DONE " + " - " + " App: " + deResponseQueryDTO.getAppId() + " - Time: " + Duration.between(start, Instant.now()).toSeconds());

            stage = "HOME PAGE";
            HomePage homePage = new HomePage(driver);
            // ========== APPLICATIONS =================
            homePage.getMenuApplicationElement().click();
            stage = "RESPONSE QUERY";

            // ========== RESPONSE QUERY =================
            DE_ReturnRaiseQueryPage de_ReturnRaiseQueryPage = new DE_ReturnRaiseQueryPage(driver);
            de_ReturnRaiseQueryPage.getResponseQueryElement().click();
            de_ReturnRaiseQueryPage.setData(deResponseQueryDTO, downdloadFileURL);
            System.out.println("Auto - FINISH: " + " - " + " App: " + deResponseQueryDTO.getAppId() + " - Time: " + Duration.between(start, Instant.now()).toSeconds());

            // ========= UPDATE DB ============================
//            Query queryUpdate1 = new Query();
//            queryUpdate1.addCriteria(Criteria.where("status").is(2).and("appId").is(deResponseQueryDTO.getAppId()));
//            Update update1 = new Update();
//            update1.set("userauto", accountDTO.getUserName());
//            update1.set("status", 1);
//            System.out.println("Auto: " + accountDTO.getUserName() + " - UPDATE STATUS " + " - " + " App: " + deResponseQueryDTO.getAppId() + " - Time: " + Duration.between(start, Instant.now()).toSeconds());

            deResponseQueryDTO.setStatus("OK");
            deResponseQueryDTO.setUserAuto(accountDTO.getUserName());

            responseModel.setProject(deResponseQueryDTO.getProject());
            responseModel.setReference_id(deResponseQueryDTO.getReference_id());
            responseModel.setTransaction_id(deResponseQueryDTO.getTransaction_id());
            responseModel.setApp_id(deResponseQueryDTO.getAppId());
            responseModel.setAutomation_result("RESPONSEQUERY PASS");

            Utilities.captureScreenShot(driver);

        } catch (Exception e) {
            deResponseQueryDTO.setStatus("ERROR");
            deResponseQueryDTO.setUserAuto(accountDTO.getUserName());

            responseModel.setProject(deResponseQueryDTO.getProject());
            responseModel.setReference_id(deResponseQueryDTO.getReference_id());
            responseModel.setTransaction_id(deResponseQueryDTO.getTransaction_id());
            responseModel.setApp_id(deResponseQueryDTO.getAppId());
            responseModel.setAutomation_result("RESPONSEQUERY FAILED" + " - " + e.getMessage());

            System.out.println("Auto Error:" + stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);
        } finally {
            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());

            System.out.println("Auto DONE:" + responseModel.getAutomation_result() + "- Project " + responseModel.getProject() + "- AppId " +responseModel.getApp_id());
            mongoTemplate.save(deResponseQueryDTO);
            logout(driver,accountDTO.getUserName());
            autoUpdateStatusRabbit(responseModel, "updateAutomation");

        }
    }
    //------------------------ END RESPONSE_QUERY -----------------------------------------------------

    //------------------------ SALE_QUEUE-----------------------------------------------------
    public void runAutomationDE_saleQueue(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO) throws Exception {
        ResponseAutomationModel responseModel = new ResponseAutomationModel();
        Instant start = Instant.now();
        String stage = "";
        DESaleQueueDTO deSaleQueueDTO = DESaleQueueDTO.builder().build();
        log.info("{}", deSaleQueueDTO);
        try {
            stage = "INIT DATA";
            //*************************** GET DATA *********************//
            deSaleQueueDTO = (DESaleQueueDTO) mapValue.get("DESaleQueueList");
            //*************************** END GET DATA *********************//
            System.out.println(stage + ": DONE");

            stage = "LOGIN FINONE";

            LoginPage loginPage = new LoginPage(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
            loginPage.clickLogin();

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "HOME PAGE";
            HomePage homePage = new HomePage(driver);
            // ========== APPLICATIONS =================
            homePage.getMenuApplicationElement().click();

            // ========== SALE QUEUE =================
            stage = "SALE QUEUE";
            DE_ReturnSaleQueuePage de_ReturnSaleQueuePage = new DE_ReturnSaleQueuePage(driver);
            de_ReturnSaleQueuePage.setData(deSaleQueueDTO, accountDTO.getUserName().toLowerCase(), downdloadFileURL);

            System.out.println("Auto - FINISH: " + stage + " - " + " App: " + deSaleQueueDTO.getAppId() + " - Time: " + Duration.between(start, Instant.now()).toSeconds());

            // ========== Last Update User ACCA =================
            if (!Objects.isNull(deSaleQueueDTO.getUserCreatedSalesQueue())){
                AssignManagerSaleQueuePage de_applicationManagerPage = new AssignManagerSaleQueuePage(driver);
                //update code, nếu không có up ACCA thì chuyen thang len DC nên reassing là user da raise saleQUEUE
                if(!deSaleQueueDTO.getDataDocuments().stream().filter(c->c.getDocumentName().contains("(ACCA)")).findAny().isPresent())
                {
                    de_applicationManagerPage.getMenuApplicationElement().click();

                    de_applicationManagerPage.getApplicationManagerElement().click();

                    // ========== APPLICATION MANAGER =================
                    stage = "APPLICATION MANAGER";

                    await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                            .until(driver::getTitle, is("Application Manager"));

                    de_applicationManagerPage.setData(deSaleQueueDTO.getAppId(), accountDTO.getUserName());

                    de_applicationManagerPage.getMenuApplicationElement().click();

                    de_applicationManagerPage.getApplicationElement().click();

                    await("Application timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                            .until(driver::getTitle, is("Application Grid"));

                    de_applicationManagerPage.getApplicationAssignedNumberElement().clear();

                    de_applicationManagerPage.getApplicationAssignedNumberElement().sendKeys(deSaleQueueDTO.getAppId());

                    await("tbApplicationAssignedElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                            .until(() -> de_applicationManagerPage.getTbApplicationAssignedElement().size() > 2);

                    WebElement applicationIdAssignedNumberElement = driver.findElement(new By.ByXPath("//table[@id='LoanApplication_Assigned']//tbody//tr//td[contains(@class,'tbl-left')]//a[contains(text(),'" + deSaleQueueDTO.getAppId() + "')]"));

                    await("webAssignElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                            .until(() -> applicationIdAssignedNumberElement.isDisplayed());

                    applicationIdAssignedNumberElement.click();

                    de_applicationManagerPage.getBtnMoveToNextStageElement().click();

                    de_applicationManagerPage.getMenuApplicationElement().click();

                    de_applicationManagerPage.getApplicationManagerElement().click();

                    // ========== APPLICATION MANAGER =================
                    stage = "APPLICATION MANAGER";

                    await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                            .until(driver::getTitle, is("Application Manager"));

                    await("appManager_lead_application_number visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                            .until(() -> de_applicationManagerPage.getApplicationManagerFormElement().isDisplayed());

                    de_applicationManagerPage.getApplicationNumberElement().sendKeys(deSaleQueueDTO.getAppId());
                    de_applicationManagerPage.getSearchApplicationElement().click();

                    await("tdApplicationElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                            .until(() -> de_applicationManagerPage.getTdApplicationElement().size() > 0);

                    await("showTaskElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                            .until(() -> de_applicationManagerPage.getShowTaskElement().isDisplayed());

                    if ("LOGIN_ACCEPTANCE".equals(de_applicationManagerPage.getTdCheckStageApplicationElement().getText())) {
                        de_applicationManagerPage.setDataACCA(deSaleQueueDTO.getUserCreatedSalesQueue());
                    }

                }
            }else{
                AssignManagerSaleQueuePage de_applicationManagerPage = new AssignManagerSaleQueuePage(driver);
                de_applicationManagerPage.getMenuApplicationElement().click();

                de_applicationManagerPage.getApplicationManagerElement().click();

                // ========== APPLICATION MANAGER =================
                stage = "APPLICATION MANAGER";

                await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(driver::getTitle, is("Application Manager"));
                //Service Acc đang lấy cho team DE tại ngày 04/09/2020
                de_applicationManagerPage.setData(deSaleQueueDTO.getAppId(), "serviceacc_ldl");
            }

            deSaleQueueDTO.setStatus("OK");
            deSaleQueueDTO.setUserAuto(accountDTO.getUserName());

            responseModel.setProject(deSaleQueueDTO.getProject());
            responseModel.setReference_id(deSaleQueueDTO.getReference_id());
            responseModel.setTransaction_id(deSaleQueueDTO.getTransaction_id());
            responseModel.setApp_id(deSaleQueueDTO.getAppId());
            responseModel.setAutomation_result("SALEQUEUE PASS");

            Utilities.captureScreenShot(driver);

        } catch (Exception e) {
            deSaleQueueDTO.setStatus("ERROR");
            deSaleQueueDTO.setUserAuto(accountDTO.getUserName());

            responseModel.setProject(deSaleQueueDTO.getProject());
            responseModel.setReference_id(deSaleQueueDTO.getReference_id());
            responseModel.setTransaction_id(deSaleQueueDTO.getTransaction_id());
            responseModel.setApp_id(deSaleQueueDTO.getAppId());
            responseModel.setAutomation_result("SALEQUEUE FAILED" + " - " + e.getMessage());

            System.out.println("Auto Error:" + stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);
        } finally {
            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            System.out.println("Auto DONE:" + responseModel.getAutomation_result() + "- Project " + responseModel.getProject() + "- AppId " +responseModel.getApp_id());
            mongoTemplate.save(deSaleQueueDTO);
            logout(driver,accountDTO.getUserName());
            autoUpdateStatusRabbit(responseModel, "updateAutomation");
        }
    }
    //------------------------ END DE_SALE_QUEUE -----------------------------------------------------

    //------------------------ START UPDATE RABBITMQ -----------------------------------------------------
    private void autoUpdateStatusRabbit(ResponseAutomationModel responseAutomationModel, String func) throws Exception {
        JsonNode jsonNode = rabbitMQService.sendAndReceive("tpf-service-esb",
                Map.of("func", func,
                        "body", Map.of("project", responseAutomationModel.getProject(),
                                "transaction_id", responseAutomationModel.getTransaction_id(),
                                "app_id", responseAutomationModel.getApp_id(),
                                "automation_result", responseAutomationModel.getAutomation_result(),
                                "reference_id", responseAutomationModel.getReference_id()
                        )));
        System.out.println("rabit:=>" + jsonNode.toString());
    }
    //------------------------ END AUTO ASSIGN -----------------------------------------------------

    //------------------------ SMARTNET-----------------------------------------------------
    public void SN_runAutomation_QuickLead(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO) throws Exception {
        Instant start = Instant.now();
        String appId = "";
        String stage = "";
        Application application = Application.builder().build();
        log.info("{}", application);
        try {
            stage = "INIT DATA";
            //*************************** GET DATA *********************//
            application = (Application) mapValue.get("ApplicationDTO");
            application.setAutomationAcc(accountDTO.getUserName());
            QuickLead quickLead = application.getQuickLead();
            //mongoTemplate.save(application);


            //*************************** END GET DATA *********************//
            Actions actions = new Actions(driver);
            System.out.println(stage + ": DONE");
            stage = "LOGIN FINONE";
            HashMap<String, String> dataControl = new HashMap<>();
            LoginPage loginPage = new LoginPage(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
            loginPage.clickLogin();
            //actions.moveToElement(loginPage.getBtnElement()).click().build().perform();

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));


            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "HOME PAGE";
            HomePage homePage = new HomePage(driver);


            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "QUICK LEAD";
            // ========== QUICK LEAD =================
            homePage.menuClick();
            homePage.leadQuickClick();

            await("Quick Lead Entry timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Quick Lead Entry"));

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "QUICK LEAD (DOCUMENT UPLOAD & APPLICATION CREATION)";
            QuickLeadPage quickLeadPage = new QuickLeadPage(driver);
            quickLeadPage.setData(quickLead);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "LEADS GRID";
            // ========== LEAD PAGE =================
            LeadsPage leadsPage = new LeadsPage(driver);
            await("Quick Lead Entry timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Lead Grid"));

            await("notifyTextElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadsPage.getNotifyTextElement().isEnabled() && leadsPage.getNotifyTextElement().isDisplayed());

            String notify = leadsPage.getNotifyTextElement().getText();
            String leadApp = "";
            if (notify.contains("LEAD")) {
                leadApp = notify.substring(notify.indexOf("LEAD"), notify.length());
            }

            System.out.println("LEAD APP: =>" + leadApp);
            leadsPage.setData(leadApp);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "LEAD STAGE";
            // ========== LEAD STAGE =================
            LeadDetailPage leadDetailPage = new LeadDetailPage(driver);
            await("contentElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadDetailPage.getContentElement().isDisplayed());

            leadDetailPage.setData(quickLead, leadApp, downdloadFileURL);

            Utilities.captureScreenShot(driver);

            await("Lead Page timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Lead Grid"));

            leadsPage.getSpanAllNotifyElement().click();
            await("getDivAllNotifyElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadsPage.getDivAllNotifyElement().isEnabled() && leadsPage.getDivAllNotifyElement().isDisplayed());

            await("getBtnAllNotifyElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadsPage.getBtnAllNotifyElement().isEnabled() && leadsPage.getBtnAllNotifyElement().isDisplayed());
            leadsPage.getBtnAllNotifyElement().click();

            Utilities.captureScreenShot(driver);
            await("getBtnAllNotifyElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadsPage.getNotifyTextSuccessElement().size() > 0);

            String leadAppID = "";
//            for (WebElement e : leadsPage.getNotifyTextSuccessElement()) {
//                System.out.println(e.getText());
//                if (e.getText().contains("APPL")) {
//                    leadAppID = e.getText().substring(e.getText().indexOf("APPL"), e.getText().indexOf("APPL") + 12);
//                }
//            }
//            System.out.println("APPID: => " + leadAppID);
//
//            Utilities.captureScreenShot(driver);
//            System.out.println(stage + ": DONE");


//            //update thêm phần assign về acc tạo app để tranh rơi vào pool
//            stage = "APPLICATION MANAGER";
//            // ========== APPLICATION MANAGER =================
//            homePage.getMenuApplicationElement().click();
//            homePage.getApplicationManagerElement().click();
//            await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(driver::getTitle, is("Application Manager"));
//
//            DE_ApplicationManagerPage de_applicationManagerPage = new DE_ApplicationManagerPage(driver);
//
//            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> de_applicationManagerPage.getApplicationManagerFormElement().isDisplayed());
//            de_applicationManagerPage.setData(leadAppID, accountDTO.getUserName());
//            System.out.println(stage + ": DONE");
//            Utilities.captureScreenShot(driver);
//
//            //-------------------- END ---------------------------
            //update thêm phần assign về acc tạo app để tranh rơi vào pool
            stage = "APPLICATION MANAGER";
            // ========== APPLICATION MANAGER =================
            homePage.getMenuApplicationElement().click();
            homePage.getApplicationManagerElement().click();
            await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Manager"));

            DE_ApplicationManagerPage de_applicationManagerPage = new DE_ApplicationManagerPage(driver);

            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> de_applicationManagerPage.getApplicationManagerFormElement().isDisplayed());

            //get appID moi o day
            leadAppID = de_applicationManagerPage.getAppID(leadApp);
            System.out.println(" APP: =>" + leadAppID);

            //smartner bo phan reassign user auto
//            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> de_applicationManagerPage.getBackBtnElement().isDisplayed());
//
//            de_applicationManagerPage.getBackBtnElement().click();
//
//            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> de_applicationManagerPage.getApplicationManagerFormElement().isDisplayed());
//
//            de_applicationManagerPage.setData(leadAppID, accountDTO.getUserName());
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            //-------------------- END ---------------------------

            application.setApplicationId(leadAppID);
            application.setLeadApp(leadApp);

            //UPDATE STATUS
            application.setStatus("QUICKLEAD PASS");
            application.setDescription("Thanh cong");


            Utilities.captureScreenShot(driver);
            //logout(driver);

        } catch (Exception e) {
            //UPDATE STATUS
            application.setStatus("QUICKLEAD FAIL");
            application.setStage(stage);
            application.setDescription(e.getMessage());

            System.out.println(stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);

            if (e.getMessage().contains("Work flow failed!!!")) {
                stage = "END OF LEAD DETAIL";

                await("Get error fail!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> driver.findElements(By.id("error-message")).size() > 0);

                if (driver.findElements(By.id("error-message")) != null && driver.findElements(By.id("error-message")).size() > 0) {
                    String error = "Error: ";
                    for (WebElement we : driver.findElements(By.id("error-message"))) {
                        error += " - " + we.getText();
                    }
                    System.out.println(stage + "=>" + error);
                }
            }
        } finally {
            if (application.getApplicationId() == null || application.getApplicationId().isEmpty() || application.getApplicationId().indexOf("LEAD") > 0 || application.getApplicationId().indexOf("APPL") < 0) {
                application.setApplicationId("UNKNOW");
            }

            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            try {
                SN_updateStatusRabbit(application, "updateAutomation", "smartnet");
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            SN_updateDB(application);
            logout(driver,accountDTO.getUserName());

        }
    }

    private void SN_updateStatusRabbit(Application application, String func, String project) throws Exception {

        JsonNode jsonNode = rabbitMQService.sendAndReceive("tpf-service-esb",
                Map.of("func", "updateAutomation", "reference_id", application.getReference_id(), "body", Map.of("app_id", application.getApplicationId() != null ? application.getApplicationId() : "",
                        "project", project,
                        "automation_result", application.getStatus(),
                        "description", application.getDescription() != null ? application.getDescription() : "",
                        "transaction_id", application.getQuickLeadId(),
                        "automation_account", application.getAutomationAcc())));
        System.out.println("rabit:=>" + jsonNode.toString());

    }

    private void SN_updateDB(Application application) throws Exception {

//        Query queryUpdate = new Query();
//        queryUpdate.addCriteria(Criteria.where("quickLeadId").is(application.getQuickLeadId()));
//        Update update = new Update();
//        update.set("lastModifiedDate", new Date());
//        AccountFinOneDTO resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, AccountFinOneDTO.class);
        mongoTemplate.save(application);

    }
    //------------------------ END SMARTNET -----------------------------------------------------

    //------------------------ QUICKLEAD - MOBILITY-----------------------------------------------------
    public void MOBILITY_runAutomation_QuickLead(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO) throws Exception {
        Instant start = Instant.now();
        String appId = "";
        String stage = "";
        Application application = Application.builder().build();
        log.info("{}", application);
        try {
            stage = "INIT DATA";
            //*************************** GET DATA *********************//
            application = (Application) mapValue.get("ApplicationDTO");
            application.setAutomationAcc(accountDTO.getUserName());
            QuickLead quickLead = application.getQuickLead();
            //mongoTemplate.save(application);


            //*************************** END GET DATA *********************//
            Actions actions = new Actions(driver);
            System.out.println(stage + ": DONE");
            stage = "LOGIN FINONE";
            HashMap<String, String> dataControl = new HashMap<>();
            LoginPage loginPage = new LoginPage(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
            loginPage.clickLogin();
            //actions.moveToElement(loginPage.getBtnElement()).click().build().perform();

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));


            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "HOME PAGE";
            HomePage homePage = new HomePage(driver);


            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "QUICK LEAD";
            // ========== QUICK LEAD =================
            homePage.menuClick();
            homePage.leadQuickClick();

            await("Quick Lead Entry timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Quick Lead Entry"));

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "QUICK LEAD (DOCUMENT UPLOAD & APPLICATION CREATION)";
            QuickLeadPage quickLeadPage = new QuickLeadPage(driver);
            quickLeadPage.setData(quickLead);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "LEADS GRID";
            // ========== LEAD PAGE =================
            LeadsPage leadsPage = new LeadsPage(driver);
            await("Quick Lead Entry timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Lead Grid"));

            await("notifyTextElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadsPage.getNotifyTextElement().isEnabled() && leadsPage.getNotifyTextElement().isDisplayed());

            String notify = leadsPage.getNotifyTextElement().getText();
            String leadApp = "";
            if (notify.contains("LEAD")) {
                leadApp = notify.substring(notify.indexOf("LEAD"), notify.length());
            }

            System.out.println("LEAD APP: =>" + leadApp);
            leadsPage.setData(leadApp);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "LEAD STAGE";
            // ========== LEAD STAGE =================
            LeadDetailPage leadDetailPage = new LeadDetailPage(driver);
            await("contentElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadDetailPage.getContentElement().isDisplayed());

            leadDetailPage.setData(quickLead, leadApp, downdloadFileURL);

            Utilities.captureScreenShot(driver);

            await("Lead Page timeout").atMost(Constant.TIME_OUT_5_M, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Lead Grid"));

            leadsPage.getSpanAllNotifyElement().click();
            await("getDivAllNotifyElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadsPage.getDivAllNotifyElement().isEnabled() && leadsPage.getDivAllNotifyElement().isDisplayed());

            await("getBtnAllNotifyElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadsPage.getBtnAllNotifyElement().isEnabled() && leadsPage.getBtnAllNotifyElement().isDisplayed());
            leadsPage.getBtnAllNotifyElement().click();

            Utilities.captureScreenShot(driver);
            await("getBtnAllNotifyElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadsPage.getNotifyTextSuccessElement().size() > 0);

            String leadAppID = "";
//            for (WebElement e : leadsPage.getNotifyTextSuccessElement()) {
//                System.out.println(e.getText());
//                if (e.getText().contains("APPL")) {
//                    leadAppID = e.getText().substring(e.getText().indexOf("APPL"), e.getText().indexOf("APPL") + 12);
//                }
//            }
//            System.out.println("APPID: => " + leadAppID);
//
//            Utilities.captureScreenShot(driver);
//            System.out.println(stage + ": DONE");


//            //update thêm phần assign về acc tạo app để tranh rơi vào pool
//            stage = "APPLICATION MANAGER";
//            // ========== APPLICATION MANAGER =================
//            homePage.getMenuApplicationElement().click();
//            homePage.getApplicationManagerElement().click();
//            await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(driver::getTitle, is("Application Manager"));
//
//            DE_ApplicationManagerPage de_applicationManagerPage = new DE_ApplicationManagerPage(driver);
//
//            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> de_applicationManagerPage.getApplicationManagerFormElement().isDisplayed());
//            de_applicationManagerPage.setData(leadAppID, accountDTO.getUserName());
//            System.out.println(stage + ": DONE");
//            Utilities.captureScreenShot(driver);
//
//            //-------------------- END ---------------------------
            //update thêm phần assign về acc tạo app để tranh rơi vào pool
            stage = "APPLICATION MANAGER";
            // ========== APPLICATION MANAGER =================
            homePage.getMenuApplicationElement().click();
            homePage.getApplicationManagerElement().click();
            await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Manager"));

            DE_ApplicationManagerPage de_applicationManagerPage = new DE_ApplicationManagerPage(driver);

            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> de_applicationManagerPage.getApplicationManagerFormElement().isDisplayed());

            //get appID moi o day
            leadAppID = de_applicationManagerPage.getAppID(leadApp);
            System.out.println(" APP: =>" + leadAppID);

            //smartner bo phan reassign user auto
//            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> de_applicationManagerPage.getBackBtnElement().isDisplayed());
//
//            de_applicationManagerPage.getBackBtnElement().click();
//
//            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> de_applicationManagerPage.getApplicationManagerFormElement().isDisplayed());
//
//            de_applicationManagerPage.setData(leadAppID, accountDTO.getUserName());
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            //-------------------- END ---------------------------

            application.setApplicationId(leadAppID);
            application.setLeadApp(leadApp);

            //UPDATE STATUS
            application.setStatus("QUICKLEAD PASS");
            application.setDescription("Thanh cong");


            Utilities.captureScreenShot(driver);
            //logout(driver);

        } catch (Exception e) {
            //UPDATE STATUS
            application.setStatus("QUICKLEAD FAIL");
            application.setStage(stage);
            application.setDescription(e.getMessage());

            System.out.println(stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);

            if (e.getMessage().contains("Work flow failed!!!")) {
                stage = "END OF LEAD DETAIL";

                await("Get error fail!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> driver.findElements(By.id("error-message")).size() > 0);

                if (driver.findElements(By.id("error-message")) != null && driver.findElements(By.id("error-message")).size() > 0) {
                    String error = "Error: ";
                    for (WebElement we : driver.findElements(By.id("error-message"))) {
                        error += " - " + we.getText();
                    }
                    System.out.println(stage + "=>" + error);
                }
            }
        } finally {
            if (application.getApplicationId() == null || application.getApplicationId().isEmpty() || application.getApplicationId().indexOf("LEAD") > 0 || application.getApplicationId().indexOf("APPL") < 0) {
                application.setApplicationId("UNKNOW");
                application.setStatus("QUICKLEAD_FAILED");
                application.setDescription("Khong thanh cong");
            }

            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            try {
                MOBILITY_updateStatusRabbit(application, "updateAutomation", "mobility");
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            MOBILITY_updateDB(application);
            logout(driver,accountDTO.getUserName());

        }
    }

    private void MOBILITY_updateStatusRabbit(Application application, String func, String project) throws Exception {

        JsonNode jsonNode = rabbitMQService.sendAndReceive("tpf-service-esb",
                Map.of("func", "updateAutomation", "reference_id", application.getReference_id(), "body", Map.of("app_id", application.getApplicationId() != null ? application.getApplicationId() : "",
                        "project", project,
                        "automation_result", application.getStatus(),
                        "description", application.getDescription() != null ? application.getDescription() : "",
                        "transaction_id", application.getQuickLeadId(),
                        "automation_account", application.getAutomationAcc())));
        System.out.println("rabit:=>" + jsonNode.toString());

    }

    private void MOBILITY_updateDB(Application application) throws Exception {

//        Query queryUpdate = new Query();
//        queryUpdate.addCriteria(Criteria.where("quickLeadId").is(application.getQuickLeadId()));
//        Update update = new Update();
//        update.set("lastModifiedDate", new Date());
//        AccountFinOneDTO resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, AccountFinOneDTO.class);
        mongoTemplate.save(application);

    }
    //------------------------ END - QUICKLEAD -----------------------------------------------------

    //------------------------ FIELD -----------------------------------------------------
    public void runAutomation_Field(WebDriver driver, Map<String, Object> mapValue, String project, String browser, String funcString) throws Exception {
        String stage = "";
        String transaction_id = "";
        String reference_id = "";
        String project_id = "";
        try {
            stage = "INIT DATA";

            List<LoginDTO> loginDTOList = new ArrayList<LoginDTO>();

            LoginDTO accountDTONew = null;
            do {
                //get list account finone available
                Query query = new Query();
                query.addCriteria(Criteria.where("active").is(0).and("project").is(project));
                AccountFinOneDTO accountFinOneDTO = mongoTemplate.findOne(query, AccountFinOneDTO.class);
                if (!Objects.isNull(accountFinOneDTO)) {
                    accountDTONew = new LoginDTO().builder().userName(accountFinOneDTO.getUsername()).password(accountFinOneDTO.getPassword()).build();

                    Query queryUpdate = new Query();
                    queryUpdate.addCriteria(Criteria.where("active").is(0).and("username").is(accountFinOneDTO.getUsername()).and("project").is(project));
                    Update update = new Update();
                    update.set("active", 1);
                    AccountFinOneDTO resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, AccountFinOneDTO.class);

                    if (resultUpdate == null) {
                        Thread.sleep(Constant.WAIT_ACCOUNT_GET_NULL);
                        accountDTONew = null;
                    } else {
                        loginDTOList.add(accountDTONew);
                        System.out.println("Get it:" + accountDTONew.toString());
                    }
                } else
                    accountDTONew = null;
            } while (!Objects.isNull(accountDTONew));

            //*************************** GET DATA *********************//
            if ("Waive_Field".equals(funcString)){
                RequestAutomationDTO RequestAutomationWaiveFieldDTOList = (RequestAutomationDTO) mapValue.get("RequestAutomationWaiveFieldList");
                transaction_id = RequestAutomationWaiveFieldDTOList.getTransaction_id();
                reference_id = RequestAutomationWaiveFieldDTOList.getReference_id();
                project_id = RequestAutomationWaiveFieldDTOList.getProject();
                List<WaiveFieldDTO> waiveFieldDTOList = RequestAutomationWaiveFieldDTOList.getWaiveFieldDTO();
                for (int i = 0; i < waiveFieldDTOList.size(); i++){
                    waiveFieldDTOList.get(i).setProject(project_id);
                    waiveFieldDTOList.get(i).setTransaction_id(transaction_id);
                    waiveFieldDTOList.get(i).setReference_id(reference_id);
                }
                //insert data
                mongoTemplate.insert(waiveFieldDTOList, WaiveFieldDTO.class);
            } else if ("Submit_Field".equals(funcString)){
                RequestAutomationDTO RequestAutomationSubmitFieldDTOList = (RequestAutomationDTO) mapValue.get("RequestAutomationSubmitFieldList");
                transaction_id = RequestAutomationSubmitFieldDTOList.getTransaction_id();
                reference_id = RequestAutomationSubmitFieldDTOList.getReference_id();
                project_id = RequestAutomationSubmitFieldDTOList.getProject();
                List<SubmitFieldDTO> submitFieldDTOList = RequestAutomationSubmitFieldDTOList.getSubmitFieldDTO();
                for (int i = 0; i < submitFieldDTOList.size(); i++){
                    submitFieldDTOList.get(i).setProject(project_id);
                    submitFieldDTOList.get(i).setTransaction_id(transaction_id);
                    submitFieldDTOList.get(i).setReference_id(reference_id);
                }
                //insert data
                mongoTemplate.insert(submitFieldDTOList, SubmitFieldDTO.class);
            }
            //*************************** END GET DATA *********************//

            if (loginDTOList.size() > 0) {
                ExecutorService workerThreadPoolDE = Executors.newFixedThreadPool(loginDTOList.size());

                for (LoginDTO loginDTO : loginDTOList) {
                    String finalTransaction_id = transaction_id;
                    String finalReference_id = reference_id;
                    String finalProject_id = project_id;
                    workerThreadPoolDE.execute(new Runnable() {
                        @SneakyThrows
                        @Override
                        public void run() {
                            if ("Waive_Field".equals(funcString)){
                                runAutomation_Waive_Field_run(loginDTO, browser, project, finalTransaction_id, finalReference_id, finalProject_id);
                            }else if ("Submit_Field".equals(funcString)){
                                runAutomation_Submit_Field_run(loginDTO, browser, project, finalTransaction_id, finalReference_id, finalProject_id);
                            }
                        }
                    });
                }

            }
        } catch (Exception e) {
            System.out.println(stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();
            Utilities.captureScreenShot(driver);
        }
    }

    public void runAutomation_Field_Old(WebDriver driver, Map<String, Object> mapValue, String project, String browser, String funcString) throws Exception {
        String stage = "";
        try {
            stage = "INIT DATA";

            List<LoginDTO> loginDTOList = new ArrayList<LoginDTO>();

            LoginDTO accountDTONew = null;
            do {
                //get list account finone available
                Query query = new Query();
                query.addCriteria(Criteria.where("active").is(0).and("project").is(project));
                AccountFinOneDTO accountFinOneDTO = mongoTemplate.findOne(query, AccountFinOneDTO.class);
                if (!Objects.isNull(accountFinOneDTO)) {
                    accountDTONew = new LoginDTO().builder().userName(accountFinOneDTO.getUsername()).password(accountFinOneDTO.getPassword()).build();

                    Query queryUpdate = new Query();
                    queryUpdate.addCriteria(Criteria.where("active").is(0).and("username").is(accountFinOneDTO.getUsername()).and("project").is(project));
                    Update update = new Update();
                    update.set("active", 1);
                    AccountFinOneDTO resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, AccountFinOneDTO.class);

                    if (resultUpdate == null) {
                        Thread.sleep(Constant.WAIT_ACCOUNT_GET_NULL);
                        System.out.println("Wait to get account...");
                        accountDTONew = null;
                    } else {
                        loginDTOList.add(accountDTONew);
                        System.out.println("Get it:" + accountDTONew.toString());
                    }
                } else
                    accountDTONew = null;
            } while (!Objects.isNull(accountDTONew));

            if (loginDTOList.size() > 0) {
                ExecutorService workerThreadPoolDE = Executors.newFixedThreadPool(loginDTOList.size());

                for (LoginDTO loginDTO : loginDTOList) {
                    workerThreadPoolDE.execute(new Runnable() {
                        @Override
                        public void run() {
                            if ("Waive_Field".equals(funcString)){
                                runAutomation_Waive_Field_run_Old(browser, loginDTO, project, mapValue);
                            }else if ("Submit_Field".equals(funcString)){
                                runAutomation_Submit_Field_run_Old(browser, loginDTO, project, mapValue);
                            }
                        }
                    });
                }

            }
        } catch (Exception e) {
            System.out.println(stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();
            Utilities.captureScreenShot(driver);
        }
    }

    //------------------------ END FIELD -----------------------------------------------------

    //------------------------ WAIVE FIELD -----------------------------------------------------
    private void runAutomation_Waive_Field_run(LoginDTO accountDTO, String browser, String project, String finalTransaction_id, String finalReference_id, String finalProject_id) throws Exception {
        WebDriver driver = null;
        Instant start = Instant.now();
        String stage = "";
        String autoAssign = "";
        int checkUpdateAuto = 0;
        System.out.println("START - Auto: " + accountDTO.getUserName() + " - Time: " + Duration.between(start, Instant.now()).toSeconds());
        WaiveFieldDTO waiveFieldDTO = WaiveFieldDTO.builder().build();
        ResponseAutomationModel responseModel = new ResponseAutomationModel();
        try {
            SeleniumGridDriver setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
            driver = setupTestDriver.getDriver();

            //get account run
            stage = "LOGIN FINONE";
            LoginPage loginPage = new LoginPage(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
            loginPage.clickLogin();

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));
            System.out.println("Auto: " + accountDTO.getUserName() + " - " + stage + ": DONE" + " - Time: " + Duration.between(start, Instant.now()).toSeconds());
            Utilities.captureScreenShot(driver);

            Query countQuery = new Query();
            countQuery.addCriteria(Criteria.where("status").is(0)
                    .and("reference_id").is(finalReference_id)
                    .and("transaction_id").is(finalTransaction_id)
                    .and("project").is(finalProject_id)
            );
            checkUpdateAuto = mongoTemplate.find(countQuery, WaiveFieldDTO.class).size();

            do {
                try {
                    Instant startIn = Instant.now();
                    System.out.println("Auto:" + accountDTO.getUserName() + " - BEGIN " + " - Time: " + Duration.between(startIn, Instant.now()).toSeconds());
                    Query query = new Query();
                    query.addCriteria(Criteria.where("status").is(0)
                            .and("reference_id").is(finalReference_id)
                            .and("transaction_id").is(finalTransaction_id)
                            .and("project").is(finalProject_id)
                    );
                    waiveFieldDTO = mongoTemplate.findOne(query, WaiveFieldDTO.class);

                    if (!Objects.isNull(waiveFieldDTO)) {
                        //update app
                        Query queryUpdate = new Query();
                        queryUpdate.addCriteria(Criteria.where("status").is(0)
                                .and("appId").is(waiveFieldDTO.getAppId())
                                .and("reference_id").is(waiveFieldDTO.getReference_id())
                                .and("project").is(waiveFieldDTO.getProject()));
                        Update update = new Update();
                        update.set("userAuto", accountDTO.getUserName());
                        update.set("status", 2);
                        WaiveFieldDTO resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, WaiveFieldDTO.class);
                        autoAssign = accountDTO.getUserName();

                        if (resultUpdate == null) {
                            continue;
                        }

                        System.out.println("Auto:" + accountDTO.getUserName() + " - GET DONE " +  " - App: " + waiveFieldDTO.getAppId() +  " - Time: " + Duration.between(startIn, Instant.now()).toSeconds());

                        stage = "HOME PAGE";
                        HomePage homePage = new HomePage(driver);
                        System.out.println(stage + ": DONE" + " - Time " + Duration.between(start, Instant.now()).toSeconds());

                        // ========== APPLICATIONS =================
                        homePage.getMenuApplicationElement().click();

                        // ========== CHECK STAGE APPLICATIONS =================
                        stage = "CHECK STAGE APPLICATION";
                        FV_CheckStageApplicationManager fv_CheckStageApplicationManager = new FV_CheckStageApplicationManager(driver);
                        fv_CheckStageApplicationManager.getMenuApplicationElement().click();
                        fv_CheckStageApplicationManager.getApplicationManagerElement().click();
                        await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(driver::getTitle, is("Application Manager"));
                        await("Application Manager Form visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(() -> fv_CheckStageApplicationManager.getApplicationManagerFormElement().isDisplayed());
                        fv_CheckStageApplicationManager.getApplicationNumberElement().sendKeys(waiveFieldDTO.getAppId());
                        fv_CheckStageApplicationManager.getSearchApplicationElement().click();
                        await("Table Application Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(() -> fv_CheckStageApplicationManager.getTdApplicationElement().size() > 2);

                        await("Stage wrong Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(() -> "FII".equals(fv_CheckStageApplicationManager.getTdCheckStageApplicationElement().getText()));

                        System.out.println(stage + ": DONE" + " - Time " + Duration.between(start, Instant.now()).toSeconds());

                        // ========== WAIVE OFF ALL =================
                        stage = "WAIVE OFF ALL";
                        FV_WaiveFieldPage fv_WaiveFieldPage = new FV_WaiveFieldPage(driver);
                        fv_WaiveFieldPage.setData(waiveFieldDTO, accountDTO.getUserName().toLowerCase());
                        System.out.println(stage + ": DONE" + " - Time " + Duration.between(start, Instant.now()).toSeconds());

                        // ========= UPDATE DB ============================
                        Query queryUpdate1 = new Query();
                        queryUpdate1.addCriteria(Criteria.where("status").is(2)
                                .and("appId").is(waiveFieldDTO.getAppId())
                                .and("userAuto").is(accountDTO.getUserName())
                                .and("reference_id").is(waiveFieldDTO.getReference_id())
                                .and("project").is(waiveFieldDTO.getProject()));
                        Update update1 = new Update();
                        update1.set("userAuto", accountDTO.getUserName());
                        update1.set("status", 1);
                        update1.set("checkUpdate", 1);
                        update1.set("automation_result", "WAIVE_FIELD_PASS");
                        update1.set("automation_result_message", "DONE");
                        mongoTemplate.findAndModify(queryUpdate1, update1, WaiveFieldDTO.class);
                        System.out.println("AUTO - WAIVE FIELD PASS: " + stage + " - " + " App: " + waiveFieldDTO.getAppId() + " - User: " + accountDTO.getUserName());
                    }
                } catch (Exception ex) {
                    Query queryUpdate = new Query();
                    queryUpdate.addCriteria(Criteria.where("status").is(2)
                            .and("appId").is(waiveFieldDTO.getAppId())
                            .and("userAuto").is(accountDTO.getUserName())
                            .and("reference_id").is(waiveFieldDTO.getReference_id())
                            .and("project").is(waiveFieldDTO.getProject()));
                    Update update = new Update();
                    update.set("userAuto", accountDTO.getUserName());
                    update.set("status", 3);
                    update.set("checkUpdate", 1);
                    update.set("automation_result", "WAIVE_FIELD_FAILED");
                    update.set("automation_result_message", ex.getMessage());
                    mongoTemplate.findAndModify(queryUpdate, update, WaiveFieldDTO.class);
                    System.out.println("AUTO - WAIVE FIELD FAILED: " + ex.getMessage() + " - " + stage + " - " + " App: " + waiveFieldDTO.getAppId() + " - User: " + accountDTO.getUserName());
                    Utilities.captureScreenShot(driver);
                }
            } while (!Objects.isNull(waiveFieldDTO));
        } catch (Exception e) {
            System.out.println("User Auto:" + accountDTO.getUserName() + " - " + stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();
            Utilities.captureScreenShot(driver);

            Query queryUpdate = new Query();
            queryUpdate.addCriteria(Criteria.where("status").is(0)
                    .and("appId").is(waiveFieldDTO.getAppId())
                    .and("userAuto").is(accountDTO.getUserName())
                    .and("reference_id").is(waiveFieldDTO.getReference_id())
                    .and("project").is(waiveFieldDTO.getProject()));
            Update update = new Update();
            update.set("userAuto", accountDTO.getUserName());
            update.set("status", 3);
            update.set("automation_result", "WAIVE_FIELD_FAILED");
            update.set("automation_result_message", e.getMessage());
            mongoTemplate.findAndModify(queryUpdate, update, WaiveFieldDTO.class);
            System.out.println("AUTO - WAIVE FIELD FAILED: " + e.getMessage() + " - " + stage + " - " + " App: " + waiveFieldDTO.getAppId() + " - User: " + accountDTO.getUserName());
            Utilities.captureScreenShot(driver);

        } finally {
            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            logout(driver,accountDTO.getUserName());
            pushAccountToQueue(accountDTO, project);
            if (accountDTO.getUserName().equals(autoAssign)){
                Query queryUpdateFailed = new Query();
                queryUpdateFailed.addCriteria(Criteria.where("reference_id").is(finalReference_id)
                        .and("transaction_id").is(finalTransaction_id)
                        .and("project").is(finalProject_id)
                        .and("checkUpdate").is(1)
                );
                List<MobilityFieldReponeDTO> resultRespone = mongoTemplate.find(queryUpdateFailed, MobilityFieldReponeDTO.class);

                if (resultRespone.size() == checkUpdateAuto){
                    responseModel.setReference_id(finalReference_id);
                    responseModel.setProject(finalProject_id);
                    responseModel.setTransaction_id("transaction_waive_field");
                    responseModel.setData(resultRespone);
                    autoUpdateStatusRabbitMobility(responseModel, "updateAutomation");
                }
            }
            System.out.println("AUTO - WAIVE FIELD" + ": END" + " - Time " + Duration.between(start, Instant.now()).toSeconds());
        }
    }

    private void runAutomation_Waive_Field_run_Old(String browser, LoginDTO accountDTO, String project, Map<String, Object> mapValue) {
        WebDriver driver = null;
        ResponseAutomationModel responseModel = new ResponseAutomationModel();
        Instant start = Instant.now();
        String stage = "";
        String transaction_id = "";
        String reference_id = "";
        String projectField = "";
        WaiveFieldDTO waiveFieldDTO = WaiveFieldDTO.builder().build();
        List<WaiveFieldDTO> checkResultExist = null;
        System.out.println("Auto - WAIVE FIELD" + ": START" + " - Time " + Duration.between(start, Instant.now()).toSeconds());
        try {
            RequestAutomationDTO RequestAutomationWaiveFieldDTOList = (RequestAutomationDTO) mapValue.get("RequestAutomationWaiveFieldList");
            transaction_id = RequestAutomationWaiveFieldDTOList.getTransaction_id();
            reference_id = RequestAutomationWaiveFieldDTOList.getReference_id();
            projectField = RequestAutomationWaiveFieldDTOList.getProject();
            List<WaiveFieldDTO> waiveFieldDTOList = RequestAutomationWaiveFieldDTOList.getWaiveFieldDTO();
            for (int i = 0; i < waiveFieldDTOList.size(); i++){
                waiveFieldDTOList.get(i).setProject(projectField);
                waiveFieldDTOList.get(i).setTransaction_id(transaction_id);
                waiveFieldDTOList.get(i).setReference_id(reference_id);
            }


            SeleniumGridDriver setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
            driver = setupTestDriver.getDriver();

            stage = "LOGIN FINONE";
            LoginPage loginPage = new LoginPage(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
            loginPage.clickLogin();
            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            Query query = new Query();
            query.addCriteria(Criteria.where("transaction_id").is(transaction_id).and("reference_id").is(reference_id).and("project").is(projectField));
            checkResultExist = mongoTemplate.find(query, WaiveFieldDTO.class);
            if (checkResultExist.size() < 1){
                mongoTemplate.insert(waiveFieldDTOList, WaiveFieldDTO.class);
            }

            do {
                try {
                    query = new Query();
                    query.addCriteria(Criteria.where("status").is(0).and("transaction_id").is(transaction_id).and("reference_id").is(reference_id).and("project").is(projectField));
                    waiveFieldDTO = mongoTemplate.findOne(query, WaiveFieldDTO.class);
                    if (!Objects.isNull(waiveFieldDTO)) {

                        //update app
                        Query queryUpdate = new Query();
                        queryUpdate.addCriteria(Criteria.where("_id").is(waiveFieldDTO.getId()).and("status").is(0).and("appId").is(waiveFieldDTO.getAppId()));

                        Update updateFirst2 = new Update();
                        updateFirst2.set("userAuto", accountDTO.getUserName());
                        updateFirst2.set("status", 2);
                        updateFirst2.set("transaction_id", transaction_id);
                        updateFirst2.set("reference_id", reference_id);
                        updateFirst2.set("project", projectField);
                        updateFirst2.set("automation_result", "WAIVE_FIELD_RUN");

                        WaiveFieldDTO resultUpdate = mongoTemplate.findAndModify(queryUpdate, updateFirst2, WaiveFieldDTO.class);

                        if (resultUpdate == null) {
                            continue;
                        }

                        stage = "HOME PAGE";
                        HomePage homePage = new HomePage(driver);
                        System.out.println(stage + ": DONE" + " - Time " + Duration.between(start, Instant.now()).toSeconds());

                        // ========== APPLICATIONS =================
                        homePage.getMenuApplicationElement().click();

                        // ========== CHECK STAGE APPLICATIONS =================
                        stage = "CHECK STAGE APPLICATION";
                        FV_CheckStageApplicationManager fv_CheckStageApplicationManager = new FV_CheckStageApplicationManager(driver);
                        fv_CheckStageApplicationManager.getMenuApplicationElement().click();
                        fv_CheckStageApplicationManager.getApplicationManagerElement().click();
                        await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(driver::getTitle, is("Application Manager"));
                        await("Application Manager Form visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(() -> fv_CheckStageApplicationManager.getApplicationManagerFormElement().isDisplayed());
                        fv_CheckStageApplicationManager.getApplicationNumberElement().sendKeys(waiveFieldDTO.getAppId());
                        fv_CheckStageApplicationManager.getSearchApplicationElement().click();
                        await("Table Application Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(() -> fv_CheckStageApplicationManager.getTdApplicationElement().size() > 2);

                        await("Stage wrong Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(() -> "FII".equals(fv_CheckStageApplicationManager.getTdCheckStageApplicationElement().getText()));

                        System.out.println(stage + ": DONE" + " - Time " + Duration.between(start, Instant.now()).toSeconds());

                        // ========== WAIVE OFF ALL =================
                        stage = "WAIVE OFF ALL";
                        FV_WaiveFieldPage fv_WaiveFieldPage = new FV_WaiveFieldPage(driver);
                        fv_WaiveFieldPage.setData(waiveFieldDTO, accountDTO.getUserName().toLowerCase());
                        System.out.println(stage + ": DONE" + " - Time " + Duration.between(start, Instant.now()).toSeconds());

                        // ========= UPDATE DB ============================
                        Query queryUpdate1 = new Query();
                        queryUpdate1.addCriteria(Criteria.where("_id").is(waiveFieldDTO.getId()).and("status").is(2).and("appId").is(waiveFieldDTO.getAppId()).and("transaction_id").is(waiveFieldDTO.getTransaction_id()).and("reference_id").is(waiveFieldDTO.getReference_id()));
                        Update update1Pass = new Update();
                        update1Pass.set("userAuto", accountDTO.getUserName());
                        update1Pass.set("status", 1);
                        update1Pass.set("automation_result", "WAIVE_FIELD_PASS");
                        mongoTemplate.findAndModify(queryUpdate1, update1Pass, WaiveFieldDTO.class);

                        Thread.sleep(Constant.TIME_OUT_S);

                        Utilities.captureScreenShot(driver);
                        System.out.println("AUTO - WAIVE FIELD" + ": DONE - PASS" + " - Time " + Duration.between(start, Instant.now()).toSeconds());
                    }
                } catch (Exception ex) {
                    Query queryUpdateFaild = new Query();
                    queryUpdateFaild.addCriteria(Criteria.where("_id").is(waiveFieldDTO.getId()).and("appId").is(waiveFieldDTO.getAppId()).and("transaction_id").is(transaction_id).and("reference_id").is(reference_id));
                    Update updateFailed = new Update();
                    updateFailed.set("userAuto", accountDTO.getUserName());
                    updateFailed.set("status", 3);
                    updateFailed.set("automation_result", "WAIVE_FIELD_FAILED");
                    updateFailed.set("automation_result_message", ex.getMessage());
                    mongoTemplate.findAndModify(queryUpdateFaild, updateFailed, WaiveFieldDTO.class);
                    Thread.sleep(Constant.TIME_OUT_S);
                }
            } while (!Objects.isNull(waiveFieldDTO));
        } catch (Exception e) {

            Query queryUpdateFailed2 = new Query();
            queryUpdateFailed2.addCriteria(Criteria.where("transaction_id").is(transaction_id).and("reference_id").is(reference_id));
            Update updateFailed2 = new Update();
            updateFailed2.set("userAuto", accountDTO.getUserName());
            updateFailed2.set("status", 3);
            updateFailed2.set("automation_result", "WAIVE_FIELD_FAILED");
            updateFailed2.set("automation_result_message", e.getMessage());
            mongoTemplate.findAndModify(queryUpdateFailed2, updateFailed2, WaiveFieldDTO.class);
            System.out.println("WAIVE_FIELD: FAILED - MESSAGE: " + e.getMessage() + "\n TRACE: " + e.toString());
            Utilities.captureScreenShot(driver);

        } finally {

            Query queryUpdateFailed = new Query();
            queryUpdateFailed.addCriteria(Criteria.where("transaction_id").is(transaction_id).and("reference_id").is(reference_id));
            List<MobilityFieldReponeDTO> resultRespone = mongoTemplate.find(queryUpdateFailed, MobilityFieldReponeDTO.class);

            responseModel.setReference_id(reference_id);
            responseModel.setProject(projectField);
            responseModel.setTransaction_id(transaction_id);
            responseModel.setData(resultRespone);

            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            logout(driver,accountDTO.getUserName());
            pushAccountToQueue(accountDTO, project);
            try {
                autoUpdateStatusRabbitMobility(responseModel, "updateAutomation");
            } catch (Exception e) {
                System.out.println("UPDATE STATUS RABBIT: FAILED - MESSAGE: " + e.getMessage() + "\n TRACE: " + e.toString());
                e.printStackTrace();
            }
            System.out.println("AUTO - WAIVE FIELD" + ": END" + " - Time " + Duration.between(start, Instant.now()).toSeconds());
        }
    }
    //------------------------ END WAIVE FIELD -----------------------------------------------------

    //------------------------ SUBMIT FIELD -----------------------------------------------------
    private void runAutomation_Submit_Field_run(LoginDTO accountDTO, String browser, String project, String finalTransaction_id, String finalReference_id, String finalProject_id) throws Exception {
        WebDriver driver = null;
        Instant start = Instant.now();
        String stage = "";
        String autoAssign = "";
        int checkUpdateAuto = 0;
        System.out.println("START - Auto: " + accountDTO.getUserName() + " - Time: " + Duration.between(start, Instant.now()).toSeconds());
        SubmitFieldDTO submitFieldDTO = SubmitFieldDTO.builder().build();
        ResponseAutomationModel responseModel = new ResponseAutomationModel();
        try {
            SeleniumGridDriver setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
            driver = setupTestDriver.getDriver();
            //get account run
            stage = "LOGIN FINONE";
            LoginPage loginPage = new LoginPage(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
            loginPage.clickLogin();

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));
            System.out.println("Auto: " + accountDTO.getUserName() + " - " + stage + ": DONE" + " - Time: " + Duration.between(start, Instant.now()).toSeconds());
            Utilities.captureScreenShot(driver);

            Query countQuery = new Query();
            countQuery.addCriteria(Criteria.where("status").is(0)
                    .and("reference_id").is(finalReference_id)
                    .and("transaction_id").is(finalTransaction_id)
                    .and("project").is(finalProject_id)
            );
            checkUpdateAuto = mongoTemplate.find(countQuery, SubmitFieldDTO.class).size();

            do {
                try {
                    Instant startIn = Instant.now();
                    System.out.println("Auto:" + accountDTO.getUserName() + " - BEGIN " + " - Time: " + Duration.between(startIn, Instant.now()).toSeconds());
                    Query query = new Query();
                    query.addCriteria(Criteria.where("status").is(0)
                            .and("reference_id").is(finalReference_id)
                            .and("transaction_id").is(finalTransaction_id)
                            .and("project").is(finalProject_id)
                    );
                    submitFieldDTO = mongoTemplate.findOne(query, SubmitFieldDTO.class);

                    if (!Objects.isNull(submitFieldDTO)) {
                        //update app
                        Query queryUpdate = new Query();
                        queryUpdate.addCriteria(Criteria.where("status").is(0)
                                .and("appId").is(submitFieldDTO.getAppId())
                                .and("reference_id").is(submitFieldDTO.getReference_id())
                                .and("project").is(submitFieldDTO.getProject()));
                        Update update = new Update();
                        update.set("userAuto", accountDTO.getUserName());
                        update.set("status", 2);
                        SubmitFieldDTO resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, SubmitFieldDTO.class);
                        autoAssign = accountDTO.getUserName();

                        if (resultUpdate == null) {
                            continue;
                        }

                        System.out.println("Auto:" + accountDTO.getUserName() + " - GET DONE " + " - " + " App: " + submitFieldDTO.getAppId() + " - User: " + submitFieldDTO.getUserName() + " - Time: " + Duration.between(startIn, Instant.now()).toSeconds());

                        stage = "HOME PAGE";
                        HomePage homePage = new HomePage(driver);
                        System.out.println(stage + ": DONE" + " - Time " + Duration.between(start, Instant.now()).toSeconds());

                        // ========== APPLICATIONS =================
                        homePage.getMenuApplicationElement().click();

                        // ========== CHECK STAGE APPLICATIONS =================
                        stage = "CHECK STAGE APPLICATION";
                        FV_CheckStageApplicationManager fv_CheckStageApplicationManager = new FV_CheckStageApplicationManager(driver);
                        fv_CheckStageApplicationManager.getMenuApplicationElement().click();
                        fv_CheckStageApplicationManager.getApplicationManagerElement().click();
                        await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(driver::getTitle, is("Application Manager"));
                        await("Application Manager Form visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(() -> fv_CheckStageApplicationManager.getApplicationManagerFormElement().isDisplayed());
                        fv_CheckStageApplicationManager.getApplicationNumberElement().sendKeys(submitFieldDTO.getAppId());
                        fv_CheckStageApplicationManager.getSearchApplicationElement().click();
                        await("Table Application Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(() -> fv_CheckStageApplicationManager.getTdApplicationElement().size() > 2);

                        String stageApplication = fv_CheckStageApplicationManager.getTdCheckStageApplicationElement().getText();

                        System.out.println(stage + ": DONE" + " - Time " + Duration.between(start, Instant.now()).toSeconds());
                        if ("FII".equals(stageApplication)){
                            // ========== FIELD VERIFICATION =================
                            stage = "FIELD VERIFICATION";
                            FV_FieldVerificationPage fv_FieldVerificationPage = new FV_FieldVerificationPage(driver);
                            fv_FieldVerificationPage.setData(submitFieldDTO, accountDTO.getUserName().toLowerCase(), downdloadFileURL, start);
                            System.out.println(stage + ": DONE" + " - Time " + Duration.between(start, Instant.now()).toSeconds());
                            stageApplication = "FIV";
                        }
                        if ("FIV".equals(stageApplication)){
                            // ========== FIELD INVESTIGATION VERIFICATION =================
                            stage = "FIELD INVESTIGATION VERIFICATION";
                            FV_FieldInvestigationVerificationPage fv_FieldInvestigationVerificationPage = new FV_FieldInvestigationVerificationPage(driver);
                            fv_FieldInvestigationVerificationPage.setData(submitFieldDTO, downdloadFileURL, accountDTO.getUserName());
                            System.out.println(stage + ": DONE" + " - Time " + Duration.between(start, Instant.now()).toSeconds());
                            stageApplication = "FIC";
                        }
                        if ("FIC".equals(stageApplication)){
                            // ========== FIELD INVESTIGATION DETAILS =================
                            stage = "FIELD INVESTIGATION DETAILS";
                            FV_FieldInvestigationDetailsPage fv_FieldInvestigationDetailsPage = new FV_FieldInvestigationDetailsPage(driver);
                            fv_FieldInvestigationDetailsPage.setData(submitFieldDTO, accountDTO.getUserName().toLowerCase());
                            System.out.println(stage + ": DONE" + " - Time " + Duration.between(start, Instant.now()).toSeconds());
                        } else {
                            await("Stage wrong Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                    .until(() -> "FII".equals(fv_CheckStageApplicationManager.getTdCheckStageApplicationElement().getText()));
                        }

                        // ========= UPDATE DB ============================
                        Query queryUpdate1 = new Query();
                        queryUpdate1.addCriteria(Criteria.where("status").is(2)
                                .and("appId").is(submitFieldDTO.getAppId())
                                .and("userAuto").is(accountDTO.getUserName())
                                .and("reference_id").is(submitFieldDTO.getReference_id())
                                .and("project").is(submitFieldDTO.getProject()));
                        Update update1 = new Update();
                        update1.set("userAuto", accountDTO.getUserName());
                        update1.set("status", 1);
                        update1.set("checkUpdate", 1);
                        update1.set("automation_result", "SUBMIT_FIELD_PASS");
                        update1.set("automation_result_message", "DONE");
                        mongoTemplate.findAndModify(queryUpdate1, update1, SubmitFieldDTO.class);
                        System.out.println("AUTO - SUBMIT FIELD PASS: " + stage + " - " + " App: " + submitFieldDTO.getAppId() + " - User: " + accountDTO.getUserName() + " - Time: " + Duration.between(startIn, Instant.now()).toSeconds());
                    }
                } catch (Exception ex) {
                    Query queryUpdate = new Query();
                    queryUpdate.addCriteria(Criteria.where("status").is(2)
                            .and("appId").is(submitFieldDTO.getAppId())
                            .and("userAuto").is(accountDTO.getUserName())
                            .and("reference_id").is(submitFieldDTO.getReference_id())
                            .and("project").is(submitFieldDTO.getProject()));
                    Update update = new Update();
                    update.set("userAuto", accountDTO.getUserName());
                    update.set("status", 3);
                    update.set("checkUpdate", 1);
                    update.set("automation_result", "SUBMIT_FIELD_FAILED");
                    update.set("automation_result_message", ex.getMessage());
                    mongoTemplate.findAndModify(queryUpdate, update, SubmitFieldDTO.class);
                    System.out.println("AUTO - SUBMIT FIELD FAILED: " + ex.getMessage() + " - " + stage + " - " + " App: " + submitFieldDTO.getAppId() + " - User: " + accountDTO.getUserName());
                    Utilities.captureScreenShot(driver);
                }
            } while (!Objects.isNull(submitFieldDTO));
        } catch (Exception e) {
            System.out.println("User Auto:" + accountDTO.getUserName() + " - " + stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();
            Utilities.captureScreenShot(driver);

            Query queryUpdate = new Query();
            queryUpdate.addCriteria(Criteria.where("status").is(0)
                    .and("appId").is(submitFieldDTO.getAppId())
                    .and("userAuto").is(accountDTO.getUserName())
                    .and("reference_id").is(submitFieldDTO.getReference_id())
                    .and("project").is(submitFieldDTO.getProject()));
            Update update = new Update();
            update.set("userAuto", accountDTO.getUserName());
            update.set("status", 3);
            update.set("automation_result", "SUBMIT_FIELD_FAILED");
            update.set("automation_result_message", e.getMessage());
            mongoTemplate.findAndModify(queryUpdate, update, SubmitFieldDTO.class);
            System.out.println("AUTO - SUBMIT FIELD FAILED: " + e.getMessage() + " - " + stage + " - " + " App: " + submitFieldDTO.getAppId() + " - User: " + accountDTO.getUserName());
            Utilities.captureScreenShot(driver);

        } finally {
            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            logout(driver,accountDTO.getUserName());
            pushAccountToQueue(accountDTO, project);
            if (accountDTO.getUserName().equals(autoAssign)){
                Query queryUpdateFailed = new Query();
                queryUpdateFailed.addCriteria(Criteria.where("reference_id").is(finalReference_id)
                        .and("transaction_id").is(finalTransaction_id)
                        .and("project").is(finalProject_id)
                        .and("checkUpdate").is(1)
                );
                List<MobilityFieldReponeDTO> resultRespone = mongoTemplate.find(queryUpdateFailed, MobilityFieldReponeDTO.class);

                if (resultRespone.size() == checkUpdateAuto){
                    responseModel.setReference_id(finalReference_id);
                    responseModel.setProject(finalProject_id);
                    responseModel.setTransaction_id("transaction_submit_field");
                    responseModel.setData(resultRespone);
                    autoUpdateStatusRabbitMobility(responseModel, "updateAutomation");
                }
            }
            System.out.println("AUTO - SUBMIT FIELD" + ": END" + " - Time " + Duration.between(start, Instant.now()).toSeconds());
        }
    }

    private void runAutomation_Submit_Field_run_Old(String browser, LoginDTO accountDTO, String project, Map<String, Object> mapValue) {
        WebDriver driver = null;
        ResponseAutomationModel responseModel = new ResponseAutomationModel();
        Instant start = Instant.now();
        String stage = "";
        String transaction_id = "";
        String reference_id = "";
        String projectField = "";
        SubmitFieldDTO submitFieldDTO = SubmitFieldDTO.builder().build();
        List<SubmitFieldDTO> checkResultExist = null;
        System.out.println("Auto - SUBMIT FIELD" + ": START" + " - Time " + Duration.between(start, Instant.now()).toSeconds());
        try {
            RequestAutomationDTO RequestAutomationSubmitFieldDTOList = (RequestAutomationDTO) mapValue.get("RequestAutomationSubmitFieldList");
            transaction_id = RequestAutomationSubmitFieldDTOList.getTransaction_id();
            reference_id = RequestAutomationSubmitFieldDTOList.getReference_id();
            projectField = RequestAutomationSubmitFieldDTOList.getProject();
            List<SubmitFieldDTO> submitFieldDTOList = RequestAutomationSubmitFieldDTOList.getSubmitFieldDTO();
            for (int i = 0; i < submitFieldDTOList.size(); i++){
                submitFieldDTOList.get(i).setProject(projectField);
                submitFieldDTOList.get(i).setTransaction_id(transaction_id);
                submitFieldDTOList.get(i).setReference_id(reference_id);
            }

            SeleniumGridDriver setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
            driver = setupTestDriver.getDriver();

            stage = "LOGIN FINONE";
            LoginPage loginPage = new LoginPage(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
            loginPage.clickLogin();
            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            Query query = new Query();
            query.addCriteria(Criteria.where("transaction_id").is(transaction_id).and("reference_id").is(reference_id).and("project").is(projectField));
            checkResultExist = mongoTemplate.find(query, SubmitFieldDTO.class);
            if (checkResultExist.size() < 1){
                mongoTemplate.insert(submitFieldDTOList, WaiveFieldDTO.class);
            }

            do {
                try {
                    query = new Query();
                    query.addCriteria(Criteria.where("status").is(0).and("transaction_id").is(transaction_id).and("reference_id").is(reference_id).and("project").is(projectField));
                    submitFieldDTO = mongoTemplate.findOne(query, SubmitFieldDTO.class);
                    if (!Objects.isNull(submitFieldDTO)) {

                        //update app
                        Query queryUpdate = new Query();
                        queryUpdate.addCriteria(Criteria.where("_id").is(new ObjectId(submitFieldDTO.getId())).and("status").is(0).and("appId").is(submitFieldDTO.getAppId()).and("transaction_id").is(submitFieldDTO.getTransaction_id()).and("reference_id").is(submitFieldDTO.getReference_id()));

                        Update updateFirst2 = new Update();
                        updateFirst2.set("userAuto", accountDTO.getUserName());
                        updateFirst2.set("status", 2);
                        updateFirst2.set("transaction_id", transaction_id);
                        updateFirst2.set("reference_id", reference_id);
                        updateFirst2.set("project", projectField);
                        updateFirst2.set("automation_result", "SUBMIT_FIELD_RUN");
                        SubmitFieldDTO resultUpdate = mongoTemplate.findAndModify(queryUpdate, updateFirst2, SubmitFieldDTO.class);

                        if (resultUpdate == null) {
                            continue;
                        }

                        stage = "HOME PAGE";
                        HomePage homePage = new HomePage(driver);
                        System.out.println(stage + ": DONE" + " - Time " + Duration.between(start, Instant.now()).toSeconds());

                        // ========== APPLICATIONS =================
                        homePage.getMenuApplicationElement().click();

                        // ========== CHECK STAGE APPLICATIONS =================
                        stage = "CHECK STAGE APPLICATION";
                        FV_CheckStageApplicationManager fv_CheckStageApplicationManager = new FV_CheckStageApplicationManager(driver);
                        fv_CheckStageApplicationManager.getMenuApplicationElement().click();
                        fv_CheckStageApplicationManager.getApplicationManagerElement().click();
                        await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(driver::getTitle, is("Application Manager"));
                        await("Application Manager Form visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(() -> fv_CheckStageApplicationManager.getApplicationManagerFormElement().isDisplayed());
                        fv_CheckStageApplicationManager.getApplicationNumberElement().sendKeys(submitFieldDTO.getAppId());
                        fv_CheckStageApplicationManager.getSearchApplicationElement().click();
                        await("Table Application Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(() -> fv_CheckStageApplicationManager.getTdApplicationElement().size() > 2);

                        String stageApplication = fv_CheckStageApplicationManager.getTdCheckStageApplicationElement().getText();

                        System.out.println(stage + ": DONE" + " - Time " + Duration.between(start, Instant.now()).toSeconds());
                        if ("FII".equals(stageApplication)){
                            // ========== FIELD VERIFICATION =================
                            stage = "FIELD VERIFICATION";
                            FV_FieldVerificationPage fv_FieldVerificationPage = new FV_FieldVerificationPage(driver);
                            fv_FieldVerificationPage.setData(submitFieldDTO, accountDTO.getUserName().toLowerCase(), downdloadFileURL, start);
                            System.out.println(stage + ": DONE" + " - Time " + Duration.between(start, Instant.now()).toSeconds());
                            stageApplication = "FIV";
                        }
                        if ("FIV".equals(stageApplication)){
                            // ========== FIELD INVESTIGATION VERIFICATION =================
                            stage = "FIELD INVESTIGATION VERIFICATION";
                            FV_FieldInvestigationVerificationPage fv_FieldInvestigationVerificationPage = new FV_FieldInvestigationVerificationPage(driver);
                            fv_FieldInvestigationVerificationPage.setData(submitFieldDTO, downdloadFileURL, accountDTO.getUserName());
                            System.out.println(stage + ": DONE" + " - Time " + Duration.between(start, Instant.now()).toSeconds());
                            stageApplication = "FIC";
                        }
                        if ("FIC".equals(stageApplication)){
                            // ========== FIELD INVESTIGATION DETAILS =================
                            stage = "FIELD INVESTIGATION DETAILS";
                            FV_FieldInvestigationDetailsPage fv_FieldInvestigationDetailsPage = new FV_FieldInvestigationDetailsPage(driver);
                            fv_FieldInvestigationDetailsPage.setData(submitFieldDTO, accountDTO.getUserName().toLowerCase());
                            System.out.println(stage + ": DONE" + " - Time " + Duration.between(start, Instant.now()).toSeconds());
                        } else {
                            await("Stage wrong Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                    .until(() -> "FII".equals(fv_CheckStageApplicationManager.getTdCheckStageApplicationElement().getText()));
                        }

                        // ========= UPDATE DB ============================
                        Query queryUpdate1 = new Query();
                        queryUpdate1.addCriteria(Criteria.where("appId").is(new ObjectId(submitFieldDTO.getAppId())).and("status").is(2).and("transaction_id").is(transaction_id).and("reference_id").is(reference_id));
                        Update update1Pass = new Update();
                        update1Pass.set("userAuto", accountDTO.getUserName());
                        update1Pass.set("status", 1);
                        update1Pass.set("automation_result", "SUBMIT_FIELD_PASS");
                        mongoTemplate.findAndModify(queryUpdate1, update1Pass, SubmitFieldDTO.class);

                        Thread.sleep(Constant.TIME_OUT_S);

                        Utilities.captureScreenShot(driver);
                        System.out.println("Auto - SUBMIT FIELD" + ": FINISH - PASS" + " - Time " + Duration.between(start, Instant.now()).toSeconds());
                    }
                } catch (Exception ex) {
                    Query queryUpdateFailed = new Query();
                    queryUpdateFailed.addCriteria(Criteria.where("_id").is(new ObjectId(submitFieldDTO.getId())).and("appId").is(submitFieldDTO.getAppId()).and("transaction_id").is(transaction_id).and("reference_id").is(reference_id));
                    Update updateFailed = new Update();
                    updateFailed.set("userAuto", accountDTO.getUserName());
                    updateFailed.set("status", 3);
                    updateFailed.set("automation_result", "SUBMIT_FIELD_FAILED");
                    updateFailed.set("automation_result_message", ex.getMessage());
                    mongoTemplate.findAndModify(queryUpdateFailed, updateFailed, SubmitFieldDTO.class);
                    Thread.sleep(Constant.TIME_OUT_S);

                    System.out.println(ex.getMessage());

                    System.out.println("Auto - SUBMIT FIELD" + ": FINISH - FAILED" + " - Time " + Duration.between(start, Instant.now()).toSeconds());
                }
            } while (!Objects.isNull(submitFieldDTO));
        } catch (Exception e) {

            Query queryUpdateFaild = new Query();
            queryUpdateFaild.addCriteria(Criteria.where("appId").is(submitFieldDTO.getAppId()).and("transaction_id").is(transaction_id).and("reference_id").is(reference_id));
            Update updateFailed2 = new Update();
            updateFailed2.set("userAuto", accountDTO.getUserName());
            updateFailed2.set("status", 3);
            updateFailed2.set("automation_result", "SUBMIT_FIELD_FAILED");
            updateFailed2.set("automation_result_message", e.getMessage());
            mongoTemplate.findAndModify(queryUpdateFaild, updateFailed2, SubmitFieldDTO.class);


            System.out.println("User Auto:" + accountDTO.getUserName() + " - " + stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);

            System.out.println("Auto - SUBMIT FIELD" + ": FINISH - FAILED" + " - Time " + Duration.between(start, Instant.now()).toSeconds());
        } finally {
            Query queryUpdateFailed = new Query();
            queryUpdateFailed.addCriteria(Criteria.where("transaction_id").is(transaction_id).and("reference_id").is(reference_id));
            List<MobilityFieldReponeDTO> resultRespone = mongoTemplate.find(queryUpdateFailed, MobilityFieldReponeDTO.class);

            responseModel.setReference_id(reference_id);
            responseModel.setProject(projectField);
            responseModel.setTransaction_id(transaction_id);
            responseModel.setData(resultRespone);


            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            logout(driver,accountDTO.getUserName());
            pushAccountToQueue(accountDTO, project);
            try {
                autoUpdateStatusRabbitMobility(responseModel, "updateAutomation");
            } catch (Exception e) {
                System.out.println("UPDATE STATUS RABBIT: FAILED - MESSAGE: " + e.getMessage() + "\n TRACE: " + e.toString());
                e.printStackTrace();
            }
            System.out.println("Auto - SUBMIT FIELD" + ": END" + " - Time " + Duration.between(start, Instant.now()).toSeconds());
        }
    }

    //------------------------ END SUBMIT FIELD -----------------------------------------------------

    //------------------------ START UPDATE RABBITMQ -----------------------------------------------------
    private void autoUpdateStatusRabbitMobility(ResponseAutomationModel responseAutomationModel, String func) throws Exception {
        JsonNode jsonNode = rabbitMQService.sendAndReceive("tpf-service-esb",
                Map.of("func", func,
                        "body", Map.of(
                                "project", responseAutomationModel.getProject(),
                                "transaction_id", responseAutomationModel.getTransaction_id(),
                                "reference_id", responseAutomationModel.getReference_id(),
                                "data",responseAutomationModel.getData()
                        )));

        System.out.println("rabit:=>" + jsonNode.toString());
    }

    //------------------------ END UPDATE RABBITMQ -----------------------------------------------------

    //------------------------ PROJECT CRM -----------------------------------------------------
    //------------------------ QUICKLEAD CRM -----------------------------------------------------
    public void CRM_runAutomation_QuickLead(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO, String project) throws Exception {
        Instant start = Instant.now();
        String stage = "";
        CRM_ExistingCustomerDTO application = CRM_ExistingCustomerDTO.builder().build();
        log.info("{}", application);
        try {
            stage = "INIT DATA";
            //*************************** GET DATA *********************//
            application = (CRM_ExistingCustomerDTO) mapValue.get("ApplicationDTO");
            application.setAutomationAcc(accountDTO.getUserName());
            CRM_QuickLeadDTO quickLead = application.getQuickLead();
            //mongoTemplate.save(application);


            //*************************** END GET DATA *********************//
            System.out.println(stage + ": DONE");
            stage = "LOGIN FINONE";
            LoginPage loginPage = new LoginPage(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
            loginPage.clickLogin();

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));


            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "HOME PAGE";
            HomePage homePage = new HomePage(driver);


            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "QUICK LEAD";
            // ========== QUICK LEAD =================
            homePage.menuClick();
            homePage.leadQuickClick();

            await("Quick Lead Entry timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Quick Lead Entry"));

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "QUICK LEAD (DOCUMENT UPLOAD & APPLICATION CREATION)";
            CRM_QuickLeadPage quickLeadPage = new CRM_QuickLeadPage(driver);
            quickLeadPage.setData(quickLead);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "LEADS GRID";
            // ========== LEAD PAGE =================
            LeadsPage leadsPage = new LeadsPage(driver);
            await("Quick Lead Entry timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Lead Grid"));

            await("notifyTextElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadsPage.getNotifyTextElement().isEnabled() && leadsPage.getNotifyTextElement().isDisplayed());

            String notify = leadsPage.getNotifyTextElement().getText();
            String leadApp = "";
            if (notify.contains("LEAD")) {
                leadApp = notify.substring(notify.indexOf("LEAD"), notify.length());
            }

            System.out.println("LEAD APP: =>" + leadApp);
            leadsPage.setData(leadApp);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "LEAD STAGE";
            // ========== LEAD STAGE =================
            CRM_LeadDetailPage leadDetailPage = new CRM_LeadDetailPage(driver);
            await("contentElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadDetailPage.getContentElement().isDisplayed());

            leadDetailPage.setData(quickLead, leadApp, downdloadFileURL);

            Utilities.captureScreenShot(driver);

            await("Lead Page timeout").atMost(Constant.TIME_OUT_5_M, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Lead Grid"));

            leadsPage.getSpanAllNotifyElement().click();
            await("getDivAllNotifyElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadsPage.getDivAllNotifyElement().isEnabled() && leadsPage.getDivAllNotifyElement().isDisplayed());

            await("getBtnAllNotifyElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadsPage.getBtnAllNotifyElement().isEnabled() && leadsPage.getBtnAllNotifyElement().isDisplayed());
            leadsPage.getBtnAllNotifyElement().click();

            Utilities.captureScreenShot(driver);
            await("getBtnAllNotifyElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadsPage.getNotifyTextSuccessElement().size() > 0);

            String leadAppID = "";
            stage = "APPLICATION MANAGER";
            // ========== APPLICATION MANAGER =================
            homePage.getMenuApplicationElement().click();
            homePage.getApplicationManagerElement().click();
            await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Manager"));

            DE_ApplicationManagerPage de_applicationManagerPage = new DE_ApplicationManagerPage(driver);

            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> de_applicationManagerPage.getApplicationManagerFormElement().isDisplayed());

            //get appID moi o day
            leadAppID = de_applicationManagerPage.getAppID(leadApp);
            System.out.println(" APP: =>" + leadAppID);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            //-------------------- END ---------------------------

            application.setApplicationId(leadAppID);
            application.setLeadApp(leadApp);

            //UPDATE STATUS
            application.setStatus("QUICKLEAD PASS");
            application.setDescription("Thanh cong");


            Utilities.captureScreenShot(driver);
            //logout(driver);

        } catch (Exception e) {
            //UPDATE STATUS
            application.setStatus("QUICKLEAD FAIL");
            application.setStage(stage);
            application.setDescription(e.getMessage());

            System.out.println(stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);

            if (e.getMessage().contains("Work flow failed!!!")) {
                stage = "END OF LEAD DETAIL";

                await("Get error fail!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> driver.findElements(By.id("error-message")).size() > 0);

                if (driver.findElements(By.id("error-message")) != null && driver.findElements(By.id("error-message")).size() > 0) {
                    String error = "Error: ";
                    for (WebElement we : driver.findElements(By.id("error-message"))) {
                        error += " - " + we.getText();
                    }
                    System.out.println(stage + "=>" + error);
                }
            }
        } finally {
            if (application.getApplicationId() == null || application.getApplicationId().isEmpty() || application.getApplicationId().indexOf("LEAD") > 0 || application.getApplicationId().indexOf("APPL") < 0) {
                application.setApplicationId("UNKNOW");
                application.setStatus("QUICKLEAD_FAILED");
                application.setDescription("Khong thanh cong");
            }

            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            try {
                CRM_updateStatusRabbit(application, "updateAutomation", project);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            CRM_updateDB(application);
            logout(driver,accountDTO.getUserName());

        }
    }

    private void CRM_updateStatusRabbit(CRM_ExistingCustomerDTO application, String func, String project) throws Exception {

        JsonNode jsonNode = rabbitMQService.sendAndReceive("tpf-service-esb",
                Map.of("func", func, "reference_id", application.getReference_id(), "body", Map.of("app_id", application.getApplicationId() != null ? application.getApplicationId() : "",
                        "project", project,
                        "automation_result", application.getStatus(),
                        "description", application.getDescription() != null ? application.getDescription() : "",
                        "transaction_id", application.getQuickLeadId(),
                        "automation_account", application.getAutomationAcc())));
        System.out.println("rabit:=>" + jsonNode.toString());

    }

    private void CRM_updateDB(CRM_ExistingCustomerDTO application) throws Exception {

        mongoTemplate.save(application);

    }


    //------------------------ CRM QUICKLEAD WITH CUSTID -----------------------------------------------------
    public void CRM_runAutomation_QuickLead_With_CustID(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO) throws Exception {
        ResponseAutomationModel responseModel = new ResponseAutomationModel();
        Instant start = Instant.now();
        String stage = "";
        String applicationId = "";
        String neoCustNo = "";
        String cifNo = "";
        String idNo = "";
        CRM_ExistingCustomerDTO existingCustomerDTO = CRM_ExistingCustomerDTO.builder().build();
        try {
            stage = "INIT DATA";
            //*************************** GET DATA *********************//
            existingCustomerDTO = (CRM_ExistingCustomerDTO) mapValue.get("QuickLeadAppWithCustIDList");
            existingCustomerDTO.setAutomationAcc(accountDTO.getUserName());

            log.info("{}", existingCustomerDTO);
            //*************************** END GET DATA *********************//
            System.out.println(stage + ": DONE");

            stage = "LOGIN FINONE";

            LoginPage loginPage = new LoginPage(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
            loginPage.clickLogin();

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ========== EXISTING CUSTOMER =================
            stage = "EXISTING CUSTOMER";
            CRM_ExistingCustomerPage crm_ExistingCustomerPage = new CRM_ExistingCustomerPage(driver);
            crm_ExistingCustomerPage.getMenuApplicationElement().click();
            crm_ExistingCustomerPage.getPersonalLoanElement().click();

            await("Personal Loan Page displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> crm_ExistingCustomerPage.getCreateCustomerElement().isDisplayed());

            System.out.println("PERSONAL LOAN PAGE");

            crm_ExistingCustomerPage.getIsExistCustomerRadioElement().click();

            await("Search Existing Individual Customers With displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> crm_ExistingCustomerPage.getExistingCustomerSearchFormElement().isDisplayed());

            if(!Objects.isNull(existingCustomerDTO.getNeoCustID())){
                if(!"null".equals(existingCustomerDTO.getNeoCustID().toLowerCase())){
                    await("Neo Cust ID Text Box displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                            .until(() -> crm_ExistingCustomerPage.getNeoCustIDInputElement().isDisplayed());

                    crm_ExistingCustomerPage.getNeoCustIDInputElement().sendKeys(existingCustomerDTO.getNeoCustID());
                }
            }

            if(!Objects.isNull(existingCustomerDTO.getCifNumber())){
                if (!"null".equals(existingCustomerDTO.getCifNumber().toLowerCase())){
                    await("CIF Number Text Box displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                            .until(() -> crm_ExistingCustomerPage.getCifNumberInputElement().isDisplayed());

                    crm_ExistingCustomerPage.getCifNumberInputElement().sendKeys(existingCustomerDTO.getCifNumber());
                }
            }

            if(!Objects.isNull(existingCustomerDTO.getIdNumber())){
                if (!"null".equals(existingCustomerDTO.getIdNumber().toLowerCase())){
                    crm_ExistingCustomerPage.getIdentificationTypeInputElement().sendKeys("Current National ID");

                    await("Identification Type Ul displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                            .until(() -> crm_ExistingCustomerPage.getIdentificationTypeUlElement().isDisplayed());

                    await("Identification Type Li loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                            .until(() -> crm_ExistingCustomerPage.getIdentificationTypeLiElement().size() > 0);

                    for (WebElement e : crm_ExistingCustomerPage.getIdentificationTypeLiElement()) {
                        if (!Objects.isNull(e.getText()) && StringEscapeUtils.unescapeJava(e.getText()).equals("Current National ID")) {
                            e.click();
                            break;
                        }
                    }

                    await("ID Number Text Box displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                            .until(() -> crm_ExistingCustomerPage.getIdNumberInputElement().isDisplayed());

                    crm_ExistingCustomerPage.getIdNumberInputElement().sendKeys(existingCustomerDTO.getIdNumber());
                }
            }

            await("Search displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> crm_ExistingCustomerPage.getSearchCustomerButtonElement().isDisplayed());

            crm_ExistingCustomerPage.getSearchCustomerButtonElement().click();

            await("Not Existing Customer!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> crm_ExistingCustomerPage.getSearchCustomerTableElement().size() > 2);

            int tableSize = crm_ExistingCustomerPage.getSearchCustomerTableSizeElement().size();

            WebElement searchCustomerSelectElement = driver.findElement(new By.ByXPath("//div[@id = 'existingCustomerSearch']//form[starts-with(@id, 'applicantSearchVoForm')]//div[@id = 'example']//table[@id = 'searchData_IndividualCustomerTable']//tbody//tr["+tableSize+"]//td[contains(@class, 'select_individual')]//input[contains(@value,'Select')]"));

            await("Button Select displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> searchCustomerSelectElement.isDisplayed());

            searchCustomerSelectElement.click();

            /*await("Button Select displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> crm_ExistingCustomerPage.getSearchCustomerSelectElement().isDisplayed());
            crm_ExistingCustomerPage.getSearchCustomerSelectElement().click();*/

            await("Customer Information displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> crm_ExistingCustomerPage.getCustomerInformationFormElement().isDisplayed());

            await("Customer Information Save button displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> crm_ExistingCustomerPage.getCustomerInformationSaveElement().isDisplayed());

            crm_ExistingCustomerPage.getCustomerInformationSaveElement().click();

            Utilities.captureScreenShot(driver);

            await("Work flow failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> crm_ExistingCustomerPage.getPrimaryApplicantElement().isDisplayed());

            neoCustNo = crm_ExistingCustomerPage.getPrimaryApplicantNeoCustIDInputElement().getAttribute("value");
            System.out.println("NEO CUST ID => " + neoCustNo);
            idNo = crm_ExistingCustomerPage.getPrimaryApplicantIdNumberInputElement().getAttribute("value");
            String idNoo = idNo.substring(idNo.indexOf(":")+1).trim();
            System.out.println("ID Number => " + idNoo);
            cifNo = crm_ExistingCustomerPage.getPrimaryApplicantNeoCifNumberInputElement().getAttribute("value");
            System.out.println("CIF Number => " + cifNo);
            applicationId = crm_ExistingCustomerPage.getApplicantIdHeaderElement().getText();
            System.out.println("APPID => " + applicationId);

            existingCustomerDTO.setApplicationId(applicationId);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "COMPLETE";
            System.out.println("AUTO OK: user =>" + accountDTO.getUserName());

            System.out.println("Auto - FINISH: " + stage + " - " + Duration.between(start, Instant.now()).toSeconds());
            Utilities.captureScreenShot(driver);

            responseModel.setProject(existingCustomerDTO.getProject());
            responseModel.setReference_id(existingCustomerDTO.getReference_id());
            responseModel.setTransaction_id(existingCustomerDTO.getQuickLeadId());
            responseModel.setApp_id(applicationId);
            responseModel.setAutomation_result("QUICKLEAD PASS");

            Utilities.captureScreenShot(driver);

        } catch (Exception e) {

            responseModel.setProject(existingCustomerDTO.getProject());
            responseModel.setReference_id(existingCustomerDTO.getReference_id());
            responseModel.setTransaction_id(existingCustomerDTO.getQuickLeadId());
            responseModel.setApp_id(applicationId);
            responseModel.setAutomation_result("QUICKLEAD FAILED" + " - " + e.getMessage());

            System.out.println("Auto Error:" + stage + "\n => MESSAGE " + e.getMessage() + " => TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);
        } finally {
            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            System.out.println("Auto DONE: " + responseModel.getAutomation_result() + " => Project: " + responseModel.getProject() + " => AppId: " +responseModel.getApp_id());
            logout(driver,accountDTO.getUserName());
            updateQuickleadExistingCustomer(existingCustomerDTO);
            autoUpdateStatusRabbit(responseModel, "updateAutomation");
        }
    }
    //------------------------ END - CRM QUICKLEAD WITH CUSTID -----------------------------------------------------

    //------------------------ EXISTING CUSTOMER -----------------------------------------------------

    public void runAutomation_Existing_Customer(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO) throws Exception {
        ResponseAutomationModel responseModel = new ResponseAutomationModel();
        Instant start = Instant.now();
        String stage = "";
        String applicationId = "";
        String neoCustNo = "";
        String cifNo = "";
        String idNo = "";
        CRM_ExistingCustomerDTO existingCustomerDTO = CRM_ExistingCustomerDTO.builder().build();
        SessionId session = ((RemoteWebDriver)driver).getSessionId();
        try {
            stage = "INIT DATA";
            //*************************** GET DATA *********************//
            existingCustomerDTO = (CRM_ExistingCustomerDTO) mapValue.get("ExistingCustomerList");
            CRM_ApplicationInformationsListDTO applicationInfoDTO = (CRM_ApplicationInformationsListDTO) mapValue.get("ApplicationInfoDTO");
            CRM_LoanDetailsDTO loanDetailsDTO = (CRM_LoanDetailsDTO) mapValue.get("VapDetailsDTO");
            List<CRM_DocumentsDTO> documentDTOS = (List<CRM_DocumentsDTO>) mapValue.get("DocumentDTO");
            List<CRM_ReferencesListDTO> referenceDTO = (List<CRM_ReferencesListDTO>) mapValue.get("ReferenceDTO");
            CRM_DynamicFormDTO miscFrmAppDtlDTO = (CRM_DynamicFormDTO) mapValue.get("MiscFrmAppDtlDTO");
            log.info("{}", existingCustomerDTO);
            //*************************** END GET DATA *********************//
            System.out.println(stage + ": DONE");

            stage = "LOGIN FINONE";

            LoginPage loginPage = new LoginPage(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
            loginPage.clickLogin();

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ========== EXISTING CUSTOMER =================
            stage = "EXISTING CUSTOMER";
            CRM_ExistingCustomerPage crm_ExistingCustomerPage = new CRM_ExistingCustomerPage(driver);
            crm_ExistingCustomerPage.getMenuApplicationElement().click();
            crm_ExistingCustomerPage.getPersonalLoanElement().click();

            await("Personal Loan Page displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> crm_ExistingCustomerPage.getCreateCustomerElement().isDisplayed());

            System.out.println("PERSONAL LOAN PAGE");

            crm_ExistingCustomerPage.getIsExistCustomerRadioElement().click();

            await("Search Existing Individual Customers With displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> crm_ExistingCustomerPage.getExistingCustomerSearchFormElement().isDisplayed());

            if(!Objects.isNull(existingCustomerDTO.getNeoCustID())){
                await("Neo Cust ID Text Box displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> crm_ExistingCustomerPage.getNeoCustIDInputElement().isDisplayed());

                crm_ExistingCustomerPage.getNeoCustIDInputElement().sendKeys(existingCustomerDTO.getNeoCustID());

            }

            if(!Objects.isNull(existingCustomerDTO.getCifNumber())){
                await("CIF Number Text Box displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> crm_ExistingCustomerPage.getCifNumberInputElement().isDisplayed());

                crm_ExistingCustomerPage.getCifNumberInputElement().sendKeys(existingCustomerDTO.getCifNumber());

            }

            if(!Objects.isNull(existingCustomerDTO.getIdNumber())){

                crm_ExistingCustomerPage.getIdentificationTypeInputElement().sendKeys("Current National ID");

                await("Identification Type Ul displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> crm_ExistingCustomerPage.getIdentificationTypeUlElement().isDisplayed());

                await("Identification Type Li loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> crm_ExistingCustomerPage.getIdentificationTypeLiElement().size() > 0);

                for (WebElement e : crm_ExistingCustomerPage.getIdentificationTypeLiElement()) {
                    if (!Objects.isNull(e.getText()) && StringEscapeUtils.unescapeJava(e.getText()).equals("Current National ID")) {
                        e.click();
                        break;
                    }
                }

                await("ID Number Text Box displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> crm_ExistingCustomerPage.getIdNumberInputElement().isDisplayed());

                crm_ExistingCustomerPage.getIdNumberInputElement().sendKeys(existingCustomerDTO.getIdNumber());

            }

            await("Search displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> crm_ExistingCustomerPage.getSearchCustomerButtonElement().isDisplayed());

            crm_ExistingCustomerPage.getSearchCustomerButtonElement().click();

            await("Not Existing Customer!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> crm_ExistingCustomerPage.getSearchCustomerTableElement().size() > 2);

            int tableSize = crm_ExistingCustomerPage.getSearchCustomerTableSizeElement().size();

            WebElement searchCustomerSelectElement = driver.findElement(new By.ByXPath("//div[@id = 'existingCustomerSearch']//form[starts-with(@id, 'applicantSearchVoForm')]//div[@id = 'example']//table[@id = 'searchData_IndividualCustomerTable']//tbody//tr["+tableSize+"]//td[contains(@class, 'select_individual')]//input[contains(@value,'Select')]"));

            await("Button Select displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> searchCustomerSelectElement.isDisplayed());

            searchCustomerSelectElement.click();

            await("Customer Information displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> crm_ExistingCustomerPage.getCustomerInformationFormElement().isDisplayed());

            await("Customer Information Save button displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> crm_ExistingCustomerPage.getCustomerInformationSaveElement().isDisplayed());

            crm_ExistingCustomerPage.getCustomerInformationSaveElement().click();

            Utilities.captureScreenShot(driver);

            await("Work flow failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> crm_ExistingCustomerPage.getPrimaryApplicantElement().isDisplayed());

            neoCustNo = crm_ExistingCustomerPage.getPrimaryApplicantNeoCustIDInputElement().getAttribute("value");
            System.out.println("NEO CUST ID => " + neoCustNo);
            idNo = crm_ExistingCustomerPage.getPrimaryApplicantIdNumberInputElement().getAttribute("value");
            String idNoo = idNo.substring(idNo.indexOf(":")+1).trim();
            System.out.println("ID Number => " + idNoo);
            cifNo = crm_ExistingCustomerPage.getPrimaryApplicantNeoCifNumberInputElement().getAttribute("value");
            System.out.println("CIF Number => " + cifNo);
            applicationId = crm_ExistingCustomerPage.getApplicantIdHeaderElement().getText();
            System.out.println("APPID => " + applicationId);

            // ========== VIEW/EDIT DETAILED INFORMATION =================
            stage = "VIEW/EDIT DETAILED INFORMATION";
            crm_ExistingCustomerPage.getEditCustomerExistCustomerElement().click();
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ========== APPLICANT INFORMATION =================
            // ========== PERSONAL INFORMATION =================
            stage = "PERSONAL INFORMATION";
            CRM_ApplicationInfoPage appInfoPage = new CRM_ApplicationInfoPage(driver);
            CRM_ApplicationInfoPersonalTab personalTab = appInfoPage.getApplicationInfoPersonalTab();

            await("Load Personal Info tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> appInfoPage.getPersonalInfoTabElement().getAttribute("class").contains("active"));

            Utilities.captureScreenShot(driver);

            await("getPersonalCustomerDetailsElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> personalTab.getPersonalCustomerDetailsElement().isDisplayed());

            personalTab.setValue(applicationInfoDTO);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "EMPLOYMENT DETAILS";
            // ========== EMPLOYMENT DETAILS =================
            await("Load employment details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> appInfoPage.getEmploymentDetailsTabElement().getAttribute("class").contains("active"));

            CRM_ApplicationInfoEmploymentDetailsTab employmentDetailsTab = appInfoPage.getApplicationInfoEmploymentDetailsTab();
            await("Span Application Id not enabled Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> employmentDetailsTab.getApplicationId().isEnabled());

            employmentDetailsTab.setData(applicationInfoDTO.getEmploymentDetail());
            employmentDetailsTab.getDoneBtnElement().click();
            await("Employment Status loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> employmentDetailsTab.getTableAfterDoneElement().isDisplayed());
            employmentDetailsTab.setMajorOccupation(applicationInfoDTO.getEmploymentDetail());
            employmentDetailsTab.getSaveAndNextBtnElement().click();

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "FINANCIAL";
            // ==========FINANCIAL DETAILS =================
            if (applicationInfoDTO.getFinancialDetail() != null) {
                Thread.sleep(15000);
                CRM_ApplicationInfoFinancialDetailsTab financialDetailsTab = appInfoPage.getApplicationInfoFinancialDetailsTab();
                financialDetailsTab.openFinancialDetailsTabSection();
                financialDetailsTab.openIncomeDetailSection();
                await("Load financial details - income details Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> financialDetailsTab.getIncomeDetailDivElement().isDisplayed());
                financialDetailsTab.setIncomeDetailsData(applicationInfoDTO.getFinancialDetail());
                financialDetailsTab.saveAndNext();
            }

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "BANK / CREDIT CARD DETAILS";
            // ==========BANK DETAILS =================
            await("Load Bank details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> appInfoPage.getBankDetailsTabElement().getAttribute("class").contains("active"));

            CRM_ApplicationInfoBankDetailsTab bankDetailsTab = appInfoPage.getApplicationInfoBankDetailsTab();

            await("Span Application Id not enabled Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> bankDetailsTab.getApplicationId().isEnabled());

            await("Span Application Id not enabled Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> bankDetailsTab.getBankDetailsGridElement().isEnabled());

            if(bankDetailsTab.getBankDetailsTableElement().size() > 2){

                await("Span Application Id not enabled Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> bankDetailsTab.getBankDetailsTableElement().size() > 2);

                await("Load deleteIdDetailElement Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> bankDetailsTab.getDeleteIdDetailElement().size() > 0);

                for (int i=0; i<bankDetailsTab.getDeleteIdDetailElement().size(); i++) {
                    WebElement var = bankDetailsTab.getDeleteIdDetailElement().get(i);
                    var.click();
                }
            }

            bankDetailsTab.saveAndNext();

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ==========LOAN DETAILS=================
            stage = "LOAN DETAIL PAGE - SOURCING DETAIL TAB";
            CRM_LoanDetailsPage loanDetailsPage = new CRM_LoanDetailsPage(driver);
            Utilities.captureScreenShot(driver);
            loanDetailsPage.getTabLoanDetailsElement().click();

            CRM_LoanDetailsSourcingDetailsTab loanDetailsSourcingDetailsTab = new CRM_LoanDetailsSourcingDetailsTab(driver);
            await("Load loan details - sourcing details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getTabSourcingDetailsElement().getAttribute("class").contains("active"));
            await("Load loan details - sourcing details container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getSourcingDetailsDivContainerElement().isDisplayed());
            loanDetailsDTO.getSourcingDetails().setApplicationNumber(applicationId);
            loanDetailsSourcingDetailsTab.setData(loanDetailsDTO.getSourcingDetails());

            Utilities.captureScreenShot(driver);
            loanDetailsSourcingDetailsTab.getBtnSaveAndNextElement().click();

            Thread.sleep(15000);

            try {

                await("getBtnConfirmDeleteVapNextElement1 visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> driver.findElement(By.xpath("//div[@class='modal-scrollable']//a[contains(@id, 'confirmDeleteVapNext')]")).isDisplayed());
                //loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElement1().click();

                driver.findElement(By.xpath("//div[@class='modal-scrollable']//a[contains(@id, 'confirmDeleteVapNext')]")).click();
            } catch (Exception e) {
                log.info("Vap:" + e.toString());
            }

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ==========VAP DETAILS=======================
            if (loanDetailsDTO.getVapDetails() != null && loanDetailsDTO.getVapDetails().getVapProduct() != null && !loanDetailsDTO.getVapDetails().getVapProduct().equals("")) {
                stage = "LOAN DETAIL PAGE - VAP DETAIL TAB";
                Utilities.captureScreenShot(driver);
                CRM_LoanDetailsVapDetailsTab loanDetailsVapDetailsTab = new CRM_LoanDetailsVapDetailsTab(driver);

                await("Load loan details - sourcing details container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> loanDetailsVapDetailsTab.getVapDetailsDivContainerElement().isDisplayed());
                loanDetailsVapDetailsTab.setData(loanDetailsDTO.getVapDetails());
                Utilities.captureScreenShot(driver);
                loanDetailsVapDetailsTab.getBtnSaveAndNextElement().click();

                System.out.println(stage + ": DONE");
                Utilities.captureScreenShot(driver);

            }

            stage = "DOCUMENTS";
            // ==========DOCUMENTS=================
            if (documentDTOS.size() > 0) {
                CRM_DocumentsPage documentsPage = new CRM_DocumentsPage(driver);
                await("Load document tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> documentsPage.getTabDocumentsElement().isDisplayed() && documentsPage.getTabDocumentsElement().isEnabled());
                documentsPage.getTabDocumentsElement().click();
                await("Load document container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> documentsPage.getDocumentsContainerElement().isDisplayed());
                documentsPage.getBtnGetDocumentElement().click();
                await("Load document table Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> documentsPage.getLendingTrElement().size() > 0);

                documentsPage.setData(documentDTOS, downdloadFileURL);
                Utilities.captureScreenShot(driver);
                documentsPage.getBtnSubmitElement().click();
            }

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "REFERENCES";
            // ==========REFERENCES=================
            stage = "REFERENCES PAGE";
            CRM_ReferencesPage referencesPage = new CRM_ReferencesPage(driver);
            await("Load references tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> referencesPage.getTabReferencesElement().isDisplayed() && referencesPage.getTabReferencesElement().isEnabled());
            referencesPage.getTabReferencesElement().click();
            referencesPage.setData(referenceDTO);
            referencesPage.getSaveBtnElement().click();

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);


            // ==========MISC FRM APP DTL=================
            stage = "MISC FRM APPDTL PAGE";
            CRM_MiscFrmAppDtlPage miscFrmAppDtlPage = new CRM_MiscFrmAppDtlPage(driver);
            miscFrmAppDtlPage.getTabMiscFrmAppDtlElementByName().click();
            miscFrmAppDtlPage.setData(miscFrmAppDtlDTO);
            Utilities.captureScreenShot(driver);
            await("getBtnSaveElement end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFrmAppDtlPage._getBtnSaveElement().isEnabled());
            miscFrmAppDtlPage._getBtnSaveElement().click();
            Utilities.captureScreenShot(driver);
            await("getBtnMoveToNextStageElement end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFrmAppDtlPage.getBtnMoveToNextStageElement().isEnabled());
            Utilities.captureScreenShot(driver);

            miscFrmAppDtlPage.updateCommunicationValue(miscFrmAppDtlDTO.getRemark());

            //tam thoi cho sleep de an notification moi click dc button movetonextstage
            Thread.sleep(15000);

            miscFrmAppDtlPage.getBtnMoveToNextStageElement().click();
            Utilities.captureScreenShot(driver);
            System.out.println(stage + ": DONE");

            await("Work flow failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Grid"));

            Utilities.captureScreenShot(driver);
            stage = "COMPLETE";
            System.out.println("AUTO QUICKLEAD OK: user run auto: " + accountDTO.getUserName());

            System.out.println("AUTO QUICKLEAD - FINISH: " + stage + " - " + Duration.between(start, Instant.now()).toSeconds());
            Utilities.captureScreenShot(driver);

            responseModel.setProject(existingCustomerDTO.getProject());
            responseModel.setReference_id(existingCustomerDTO.getReference_id());
            responseModel.setTransaction_id(existingCustomerDTO.getQuickLeadId());
            responseModel.setApp_id(applicationId);
            responseModel.setAutomation_result("QUICKLEAD PASS");

            Utilities.captureScreenShot(driver);

        } catch (Exception e) {

            responseModel.setProject(existingCustomerDTO.getProject());
            responseModel.setReference_id(existingCustomerDTO.getReference_id());
            responseModel.setTransaction_id(existingCustomerDTO.getQuickLeadId());
            responseModel.setApp_id(applicationId);
            responseModel.setAutomation_result("QUICKLEAD FAILED" + " - " + "Session ID: " + session + " - " + e.getMessage());

            System.out.println("Auto Error: " + stage + "\n => MESSAGE " + e.getMessage() + " => TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);
        } finally {
            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            System.out.println(responseModel.getAutomation_result() + " => Project: " + responseModel.getProject() + " => AppId: " +responseModel.getApp_id());
            logout(driver,accountDTO.getUserName());
            pushAccountToQueue(accountDTO, responseModel.getProject().toUpperCase());
            autoUpdateStatusRabbit(responseModel, "updateAutomation");
        }
    }

    private void updateQuickleadExistingCustomer(CRM_ExistingCustomerDTO existingCustomerDTO) throws Exception {
        mongoTemplate.save(existingCustomerDTO);

    }
    //------------------------ END EXISTING CUSTOMER -----------------------------------------------------


    //------------------------ EXISTING CUSTOMER -----------------------------------------------------

    public void runAutomation_Sale_Queue_With_FullInfo(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO) throws Exception {
        ResponseAutomationModel responseModel = new ResponseAutomationModel();
        Instant start = Instant.now();
        String stage = "";
        String applicationId = "";
        CRM_SaleQueueDTO saleQueueDTO = CRM_SaleQueueDTO.builder().build();
        SessionId session = ((RemoteWebDriver)driver).getSessionId();
        try {
            stage = "INIT DATA";
            //*************************** GET DATA *********************//
            saleQueueDTO = (CRM_SaleQueueDTO) mapValue.get("SaleQueueList");
            applicationId = saleQueueDTO.getAppId();
            CRM_ApplicationInformationsListDTO applicationInfoDTO = (CRM_ApplicationInformationsListDTO) mapValue.get("ApplicationInfoDTO");
            CRM_LoanDetailsDTO loanDetailsDTO = (CRM_LoanDetailsDTO) mapValue.get("VapDetailsDTO");
            List<CRM_DocumentsDTO> documentDTOS = (List<CRM_DocumentsDTO>) mapValue.get("DocumentDTO");
            List<CRM_ReferencesListDTO> referenceDTO = (List<CRM_ReferencesListDTO>) mapValue.get("ReferenceDTO");
            CRM_DynamicFormDTO miscFrmAppDtlDTO = (CRM_DynamicFormDTO) mapValue.get("MiscFrmAppDtlDTO");
            log.info("{}", saleQueueDTO);
            //*************************** END GET DATA *********************//
            System.out.println(stage + ": DONE");

            stage = "LOGIN FINONE";

            LoginPage loginPage = new LoginPage(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
            loginPage.clickLogin();

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "HOME PAGE";
            HomePage homePage = new HomePage(driver);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ========== APPLICATIONS =================
            homePage.getMenuApplicationElement().click();
            stage = "APPLICATION MANAGER";
            // ========== APPLICATION MANAGER =================
            homePage.getApplicationManagerElement().click();
            await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Manager"));

            CRM_ApplicationManagerPage crm_applicationManagerPage = new CRM_ApplicationManagerPage(driver);

            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> crm_applicationManagerPage.getApplicationManagerFormElement().isDisplayed());

            crm_applicationManagerPage.setData(applicationId, accountDTO.getUserName());
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "APPLICATION GRID";
            // ========== APPLICATION GRID =================
            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> crm_applicationManagerPage.getMenuApplicationElement().isDisplayed());

            crm_applicationManagerPage.getMenuApplicationElement().click();
            Utilities.captureScreenShot(driver);
            crm_applicationManagerPage.getApplicationElement().click();
            Utilities.captureScreenShot(driver);
            await("Application Grid timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Grid"));
            Utilities.captureScreenShot(driver);
            crm_applicationManagerPage.applicationGrid(applicationId);
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ========== APPLICANT INFORMATION =================
            // ========== PERSONAL INFORMATION =================
            stage = "VIEW/EDIT DETAILED INFORMATION";
            CRM_ExistingCustomerPage crm_ExistingCustomerPage = new CRM_ExistingCustomerPage(driver);
            crm_ExistingCustomerPage.getEditCustomerExistCustomerElement().click();
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "PERSONAL INFORMATION";
            CRM_ApplicationInfoPage appInfoPage = new CRM_ApplicationInfoPage(driver);
            CRM_ApplicationInfoPersonalTab personalTab = appInfoPage.getApplicationInfoPersonalTab();

            await("Load Personal Info tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> appInfoPage.getPersonalInfoTabElement().getAttribute("class").contains("active"));

            await("getPersonalCustomerDetailsElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> personalTab.getPersonalCustomerDetailsElement().isDisplayed());

            personalTab.updateValue(applicationInfoDTO);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "EMPLOYMENT DETAILS";
            // ========== EMPLOYMENT DETAILS =================
            await("Load employment details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> appInfoPage.getEmploymentDetailsTabElement().getAttribute("class").contains("active"));

            CRM_ApplicationInfoEmploymentDetailsTab employmentDetailsTab = appInfoPage.getApplicationInfoEmploymentDetailsTab();
            await("Span Application Id not enabled Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> employmentDetailsTab.getApplicationId().isEnabled());

            employmentDetailsTab.setData(applicationInfoDTO.getEmploymentDetail());
            employmentDetailsTab.getDoneBtnElement().click();
            await("Employment Status loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> employmentDetailsTab.getTableAfterDoneElement().isDisplayed());
            employmentDetailsTab.setMajorOccupation(applicationInfoDTO.getEmploymentDetail());
            employmentDetailsTab.getSaveAndNextBtnElement().click();

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "FINANCIAL";
            // ==========FINANCIAL DETAILS =================
            if (applicationInfoDTO.getFinancialDetail() != null) {
                Thread.sleep(15000);
                CRM_ApplicationInfoFinancialDetailsTab financialDetailsTab = appInfoPage.getApplicationInfoFinancialDetailsTab();
                financialDetailsTab.openFinancialDetailsTabSection();
                financialDetailsTab.openIncomeDetailSection();
                await("Load financial details - income details Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> financialDetailsTab.getIncomeDetailDivElement().isDisplayed());
//                financialDetailsTab.setIncomeDetailsData(applicationInfoDTO.getFinancialDetail());
                financialDetailsTab.updateIncomeDetailsData(applicationInfoDTO.getFinancialDetail());
                financialDetailsTab.saveAndNext();
            }

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "Bank / Credit Card Details";
            // ==========BANK DETAILS =================
            await("Load Bank details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> appInfoPage.getBankDetailsTabElement().getAttribute("class").contains("active"));

            CRM_ApplicationInfoBankDetailsTab bankDetailsTab = appInfoPage.getApplicationInfoBankDetailsTab();

            await("Span Application Id not enabled Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> bankDetailsTab.getApplicationId().isEnabled());

            await("Span Application Id not enabled Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> bankDetailsTab.getBankDetailsGridElement().isEnabled());

            if(bankDetailsTab.getBankDetailsTableElement().size() > 2){

                await("Span Application Id not enabled Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> bankDetailsTab.getBankDetailsTableElement().size() > 2);

                await("Load deleteIdDetailElement Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> bankDetailsTab.getDeleteIdDetailElement().size() > 0);

                for (int i=0; i<bankDetailsTab.getDeleteIdDetailElement().size(); i++) {
                    WebElement var = bankDetailsTab.getDeleteIdDetailElement().get(i);
                    var.click();
                }
            }

            bankDetailsTab.saveAndNext();

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ==========LOAN DETAILS=================
            stage = "LOAN DETAIL PAGE - SOURCING DETAIL TAB";
            CRM_LoanDetailsPage loanDetailsPage = new CRM_LoanDetailsPage(driver);
            Utilities.captureScreenShot(driver);
            loanDetailsPage.getTabLoanDetailsElement().click();

            CRM_LoanDetailsSourcingDetailsTab loanDetailsSourcingDetailsTab = new CRM_LoanDetailsSourcingDetailsTab(driver);
            await("Load loan details - sourcing details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getTabSourcingDetailsElement().getAttribute("class").contains("active"));
            await("Load loan details - sourcing details container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getSourcingDetailsDivContainerElement().isDisplayed());
            loanDetailsDTO.getSourcingDetails().setApplicationNumber(applicationId);
            loanDetailsSourcingDetailsTab.updateData(loanDetailsDTO.getSourcingDetails());

            Utilities.captureScreenShot(driver);
            loanDetailsSourcingDetailsTab.getBtnSaveAndNextElement().click();

            Thread.sleep(15000);

            try {

                await("getBtnConfirmDeleteVapNextElement1 visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> driver.findElement(By.xpath("//div[@class='modal-scrollable']//a[contains(@id, 'confirmDeleteVapNext')]")).isDisplayed());
                //loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElement1().click();

                driver.findElement(By.xpath("//div[@class='modal-scrollable']//a[contains(@id, 'confirmDeleteVapNext')]")).click();
            } catch (Exception e) {
                log.info("Vap:" + e.toString());
            }

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ==========VAP DETAILS=======================
            if (loanDetailsDTO.getVapDetails() != null && loanDetailsDTO.getVapDetails().getVapProduct() != null && !loanDetailsDTO.getVapDetails().getVapProduct().equals("")) {
                stage = "LOAN DETAIL PAGE - VAP DETAIL TAB";
                Utilities.captureScreenShot(driver);
                CRM_LoanDetailsVapDetailsTab loanDetailsVapDetailsTab = new CRM_LoanDetailsVapDetailsTab(driver);

                await("Load loan details - sourcing details container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> loanDetailsVapDetailsTab.getVapDetailsDivContainerElement().isDisplayed());
                loanDetailsVapDetailsTab.setData(loanDetailsDTO.getVapDetails());
                Utilities.captureScreenShot(driver);
                loanDetailsVapDetailsTab.getBtnSaveAndNextElement().click();

                System.out.println(stage + ": DONE");
                Utilities.captureScreenShot(driver);

            }

            stage = "DOCUMENTS";
            // ==========DOCUMENTS=================
            if (documentDTOS.size() > 0) {
                CRM_DocumentsPage documentsPage = new CRM_DocumentsPage(driver);
                await("Load document tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> documentsPage.getTabDocumentsElement().isDisplayed() && documentsPage.getTabDocumentsElement().isEnabled());
                documentsPage.getTabDocumentsElement().click();
                await("Load document container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> documentsPage.getDocumentsContainerElement().isDisplayed());
                documentsPage.getBtnGetDocumentElement().click();
                await("Load document table Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> documentsPage.getLendingTrElement().size() > 0);

                documentsPage.setData(documentDTOS, downdloadFileURL);
                Utilities.captureScreenShot(driver);
                documentsPage.getBtnSubmitElement().click();
            }

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "REFERENCES";
            // ==========REFERENCES=================
            stage = "REFERENCES PAGE";
            CRM_ReferencesPage referencesPage = new CRM_ReferencesPage(driver);
            await("Load references tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> referencesPage.getTabReferencesElement().isDisplayed() && referencesPage.getTabReferencesElement().isEnabled());
            referencesPage.getTabReferencesElement().click();
            referencesPage.setData(referenceDTO);
            referencesPage.getSaveBtnElement().click();

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);


            // ==========MISC FRM APP DTL=================
            stage = "MISC FRM APPDTL PAGE";
            CRM_MiscFrmAppDtlPage miscFrmAppDtlPage = new CRM_MiscFrmAppDtlPage(driver);
            miscFrmAppDtlPage.getTabMiscFrmAppDtlElementByName().click();
            miscFrmAppDtlPage.setData(miscFrmAppDtlDTO);
            Utilities.captureScreenShot(driver);
            await("getBtnSaveElement end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFrmAppDtlPage._getBtnSaveElement().isEnabled());
            miscFrmAppDtlPage._getBtnSaveElement().click();
            Utilities.captureScreenShot(driver);
            await("getBtnMoveToNextStageElement end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFrmAppDtlPage.getBtnMoveToNextStageElement().isEnabled());
            Utilities.captureScreenShot(driver);

            miscFrmAppDtlPage.updateCommunicationValue(miscFrmAppDtlDTO.getRemark());

            //tam thoi cho sleep de an notification moi click dc button movetonextstage
            Thread.sleep(15000);

            miscFrmAppDtlPage.getBtnMoveToNextStageElement().click();
            Utilities.captureScreenShot(driver);
            System.out.println(stage + ": DONE");

            await("Work flow failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Grid"));

            Utilities.captureScreenShot(driver);

            Utilities.captureScreenShot(driver);
            stage = "COMPLETE";
            System.out.println("AUTO SALEQUEUE OK: user run auto: " + accountDTO.getUserName());

            System.out.println("AUTO SALEQUEUE - FINISH: " + stage + " - " + Duration.between(start, Instant.now()).toSeconds());
            Utilities.captureScreenShot(driver);

            responseModel.setProject(saleQueueDTO.getProject());
            responseModel.setReference_id(saleQueueDTO.getReference_id());
            responseModel.setTransaction_id(saleQueueDTO.getTransaction_id());
            responseModel.setApp_id(applicationId);
            responseModel.setAutomation_result("SALEQUEUE PASS");

            Utilities.captureScreenShot(driver);

        } catch (Exception e) {

            responseModel.setProject(saleQueueDTO.getProject());
            responseModel.setReference_id(saleQueueDTO.getReference_id());
            responseModel.setTransaction_id(saleQueueDTO.getTransaction_id());
            responseModel.setApp_id(applicationId);
            responseModel.setAutomation_result("SALEQUEUE FAILED" + " - " + "Session ID: " + session + " - " + e.getMessage());

            System.out.println("Auto Error: " + stage + "\n => MESSAGE " + e.getMessage() + " => TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);
        } finally {
            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            System.out.println(responseModel.getAutomation_result() + " => Project: " + responseModel.getProject() + " => AppId: " +responseModel.getApp_id());
            logout(driver,accountDTO.getUserName());
            pushAccountToQueue(accountDTO, responseModel.getProject().toUpperCase());
            autoUpdateStatusRabbit(responseModel, "updateAutomation");
        }
    }

    //------------------------ END EXISTING CUSTOMER -----------------------------------------------------

    //------------------------ END PROJECT CRM -----------------------------------------------------

    public void runAutomation_QuickLead_Assign_Pool(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO) throws Exception {
        Instant start = Instant.now();
        String appId = "";
        String stage = "";
        Application application = Application.builder().build();
        log.info("{}", application);
        try {
            stage = "INIT DATA";
            //*************************** GET DATA *********************//
            application = (Application) mapValue.get("ApplicationDTO");
            application.setAutomationAcc(accountDTO.getUserName());
            QuickLead quickLead = application.getQuickLead();
            //mongoTemplate.save(application);


            //*************************** END GET DATA *********************//
            Actions actions = new Actions(driver);
            System.out.println(stage + ": DONE");
            stage = "LOGIN FINONE";
            HashMap<String, String> dataControl = new HashMap<>();
            LoginPage loginPage = new LoginPage(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
            loginPage.clickLogin();

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));


            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "HOME PAGE";
            HomePage homePage = new HomePage(driver);


            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "QUICK LEAD";
            // ========== QUICK LEAD =================
            homePage.menuClick();
            homePage.leadQuickClick();

            await("Quick Lead Entry timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Quick Lead Entry"));

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "QUICK LEAD (DOCUMENT UPLOAD & APPLICATION CREATION)";
            QuickLeadPage quickLeadPage = new QuickLeadPage(driver);
            quickLeadPage.setData(quickLead);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "LEADS GRID";
            // ========== LEAD PAGE =================
            LeadsPage leadsPage = new LeadsPage(driver);
            await("Quick Lead Entry timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Lead Grid"));

            await("notifyTextElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadsPage.getNotifyTextElement().isEnabled() && leadsPage.getNotifyTextElement().isDisplayed());

            String notify = leadsPage.getNotifyTextElement().getText();
            String leadApp = "";
            if (notify.contains("LEAD")) {
                leadApp = notify.substring(notify.indexOf("LEAD"), notify.length());
            }

            System.out.println("LEAD APP: =>" + leadApp);
            leadsPage.setData(leadApp);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "LEAD STAGE";
            // ========== LEAD STAGE =================
            LeadDetailPage leadDetailPage = new LeadDetailPage(driver);
            await("contentElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadDetailPage.getContentElement().isDisplayed());

            leadDetailPage.setData(quickLead, leadApp, downdloadFileURL);

            Utilities.captureScreenShot(driver);

            await("Lead Page timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Lead Grid"));

            leadsPage.getSpanAllNotifyElement().click();
            await("getDivAllNotifyElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadsPage.getDivAllNotifyElement().isEnabled() && leadsPage.getDivAllNotifyElement().isDisplayed());

            await("getBtnAllNotifyElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadsPage.getBtnAllNotifyElement().isEnabled() && leadsPage.getBtnAllNotifyElement().isDisplayed());
            leadsPage.getBtnAllNotifyElement().click();

            Utilities.captureScreenShot(driver);
            await("getBtnAllNotifyElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadsPage.getNotifyTextSuccessElement().size() > 0);

            String leadAppID = "";
            stage = "APPLICATION MANAGER";
            // ========== APPLICATION MANAGER =================
            homePage.getMenuApplicationElement().click();
            homePage.getApplicationManagerElement().click();
            await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Manager"));

            DE_ApplicationManagerPage de_applicationManagerPage = new DE_ApplicationManagerPage(driver);

            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> de_applicationManagerPage.getApplicationManagerFormElement().isDisplayed());

            //get appID moi o day
            leadAppID = de_applicationManagerPage.getAppID(leadApp);
            System.out.println(" APP: =>" + leadAppID);
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            //-------------------- END ---------------------------

            application.setApplicationId(leadAppID);
            application.setLeadApp(leadApp);

            //UPDATE STATUS
            application.setStatus("QUICKLEAD PASS");
            application.setDescription("Thanh cong");


            Utilities.captureScreenShot(driver);
            //logout(driver);

        } catch (Exception e) {
            //UPDATE STATUS
            application.setStatus("QUICKLEAD FAIL");
            application.setStage(stage);
            application.setDescription(e.getMessage());

            System.out.println(stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);

            if (e.getMessage().contains("Work flow failed!!!")) {
                stage = "END OF LEAD DETAIL";

                await("Get error fail!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> driver.findElements(By.id("error-message")).size() > 0);

                if (driver.findElements(By.id("error-message")) != null && driver.findElements(By.id("error-message")).size() > 0) {
                    String error = "Error: ";
                    for (WebElement we : driver.findElements(By.id("error-message"))) {
                        error += " - " + we.getText();
                    }
                    System.out.println(stage + "=>" + error);
                }
            }
        } finally {
            if (application.getApplicationId() == null || application.getApplicationId().isEmpty() || application.getApplicationId().indexOf("LEAD") > 0 || application.getApplicationId().indexOf("APPL") < 0) {
                application.setApplicationId("UNKNOW");
            }

            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            try {
                updateStatusRabbit(application, "updateAutomation");
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            logout(driver,accountDTO.getUserName());
        }
    }

    //------------------------ AUTO ALLOCATION -----------------------------------------------------
    public void runAutomation_autoAllocation(WebDriver driver, Map<String, Object> mapValue, String project, String browser) throws Exception {
        String stage = "";
        try {
            stage = "INIT DATA";
            //*************************** GET DATA *********************//
            List<AutoReassignUserDTO> autoReassignUserDTOList = (List<AutoReassignUserDTO>) mapValue.get("AutoReassignUser");
            //*************************** END GET DATA *********************//
            List<LoginDTO> loginDTOList = new ArrayList<LoginDTO>();

            LoginDTO accountDTONew = null;
            do {
                //get list account finone available
                Query query = new Query();
                query.addCriteria(Criteria.where("active").is(0).and("project").is(project));
                AccountFinOneDTO accountFinOneDTO = mongoTemplate.findOne(query, AccountFinOneDTO.class);
                if (!Objects.isNull(accountFinOneDTO)) {
                    accountDTONew = new LoginDTO().builder().userName(accountFinOneDTO.getUsername()).password(accountFinOneDTO.getPassword()).build();

                    Query queryUpdate = new Query();
                    queryUpdate.addCriteria(Criteria.where("active").is(0).and("username").is(accountFinOneDTO.getUsername()).and("project").is(project));
                    Update update = new Update();
                    update.set("active", 1);
                    AccountFinOneDTO resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, AccountFinOneDTO.class);

                    if (resultUpdate == null) {
                        Thread.sleep(Constant.WAIT_ACCOUNT_GET_NULL);
                        accountDTONew = null;
                    } else {
                        loginDTOList.add(accountDTONew);
                        System.out.println("Get it:" + accountDTONew.toString());
                    }
                } else
                    accountDTONew = null;
            } while (!Objects.isNull(accountDTONew));

            //insert data
            mongoTemplate.insert(autoReassignUserDTOList, AutoReassignUserDTO.class);

            if (loginDTOList.size() > 0) {
                ExecutorService workerThreadPoolDE = Executors.newFixedThreadPool(loginDTOList.size());

                for (LoginDTO loginDTO : loginDTOList) {
                    workerThreadPoolDE.execute(new Runnable() {
                        @Override
                        public void run() {
                            runAutomation_autoAllocation_run(loginDTO, browser, project);
                        }
                    });
                }

            }
        } catch (Exception e) {
            System.out.println(stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();
            Utilities.captureScreenShot(driver);
        }
    }

    private void runAutomation_autoAllocation_run(LoginDTO accountDTO, String browser, String project) {
        WebDriver driver = null;
        Instant start = Instant.now();
        String stage = "";
        List<String> listAppId = new ArrayList<>();
        String amountReassignedAppId = "";
        System.out.println("START - Auto: " + accountDTO.getUserName() + " - Time: " + Duration.between(start, Instant.now()).toSeconds());
        try {
            SeleniumGridDriver setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
            driver = setupTestDriver.getDriver();
            //get account run
            stage = "LOGIN FINONE";
            LoginPage loginPage = new LoginPage(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
            loginPage.clickLogin();


            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));
            System.out.println("Auto: " + accountDTO.getUserName() + " - " + stage + ": DONE" + " - Time: " + Duration.between(start, Instant.now()).toSeconds());
            Utilities.captureScreenShot(driver);

            AutoAllocationReassignPage autoAllocationReassignPages = new AutoAllocationReassignPage(driver);
            autoAllocationReassignPages.getMenuApplicationsElement().click();
            autoAllocationReassignPages.getMenuApplicationsLiElement().click();

            AutoReassignUserDTO autoReassignUserDTO = null;
            do {
                try {
                    Instant startIn = Instant.now();
                    System.out.println("Auto:" + accountDTO.getUserName() + " - BEGIN " + " - Time: " + Duration.between(startIn, Instant.now()).toSeconds());
                    Query query = new Query();
                    query.addCriteria(Criteria.where("status").is(0));
                    autoReassignUserDTO = mongoTemplate.findOne(query, AutoReassignUserDTO.class);

                    if (!Objects.isNull(autoReassignUserDTO)) {

                        //update app
                        Query queryUpdate = new Query();
                        queryUpdate.addCriteria(Criteria.where("_id").is(new ObjectId(autoReassignUserDTO.getId())).and("status").is(0).and("reference_id").is(autoReassignUserDTO.getReference_id()).and("project").is(autoReassignUserDTO.getProject()));
                        Update update = new Update();
                        update.set("userauto", accountDTO.getUserName());
                        update.set("status", 2);
                        AutoReassignUserDTO resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, AutoReassignUserDTO.class);

                        if (resultUpdate == null) {
                            continue;
                        }

                        System.out.println("Auto:" + accountDTO.getUserName() + " - GET DATA DONE " + " - Time: " + Duration.between(startIn, Instant.now()).toSeconds());

                        stage = "APPLICATION ASSIGNED";

                        AutoAllocationReassignPage autoAllocationReassignPage = new AutoAllocationReassignPage(driver);

                        await("Application timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(driver::getTitle, is("Application Grid"));

                        Utilities.captureScreenShot(driver);

                        await("Search Options loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(() -> autoAllocationReassignPage.getSearchOptionsElement().isDisplayed());

                        new Select(autoAllocationReassignPage.getSearchOptionsElement()).selectByVisibleText("Stage");

                        Utilities.captureScreenShot(driver);

                        autoAllocationReassignPage.getSearchTextsElement().clear();

                        autoAllocationReassignPage.getSearchTextsElement().sendKeys(autoReassignUserDTO.getStageApp());

                        Thread.sleep(5000);

                        await("table Application Assigned Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(() -> autoAllocationReassignPage.getTableSearchApplicationElement().size() > 2);

                        Utilities.captureScreenShot(driver);

                        int sizeListApplicationReassign = autoAllocationReassignPage.getListColApplicationTableElement().size();
                        int sizeCheckboxReassign = Integer.parseInt(autoReassignUserDTO.getAmountApp());


                        if (sizeListApplicationReassign < sizeCheckboxReassign){
                            sizeCheckboxReassign = sizeListApplicationReassign;
                        }

                        Thread.sleep(5000);

                        listAppId = new ArrayList<>();

                        for (int i = 0; i < sizeCheckboxReassign; i++){
                            WebElement inQueueStatusElement = driver.findElement(new By.ByXPath("//table[@id='LoanApplication_Assigned']//tbody//tr//td//*[contains(text(),'" + autoAllocationReassignPage.getListColApplicationTableElement().get(i).getText() + "')]//ancestor::tr//td[8]"));
                            if (autoReassignUserDTO.getInQueueStatus().equals(inQueueStatusElement.getText())){
                                WebElement checkBoxAssignedElement = driver.findElement(new By.ByXPath("//table[@id='LoanApplication_Assigned']//tbody//tr//td//*[contains(text(),'" + autoAllocationReassignPage.getListColApplicationTableElement().get(i).getText() + "')]//ancestor::tr//td[8][text() = '" + autoReassignUserDTO.getInQueueStatus() + "']//ancestor::tr//td//input[@id = 'selectThis_LoanApplication_Assigned']"));
                                checkBoxAssignedElement.click();
                                listAppId.add(autoAllocationReassignPage.getListColApplicationTableElement().get(i).getText());
                            }
//                            autoAllocationReassignPage.getCheckboxApplicationAssignedElement().get(i).click();
                        }

                        await("Reassign For User Button Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(() -> autoAllocationReassignPage.getBtnReassignForUserElement().isDisplayed());

                        Thread.sleep(5000);

                        Utilities.captureScreenShot(driver);

                        autoAllocationReassignPage.getBtnReassignForUserElement().click();

                        await("Dialog Form Users Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(() -> autoAllocationReassignPage.getDialogFormUsersElement().isDisplayed());

                        Utilities.captureScreenShot(driver);

                        await("Dialog Form Users Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(() -> autoAllocationReassignPage.getTableUserAssignedElement().size() > 2);

                        WebElement btnSelectedUserElement = driver.findElement(By.xpath("//div[@id = 'dialog-form-users' and @aria-hidden = 'false']//div[@id = 'example']//table//tbody[@id = 'searchData']//tr//td[contains(text(),'" + autoReassignUserDTO.getReassignUser() +"')]//ancestor::tr//*[contains(@id,'selected_user')]"));

                        btnSelectedUserElement.click();

                        String UserId = driver.findElement(By.xpath("//div[@id = 'dialog-form-users' and @aria-hidden = 'false']//div[@id = 'example']//table//tbody[@id = 'searchData']//tr//td[contains(text(),'"+ autoReassignUserDTO.getReassignUser() +"')]//ancestor::tr//*[contains(@class,'usr_id')]")).getText();

                        WebElement selectedUsersElement = driver.findElement(By.xpath("//div[@id = 'dialog-form-users' and @aria-hidden = 'false']//div[@id = 'selectedDiv']//div[@id = 'selected']//div[@id = '"+ UserId +"']"));

                        await("Selected Users Timeout Loading!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(() -> selectedUsersElement.isDisplayed());

                        Utilities.captureScreenShot(driver);

                        autoAllocationReassignPage.getBtnDoneDialogFormUsersElement().click();

                        await("Selected Users Timeout Loading!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(() -> autoAllocationReassignPage.getDialogDecisionReasonElement().isDisplayed());

                        Utilities.captureScreenShot(driver);

                        new Select(autoAllocationReassignPage.getSelectDecisionReasonElement()).selectByVisibleText("Reassign");

//                        autoAllocationReassignPage.getBtnDoneDecisionReasonForReassignElement().click();

                        JavascriptExecutor btnDoneReasonForReassign = (JavascriptExecutor) driver;
                        btnDoneReasonForReassign.executeScript("arguments[0].click();", autoAllocationReassignPage.getBtnDoneDecisionReasonForReassignElement());

                        await("Work flow failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(driver::getTitle, is("Application Grid"));

                        System.out.println("Auto:" + accountDTO.getUserName() + " - GET DONE " + " - Time: " + Duration.between(startIn, Instant.now()).toSeconds());

                        // ========= UPDATE DB ============================
                        Query queryUpdate1 = new Query();
                        queryUpdate1.addCriteria(Criteria.where("status").is(2).and("reference_id").is(autoReassignUserDTO.getReference_id()).and("project").is(autoReassignUserDTO.getProject()));
                        Update update1 = new Update();
                        update1.set("userAuto", accountDTO.getUserName());
                        update1.set("status", 1);
                        update1.set("appId", listAppId);
                        update1.set("amountReassignedApp", amountReassignedAppId);
                        mongoTemplate.findAndModify(queryUpdate1, update1, AutoReassignUserDTO.class);
                        System.out.println("Auto: " + accountDTO.getUserName() + " - UPDATE STATUS " + " - Time: " + Duration.between(startIn, Instant.now()).toSeconds());
                    }
                } catch (Exception ex) {
                    Query queryUpdate = new Query();
                    queryUpdate.addCriteria(Criteria.where("status").is(0).and("reference_id").is(autoReassignUserDTO.getReference_id()).and("project").is(autoReassignUserDTO.getProject()));
                    Update update = new Update();
                    update.set("userAuto", accountDTO.getUserName());
                    update.set("status", 3);
                    update.set("appId", listAppId);
                    update.set("amountReassignedApp", amountReassignedAppId);
                    mongoTemplate.findAndModify(queryUpdate, update, AutoReassignUserDTO.class);
                    System.out.println(ex.getMessage());
                }
            } while (!Objects.isNull(autoReassignUserDTO));
        } catch (Exception e) {
            System.out.println("User Auto:" + accountDTO.getUserName() + " - " + stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();
            Utilities.captureScreenShot(driver);
        } finally {
            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            logout(driver,accountDTO.getUserName());
            pushAccountToQueue(accountDTO, project);
        }
    }

    public void runAutomation_autoAssignAllocation(WebDriver driver, Map<String, Object> mapValue, String project, String browser) throws Exception {
        String stage = "";
        try {
            stage = "INIT DATA";
            //*************************** GET DATA *********************//
            List<AutoAssignAllocationDTO> autoAssignAllocationDTOList = (List<AutoAssignAllocationDTO>) mapValue.get("AutoAssignAllocationList");
            //*************************** END GET DATA *********************//
            List<LoginDTO> loginDTOList = new ArrayList<LoginDTO>();

            LoginDTO accountDTONew = null;
            do {
                Query query = new Query();
                query.addCriteria(Criteria.where("active").is(0).and("project").is(project));
                AccountFinOneDTO accountFinOneDTO = mongoTemplate.findOne(query, AccountFinOneDTO.class);
                if (!Objects.isNull(accountFinOneDTO)) {
                    accountDTONew = new LoginDTO().builder().userName(accountFinOneDTO.getUsername()).password(accountFinOneDTO.getPassword()).build();

                    Query queryUpdate = new Query();
                    queryUpdate.addCriteria(Criteria.where("active").is(0).and("username").is(accountFinOneDTO.getUsername()).and("project").is(project));
                    Update update = new Update();
                    update.set("active", 1);
                    AccountFinOneDTO resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, AccountFinOneDTO.class);

                    if (resultUpdate == null) {
                        Thread.sleep(Constant.WAIT_ACCOUNT_GET_NULL);
                        accountDTONew = null;
                    } else {
                        loginDTOList.add(accountDTONew);
                        System.out.println("Get it:" + accountDTONew.toString());
                    }
                } else
                    accountDTONew = null;
            } while (!Objects.isNull(accountDTONew));

            //insert data
            mongoTemplate.insert(autoAssignAllocationDTOList, AutoAssignAllocationDTO.class);

            if (loginDTOList.size() > 0) {
                ExecutorService workerThreadPoolDE = Executors.newFixedThreadPool(loginDTOList.size());

                for (LoginDTO loginDTO : loginDTOList) {
                    workerThreadPoolDE.execute(new Runnable() {
                        @Override
                        public void run() {
                            runAutomation_autoAssignAllocation_run(loginDTO, browser, project);
                        }
                    });
                }

            }
        } catch (Exception e) {
            System.out.println(stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();
            Utilities.captureScreenShot(driver);
        }
    }

    private void runAutomation_autoAssignAllocation_run(LoginDTO accountDTO, String browser, String project) {
        WebDriver driver = null;
        Instant start = Instant.now();
        String stage = "";
        System.out.println("START - Auto: " + accountDTO.getUserName() + " - Time: " + Duration.between(start, Instant.now()).toSeconds());
        try {
            SeleniumGridDriver setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
            driver = setupTestDriver.getDriver();
            //get account run
            stage = "LOGIN FINONE";
            LoginPage loginPage = new LoginPage(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
            loginPage.clickLogin();

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));
            System.out.println("Auto: " + accountDTO.getUserName() + " - " + stage + ": DONE" + " - Time: " + Duration.between(start, Instant.now()).toSeconds());
            Utilities.captureScreenShot(driver);

            stage = "HOME PAGE";
            HomePage homePage = new HomePage(driver);
            // ========== APPLICATIONS =================
            homePage.getMenuApplicationElement().click();

            stage = "APPLICATION MANAGER";
            // ========== APPLICATION MANAGER =================
            homePage.getApplicationManagerElement().click();
            await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Manager"));

            AutoAssignAllocationDTO autoAssignAllocationDTO = null;

            do {
                try {
                    Instant startIn = Instant.now();


                    System.out.println("Auto:" + accountDTO.getUserName() + " - BEGIN " + " - Time: " + Duration.between(startIn, Instant.now()).toSeconds());
                    Query query = new Query();
                    query.addCriteria(Criteria.where("status").is(0));
                    autoAssignAllocationDTO = mongoTemplate.findOne(query, AutoAssignAllocationDTO.class);

                    if (!Objects.isNull(autoAssignAllocationDTO)) {

                        //update app
                        Query queryUpdate = new Query();
                        queryUpdate.addCriteria(Criteria.where("status").is(0).and("appId").is(autoAssignAllocationDTO.getAppId()).and("userName").is(autoAssignAllocationDTO.getUserName()));
                        Update update = new Update();
                        update.set("userAuto", accountDTO.getUserName());
                        update.set("status", 2);
                        AutoAssignAllocationDTO resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, AutoAssignAllocationDTO.class);

                        if (resultUpdate == null) {
                            continue;
                        }

                        System.out.println("Auto:" + accountDTO.getUserName() + " - GET DONE " + " - " + " App: " + autoAssignAllocationDTO.getAppId() + " - User: " + autoAssignAllocationDTO.getUserName() + " - Time: " + Duration.between(startIn, Instant.now()).toSeconds());

                        String appID = autoAssignAllocationDTO.getAppId();
                        AutoAssignAllocationPage autoAssignAllocationPage = new AutoAssignAllocationPage(driver);
                        await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(() -> autoAssignAllocationPage.getApplicationManagerFormElement().isDisplayed());
                        autoAssignAllocationPage.setData(appID, autoAssignAllocationDTO.getUserName().toLowerCase());

                        await("tdApplicationElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(() -> autoAssignAllocationPage.getTdApplicationElement().size() > 0);

                        await("showTaskElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(() -> autoAssignAllocationPage.getShowTaskElement().isDisplayed());

                        autoAssignAllocationPage.getBackBtnElement().click();

                        await("Application Manager Back timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(driver::getTitle, is("Application Manager"));

                        System.out.println("Auto: " + accountDTO.getUserName() + " - FINISH " + " - " + " App: " + autoAssignAllocationDTO.getAppId() + " - User: " + autoAssignAllocationDTO.getUserName() + " - Time: " + Duration.between(startIn, Instant.now()).toSeconds());

                        // ========= UPDATE DB ============================
                        Query queryUpdate1 = new Query();
                        queryUpdate1.addCriteria(Criteria.where("status").is(2).and("appId").is(autoAssignAllocationDTO.getAppId()).and("userName").is(autoAssignAllocationDTO.getUserName()));
                        Update update1 = new Update();
                        update1.set("userAuto", accountDTO.getUserName());
                        update1.set("status", 1);
                        mongoTemplate.findAndModify(queryUpdate1, update1, AutoAssignAllocationDTO.class);
                        System.out.println("Auto: " + accountDTO.getUserName() + " - UPDATE STATUS " + " - " + " App: " + autoAssignAllocationDTO.getAppId() + " - User: " + autoAssignAllocationDTO.getUserName() + " - Time: " + Duration.between(startIn, Instant.now()).toSeconds());
                    }
                } catch (Exception ex) {
                    Query queryUpdate = new Query();
                    queryUpdate.addCriteria(Criteria.where("status").is(0).and("appId").is(autoAssignAllocationDTO.getAppId()).and("userName").is(autoAssignAllocationDTO.getUserName()));
                    Update update = new Update();
                    update.set("userAuto", accountDTO.getUserName());
                    update.set("status", 3);
                    mongoTemplate.findAndModify(queryUpdate, update, AutoAssignAllocationDTO.class);

                    System.out.println(ex.getMessage());
                }
            } while (!Objects.isNull(autoAssignAllocationDTO));
        } catch (Exception e) {
            System.out.println("User Auto:" + accountDTO.getUserName() + " - " + stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);
        } finally {
            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            logout(driver,accountDTO.getUserName());
            pushAccountToQueue(accountDTO, project);
        }
    }
    //------------------------ END AUTO ALLOCATION -----------------------------------------------------


}
