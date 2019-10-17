package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.tpf.microservices.models.Application;

@Service
public class ConvertService {

	@Autowired
	private ObjectMapper mapper;

	public ObjectNode toAppDisplay(Application application) {
		ObjectNode app = mapper.createObjectNode();
		app.put("project", "dataentry");
		app.put("uuid", application.getId());
		app.put("appId", application.getApplicationId());
//		app.put("partnerId", "");// chua co
		app.put("fullName",
				(application.getQuickLead().getFirstName() + application.getQuickLead().getLastName()).replaceAll("\\s+", " "));
		app.put("automationResult", application.getDescription());
		ArrayNode documents = mapper.createArrayNode();
		if(application.getQuickLead().getDocuments() != null) {
			application.getQuickLead().getDocuments().forEach(e -> {
				ObjectNode doc = mapper.createObjectNode();
				doc.put("documentType", e.getOriginalname());
				doc.put("viewUrl", e.getFilename());
				doc.put("downloadUrl", e.getFilename());
//			doc.set("updatedAt", mapper.convertValue(e.getUpdatedAt(), JsonNode.class));
				documents.add(doc);
			});
			app.set("documents", documents);
		}

		ArrayNode comments = mapper.createArrayNode();
		if(application.getComment() != null){
			application.getComment().forEach(e -> {
				ObjectNode doc = mapper.createObjectNode();
				doc.put("commentId", e.getCommentId());
				doc.put("state", e.getState());
				doc.put("type", e.getType());
				doc.put("code", e.getCode());
				doc.put("request", e.getRequest());
				try {
					doc.put("response", mapper.writeValueAsString(e.getResponse()));
				} catch (JsonProcessingException e1) {
					e1.printStackTrace();
				}
				comments.add(doc);
			});
			app.set("comments", comments);
		}
		ObjectNode optional = mapper.createObjectNode();
//		optional.put("smsResult", momo.getSmsResult());
		app.set("optional", optional);
		return app;
	}

}