
package vn.com.tpf.microservices.models.leadCreation;
import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the create package. 
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


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: create
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link LeadCreationResponse }
     * 
     */
    public LeadCreationResponse createLeadCreationResponse() {
        return new LeadCreationResponse();
    }

    /**
     * Create an instance of {@link LeadCreationRequest }
     * 
     */
    public LeadCreationRequest createLeadCreationRequest() {
        return new LeadCreationRequest();
    }

    /**
     * Create an instance of {@link PersonInfo }
     * 
     */
    public PersonInfo createPersonInfo() {
        return new PersonInfo();
    }

    /**
     * Create an instance of {@link Documents }
     * 
     */
    public Documents createDocuments() {
        return new Documents();
    }

    /**
     * Create an instance of {@link SourcingDetails }
     * 
     */
    public SourcingDetails createSourcingDetails() {
        return new SourcingDetails();
    }

    /**
     * Create an instance of {@link LoanInformation }
     * 
     */
    public LoanInformation createLoanInformation() {
        return new LoanInformation();
    }

    /**
     * Create an instance of {@link CommunicationDetails }
     * 
     */
    public CommunicationDetails createCommunicationDetails() {
        return new CommunicationDetails();
    }

    /**
     * Create an instance of {@link GeoTagging }
     * 
     */
    public GeoTagging createGeoTagging() {
        return new GeoTagging();
    }

    /**
     * Create an instance of {@link AttachmentDetails }
     * 
     */
    public AttachmentDetails createAttachmentDetails() {
        return new AttachmentDetails();
    }

    /**
     * Create an instance of {@link WorkAndIncomeType }
     * 
     */
    public WorkAndIncomeType createWorkAndIncomeType() {
        return new WorkAndIncomeType();
    }

    /**
     * Create an instance of {@link AmountField }
     * 
     */
    public AmountField createAmountField() {
        return new AmountField();
    }

}
