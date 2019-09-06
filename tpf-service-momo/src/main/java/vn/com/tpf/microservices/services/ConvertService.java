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
		app.put("fullName",
				(momo.getFirstName() + " " + momo.getMiddleName() + " " + momo.getLastName()).replaceAll("\\s+", " "));
		app.put("status", momo.getStatus());
		app.put("automationResult", momo.getAutomationResult());
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
		app.set("optional", optional);
		return app;
	}

	public ObjectNode toAppFinnone(Momo momo) {
		ObjectNode app = mapper.createObjectNode();
		return app;
	}

}
