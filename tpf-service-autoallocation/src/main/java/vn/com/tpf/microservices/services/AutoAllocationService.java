package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import vn.com.tpf.microservices.dao.*;
import vn.com.tpf.microservices.models.*;

import javax.persistence.criteria.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.*;

@Service
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

	private static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	private static String SHEET = "UserTeam";
	private static String ROLE_LEADER = "role_leader";
	private static String ROLE_SUB = "role_supervisor";
	/**
	 * To check rule and return config rule to inhouse service
	 * @param request
	 * @return 0: Robot, 1: API
	 */
	public Map<String, Object> getAppAutoAllocation(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		try {
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

	public Map<String, Object> getUserToAssign(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		try {
		} catch (Exception e) {
			log.info("Error: " + e);
		}

		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> getAllUser(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
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
				listUserDetails = userDetailsDAO.findAllUserForSub(requestModel.getTeamName(), pageable);
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
			responseModel.setData(listUserDetails.getContent());

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
				UserDetail userDetail = userDetailsDAO.findByUserNameAndTeamLeader(requestModel.getUserName(), requestModel.getUserLogin());

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
					responseModel.setData(userDetail);
				}
			} else if (requestModel.getRoleUserLogin().equals(ROLE_SUB)) {
				if (requestModel.getUserName() == null || requestModel.getUserName().isEmpty()) {
					Sort typeSort = Sort.by(requestModel.getSortItem()).descending();
					if (requestModel.getTypeSort() == "ESC") {
						typeSort = Sort.by(requestModel.getSortItem()).ascending();
					}

					Pageable pageable = PageRequest.of(requestModel.getPage() , requestModel.getItemPerPage(), typeSort);

					Page<UserDetail> listUserDetails = userDetailsDAO.findAllByTeamLeader(requestModel.getUserLeader(), pageable);

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
					responseModel.setData(listUserDetails);
				} else {
					UserDetail userDetail = userDetailsDAO.findByUserNameAndTeamName(requestModel.getUserName(), requestModel.getTeamName());

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
						responseModel.setData(userDetail);
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

		try {
			Assert.notNull(request.get("body"), "no body");
			log.info("Request: " + request);
			boolean isFail = false;

			String appNumber = request.get("body").path("appNumber") != null ?
					request.get("body").path("appNumber").asText() : "";
			String assignee = request.get("body").path("assignee") != null ?
					request.get("body").path("assignee").asText() : "";
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
			if (request.get("body").path("teamName") != null) {
				Iterator<JsonNode> i = request.get("body").path("teamName").iterator();
				i.forEachRemaining(jsonNodeTeamNames::add);
				for(JsonNode j : jsonNodeTeamNames) {
					teamNames.add(j.asText());
				}
			}

			int pageSize = request.get("body").path("pageSize").asInt();
			int limit = request.get("body").path("limit").asInt();
			String sortType = request.get("body").path("sortType") != null ?
					request.get("body").path("sortType").asText(): "";
			String sortStyle = request.get("body").path("sortStyle") != null ?
					request.get("body").path("sortStyle").asText(): "";

			Pageable pagination = PageRequest.of(pageSize - 1, limit);
			if (!sortType.isEmpty()) {
				Sort sort = null;
				if (sortStyle.equals("ASC")) {
					sort = Sort.by(sortType).ascending();
				} else if (sortStyle.equals("DECS")){
					sort = Sort.by(sortType).descending();
				} else {
					isFail = true;
					log.error("Sort Style is incorrect");
				}
				pagination = PageRequest.of(pageSize - 1 , limit, sort);
			}
			if (!isFail) {
				Page<AllocationHistoryView> allocationHistoryViews	= allocationHistoryViewDao.findAll(new Specification() {
					@Override
					public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
						List<Predicate> predicates = new ArrayList<>();

						if (!assignee.isEmpty() && !role.isEmpty()) {
							if (role.equals(ROLE_LEADER)) {
								predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("teamLeader"), assignee)));
							} else if (role.equals(ROLE_SUB) && teamNames.size() > 0) {
								CriteriaBuilder.In<String> inClause = criteriaBuilder.in(root.get("teamName"));
								for (String t : teamNames) {
									inClause.value(t);
								}
								predicates.add(criteriaBuilder.and(inClause));
							} else {
								predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("assignee"), assignee)));
							}
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

				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(200);
				responseModel.setData(allocationHistoryViews.getContent());
			} else {
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(500);
				responseModel.setMessage("Others error");
			}


		} catch (Exception e) {
			log.error("Error: " + e);
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
			}

			userCheckingDAO.saveAll(requestModel.getListUser());

			String query = String.format("SELECT  FN_CHECKING_USER ('%s','%s') RESULT FROM DUAL",
					requestModel.getUserLogin(),
					requestModel.getTeamNameUser());

			String row_string = jdbcTemplate.queryForObject(query,new Object[]{},
					(rs, rowNum) ->
							rs.getString(("RESULT")
							));

			if (row_string.isEmpty()) {
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
		try {
			Assert.notNull(request.get("body"), "no body");
			UserChecking requestModel = mapper.treeToValue(request.get("body"), UserChecking.class);

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
				requestModel.setUserRole("role_user");
				requestModel.setCheckedFlag("OPEN");
				userCheckingDAO.save(requestModel);

				String query = String.format("SELECT  FN_CHECKING_USER ('%s','%s') RESULT FROM DUAL",
						requestModel.getUserLogin(),
						requestModel.getTeamName());

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

	/**
	 * To save log when inhouse call this service
	 * @param request
	 * @return
	 */
	public Map<String, Object> sendAppFromF1(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		try {
			Assert.notNull(request.get("body"), "no body");
			RequestModel requestModel = mapper.treeToValue(request.get("body"), RequestModel.class);

			if (requestModel == null) {
				responseModel.setRequest_id(request_id);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(500);
				responseModel.setMessage("Data is empty");
			} else {
				ETLDataPush etlDataPush = new ETLDataPush();
				Long id = etlDataPushDAO.getIdFromSequence();
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

				etlDataPushDAO.save(etlDataPush);

				responseModel.setRequest_id(request_id);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(200);
				responseModel.setMessage("Push success.");
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

	public Map<String, Object> getAssignConfig() {
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
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
			log.info("Error: " + e);
			responseModel.setRequest_id(request_id);
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


//			if (validationAssignConfig(assignConfigRequest, responseModel)) {
//				assignConfigDAO.save(assignConfigRequest);
//			} else {
//
//			}


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

//	private boolean validationAssignConfig(AssignConfig assignConfigRequest, ResponseModel responseModel) {
//		if (assignConfigRequest != null) {
//			if (assignConfigRequest.getStageName() == null) {
//				responseModel.setReference_id(UUID.randomUUID().toString());
//				responseModel.setDate_time(new Timestamp(new Date().getTime()));
//				responseModel.setResult_code(500);
//				responseModel.setMessage("Stage Name is null");
//				return false;
//			} else {
//				if (assignConfigRequest.getStageName().isEmpty()) {
//					responseModel.setReference_id(UUID.randomUUID().toString());
//					responseModel.setDate_time(new Timestamp(new Date().getTime()));
//					responseModel.setResult_code(500);
//					responseModel.setMessage("Stage Name is empty");
//					return false;
//				}
//			}
//		} else {
//			responseModel.setReference_id(UUID.randomUUID().toString());
//			responseModel.setDate_time(new Timestamp(new Date().getTime()));
//			responseModel.setResult_code(500);
//			responseModel.setMessage("Request error");
//			return false;
//		}
//		return true;
//	}
}