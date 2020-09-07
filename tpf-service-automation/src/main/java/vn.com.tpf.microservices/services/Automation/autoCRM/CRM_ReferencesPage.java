package vn.com.tpf.microservices.services.Automation.autoCRM;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.springframework.util.StringUtils;
import vn.com.tpf.microservices.models.AutoCRM.CRM_ReferencesListDTO;
import vn.com.tpf.microservices.models.Automation.ReferenceDTO;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Getter
public class CRM_ReferencesPage {

    private WebDriver _driver;
    @FindBy(how = How.ID, using = "applicationChildTabs_referencesInfo")
    @CacheLookup
    private WebElement tabReferencesElement;

    //references_details_table_wrapper
    @FindBy(how = How.ID, using = "referenceDetailList")
    @CacheLookup
    private WebElement tabReferencesContainerElement;

    @FindBy(how = How.XPATH, using = "//input[contains(@id, 'customer_references_name_')]")
    @CacheLookup
    private List<WebElement> fullNameElement;

    @FindBy(how = How.ID, using = "create_new_reference_detail_row")
    @CacheLookup
    private WebElement btnCreateNewRowElement;

    @FindBy(how = How.XPATH, using = "//button[@class='saveBtnHkeys btn btn-primary']")
    @CacheLookup
    private WebElement saveBtnElement;

    //----------------------------UPDATE---------------------------------
    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'references_details_table_wrapper')]//*[contains(@id,'DeleteReferences')]")
    @CacheLookup
    private List<WebElement> deleteElement;

    @FindBy(how = How.ID, using = "references_details_table")
    @CacheLookup
    private WebElement tableReferencesElement;

    public CRM_ReferencesPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    public void setData(List<CRM_ReferencesListDTO> datas) throws IOException {

        await("tableReferencesElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> tableReferencesElement.isDisplayed());

        int index = 0;
        for (CRM_ReferencesListDTO data : datas) {
            _driver.findElement(By.id("customer_references_name_"+ index)).sendKeys(data.getFullName());

            WebElement relationship = _driver.findElement(By.id("customer_references_relationship_" + index + "_chzn"));
            relationship.click();
            List<WebElement> relationships = _driver.findElements(By.xpath("//*[contains(@id, 'customer_references_relationship_" + index + "_chzn_o_')]"));
            await("relationships loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> relationships.size() > 0);
            Utilities.chooseDropdownValue(data.getRelationShip(), relationships);

            WebElement mobilePhone = _driver.findElement(By.id("mobilenumber_" + index + "_phoneNumber"));
            mobilePhone.sendKeys(data.getMobilePhoneNumber());

            //update them nhap primary phone
//            if(data.getPriNumber()!=null&&!StringUtils.isEmpty(data.getPriNumber()))
//            {
//                WebElement priNumber = _driver.findElement(By.id("phoneNumber_phoneNumber_" + index));
//                priNumber.sendKeys(data.getPriNumber());
//
//                WebElement stdNumber = _driver.findElement(By.id("stdCode_phoneNumber_" + index));
//                stdNumber.sendKeys(data.getPriStd());
//
//                WebElement extNumber = _driver.findElement(By.id("extension_phoneNumber_" + index));
//                extNumber.sendKeys(data.getPriExt());
//            }

            if (index < datas.size() - 1) {
                await("Btn Add Reference not enabled - Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> btnCreateNewRowElement.isEnabled());
                btnCreateNewRowElement.click();
            }
            index++;
        }
    }

}
