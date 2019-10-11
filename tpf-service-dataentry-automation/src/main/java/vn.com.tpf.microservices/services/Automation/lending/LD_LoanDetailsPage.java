package vn.com.tpf.microservices.services.Automation.lending;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

@Getter
public class LD_LoanDetailsPage {
    @FindBy(how = How.ID, using = "applicationChildTabs_loanInfo")
    @CacheLookup
    private WebElement tabLoanDetailsElement;

    private LD_LoanDetailsSourcingDetailsTab loanDetailsSourcingDetailsTab;

    public LD_LoanDetailsPage(WebDriver driver) {
    	PageFactory.initElements(driver, this);
        loanDetailsSourcingDetailsTab = new LD_LoanDetailsSourcingDetailsTab(driver);
    }

}
