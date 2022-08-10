package vn.com.tpf.microservices.services.Automation.AutomationPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

public class DocumentPage {
    public String checkCurrrentStatus(WebDriver _driver){
        WebElement editViewDoc = _driver.findElement(By.xpath("//div[contains(@class,'inputBox clearfix ng-scope')]//select[@title='Status']"));
        Select selectStatus = new Select(editViewDoc);
        WebElement e = selectStatus.getFirstSelectedOption();
        System.out.println("checkCurrrentStatus: "+e.getText());
        return e.getText();
    }

    public void changeDocumentStatus(WebDriver _driver, String status) throws InterruptedException {
        Actions actions = new Actions(_driver);
        _driver.findElement(By.xpath("//div[contains(@class,'inputBox clearfix ng-scope')]//select[@title='Status']")).click();
        List<WebElement> lendingPhotoContainerElement = _driver.findElements(By.xpath("//div[contains(@class,'inputBox clearfix ng-scope')]//select[@title='Status']//option"));
        await("Load lendingPhotoContainerElement Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> lendingPhotoContainerElement.size() != 0);

        {
            Utilities.chooseDropdownValue(status, lendingPhotoContainerElement);
            System.out.println("change Status to: " + status);
        }


    }
}
