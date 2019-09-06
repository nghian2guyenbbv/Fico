package vn.com.tpf.microservices.services;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.com.tpf.microservices.models.Momo;
import vn.com.tpf.microservices.models.ResponseMomoDisburse;
import vn.com.tpf.microservices.models.ResponseMomoStatus;
import vn.com.tpf.microservices.models.ResponseMomoStatusDetail;

@Service
public class MomoService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private ObjectNode error;

	private ObjectNode status;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private ApiService apiService;

	@Autowired
	private ConvertService convertService;

	@Autowired
	private RabbitMQService rabbitMQService;

	@Autowired
	private MongoTemplate mongoTemplate;

	@PostConstruct
	private void init() {
		status = mapper.createObjectNode();
		status.put("APPROVED", 0);
		status.put("PROCESSING", 1);
		status.put("APPROVED_AND_WAITING_SMS", 2);
		status.put("DISBURSED", 4);
		status.put("CANCELLED", 5);
		status.put("REJECTED", 6);
		status.put("LIQUIDATION", 7);

		error = mapper.createObjectNode();
		Map<?, ?> requestId = Map.of("code", 100, "message", "requestId is required string and not empty");
		error.set("requestId", mapper.convertValue(requestId, JsonNode.class));
		Map<?, ?> dateTime = Map.of("code", 101, "message", "dateTime is required string and not empty");
		error.set("dateTime", mapper.convertValue(dateTime, JsonNode.class));
		Map<?, ?> cityNotExists = Map.of("code", 102, "message", "city not exits");
		error.set("cityNotExists", mapper.convertValue(cityNotExists, JsonNode.class));
		Map<?, ?> districtNotExists = Map.of("code", 103, "message", "district not exits");
		error.set("districtNotExists", mapper.convertValue(districtNotExists, JsonNode.class));
		Map<?, ?> agree1 = Map.of("code", 104, "message", "agree1 is required boolean");
		error.set("agree1", mapper.convertValue(agree1, JsonNode.class));
		Map<?, ?> agree2 = Map.of("code", 105, "message", "agree2 is required boolean");
		error.set("agree2", mapper.convertValue(agree2, JsonNode.class));
		Map<?, ?> agree3 = Map.of("code", 106, "message", "agree3 is required boolean");
		error.set("agree3", mapper.convertValue(agree3, JsonNode.class));
		Map<?, ?> agree4 = Map.of("code", 107, "message", "agree4 is required boolean");
		error.set("agree4", mapper.convertValue(agree4, JsonNode.class));
		Map<?, ?> insurrance = Map.of("code", 108, "message", "insurrance is required boolean");
		error.set("insurrance", mapper.convertValue(insurrance, JsonNode.class));
		Map<?, ?> amount = Map.of("code", 109, "message", "amount is required number");
		error.set("amount", mapper.convertValue(amount, JsonNode.class));
		Map<?, ?> dueDate = Map.of("code", 110, "message", "dueDate is required number");
		error.set("dueDate", mapper.convertValue(dueDate, JsonNode.class));
		Map<?, ?> fee = Map.of("code", 111, "message", "fee is required number");
		error.set("fee", mapper.convertValue(fee, JsonNode.class));
		Map<?, ?> loanTime = Map.of("code", 112, "message", "loanTime is required number");
		error.set("loanTime", mapper.convertValue(loanTime, JsonNode.class));
		Map<?, ?> salary = Map.of("code", 113, "message", "salary is required number");
		error.set("salary", mapper.convertValue(salary, JsonNode.class));
		Map<?, ?> address1 = Map.of("code", 114, "message", "address1 is required string and not empty");
		error.set("address1", mapper.convertValue(address1, JsonNode.class));
		Map<?, ?> address2 = Map.of("code", 115, "message", "address2 is required string and not empty");
		error.set("address2", mapper.convertValue(address2, JsonNode.class));
		Map<?, ?> district = Map.of("code", 116, "message", "district is required string and not empty");
		error.set("district", mapper.convertValue(district, JsonNode.class));
		Map<?, ?> city = Map.of("code", 117, "message", "city is required string and not empty");
		error.set("city", mapper.convertValue(city, JsonNode.class));
		Map<?, ?> dateOfBirth = Map.of("code", 118, "message", "dateOfBirth is required string format dd/MM/yyyy");
		error.set("dateOfBirth", mapper.convertValue(dateOfBirth, JsonNode.class));
		Map<?, ?> email = Map.of("code", 119, "message", "email is required string and not empty");
		error.set("email", mapper.convertValue(email, JsonNode.class));
		Map<?, ?> firstName = Map.of("code", 120, "message", "firstName is required string and not empty");
		error.set("firstName", mapper.convertValue(firstName, JsonNode.class));
		Map<?, ?> lastName = Map.of("code", 121, "message", "lastName is required string and not empty");
		error.set("lastName", mapper.convertValue(lastName, JsonNode.class));
		Map<?, ?> middleName = Map.of("code", 122, "message", "middleName is required string");
		error.set("middleName", mapper.convertValue(middleName, JsonNode.class));
		Map<?, ?> gender = Map.of("code", 123, "message", "gender is required male or female");
		error.set("gender", mapper.convertValue(gender, JsonNode.class));
		Map<?, ?> issueDate = Map.of("code", 124, "message", "issueDate is required string format dd/MM/yyyy");
		error.set("issueDate", mapper.convertValue(issueDate, JsonNode.class));
		Map<?, ?> issuePlace = Map.of("code", 125, "message", "issuePlace is required string and not empty");
		error.set("issuePlace", mapper.convertValue(issuePlace, JsonNode.class));
		Map<?, ?> maritalStatus = Map.of("code", 126, "message", "maritalStatus is required string and not empty");
		error.set("maritalStatus", mapper.convertValue(maritalStatus, JsonNode.class));
		Map<?, ?> momoLoanId = Map.of("code", 127, "message", "momoLoanId is required string and not empty");
		error.set("momoLoanId", mapper.convertValue(momoLoanId, JsonNode.class));
		Map<?, ?> personalId = Map.of("code", 128, "message", "personalId is required string and not empty");
		error.set("personalId", mapper.convertValue(personalId, JsonNode.class));
		Map<?, ?> phoneNumber = Map.of("code", 129, "message", "phoneNumber is required string. Ex: +84123456789");
		error.set("phoneNumber", mapper.convertValue(phoneNumber, JsonNode.class));
		Map<?, ?> ward = Map.of("code", 130, "message", "ward is required string and not empty");
		error.set("ward", mapper.convertValue(ward, JsonNode.class));
		Map<?, ?> productCode = Map.of("code", 131, "message", "productCode is required string and not empty");
		error.set("productCode", mapper.convertValue(productCode, JsonNode.class));
		Map<?, ?> photos = Map.of("code", 132, "message", "photos is required array and not empty");
		error.set("photos", mapper.convertValue(photos, JsonNode.class));
		Map<?, ?> references = Map.of("code", 133, "message", "references is required array and not empty");
		error.set("references", mapper.convertValue(references, JsonNode.class));
	}

	private JsonNode validation(JsonNode body, List<String> fields) {
		for (String field : fields) {
			if (field.equals("requestId")
					&& (!body.path("requestId").isTextual() || body.path("requestId").asText().isEmpty())) {
				return error.get("requestId");
			}
			if (field.equals("data.agree1") && !body.path("data").path("agree1").isBoolean()) {
				return error.get("agree1");
			}
			if (field.equals("data.agree2") && !body.path("data").path("agree2").isBoolean()) {
				return error.get("agree2");
			}
			if (field.equals("data.agree3") && !body.path("data").path("agree3").isBoolean()) {
				return error.get("agree3");
			}
			if (field.equals("data.agree4") && !body.path("data").path("agree4").isBoolean()) {
				return error.get("agree4");
			}
			if (field.equals("data.insurrance") && !body.path("data").path("insurrance").isBoolean()) {
				return error.get("insurrance");
			}
			if (field.equals("data.amount") && !body.path("data").path("amount").isNumber()) {
				return error.get("amount");
			}
			if (field.equals("data.dueDate") && !body.path("data").path("dueDate").isNumber()) {
				return error.get("dueDate");
			}
			if (field.equals("data.fee") && !body.path("data").path("fee").isNumber()) {
				return error.get("fee");
			}
			if (field.equals("data.loanTime") && !body.path("data").path("loanTime").isNumber()) {
				return error.get("loanTime");
			}
			if (field.equals("data.salary") && !body.path("data").path("salary").isNumber()) {
				return error.get("salary");
			}
			if (field.equals("data.address1") && (!body.path("data").path("address1").isTextual()
					|| body.path("data").path("address1").asText().isEmpty())) {
				return error.get("address1");
			}
			if (field.equals("data.address2") && (!body.path("data").path("address2").isTextual()
					|| body.path("data").path("address2").asText().isEmpty())) {
				return error.get("address2");
			}
			if (field.equals("data.district") && (!body.path("data").path("district").isTextual()
					|| body.path("data").path("district").asText().isEmpty())) {
				return error.get("district");
			}
			if (field.equals("data.city")
					&& (!body.path("data").path("city").isTextual() || body.path("data").path("city").asText().isEmpty())) {
				return error.get("city");
			}
			if (field.equals("data.dateOfBirth") && !body.path("data").path("dateOfBirth").asText()
					.matches("^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}$")) {
				return error.get("dateOfBirth");
			}
			if (field.equals("data.email")
					&& (!body.path("data").path("email").isTextual() || body.path("data").path("email").asText().isEmpty())) {
				return error.get("email");
			}
			if (field.equals("data.firstName") && (!body.path("data").path("firstName").isTextual()
					|| body.path("data").path("firstName").asText().isEmpty())) {
				return error.get("firstName");
			}
			if (field.equals("data.lastName") && (!body.path("data").path("lastName").isTextual()
					|| body.path("data").path("lastName").asText().isEmpty())) {
				return error.get("lastName");
			}
			if (field.equals("data.middleName") && !body.path("data").path("middleName").isTextual()) {
				return error.get("middleName");
			}
			if (field.equals("data.gender")
					&& !body.path("data").path("gender").asText().toLowerCase().matches("^(male|female)$")) {
				return error.get("gender");
			}
			if (field.equals("data.issueDate") && !body.path("data").path("issueDate").asText()
					.matches("^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}$")) {
				return error.get("issueDate");
			}
			if (field.equals("data.issuePlace") && (!body.path("data").path("issuePlace").isTextual()
					|| body.path("data").path("issuePlace").asText().isEmpty())) {
				return error.get("issuePlace");
			}
			if (field.equals("data.maritalStatus") && (!body.path("data").path("maritalStatus").isTextual()
					|| body.path("data").path("maritalStatus").asText().isEmpty())) {
				return error.get("maritalStatus");
			}
			if (field.equals("data.momoLoanId") && (!body.path("data").path("momoLoanId").isTextual()
					|| body.path("data").path("momoLoanId").asText().isEmpty())) {
				return error.get("momoLoanId");
			}
			if (field.equals("data.personalId")
					&& !body.path("data").path("personalId").asText().matches("^(?=(?:.{9}|.{12})$)[0-9]*$")) {
				return error.get("personalId");
			}
			if (field.equals("data.phoneNumber")
					&& !body.path("data").path("phoneNumber").asText().matches("^(\\+84)(?=(?:.{9}|.{10})$)[0-9]*$")) {
				return error.get("phoneNumber");
			}
			if (field.equals("data.ward")
					&& (!body.path("data").path("ward").isTextual() || body.path("data").path("ward").asText().isEmpty())) {
				return error.get("ward");
			}
			if (field.equals("data.productCode") && (!body.path("data").path("productCode").isTextual()
					|| body.path("data").path("productCode").asText().isEmpty())) {
				return error.get("productCode");
			}
			if (field.equals("data.photos")
					&& (!body.path("data").path("photos").isArray() || body.path("data").path("photos").size() == 0)) {
				return error.get("photos");
			}
			if (field.equals("data.references")
					&& (!body.path("data").path("references").isArray() || body.path("data").path("references").size() == 0)) {
				return error.get("references");
			}
		}
		return null;
	}

	private JsonNode response(int status, JsonNode data) {
		ObjectNode response = mapper.createObjectNode();
		response.put("status", status).set("data", data);
		return response;
	}

	private JsonNode response(int code, JsonNode body, JsonNode data) {
		ObjectNode response = mapper.createObjectNode();
		ObjectNode res = mapper.createObjectNode();
		res.put("resultCode", code);
		res.put("requestId", body.path("requestId").asText());
		res.put("referenceId", body.path("referenceId").asText());
		if (code == 0) {
			res.set("data", data);
		} else {
			res.set("message", data.path("message"));
		}
		response.put("status", 200).set("data", res);
		return response;
	}

	private void rabbitLog(JsonNode body, JsonNode data) {
		ObjectNode dataLog = mapper.createObjectNode();
		dataLog.put("type", "[==RABBITMQ-LOG==]");
		dataLog.set("result", data);
		dataLog.set("payload", body);
		log.info("{}", dataLog);
	}

	public JsonNode getDetail(JsonNode request) throws Exception {
		Momo momo = mongoTemplate.findOne(Query.query(Criteria.where("id").is(request.path("param").path("id").asText())),
				Momo.class);

		if (momo == null) {
			return response(404, mapper.createObjectNode().put("message", "Not Found"));
		}

		return response(200, mapper.convertValue(momo, JsonNode.class));
	}

	public JsonNode createMomo(JsonNode request) throws Exception {
		JsonNode body = request.path("body");
		JsonNode valid = validation(body,
				Arrays.asList("requestId", "data.agree1", "data.agree2", "data.agree3", "data.agree4", "data.insurrance",
						"data.amount", "data.dueDate", "data.fee", "data.loanTime", "data.salary", "data.address1", "data.address2",
						"data.district", "data.city", "data.dateOfBirth", "data.email", "data.firstName", "data.lastName",
						"data.middleName", "data.gender", "data.issueDate", "data.issuePlace", "data.maritalStatus",
						"data.momoLoanId", "data.personalId", "data.phoneNumber", "data.ward", "data.productCode", "data.photos",
						"data.references"));

		if (valid != null) {
			return response(valid.get("code").asInt(), body, mapper.createObjectNode().set("message", valid.get("message")));
		}

		JsonNode data = body.path("data");
		JsonNode address = rabbitMQService.sendAndReceive("tpf-service-assets",
				Map.of("func", "getAddress", "token", "Bearer " + rabbitMQService.getToken().path("access_token").asText(),
						"param", Map.of("areaCode", data.path("district").asText())));

		if (address.path("status").asInt() != 200) {
			return response(error.get("districtNotExists").get("code").asInt(), body,
					mapper.createObjectNode().set("message", error.get("districtNotExists").get("message")));
		}
		if (!address.path("data").path("postCode").asText().equals(data.path("city").asText())) {
			return response(error.get("cityNotExists").get("code").asInt(), body,
					mapper.createObjectNode().set("message", error.get("cityNotExists").get("message")));
		}

		Momo momo = mapper.convertValue(data, Momo.class);
		momo.getPhotos().forEach(e -> e.setUpdatedAt(new Date()));
		momo.setCity(address.path("data").path("cityName").asText());
		momo.setDistrict(address.path("data").path("areaName").asText());
		momo.setRegion(address.path("data").path("region").asText());
		mongoTemplate.save(momo);

		rabbitMQService.send("tpf-service-esb",
				Map.of("func", "createApp", "token", "Bearer " + rabbitMQService.getToken().path("access_token").asText(),
						"param", Map.of("request_id", body.path("requestId"), "reference_id", body.path("referenceId")), "body",
						convertService.toAppFinnone(momo)));

		rabbitMQService.send("tpf-service-app",
				Map.of("func", "createApp", "token", "Bearer " + rabbitMQService.getToken().path("access_token").asText(),
						"body", convertService.toAppDisplay(momo)));

		return response(0, body, mapper.convertValue(momo, JsonNode.class));
	}

	public JsonNode updateAutomationResult(JsonNode request) throws Exception {
		JsonNode body = request.path("body");

		Assert.isTrue(body.path("momo_loan_id").isTextual() && !body.path("momo_loan_id").asText().isEmpty(),
				"momo_loan_id is required and not empty");
		Assert.isTrue(body.path("app_id").isTextual() && !body.path("app_id").asText().isEmpty(),
				"app_id is required and not empty");
		Assert.isTrue(body.path("automation_result").isTextual() && !body.path("automation_result").asText().isEmpty(),
				"automation_result is required and not empty");
		Assert.isTrue(body.path("access_key").isTextual() && !body.path("access_key").asText().isEmpty(),
				"access_key is required and not empty");

		if (!body.path("access_key").asText().equals("access_key_db")) {
			return response(401, mapper.createObjectNode().put("message", "Unauthorized"));
		}

		Query query = Query.query(Criteria.where("momoLoanId").is(body.path("momo_loan_id").asText()));
		Update update = new Update().set("appId", body.path("app_id").asText())
				.set("automationResult", body.path("automation_result").asText()).set("status", "PROCESSING");

		Momo momo = mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), Momo.class);

		if (momo == null) {
			return response(404, mapper.createObjectNode().put("message", "Momo Loan Id Not Found"));
		}

		rabbitMQService.send("tpf-service-app",
				Map.of("func", "updateApp", "token", "Bearer " + rabbitMQService.getToken().path("access_token").asText(),
						"param", Map.of("project", "momo", "id", momo.getId()), "body", convertService.toAppDisplay(momo)));

		return response(200, null);
	}

	public JsonNode updateStatus(JsonNode request) throws Exception {
		JsonNode body = request.path("body");

		Assert.isTrue(body.path("momo_loan_id").isTextual() && !body.path("momo_loan_id").asText().isEmpty(),
				"momo_loan_id is required and not empty");
		Assert.isTrue(body.path("status").isTextual() && !body.path("status").asText().isEmpty(),
				"status is required and not empty");
		Assert.isTrue(body.path("access_key").isTextual() && !body.path("access_key").asText().isEmpty(),
				"access_key is required and not empty");

		if (!body.path("access_key").asText().equals("access_key_db")) {
			return response(401, mapper.createObjectNode().put("message", "Unauthorized"));
		}

		Momo momo = mongoTemplate.findOne(Query.query(Criteria.where("momoLoanId").is(body.path("momo_loan_id").asText())),
				Momo.class);

		if (momo == null) {
			return response(404, mapper.createObjectNode().put("message", "Momo Loan Id Not Found"));
		}

		if (momo.getAppId() == null) {
			return response(404, mapper.createObjectNode().put("message", "AppId Not Found"));
		}

		if (body.path("status").asText().equals("REJECTED") || body.path("status").asText().equals("CANCELLED")) {
			new Thread(() -> {
				try {
					JsonNode reason = rabbitMQService.sendAndReceive("tpf-service-esb",
							Map.of("func", "getReason", "token", "Bearer " + rabbitMQService.getToken().path("access_token").asText(),
									"param", Map.of("appId", momo.getAppId(), "status", body.path("status").asText())));
					if (reason.path("status").asInt() == 200) {
						ResponseMomoStatus momoStatus = mapper.convertValue(reason.path("data"), ResponseMomoStatus.class);
						momoStatus.setRequestId(body.path("requestId").asText());
						momoStatus.setMomoLoanId(momo.getMomoLoanId());
						momoStatus.setPhoneNumber(momo.getPhoneNumber());
						momoStatus.setStatus(status.path(body.path("status").asText()).asInt());
						if (momoStatus.getDetail() == null) {
							momoStatus.setDetail(new ResponseMomoStatusDetail());
						}
						momoStatus.getDetail().setApplicationId(momo.getAppId());
						apiService.sendStatusToMomo(mapper.convertValue(momoStatus, ObjectNode.class));
					} else {
						rabbitLog(body, reason);
					}
				} catch (Exception e) {
					log.error(e.getMessage());
				}
			}).start();
		} else if (body.path("status").asText().equals("APPROVED")) {
			JsonNode sms = rabbitMQService.sendAndReceive("tpf-service-sms", Map.of("func", "sendSms", "token",
					"Bearer " + rabbitMQService.getToken().path("access_token").asText(), "body",
					Map.of("phone", momo.getPhoneNumber(), "content", "Chuc mung khach hang, CMND " + momo.getPersonalId()
							+ " duoc phe duyet voi tong so tien vay Sanction Loan Amount vnd, thoi han vay Sanction Tenure va khoan tra moi thang Installment Amount. Vui long tra loi tin nhan voi cu phap TPB MOMO Y den so 8089 de xac nhan khoan vay hoac TPB MOMO N de tu choi khoan vay")));
			if (sms.path("status").asInt() == 200) {
				momo.setSmsResult("W");
				new Thread(() -> {
					try {
						JsonNode loan = rabbitMQService.sendAndReceive("tpf-service-esb",
								Map.of("func", "getLoan", "token", "Bearer " + rabbitMQService.getToken().path("access_token").asText(),
										"param", Map.of("appId", momo.getAppId())));
						if (loan.path("status").asInt() == 200) {
							ResponseMomoStatus momoStatus = mapper.convertValue(loan.path("data"), ResponseMomoStatus.class);
							momoStatus.setRequestId(body.path("requestId").asText());
							momoStatus.setMomoLoanId(momo.getMomoLoanId());
							momoStatus.setPhoneNumber(momo.getPhoneNumber());
							momoStatus.setStatus(status.path("APPROVED_AND_WAITING_SMS").asInt());
							if (momoStatus.getDetail() == null) {
								momoStatus.setDetail(new ResponseMomoStatusDetail());
							}
							momoStatus.getDetail().setApplicationId(momo.getAppId());
							apiService.sendStatusToMomo(mapper.convertValue(momoStatus, ObjectNode.class));
						} else {
							rabbitLog(body, loan);
						}
					} catch (Exception e) {
						log.error(e.getMessage());
					}
				}).start();
			} else {
				momo.setSmsResult("send sms error");
				rabbitLog(body, sms);
			}
		} else if (body.path("status").asText().equals("DISBURSED")) {
			new Thread(() -> {
				try {
					JsonNode loan = rabbitMQService.sendAndReceive("tpf-service-esb",
							Map.of("func", "getLoan", "token", "Bearer " + rabbitMQService.getToken().path("access_token").asText(),
									"param", Map.of("appId", momo.getAppId())));
					if (loan.path("status").asInt() == 200) {
						ResponseMomoStatus momoStatus = mapper.convertValue(loan.path("data"), ResponseMomoStatus.class);
						momoStatus.setRequestId(body.path("requestId").asText());
						momoStatus.setMomoLoanId(momo.getMomoLoanId());
						momoStatus.setPhoneNumber(momo.getPhoneNumber());
						momoStatus.setStatus(status.path(body.path("status").asText()).asInt());
						if (momoStatus.getDetail() == null) {
							momoStatus.setDetail(new ResponseMomoStatusDetail());
						}
						momoStatus.getDetail().setApplicationId(momo.getAppId());
						apiService.sendStatusToMomo(mapper.convertValue(momoStatus, ObjectNode.class));
					} else {
						rabbitLog(body, loan);
					}
				} catch (Exception e) {
					log.error(e.getMessage());
				}
			}).start();
		}

		momo.setStatus(body.path("status").asText());
		momo.setAutomationResult(momo.getAutomationResult().equals("Pass") ? momo.getAutomationResult() : "Fix");
		mongoTemplate.save(momo);

		rabbitMQService.send("tpf-service-app",
				Map.of("func", "updateApp", "token", "Bearer " + rabbitMQService.getToken().path("access_token").asText(),
						"param", Map.of("project", "momo", "id", momo.getId()), "body", convertService.toAppDisplay(momo)));

		return response(200, null);
	}

	public JsonNode updateSmsResult(JsonNode request) throws Exception {
		JsonNode body = request.path("body");

		Assert.isTrue(body.path("phone_number").isTextual() && !body.path("phone_number").asText().isEmpty(),
				"phone_number is required and not empty");
		Assert.isTrue(body.path("sms_result").isTextual() && !body.path("sms_result").asText().isEmpty(),
				"sms_result is required and not empty");
		Assert.isTrue(body.path("access_key").isTextual() && !body.path("access_key").asText().isEmpty(),
				"access_key is required and not empty");

		if (!body.path("access_key").asText().equals("access_key_db")) {
			return response(401, mapper.createObjectNode().put("message", "Unauthorized"));
		}

		Query query = Query.query(Criteria.where("phoneNumber").is(body.path("phone_number").asText()));
		query.with(Sort.by(Direction.DESC, "createdAt")).limit(1);

		List<Momo> list = mongoTemplate.find(query, Momo.class);

		if (list.size() == 0) {
			return response(404, mapper.createObjectNode().put("message", "Phone Number Not Found"));
		}

		if (list.get(0).getAppId() == null) {
			return response(404, mapper.createObjectNode().put("message", "AppId Not Found"));
		}

		Momo momo = list.get(0);

		if (body.path("sms_result").asText().equals("SMS_Y")) {
			momo.setSmsResult("Y");
			new Thread(() -> {
				try {
					JsonNode loan = rabbitMQService.sendAndReceive("tpf-service-esb",
							Map.of("func", "getLoan", "token", "Bearer " + rabbitMQService.getToken().path("access_token").asText(),
									"param", Map.of("appId", momo.getAppId())));
					if (loan.path("status").asInt() == 200) {
						ResponseMomoDisburse momoDisburse = mapper.convertValue(loan.path("data"), ResponseMomoDisburse.class);
						momoDisburse.setRequestId(body.path("requestId").asText());
						momoDisburse.setMomoLoanId(momo.getMomoLoanId());
						momoDisburse.setPhoneNumber(momo.getPhoneNumber());
						momoDisburse.setDescription("Khách hàng được giải ngân");
						apiService.sendDisburseToMomo(mapper.convertValue(momoDisburse, ObjectNode.class));
					} else {
						rabbitLog(body, loan);
					}
				} catch (Exception e) {
					log.error(e.getMessage());
				}
			}).start();
		} else if (body.path("sms_result").asText().equals("SMS_N")) {
			momo.setSmsResult("N");
			new Thread(() -> {
				ResponseMomoStatus momoStatus = new ResponseMomoStatus();
				momoStatus.setRequestId(body.path("requestId").asText());
				momoStatus.setMomoLoanId(momo.getMomoLoanId());
				momoStatus.setPhoneNumber(momo.getPhoneNumber());
				momoStatus.setStatus(status.path("CANCELLED").asInt());
				momoStatus.setDescription("Khách hàng SMS N");
				apiService.sendStatusToMomo(mapper.convertValue(momoStatus, ObjectNode.class));
			}).start();
		} else if (body.path("sms_result").asText().equals("SMS_F")) {
			momo.setSmsResult("F");
		}

		mongoTemplate.save(momo);

		rabbitMQService.send("tpf-service-app",
				Map.of("func", "updateApp", "token", "Bearer " + rabbitMQService.getToken().path("access_token").asText(),
						"param", Map.of("project", "momo", "id", momo.getId()), "body", convertService.toAppDisplay(momo)));

		return response(200, null);
	}
}