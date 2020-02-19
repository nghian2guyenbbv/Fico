package vn.com.tpf.microservices.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.com.tpf.microservices.models.Comment;
import vn.com.tpf.microservices.models.DocPostApproved;
import vn.com.tpf.microservices.models.Fpt;

import vn.com.tpf.microservices.models.Supplement;

@Service
public class FptService {

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
//		status = mapper.createObjectNode();
//		status.put("APPROVED", 0);
//		status.put("PROCESSING", 1);
//		status.put("APPROVED_AND_WAITING_SMS", 2);
//		status.put("DISBURSED", 4);
//		status.put("CANCELLED", 5);
//		status.put("REJECTED", 6);
//		status.put("LIQUIDATION", 7);

		error = mapper.createObjectNode();
		Map<?, ?> requestId = Map.of("code", 100, "message", "request_id is required string and not empty");
		error.set("requestId", mapper.convertValue(requestId, JsonNode.class));
		Map<?, ?> dateTime = Map.of("code", 101, "message", "date_time is required string and not empty");
		error.set("dateTime", mapper.convertValue(dateTime, JsonNode.class));

		Map<?, ?> references = Map.of("code", 104, "message", "references");
		error.set("references", mapper.convertValue(references, JsonNode.class));
		Map<?, ?> postCodeNotExists = Map.of("code", 103, "message", "city not exits");
		error.set("postCodeNotExists", mapper.convertValue(postCodeNotExists, JsonNode.class));
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

			if (field.equals("data.firstName") && (!body.path("data").path("firstName").isTextual()
					|| body.path("data").path("firstName").asText().isEmpty())) {
				return error.get("firstName");
			}
			if (field.equals("data.middleName") && !body.path("data").path("middleName").isTextual()) {
				return error.get("middleName");
			}
			if (field.equals("data.lastName") && (!body.path("data").path("lastName").isTextual()
					|| body.path("data").path("lastName").asText().isEmpty())) {
				return error.get("lastName");
			}
			if (field.equals("data.gender") && !body.path("data").path("gender").asText().matches("^(Male|Female)$")) {
				return error.get("gender");
			}

			if (field.equals("data.dateOfBirth") && (!body.path("data").path("dateOfBirth").isTextual()
					|| body.path("data").path("dateOfBirth").asText().isEmpty())) {
				return error.get("dateOfBirth");
			}
			if (field.equals("data.nationalId")
					&& !body.path("data").path("nationalId").asText().matches("^(?=(?:.{9}|.{12})$)[0-9]*$")) {
				return error.get("nationalId");
			}

			if (field.equals("data.issuePlace") && (!body.path("data").path("issuePlace").isTextual()
					|| body.path("data").path("issuePlace").asText().isEmpty())) {
				return error.get("issuePlace");
			}

			if (field.equals("data.issueDate") && (!body.path("data").path("issueDate").isTextual()
					|| body.path("data").path("issueDate").asText().isEmpty())) {
				return error.get("issueDate");
			}

			if (field.equals("data.employeeCard") && (!body.path("data").path("employeeCard").isTextual()
					|| body.path("data").path("employeeCard").asText().isEmpty())) {
				return error.get("employeeCard");
			}

			if (field.equals("data.maritalStatus")
					&& !body.path("data").path("maritalStatus").asText().matches("^(Single|Married)$")) {
				return error.get("maritalStatus");
			}
			if (field.equals("data.mobilePhone")
					&& !body.path("data").path("mobilePhone").asText().matches("^(\\+84)(?=(?:.{9}|.{10})$)[0-9]*$")) {
				return error.get("mobilePhone");
			}

			if (field.equals("data.map") && (!body.path("data").path("employeeCard").isTextual()
					|| body.path("data").path("map").asText().isEmpty())) {
				return error.get("map");
			}

			if (field.equals("data.ownerNationalId") && (!body.path("data").path("ownerNationalId").isTextual()
					|| body.path("data").path("ownerNationalId").asText().isEmpty())) {
				return error.get("ownerNationalId");
			}
			if (field.equals("data.contactAddress") && (!body.path("data").path("contactAddress").isTextual()
					|| body.path("data").path("contactAddress").asText().isEmpty())) {
				return error.get("contactAddress");
			}
			if (field.equals("data.dsaCode") && (!body.path("data").path("dsaCode").isTextual()
					|| body.path("data").path("dsaCode").asText().isEmpty())) {
				return error.get("dsaCode");
			}

			if (field.equals("data.companyName") && (!body.path("data").path("companyName").isTextual()
					|| body.path("data").path("companyName").asText().isEmpty())) {
				return error.get("companyName");
			}

			if (field.equals("data.taxCode") && (!body.path("data").path("taxCode").isTextual()
					|| body.path("data").path("taxCode").asText().isEmpty())) {
				return error.get("taxCode");
			}

			if (field.equals("data.salary") && !body.path("data").path("salary").isNumber()) {
				return error.get("salary");
			}

			if (field.equals("data.durationYear") && !body.path("data").path("durationYear").isNumber()) {
				return error.get("durationYear");
			}
			if (field.equals("data.durationMonth") && !body.path("data").path("durationMonth").isNumber()) {
				return error.get("durationYear");
			}
			if (field.equals("data.loanDetail") && (!body.path("data").path("loanDetail").isObject())) {
				return error.get("loanDetail");
			} else {
				JsonNode loanDetail = body.path("data").path("loanDetail");
				String a = loanDetail.path("product").asText();

				if (loanDetail.path("product").asText().isEmpty())
					return error.get("loanDetail");
				if (!loanDetail.path("tenor").isNumber())
					return error.get("loanDetail");
				if (!loanDetail.path("annualr").isDouble())
					return error.get("loanDetail");
				if (!loanDetail.path("downPayment").isNumber())
					return error.get("loanDetail");
				if (!loanDetail.path("dueDate").isNumber())
					return error.get("loanDetail");
				if (!loanDetail.path("emi").isNumber())
					return error.get("loanDetail");
				if (!loanDetail.path("loanAmount").isNumber())
					return error.get("loanDetail");
			}

			if (field.equals("data.addresses") && (!body.path("data").path("addresses").isArray()
					|| body.path("data").path("addresses").size() == 0)) {
				return error.get("addresses");
			} else {
				ArrayNode addresses = (ArrayNode) body.path("data").path("addresses");
				for (JsonNode address : addresses) {
					if (address.path("addressType").asText().isEmpty())
						return error.get("addresss");
					if (address.path("address1").asText().isEmpty())
						return error.get("addresss");
					if (address.path("address2").asText().isEmpty())
						return error.get("addresss");
					if (address.path("ward").asText().isEmpty())
						return error.get("addresss");
					if (address.path("district").asText().isEmpty())
						return error.get("addresss");
					if (address.path("province").asText().isEmpty())
						return error.get("addresss");

				}
			}
			if (field.equals("data.photos")
					&& (!body.path("data").path("photos").isArray() || body.path("data").path("photos").size() != 3)) {
				return error.get("photos");
			} else {
				ArrayNode photos = (ArrayNode) body.path("data").path("photos");
				for (JsonNode photo : photos)
					if (!photo.path("documentType").asText().matches("^(National ID Back|National ID Front|Selfie)$")
							|| !photo.path("link").asText().matches(
									"^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$"))
						return error.get("photos");
			}

			if (field.equals("data.productDetails") && (!body.path("data").path("productDetails").isArray()
					|| body.path("data").path("productDetails").size() == 0)) {
				return error.get("productDetails");
			} else {
				ArrayNode productDetails = (ArrayNode) body.path("data").path("productDetails");
				for (JsonNode productDetail : productDetails) {
					JsonNode a = productDetail.path("quantity");
					if (productDetail.path("model").asText().isEmpty())
						return error.get("productDetails");
					if (productDetail.path("goodCode").asText().isEmpty())
						return error.get("productDetails");
					if (productDetail.path("goodType").asText().isEmpty())
						return error.get("productDetails");
					if (!productDetail.path("quantity").isNumber())
						return error.get("productDetails");
					if (!productDetail.path("goodPrice").isNumber())
						return error.get("productDetails");

				}
			}

			if (field.equals("data.references") && (!body.path("data").path("references").isArray()
					|| body.path("data").path("references").size() == 0)) {
				return error.get("references");
			} else {
				ArrayNode references = (ArrayNode) body.path("data").path("references");
				for (JsonNode reference : references) {
					if (!reference.path("relation").asText().matches("^(Colleague|Relative|Spouse)$"))
						return error.get("references");
					if (reference.path("phoneNumber").asText().isEmpty()
							|| !reference.path("phoneNumber").asText().matches("^[0-9]{10}$"))
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
		return mapper.createObjectNode().put("status", status).set("data", data);
	}

	private JsonNode response(int code, JsonNode body, JsonNode data) {
		ObjectNode res = mapper.createObjectNode();
		res.put("result_code", code);
		res.put("request_id", body.path("request_id").asText());
		res.put("reference_id", body.path("reference_id").asText());
		res.set("date_time", mapper.convertValue(new Date(), JsonNode.class));
		if (code == 0) {
			res.set("data", data);
		} else {
			res.set("message", data.path("message"));
		}
		return mapper.createObjectNode().put("status", 200).set("data", res);
	}

	public JsonNode addCommentFromFpt(JsonNode request) throws Exception {

		JsonNode body = request.path("body");
		String cus_id = body.path("cus_id").asText();
		JsonNode comments = body.path("comments");
		String type = body.path("type").asText();

		Query query = new Query();
		Update update = new Update();

		Fpt fpt = mongoTemplate.findOne(query.addCriteria(Criteria.where("custId").is(cus_id)), Fpt.class);

		if (fpt == null) {
			return response(404, mapper.createObjectNode().put("message", "Not Found cus_id"));
		}

		if (fpt.getSupplement() == null) {
			return response(404, mapper.createObjectNode().put("message", "Not Found Comment"));
		}

		Set<Supplement> setsupplements = (Set<Supplement>) fpt.getSupplement().stream()
				.filter(item -> (item.isPending() == false)).collect(Collectors.toSet());// @formatter:off

		if (setsupplements.size() == 0) {
			return response(404, mapper.createObjectNode().put("message", "Not Found Supplement from TpBank"));
		}

		Set<Supplement> newcomments = new HashSet<Supplement>();

		if (type.equals("Supplement")) {
			if (comments.isArray()) {
				for (JsonNode jsonNode : comments) {
					String code = jsonNode.get("code").asText();
					String commentFpt = jsonNode.get("comment").asText();

					for (Supplement sup : setsupplements) {
						if (sup.getCode().equals(code)) {
							long millis = System.currentTimeMillis();
							java.util.Date date = new java.util.Date(millis);
							Supplement newsupplement = new Supplement();
							newsupplement.SupplementFpt(code, commentFpt, sup.getCommentTpf(), "Supplement", date,
									sup.getCreatedAt());
							newcomments.add(newsupplement);
						}

					}
				}
			}
		} else if (type.equals("Returned")) {
			if (comments.isArray()) {
				for (JsonNode jsonNode : comments) {
					String code = jsonNode.get("code").asText();
					String commentFpt = jsonNode.get("comment").asText();

					for (Supplement sup : setsupplements) {
						if (sup.getCode().equals(code)) {
							long millis = System.currentTimeMillis();
							java.util.Date date = new java.util.Date(millis);
							Supplement newsupplement = new Supplement();
							newsupplement.SupplementFpt(code, commentFpt, sup.getCommentTpf(), "Returned", date,
									sup.getCreatedAt());
							newcomments.add(newsupplement);
						}

					}
				}
			}
		} else {
			return response(404, mapper.createObjectNode().put("message", "Not Found Type"));
		}

		return response(200, mapper.convertValue(setsupplements, JsonNode.class));
	}

	public JsonNode getDetail(JsonNode request) throws Exception {
		Fpt fpt = mongoTemplate.findOne(Query.query(Criteria.where("custId").is(request.path("body").asText())),
				Fpt.class);

		if (fpt == null) {
			return response(404, mapper.createObjectNode().put("message", "Not Found"));
		}

		return response(200, mapper.convertValue(fpt, JsonNode.class));
	}

	public JsonNode showdata() {
		Query query = new Query();
		List<Fpt> fpts = mongoTemplate.find(query, Fpt.class);
		String data = "null";

		return response(200, mapper.convertValue(fpts, JsonNode.class));
	}

	// add App_id
	public JsonNode updatedappid(JsonNode request) throws Exception {
		JsonNode body = request.path("body");
		String cus_id = body.path("cus_id").asText();
		String appId = body.path("data").path("appId").asText();
		Query query = new Query();
		Update update = new Update();

		Fpt fpt = mongoTemplate.findOne(query.addCriteria(Criteria.where("custId").is(cus_id)), Fpt.class);

		if (fpt == null) {
			return response(404, mapper.createObjectNode().put("message", "Not Found"));
		}

		update.set("appId", appId);
		mongoTemplate.updateFirst(query, update, Fpt.class);

		return response(200, mapper.convertValue(fpt, JsonNode.class));
	}

//	public JsonNode addCommentFromTpBank(JsonNode request) throws Exception {
//
//		Query query = new Query();
//		Query querysupllement = new Query();
//		Query queryreturned = new Query();
//		JsonNode body = request.path("body");
//		String cus_id = body.path("cus_id").asText();
//		String type = body.path("type").asText();
//		JsonNode comments = body.path("comments");
//		if (cus_id.isEmpty()) {
//			return response(404, mapper.createObjectNode().put("message", "Not Found cus_id"));
//		}
//		if (!type.matches("^(Returned|Supplement)$")) {
//			return response(404, mapper.createObjectNode().put("message", "Error type"));
//		}
//		if (comments.size() == 0) {
//			return response(404, mapper.createObjectNode().put("message", "Error Not Comment"));
//		}
//
//		Fpt fpt = mongoTemplate.findOne(query.addCriteria(Criteria.where("custId").is(cus_id)), Fpt.class);
//
//		if (fpt == null) {
//			return response(404, mapper.createObjectNode().put("message", "Not Found customer"));
//		}
//
//		if (!fpt.getStatus().equals("PROCESSING")) {
//			return response(404,
//					mapper.createObjectNode().put("message", "Can not updated comment because Status is RETURNED"));
//		}
//		List<Comment> CommentReturned = mongoTemplate.find(queryreturned, Comment.class);
//		queryreturned.addCriteria(Criteria.where("type").is("returned"));
//		List<String> CodeReturned = new ArrayList<String>();
//		for (Comment comment : CommentReturned) {
//			for (Subcode subcode : comment.getSubcode()) {
//				CodeReturned.add(subcode.getCode());
//			}
//		}
//
//		querysupllement.addCriteria(Criteria.where("type").is("supplement"));
//		List<Comment> CommentSuplement = mongoTemplate.find(querysupllement, Comment.class);
//
//		List<String> CodeSupplement = new ArrayList<String>();
//		for (Comment comment : CommentSuplement) {
//			for (Subcode subcode : comment.getSubcode()) {
//				CodeSupplement.add(subcode.getCode());
//			}
//		}
//
//		Set<Supplement> setcomments = new HashSet<Supplement>();
//
//		if (type.equals("Supplement")) {
//
//			if (comments.isArray()) {
//				for (JsonNode jsonNode : comments) {
//					String code = jsonNode.get("code").asText();
//					String commentTpBank = jsonNode.get("comment").asText();
//					if (CodeSupplement.contains(code)) {
//						Supplement supplement = new Supplement();
//						long millis = System.currentTimeMillis();
//						java.util.Date date = new java.util.Date(millis);
//						supplement.SupplemenTPBank(code, commentTpBank, "Supplement", date);
//						setcomments.add(supplement);
//					}
//				}
//			}
//		} else if (type.equals("Returned")) {
//
//			if (comments.isArray()) {
//				for (JsonNode jsonNode : comments) {
//					String code = jsonNode.get("code").asText();
//					String commentTpBank = jsonNode.get("comment").asText();
//					if (CodeReturned.contains(code)) {
//						Supplement supplement = new Supplement();
//						long millis = System.currentTimeMillis();
//						java.util.Date date = new java.util.Date(millis);
//						supplement.SupplemenTPBank(code, commentTpBank, "Returned", date);
//						setcomments.add(supplement);
//					}
//				}
//			}
//		} else {
//			return response(404, mapper.createObjectNode().put("message", "Not Found Type comment"));
//		}
//
//		if (setcomments.size() == 0) {
//			return response(404, mapper.createObjectNode().put("message", "Code Not Found"));
//		}
//
//		if (fpt.getSupplement() != null) {
//			Set<Supplement> setsupplementsDB = (Set<Supplement>) fpt.getSupplement().stream()
//					.collect(Collectors.toSet());
//
//			boolean MergSet = setcomments.addAll(setsupplementsDB);
//
//			if (MergSet == false) {
//				return response(404, mapper.createObjectNode().put("message", "Not Merging"));
//			}
//		}
//
//		Update update = new Update();
//		update.set("status", "RETURNED");
//		update.set("Supplement", setcomments);
//		mongoTemplate.updateFirst(query, update, Fpt.class);
//
//		return response(200, mapper.convertValue(fpt, JsonNode.class));
//
//	}

	public JsonNode adddocPostApproved(JsonNode request) throws Exception {

		JsonNode body = request.path("body");
		String cus_id = body.path("cus_id").asText();
		JsonNode docPostApproved = body.path("data").path("docPostApproved");
		Set<DocPostApproved> SetdocPostApproved = mapper.convertValue(docPostApproved,
				new TypeReference<Set<DocPostApproved>>() {
				});
		Query query = new Query();
		Update update = new Update();

		Fpt fpt = mongoTemplate.findOne(query.addCriteria(Criteria.where("custId").is(cus_id)), Fpt.class);

		if (fpt == null) {
			return response(404, mapper.createObjectNode().put("message", "Not Found"));
		}

		update.set("docPostApproved", SetdocPostApproved);
		mongoTemplate.updateFirst(query, update, Fpt.class);

		return response(200, mapper.convertValue(fpt, JsonNode.class));
	}

	public JsonNode createFpt(JsonNode request) throws Exception {
		JsonNode body = request.path("body");
		JsonNode valid = validation(body,
				Arrays.asList("request_id", "data.maritalStatus", "data.middleName", "data.salary", "data.nationalId",
						"data.productDetails", "data.lastName", "data.gender", "data.mobilePhone", "data.addresses",
						"data.photos", "data.references", "data.employeeCard", "data.dateOfBirth",
						"data.ownerNationalId", "data.map", "data.dsaCode", "data.durationYear", "data.durationMonth",
						"data.loanDetail", "data.issuePlace", "data.issueDate", "data.contactAddress",
						"data.companyName", "data.taxCode"));

		if (valid != null) {
			return response(valid.get("code").asInt(), body,
					mapper.createObjectNode().set("message", valid.get("message")));
		}
		JsonNode data = body.path("data");
		JsonNode postCode = rabbitMQService.sendAndReceive("tpf-service-assets",
				Map.of("func", "getAddress", "reference_id", body.path("reference_id"), "param",
						Map.of("postCode", data.path("issuePlace").asText())));

		if (postCode.path("status").asInt() != 200) {
			return response(error.get("postCodeNotExists").get("code").asInt(), body,
					mapper.createObjectNode().set("message", error.get("postCodeNotExists").get("message")));
		}
		
		((ObjectNode) data).put("issuePlace", postCode.path("data").path("cityName"));
		Fpt fpt = mapper.convertValue(body.path("data"), Fpt.class);

		fpt.getAddresses().forEach(address -> {
			try {
				JsonNode addr = rabbitMQService.sendAndReceive("tpf-service-assets", Map.of("func", "getAddress",
						"reference_id", body.path("reference_id"), "param", Map.of("areaCode", address.getDistrict())));
				address.setDistrict(addr.path("data").path("areaName").asText());
				address.setProvince(addr.path("data").path("cityName").asText());
				address.setRegion(addr.path("data").path("region").asText());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		mongoTemplate.save(fpt);

		rabbitMQService.send("tpf-service-app", Map.of("func", "createApp", "reference_id", body.path("reference_id"),
				"body", convertService.toAppDisplay(fpt)));

		return response(200, mapper.createObjectNode().put("message", "retry partner id " + fpt.getAppId() + " success"));

	}

	// chua dung

	public JsonNode updateAutomation(JsonNode request) throws Exception {
		JsonNode body = request.path("body");

		Assert.isTrue(body.path("transaction_id").isTextual() && !body.path("transaction_id").asText().isEmpty(),
				"transaction_id is required and not empty");
		Assert.isTrue(body.path("app_id").isTextual() && !body.path("app_id").asText().isEmpty(),
				"app_id is required and not empty");
		Assert.isTrue(body.path("automation_result").isTextual() && !body.path("automation_result").asText().isEmpty(),
				"automation_result is required and not empty");

		Query query = Query.query(Criteria.where("custId").is(body.path("transaction_id").asText()));
		Update update = new Update().set("appId", body.path("app_id").asText())
				.set("automationResult", body.path("automation_result").asText()).set("status", "PROCESSING");
		Fpt fpt = mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), Fpt.class);

		if (fpt == null) {
			return response(404, mapper.createObjectNode().put("message", "Cust Id Not Found"));
		}

		rabbitMQService.send("tpf-service-app", Map.of("func", "updateApp", "reference_id", body.path("reference_id"),
				"param", Map.of("project", "fpt", "id", fpt.getId()), "body", convertService.toAppDisplay(fpt)));

		return response(200, null);
	}
//
//	public JsonNode updateStatus(JsonNode request) throws Exception {
//		JsonNode body = request.path("body");
//
//		Fpt fpt = mongoTemplate.findOne(Query.query(Criteria.where("appId").is(body.path("app_id").asText())), Fpt.class);
//
//		if (fpt == null) {
//			return response(404, mapper.createObjectNode().put("message", "AppId Not Found"));
//		}
//
//		if (body.path("status").asText().equals("APPROVED")) {
////			SyncACCA
//		}
//
//		fpt.setStatus(body.path("status").asText());
//		fpt.setAutomationResult(fpt.getAutomationResult().equals("Pass") ? fpt.getAutomationResult() : "Fix");
//		mongoTemplate.save(fpt);
//
//		new Thread(() -> {
//			apiService
//					.sendStatusToFpt(mapper.createObjectNode().put("custId", fpt.getCustId()).put("status", fpt.getStatus()));
//		}).start();
//
//		rabbitMQService.send("tpf-service-app", Map.of("func", "updateApp", "reference_id", body.path("reference_id"),
//				"param", Map.of("project", "fpt", "id", fpt.getId()), "body", convertService.toAppDisplay(fpt)));
//
//		return response(200, null);
//	}
//
}