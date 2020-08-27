package vn.com.tpf.microservices.services.Automation.autoAllocation;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import vn.com.tpf.microservices.models.AutoAllocation.AutoAllocationDTO;

import java.util.List;

@Getter
public class AutoAllocationReassignPage {
    private WebDriver _driver;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]")
    private WebElement menuApplicationsElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@class,'applications-li')]//div[contains(@class,'one-col')][2]//li[1]//span[contains(text(),'Applications')]")
    @CacheLookup
    private WebElement menuApplicationsLiElement;

    @FindBy(how = How.ID, using = "searchOptions")
    @CacheLookup
    private WebElement searchOptionsElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'LoanApplication_Assigned_filter']//select[@id = 'searchOptions']//option")
    @CacheLookup
    private List<WebElement> searchOptionsListElement;

    @FindBy(how = How.XPATH, using = "//div[contains(@id,'LoanApplication_Assigned_wrapper')]//div[contains(@id,'LoanApplication_Assigned_filter')]//input[contains(@type,'text')]")
    @CacheLookup
    private WebElement searchTextsElement;

    @FindBy(how = How.XPATH, using = "//table[@id='LoanApplication_Assigned']//tbody//tr//td")
    private List<WebElement> tableSearchApplicationElement;

    @FindBy(how = How.XPATH, using = "//table[@id='LoanApplication_Assigned']//tbody//tr//td//input[@id = 'selectThis_LoanApplication_Assigned']")
    private List<WebElement> checkboxApplicationAssignedElement;

//    @FindBy(how = How.XPATH, using = "//div[@id = 'commonButton_LoanApplication_Assigned']//button[@id = 'ReassignForUserLoanApplication_AssignedBttn']")
//    @FindBy(how = How.XPATH, using = "(//table[@id='LoanApplication_Assigned']//tbody//ancestor::tr[contains(@class, 'bg-skyBlue_imp')]//td//img[@id = 'ReassignForUser'])[1]")
    @FindBy(how = How.ID, using = "ReassignForUserLoanApplication_AssignedBttn")
    private WebElement btnReassignForUserElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'dialog-form-users' and @aria-hidden = 'false']")
    private WebElement dialogFormUsersElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'dialog-form-users' and @aria-hidden = 'false']//div[@id = 'example']//table//tbody[@id = 'searchData']//tr//td")
    private List<WebElement> tableUserAssignedElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'dialog-form-users' and @aria-hidden = 'false']//a[contains(text(), 'Done')]")
    private WebElement btnDoneDialogFormUsersElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'reason-form-reassign' and @aria-hidden = 'false']")
    private WebElement dialogDecisionReasonElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'reason-form-reassign' and @aria-hidden = 'false']//div[@id = 'decisionReason']//select[@id = 'reasonSelect-reassign']")
    private WebElement selectDecisionReasonElement;

    @FindBy(how = How.XPATH, using = "//div[@id = 'reason-form-reassign' and @aria-hidden = 'false']//a[contains(text(),'Done')]")
    private WebElement btnDoneDecisionReasonForReassignElement;

    @FindBy(how = How.XPATH, using = " //table[@id='LoanApplication_Assigned']//tbody//ancestor::tr[contains(@class, 'bg-skyBlue_imp')]//td[2]")
    private List<WebElement> listApplicationAssignedElement;

    public AutoAllocationReassignPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    public void setData(AutoAllocationDTO autoAllocationDTO) {

    }

}
