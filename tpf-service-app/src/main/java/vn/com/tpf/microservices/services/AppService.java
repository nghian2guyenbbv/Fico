package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import vn.com.tpf.microservices.models.App;
import vn.com.tpf.microservices.models.ReportStatus;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@Service
public class AppService {

	private static final String DATA_ENTRY = "data_entry";
	private static final String DOCUMENT_CHECK = "document_check";
	private static final String LOAN_BOOKING = "loan_booking";

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private RabbitMQService rabbitMQService;

	private String getDepartment(App entity) {
		String status = entity.getStatus() != null ? entity.getStatus() : "";
		String department = "";

		if (status.equals("PROCESSING_FAIL"))
			department = DATA_ENTRY;
		else if (status.equals("PROCESSING_PASS") || status.equals("PROCESSING_FIX") || status.equals("RETURNED"))
			department = DOCUMENT_CHECK;
		else if (status.equals("APPROVED") || status.equals("SUPPLEMENT"))
			department = LOAN_BOOKING;

		return department;
	}

	private JsonNode response(int status, JsonNode data, long total) {
		ObjectNode response = mapper.createObjectNode();
		response.put("status", status).put("total", total).set("data", data);
		return response;
	}

	@SuppressWarnings("unchecked")
	public JsonNode getListApp(JsonNode request, JsonNode info) {
		int page = request.path("param").path("page").asInt(1);
		int limit = request.path("param").path("limit").asInt(10);
		String[] sort = request.path("param").path("sort").asText("createdAt,desc").split(",");

		Query query = new Query();
		Criteria criteria = new Criteria();

		if (request.path("param").path("assigned").isTextual()) {
			if (request.path("param").path("assigned").asText().isEmpty()) {
				criteria = Criteria.where("assigned").is(null);
			} else {
				criteria = Criteria.where("assigned").is(request.path("param").path("assigned").asText());
				if (info.path("authorities").toString()
						.matches(".*(\"role_root\"|\"role_admin\"|\"role_manager\"|\"role_viewer\").*")) {
					criteria = Criteria.where("assigned").ne(null);
				}
			}
		}

		if (request.path("param").path("appId").isTextual()) {
			criteria.and("appId").regex(request.path("param").path("appId").asText(), "i");
		}

		if (!info.path("authorities").toString().matches(".*(\"role_root\").*")) {
			Set<String> projects = new HashSet<>();
			if (request.path("param").path("project").isTextual()) {
				projects = StringUtils.commaDelimitedListToSet(request.path("param").path("project").asText());
			} else if (info.path("projects").isArray()) {
				projects = mapper.convertValue(info.path("projects"), Set.class);
			}
			criteria.and("project").in(projects);
		}

		if (request.path("param").path("department").isTextual()) {
			Set<String> status = new HashSet<>();
			if (request.path("param").path("department").asText().equals(DATA_ENTRY))
				status.add("PROCESSING_FAIL");
			else if (request.path("param").path("department").asText().equals(DOCUMENT_CHECK))
				status.addAll(Arrays.asList("PROCESSING_PASS", "PROCESSING_FIX", "RETURNED"));
			else if (request.path("param").path("department").asText().equals(LOAN_BOOKING))
				status.addAll(Arrays.asList("APPROVED", "SUPPLEMENT"));
			criteria.and("status").in(status);
		}

		if (request.path("param").path("status").isTextual()) {
			criteria.and("status").in(StringUtils.commaDelimitedListToSet(request.path("param").path("status").asText()));
		}

		query.addCriteria(criteria);

		if (request.path("param").path("fromDate").textValue() != null & request.path("param").path("toDate").textValue() != null){
			Timestamp fromDate = Timestamp.valueOf(request.path("param").path("fromDate").textValue() + " 00:00:00");
			Timestamp toDate = Timestamp.valueOf(request.path("param").path("toDate").textValue() + " 23:23:59");
			query.addCriteria(Criteria.where("createdAt").gte(fromDate).lte(toDate));
		}

		if (request.path("param").path("project").textValue() != null && request.path("param").path("project").textValue().equals("dataentry")) {
			List<String> branchUser = new ArrayList<String>();
			try {
				if (info.get("branches") != null) {
					branchUser = mapper.readValue(info.get("branches").toString(), List.class);
					if (request.path("param").path("branchName").textValue() == null) {
						query.addCriteria(Criteria.where("optional.branchName").in(branchUser));
					} else {
						String[] branchQuery = request.path("param").path("branchName").textValue().split(",");
						ArrayList<String> ar = new ArrayList<String>();

						for (int i = 0; i < branchQuery.length; i++) {
							if (branchUser.contains(branchQuery[i]))
								ar.add(branchQuery[i]);
						}
						query.addCriteria(Criteria.where("optional.branchName").in(ar));
					}
				} else {
					query.addCriteria(Criteria.where("optional.branchName").in(branchUser));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

        if (request.path("param").path("fullName").textValue() != null & !request.path("param").path("fullName").asText().equals("")){
			query.addCriteria(Criteria.where("fullName").regex(StringUtils.trimWhitespace(request.path("param").path("fullName").textValue()), "i"));
        }

        if (request.path("param").path("identificationNumber").textValue() != null & !request.path("param").path("identificationNumber").asText().equals("")){
            query.addCriteria(Criteria.where("optional.identificationNumber").is(request.path("param").path("identificationNumber").textValue()));
        }

		long total = mongoTemplate.count(query, App.class);

		List<Sort.Order> orders = new ArrayList<>();
		for (int i = 0; i < sort.length-1; i=i+2) {
			orders.add(new Sort.Order(Direction.valueOf(sort[i + 1].toUpperCase()), sort[i]));
		}
		Sort sorts = new Sort(orders.toArray(new Sort.Order[orders.size()]));

//		query.with(PageRequest.of(page - 1, limit, Sort.by(Direction.fromString(sort[1]), sort[0])));
		query.with(PageRequest.of(page - 1, limit, sorts));

		List<App> list = mongoTemplate.find(query, App.class);

		return response(200, mapper.convertValue(list, JsonNode.class), total);
	}

	public JsonNode getApp(JsonNode request) throws Exception {
		Query query = Query.query(Criteria.where("id").is(request.path("param").path("id").asText()));
		App app = mongoTemplate.findOne(query, App.class);

		if (app == null) {
			return response(404, mapper.createObjectNode().put("message", "Not Found"), 0);
		}

		JsonNode res = rabbitMQService.sendAndReceive("tpf-service-" + app.getProject(),
				Map.of("func", "getDetail", "param", Map.of("id", app.getUuid().split("_")[1])));

		return response(res.path("status").asInt(), res.path("data"), 0);
	}

	public JsonNode createApp(JsonNode request) throws Exception {
		App entity = mapper.convertValue(request.path("body"), App.class);
		entity.setUuid(entity.getProject() + "_" + entity.getUuid());

		if (entity.getAutomationResult() != null) {
			entity.setAutomationHistory(
					new HashSet<>(Arrays.asList(Map.of("status", entity.getAutomationResult(), "createdAt", new Date()))));
		}
		if (entity.getStatus() != null) {
			String status = entity.getStatus().toUpperCase();
			if (status.equals("PROCESSING") && entity.getAutomationResult() != null) {
				String auto = entity.getAutomationResult().toUpperCase();
				status = (auto.equals("PASS") || auto.equals("FIX")) ? (status + "_" + auto) : (status + "_" + "FAIL");
			}
			entity.setStatus(status);
			entity.setStatusHistory(
					new HashSet<>(Arrays.asList(Map.of("status", entity.getStatus(), "createdAt", new Date()))));
		}

		try {
			entity.setFullName(StringUtils.trimWhitespace(entity.getFullName()));
		} catch (Exception e) {
		}

		mongoTemplate.save(entity);

		String dTo = getDepartment(entity);
		Map<?, ?> notify = Map.of("body", Map.of("from", "", "to", dTo, "project", entity.getProject(), "data", entity));
		rabbitMQService.send("tpf-service-webportal", notify);

		return response(201, mapper.convertValue(entity, JsonNode.class), 0);
	}

	public JsonNode updateApp(JsonNode request) throws Exception {
		String id = request.path("param").path("id").asText();
		String project = request.path("param").path("project").asText().toLowerCase();
		if (project.isEmpty()) {
			project = request.path("body").path("project").asText().toLowerCase();
		}

		Assert.hasText(id, "param id is required");
		Assert.hasText(project, "param project is required");
		Assert.notNull(request.path("body"), "no body");

		Query query = Query.query(Criteria.where("uuid").is(project + "_" + id));
		App oEntity = mongoTemplate.findOne(query, App.class);

		if (oEntity == null) {
			return response(404, mapper.createObjectNode().put("message", "Not Found"), 0);
		}

		App entity = mapper.convertValue(request.path("body"), App.class);

		Update update = new Update();
		update.set("project", project);
		if (entity.getAppId() != null) {
			update.set("appId", entity.getAppId());
		}
		if (entity.getFullName() != null) {
			update.set("fullName", StringUtils.trimWhitespace(entity.getFullName()));
		}
		if (entity.getPartnerId() != null) {
			update.set("partnerId", entity.getPartnerId());
		}
		if (entity.getOptional() != null) {
			update.set("optional", entity.getOptional());
		}
		if (!entity.getDocuments().isEmpty()) {
			update.set("documents", entity.getDocuments());
		}
		if (!entity.getComments().isEmpty()) {
			update.set("comments", entity.getComments());
		}
		if (entity.getStatus() != null) {
			String status = entity.getStatus().toUpperCase();
			if (status.equals("PROCESSING") && entity.getAutomationResult() != null) {
				String auto = entity.getAutomationResult().toUpperCase();
				status = (auto.equals("PASS") || auto.equals("FIX")) ? (status + "_" + auto) : (status + "_" + "FAIL");
			}
			entity.setStatus(status);
			update.set("status", entity.getStatus());
			update.push("statusHistory").atPosition(0).value(Map.of("status", entity.getStatus(), "createdAt", new Date()));
		}
		if (entity.getAutomationResult() != null) {
			update.set("automationResult", entity.getAutomationResult());
			if (entity.getStatus() != null && entity.getStatus().split("_")[0].equals("PROCESSING")) {
				update.push("automationHistory").atPosition(0)
						.value(Map.of("status", entity.getAutomationResult(), "createdAt", new Date()));
			}
		}
		if (entity.getAssigned() != null) {
			update.set("assigned", entity.getAssigned());
			update.push("assignedHistory").atPosition(0).value(Map.of("assigned", entity.getAssigned(), "status",
					oEntity.getStatus() != null ? oEntity.getStatus() : "", "createdAt", new Date()));
		}
		String unassigned = request.path("body").path("unassigned").asText();
		if (!unassigned.isEmpty() && unassigned.equals(oEntity.getAssigned())) {
			update.unset("assigned");
		}

		String dFrom = getDepartment(oEntity);
		String dTo = getDepartment(entity);

		if (!project.equals("dataentry")) {
			if (dTo.isEmpty()) {
				dTo = dFrom;
			}
			if (!dFrom.equals(dTo)) {
				update.unset("assigned");
			}
		}

        update.set("updatedAt", new Date());

		App nEntity = mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), App.class);

		Map<?, ?> notify = Map.of("body",
				Map.of("from", dFrom, "to", dTo, "project", nEntity.getProject(), "data", nEntity));
		rabbitMQService.send("tpf-service-webportal", notify);

		return response(200, mapper.convertValue(nEntity, JsonNode.class), 0);
	}

	

	public JsonNode updateAppId(JsonNode request) throws Exception {
		JsonNode body = request.path("body");
		Assert.isTrue(body.path("project").isTextual() && !body.path("project").asText().isEmpty(),
				"project is required and not empty");
		
		Assert.isTrue(body.path("appId").isTextual() && !body.path("appId").asText().isEmpty(),
				"appId is required and not empty");
		Assert.isTrue(body.path("partnerId").isTextual() && !body.path("partnerId").asText().isEmpty(),
				"partnerId is required and not empty");
		
		Query query = Query.query(Criteria.where("project").is(body.path("project").asText()).and("partnerId").is(body.path("partnerId").asText()));
		App app = mongoTemplate.findOne(query, App.class);

		if (app == null) {
			return response(404, mapper.createObjectNode().put("message", "PartnerId Not Found"), 0);
		}

		JsonNode res = rabbitMQService.sendAndReceive("tpf-service-" + app.getProject().toLowerCase(), Map.of("func", "updateAppId", "body", body));
		
		return response(res.path("status").asInt(), res.path("data"), 0);

	
	}
	
	public JsonNode updateStatus(JsonNode request) throws Exception {
		JsonNode body = request.path("body");

		Assert.isTrue(body.path("app_id").isTextual() && !body.path("app_id").asText().isEmpty(),
				"app_id is required and not empty");
		Assert.isTrue(body.path("status").isTextual() && !body.path("status").asText().isEmpty(),
				"status is required and not empty");
		Assert.isTrue(body.path("access_key").isTextual() && !body.path("access_key").asText().isEmpty(),
				"access_key is required and not empty");

		if (!body.path("access_key").asText().equals("access_key_db")) {
			return response(401, mapper.createObjectNode().put("message", "Unauthorized"), 0);
		}

		Query query = Query.query(Criteria.where("appId").is(body.path("app_id").asText()));
		App app = mongoTemplate.findOne(query, App.class);

		if (app == null) {
			return response(404, mapper.createObjectNode().put("message", "Not Found"), 0);
		}

		rabbitMQService.send("tpf-service-" + app.getProject(), Map.of("func", "updateStatus", "body", body));

		return response(200, null, 0);
	}
	


	public JsonNode getCountStatus(JsonNode request) {
		String referenceId = UUID.randomUUID().toString();
		try{
			AggregationOperation match1 = Aggregation.match(Criteria.where("project").in(request.path("param").path("project").textValue()));

//			AggregationOperation match1 = Aggregation.match(Criteria.where("createdDate").gte(fromDate).lte(toDate).and("status").in(inputQuery));
			AggregationOperation group = Aggregation.group("status").count().as("count");
			AggregationOperation sort = Aggregation.sort(Sort.Direction.ASC, "count");
			AggregationOperation project = Aggregation.project().andExpression("_id").as("status").andExpression("count").as("appNo");
			//AggregationOperation limit = Aggregation.limit(Constants.BOARD_TOP_LIMIT);
			Aggregation aggregation = Aggregation.newAggregation(match1, group, sort, project/*, limit*/);
			AggregationResults<ReportStatus> results = mongoTemplate.aggregate(aggregation, App.class, ReportStatus.class);

			List<ReportStatus> resultData = results.getMappedResults();

			if (resultData.size() > 0){
				return response(200, mapper.convertValue(resultData, JsonNode.class), resultData.size());
			}else{
				return response(200, null, 0);
			}
		}
		catch (Exception e) {
		}
		return response(200, null, 0);
	}
}