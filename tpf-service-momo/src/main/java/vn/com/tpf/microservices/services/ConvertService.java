package vn.com.tpf.microservices.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.com.tpf.microservices.models.Momo;

@Service
public class ConvertService {

	@Autowired
	private ObjectMapper mapper;

	public ObjectNode toAppDisplay(Momo momo) {
		ObjectNode app = mapper.createObjectNode();
		app.put("project", "momo");
		app.put("uuid", momo.getId());
		app.put("appId", momo.getAppId());
		app.put("partnerId", momo.getMomoLoanId());
		app.put("status", momo.getStatus());
		app.put("automationResult", momo.getAutomationResult());
		app.put("fullName",
				(momo.getFirstName() + " " + momo.getMiddleName() + " " + momo.getLastName()).replaceAll("\\s+", " "));
		ArrayNode documents = mapper.createArrayNode();
		momo.getPhotos().forEach(e -> {
			ObjectNode doc = mapper.createObjectNode();
			doc.put("documentType", e.getType());
			doc.put("viewUrl", e.getLink());
			doc.put("downloadUrl", e.getLink());
			doc.set("updatedAt", mapper.convertValue(e.getUpdatedAt(), JsonNode.class));
			documents.add(doc);
		});
		app.set("documents", documents);
		ObjectNode optional = mapper.createObjectNode();
		optional.put("smsResult", momo.getSmsResult());
		optional.put("error", momo.getError());
		app.set("optional", optional);
		return app;
	}

	public ObjectNode toAppFinnone(Momo momo) {
		ObjectNode app = mapper.createObjectNode();
		app.put("project", "momo");

		ObjectNode spouse = mapper.createObjectNode();
		ArrayNode references = mapper.createArrayNode();
		momo.getReferences().forEach(e -> {
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
		momo.getPhotos().forEach(e -> {
			documents.add(mapper.createObjectNode().put("type", e.getType()).put("link", e.getLink()));
		});
		app.set("documents", documents);

		ObjectNode applicationInformation = mapper.createObjectNode();
		ObjectNode personalInformation = mapper.createObjectNode();
		personalInformation.set("personalInfo",
				mapper.createObjectNode().put("firstName", momo.getFirstName()).put("middleName", momo.getMiddleName())
						.put("lastName", momo.getLastName())
						.put("fullName",
								(momo.getFirstName() + " " + momo.getMiddleName() + " " + momo.getLastName()).replaceAll("\\s+", " "))
						.put("gender", momo.getGender()).put("dateOfBirth", momo.getDateOfBirth()).put("nationality", "Vietnamese")
						.put("maritalStatus", momo.getMaritalStatus()));
		personalInformation.set("identifications",
				mapper.createArrayNode()
						.add(mapper.createObjectNode().put("identificationType", "Current National ID")
								.put("issuingCountry", "Vietnam").put("identificationNumber", momo.getPersonalId())
								.put("placeOfBirth", momo.getIssuePlace()).put("issueDate", momo.getIssueDate()))
						.add(mapper.createObjectNode().put("identificationType", "Spouse Current National ID")
								.put("issuingCountry", "Vietnam").set("identificationNumber", spouse.path("personalId"))));
		personalInformation.set("addresses", mapper.createArrayNode().add(mapper.createObjectNode()
				.put("addressType", "Current Address").put("country", "Vietnam").put("state", momo.getRegion())
				.put("city", momo.getCity()).put("zipcode", "70000").put("area", momo.getDistrict())
				.put("addressLine1", momo.getAddress1()).put("addressLine2", momo.getAddress2())
				.put("addressLine3", momo.getWard()).set("phoneNumbers", mapper.createArrayNode().add(
						mapper.createObjectNode().put("phoneType", "Mobile Phone").put("phoneNumber", momo.getPhoneNumber())))));
		personalInformation.set("communicationDetails",
				mapper.createObjectNode().put("primaryAddress", "Current Address").put("primaryEmailId", momo.getEmail())
						.set("phoneNumbers", mapper.createArrayNode().add(
								mapper.createObjectNode().put("phoneType", "Mobile Phone").put("phoneNumber", momo.getPhoneNumber()))));
		personalInformation.set("family", mapper.createArrayNode().add(spouse));
		applicationInformation.set("personalInformation", personalInformation);
		applicationInformation.set("financialDetails", mapper.createArrayNode()
				.add(mapper.createObjectNode().put("incomeExpense", "Main Personal Income").put("amount", momo.getSalary())));
		app.set("applicationInformation", applicationInformation);

		ObjectNode loanDetails = mapper.createObjectNode();
		loanDetails.set("sourcingDetails",
				mapper.createObjectNode().put("productCode", momo.getProductCode()).put("loanAmountRequested", momo.getAmount())
						.put("requestedTenure", momo.getLoanTime()).put("loanApplicationType", "New Application")
						.put("chassisApplicationNum", momo.getMomoLoanId()).put("saleAgentCode", "OTHER VALUE"));
		if (momo.isInsurrance()) {
			loanDetails.set("vapDetails", mapper.createObjectNode().put("vapProduct", "INSP01_InsParameter")
					.put("vapTreatment", "Financed").put("insuranceCompany", "TPF_GIC-Global Insurance Company"));
		}
		app.set("loanDetails", loanDetails);

		return app;
	}

}
