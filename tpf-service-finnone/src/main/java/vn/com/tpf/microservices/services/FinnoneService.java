package vn.com.tpf.microservices.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class FinnoneService {
	

	@Autowired
	private ObjectMapper mapper;
	
	 @Autowired
	private JdbcTemplate jdbcTemplate;

	private JsonNode response(int status, JsonNode data) {
		ObjectNode response = mapper.createObjectNode();
		response.put("status", status).set("data", data);
		return response;
	}

	public JsonNode getReason(JsonNode request) {
		ObjectNode data = mapper.createObjectNode();
		try {
			String query = String.format("SELECT  FN_GET_REASON ('%s','%s') RESULT FROM DUAL", request.path("param").path("appId").asText(),request.path("param").path("status").asText());
			String row_string = jdbcTemplate.queryForObject(query,new Object[]{},
					(rs, rowNum) ->
				rs.getString(("RESULT")
	        ));
			JsonNode rows =  mapper.readTree(row_string);
			if(rows.path("result").asInt() == 0) {
				data.put("description", rows.path("data").path("description").asText());
				data.put("reasonId", rows.path("data").path("reasonId").asInt());
				return response(200, data);
			}else {
				data.put("message", rows.path("data").path("Message").asText());
				return response(500, data);
			}
		
		}catch (Exception e) {
			data.put("message", e.getMessage());
			return response(500, data);
		}
	}

	public JsonNode getLoan(JsonNode request) {
		ObjectNode data = mapper.createObjectNode();
		try {
			String query = String.format("SELECT  FUNC_GET_MOMO_DISBURSAL_LOAN ('%s') RESULT FROM DUAL",
					request.path("param").path("appId").asText());
			String row_string = jdbcTemplate.queryForObject(query, new Object[] {},
					(rs, rowNum) -> rs.getString(("RESULT")));
			JsonNode rows = mapper.readTree(row_string);
			if (rows.path("result").asInt() == 0) {
				data.put("totalAmount", rows.path("data").path("totalAmount").asLong());
				data.put("actualAmount", rows.path("data").path("actualAmount").asLong());
				ObjectNode detail = mapper.createObjectNode();
				detail.put("loanId", rows.path("data").path("loanId").asText());
				detail.put("tenor", rows.path("data").path("tenor").asInt());
				detail.put("rate", rows.path("data").path("rate").asInt());
				detail.put("disbursementDate", rows.path("data").path("disbursementDate").asText());
				detail.put("maturityDate", rows.path("data").path("maturityDate").asText());
				detail.put("dueDate", rows.path("data").path("dueDate").asInt());
				detail.put("EMI", rows.path("data").path("emi").asLong());
				detail.put("paymentBankAccount", rows.path("data").path("paymentBankAccount").asText());
				detail.put("firstInstallmentDate", rows.path("data").path("firstInstallmentDate").asText());
				detail.put("firstInstallmentAmount", rows.path("data").path("firstInstallmentAmount").asLong());
				data.set("detail", detail);
				ArrayNode feeDetail = mapper.createArrayNode();
				for (JsonNode fee : (ArrayNode) rows.path("data").path("feeDetail"))
					feeDetail.add(mapper.convertValue(
							Map.of("type", fee.path("type").asText(), "amount", fee.path("amount").asInt()),
							JsonNode.class));
				data.set("feeDetail", feeDetail);
				return response(200, data);
			} else {
				data.put("message", rows.path("data").path("Message").asText());
				return response(500, data);
			}

		} catch (Exception e) {
			data.put("message", e.getMessage());
			return response(500, data);
		}
	}

}