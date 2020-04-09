package vn.com.tpf.microservices.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import java.util.UUID;

import java.util.Set;


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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.com.tpf.microservices.models.*;

import vn.com.tpf.microservices.utils.Utils;

@Service
public class MomoService {

	private final String STAGE_PRECHECK_DONE = "PRECHECKED";
	private final String STAGE_CREATE_APP = "CREATE_APP";

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

	@Autowired
	private Utils utils;

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

	public JsonNode preCheckMomo(JsonNode request) throws Exception {
		JsonNode body = request.path("body");

		if (body.path("data").isNull())
			return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message", "data not null"));
		final JsonNode data = request.path("body").path("data");
		final String momoLoanId = data.path("momoLoanId").asText();
		if (momoLoanId.isEmpty() || momoLoanId.isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.momoLoanId not null"));
		Momo momo = mongoTemplate.findOne(Query.query(Criteria.where("momoLoanId").is(momoLoanId)), Momo.class);
		if (momo != null)
			return utils.getJsonNodeResponse(1, body,
					mapper.createObjectNode().put("message", String.format("data.momoLoanId %s exits", momoLoanId)));
		if (data.path("firstName").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.firstName not blank"));
		if (data.path("lastName").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.lastName not blank"));

		if (data.path("middleName").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.middleName not blank"));

		if (!data.path("gender").asText().matches("^(Male|Female)$"))
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.gender not blank"));

		if (!data.path("personalId").asText().matches("^(?=(?:.{9}|.{12})$)[0-9]*$"))
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.personalId not valid"));
		if (!data.path("dateOfBirth").asText()
				.matches("^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}$"))
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.dateOfBirth not blank"));
		if (!data.path("address1").isTextual() || data.path("address1").asText().isEmpty())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.address1 not fail"));
		if (data.path("district").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.district not blank"));
		if (data.path("city").asText().isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.city not blank"));
		if (!data.path("phoneNumber").asText().matches("^(\\+84)(?=(?:.{9}|.{10})$)[0-9]*$"))
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.phoneNumber not blank"));
		if (!data.path("salary").isNumber())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.salary not fail"));
		if (!data.path("ward").isTextual() || data.path("ward").asText().isEmpty())
			return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message", "data.ward not fail"));
		if (!data.path("address2").isTextual() || data.path("address2").asText().isEmpty())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.address2 not fail"));

		JsonNode address = rabbitMQService.sendAndReceive("tpf-service-assets",
				Map.of("func", "getAddress", "reference_id", body.path("reference_id"), "param",
						Map.of("areaCode", data.path("district").asText())));
		if (address.path("status").asInt() != 200)
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.district not found"));
		if (!address.path("data").path("postCode").asText().equals(data.path("city").asText()))
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.city not found"));

		momo = Momo.builder().momoLoanId(momoLoanId).firstName(data.path("firstName").asText())
				.lastName(data.path("lastName").asText()).firstName(data.path("lastName").asText())
				.middleName(data.path("middleName").asText()).dateOfBirth(data.path("dateOfBirth").asText())
				.city(address.path("data").path("cityName").asText())
				.district(address.path("data").path("areaName").asText())
				.region(address.path("data").path("region").asText()).personalId(data.path("personalId").asText())
				.Stage(STAGE_PRECHECK_DONE).address1(data.path("address1").asText())
				.address2(data.path("address2").asText()).ward(data.path("ward").asText()).salary(data.path("salary").asLong()).build();

		mongoTemplate.save(momo);
		return utils.getJsonNodeResponse(0, body, data);
	}

	public JsonNode createMomo(JsonNode request) throws Exception {
		JsonNode body = request.path("body");

		if (body.path("data").isNull())
			return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message", "data not null"));
		final JsonNode data = request.path("body").path("data");
		final String momoLoanId = data.path("momoLoanId").asText();

		if (momoLoanId.isEmpty() || momoLoanId.isBlank())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.momoLoanId not null"));
		Query query = Query.query(Criteria.where("momoLoanId").is(momoLoanId));
		Momo momo = mongoTemplate.findOne(query, Momo.class);
		if (momo == null)
			return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message",
					String.format("data.momoLoadId %s not PreCheck", momoLoanId)));

		if (!momo.getStage().equals(STAGE_PRECHECK_DONE))
			return utils.getJsonNodeResponse(1, body, mapper.createObjectNode().put("message", "Not find PreCheck"));

		if (!data.path("agree1").asBoolean())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.agree1 is false"));
		if (!data.path("agree2").asBoolean())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.agree2 is false"));
		if (!data.path("agree3").asBoolean())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.agree3 is false"));
		if (!data.path("agree4").asBoolean())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.agree4 is false"));
		if (!data.path("insurrance").isBoolean())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.insurrance not fail"));
		if (!data.path("amount").isNumber() || data.path("amount").asText().isEmpty())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.amount not fail"));
		if (!data.path("dueDate").isNumber() || data.path("dueDate").asText().isEmpty())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.dueDate not fail"));
		if (!data.path("fee").isNumber() || data.path("fee").asText().isEmpty())
			return utils.getJsonNodeResponse(499, body, mapper.createObjectNode().put("message", "data.fee not fail"));
		if (!data.path("loanTime").isNumber() || data.path("loanTime").asText().isEmpty())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.loanTime not fail"));
		if (!data.path("issuePlace").isTextual() || data.path("issuePlace").asText().isEmpty())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.issuePlace not fail"));
		if (!data.path("productCode").isTextual() || data.path("productCode").asText().isEmpty())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.productCode not fail"));

		if (!data.path("email").isTextual() || data.path("email").asText().isEmpty())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.email not fail"));

		if (!data.path("maritalStatus").asText().matches("^(Single|Married)$"))
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.maritalStatus not fail"));

		if (!data.path("issueDate").asText().matches("^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}$"))
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.issueDate not fail"));
		List<HashMap> referenceSet = new ArrayList<HashMap>();
		if (!data.path("references").isArray())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.references not fail"));
		else {
			ArrayNode references = (ArrayNode) data.path("references");
			for (JsonNode reference : references) {
				if (!reference.path("relation").asText().matches("^(Colleague|Relative|Spouse)$"))
					return utils.getJsonNodeResponse(499, body,
							mapper.createObjectNode().put("message", "data.references not fail"));
				if (reference.path("phoneNumber").asText().isEmpty()
						|| !reference.path("phoneNumber").asText().matches("^[0-9]{10}$"))
					return utils.getJsonNodeResponse(499, body,
							mapper.createObjectNode().put("message", "data.references not fail"));
				if (reference.path("relation").asText().equals("Spouse"))
					if (reference.path("personalId").asText().isEmpty()
							|| !reference.path("personalId").asText().matches("^(?=(?:.{9}|.{12})$)[0-9]*$"))
						return utils.getJsonNodeResponse(499, body,
								mapper.createObjectNode().put("message", "data.references not fail"));
				HashMap<String, String> docUpload = new HashMap<>();
				docUpload.put("fullName", reference.path("fullName").asText());
				docUpload.put("personalId", reference.path("personalId").asText());
				docUpload.put("relation", reference.path("relation").asText());
				docUpload.put("phoneNumber", reference.path("phoneNumber").asText());
				referenceSet.add(docUpload);
			}
		}
		List<HashMap> photoSet = new ArrayList<HashMap>();
		if (!data.path("photos").isArray())
			return utils.getJsonNodeResponse(499, body,
					mapper.createObjectNode().put("message", "data.photos not fail"));
		else {
			ArrayNode photos = (ArrayNode) data.path("photos");
			for (JsonNode photo : photos)
				if (!photo.path("type").asText().matches("^(National ID Back|National ID Front|Selfie)$")
						|| !photo.path("link").asText().matches(
								"^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$"))

					return utils.getJsonNodeResponse(499, body,
							mapper.createObjectNode().put("message", "data.photos not fail"));
				else {
					HashMap<String, Object> docUpload = new HashMap<>();
					docUpload.put("type", photo.path("type").asText());
					docUpload.put("link", photo.path("link").asText());
					docUpload.put("updatedAt", new Date());
					photoSet.add(docUpload);
				}
		}

		Update update = new Update().set("agree1", data.path("agree1").asBoolean()).set("agree2", data.path("agree2").asBoolean())
				.set("agree3", data.path("agree3").asBoolean()).set("agree4", data.path("agree4").asBoolean())
				.set("insurrance", data.path("insurrance").asBoolean()).set("amount", data.path("amount").asLong())
				.set("dueDate", data.path("dueDate").asLong()).set("fee", data.path("fee").asLong())
				.set("loanTime", data.path("loanTime").asLong()).set("issuePlace", data.path("issuePlace").asText())
				.set("productCode", data.path("productCode").asText()).set("email", data.path("email").asText())
				.set("maritalStatus", data.path("maritalStatus").asText()).set("issueDate", data.path("issueDate").asText())
				.set("issueDate", data.path("issueDate").asText()).set("photos", photoSet).set("references", referenceSet)
				.set("Stage", STAGE_CREATE_APP).set("preChecks", Map.of("createdAt", new Date(), "data", "Success"));
		momo = mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), Momo.class);

		rabbitMQService.send("tpf-service-esb", Map.of("func", "createApp", "reference_id", body.path("reference_id"),
				"body", convertService.toAppFinnone(momo)));


		rabbitMQService.send("tpf-service-app", Map.of("func", "createApp", "reference_id", body.path("reference_id"),
				"body", convertService.toAppDisplay(momo)));

		return utils.getJsonNodeResponse(0, body, data);
	}

	public JsonNode retryAutomation(JsonNode request) throws Exception {

		Momo momo = mongoTemplate
				.findOne(Query.query(Criteria.where("id").is(request.path("param").path("id").asText())), Momo.class);

		if (momo == null) {
			return response(404, mapper.createObjectNode().put("message", "Momo Loan Id Not Found"));
		}
		if (momo.getAutomationResult().isBlank() || momo.getAppId().isBlank()
				|| !momo.getAppId().toUpperCase().contains("UNK"))
			return response(404, mapper.createObjectNode().put("message", "Momo Loan Id Cannt Retry"));

		rabbitMQService.send("tpf-service-esb", Map.of("func", "createApp", "reference_id",
				momo.getMomoLoanId() + UUID.randomUUID().toString(), "body", convertService.toAppFinnone(momo)));
		return response(0,
				mapper.createObjectNode().put("message", "retry partner id " + momo.getMomoLoanId() + " success"));

	}
	
	
	public JsonNode updateAppId(JsonNode request) throws Exception {
		JsonNode body = request.path("body");

		Assert.isTrue(body.path("partnerId").isTextual() && !body.path("partnerId").asText().isEmpty(),
				"partnerId is required and not empty");
		Assert.isTrue(body.path("appId").isTextual() && !body.path("appId").asText().isEmpty(),
				"appId is required and not empty");
		
		if( mongoTemplate.findOne(Query.query(Criteria.where("appId").is(body.path("appId").asText())), Momo.class) != null) 
			return response(404, mapper.createObjectNode().put("message", "AppId Is Exits"));
		
		Query query = Query.query(Criteria.where("momoLoanId").is(body.path("partnerId").asText()).and("appId").is("UNKNOW"));
		Update update = new Update().set("appId", body.path("appId").asText())
				.set("automationResult","FIX").set("status", "PROCESSING");

		Momo momo = mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), Momo.class);
		
		if (momo == null) {
			return response(404, mapper.createObjectNode().put("message", "Momo Loan Id Not Found Or AppId Is UNKNOW"));
		}

		rabbitMQService.send("tpf-service-app",
				Map.of("func", "updateApp", "reference_id", request.path("reference_id"), "param",
						Map.of("project", "momo", "id", momo.getId()), "body", convertService.toAppDisplay(momo)));

		return response(200, mapper.createObjectNode().put("message", "Update AppId Success"));
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
						JsonNode loan = rabbitMQService.sendAndReceive("tpf-service-esb", Map.of("func", "getLoan",
								"reference_id", body.path("reference_id"), "param", Map.of("appId", momo.getAppId())));
						if (loan.path("status").asInt() == 200) {
							rabbitMQService.sendAndReceive("tpf-service-sms", Map.of("func", "sendSms", "reference_id",
									body.path("reference_id"), "body",
									Map.of("phone", momo.getPhoneNumber(), "content", "Chuc mung khach hang, CMND "
											+ momo.getPersonalId() + " duoc phe duyet voi tong so tien vay "
											+ loan.path("data").path("totalAmount").asText() + " vnd, thoi han vay "
											+ loan.path("data").path("detail").path("tenor").asText()
											+ " va khoan tra moi thang "
											+ loan.path("data").path("detail").path("emi").asText()
											+ ". Vui long tra loi tin nhan voi cu phap TPB MOMO Y den so 8089 de xac nhan khoan vay hoac TPB MOMO N de tu choi khoan vay")));
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
						int sendCount = 0; 
						do {
							JsonNode result =  apiService.sendStatusToMomo(mapper.convertValue(momoStatus, JsonNode.class));
							if (result.path("resultCode").asText().equals("0")) 
								return;
							sendCount++;
							Thread.sleep(5*60*1000);
				        } while (sendCount <= 2);
						
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
		
		if (!body.path("sms_result").asText().trim().toUpperCase().matches("^(TPB MOMO Y|TPB MOMO N)$")) 
			return response(404, "Cam on Quy khach da phan hoi thong tin. TN nay khong hop le. LH: 1900636633");
		
		Query query = Query.query(Criteria.where("phoneNumber")
				.is(body.path("phone_number").asText().replaceAll("^[0]", "+84")).and("appId").ne(null).and("status").is("APPROVED_FINNONE"));
		query.with(Sort.by(Direction.DESC, "createdAt")).limit(1);

		List<Momo> list = mongoTemplate.find(query, Momo.class);

		if (list.size() == 0) 
			return response(404, "Cam on Quy khach da phan hoi thong tin. TN nay khong hop le. LH: 1900636633");
		Momo momo = list.get(0);
		if (((new Date()).getTime() - momo.getUpdatedAt().getTime()) > 72 * 60 * 60 * 1000) 
			return response(404, "Cam on Quy khach da phan hoi thong tin. TN nay khong hop le do da qua 72H. LH: 1900636633");
		
		if (body.path("sms_result").asText().trim().toUpperCase().equals(SMS_Y)) {
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
				new Criteria().andOperator(Criteria.where("status").is("APPROVED_FINNONE"),
						Criteria.where("smsResult").is("F")),
				new Criteria().andOperator(Criteria.where("status").is("APPROVED_FINNONE"),
						Criteria.where("updatedAt").lte(new Date(new Date().getTime() - 72 * 60 * 60 * 1000))));

		query.addCriteria(criteria);

		List<Momo> listMomo = mongoTemplate.find(query, Momo.class);

		List<ObjectNode> listCancelled = new ArrayList<ObjectNode>();
		

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
			listCancelled.add(mapper.createObjectNode().put("appId", momo.getAppId())
					.put("customter",
							String.format("%s %s %s", momo.getLastName(), momo.getMiddleName(), momo.getFirstName()))
					.put("status", status).put("lastUpdated",  new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(momo.getUpdatedAt())));
		}

		return response(200, mapper.convertValue(listCancelled, JsonNode.class));
	}

}