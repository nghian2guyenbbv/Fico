package vn.com.tpf.microservices.services.Automation.autoCRM;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

@Getter
public class CRM_LoanDetailsPage {

    @FindBy(how = How.ID, using = "applicationChildTabs_loanInfo")
    @CacheLookup
    private WebElement tabLoanDetailsElement;

    private CRM_LoanDetailsSourcingDetailsTabPage loanDetailsSourcingDetailsTab;

    public CRM_LoanDetailsPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        loanDetailsSourcingDetailsTab = new CRM_LoanDetailsSourcingDetailsTabPage(driver);
    }

}
