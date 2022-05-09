package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import vn.com.tpf.microservices.driver.SeleniumGridDriver;
import vn.com.tpf.microservices.models.AutoAllocation.AutoAllocationDTO;
import vn.com.tpf.microservices.models.AutoAllocation.AutoAllocationResponseDTO;
import vn.com.tpf.microservices.models.AutoAllocation.AutoAssignAllocationDTO;
import vn.com.tpf.microservices.models.AutoAssign.AutoAssignDTO;
import vn.com.tpf.microservices.models.AutoCRM.*;
import vn.com.tpf.microservices.models.AutoField.*;
import vn.com.tpf.microservices.models.AutoMField.MFieldRequest;
import vn.com.tpf.microservices.models.AutoMField.WaiveFieldsDTO;
import vn.com.tpf.microservices.models.AutoQuickLead.QuickLeadDTO;
import vn.com.tpf.microservices.models.AutoQuickLead.QuickLeadDetails;
import vn.com.tpf.microservices.models.AutoReturnQuery.ResponseQueryDTO;
import vn.com.tpf.microservices.models.AutoReturnQuery.SaleQueueDTO;
import vn.com.tpf.microservices.models.Automation.*;
import vn.com.tpf.microservices.models.AutomationMonitor.AutomationMonitorDTO;
import vn.com.tpf.microservices.models.DEReturn.DEResponseQueryDTO;
import vn.com.tpf.microservices.models.DEReturn.DESaleQueueDTO;
import vn.com.tpf.microservices.models.QuickLead.Application;
import vn.com.tpf.microservices.models.QuickLead.QuickLead;
import vn.com.tpf.microservices.models.ResponseAutomationModel;
import vn.com.tpf.microservices.services.Automation.*;
import vn.com.tpf.microservices.services.Automation.autoAllocation.AutoAssignAllocationPage;
import vn.com.tpf.microservices.services.Automation.autoCRM.*;
import vn.com.tpf.microservices.services.Automation.autoField.*;
import vn.com.tpf.microservices.services.Automation.autoQuickLead.LeadGridPage;
import vn.com.tpf.microservices.services.Automation.autoQuickLead.LeadPage;
import vn.com.tpf.microservices.services.Automation.autoQuickLead.QuickLeadEntryPage;
import vn.com.tpf.microservices.services.Automation.deReturn.AssignManagerSaleQueuePage;
import vn.com.tpf.microservices.services.Automation.deReturn.DE_ReturnRaiseQueryPage;
import vn.com.tpf.microservices.services.Automation.deReturn.DE_ReturnSaleQueuePage;
import vn.com.tpf.microservices.services.Automation.lending.*;
import vn.com.tpf.microservices.services.Automation.returnQuery.ApplicationManagerPage;
import vn.com.tpf.microservices.services.Automation.returnQuery.ResponseQueryPage;
import vn.com.tpf.microservices.services.Automation.returnQuery.SaleQueuePage;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.*;
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
                System.out.println("Get it:" + accountDTO.getUserName());
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
                    System.out.println("Get it:" + accountDTO.getUserName());
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
            queryUpdate.addCriteria(Criteria.where("username").is(accountDTO.getUserName()).and("project").is(project).and("active").is(1));
            Update update = new Update();
            update.set("active", 0);
            AccountFinOneDTO resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, AccountFinOneDTO.class);
            System.out.println("Update it:" + accountDTO.toString());
        }
        // }
    }

    public void logout(WebDriver driver, String accountAuto) {
        try {

            System.out.println("Logout");
            LogoutPage logoutPage = new LogoutPage(driver);
            logoutPage.logout();
            log.info("Logout: Done => " + accountAuto);
        } catch (Exception e) {
            System.out.println("LOGOUT: =>" + accountAuto + " - " + e.toString());
        }
    }

    public void executor(Queue<LoginDTO> accounts, String browser, Map<String, Object> mapValue, String function, String project) {
        WebDriver driver = null;
        SeleniumGridDriver setupTestDriver = null;
        LoginDTO accountDTO = null;
        try {

//            SeleniumGridDriver setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
//            driver = setupTestDriver.getDriver();

            switch (function) {
                case "runAutomation_UpdateInfo":
                    accountDTO = pollAccountFromQueue(accounts, project);
                    setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
                    driver = setupTestDriver.getDriver();
                    System.out.println("run runAutomation_UpdateInfo");
                    runAutomation_UpdateInfo(driver, mapValue, accountDTO);
                    break;
                case "runAutomation_UpdateAppError":
                    accountDTO = pollAccountFromQueue(accounts, "DEE");
                    setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
                    driver = setupTestDriver.getDriver();
                    runAutomation_UpdateAppError(driver, mapValue, accountDTO);
                    break;
                case "quickLead":
                    accountDTO = pollAccountFromQueue(accounts, project);
                    setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
                    driver = setupTestDriver.getDriver();
                    runAutomation_QuickLead(driver, mapValue, accountDTO);
                    break;
                case "momoCreateApp":
                    accountDTO = pollAccountFromQueue(accounts, project);
                    setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
                    driver = setupTestDriver.getDriver();
                    runAutomation_momoCreateApp(driver, mapValue, accountDTO);
                    break;
                case "fptCreateApp":
                    accountDTO = pollAccountFromQueue(accounts, project);
                    setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
                    driver = setupTestDriver.getDriver();
                    runAutomation_fptCreateApp(driver, mapValue, accountDTO);
                    break;
                case "runAutomationDE_AutoAssign":
                    //chay get account trong function
                    setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
                    driver = setupTestDriver.getDriver();
                    runAutomationDE_autoAssign(driver, mapValue, project, browser);
                    break;
                case "runAutomationDE_ResponseQuery":
                    accountDTO = return_pollAccountFromQueue(accounts, project.toUpperCase(), mapValue);
                    setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
                    driver = setupTestDriver.getDriver();
                    runAutomationDE_responseQuery(driver, mapValue, accountDTO);
                    break;
                case "runAutomationDE_SaleQueue":
                    accountDTO = pollAccountFromQueue(accounts, project.toUpperCase());
                    setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
                    driver = setupTestDriver.getDriver();
                    runAutomationDE_saleQueue(driver, mapValue, accountDTO);
                    break;
                case "SN_quickLead":
                    accountDTO = pollAccountFromQueue(accounts, project);
                    setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
                    driver = setupTestDriver.getDriver();
                    SN_runAutomation_QuickLead(driver, mapValue, accountDTO);
                    break;
                case "runAutomation_Waive_Field":
                    setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
                    driver = setupTestDriver.getDriver();
                    runAutomation_Field(driver, mapValue, browser, project);
                    break;
                case "runAutomation_Submit_Field":
                    accountDTO = pollAccountFromQueue(accounts, project);
                    setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
                    driver = setupTestDriver.getDriver();
                    runAutomation_Submit_Fields_run(driver, mapValue, accountDTO);
                    break;
                case "MOBILITY_quickLead":
                    accountDTO = pollAccountFromQueue(accounts, project);
                    setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
                    driver = setupTestDriver.getDriver();
                    MOBILITY_runAutomation_QuickLead(driver, mapValue, accountDTO);
                    break;
                case "runAutomation_Existing_Customer":
                    accountDTO = pollAccountFromQueue(accounts, project);
                    setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
                    driver = setupTestDriver.getDriver();
                    runAutomation_Existing_Customer(driver, mapValue, accountDTO);
                    break;
                case "CRM_quickLead_With_CustID":
                    accountDTO = pollAccountFromQueue(accounts, project.toUpperCase());
                    setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
                    driver = setupTestDriver.getDriver();
                    CRM_runAutomation_QuickLead_With_CustID(driver, mapValue, accountDTO);
                    break;
                case "CRM_quickLead":
                    accountDTO = pollAccountFromQueue(accounts, project.toUpperCase());
                    setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
                    driver = setupTestDriver.getDriver();
                    CRM_runAutomation_QuickLead(driver, mapValue, accountDTO, project);
                    break;
                case "quickLead_Assign_Pool":
                    accountDTO = pollAccountFromQueue(accounts, project);
                    setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
                    driver = setupTestDriver.getDriver();
                    runAutomation_QuickLead_Assign_Pool(driver, mapValue, accountDTO);
                    break;
                case "runAutomation_Sale_Queue_With_FullInfo":
                    accountDTO = pollAccountFromQueue(accounts, project);
                    setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
                    driver = setupTestDriver.getDriver();
                    runAutomation_SendBack(driver, mapValue, accountDTO);
                    break;
                case "runAutomation_AutoAssign_Allocation":
                    runAutomation_autoAssignAllocation(mapValue, project, browser);
                    break;
                case "MOBILITY_quickLead_Vendor":
                    accountDTO = pollAccountFromQueue(accounts, project);
                    setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
                    driver = setupTestDriver.getDriver();
                    MOBILITY_runAutomation_QuickLead_Vendor(driver, mapValue, accountDTO);
                    break;
                case "runAutomation_quickLeadApplication":
                    accountDTO = getAccountFromMongoDB(accounts, project, "quickLeadApplication");
                    setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
                    driver = setupTestDriver.getDriver();
                    runAutomation_QuickLeadApplication(driver, mapValue, accountDTO);
                    break;
                case "runAutomation_quickLeadApplicationVendor":
                    accountDTO = getAccountFromMongoDB(accounts, project, "quickLeadApplicationVendor");
                    setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
                    driver = setupTestDriver.getDriver();
                    runAutomation_QuickLeadApplication_Vendor(driver, mapValue, accountDTO);
                    break;
                case "runAutomation_ResponseQuery":
                    accountDTO = getAccountResponseQuery(accounts, project, mapValue, "responseQuery");
                    setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
                    driver = setupTestDriver.getDriver();
                    runAutomation_ResponseQuery(driver, mapValue, accountDTO);
                    break;
                case "runAutomation_SaleQueue":
                    accountDTO = getAccountFromMongoDB(accounts, project, "saleQueue");
                    setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
                    driver = setupTestDriver.getDriver();
                    runAutomation_SaleQueue(driver, mapValue, accountDTO);
                    break;
                case "runAutomation_WaiveField":
                    setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
                    driver = setupTestDriver.getDriver();
                    runAutomation_Field(driver, mapValue, browser, project);
                    break;
                case "runAutomation_return":
                    accountDTO = pollAccountFromQueue(accounts, project);
                    setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
                    driver = setupTestDriver.getDriver();
                    runAutomation_return(driver, mapValue, accountDTO);
                    break;
                case "runAutomation_fine1Neo":
                    accountDTO = pollAccountFromQueue(accounts, project);
                    setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
                    driver = setupTestDriver.getDriver();
                    runAutomation_fine1Neo(driver, mapValue, accountDTO);
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
            LoginPageNeo loginPage = new LoginPageNeo(driver);
            Utilities.captureScreenShot(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword(), "CAS");
            loginPage.clickLogin();
            Utilities.captureScreenShot(driver);
            //actions.moveToElement(loginPage.getBtnElement()).click().build().perform();

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));


            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "HOME PAGE";
            HomePageNeo homePage = new HomePageNeo(driver);


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
            QuickLeadPageNeo quickLeadPage = new QuickLeadPageNeo(driver);
            quickLeadPage.setData(quickLead);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "LEADS GRID";
            // ========== LEAD PAGE =================
            LeadsPageNeo leadsPage = new LeadsPageNeo(driver);
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
            //leadsPage.setData(leadApp);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            //stage = "LEAD STAGE";
            // ========== LEAD STAGE =================
            Thread.sleep(10000);
            this.getApplicationLead(driver, leadApp);
            LeadDetailPage leadDetailPage = new LeadDetailPage(driver);
            await("contentElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadDetailPage.getContentElement().isDisplayed());

            boolean flagResult = leadDetailPage.setData(quickLead, leadApp, downdloadFileURL);

            if (flagResult == false) {
                application.setApplicationId("UNKNOW");
                application.setStatus("ERROR");
                application.setStage(stage);
                application.setDescription("File not enough!!!");
                return;
            }

            Utilities.captureScreenShot(driver);

            await("Lead Page timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Lead Grid"));


            String leadAppID = "";
            //update thêm phần assign về acc tạo app để tranh rơi vào pool
            stage = "APPLICATION MANAGER";
            // ========== APPLICATION MANAGER =================
            leadAppID = this.assignManger2(driver, leadApp, "TPF DATA ENTRY", accountDTO.getUserName());
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
            logout(driver, accountDTO.getUserName());
        }
    }

    public void runAutomation_UpdateInfo(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO) throws Exception {
        String stage = "";
        Instant start = Instant.now();
        Application application = Application.builder().build();
        Actions actions=new Actions(driver);
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
            LoginPageNeo loginPage = new LoginPageNeo(driver);
            Utilities.captureScreenShot(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword(), "CAS");
            loginPage.clickLogin();
            Utilities.captureScreenShot(driver);
            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "HOME PAGE";
            HomePageNeo homePage = new HomePageNeo(driver);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ========== APPLICATIONS =================
            String leadAppID = application.getApplicationId();
//            homePage.getMenuApplicationElement().click();
//
            stage = "APPLICATION MANAGER";
//            // ========== APPLICATION MANAGER =================
//            this.assignManger(driver, leadAppID, "TPF DATA ENTRY", accountDTO.getUserName());
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
//
            stage = "APPLICATION GRID";
            // ========== APPLICATION GRID =================
            this.getApplicationAppId(driver, leadAppID);
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
//
            stage = "LEAD DETAILS (DATA ENTRY)";
//            // ========== LEAD DETAILS (DATA ENTRY) =================
            LeadDetailDEPage leadDetailDEPage = new LeadDetailDEPage(driver);
            leadDetailDEPage.applicationInformation();
            Utilities.captureScreenShot(driver);
//
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
//
            stage = "PERSONAL INFORMATION";

            // ========== PERSONAL INFORMATION =================
            DE_ApplicationInfoPage appInfoPage = new DE_ApplicationInfoPage(driver);
            DE_ApplicationInfoPersonalTabNeo personalTab = appInfoPage.getApplicationInfoPersonalTabNeo();
////
            await("getPersonalCustomerDetailsElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> personalTab.getPersonalCustomerDetailsElement().isDisplayed());
////// comment delete
//            personalTab.setValue(applicationInfoDTO);
            personalTab.updateValue(applicationInfoDTO);
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "EMPLOYMENT DETAILS";
            // ========== EMPLOYMENT DETAILS =================
            driver.findElement(By.id("customerMainChildTabs_employment_tab")).click();
            await("Load employment details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> appInfoPage.getEmploymentDetailsTabElement().getAttribute("class").contains("active"));

            DE_ApplicationInfoEmploymentDetailsTabNeo employmentDetailsTab = appInfoPage.getApplicationInfoEmploymentDetailsTabNeo();
            await("Span Application Id not enabled Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> employmentDetailsTab.getApplicationId().isEnabled());

            //validate data truoc khi update: employee detail, update 8-9-2020
            if (!applicationInfoDTO.getEmploymentDetails().getOccupationType().equals("Others")) {
                if (StringUtils.isEmpty(applicationInfoDTO.getEmploymentDetails().getIndustry())) {
                    //UPDATE STATUS
                    application.setStatus("ERROR");
                    application.setStage(stage);
                    application.setDescription("Industry must have value");
                    return;
                }
            }
            //end validate

            employmentDetailsTab.setData(applicationInfoDTO.getEmploymentDetails());
            employmentDetailsTab.getDoneBtnElement().click();

//             check error sau khi click done - employee detail , update 8-9-2020
            List<WebElement> checkError = driver.findElements(By.xpath("//span[contains(text(), 'This field is required.')]"));

            if (checkError != null && checkError.size() > 0) {
                //UPDATE STATUS
                application.setStatus("ERROR");
                application.setStage(stage);
                application.setDescription("Error at employment detail");
                return;
            }
//             end check error

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
                DE_ApplicationInfoFinancialDetailsTabNeo financialDetailsTab = appInfoPage.getApplicationInfoFinancialDetailsTabNeo();

                financialDetailsTab.openIncomeDetailSection();
                await("Load financial details - income details Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> financialDetailsTab.getIncomeDetailDivElement().isDisplayed());
                financialDetailsTab.setIncomeDetailsData(applicationInfoDTO.getIncomeDetails());
                actions.moveToElement(financialDetailsTab.getBtnSaveAndNextElement()).click().build().perform();
                financialDetailsTab.saveAndNext();
            }

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "BANK/CREDIT CARD DETAILS";
            // ==========BANK/CREDIT CARD DETAILS=================
            if (applicationInfoDTO.getBankCreditCardDetails() != null && applicationInfoDTO.getBankCreditCardDetails().size() > 0) {
                await("Load Bank/Credit card details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> driver.findElement(By.id("bankDetails")).isDisplayed());
                await("Load Bank/Credit card details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> appInfoPage.getBankCreditCardDetailsDetailsTabElement().getAttribute("class").contains("active"));

                DE_ApplicationInfoBankCreditCardDetailsTabNeo bankCreditCardDetailsTab = appInfoPage.getApplicationInfoBankCreditCardDetailsTabNeo();
                bankCreditCardDetailsTab.updateBankDetailsData(applicationInfoDTO.getBankCreditCardDetails());
                bankCreditCardDetailsTab.saveAndNext();
            }

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ==========LOAN DETAILS=================
            stage = "LOAN DETAIL PAGE - SOURCING DETAIL TAB";
            DE_LoanDetailsPage loanDetailsPage = new DE_LoanDetailsPage(driver);
            Utilities.captureScreenShot(driver);
            loanDetailsPage.getTabLoanDetailsElement().click();

            DE_LoanDetailsSourcingDetailsTabNeo loanDetailsSourcingDetailsTab = new DE_LoanDetailsSourcingDetailsTabNeo(driver);
            await("Load loan details - sourcing details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getTabSourcingDetailsElement().getAttribute("class").contains("active"));
            await("Load loan details - sourcing details container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getSourcingDetailsDivContainerElement().isDisplayed());
            loanDetailsSourcingDetailsTab.setData(loanDetailsDTO);
            Utilities.captureScreenShot(driver);
            actions.moveToElement(loanDetailsSourcingDetailsTab.getBtnSaveAndNextElement()).click().build().perform();

            //==================
            try {
                Thread.sleep(5000);
                List<WebElement> confirmDeleteVap = driver.findElements(By.xpath("//div[@class='modal-scrollable']//a[contains(@id, 'confirmDeleteVapNext')]"));
                if (confirmDeleteVap.size() > 0){
                    await("getBtnConfirmDeleteVapNextElement1 visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                            .until(() -> confirmDeleteVap.get(0).isDisplayed());
                    Utilities.captureScreenShot(driver);
                    confirmDeleteVap.get(0).click();
                    System.out.println("LOAN DETAILS: CONFIRM DELETE VAP");
                }
            } catch (Exception e) {
                log.info("Vap:" + e.toString());
            }

            //}


            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
//
//            // ==========VAP DETAILS=======================
            if (loanDetailsVapDTO != null && loanDetailsVapDTO.getVapProduct() != null && !loanDetailsVapDTO.getVapProduct().equals("")) {
                stage = "LOAN DETAIL PAGE - VAP DETAIL TAB";
                Utilities.captureScreenShot(driver);
                DE_LoanDetailsVapDetailsTabNeo loanDetailsVapDetailsTab = new DE_LoanDetailsVapDetailsTabNeo(driver);

                await("Load loan details - sourcing details container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> loanDetailsVapDetailsTab.getVapDetailsDivContainerElement().isDisplayed());
                loanDetailsVapDetailsTab.updateData(loanDetailsVapDTO);
                Utilities.captureScreenShot(driver);
                loanDetailsVapDetailsTab.getBtnSaveAndNextElement().click();

                System.out.println("LOAN DETAILS - VAP: DONE");

            }
//
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "DOCUMENTS";
//            // ==========DOCUMENTS=================
            if (documentDTOS.size() > 0) {
//                DE_DocumentsPage documentsPage = new DE_DocumentsPage(driver);
//                await("Load document tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                        .until(() -> documentsPage.getTabDocumentsElement().isDisplayed() && documentsPage.getTabDocumentsElement().isEnabled());
//                documentsPage.getTabDocumentsElement().click();
//                await("Load document container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                        .until(() -> documentsPage.getDocumentsContainerElement().isDisplayed());
//                documentsPage.getBtnGetDocumentElement().click();
//                await("Load document table Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                        .until(() -> documentsPage.getLendingTrElement().size() > 0);
//
//                documentsPage.updateData(documentDTOS, downdloadFileURL);
//                Utilities.captureScreenShot(driver);
//                documentsPage.getBtnSubmitElement().click();
            }

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "REFERENCES";
            // ==========REFERENCES=================
            stage = "REFERENCES PAGE";
            DE_ReferencesPageNeo referencesPage = new DE_ReferencesPageNeo(driver);
            await("Load references tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> referencesPage.getTabReferencesElement().isDisplayed() && referencesPage.getTabReferencesElement().isEnabled());
            referencesPage.getTabReferencesElement().click();
            Thread.sleep(5000);
            referencesPage.updateData(referenceDTO);
            referencesPage.getSaveBtnElement().click();

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ==========MISC FRM APP DTL=================
            stage = "MISC FRM APPDTL PAGE";
            DE_MiscFrmAppDtlPageNeo miscFrmAppDtlPage = new DE_MiscFrmAppDtlPageNeo(driver);
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

            actions.moveToElement(miscFrmAppDtlPage.getBtnMoveToNextStageElement()).click().build().perform();
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
//
//            logout(driver);

        } catch (Exception e) {
            //UPDATE STATUS
            application.setStatus("ERROR");
            application.setStage(stage);
            application.setDescription(e.getMessage() + "- toString: " + e.toString());

            System.out.println(stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();
//
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
//
            logout(driver, accountDTO.getUserName());
//
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
                    updateAppError_EndLeadDetailV1(driver, stageError, accountDTO, leadAppID, mapValue);//KO BIT KHAC GI
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
            logout(driver, accountDTO.getUserName());
        }
    }

    private void updateStatusRabbit(Application application, String func) throws Exception {

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
            LoginPageNeo loginPage = new LoginPageNeo(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword(), "CAS");
            loginPage.clickLogin();
            Utilities.captureScreenShot(driver);

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));
            Utilities.captureScreenShot(driver);

            stage = "HOME PAGE";
            HomePage homePage = new HomePage(driver);

            //DATA INPUT UPDATE
            //String leadAppID="APPL00086527";
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "APPLICATION MANAGER";
//            // ========== APPLICATION MANAGER =================
//            this.assignManger(driver, leadAppID, "TPF DATA ENTRY", accountDTO.getUserName());
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);


            stage = "APPLICATION GRID";
            // ========== APPLICATION GRID =================
            this.getApplicationAppId(driver, leadAppID);
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
//
            stage = "LEAD DETAILS (DATA ENTRY)";
//            // ========== LEAD DETAILS (DATA ENTRY) =================
            LeadDetailDEPage leadDetailDEPage = new LeadDetailDEPage(driver);
            leadDetailDEPage.applicationInformation();
            Utilities.captureScreenShot(driver);
//
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            // ==========MISC FRM APP DTL=================
            stage = "MISC FRM APPDTL";
            DE_MiscFrmAppDtlPageNeo miscFrmAppDtlPage = new DE_MiscFrmAppDtlPageNeo(driver);
            miscFrmAppDtlPage.getTabMiscFrmAppDtlElementByName().click();

            miscFrmAppDtlPage.updateData(miscFrmAppDtlDTO);

            await("getBtnSaveElement end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFrmAppDtlPage._getBtnSaveElement().isEnabled());

            Utilities.captureScreenShot(driver);
            miscFrmAppDtlPage._getBtnSaveElement().click();

            await("getBtnMoveToNextStageElement end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFrmAppDtlPage.getBtnMoveToNextStageElement().isEnabled());

            //tam thoi cho sleep de an notification moi click dc button movetonextstage
            Thread.sleep(10000);

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
            logout(driver, accountDTO.getUserName());
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
            logout(driver, accountDTO.getUserName());
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
            LoginPageNeo loginPage = new LoginPageNeo(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword(), "CAS");
            loginPage.clickLogin();
            Utilities.captureScreenShot(driver);
            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));


            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "HOME PAGE";
            HomePage homePage = new HomePage(driver);

            //DATA INPUT UPDATE
            //String leadAppID="APPL00086527";
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ========== APPLICATIONS =================
//            String leadAppID = application.getApplicationId();
//            homePage.getMenuApplicationElement().click();
//
            stage = "APPLICATION MANAGER";
//            // ========== APPLICATION MANAGER =================
//            this.assignManger(driver, leadAppID, "TPF DATA ENTRY", accountDTO.getUserName());
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);


            stage = "APPLICATION GRID";
            // ========== APPLICATION GRID =================
            this.getApplicationAppId(driver, leadAppID);
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
//
            stage = "LEAD DETAILS (DATA ENTRY)";
//            // ========== LEAD DETAILS (DATA ENTRY) =================
            LeadDetailDEPage leadDetailDEPage = new LeadDetailDEPage(driver);
            leadDetailDEPage.applicationInformation();
            Utilities.captureScreenShot(driver);
//
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "PERSONAL INFORMATION";
            // ========== PERSONAL INFORMATION =================
            DE_ApplicationInfoPage appInfoPage = new DE_ApplicationInfoPage(driver);
            DE_ApplicationInfoPersonalTabNeo personalTab = appInfoPage.getApplicationInfoPersonalTabNeo();

            await("getPersonalCustomerDetailsElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> personalTab.getPersonalCustomerDetailsElement().isDisplayed());

            personalTab.updateValue(applicationInfoDTO);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);await("Load employment details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> appInfoPage.getEmploymentDetailsTabElement().getAttribute("class").contains("active"));

            DE_ApplicationInfoEmploymentDetailsTabNeo employmentDetailsTab = appInfoPage.getApplicationInfoEmploymentDetailsTabNeo();
            await("Span Application Id not enabled Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> employmentDetailsTab.getApplicationId().isEnabled());

            //validate data truoc khi update: employee detail, update 8-9-2020
            if (!applicationInfoDTO.getEmploymentDetails().getOccupationType().equals("Others")) {
                if (StringUtils.isEmpty(applicationInfoDTO.getEmploymentDetails().getIndustry())) {
                    //UPDATE STATUS
                    application.setStatus("ERROR");
                    application.setStage(stage);
                    application.setDescription("Industry must have value");
                    return;
                }
            }
            //end validate

            employmentDetailsTab.updateData(applicationInfoDTO.getEmploymentDetails());
            employmentDetailsTab.getDoneBtnElement().click();
            // check error sau khi click done - employee detail , update 8-9-2020
            List<WebElement> checkError = driver.findElements(By.xpath("//span[contains(text(), 'This field is required.')]"));

            if (checkError != null && checkError.size() > 0) {
                //UPDATE STATUS
                application.setStatus("ERROR");
                application.setStage(stage);
                application.setDescription("Error at employment detail");
                return;
            }
            // end check error

            await("Employment Status loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> employmentDetailsTab.getTableAfterDoneElement().isDisplayed());
            employmentDetailsTab.setExperienceInIndustry(applicationInfoDTO.getEmploymentDetails());
            employmentDetailsTab.getSaveAndNextBtnElement().click();

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "FINANCIAL DETAILS";
            // ==========FINANCIAL DETAILS =================
            if (applicationInfoDTO.getIncomeDetails() != null && applicationInfoDTO.getIncomeDetails().size() > 0) {
                DE_ApplicationInfoFinancialDetailsTabNeo financialDetailsTab = appInfoPage.getApplicationInfoFinancialDetailsTabNeo();
                financialDetailsTab.openIncomeDetailSection();
                await("Load financial details - income details Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> financialDetailsTab.getIncomeDetailDivElement().isDisplayed());
                financialDetailsTab.updateIncomeDetailsData(applicationInfoDTO.getIncomeDetails());
                financialDetailsTab.saveAndNext();
            }

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "BANK/CREDIT CARD DETAILS";
            // ==========BANK/CREDIT CARD DETAILS=================
            await("Load Bank/Credit card details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> appInfoPage.getBankCreditCardDetailsDetailsTabElement().getAttribute("class").contains("active"));

            DE_ApplicationInfoBankCreditCardDetailsTabNeo bankCreditCardDetailsTab = appInfoPage.getApplicationInfoBankCreditCardDetailsTabNeo();
            bankCreditCardDetailsTab.updateBankDetailsData(applicationInfoDTO.getBankCreditCardDetails());
            bankCreditCardDetailsTab.saveAndNext();

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ==========LOAN DETAILS=================
            stage = "LOAN DETAIL - SOURCING DETAIL TAB";
            DE_LoanDetailsPage loanDetailsPage = new DE_LoanDetailsPage(driver);
            Utilities.captureScreenShot(driver);
            loanDetailsPage.getTabLoanDetailsElement().click();

            DE_LoanDetailsSourcingDetailsTabNeo loanDetailsSourcingDetailsTab = new DE_LoanDetailsSourcingDetailsTabNeo(driver);
            await("Load loan details - sourcing details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getTabSourcingDetailsElement().getAttribute("class").contains("active"));
            await("Load loan details - sourcing details container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getSourcingDetailsDivContainerElement().isDisplayed());
            loanDetailsSourcingDetailsTab.updateData(loanDetailsDTO);
            Utilities.captureScreenShot(driver);
            Actions actions=new Actions(driver);
            actions.moveToElement(loanDetailsSourcingDetailsTab.getBtnSaveAndNextElement()).click().build().perform();

            try {
                Thread.sleep(5000);
                List<WebElement> confirmDeleteVap = driver.findElements(By.xpath("//div[@class='modal-scrollable']//a[contains(@id, 'confirmDeleteVapNext')]"));
                if (confirmDeleteVap.size() > 0){
                    await("getBtnConfirmDeleteVapNextElement1 visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                            .until(() -> confirmDeleteVap.get(0).isDisplayed());
                    Utilities.captureScreenShot(driver);
                    confirmDeleteVap.get(0).click();
                    System.out.println("LOAN DETAILS: CONFIRM DELETE VAP");
                }
            } catch (Exception e) {
                log.info("Vap:" + e.toString());
            }


            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ========== VAP DETAILS =======================
            if (loanDetailsVapDTO != null && loanDetailsVapDTO.getVapProduct() != null && !loanDetailsVapDTO.getVapProduct().equals("")) {
                stage = "LOAN DETAIL - VAP DETAIL TAB";
                Utilities.captureScreenShot(driver);
                DE_LoanDetailsVapDetailsTabNeo loanDetailsVapDetailsTab = new DE_LoanDetailsVapDetailsTabNeo(driver);

                await("Load loanDetailsVapDetailsTab container Timeout!").atMost(Constant.TIME_OUT_5_M, TimeUnit.SECONDS)
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

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "REFERENCES";
            DE_ReferencesPageNeo referencesPage = new DE_ReferencesPageNeo(driver);
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
            DE_MiscFrmAppDtlPageNeo miscFrmAppDtlPage = new DE_MiscFrmAppDtlPageNeo(driver);
            miscFrmAppDtlPage.getTabMiscFrmAppDtlElementByName().click();

            miscFrmAppDtlPage.updateData(miscFrmAppDtlDTO);

            await("getBtnSaveElement end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFrmAppDtlPage._getBtnSaveElement().isEnabled());

            Utilities.captureScreenShot(driver);
            miscFrmAppDtlPage._getBtnSaveElement().click();

            await("getBtnMoveToNextStageElement end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFrmAppDtlPage.getBtnMoveToNextStageElement().isEnabled());

            //tam thoi cho sleep de an notification moi click dc button movetonextstage
            Thread.sleep(10000);

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
            logout(driver, accountDTO.getUserName());
        }
    }

    private void updateAppError_EndLeadDetailWithAddress(WebDriver driver, String stage, LoginDTO accountDTO, String leadAppID, Map<String, Object> mapValue) throws Exception {
        Application application = Application.builder().build();

        try {
            stage = "INIT DATA";
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
            LoginPageNeo loginPage = new LoginPageNeo(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword(), "CAS");
            loginPage.clickLogin();
            Utilities.captureScreenShot(driver);

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));
            Utilities.captureScreenShot(driver);

            stage = "HOME PAGE";
            HomePage homePage = new HomePage(driver);

            //DATA INPUT UPDATE
            //String leadAppID="APPL00086527";
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "APPLICATION MANAGER";
//            // ========== APPLICATION MANAGER =================
//            this.assignManger(driver, leadAppID, "TPF DATA ENTRY", accountDTO.getUserName());
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);


            stage = "APPLICATION GRID";
            // ========== APPLICATION GRID =================
            this.getApplicationAppId(driver, leadAppID);
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
//
            stage = "LEAD DETAILS (DATA ENTRY)";
//            // ========== LEAD DETAILS (DATA ENTRY) =================
            LeadDetailDEPage leadDetailDEPage = new LeadDetailDEPage(driver);
            leadDetailDEPage.applicationInformation();
            Utilities.captureScreenShot(driver);
//
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "PERSONAL INFORMATION";
            // ========== PERSONAL INFORMATION =================
            DE_ApplicationInfoPage appInfoPage = new DE_ApplicationInfoPage(driver);
            DE_ApplicationInfoPersonalTabNeo personalTab = appInfoPage.getApplicationInfoPersonalTabNeo();

            await("getPersonalCustomerDetailsElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> personalTab.getPersonalCustomerDetailsElement().isDisplayed());

            personalTab.updateValue(applicationInfoDTO);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "EMPLOYMENT DETAILS";
            // ========== EMPLOYMENT DETAILS =================

            await("Load employment details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> appInfoPage.getEmploymentDetailsTabElement().getAttribute("class").contains("active"));

            DE_ApplicationInfoEmploymentDetailsTabNeo employmentDetailsTab = appInfoPage.getApplicationInfoEmploymentDetailsTabNeo();
            await("Span Application Id not enabled Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> employmentDetailsTab.getApplicationId().isEnabled());

            //validate data truoc khi update: employee detail, update 8-9-2020
            if (!applicationInfoDTO.getEmploymentDetails().getOccupationType().equals("Others")) {
                if (StringUtils.isEmpty(applicationInfoDTO.getEmploymentDetails().getIndustry())) {
                    //UPDATE STATUS
                    application.setStatus("ERROR");
                    application.setStage(stage);
                    application.setDescription("Industry must have value");
                    return;
                }
            }
            //end validate

            employmentDetailsTab.updateData(applicationInfoDTO.getEmploymentDetails());
            employmentDetailsTab.getDoneBtnElement().click();
            // check error sau khi click done - employee detail , update 8-9-2020
            List<WebElement> checkError = driver.findElements(By.xpath("//span[contains(text(), 'This field is required.')]"));

            if (checkError != null && checkError.size() > 0) {
                //UPDATE STATUS
                application.setStatus("ERROR");
                application.setStage(stage);
                application.setDescription("Error at employment detail");
                return;
            }
            // end check error

            await("Employment Status loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> employmentDetailsTab.getTableAfterDoneElement().isDisplayed());
            employmentDetailsTab.setExperienceInIndustry(applicationInfoDTO.getEmploymentDetails());
            employmentDetailsTab.getSaveAndNextBtnElement().click();

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "BANK/CREDIT CARD DETAILS";
            // ==========BANK/CREDIT CARD DETAILS=================
            await("Load Bank/Credit card details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> appInfoPage.getBankCreditCardDetailsDetailsTabElement().getAttribute("class").contains("active"));

            DE_ApplicationInfoBankCreditCardDetailsTabNeo bankCreditCardDetailsTab = appInfoPage.getApplicationInfoBankCreditCardDetailsTabNeo();
            bankCreditCardDetailsTab.updateBankDetailsData(applicationInfoDTO.getBankCreditCardDetails());
            bankCreditCardDetailsTab.saveAndNext();

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ==========LOAN DETAILS=================
            stage = "LOAN DETAIL - SOURCING DETAIL TAB";
            DE_LoanDetailsPage loanDetailsPage = new DE_LoanDetailsPage(driver);
            Utilities.captureScreenShot(driver);
            loanDetailsPage.getTabLoanDetailsElement().click();

            DE_LoanDetailsSourcingDetailsTabNeo loanDetailsSourcingDetailsTab = new DE_LoanDetailsSourcingDetailsTabNeo(driver);
            await("Load loan details - sourcing details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getTabSourcingDetailsElement().getAttribute("class").contains("active"));
            await("Load loan details - sourcing details container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getSourcingDetailsDivContainerElement().isDisplayed());
            loanDetailsSourcingDetailsTab.updateData(loanDetailsDTO);
            Utilities.captureScreenShot(driver);
            Actions actions=new Actions(driver);
            actions.moveToElement(loanDetailsSourcingDetailsTab.getBtnSaveAndNextElement()).click().build().perform();

            try {
                Thread.sleep(5000);
                List<WebElement> confirmDeleteVap = driver.findElements(By.xpath("//div[@class='modal-scrollable']//a[contains(@id, 'confirmDeleteVapNext')]"));
                if (confirmDeleteVap.size() > 0){
                    await("getBtnConfirmDeleteVapNextElement1 visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                            .until(() -> confirmDeleteVap.get(0).isDisplayed());
                    Utilities.captureScreenShot(driver);
                    confirmDeleteVap.get(0).click();
                    System.out.println("LOAN DETAILS: CONFIRM DELETE VAP");
                }
            } catch (Exception e) {
                log.info("Vap:" + e.toString());
            }


            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ========== VAP DETAILS =======================
            if (loanDetailsVapDTO != null && loanDetailsVapDTO.getVapProduct() != null && !loanDetailsVapDTO.getVapProduct().equals("")) {
                stage = "LOAN DETAIL - VAP DETAIL TAB";
                Utilities.captureScreenShot(driver);
                DE_LoanDetailsVapDetailsTabNeo loanDetailsVapDetailsTab = new DE_LoanDetailsVapDetailsTabNeo(driver);

                await("Load loanDetailsVapDetailsTab container Timeout!").atMost(Constant.TIME_OUT_5_M, TimeUnit.SECONDS)
                        .until(() -> loanDetailsVapDetailsTab.getVapDetailsDivContainerElement().isDisplayed());

                loanDetailsVapDetailsTab.updateData(loanDetailsVapDTO);
                Utilities.captureScreenShot(driver);
                loanDetailsVapDetailsTab.getBtnSaveAndNextElement().click();

                System.out.println("LOAN DETAILS - VAP: DONE");

            }

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ==========MISC FRM APP DTL=================
            stage = "MISC FRM APPDTL";
            DE_MiscFrmAppDtlPageNeo miscFrmAppDtlPage = new DE_MiscFrmAppDtlPageNeo(driver);
            miscFrmAppDtlPage.getTabMiscFrmAppDtlElementByName().click();

            miscFrmAppDtlPage.updateData(miscFrmAppDtlDTO);

            await("getBtnSaveElement end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFrmAppDtlPage._getBtnSaveElement().isEnabled());

            Utilities.captureScreenShot(driver);
            miscFrmAppDtlPage._getBtnSaveElement().click();

            await("getBtnMoveToNextStageElement end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFrmAppDtlPage.getBtnMoveToNextStageElement().isEnabled());

            //tam thoi cho sleep de an notification moi click dc button movetonextstage
            Thread.sleep(10000);

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
            logout(driver, accountDTO.getUserName());
        }
    }

    private void updateAppError_Full(WebDriver driver, String stage, LoginDTO accountDTO, String leadAppID, Map<String, Object> mapValue) throws Exception {
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




            System.out.println(stage + ": DONE");
            stage = "LOGIN FINONE";
            HashMap<String, String> dataControl = new HashMap<>();
            LoginPageNeo loginPage = new LoginPageNeo(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword(), "CAS");
            loginPage.clickLogin();
            Utilities.captureScreenShot(driver);
            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));


            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "HOME PAGE";
            HomePage homePage = new HomePage(driver);

            //DATA INPUT UPDATE
            //String leadAppID="APPL00086527";
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ========== APPLICATIONS =================
//            String leadAppID = application.getApplicationId();
//            homePage.getMenuApplicationElement().click();
//
            stage = "APPLICATION MANAGER";
//            // ========== APPLICATION MANAGER =================
//            this.assignManger(driver, leadAppID, "TPF DATA ENTRY", accountDTO.getUserName());
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);


            stage = "APPLICATION GRID";
            // ========== APPLICATION GRID =================
            this.getApplicationAppId(driver, leadAppID);
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
//
            stage = "LEAD DETAILS (DATA ENTRY)";
//            // ========== LEAD DETAILS (DATA ENTRY) =================
            LeadDetailDEPage leadDetailDEPage = new LeadDetailDEPage(driver);
            leadDetailDEPage.applicationInformation();
            Utilities.captureScreenShot(driver);
//
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
//
            stage = "PERSONAL INFORMATION";
            // ========== PERSONAL INFORMATION =================
            DE_ApplicationInfoPage appInfoPage = new DE_ApplicationInfoPage(driver);
            DE_ApplicationInfoPersonalTabNeo personalTab = appInfoPage.getApplicationInfoPersonalTabNeo();

            await("getPersonalCustomerDetailsElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> personalTab.getPersonalCustomerDetailsElement().isDisplayed());

            personalTab.updateValue(applicationInfoDTO);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "EMPLOYMENT DETAILS";
//             ========== EMPLOYMENT DETAILS =================
            await("Load employment details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> appInfoPage.getEmploymentDetailsTabElement().getAttribute("class").contains("active"));

            DE_ApplicationInfoEmploymentDetailsTabNeo employmentDetailsTab = appInfoPage.getApplicationInfoEmploymentDetailsTabNeo();
            await("Span Application Id not enabled Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> employmentDetailsTab.getApplicationId().isEnabled());

            //validate data truoc khi update: employee detail, update 8-9-2020
            if (!applicationInfoDTO.getEmploymentDetails().getOccupationType().equals("Others")) {
                if (StringUtils.isEmpty(applicationInfoDTO.getEmploymentDetails().getIndustry())) {
                    //UPDATE STATUS
                    application.setStatus("ERROR");
                    application.setStage(stage);
                    application.setDescription("Industry must have value");
                    return;
                }
            }
            //end validate

            employmentDetailsTab.updateData(applicationInfoDTO.getEmploymentDetails());
            employmentDetailsTab.getDoneBtnElement().click();
            // check error sau khi click done - employee detail , update 8-9-2020
            List<WebElement> checkError = driver.findElements(By.xpath("//span[contains(text(), 'This field is required.')]"));

            if (checkError != null && checkError.size() > 0) {
                //UPDATE STATUS
                application.setStatus("ERROR");
                application.setStage(stage);
                application.setDescription("Error at employment detail");
                return;
            }
            // end check error

            await("Employment Status loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> employmentDetailsTab.getTableAfterDoneElement().isDisplayed());
            employmentDetailsTab.setExperienceInIndustry(applicationInfoDTO.getEmploymentDetails());
            employmentDetailsTab.getSaveAndNextBtnElement().click();

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "FINANCIAL DETAILS";
            // ==========FINANCIAL DETAILS =================
            if (applicationInfoDTO.getIncomeDetails() != null && applicationInfoDTO.getIncomeDetails().size() > 0) {
                DE_ApplicationInfoFinancialDetailsTabNeo financialDetailsTab = appInfoPage.getApplicationInfoFinancialDetailsTabNeo();
                financialDetailsTab.openIncomeDetailSection();
                await("Load financial details - income details Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> financialDetailsTab.getIncomeDetailDivElement().isDisplayed());
                financialDetailsTab.updateIncomeDetailsData(applicationInfoDTO.getIncomeDetails());
                financialDetailsTab.saveAndNext();
            }

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "BANK/CREDIT CARD DETAILS";
            // ==========BANK/CREDIT CARD DETAILS=================
            await("Load Bank/Credit card details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> appInfoPage.getBankCreditCardDetailsDetailsTabElement().getAttribute("class").contains("active"));

            DE_ApplicationInfoBankCreditCardDetailsTabNeo bankCreditCardDetailsTab = appInfoPage.getApplicationInfoBankCreditCardDetailsTabNeo();
            bankCreditCardDetailsTab.updateBankDetailsData(applicationInfoDTO.getBankCreditCardDetails());
            bankCreditCardDetailsTab.saveAndNext();

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ==========LOAN DETAILS=================
            stage = "LOAN DETAIL - SOURCING DETAIL TAB";
            DE_LoanDetailsPage loanDetailsPage = new DE_LoanDetailsPage(driver);
            Utilities.captureScreenShot(driver);
            loanDetailsPage.getTabLoanDetailsElement().click();

            DE_LoanDetailsSourcingDetailsTabNeo loanDetailsSourcingDetailsTab = new DE_LoanDetailsSourcingDetailsTabNeo(driver);
            await("Load loan details - sourcing details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getTabSourcingDetailsElement().getAttribute("class").contains("active"));
            await("Load loan details - sourcing details container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getSourcingDetailsDivContainerElement().isDisplayed());
            loanDetailsSourcingDetailsTab.updateData(loanDetailsDTO);
            Utilities.captureScreenShot(driver);
            Actions actions=new Actions(driver);
            actions.moveToElement(loanDetailsSourcingDetailsTab.getBtnSaveAndNextElement()).click().build().perform();

            try {
                Thread.sleep(5000);
                List<WebElement> confirmDeleteVap = driver.findElements(By.xpath("//div[@class='modal-scrollable']//a[contains(@id, 'confirmDeleteVapNext')]"));
                if (confirmDeleteVap.size() > 0){
                    await("getBtnConfirmDeleteVapNextElement1 visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                            .until(() -> confirmDeleteVap.get(0).isDisplayed());
                    Utilities.captureScreenShot(driver);
                    confirmDeleteVap.get(0).click();
                    System.out.println("LOAN DETAILS: CONFIRM DELETE VAP");
                }
            } catch (Exception e) {
                log.info("Vap:" + e.toString());
            }


            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ========== VAP DETAILS =======================
            if (loanDetailsVapDTO != null && loanDetailsVapDTO.getVapProduct() != null && !loanDetailsVapDTO.getVapProduct().equals("")) {
                stage = "LOAN DETAIL - VAP DETAIL TAB";
                Utilities.captureScreenShot(driver);
                DE_LoanDetailsVapDetailsTabNeo loanDetailsVapDetailsTab = new DE_LoanDetailsVapDetailsTabNeo(driver);

                await("Load loanDetailsVapDetailsTab container Timeout!").atMost(Constant.TIME_OUT_5_M, TimeUnit.SECONDS)
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
//                DE_DocumentsPage documentsPage = new DE_DocumentsPage(driver);
//                await("Load document tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                        .until(() -> documentsPage.getTabDocumentsElement().isDisplayed() && documentsPage.getTabDocumentsElement().isEnabled());
//                documentsPage.getTabDocumentsElement().click();
//                await("Load document container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                        .until(() -> documentsPage.getDocumentsContainerElement().isDisplayed());
//                documentsPage.getBtnGetDocumentElement().click();
//                await("Load document table Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                        .until(() -> documentsPage.getLendingTrElement().size() > 0);
//
//                documentsPage.updateData(documentDTOS, downdloadFileURL);
//                Utilities.captureScreenShot(driver);
//                documentsPage.getBtnSubmitElement().click();
//                Thread.sleep(10000);
            }


            // ==========REFERENCES=================
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "REFERENCES";
            DE_ReferencesPageNeo referencesPage = new DE_ReferencesPageNeo(driver);
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
            DE_MiscFrmAppDtlPageNeo miscFrmAppDtlPage = new DE_MiscFrmAppDtlPageNeo(driver);
            miscFrmAppDtlPage.getTabMiscFrmAppDtlElementByName().click();

            miscFrmAppDtlPage.updateData(miscFrmAppDtlDTO);

            await("getBtnSaveElement end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFrmAppDtlPage._getBtnSaveElement().isEnabled());

            Utilities.captureScreenShot(driver);
            miscFrmAppDtlPage._getBtnSaveElement().click();

            await("getBtnMoveToNextStageElement end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFrmAppDtlPage.getBtnMoveToNextStageElement().isEnabled());

            //tam thoi cho sleep de an notification moi click dc button movetonextstage
            Thread.sleep(10000);

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
            String function = mapValue.getOrDefault("func", "updateAppError").toString();
            updateStatusRabbit(application, function);
            System.out.println(stage);
            logout(driver, accountDTO.getUserName());
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
            logout(driver, accountDTO.getUserName());
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
            logout(driver, accountDTO.getUserName());
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

            logout(driver, accountDTO.getUserName());
            LD_updateStatusRabbit(application, "updateAutomation", "momo");

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

            logout(driver, accountDTO.getUserName());
            LD_updateStatusRabbit(application, "updateAutomation", "fpt");

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
                        System.out.println("Get it:" + accountDTONew.getUserName());
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
            LoginPageNeo loginPage = new LoginPageNeo(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword(),"CAS");
            loginPage.clickLogin();
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

                        String appID = autoAssignDTO.getAppid();
                        SearchMenu goToMn = new SearchMenu(driver);
                        goToMn.MoveToPage(Constant.MENU_NAME_LINK_APPLICATION_MANAGER);
                        // ========== APPLICATION MANAGER =================
                        stage = "APPLICATION MANAGER";
                        await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(driver::getTitle, is("Application Manager"));

                        DE_ApplicationManagerPage de_applicationManagerPage = new DE_ApplicationManagerPage(driver);

                        await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(() -> de_applicationManagerPage.getApplicationManagerFormElement().isDisplayed());

                        de_applicationManagerPage.setData(appID, autoAssignDTO.getUsername().toLowerCase());

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
            logout(driver, accountDTO.getUserName());
            pushAccountToQueue(accountDTO, project);
        }
    }
    //------------------------ END AUTO ASSIGN -----------------------------------------------------

    //region PROJECT: AUTO SMARTNET
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
                    System.out.println("Get it:" + accountDTO.getUserName());
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

            //***************************//LOGIN PAGE//***************************//

            LoginPageNeo loginPage = new LoginPageNeo(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword(),"CAS");
            loginPage.clickLogin();
            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));

            //***************************//END LOGIN//***************************//

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            System.out.println("Auto:" + accountDTO.getUserName() + " - GET DONE " + " - " + " App: " + deResponseQueryDTO.getAppId() + " - Time: " + Duration.between(start, Instant.now()).toSeconds());

            stage = "HOME PAGE";
            SearchMenu searchMenu = new SearchMenu(driver);
            searchMenu.MoveToPage(Constant.MENU_NAME_LINK_RESPONSE_QUERY);
            // ========== APPLICATIONS =================
            stage = "RESPONSE QUERY";

            // ========== RESPONSE QUERY =================
            DE_ReturnRaiseQueryPage de_ReturnRaiseQueryPage = new DE_ReturnRaiseQueryPage(driver);
            de_ReturnRaiseQueryPage.setData(deResponseQueryDTO, downdloadFileURL);
            System.out.println("Auto - FINISH: " + " - " + " App: " + deResponseQueryDTO.getAppId() + " - Time: " + Duration.between(start, Instant.now()).toSeconds());

            // ========= UPDATE DB ============================

            Query queryUpdate1 = new Query();
            queryUpdate1.addCriteria(Criteria.where("status").is(2).and("appId").is(deResponseQueryDTO.getAppId()));
            Update update1 = new Update();
            update1.set("userauto", accountDTO.getUserName());
            update1.set("status", 1);
            System.out.println("Auto: " + accountDTO.getUserName() + " - UPDATE STATUS " + " - " + " App: " + deResponseQueryDTO.getAppId() + " - Time: " + Duration.between(start, Instant.now()).toSeconds());

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

            System.out.println("Auto DONE:" + responseModel.getAutomation_result() + "- Project " + responseModel.getProject() + "- AppId " + responseModel.getApp_id());

            mongoTemplate.save(deResponseQueryDTO);
            logout(driver, accountDTO.getUserName());
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

            //***************************//LOGIN PAGE//***************************//
            LoginPageNeo loginPage = new LoginPageNeo(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword(),"CAS");
            loginPage.clickLogin();
            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));

            //***************************//END LOGIN//***************************//
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ========== SALE QUEUE =================
            stage = "SALE QUEUE";
            DE_ReturnSaleQueuePage de_ReturnSaleQueuePage = new DE_ReturnSaleQueuePage(driver);
            de_ReturnSaleQueuePage.setData(deSaleQueueDTO, accountDTO.getUserName().toLowerCase(), downdloadFileURL);

            System.out.println("Auto - FINISH: " + stage + " - " + " App: " + deSaleQueueDTO.getAppId() + " - Time: " + Duration.between(start, Instant.now()).toSeconds());

            // ========== Last Update User ACCA =================
            if (!Objects.isNull(deSaleQueueDTO.getUserCreatedSalesQueue())) {
                AssignManagerSaleQueuePage de_applicationManagerPage = new AssignManagerSaleQueuePage(driver);
                //update code, nếu không có up ACCA thì chuyen thang len DC nên reassing là user da raise saleQUEUE
                if (!deSaleQueueDTO.getDataDocuments().stream().filter(c -> c.getDocumentName().contains("(ACCA)")).findAny().isPresent()) {
                    SearchMenu searchMenuMN = new SearchMenu(driver);
                    searchMenuMN.MoveToPage(Constant.MENU_NAME_LINK_APPLICATION_MANAGER);

                    // ========== APPLICATION MANAGER =================
                    stage = "APPLICATION MANAGER";

                    await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                            .until(driver::getTitle, is("Application Manager"));

                    de_applicationManagerPage.setData(deSaleQueueDTO.getAppId(), accountDTO.getUserName());
                    SearchMenu searchMenuApp = new SearchMenu(driver);
                    searchMenuApp.MoveToPage(Constant.MENU_NAME_LINK_APPLICATIONS);

                    await("Application timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                            .until(driver::getTitle, is("Application Grid"));

                    de_applicationManagerPage.getApplicationAssignedNumberElement().clear();
                    de_applicationManagerPage.getApplicationAssignedNumberElement().sendKeys(deSaleQueueDTO.getAppId());
                    de_applicationManagerPage.getApplicationAssignedNumberElement().sendKeys(Keys.ENTER);

                    await("tbApplicationAssignedElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                            .until(() -> de_applicationManagerPage.getTbApplicationAssignedElement().size() > 2);

                    WebElement applicationIdAssignedNumberElement = driver.findElement(By.xpath("//*[@id='LoanApplication_Assigned_wrapper']/div[1]/div/div[2]/div[2]/div/table/tbody/tr/td/a"));

                    await("webAssignElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                            .until(() -> applicationIdAssignedNumberElement.isDisplayed());

                    applicationIdAssignedNumberElement.click();

                    de_applicationManagerPage.getBtnMoveToNextStageElement().click();

                    await("Application timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                            .until(driver::getTitle, is("Application Grid"));
//                    de_applicationManagerPage.getMenuApplicationElement().click();
//                    de_applicationManagerPage.getApplicationManagerElement().click();

                    SearchMenu searchMenuMN_1 = new SearchMenu(driver);
                    searchMenuMN_1.MoveToPage(Constant.MENU_NAME_LINK_APPLICATION_MANAGER);

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
            } else {
                AssignManagerSaleQueuePage de_applicationManagerPage = new AssignManagerSaleQueuePage(driver);

                SearchMenu searchMenuMN_2 = new SearchMenu(driver);
                searchMenuMN_2.MoveToPage(Constant.MENU_NAME_LINK_APPLICATION_MANAGER);

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
            System.out.println("Auto DONE:" + responseModel.getAutomation_result() + "- Project " + responseModel.getProject() + "- AppId " + responseModel.getApp_id());
            mongoTemplate.save(deSaleQueueDTO);
            logout(driver, accountDTO.getUserName());
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
            LoginPageNeo loginPage = new LoginPageNeo(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword(),"CAS");
            loginPage.clickLogin();
            //actions.moveToElement(loginPage.getBtnElement()).click().build().perform();

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));


            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "HOME PAGE";
            HomePageNeo homePage = new HomePageNeo(driver);


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
            QuickLeadPageNeo quickLeadPage = new QuickLeadPageNeo(driver);
            quickLeadPage.setData(quickLead);
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "LEADS GRID";
            // ========== LEAD PAGE =================
            LeadsPageNeo leadsPage = new LeadsPageNeo(driver);
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
//            leadsPage.setData(leadApp);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "LEAD STAGE";
            // ========== LEAD STAGE =================
            //Thread.sleep(3000);
            //this.assignManger(driver, leadApp, "TPF DATA ENTRY", "trunglc");
            //=============================
            Thread.sleep(10000);
            this.getApplicationLead(driver, leadApp);
            //=============================
            LeadDetailPage leadDetailPage = new LeadDetailPage(driver);
            await("contentElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadDetailPage.getContentElement().isDisplayed());
            boolean flagResult = leadDetailPage.setData(quickLead, leadApp, downdloadFileURL);

            if (flagResult == false) {
                application.setApplicationId("UNKNOW");
                application.setStatus("ERROR");
                application.setStage(stage);
                application.setDescription("File not enough!!!");
                return;
            }

            Utilities.captureScreenShot(driver);

            await("Lead Page timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Lead Grid"));
            String leadAppID = "";

//            System.out.println("LEAD APP: =>" + leadApp);

            //update thêm phần assign về acc tạo app để tranh rơi vào pool
            stage = "APPLICATION MANAGER";
            // ========== APPLICATION MANAGER =================
            leadAppID = this.assignManger2(driver, leadApp, "TPF DATA ENTRY", "trunglc");

            //get appID moi o day
            System.out.println(" APP: =>" + leadAppID);

            //smartner bo phan reassign user auto

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            //-------------------- END ---------------------------

            application.setApplicationId(leadAppID);
            application.setLeadApp(leadApp);

            //UPDATE STATUS
            application.setStatus("QUICKLEAD PASS");
            application.setDescription("Thanh cong");


            Utilities.captureScreenShot(driver);

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
                application.setStatus("QUICKLEAD FAIL");
                application.setDescription("Khong thanh cong");
            }

            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            try {
                SN_updateStatusRabbit(application, "updateAutomation", "smartnet");
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            SN_updateDB(application);
            logout(driver, accountDTO.getUserName());

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
    //endregion

    //region PROJECT: AUTO MOBILITY
    //------------------------ QUICKLEAD - MOBILITY-----------------------------------------------------
    public void MOBILITY_runAutomation_QuickLead(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO) throws Exception {
        Instant start = Instant.now();
        String appId = "";
        String stage = "";
        Application application = Application.builder().build();
        SessionId session = ((RemoteWebDriver) driver).getSessionId();
        log.info("{}", application);
        try {
            stage = "INIT DATA";
            Date currentDateTest = new Date();
            //*************************** GET DATA *********************//
            application = (Application) mapValue.get("ApplicationDTO");
            application.setAutomationAcc(accountDTO.getUserName());
            QuickLead quickLead = application.getQuickLead();
            //mongoTemplate.save(application);


            //*************************** END GET DATA *********************//
            Actions actions = new Actions(driver);
            System.out.println(stage + ": DONE");


            //***************************//LOGIN FINONE//***************************//
            stage = "LOGIN FINONE";

            LoginPageNeo loginPage = new LoginPageNeo(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword(), "CAS");
            loginPage.clickLogin();
            //***************************//END LOGIN//***************************//

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "HOME PAGE";
            HomePageNeo homePage = new HomePageNeo(driver);

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
            QuickLeadPageNeo quickLeadPage = new QuickLeadPageNeo(driver);
            quickLeadPage.setData(quickLead);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "LEADS GRID";
            // ========== LEAD PAGE =================
            LeadsPageNeo leadsPage = new LeadsPageNeo(driver);
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
//            leadsPage.setData(leadApp);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "LEAD STAGE";
            // ========== LEAD STAGE =================
            //Thread.sleep(3000);
            //this.assignManger(driver, leadApp, "TPF DATA ENTRY", "trunglc");
            //=============================
            Thread.sleep(10000);
            this.getApplicationLead(driver, leadApp);

            //=============================
            LeadDetailPage leadDetailPage = new LeadDetailPage(driver);
            await("contentElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadDetailPage.getContentElement().isDisplayed());

            boolean flagResult = leadDetailPage.setData(quickLead, leadApp, downdloadFileURL);

            if (flagResult == false) {
                application.setApplicationId("UNKNOW");
                application.setStatus("ERROR");
                application.setStage(stage);
                application.setDescription("File not enough!!!");
                return;
            }

            Utilities.captureScreenShot(driver);

            await("Lead Page timeout").atMost(Constant.TIME_OUT_5_M, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Lead Grid"));



            String leadAppID = "";
//            //-------------------- END ---------------------------
            //update thêm phần assign về acc tạo app để tranh rơi vào pool
            stage = "APPLICATION MANAGER";
            leadAppID = this.assignManger2(driver, leadApp, "TPF DATA ENTRY", accountDTO.getUserName());
            System.out.println(" APP: =>" + leadAppID);


            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            //-------------------- END ---------------------------

            application.setApplicationId(leadAppID);
            application.setLeadApp(leadApp);

            //UPDATE STATUS
            application.setStatus("QUICKLEAD PASS");
            application.setDescription("Thanh cong" + " - Session: " + session);


            Utilities.captureScreenShot(driver);
            //logout(driver);

        } catch (Exception e) {
            //UPDATE STATUS
            application.setStatus("QUICKLEAD FAIL");
            application.setStage(stage);
            application.setDescription(e.getMessage() + " - Session: " + session);

            System.out.println(stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);

        } finally {
            if (application.getApplicationId() == null || application.getApplicationId().isEmpty() || application.getApplicationId().indexOf("LEAD") > 0 || application.getApplicationId().indexOf("APPL") < 0) {
                application.setApplicationId("UNKNOW");
                application.setStatus("QUICKLEAD_FAILED");
                if (!"File not enough!!!".equals(application.getDescription())) {
                    application.setDescription("Khong thanh cong" + " - Session: " + session);
                }else{
                    application.setDescription(application.getDescription() + " - Session: " + session);
                }
            }

            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            try {
                MOBILITY_updateStatusRabbit(application, "updateAutomation", "mobility");
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            MOBILITY_updateDB(application);
            logout(driver, accountDTO.getUserName());

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
    public void runAutomation_Field(WebDriver driver, Map<String, Object> mapValue, String browser, String projectAuto) throws Exception {
        String stage = "";
        int _totalAppId = 0;
        try {
            stage = "INIT DATA";

            List<LoginDTO> loginDTOList = new ArrayList<LoginDTO>();

            LoginDTO accountDTONew = null;
            do {
                //get list account finone available
                Query query = new Query();
                query.addCriteria(Criteria.where("active").is(0).and("project").is(projectAuto));
                AccountFinOneDTO accountFinOneDTO = mongoTemplate.findOne(query, AccountFinOneDTO.class);
                if (!Objects.isNull(accountFinOneDTO)) {
                    accountDTONew = new LoginDTO().builder().userName(accountFinOneDTO.getUsername()).password(accountFinOneDTO.getPassword()).build();

                    Query queryUpdate = new Query();
                    queryUpdate.addCriteria(Criteria.where("active").is(0).and("username").is(accountFinOneDTO.getUsername()).and("project").is(projectAuto));
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
            RequestAutomationDTO RequestAutomationWaiveFieldDTOList = (RequestAutomationDTO) mapValue.get("RequestAutomationWaiveFieldList");
            _totalAppId = RequestAutomationWaiveFieldDTOList.getWaiveFieldDTO().size();
            List<WaiveFieldDTO> waiveFieldDTOList = (List<WaiveFieldDTO>) mapValue.get("waiveFieldList");
            mongoTemplate.insert(waiveFieldDTOList, WaiveFieldDTO.class);
            //*************************** END GET DATA *********************//

            if (loginDTOList.size() > 0) {
                ExecutorService workerThreadPoolDE = Executors.newFixedThreadPool(loginDTOList.size());

                for (LoginDTO loginDTO : loginDTOList) {
                    int totalAppId = _totalAppId;
                    workerThreadPoolDE.execute(new Runnable() {
                        @SneakyThrows
                        @Override
                        public void run() {
                            runAutomation_Waive_Field_run(loginDTO, browser, projectAuto, totalAppId);
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
    private void runAutomation_Waive_Field_run(LoginDTO accountDTO, String browser, String projectAuto, int totalAppId) {
        WebDriver driver = null;
        Instant start = Instant.now();
        String stage = "";
        String referenceId = "";
        String projectId = "";
        ResponseAutomationModel responseModel = new ResponseAutomationModel();
        System.out.println("START - Auto: " + accountDTO.getUserName() + " - Time: " + Duration.between(start, Instant.now()).toSeconds());
        try {
            SeleniumGridDriver setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
            driver = setupTestDriver.getDriver();
            SessionId session = ((RemoteWebDriver)driver).getSessionId();

            //***************************//LOGIN FINONE//***************************//
            stage = "LOGIN FINONE";

            LoginPageNeo loginPage = new LoginPageNeo(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword(),"CAS");
            loginPage.clickLogin();

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));

            //***************************//END LOGIN//***************************//
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            System.out.println("Auto: " + accountDTO.getUserName() + " - " + stage + ": DONE" + " - Time: " + Duration.between(start, Instant.now()).toSeconds());

            WaiveFieldDTO waiveFieldDTO = null;

            do {
                try {
                    System.out.println("Auto: " + accountDTO.getUserName() + " - BEGIN " + " - Time: " + Duration.between(start, Instant.now()).toSeconds());
                    Query query = new Query();
                    query.addCriteria(Criteria.where("status").is(0));
                    waiveFieldDTO = mongoTemplate.findOne(query, WaiveFieldDTO.class);

                    if (!Objects.isNull(waiveFieldDTO)) {

                        //update app
                        Query queryUpdate = new Query();
                        queryUpdate.addCriteria(Criteria.where("_id").is(new ObjectId(waiveFieldDTO.getId())).and("status").is(0).and("project").is(waiveFieldDTO.getProject()).and("projectAuto").is("WAIVEFIELD"));
                        Update update = new Update();
                        update.set("userAuto", accountDTO.getUserName());
                        update.set("status", 2);
                        WaiveFieldDTO resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, WaiveFieldDTO.class);

                        if (resultUpdate == null) {
                            continue;
                        }

                        System.out.println("Auto: " + accountDTO.getUserName() + " App: " + waiveFieldDTO.getAppId() + " - GET DONE " + " - User: " + accountDTO.getUserName() + " - Time: " + Duration.between(start, Instant.now()).toSeconds());

                        referenceId = resultUpdate.getReferenceId();
                        projectId = resultUpdate.getProject();

                        stage = "HOME PAGE";
                        System.out.println(stage + ": DONE" + " - Time " + Duration.between(start, Instant.now()).toSeconds());

                        // ========== CHECK STAGE APPLICATIONS =================
                        stage = "CHECK STAGE APPLICATION";

                        FV_CheckStageApplicationManager fv_CheckStageApplicationManager = new FV_CheckStageApplicationManager(driver);
                        SearchMenu gotoAppMn = new SearchMenu(driver);
                        gotoAppMn.MoveToPage(Constant.MENU_NAME_LINK_APPLICATION_MANAGER);

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

                        System.out.println("PAGE LOADING: " + driver.getTitle());

                        await("WAIVE OFF ALL - FIELD INVESTIGATION INITIATION failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(driver::getTitle, is("Application Grid"));

                        System.out.println(stage + ": DONE" + " - Time " + Duration.between(start, Instant.now()).toSeconds());

                        // ========= UPDATE DB ============================
                        Query queryUpdate1 = new Query();
                        queryUpdate1.addCriteria(Criteria.where("_id").is(new ObjectId(waiveFieldDTO.getId())).and("status").is(2).and("project").is(waiveFieldDTO.getProject()).and("projectAuto").is("WAIVEFIELD"));
                        Update update1 = new Update();
                        update1.set("userAuto", accountDTO.getUserName());
                        update1.set("status", 1);
                        update1.set("checkUpdate", 1);
                        update1.set("automation_result", "WAIVE_FIELD_PASS");
                        update1.set("automation_result_message", "Session ID:" + session);
                        mongoTemplate.findAndModify(queryUpdate1, update1, WaiveFieldDTO.class);
                        System.out.println("Auto: " + accountDTO.getUserName() + " App: " + waiveFieldDTO.getAppId() + " - UPDATE STATUS " + " - User: " + accountDTO.getUserName() + " - Time: " + Duration.between(start, Instant.now()).toSeconds());
                    }
                } catch (Exception ex) {
                    Query queryUpdate = new Query();
                    queryUpdate.addCriteria(Criteria.where("_id").is(new ObjectId(waiveFieldDTO.getId())).and("status").is(2).and("project").is(waiveFieldDTO.getProject()).and("projectAuto").is("WAIVEFIELD"));
                    Update update = new Update();
                    update.set("userAuto", accountDTO.getUserName());
                    update.set("status", 3);
                    update.set("checkUpdate", 1);
                    update.set("automation_result", "WAIVE_FIELD_FAILED");
                    update.set("automation_result_message", "Session ID:" + session + "- ERROR: " + ex.getMessage() );
                    mongoTemplate.findAndModify(queryUpdate, update, WaiveFieldDTO.class);
                    System.out.println(ex.getMessage());
                }
            } while (!Objects.isNull(waiveFieldDTO));
        } catch (Exception e) {
            System.out.println("User Auto:" + accountDTO.getUserName() + " - " + stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);
        } finally {
            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            logout(driver,accountDTO.getUserName());
            pushAccountToQueue(accountDTO, projectAuto);
            if(!StringUtils.isEmpty(referenceId)){
                Query queryUpdateFailed = new Query();
                queryUpdateFailed.addCriteria(Criteria.where("referenceId").is(referenceId).and("checkUpdate").is(1));
                List<MobilityFieldReponeDTO> resultRespone = mongoTemplate.find(queryUpdateFailed, MobilityFieldReponeDTO.class);
                if (resultRespone.size() == totalAppId)
                {
                    responseModel.setReference_id(referenceId);
                    responseModel.setProject(projectId);
                    responseModel.setTransaction_id("transaction_waive_field");
                    responseModel.setData(resultRespone);
                    try {
                        autoUpdateStatusRabbitMobility(responseModel, "updateAutomation");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (driver != null) {
                driver.close();
                driver.quit();
            }
        }
    }

    //------------------------ END WAIVE FIELD -----------------------------------------------------

    //------------------------ SUBMIT FIELD -----------------------------------------------------
    private void runAutomation_Submit_Field_run(LoginDTO accountDTO, String browser, String project, int totalAppId) {
        WebDriver driver = null;
        Instant start = Instant.now();
        String stage = "";
        String referenceId = "";
        String projectId = "";
        ResponseAutomationModel responseModel = new ResponseAutomationModel();
        System.out.println("START - Auto: " + accountDTO.getUserName() + " - Time: " + Duration.between(start, Instant.now()).toSeconds());
        try {
            SeleniumGridDriver setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
            driver = setupTestDriver.getDriver();
            SessionId session = ((RemoteWebDriver)driver).getSessionId();
            //get account run
            stage = "LOGIN FINONE";
            LoginPage loginPage = new LoginPage(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
            loginPage.clickLogin();

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));
            System.out.println("Auto: " + accountDTO.getUserName() + " - " + stage + ": DONE" + " - Time: " + Duration.between(start, Instant.now()).toSeconds());
            Utilities.captureScreenShot(driver);

            SubmitFieldDTO submitFieldDTO = null;

            do {
                try {
                    System.out.println("Auto: " + accountDTO.getUserName() + " - BEGIN " + " - Time: " + Duration.between(start, Instant.now()).toSeconds());
                    Query query = new Query();
                    query.addCriteria(Criteria.where("status").is(0));
                    submitFieldDTO = mongoTemplate.findOne(query, SubmitFieldDTO.class);

                    if (!Objects.isNull(submitFieldDTO)) {

                        //update app
                        Query queryUpdate = new Query();
                        queryUpdate.addCriteria(Criteria.where("_id").is(new ObjectId(submitFieldDTO.getId())).and("status").is(0).and("project").is(submitFieldDTO.getProject()).and("funcProject").is("SubmitField"));
                        Update update = new Update();
                        update.set("userAuto", accountDTO.getUserName());
                        update.set("referenceId", submitFieldDTO.getReferenceId());
                        update.set("status", 2);
                        SubmitFieldDTO resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, SubmitFieldDTO.class);

                        if (resultUpdate == null) {
                            continue;
                        }

                        System.out.println("Auto: " + accountDTO.getUserName() + " App: " + submitFieldDTO.getAppId() + " - GET DONE " + " - User: " + accountDTO.getUserName() + " - Time: " + Duration.between(start, Instant.now()).toSeconds());

                        referenceId = resultUpdate.getReferenceId();
                        projectId = resultUpdate.getProject();

                        stage = "HOME PAGE";
//                        HomePage homePage = new HomePage(driver);
                        System.out.println(stage + ": DONE" + " - Time " + Duration.between(start, Instant.now()).toSeconds());

                        // ========== APPLICATIONS =================
//                        homePage.getMenuApplicationElement().click();

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
                        queryUpdate1.addCriteria(Criteria.where("_id").is(new ObjectId(submitFieldDTO.getId())).and("status").is(2).and("project").is(submitFieldDTO.getProject()).and("funcProject").is("SubmitField"));
                        Update update1 = new Update();
                        update1.set("userAuto", accountDTO.getUserName());
                        update1.set("status", 1);
                        update1.set("checkUpdate", 1);
                        update1.set("automation_result", "SUBMIT_FIELD_PASS");
                        update1.set("automation_result_message", "Session ID:" + session);
                        mongoTemplate.findAndModify(queryUpdate1, update1, SubmitFieldDTO.class);
                        if(StringUtils.isEmpty(referenceId)){
                            Query queryUpdateFailed = new Query();
                            queryUpdateFailed.addCriteria(Criteria.where("referenceId").is(referenceId).and("checkUpdate").is(1));
                            List<MobilityFieldReponeDTO> resultRespone = mongoTemplate.find(queryUpdateFailed, MobilityFieldReponeDTO.class);
                            if (resultRespone.size() == totalAppId)
                            {
                                responseModel.setReference_id(referenceId);
                                responseModel.setProject(projectId);
                                responseModel.setTransaction_id("transaction_submit_field");
                                responseModel.setData(resultRespone);
                                try {
                                    autoUpdateStatusRabbitMobility(responseModel, "updateAutomation");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        System.out.println("Auto: " + accountDTO.getUserName() + " App: " + submitFieldDTO.getAppId() + " - UPDATE STATUS " + " - User: " + accountDTO.getUserName() + " - Time: " + Duration.between(start, Instant.now()).toSeconds());
                    }
                } catch (Exception ex) {
                    Query queryUpdate = new Query();
                    queryUpdate.addCriteria(Criteria.where("_id").is(new ObjectId(submitFieldDTO.getId())).and("status").is(2).and("project").is(submitFieldDTO.getProject()).and("funcProject").is("SubmitField"));
                    Update update = new Update();
                    update.set("userAuto", accountDTO.getUserName());
                    update.set("status", 3);
                    update.set("checkUpdate", 1);
                    update.set("automation_result", "SUBMIT_FIELD_FAILED");
                    update.set("automation_result_message", "Session ID:" + session + "- ERROR: " + ex.getMessage() );
                    mongoTemplate.findAndModify(queryUpdate, update, SubmitFieldDTO.class);
                    if(StringUtils.isEmpty(referenceId)){
                        Query queryUpdateFailed = new Query();
                        queryUpdateFailed.addCriteria(Criteria.where("referenceId").is(referenceId).and("checkUpdate").is(1));
                        List<MobilityFieldReponeDTO> resultRespone = mongoTemplate.find(queryUpdateFailed, MobilityFieldReponeDTO.class);
                        if (resultRespone.size() == totalAppId)
                        {
                            responseModel.setReference_id(referenceId);
                            responseModel.setProject(projectId);
                            responseModel.setTransaction_id("transaction_submit_field");
                            responseModel.setData(resultRespone);
                            try {
                                autoUpdateStatusRabbitMobility(responseModel, "updateAutomation");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    System.out.println(ex.getMessage());
                }
            } while (!Objects.isNull(submitFieldDTO));
        } catch (Exception e) {
            System.out.println("User Auto:" + accountDTO.getUserName() + " - " + stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();
            Utilities.captureScreenShot(driver);
        } finally {
            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            logout(driver,accountDTO.getUserName());
            pushAccountToQueue(accountDTO, project);
            if (driver != null) {
                driver.close();
                driver.quit();
            }
        }
    }

    public void runAutomation_Submit_Fields_run(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO) throws Exception {
        ResponseAutomationModel responseModel = new ResponseAutomationModel();
        Instant start = Instant.now();
        String stage = "";
        String referenceId = "";
        String projectId = "";
        String applicationId = "UNKNOWN";
        String idMongoDb = "";
        System.out.println("START - Auto: " + accountDTO.getUserName() + " - Time: " + Duration.between(start, Instant.now()).toSeconds());
        SubmitFieldDTO submitFieldDTO = SubmitFieldDTO.builder().build();
        SessionId session = ((RemoteWebDriver)driver).getSessionId();
        try {
            stage = "INIT DATA";
            //*************************** INSERT DATA *********************//
            SubmitFieldDTO submitFieldDTOList = (SubmitFieldDTO) mapValue.get("submitFieldList");
//            mongoTemplate.insert(submitFieldDTOList);
            SubmitFieldDTO submitFieldIdMongoDB = mongoTemplate.insert(submitFieldDTOList);
            idMongoDb = submitFieldIdMongoDB.getId();
            System.out.println("Id_MongoDB => " + idMongoDb);
            applicationId = submitFieldDTOList.getAppId();
            referenceId = submitFieldDTOList.getReferenceId();
            projectId = submitFieldDTOList.getProject();
            System.out.println("Application_Id: " + applicationId);
            System.out.println("Reference_Id: " + referenceId);
            //*************************** END INSERT DATA *********************//

            //*************************** GET DATA *********************//
            System.out.println("Auto: " + accountDTO.getUserName() + " - BEGIN " + " - Time: " + Duration.between(start, Instant.now()).toSeconds());
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(new ObjectId(idMongoDb)).and("status").is(0).and("appId").is(applicationId).and("project").is(projectId).and("projectAuto").is("SUBMITFIELD"));
            submitFieldDTO = mongoTemplate.findOne(query, SubmitFieldDTO.class);
            log.info("{}", submitFieldDTO);

            Update update = new Update();
            update.set("userAuto", accountDTO.getUserName());
            update.set("status", 2);
            mongoTemplate.findAndModify(query, update, SubmitFieldDTO.class);

            //*************************** END GET DATA *********************//

            System.out.println(stage + ": DONE");

            //***************************//LOGIN FINONE//***************************//
            stage = "LOGIN FINONE";

            LoginPageNeo loginPage = new LoginPageNeo(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword(),"CAS");
            loginPage.clickLogin();

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));

            //***************************//END LOGIN//***************************//
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "HOME PAGE";
            System.out.println(stage + ": DONE" + " - Time " + Duration.between(start, Instant.now()).toSeconds());

            // ========== CHECK STAGE APPLICATIONS =================
            stage = "CHECK STAGE APPLICATION";
            FV_CheckStageApplicationManager fv_CheckStageApplicationManager = new FV_CheckStageApplicationManager(driver);
            SearchMenu goToMn = new SearchMenu(driver);
            goToMn.MoveToPage(Constant.MENU_NAME_LINK_APPLICATION_MANAGER);

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

                System.out.println("PAGE LOADING: " + driver.getTitle());

                await("Field Investigation Initiation failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> fv_FieldVerificationPage.getCheckMoveToNextStageElement().size() == 0);

                await("FIELD INVESTIGATION INITIATION failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(driver::getTitle, is("Application Grid"));

                System.out.println("STAGE - " + stageApplication + " - PASS");

                System.out.println(stage + ": DONE" + " - Time " + Duration.between(start, Instant.now()).toSeconds());
                stageApplication = "FIV";
            }
            if ("FIV".equals(stageApplication)){
                // ========== FIELD INVESTIGATION VERIFICATION =================
                stage = "FIELD INVESTIGATION VERIFICATION";
                FV_FieldInvestigationVerificationPage fv_FieldInvestigationVerificationPage = new FV_FieldInvestigationVerificationPage(driver);
                fv_FieldInvestigationVerificationPage.setData(submitFieldDTO, downdloadFileURL, accountDTO.getUserName());

                System.out.println("PAGE LOADING: " + driver.getTitle());

                await("FIELD INVESTIGATION VERIFICATION failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(driver::getTitle, is("FI Entries Grid"));

                System.out.println("STAGE - " + stageApplication + " - PASS");

                System.out.println(stage + ": DONE" + " - Time " + Duration.between(start, Instant.now()).toSeconds());
                stageApplication = "FIC";
            }
            if ("FIC".equals(stageApplication)){
                // ========== FIELD INVESTIGATION DETAILS =================
                stage = "FIELD INVESTIGATION DETAILS";
                FV_FieldInvestigationDetailsPage fv_FieldInvestigationDetailsPage = new FV_FieldInvestigationDetailsPage(driver);
                fv_FieldInvestigationDetailsPage.setData(submitFieldDTO, accountDTO.getUserName().toLowerCase());

                System.out.println("PAGE LOADING: " + driver.getTitle());

//                await("Field Investigation Details failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                        .until(() -> fv_FieldInvestigationDetailsPage.getCheckMoveToNextStageElement().size() == 0);

                await("FIELD INVESTIGATION DETAILS failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(driver::getTitle, is("Application Grid"));

                System.out.println("STAGE - " + stageApplication + " - PASS");

                System.out.println(stage + ": DONE" + " - Time " + Duration.between(start, Instant.now()).toSeconds());
            } else {
                await("Stage wrong Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> "FII".equals(fv_CheckStageApplicationManager.getTdCheckStageApplicationElement().getText()));
            }

            // ========= UPDATE DB ============================
            Query queryUpdate1 = new Query();
            queryUpdate1.addCriteria(Criteria.where("_id").is(new ObjectId(idMongoDb)).and("status").is(2).and("appId").is(applicationId).and("project").is(submitFieldDTO.getProject()).and("projectAuto").is("SUBMITFIELD"));
            Update update1 = new Update();
            update1.set("userAuto", accountDTO.getUserName());
            update1.set("status", 1);
            update1.set("automation_result", "SUBMIT_FIELD_PASS");
            update1.set("automation_result_message", "Session ID:" + session);
            mongoTemplate.findAndModify(queryUpdate1, update1, SubmitFieldDTO.class);

            Utilities.captureScreenShot(driver);

        } catch (Exception e) {
            Query queryUpdate = new Query();
            queryUpdate.addCriteria(Criteria.where("_id").is(new ObjectId(idMongoDb)).and("status").is(2).and("appId").is(applicationId).and("project").is(submitFieldDTO.getProject()).and("projectAuto").is("SUBMITFIELD"));
            Update update = new Update();
            update.set("userAuto", accountDTO.getUserName());
            update.set("status", 3);
            update.set("automation_result", "SUBMIT_FIELD_FAILED");
            update.set("automation_result_message", "Session ID:" + session + "- ERROR: " + e.getMessage() );
            mongoTemplate.findAndModify(queryUpdate, update, SubmitFieldDTO.class);
            System.out.println("Auto Error: " + stage + "\n => MESSAGE " + e.getMessage() + " => TRACE: " + e.toString());
            e.printStackTrace();
            Utilities.captureScreenShot(driver);
        } finally {
            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            System.out.println("Auto DONE:" + applicationId + " - Project: " + projectId + " - UserAuto: " + accountDTO.getUserName());
            logout(driver,accountDTO.getUserName());
            if(!StringUtils.isEmpty(referenceId)){
                Query queryUpdateFailed = new Query();
                queryUpdateFailed.addCriteria(Criteria.where("referenceId").is(referenceId).and("_id").is(new ObjectId(idMongoDb)));
                List<MobilityFieldReponeDTO> resultRespone = mongoTemplate.find(queryUpdateFailed, MobilityFieldReponeDTO.class);
                responseModel.setReference_id(referenceId);
                responseModel.setProject(projectId);
                responseModel.setTransaction_id("transaction_submit_field");
                responseModel.setData(resultRespone);
                autoUpdateStatusRabbitMobility(responseModel, "updateAutomation");
            }
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
                                "data", responseAutomationModel.getData()
                        )));

        System.out.println("rabit:=>" + jsonNode.toString());
    }

    //------------------------ END UPDATE RABBITMQ -----------------------------------------------------
    //endregion

    //region PROJECT: AUTO CRM
    //------------------------ QUICKLEAD CRM -----------------------------------------------------
    public void CRM_runAutomation_QuickLead(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO, String project) throws Exception {
        Instant start = Instant.now();
        String stage = "";
        SessionId session = ((RemoteWebDriver) driver).getSessionId();
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
            LoginPageNeo loginPage = new LoginPageNeo(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword(),"CAS");
            loginPage.clickLogin();
            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));


            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "HOME PAGE";
            HomePageNeo homePage = new HomePageNeo(driver);


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
            CRM_QuickLeadPageNeo quickLeadPage = new CRM_QuickLeadPageNeo(driver);
            quickLeadPage.setData(quickLead);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "LEADS GRID";
            // ========== LEAD PAGE =================
            LeadsPageNeo leadsPage = new LeadsPageNeo(driver);
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
//            leadsPage.setData(leadApp);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "LEAD STAGE";
            // ========== LEAD STAGE =================
//            Thread.sleep(3000);
//            this.assignManger(driver, leadApp, "TPF DATA ENTRY", "trunglc");
            //=============================
            Thread.sleep(10000);
            this.getApplicationLead(driver, leadApp);

            CRM_LeadDetailPage leadDetailPage = new CRM_LeadDetailPage(driver);
            await("contentElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadDetailPage.getContentElement().isDisplayed());

            leadDetailPage.setData(quickLead, leadApp, downdloadFileURL);

            Utilities.captureScreenShot(driver);

            await("Lead Page timeout").atMost(Constant.TIME_OUT_5_M, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Lead Grid"));

            String leadAppID = "";
            stage = "APPLICATION MANAGER";
            // ========== APPLICATION MANAGER =================
            leadAppID = this.assignManger2(driver, leadApp, "TPF DATA ENTRY", accountDTO.getUserName());

            //get appID moi o day
            System.out.println("APP: =>" + leadAppID);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            //-------------------- END ---------------------------

            application.setApplicationId(leadAppID);
            application.setLeadApp(leadApp);

            //UPDATE STATUS
            application.setStatus("QUICKLEAD PASS");
            application.setDescription("Thanh cong" + " - Session: " + session);


            Utilities.captureScreenShot(driver);
            //logout(driver);

        } catch (Exception e) {
            //UPDATE STATUS
            application.setStatus("QUICKLEAD FAIL");
            application.setStage(stage);
            application.setDescription(e.getMessage() + " - Session: " + session);

            System.out.println(stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);
        } finally {
            if (application.getApplicationId() == null || application.getApplicationId().isEmpty() || application.getApplicationId().indexOf("LEAD") > 0 || application.getApplicationId().indexOf("APPL") < 0) {
                application.setApplicationId("UNKNOW");
                application.setStatus("QUICKLEAD_FAILED");
                if (!"File not enough!!!".equals(application.getDescription())) {
                    application.setDescription("Khong thanh cong" + " - Session: " + session);
                }else{
                    application.setDescription(application.getDescription() + " - Session: " + session);
                }
            }

            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            try {
                CRM_updateStatusRabbit(application, "updateAutomation", project.toLowerCase());
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            CRM_updateDB(application);
            logout(driver, accountDTO.getUserName());

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


    //------------------------ EXISTING CUSTOMER -----------------------------------------------------
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

            if (!Objects.isNull(existingCustomerDTO.getNeoCustID())) {
                if (!"null".equals(existingCustomerDTO.getNeoCustID().toLowerCase())) {
                    await("Neo Cust ID Text Box displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                            .until(() -> crm_ExistingCustomerPage.getNeoCustIDInputElement().isDisplayed());

                    crm_ExistingCustomerPage.getNeoCustIDInputElement().sendKeys(existingCustomerDTO.getNeoCustID());
                }
            }

            if (!Objects.isNull(existingCustomerDTO.getCifNumber())) {
                if (!"null".equals(existingCustomerDTO.getCifNumber().toLowerCase())) {
                    await("CIF Number Text Box displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                            .until(() -> crm_ExistingCustomerPage.getCifNumberInputElement().isDisplayed());

                    crm_ExistingCustomerPage.getCifNumberInputElement().sendKeys(existingCustomerDTO.getCifNumber());
                }
            }

            if (!Objects.isNull(existingCustomerDTO.getIdNumber())) {
                if (!"null".equals(existingCustomerDTO.getIdNumber().toLowerCase())) {
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

            WebElement searchCustomerSelectElement = driver.findElement(new By.ByXPath("//div[@id = 'existingCustomerSearch']//form[starts-with(@id, 'applicantSearchVoForm')]//div[@id = 'example']//table[@id = 'searchData_IndividualCustomerTable']//tbody//tr[" + tableSize + "]//td[contains(@class, 'select_individual')]//input[contains(@value,'Select')]"));

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
            String idNoo = idNo.substring(idNo.indexOf(":") + 1).trim();
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
            System.out.println("Auto DONE: " + responseModel.getAutomation_result() + " => Project: " + responseModel.getProject() + " => AppId: " + responseModel.getApp_id());
            logout(driver, accountDTO.getUserName());
            updateQuickleadExistingCustomer(existingCustomerDTO);
            autoUpdateStatusRabbit(responseModel, "updateAutomation");
        }
    }

    public void runAutomation_Existing_Customer(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO) throws Exception {
        Instant start = Instant.now();
        String stage = "";
        String applicationId = "";
        String stageError = "";
        SessionId session = ((RemoteWebDriver) driver).getSessionId();
        try {
            stage = "INIT DATA";
            //*************************** GET DATA *********************//
            CRM_ExistingCustomerDTO existingCustomerDTO = (CRM_ExistingCustomerDTO) mapValue.get("ExistingCustomerList");
            applicationId = existingCustomerDTO.getApplicationId();
            if (applicationId != null) {
                if (!applicationId.isEmpty() && !applicationId.contains("UNKNOWN")) {
                    if (applicationId.contains("APPL")) {
                        stageError = "UPDATE";
                    }
                }
            }
            //*************************** END GET DATA *********************//
            //---------------- GET STAGE -------------------------//
            switch (stageError) {
                case "UPDATE":
                    runAutomation_Existing_Customer_Update(mapValue, driver, accountDTO);
                    break;
                default:
                    runAutomation_Existing_Customer_Full(mapValue, driver, accountDTO);
                    break;
            }

            return;

        } catch (Exception e) {
            System.out.println(stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();
        } finally {
            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            System.out.println("DONE: " + accountDTO.getUserName() + " - SessionId: " + session);
            if (driver != null) {
                driver.close();
                driver.quit();
            }
        }
    }

    public void runAutomation_Existing_Customer_Full(Map<String, Object> mapValue, WebDriver driver, LoginDTO accountDTO) throws Exception {
        ResponseAutomationModel responseModel = new ResponseAutomationModel();
        Instant start = Instant.now();
        String stage = "";
        String applicationId = "UNKNOWN";
        String neoCustNo = "";
        String cifNo = "";
        String idNo = "";
        CRM_ExistingCustomerDTO existingCustomerDTO = CRM_ExistingCustomerDTO.builder().build();
        SessionId session = ((RemoteWebDriver) driver).getSessionId();
        Actions actions = new Actions(driver);
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
            mongoTemplate.save(existingCustomerDTO);
            //*************************** END GET DATA *********************//
            System.out.println(stage + ": DONE");

            stage = "LOGIN FINONE";

            //***************************//LOGIN PAGE//***************************//
            LoginPageNeo loginPage = new LoginPageNeo(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword(),"CAS");
            loginPage.clickLogin();

            //***************************//END LOGIN//***************************//

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ========== EXISTING CUSTOMER =================
            stage = "EXISTING CUSTOMER";
            CRM_ExistingCustomerPage crm_ExistingCustomerPage = new CRM_ExistingCustomerPage(driver);
            crm_ExistingCustomerPage.application_personal_Click();

            await("Personal Loan Page displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> crm_ExistingCustomerPage.getCustomerPersonal().isDisplayed());
            crm_ExistingCustomerPage.getCustomerPersonal().click();
            await("Personal Loan Page displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> crm_ExistingCustomerPage.getCreateCustomerElement().isDisplayed());

            System.out.println("PERSONAL LOAN PAGE");

            crm_ExistingCustomerPage.getIsExistCustomerRadioElement().click();

            await("Search Existing Individual Customers With displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> crm_ExistingCustomerPage.getExistingCustomerSearchFormElement().isDisplayed());

            if (!Objects.isNull(existingCustomerDTO.getNeoCustID())) {
                await("Neo Cust ID Text Box displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> crm_ExistingCustomerPage.getNeoCustIDInputElement().isDisplayed());

                crm_ExistingCustomerPage.getNeoCustIDInputElement().sendKeys(existingCustomerDTO.getNeoCustID());

            }

            if (!Objects.isNull(existingCustomerDTO.getCifNumber())) {
                await("CIF Number Text Box displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> crm_ExistingCustomerPage.getCifNumberInputElement().isDisplayed());

                crm_ExistingCustomerPage.getCifNumberInputElement().sendKeys(existingCustomerDTO.getCifNumber());

            }

            if (!StringUtils.isEmpty(existingCustomerDTO.getIdNumber())) {

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

            WebElement searchCustomerSelectElement = driver.findElement(new By.ByXPath("//div[@id = 'existingCustomerSearch']//form[starts-with(@id, 'applicantSearchVoForm')]//div[@id = 'example']//table[@id = 'searchData_IndividualCustomerTable']//tbody//tr[" + tableSize + "]//td[contains(@class, 'select_individual')]//input[contains(@value,'Select')]"));

            await("Button Select displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> searchCustomerSelectElement.isDisplayed());

            searchCustomerSelectElement.click();

            await("Customer Information displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> crm_ExistingCustomerPage.getCustomerInformationFormElement().isDisplayed());

            await("Customer Information Save button displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> crm_ExistingCustomerPage.getCustomerInformationSaveElement().isDisplayed());

            crm_ExistingCustomerPage.getCustomerInformationSaveElement().click();

            Utilities.captureScreenShot(driver);

            System.out.println("Check _v1");
            await("Work flow failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> crm_ExistingCustomerPage.getPrimaryApplicantElement().isDisplayed());

            neoCustNo = crm_ExistingCustomerPage.getPrimaryApplicantNeoCustIDInputElement().getAttribute("value");
            System.out.println("NEO CUST ID => " + neoCustNo);
            idNo = crm_ExistingCustomerPage.getPrimaryApplicantIdNumberInputElement().getAttribute("value");
            String idNoo = idNo.substring(idNo.indexOf(":") + 1).trim();
            System.out.println("ID Number => " + idNoo);
            cifNo = crm_ExistingCustomerPage.getPrimaryApplicantNeoCifNumberInputElement().getAttribute("value");
            System.out.println("CIF Number => " + cifNo);
            applicationId = crm_ExistingCustomerPage.getApplicantIdHeaderElement().getText();
            System.out.println("APPID => " + applicationId);


            //switch test flow
//            applicationId = crm_ExistingCustomerPage.getApplicantIdHeaderElement().getText();
//            System.out.println("APPID => " + applicationId);
//            SearchMenu goToApp = new SearchMenu(driver);
//            goToApp.MoveToPage(Constant.MENU_NAME_LINK_APPLICATIONS);
//
//            await("Work flow failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(driver::getTitle, is("Application Grid"));
//            ((RemoteWebDriver) driver).findElementById("lead").click();
//            WebElement inputApp = driver.findElement(By.xpath("//*[@id='LoanApplication_Assigned_wrapper']/div[1]/div/div[2]/div[1]/table/thead[1]/tr/th/input"));
//            inputApp.sendKeys("APPL01384178");
//            inputApp.sendKeys(Keys.ENTER);
//            Thread.sleep(3000);
//            driver.findElement(By.xpath("//*[@id='LoanApplication_Assigned_wrapper']/div[1]/div/div[2]/div[2]/div/table/tbody/tr/td/a")).click();
//
//            await("Work flow failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(driver::getTitle, is("Application Grid"));
//
//            driver.findElement(By.xpath("//*[@id='applicationChildTabs_customerPersonal']/a")).click();

            //switch test flow end
            // ========== VIEW/EDIT DETAILED INFORMATION =================
            stage = "VIEW/EDIT DETAILED INFORMATION";
            crm_ExistingCustomerPage.getEditCustomerExistCustomerElement().click();
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ========== APPLICANT INFORMATION =================
             //========== PERSONAL INFORMATION =================
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
                Thread.sleep(5000);
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

            if (bankDetailsTab.getBankDetailsTableElement().size() > 2) {

                await("Span Application Id not enabled Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> bankDetailsTab.getBankDetailsTableElement().size() > 2);

                await("Load deleteIdDetailElement Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> bankDetailsTab.getDeleteIdDetailElement().size() > 0);

                for (int i = 0; i < bankDetailsTab.getDeleteIdDetailElement().size(); i++) {
                    WebElement var = bankDetailsTab.getDeleteIdDetailElement().get(i);
                    var.click();
                }
            }
            // CHECK BANK/CREDIT CARD
            bankDetailsTab.saveAndNext();

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ==========LOAN DETAILS=================
            stage = "LOAN DETAIL PAGE - SOURCING DETAIL TAB";
            CRM_LoanDetailsPage loanDetailsPage = new CRM_LoanDetailsPage(driver);
            Utilities.captureScreenShot(driver);
            loanDetailsPage.getTabLoanDetailsElement().click();

            CRM_LoanDetailsSourcingDetailsTab loanDetailsSourcingDetailsTab = new CRM_LoanDetailsSourcingDetailsTab(driver);
            with().pollInterval(5, TimeUnit.SECONDS).
            await("Load loan details - sourcing details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getTabSourcingDetailsElement().getAttribute("class").contains("active"));
            await("Load loan details - sourcing details container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getSourcingDetailsDivContainerElement().isDisplayed());

            loanDetailsSourcingDetailsTab.setData(loanDetailsDTO.getSourcingDetails());

            Utilities.captureScreenShot(driver);
            loanDetailsSourcingDetailsTab.getBtnSaveAndNextElement().click();

            Thread.sleep(5000);

            boolean confirmDeleteVap = driver.findElements(By.xpath("//div[@class='modal-scrollable']//a[contains(@id, 'confirmDeleteVapNext')]")).size() != 0;

            if (confirmDeleteVap){

                await("Confirm Delete Vap Next Visibale!!!").atMost(15, TimeUnit.SECONDS)
                        .until(() -> loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElement1().isDisplayed());

                Utilities.captureScreenShot(driver);

                loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElement1().click();
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
                documentsPage.setData(documentDTOS, downdloadFileURL);
            }

            System.out.println(stage + ": DONE");

            stage = "REFERENCES";
            // ==========REFERENCES=================
            stage = "REFERENCES PAGE";
            CRM_ReferencesPage referencesPage = new CRM_ReferencesPage(driver);
            await("Load references tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> referencesPage.getTabReferencesElement().isDisplayed() && referencesPage.getTabReferencesElement().isEnabled());
            referencesPage.getTabReferencesElement().click();
            Thread.sleep(2000);
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

            miscFrmAppDtlPage.updateCommunicationValue(miscFrmAppDtlDTO.getRemark());

            with().pollInterval(org.awaitility.Duration.FIVE_SECONDS).
            await("Button Move To Next Stage Element end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFrmAppDtlPage.getBtnMoveToNextStageElement().isEnabled());

            Utilities.captureScreenShot(driver);

            Thread.sleep(15000);

            miscFrmAppDtlPage.keyActionMoveNextStage();

//            actions.moveToElement(keyActionMoveNextStage.getBtnMoveToNextStageElement()).click().perform();
//            actions.click(miscFrmAppDtlPage.getBtnMoveToNextStageElement()).perform();

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
            responseModel.setAutomation_result("QUICKLEAD PASS" + " - Session ID: " + session + " - UserAuto: " + accountDTO.getUserName());

            Utilities.captureScreenShot(driver);

            Query queryUpdate = new Query();
            queryUpdate.addCriteria(Criteria.where("_id").is(new ObjectId(existingCustomerDTO.getId())).and("quickLeadId").is(existingCustomerDTO.getQuickLeadId()));
            Update update = new Update();
            update.set("automationAcc", accountDTO.getUserName());
            update.set("applicationId", applicationId);
            update.set("status", "QUICKLEAD PASS");
            update.set("description", "Success");
            mongoTemplate.findAndModify(queryUpdate, update, CRM_ExistingCustomerDTO.class);

        } catch (Exception e) {

            responseModel.setProject(existingCustomerDTO.getProject());
            responseModel.setReference_id(existingCustomerDTO.getReference_id());
            responseModel.setTransaction_id(existingCustomerDTO.getQuickLeadId());
            responseModel.setApp_id(applicationId);
            responseModel.setAutomation_result("QUICKLEAD FAILED" + " - Session ID: " + session + " - UserAuto: " + accountDTO.getUserName() + " - " + "Error: " + e.getMessage());

            System.out.println("Auto Error: " + stage + "\n => MESSAGE " + e.getMessage() + " => TRACE: " + e.toString());
            e.printStackTrace();

            if (e.getMessage().contains("Work flow failed!!!")) {
                stage = "END OF LEAD DETAIL";
                existingCustomerDTO.setStage(stage);
                await("Get error fail!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> driver.findElements(By.id("error-message")).size() > 0);
                int errorMessage = driver.findElements(By.xpath("//div[@id = 'rule-error-block-application-completion'][contains(@class, 'block-no')]")).size();
                if(errorMessage == 0) {
                    if (driver.findElements(By.id("error-message")) != null && driver.findElements(By.id("error-message")).size() > 0) {
                        String error = "Error: ";
                        for (WebElement we : driver.findElements(By.id("error-message"))) {
                            error += we.getText();
                        }
                        existingCustomerDTO.setError(error);
                        responseModel.setAutomation_result("QUICKLEAD FAILED" + " - Session ID: " + session + " - UserAuto: " + accountDTO.getUserName() + " - Error: " + existingCustomerDTO.getError());
                        System.out.println(stage + "=>" + error);
                    }
                }else{
                    existingCustomerDTO.setError("Cannot click Move next stage");
                    responseModel.setAutomation_result("QUICKLEAD FAILED" + " - Session ID: " + session + " - UserAuto: " + accountDTO.getUserName() + " - Error: " + existingCustomerDTO.getError());
                    System.out.println(stage + "=>" + existingCustomerDTO.getError());
                }
            }

            Utilities.captureScreenShot(driver);

            Query queryUpdate = new Query();
            queryUpdate.addCriteria(Criteria.where("_id").is(new ObjectId(existingCustomerDTO.getId())).and("quickLeadId").is(existingCustomerDTO.getQuickLeadId()));
            Update update = new Update();
            update.set("automationAcc", accountDTO.getUserName());
            update.set("applicationId", applicationId);
            update.set("status", "QUICKLEAD FAILED");
            update.set("description", "Error: " + existingCustomerDTO.getError());
            mongoTemplate.findAndModify(queryUpdate, update, CRM_ExistingCustomerDTO.class);

        } finally {
            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            System.out.println(responseModel.getAutomation_result() + " => Project: " + responseModel.getProject() + " => AppId: " + responseModel.getApp_id());
            logoutV2(driver, accountDTO);
            autoUpdateStatusRabbit(responseModel, "updateAutomation");
        }
    }

    public void runAutomation_Existing_Customer_Update(Map<String, Object> mapValue, WebDriver driver, LoginDTO accountDTO) throws Exception {
        ResponseAutomationModel responseModel = new ResponseAutomationModel();
        Instant start = Instant.now();
        String stage = "";
        String applicationId = "";
        CRM_ExistingCustomerDTO existingCustomerDTO = CRM_ExistingCustomerDTO.builder().build();
        SessionId session = ((RemoteWebDriver) driver).getSessionId();
        Actions actions = new Actions(driver);
        try {
            stage = "INIT DATA";
            //*************************** GET DATA *********************//
            existingCustomerDTO = (CRM_ExistingCustomerDTO) mapValue.get("ExistingCustomerList");
            applicationId = existingCustomerDTO.getApplicationId();
            CRM_ApplicationInformationsListDTO applicationInfoDTO = (CRM_ApplicationInformationsListDTO) mapValue.get("ApplicationInfoDTO");
            CRM_LoanDetailsDTO loanDetailsDTO = (CRM_LoanDetailsDTO) mapValue.get("VapDetailsDTO");
            List<CRM_DocumentsDTO> documentDTOS = (List<CRM_DocumentsDTO>) mapValue.get("DocumentDTO");
            List<CRM_ReferencesListDTO> referenceDTO = (List<CRM_ReferencesListDTO>) mapValue.get("ReferenceDTO");
            CRM_DynamicFormDTO miscFrmAppDtlDTO = (CRM_DynamicFormDTO) mapValue.get("MiscFrmAppDtlDTO");
            log.info("{}", existingCustomerDTO);
            //*************************** END GET DATA *********************//
            System.out.println(stage + ": DONE");

            stage = "LOGIN FINONE";

            //***************************//LOGIN PAGE//***************************//

//            LoginPage loginPage = new LoginPage(driver);
//            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
//            Utilities.captureScreenShot(driver);
//            loginPage.clickLogin();
//            Utilities.captureScreenShot(driver);
//
//            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(driver::getTitle, is("DashBoard"));

            LoginPageNeo loginPage = new LoginPageNeo(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword(),"CAS");
            loginPage.clickLogin();

            //***************************//END LOGIN//***************************//

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "HOME PAGE";
            HomePageNeo homePage = new HomePageNeo(driver);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            // ========== APPLICATIONS =================
            homePage.menuClick();
            stage = "APPLICATION MANAGER";
            // ========== APPLICATION MANAGER =================
            homePage.applicationManagerClick();
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

            getApplicationAppId(driver, applicationId);
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
                Thread.sleep(5000);
                CRM_ApplicationInfoFinancialDetailsTab financialDetailsTab = appInfoPage.getApplicationInfoFinancialDetailsTab();
                financialDetailsTab.openFinancialDetailsTabSection();
                financialDetailsTab.openIncomeDetailSection();
                with().pollInterval(5, TimeUnit.SECONDS).
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

            if (bankDetailsTab.getBankDetailsTableElement().size() > 2) {

                await("Span Application Id not enabled Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> bankDetailsTab.getBankDetailsTableElement().size() > 2);

                await("Load deleteIdDetailElement Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> bankDetailsTab.getDeleteIdDetailElement().size() > 0);

                for (int i = 0; i < bankDetailsTab.getDeleteIdDetailElement().size(); i++) {
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
            with().pollInterval(5, TimeUnit.SECONDS).
            await("Load loan details - sourcing details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getTabSourcingDetailsElement().getAttribute("class").contains("active"));
            await("Load loan details - sourcing details container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getSourcingDetailsDivContainerElement().isDisplayed());

            loanDetailsSourcingDetailsTab.setData(loanDetailsDTO.getSourcingDetails());

            Utilities.captureScreenShot(driver);
            loanDetailsSourcingDetailsTab.getBtnSaveAndNextElement().click();

            Thread.sleep(5000);

//            try {
//
//                await("getBtnConfirmDeleteVapNextElement1 visibale Timeout!").atMost(60, TimeUnit.SECONDS)
//                        .until(() -> driver.findElement(By.xpath("//div[@class='modal-scrollable']//a[contains(@id, 'confirmDeleteVapNext')]")).isDisplayed());
//                //loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElement1().click();
//
//                driver.findElement(By.xpath("//div[@class='modal-scrollable']//a[contains(@id, 'confirmDeleteVapNext')]")).click();
//            } catch (Exception e) {
//                System.out.println("Confirm Delete Vap Next Visibale!!!");
//            }

            boolean confirmDeleteVap = driver.findElements(By.xpath("//div[@class='modal-scrollable']//a[contains(@id, 'confirmDeleteVapNext')]")).size() != 0;

            if (confirmDeleteVap){

                await("Confirm Delete Vap Next Visibale!!!").atMost(15, TimeUnit.SECONDS)
                        .until(() -> loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElement1().isDisplayed());

                Utilities.captureScreenShot(driver);

                loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElement1().click();
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

            miscFrmAppDtlPage.updateCommunicationValue(miscFrmAppDtlDTO.getRemark());

            with().pollInterval(org.awaitility.Duration.FIVE_SECONDS).
            await("Button Move To Next Stage Element end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFrmAppDtlPage.getBtnMoveToNextStageElement().isEnabled());

            Utilities.captureScreenShot(driver);

            Thread.sleep(15000);

            miscFrmAppDtlPage.keyActionMoveNextStage();

//            actions.moveToElement(keyActionMoveNextStage.getBtnMoveToNextStageElement()).click().perform();
//            actions.click(miscFrmAppDtlPage.getBtnMoveToNextStageElement()).perform();

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
            responseModel.setAutomation_result("QUICKLEAD PASS" + " - Session ID: " + session + " - UserAuto: " + accountDTO.getUserName());

            Utilities.captureScreenShot(driver);

            Query queryUpdate = new Query();
            queryUpdate.addCriteria(Criteria.where("appId").is(applicationId).and("quickLeadId").is(existingCustomerDTO.getQuickLeadId()));
            Update update = new Update();
            update.set("automationAcc", accountDTO.getUserName());
            update.set("applicationId", applicationId);
            update.set("status", "QUICKLEAD PASS");
            update.set("description", "Success");
            mongoTemplate.findAndModify(queryUpdate, update, CRM_ExistingCustomerDTO.class);

        } catch (Exception e) {

            responseModel.setProject(existingCustomerDTO.getProject());
            responseModel.setReference_id(existingCustomerDTO.getReference_id());
            responseModel.setTransaction_id(existingCustomerDTO.getQuickLeadId());
            responseModel.setApp_id(applicationId);
            responseModel.setAutomation_result("QUICKLEAD FAILED" + " - Session ID: " + session + " - UserAuto: " + accountDTO.getUserName() + " - Error: " + e.getMessage());

            System.out.println("Auto Error: " + stage + "\n => MESSAGE " + e.getMessage() + " => TRACE: " + e.toString());
            e.printStackTrace();

            if (e.getMessage().contains("Work flow failed!!!")) {
                stage = "END OF LEAD DETAIL";
                existingCustomerDTO.setStage(stage);
                await("Get error fail!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> driver.findElements(By.id("error-message")).size() > 0);
                int errorMessage = driver.findElements(By.xpath("//div[@id = 'rule-error-block-application-completion'][contains(@class, 'block-no')]")).size();
                if(errorMessage == 0) {
                    if (driver.findElements(By.id("error-message")) != null && driver.findElements(By.id("error-message")).size() > 0) {
                        String error = "Error: ";
                        for (WebElement we : driver.findElements(By.id("error-message"))) {
                            error += we.getText();
                        }
                        existingCustomerDTO.setError(error);
                        responseModel.setAutomation_result("QUICKLEAD FAILED" + " - Session ID: " + session + " - UserAuto: " + accountDTO.getUserName() + " - Error: " + existingCustomerDTO.getError());
                        System.out.println(stage + "=>" + error);
                    }
                }else{
                    existingCustomerDTO.setError("Cannot click Move next stage");
                    responseModel.setAutomation_result("QUICKLEAD FAILED" + " - Session ID: " + session + " - UserAuto: " + accountDTO.getUserName() + " - Error: " + existingCustomerDTO.getError());
                    System.out.println(stage + "=>" + existingCustomerDTO.getError());
                }
            }

            Utilities.captureScreenShot(driver);

            Query queryUpdate = new Query();
            queryUpdate.addCriteria(Criteria.where("appId").is(applicationId).and("quickLeadId").is(existingCustomerDTO.getQuickLeadId()));
            Update update = new Update();
            update.set("automationAcc", accountDTO.getUserName());
            update.set("applicationId", applicationId);
            update.set("status", "QUICKLEAD FAILED");
            update.set("description", "Error: " + existingCustomerDTO.getError());
            mongoTemplate.findAndModify(queryUpdate, update, CRM_ExistingCustomerDTO.class);

        } finally {
            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            System.out.println(responseModel.getAutomation_result() + " => Project: " + responseModel.getProject() + " => AppId: " + responseModel.getApp_id());
            logoutV2(driver, accountDTO);
            autoUpdateStatusRabbit(responseModel, "updateAutomation");
        }
    }

    public void runAutomation_SendBack(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO) throws Exception {
        ResponseAutomationModel responseModel = new ResponseAutomationModel();
        Instant start = Instant.now();
        String stage = "";
        String applicationId = "";
        CRM_SaleQueueDTO saleQueueDTO = CRM_SaleQueueDTO.builder().build();
        SessionId session = ((RemoteWebDriver) driver).getSessionId();
        Actions actions = new Actions(driver);
        try {
            stage = "INIT DATA";
            //*************************** GET DATA *********************//
            saleQueueDTO = (CRM_SaleQueueDTO) mapValue.get("SaleQueueList");
            applicationId = saleQueueDTO.getApplicationId();
            CRM_ApplicationInformationsListDTO applicationInfoDTO = (CRM_ApplicationInformationsListDTO) mapValue.get("ApplicationInfoDTO");
            CRM_LoanDetailsDTO loanDetailsDTO = (CRM_LoanDetailsDTO) mapValue.get("VapDetailsDTO");
            List<CRM_DocumentsDTO> documentDTOS = (List<CRM_DocumentsDTO>) mapValue.get("DocumentDTO");
            List<CRM_ReferencesListDTO> referenceDTO = (List<CRM_ReferencesListDTO>) mapValue.get("ReferenceDTO");
            CRM_DynamicFormDTO miscFrmAppDtlDTO = (CRM_DynamicFormDTO) mapValue.get("MiscFrmAppDtlDTO");
            log.info("{}", saleQueueDTO);
            mongoTemplate.save(saleQueueDTO);
            //*************************** END GET DATA *********************//
            System.out.println(stage + ": DONE");

            stage = "LOGIN FINONE";

            //***************************//LOGIN PAGE//***************************//

//            LoginPage loginPage = new LoginPage(driver);
//            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
//            Utilities.captureScreenShot(driver);
//            loginPage.clickLogin();
//            Utilities.captureScreenShot(driver);
//
//            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(driver::getTitle, is("DashBoard"));

            LoginV2Page loginPage = new LoginV2Page(driver);

            loginPage.loginValue(accountDTO);

            //***************************//END LOGIN//***************************//

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

            personalTab.setValueSendBack(applicationInfoDTO);

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
                Thread.sleep(5000);
                CRM_ApplicationInfoFinancialDetailsTab financialDetailsTab = appInfoPage.getApplicationInfoFinancialDetailsTab();
                financialDetailsTab.openFinancialDetailsTabSection();
                financialDetailsTab.openIncomeDetailSection();
                with().pollInterval(5, TimeUnit.SECONDS).
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

            if (bankDetailsTab.getBankDetailsTableElement().size() > 2) {

                await("Span Application Id not enabled Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> bankDetailsTab.getBankDetailsTableElement().size() > 2);

                await("Load deleteIdDetailElement Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> bankDetailsTab.getDeleteIdDetailElement().size() > 0);

                for (int i = 0; i < bankDetailsTab.getDeleteIdDetailElement().size(); i++) {
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
            with().pollInterval(5, TimeUnit.SECONDS).
            await("Load loan details - sourcing details tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getTabSourcingDetailsElement().getAttribute("class").contains("active"));
            await("Load loan details - sourcing details container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getSourcingDetailsDivContainerElement().isDisplayed());
            loanDetailsDTO.getSourcingDetails().setApplicationNumber(applicationId);
            loanDetailsSourcingDetailsTab.updateData(loanDetailsDTO.getSourcingDetails());

            Utilities.captureScreenShot(driver);
            loanDetailsSourcingDetailsTab.getBtnSaveAndNextElement().click();

            Thread.sleep(5000);

//            try {
//
//                await("getBtnConfirmDeleteVapNextElement1 visibale Timeout!").atMost(60, TimeUnit.SECONDS)
//                        .until(() -> driver.findElement(By.xpath("//div[@class='modal-scrollable']//a[contains(@id, 'confirmDeleteVapNext')]")).isDisplayed());
//                //loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElement1().click();
//
//                driver.findElement(By.xpath("//div[@class='modal-scrollable']//a[contains(@id, 'confirmDeleteVapNext')]")).click();
//            } catch (Exception e) {
//                System.out.println("Confirm Delete Vap Next Visibale!!!");
//            }

            boolean confirmDeleteVap = driver.findElements(By.xpath("//div[@class='modal-scrollable']//a[contains(@id, 'confirmDeleteVapNext')]")).size() != 0;

            if (confirmDeleteVap){

                await("Confirm Delete Vap Next Visibale!!!").atMost(15, TimeUnit.SECONDS)
                        .until(() -> loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElement1().isDisplayed());

                Utilities.captureScreenShot(driver);

                loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElement1().click();
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
            referencesPage.updateData(referenceDTO);
            referencesPage.getSaveBtnElement().click();

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);


            // ==========MISC FRM APP DTL=================
            stage = "MISC FRM APPDTL PAGE";
            CRM_MiscFrmAppDtlPage miscFrmAppDtlPage = new CRM_MiscFrmAppDtlPage(driver);
            miscFrmAppDtlPage.getTabMiscFrmAppDtlElementByName().click();
            miscFrmAppDtlPage.updateData(miscFrmAppDtlDTO);
            Utilities.captureScreenShot(driver);
            await("getBtnSaveElement end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFrmAppDtlPage._getBtnSaveElement().isEnabled());
            miscFrmAppDtlPage._getBtnSaveElement().click();

            Utilities.captureScreenShot(driver);

            miscFrmAppDtlPage.updateCommunicationValue(miscFrmAppDtlDTO.getRemark());

            with().pollInterval(org.awaitility.Duration.FIVE_SECONDS).
            await("Button Move To Next Stage Element end tab not enabled!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFrmAppDtlPage.getBtnMoveToNextStageElement().isEnabled());
            miscFrmAppDtlPage.getBtnMoveToNextStageElement().click();

            Utilities.captureScreenShot(driver);

            Thread.sleep(15000);

//            miscFrmAppDtlPage.keyActionMoveNextStage(); check again 14.4

//            actions.moveToElement(keyActionMoveNextStage.getBtnMoveToNextStageElement()).click().perform();
//            actions.click(miscFrmAppDtlPage.getBtnMoveToNextStageElement()).perform();

            Utilities.captureScreenShot(driver);

            System.out.println(stage + ": DONE");

            await("Work flow failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Grid"));

            Utilities.captureScreenShot(driver);

            stage = "COMPLETE";
            System.out.println("AUTO SALEQUEUE OK: user run auto: " + accountDTO.getUserName());

            System.out.println("AUTO SALEQUEUE - FINISH: " + stage + " - " + Duration.between(start, Instant.now()).toSeconds());
            Utilities.captureScreenShot(driver);

            responseModel.setProject(saleQueueDTO.getProject());
            responseModel.setReference_id(saleQueueDTO.getReference_id());
            responseModel.setTransaction_id(saleQueueDTO.getTransaction_id());
            responseModel.setApp_id(applicationId);
            responseModel.setAutomation_result("SALEQUEUE PASS" + " - Session ID: " + session + " - UserAuto: " + accountDTO.getUserName());

            Utilities.captureScreenShot(driver);

            Query queryUpdate = new Query();
            queryUpdate.addCriteria(Criteria.where("appId").is(applicationId).and("reference_id").is(saleQueueDTO.getReference_id()));
            Update update = new Update();
            update.set("automationAcc", accountDTO.getUserName());
            update.set("applicationId", applicationId);
            update.set("status", "SALEQUEUE PASS");
            update.set("description", "Success");
            mongoTemplate.findAndModify(queryUpdate, update, CRM_ExistingCustomerDTO.class);

        } catch (Exception e) {

            responseModel.setProject(saleQueueDTO.getProject());
            responseModel.setReference_id(saleQueueDTO.getReference_id());
            responseModel.setTransaction_id(saleQueueDTO.getTransaction_id());
            responseModel.setApp_id(applicationId);
            responseModel.setAutomation_result("SALEQUEUE FAILED" + " - Session ID: " + session + " - UserAuto: " + accountDTO.getUserName() + " - Error: " + e.getMessage());

            System.out.println("Auto Error: " + stage + "\n => MESSAGE " + e.getMessage() + " => TRACE: " + e.toString());
            e.printStackTrace();

            if (e.getMessage().contains("Work flow failed!!!")) {
                stage = "END OF LEAD DETAIL";
                saleQueueDTO.setStage(stage);
                await("Get error fail!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> driver.findElements(By.id("error-message")).size() > 0);

                int errorMessage = driver.findElements(By.xpath("//div[@id = 'rule-error-block-application-completion'][contains(@class, 'block-no')]")).size();
                if(errorMessage == 0) {
                    if (driver.findElements(By.id("error-message")) != null && driver.findElements(By.id("error-message")).size() > 0) {
                        String error = "Error: ";
                        for (WebElement we : driver.findElements(By.id("error-message"))) {
                            error += we.getText();
                        }
                        saleQueueDTO.setError(error);
                        responseModel.setAutomation_result("SALEQUEUE FAILED" + " - Session ID: " + session + " - UserAuto: " + accountDTO.getUserName() + " - Error: " + saleQueueDTO.getError());
                        System.out.println(stage + "=>" + error);
                    }
                }else{
                    saleQueueDTO.setError("Cannot click Move next stage");
                    responseModel.setAutomation_result("SALEQUEUE FAILED" + " - Session ID: " + session + " - UserAuto: " + accountDTO.getUserName() + " - Error: " + saleQueueDTO.getError());
                    System.out.println(stage + "=>" + saleQueueDTO.getError());
                }
            }

            Utilities.captureScreenShot(driver);

            Query queryUpdate = new Query();
            queryUpdate.addCriteria(Criteria.where("appId").is(applicationId).and("reference_id").is(saleQueueDTO.getReference_id()));
            Update update = new Update();
            update.set("automationAcc", accountDTO.getUserName());
            update.set("applicationId", applicationId);
            update.set("status", "SALEQUEUE FAILED");
            update.set("description", "Error: " + saleQueueDTO.getError());
            mongoTemplate.findAndModify(queryUpdate, update, CRM_ExistingCustomerDTO.class);

        } finally {
            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            System.out.println(responseModel.getAutomation_result() + " => Project: " + responseModel.getProject() + " => AppId: " + responseModel.getApp_id());
            logoutV2(driver, accountDTO);
            autoUpdateStatusRabbit(responseModel, "updateAutomation");
        }
    }

    private void updateQuickleadExistingCustomer(CRM_ExistingCustomerDTO existingCustomerDTO) throws Exception {
        mongoTemplate.save(existingCustomerDTO);

    }

    public void logoutV2(WebDriver driver, LoginDTO accountAuto) {
        try {
            System.out.println("Logout");
            LogoutPageV2 logoutPage = new LogoutPageV2(driver);
            logoutPage.logout();
            log.info("Logout: Done => " + accountAuto.getUserName());
        } catch (Exception e) {
            System.out.println("LOGOUT: =>" + accountAuto.getUserName() + " - " + e.toString());
        }
    }

    private void pushAccountToQueueV2(LoginDTO accountDTO, String project) throws InterruptedException {
        if (!Objects.isNull(accountDTO)) {
            Thread.sleep(60000);
            Query queryUpdate = new Query();
            queryUpdate.addCriteria(Criteria.where("username").is(accountDTO.getUserName()).and("project").is(project));
            Update update = new Update();
            update.set("active", 0);
            mongoTemplate.findAndModify(queryUpdate, update, AccountFinOneDTO.class);
            System.out.println("Update it:" + accountDTO.toString());
        }
    }

    //------------------------ END EXISTING CUSTOMER -----------------------------------------------------

    //endregion

    //region PROJECT: AUTO QUICKLEAD POOL
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
            mongoTemplate.save(application);


            //*************************** END GET DATA *********************//
            Actions actions = new Actions(driver);
            System.out.println(stage + ": DONE");

//            stage = "LOGIN FINONE";
//            HashMap<String, String> dataControl = new HashMap<>();
//            LoginPage loginPage = new LoginPage(driver);
//            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
//            loginPage.clickLogin();
//
//            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(driver::getTitle, is("DashBoard"));


            //***************************//LOGIN FINONE//***************************//
            stage = "LOGIN FINONE";

            LoginPageNeo loginPage = new LoginPageNeo(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword(),"CAS");
            loginPage.clickLogin();

            //***************************//END LOGIN//***************************//

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "HOME PAGE";
            HomePageNeo homePage = new HomePageNeo(driver);


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
            QuickLeadPageNeo quickLeadPage = new QuickLeadPageNeo(driver);
            quickLeadPage.setData(quickLead);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "LEADS GRID";
            // ========== LEAD PAGE =================
            LeadsPageNeo leadsPage = new LeadsPageNeo(driver);
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

//            leadDetailPage.setData(quickLead, leadApp, downdloadFileURL);

            boolean flagResult = leadDetailPage.setData(quickLead, leadApp, downdloadFileURL);

            if (flagResult == false) {
                application.setApplicationId("UNKNOW");
                application.setStatus("ERROR");
                application.setStage(stage);
                application.setDescription("File not enough!!!");
                return;
            }

            Utilities.captureScreenShot(driver);

            await("Lead Page timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Lead Grid"));

            String leadAppID = "";
            stage = "APPLICATION MANAGER";
            // ========== APPLICATION MANAGER =================
            leadAppID = this.assignManger2(driver, leadApp, "TPF DATA ENTRY", accountDTO.getUserName());

            //get appID moi o day
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
            logout(driver, accountDTO.getUserName());
        }
    }
    //endregion

    //region PROJECT: AUTO ALLOCATION
    public void runAutomation_autoAssignAllocation(Map<String, Object> mapValue, String project, String browser) throws Exception {
        String stage = "";
        try {
            stage = "INIT DATA";
            //*************************** GET DATA *********************//
            AutoAllocationDTO autoAllocationDTOList = (AutoAllocationDTO) mapValue.get("AutoAssignAllocationList");
            List<AutoAssignAllocationDTO> autoAssignAllocationDTOList = (List<AutoAssignAllocationDTO>) mapValue.get("AutoAssignAllocation");
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
        }
    }

    private void runAutomation_autoAssignAllocation_run(LoginDTO accountDTO, String browser, String project) {
        WebDriver driver = null;
        Instant start = Instant.now();
        String stage = "";
        String appID = "";
        String referenceId = "";
        String projectId = "";
        ResponseAutomationModel responseModel = new ResponseAutomationModel();
        System.out.println("START - Auto: " + accountDTO.getUserName() + " - Time: " + Duration.between(start, Instant.now()).toSeconds());
        try {
            SeleniumGridDriver setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
            driver = setupTestDriver.getDriver();
            SessionId session = ((RemoteWebDriver)driver).getSessionId();
            //get account run
            stage = "LOGIN FINONE";

            LoginPageNeo loginPage = new LoginPageNeo(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword(),"CAS");
            loginPage.clickLogin();

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));

            System.out.println("Auto: " + accountDTO.getUserName() + " - " + stage + ": LOGIN DONE" + " - Time: " + Duration.between(start, Instant.now()).toSeconds());
            Utilities.captureScreenShot(driver);
            SearchMenu goToMN = new SearchMenu(driver);
            goToMN.MoveToPage(Constant.MENU_NAME_LINK_APPLICATION_MANAGER);
            stage = "APPLICATION MANAGER";
            // ========== APPLICATION MANAGER =================
            await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Manager"));

            AutoAssignAllocationDTO autoAssignAllocationDTO = null;

            do {
                try {
                    System.out.println("Auto: " + accountDTO.getUserName() + " - AUTO BEGIN " + " - Time: " + Duration.between(start, Instant.now()).toSeconds());
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

                        System.out.println("Auto: " + accountDTO.getUserName() + " - App: " + autoAssignAllocationDTO.getAppId() + " - GET DATA DONE " + " - "  + " User: " + autoAssignAllocationDTO.getUserName() + " - Time: " + Duration.between(start, Instant.now()).toSeconds());

                        referenceId = resultUpdate.getReference_id();

                        projectId = resultUpdate.getProject();

                        appID = autoAssignAllocationDTO.getAppId();

                        AutoAssignAllocationPage autoAssignAllocationPage = new AutoAssignAllocationPage(driver);

                        await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(() -> autoAssignAllocationPage.getApplicationManagerFormElement().isDisplayed());

                        autoAssignAllocationPage.setData(appID, autoAssignAllocationDTO.getUserName().toLowerCase());

                        System.out.println("Auto: " + accountDTO.getUserName() + " - App: " + autoAssignAllocationDTO.getAppId() + " - APPLICATION MANAGER " + " - "  + "User: " + autoAssignAllocationDTO.getUserName() + " - Time: " + Duration.between(start, Instant.now()).toSeconds());

                        await("tdApplicationElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(() -> autoAssignAllocationPage.getTdApplicationElement().size() > 2);

                        await("showTaskElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(() -> autoAssignAllocationPage.getShowTaskElement().isDisplayed());

                        String userName = autoAssignAllocationDTO.getUserName().toLowerCase();

                        String userNameTable = autoAssignAllocationPage.getUserAssignManager().getAttribute("innerHTML").trim();

                        await("No User Found!!!").atMost(15, TimeUnit.SECONDS)
                                .until(() -> userName.equals(userNameTable));

                        autoAssignAllocationPage.getBackBtnElement().click();

                        await("Application Manager Back timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(driver::getTitle, is("Application Manager"));

                        System.out.println("Auto: " + accountDTO.getUserName() + " App: " + autoAssignAllocationDTO.getAppId() + " - AUTO FINISH " + " - " + "User: " + autoAssignAllocationDTO.getUserName() + " - Time: " + Duration.between(start, Instant.now()).toSeconds());

                        // ========= UPDATE DB ============================
                        Query queryUpdate1 = new Query();
                        queryUpdate1.addCriteria(Criteria.where("status").is(2).and("appId").is(autoAssignAllocationDTO.getAppId()).and("userName").is(autoAssignAllocationDTO.getUserName()));
                        Update update1 = new Update();
                        update1.set("userAuto", accountDTO.getUserName());
                        update1.set("status", 1);
                        update1.set("assignStatus", 1);
                        update1.set("automationResult", "AUTOASSIGN_PASS");
                        update1.set("automationResultMessage", "Session ID:" + session);
                        mongoTemplate.findAndModify(queryUpdate1, update1, AutoAssignAllocationDTO.class);

                        Query queryUpdatePass = new Query();
                        queryUpdatePass.addCriteria(Criteria.where("appId").is(appID).and("userName").is(autoAssignAllocationDTO.getUserName()).and("status").is(1).and("idAutoAllocation").is(autoAssignAllocationDTO.getIdAutoAllocation()));
                        List<AutoAllocationResponseDTO> resultRespone = mongoTemplate.find(queryUpdatePass, AutoAllocationResponseDTO.class);
                        responseModel.setReference_id(referenceId);
                        responseModel.setProject(projectId);
                        responseModel.setData(resultRespone);
                        autoUpdateStatusRabbitAllocation(responseModel, "updateStatusApp", accountDTO.getUserName(), appID);

                        System.out.println("Auto: " + accountDTO.getUserName() + " App: " + autoAssignAllocationDTO.getAppId() + " - UPDATE STATUS " + " - " + "User: " + autoAssignAllocationDTO.getUserName() + " - Time: " + Duration.between(start, Instant.now()).toSeconds());

                    }
                } catch (Exception ex) {
                    Query queryUpdate = new Query();
                    queryUpdate.addCriteria(Criteria.where("status").in(0, 2).and("appId").is(autoAssignAllocationDTO.getAppId()).and("userName").is(autoAssignAllocationDTO.getUserName()));
                    Update update = new Update();
                    update.set("userAuto", accountDTO.getUserName());
                    update.set("status", 3);
                    update.set("assignStatus", 1);
                    update.set("automationResult", "AUTOASSIGN_FAILED");
                    update.set("automationResultMessage", "Session ID:" + session + "- ERROR: " + ex.getMessage().substring(0, ex.getMessage().indexOf(" in")));
                    mongoTemplate.findAndModify(queryUpdate, update, AutoAssignAllocationDTO.class);

                    Query queryUpdateFail = new Query();
                    queryUpdateFail.addCriteria(Criteria.where("appId").is(appID).and("userName").is(autoAssignAllocationDTO.getUserName()).and("status").is(3).and("idAutoAllocation").is(autoAssignAllocationDTO.getIdAutoAllocation()));
                    List<AutoAllocationResponseDTO> resultRespone = mongoTemplate.find(queryUpdateFail, AutoAllocationResponseDTO.class);
                    responseModel.setReference_id(referenceId);
                    responseModel.setProject(projectId);
                    responseModel.setData(resultRespone);
                    autoUpdateStatusRabbitAllocation(responseModel, "updateStatusApp", accountDTO.getUserName(), appID);

                    WebElement buttonBackElement = driver.findElement(By.xpath("//*[contains(@class,'backSaveBtns')]//input[@type='button']"));

                    buttonBackElement.click();

                    await("Application Manager Back timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                            .until(driver::getTitle, is("Application Manager"));

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
            if (driver != null) {
                driver.close();
                driver.quit();
            }
        }
    }

    private void autoUpdateStatusRabbitAllocation(ResponseAutomationModel responseAutomationModel, String func, String userAuto, String appID) throws Exception {
        JsonNode jsonNode = rabbitMQService.sendAndReceive(rabbitIdRes,
                    Map.of("func", func,
                            "body", Map.of(
                                    "project", responseAutomationModel.getProject(),
                                    "reference_id", responseAutomationModel.getReference_id(),
                                    "autoAssign",responseAutomationModel.getData()
                            )));

        boolean checkJsonNode = jsonNode.isNull();
        Query queryUpdateStatusRabbit = new Query();
        Update updateStatusRabbit = new Update();
        if (!checkJsonNode){
            System.out.println("rabit:=>" + jsonNode.toString());
            queryUpdateStatusRabbit.addCriteria(Criteria.where("status").in(1, 3).and("appId").is(appID).and("userAuto").is(userAuto));
            updateStatusRabbit.set("automationResultRabbit", "rabit: => PASS");
            mongoTemplate.findAndModify(queryUpdateStatusRabbit, updateStatusRabbit, AutoAssignAllocationDTO.class);
        }else{
            queryUpdateStatusRabbit.addCriteria(Criteria.where("status").in(1, 3).and("appId").is(appID).and("userAuto").is(userAuto));
            updateStatusRabbit.set("automationResultRabbit", "rabit: => FAIL");
            mongoTemplate.findAndModify(queryUpdateStatusRabbit, updateStatusRabbit, AutoAssignAllocationDTO.class);
        }

//        JsonNode jsonNode = null;
//        Query queryUpdateStatusRabbit = new Query();
//        Update updateStatusRabbit = new Update();
//        try {
//            jsonNode = rabbitMQService.sendAndReceive(rabbitIdRes,
//                    Map.of("func", func,
//                            "body", Map.of(
//                                    "project", responseAutomationModel.getProject(),
//                                    "reference_id", responseAutomationModel.getReference_id(),
//                                    "autoAssign",responseAutomationModel.getData()
//                            )));
//            System.out.println("rabit:=>" + jsonNode.toString());
//            queryUpdateStatusRabbit.addCriteria(Criteria.where("status").in(1, 3).and("appId").is(appID).and("userAuto").is(userAuto));
//            updateStatusRabbit.set("automationResultRabbit", "rabit: => PASS");
//            mongoTemplate.findAndModify(queryUpdateStatusRabbit, updateStatusRabbit, AutoAssignAllocationDTO.class);
//        } catch (Exception e) {
//            queryUpdateStatusRabbit.addCriteria(Criteria.where("status").in(1, 3).and("appId").is(appID).and("userAuto").is(userAuto));
//            updateStatusRabbit.set("automationResultRabbit", "rabit: => FAIL");
//            mongoTemplate.findAndModify(queryUpdateStatusRabbit, updateStatusRabbit, AutoAssignAllocationDTO.class);
//        }
    }

    //endregion

    //region PROJECT: AUTO QUICKLEAD MOBILITY VENDOR
    public void MOBILITY_runAutomation_QuickLead_Vendor(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO) throws Exception {
        Instant start = Instant.now();
        String appId = "";
        String stage = "";
        Application application = Application.builder().build();
        SessionId session = ((RemoteWebDriver) driver).getSessionId();
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

            //***************************//LOGIN FINONE//***************************//
            stage = "LOGIN FINONE";

            LoginPageNeo loginPage = new LoginPageNeo(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword(), "CAS");
            loginPage.clickLogin();

            //***************************//END LOGIN//***************************//

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            stage = "HOME PAGE";
            HomePageNeo homePage = new HomePageNeo(driver);


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
            QuickLeadPageNeo quickLeadPage = new QuickLeadPageNeo(driver);
            quickLeadPage.setData(quickLead);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "LEADS GRID";
            // ========== LEAD PAGE =================
            LeadsPageNeo leadsPage = new LeadsPageNeo(driver);
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
//            leadsPage.setData(leadApp);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            Thread.sleep(10000);
            this.getApplicationLead(driver, leadApp);

            stage = "LEAD STAGE";
            // ========== LEAD STAGE =================
            LeadDetailPage leadDetailPage = new LeadDetailPage(driver);
            await("contentElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadDetailPage.getContentElement().isDisplayed());

            boolean flagResult = leadDetailPage.setData(quickLead, leadApp, downdloadFileURL);

            if (flagResult == false) {
                application.setApplicationId("UNKNOW");
                application.setStatus("ERROR");
                application.setStage(stage);
                application.setDescription("File not enough!!!");
                return;
            }

            Utilities.captureScreenShot(driver);


            String leadAppID = "";
            //update thêm phần assign về acc tạo app để tranh rơi vào pool
            stage = "APPLICATION MANAGER";
            // ========== APPLICATION MANAGER =================
            //get appID moi o day
            leadAppID = this.assignManger2(driver, leadApp, "TPF DATA ENTRY", accountDTO.getUserName());
            System.out.println(" APP: =>" + leadAppID);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            //-------------------- END ---------------------------

            application.setApplicationId(leadAppID);
            application.setLeadApp(leadApp);

            //UPDATE STATUS
            application.setStatus("QUICKLEAD PASS");
            application.setDescription("Thanh cong" + " - " + session);


            Utilities.captureScreenShot(driver);
            //logout(driver);

        } catch (Exception e) {
            //UPDATE STATUS
            application.setStatus("QUICKLEAD FAIL");
            application.setStage(stage);
            application.setDescription(e.getMessage() + " - " + session);

            System.out.println(stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);
        } finally {
            if (application.getApplicationId() == null || application.getApplicationId().isEmpty() || application.getApplicationId().indexOf("LEAD") > 0 || application.getApplicationId().indexOf("APPL") < 0) {
                application.setApplicationId("UNKNOW");
                application.setStatus("QUICKLEAD_FAILED");
                if (!"File not enough!!!".equals(application.getDescription())) {
                    application.setDescription("Khong thanh cong" + " - Session: " + session);
                }else{
                    application.setDescription(application.getDescription() + " - Session: " + session);
                }
            }

            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            try {
                MOBILITY_updateStatusRabbit(application, "updateAutomation", "mobility");
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            MOBILITY_updateDB(application);
            logout(driver, accountDTO.getUserName());

        }
    }

    //endregion

    //*****************NEW AUTOMATION

    //region LOGIN
    private LoginDTO getAccountFromMongoDB(Queue<LoginDTO> accounts, String project, String funcAutomation) throws Exception {
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
                update.set("funcAutomation", funcAutomation);
                AccountFinOneDTO resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, AccountFinOneDTO.class);

                if (resultUpdate == null) {
                    Thread.sleep(Constant.WAIT_ACCOUNT_GET_NULL);
                    accountDTO = null;
                } else {
                    System.out.println("Get it:" + accountDTO.getUserName());
                    System.out.println("Exist:" + accounts.size());
                }
            } else
                Thread.sleep(Constant.WAIT_ACCOUNT_TIMEOUT);
        }

        return accountDTO;
    }
    //endregion

    //region LOGOUT
    public void logoutFinOne(WebDriver driver, LoginDTO accountAuto) {
        try {
            System.out.println("LOGOUT ACCOUNT AUTOMATION: " + accountAuto.getUserName());
            LogoutPageV2 logoutPage = new LogoutPageV2(driver);
            logoutPage.logout();
            log.info("LOGOUT ACCOUNT AUTOMATION: DONE => " + accountAuto.getUserName());
        } catch (Exception e) {
            System.out.println("LOGOUT ACCOUNT AUTOMATION: =>" + accountAuto.getUserName() + " - " + e.toString());
        }
    }
    //endregion

    //region UPDATE STATUS ACCOUNT LOGOUT
    private void updateAccountFromMongoDB(LoginDTO accountDTO, String project) {
        if (!Objects.isNull(accountDTO)) {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Query queryUpdate = new Query();
            queryUpdate.addCriteria(Criteria.where("username").is(accountDTO.getUserName()).and("project").is(project).and("active").is(1));
            Update update = new Update();
            update.set("active", 0);
            update.set("funcAutomation", "LOGOUT");
            mongoTemplate.findAndModify(queryUpdate, update, AccountFinOneDTO.class);
            System.out.println("Update Account: " + accountDTO.getUserName() + " - DONE");
        }
    }
    //endregion

    //region RESPONSE UPDATE AUTOMATION

    private void responseUpdateAutomation(ResponseAutomationModel responseAutomationModel, String func) throws Exception {
        JsonNode jsonNode = rabbitMQService.sendAndReceive(rabbitIdRes,
                Map.of("func", func,
                        "body", Map.of("project", responseAutomationModel.getProject(),
                                "transaction_id", responseAutomationModel.getTransaction_id(),
                                "app_id", responseAutomationModel.getApp_id(),
                                "automation_result", responseAutomationModel.getAutomation_result(),
                                "reference_id", responseAutomationModel.getReference_id()
                        )));
        System.out.println("rabit:=>" + jsonNode.toString());
    }

    private void responseUpdateAutomation(QuickLeadDTO quickLeadDTO, String func) throws Exception {

        JsonNode jsonNode = rabbitMQService.sendAndReceive(rabbitIdRes,
                Map.of("func", func, "reference_id", quickLeadDTO.getReference_id(), "body", Map.of("app_id", quickLeadDTO.getApplicationId() != null ? quickLeadDTO.getApplicationId() : "",
                        "project", quickLeadDTO.getProject(),
                        "automation_result", quickLeadDTO.getStatus(),
                        "description", quickLeadDTO.getDescription() != null ? quickLeadDTO.getDescription() : "",
                        "transaction_id", quickLeadDTO.getQuickLeadId(),
                        "automation_account", quickLeadDTO.getAutomationAcc())));
        System.out.println("rabit:=>" + jsonNode.toString());

    }

    //endregion

    //region QUICKLEAD ASSIGN POOL
    public void runAutomation_QuickLeadApplication(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO) throws Exception {
        Instant start = Instant.now();
        String stage = "";
        String leadAppID = "";
        String automationMonitorId = "";
        QuickLeadDTO quickLeadDTO = QuickLeadDTO.builder().build();
        AutomationMonitorDTO automationMonitorDTO = AutomationMonitorDTO.builder().build();
        SessionId session = ((RemoteWebDriver) driver).getSessionId();
        log.info("{}", quickLeadDTO);
        try {
            stage = "INIT DATA";
            //*************************** GET DATA *********************//
            quickLeadDTO = (QuickLeadDTO) mapValue.get("QuickLeadDTOList");
            quickLeadDTO.setAutomationAcc(accountDTO.getUserName());
            QuickLeadDetails quickLead = quickLeadDTO.getQuickLead();

            //*************************** END GET DATA *********************//

            Query querAutoMonitor = new Query();
            querAutoMonitor.addCriteria(Criteria.where("quickLeadId").is(quickLeadDTO.getQuickLeadId()).and("flagStatus").is(0).and("project").is(quickLeadDTO.getProject()).and("funcAutomation").is("quickLeadApplication"));
            Update updateAutoMonitor = new Update();
            updateAutoMonitor.set("accountAuto", accountDTO.getUserName());
            updateAutoMonitor.set("flagStatus", 2);
            automationMonitorDTO = mongoTemplate.findAndModify(querAutoMonitor, updateAutoMonitor, AutomationMonitorDTO.class);
            automationMonitorId = automationMonitorDTO.getId();

            System.out.println(stage + ": DONE");

            //***************************//LOGIN FINONE//***************************//
            stage = "LOGIN FINONE";

            LoginV2Page loginPage = new LoginV2Page(driver);
            loginPage.loginValue(accountDTO);

            //***************************//END LOGIN//***************************//

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
            QuickLeadEntryPage quickLeadEntryPage = new QuickLeadEntryPage(driver);
            quickLeadEntryPage.setData(quickLead);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "LEADS GRID";
            // ========== LEAD PAGE =================
            LeadGridPage leadGridPage = new LeadGridPage(driver);
            await("Quick Lead Entry timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Lead Grid"));

            await("notifyTextElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadGridPage.getNotifyTextElement().isEnabled() && leadGridPage.getNotifyTextElement().isDisplayed());

            String notify = leadGridPage.getNotifyTextElement().getText();
            String leadApp = "";

            if (notify.contains("LEAD")) {
                leadApp = notify.substring(notify.indexOf("LEAD"), notify.length());
            }

            System.out.println("LEAD APP: => " + leadApp);
            leadGridPage.setData(leadApp);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "LEAD STAGE";
            // ========== LEAD STAGE =================
            LeadPage leadPage = new LeadPage(driver);
            await("contentElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadPage.getContentElement().isDisplayed());

            boolean flagResult = leadPage.setData(quickLead, leadApp, downdloadFileURL);

            if (flagResult == false) {
                quickLeadDTO.setApplicationId("UNKNOW");
                quickLeadDTO.setStatus("ERROR");
                quickLeadDTO.setStage(stage);
                quickLeadDTO.setDescription("File not enough!!!");
                return;
            }

            Utilities.captureScreenShot(driver);

            await("Lead Page timeout").atMost(Constant.TIME_OUT_5_M, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Lead Grid"));

            leadGridPage.getSpanAllNotifyElement().click();
            await("getDivAllNotifyElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadGridPage.getDivAllNotifyElement().isEnabled() && leadGridPage.getDivAllNotifyElement().isDisplayed());

            await("getBtnAllNotifyElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadGridPage.getBtnAllNotifyElement().isEnabled() && leadGridPage.getBtnAllNotifyElement().isDisplayed());
            leadGridPage.getBtnAllNotifyElement().click();

            Utilities.captureScreenShot(driver);
            await("getBtnAllNotifyElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadGridPage.getNotifyTextSuccessElement().size() > 0);

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
            System.out.println("APP: => " + leadAppID);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            //-------------------- END ---------------------------

            quickLeadDTO.setApplicationId(leadAppID);
            quickLeadDTO.setLeadApp(leadApp);

            //UPDATE STATUS
            quickLeadDTO.setStatus("QUICKLEAD PASS");
            quickLeadDTO.setDescription("Thanh cong" + " - Session: " + session);

            //region //*****Update Automation Monitor*****//
            querAutoMonitor = new Query();
            querAutoMonitor.addCriteria(Criteria.where("_id").is(new ObjectId(automationMonitorId)).and("quickLeadId").is(quickLeadDTO.getQuickLeadId()).and("flagStatus").is(2).and("project").is(quickLeadDTO.getProject()).and("funcAutomation").is("quickLeadApplication"));
            updateAutoMonitor = new Update();
            updateAutoMonitor.set("accountAuto", accountDTO.getUserName());
            updateAutoMonitor.set("applicationId", quickLeadDTO.getApplicationId());
            updateAutoMonitor.set("description", "QUICKLEAD_PASS");
            updateAutoMonitor.set("flagStatus", 1);
            updateAutoMonitor.set("status", "COMPLETED");
            mongoTemplate.findAndModify(querAutoMonitor, updateAutoMonitor, AutomationMonitorDTO.class);
            //endregion

            Utilities.captureScreenShot(driver);

        } catch (Exception e) {
            //UPDATE STATUS
            quickLeadDTO.setStatus("QUICKLEAD_FAILED");
            quickLeadDTO.setStage(stage);
            quickLeadDTO.setDescription(e.getMessage());

            //region //*****Update Automation Monitor*****//
            Query querAutoMonitor = new Query();
            querAutoMonitor.addCriteria(Criteria.where("_id").is(new ObjectId(automationMonitorId)).and("quickLeadId").is(quickLeadDTO.getQuickLeadId()).and("flagStatus").is(2).and("project").is(quickLeadDTO.getProject()).and("funcAutomation").is("quickLeadApplication"));
            Update updateAutoMonitor = new Update();
            updateAutoMonitor.set("accountAuto", accountDTO.getUserName());
            updateAutoMonitor.set("applicationId", quickLeadDTO.getApplicationId());
            updateAutoMonitor.set("description", "QUICKLEAD_FAIL - Error: " + e.getMessage() + " - Session: " + session);
            updateAutoMonitor.set("flagStatus", 3);
            updateAutoMonitor.set("flagRetry", 1);
            updateAutoMonitor.set("status", "AUTO_QUICKLEAD_FAIL");
            mongoTemplate.findAndModify(querAutoMonitor, updateAutoMonitor, AutomationMonitorDTO.class);
            //endregion

            System.out.println(stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);

        } finally {
            if (quickLeadDTO.getApplicationId() == null || quickLeadDTO.getApplicationId().isEmpty() || quickLeadDTO.getApplicationId().indexOf("LEAD") > 0 || quickLeadDTO.getApplicationId().indexOf("APPL") < 0) {
                quickLeadDTO.setApplicationId("UNKNOWN");
                quickLeadDTO.setStatus("QUICKLEAD_FAILED");
                if (!"File not enough!!!".equals(quickLeadDTO.getDescription())) {
                    quickLeadDTO.setDescription("Khong thanh cong" + " - Session: " + session + " - LEADID" + leadAppID);
                }else{
                    quickLeadDTO.setDescription(quickLeadDTO.getDescription() + " - Session: " + session);
                }
            }

            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            try {
                responseUpdateAutomation(quickLeadDTO, "updateAutomation");
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            updateSaveDB(quickLeadDTO);
            logoutFinOne(driver, accountDTO);
            updateAccountFromMongoDB(accountDTO, quickLeadDTO.getProject().toUpperCase());
        }
    }
    //endregion

    //region QUICKLEAD ASSIGN VENDOR
    public void runAutomation_QuickLeadApplication_Vendor(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO) throws Exception {
        Instant start = Instant.now();
        String stage = "";
        String leadAppID = "";
        String automationMonitorId = "";
        QuickLeadDTO quickLeadDTO = QuickLeadDTO.builder().build();
        AutomationMonitorDTO automationMonitorDTO = AutomationMonitorDTO.builder().build();
        SessionId session = ((RemoteWebDriver) driver).getSessionId();
        log.info("{}", quickLeadDTO);
        try {
            stage = "INIT DATA";
            //*************************** GET DATA *********************//
            quickLeadDTO = (QuickLeadDTO) mapValue.get("QuickLeadDTOList");
            quickLeadDTO.setAutomationAcc(accountDTO.getUserName());
            QuickLeadDetails quickLead = quickLeadDTO.getQuickLead();

            //*************************** END GET DATA *********************//

            Query querAutoMonitor = new Query();
            querAutoMonitor.addCriteria(Criteria.where("quickLeadId").is(quickLeadDTO.getQuickLeadId()).and("flagStatus").is(0).and("project").is(quickLeadDTO.getProject()).and("funcAutomation").is("quickLeadApplicationVendor"));
            Update updateAutoMonitor = new Update();
            updateAutoMonitor.set("accountAuto", accountDTO.getUserName());
            updateAutoMonitor.set("flagStatus", 2);
            automationMonitorDTO = mongoTemplate.findAndModify(querAutoMonitor, updateAutoMonitor, AutomationMonitorDTO.class);
            automationMonitorId = automationMonitorDTO.getId();

            System.out.println(stage + ": DONE");

            //***************************//LOGIN FINONE//***************************//
            stage = "LOGIN FINONE";

            LoginV2Page loginPage = new LoginV2Page(driver);
            loginPage.loginValue(accountDTO);

            //***************************//END LOGIN//***************************//

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
            QuickLeadEntryPage quickLeadEntryPage = new QuickLeadEntryPage(driver);
            quickLeadEntryPage.setData(quickLead);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "LEADS GRID";
            // ========== LEAD PAGE =================
            LeadGridPage leadGridPage = new LeadGridPage(driver);
            await("Quick Lead Entry timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Lead Grid"));

            await("notifyTextElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadGridPage.getNotifyTextElement().isEnabled() && leadGridPage.getNotifyTextElement().isDisplayed());

            String notify = leadGridPage.getNotifyTextElement().getText();
            String leadApp = "";

            if (notify.contains("LEAD")) {
                leadApp = notify.substring(notify.indexOf("LEAD"), notify.length());
            }

            System.out.println("LEAD APP: => " + leadApp);
            leadGridPage.setData(leadApp);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "LEAD STAGE";
            // ========== LEAD STAGE =================
            LeadPage leadPage = new LeadPage(driver);
            await("contentElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadPage.getContentElement().isDisplayed());

            boolean flagResult = leadPage.setData(quickLead, leadApp, downdloadFileURL);

            if (flagResult == false) {
                quickLeadDTO.setApplicationId("UNKNOW");
                quickLeadDTO.setStatus("ERROR");
                quickLeadDTO.setStage(stage);
                quickLeadDTO.setDescription("File not enough!!!");
                return;
            }

            Utilities.captureScreenShot(driver);

            await("Lead Page timeout").atMost(Constant.TIME_OUT_5_M, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Lead Grid"));

            leadGridPage.getSpanAllNotifyElement().click();
            await("getDivAllNotifyElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadGridPage.getDivAllNotifyElement().isEnabled() && leadGridPage.getDivAllNotifyElement().isDisplayed());

            await("getBtnAllNotifyElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadGridPage.getBtnAllNotifyElement().isEnabled() && leadGridPage.getBtnAllNotifyElement().isDisplayed());
            leadGridPage.getBtnAllNotifyElement().click();

            Utilities.captureScreenShot(driver);
            await("getBtnAllNotifyElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadGridPage.getNotifyTextSuccessElement().size() > 0);

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
            System.out.println("APP: => " + leadAppID);

            //******update thêm phần assign về acc tạo app để tranh rơi vào pool******//
            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> de_applicationManagerPage.getBackBtnElement().isDisplayed());

            de_applicationManagerPage.getBackBtnElement().click();

            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> de_applicationManagerPage.getApplicationManagerFormElement().isDisplayed());

            de_applicationManagerPage.setData(leadAppID, accountDTO.getUserName());
            //******END******//

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);

            //-------------------- END ---------------------------

            quickLeadDTO.setApplicationId(leadAppID);
            quickLeadDTO.setLeadApp(leadApp);

            //UPDATE STATUS
            quickLeadDTO.setStatus("QUICKLEAD PASS");
            quickLeadDTO.setDescription("Thanh cong" + " - Session: " + session);

            //region //*****Update Automation Monitor*****//
            querAutoMonitor = new Query();
            querAutoMonitor.addCriteria(Criteria.where("_id").is(new ObjectId(automationMonitorId)).and("quickLeadId").is(quickLeadDTO.getQuickLeadId()).and("flagStatus").is(2).and("project").is(quickLeadDTO.getProject()).and("funcAutomation").is("quickLeadApplicationVendor"));
            updateAutoMonitor = new Update();
            updateAutoMonitor.set("accountAuto", accountDTO.getUserName());
            updateAutoMonitor.set("applicationId", quickLeadDTO.getApplicationId());
            updateAutoMonitor.set("description", "QUICKLEAD_PASS");
            updateAutoMonitor.set("flagStatus", 1);
            updateAutoMonitor.set("status", "COMPLETED");
            mongoTemplate.findAndModify(querAutoMonitor, updateAutoMonitor, AutomationMonitorDTO.class);
            //endregion

            Utilities.captureScreenShot(driver);

        } catch (Exception e) {
            //UPDATE STATUS
            quickLeadDTO.setStatus("QUICKLEAD_FAILED");
            quickLeadDTO.setStage(stage);
            quickLeadDTO.setDescription(e.getMessage());

            //region //*****Update Automation Monitor*****//
            Query querAutoMonitor = new Query();
            querAutoMonitor.addCriteria(Criteria.where("_id").is(new ObjectId(automationMonitorId)).and("quickLeadId").is(quickLeadDTO.getQuickLeadId()).and("flagStatus").is(2).and("project").is(quickLeadDTO.getProject()).and("funcAutomation").is("quickLeadApplicationVendor"));
            Update updateAutoMonitor = new Update();
            updateAutoMonitor.set("accountAuto", accountDTO.getUserName());
            updateAutoMonitor.set("applicationId", quickLeadDTO.getApplicationId());
            updateAutoMonitor.set("description", "QUICKLEAD_FAIL - Error: " + e.getMessage() + " - Session: " + session);
            updateAutoMonitor.set("flagStatus", 3);
            updateAutoMonitor.set("flagRetry", 1);
            updateAutoMonitor.set("status", "AUTO_QUICKLEAD_FAIL");
            mongoTemplate.findAndModify(querAutoMonitor, updateAutoMonitor, AutomationMonitorDTO.class);
            //endregion

            System.out.println(stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);

        } finally {
            if (quickLeadDTO.getApplicationId() == null || quickLeadDTO.getApplicationId().isEmpty() || quickLeadDTO.getApplicationId().indexOf("LEAD") > 0 || quickLeadDTO.getApplicationId().indexOf("APPL") < 0) {
                quickLeadDTO.setApplicationId("UNKNOWN");
                quickLeadDTO.setStatus("QUICKLEAD_FAILED");
                if (!"File not enough!!!".equals(quickLeadDTO.getDescription())) {
                    quickLeadDTO.setDescription("Khong thanh cong" + " - Session: " + session + " - LEADID" + leadAppID);
                }else{
                    quickLeadDTO.setDescription(quickLeadDTO.getDescription() + " - Session: " + session);
                }
            }

            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            try {
                responseUpdateAutomation(quickLeadDTO, "updateAutomation");
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            updateSaveDB(quickLeadDTO);
            logoutFinOne(driver, accountDTO);
            updateAccountFromMongoDB(accountDTO, quickLeadDTO.getProject().toUpperCase());
        }
    }
    //endregion

    //region UPDATE SAVE MONGODB
    private void updateSaveDB(QuickLeadDTO quickLeadDTO) throws Exception {
        mongoTemplate.save(quickLeadDTO);
    }
    //endregion

    //region RESPONSE QUERY
    private LoginDTO getAccountResponseQuery(Queue<LoginDTO> accounts, String project, Map<String, Object> mapValue, String funcAutomation) throws Exception {
        LoginDTO accountDTO = null;
        while (Objects.isNull(accountDTO)) {
            System.out.println("Wait to get account...");
            ResponseQueryDTO responseQueryDTO = (ResponseQueryDTO) mapValue.get("ResponseQueryList");
            Query query = new Query();
            query.addCriteria(Criteria.where("applicationId").is(responseQueryDTO.getApplicationId()));
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
                update.set("funcAutomation", funcAutomation);
                AccountFinOneDTO resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, AccountFinOneDTO.class);

                if (resultUpdate == null) {
                    Thread.sleep(Constant.WAIT_ACCOUNT_GET_NULL);
                    accountDTO = null;
                } else {
                    System.out.println("Get it:" + accountDTO.getUserName());
                    System.out.println("Exist:" + accounts.size());
                }
            } else
                Thread.sleep(Constant.WAIT_ACCOUNT_TIMEOUT);
        }

        return accountDTO;
    }

    public void runAutomation_ResponseQuery(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO) throws Exception {
        ResponseAutomationModel responseModel = new ResponseAutomationModel();
        Instant start = Instant.now();
        String stage = "";
        String idMongoDb = "";
        String automationMonitorId = "";
        SessionId session = ((RemoteWebDriver)driver).getSessionId();
        ResponseQueryDTO responseQueryDTO = ResponseQueryDTO.builder().build();
        AutomationMonitorDTO automationMonitorDTO = AutomationMonitorDTO.builder().build();
        log.info("{}", responseQueryDTO);
        try {
            stage = "INIT DATA";
            //*************************** INSERT DATA *********************//
            responseQueryDTO = (ResponseQueryDTO) mapValue.get("ResponseQueryList");
            System.out.println(stage + ": DONE");
            ResponseQueryDTO idMongoDB = mongoTemplate.insert(responseQueryDTO);
            idMongoDb = idMongoDB.getId();
            //*************************** INSERT DATA - DONE *********************//

            stage = "LOGIN FINONE";
            //***************************//LOGIN PAGE//***************************//
            LoginPageNeo loginPage = new LoginPageNeo(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword(),"CAS");
            loginPage.clickLogin();
            System.out.println("Func: responseQuery" + " - AccountAuto: " + accountDTO.getUserName() + " - " + stage + " DONE" + " - AppId: " + responseQueryDTO.getApplicationId());
            //***************************//END LOGIN//***************************//

            //*************************** UPDATE STATUS DATA *********************//
            Query querAutoMonitor = new Query();
            querAutoMonitor.addCriteria(Criteria.where("applicationId").is(responseQueryDTO.getApplicationId()).and("flagStatus").is(0).and("project").is(responseQueryDTO.getProject()).and("funcAutomation").is("responseQuery"));
            Update updateAutoMonitor = new Update();
            updateAutoMonitor.set("accountAuto", accountDTO.getUserName());
            updateAutoMonitor.set("flagStatus", 2);
            automationMonitorDTO = mongoTemplate.findAndModify(querAutoMonitor, updateAutoMonitor, AutomationMonitorDTO.class);
            automationMonitorId = automationMonitorDTO.getId();

            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(new ObjectId(idMongoDb)).and("status").is(0).and("appId").is(responseQueryDTO.getApplicationId()).and("project").is(responseQueryDTO.getProject()));
            Update update = new Update();
            update.set("userAuto", accountDTO.getUserName());
            update.set("status", 2);
            mongoTemplate.findAndModify(query, update, ResponseQueryDTO.class);
            //*************************** END UPDATE STATUS DATA *********************//

            stage = "RESPONSE QUERY";
            ResponseQueryPage returnRaiseQueryPage = new ResponseQueryPage(driver);
            SearchMenu searchMenu = new SearchMenu(driver);
            searchMenu.MoveToPage(Constant.MENU_NAME_LINK_RESPONSE_QUERY);

            await("Response Query Page loading timeout!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Response Query"));

            returnRaiseQueryPage.setData(responseQueryDTO, downdloadFileURL);

            System.out.println("Func: responseQuery" + " - AccountAuto: " + accountDTO.getUserName() + " - " + stage + "DONE" + " - AppId: " + responseQueryDTO.getApplicationId());

            //***************************//UPDATE DB//***************************//
            stage = "UPDATE MONGODB";
            Query queryPass = new Query();
            queryPass.addCriteria(Criteria.where("_id").is(new ObjectId(idMongoDb)).and("status").is(2).and("appId").is(responseQueryDTO.getApplicationId()));
            Update updatePass = new Update();
            updatePass.set("userAuto", accountDTO.getUserName());
            updatePass.set("status", 1);
            mongoTemplate.findAndModify(queryPass, updatePass, ResponseQueryDTO.class);
            System.out.println("Func try: responseQuery" + " - AccountAuto: " + accountDTO.getUserName() + " - " + stage + "DONE" + " - AppId: " + responseQueryDTO.getApplicationId());
            //***************************//END UPDATE DB//***************************//

            responseModel.setProject(responseQueryDTO.getProject());
            responseModel.setReference_id(responseQueryDTO.getReferenceId());
            responseModel.setTransaction_id(responseQueryDTO.getTransactionId());
            responseModel.setApp_id(responseQueryDTO.getApplicationId());
            responseModel.setAutomation_result("RESPONSEQUERY PASS");

            Utilities.captureScreenShot(driver);

            //region //*****Update Automation Monitor*****//
            querAutoMonitor = new Query();
            querAutoMonitor.addCriteria(Criteria.where("_id").is(new ObjectId(automationMonitorId)).and("applicationId").is(responseQueryDTO.getApplicationId()).and("flagStatus").is(2).and("project").is(responseQueryDTO.getProject()).and("funcAutomation").is("responseQuery"));
            updateAutoMonitor = new Update();
            updateAutoMonitor.set("accountAuto", accountDTO.getUserName());
            updateAutoMonitor.set("description", "RESPONSEQUERY_PASS");
            updateAutoMonitor.set("flagStatus", 1);
            updateAutoMonitor.set("status", "COMPLETED");
            mongoTemplate.findAndModify(querAutoMonitor, updateAutoMonitor, AutomationMonitorDTO.class);
            //endregion

        } catch (Exception e) {
            //***************************//UPDATE DB//***************************//
            stage = "UPDATE MONGODB";
            Query queryFaild = new Query();
            queryFaild.addCriteria(Criteria.where("_id").is(new ObjectId(idMongoDb)).and("status").in(0,2).and("appId").is(responseQueryDTO.getApplicationId()));
            Update updateFaild = new Update();
            updateFaild.set("userAuto", accountDTO.getUserName());
            updateFaild.set("status", 3);
            mongoTemplate.findAndModify(queryFaild, updateFaild, ResponseQueryDTO.class);
            System.out.println("Func catch: responseQuery" + " - AccountAuto: " + accountDTO.getUserName() + " - " + stage + "DONE" + " - AppId: " + responseQueryDTO.getApplicationId());
            //***************************//END UPDATE DB//***************************//

            //region //*****Update Automation Monitor*****//
            Query querAutoMonitor = new Query();
            querAutoMonitor.addCriteria(Criteria.where("_id").is(new ObjectId(automationMonitorId)).and("applicationId").is(responseQueryDTO.getApplicationId()).and("flagStatus").is(2).and("project").is(responseQueryDTO.getProject()).and("funcAutomation").is("responseQuery"));
            Update updateAutoMonitor = new Update();
            updateAutoMonitor.set("accountAuto", accountDTO.getUserName());
            updateAutoMonitor.set("description", "RESPONSEQUERY_FAIL - Error: " + e.getMessage() + " - Session: " + session);
            updateAutoMonitor.set("flagStatus", 3);
            updateAutoMonitor.set("flagRetry", 1);
            updateAutoMonitor.set("status", "AUTO_RESPONSEQUERY_FAIL");
            mongoTemplate.findAndModify(querAutoMonitor, updateAutoMonitor, AutomationMonitorDTO.class);
            //endregion

            responseModel.setProject(responseQueryDTO.getProject());
            responseModel.setReference_id(responseQueryDTO.getReferenceId());
            responseModel.setTransaction_id(responseQueryDTO.getTransactionId());
            responseModel.setApp_id(responseQueryDTO.getApplicationId());
            responseModel.setAutomation_result("RESPONSEQUERY FAILED" + " - " + e.getMessage() + " - Session: " + session);
            e.printStackTrace();

            Utilities.captureScreenShot(driver);
        } finally {
            logoutFinOne(driver, accountDTO);
            updateAccountFromMongoDB(accountDTO, responseQueryDTO.getProject().toUpperCase());
            responseUpdateAutomation(responseModel, "updateAutomation");


            logout(driver, accountDTO.getUserName());
            autoUpdateStatusRabbit(responseModel, "updateAutomation");
            System.out.println("Func finally: responseQuery" + " - AppId: " + responseModel.getApp_id() + " - DONE: " + responseModel.getAutomation_result() + "- Project " + responseModel.getProject());
        }
    }
    //endregion

    //******region SALE QUEUE
    public void runAutomation_SaleQueue(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO) throws Exception {
        ResponseAutomationModel responseModel = new ResponseAutomationModel();
        Instant start = Instant.now();
        String stage = "";
        String idMongoDb = "";
        String automationMonitorId = "";
        SessionId session = ((RemoteWebDriver)driver).getSessionId();
        SaleQueueDTO saleQueueDTO = SaleQueueDTO.builder().build();
        AutomationMonitorDTO automationMonitorDTO = AutomationMonitorDTO.builder().build();
        try {
            stage = "INIT DATA";
            //*************************** INSERT DATA *********************//
            saleQueueDTO = (SaleQueueDTO) mapValue.get("SaleQueueList");
            System.out.println(stage + ": DONE");
            SaleQueueDTO idMongoDB = mongoTemplate.insert(saleQueueDTO);
            idMongoDb = idMongoDB.getId();

            //*************************** INSERT DATA - DONE *********************//

            //*************************** UPDATE STATUS DATA *********************//
            Query querAutoMonitor = new Query();
            querAutoMonitor.addCriteria(Criteria.where("applicationId").is(saleQueueDTO.getApplicationId()).and("flagStatus").is(0).and("project").is(saleQueueDTO.getProject()).and("funcAutomation").is("saleQueue"));
            Update updateAutoMonitor = new Update();
            updateAutoMonitor.set("accountAuto", accountDTO.getUserName());
            updateAutoMonitor.set("flagStatus", 2);
            automationMonitorDTO = mongoTemplate.findAndModify(querAutoMonitor, updateAutoMonitor, AutomationMonitorDTO.class);
            automationMonitorId = automationMonitorDTO.getId();

            Query querySaleQueue = new Query();
            querySaleQueue.addCriteria(Criteria.where("_id").is(new ObjectId(idMongoDb)).and("status").is(0).and("appId").is(saleQueueDTO.getApplicationId()).and("project").is(saleQueueDTO.getProject()));
            Update updateSaleQueue = new Update();
            updateSaleQueue.set("userAuto", accountDTO.getUserName());
            updateSaleQueue.set("status", 2);
            mongoTemplate.findAndModify(querySaleQueue, updateSaleQueue, SaleQueueDTO.class);
            //*************************** END UPDATE STATUS DATA *********************//

            stage = "LOGIN FINONE";

            //***************************//LOGIN PAGE//***************************//
            LoginPageNeo loginPage = new LoginPageNeo(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword(),"CAS");
            loginPage.clickLogin();
            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));

            Utilities.captureScreenShot(driver);
            System.out.println("Func: saleQueue" + " - AccountAuto: " + accountDTO.getUserName() + " - " + stage + " DONE" + " - AppId: " + saleQueueDTO.getApplicationId());
            //***************************//END LOGIN//***************************//

            // ========== SALE QUEUE =================
            stage = "APPLICATION MANAGER";
            ApplicationManagerPage applicationManagerPage = new ApplicationManagerPage(driver);
            SearchMenu searchMenuMN = new SearchMenu(driver);
            searchMenuMN.MoveToPage(Constant.MENU_NAME_LINK_APPLICATION_MANAGER);

            await("Application Manager Page loading timeout!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Manager"));

            // Assigned Application with user login
            applicationManagerPage.setData(saleQueueDTO.getApplicationId(), accountDTO.getUserName());

            System.out.println("Func: saleQueue" + " - AccountAuto: " + accountDTO.getUserName() + " - " + stage + " DONE" + " - AppId: " + saleQueueDTO.getApplicationId());

            stage = "SALE QUEUE";
            SaleQueuePage saleQueuePage = new SaleQueuePage(driver);
            SearchMenu searchMenuSQ = new SearchMenu(driver);
            searchMenuSQ.MoveToPage(Constant.MENU_NAME_LINK_APPLICATIONS);

            await("Application Grid Page loading timeout!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Grid"));

            driver.findElement(By.xpath("//*[@id='lead']/a")).click();


            saleQueuePage.setData(saleQueueDTO, downdloadFileURL);
            System.out.println("Func: saleQueue" + " - AccountAuto: " + accountDTO.getUserName() + " - " + stage + " DONE" + " - AppId: " + saleQueueDTO.getApplicationId());


            // ========== Last Update User ACCA =================
            if (!Objects.isNull(saleQueueDTO.getUserCreatedSalesQueue())) {
                //update code, nếu không có up ACCA thì chuyen thang len DC nên reassing là user da raise saleQUEUE
                if (!saleQueueDTO.getDocuments().stream().filter(c -> c.getDocumentName().contains("(ACCA)")).findAny().isPresent()) {
                    SearchMenu searchMenuMN_1 = new SearchMenu(driver);
                    searchMenuMN_1.MoveToPage(Constant.MENU_NAME_LINK_APPLICATION_MANAGER);
                    // ========== APPLICATION MANAGER =================
                    stage = "APPLICATION MANAGER";
                    await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                            .until(driver::getTitle, is("Application Manager"));


                    applicationManagerPage.assignNotACCA(saleQueueDTO.getApplicationId(), accountDTO.getUserName());
                    SearchMenu searchMenuApp = new SearchMenu(driver);
                    searchMenuApp.MoveToPage(Constant.MENU_NAME_LINK_APPLICATIONS);
                    await("Application timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                            .until(driver::getTitle, is("Application Grid"));

                    driver.findElement(By.xpath("//*[@id='lead']/a")).click();

                    await("Input Application Assigned timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                            .until(() -> applicationManagerPage.getApplicationAssignedNumberElement().isDisplayed());

                    applicationManagerPage.getApplicationAssignedNumberElement().clear();
                    applicationManagerPage.getApplicationAssignedNumberElement().sendKeys(saleQueueDTO.getApplicationId());

                    await("tbApplicationAssignedElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                            .until(() -> applicationManagerPage.getTbApplicationAssignedElement().size() > 2);

                    WebElement applicationIdAssignedNumberElement = driver.findElement(By.xpath("//*[@id='LoanApplication_Assigned_wrapper']/div[1]/div/div[2]/div[2]/div/table/tbody/tr/td/a"));

                    await("webAssignElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                            .until(() -> applicationIdAssignedNumberElement.isDisplayed());
                    applicationIdAssignedNumberElement.click();

                    //MoveTo next step
                    driver.findElement(By.xpath("//div[contains(@id, 'move_to_next_stage_div')]//button[contains(@id, 'move_to_next_stage')]")).click();

                    await("Application timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                            .until(driver::getTitle, is("Application Grid"));

                    SearchMenu searchMenuMN_2 = new SearchMenu(driver);
                    searchMenuMN_2.MoveToPage(Constant.MENU_NAME_LINK_APPLICATION_MANAGER);
                    // ========== APPLICATION MANAGER =================
                    stage = "APPLICATION MANAGER";

                    await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                            .until(driver::getTitle, is("Application Manager"));

                    applicationManagerPage.getApplicationNumberElement().sendKeys(saleQueueDTO.getApplicationId());

                    applicationManagerPage.getSearchApplicationElement().click();

                    await("tdApplicationElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                            .until(() -> applicationManagerPage.getTdApplicationElement().size() > 0);

                    await("showTaskElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                            .until(() -> applicationManagerPage.getShowTaskElement().isDisplayed());

                    if ("runAutomationDE_SaleQueueLOGIN_ACCEPTANCE".equals(applicationManagerPage.getTdCheckStageApplicationElement().getText())) {
                        applicationManagerPage.setDataACCA(saleQueueDTO.getUserCreatedSalesQueue());
                    }
                }
            } else {
                AssignManagerSaleQueuePage de_applicationManagerPage = new AssignManagerSaleQueuePage(driver);
                SearchMenu searchMenuMN_3 = new SearchMenu(driver);
                searchMenuMN_3.MoveToPage(Constant.MENU_NAME_LINK_APPLICATION_MANAGER);

                // ========== APPLICATION MANAGER =================
                stage = "APPLICATION MANAGER";
                await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(driver::getTitle, is("Application Manager"));
                //waitting sale queue success
                Thread.sleep(10000);
                //Service Acc đang lấy cho team DE tại ngày 04/09/2020
                de_applicationManagerPage.setData(saleQueueDTO.getApplicationId(), "serviceacc_ldl");
            }

            responseModel.setProject(saleQueueDTO.getProject());
            responseModel.setReference_id(saleQueueDTO.getReferenceId());
            responseModel.setTransaction_id(saleQueueDTO.getTransactionId());
            responseModel.setApp_id(saleQueueDTO.getApplicationId());
            responseModel.setAutomation_result("SALEQUEUE PASS");

            querySaleQueue = new Query();
            querySaleQueue.addCriteria(Criteria.where("_id").is(new ObjectId(idMongoDb)).and("status").is(2).and("appId").is(saleQueueDTO.getApplicationId()).and("project").is(saleQueueDTO.getProject()));
            updateSaleQueue = new Update();
            updateSaleQueue.set("userAuto", accountDTO.getUserName());
            updateSaleQueue.set("status", 1);
            mongoTemplate.findAndModify(querySaleQueue, updateSaleQueue, SaleQueueDTO.class);

            //region //*****Update Automation Monitor*****//
            querAutoMonitor = new Query();
            querAutoMonitor.addCriteria(Criteria.where("_id").is(new ObjectId(automationMonitorId)).and("applicationId").is(saleQueueDTO.getApplicationId()).and("flagStatus").is(2).and("project").is(saleQueueDTO.getProject()).and("funcAutomation").is("saleQueue"));
            updateAutoMonitor = new Update();
            updateAutoMonitor.set("accountAuto", accountDTO.getUserName());
            updateAutoMonitor.set("description", "SALEQUEUE_PASS");
            updateAutoMonitor.set("flagStatus", 1);
            updateAutoMonitor.set("status", "COMPLETED");
            mongoTemplate.findAndModify(querAutoMonitor, updateAutoMonitor, AutomationMonitorDTO.class);
            //endregion

            Utilities.captureScreenShot(driver);

        } catch (Exception e) {
            Query querySaleQueue = new Query();
            querySaleQueue.addCriteria(Criteria.where("_id").is(new ObjectId(idMongoDb)).and("status").is(2).and("appId").is(saleQueueDTO.getApplicationId()).and("project").is(saleQueueDTO.getProject()));
            Update updateSaleQueue = new Update();
            updateSaleQueue.set("userAuto", accountDTO.getUserName());
            updateSaleQueue.set("status", 3);
            mongoTemplate.findAndModify(querySaleQueue, updateSaleQueue, SaleQueueDTO.class);

            responseModel.setProject(saleQueueDTO.getProject());
            responseModel.setReference_id(saleQueueDTO.getReferenceId());
            responseModel.setTransaction_id(saleQueueDTO.getTransactionId());
            responseModel.setApp_id(saleQueueDTO.getApplicationId());
            responseModel.setAutomation_result("SALEQUEUE FAILED" + " - " + e.getMessage() + " - Session: " + session);

            //region //*****Update Automation Monitor*****//
            Query querAutoMonitor = new Query();
            querAutoMonitor.addCriteria(Criteria.where("_id").is(new ObjectId(automationMonitorId)).and("applicationId").is(saleQueueDTO.getApplicationId()).and("flagStatus").is(2).and("project").is(saleQueueDTO.getProject()).and("funcAutomation").is("saleQueue"));
            Update updateAutoMonitor = new Update();
            updateAutoMonitor.set("accountAuto", accountDTO.getUserName());
            updateAutoMonitor.set("description", "SALEQUEUE_FAIL - Error: " + e.getMessage() + " - Session: " + session);
            updateAutoMonitor.set("flagStatus", 3);
            updateAutoMonitor.set("flagRetry", 1);
            updateAutoMonitor.set("status", "AUTO_SALEQUEUE_FAIL");
            mongoTemplate.findAndModify(querAutoMonitor, updateAutoMonitor, AutomationMonitorDTO.class);
            //endregion

            System.out.println("Auto Error:" + stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);
        } finally {
            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            System.out.println("Auto DONE:" + responseModel.getAutomation_result() + "- Project " + responseModel.getProject() + "- AppId " + responseModel.getApp_id());
            mongoTemplate.save(saleQueueDTO);
            logoutFinOne(driver, accountDTO);
            updateAccountFromMongoDB(accountDTO, saleQueueDTO.getProject().toUpperCase());
            responseUpdateAutomation(responseModel, "updateAutomation");
        }
    }
    //******endregion

    //region WAIVE FIELD
    public void runAutomation_WaiveField(WebDriver driver, Map<String, Object> mapValue, String browser, String projectAuto, String funcAutomation) throws Exception {
        String stage = "";
        int _totalAppId = 0;
        try {
            stage = "INIT DATA";

            List<LoginDTO> loginDTOList = new ArrayList<LoginDTO>();

            LoginDTO accountDTONew = null;
            do {
                //get list account finone available
                Query query = new Query();
                query.addCriteria(Criteria.where("active").is(0).and("project").is(projectAuto));
                AccountFinOneDTO accountFinOneDTO = mongoTemplate.findOne(query, AccountFinOneDTO.class);
                if (!Objects.isNull(accountFinOneDTO)) {
                    accountDTONew = new LoginDTO().builder().userName(accountFinOneDTO.getUsername()).password(accountFinOneDTO.getPassword()).build();

                    Query queryUpdate = new Query();
                    queryUpdate.addCriteria(Criteria.where("active").is(0).and("username").is(accountFinOneDTO.getUsername()).and("project").is(projectAuto));
                    Update update = new Update();
                    update.set("active", 1);
                    update.set("funcAutomation", funcAutomation);
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
            MFieldRequest mFieldRequestList = (MFieldRequest) mapValue.get("RequestWaiveFieldList");
            _totalAppId = mFieldRequestList.getWaiveFieldDTO().size();
            List<WaiveFieldsDTO> waiveFieldDTOList = (List<WaiveFieldsDTO>) mapValue.get("waiveFieldList");
            mongoTemplate.insert(waiveFieldDTOList, WaiveFieldsDTO.class);
            //*************************** END GET DATA *********************//

            if (loginDTOList.size() > 0) {
                ExecutorService workerThreadPoolDE = Executors.newFixedThreadPool(loginDTOList.size());

                for (LoginDTO loginDTO : loginDTOList) {
                    int totalAppId = _totalAppId;
                    workerThreadPoolDE.execute(new Runnable() {
                        public void run() {
                            runAutomation_WaiveField_run(loginDTO, browser, projectAuto, totalAppId);
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

    private void runAutomation_WaiveField_run(LoginDTO accountDTO, String browser, String projectAuto, int totalAppId) {
        WebDriver driver = null;
        Instant start = Instant.now();
        String stage = "";
        String referenceId = "";
        String projectId = "";
        ResponseAutomationModel responseModel = new ResponseAutomationModel();
        System.out.println("START - Auto: " + accountDTO.getUserName() + " - Time: " + Duration.between(start, Instant.now()).toSeconds());
        try {
            SeleniumGridDriver setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null, seleHost, selePort);
            driver = setupTestDriver.getDriver();
            SessionId session = ((RemoteWebDriver)driver).getSessionId();
            //***************************//LOGIN FINONE//***************************//
            stage = "LOGIN FINONE";

            LoginV2Page loginPage = new LoginV2Page(driver);
            loginPage.loginValue(accountDTO);

            //***************************//END LOGIN//***************************//

            System.out.println(stage + ": DONE");

            Utilities.captureScreenShot(driver);


            System.out.println("Auto: " + accountDTO.getUserName() + " - " + stage + ": DONE" + " - Time: " + Duration.between(start, Instant.now()).toSeconds());
            Utilities.captureScreenShot(driver);

            WaiveFieldDTO waiveFieldDTO = null;

            do {
                try {
                    System.out.println("Auto: " + accountDTO.getUserName() + " - BEGIN " + " - Time: " + Duration.between(start, Instant.now()).toSeconds());
                    Query query = new Query();
                    query.addCriteria(Criteria.where("status").is(0));
                    waiveFieldDTO = mongoTemplate.findOne(query, WaiveFieldDTO.class);

                    if (!Objects.isNull(waiveFieldDTO)) {

                        //update app
                        Query queryUpdate = new Query();
                        queryUpdate.addCriteria(Criteria.where("_id").is(new ObjectId(waiveFieldDTO.getId())).and("status").is(0).and("project").is(waiveFieldDTO.getProject()).and("projectAuto").is("WAIVEFIELD"));
                        Update update = new Update();
                        update.set("userAuto", accountDTO.getUserName());
                        update.set("status", 2);
                        WaiveFieldDTO resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, WaiveFieldDTO.class);

                        if (resultUpdate == null) {
                            continue;
                        }

                        System.out.println("Auto: " + accountDTO.getUserName() + " App: " + waiveFieldDTO.getAppId() + " - GET DONE " + " - User: " + accountDTO.getUserName() + " - Time: " + Duration.between(start, Instant.now()).toSeconds());

                        referenceId = resultUpdate.getReferenceId();
                        projectId = resultUpdate.getProject();

                        stage = "HOME PAGE";
                        System.out.println(stage + ": DONE" + " - Time " + Duration.between(start, Instant.now()).toSeconds());

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

                        System.out.println("PAGE LOADING: " + driver.getTitle());

                        await("WAIVE OFF ALL - FIELD INVESTIGATION INITIATION failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                                .until(driver::getTitle, is("Application Grid"));

                        System.out.println(stage + ": DONE" + " - Time " + Duration.between(start, Instant.now()).toSeconds());

                        // ========= UPDATE DB ============================
                        Query queryUpdate1 = new Query();
                        queryUpdate1.addCriteria(Criteria.where("_id").is(new ObjectId(waiveFieldDTO.getId())).and("status").is(2).and("project").is(waiveFieldDTO.getProject()).and("projectAuto").is("WAIVEFIELD"));
                        Update update1 = new Update();
                        update1.set("userAuto", accountDTO.getUserName());
                        update1.set("status", 1);
                        update1.set("checkUpdate", 1);
                        update1.set("automation_result", "WAIVE_FIELD_PASS");
                        update1.set("automation_result_message", "Session ID:" + session);
                        mongoTemplate.findAndModify(queryUpdate1, update1, WaiveFieldDTO.class);
                        System.out.println("Auto: " + accountDTO.getUserName() + " App: " + waiveFieldDTO.getAppId() + " - UPDATE STATUS " + " - User: " + accountDTO.getUserName() + " - Time: " + Duration.between(start, Instant.now()).toSeconds());
                    }
                } catch (Exception ex) {
                    Query queryUpdate = new Query();
                    queryUpdate.addCriteria(Criteria.where("_id").is(new ObjectId(waiveFieldDTO.getId())).and("status").is(2).and("project").is(waiveFieldDTO.getProject()).and("projectAuto").is("WAIVEFIELD"));
                    Update update = new Update();
                    update.set("userAuto", accountDTO.getUserName());
                    update.set("status", 3);
                    update.set("checkUpdate", 1);
                    update.set("automation_result", "WAIVE_FIELD_FAILED");
                    update.set("automation_result_message", "Session ID:" + session + "- ERROR: " + ex.getMessage() );
                    mongoTemplate.findAndModify(queryUpdate, update, WaiveFieldDTO.class);
                    System.out.println(ex.getMessage());
                }
            } while (!Objects.isNull(waiveFieldDTO));
        } catch (Exception e) {
            System.out.println("User Auto:" + accountDTO.getUserName() + " - " + stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);
        } finally {
            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            logout(driver,accountDTO.getUserName());
            pushAccountToQueue(accountDTO, projectAuto);
            if(!StringUtils.isEmpty(referenceId)){
                Query queryUpdateFailed = new Query();
                queryUpdateFailed.addCriteria(Criteria.where("referenceId").is(referenceId).and("checkUpdate").is(1));
                List<MobilityFieldReponeDTO> resultRespone = mongoTemplate.find(queryUpdateFailed, MobilityFieldReponeDTO.class);
                if (resultRespone.size() == totalAppId)
                {
                    responseModel.setReference_id(referenceId);
                    responseModel.setProject(projectId);
                    responseModel.setTransaction_id("transaction_waive_field");
                    responseModel.setData(resultRespone);
                    try {
                        autoUpdateStatusRabbitMobility(responseModel, "updateAutomation");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (driver != null) {
                driver.close();
                driver.quit();
            }
        }
    }
    //endregion

    //region SUBMIT FIELD
    public void runAutomation_SubmitFields_run(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO) throws Exception {
        ResponseAutomationModel responseModel = new ResponseAutomationModel();
        Instant start = Instant.now();
        String stage = "";
        String referenceId = "";
        String projectId = "";
        String applicationId = "UNKNOWN";
        String idMongoDb = "";
        System.out.println("START - Auto: " + accountDTO.getUserName() + " - Time: " + Duration.between(start, Instant.now()).toSeconds());
        SubmitFieldDTO submitFieldDTO = SubmitFieldDTO.builder().build();
        SessionId session = ((RemoteWebDriver)driver).getSessionId();
        try {
            stage = "INIT DATA";
            //*************************** INSERT DATA *********************//
            SubmitFieldDTO submitFieldDTOList = (SubmitFieldDTO) mapValue.get("submitFieldList");
//            mongoTemplate.insert(submitFieldDTOList);
            SubmitFieldDTO submitFieldIdMongoDB = mongoTemplate.insert(submitFieldDTOList);
            idMongoDb = submitFieldIdMongoDB.getId();
            System.out.println("Id_MongoDB => " + idMongoDb);
            applicationId = submitFieldDTOList.getAppId();
            referenceId = submitFieldDTOList.getReferenceId();
            projectId = submitFieldDTOList.getProject();
            System.out.println("Application_Id: " + applicationId);
            System.out.println("Reference_Id: " + referenceId);
            //*************************** END INSERT DATA *********************//

            //*************************** GET DATA *********************//
            System.out.println("Auto: " + accountDTO.getUserName() + " - BEGIN " + " - Time: " + Duration.between(start, Instant.now()).toSeconds());
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(new ObjectId(idMongoDb)).and("status").is(0).and("appId").is(applicationId).and("project").is(projectId).and("projectAuto").is("SUBMITFIELD"));
            submitFieldDTO = mongoTemplate.findOne(query, SubmitFieldDTO.class);
            log.info("{}", submitFieldDTO);

            Update update = new Update();
            update.set("userAuto", accountDTO.getUserName());
            update.set("status", 2);
            mongoTemplate.findAndModify(query, update, SubmitFieldDTO.class);

            //*************************** END GET DATA *********************//

            System.out.println(stage + ": DONE");

            //***************************//LOGIN FINONE//***************************//
            stage = "LOGIN FINONE";

            LoginV2Page loginPage = new LoginV2Page(driver);
            loginPage.loginValue(accountDTO);

            //***************************//END LOGIN//***************************//

            System.out.println(stage + ": DONE");

            Utilities.captureScreenShot(driver);

            stage = "HOME PAGE";
            System.out.println(stage + ": DONE" + " - Time " + Duration.between(start, Instant.now()).toSeconds());

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

                System.out.println("PAGE LOADING: " + driver.getTitle());

                await("Field Investigation Initiation failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> fv_FieldVerificationPage.getCheckMoveToNextStageElement().size() == 0);

                await("FIELD INVESTIGATION INITIATION failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(driver::getTitle, is("Application Grid"));

                System.out.println("STAGE - " + stageApplication + " - PASS");

                System.out.println(stage + ": DONE" + " - Time " + Duration.between(start, Instant.now()).toSeconds());
                stageApplication = "FIV";
            }
            if ("FIV".equals(stageApplication)){
                // ========== FIELD INVESTIGATION VERIFICATION =================
                stage = "FIELD INVESTIGATION VERIFICATION";
                FV_FieldInvestigationVerificationPage fv_FieldInvestigationVerificationPage = new FV_FieldInvestigationVerificationPage(driver);
                fv_FieldInvestigationVerificationPage.setData(submitFieldDTO, downdloadFileURL, accountDTO.getUserName());

                System.out.println("PAGE LOADING: " + driver.getTitle());

                await("FIELD INVESTIGATION VERIFICATION failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(driver::getTitle, is("FI Entries Grid"));

                System.out.println("STAGE - " + stageApplication + " - PASS");

                System.out.println(stage + ": DONE" + " - Time " + Duration.between(start, Instant.now()).toSeconds());
                stageApplication = "FIC";
            }
            if ("FIC".equals(stageApplication)){
                // ========== FIELD INVESTIGATION DETAILS =================
                stage = "FIELD INVESTIGATION DETAILS";
                FV_FieldInvestigationDetailsPage fv_FieldInvestigationDetailsPage = new FV_FieldInvestigationDetailsPage(driver);
                fv_FieldInvestigationDetailsPage.setData(submitFieldDTO, accountDTO.getUserName().toLowerCase());

                System.out.println("PAGE LOADING: " + driver.getTitle());

                await("Field Investigation Details failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> fv_FieldInvestigationDetailsPage.getCheckMoveToNextStageElement().size() == 0);

                await("FIELD INVESTIGATION DETAILS failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(driver::getTitle, is("Application Grid"));

                System.out.println("STAGE - " + stageApplication + " - PASS");

                System.out.println(stage + ": DONE" + " - Time " + Duration.between(start, Instant.now()).toSeconds());
            } else {
                await("Stage wrong Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> "FII".equals(fv_CheckStageApplicationManager.getTdCheckStageApplicationElement().getText()));
            }

            // ========= UPDATE DB ============================
            Query queryUpdate1 = new Query();
            queryUpdate1.addCriteria(Criteria.where("_id").is(new ObjectId(idMongoDb)).and("status").is(2).and("appId").is(applicationId).and("project").is(submitFieldDTO.getProject()).and("projectAuto").is("SUBMITFIELD"));
            Update update1 = new Update();
            update1.set("userAuto", accountDTO.getUserName());
            update1.set("status", 1);
            update1.set("automation_result", "SUBMIT_FIELD_PASS");
            update1.set("automation_result_message", "Session ID:" + session);
            mongoTemplate.findAndModify(queryUpdate1, update1, SubmitFieldDTO.class);

            Utilities.captureScreenShot(driver);

        } catch (Exception e) {
            Query queryUpdate = new Query();
            queryUpdate.addCriteria(Criteria.where("_id").is(new ObjectId(idMongoDb)).and("status").is(2).and("appId").is(applicationId).and("project").is(submitFieldDTO.getProject()).and("projectAuto").is("SUBMITFIELD"));
            Update update = new Update();
            update.set("userAuto", accountDTO.getUserName());
            update.set("status", 3);
            update.set("automation_result", "SUBMIT_FIELD_FAILED");
            update.set("automation_result_message", "Session ID:" + session + "- ERROR: " + e.getMessage() );
            mongoTemplate.findAndModify(queryUpdate, update, SubmitFieldDTO.class);
            System.out.println("Auto Error: " + stage + "\n => MESSAGE " + e.getMessage() + " => TRACE: " + e.toString());
            e.printStackTrace();
            Utilities.captureScreenShot(driver);
        } finally {
            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            System.out.println("Auto DONE:" + applicationId + " - Project: " + projectId + " - UserAuto: " + accountDTO.getUserName());
            logout(driver,accountDTO.getUserName());
            if(!StringUtils.isEmpty(referenceId)){
                Query queryUpdateFailed = new Query();
                queryUpdateFailed.addCriteria(Criteria.where("referenceId").is(referenceId).and("_id").is(new ObjectId(idMongoDb)));
                List<MobilityFieldReponeDTO> resultRespone = mongoTemplate.find(queryUpdateFailed, MobilityFieldReponeDTO.class);
                responseModel.setReference_id(referenceId);
                responseModel.setProject(projectId);
                responseModel.setTransaction_id("transaction_submit_field");
                responseModel.setData(resultRespone);
                autoUpdateStatusRabbitMobility(responseModel, "updateAutomation");
            }
        }
    }
    //endregion

    //region return simple product
    private void runAutomation_return(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO) {
        Instant start = Instant.now();
        String stage = "";
        StringBuilder sb = new StringBuilder();

        try {
            stage = "INIT DATA";
            //*************************** GET DATA *********************//
            Application application = (Application) mapValue.get("ApplicationDTO");
            String leadAppID = application.getApplicationId();

            sb.append("START: runAutomation_return APP: ").append(leadAppID)
                    .append(" - TIME: ").append(new Date().toString());

            updateAppError_Full(driver, stage, accountDTO, leadAppID, mapValue);

            sb.append(" - END runAutomation_return").append(" - TIME: ").append(new Date().toString());
        }catch (Exception e) {
            sb.append(" - ERROR: ").append(e.toString()).append(" - TIME: ").append(new Date().toString());;
        } finally {
            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            System.out.println(stage);
            log.info("{}", sb.toString());
        }
    }
    //endregion

    private void runAutomation_fine1Neo(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO) {
        Instant start = Instant.now();
        String stage = "";
        StringBuilder sb = new StringBuilder();
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
            LoginPageNeo loginPage = new LoginPageNeo(driver);
            Utilities.captureScreenShot(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword(), "CAS");
            loginPage.clickLogin();
            Utilities.captureScreenShot(driver);
//
//            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(driver::getTitle, is("DashBoard"));
//            System.out.println(stage + ": DONE");
//            Utilities.captureScreenShot(driver);
//
            stage = "HOME PAGE";
            HomePageNeo homePage = new HomePageNeo(driver);
//            stage = "QUICK LEAD";
////            // ========== QUICK LEAD =================
            homePage.menuClick();
            //=====================
            homePage.leadQuickClick();
            await("Quick Lead Entry timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Quick Lead Entry"));
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "QUICK LEAD (DOCUMENT UPLOAD & APPLICATION CREATION)";
            QuickLeadPageNeo quickLeadPage = new QuickLeadPageNeo(driver);
            quickLeadPage.setData(quickLead);
            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
            stage = "LEADS GRID";
//            // ========== LEAD PAGE =================
            LeadsPageNeo leadsPage = new LeadsPageNeo(driver);
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
//            leadsPage.setData(leadApp);

            System.out.println(stage + ": DONE");
            Utilities.captureScreenShot(driver);
//            stage = "LEAD STAGE";
//            // ========== LEAD STAGE =================
            //=============================
//            Thread.sleep(3000);
//            this.assignManger(driver, leadApp, "TPF DATA ENTRY", "trunglc");
            //=============================
            Thread.sleep(10000);
            this.getApplicationLead(driver, leadApp);
            //=============================
            LeadDetailPage leadDetailPage = new LeadDetailPage(driver);
            await("contentElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> leadDetailPage.getContentElement().isDisplayed());
            boolean flagResult = leadDetailPage.setData(quickLead, leadApp, downdloadFileURL);

            if (flagResult == false) {
                application.setApplicationId("UNKNOW");
                application.setStatus("ERROR");
                application.setStage(stage);
                application.setDescription("File not enough!!!");
                return;
            }

            Utilities.captureScreenShot(driver);
            await("Lead Page timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Lead Grid"));
        }catch (Exception e) {
            sb.append(" - ERROR: ").append(e.toString()).append(" - TIME: ").append(new Date().toString());;
        } finally {
            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            System.out.println(stage);
            log.info("{}", sb.toString());
        }
    }
    private void assignManger(WebDriver driver, String leadApp, String teamName, String accountName) throws InterruptedException {
        HomePageNeo homePage = new HomePageNeo(driver);
        homePage.menuClick();
        homePage.applicationManagerClick();
        homePage.getLeadNumber().sendKeys(leadApp);
        homePage.getSearchApplication().click();
        Thread.sleep(Constant.WAIT_ACCOUNT_GET_NULL);
        homePage.getShowTasks().click();
        homePage.getEditAssigned().click();
        homePage.getTeamName().clear();
        homePage.getTeamName().sendKeys(teamName);
        await("branchOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> homePage.getTeamOptionElement().size() > 0);
        for (WebElement e : homePage.getTeamOptionElement()) {
            if (!Objects.isNull(e.getAttribute("username")) && StringEscapeUtils.unescapeJava(e.getAttribute("username").toUpperCase()).equals(teamName)) {
                e.click();
                break;
            }
        }
        homePage.getNameAccount().clear();
        homePage.getNameAccount().sendKeys(accountName);
        await("branchOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> homePage.getAccountOptionElement().size() > 0);
        for (WebElement e : homePage.getAccountOptionElement()) {
            if (!Objects.isNull(e.getAttribute("username")) && StringEscapeUtils.unescapeJava(e.getAttribute("username").toUpperCase()).equals(accountName.toUpperCase())) {
                e.click();
                break;
            }
        }
        homePage.getSaveAssigned().click();
    }

    private void getApplicationLead(WebDriver driver, String leadApp) throws InterruptedException {
        HomePageNeo homePage= new HomePageNeo(driver);
//        homePage.menuClick();
//        homePage.leadQuickClickSearch();
        homePage.getAssigned().click();
        homePage.getSearchQueryElement().sendKeys(leadApp);
        homePage.getSearchQueryElement().sendKeys(Keys.ENTER);
        Thread.sleep(Constant.WAIT_ACCOUNT_GET_NULL);
        homePage.getLeadQueryElement().click();
    }

    private void getApplicationAppId(WebDriver driver, String leadApp) throws InterruptedException {
        HomePageNeo homePage= new HomePageNeo(driver);
        homePage.menuClick();
        homePage.applicationClick();
        Thread.sleep(Constant.WAIT_ASSIGN_TIMEOUT);
        homePage.getAssignedApp().click();
        await("branchOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> homePage.getApplicationNumber().isDisplayed());
        homePage.getApplicationNumber().sendKeys(leadApp);
        homePage.getApplicationNumber().sendKeys(Keys.ENTER);
        Thread.sleep(Constant.WAIT_ACCOUNT_GET_NULL);
        homePage.getAppQueryElement().click();
    }

    private String assignManger2(WebDriver driver, String leadApp, String teamName, String accountName) throws InterruptedException {
        HomePageNeo homePageNeo = new HomePageNeo(driver);
        homePageNeo.menuClick();
        homePageNeo.applicationManagerClick();
        homePageNeo.getLeadNumber().sendKeys(leadApp);
        homePageNeo.getSearchApplication().click();
        Thread.sleep(Constant.WAIT_ACCOUNT_GET_NULL);
        await("showTasks displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> homePageNeo.getShowTasks().isDisplayed());
        homePageNeo.getShowTasks().click();
        homePageNeo.getEditAssigned().click();
        homePageNeo.getTeamName().clear();
        homePageNeo.getTeamName().sendKeys(teamName);
        await("branchOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> homePageNeo.getTeamOptionElement().size() > 0);
        for (WebElement e : homePageNeo.getTeamOptionElement()) {
            if (!Objects.isNull(e.getAttribute("username")) && StringEscapeUtils.unescapeJava(e.getAttribute("username").toUpperCase()).equals(teamName)) {
                e.click();
                break;
            }
        }
        homePageNeo.getNameAccount().clear();
        homePageNeo.getNameAccount().sendKeys(accountName);
        await("branchOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> homePageNeo.getAccountOptionElement().size() > 0);
        for (WebElement e : homePageNeo.getAccountOptionElement()) {
            if (!Objects.isNull(e.getAttribute("username")) && StringEscapeUtils.unescapeJava(e.getAttribute("username").toUpperCase()).equals(accountName.toUpperCase())) {
                e.click();
                break;
            }
        }
        homePageNeo.getSaveAssigned().click();
        return homePageNeo.getApplicationTableAppIDElement().getText();
    }

}