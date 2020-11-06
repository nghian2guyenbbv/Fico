package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import vn.com.tpf.microservices.dao.*;
import vn.com.tpf.microservices.models.*;

import javax.persistence.criteria.*;
import java.sql.Timestamp;
import java.time.LocalTime;
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
	AllocationPendingCodeDao allocationPendingCodeDao;

	@Autowired
	AssignConfigDAO assignConfigDAO;

	@Autowired
	TeamConfigDAO teamConfigDAO;

	@Autowired
	UserDetailsViewDAO userDetailsViewDAO;

	@Autowired
	UserDetailsDAO userDetailsDAO;

	@Autowired
	private RabbitMQService rabbitMQService;

	@Autowired
	AssignmentDetailDAO assignmentDetailDAO;

	@Autowired
	AllocationPendingDetailDao allocationPendingDetailDao;

	@Autowired
	AssignConfigViewDAO assignConfigViewDAO;

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

			Page<UserDetailView> listUserDetails;
			if ( requestModel.getRoleUserLogin().equals(ROLE_LEADER)) {
				listUserDetails = userDetailsViewDAO.findAllUserForLeader(requestModel.getUserLogin(), pageable);
			} else {
				if ( requestModel.getTeamName().size() > 0) {
					listUserDetails = userDetailsViewDAO.findAllUserForSub(requestModel.getTeamName(), pageable);
				} else {
					responseModel.setRequest_id(request_id);
					responseModel.setReference_id(UUID.randomUUID().toString());
					responseModel.setDate_time(new Timestamp(new Date().getTime()));
					responseModel.setResult_code(500);
					responseModel.setMessage("Team Name is empty. Please send item teamName in request.");
					return Map.of("status", 200, "data", responseModel);
				}
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
				UserDetailView userDetailView = userDetailsViewDAO.findByUserNameAndTeamLeader(requestModel.getUserName(),
						requestModel.getUserLogin());

				if (userDetailView == null) {
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
					List<UserDetailView> userDetailViewList = new ArrayList<>();
					userDetailViewList.add(userDetailView);
					result.put("data", userDetailViewList);
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

					Page<UserDetailView> listUserDetails = userDetailsViewDAO.findAllByTeamLeader(
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
					List<UserDetailView> userDetailView;
					if (requestModel.getTeamName().size() > 0) {
						userDetailView = userDetailsViewDAO.findByUserNameAndTeamName(requestModel.getUserName(),
								requestModel.getTeamName());
					} else {
						responseModel.setRequest_id(request_id);
						responseModel.setReference_id(UUID.randomUUID().toString());
						responseModel.setDate_time(new Timestamp(new Date().getTime()));
						responseModel.setResult_code(500);
						responseModel.setMessage("Team Name is empty. Please send item teamName in request.");
						return Map.of("status", 200, "data", responseModel);
					}

					if (userDetailView == null) {
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
						result.put("data", userDetailView);
						result.put("totalRecords", userDetailView.size());
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
							Predicate p1 =  criteriaBuilder.equal(root.get("teamLeader"), userName);
							Predicate p2 = 	criteriaBuilder.equal(root.get("assignee"), userName);
							predicates.add(criteriaBuilder.or(p1, p2));
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
			UserDetailView requestModel = mapper.treeToValue(request.get("body"), UserDetailView.class);

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
		Timestamp ts = Timestamp.valueOf(request.get("body").get("createdDate").asText());
		try {
			Assert.notNull(request.get("body"), "no body");
			RequestModel requestModel = mapper.treeToValue(request.get("body"), RequestModel.class);

			if (requestModel == null) {
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(500);
				responseModel.setMessage("Data is empty");
			} else {
				List<ETLDataPush> listEtlDataPush = etlDataPushDAO.findByAppNumberAndSuorceEtl(requestModel.getApplicationNo(), "ESB");
				ETLDataPush etlDataPush = null;
				if(listEtlDataPush.isEmpty()){
					etlDataPush = new ETLDataPush();
					saveEtlDataPush(etlDataPush, requestModel);
				}else{
					etlDataPush = listEtlDataPush.get(0);
					saveEtlDataPush(etlDataPush, requestModel);
				}

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
		}
		return Map.of("status", 200, "data", responseModel);
	}

	private void saveEtlDataPush(ETLDataPush etlDataPush, RequestModel requestModel){
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
		etlDataPush.setSuorceEtl("ESB");
		etlDataPushDAO.save(etlDataPush);
	}

	public Map<String, Object> getAssignConfig() {
		ResponseModel responseModel = new ResponseModel();
		try {
			Map<String, Object> result = new HashMap<>();
			Map<String, AssignConfigResponse> mapAssignConfig = new HashMap<>();
			List<AssignConfigView> lst = assignConfigViewDAO.findAll();
			List<String> listTeamName = teamConfigDAO.getListTeamName();

			for (AssignConfigView ac : lst) {
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
					assignConfigResponse.setSortIndex(ac.getSortIndex());
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
			log.error("getAssignConfig - Error: {}, class: {}, line: {}", e, e.getStackTrace()[0].getClassName(),
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
			String SQL = "SELECT * FROM {PAGE} WHERE CASE {USER_ROLE} WHEN 'role_user' THEN ASSIGNEE WHEN 'role_leader' THEN NVL(TEAM_LEADER,ASSIGNEE) END = {USER_LOGIN}";
			String SQLSUP = "SELECT A.* FROM {PAGE} A JOIN ALLOCATION_USER_DETAIL B ON A.TEAM_NAME = B.TEAM_NAME AND B.USER_ROLE = 'role_supervisor' AND B.USER_NAME = {USER_LOGIN}";

			List<Map<String, Object>> dashboards;
			if (!request.path("body").path("userRole").textValue().equals("role_supervisor")) {
				SQL = SQL.replace("{PAGE}", "V_ALLOCATION_DASHBOARD");
				SQL = SQL.replace("{USER_ROLE}", "'" + request.path("body").path("userRole").textValue() + "'");
				SQL = SQL.replace("{USER_LOGIN}", "'" + request.path("body").path("userLogin").textValue() + "'");
				dashboards = jdbcTemplate.queryForList(SQL);
			} else {
				SQLSUP = SQLSUP.replace("{PAGE}", "V_ALLOCATION_DASHBOARD");
				SQLSUP = SQLSUP.replace("{USER_LOGIN}", "'" + request.path("body").path("userLogin").textValue() + "'");
				dashboards = jdbcTemplate.queryForList(SQLSUP);
			}
			ArrayNode dataArrayNode = mapper.createArrayNode();
			if (dashboards != null && !dashboards.isEmpty()) {
				for (Map<String, Object> dashboard : dashboards) {
					ObjectNode data = mapper.createObjectNode();
					for (Iterator<Map.Entry<String, Object>> it = dashboard.entrySet().iterator(); it.hasNext(); ) {
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
			List<ViewTeamConfig> listViewTeamConfig =  jdbcTemplate.query(SQL, new Object[]{},  (rs, rowNum) ->{
				ViewTeamConfig viewTeamConfig = new ViewTeamConfig();
				viewTeamConfig.setUserName(rs.getString("USER_NAME"));
				viewTeamConfig.setUserRole(rs.getString("USER_ROLE"));
				viewTeamConfig.setTeamName(rs.getString("TEAM_NAME"));
				viewTeamConfig.setMaxPending(rs.getInt("MAX_PENDING"));
				viewTeamConfig.setMaxQuota(rs.getInt("MAX_QUOTA"));
				return viewTeamConfig;
			});
			ArrayNode dataArrayNode = mapper.createArrayNode();
			if(!listViewTeamConfig.isEmpty()){
				Iterator<ViewTeamConfig> it = listViewTeamConfig.iterator();
				while (it.hasNext()){
					ViewTeamConfig viewTeamConfig = it.next();
					ObjectNode data = mapper.createObjectNode();
					data.put("userName", viewTeamConfig.getUserName());
					data.put("userRole", viewTeamConfig.getUserRole());
					data.put("teamName", viewTeamConfig.getTeamName());
					data.put("maxPending", viewTeamConfig.getMaxPending());
					data.put("maxQuota", viewTeamConfig.getMaxQuota());
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
				teamConfig.setMaxPending(request.path("body").path("maxPending").intValue());
				teamConfig.setMaxQuota(request.path("body").path("maxQuota").intValue());
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
			UserDetailView requestModel = mapper.treeToValue(request.get("body"), UserDetailView.class);

			if (requestModel.getUserName() == null || requestModel.getUserName().isEmpty()) {
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(500);
				responseModel.setMessage("User name is mandatory!");
				return Map.of("status", 200, "data", responseModel);
			}

			List<UserDetailView> userDetailViewList = userDetailsViewDAO.findAllByUserName(requestModel.getUserName());

			if (userDetailViewList.size() <= 0 || userDetailViewList == null) {
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
			responseModel.setData(userDetailViewList);

		} catch (Exception e) {
			log.error("Error: " + e);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(500);
			responseModel.setMessage("Others error");
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> removeUser(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		log.info("removeUser: " + request);
		try {
			Assert.notNull(request.get("body"), "no body");
			UserDetailView requestModel = mapper.treeToValue(request.get("body"), UserDetailView.class);

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
			log.error("Error: " + e);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(500);
			responseModel.setMessage("Others error");
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
			responseModel.setMessage("Others error");
		}
		return Map.of("status", 200, "data", responseModel);
	}

	@Async
	@Scheduled(fixedRateString = "${spring.syncfunc.fixedRate}")
	public void callFunctionAssignApp() {
		log.info("callFunctionAssignApp is start.");
		String sql = "select * from VIEW_ALLOCATION_FUNCTION";
		List<ConfigFunction> listConfigFunction = jdbcTemplate.query(sql, new Object[]{},  (rs, rowNum) ->{
			ConfigFunction configFunction = new ConfigFunction();
			configFunction.setConfig(rs.getString("CONFIG"));
			configFunction.setFromTime(rs.getString("FROM_TIME"));
			configFunction.setToTime(rs.getString("TO_TIME"));
			configFunction.setLimit(rs.getInt("LIMIT"));
			return configFunction;
		});
		ConfigFunction configFunction = listConfigFunction.get(0);
		if (configFunction == null) {
			log.error("No config");
		} else {
			LocalTime now = LocalTime.now();
			LocalTime fromTime = LocalTime.parse(configFunction.getFromTime());
			LocalTime toTime = LocalTime.parse(configFunction.getToTime());
			if (now.isBefore(toTime) && now.isAfter(fromTime) && configFunction.getConfig().equals("TRUE")) {
				try {
					log.info("callFunctionAssignApp is running " + now);
					String query = String.format("SELECT FN_ALLOCATION_ASSIGN_APP() FROM DUAL");
					String result = jdbcTemplate.queryForObject(
							query, String.class);

					log.info("callFunctionAssignApp - time: {} - result: {} ", now, result);
				} catch (Exception e) {
					log.info("callFunctionAssignApp: - Error: {}, class: {}, line: {}", e, e.getStackTrace()[0].getClassName(),
							e.getStackTrace()[0].getLineNumber());
				}
			}
		}
	}

	@Scheduled(fixedRateString = "${spring.syncrobot.fixedRate}")
	public void pushAssignToRobot() {
		log.info("pushAssignToRobot - get config from DB");
		String sql = "select * from VIEW_ALLOCATION_ROBOT";
		List<ConfigRobot> listConfigRobot = jdbcTemplate.query(sql, new Object[]{},  (rs, rowNum) ->{
			ConfigRobot configRobot = new ConfigRobot();
			configRobot.setConfig(rs.getString("CONFIG"));
			configRobot.setFromTime(rs.getString("FROM_TIME"));
			configRobot.setToTime(rs.getString("TO_TIME"));
			configRobot.setLimit(rs.getInt("LIMIT"));
			return configRobot;
		});
		ConfigRobot configRobot = listConfigRobot.get(0);
		if (configRobot == null) {
			log.error("No Config");
		} else {
			LocalTime now = LocalTime.now();
			LocalTime fromTime = LocalTime.parse(configRobot.getFromTime());
			LocalTime toTime = LocalTime.parse(configRobot.getToTime());
			if (now.isBefore(toTime) && now.isAfter(fromTime) && configRobot.getConfig().equals("TRUE")) {
				log.info("pushAssignToRobot is start.");
				try {
					Pageable pageable = PageRequest.of(0, configRobot.getLimit(), Sort.by("id").ascending());
					Page<AssignmentDetail> assignmentDetailsList = assignmentDetailDAO.findByStatusAssign(KEY_ASSIGN_APP, pageable);

					if (assignmentDetailsList.getContent() == null || assignmentDetailsList.getContent().size() <= 0
							|| assignmentDetailsList.getContent().isEmpty()) {
						log.info("pushAssignToRobotApplication to assign is empty");
					} else {
						log.info("pushAssignToRobot is running");
						BodyAssignRobot bodyAssignRobot = new BodyAssignRobot();
						bodyAssignRobot.setProject("allocation");
						bodyAssignRobot.setReference_id(UUID.randomUUID().toString());

						RequestAssignRobot requestAssignRobot = new RequestAssignRobot();
						requestAssignRobot.setFunc("autoAssignUser");
						requestAssignRobot.setBody(bodyAssignRobot);
						List<AutoAssignModel> autoAssignModelsList = new ArrayList<AutoAssignModel>();
						for (AssignmentDetail ad : assignmentDetailsList.getContent()) {
							AutoAssignModel autoAssignModel = new AutoAssignModel();
							autoAssignModel.setAppId(ad.getAppNumber());
							autoAssignModel.setUserName(ad.getAssignee());
							autoAssignModelsList.add(autoAssignModel);
							ad.setPickUptime(new Timestamp(new Date().getTime()));
							ad.setStatusAssign("ASSIGNING");
							assignmentDetailDAO.save(ad);
							log.info("pushAssignToRobot is save to DB: appNum - {}", ad.getAppNumber());
							bodyAssignRobot.setAutoAssign(autoAssignModelsList);
						}
						rabbitMQService.send("tpf-service-automation-allocation",
								requestAssignRobot);
						log.info("pushAssignToRobot push success " + requestAssignRobot);
					}
				} catch (Exception e) {
					log.info("Error: pushAssignToRobot " + e);
				}
			}
		}
	}

	public Map<String, Object> updateStatusApp(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		log.info("updateStatusApp: " + request);
		try {
			Assert.notNull(request.get("body"), "no body");
			BodyAssignRobot requestModel = mapper.readValue(request.get("body").toString(), new TypeReference<BodyAssignRobot>(){});

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
				UserDetail userDetail = userDetailsDAO.findByUserName(ad.getUserName());
				if (assignmentDetail == null) {
					responseModel.setReference_id(UUID.randomUUID().toString());
					responseModel.setDate_time(new Timestamp(new Date().getTime()));
					responseModel.setResult_code(200);
					responseModel.setMessage(ad.getAppId() + "is empty.");
				} else {
					assignmentDetail.setBotName(ad.getUserAuto());
					assignmentDetail.setAssignedTime(new Timestamp(new Date().getTime()));
					if (ad.getAutomationResult().equals("AUTOASSIGN_FAILED")) {
						assignmentDetail.setStatusAssign("FAILED");
						assignmentDetail.setErrorTime(new Timestamp(new Date().getTime()));
						assignmentDetail.setErrorMessage(ad.getAutomationResultMessage());
						log.info("updateStatusApp - appNumber: {} - QuotaAppNumBefore: {} - Assignee: {}", assignmentDetail.getAppNumber(),
								userDetail.getQuotaApp(), assignmentDetail.getAssignee());
						int quotaApp = userDetail.getQuotaApp()-1;
						log.info("updateStatusApp - appNumber: {} - QuotaAppNumAfterSub: {} - Assignee: {}", assignmentDetail.getAppNumber(),
								quotaApp, assignmentDetail.getAssignee());
						userDetail.setQuotaApp(quotaApp);
						userDetailsDAO.save(userDetail);
					} else {
						assignmentDetail.setStatusAssign("PROCESSING");
						log.info("Update Status Assign Success" + assignmentDetail.getAppNumber());
					}
					assignmentDetailDAO.save(assignmentDetail);
				}
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
			responseModel.setMessage("Others error");
		}
		return Map.of("status", 200, "data", responseModel);
	}

//	public Map<String, Object> getConfigRobotProcedure() {
//		ResponseModel responseModel = new ResponseModel();
//		log.info("getConfigRobotProcedure is running");
//		try {
//			List<ConfigRobotProcedure> configRobotProcedure = configRobotProcedureDAO.findAll();
//
//			if (configRobotProcedure.size() <=0 || configRobotProcedure.isEmpty()) {
//				responseModel.setReference_id(UUID.randomUUID().toString());
//				responseModel.setDate_time(new Timestamp(new Date().getTime()));
//				responseModel.setResult_code(500);
//				responseModel.setMessage("Config is empty");
//				return Map.of("status", 200, "data", responseModel);
//			}
//
//			responseModel.setReference_id(UUID.randomUUID().toString());
//			responseModel.setDate_time(new Timestamp(new Date().getTime()));
//			responseModel.setResult_code(200);
//			responseModel.setMessage("Get success");
//			responseModel.setData(configRobotProcedure);
//
//		} catch (Exception e) {
//			log.info("Error: " + e);
//			responseModel.setReference_id(UUID.randomUUID().toString());
//			responseModel.setDate_time(new Timestamp(new Date().getTime()));
//			responseModel.setResult_code(500);
//			log.info("{}", e);
//			responseModel.setMessage("Others error");
//		}
//		return Map.of("status", 200, "data", responseModel);
//	}
//
//	public Map<String, Object> updateConfigRobotProcedure(JsonNode request) {
//		ResponseModel responseModel = new ResponseModel();
//		log.info("updateConfigRobotProcedure" + request);
//		try {
//			Assert.notNull(request.get("body"), "no body");
//			ConfigRobotProcedure requestModel = mapper.treeToValue(request.get("body"), ConfigRobotProcedure.class);
//
//			if (requestModel == null) {
//				responseModel.setReference_id(UUID.randomUUID().toString());
//				responseModel.setDate_time(new Timestamp(new Date().getTime()));
//				responseModel.setResult_code(500);
//				responseModel.setMessage("Config is empty");
//				return Map.of("status", 200, "data", responseModel);
//			}
//
//			configRobotProcedureDAO.save(requestModel);
//
//			responseModel.setReference_id(UUID.randomUUID().toString());
//			responseModel.setDate_time(new Timestamp(new Date().getTime()));
//			responseModel.setResult_code(200);
//			responseModel.setMessage("Update success");
//
//		} catch (Exception e) {
//			log.info("Error: " + e);
//			responseModel.setReference_id(UUID.randomUUID().toString());
//			responseModel.setDate_time(new Timestamp(new Date().getTime()));
//			responseModel.setResult_code(500);
//			log.info("{}", e);
//			responseModel.setMessage("Others error");
//		}
//		return Map.of("status", 200, "data", responseModel);
//	}

	public Map<String, Object> getAllPendingCode(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		log.info("getAllPendingCode" + request);
		responseModel.setReference_id(UUID.randomUUID().toString());
		responseModel.setDate_time(new Timestamp(new Date().getTime()));
		try {
			List<AllocationPendingCode> allocationPendingCodes = allocationPendingCodeDao.findAll();
			ArrayNode dataArrayNode = mapper.createArrayNode();
			for(int i = 0; i< allocationPendingCodes.size();i++){
				ObjectNode data = mapper.createObjectNode();
				data.put("codePending", allocationPendingCodes.get(i).getCodePending());
				data.put("codeDesc", allocationPendingCodes.get(i).getCodeDesc());
				data.put("stageName", allocationPendingCodes.get(i).getStageName());
				dataArrayNode.add(data);
			}
			responseModel.setData(dataArrayNode);

		} catch (Exception e) {
			log.error("Error: " + e);
			responseModel.setResult_code(500);
			responseModel.setMessage("Others error");
		}
		return Map.of("status", 200, "data", responseModel);
	}
	public Map<String, Object> updatePendingDetail(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		log.info("updatePendingDetail" + request);
		responseModel.setReference_id(UUID.randomUUID().toString());
		responseModel.setDate_time(new Timestamp(new Date().getTime()));
		try {
			String appNumber = request.path("body").path("appNumber").textValue();
			String stageName = request.path("body").path("stageName").textValue();
			Timestamp creation_appStage_time = Timestamp.valueOf(request.path("body").path("creation_appStage_time").textValue());
			List<AssignmentDetail> listAssignmentDetail = assignmentDetailDAO.findByAppNumberAndStageNameAndAndCreationApplStageTime(appNumber, stageName, creation_appStage_time);
			Iterator<AssignmentDetail> it = listAssignmentDetail.iterator();
			if(listAssignmentDetail.size() == 0){
				responseModel.setResult_code(202);
				responseModel.setMessage("Application can't be hold. Please reload page.");
			}else{
				UserDetail userDetail = userDetailsDAO.findByUserName(request.path("body").path("pendingUser").textValue());
				if(userDetail.getPendingApp() < request.path("body").path("maxPending").intValue()){
					updateInfor(it, request, userDetail, responseModel);
				}else{
					responseModel.setResult_code(203);
					responseModel.setMessage("Number of pending app is limited. Please contact your supervisor for support.");
				}
			}
		} catch (Exception e) {
			log.error("Error: " + e);
			responseModel.setResult_code(500);
			responseModel.setMessage("Others error");
		}
		return Map.of("status", 200, "data", responseModel);
	}

	private void updateInfor(Iterator<AssignmentDetail> it, JsonNode request, UserDetail userDetail, ResponseModel responseModel){
		while (it.hasNext()){
			try{
				AssignmentDetail assignmentDetail = it.next();
				AllocationPendingDetail allocationPendingDetail = new AllocationPendingDetail();
				allocationPendingDetail.setAppNumber(assignmentDetail.getAppNumber());
				allocationPendingDetail.setStageName(request.path("body").path("stageName").textValue());
				allocationPendingDetail.setCreationApplstageTime(assignmentDetail.getCreationApplStageTime());
				allocationPendingDetail.setPendingCode(request.path("body").path("pendingCode").textValue());
				allocationPendingDetail.setPendingComments(request.path("body").path("pendingComments").textValue());
				allocationPendingDetail.setPendingUser(request.path("body").path("pendingUser").textValue());
				allocationPendingDetail.setPendingDate(new Timestamp(new Date().getTime()));
				allocationPendingDetail.setTeamUser(request.path("body").path("teamUser").textValue());
				allocationPendingDetail.setCurrentCycle(assignmentDetail.getCurrentCycle());
				allocationPendingDetailDao.save(allocationPendingDetail);

				assignmentDetail.setStatusAssign("PENDING");
				assignmentDetailDAO.save(assignmentDetail);
				log.info("updatePendingDetail - appNumber: {} - PendingAppNumBefore: {} - pendingUser: {}",
						assignmentDetail.getAppNumber(), userDetail.getPendingApp(), allocationPendingDetail.getPendingUser());
				int pendingApp = userDetail.getPendingApp()+1;
				log.info("updatePendingDetail - appNumber: {} - PendingAppNumAfterAdd: {} - pendingUser: {}", assignmentDetail.getAppNumber(),
						pendingApp, allocationPendingDetail.getPendingUser());
				userDetail.setPendingApp(pendingApp);
				log.info("updatePendingDetail - appNumber: {} - QuotaAppNumBefore: {} - pendingUser: {}", assignmentDetail.getAppNumber(),
						userDetail.getQuotaApp(), allocationPendingDetail.getPendingUser());
				int quotaApp = userDetail.getQuotaApp()-1;
				log.info("updatePendingDetail - appNumber: {} - QuotaAppNumAfterSub: {} - pendingUser: {}", assignmentDetail.getAppNumber(),
						quotaApp, allocationPendingDetail.getPendingUser());
				userDetail.setQuotaApp(quotaApp);
				userDetailsDAO.save(userDetail);
				responseModel.setResult_code(200);
				responseModel.setMessage("SUCCESS");
			}catch (Exception e) {
				log.error("Error: " + e);
				responseModel.setResult_code(201);
				responseModel.setMessage("Insert into Assignment Detail not success");
			}
		}
	}

	public Map<String, Object> updateVendor(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		log.info("updateVendor" + request);
		try{
			List<ETLDataPush> listETLData = etlDataPushDAO.findByAppNumberAndSuorceEtl(request.path("body").path("appNumber").textValue(), "ESB");
			ETLDataPush etlDataPush = null;
			if(listETLData.isEmpty()){
				etlDataPush = new ETLDataPush();
				etlDataPush.setCreatedTimeVendor(new Timestamp(new Date().getTime()));
				etlDataPush.setVendorName(request.path("body").path("vendorId").textValue());
			}else{
				etlDataPush = listETLData.get(0);
				etlDataPush.setCreatedTimeVendor(new Timestamp(new Date().getTime()));
				etlDataPush.setVendorName(request.path("body").path("vendorId").textValue());
			}
			etlDataPushDAO.save(etlDataPush);
			responseModel.setResult_code(200);
			responseModel.setMessage("SUCCESS");
		} catch (Exception e) {
			log.error("Error: " + e);
			responseModel.setResult_code(500);
			responseModel.setMessage("Others error");
		}

		return Map.of("status", 200, "data", responseModel);
	}

	public Object updateRaiseQuery(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		log.info("updateRaiseQuery" + request);
		try{
			AssignmentDetail assignmentDetail = assignmentDetailDAO.findByAppNumberAndStageName(request.path("body").path("applicationNo").textValue(), request.path("body").path("stage").textValue());
			if(assignmentDetail == null){
				log.info("updateRaiseQuery: Can not find " + request.path("body").path("applicationNo").textValue() + "in Assignment Detail");
				responseModel.setResult_code(204);
				responseModel.setMessage("Can not found appNo");
			}else{
				AllocationPendingDetail allocationPendingDetail = new AllocationPendingDetail();
				allocationPendingDetail.setAppNumber(assignmentDetail.getAppNumber());
				allocationPendingDetail.setCreationApplstageTime(assignmentDetail.getCreationApplStageTime());
				allocationPendingDetail.setStageName(request.path("body").path("stage").textValue());
				allocationPendingDetail.setPendingCode("PENRS");
				allocationPendingDetail.setPendingComments(request.path("body").path("comment").textValue());
				allocationPendingDetail.setPendingUser(assignmentDetail.getAssignee());
				allocationPendingDetail.setPendingDate(new Timestamp(new Date().getTime()));
				allocationPendingDetail.setTeamUser(assignmentDetail.getTeamAssignee());
				allocationPendingDetail.setCurrentCycle(assignmentDetail.getCurrentCycle());
				UserDetail userDetail = userDetailsDAO.findByUserName(assignmentDetail.getAssignee());
				if(userDetail == null){
					responseModel.setResult_code(205);
					responseModel.setMessage("Can not found user");
					log.info("updateRaiseQuery: Can not find " + assignmentDetail.getAssignee() + "in User Detail. For : " + request.path("body").path("applicationNo").textValue() + "," + request.path("body").path("stage").textValue());
				}else{
					allocationPendingDetailDao.save(allocationPendingDetail);
					assignmentDetail.setStatusAssign("PENDING");
					assignmentDetailDAO.save(assignmentDetail);
					log.info("updatePendingDetail - appNumber: {} - PendingAppNumBefore: {} - pendingUser: {}",
							assignmentDetail.getAppNumber(), userDetail.getPendingApp(), allocationPendingDetail.getPendingUser());
					log.info("updatePendingDetail - appNumber: {} - QuotaAppNumBefore: {} - pendingUser: {}", assignmentDetail.getAppNumber(),
							userDetail.getQuotaApp(), allocationPendingDetail.getPendingUser());
					int pendingApp = userDetail.getPendingApp()+1;
					int quotaApp = userDetail.getQuotaApp()-1;
					userDetail.setPendingApp(pendingApp);
					userDetail.setQuotaApp(quotaApp);
					log.info("updatePendingDetail - appNumber: {} - PendingAppNumAfterAdd: {} - pendingUser: {}", assignmentDetail.getAppNumber(),
							pendingApp, allocationPendingDetail.getPendingUser());
					log.info("updatePendingDetail - appNumber: {} - QuotaAppNumAfterSub: {} - pendingUser: {}", assignmentDetail.getAppNumber(),
							quotaApp, allocationPendingDetail.getPendingUser());
					userDetailsDAO.save(userDetail);
					responseModel.setResult_code(200);
					responseModel.setMessage("Success");
				}
			}

		}catch (Exception e) {
			log.error("Error: " + e);
			responseModel.setResult_code(500);
			responseModel.setMessage("Others error");
		}
		return Map.of("status", 200, "data", responseModel);
	}
}