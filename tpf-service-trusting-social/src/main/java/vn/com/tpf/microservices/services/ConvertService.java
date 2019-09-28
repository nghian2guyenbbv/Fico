package vn.com.tpf.microservices.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.com.tpf.microservices.models.TrustingSocial;

@Service
public class ConvertService {

	@Autowired
	private ObjectMapper mapper;

	public ObjectNode toAppDisplay(TrustingSocial ts) {
		ObjectNode app = mapper.createObjectNode();
		app.put("project", "ts");
		app.put("uuid", ts.getId());
		app.put("partnerId", ts.getTs_lead_id());
		app.put("fullName",
				(ts.getFirst_name() + " " + ts.getMiddle_name() + " " + ts.getLast_name()).replaceAll("\\s+", " "));
		ArrayNode documents = mapper.createArrayNode();
		ts.getDocuments().forEach(e -> {
			ObjectNode doc = mapper.createObjectNode();
			doc.put("documentType", e.getDocument_type());
			doc.put("viewUrl", e.getView_url());
			doc.put("downloadUrl", e.getDownload_url());
			doc.set("updatedAt", mapper.convertValue(e.getUpdatedAt(), JsonNode.class));
			documents.add(doc);
		});
		app.set("documents", documents);
		ObjectNode optional = mapper.createObjectNode();
		optional.put("scheme", ts.getProduct_code());
		optional.put("nationalId", ts.getNational_id());
		optional.put("stage", ts.getStage());
		optional.put("city", ts.getProvince());
		optional.put("dob", ts.getDob());
		optional.put("dsaCode", ts.getDsa_code());
		optional.put("loanAmount", ts.getScore_range());
		app.set("optional", optional);
		app.set("comments", mapper.convertValue(ts.getComments(), JsonNode.class));
		return app;
	}

}
