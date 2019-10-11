package vn.com.tpf.microservices.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class FinnoneService {

	@Autowired
	private ObjectMapper mapper;

	private JsonNode response(int status, JsonNode data) {
		ObjectNode response = mapper.createObjectNode();
		response.put("status", status).set("data", data);
		return response;
	}

	public JsonNode getReason(JsonNode request) {
		ObjectNode data = mapper.createObjectNode();
		data.put("description", request.path("param").path("status").asText());
		data.put("reasonId", 1);
		return response(200, data);
	}

	public JsonNode getLoan(JsonNode request) {
		ObjectNode data = mapper.createObjectNode();
		data.put("totalAmount", 3000000);
		data.put("actualAmount", 3000000);
		ObjectNode detail = mapper.createObjectNode();
		detail.put("loanId", "loanId");
		detail.put("tenor", 3);
		detail.put("rate", 39);
		detail.put("disbursementDate", "");
		detail.put("maturityDate", "");
		detail.put("dueDate", 1);
		detail.put("emi", 1066000);
		detail.put("paymentBankAccount", "");
		detail.put("firstInstallmentDate", "");
		detail.put("firstInstallmentAmount", 1066000);
		data.set("detail", detail);
		ArrayNode feeDetail = mapper.createArrayNode();
		feeDetail.add(mapper.convertValue(Map.of("type", "Insurrance", "amount", 0), JsonNode.class));
		feeDetail.add(mapper.convertValue(Map.of("type", "Service", "amount", 0), JsonNode.class));
		data.set("feeDetail", feeDetail);
		return response(200, data);
	}

}