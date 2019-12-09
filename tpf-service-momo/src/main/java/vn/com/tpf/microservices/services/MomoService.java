package vn.com.tpf.microservices.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.com.tpf.microservices.models.Momo;
import vn.com.tpf.microservices.models.ResponseMomoDisburse;
import vn.com.tpf.microservices.models.ResponseMomoStatus;
import vn.com.tpf.microservices.models.ResponseMomoStatusDetail;

@Service
public class MomoService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final String SMS_Y = "TPB MOMO Y";
	private final String SMS_N = "TPB MOMO N";

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
		Map<?, ?> requestId = Map.of("code", 2, "message", "request_id");
		error.set("requestId", mapper.convertValue(requestId, JsonNode.class));
//		Map<?, ?> dateTime = Map.of("code", 101, "message", "date_time is required string and not empty");
//		error.set("dateTime", mapper.convertValue(dateTime, JsonNode.class));
		Map<?, ?> cityNotExists = Map.of("code", 2, "message", "city");
		error.set("cityNotExists", mapper.convertValue(cityNotExists, JsonNode.class));
		Map<?, ?> districtNotExists = Map.of("code", 2, "message", "district");
		error.set("districtNotExists", mapper.convertValue(districtNotExists, JsonNode.class));
		Map<?, ?> agree1 = Map.of("code", 2, "message", "agree1");
		error.set("agree1", mapper.convertValue(agree1, JsonNode.class));
		Map<?, ?> agree2 = Map.of("code", 2, "message", "agree2");
		error.set("agree2", mapper.convertValue(agree2, JsonNode.class));
		Map<?, ?> agree3 = Map.of("code", 2, "message", "agree3");
		error.set("agree3", mapper.convertValue(agree3, JsonNode.class));
		Map<?, ?> agree4 = Map.of("code", 2, "message", "agree4");
		error.set("agree4", mapper.convertValue(agree4, JsonNode.class));
		Map<?, ?> insurrance = Map.of("code", 2, "message", "insurrance");
		error.set("insurrance", mapper.convertValue(insurrance, JsonNode.class));
		Map<?, ?> amount = Map.of("code", 2, "message", "amount");
		error.set("amount", mapper.convertValue(amount, JsonNode.class));
		Map<?, ?> dueDate = Map.of("code", 2, "message", "dueDate");
		error.set("dueDate", mapper.convertValue(dueDate, JsonNode.class));
		Map<?, ?> fee = Map.of("code", 2, "message", "fee");
		error.set("fee", mapper.convertValue(fee, JsonNode.class));
		Map<?, ?> loanTime = Map.of("code", 2, "message", "loanTime");
		error.set("loanTime", mapper.convertValue(loanTime, JsonNode.class));
		Map<?, ?> salary = Map.of("code", 2, "message", "salary");
		error.set("salary", mapper.convertValue(salary, JsonNode.class));
		Map<?, ?> address1 = Map.of("code", 2, "message", "address1");
		error.set("address1", mapper.convertValue(address1, JsonNode.class));
		Map<?, ?> address2 = Map.of("code", 2, "message", "address2");
		error.set("address2", mapper.convertValue(address2, JsonNode.class));
		Map<?, ?> district = Map.of("code", 2, "message", "district");
		error.set("district", mapper.convertValue(district, JsonNode.class));
		Map<?, ?> city = Map.of("code", 2, "message", "city");
		error.set("city", mapper.convertValue(city, JsonNode.class));
		Map<?, ?> dateOfBirth = Map.of("code", 2, "message", "dateOfBirth");
		error.set("dateOfBirth", mapper.convertValue(dateOfBirth, JsonNode.class));
		Map<?, ?> email = Map.of("code", 2, "message", "email");
		error.set("email", mapper.convertValue(email, JsonNode.class));
		Map<?, ?> firstName = Map.of("code", 2, "message", "firstName");
		error.set("firstName", mapper.convertValue(firstName, JsonNode.class));
		Map<?, ?> lastName = Map.of("code", 2, "message", "lastName");
		error.set("lastName", mapper.convertValue(lastName, JsonNode.class));
		Map<?, ?> middleName = Map.of("code", 2, "message", "middleName");
		error.set("middleName", mapper.convertValue(middleName, JsonNode.class));
		Map<?, ?> gender = Map.of("code", 2, "message", "gender");
		error.set("gender", mapper.convertValue(gender, JsonNode.class));
		Map<?, ?> issueDate = Map.of("code", 2, "message", "issueDate");
		error.set("issueDate", mapper.convertValue(issueDate, JsonNode.class));
		Map<?, ?> issuePlace = Map.of("code", 2, "message", "issuePlace");
		error.set("issuePlace", mapper.convertValue(issuePlace, JsonNode.class));
		Map<?, ?> maritalStatus = Map.of("code", 2, "message", "maritalStatus");
		error.set("maritalStatus", mapper.convertValue(maritalStatus, JsonNode.class));
		Map<?, ?> momoLoanId = Map.of("code", 2, "message", "momoLoanId");
		error.set("momoLoanId", mapper.convertValue(momoLoanId, JsonNode.class));
		Map<?, ?> personalId = Map.of("code", 2, "message",
				"personalId");
		error.set("personalId", mapper.convertValue(personalId, JsonNode.class));
		Map<?, ?> phoneNumber = Map.of("code", 2, "message", "phoneNumber");
		error.set("phoneNumber", mapper.convertValue(phoneNumber, JsonNode.class));
		Map<?, ?> ward = Map.of("code", 2, "message", "ward");
		error.set("ward", mapper.convertValue(ward, JsonNode.class));
		Map<?, ?> productCode = Map.of("code", 2, "message", "productCode");
		error.set("productCode", mapper.convertValue(productCode, JsonNode.class));
		Map<?, ?> photos = Map.of("code", 2, "message",
				"photos");
		error.set("photos", mapper.convertValue(photos, JsonNode.class));
		Map<?, ?> references = Map.of("code", 2, "message",
				"references");
		error.set("references", mapper.convertValue(references, JsonNode.class));
	}

	private JsonNode validation(JsonNode body, List<String> fields) {
		for (String field : fields) {
			if (field.equals("request_id")
					&& (!body.path("request_id").isTextual() || body.path("request_id").asText().isEmpty())) {
				return error.get("requestId");
			}
			if (field.equals("date_time")
					&& (!body.path("date_time").isTextual() || body.path("date_time").asText().isEmpty())) {
				return error.get("dateTime");
			}
			if (field.equals("data.agree1") && !body.path("data").path("agree1").asBoolean()) {
				return error.get("agree1");
			}
			if (field.equals("data.agree2") && !body.path("data").path("agree2").asBoolean()) {
				return error.get("agree2");
			}
			if (field.equals("data.agree3") && !body.path("data").path("agree3").asBoolean()) {
				return error.get("agree3");
			}
			if (field.equals("data.agree4") && !body.path("data").path("agree4").asBoolean()) {
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
			if (field.equals("data.city") && (!body.path("data").path("city").isTextual()
					|| body.path("data").path("city").asText().isEmpty())) {
				return error.get("city");
			}
			if (field.equals("data.dateOfBirth") && !body.path("data").path("dateOfBirth").asText()
					.matches("^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}$")) {
				return error.get("dateOfBirth");
			}
			if (field.equals("data.email") && (!body.path("data").path("email").isTextual()
					|| body.path("data").path("email").asText().isEmpty())) {
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
			if (field.equals("data.gender") && !body.path("data").path("gender").asText().matches("^(Male|Female)$")) {
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
			if (field.equals("data.maritalStatus")
					&& !body.path("data").path("maritalStatus").asText().matches("^(Single|Married)$")) {
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
			if (field.equals("data.ward") && (!body.path("data").path("ward").isTextual()
					|| body.path("data").path("ward").asText().isEmpty())) {
				return error.get("ward");
			}
			if (field.equals("data.productCode") && (!body.path("data").path("productCode").isTextual()
					|| body.path("data").path("productCode").asText().isEmpty())) {
				return error.get("productCode");
			}
			if (field.equals("data.photos")
					&& (!body.path("data").path("photos").isArray() || body.path("data").path("photos").size() != 3)) {
				return error.get("photos");
			} else {
				ArrayNode photos = (ArrayNode) body.path("data").path("photos");
				for (JsonNode photo : photos)
					if (!photo.path("type").asText().matches("^(National ID Back|National ID Front|Selfie)$")
							|| !photo.path("link").asText().matches(
									"^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$"))
						return error.get("photos");
			}
			if (field.equals("data.references") && (!body.path("data").path("references").isArray()
					|| body.path("data").path("references").size() == 0)) {
				return error.get("references");
			} else {
				ArrayNode references = (ArrayNode) body.path("data").path("references");
				for (JsonNode reference : references) {
					if (!reference.path("relation").asText().matches("^(Colleague|Relative|Spouse)$"))
						return error.get("references");
					if (reference.path("phoneNumber").asText().isEmpty() || !reference.path("phoneNumber").asText().matches("^[0-9]{10}$"))  	
						return error.get("references");
					if (reference.path("relation").asText().equals("Spouse"))
						if (reference.path("personalId").asText().isEmpty()
								|| !reference.path("personalId").asText().matches("^(?=(?:.{9}|.{12})$)[0-9]*$"))
							return error.get("references");
				}
			}
		}
		return null;
	}

	private JsonNode response(int status, JsonNode data) {
		ObjectNode response = mapper.createObjectNode();
		response.put("status", status).set("data", data);
		return response;
	}
	
	private JsonNode response(int status, String messages) {
		ObjectNode response = mapper.createObjectNode();
		response.put("status", status).put("data", messages);
		return response;
	}

	private JsonNode response(int code, JsonNode body, JsonNode data) {
		ObjectNode response = mapper.createObjectNode();
		ObjectNode res = mapper.createObjectNode();
		res.put("result_code", code);
		res.put("request_id", body.path("request_id").asText());
		res.put("reference_id", body.path("reference_id").asText());
//		res.set("date_time", mapper.convertValue(new Date(), JsonNode.class));
		if (code == 0) {
			if (!Objects.isNull(data))
				res.set("data", data);
			response.put("status", 200).set("data", res);
		} else {
			res.set("message", data.path("message"));
			if (res.get("result_code").asInt() == 2)
				response.put("status", 400).set("data", res);
			else 
				response.put("status", 500).set("data", res);
		}
		
		return response;
	}

	public JsonNode getDetail(JsonNode request) throws Exception {
		Momo momo = mongoTemplate
				.findOne(Query.query(Criteria.where("id").is(request.path("param").path("id").asText())), Momo.class);

		if (momo == null) {
			return response(404, mapper.createObjectNode().put("message", "AppId Momo Not Found"));
		}

		return response(200, mapper.convertValue(momo, JsonNode.class));
	}

	public JsonNode createMomo(JsonNode request) throws Exception {
		JsonNode body = request.path("body");
		JsonNode valid = validation(body,
				Arrays.asList("request_id", "date_time", "data.agree1", "data.agree2", "data.agree3", "data.agree4",
						"data.insurrance", "data.amount", "data.dueDate", "data.fee", "data.loanTime", "data.salary",
						"data.address1", "data.address2", "data.district", "data.city", "data.dateOfBirth",
						"data.email", "data.firstName", "data.lastName", "data.middleName", "data.gender",
						"data.issueDate", "data.issuePlace", "data.maritalStatus", "data.momoLoanId", "data.personalId",
						"data.phoneNumber", "data.ward", "data.productCode", "data.photos", "data.references"));

		if (valid != null) {
			return response(valid.get("code").asInt(), body,
					mapper.createObjectNode().set("message", valid.get("message")));
		}

		JsonNode data = body.path("data");
		JsonNode address = rabbitMQService.sendAndReceive("tpf-service-assets",
				Map.of("func", "getAddress", "reference_id", body.path("reference_id"), "param",
						Map.of("areaCode", data.path("district").asText())));

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
		
		rabbitMQService.send("tpf-service-esb", Map.of("func", "createApp", "reference_id", body.path("reference_id"),
				"body", convertService.toAppFinnone(momo)));

		rabbitMQService.send("tpf-service-app", Map.of("func", "createApp", "reference_id", body.path("reference_id"),
				"body", convertService.toAppDisplay(momo)));

		return response(0, body, null);
	}

	public JsonNode updateAutomation(JsonNode request) throws Exception {
		JsonNode body = request.path("body");

		Assert.isTrue(body.path("transaction_id").isTextual() && !body.path("transaction_id").asText().isEmpty(),
				"transaction_id is required and not empty");
		Assert.isTrue(body.path("app_id").isTextual() && !body.path("app_id").asText().isEmpty(),
				"app_id is required and not empty");
		Assert.isTrue(body.path("automation_result").isTextual() && !body.path("automation_result").asText().isEmpty(),
				"automation_result is required and not empty");

		Query query = Query.query(Criteria.where("momoLoanId").is(body.path("transaction_id").asText()));
		Update update = new Update().set("appId", body.path("app_id").asText())
				.set("automationResult", body.path("automation_result").asText()).set("status", "PROCESSING");

		Momo momo = mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), Momo.class);

		if (momo == null) {
			return response(404, mapper.createObjectNode().put("message", "Momo Loan Id Not Found"));
		}

		rabbitMQService.send("tpf-service-app",
				Map.of("func", "updateApp", "reference_id", request.path("reference_id"), "param",
						Map.of("project", "momo", "id", momo.getId()), "body", convertService.toAppDisplay(momo)));

		return response(200, mapper.createObjectNode().put("message", "Update Automation Success"));
	}

	public JsonNode updateStatus(JsonNode request) throws Exception {
		JsonNode body = request.path("body");
		log.info(body.path("app_id").asText());
		Momo momo = mongoTemplate.findOne(Query.query(Criteria.where("appId").is(body.path("app_id").asText())),
				Momo.class);

		if (momo == null) {
			return response(404, mapper.createObjectNode().put("message", "AppId Momo Not Found"));
		}

		if (body.path("status").asText().equals("REJECTED") || body.path("status").asText().equals("CANCELLED")) {
			momo.setStatus(body.path("status").asText());
			new Thread(() -> {
				for (int i = 0; i < 10; i++) {
					try {
						JsonNode reason = rabbitMQService.sendAndReceive("tpf-service-esb",
								Map.of("func", "getReason", "reference_id", body.path("reference_id"), "param",
										Map.of("appId", momo.getAppId(), "status", body.path("status").asText())));
						if (reason.path("status").asInt() == 200) {
							ResponseMomoStatus momoStatus = mapper.convertValue(reason.path("data"),
									ResponseMomoStatus.class);
							momoStatus.setRequestId(body.path("reference_id").asText());
							momoStatus.setMomoLoanId(momo.getMomoLoanId());
							momoStatus.setPhoneNumber(momo.getPhoneNumber());
							momoStatus.setStatus(status.path(body.path("status").asText()).asInt());
							if (momoStatus.getDetail() == null) {
								momoStatus.setDetail(new ResponseMomoStatusDetail());
							}
							momoStatus.getDetail().setApplicationId(null);
							apiService.sendStatusToMomo(mapper.convertValue(momoStatus, JsonNode.class));
							break;
						} else
							Thread.sleep(20000);
					} catch (Exception e) {
						log.error(e.toString());
					}

				}

			}).start();
		} else if (body.path("status").asText().equals("APPROVED")) {
			momo.setStatus("APPROVED_FINNONE");
			momo.setSmsResult("W");
				new Thread(() -> {
					for (int i = 0; i < 10; i++) {
						try {
							JsonNode loan = rabbitMQService.sendAndReceive("tpf-service-esb",
									Map.of("func", "getLoan", "reference_id", body.path("reference_id"), "param",
											Map.of("appId", momo.getAppId())));
							if (loan.path("status").asInt() == 200) {
								rabbitMQService.sendAndReceive("tpf-service-sms", Map.of("func", "sendSms", "reference_id",
										body.path("reference_id"), "body",
										Map.of("phone", momo.getPhoneNumber(), "content", "Chuc mung khach hang, CMND "
												+ momo.getPersonalId()
												+ " duoc phe duyet voi tong so tien vay "+loan.path("data").path("totalAmount").asText()+" vnd, thoi han vay "+loan.path("data").path("detail").path("tenor").asText()+" va khoan tra moi thang "+loan.path("data").path("detail").path("emi").asText()+". Vui long tra loi tin nhan voi cu phap TPB MOMO Y den so 8089 de xac nhan khoan vay hoac TPB MOMO N de tu choi khoan vay")));
								ResponseMomoStatus momoStatus = mapper.convertValue(loan.path("data"),
										ResponseMomoStatus.class);
								momoStatus.setRequestId(body.path("reference_id").asText());
								momoStatus.setMomoLoanId(momo.getMomoLoanId());
								momoStatus.setPhoneNumber(momo.getPhoneNumber());
								momoStatus.setStatus(status.path("APPROVED_AND_WAITING_SMS").asInt());
								if (momoStatus.getDetail() == null) {
									momoStatus.setDetail(new ResponseMomoStatusDetail());
								}
								momoStatus.getDetail().setApplicationId(null);
								apiService.sendStatusToMomo(mapper.convertValue(momoStatus, JsonNode.class));
								break;
							} else
								Thread.sleep(20000);
						} catch (Exception e) {
							log.error(e.toString());
						}
					}
				}).start();
			
		} else if (body.path("status").asText().equals("DISBURSED")) {
			momo.setStatus(body.path("status").asText());
			new Thread(() -> {
				try {
					JsonNode loan = rabbitMQService.sendAndReceive("tpf-service-esb", Map.of("func", "getLoan",
							"reference_id", body.path("reference_id"), "param", Map.of("appId", momo.getAppId())));
					if (loan.path("status").asInt() == 200) {
						ResponseMomoStatus momoStatus = mapper.convertValue(loan.path("data"),
								ResponseMomoStatus.class);
						ResponseMomoDisburse momoDisburse = mapper.convertValue(loan.path("data"),
								ResponseMomoDisburse.class);
						momoStatus.setRequestId(body.path("reference_id").asText());
						momoStatus.setMomoLoanId(momo.getMomoLoanId());
						momoStatus.setPhoneNumber(momo.getPhoneNumber());
						momoStatus.setStatus(status.path(body.path("status").asText()).asInt());
						if (momoStatus.getDetail() == null) {
							momoStatus.setDetail(new ResponseMomoStatusDetail());
						}
						momoStatus.getDetail().setApplicationId(momoDisburse.getDetail().getLoanId());
						apiService.sendStatusToMomo(mapper.convertValue(momoStatus, JsonNode.class));
					}
				} catch (Exception e) {
					log.error(e.toString());
				}
			}).start();
		}

		momo.setAutomationResult(momo.getAutomationResult().equals("Pass") ? momo.getAutomationResult() : "Fix");
		mongoTemplate.save(momo);

		rabbitMQService.send("tpf-service-app", Map.of("func", "updateApp", "reference_id", body.path("reference_id"),
				"param", Map.of("project", "momo", "id", momo.getId()), "body", convertService.toAppDisplay(momo)));

		 return response(200, mapper.createObjectNode().put("message", "Update Status Success"));
	}

	public JsonNode updateSms(JsonNode request) throws Exception {
		JsonNode body = request.path("body");
		String smsResponse = "";

		Assert.isTrue(body.path("phone_number").isTextual() && !body.path("phone_number").asText().isEmpty(),
				"phone_number is required and not empty");
		Assert.isTrue(body.path("sms_result").isTextual() && !body.path("sms_result").asText().isEmpty(),
				"sms_result is required and not empty");
		if (!body.path("sms_result").asText().trim().toUpperCase().matches("^(TPB MOMO Y|TPB MOMO N)$")) {
			smsResponse = "Cam on Quy khach da phan hoi thong tin. TN nay khong hop le. LH: 1900636633";
			return response(404, smsResponse);
		}
		
		Query query = Query.query(Criteria.where("phoneNumber").is(body.path("phone_number").asText().replaceAll("^[0]", "+84")));
		query.with(Sort.by(Direction.DESC, "createdAt")).limit(1);

		List<Momo> list = mongoTemplate.find(query, Momo.class);

		if (list.size() == 0) {
			smsResponse = "Cam on Quy khach da phan hoi thong tin. TN nay khong hop le. LH: 1900636633";
			return response(404, smsResponse);
		}

		if (list.get(0).getAppId() == null) {
			smsResponse = "Cam on Quy khach da phan hoi thong tin. TN nay khong hop le. LH: 1900636633";
			return response(404, smsResponse);
		}

		Momo momo = list.get(0);
	
		if( momo.getSmsResult() == null || !momo.getSmsResult().equals("W") || !momo.getStatus().equals("APPROVED_FINNONE")) {
			smsResponse = "Cam on Quy khach da phan hoi thong tin. TN nay khong hop le. LH: 1900636633";
			return response(404, smsResponse);
		}else if (((new Date()).getTime() - momo.getUpdatedAt().getTime()) > 72*60*60*1000 ) { //72h
			momo.setSmsResult("F");
			smsResponse = "Cam on Quy khach da phan hoi thong tin. TN nay khong hop le do da qua 72H. LH: 1900636633";
			
		} else if (body.path("sms_result").asText().trim().toUpperCase().equals(SMS_Y)) {
			momo.setSmsResult("Y");
			smsResponse = "Cam on Quy khach da phan hoi thong tin. Ho so cua quy Khach dang duoc tiep tuc xu ly giai ngan.";
			new Thread(() -> {
				try {
					JsonNode loan = rabbitMQService.sendAndReceive("tpf-service-esb", Map.of("func", "getLoan",
							"reference_id", body.path("reference_id"), "param", Map.of("appId", momo.getAppId())));
					if (loan.path("status").asInt() == 200) {
						ResponseMomoDisburse momoDisburse = mapper.convertValue(loan.path("data"),
								ResponseMomoDisburse.class);
						momoDisburse.setRequestId(body.path("reference_id").asText());
						momoDisburse.setMomoLoanId(momo.getMomoLoanId());
						momoDisburse.setPhoneNumber(momo.getPhoneNumber());
						momoDisburse.setDescription("Khách hàng được giải ngân");

						JsonNode result = apiService
								.sendDisburseToMomo(mapper.convertValue(momoDisburse, JsonNode.class));

						if (result.path("resultCode").asText().equals("500")) {
							momo.setError(result.path("message").asText());
						} else if (result.path("resultCode").asText().equals("0")) {
							momo.setStatus("APPROVED");
						} else {
							momo.setError(result.path("message").asText());
							momo.setStatus("MOMO_CANCELLED");
						}

						mongoTemplate.save(momo);

						rabbitMQService.send("tpf-service-app",
								Map.of("func", "updateApp", "reference_id", body.path("reference_id"), "param",
										Map.of("project", "momo", "id", momo.getId()), "body",
										convertService.toAppDisplay(momo)));
					}
				} catch (Exception e) {
					log.error(e.toString());
				}
			}).start();
		} else if (body.path("sms_result").asText().trim().toUpperCase().equals(SMS_N)) {
			momo.setSmsResult("N");
			smsResponse = "Cam on Quy khach da phan hoi thong tin. Yeu cau HUY cua Quy khach dang duoc xu ly.";
			momo.setStatus("CUSTOMER_CANCELLED");
//			new Thread(() -> {
//				ResponseMomoStatus momoStatus = new ResponseMomoStatus();
//				momoStatus.setRequestId(body.path("reference_id").asText());
//				momoStatus.setMomoLoanId(momo.getMomoLoanId());
//				momoStatus.setPhoneNumber(momo.getPhoneNumber());
//				momoStatus.setStatus(status.path("CANCELLED").asInt());
//				momoStatus.setDescription("SMS N");
//				apiService.sendStatusToMomo(mapper.convertValue(momoStatus, JsonNode.class));
//			}).start();
		}  else {
			momo.setSmsResult("F");
			smsResponse = "Cam on Quy khach da phan hoi thong tin. TN nay khong hop le. LH: 1900636633";
		}
		
		mongoTemplate.save(momo);

		rabbitMQService.send("tpf-service-app", Map.of("func", "updateApp", "reference_id", body.path("reference_id"),
				"param", Map.of("project", "momo", "id", momo.getId()), "body", convertService.toAppDisplay(momo)));

		return response(404, smsResponse);
	}
	
	
	public JsonNode getListAppCancelled(JsonNode request) {

		Query query = new Query();
		Criteria criteria = new Criteria().orOperator(
				 Criteria.where("status").in(Arrays.asList("CUSTOMER_CANCELLED", "MOMO_CANCELLED")),
				 new Criteria().andOperator(Criteria.where("status").is("APPROVED_FINNONE"), Criteria.where("smsResult").is("F")),
				 new Criteria().andOperator(Criteria.where("status").is("APPROVED_FINNONE"), Criteria.where("smsResult").is("W"), Criteria.where("updatedAt").lte(new Date(new Date().getTime() -72 * 60 * 60 * 1000)))
				);
		
		query.addCriteria(criteria);

		List<Momo> listMomo = mongoTemplate.find(query, Momo.class);
		
		List<ObjectNode> listCancelled = new   ArrayList<ObjectNode>();;
		
		for (Momo momo : listMomo) {
			String status = "";
			switch (momo.getStatus().trim().toUpperCase()) {
			case "CUSTOMER_CANCELLED":
				status = "Khách hàng từ chối giải ngân";
				break;
			case "MOMO_CANCELLED":
				status = "Momo GN thất bại";
				break;
			default:
				status = "Khách hàng không phản hồi";
				break;
			}
			listCancelled.add(  mapper.createObjectNode().put("appId", momo.getAppId())
					.put("customter", String.format("%s %s %s",  momo.getLastName(), momo.getMiddleName(),momo.getFirstName()))
					.put("status", status));
		}
		
		return response(200, mapper.convertValue(listCancelled, JsonNode.class));
	}

}