package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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
import vn.com.tpf.microservices.models.AutoAssign.AutoAssignDTO;
import vn.com.tpf.microservices.models.Automation.*;
import vn.com.tpf.microservices.models.DEReturn.DEResponseQueryDTO;
import vn.com.tpf.microservices.models.DEReturn.DESaleQueueDTO;
import vn.com.tpf.microservices.models.DEReturn.DESaleQueueDocumentDTO;
import vn.com.tpf.microservices.models.QuickLead.Application;
import vn.com.tpf.microservices.models.QuickLead.QuickLead;
import vn.com.tpf.microservices.models.ResponseAutomationModel;
import vn.com.tpf.microservices.services.Automation.*;
import vn.com.tpf.microservices.services.Automation.deReturn.DE_ReturnApplicationManagerPage;
import vn.com.tpf.microservices.services.Automation.deReturn.DE_ReturnRaiseQueryPage;
import vn.com.tpf.microservices.services.Automation.deReturn.DE_ReturnSaleQueuePage;
import vn.com.tpf.microservices.services.Automation.lending.*;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.sql.Timestamp;
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

    @Value("${spring.url.finone}")
    private String fin1URL;

    @Value("${spring.url.seleHost}")
    private String seleHost;

    @Value("${spring.url.selePort}")
    private String selePort;


    @Value("${spring.url.downloadFile}")
    private String downdloadFileURL;

    private LoginDTO pollAccountFromQueue_OLD(Queue<LoginDTO> accounts,String project) throws Exception {
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

    private LoginDTO pollAccountFromQueue(Queue<LoginDTO> accounts,String project) throws Exception {
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
            AccountFinOneDTO accountFinOneDTO=mongoTemplate.findOne(query, AccountFinOneDTO.class);
            if (!Objects.isNull(accountFinOneDTO)) {
                accountDTO=new LoginDTO().builder().userName(accountFinOneDTO.getUsername()).password(accountFinOneDTO.getPassword()).build();

                Query queryUpdate = new Query();
                queryUpdate.addCriteria(Criteria.where("active").is(0).and("username").is(accountFinOneDTO.getUsername()).and("project").is(project));
                Update update = new Update();
                update.set("active", 1);
                AccountFinOneDTO resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, AccountFinOneDTO.class);

                if(resultUpdate==null)
                {
                    Thread.sleep(Constant.WAIT_ACCOUNT_GET_NULL);
                    accountDTO=null;
                }
                else {
                    System.out.println("Get it:" + accountDTO.toString());
                    System.out.println("Exist:" + accounts.size());
                }
            } else
                Thread.sleep(Constant.WAIT_ACCOUNT_TIMEOUT);
        }


        return accountDTO;
    }

    private void pushAccountToQueue_OLD(Queue<LoginDTO> accounts,LoginDTO accountDTO) {
        synchronized (accounts) {
            if (!Objects.isNull(accountDTO)) {
                System.out.println("push to queue... : " + accountDTO.toString());
                accounts.add(accountDTO);
            }
        }
    }

    private void pushAccountToQueue(LoginDTO accountDTO) {
        //synchronized (accounts) {
            if (!Objects.isNull(accountDTO)) {
//                System.out.println("push to queue... : " + accountDTO.toString());
//                accounts.add(accountDTO);

                Query queryUpdate = new Query();
                queryUpdate.addCriteria(Criteria.where("username").is(accountDTO.getUserName()));
                Update update = new Update();
                update.set("active", 0);
                AccountFinOneDTO resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, AccountFinOneDTO.class);
                System.out.println("Update it:" + accountDTO.toString());
            }
       // }
    }

    public void logout(WebDriver driver) {
        try {

            System.out.println("Logout");
            LogoutPage logoutPage = new LogoutPage(driver);
            logoutPage.logout();
        } catch (Exception e) {
            System.out.println("LOGOUT:" + e.toString());
        }
    }

    public void executor(Queue<LoginDTO> accounts, String browser, Map<String, Object> mapValue, String function,String project) {
        WebDriver driver = null;
        LoginDTO accountDTO = null;
        try {

            SeleniumGridDriver setupTestDriver = new SeleniumGridDriver(null, browser,fin1URL, null,seleHost,selePort);
            driver = setupTestDriver.getDriver();

            switch (function){
                case "runAutomation_UpdateInfo":
                    accountDTO = pollAccountFromQueue(accounts,project);
                    runAutomation_UpdateInfo(driver, mapValue, accountDTO);
                    break;
                case "runAutomation_UpdateAppError":
                    accountDTO = pollAccountFromQueue(accounts,project);
                    runAutomation_UpdateAppError(driver,mapValue,accountDTO);
                    break;
                case "quickLead":
                    accountDTO = pollAccountFromQueue(accounts,project);
                    runAutomation_QuickLead(driver, mapValue, accountDTO);
                    break;
                case "momoCreateApp":
                    accountDTO = pollAccountFromQueue_OLD(accounts,project);
                    runAutomation_momoCreateApp(driver, mapValue, accountDTO);
                    break;
                case "fptCreateApp":
                    accountDTO = pollAccountFromQueue(accounts,project);
                    runAutomation_fptCreateApp(driver, mapValue, accountDTO);
                    break;
                case "runAutomationDE_AutoAssign":
                    //chay get account trong function
                    runAutomationDE_autoAssign(driver, mapValue,project, browser);
                    break;
                case "runAutomationDE_ResponseQuery":
                    accountDTO = pollAccountFromQueue(accounts,project);
                    runAutomationDE_responseQuery(driver, mapValue, accountDTO);
                    break;
                case "runAutomationDE_SaleQueue":
                    accountDTO = pollAccountFromQueue(accounts,project);
                    runAutomationDE_saleQueue(driver, mapValue, accountDTO);
                    break;
                case "SN_quickLead":
                    accountDTO = pollAccountFromQueue(accounts,project);
                    SN_runAutomation_QuickLead(driver, mapValue, accountDTO);
                    break;
            }

        } catch (Exception e) {
            System.out.println("executor:" + e.toString());
            Utilities.captureScreenShot(driver);
        } finally {
            //logout(driver);
            if(project.equals("MOMO"))
            {
                pushAccountToQueue_OLD(accounts,accountDTO);
            }else
            {
                pushAccountToQueue(accountDTO);
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
        String stage= "";
        Application application= Application.builder().build();
        log.info("{}", application);
        try {
            stage="INIT DATA";
            //*************************** GET DATA *********************//
            application = (Application) mapValue.get("ApplicationDTO");
            QuickLead quickLead=application.getQuickLead();
            mongoTemplate.save(application);


            //*************************** END GET DATA *********************//
            Actions actions=new Actions(driver);
            System.out.println(stage + ": DONE" );
            stage="LOGIN FINONE";
            HashMap<String, String> dataControl = new HashMap<>();
            LoginPage loginPage = new LoginPage(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
            loginPage.clickLogin();
            //actions.moveToElement(loginPage.getBtnElement()).click().build().perform();
            
            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));


            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            stage="HOME PAGE";
            HomePage homePage = new HomePage(driver);


            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            stage="QUICK LEAD";
            // ========== QUICK LEAD =================
            homePage.menuClick();
            homePage.leadQuickClick();

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

            leadDetailPage.setData(quickLead,leadApp,downdloadFileURL);

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
            stage="APPLICATION MANAGER";
            // ========== APPLICATION MANAGER =================
            homePage.getMenuApplicationElement().click();
            homePage.getApplicationManagerElement().click();
            await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Manager"));

            DE_ApplicationManagerPage de_applicationManagerPage=new DE_ApplicationManagerPage(driver);

            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(()->de_applicationManagerPage.getApplicationManagerFormElement().isDisplayed());

            //get appID moi o day
            leadAppID=de_applicationManagerPage.getAppID(leadApp);

            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(()->de_applicationManagerPage.getBackBtnElement().isDisplayed());

            de_applicationManagerPage.getBackBtnElement().click();

            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(()->de_applicationManagerPage.getApplicationManagerFormElement().isDisplayed());

            de_applicationManagerPage.setData(leadAppID,accountDTO.getUserName());
            System.out.println(stage + ": DONE" );
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

            System.out.println(stage + "=> MESSAGE " + e.getMessage() +"\n TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);

            if (e.getMessage().contains("Work flow failed!!!")) {
                stage="END OF LEAD DETAIL";

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
            if(application.getApplicationId()== null || application.getApplicationId().isEmpty() || application.getApplicationId().indexOf("LEAD") > 0 || application.getApplicationId().indexOf("APPL") < 0 )
            {
                application.setApplicationId("UNKNOW");
            }

            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            try {
                updateStatusRabbit(application,"updateAutomation");
            }catch (Exception e)
            {
                System.out.println(e.toString());
            }
            logout(driver);
        }
    }

    public void runAutomation_UpdateInfo(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO) throws Exception {
        String stage= "";
        Instant start = Instant.now();
        Application application= Application.builder().build();
        try {
            stage="INIT DATA";
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

            //vinId = loanDetailsDTO.getApplicationNumber();
            //*************************** END GET DATA *********************//

            System.out.println(stage + ": DONE" );
            stage="LOGIN FINONE";
            HashMap<String, String> dataControl = new HashMap<>();
            LoginPage loginPage = new LoginPage(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
            loginPage.clickLogin();

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            stage="HOME PAGE";
            HomePage homePage = new HomePage(driver);

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            // ========== APPLICATIONS =================
            String leadAppID=application.getApplicationId();
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
            stage="APPLICATION MANAGER";
            // ========== APPLICATION MANAGER =================
            homePage.getApplicationManagerElement().click();
            await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Manager"));

            DE_ApplicationManagerPage de_applicationManagerPage=new DE_ApplicationManagerPage(driver);

            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(()->de_applicationManagerPage.getApplicationManagerFormElement().isDisplayed());
            de_applicationManagerPage.setData(leadAppID,accountDTO.getUserName());
            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            stage="APPLICATION GRID";
            // ========== APPLICATION GRID =================
            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(()-> de_applicationManagerPage.getMenuApplicationElement().isDisplayed());

            de_applicationManagerPage.getMenuApplicationElement().click();
            Utilities.captureScreenShot(driver);
            de_applicationManagerPage.getApplicationElement().click();
            Utilities.captureScreenShot(driver);
            await("Application Grid timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Grid"));
            Utilities.captureScreenShot(driver);
            ApplicationGridPage applicationGridPage=new ApplicationGridPage(driver);
            applicationGridPage.updateData(leadAppID);
            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            stage="LEAD DETAILS (DATA ENTRY)";
            // ========== LEAD DETAILS (DATA ENTRY) =================
            LeadDetailDEPage leadDetailDEPage= new LeadDetailDEPage(driver);
            leadDetailDEPage.setData(leadAppID);
            Utilities.captureScreenShot(driver);

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            stage="PERSONAL INFORMATION";
            // ========== PERSONAL INFORMATION =================
            DE_ApplicationInfoPage appInfoPage = new DE_ApplicationInfoPage(driver);
            DE_ApplicationInfoPersonalTab personalTab = appInfoPage.getApplicationInfoPersonalTab();

            await("getPersonalCustomerDetailsElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(()->personalTab.getPersonalCustomerDetailsElement().isDisplayed());

            personalTab.setValue(applicationInfoDTO);

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);
            stage="EMPLOYMENT DETAILS";
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

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);
            stage="EMPLOYMENT DETAILS - FINANCIAL";
            // ==========FINANCIAL DETAILS =================
            if (applicationInfoDTO.getIncomeDetails() != null && applicationInfoDTO.getIncomeDetails().size() > 0) {
                DE_ApplicationInfoFinancialDetailsTab financialDetailsTab = appInfoPage.getApplicationInfoFinancialDetailsTab();

                financialDetailsTab.openIncomeDetailSection();
                await("Load financial details - income details Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> financialDetailsTab.getIncomeDetailDivElement().isDisplayed());
                financialDetailsTab.setIncomeDetailsData(applicationInfoDTO.getIncomeDetails());
                financialDetailsTab.saveAndNext();
            }

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            // ==========LOAN DETAILS=================
            stage="LOAN DETAIL PAGE - SOURCING DETAIL TAB";
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

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            // ==========VAP DETAILS=======================
            if (loanDetailsVapDTO != null && loanDetailsVapDTO.getVapProduct() !=null && !loanDetailsVapDTO.getVapProduct().equals("")) {
                stage="LOAN DETAIL PAGE - VAP DETAIL TAB";
                Utilities.captureScreenShot(driver);
                DE_LoanDetailsVapDetailsTab loanDetailsVapDetailsTab = new DE_LoanDetailsVapDetailsTab(driver);

                await("Load loan details - sourcing details container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> loanDetailsVapDetailsTab.getVapDetailsDivContainerElement().isDisplayed());
                loanDetailsVapDetailsTab.setData(loanDetailsVapDTO);
                Utilities.captureScreenShot(driver);
                loanDetailsVapDetailsTab.getBtnSaveAndNextElement().click();

                System.out.println("LOAN DETAILS - VAP: DONE");

            }

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);
            stage="DOCUMENTS";
            // ==========DOCUMENTS=================
            if(documentDTOS.size()>0) {
                DE_DocumentsPage documentsPage = new DE_DocumentsPage(driver);
                await("Load document tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> documentsPage.getTabDocumentsElement().isDisplayed() && documentsPage.getTabDocumentsElement().isEnabled());
                documentsPage.getTabDocumentsElement().click();
                await("Load document container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> documentsPage.getDocumentsContainerElement().isDisplayed());
                documentsPage.getBtnGetDocumentElement().click();
                await("Load document table Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> documentsPage.getLendingTrElement().size() > 0);

                documentsPage.updateData(documentDTOS,downdloadFileURL);
                Utilities.captureScreenShot(driver);
                documentsPage.getBtnSubmitElement().click();
            }

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);
            stage="REFERENCES";
            // ==========REFERENCES=================
            stage="REFERENCES PAGE";
            DE_ReferencesPage referencesPage = new DE_ReferencesPage(driver);
            await("Load references tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> referencesPage.getTabReferencesElement().isDisplayed() && referencesPage.getTabReferencesElement().isEnabled());
            referencesPage.getTabReferencesElement().click();
            referencesPage.setData(referenceDTO);
            referencesPage.getSaveBtnElement().click();

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            // ==========MISC FRM APP DTL=================
            stage="MISC FRM APPDTL PAGE";
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
            miscFrmAppDtlPage.getBtnMoveToNextStageElement().click();
            Utilities.captureScreenShot(driver);
            System.out.println("MISC FRM APP DTL: DONE");

            await("Work flow failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Grid"));

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);
            stage="COMPLETE";
            System.out.println("AUTO OK: user =>" + accountDTO.getUserName());

            //UPDATE STATUS
            application.setStatus("OK");
            application.setDescription("Thanh cong");

            //logout(driver);

        } catch (Exception e) {
            //UPDATE STATUS
            application.setStatus("ERROR");
            application.setStage(stage);
            application.setDescription(e.getMessage());

            System.out.println(stage + "=> MESSAGE " + e.getMessage() +"\n TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);

            if (e.getMessage().contains("Work flow failed!!!")) {
                stage="END OF LEAD DETAIL";
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
            updateStatusRabbit(application,"updateFullApp");
            logout(driver);
        }
    }

    public void runAutomation_UpdateAppError(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO) throws Exception {
        Instant start = Instant.now();
        String stage= "";
        String stageError="";
        Application application= Application.builder().build();
        String leadAppID="";
        try {
            stage="INIT DATA";
            //*************************** GET DATA *********************//
            application = (Application) mapValue.get("ApplicationDTO");
            leadAppID=application.getApplicationId();
            ApplicationInfoDTO applicationInfoDTO = (ApplicationInfoDTO) mapValue.get("ApplicationInfoDTO");
            LoanDetailsDTO loanDetailsDTO = (LoanDetailsDTO) mapValue.get("LoanDetailsDTO");
            LoanDetailsVapDTO loanDetailsVapDTO = (LoanDetailsVapDTO) mapValue.get("LoanDetailsVapDTO");
            List<ReferenceDTO> referenceDTO = (List<ReferenceDTO>) mapValue.get("ReferenceDTO");
            MiscFptDTO miscFptDTO = (MiscFptDTO) mapValue.get("MiscFptDTO");
            MiscFrmAppDtlDTO miscFrmAppDtlDTO = (MiscFrmAppDtlDTO) mapValue.get("MiscFrmAppDtlDTO");
            MiscVinIdDTO miscVinIdDTO = (MiscVinIdDTO) mapValue.get("MiscVinIdDTO");

            //vinId = loanDetailsDTO.getApplicationNumber();
            //*************************** END GET DATA *********************//

            //---------------- GET STAGE -------------------------//
            stageError=application.getStage();

            switch (stageError)
            {
                case "END OF LEAD DETAIL":
                    if(application.getError()!=null && application.getError().contains("Address")){
                        updateAppError_EndLeadDetailWithAddress(driver, stageError, accountDTO, leadAppID, mapValue);
                    }
                    else {
                        updateAppError_EndLeadDetail(driver, stageError, accountDTO, leadAppID, mapValue);
                    }
                    break;
                case "END OF LEAD DETAILV1":
                    updateAppError_EndLeadDetailV1(driver, stageError, accountDTO, leadAppID, mapValue);
                    break;
                case "PERSONAL INFORMATION":
                    updateAppError_PersonalInformation(driver,stageError,accountDTO,leadAppID,mapValue);
                    break;
                case "EMPLOYMENT DETAILS":
                    updateAppError_PersonalInformation(driver,stageError,accountDTO,leadAppID,mapValue);
                    break;
                case "LOGIN FINONE":
                    updateAppError_PersonalInformation(driver,stageError,accountDTO,leadAppID,mapValue);
                    break;
                case "INIT DATA":
                    updateAppError_PersonalInformation(driver,stageError,accountDTO,leadAppID,mapValue);
                    break;
                case "FULL":
                    updateAppError_Full(driver,stageError,accountDTO,leadAppID,mapValue);
                    break;
                default:
                    updateAppError_Full(driver,stageError,accountDTO,leadAppID,mapValue);
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

            System.out.println(stage + "=> MESSAGE " + e.getMessage() +"\n TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);

            if (e.getMessage().contains("Work flow failed!!!")) {
                stage="END OF LEAD DETAIL";
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
            logout(driver);
        }
    }

    private void updateStatusRabbit(Application application,String func) throws Exception {
//        JsonNode jsonNode= rabbitMQService.sendAndReceive("tpf-service-dataentry",
//                Map.of("func", func, "token",
//                        String.format("Bearer %s", rabbitMQService.getToken().path("access_token").asText()),"body", application));

        JsonNode jsonNode= rabbitMQService.sendAndReceive("tpf-service-dataentry",
                Map.of("func", func,"body", application));
        System.out.println("rabit:=>" + jsonNode.toString());

    }

    private void updateAppError_EndLeadDetail(WebDriver driver, String stage, LoginDTO accountDTO, String leadAppID, Map<String, Object> mapValue) throws Exception {
        Application application= Application.builder().build();
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
            stage="APPLICATION MANAGER";
            // ========== APPLICATION MANAGER =================
            homePage.getApplicationManagerElement().click();
            await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Manager"));

            DE_ApplicationManagerPage de_applicationManagerPage=new DE_ApplicationManagerPage(driver);

            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(()->de_applicationManagerPage.getApplicationManagerFormElement().isDisplayed());
            de_applicationManagerPage.setData(leadAppID,accountDTO.getUserName());
            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            stage="APPLICATION GRID";
            // ========== APPLICATION GRID =================
            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(()-> de_applicationManagerPage.getMenuApplicationElement().isDisplayed());

            de_applicationManagerPage.getMenuApplicationElement().click();
            Utilities.captureScreenShot(driver);
            de_applicationManagerPage.getApplicationElement().click();
            Utilities.captureScreenShot(driver);
            await("Application Grid timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Grid"));
            Utilities.captureScreenShot(driver);
            ApplicationGridPage applicationGridPage=new ApplicationGridPage(driver);
            applicationGridPage.updateData(leadAppID);
            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            // ========== LEAD DETAILS (DATA ENTRY) =================
            LeadDetailDEPage leadDetailDEPage= new LeadDetailDEPage(driver);
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
            logout(driver);
        }
    }

    private void updateAppError_EndLeadDetailV1(WebDriver driver, String stage, LoginDTO accountDTO, String leadAppID, Map<String, Object> mapValue) throws Exception {
        Application application= Application.builder().build();
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
            stage="APPLICATION MANAGER";
            // ========== APPLICATION MANAGER =================
            homePage.getApplicationManagerElement().click();
            await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Manager"));

            DE_ApplicationManagerPage de_applicationManagerPage=new DE_ApplicationManagerPage(driver);

            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(()->de_applicationManagerPage.getApplicationManagerFormElement().isDisplayed());
            de_applicationManagerPage.setData(leadAppID,accountDTO.getUserName());
            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            stage="APPLICATION GRID";
            // ========== APPLICATION GRID =================
            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(()-> de_applicationManagerPage.getMenuApplicationElement().isDisplayed());

            de_applicationManagerPage.getMenuApplicationElement().click();
            Utilities.captureScreenShot(driver);
            de_applicationManagerPage.getApplicationElement().click();
            Utilities.captureScreenShot(driver);
            await("Application Grid timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Grid"));
            Utilities.captureScreenShot(driver);
            ApplicationGridPage applicationGridPage=new ApplicationGridPage(driver);
            applicationGridPage.updateData(leadAppID);
            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            // ========== LEAD DETAILS (DATA ENTRY) =================
            LeadDetailDEPage leadDetailDEPage= new LeadDetailDEPage(driver);
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
            logout(driver);
        }
    }

    private void updateAppError_PersonalInformation(WebDriver driver, String stage, LoginDTO accountDTO, String leadAppID, Map<String, Object> mapValue) throws Exception {
        Application application= Application.builder().build();
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
            stage="APPLICATION MANAGER";
            // ========== APPLICATION MANAGER =================
            homePage.getApplicationManagerElement().click();
            await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Manager"));

            DE_ApplicationManagerPage de_applicationManagerPage=new DE_ApplicationManagerPage(driver);

            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(()->de_applicationManagerPage.getApplicationManagerFormElement().isDisplayed());
            de_applicationManagerPage.setData(leadAppID,accountDTO.getUserName());
            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            stage="APPLICATION GRID";
            // ========== APPLICATION GRID =================
            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(()-> de_applicationManagerPage.getMenuApplicationElement().isDisplayed());

            de_applicationManagerPage.getMenuApplicationElement().click();
            Utilities.captureScreenShot(driver);
            de_applicationManagerPage.getApplicationElement().click();
            Utilities.captureScreenShot(driver);
            await("Application Grid timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Grid"));
            Utilities.captureScreenShot(driver);
            ApplicationGridPage applicationGridPage=new ApplicationGridPage(driver);
            applicationGridPage.updateData(leadAppID);
            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            // ========== LEAD DETAILS (DATA ENTRY) =================
            LeadDetailDEPage leadDetailDEPage= new LeadDetailDEPage(driver);
            leadDetailDEPage.setData(leadAppID);
            Utilities.captureScreenShot(driver);

            stage="PERSONAL INFORMATION";
            // ========== PERSONAL INFORMATION =================
            DE_ApplicationInfoPage appInfoPage = new DE_ApplicationInfoPage(driver);
            DE_ApplicationInfoPersonalTab personalTab = appInfoPage.getApplicationInfoPersonalTab();

            await("getPersonalCustomerDetailsElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(()->personalTab.getPersonalCustomerDetailsElement().isDisplayed());

            personalTab.updateValue(applicationInfoDTO);

            System.out.println(stage + ": DONE" );
            stage="EMPLOYMENT DETAILS";
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

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);
            stage="EMPLOYMENT DETAILS - FINANCIAL";
            // ==========FINANCIAL DETAILS =================
            if (applicationInfoDTO.getIncomeDetails() != null && applicationInfoDTO.getIncomeDetails().size() > 0) {
                DE_ApplicationInfoFinancialDetailsTab financialDetailsTab = appInfoPage.getApplicationInfoFinancialDetailsTab();

                financialDetailsTab.openIncomeDetailSection();
                await("Load financial details - income details Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> financialDetailsTab.getIncomeDetailDivElement().isDisplayed());
                financialDetailsTab.setIncomeDetailsData(applicationInfoDTO.getIncomeDetails());
                financialDetailsTab.saveAndNext();
            }

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            // ==========LOAN DETAILS=================
            stage="LOAN DETAIL PAGE - SOURCING DETAIL TAB";
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

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            // ==========VAP DETAILS=======================
            if (loanDetailsVapDTO != null && loanDetailsVapDTO.getVapProduct()!=null && !loanDetailsVapDTO.getVapProduct().equals("")) {
                stage="LOAN DETAIL PAGE - VAP DETAIL TAB";
                Utilities.captureScreenShot(driver);
                DE_LoanDetailsVapDetailsTab loanDetailsVapDetailsTab = new DE_LoanDetailsVapDetailsTab(driver);

                await("Load loan details - sourcing details container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> loanDetailsVapDetailsTab.getVapDetailsDivContainerElement().isDisplayed());
                loanDetailsVapDetailsTab.setData(loanDetailsVapDTO);
                Utilities.captureScreenShot(driver);
                loanDetailsVapDetailsTab.getBtnSaveAndNextElement().click();

                System.out.println("LOAN DETAILS - VAP: DONE");

            }

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);
            stage="DOCUMENTS";
            // ==========DOCUMENTS=================
            if(documentDTOS.size()>0) {
                DE_DocumentsPage documentsPage = new DE_DocumentsPage(driver);
                await("Load document tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> documentsPage.getTabDocumentsElement().isDisplayed() && documentsPage.getTabDocumentsElement().isEnabled());
                documentsPage.getTabDocumentsElement().click();
                await("Load document container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> documentsPage.getDocumentsContainerElement().isDisplayed());
                documentsPage.getBtnGetDocumentElement().click();
                await("Load document table Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> documentsPage.getLendingTrElement().size() > 0);

                documentsPage.updateData(documentDTOS,downdloadFileURL);
                Utilities.captureScreenShot(driver);
                documentsPage.getBtnSubmitElement().click();
            }

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);
            stage="REFERENCES";
            // ==========REFERENCES=================
            stage="REFERENCES PAGE";
            DE_ReferencesPage referencesPage = new DE_ReferencesPage(driver);
            await("Load references tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> referencesPage.getTabReferencesElement().isDisplayed() && referencesPage.getTabReferencesElement().isEnabled());
            referencesPage.getTabReferencesElement().click();
            referencesPage.setData(referenceDTO);
            referencesPage.getSaveBtnElement().click();

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            // ==========MISC FRM APP DTL=================
            stage="MISC FRM APPDTL PAGE";
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
            miscFrmAppDtlPage.getBtnMoveToNextStageElement().click();
            Utilities.captureScreenShot(driver);
            System.out.println("MISC FRM APP DTL: DONE");

            await("Work flow failed!!!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Grid"));

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);
            stage="COMPLETE";
            System.out.println("AUTO OK: user =>" + accountDTO.getUserName());

            //UPDATE STATUS
            application.setStatus("OK");
            application.setStage(stage);
            application.setDescription("Thanh cong");

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
            logout(driver);
        }
    }

    private void updateAppError_EndLeadDetailWithAddress(WebDriver driver, String stage, LoginDTO accountDTO, String leadAppID, Map<String, Object> mapValue) throws Exception {
        Application application= Application.builder().build();
        try {
            application = (Application) mapValue.get("ApplicationDTO");
            ApplicationInfoDTO applicationInfoDTO = (ApplicationInfoDTO) mapValue.get("ApplicationInfoDTO");
            LoanDetailsDTO loanDetailsDTO = (LoanDetailsDTO) mapValue.get("LoanDetailsDTO");
            LoanDetailsVapDTO loanDetailsVapDTO = (LoanDetailsVapDTO) mapValue.get("LoanDetailsVapDTO");
            List<ReferenceDTO> referenceDTO = (List<ReferenceDTO>) mapValue.get("ReferenceDTO");
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
            stage="APPLICATION MANAGER";
            // ========== APPLICATION MANAGER =================
            homePage.getApplicationManagerElement().click();
            await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Manager"));

            DE_ApplicationManagerPage de_applicationManagerPage=new DE_ApplicationManagerPage(driver);

            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(()->de_applicationManagerPage.getApplicationManagerFormElement().isDisplayed());
            de_applicationManagerPage.setData(leadAppID,accountDTO.getUserName());
            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);


            stage="APPLICATION GRID";
            // ========== APPLICATION GRID =================
            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(()-> de_applicationManagerPage.getMenuApplicationElement().isDisplayed());

            de_applicationManagerPage.getMenuApplicationElement().click();
            Utilities.captureScreenShot(driver);
            de_applicationManagerPage.getApplicationElement().click();
            Utilities.captureScreenShot(driver);
            await("Application Grid timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Grid"));
            Utilities.captureScreenShot(driver);
            ApplicationGridPage applicationGridPage=new ApplicationGridPage(driver);
            applicationGridPage.updateData(leadAppID);
            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            stage="LEAD DETAILS (DATA ENTRY)";
            // ========== LEAD DETAILS (DATA ENTRY) =================
            LeadDetailDEPage leadDetailDEPage= new LeadDetailDEPage(driver);
            leadDetailDEPage.setData(leadAppID);
            Utilities.captureScreenShot(driver);

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            stage="PERSONAL INFORMATION";
            // ========== PERSONAL INFORMATION =================
            DE_ApplicationInfoPage appInfoPage = new DE_ApplicationInfoPage(driver);
            DE_ApplicationInfoPersonalTab personalTab = appInfoPage.getApplicationInfoPersonalTab();

            await("getPersonalCustomerDetailsElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(()->personalTab.getPersonalCustomerDetailsElement().isDisplayed());

            personalTab.updateValue(applicationInfoDTO);

            System.out.println(stage + ": DONE" );
            stage="EMPLOYMENT DETAILS";
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

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);
            stage="FINANCIAL DETAILS";
            // ==========FINANCIAL DETAILS =================
            if (applicationInfoDTO.getIncomeDetails() != null && applicationInfoDTO.getIncomeDetails().size() > 0) {
                DE_ApplicationInfoFinancialDetailsTab financialDetailsTab = appInfoPage.getApplicationInfoFinancialDetailsTab();

                financialDetailsTab.openIncomeDetailSection();
                await("Load financial details - income details Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> financialDetailsTab.getIncomeDetailDivElement().isDisplayed());
                financialDetailsTab.updateIncomeDetailsData(applicationInfoDTO.getIncomeDetails());
                financialDetailsTab.saveAndNext();
            }

            System.out.println(stage + ": DONE" );
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
            logout(driver);
        }
    }

    private void updateAppError_Full(WebDriver driver, String stage, LoginDTO accountDTO, String leadAppID, Map<String, Object> mapValue) throws Exception {
        Application application= Application.builder().build();
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
            stage="APPLICATION MANAGER";
            // ========== APPLICATION MANAGER =================
            homePage.getApplicationManagerElement().click();
            await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Manager"));

            DE_ApplicationManagerPage de_applicationManagerPage=new DE_ApplicationManagerPage(driver);

            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(()->de_applicationManagerPage.getApplicationManagerFormElement().isDisplayed());
            de_applicationManagerPage.setData(leadAppID,accountDTO.getUserName());
            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            stage="APPLICATION GRID";
            // ========== APPLICATION GRID =================
            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(()-> de_applicationManagerPage.getMenuApplicationElement().isDisplayed());

            de_applicationManagerPage.getMenuApplicationElement().click();
            Utilities.captureScreenShot(driver);
            de_applicationManagerPage.getApplicationElement().click();
            Utilities.captureScreenShot(driver);
            await("Application Grid timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Grid"));
            Utilities.captureScreenShot(driver);
            ApplicationGridPage applicationGridPage=new ApplicationGridPage(driver);
            applicationGridPage.updateData(leadAppID);
            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            stage="LEAD DETAILS (DATA ENTRY)";
            // ========== LEAD DETAILS (DATA ENTRY) =================
            LeadDetailDEPage leadDetailDEPage= new LeadDetailDEPage(driver);
            leadDetailDEPage.setData(leadAppID);
            Utilities.captureScreenShot(driver);

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);
            stage="PERSONAL INFORMATION";
            // ========== PERSONAL INFORMATION =================
            DE_ApplicationInfoPage appInfoPage = new DE_ApplicationInfoPage(driver);
            DE_ApplicationInfoPersonalTab personalTab = appInfoPage.getApplicationInfoPersonalTab();

            await("getPersonalCustomerDetailsElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(()->personalTab.getPersonalCustomerDetailsElement().isDisplayed());

            personalTab.updateValue(applicationInfoDTO);

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);
            stage="EMPLOYMENT DETAILS";
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

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);
            stage="FINANCIAL DETAILS";
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

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            // ==========LOAN DETAILS=================
            stage="LOAN DETAIL - SOURCING DETAIL TAB";
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

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            // ========== VAP DETAILS =======================
            if (loanDetailsVapDTO != null && loanDetailsVapDTO.getVapProduct()!=null && !loanDetailsVapDTO.getVapProduct().equals("")) {
                stage="LOAN DETAIL - VAP DETAIL TAB";
                Utilities.captureScreenShot(driver);
                DE_LoanDetailsVapDetailsTab loanDetailsVapDetailsTab = new DE_LoanDetailsVapDetailsTab(driver);

                await("Load loanDetailsVapDetailsTab container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> loanDetailsVapDetailsTab.getVapDetailsDivContainerElement().isDisplayed());

                loanDetailsVapDetailsTab.updateData(loanDetailsVapDTO);
                Utilities.captureScreenShot(driver);
                loanDetailsVapDetailsTab.getBtnSaveAndNextElement().click();

                System.out.println("LOAN DETAILS - VAP: DONE");

            }

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            stage="DOCUMENTS";
            // ==========DOCUMENTS=================
            if(documentDTOS.size()>0) {
                DE_DocumentsPage documentsPage = new DE_DocumentsPage(driver);
                await("Load document tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> documentsPage.getTabDocumentsElement().isDisplayed() && documentsPage.getTabDocumentsElement().isEnabled());
                documentsPage.getTabDocumentsElement().click();
                await("Load document container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> documentsPage.getDocumentsContainerElement().isDisplayed());
                documentsPage.getBtnGetDocumentElement().click();
                await("Load document table Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> documentsPage.getLendingTrElement().size() > 0);

                documentsPage.updateData(documentDTOS,downdloadFileURL);
                Utilities.captureScreenShot(driver);
                documentsPage.getBtnSubmitElement().click();
            }


            // ==========REFERENCES=================
            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);
            stage="REFERENCES";
            DE_ReferencesPage referencesPage = new DE_ReferencesPage(driver);
            await("Load references tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> referencesPage.getTabReferencesElement().isDisplayed() && referencesPage.getTabReferencesElement().isEnabled());
            referencesPage.getTabReferencesElement().click();

            referencesPage.updateData(referenceDTO);
            Utilities.captureScreenShot(driver);
            referencesPage.getSaveBtnElement().click();

            System.out.println("REFERENCES DETAILS: DONE");

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            // ==========MISC FRM APP DTL=================
            stage="MISC FRM APPDTL";
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

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);
            stage="COMPLETE";
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
            logout(driver);
        }
    }

    private void updateAppError_FullAll(WebDriver driver, String stage, LoginDTO accountDTO, String leadAppID, Map<String, Object> mapValue) throws Exception {
        Application application= Application.builder().build();
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

            stage="APPLICATION MANAGER";
            // ========== APPLICATION MANAGER =================
            homePage.getMenuApplicationElement().click();
            homePage.getApplicationManagerElement().click();
            await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Manager"));

            DE_ApplicationManagerPage de_applicationManagerPage=new DE_ApplicationManagerPage(driver);

            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(()->de_applicationManagerPage.getApplicationManagerFormElement().isDisplayed());
            de_applicationManagerPage.setData(leadAppID,accountDTO.getUserName());
            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            stage="APPLICATION GRID";
            // ========== APPLICATION GRID =================
            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(()-> de_applicationManagerPage.getMenuApplicationElement().isDisplayed());

            de_applicationManagerPage.getMenuApplicationElement().click();
            Utilities.captureScreenShot(driver);
            de_applicationManagerPage.getApplicationElement().click();
            Utilities.captureScreenShot(driver);
            await("Application Grid timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Grid"));
            Utilities.captureScreenShot(driver);
            ApplicationGridPage applicationGridPage=new ApplicationGridPage(driver);
            applicationGridPage.updateData(leadAppID);
            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            stage="LEAD DETAILS (DATA ENTRY)";
            // ========== LEAD DETAILS (DATA ENTRY) =================
            LeadDetailDEPage leadDetailDEPage= new LeadDetailDEPage(driver);
            leadDetailDEPage.setData(leadAppID);
            Utilities.captureScreenShot(driver);

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);
            stage="PERSONAL INFORMATION";
            // ========== PERSONAL INFORMATION =================
            DE_ApplicationInfoPage appInfoPage = new DE_ApplicationInfoPage(driver);
            DE_ApplicationInfoPersonalTab personalTab = appInfoPage.getApplicationInfoPersonalTab();

            await("getPersonalCustomerDetailsElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(()->personalTab.getPersonalCustomerDetailsElement().isDisplayed());

            personalTab.updateValue(applicationInfoDTO);

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);
            stage="EMPLOYMENT DETAILS";
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

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);
            stage="FINANCIAL DETAILS";
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

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            // ==========LOAN DETAILS=================
            stage="LOAN DETAIL - SOURCING DETAIL TAB";
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

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            // ========== VAP DETAILS =======================
            if (loanDetailsVapDTO != null && loanDetailsVapDTO.getVapProduct()!=null && !loanDetailsVapDTO.getVapProduct().equals("")) {
                stage="LOAN DETAIL - VAP DETAIL TAB";
                Utilities.captureScreenShot(driver);
                DE_LoanDetailsVapDetailsTab loanDetailsVapDetailsTab = new DE_LoanDetailsVapDetailsTab(driver);

                await("Load loanDetailsVapDetailsTab container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> loanDetailsVapDetailsTab.getVapDetailsDivContainerElement().isDisplayed());

                loanDetailsVapDetailsTab.updateData(loanDetailsVapDTO);
                Utilities.captureScreenShot(driver);
                loanDetailsVapDetailsTab.getBtnSaveAndNextElement().click();

                System.out.println("LOAN DETAILS - VAP: DONE");

            }

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            stage="DOCUMENTS";
            // ==========DOCUMENTS=================
            if(documentDTOS.size()>0) {
                DE_DocumentsPage documentsPage = new DE_DocumentsPage(driver);
                await("Load document tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> documentsPage.getTabDocumentsElement().isDisplayed() && documentsPage.getTabDocumentsElement().isEnabled());
                documentsPage.getTabDocumentsElement().click();
                await("Load document container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> documentsPage.getDocumentsContainerElement().isDisplayed());
                documentsPage.getBtnGetDocumentElement().click();
                await("Load document table Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> documentsPage.getLendingTrElement().size() > 0);

                documentsPage.updateData(documentDTOS,downdloadFileURL);
                Utilities.captureScreenShot(driver);
                documentsPage.getBtnSubmitElement().click();
            }


            // ==========REFERENCES=================
            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);
            stage="REFERENCES";
            DE_ReferencesPage referencesPage = new DE_ReferencesPage(driver);
            await("Load references tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> referencesPage.getTabReferencesElement().isDisplayed() && referencesPage.getTabReferencesElement().isEnabled());
            referencesPage.getTabReferencesElement().click();

            referencesPage.updateData(referenceDTO);
            Utilities.captureScreenShot(driver);
            referencesPage.getSaveBtnElement().click();

            System.out.println("REFERENCES DETAILS: DONE");

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            // ==========MISC FRM APP DTL=================
            stage="MISC FRM APPDTL";
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

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);
            stage="COMPLETE";
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
            logout(driver);
        }
    }

    //------------------------ END DATA ENTRY -------------------------------------------

    //------------------------ MOMO -----------------------------------------------------

    public void runAutomation_momoCreateApp(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO) throws Exception {
        String stage= "";
        Application application= Application.builder().build();

        try {
            stage="INIT DATA";
            String appId="";
            //*************************** GET DATA *********************//
            application = (Application) mapValue.get("ApplicationDTO");
            ApplicationInfoDTO applicationInfoDTO = (ApplicationInfoDTO) mapValue.get("ApplicationInfoDTO");
            LoanDetailsDTO loanDetailsDTO = (LoanDetailsDTO) mapValue.get("LoanDetailsDTO");
            LoanDetailsVapDTO loanDetailsVapDTO = (LoanDetailsVapDTO) mapValue.get("LoanDetailsVapDTO");
            List<ReferenceDTO> referenceDTO = (List<ReferenceDTO>) mapValue.get("ReferenceDTO");
            List<DocumentDTO> documentDTOS = (List<DocumentDTO>) mapValue.get("DocumentDTO");
            MiscFrmAppDtlDTO miscFrmAppDtlDTO = (MiscFrmAppDtlDTO) mapValue.get("MiscFrmAppDtlDTO");
            //*************************** END GET DATA *********************//

            System.out.println(stage + ": DONE" );
            stage="LOGIN FINONE";
            HashMap<String, String> dataControl = new HashMap<>();
            LoginPage loginPage = new LoginPage(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
            loginPage.clickLogin();

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            stage="HOME PAGE";
            HomePage homePage = new HomePage(driver);
            homePage.menuClick();
            homePage.menuChildClick();

            await("Creation Misc2WLPage timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Create Application"));
            LD_ApplicationInfoPage appInfoPage = new LD_ApplicationInfoPage(driver);

            //========== PERSONAL INFORMATION=================
            stage="APPLICATION INFORMATION PAGE - PERSONAL INFORMATION";
            LD_ApplicationInfoPersonalTab personalTab = appInfoPage.getApplicationInfoPersonalTab();
            personalTab.btnCreateClick();

            personalTab.setValue(applicationInfoDTO.getGender(), applicationInfoDTO.getFirstName(),
                    applicationInfoDTO.getMiddleName(), applicationInfoDTO.getLastName(), applicationInfoDTO.getDateOfBirth(),
                    applicationInfoDTO.getPlaceOfIssue(), applicationInfoDTO.getMaritalStatus(), applicationInfoDTO.getNational(), applicationInfoDTO.getEducation());
            Utilities.captureScreenShot(driver);
            personalTab.btnPersonInfoProcessClick();

            System.out.println("PERSONAL BASIC: DONE");

            // personal infomation tab - handle identification section
            stage="APPLICATION INFORMATION PAGE - IDENTIFICATION";
            personalTab.loadIdentificationSection();
            await("Load Identification Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> personalTab.getIdentificationDivElement().isDisplayed());
            personalTab.setIdentificationValue(applicationInfoDTO.getIdentification());
            Utilities.captureScreenShot(driver);
            System.out.println("PERSONAL IDENTIFICATION: DONE");

            // personal infomation tab - handle address section
            stage="APPLICATION INFORMATION PAGE - ADDRESS";
            personalTab.loadAddressSection();
            await("Load Identification Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> personalTab.getAddressDivElement().isDisplayed());

            personalTab.setAddressValue(applicationInfoDTO.getAddress());
            personalTab.loadAddressSection(); // close section after complete input

            Utilities.captureScreenShot(driver);
            System.out.println("PERSONAL ADDRESS: DONE");

            // personal infomation tab - handle family
            if (applicationInfoDTO.getFamily().size() > 0) {
                stage="APPLICATION INFORMATION PAGE - FAMILY";

                personalTab.loadFamilySection();
                await("Load Family Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> personalTab.getFamilyDivElement().isDisplayed());
                personalTab.setFamilyValue(applicationInfoDTO.getFamily());
                Utilities.captureScreenShot(driver);
                System.out.println("PERSONAL FAMILY: DONE");
            }

            // personal infomation tab- handle communication details
            stage="APPLICATION INFORMATION PAGE - COMMUNICATION";
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
            stage="APPLICATION INFORMATION PAGE - CHECK DUPLICATE";
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
            stage="APPLICATION INFORMATION PAGE - EMPLOYMENT DETAIL TAB";
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
                stage="APPLICATION INFORMATION PAGE - FINANCIAL TAB";
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
            stage="LOAN DETAIL PAGE - SOURCING DETAIL TAB";
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
            if (loanDetailsVapDTO != null && loanDetailsVapDTO.getVapProduct()!=null && !loanDetailsVapDTO.getVapProduct().equals("")) {
                stage="LOAN DETAIL PAGE - VAP DETAIL TAB";
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
            stage="REFERENCES PAGE";
            LD_ReferencesPage referencesPage = new LD_ReferencesPage(driver);
            await("Load references tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> referencesPage.getTabReferencesElement().isDisplayed() && referencesPage.getTabReferencesElement().isEnabled());
            referencesPage.getTabReferencesElement().click();
            referencesPage.setData(referenceDTO);
            Utilities.captureScreenShot(driver);
            referencesPage.getSaveBtnElement().click();

            System.out.println("REFERENCES DETAILS: DONE");


            // ==========MISC FRM APP DTL=================
            stage="MISC FRM APPDTL PAGE";
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

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);
            stage="COMPLETE";
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

            System.out.println(stage + "=> MESSAGE: " + e.toString() +"\n TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);

            if (e.getMessage().contains("Work flow failed!!!")) {
                stage="END OF LEAD DETAIL";

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
            if(application.getApplicationId()== null || application.getApplicationId().isEmpty())
            {
                application.setApplicationId("UNKNOW");
            }
            logout(driver);
            LD_updateStatusRabbit(application,"updateAutomation","momo");
        }
    }

    private void LD_updateStatusRabbit(Application application,String func,String project) throws Exception {
//        JsonNode jsonNode= rabbitMQService.sendAndReceive("tpf-service-esb",
//                Map.of("func", "updateAutomation", "token",
//                        String.format("Bearer %s", rabbitMQService.getToken().path("access_token").asText()),"body", Map.of("app_id",application.getApplicationId()!=null ? application.getApplicationId():"",
//                                "project",project,
//                                "automation_result",application.getStatus(),
//                                "reference_id",UUID.randomUUID().toString(),
//                                "description",application.getDescription()!=null?application.getDescription():"",
//                                "transaction_id", application.getLoanDetails().getSourcingDetails().getChassisApplicationNum())));

        JsonNode jsonNode= rabbitMQService.sendAndReceive("tpf-service-esb",
                Map.of("func", "updateAutomation","reference_id",application.getReference_id(),"body", Map.of("app_id",application.getApplicationId()!=null ? application.getApplicationId():"",
                                "project",project,
                                "automation_result",application.getStatus(),
                                "description",application.getDescription()!=null?application.getDescription():"",
                                "transaction_id", application.getLoanDetails().getSourcingDetails().getChassisApplicationNum())));
        System.out.println("rabit:=>" + jsonNode.toString());

    }

    //------------------------ END MOMO -------------------------------------------------

    //------------------------ FPT -----------------------------------------------------
    public void runAutomation_fptCreateApp(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO) throws Exception {
        String stage= "";
        Application application= Application.builder().build();

        try {
            stage="INIT DATA";
            String appId="";
            //*************************** GET DATA *********************//
            application = (Application) mapValue.get("ApplicationDTO");
            ApplicationInfoDTO applicationInfoDTO = (ApplicationInfoDTO) mapValue.get("ApplicationInfoDTO");
            LoanDetailsDTO loanDetailsDTO = (LoanDetailsDTO) mapValue.get("LoanDetailsDTO");
            LoanDetailsVapDTO loanDetailsVapDTO = (LoanDetailsVapDTO) mapValue.get("LoanDetailsVapDTO");
            List<ReferenceDTO> referenceDTO = (List<ReferenceDTO>) mapValue.get("ReferenceDTO");
            List<DocumentDTO> documentDTOS = (List<DocumentDTO>) mapValue.get("DocumentDTO");
            MiscFrmAppDtlDTO miscFrmAppDtlDTO = (MiscFrmAppDtlDTO) mapValue.get("MiscFrmAppDtlDTO");
            MiscFptDTO miscFptDTO=(MiscFptDTO) mapValue.get("MiscFptDTO");
            //*************************** END GET DATA *********************//

            System.out.println(stage + ": DONE" );
            stage="LOGIN FINONE";
            HashMap<String, String> dataControl = new HashMap<>();
            LoginPage loginPage = new LoginPage(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
            loginPage.clickLogin();

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            stage="HOME PAGE";
            HomePage homePage = new HomePage(driver);
            homePage.menuClick();
            homePage.menuChildClick();

            await("Creation Misc2WLPage timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Create Application"));
            LD_ApplicationInfoPage appInfoPage = new LD_ApplicationInfoPage(driver);

            //========== PERSONAL INFORMATION=================
            stage="APPLICATION INFORMATION PAGE - PERSONAL INFORMATION";
            LD_ApplicationInfoPersonalTab personalTab = appInfoPage.getApplicationInfoPersonalTab();
            personalTab.btnCreateClick();

            personalTab.setValue(applicationInfoDTO.getGender(), applicationInfoDTO.getFirstName(),
                    applicationInfoDTO.getMiddleName(), applicationInfoDTO.getLastName(), applicationInfoDTO.getDateOfBirth(),
                    applicationInfoDTO.getPlaceOfIssue(), applicationInfoDTO.getMaritalStatus(), applicationInfoDTO.getNational(), applicationInfoDTO.getEducation());
            Utilities.captureScreenShot(driver);
            personalTab.btnPersonInfoProcessClick();

            System.out.println("PERSONAL BASIC: DONE");

            // personal infomation tab - handle identification section
            stage="APPLICATION INFORMATION PAGE - IDENTIFICATION";
            personalTab.loadIdentificationSection();
            await("Load Identification Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> personalTab.getIdentificationDivElement().isDisplayed());
            personalTab.setIdentificationValue(applicationInfoDTO.getIdentification());
            Utilities.captureScreenShot(driver);
            System.out.println("PERSONAL IDENTIFICATION: DONE");

            // personal infomation tab - handle address section
            stage="APPLICATION INFORMATION PAGE - ADDRESS";
            personalTab.loadAddressSection();
            await("Load Identification Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> personalTab.getAddressDivElement().isDisplayed());

            personalTab.setAddressValue(applicationInfoDTO.getAddress());
            personalTab.loadAddressSection(); // close section after complete input

            Utilities.captureScreenShot(driver);
            System.out.println("PERSONAL ADDRESS: DONE");

            // personal infomation tab - handle family
            if (applicationInfoDTO.getFamily().size() > 0) {
                stage="APPLICATION INFORMATION PAGE - FAMILY";

                personalTab.loadFamilySection();
                await("Load Family Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> personalTab.getFamilyDivElement().isDisplayed());
                personalTab.setFamilyValue(applicationInfoDTO.getFamily());
                Utilities.captureScreenShot(driver);
                System.out.println("PERSONAL FAMILY: DONE");
            }

            // personal infomation tab- handle communication details
            stage="APPLICATION INFORMATION PAGE - COMMUNICATION";
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
            stage="APPLICATION INFORMATION PAGE - CHECK DUPLICATE";
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
            stage="APPLICATION INFORMATION PAGE - EMPLOYMENT DETAIL TAB";
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
                stage="APPLICATION INFORMATION PAGE - FINANCIAL TAB";
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
            stage="LOAN DETAIL PAGE - SOURCING DETAIL TAB";
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
            if (loanDetailsVapDTO != null && loanDetailsVapDTO.getVapProduct()!=null && !loanDetailsVapDTO.getVapProduct().equals("")) {
                stage="LOAN DETAIL PAGE - VAP DETAIL TAB";
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
            stage="REFERENCES PAGE";
            LD_ReferencesPage referencesPage = new LD_ReferencesPage(driver);
            await("Load references tab Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> referencesPage.getTabReferencesElement().isDisplayed() && referencesPage.getTabReferencesElement().isEnabled());
            referencesPage.getTabReferencesElement().click();
            referencesPage.setData(referenceDTO);
            Utilities.captureScreenShot(driver);
            referencesPage.getSaveBtnElement().click();

            System.out.println("REFERENCES DETAILS: DONE");

            // ==========MISC FRM FPT=====================
            stage="MISC FRM FPT";
            LD_MiscFptPage miscFptPage = new LD_MiscFptPage(driver);
            miscFptPage.getTabMiscFptElement().click();
            await("MISC FPT container Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> miscFptPage.getTabMiscFptContainerElement().isDisplayed());
            miscFptPage.setData(miscFptDTO);
            Utilities.captureScreenShot(driver);
            miscFptPage.getBtnSaveElement().click();

            System.out.println("MISC FRM FPT: DONE");

            // ==========MISC FRM APP DTL=================
            stage="MISC FRM APPDTL PAGE";
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

            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);
            stage="COMPLETE";
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

            System.out.println(stage + "=> MESSAGE: " + e.toString() +"\n TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);

            if (e.getMessage().contains("Work flow failed!!!")) {
                stage="END OF LEAD DETAIL";

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
            logout(driver);
            LD_updateStatusRabbit(application,"updateAutomation","fpt");
        }
    }
    //------------------------ END FPT -----------------------------------------------------

    //------------------------ AUTO ASSIGN -----------------------------------------------------
    public void runAutomationDE_autoAssign(WebDriver driver, Map<String, Object> mapValue,String project,String browser) throws Exception {
        String stage= "";
        try {
            stage="INIT DATA";
            //*************************** GET DATA *********************//
            List<AutoAssignDTO> autoAssignDTOList = (List<AutoAssignDTO>) mapValue.get("AutoAssignList");
            //*************************** END GET DATA *********************//
            List<LoginDTO> loginDTOList=new ArrayList<LoginDTO>();

            LoginDTO accountDTONew = null;
            do{
                //get list account finone available
                Query query = new Query();
                query.addCriteria(Criteria.where("active").is(0).and("project").is(project));
                AccountFinOneDTO accountFinOneDTO=mongoTemplate.findOne(query, AccountFinOneDTO.class);
                if (!Objects.isNull(accountFinOneDTO)) {
                    accountDTONew=new LoginDTO().builder().userName(accountFinOneDTO.getUsername()).password(accountFinOneDTO.getPassword()).build();

                    Query queryUpdate = new Query();
                    queryUpdate.addCriteria(Criteria.where("active").is(0).and("username").is(accountFinOneDTO.getUsername()).and("project").is(project));
                    Update update = new Update();
                    update.set("active", 1);
                    AccountFinOneDTO resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, AccountFinOneDTO.class);

                    if(resultUpdate==null)
                    {
                        Thread.sleep(Constant.WAIT_ACCOUNT_GET_NULL);
                        accountDTONew=null;
                    }
                    else {
                        loginDTOList.add(accountDTONew);
                        System.out.println("Get it:" + accountDTONew.toString());
                    }
                } else
                    accountDTONew=null;
            }while (!Objects.isNull(accountDTONew));

            //insert data
            mongoTemplate.insert(autoAssignDTOList, AutoAssignDTO.class);

            if(loginDTOList.size()>0) {
                ExecutorService workerThreadPoolDE = Executors.newFixedThreadPool(loginDTOList.size());

                for (LoginDTO loginDTO: loginDTOList){
                    workerThreadPoolDE.execute(new Runnable () {
                        @Override
                        public void run() {
                            runAutomationDE_autoAssign_run(loginDTO,browser);
//                            runAutomationDE_autoAssign_run2(driver,loginDTO,browser);
                        }
                    });
                }

            }
        } catch (Exception e) {
            System.out.println(stage + "=> MESSAGE " + e.getMessage() +"\n TRACE: " + e.toString());
            e.printStackTrace();
            Utilities.captureScreenShot(driver);
        }
    }

    private void runAutomationDE_autoAssign_run(LoginDTO accountDTO, String browser) {
        WebDriver driver = null;
        Instant start = Instant.now();
        String stage = "";
        System.out.println("START - Auto: " + accountDTO.getUserName() + " - Time: " + Duration.between(start, Instant.now()).toSeconds());
        try {
            selePort = "4444";
            SeleniumGridDriver setupTestDriver = new SeleniumGridDriver(null, browser, fin1URL, null,seleHost,selePort);
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

                        if(resultUpdate==null)
                        {
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

                        System.out.println("Auto: " + accountDTO.getUserName()+ " - FINISH " + " - " + " App: " + autoAssignDTO.getAppid() + " - User: " + autoAssignDTO.getUsername() + " - Time: " + Duration.between(startIn, Instant.now()).toSeconds());

                        // ========= UPDATE DB ============================
                        Query queryUpdate1 = new Query();
                        queryUpdate1.addCriteria(Criteria.where("status").is(2).and("appid").is(autoAssignDTO.getAppid()).and("username").is(autoAssignDTO.getUsername()));
                        Update update1 = new Update();
                        update1.set("userauto", accountDTO.getUserName());
                        update1.set("status", 1);
                        AutoAssignDTO resultUpdate1 = mongoTemplate.findAndModify(queryUpdate1, update1, AutoAssignDTO.class);
                        System.out.println("Auto: " + accountDTO.getUserName()+ " - UPDATE STATUS " + " - " + " App: " + autoAssignDTO.getAppid() + " - User: " + autoAssignDTO.getUsername() + " - Time: " + Duration.between(startIn, Instant.now()).toSeconds());
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
            System.out.println("User Auto:" + accountDTO.getUserName() +" - " + stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);
        } finally {
            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            logout(driver);
            pushAccountToQueue(accountDTO);
        }
    }
    //------------------------ END AUTO ASSIGN -----------------------------------------------------

    //------------------------ RESPONSE_QUERY-----------------------------------------------------
    public void runAutomationDE_responseQuery(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO) throws Exception {
        ResponseAutomationModel responseModel = new ResponseAutomationModel();
        Timestamp date_time = new Timestamp(new Date().getTime());
        Instant start = Instant.now();
        String stage= "";
        DEResponseQueryDTO deResponseQueryDTO = DEResponseQueryDTO.builder().build();
        log.info("{}", deResponseQueryDTO);
        try {
            stage="INIT DATA";
            //*************************** GET DATA *********************//
            deResponseQueryDTO = (DEResponseQueryDTO) mapValue.get("DEResponseQueryList");
            //*************************** END GET DATA *********************//
            Actions actions=new Actions(driver);
            System.out.println(stage + ": DONE" );
            stage="LOGIN FINONE";
            HashMap<String, String> dataControl = new HashMap<>();
            LoginPage loginPage = new LoginPage(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
            loginPage.clickLogin();

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));


            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);


            System.out.println("Auto:" + accountDTO.getUserName() + " - GET DONE " + " - " + " App: " + deResponseQueryDTO.getAppId() + " - User: " + deResponseQueryDTO.getUserName() + " - Time: " + Duration.between(start, Instant.now()).toSeconds());

            stage = "HOME PAGE";
            HomePage homePage = new HomePage(driver);
            //System.out.println("Acc: " + accountDTO.getUserName() + "-" + stage + ": DONE");
            // ========== APPLICATIONS =================
            homePage.getMenuApplicationElement().click();
            stage = "RESPONSE QUERY";

            // ========== RESPONSE QUERY =================
            DE_ReturnRaiseQueryPage de_ReturnRaiseQueryPage = new DE_ReturnRaiseQueryPage(driver);
            de_ReturnRaiseQueryPage.getResponseQueryElement().click();
            de_ReturnRaiseQueryPage.setData(deResponseQueryDTO, downdloadFileURL);
            System.out.println("Auto: " + accountDTO.getUserName()+ " - FINISH " + " - " + " App: " + deResponseQueryDTO.getAppId() + " - User: " + deResponseQueryDTO.getUserName() + " - Time: " + Duration.between(start, Instant.now()).toSeconds());

            // ========= UPDATE DB ============================
            Query queryUpdate1 = new Query();
            queryUpdate1.addCriteria(Criteria.where("status").is(2).and("appId").is(deResponseQueryDTO.getAppId()).and("userName").is(deResponseQueryDTO.getUserName()));
            Update update1 = new Update();
            update1.set("userauto", accountDTO.getUserName());
            update1.set("status", 1);
            System.out.println("Auto: " + accountDTO.getUserName()+ " - UPDATE STATUS " + " - " + " App: " + deResponseQueryDTO.getAppId() + " - User: " + deResponseQueryDTO.getUserName() + " - Time: " + Duration.between(start, Instant.now()).toSeconds());

            responseModel.setRequest_id(deResponseQueryDTO.getReference_id());
            responseModel.setReference_id(UUID.randomUUID().toString());
            responseModel.setDate_time(date_time);
            responseModel.setResult_code(0);
            responseModel.setData(deResponseQueryDTO);

            Utilities.captureScreenShot(driver);

        } catch (Exception e) {
            System.out.println("User Auto:" + accountDTO.getUserName() +" - " + stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();

            responseModel.setRequest_id(deResponseQueryDTO.getReference_id());
            responseModel.setReference_id(UUID.randomUUID().toString());
            responseModel.setDate_time(date_time);
            responseModel.setResult_code(500);
            responseModel.setMessage(e.getMessage());
            responseModel.setData(deResponseQueryDTO);

            Utilities.captureScreenShot(driver);
        } finally {
            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            DEReturn_updateStatusRabbit(responseModel,"updateAutomation");
            logout(driver);
        }
    }
    //------------------------ END RESPONSE_QUERY -----------------------------------------------------

    //------------------------ SALE_QUEUE-----------------------------------------------------
    public void runAutomationDE_saleQueue(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO) throws Exception {
        ResponseAutomationModel responseModel = new ResponseAutomationModel();
        Timestamp date_time = new Timestamp(new Date().getTime());
        Instant start = Instant.now();
        String stage= "";
        DESaleQueueDTO deSaleQueueDTO = DESaleQueueDTO.builder().build();
        log.info("{}", deSaleQueueDTO);
        try {
            stage="INIT DATA";
            //*************************** GET DATA *********************//
            deSaleQueueDTO = (DESaleQueueDTO) mapValue.get("DESaleQueueList");
            //*************************** END GET DATA *********************//
            Actions actions=new Actions(driver);
            System.out.println(stage + ": DONE" );
            stage="LOGIN FINONE";
            HashMap<String, String> dataControl = new HashMap<>();
            LoginPage loginPage = new LoginPage(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
            loginPage.clickLogin();

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));


            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            System.out.println("Auto: " + accountDTO.getUserName() + " - GET DONE " + " - " + " App: " + deSaleQueueDTO.getAppId() + " - User: " + deSaleQueueDTO.getUserName() + " - Time: " + Duration.between(start, Instant.now()).toSeconds());

            stage = "HOME PAGE";
            HomePage homePage = new HomePage(driver);
            //System.out.println("Acc: " + accountDTO.getUserName() + "-" + stage + ": DONE");
            // ========== APPLICATIONS =================
            String lastUpdate = deSaleQueueDTO.getLastUpdate();
            homePage.getMenuApplicationElement().click();

            stage = "SALE QUEUE";
            // ========== SALE QUEUE =================
            DE_ReturnSaleQueuePage de_ReturnSaleQueuePage = new DE_ReturnSaleQueuePage(driver);
            de_ReturnSaleQueuePage.getApplicationElement().click();
            de_ReturnSaleQueuePage.setData(deSaleQueueDTO, downdloadFileURL);

            DE_ReturnApplicationManagerPage de_ReturnApplicationManagerPage = new DE_ReturnApplicationManagerPage(driver);
            for (DESaleQueueDocumentDTO documentList : deSaleQueueDTO.getDataDocument()) {
                if (documentList.getDocumentName().contains("(ACCA)")) {
                    de_ReturnApplicationManagerPage.setData(deSaleQueueDTO.getAppId(), lastUpdate);
                }
                break;
            }

            System.out.println("Auto: " + accountDTO.getUserName()+ " - FINISH " + " - " + " App: " + deSaleQueueDTO.getAppId() + " - User: " + deSaleQueueDTO.getUserName() + " - Time: " + Duration.between(start, Instant.now()).toSeconds());

            // ========= UPDATE DB ============================
            Query queryUpdate1 = new Query();
            queryUpdate1.addCriteria(Criteria.where("status").is(2).and("appId").is(deSaleQueueDTO.getAppId()).and("userName").is(deSaleQueueDTO.getUserName()));
            Update update1 = new Update();
            update1.set("userauto", accountDTO.getUserName());
            update1.set("status", 1);
            System.out.println("Auto: " + accountDTO.getUserName()+ " - UPDATE STATUS " + " - " + " App: " + deSaleQueueDTO.getAppId() + " - User: " + deSaleQueueDTO.getUserName() + " - Time: " + Duration.between(start, Instant.now()).toSeconds());

            responseModel.setRequest_id(deSaleQueueDTO.getReference_id());
            responseModel.setReference_id(UUID.randomUUID().toString());
            responseModel.setDate_time(date_time);
            responseModel.setResult_code(0);
            responseModel.setData(deSaleQueueDTO);

            Utilities.captureScreenShot(driver);

        } catch (Exception e) {
            responseModel.setRequest_id(deSaleQueueDTO.getReference_id());
            responseModel.setReference_id(UUID.randomUUID().toString());
            responseModel.setDate_time(date_time);
            responseModel.setResult_code(500);
            responseModel.setMessage(e.getMessage());
            responseModel.setData(deSaleQueueDTO);

            System.out.println("User Auto:" + accountDTO.getUserName() +" - " + stage + "=> MESSAGE " + e.getMessage() + "\n TRACE: " + e.toString());
            e.printStackTrace();
            Utilities.captureScreenShot(driver);
        } finally {
            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            DEReturn_updateStatusRabbit(responseModel,"updateAutomation");
            logout(driver);
        }
    }
    //------------------------ END DE_SALE_QUEUE -----------------------------------------------------

    //------------------------ START UPDATE RABBITMQ -----------------------------------------------------
    private void DEReturn_updateStatusRabbit(ResponseAutomationModel responseAutomationModel, String func) throws Exception {
        JsonNode jsonNode= rabbitMQService.sendAndReceive("tpf-service-dataentry",
                Map.of("func", func,"body", responseAutomationModel.getData()));
        System.out.println("rabit:=>" + jsonNode.toString());
    }
    //------------------------ END AUTO ASSIGN -----------------------------------------------------

    //------------------------ SMARTNET-----------------------------------------------------
    public void SN_runAutomation_QuickLead(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO) throws Exception {
        Instant start = Instant.now();
        String appId = "";
        String stage= "";
        Application application= Application.builder().build();
        log.info("{}", application);
        try {
            stage="INIT DATA";
            //*************************** GET DATA *********************//
            application = (Application) mapValue.get("ApplicationDTO");
            QuickLead quickLead=application.getQuickLead();
            mongoTemplate.save(application);


            //*************************** END GET DATA *********************//
            Actions actions=new Actions(driver);
            System.out.println(stage + ": DONE" );
            stage="LOGIN FINONE";
            HashMap<String, String> dataControl = new HashMap<>();
            LoginPage loginPage = new LoginPage(driver);
            loginPage.setLoginValue(accountDTO.getUserName(), accountDTO.getPassword());
            loginPage.clickLogin();
            //actions.moveToElement(loginPage.getBtnElement()).click().build().perform();

            await("Login timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("DashBoard"));


            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            stage="HOME PAGE";
            HomePage homePage = new HomePage(driver);


            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            stage="QUICK LEAD";
            // ========== QUICK LEAD =================
            homePage.menuClick();
            homePage.leadQuickClick();

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

            leadDetailPage.setData(quickLead,leadApp,downdloadFileURL);

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
                    leadAppID=e.getText().substring(e.getText().indexOf("APPL"),e.getText().indexOf("APPL") + 12);
                }
            }
            System.out.println("APPID: => " + leadAppID);

            Utilities.captureScreenShot(driver);
            System.out.println(stage + ": DONE" );


            //update thêm phần assign về acc tạo app để tranh rơi vào pool
            stage="APPLICATION MANAGER";
            // ========== APPLICATION MANAGER =================
            homePage.getMenuApplicationElement().click();
            homePage.getApplicationManagerElement().click();
            await("Application Manager timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Manager"));

            DE_ApplicationManagerPage de_applicationManagerPage=new DE_ApplicationManagerPage(driver);

            await("getApplicationManagerFormElement displayed timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(()->de_applicationManagerPage.getApplicationManagerFormElement().isDisplayed());
            de_applicationManagerPage.setData(leadAppID,accountDTO.getUserName());
            System.out.println(stage + ": DONE" );
            Utilities.captureScreenShot(driver);

            //-------------------- END ---------------------------

            application.setApplicationId(leadAppID);

            //UPDATE STATUS
            application.setStatus("Pass Quicklead");
            application.setDescription("Thanh cong");


            Utilities.captureScreenShot(driver);
            //logout(driver);

        } catch (Exception e) {
            //UPDATE STATUS
            application.setStatus("ERROR");
            application.setStage(stage);
            application.setDescription(e.getMessage());

            System.out.println(stage + "=> MESSAGE " + e.getMessage() +"\n TRACE: " + e.toString());
            e.printStackTrace();

            Utilities.captureScreenShot(driver);

            if (e.getMessage().contains("Work flow failed!!!")) {
                stage="END OF LEAD DETAIL";

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
            if(application.getApplicationId()== null || application.getApplicationId().isEmpty() || application.getApplicationId().indexOf("LEAD") > 0 || application.getApplicationId().indexOf("APPL") < 0 )
            {
                application.setApplicationId("UNKNOW");
            }

            Instant finish = Instant.now();
            System.out.println("EXEC: " + Duration.between(start, finish).toMinutes());
            try {
                SN_updateStatusRabbit(application,"updateAutomation","smartnet");
            }catch (Exception e)
            {
                System.out.println(e.toString());
            }
            logout(driver);


        }
    }

    private void SN_updateStatusRabbit(Application application,String func,String project) throws Exception {

        JsonNode jsonNode= rabbitMQService.sendAndReceive("tpf-service-esb",
                Map.of("func", "updateAutomation","reference_id",application.getReference_id(),"body", Map.of("app_id",application.getApplicationId()!=null ? application.getApplicationId():"",
                        "project",project,
                        "automation_result",application.getStatus(),
                        "description",application.getDescription()!=null?application.getDescription():"",
                        "transaction_id", application.getQuickLeadId())));
        System.out.println("rabit:=>" + jsonNode.toString());

    }

    private void SN_updateDB(Application application) throws Exception {

        Query queryUpdate = new Query();
        queryUpdate.addCriteria(Criteria.where("active").is(0).and("username").is(accountFinOneDTO.getUsername()).and("project").is(project));
        Update update = new Update();
        update.set("active", 1);
        update.set("lastModifiedDate", new Date());
        AccountFinOneDTO resultUpdate = mongoTemplate.findAndModify(queryUpdate, update, AccountFinOneDTO.class);

    }
    //------------------------ END SMARTNET-----------------------------------------------------
}
