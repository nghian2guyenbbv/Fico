package vn.com.tpf.microservices.services.Automation.autoCRM;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.Getter;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import vn.com.tpf.microservices.models.AutoCRM.*;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Getter
public class CRM_ApplicationInfoPersonalTab {

    private WebDriver _driver;
    @FindBy(how = How.ID, using = "create_new_applicant")
    @CacheLookup
    private WebElement btnCreateNewUserElement;

    @FindBy(how = How.ID, using = "genderType_new_chzn")
    @CacheLookup
    private WebElement genderSelectElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'genderType_new_chzn_o_')]")
    @CacheLookup
    private List<WebElement> genderSelectOptionElement;

    @FindBy(how = How.ID, using = "placeOfBirth")
    @CacheLookup
    private WebElement placeOfBirthElement;

    @FindBy(how = How.ID, using = "firstName")
    @CacheLookup
    private WebElement firstNameElement;

    @FindBy(how = How.ID, using = "middleName")
    @CacheLookup
    private WebElement middleNameElement;

    @FindBy(how = How.ID, using = "lastName")
    @CacheLookup
    private WebElement lastNameElement;

    @FindBy(how = How.ID, using = "dob")
    @CacheLookup
    private WebElement dateOfBirthdaElement;

    @FindBy(how = How.ID, using = "maritalStatus_chzn")
    @CacheLookup
    private WebElement maritalStatusElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'maritalStatus_chzn_o_')]")
    @CacheLookup
    private List<WebElement> maritalStatusOptionElement;

    @FindBy(how = How.ID, using = "nationality_chzn")
    @CacheLookup
    private WebElement nationalElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'nationality_chzn_o_')]")
    @CacheLookup
    private List<WebElement> nationalOptionElement;

    @FindBy(how = How.ID, using = "category_chzn")
    @CacheLookup
    private WebElement educationElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'category_chzn_o_')]")
    @CacheLookup
    private List<WebElement> educationOptionElement;

    @FindBy(how = How.ID, using = "personInfo_proceed")
    @CacheLookup
    private WebElement btnPersonInfoProcessElement;

    // Identification
    @FindBy(how = How.ID, using = "loadIdentification")
    @CacheLookup
    private WebElement btnLoadIdentificationElement;

    @FindBy(how = How.ID, using = "identification")
    @CacheLookup
    private WebElement identificationDivElement;

    @FindBy(how = How.XPATH, using = "//input[@class='btn btn-inverse']")
    @CacheLookup
    private WebElement btnAddIdentElement;

    // Address
    @FindBy(how = How.ID, using = "address")
    private WebElement formAddressEditElement;

    @FindBy(how = How.ID, using = "loadaddress")
    @CacheLookup
    private WebElement btnLoadAddressElementElement;

    @FindBy(how = How.ID, using = "address_detail_body")
    @CacheLookup
    private WebElement addressDivElement;

    @FindBy(how = How.ID, using = "addressType_chzn")
    private WebElement addressTypeElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'addressType_chzn_o_')]")
    private List<WebElement> addressTypeOptionElement;

    @FindBy(how = How.ID, using = "text_country_chzn")
    private WebElement textCountryElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'text_country_chzn_o_')]")
    private List<WebElement> textCountryOptionElement;

    @FindBy(how = How.ID, using = "state_country_chzn")
    private WebElement regionElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'state_country_chzn_o_')]")
    private List<WebElement> regionOptionElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'state_country_chzn')]//input")
    private WebElement regionInputElement;

    @FindBy(how = How.ID, using = "city_country_chzn")
    private WebElement cityElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'city_country_chzn_o_')]")
    private List<WebElement> cityOptionElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'city_country_chzn')]//input")
    private WebElement cityInputElement;

    @FindBy(how = How.ID, using = "Text_postalCode_country")
    private WebElement pinCodeElement;

    @FindBy(how = How.ID, using = "listitem_postalCode_country0a")
    private WebElement pinCodeValueElement;

    @FindBy(how = How.ID, using = "area_country_chzn")
    private WebElement areaElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'area_country_chzn_o_')]")
    private List<WebElement> areaOptionElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'area_country_chzn')]//input")
    private WebElement areaInputElement;

    @FindBy(how = How.ID, using = "address1ToBeAddedInput_country")
    private WebElement address1Element;

    @FindBy(how = How.ID, using = "address2ToBeAdded_country")
    private WebElement address2Element;

    @FindBy(how = How.ID, using = "address3ToBeAdded_country")
    private WebElement address3Element;

    @FindBy(how = How.ID, using = "address_landmark")
    private WebElement addressLandmark;

    @FindBy(how = How.ID, using = "address_noOfYearsAtCurrentAdress")
    private WebElement currentAddrYearsElement;

    @FindBy(how = How.ID, using = "address_noOfMonthsAtCurrentAdress")
    private WebElement currentAddrMonthsElement;

    @FindBy(how = How.ID, using = "address_yearsInCurrentCity")
    private WebElement currentCityYearsElement;

    @FindBy(how = How.ID, using = "address_monthsInCurrentCity")
    @CacheLookup
    private WebElement currentCityMonthsElement;

    @FindBy(how = How.XPATH, using = "//*[@id='phoneNumberList1_phoneNumber']")
    private WebElement mobilePhoneNumberElement;

    @FindBy(how = How.XPATH, using = "//*[@id='stdCode_phoneNumberList_new1']")
    private WebElement primaryStdElement;

    @FindBy(how = How.XPATH, using = "//*[@id='phoneNumber_phoneNumberList_new1']")
    private WebElement primaryNumberElement;

    @FindBy(how = How.XPATH, using = "//*[@id='extension_phoneNumberList_new1']")
    private WebElement primaryEXTNElement;

    @FindBy(how = How.ID, using = "create_another_Address")
    private WebElement cbCreateAnotherElement;

    @FindBy(how = How.XPATH, using = "//*[@id='save_cust_address']")
    private WebElement btnSaveAddressElement;

    @FindBy(how = How.ID, using = "create_new")
    private WebElement btnCreateAnotherElement;

    @FindBy(how = How.CLASS_NAME, using = "DedupeButton")
    private WebElement btnCheckDuplicateElement;

    @FindBy(how = How.ID, using = "dedupeCheckButton")
    private WebElement btnCheckDuplicateUpdateElement;

    @FindBy(how = How.ID, using = "noOfMatchesFound")
    private WebElement numDuplicateElement;

    // Communication Details
    @FindBy(how = How.ID, using = "loadCust_comm")
    @CacheLookup
    private WebElement loadCommunicationDetailElement;

    @FindBy(how = How.ID, using = "communiation_details")
    @CacheLookup
    private WebElement communicationDetailDivElement;

    @FindBy(how = How.CLASS_NAME, using = "primaryAddCheckBox")
    @CacheLookup
    private List<WebElement> checkBoxPrimaryAddressElements;

    @FindBy(how = How.XPATH, using = "//*[@id='communicationList']/tbody/tr[td[4]='Current Address']/td[7]/input")
    @CacheLookup
    private WebElement cbPrimaryAddessElement;

    @FindBy(how = How.ID, using = "emailAddress0")
    @CacheLookup
    private WebElement primaryEmailElement;

    @FindBy(how = How.ID, using = "customer_mobile_phonen1_phoneNumber")
    @CacheLookup
    private WebElement cusMobileElements;


    // Family
    @FindBy(how = How.ID, using = "loadFamily")
    private WebElement loadFamilyElement;

    @FindBy(how = How.ID, using = "family")
    private WebElement familyDivElement;

    @FindBy(how = How.ID, using = "noOfDependents")
    @CacheLookup
    private WebElement numOfDependentsElement;

    @FindBy(how = How.ID, using = "noOfChildren")
    @CacheLookup
    private WebElement numOfChildElement;

    // TODO: will be change to dynamic element
    @FindBy(how = How.ID, using = "fmDetail_memberName0")
    @CacheLookup
    private WebElement memberNameElement;

    @FindBy(how = How.ID, using = "fmDetail_relationshipType0")
    @CacheLookup
    private WebElement relationshipTypeElement;

    @FindBy(how = How.ID, using = "fmDetail_phoneNumber0")
    @CacheLookup
    private WebElement phoneNumberElement;

    @FindBy(how = How.ID, using = "fmDetail_eduStatus0")
    @CacheLookup
    private WebElement educationStatusElement;

    @FindBy(how = How.ID, using = "fmDetail_occupation0")
    @CacheLookup
    private WebElement comNameElement;

    @FindBy(how = How.ID, using = "fmDetail_isDependent0")
    @CacheLookup
    private WebElement IsDependentElement;

    // end page
    @FindBy(how = How.ID, using = "saveAndNextCustFormButton1")
    @CacheLookup
    private WebElement saveAndNextElement;

    //update for DE
    @FindBy(how = How.ID, using = "personal_customer_details")
    @CacheLookup
    private WebElement personalCustomerDetailsElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'addressGrid')]")
    @CacheLookup
    private WebElement addressGridElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'editTag')]")
    @CacheLookup
    private WebElement editTadAddressElement;

    //----------------------------- UPDATE PAGE -------------------------------
    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'DeleteIdDetails')]")
    @CacheLookup
    private List<WebElement> deleteIdDetailElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'deleteTag')]")
    @CacheLookup
    private List<WebElement> deleteTagElement;


    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'viewTag')]")
    @CacheLookup
    private List<WebElement> viewTagElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id, 'address_details_Table_wrapper')]//tbody//tr")
    @CacheLookup
    private List<WebElement> trAddressListElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id,'address')]//input[contains(@placeholder,'Mobile Phone')]")
    private WebElement updateMobilePhoneNumberElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id,'address')]//input[contains(@placeholder,'STD')]")
    private WebElement updatePrimarySTDElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id,'address')]//input[contains(@placeholder,'NUMBER')]")
    private WebElement updatePrimaryNumberElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id,'address')]//input[contains(@placeholder,'EXTN')]")
    private WebElement updatePrimaryExtElement;


    //------------------------- update them
    @FindBy(how = How.XPATH, using = "//a[contains(@title,'Add Alternate Mobile Number')]")
    private WebElement addAlternateMobileElement;

    @FindBy(how = How.XPATH, using = "//*[contains(@id,'dynamic_addedPNO')]//input[contains(@placeholder,'Mobile Phone')]")
    private List<WebElement> listMobileAlternateElement;


    public CRM_ApplicationInfoPersonalTab(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    public void setValue(CRM_ApplicationInformationsListDTO applicationInfoDTO) throws IOException {
        this.genderSelectElement.click();
        await("genderSelectOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> genderSelectOptionElement.size() > 0);
        Utilities.chooseDropdownValue(applicationInfoDTO.getGender(), genderSelectOptionElement);

        this.firstNameElement.clear();
        this.firstNameElement.sendKeys(applicationInfoDTO.getFirstName());
        this.middleNameElement.clear();
        this.middleNameElement.sendKeys(applicationInfoDTO.getMiddleName());
        this.lastNameElement.clear();
        this.lastNameElement.sendKeys(applicationInfoDTO.getLastName());
        this.dateOfBirthdaElement.clear();
        this.dateOfBirthdaElement.sendKeys(applicationInfoDTO.getDateOfBirth());
        this.placeOfBirthElement.clear();
        this.placeOfBirthElement.sendKeys(applicationInfoDTO.getPlaceOfIssue());

        this.maritalStatusElement.click();
        await("maritalStatusOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> maritalStatusOptionElement.size() > 0);
        Utilities.chooseDropdownValue(applicationInfoDTO.getMaritalStatus(), maritalStatusOptionElement);

        this.nationalElement.click();
        await("nationalOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> nationalOptionElement.size() > 0);
        Utilities.chooseDropdownValue(applicationInfoDTO.getNational(), nationalOptionElement);

        this.educationElement.click();
        await("educationOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> educationOptionElement.size() > 0);
        Utilities.chooseDropdownValue(applicationInfoDTO.getEducation(), educationOptionElement);

        //btnPersonInfoProcessClick(); -- update ko co nút này click

        loadIdentificationSection();
        await("Load identificationDivElement Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> identificationDivElement.isDisplayed());
        setIdentificationValue(applicationInfoDTO.getIdentification());

        loadAddressSection();
        await("Load addressGridElement Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> addressGridElement.isDisplayed());

        //click edit
        await("editTadAddressElement Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> editTadAddressElement.isDisplayed());
        Actions actions = new Actions(_driver);
        editTadAddressElement.click();

        await("formAddressEditElement Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> formAddressEditElement.isDisplayed());

        Utilities.captureScreenShot(_driver);
        //editTadAddressElement.click();
        setAddressValue(applicationInfoDTO.getAddress());
        loadAddressSection();

        loadCommunicationSection();
        await("Load Communication details Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> communicationDetailDivElement.isDisplayed());
        selectPrimaryAddress( applicationInfoDTO.getCommunicationDetails().getPrimaryAddress());//default la Current Address
        await("emailPrimary loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> primaryEmailElement.isDisplayed());
        getPrimaryEmailElement().sendKeys(applicationInfoDTO.getEmail());


        //update them nếu chon loai dia chi lien lac khác current thì vẩn nhap số current, luc nao so mobile cung lay cua current
        if(applicationInfoDTO.communicationDetails.getPhoneNumbers()!=null && applicationInfoDTO.communicationDetails.getPhoneNumbers().size()>=1
                && !applicationInfoDTO.communicationDetails.getPrimaryAddress().equals("Current Address"))
        {
            cusMobileElements.clear();
            cusMobileElements.sendKeys(applicationInfoDTO.communicationDetails.getPhoneNumbers().get(0).getPhoneNumber());
        }

        //check nhap them so dien thoai thu 2, update them, luc nao so mobile cung lay cua current
        if(applicationInfoDTO.communicationDetails.getPhoneNumbers()!=null && applicationInfoDTO.communicationDetails.getPhoneNumbers().size()>=2)
        {
            addAlternateMobileElement.click();

            await("listMobileAlternateElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> listMobileAlternateElement.size() > 0);

            listMobileAlternateElement.get(0).sendKeys(applicationInfoDTO.communicationDetails.getPhoneNumbers().get(1).getPhoneNumber());
            Utilities.captureScreenShot(_driver);
        }

        loadCommunicationSection(); // close section after complete input

        await("Button check address duplicate not enabled").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> btnCheckDuplicateElement.isEnabled());

        btnCheckDuplicateElement.click();

        await("numDuplicateElement not enabled").atMost(120, TimeUnit.SECONDS)
                .until(() -> StringUtils.isNotEmpty(numDuplicateElement.getText()));

        saveAndNext();
    }

    public void loadIdentificationSection() {
        this.btnLoadIdentificationElement.click();
    }

    public void setIdentificationValue(List<CRM_IdentificationsListDTO> datas) throws JsonParseException, JsonMappingException, IOException {
        int index = 0;
        WebElement type;
        WebElement country;
        for (CRM_IdentificationsListDTO data : datas) {
            type = _driver.findElement(By.id("idDetail_identificationType" + index));
            new Select(type).selectByVisibleText(data.getDocumentType());

            _driver.findElement(By.id("idDetail_identificationNumber" + index)).clear();
            _driver.findElement(By.id("idDetail_identificationNumber" + index)).sendKeys(data.getDocumentNumber());
            _driver.findElement(By.id("idDetail_issueDate" + index)).clear();
            _driver.findElement(By.id("idDetail_issueDate" + index)).sendKeys(data.getIssueDate());
            _driver.findElement(By.id("idDetail_expiryDate" + index)).clear();
            _driver.findElement(By.id("idDetail_expiryDate" + index)).sendKeys(data.getExpirationDate());
            country = _driver.findElement(By.id("idDetail_country" + index));
            new Select(country).selectByVisibleText(data.getCountryOfIssue());
            if (index < datas.size() - 1) {
                await("Btn Add Element not enabled Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> btnAddIdentElement.isEnabled());
                btnAddIdentElement.click();
                index++;
            }
        }
    }

    public void loadAddressSection() {
        JavascriptExecutor jse2 = (JavascriptExecutor) _driver;
        jse2.executeScript("arguments[0].click();", btnLoadAddressElementElement);
//        this.btnLoadAddressElementElement.click();
    }

    public void loadCommunicationSection() {
        this.loadCommunicationDetailElement.click();
    }

    public void loadFamilySection() {
        await("loadFamilyElement visibale Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> loadFamilyElement.isDisplayed());
        this.loadFamilyElement.click();
    }

    public void selectPrimaryAddress(String type) {
        if(_driver.findElements(By.xpath("//*[@id='communicationList']/tbody/tr[td[4]='"+ type +"']/td[7]/input")).size()>0)
        {
            Utilities.captureScreenShot(_driver);
            WebElement webElement=_driver.findElement(By.xpath("//*[@id='communicationList']/tbody/tr[td[4]='"+ type +"']/td[7]/input"));
            webElement.click();
            Utilities.captureScreenShot(_driver);
        }
    }

    public void setAddressValue(List<CRM_AddressListDTO> datas) throws JsonParseException, JsonMappingException, IOException {
        int index = 0;
        Actions actions = new Actions(_driver);
        Utilities.captureScreenShot(_driver);
        for (CRM_AddressListDTO data : datas) {

            if(index!=0) {
                await("btnCreateAnotherElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> btnCreateAnotherElement.isEnabled());
                actions.moveToElement(btnCreateAnotherElement).click().build().perform();
            }


            await("addressDivElement display Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> addressDivElement.isDisplayed());
            mobilePhoneNumberElement.click();
            await("textCountryElement not enabled Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> addressTypeElement.isDisplayed());

            actions.moveToElement(addressTypeElement).click().build().perform();
            await("addressTypeOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> addressTypeOptionElement.size() > 1);
            for (WebElement element : addressTypeOptionElement) {
                if (element.getText().equals(data.getAddressType())) {
                    element.click();
                    break;
                }
            }

            actions.moveToElement(textCountryElement).click().build().perform();
            await("textCountryOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> textCountryOptionElement.size() > 1);
            for (WebElement element : textCountryOptionElement) {
                if (element.getText().equals(data.getCountry())) {
                    element.click();
                    break;
                }
            }

//			Select countrySelect = new Select(textCountryElement);
//			await("Country loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//			.until(() -> countrySelect.getOptions().size() > 0);
//			countrySelect.selectByVisibleText(data.getCountry());

            actions.moveToElement(regionElement).click().build().perform();
            await("regionOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> regionOptionElement.size() > 1);
//            for (WebElement element : regionOptionElement) {
//                if (element.getText().equals(data.getRegion())) {
//                    element.click();
//                    break;
//                }
//            }

            //update lai chỗ này để load lại đia chỉ lần đầu tiên cho quicklead, nó ko load lai data nếu ko reload lai region
            regionInputElement.sendKeys("Select");
            regionInputElement.sendKeys(Keys.ENTER);

            actions.moveToElement(regionElement).click().build().perform();
            regionInputElement.sendKeys(data.getRegion());
            regionInputElement.sendKeys(Keys.ENTER);
            //end update

//			Select regionSelect = new Select(regionElement);
//			await("Region loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//			.until(() -> regionSelect.getOptions().size() > 0);
//			regionSelect.selectByVisibleText(data.getRegion());

            actions.moveToElement(cityElement).click().build().perform();
            await("cityOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> cityOptionElement.size() > 1);
//            for (WebElement element : cityOptionElement) {
//                if (element.getText().equals(data.getCity())) {
//                    element.click();
//                    break;
//                }
//            }
            cityInputElement.sendKeys(data.getCity().toUpperCase());
            cityInputElement.sendKeys(Keys.ENTER);

//            Select citySelect = new Select(cityElement);
//            await("City loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> citySelect.getOptions().size() > 0);
//            citySelect.selectByVisibleText(data.getCity());

            pinCodeElement.sendKeys("%%%");
            await("Pincode loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> pinCodeValueElement.isDisplayed());
            actions.moveToElement(pinCodeValueElement).click().build().perform();

            actions.moveToElement(areaElement).click().build().perform();
            await("areaOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> areaOptionElement.size() > 1);

//            for (WebElement element : areaOptionElement) {
//                if (element.getText().equals(data.getArea())) {
//                    element.click();
//                    break;
//                }
//            }

            // sua bang cach nhap enter, hoa thuong deu dc
            areaInputElement.sendKeys(data.getArea().toUpperCase());
            areaInputElement.sendKeys(Keys.ENTER);


//            Select areaSelect = new Select(areaElement);
//            await("Area loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> areaSelect.getOptions().size() > 0);
//            areaSelect.selectByVisibleText(data.getArea());

            address1Element.sendKeys(data.getBuilding());
            address2Element.sendKeys(data.getHouse());
            address3Element.sendKeys(data.getWard());


            //update them landmard
            addressLandmark.clear();
            addressLandmark.sendKeys(data.getAddressLandmark());

            currentAddrYearsElement.sendKeys(data.getResidentDurationYear());
            currentAddrMonthsElement.sendKeys(data.getResidentDurationMonth());
//            currentCityYearsElement.sendKeys(data.getCityDurationYear());
//            currentCityMonthsElement.sendKeys(data.getCityDurationMonth());


            //dien so dien thoan ban neu co
            primaryStdElement.clear();
            primaryStdElement.sendKeys(data.getPriStd());
            primaryNumberElement.clear();
            primaryNumberElement.sendKeys(data.getPriNumber());
            primaryEXTNElement.clear();
            primaryEXTNElement.sendKeys(data.getPriExt());

            mobilePhoneNumberElement.sendKeys(data.getMobilePhone());
            Utilities.captureScreenShot(_driver);
            actions.moveToElement(btnSaveAddressElement).click().build().perform();
            if (index < datas.size() - 1) {
//            	actions.moveToElement(cbCreateAnotherElement).click().build().perform();
//                actions.moveToElement(btnSaveAddressElement).click().build().perform();
                await("btnCreateAnotherElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> btnCreateAnotherElement.isEnabled());
                actions.moveToElement(btnCreateAnotherElement).click().build().perform();
                //TODO: temporarily wait by webdriver
//            	new WebDriverWait(_driver, Constant.TIME_OUT_S).until(ExpectedConditions.alertIsPresent());
//            	_driver.switchTo().alert().accept();
            }
            index++;
            Utilities.captureScreenShot(_driver);
        }
    }

    public void saveAndNext() {
        this.saveAndNextElement.click();
    }

}
