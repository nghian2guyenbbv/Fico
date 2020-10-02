package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import vn.com.tpf.microservices.commons.AllocationTeamConfig;
import vn.com.tpf.microservices.dao.*;
import vn.com.tpf.microservices.models.*;

import javax.persistence.criteria.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@EnableScheduling
public class AutoAllocationService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ETLDataPushDAO etlDataPushDAO;

	@Autowired
	private UserCheckingDAO userCheckingDAO;

	@Autowired
	AllocationHistoryViewDao allocationHistoryViewDao;

	@Autowired
	AssignConfigDAO assignConfigDAO;

	@Autowired
	TeamConfigDAO teamConfigDAO;

	@Autowired
	UserDetailsDAO userDetailsDAO;

	@Autowired
	private RabbitMQService rabbitMQService;

	@Autowired
	AssignmentDetailDAO assignmentDetailDAO;

	@Value("${spring.syncrobot.limit}")
	private Integer limit;

	@Value("${spring.syncpro.fromTime}")
	private String fromTimePro;

	@Value("${spring.syncpro.toTime}")
	private String toTimePro;

	@Value("${spring.syncrobot.fromTime}")
	private String fromTimeRobot;

	@Value("${spring.syncrobot.toTime}")
	private String toTimeRobot;

	private static String ROLE_LEADER = "role_leader";
	private static String ROLE_SUB = "role_supervisor";
	private static String KEY_ASSIGN_APP = "WAITING";

	public Map<String, Object> getAllUser(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		log.info("getAllUser: " + request);
		try {
			Assert.notNull(request.get("body"), "no body");
			RequestGetUserDetail requestModel = mapper.treeToValue(request.get("body"), RequestGetUserDetail.class);

			Sort typeSort = Sort.by(requestModel.getSortItem()).descending();
			if (requestModel.getTypeSort().equals("ASC")) {
				typeSort = Sort.by(requestModel.getSortItem()).ascending();
			}

			Pageable pageable = PageRequest.of(requestModel.getPage() , requestModel.getItemPerPage(), typeSort);

			Page<UserDetail> listUserDetails;
			if ( requestModel.getRoleUserLogin().equals(ROLE_LEADER)) {
				listUserDetails = userDetailsDAO.findAllUserForLeader(requestModel.getUserLogin(), pageable);
			} else {
				String teamName1 = requestModel.getTeamName();
				String teamName2 = "";
				if ( teamName1 != null && requestModel.getTeamName().contains(",")) {
					teamName1 = requestModel.getTeamName().substring(0 ,requestModel.getTeamName().indexOf(","));
					teamName2 =  requestModel.getTeamName().substring(requestModel.getTeamName().indexOf(",") + 1,
							requestModel.getTeamName().length());
				}
				listUserDetails = userDetailsDAO.findAllUserForSub(teamName1, teamName2, pageable);
			}

			if (listUserDetails.getContent().size() <= 0 || listUserDetails.getContent() == null) {
				responseModel.setRequest_id(request_id);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(500);
				responseModel.setMessage("Empty");
				return Map.of("status", 200, "data", responseModel);
			}

			responseModel.setRequest_id(request_id);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(200);
			responseModel.setMessage("Get success");
			Map<String, Object> result = new HashMap<>();
			result.put("data", listUserDetails.getContent());
			result.put("totalRecords", listUserDetails.getTotalElements());
			responseModel.setData(result);

		} catch (Exception e) {
			log.info("Error: " + e);
			responseModel.setRequest_id(request_id);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(500);
			responseModel.setMessage("Others error");
			log.info("{}", e);
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> getUser(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		log.info("getUser: " + request);
		try {
			Assert.notNull(request.get("body"), "no body");
			RequestGetUserDetail requestModel = mapper.treeToValue(request.get("body"), RequestGetUserDetail.class);

			if (requestModel == null) {
				responseModel.setRequest_id(request_id);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(500);
				responseModel.setMessage("Empty");
				return Map.of("status", 200, "data", responseModel);
			}

			if (requestModel.getRoleUserLogin().equals(ROLE_LEADER)) {
				UserDetail userDetail = userDetailsDAO.findByUserNameAndTeamLeader(requestModel.getUserName(),
						requestModel.getUserLogin());

				if (userDetail == null) {
					responseModel.setRequest_id(request_id);
					responseModel.setReference_id(UUID.randomUUID().toString());
					responseModel.setDate_time(new Timestamp(new Date().getTime()));
					responseModel.setResult_code(500);
					responseModel.setMessage("Empty");
					return Map.of("status", 200, "data", responseModel);
				} else {
					responseModel.setRequest_id(request_id);
					responseModel.setReference_id(UUID.randomUUID().toString());
					responseModel.setDate_time(new Timestamp(new Date().getTime()));
					responseModel.setResult_code(200);
					Map<String, Object> result = new HashMap<>();
					List<UserDetail> userDetailList = new ArrayList<>();
					userDetailList.add(userDetail);
					result.put("data", userDetailList);
					result.put("totalRecords", 1);
					responseModel.setData(result);
				}
			} else if (requestModel.getRoleUserLogin().equals(ROLE_SUB)) {
				if (requestModel.getUserName() == null || requestModel.getUserName().isEmpty()) {
					Sort typeSort = Sort.by(requestModel.getSortItem()).descending();
					if (requestModel.getTypeSort() == "ESC") {
						typeSort = Sort.by(requestModel.getSortItem()).ascending();
					}

					Pageable pageable = PageRequest.of(requestModel.getPage() , requestModel.getItemPerPage(), typeSort);

					Page<UserDetail> listUserDetails = userDetailsDAO.findAllByTeamLeader(
							requestModel.getUserLeader(), pageable);

					if (listUserDetails.getSize() <= 0 || listUserDetails == null) {
						responseModel.setRequest_id(request_id);
						responseModel.setReference_id(UUID.randomUUID().toString());
						responseModel.setDate_time(new Timestamp(new Date().getTime()));
						responseModel.setResult_code(500);
						responseModel.setMessage("Empty");
						return Map.of("status", 200, "data", responseModel);
					}

					responseModel.setRequest_id(request_id);
					responseModel.setReference_id(UUID.randomUUID().toString());
					responseModel.setDate_time(new Timestamp(new Date().getTime()));
					responseModel.setResult_code(200);
					responseModel.setMessage("Get success");
					Map<String, Object> result = new HashMap<>();
					result.put("data", listUserDetails.getContent());
					result.put("totalRecords", listUserDetails.getTotalElements());
					responseModel.setData(result);
				} else {
					String teamName1 = requestModel.getTeamName();
					String teamName2 = "";
					if (requestModel.getTeamName().contains(",")) {
						teamName1 = requestModel.getTeamName().substring(0 ,requestModel.getTeamName().indexOf(","));
						teamName2 =  requestModel.getTeamName().substring(requestModel.getTeamName().indexOf(",") + 1,
								requestModel.getTeamName().length());
					}
					List<UserDetail> userDetail = userDetailsDAO.findByUserNameAndTeamName(requestModel.getUserName(),
							teamName1, teamName2);

					if (userDetail == null) {
						responseModel.setRequest_id(request_id);
						responseModel.setReference_id(UUID.randomUUID().toString());
						responseModel.setDate_time(new Timestamp(new Date().getTime()));
						responseModel.setResult_code(500);
						responseModel.setMessage("Empty");
						return Map.of("status", 200, "data", responseModel);
					} else {
						responseModel.setRequest_id(request_id);
						responseModel.setReference_id(UUID.randomUUID().toString());
						responseModel.setDate_time(new Timestamp(new Date().getTime()));
						responseModel.setResult_code(200);
						Map<String, Object> result = new HashMap<>();
						result.put("data", userDetail);
						result.put("totalRecords", userDetail.size());
						responseModel.setData(result);
					}
				}
			}
		} catch (Exception e) {
			log.info("Error: " + e);
			responseModel.setRequest_id(request_id);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(500);
			responseModel.setMessage("Others error");
			log.info("{}", e);
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> getHistoryApp(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		log.info("getHistoryApp - Request: " + request);
		try {
			Assert.notNull(request.get("body"), "no body");
			Map<String, Object> result = new HashMap<>();
			String appNumber = request.get("body").path("appNumber") != null ?
					request.get("body").path("appNumber").asText() : "";
			String assignee = request.get("body").path("assignee") != null ?
					request.get("body").path("assignee").asText() : "";
			String userName = request.get("body").path("userName") != null ?
					request.get("body").path("userName").asText() : "";
			String statusAssign = request.get("body").path("statusAssign") != null ?
					request.get("body").path("statusAssign").asText(): "";
			String from = request.get("body").path("from") != null ?
					request.get("body").path("from").asText(): "";
			String to = request.get("body").path("to") != null ?
					request.get("body").path("to").asText(): "";
			String role = request.get("body").path("role") != null ?
					request.get("body").path("role").asText(): "";
			List<String> teamNames = new ArrayList<>();
			List<JsonNode> jsonNodeTeamNames = new ArrayList<>();
			Iterator<JsonNode> i = request.get("body").path("teamName").iterator();
			i.forEachRemaining(jsonNodeTeamNames::add);
			for(JsonNode j : jsonNodeTeamNames) {
				teamNames.add(j.asText());
			}
			int pageSize = request.get("body").path("pageSize").asInt();
			int limit = request.get("body").path("limit").asInt();
			String sortType = request.get("body").path("sortType") != null ?
					request.get("body").path("sortType").asText(): "";
			String sortStyle = request.get("body").path("sortStyle") != null ?
					request.get("body").path("sortStyle").asText(): "";

			Pageable pagination = PageRequest.of(pageSize - 1, limit);

			if (!sortType.isEmpty()) {
				Sort sort;
				if (sortStyle.equals("ASC")) {
					sort = Sort.by(sortType).ascending();
				} else {
					sort = Sort.by(sortType).descending();
				}
				pagination = PageRequest.of(pageSize - 1 , limit, sort);
			}


			Page<AllocationHistoryView> allocationHistoryViews	= allocationHistoryViewDao.findAll(new Specification() {
				@Override
				public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();

					if (!userName.isEmpty()) {
						if (role.equals("role_leader")) {
							predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("teamLeader"), userName)));
						} else if (role.equals("role_supervisor") && teamNames.size() > 0){
							CriteriaBuilder.In<String> inClause = criteriaBuilder.in(root.get("teamName"));
							for (String t : teamNames) {
								inClause.value(t);
							}
							predicates.add(criteriaBuilder.and(inClause));
						} else {
							predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("assignee"), userName)));
						}
					}

					if(!assignee.isEmpty()) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("assignee"), assignee)));
					}

					if(!appNumber.isEmpty()) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("appNumber"), appNumber)));
					}
					if(!statusAssign.isEmpty()) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("statusAssign"), statusAssign)));
					}
					if(!from.isEmpty()) {
						Timestamp fromTimestamp = Timestamp.valueOf(from);
						predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThan(root.get("assignedTime"), fromTimestamp)));
					}
					if(!to.isEmpty()) {
						Timestamp toTimestamp = Timestamp.valueOf(to);
						predicates.add(criteriaBuilder.and(criteriaBuilder.lessThan(root.get("assignedTime"), toTimestamp)));
					}

					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			}, pagination);
			result.put("data", allocationHistoryViews.getContent());
			result.put("totalRecords", allocationHistoryViews.getTotalElements());
			responseModel.setData(result);
		} catch (Exception e) {
			log.info("getHistoryApp - Error: {}, class: {}, line: {}", e, e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getLineNumber());
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(500);
			responseModel.setMessage("Others error");
		}

		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> uploadUser(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		log.info("uploadUser: " + request);
		try {
			Assert.notNull(request.get("body"), "no body");
			RequestImportFile requestModel = mapper.treeToValue(request.get("body"), RequestImportFile.class);

		if (requestModel == null) {
			responseModel.setRequest_id(request_id);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(500);
			responseModel.setMessage("File is mandatory");
		} else {
			if (requestModel.getListUser() == null || requestModel.getListUser().size() <= 0 || requestModel.getListUser().isEmpty()) {
				responseModel.setRequest_id(request_id);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(500);
				responseModel.setMessage("List user in file is empty.");
				return Map.of("status", 200, "data", responseModel);
			}

			for (UserChecking us : requestModel.getListUser()) {
				us.setCreateDate(new Timestamp(new Date().getTime()));
				us.setUserRole("role_user");
				us.setCheckedFlag("OPEN");
				us.setUserLogin(requestModel.getUserLogin());
			}

			userCheckingDAO.saveAll(requestModel.getListUser());

			String query = String.format("SELECT  FN_CHECKING_USER ('%s','%s') RESULT FROM DUAL",
					requestModel.getUserLogin(),
					requestModel.getTeamNameUser());

			String row_string = jdbcTemplate.queryForObject(query,new Object[]{},
					(rs, rowNum) ->
							rs.getString(("RESULT")
							));

			if (row_string == null || row_string.isEmpty()) {
				responseModel.setRequest_id(request_id);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(200);
				responseModel.setMessage("Add user success !");
			} else {
				responseModel.setRequest_id(request_id);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(500);
				responseModel.setMessage(row_string + "add failed.");
				return Map.of("status", 200, "data", responseModel);
			}
		}
		} catch (Exception e) {
			log.info("Error: " + e);
			responseModel.setRequest_id(request_id);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(500);
			responseModel.setMessage("Others error");

			log.info("{}", e);
		}
		return Map.of("status", 200, "data", responseModel);
		}


	public Map<String, Object> addUser(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		log.info("addUser: " + request);
		try {
			Assert.notNull(request.get("body"), "no body");
			UserDetail requestModel = mapper.treeToValue(request.get("body"), UserDetail.class);

			if (requestModel == null) {
				responseModel.setRequest_id(request_id);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(500);
				responseModel.setMessage("Add User Failed. Please fill all data of User.");
			} else {
				if (requestModel.getUserName().isEmpty()){
					responseModel.setRequest_id(request_id);
					responseModel.setReference_id(UUID.randomUUID().toString());
					responseModel.setDate_time(new Timestamp(new Date().getTime()));
					responseModel.setResult_code(500);
					responseModel.setMessage("User name is mandatory !");
					return Map.of("status", 200, "data", responseModel);
				}

				requestModel.setCreateDate(new Timestamp(new Date().getTime()));

				String query = String.format("SELECT  FN_ADD_USER ('%s','%s','%s','%s','%s','%s','%s') RESULT FROM DUAL",
						requestModel.getUserLogin(),
						requestModel.getRoleUserLogin(),
						requestModel.getUserName(),
						requestModel.getUserRole(),
						requestModel.getTeamName(),
						requestModel.getActiveFlag(),
						requestModel.getTeamLeader()
						);

				String row_string = jdbcTemplate.queryForObject(query,new Object[]{},
						(rs, rowNum) ->
								rs.getString(("RESULT")
								));

				if (row_string.equals("ADD USER SUCCESS")) {
					responseModel.setRequest_id(request_id);
					responseModel.setReference_id(UUID.randomUUID().toString());
					responseModel.setDate_time(new Timestamp(new Date().getTime()));
					responseModel.setResult_code(200);
					responseModel.setMessage("ADD USER SUCCESS !");
				} else {
					responseModel.setRequest_id(request_id);
					responseModel.setReference_id(UUID.randomUUID().toString());
					responseModel.setDate_time(new Timestamp(new Date().getTime()));
					responseModel.setResult_code(500);
					responseModel.setMessage(row_string);
					return Map.of("status", 200, "data", responseModel);
				}
			}
		} catch (Exception e) {
			log.info("Error: " + e);
			responseModel.setRequest_id(request_id);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(500);
			responseModel.setMessage("Others error");

			log.info("{}", e);
		}
		return Map.of("status", 200, "data", responseModel);
	}

	/**
	 * To save log when inhouse call this service
	 * @param request
	 * @return
	 */
	public Map<String, Object> sendAppFromF1(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		log.info("sendAppFromF1: " + request);
		String ts = String.valueOf(request.get("body").get("createdDate").asText().replace(" ","T"));
		JsonNode jsonNode = request.get("body");
		((ObjectNode) jsonNode).put("createdDate", ts );
		try {
			Assert.notNull(request.get("body"), "no body");
			RequestModel requestModel = mapper.treeToValue(jsonNode, RequestModel.class);

			if (requestModel == null) {
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(500);
				responseModel.setMessage("Data is empty");
			} else {
				ETLDataPush etlDataPush = new ETLDataPush();
				etlDataPush.setAppNumber(requestModel.getApplicationNo());
				etlDataPush.setCreateDate(requestModel.getCreatedDate());
				etlDataPush.setCreateUser(requestModel.getCreatedUser());
				etlDataPush.setCusId(requestModel.getCustomerID());
				etlDataPush.setCusName(requestModel.getCustomerName());
				etlDataPush.setLeadId(requestModel.getLeadId());
				etlDataPush.setLoanAmt(requestModel.getLoanAmountRequested());
				etlDataPush.setProduct(requestModel.getProduct());
				etlDataPush.setScheme(requestModel.getScheme());
				etlDataPush.setStageName(requestModel.getStage());
				etlDataPush.setStatus(requestModel.getStatus());
				etlDataPush.setSuorceChanel(requestModel.getSourceChannel());
				etlDataPush.setUpdateDate("");
				etlDataPush.setSuorceEtl("ESB");

				etlDataPushDAO.save(etlDataPush);

				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(200);
				responseModel.setMessage("Push success.");
			}
		} catch (Exception e) {
			log.info("Error: " + e);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(500);
			responseModel.setMessage("Others error");

			log.info("{}", e);
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> getAssignConfig() {
		ResponseModel responseModel = new ResponseModel();
		try {
			Map<String, Object> result = new HashMap<>();
			Map<String, AssignConfigResponse> mapAssignConfig = new HashMap<>();
			List<AssignConfig> lst = assignConfigDAO.findAllByOrderByStageName();
			List<String> listTeamName = teamConfigDAO.getListTeamName();

			for (AssignConfig ac : lst) {
				if(!mapAssignConfig.containsKey(ac.getStageName())) {
					AssignConfigResponse assignConfigResponse = new AssignConfigResponse();
					assignConfigResponse.setStageName(ac.getStageName());
					Map<String, AssignConfigProductResponse> assignConfigProductResponseMap = new HashMap<>();
					AssignConfigProductResponse assignConfigProductResponse = new AssignConfigProductResponse();
					assignConfigProductResponse.setId(ac.getId());
					assignConfigProductResponse.setAssignFlag(ac.getAssignFlag());
					assignConfigProductResponse.setTeamName(ac.getTeamName());
					assignConfigProductResponseMap.put(ac.getProduct(), assignConfigProductResponse);
					assignConfigResponse.setProducts(assignConfigProductResponseMap);
					mapAssignConfig.put(ac.getStageName(), assignConfigResponse);
				} else {
					AssignConfigProductResponse assignConfigProductResponse = new AssignConfigProductResponse();
					assignConfigProductResponse.setId(ac.getId());
					assignConfigProductResponse.setAssignFlag(ac.getAssignFlag());
					assignConfigProductResponse.setTeamName(ac.getTeamName());
					Map<String, AssignConfigProductResponse> product = mapAssignConfig.get(ac.getStageName()).getProducts();
					product.put(ac.getProduct(),assignConfigProductResponse);
				}
			}
			result.put("data", mapAssignConfig);
			result.put("lstTeamName", listTeamName);
			responseModel.setData(result);
			responseModel.setResult_code(200);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
		} catch (Exception e) {
			log.info("getAssignConfig - Error: {}, class: {}, line: {}", e, e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getLineNumber());
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(500);
			responseModel.setMessage("Others error");

		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String,Object> setAssignConfig(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		try {
			log.info("setAssignConfig - Request: " + request);
			AssignConfig assignConfigRequest = mapper.readValue(request.get("body").toString(),
					new TypeReference<AssignConfig>(){});
			Date date = new Date();
			Timestamp now = new Timestamp(date.getTime());
			if (assignConfigRequest != null) {
				if (assignConfigRequest.getId() != null) {
					Optional<AssignConfig> assignConfig = assignConfigDAO.findById(assignConfigRequest.getId());
					assignConfigRequest.setUpdateDate(now);
					assignConfigRequest.setCreateDate(assignConfig.get().getCreateDate());
					assignConfigRequest.setUserCreate(assignConfig.get().getUserCreate());
				} else {
					assignConfigRequest.setCreateDate(now);
				}
				assignConfigDAO.save(assignConfigRequest);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(200);
				responseModel.setMessage("Save success");
			} else {
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(500);
				responseModel.setMessage("Request error");
			}

		} catch (Exception e) {
			log.info("setAssignConfig - Error: {}, class: {}, line: {}", e, e.getStackTrace()[0].getClassName(),
					e.getStackTrace()[0].getLineNumber());
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(500);
			responseModel.setMessage("Others error");
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> getDashboard(JsonNode request) {
		String logStr = "";
		logStr += "Request Data : " + request;
		ResponseModel responseModel = new ResponseModel();
		responseModel.setReference_id(UUID.randomUUID().toString());
		try {
		String SQL = "SELECT * FROM {PAGE} WHERE CASE {USER_ROLE} WHEN 'role_user' THEN ASSIGNEE WHEN 'role_leader' THEN TEAM_LEADER END = {USER_LOGIN}";
		String SQLSUP = "SELECT A.* FROM {PAGE} A JOIN ALLOCATION_USER_DETAIL B ON A.TEAM_NAME = B.TEAM_NAME AND B.USER_ROLE = 'role_supervisor' AND B.USER_NAME = {USER_LOGIN}";

		List<Map<String, Object>> dashboards;
		if(!request.path("body").path("userRole").textValue().equals("role_supervisor")){
			SQL = SQL.replace("{PAGE}", "V_ALLOCATION_DASHBOARD");
			SQL = SQL.replace("{USER_ROLE}", "'"+request.path("body").path("userRole").textValue()+"'");
			SQL = SQL.replace("{USER_LOGIN}", "'"+request.path("body").path("userLogin").textValue()+"'");
			dashboards = jdbcTemplate.queryForList(SQL);
		}else{
			SQLSUP = SQLSUP.replace("{PAGE}", "V_ALLOCATION_DASHBOARD");
			SQLSUP = SQLSUP.replace("{USER_LOGIN}", "'"+request.path("body").path("userLogin").textValue()+"'");
			dashboards = jdbcTemplate.queryForList(SQLSUP);
		}
		ArrayNode dataArrayNode = mapper.createArrayNode();
		if (dashboards!=null && !dashboards.isEmpty()) {
			for (Map<String, Object> dashboard : dashboards) {
				ObjectNode data = mapper.createObjectNode();
				for (Iterator<Map.Entry<String, Object>> it = dashboard.entrySet().iterator(); it.hasNext();) {
					Map.Entry<String, Object> entry = it.next();
					data.put(entry.getKey(), String.valueOf(entry.getValue()));
				}
				dataArrayNode.add(data);
			}
		}
		responseModel.setData(dataArrayNode);
		logStr += "Response: " + responseModel.toString();
		} catch (Exception e) {
			log.error("Error: " + e);
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(500);
			responseModel.setMessage("Others error");
		}finally {
			log.info(logStr);
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> getPending(JsonNode request) {
		String logStr = "";
		logStr += "Request Data : " + request;
		ResponseModel responseModel = new ResponseModel();
		responseModel.setReference_id(UUID.randomUUID().toString());
		try{
			String SQL = "SELECT * FROM V_ALLOCATION_TEAM_CONFIG P WHERE P.USER_NAME = {USER_LOGIN}";
			SQL = SQL.replace("{USER_LOGIN}", "'"+request.path("body").path("userLogin").textValue()+"'");
			List<Map<String, Object>> pending = jdbcTemplate.queryForList(SQL);
			ArrayNode dataArrayNode = mapper.createArrayNode();
			if (pending!=null && !pending.isEmpty()) {
				for (Map<String, Object> dashboard : pending) {
					ObjectNode data = mapper.createObjectNode();
					for (Iterator<Map.Entry<String, Object>> it = dashboard.entrySet().iterator(); it.hasNext();) {
						Map.Entry<String, Object> entry = it.next();
						data.put(AllocationTeamConfig.getCode(entry.getKey()), String.valueOf(entry.getValue()));
					}
					dataArrayNode.add(data);
				}
			}
			responseModel.setData(dataArrayNode);
			logStr += "Response: " + responseModel.toString();
		} catch (Exception e) {
			log.error("Error: " + e);
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(500);
			responseModel.setMessage("Others error");
		}finally {
			log.info(logStr);
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> updatePending(JsonNode request) {
		String logStr = "";
		logStr += "Request Data : " + request;
		ResponseModel responseModel = new ResponseModel();
		responseModel.setReference_id(UUID.randomUUID().toString());
		try{
			List<TeamConfig> listTeamName = teamConfigDAO.findByTeamName(request.path("body").path("userTeam").textValue());
			Iterator<TeamConfig> it = listTeamName.iterator();
			while (it.hasNext()){
				TeamConfig  teamConfig = it.next();
				teamConfig.setMaxPending(Long.parseLong(request.path("body").path("maxPending").textValue()));
				teamConfig.setMaxQuota(Long.parseLong(request.path("body").path("maxQuota").textValue()));
				teamConfig.setUserUpdate(request.path("body").path("userLogin").textValue());
				teamConfig.setUpdateDate(new Timestamp(new Date().getTime()));
				teamConfigDAO.save(teamConfig);
			}
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(200);
			responseModel.setMessage("Save success");
		} catch (Exception e) {
			log.error("Error: " + e);
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(500);
			responseModel.setMessage("Others error");
		}finally {
			log.info(logStr);
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> getInfoUserLogin(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		log.info("getInfoUserLogin: " + request);
		try {
			Assert.notNull(request.get("body"), "no body");
			UserDetail requestModel = mapper.treeToValue(request.get("body"), UserDetail.class);

			if (requestModel.getUserName() == null || requestModel.getUserName().isEmpty()) {
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(500);
				responseModel.setMessage("User name is mandatory!");
				return Map.of("status", 200, "data", responseModel);
			}

			List<UserDetail> userDetailList = userDetailsDAO.findAllByUserName(requestModel.getUserName());

			if (userDetailList.size() <= 0 || userDetailList == null) {
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(500);
				responseModel.setMessage("Empty");
				return Map.of("status", 200, "data", responseModel);
			}

			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(200);
			responseModel.setMessage("Get success");
			responseModel.setData(userDetailList);

		} catch (Exception e) {
			log.info("Error: " + e);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(500);
			responseModel.setMessage("Others error");
			log.info("{}", e);
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> removeUser(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		log.info("removeUser: " + request);
		try {
			Assert.notNull(request.get("body"), "no body");
			UserDetail requestModel = mapper.treeToValue(request.get("body"), UserDetail.class);

			if (requestModel.getUserId() == null) {
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(500);
				responseModel.setMessage("User ID is mandatory!");
				return Map.of("status", 200, "data", responseModel);
			}

			userDetailsDAO.deleteById(requestModel.getUserId());

			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(200);
			responseModel.setMessage("Delete success");

		} catch (Exception e) {
			log.info("Error: " + e);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(500);
			responseModel.setMessage("Others error");
			log.info("{}", e);
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> changeActiveUser(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		log.info("changeActiveUser: " + request);
		try {
			Assert.notNull(request.get("body"), "no body");
			UserDetail requestModel = mapper.treeToValue(request.get("body"), UserDetail.class);

			if (requestModel.getUserId() == null) {
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(500);
				responseModel.setMessage("User ID is mandatory!");
				return Map.of("status", 200, "data", responseModel);
			}

			userDetailsDAO.save(requestModel);

			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(200);
			responseModel.setMessage("Change success");

		} catch (Exception e) {
			log.info("Error: " + e);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(500);
			log.info("{}", e);
			responseModel.setMessage("Others error");
		}
		return Map.of("status", 200, "data", responseModel);
	}

	@Scheduled(fixedRateString = "${spring.syncpro.fixedRate}")
	public void callProcedureAssignApp() {
		LocalTime now = LocalTime.now();
		LocalTime fromTime = LocalTime.parse(fromTimePro);
		LocalTime toTime = LocalTime.parse(toTimePro);
		if (now.isBefore(toTime) && now.isAfter(fromTime)) {
			try {
				log.info("callProcedureAssignApp is running" + now);
				String query = String.format("CALL PR_ALLOCATION_ASSIGN_APP()");
				jdbcTemplate.execute(query);

			} catch (Exception e) {
				log.info("callProcedureAssignApp", e);
			}

		}
	}

	@Scheduled(fixedRateString = "${spring.syncrobot.fixedRate}")
	public void pushAssignToRobot() {
		LocalTime now = LocalTime.now();
		LocalTime fromTime = LocalTime.parse(fromTimeRobot);
		LocalTime toTime = LocalTime.parse(toTimeRobot);
		if (now.isBefore(toTime) && now.isAfter(fromTime)) {
			try {
				log.info("pushAsignToRobot is running");
				Pageable pageable = PageRequest.of(0, limit, Sort.by("id").ascending());
				Page<AssignmentDetail> assignmentDetailsList = assignmentDetailDAO.findByStatusAssign(KEY_ASSIGN_APP, pageable);

				if (assignmentDetailsList.getContent() == null || assignmentDetailsList.getContent().size() <= 0
						|| assignmentDetailsList.getContent().isEmpty()) {
					log.info("pushAsignToRobotApplication to assign is empty");
				} else {

					List<AutoAssignModel> autoAssignModelsList = new ArrayList<AutoAssignModel>();

					for (AssignmentDetail ad : assignmentDetailsList.getContent()) {
						AutoAssignModel autoAssignModel = new AutoAssignModel();
						autoAssignModel.setAppId(ad.getAppNumber());
						autoAssignModel.setUserName(ad.getAssignee());
						autoAssignModelsList.add(autoAssignModel);
						ad.setPickUptime(new Timestamp(new Date().getTime()));
						ad.setStatusAssign("ASSIGNING");
						assignmentDetailDAO.save(ad);
					}

					BodyAssignRobot bodyAssignRobot = new BodyAssignRobot();
					bodyAssignRobot.setProject("allocation");
					bodyAssignRobot.setReference_id(UUID.randomUUID().toString());
					bodyAssignRobot.setAutoAssign(autoAssignModelsList);

					RequestAssignRobot requestAssignRobot = new RequestAssignRobot();
					requestAssignRobot.setFunc("autoAssignUser");
					requestAssignRobot.setBody(bodyAssignRobot);

					new Thread(() -> {
						try {
							rabbitMQService.send("tpf-service-automation-allocation",
									requestAssignRobot);
							log.info("pushAssignToRobot push success" + requestAssignRobot);
						} catch (Exception e) {
							log.info("Error: pushAssignToRobot " + e);
						}
					}).start();
				}
			} catch (Exception e) {
				log.info("Error: pushAssignToRobot " + e);
				log.info("{}", e);
			}
		}
	}

	public Map<String, Object> updateStatusApp(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		log.info("updateStatusApp: " + request);
		try {
			Assert.notNull(request.get("body"), "no body");
			BodyAssignRobot requestModel = mapper.treeToValue(request.get("body").get("body"), BodyAssignRobot.class);

			if (requestModel.getAutoAssign() == null) {
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(500);
				responseModel.setMessage("User ID is mandatory!");
				return Map.of("status", 200, "data", responseModel);
			}
			List<AutoAssignModel> autoAssignModels = requestModel.getAutoAssign();
			for (AutoAssignModel ad : autoAssignModels) {
				AssignmentDetail assignmentDetail = assignmentDetailDAO.findAssignmentDetailByAppNumberAndAssigneeAndStatusAssign(
						ad.getAppId(), ad.getUserName(), "ASSIGNING");
				assignmentDetail.setBotName(ad.getUserAuto());
				assignmentDetail.setAssignedTime(new Timestamp(new Date().getTime()));
				if (ad.getAutomationResult().equals("AUTOASSIGN_FAILED")){
					assignmentDetail.setStatusAssign("FAILED");
					assignmentDetail.setErrorTime(new Timestamp(new Date().getTime()));
					assignmentDetail.setErrorMessage(ad.getAutomationResultMessage());
				} else {
					assignmentDetail.setStatusAssign("PROCESSING");
				}
				assignmentDetailDAO.save(assignmentDetail);
			}

			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(200);
			responseModel.setMessage("Update success");

		} catch (Exception e) {
			log.info("Error: " + e);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(500);
			log.info("{}", e);
			responseModel.setMessage("Others error");
		}
		return Map.of("status", 200, "data", responseModel);
	}

}