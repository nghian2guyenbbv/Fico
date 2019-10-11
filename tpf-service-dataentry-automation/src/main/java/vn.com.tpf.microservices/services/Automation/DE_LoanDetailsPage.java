package vn.com.tpf.microservices.services.Automation;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

@Getter
public class DE_LoanDetailsPage {
    @FindBy(how = How.ID, using = "applicationChildTabs_loanInfo")
    @CacheLookup
    private WebElement tabLoanDetailsElement;

    private LoanDetailsSourcingDetailsTab loanDetailsSourcingDetailsTab;

    public DE_LoanDetailsPage(WebDriver driver) {
    	PageFactory.initElements(driver, this);
        loanDetailsSourcingDetailsTab = new LoanDetailsSourcingDetailsTab(driver);
    }

}
