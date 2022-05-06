package vn.com.tpf.microservices.services.Automation;

import lombok.Data;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.springframework.stereotype.Service;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Data
public class SearchMenu {
    private static WebDriver _driver;

    @FindBy(how = How.ID, using = "menuClick")
    @CacheLookup
    private WebElement MenuClick;

    @FindBy(how = How.ID, using = "textMenuListSearch")
    @CacheLookup
    private WebElement textMenuListSearch;

    @FindBy(how = How.XPATH, using = "//*[@id='searchMenu']/li/a")
    @CacheLookup
    private List<WebElement> listMenuSearch;

    private String textSearch;

    public SearchMenu(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    public void MoveToPage(String textSearch){
        this.getMenuClick().click();
        await("textMenuListSearch Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() ->textMenuListSearch.isDisplayed());
        this.getTextMenuListSearch().sendKeys(textSearch);
        // user do not have permission access menu if time out happen
        await("textMenuListSearch Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() ->listMenuSearch.size() > 0);
        Utilities.captureScreenShot(_driver);
        Utilities.chooseDropdownValue(textSearch,listMenuSearch);

    }
}
