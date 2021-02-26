package vn.com.tpf.microservices.services;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class FinnoneService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public JdbcTemplate jdbcTemplateFicocen;

	@Autowired
	public JdbcTemplate jdbcTemplateFicoih;

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
			String query = String.format("SELECT * FROM  V_APP_INFO WHERE APPLICATION_NUMBER = '%s'",
					request.path("body").path("data").path("appId").asText());
			Object rowObjectNode = jdbcTemplateFicocen.queryForObject(query, new Object[] {}, (rs, rowNum) -> {
				ObjectNode row = mapper.createObjectNode();
				row.put("appId", rs.getString("APPLICATION_NUMBER"));
//				System.out.println("APPLICATION_NUMBER");
				row.put("status", rs.getString("STATUS").toUpperCase().replace(" ", "_"));
//				System.out.println("STATUS");
				row.put("stage", rs.getString("STAGE").toUpperCase().replace(" ", "_"));
//				System.out.println("STAGE");
				row.put("reasonCode", rs.getString("REASON_CODE"));
				row.put("reasonCodeValue", rs.getString("REASON_CODE_VALUE"));
//				System.out.println("REASON_CODE");
				row.put("reasonDetail", rs.getString("REASON_DETAIL"));
//				System.out.println("REASON_DETAIL");
				row.put("taxCode", rs.getString("TAX_CODE"));
//				System.out.println("TAX_CODE");
				row.put("finalProduct", rs.getString("FINAL_PRODUCT"));
//				System.out.println("FINAL_PRODUCT");
				row.put("tenor", rs.getDouble("TENOR"));
//				System.out.println("TENOR");
				row.put("loanAmount", rs.getLong("LOAN_AMOUNT"));
//				System.out.println("LOAN_AMOUNT");
				row.put("interestRate", rs.getDouble("INTEREST_RATE"));
//				System.out.println("INTEREST_RATE");
				row.put("loanAccount", rs.getString("LOAN_ACCOUNT"));
//				System.out.println("LOAN_ACCOUNT");
				row.put("disbursementDate", rs.getString("DISBURSAL_DATE"));
//				System.out.println("DISBURSAL_DATE");
				row.put("loanStatus", rs.getString("LOAN_STATUS"));
//				System.out.println("LOAN_STATUS");
				row.put("insurance", rs.getBoolean("INSURANCE"));
//				System.out.println("INSURANCE");
				row.put("courierCode", rs.getString("COURIER_CODE"));
//				System.out.println("COURIER_CODE");
				row.put("lastSubmitDate", rs.getString("LAST_SUBMIT_DATE"));
//				System.out.println("LAST_SUBMIT_DATE");
				row.put("lastUpdateDate", rs.getString("LAST_UPDATE_DATE"));
//				System.out.println("LAST_UPDATE_DATE");
				row.put("emi", rs.getDouble("EMI"));
//				System.out.println("EMI");
				row.put("userName", rs.getString("USER_NAME"));
				return row;
			});
			return response(200, mapper.convertValue(rowObjectNode, JsonNode.class));
		} catch (Exception e) {
			return response(500, mapper.createObjectNode().put("message", e.getMessage()));
		}
	}

	public JsonNode getDataFields(JsonNode request) {


		try {
			JsonNode apps = request.path("body").path("apps");
			
			String input = "";
			for (JsonNode app : apps) {
				input += String.format("%s,", app.path("appId").asText());
			}
	
			String query = String.format("SELECT * FROM TABLE(FN_GET_DATA_API_F1('%s'))",
					 StringUtils.removeEnd(input, ","));

			
			List<JsonNode> output = jdbcTemplate.query(query, new Object[] {},
					(rs, rowNum) -> {
						ObjectNode row = mapper.createObjectNode();
						row.put("appId", rs.getString("APPLICATION_NUMBER") != null ? rs.getString("APPLICATION_NUMBER"): "*");

						row.put("fullName", rs.getString("CUSTOMER_NAME")!= null ? rs.getString("CUSTOMER_NAME"): "*");

						row.put("phone", rs.getString("PHONE_NUMBER")!= null ? rs.getString("PHONE_NUMBER"): "*");

						row.put("dateOfBirth", rs.getString("DOB")!= null ? rs.getString("DOB"): "*");
						row.put("sex", rs.getString("GENDER")!= null ? rs.getString("GENDER"): "*");

						row.put("nationalId", rs.getString("CUST_ICARD_NO")!= null ? rs.getString("CUST_ICARD_NO"): "*");

						row.put("bankCard", rs.getString("BANK_CARD")!= null ? rs.getString("BANK_CARD"): "*");

						row.put("spouseName", rs.getString("MEMBER_NAME1")!= null ? rs.getString("MEMBER_NAME1"): "*");

						row.put("spouseIdCard", rs.getString("MEM_ICARD_NO")!= null ? rs.getString("MEM_ICARD_NO"): "*");

						row.put("spousePhone", rs.getString("PHONE_NUMBER_FM")!= null ? rs.getString("PHONE_NUMBER_FM"): "*");

						row.put("homeAddress", rs.getString("HOME_ADDRESS")!= null ? rs.getString("HOME_ADDRESS"): "*");

						row.put("cityCodeHome", rs.getString("HOME_CITY")!= null ? rs.getString("HOME_CITY"): "*");

						row.put("districtCodeHome", rs.getString("HOME_DISTRICT")!= null ? rs.getString("HOME_DISTRICT"): "*");

						row.put("homeCom", rs.getString("COMP_ADDRESS")!= null ? rs.getString("COMP_ADDRESS"): "*");

						row.put("cityCodeCom", rs.getString("COMP_CITY")!= null ? rs.getString("COMP_CITY"): "*");

						row.put("districtCodeCom", rs.getString("COMP_DISTRICT")!= null ? rs.getString("COMP_DISTRICT"): "*");

						row.put("comName", rs.getString("COMP_NAME")!= null ? rs.getString("COMP_NAME"): "*");

						row.put("position", rs.getString("COMP_POSITION")!= null ? rs.getString("COMP_POSITION"): "*");

						row.put("appStatus", rs.getString("APPL_STATUS")!= null ? rs.getString("APPL_STATUS"): "*");

						row.put("appStage", rs.getString("APPL_STAGE")!= null ? rs.getString("APPL_STAGE"): "*");
						
						return row;
					}
			);

			return response(200, mapper.convertValue(output, JsonNode.class));
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

	public JsonNode getEducationField(JsonNode request) {
		ObjectNode data = mapper.createObjectNode();
		try {
			String query = String.format("SELECT serviceapp.fn_get_cus_edu('%s') RESULT FROM DUAL", request.path("custid").asText());
			String result = jdbcTemplateFicoih.queryForObject(query, String.class);

			log.info("getEducationField: " + result);

			if(StringUtils.isEmpty(result))
			{
				result="Others";
			}

			data.put("description", result);
			return response(200, data);

		} catch (Exception e) {
			data.put("description", "Others");
			log.info("getEducationField: " + e.toString());
			return response(200, data);
		}
	}


}