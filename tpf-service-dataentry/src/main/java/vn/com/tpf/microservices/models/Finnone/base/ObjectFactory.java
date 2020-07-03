
package vn.com.tpf.microservices.models.Finnone.base;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.nucleus.finnone.pro.ws.base.schema package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _BranchCode_QNAME = new QName("http://schema.base.ws.pro.finnone.nucleus.com", "branchCode");
    private final static QName _FaultReason_QNAME = new QName("http://schema.base.ws.pro.finnone.nucleus.com", "faultReason");
    private final static QName _TenantName_QNAME = new QName("http://schema.base.ws.pro.finnone.nucleus.com", "tenantName");
    private final static QName _Header_QNAME = new QName("http://schema.base.ws.pro.finnone.nucleus.com", "Header");
    private final static QName _Phone_QNAME = new QName("http://schema.base.ws.pro.finnone.nucleus.com", "phone");
    private final static QName _Detail_QNAME = new QName("http://schema.base.ws.pro.finnone.nucleus.com", "detail");
    private final static QName _TenantId_QNAME = new QName("http://schema.base.ws.pro.finnone.nucleus.com", "tenantId");
    private final static QName _ApplicationException_QNAME = new QName("http://schema.base.ws.pro.finnone.nucleus.com", "applicationException");
    private final static QName _Email_QNAME = new QName("http://schema.base.ws.pro.finnone.nucleus.com", "email");
    private final static QName _UserRole_QNAME = new QName("http://schema.base.ws.pro.finnone.nucleus.com", "userRole");
    private final static QName _CurrencyId_QNAME = new QName("http://schema.base.ws.pro.finnone.nucleus.com", "currencyId");
    private final static QName _CurrencyISOCode_QNAME = new QName("http://schema.base.ws.pro.finnone.nucleus.com", "currencyISOCode");
    private final static QName _CurrencyDescription_QNAME = new QName("http://schema.base.ws.pro.finnone.nucleus.com", "currencyDescription");
    private final static QName _BranchId_QNAME = new QName("http://schema.base.ws.pro.finnone.nucleus.com", "branchId");
    private final static QName _SystemException_QNAME = new QName("http://schema.base.ws.pro.finnone.nucleus.com", "systemException");
    private final static QName _Fault_QNAME = new QName("http://schema.base.ws.pro.finnone.nucleus.com", "Fault");
    private final static QName _Mobile_QNAME = new QName("http://schema.base.ws.pro.finnone.nucleus.com", "mobile");
    private final static QName _BranchName_QNAME = new QName("http://schema.base.ws.pro.finnone.nucleus.com", "branchName");
    private final static QName _UserCode_QNAME = new QName("http://schema.base.ws.pro.finnone.nucleus.com", "userCode");
    private final static QName _FaultMessage_QNAME = new QName("http://schema.base.ws.pro.finnone.nucleus.com", "faultMessage");
    private final static QName _UserName_QNAME = new QName("http://schema.base.ws.pro.finnone.nucleus.com", "userName");
    private final static QName _LanguageCode_QNAME = new QName("http://schema.base.ws.pro.finnone.nucleus.com", "languageCode");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.nucleus.finnone.pro.ws.base.schema
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ExceptionType }
     * 
     */
    public ExceptionType createExceptionType() {
        return new ExceptionType();
    }

    /**
     * Create an instance of {@link FaultType }
     * 
     */
    public FaultType createFaultType() {
        return new FaultType();
    }

    /**
     * Create an instance of {@link Header }
     * 
     */
    public Header createHeader() {
        return new Header();
    }

    /**
     * Create an instance of {@link ExceptionDetailType }
     * 
     */
    public ExceptionDetailType createExceptionDetailType() {
        return new ExceptionDetailType();
    }

    /**
     * Create an instance of {@link User }
     * 
     */
    public User createUser() {
        return new User();
    }

    /**
     * Create an instance of {@link Tenant }
     * 
     */
    public Tenant createTenant() {
        return new Tenant();
    }

    /**
     * Create an instance of {@link Branch }
     * 
     */
    public Branch createBranch() {
        return new Branch();
    }

    /**
     * Create an instance of {@link Currency }
     * 
     */
    public Currency createCurrency() {
        return new Currency();
    }

    /**
     * Create an instance of {@link UserInfoType }
     * 
     */
    public UserInfoType createUserInfoType() {
        return new UserInfoType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schema.base.ws.pro.finnone.nucleus.com", name = "branchCode")
    public JAXBElement<String> createBranchCode(String value) {
        return new JAXBElement<String>(_BranchCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schema.base.ws.pro.finnone.nucleus.com", name = "faultReason")
    public JAXBElement<String> createFaultReason(String value) {
        return new JAXBElement<String>(_FaultReason_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schema.base.ws.pro.finnone.nucleus.com", name = "tenantName")
    public JAXBElement<String> createTenantName(String value) {
        return new JAXBElement<String>(_TenantName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Header }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schema.base.ws.pro.finnone.nucleus.com", name = "Header")
    public JAXBElement<Header> createHeader(Header value) {
        return new JAXBElement<Header>(_Header_QNAME, Header.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schema.base.ws.pro.finnone.nucleus.com", name = "phone")
    public JAXBElement<String> createPhone(String value) {
        return new JAXBElement<String>(_Phone_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExceptionDetailType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schema.base.ws.pro.finnone.nucleus.com", name = "detail")
    public JAXBElement<ExceptionDetailType> createDetail(ExceptionDetailType value) {
        return new JAXBElement<ExceptionDetailType>(_Detail_QNAME, ExceptionDetailType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schema.base.ws.pro.finnone.nucleus.com", name = "tenantId")
    public JAXBElement<Long> createTenantId(Long value) {
        return new JAXBElement<Long>(_TenantId_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExceptionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schema.base.ws.pro.finnone.nucleus.com", name = "applicationException")
    public JAXBElement<ExceptionType> createApplicationException(ExceptionType value) {
        return new JAXBElement<ExceptionType>(_ApplicationException_QNAME, ExceptionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schema.base.ws.pro.finnone.nucleus.com", name = "email")
    public JAXBElement<String> createEmail(String value) {
        return new JAXBElement<String>(_Email_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schema.base.ws.pro.finnone.nucleus.com", name = "userRole")
    public JAXBElement<String> createUserRole(String value) {
        return new JAXBElement<String>(_UserRole_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schema.base.ws.pro.finnone.nucleus.com", name = "currencyId")
    public JAXBElement<Long> createCurrencyId(Long value) {
        return new JAXBElement<Long>(_CurrencyId_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schema.base.ws.pro.finnone.nucleus.com", name = "currencyISOCode")
    public JAXBElement<String> createCurrencyISOCode(String value) {
        return new JAXBElement<String>(_CurrencyISOCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schema.base.ws.pro.finnone.nucleus.com", name = "currencyDescription")
    public JAXBElement<String> createCurrencyDescription(String value) {
        return new JAXBElement<String>(_CurrencyDescription_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schema.base.ws.pro.finnone.nucleus.com", name = "branchId")
    public JAXBElement<Long> createBranchId(Long value) {
        return new JAXBElement<Long>(_BranchId_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExceptionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schema.base.ws.pro.finnone.nucleus.com", name = "systemException")
    public JAXBElement<ExceptionType> createSystemException(ExceptionType value) {
        return new JAXBElement<ExceptionType>(_SystemException_QNAME, ExceptionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FaultType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schema.base.ws.pro.finnone.nucleus.com", name = "Fault")
    public JAXBElement<FaultType> createFault(FaultType value) {
        return new JAXBElement<FaultType>(_Fault_QNAME, FaultType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schema.base.ws.pro.finnone.nucleus.com", name = "mobile")
    public JAXBElement<String> createMobile(String value) {
        return new JAXBElement<String>(_Mobile_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schema.base.ws.pro.finnone.nucleus.com", name = "branchName")
    public JAXBElement<String> createBranchName(String value) {
        return new JAXBElement<String>(_BranchName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schema.base.ws.pro.finnone.nucleus.com", name = "userCode")
    public JAXBElement<String> createUserCode(String value) {
        return new JAXBElement<String>(_UserCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schema.base.ws.pro.finnone.nucleus.com", name = "faultMessage")
    public JAXBElement<String> createFaultMessage(String value) {
        return new JAXBElement<String>(_FaultMessage_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schema.base.ws.pro.finnone.nucleus.com", name = "userName")
    public JAXBElement<String> createUserName(String value) {
        return new JAXBElement<String>(_UserName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schema.base.ws.pro.finnone.nucleus.com", name = "languageCode")
    public JAXBElement<String> createLanguageCode(String value) {
        return new JAXBElement<String>(_LanguageCode_QNAME, String.class, null, value);
    }

}
