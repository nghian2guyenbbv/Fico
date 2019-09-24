package vn.com.tpf.microservices.services;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.tpf.microservices.driver.SeleniumGridDriver;
import vn.com.tpf.microservices.models.Automation.*;
import vn.com.tpf.microservices.models.QuickLead.Application;
import vn.com.tpf.microservices.models.QuickLead.QuickLead;
import vn.com.tpf.microservices.services.Automation.*;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.is;

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
                case "runAutomation_UpdateInfo":
                    runAutomation_UpdateInfo(driver, mapValue, accountDTO);
                    break;
                case "runAutomation_UpdateAppError":
                    runAutomation_UpdateAppError(driver,mapValue,accountDTO);
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
        String appId = "";
        String stage= "";
        Application application= Application.builder().build();
        try {
            stage="INIT DATA";
            //*************************** GET DATA *********************//
            application = (Application) mapValue.get("ApplicationDTO");
            QuickLead quickLead=application.getQuickLead();

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

            application.setApplicationId(leadAppID);

            //UPDATE STATUS
            application.setStatus("OK");
            application.setDescription("Thanh cong");

            homePage.getMenuApplicationElement().click();
            homePage.getApplicationElement().click();
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
            updateStatusRabbit(application,"updateAutomation");
            logout(driver);
        }
    }

    public void runAutomation_UpdateInfo(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO) throws Exception {
        String stage= "";
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
            homePage.getApplicationElement().click();

            await("Application Grid timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Grid"));

            ApplicationGridPage applicationGridPage=new ApplicationGridPage(driver);
            applicationGridPage.setData(leadAppID);
            Utilities.captureScreenShot(driver);

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
            if (loanDetailsVapDTO != null && !loanDetailsVapDTO.getVapProduct().equals("")) {
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

                documentsPage.updateData(documentDTOS);
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
            updateStatusRabbit(application,"updateFullApp");
            logout(driver);
        }
    }

    public void runAutomation_UpdateAppError(WebDriver driver, Map<String, Object> mapValue, LoginDTO accountDTO) throws Exception {
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
                    updateAppError_EndLeadDetail(driver,stageError,accountDTO,leadAppID,mapValue);
                    break;
                case "PERSONAL INFORMATION":
                    updateAppError_PersonalInformation(driver,stageError,accountDTO,leadAppID,mapValue);
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
            updateStatusRabbit(application,"updateAppError");
            System.out.println(stage);
            logout(driver);
        }
    }

    private void updateStatusRabbit(Application application,String func) throws Exception {
        rabbitMQService.send("tpf-service-dataentry",
                Map.of("func", func, "token",
                        String.format("Bearer %s", rabbitMQService.getToken().path("access_token").asText()),"body", application));

    }

    private void updateAppError_EndLeadDetail(WebDriver driver, String stage, LoginDTO accountDTO, String leadAppID, Map<String, Object> mapValue) throws Exception {
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
            // ========== APPLICATIONS =================
            homePage.getMenuApplicationElement().click();
            homePage.getApplicationElement().click();

            await("Application Grid timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Grid"));

            ApplicationGridPage applicationGridPage = new ApplicationGridPage(driver);
            applicationGridPage.updateData(leadAppID);
            Utilities.captureScreenShot(driver);

            // ========== LEAD DETAILS (DATA ENTRY) =================
            LeadDetailDEPage leadDetailDEPage= new LeadDetailDEPage(driver);
            leadDetailDEPage.setData(leadAppID);
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
            await("modalConfirmElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getModalConfirmDeleteVapNextElement().size() > 0);

            await("modalBtnConfirmElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getModalConfirmDeleteVapNextElement().get(1).findElement(By.id("confirmDeleteVapNext")).isDisplayed());

            System.out.println(loanDetailsSourcingDetailsTab.getModalConfirmDeleteVapNextElement().size());

            Actions actions = new Actions(driver);

            await("getBtnConfirmDeleteVapNextElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElement().size() > 0);

            System.out.println(loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElement().size());

            await("getBtnConfirmDeleteVapNextElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElement().get(1).isDisplayed());

            loanDetailsSourcingDetailsTab.getBtnConfirmDeleteVapNextElement().get(1).click();

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
            // ========== APPLICATIONS =================
            homePage.getMenuApplicationElement().click();
            homePage.getApplicationElement().click();

            await("Application Grid timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(driver::getTitle, is("Application Grid"));

            ApplicationGridPage applicationGridPage = new ApplicationGridPage(driver);
            applicationGridPage.updateData(leadAppID);
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
            if (loanDetailsVapDTO != null && !loanDetailsVapDTO.getVapProduct().equals("")) {
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

                documentsPage.updateData(documentDTOS);
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
            updateStatusRabbit(application, "updateAppError");
            System.out.println(stage);
            logout(driver);
        }
    }

}
