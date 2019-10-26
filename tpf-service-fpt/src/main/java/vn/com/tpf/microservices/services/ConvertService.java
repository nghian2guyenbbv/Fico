package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.tpf.microservices.models.Fpt;
import vn.com.tpf.microservices.models.LoanDetail;

@Service
public class ConvertService {

	@Autowired
	private ObjectMapper mapper;

	public ObjectNode toAppDisplay(Fpt fpt) {
		ObjectNode app = mapper.createObjectNode();
		app.put("project", "fpt");
		app.put("uuid", fpt.getId());
		app.put("appId", fpt.getAppId());
		app.put("partnerId", fpt.getCustId());
		app.put("status", fpt.getStatus());
		app.put("automationResult", fpt.getAutomationResult());
		app.put("fullName",
				(fpt.getFirstName() + " " + fpt.getMiddleName() + " " + fpt.getLastName()).replaceAll("\\s+", " "));
		ArrayNode documents = mapper.createArrayNode();
		fpt.getPhotos().forEach(e -> {
			ObjectNode doc = mapper.createObjectNode();
			doc.put("documentType", e.getDocumentType());
			doc.put("viewUrl", e.getLink());
			doc.put("downloadUrl", e.getLink());
			doc.set("updatedAt", mapper.convertValue(e.getUpdatedAt(), JsonNode.class));
			documents.add(doc);
		});
		app.set("documents", documents);
		return app;
	}

	public ObjectNode toAppFinnone(Fpt fpt) {
		ObjectNode app = mapper.createObjectNode();
		app.put("project", "fpt");

		ObjectNode spouse = mapper.createObjectNode();
		ArrayNode references = mapper.createArrayNode();
		fpt.getReferences().forEach(e -> {
			if (e.getRelation().equals("Spouse")) {
				spouse.put("memberName", e.getFullName());
				spouse.put("phoneNumber", e.getPhoneNumber());
				spouse.put("relationship", e.getRelation());
				spouse.put("personalId", e.getPersonalId());
			} else {
				ObjectNode ref = mapper.createObjectNode();
				ref.put("name", e.getFullName());
				ref.put("relationship", e.getRelation());
				ref.set("phoneNumbers", mapper.createArrayNode()
						.add(mapper.createObjectNode().put("phoneType", "Mobile Phone").put("phoneNumber", e.getPhoneNumber())));
				references.add(ref);
			}
		});
		app.set("references", references);

		ArrayNode documents = mapper.createArrayNode();
		fpt.getPhotos().forEach(e -> {
			documents.add(mapper.createObjectNode().put("type", e.getDocumentType()).put("link", e.getLink()));
		});
		app.set("documents", documents);

		ObjectNode applicationInformation = mapper.createObjectNode();
		ObjectNode personalInformation = mapper.createObjectNode();
		personalInformation.set("personalInfo",
				mapper.createObjectNode().put("firstName", fpt.getFirstName()).put("middleName", fpt.getMiddleName())
						.put("lastName", fpt.getLastName())
						.put("fullName",
								(fpt.getFirstName() + " " + fpt.getMiddleName() + " " + fpt.getLastName()).replaceAll("\\s+", " "))
						.put("gender", fpt.getGender()).put("dateOfBirth", fpt.getDateOfBirth()).put("nationality", "Vietnamese")
						.put("maritalStatus", fpt.getMaritalStatus()));
		personalInformation.set("identifications",
				mapper.createArrayNode()
						.add(mapper.createObjectNode().put("identificationType", "Current National ID")
								.put("issuingCountry", "Vietnam").put("identificationNumber", fpt.getNationalId())
								.put("placeOfBirth", fpt.getIssuePlace()).put("issueDate", fpt.getIssueDate()))
						.add(mapper.createObjectNode().put("identificationType", "Spouse Current National ID")
								.put("issuingCountry", "Vietnam").set("identificationNumber", spouse.path("personalId"))));

		ArrayNode addresses = mapper.createArrayNode();
		fpt.getAddresses().forEach(e -> {
			ObjectNode address = mapper.createObjectNode();
			address.put("country", "Vietnam");
			address.put("zipcode", "70000");
			address.put("addressType", e.getAddressType());
			address.put("addressLine1", e.getAddress1());
			address.put("addressLine2", e.getAddress2());
			address.put("addressLine3", e.getWard());
			address.put("area", e.getDistrict());
			address.put("city", e.getProvince());
			address.put("state", e.getRegion());
			if (e.getAddressType().equals("Current Address")) {
				address.set("phoneNumbers", mapper.createArrayNode()
						.add(mapper.createObjectNode().put("phoneType", "Mobile Phone").put("phoneNumber", fpt.getMobilePhone())));
			}
			addresses.add(address);
		});
		personalInformation.set("addresses", addresses);

		personalInformation.set("communicationDetails",
				mapper.createObjectNode().put("primaryAddress", "Current Address").set("phoneNumbers", mapper.createArrayNode()
						.add(mapper.createObjectNode().put("phoneType", "Mobile Phone").put("phoneNumber", fpt.getMobilePhone()))));
		personalInformation.set("family", mapper.createArrayNode().add(spouse));
		applicationInformation.set("personalInformation", personalInformation);
		applicationInformation.set("financialDetails", mapper.createArrayNode()
				.add(mapper.createObjectNode().put("incomeExpense", "Main Personal Income").put("amount", fpt.getSalary())));
		app.set("applicationInformation", applicationInformation);

		LoanDetail loanDetail = fpt.getLoanDetail();
		if (loanDetail != null) {
			ObjectNode loanDetails = mapper.createObjectNode();
			loanDetails.set("sourcingDetails",
					mapper.createObjectNode().put("productCode", loanDetail.getProduct())
							.put("loanAmountRequested", loanDetail.getLoanAmount()).put("requestedTenure", loanDetail.getTenor())
							.put("loanApplicationType", "New Application").put("chassisApplicationNum", loanDetail.getLoanId())
							.put("saleAgentCode", "OTHER VALUE"));
			app.set("loanDetails", loanDetails);
		}

		return app;
	}

}
