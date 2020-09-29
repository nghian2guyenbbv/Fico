package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.catalina.User;
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
import java.sql.Types;
import java.text.SimpleDateFormat;
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
				us.setUserRole("role_user");
				us.setCheckedFlag("OPEN");

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
		String request_id = null;
		String ts = String.valueOf(request.get("body").get("createdDate").asText().replace(" ","T"));
		JsonNode jsonNode = request.get("body");
		((ObjectNode) jsonNode).put("createdDate", ts );
		try {
			Assert.notNull(request.get("body"), "no body");
			RequestModel requestModel = mapper.treeToValue(jsonNode, RequestModel.class);

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

	public Map<String, Object> getInfoUserLogin(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		try {
			Assert.notNull(request.get("body"), "no body");
			UserDetail requestModel = mapper.treeToValue(request.get("body"), UserDetail.class);

			if (requestModel.getUserName() == null || requestModel.getUserName().isEmpty()) {
				responseModel.setRequest_id(request_id);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(500);
				responseModel.setMessage("User name is mandatory!");
				return Map.of("status", 200, "data", responseModel);
			}

			List<UserDetail> userDetailList = userDetailsDAO.findAllByUserName(requestModel.getUserName());

			if (userDetailList.size() <= 0 || userDetailList == null) {
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
			responseModel.setData(userDetailList);

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

	public Map<String, Object> removeUser(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		try {
			Assert.notNull(request.get("body"), "no body");
			UserDetail requestModel = mapper.treeToValue(request.get("body"), UserDetail.class);

			if (requestModel.getUserId() == null) {
				responseModel.setRequest_id(request_id);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(500);
				responseModel.setMessage("User ID is mandatory!");
				return Map.of("status", 200, "data", responseModel);
			}

			userDetailsDAO.deleteById(requestModel.getUserId());

			responseModel.setRequest_id(request_id);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(200);
			responseModel.setMessage("Delete success");

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

	public Map<String, Object> changeActiveUser(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		try {
			Assert.notNull(request.get("body"), "no body");
			UserDetail requestModel = mapper.treeToValue(request.get("body"), UserDetail.class);

			if (requestModel.getUserId() == null) {
				responseModel.setRequest_id(request_id);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(500);
				responseModel.setMessage("User ID is mandatory!");
				return Map.of("status", 200, "data", responseModel);
			}

			userDetailsDAO.save(requestModel);

			responseModel.setRequest_id(request_id);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(200);
			responseModel.setMessage("Change success");

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

	@Scheduled
	public Map<String, Object> pushAsignToRobot(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		try {
			Assert.notNull(request.get("body"), "no body");
			UserDetail requestModel = mapper.treeToValue(request.get("body"), UserDetail.class);

			if (requestModel.getUserId() == null) {
				responseModel.setRequest_id(request_id);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(500);
				responseModel.setMessage("User ID is mandatory!");
				return Map.of("status", 200, "data", responseModel);
			}

			userDetailsDAO.save(requestModel);

			responseModel.setRequest_id(request_id);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(200);
			responseModel.setMessage("Change success");

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

}