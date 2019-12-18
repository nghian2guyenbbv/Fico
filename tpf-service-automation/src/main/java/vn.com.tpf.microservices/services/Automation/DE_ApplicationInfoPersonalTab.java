package vn.com.tpf.microservices.services.Automation;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import vn.com.tpf.microservices.models.Automation.AddressDTO;
import vn.com.tpf.microservices.models.Automation.ApplicationInfoDTO;
import vn.com.tpf.microservices.models.Automation.FamilyDTO;
import vn.com.tpf.microservices.models.Automation.IdentificationDTO;
import vn.com.tpf.microservices.utilities.Constant;
import vn.com.tpf.microservices.utilities.Utilities;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Getter
public class DE_ApplicationInfoPersonalTab {
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

    @FindBy(how = How.ID, using = "address1ToBeAddedInput_country")
    private WebElement address1Element;

    @FindBy(how = How.ID, using = "address2ToBeAdded_country")
    private WebElement address2Element;

    @FindBy(how = How.ID, using = "address3ToBeAdded_country")
    private WebElement address3Element;

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


    public DE_ApplicationInfoPersonalTab(WebDriver driver) {
        PageFactory.initElements(driver, this);
        _driver = driver;
    }

    public void btnCreateClick() {
        this.btnCreateNewUserElement.click();
    }

    public void setValue(String gender, String firstName, String middleName, String lastName, String dateOfBirth,
                         String placeOfIssue, String maritialStatus, String national, String education) {
        try {


            this.genderSelectElement.click();
            await("genderSelectOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> genderSelectOptionElement.size() > 0);
            Utilities.chooseDropdownValue(gender, genderSelectOptionElement);

            this.firstNameElement.sendKeys(firstName);
            this.middleNameElement.sendKeys(middleName);
            this.lastNameElement.sendKeys(lastName);
            this.dateOfBirthdaElement.sendKeys(dateOfBirth);
            this.placeOfBirthElement.sendKeys(placeOfIssue);

            this.maritalStatusElement.click();
            await("maritalStatusOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> maritalStatusOptionElement.size() > 0);
            Utilities.chooseDropdownValue(maritialStatus, maritalStatusOptionElement);

            this.nationalElement.click();
            await("nationaloptionelement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> nationalOptionElement.size() > 0);
            Utilities.chooseDropdownValue(national, nationalOptionElement);

            this.educationElement.click();
            await("educationOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> educationOptionElement.size() > 0);
            Utilities.chooseDropdownValue(education, educationOptionElement);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    public void setValue(ApplicationInfoDTO applicationInfoDTO) throws IOException {
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

        if (applicationInfoDTO.getFamily().size() > 0) {
            loadFamilySection();
            await("Load Family Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> familyDivElement.isDisplayed());
            setFamilyValue(applicationInfoDTO.getFamily());
        }

        loadCommunicationSection();
        await("Load Communication details Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> communicationDetailDivElement.isDisplayed());
        selectPrimaryAddress(0);//default la Current Address
        await("emailPrimary loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> primaryEmailElement.isDisplayed());
        getPrimaryEmailElement().sendKeys(applicationInfoDTO.getEmail());
        loadCommunicationSection(); // close section after complete input

        await("Button check address duplicate not enabled").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> btnCheckDuplicateElement.isEnabled());

        btnCheckDuplicateElement.click();

        await("numDuplicateElement not enabled").atMost(120, TimeUnit.SECONDS)
                .until(() -> StringUtils.isNotEmpty(numDuplicateElement.getText()));

        saveAndNext();
    }

    public void btnPersonInfoProcessClick() {
        this.btnPersonInfoProcessElement.click();
    }

    public void loadIdentificationSection() {
        this.btnLoadIdentificationElement.click();
    }

    public void UpdateLoadIdentificationSection() {
        this.btnLoadIdentificationElement.click();
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

    public void setIdentificationValue(List<IdentificationDTO> datas) throws JsonParseException, JsonMappingException, IOException {
        int index = 0;
        WebElement type;
        WebElement country;
        for (IdentificationDTO data : datas) {
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

    public void updateIdentificationValue(List<IdentificationDTO> datas, int indexRow) throws JsonParseException, JsonMappingException, IOException {
        int index=0;
        WebElement type;
        WebElement country;
        for (IdentificationDTO data : datas) {
            type = _driver.findElement(By.id("idDetail_identificationType" + indexRow));
            new Select(type).selectByVisibleText(data.getDocumentType());

            _driver.findElement(By.id("idDetail_identificationNumber" + indexRow)).sendKeys(data.getDocumentNumber());
            _driver.findElement(By.id("idDetail_issueDate" + indexRow)).sendKeys(data.getIssueDate());
            _driver.findElement(By.id("idDetail_expiryDate" + indexRow)).sendKeys(data.getExpirationDate());
            country = _driver.findElement(By.id("idDetail_country" + indexRow));
            new Select(country).selectByVisibleText(data.getCountryOfIssue());
            if (index < datas.size() - 1) {
                await("Btn Add Element not enabled Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> btnAddIdentElement.isEnabled());
                btnAddIdentElement.click();
                index++;
            }
            indexRow++;
        }

    }

    public void setAddressValue(List<AddressDTO> datas) throws JsonParseException, JsonMappingException, IOException {
        int index = 0;
        Actions actions = new Actions(_driver);
        Utilities.captureScreenShot(_driver);
        for (AddressDTO data : datas) {
            await("btnCreateAnotherElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> btnCreateAnotherElement.isEnabled());
            actions.moveToElement(btnCreateAnotherElement).click().build().perform();

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
//
//			Select addressSelect = new Select(addressTypeElement);
//			await("Address loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//			.until(() -> addressSelect.getOptions().size() > 0);
//			addressSelect.selectByVisibleText(data.getAddressType());

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
            for (WebElement element : regionOptionElement) {
                if (element.getText().equals(data.getRegion())) {
                    element.click();
                    break;
                }
            }
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
            for (WebElement element : areaOptionElement) {
                if (element.getText().equals(data.getArea())) {
                    element.click();
                    break;
                }
            }
//            Select areaSelect = new Select(areaElement);
//            await("Area loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
//                    .until(() -> areaSelect.getOptions().size() > 0);
//            areaSelect.selectByVisibleText(data.getArea());

            address1Element.sendKeys(data.getBuilding());
            address2Element.sendKeys(data.getHouse());
            address3Element.sendKeys(data.getWard());


            currentAddrYearsElement.sendKeys(data.getResidentDurationYear());
            currentAddrMonthsElement.sendKeys(data.getResidentDurationMonth());
//            currentCityYearsElement.sendKeys(data.getCityDurationYear());
//            currentCityMonthsElement.sendKeys(data.getCityDurationMonth());
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

    public void updateAddressValue(List<AddressDTO> datas) throws JsonParseException, JsonMappingException, Exception {

        Actions actions = new Actions(_driver);

        //check xem co address nao empty type ko

        if(viewTagElement.size()!=0)
        {
            for (WebElement e : viewTagElement)
            {
                if(e.getText().equals(""))
                {
                    WebElement weDel =_driver.findElement(By.xpath("//*[contains(@id,'address_details_Table_wrapper')]//*[contains(text(),'')]//ancestor::tr//*[contains(@id,'deleteTag')]"));
                    weDel.click();
                }
            }
        }

        await("btnCreateAnotherElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> btnCreateAnotherElement.isDisplayed());

        btnCreateAnotherElement.click();

        for (AddressDTO data : datas) {

            System.out.println("data => " + data.getAddressType());
            await("trAddressListElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> trAddressListElement.size() > 0);
            Utilities.captureScreenShot(_driver);


            if(_driver.findElements(By.xpath("//*[contains(@id,'address_details_Table_wrapper')]//*[contains(text(),'" + data.getAddressType() +"')]//ancestor::tr//*[contains(@id,'editTag')]")).size()!=0)
            {

                WebElement we =_driver.findElement(By.xpath("//*[contains(@id,'address_details_Table_wrapper')]//*[contains(text(),'" + data.getAddressType() +"')]//ancestor::tr//*[contains(@id,'editTag')]"));
                we.click();

                await("addressDivElement display Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> addressDivElement.isDisplayed());

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

                actions.moveToElement(regionElement).click().build().perform();
                await("regionOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> regionOptionElement.size() > 1);
                for (WebElement element : regionOptionElement) {
                    if (element.getText().equals(data.getRegion())) {
                        element.click();
                        break;
                    }
                }

                actions.moveToElement(cityElement).click().build().perform();
                await("cityOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> cityOptionElement.size() > 1);
                cityInputElement.sendKeys(data.getCity().toUpperCase());
                cityInputElement.sendKeys(Keys.ENTER);

                pinCodeElement.clear();
                pinCodeElement.sendKeys("%%%");
                await("Pincode loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> pinCodeValueElement.isDisplayed());
                actions.moveToElement(pinCodeValueElement).click().build().perform();

                actions.moveToElement(areaElement).click().build().perform();
                await("areaOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> areaOptionElement.size() > 1);
                for (WebElement element : areaOptionElement) {
                    if (element.getText().equals(data.getArea())) {
                        element.click();
                        break;
                    }
                }

                address1Element.clear();
                address1Element.sendKeys(data.getBuilding());
                address2Element.clear();
                address2Element.sendKeys(data.getHouse());
                address3Element.clear();
                address3Element.sendKeys(data.getWard());

                currentAddrYearsElement.clear();
                currentAddrYearsElement.sendKeys(data.getResidentDurationYear());
                currentAddrMonthsElement.clear();
                currentAddrMonthsElement.sendKeys(data.getResidentDurationMonth());
                updateMobilePhoneNumberElement.clear();
                updateMobilePhoneNumberElement.sendKeys(data.getMobilePhone());
                Utilities.captureScreenShot(_driver);
                actions.moveToElement(btnSaveAddressElement).click().build().perform();
            }
            else {
                await("btnCreateAnotherElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> btnCreateAnotherElement.isEnabled());
                btnCreateAnotherElement.click();


                await("addressDivElement display Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> addressDivElement.isDisplayed());

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

                actions.moveToElement(regionElement).click().build().perform();
                await("regionOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> regionOptionElement.size() > 1);
                for (WebElement element : regionOptionElement) {
                    if (element.getText().equals(data.getRegion())) {
                        element.click();
                        break;
                    }
                }

                actions.moveToElement(cityElement).click().build().perform();
                await("cityOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> cityOptionElement.size() > 1);

                cityInputElement.sendKeys(data.getCity().toUpperCase());
                cityInputElement.sendKeys(Keys.ENTER);

                pinCodeElement.clear();
                pinCodeElement.sendKeys("%%%");
                await("Pincode loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> pinCodeValueElement.isDisplayed());
                actions.moveToElement(pinCodeValueElement).click().build().perform();

                actions.moveToElement(areaElement).click().build().perform();
                await("areaOptionElement loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                        .until(() -> areaOptionElement.size() > 1);
                for (WebElement element : areaOptionElement) {
                    if (element.getText().equals(data.getArea())) {
                        element.click();
                        break;
                    }
                }

                address1Element.clear();
                address1Element.sendKeys(data.getBuilding());
                address2Element.clear();
                address2Element.sendKeys(data.getHouse());
                address3Element.clear();
                address3Element.sendKeys(data.getWard());

                currentAddrYearsElement.clear();
                currentAddrYearsElement.sendKeys(data.getResidentDurationYear());
                currentAddrMonthsElement.clear();
                currentAddrMonthsElement.sendKeys(data.getResidentDurationMonth());

                updateMobilePhoneNumberElement.clear();
                updateMobilePhoneNumberElement.sendKeys(data.getMobilePhone());
                Utilities.captureScreenShot(_driver);
                actions.moveToElement(btnSaveAddressElement).click().build().perform();
            }
        }
    }

    public void selectPrimaryAddress(int index) {
        //checkBoxPrimaryAddressElements.get(index).click();
        cbPrimaryAddessElement.click();
    }

    public void setFamilyValue(List<FamilyDTO> datas) throws IOException {
        for (FamilyDTO data : datas) {
            this.memberNameElement.sendKeys(data.getMemberName());
            new Select(this.relationshipTypeElement).selectByVisibleText(data.getRelationshipType());
            this.phoneNumberElement.sendKeys(data.getPhoneNumber());
            if (StringUtils.isNotBlank(data.getEducationStatus()))
                new Select(this.educationStatusElement).selectByVisibleText(data.getEducationStatus());
            this.comNameElement.sendKeys(data.getComName());
            if (StringUtils.isNotBlank(data.getIsDependent()) && Integer.parseInt(data.getIsDependent()) == 1)
                this.IsDependentElement.click();

            // TODO: instead of break this loop, we must create new family row element here if have more data
            break;
        }

    }

    public void updateFamilyValue(List<FamilyDTO> datas) throws IOException {
        for (FamilyDTO data : datas) {
            this.memberNameElement.clear();
            this.memberNameElement.sendKeys(data.getMemberName());
            new Select(this.relationshipTypeElement).selectByVisibleText(data.getRelationshipType());
            this.phoneNumberElement.clear();
            this.phoneNumberElement.sendKeys(data.getPhoneNumber());
            if (StringUtils.isNotBlank(data.getEducationStatus()))
                new Select(this.educationStatusElement).selectByVisibleText(data.getEducationStatus());
            this.comNameElement.clear();
            this.comNameElement.sendKeys(data.getComName());
            if (StringUtils.isNotBlank(data.getIsDependent()) && Integer.parseInt(data.getIsDependent()) == 1)
                this.IsDependentElement.click();

            // TODO: instead of break this loop, we must create new family row element here if have more data
            break;
        }

    }

    public void saveAndNext() {
        this.saveAndNextElement.click();
    }

    // TODO: compare original data vs element data and report if not equals
    public void validInOutData(Map<String, String> mapValue, String gender, String firstName, String middleName, String lastName, String dateOfBirth,
                               String placeOfIssue, String maritialStatus, String national, String education,
                               String indentifitionObj, String addressObj, String familyObj) throws Exception {
        Utilities.checkInput(mapValue, "ApplicationInfo_PersonalInfo_Gender", gender, genderSelectOptionElement);
        Utilities.checkInput(mapValue, "ApplicationInfo_PersonalInfo_FirstName", firstName, firstNameElement);
        Utilities.checkInput(mapValue, "ApplicationInfo_PersonalInfo_MiddleName", middleName, middleNameElement);
        Utilities.checkInput(mapValue, "ApplicationInfo_PersonalInfo_LastName", lastName, lastNameElement);
        Utilities.checkInput(mapValue, "ApplicationInfo_PersonalInfo_DateOfBirth", dateOfBirth, dateOfBirthdaElement);
        Utilities.checkInput(mapValue, "ApplicationInfo_PersonalInfo_PlaceOfBirth", placeOfIssue, placeOfBirthElement);
        Utilities.checkInput(mapValue, "ApplicationInfo_PersonalInfo_maritalStatus", maritialStatus, maritalStatusOptionElement);
        Utilities.checkInput(mapValue, "ApplicationInfo_PersonalInfo_national", national, nationalOptionElement);
        Utilities.checkInput(mapValue, "ApplicationInfo_PersonalInfo_education", education, educationOptionElement);

        ObjectMapper mapper = new ObjectMapper();
        IdentificationDTO[] identificationDTOs = mapper.readValue(indentifitionObj, IdentificationDTO[].class);
        int index = 0;
        for (IdentificationDTO data : identificationDTOs) {
            WebElement type = _driver.findElement(By.id("idDetail_identificationType" + index));
            if (!new Select(type).getFirstSelectedOption().getText().equals(data.getDocumentType())) {
                mapValue.put("ApplicationInfo_PersonalInfo_documentType_" + index, new Select(type).getFirstSelectedOption().getText());
            }

            if (!_driver.findElement(By.id("idDetail_identificationNumber" + index)).getAttribute("value").equals(data.getDocumentNumber())) {
                mapValue.put("ApplicationInfo_PersonalInfo_documentNumber_" + index, _driver.findElement(By.id("idDetail_identificationNumber" + index)).getAttribute("value"));
            }

            if (!_driver.findElement(By.id("idDetail_issueDate" + index)).getAttribute("value").equals(data.getIssueDate())) {
                mapValue.put("ApplicationInfo_PersonalInfo_issueDate_" + index, _driver.findElement(By.id("idDetail_issueDate" + index)).getAttribute("value"));
            }

            if (!_driver.findElement(By.id("idDetail_expiryDate" + index)).getAttribute("value").equals(data.getExpirationDate())) {
                mapValue.put("ApplicationInfo_PersonalInfo_expiryDate_" + index, _driver.findElement(By.id("idDetail_expiryDate" + index)).getAttribute("value"));
            }

            WebElement country = _driver.findElement(By.id("idDetail_country" + index));
            if (!new Select(country).getFirstSelectedOption().getText().equals(data.getCountryOfIssue())) {
                mapValue.put("ApplicationInfo_PersonalInfo_countryOfIssue_" + index, new Select(country).getFirstSelectedOption().getText());
            }
            index++;
        }

        AddressDTO[] datas = mapper.readValue(addressObj, AddressDTO[].class);
        index = 0;
        for (AddressDTO data : datas) {
            Utilities.checkInput(mapValue, "ApplicationInfo_PersonalInfo_addressType_" + index, data.getAddressType(), addressTypeOptionElement);
            Utilities.checkInput(mapValue, "ApplicationInfo_PersonalInfo_country_" + index, data.getCountry(), textCountryOptionElement);
            Utilities.checkInput(mapValue, "ApplicationInfo_PersonalInfo_region_" + index, data.getRegion(), regionOptionElement);
            Utilities.checkInput(mapValue, "ApplicationInfo_PersonalInfo_city_" + index, data.getCity(), cityOptionElement);
            Utilities.checkInput(mapValue, "ApplicationInfo_PersonalInfo_pinCode_" + index, data.getPinCode(), pinCodeElement);
            Utilities.checkInput(mapValue, "ApplicationInfo_PersonalInfo_area_" + index, data.getArea(), areaOptionElement);
            Utilities.checkInput(mapValue, "ApplicationInfo_PersonalInfo_building_" + index, data.getBuilding(), address1Element);
            Utilities.checkInput(mapValue, "ApplicationInfo_PersonalInfo_house_" + index, data.getHouse(), address2Element);
            Utilities.checkInput(mapValue, "ApplicationInfo_PersonalInfo_ward_" + index, data.getWard(), address3Element);
            Utilities.checkInput(mapValue, "ApplicationInfo_PersonalInfo_durationOfResidentYears_" + index, data.getResidentDurationYear(), currentAddrYearsElement);
            Utilities.checkInput(mapValue, "ApplicationInfo_PersonalInfo_durationOfResidentMonths_" + index, data.getResidentDurationMonth(), currentAddrMonthsElement);
            Utilities.checkInput(mapValue, "ApplicationInfo_PersonalInfo_durationOfCurCityYears_" + index, data.getCityDurationYear(), currentCityYearsElement);
            Utilities.checkInput(mapValue, "ApplicationInfo_PersonalInfo_durationOfCurCityMonths_" + index, data.getCityDurationMonth(), currentCityMonthsElement);
            index++;
        }

        String[] familyData = familyObj.split("|");
        Utilities.checkInput(mapValue, "ApplicationInfo_PersonalInfo_memberName", familyData[0], memberNameElement);

        if (!new Select(relationshipTypeElement).getFirstSelectedOption().getText().equals(familyData[1])) {
            mapValue.put("ApplicationInfo_PersonalInfo_relationshipType", new Select(relationshipTypeElement).getFirstSelectedOption().getText());
        }

        Utilities.checkInput(mapValue, "ApplicationInfo_PersonalInfo_phoneNumber", familyData[2], phoneNumberElement);
        if (!new Select(educationStatusElement).getFirstSelectedOption().getText().equals(familyData[3])) {
            mapValue.put("ApplicationInfo_PersonalInfo_educationStatus", new Select(educationStatusElement).getFirstSelectedOption().getText());
        }
        Utilities.checkInput(mapValue, "ApplicationInfo_PersonalInfo_comName", familyData[4], comNameElement);
//        if (Integer.parseInt(familyData[5]) == 1)
//            this.IsDependentElement.click();


    }

    public void updateValue(ApplicationInfoDTO applicationInfoDTO) throws Exception
    {

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


        //Update Identification
        loadIdentificationSection();
        await("Load identificationDivElement Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> identificationDivElement.isDisplayed());

        await("Load deleteIdDetailElement Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> deleteIdDetailElement.size() > 0);

        for (int i=0; i<deleteIdDetailElement.size()-1; i++)
        {
            WebElement var = deleteIdDetailElement.get(i);
            var.click();
        }
        await("Btn Add Element not enabled Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> btnAddIdentElement.isEnabled());
        btnAddIdentElement.click();
        updateIdentificationValue(applicationInfoDTO.getIdentification(),deleteIdDetailElement.size()-1);
        //end update identification

        //update address: ko xoa dc do anh huong cac employment detail nen chi edit
        loadAddressSection();
        await("Load addressGridElement Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> addressGridElement.isDisplayed());

        //click edit
        updateAddressValue(applicationInfoDTO.getAddress());
        loadAddressSection();

        if (applicationInfoDTO.getFamily().size() > 0) {
            loadFamilySection();
            await("Load Family Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                    .until(() -> familyDivElement.isDisplayed());
            updateFamilyValue(applicationInfoDTO.getFamily());
        }

        loadCommunicationSection();
        await("Load Communication details Section Timeout!").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> communicationDetailDivElement.isDisplayed());
        selectPrimaryAddress(0);//default la Current Address
        await("emailPrimary loading timeout").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> primaryEmailElement.isDisplayed());
        getPrimaryEmailElement().clear();
        getPrimaryEmailElement().sendKeys(applicationInfoDTO.getEmail());
        loadCommunicationSection(); // close section after complete input

        await("Button check address duplicate not enabled").atMost(Constant.TIME_OUT_S, TimeUnit.SECONDS)
                .until(() -> btnCheckDuplicateUpdateElement.isDisplayed());

        Actions actions=new Actions(_driver);
         //actions.moveToElement(btnCheckDuplicateUpdateElement).click().build().perform();
        btnCheckDuplicateElement.click();

         //ko hien thi
         await("numDuplicateElement text not enabled").atMost(60, TimeUnit.SECONDS)
                .until(() -> StringUtils.isNotEmpty(numDuplicateElement.getText()));

        saveAndNext();
    }

}
