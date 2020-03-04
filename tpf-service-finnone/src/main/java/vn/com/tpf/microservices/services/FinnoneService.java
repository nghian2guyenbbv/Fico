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
			String query = String.format("SELECT  FN_GET_REASON ('%s','%s') RESULT FROM DUAL",
					request.path("param").path("appId").asText(), request.path("param").path("status").asText());
			String row_string = jdbcTemplate.queryForObject(query, new Object[] {},
					(rs, rowNum) -> rs.getString(("RESULT")));
			JsonNode rows = mapper.readTree(row_string);
			if (rows.path("result").asInt() == 0) {
				data.put("description", rows.path("data").path("description").asText());
				data.put("reasonId", rows.path("data").path("reasonId").asInt());
				return response(200, data);
			} else {
				data.put("description", rows.path("data").path("Message").asText());
				return response(500, data);
			}

		} catch (Exception e) {
			data.put("message", e.getMessage());
			return response(500, data);
		}
	}

	public JsonNode getAppInfo(JsonNode request) {

		try {
			String query = String.format("SELECT * FROM TABLE(FN_APP_INFO('%s'))", request.path("body").path("data").path("appId").asText());
			Object rowObjectNode = jdbcTemplate.queryForObject(query, new Object[] {}, (rs, rowNum) -> {
				ObjectNode row = mapper.createObjectNode();
				row.put("appId", rs.getString("APPLICATION_NUMBER"));
				row.put("status", rs.getString("STATUS"));
				row.put("stage", rs.getString("STAGE"));
				row.put("reasonReturn", rs.getString("REASON_RETURN"));
				row.put("reasonCancel", rs.getString("REASON_CANCEL"));
				row.put("taxCode", rs.getString("TAX_CODE"));
				row.put("finalProduct", rs.getString("FINAL_PRODUCT"));
				row.put("tenor", rs.getDouble("TENURE"));
				row.put("loanAmount", rs.getLong("LOAN_AMOUNT"));
				row.put("interestRate", rs.getDouble("INTEREST_RATE"));
				row.put("loanAccount", rs.getString("LOAN_ACCOUNT"));
				row.put("disbursementDate", rs.getString("DISBURSAL_DATE"));
				row.put("loanStatus", rs.getString("LOAN_STATUS"));
				row.put("insurance", rs.getBoolean("INSURANCE"));
				row.put("courierCode", rs.getString("COURIER_CODE"));
				row.put("lastSubmitDate", rs.getString("LAST_SUBMIT_DATE"));
				row.put("lastUpdateDate", rs.getString("LAST_UPDATE_DATE"));
				row.put("remark", rs.getString("REMARKS"));
				row.put("emi", rs.getDouble("EMI"));
				return row;
			});

			return response(200, mapper.convertValue(rowObjectNode, JsonNode.class));

		} catch (Exception e) {

			return response(500, mapper.createObjectNode().put("message", e.getMessage()));

		}
	}

	public JsonNode getCheckList(JsonNode request) {
		ObjectNode data = mapper.createObjectNode();
		try {
			String query = String.format("SELECT  FN_CHECK_LIST ('%s','%s','%s') RESULT FROM DUAL",
					request.path("param").path("bank_card_number").asText(),
					request.path("param").path("dsa_code").asText(), request.path("param").path("area_code").asText());
			String row_string = jdbcTemplate.queryForObject(query, new Object[] {},
					(rs, rowNum) -> rs.getString(("RESULT")));
			JsonNode rows = mapper.readTree(row_string);
			data.put("result", rows.path("result").asInt()); // 0 -> pass . 1 -> fail
			data.put("description", rows.path("description").asText());
			return response(200, data);

		} catch (Exception e) {
			data.put("result", 0);
			data.put("description", e.getMessage());
			return response(200, data);
		}
	}

	public JsonNode getPreCheckList(JsonNode request) {
		ObjectNode data = mapper.createObjectNode();
		try {
			String query = String.format("SELECT  FN_PRECHECK_LIST  ('%s','%s','%s','%s') RESULT FROM DUAL",
					request.path("body").path("bankCardNumber").asText(), request.path("body").path("dsaCode").asText(),
					request.path("body").path("areaCode").asText(), request.path("body").path("nationalId").asText());
			String row_string = jdbcTemplate.queryForObject(query, new Object[] {},
					(rs, rowNum) -> rs.getString(("RESULT")));
			JsonNode rows = mapper.readTree(row_string);
			data.put("result", rows.path("result").asInt()); // 0 -> pass . 1 -> fail
			data.put("description", rows.path("description").asText());
			return response(200, data);

		} catch (Exception e) {
			data.put("result", 0);
			data.put("description", e.getMessage());
			return response(200, data);
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
				detail.put("loanId", rows.path("data").path("detail").path("loanId").asText());
				detail.put("tenor", rows.path("data").path("detail").path("tenor").asInt());
				detail.put("rate", rows.path("data").path("detail").path("rate").asDouble());
				detail.put("disbursementDate", rows.path("data").path("detail").path("disbursementDate").asText());
				detail.put("maturityDate", rows.path("data").path("detail").path("maturityDate").asText());
				detail.put("dueDate", rows.path("data").path("detail").path("dueDate").asInt());
				detail.put("emi", rows.path("data").path("detail").path("emi").asLong());
				detail.put("paymentBankAccount", rows.path("data").path("detail").path("paymentBankAccount").asText());
				detail.put("firstInstallmentDate",
						rows.path("data").path("detail").path("firstInstallmentDate").asText());
				detail.put("firstInstallmentAmount",
						rows.path("data").path("detail").path("firstInstallmentAmount").asLong());
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

	public JsonNode getCheckDupApplication(JsonNode request) {
		ObjectNode data = mapper.createObjectNode();
		try {
			String query = String.format("SELECT  FN_CHECK_DUP_APPLICATION_V2 ('%s','%s','%s','%s') RESULT FROM DUAL",
					request.path("body").path("appId").asText(), request.path("body").path("nationalId").asText(),
					request.path("body").path("fullName").asText(), request.path("body").path("dob").asText());
			String row_string = jdbcTemplate.queryForObject(query, new Object[] {},
					(rs, rowNum) -> rs.getString(("RESULT")));
			JsonNode rows = mapper.readTree(row_string);
			return response(200, rows);

		} catch (Exception e) {
			data.put("message", e.getMessage());
			return response(500, data);
		}
	}

}