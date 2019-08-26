package vn.com.tpf.microservices.services;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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

import vn.com.tpf.microservices.models.App;

@Service
public class AppService {

	private static final String AUTOMATION = "automation";
	private static final String DOCUMENT_CHECK = "document_check";
	private static final String LOAN_BOOKING = "loan_booking";

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private RabbitMQService rabbitMQService;

	@SuppressWarnings("unchecked")
	public Map<String, Object> getListApp(JsonNode request, JsonNode info) {
		int page = request.path("param").path("page").asInt(1);
		int limit = request.path("param").path("limit").asInt(10);
		String[] sort = request.path("param").path("sort").asText("createdAt,desc").split(",");

		Query query = new Query();
		Criteria criteria = Criteria.where("assigned").is(null);

		if (request.path("param").path("assigned").isTextual()) {
			if (info.path("authorities").toString()
					.matches(".*(\"role_root\"|\"role_admin\"|\"role_manager\"|\"role_viewer\").*")) {
				criteria = Criteria.where("assigned").ne(null);
			} else {
				criteria = Criteria.where("assigned").is(request.path("param").path("assigned").asText());
			}
		}

		if (request.path("param").path("appId").isTextual()) {
			criteria.and("appId").regex(request.path("param").path("appId").asText(), "i");
		}

		if (!info.path("authorities").toString().matches(".*(\"role_root\").*")) {
			Set<String> projects = new HashSet<>();
			if (request.path("param").path("project").isTextual()) {
				projects.add(request.path("param").path("project").asText());
			} else if (info.path("projects").isArray()) {
				projects = mapper.convertValue(info.path("projects"), Set.class);
			}
			criteria.and("project").in(projects);

			Set<String> status = new HashSet<>();
			if (info.path("departments").isArray()) {
				Set<String> departments = mapper.convertValue(info.path("departments"), Set.class);
				departments.forEach(e -> {
					if (e.equals(AUTOMATION))
						status.add("PROCESSING_FAIL");
					else if (e.equals(DOCUMENT_CHECK))
						status.addAll(Arrays.asList("PROCESSING_PASS", "PROCESSING_FIX", "RETURNED"));
					else if (e.equals(LOAN_BOOKING))
						status.addAll(Arrays.asList("APPROVED", "SUPPLEMENT"));
				});
			}
			criteria.and("status").in(status);
		}

		query.addCriteria(criteria);

		long total = mongoTemplate.count(query, App.class);

		query.with(PageRequest.of(page - 1, limit, Sort.by(Direction.fromString(sort[1]), sort[0])));
		List<App> list = mongoTemplate.find(query, App.class);

		return Map.of("status", 200, "data", list, "total", total);
	}

	public Map<String, Object> createApp(JsonNode request) throws Exception {
		String project = request.path("param").path("project").asText().toLowerCase();
		if (project.isEmpty()) {
			project = request.path("body").path("project").asText().toLowerCase();
		}

		Assert.hasText(project, "param project is required");
		Assert.notNull(request.get("body"), "no body");

		App entity = new App();

		if (project.equals("fpt"))
			convertFpt(entity, request, "create");
		else if (project.equals("vinid"))
			convertVinId(entity, request, "create");
		else if (project.equals("momo"))
			convertMomo(entity, request, "create");
		else if (project.equals("trustingsocial"))
			convertTrustingSocial(entity, request, "create");
		else
			return Map.of("status", 400, "data", Map.of("message", "Project Invalid"));

		Date date = new Date();
		if (entity.getStatus() != null) {
			entity.setStatusHistory(new HashSet<>(Arrays.asList(Map.of("status", entity.getStatus(), "createdAt", date))));
		}
		if (entity.getAutomationResult() != null) {
			entity.setAutomationHistory(
					new HashSet<>(Arrays.asList(Map.of("status", entity.getAutomationResult(), "createdAt", date))));
		}

		mongoTemplate.save(entity);

		String roomTo = getRoom(entity);
		if (!roomTo.isEmpty()) {
			Map<?, ?> body = Map.of("from", "", "to", roomTo, "project", entity.getProject(), "data", entity);
			Map<?, ?> notify = Map.of("token", rabbitMQService.getToken().path("access_token"), "body", body);
			rabbitMQService.send("tpf-service-webportal", notify);
		}

		return Map.of("status", 201, "data", entity);
	}

	public Map<String, Object> updateApp(JsonNode request) throws Exception {
		String id = request.path("param").path("id").asText();
		String project = request.path("param").path("project").asText().toLowerCase();
		if (project.isEmpty()) {
			project = request.path("body").path("project").asText().toLowerCase();
		}

		Assert.hasText(id, "param id is required");
		Assert.hasText(project, "param project is required");
		Assert.notNull(request.get("body"), "no body");

		Query query = Query.query(Criteria.where("uuid").is(project + "_" + id));
		App oEntity = mongoTemplate.findOne(query, App.class);

		if (oEntity == null) {
			return Map.of("status", 404, "data", Map.of("message", "Not Found"));
		}

		App entity = new App();

		if (project.equals("fpt"))
			convertFpt(entity, request, "update");
		else if (project.equals("vinid"))
			convertVinId(entity, request, "update");
		else if (project.equals("momo"))
			convertMomo(entity, request, "update");
		else if (project.equals("trustingsocial"))
			convertTrustingSocial(entity, request, "update");
		else
			return Map.of("status", 400, "data", Map.of("message", "Project Invalid"));

		Update update = new Update();
		update.set("project", entity.getProject());
		if (entity.getAppId() != null) {
			update.set("appId", entity.getAppId());
		}
		if (entity.getFullName() != null) {
			update.set("fullName", entity.getFullName());
		}
		if (entity.getPartnerId() != null) {
			update.set("partnerId", entity.getPartnerId());
		}
		if (entity.getProductCode() != null) {
			update.set("productCode", entity.getProductCode());
		}
		if (entity.getSchemeCode() != null) {
			update.set("schemeCode", entity.getSchemeCode());
		}
		if (!entity.getPhotos().isEmpty()) {
			update.set("photos", entity.getPhotos());
		}
		if (entity.getStatus() != null) {
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
			update.push("assignedHistory").atPosition(0)
					.value(Map.of("assigned", entity.getAssigned(), "status", oEntity.getStatus(), "createdAt", new Date()));
		}
		String unassigned = request.path("body").path("unassigned").asText();
		if (!unassigned.isEmpty() && unassigned.equals(oEntity.getAssigned())) {
			update.unset("assigned");
		}

		String roomFrom = getRoom(oEntity);
		String roomTo = getRoom(entity);

		if (roomTo.isEmpty()) {
			roomTo = roomFrom;
		}

		if (!roomFrom.equals(roomTo)) {
			update.unset("assigned");
		}

		App nEntity = mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), App.class);

		if (!roomFrom.isEmpty() && !roomTo.isEmpty()) {
			Map<?, ?> body = Map.of("from", roomFrom, "to", roomTo, "project", nEntity.getProject(), "data", nEntity);
			Map<?, ?> notify = Map.of("token", rabbitMQService.getToken().path("access_token"), "body", body);
			rabbitMQService.send("tpf-service-webportal", notify);
		}

		return Map.of("status", 200, "data", nEntity);
	}

	private String getRoom(App entity) {
		String status = entity.getStatus() != null ? entity.getStatus() : "";
		String room = "";

		if (status.equals("PROCESSING_FAIL"))
			room = AUTOMATION;
		else if (status.equals("PROCESSING_PASS") || status.equals("PROCESSING_FIX") || status.equals("RETURNED"))
			room = DOCUMENT_CHECK;
		else if (status.equals("APPROVED") || status.equals("SUPPLEMENT"))
			room = LOAN_BOOKING;

		return room;
	}

	private void convertFpt(App entity, JsonNode request, String type) {
		JsonNode body = request.path("body");
		JsonNode uuid = body.path("id");
		JsonNode partnerId = body.path("custId");
		JsonNode firstName = body.path("firstName");
		JsonNode middleName = body.path("middleName");
		JsonNode lastName = body.path("lastName");
		JsonNode automationResult = body.path("automationResult");
		JsonNode photos = body.path("photos");
		JsonNode status = body.path("status");
		JsonNode scheme = body.path("loanDetail").path("product");
		JsonNode assigned = body.path("assigned");
		JsonNode appId = body.path("appId");

		if (type.equals("create")) {
			Assert.isTrue(uuid.isNumber(), "id is required number");
			Assert.isTrue(partnerId.isNumber(), "custId is required number");
			Assert.isTrue(firstName.isTextual() && !firstName.asText().isEmpty(),
					"firstName is required string and not empty");
			Assert.isTrue(middleName.isTextual(), "middleName is required string");
			Assert.isTrue(lastName.isTextual() && !lastName.asText().isEmpty(), "lastName is required string and not empty");
			Assert.isTrue(automationResult.isTextual() && !automationResult.asText().isEmpty(),
					"automationResult is required string and not empty");
			Assert.isTrue(status.isTextual() && !status.asText().isEmpty(), "status is required string and not empty");
			Assert.isTrue(scheme.isTextual() && !scheme.asText().isEmpty(),
					"loanDetail.product is required string and not empty");
			Assert.isTrue(photos.isArray(), "photos is required array and not empty");
		}

		entity.setProject("fpt");
		if (uuid.isNumber()) {
			entity.setUuid("fpt_" + uuid.asText());
		}
		if (assigned.isTextual() && !assigned.asText().isEmpty()) {
			entity.setAssigned(assigned.asText());
		}
		if (appId.isTextual() && !appId.asText().isEmpty()) {
			entity.setAppId(appId.asText());
		}
		if (firstName.isTextual() && !firstName.asText().isEmpty() && middleName.isTextual()
				&& !middleName.asText().isEmpty() && lastName.isTextual() && !lastName.asText().isEmpty()) {
			entity.setFullName(
					(firstName.asText() + " " + middleName.asText() + " " + lastName.asText()).replaceAll("\\s+", " "));
		}
		if (partnerId.isNumber()) {
			entity.setPartnerId(partnerId.asText());
		}
		if (scheme.isTextual() && !scheme.asText().isEmpty()) {
			entity.setSchemeCode(scheme.asText());
			entity.setProductCode(entity.getSchemeCode().split("_")[0]);
		}
		if (automationResult.isTextual() && !automationResult.asText().isEmpty()) {
			entity.setAutomationResult(automationResult.asText());
		}
		if (status.isTextual() && !status.asText().isEmpty()) {
			String sts = status.asText().toUpperCase();
			if (sts.equals("PROCESSING") && entity.getAutomationResult() != null) {
				String auto = entity.getAutomationResult().toUpperCase();
				sts = (auto.equals("PASS") || auto.equals("FIX")) ? (sts + "_" + auto) : (sts + "_" + "FAIL");
			}
			entity.setStatus(sts);
		}
		if (photos.isArray()) {
			Set<Map<?, ?>> pts = new HashSet<>();
			photos.forEach(e -> {
				pts.add(
						Map.of("type", e.path("documentType").asText(), "link", e.path("link").asText(), "createdAt", new Date()));
			});
			entity.setPhotos(pts);
		}
	}

	private void convertVinId(App entity, JsonNode request, String type) {

	}

	private void convertMomo(App entity, JsonNode request, String type) {

	}

	private void convertTrustingSocial(App entity, JsonNode request, String type) {
		JsonNode body = request.path("body");
		JsonNode uuid = body.path("id");
		JsonNode partnerId = body.path("tsLeadId");
		JsonNode firstName = body.path("firstName");
		JsonNode middleName = body.path("middleName");
		JsonNode lastName = body.path("lastName");
		JsonNode photos = body.path("documents");
		JsonNode status = body.path("status");
		JsonNode nationalId = body.path("nationalId");
		JsonNode assigned = body.path("assigned");
		JsonNode appId = body.path("appId");

		if (type.equals("create")) {
			Assert.isTrue(uuid.isTextual() && !uuid.asText().isEmpty(), "id is required string and not empty");
			Assert.isTrue(partnerId.isTextual() && !partnerId.asText().isEmpty(),
					"tsLeadId is required string and not empty");
			Assert.isTrue(firstName.isTextual() && !firstName.asText().isEmpty(),
					"firstName is required string and not empty");
			Assert.isTrue(middleName.isTextual(), "middleName is required string");
			Assert.isTrue(lastName.isTextual() && !lastName.asText().isEmpty(), "lastName is required string and not empty");
			Assert.isTrue(status.isTextual() && !status.asText().isEmpty(), "status is required string and not empty");
			Assert.isTrue(nationalId.isTextual() && !nationalId.asText().isEmpty(),
					"nationalId is required string and not empty");
			Assert.isTrue(photos.isArray(), "documents is required array and not empty");
		}

		entity.setProject("trustingsocial");
		if (uuid.isTextual() && !uuid.asText().isEmpty()) {
			entity.setUuid("trustingsocial_" + uuid.asText());
		}
		if (assigned.isTextual() && !assigned.asText().isEmpty()) {
			entity.setAssigned(assigned.asText());
		}
		if (appId.isTextual() && !appId.asText().isEmpty()) {
			entity.setAppId(appId.asText());
		}
		if (firstName.isTextual() && !firstName.asText().isEmpty() && middleName.isTextual() && lastName.isTextual()
				&& !lastName.asText().isEmpty()) {
			entity.setFullName(
					(firstName.asText() + " " + middleName.asText() + " " + lastName.asText()).replaceAll("\\s+", " "));
		}
		if (partnerId.isTextual() && !partnerId.asText().isEmpty()) {
			entity.setPartnerId(partnerId.asText());
		}
		if (nationalId.isTextual() && !nationalId.asText().isEmpty()) {
			entity.setNationalId(nationalId.asText());
		}
		if (status.isTextual() && !status.asText().isEmpty()) {
			String sts = status.asText().toUpperCase();
			if (sts.equals("PROCESSING")) {
				sts = sts + "_" + "PASS";
			}
			entity.setStatus(sts);
		}
		if (photos.isArray()) {
			Set<Map<?, ?>> pts = new HashSet<>();
			photos.forEach(e -> {
				pts.add(Map.of("type", e.path("document_type").asText(), "link", e.path("view_url").asText(), "download",
						e.path("donwload_url").asText(), "createdAt", new Date()));
			});
			entity.setPhotos(pts);
		}
	}

}