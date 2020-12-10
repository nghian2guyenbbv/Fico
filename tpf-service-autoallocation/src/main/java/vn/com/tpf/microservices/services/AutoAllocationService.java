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
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import vn.com.tpf.microservices.commons.CheckResultService;
import vn.com.tpf.microservices.commons.ResultData;
import vn.com.tpf.microservices.dao.*;
import vn.com.tpf.microservices.models.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
    AssignmentDetailDAO assignmentDetailDAO;

    @Autowired
    AllocationPendingDetailDao allocationPendingDetailDao;

    @Autowired
    AllocationReassignedDetailDao reassignedDetailDao;

    @Autowired
    AssignConfigViewDAO assignConfigViewDAO;

    @Autowired
    AllocationLogQuotaDao allocationLogQuotaDao;

    @Autowired
    CheckResultService checkResultService;

    private static String ROLE_LEADER = "role_leader";
    private static String ROLE_SUB = "role_supervisor";
    private static String ROLE_USER = "role_user";


    /**
     * Get user team
     */
    public Map<String, Object> getAllUser(JsonNode request) {
        ResponseModel responseModel = new ResponseModel();
        log.info("getAllUser - request: {}", request);
        responseModel.setReference_id(UUID.randomUUID().toString());
        responseModel.setDate_time(new Timestamp(new Date().getTime()));
        try {
            Assert.notNull(request.get("body"), "no body");
            RequestGetUserDetail requestModel = mapper.treeToValue(request.get("body"), RequestGetUserDetail.class);
            if (requestModel != null) {
                Sort typeSort = Sort.by(requestModel.getSortItem()).descending();
                if (requestModel.getTypeSort().equals("ASC")) {
                    typeSort = Sort.by(requestModel.getSortItem()).ascending();
                }

                Pageable pageable = PageRequest.of(requestModel.getPage(), requestModel.getItemPerPage(), typeSort);

                Page<UserDetailView> listUserDetails;
                String roleUser = checkRoleUser(requestModel.getUserLogin());
                log.info("getAllUser - roleUser: {} , userLogin: {}", roleUser, requestModel.getUserLogin());

                if (roleUser.equals(ROLE_LEADER)) {
                    listUserDetails = userDetailsViewDAO.findAllUserForLeader(requestModel.getUserLogin(), pageable);
                } else if (roleUser.equals(ROLE_SUB)) {
                    if (requestModel.getTeamName().size() > 0) {
                        listUserDetails = userDetailsViewDAO.findAllUserForSub(requestModel.getTeamName(), pageable);
                    } else {
                        responseModel = checkResultService.checkResult(ResultData.DATA_EMPTY, responseModel);
                        return Map.of("status", 200, "data", responseModel);
                    }
                } else {
                    responseModel = checkResultService.checkResult(ResultData.PERMISSION_FAILED, responseModel);
                    return Map.of("status", 200, "data", responseModel);
                }

                log.info("getAllUser - UserDetailList: {}", listUserDetails.getContent());

                if (listUserDetails.getContent().size() <= 0 || listUserDetails.getContent() == null) {
                    responseModel = checkResultService.checkResult(ResultData.DATA_EMPTY, responseModel);
                    return Map.of("status", 200, "data", responseModel);
                }

                responseModel = checkResultService.checkResult(ResultData.SUCCESS, responseModel);
                Map<String, Object> result = new HashMap<>();
                result.put("data", listUserDetails.getContent());
                result.put("totalRecords", listUserDetails.getTotalElements());
                responseModel.setData(result);
            }

        } catch (Exception e) {
            log.error("getAllUser - Error: {}, class: {}, line: {}", e, e.getStackTrace()[0].getClassName(),
                    e.getStackTrace()[0].getLineNumber());
            responseModel = checkResultService.checkResult(ResultData.OTHER_ERROR, responseModel);
        }
        return Map.of("status", 200, "data", responseModel);
    }

    /**
     * Get History data
     */
    public Map<String, Object> getHistoryApp(JsonNode request) {
        ResponseModel responseModel = new ResponseModel();
        log.info("getHistoryApp - Request: {}" + request);
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
                    request.get("body").path("statusAssign").asText() : "";
            String from = request.get("body").path("from") != null ?
                    request.get("body").path("from").asText() : "";
            String to = request.get("body").path("to") != null ?
                    request.get("body").path("to").asText() : "";
            String role = request.get("body").path("role") != null ?
                    request.get("body").path("role").asText() : "";
            List<String> teamNames = new ArrayList<>();
            List<JsonNode> jsonNodeTeamNames = new ArrayList<>();
            Iterator<JsonNode> i = request.get("body").path("teamName").iterator();
            i.forEachRemaining(jsonNodeTeamNames::add);
            for (JsonNode j : jsonNodeTeamNames) {
                teamNames.add(j.asText());
            }
            int pageSize = request.get("body").path("pageSize").asInt();
            int limit = request.get("body").path("limit").asInt();
            String sortType = request.get("body").path("sortType") != null ?
                    request.get("body").path("sortType").asText() : "";
            String sortStyle = request.get("body").path("sortStyle") != null ?
                    request.get("body").path("sortStyle").asText() : "";

            Pageable pagination = PageRequest.of(pageSize - 1, limit);

            if (!sortType.isEmpty()) {
                Sort sort;
                if (sortStyle.equals("ASC")) {
                    sort = Sort.by(sortType).ascending();
                } else {
                    sort = Sort.by(sortType).descending();
                }
                pagination = PageRequest.of(pageSize - 1, limit, sort);
            }


            Page<AllocationHistoryView> allocationHistoryViews = allocationHistoryViewDao.findAll(new Specification() {
                @Override
                public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicates = new ArrayList<>();

                    if (!userName.isEmpty()) {
                        if (role.equals("role_leader")) {
                            Predicate p1 = criteriaBuilder.equal(root.get("teamLeader"), userName);
                            Predicate p2 = criteriaBuilder.equal(root.get("assignee"), userName);
                            predicates.add(criteriaBuilder.or(p1, p2));
                        } else if (role.equals("role_supervisor") && teamNames.size() > 0) {
                            CriteriaBuilder.In<String> inClause = criteriaBuilder.in(root.get("teamName"));
                            for (String t : teamNames) {
                                inClause.value(t);
                            }
                            predicates.add(criteriaBuilder.and(inClause));
                        } else {
                            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("assignee"), userName)));
                        }
                    }

                    if (!assignee.isEmpty()) {
                        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("assignee"), assignee)));
                    }

                    if (!appNumber.isEmpty()) {
                        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("appNumber"), appNumber)));
                    }
                    if (!statusAssign.isEmpty()) {
                        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("statusAssign"), statusAssign)));
                    }
                    if (!from.isEmpty()) {
                        Timestamp fromTimestamp = Timestamp.valueOf(from);
                        predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThan(root.get("assignedTime"), fromTimestamp)));
                    }
                    if (!to.isEmpty()) {
                        Timestamp toTimestamp = Timestamp.valueOf(to);
                        predicates.add(criteriaBuilder.and(criteriaBuilder.lessThan(root.get("assignedTime"), toTimestamp)));
                    }

                    return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
                }
            }, pagination);
            result.put("data", allocationHistoryViews.getContent());
            result.put("totalRecords", allocationHistoryViews.getTotalElements());
            responseModel.setData(result);
            responseModel = checkResultService.checkResult(ResultData.SUCCESS, responseModel);
        } catch (Exception e) {
            log.info("getHistoryApp - Error: {}, class: {}, line: {}", e, e.getStackTrace()[0].getClassName(),
                    e.getStackTrace()[0].getLineNumber());
            responseModel.setReference_id(UUID.randomUUID().toString());
            responseModel = checkResultService.checkResult(ResultData.OTHER_ERROR, responseModel);
        }

        return Map.of("status", 200, "data", responseModel);
    }

    /**
     * Upload user from excel file
     */
    public Map<String, Object> uploadUser(JsonNode request) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setReference_id(UUID.randomUUID().toString());
        responseModel.setDate_time(new Timestamp(new Date().getTime()));
        log.info("uploadUser - Request: {}" + request);
        try {
            Assert.notNull(request.get("body"), "no body");
            RequestImportFile requestModel = mapper.treeToValue(request.get("body"), RequestImportFile.class);

            if (requestModel == null) {
                responseModel = checkResultService.checkResult(ResultData.IS_REQUITED, responseModel);
            } else {
                String userLogin = requestModel.getUserLogin();
                if (checkRoleUser(userLogin).equals(ROLE_LEADER)) {
                    if (requestModel.getListUser() == null || requestModel.getListUser().size() <= 0
                            || requestModel.getListUser().isEmpty()) {
                        responseModel = checkResultService.checkResult(ResultData.DATA_EMPTY, responseModel);
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

                    String row_string = jdbcTemplate.queryForObject(query, new Object[]{},
                            (rs, rowNum) ->
                                    rs.getString(("RESULT")
                                    ));

                    if (row_string == null || row_string.isEmpty()) {
                        responseModel = checkResultService.checkResult(ResultData.SUCCESS, responseModel);
                    } else {
                        log.error("uploadUser - function_failed: {}", row_string);
                        responseModel = checkResultService.checkResult(ResultData.FAIL, responseModel);
                        return Map.of("status", 200, "data", responseModel);
                    }
                } else {
                    responseModel = checkResultService.checkResult(ResultData.PERMISSION_FAILED, responseModel);
                    return Map.of("status", 200, "data", responseModel);
                }

            }
        } catch (Exception e) {
            log.error("uploadUser - Error: {}, class: {}, line: {}", e, e.getStackTrace()[0].getClassName(),
                    e.getStackTrace()[0].getLineNumber());
            responseModel = checkResultService.checkResult(ResultData.OTHER_ERROR, responseModel);
        }
        return Map.of("status", 200, "data", responseModel);
    }

    /**
     * Add user to team
     */
    public Map<String, Object> addUser(JsonNode request) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setReference_id(UUID.randomUUID().toString());
        responseModel.setDate_time(new Timestamp(new Date().getTime()));
        log.info("addUser - Request: {} ", request);
        try {
            Assert.notNull(request.get("body"), "no body");
            UserDetailView requestModel = mapper.treeToValue(request.get("body"), UserDetailView.class);

            if (requestModel == null) {
                responseModel = checkResultService.checkResult(ResultData.DATA_EMPTY, responseModel);
            } else {
                if (requestModel.getUserName().isEmpty()) {
                    responseModel.setReference_id(UUID.randomUUID().toString());
                    responseModel.setDate_time(new Timestamp(new Date().getTime()));
                    responseModel = checkResultService.checkResult(ResultData.IS_REQUITED, responseModel);
                    return Map.of("status", 200, "data", responseModel);
                }
                String userRole = checkRoleUser(requestModel.getUserLogin());
                if (!userRole.equals(ROLE_USER)) {
                    requestModel.setCreateDate(new Timestamp(new Date().getTime()));

                    String query = String.format("SELECT  FN_ADD_USER ('%s','%s','%s','%s','%s','%s','%s') RESULT FROM DUAL",
                            requestModel.getUserLogin(),
                            userRole,
                            requestModel.getUserName(),
                            requestModel.getUserRole(),
                            requestModel.getTeamName(),
                            requestModel.getActiveFlag(),
                            requestModel.getTeamLeader()
                    );

                    String row_string = jdbcTemplate.queryForObject(query, new Object[]{},
                            (rs, rowNum) ->
                                    rs.getString(("RESULT")
                                    ));

                    log.info("addUser - result_from_DB: {}", row_string);

                    if (row_string.equals(ResultData.SUCCESS.getResultCode())) {
                        responseModel = checkResultService.checkResult(ResultData.SUCCESS, responseModel);
                    } else {
                        responseModel = checkResultService.checkResult(ResultData.FAIL, responseModel);
                    }
                } else {
                    responseModel = checkResultService.checkResult(ResultData.PERMISSION_FAILED, responseModel);
                }
                return Map.of("status", 200, "data", responseModel);
            }
        } catch (Exception e) {
            log.error("addUser - Error: {}, class: {}, line: {}", e, e.getStackTrace()[0].getClassName(),
                    e.getStackTrace()[0].getLineNumber());
            responseModel.setResult_code(500);
            responseModel.setMessage("Others error");
        }
        return Map.of("status", 200, "data", responseModel);
    }

    /**
     * To save log when inhouse call this service
     *
     * @param request
     * @return
     */
    public Map<String, Object> sendAppFromF1(JsonNode request) {
        ResponseModel responseModel = new ResponseModel();
        log.info("sendAppFromF1 - Request: {} " + request);
        responseModel.setReference_id(UUID.randomUUID().toString());
        responseModel.setDate_time(new Timestamp(new Date().getTime()));
        try {
            Assert.notNull(request.get("body"), "no body");
            RequestModel requestModel = mapper.treeToValue(request.get("body"), RequestModel.class);

            if (requestModel == null) {
                responseModel.setResult_code(500);
                responseModel.setMessage("Data is empty");
            } else {
                List<ETLDataPush> listEtlDataPush = etlDataPushDAO
                        .findByAppNumberAndSuorceEtl(requestModel.getApplicationNo(), "ESB");
                ETLDataPush etlDataPush;
                if (listEtlDataPush.isEmpty()) {
                    etlDataPush = new ETLDataPush();
                    etlDataPush.setAssignedFlag("N");
                    log.info("sendAppFromF1 - newETL");
                } else {
                    log.info("sendAppFromF1 - updateETL");
                    etlDataPush = listEtlDataPush.get(0);
                }
                saveEtlDataPush(etlDataPush, requestModel);
                responseModel = checkResultService.checkResult(ResultData.SUCCESS, responseModel);
            }
        } catch (Exception e) {
            log.error("sendAppFromF1 - Error: {}, class: {}, line: {}", e, e.getStackTrace()[0].getClassName(),
                    e.getStackTrace()[0].getLineNumber());
            responseModel = checkResultService.checkResult(ResultData.OTHER_ERROR, responseModel);
        }
        return Map.of("status", 200, "data", responseModel);
    }

    private void saveEtlDataPush(ETLDataPush etlDataPush, RequestModel requestModel) {
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
        log.info("sendAppFromF1 - saveEtlDataPush - ETLDataPush: {}", etlDataPush);
        etlDataPushDAO.save(etlDataPush);
    }

    /**
     * Get All Configuration
     */
    public Map<String, Object> getAssignConfig() {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setReference_id(UUID.randomUUID().toString());
        responseModel.setDate_time(new Timestamp(new Date().getTime()));
        try {
            Map<String, Object> result = new HashMap<>();
            Map<String, AssignConfigResponse> mapAssignConfig = new HashMap<>();
            List<AssignConfigView> lst = assignConfigViewDAO.findAll();
            List<String> listTeamName = teamConfigDAO.getListTeamName();

            for (AssignConfigView ac : lst) {
                if (!mapAssignConfig.containsKey(ac.getStageName())) {
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
                    product.put(ac.getProduct(), assignConfigProductResponse);
                }
            }
            result.put("data", mapAssignConfig);
            result.put("lstTeamName", listTeamName);
            responseModel = checkResultService.checkResult(ResultData.SUCCESS, responseModel);
        } catch (Exception e) {
            log.error("getAssignConfig - Error: {}, class: {}, line: {}", e, e.getStackTrace()[0].getClassName(),
                    e.getStackTrace()[0].getLineNumber());
            responseModel = checkResultService.checkResult(ResultData.OTHER_ERROR, responseModel);
        }
        return Map.of("status", 200, "data", responseModel);
    }

    /**
     * Update Configuration
     */
    public Map<String, Object> setAssignConfig(JsonNode request) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setReference_id(UUID.randomUUID().toString());
        responseModel.setDate_time(new Timestamp(new Date().getTime()));
        try {
            log.info("setAssignConfig - Request: " + request);
            AssignConfig assignConfigRequest = mapper.readValue(request.get("body").toString(),
                    new TypeReference<AssignConfig>() {
                    });
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
                responseModel = checkResultService.checkResult(ResultData.SUCCESS, responseModel);
            } else {
                responseModel = checkResultService.checkResult(ResultData.FAIL, responseModel);
            }

        } catch (Exception e) {
            log.info("setAssignConfig - Error: {}, class: {}, line: {}", e, e.getStackTrace()[0].getClassName(),
                    e.getStackTrace()[0].getLineNumber());
            responseModel = checkResultService.checkResult(ResultData.OTHER_ERROR, responseModel);
        }
        return Map.of("status", 200, "data", responseModel);
    }

    public Map<String, Object> getDashboard(JsonNode request) {
        String logStr = "";
        logStr += "Request Data : " + request;
        ResponseModel responseModel = new ResponseModel();
        responseModel.setReference_id(UUID.randomUUID().toString());
        responseModel.setDate_time(new Timestamp(new Date().getTime()));
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
            responseModel = checkResultService.checkResult(ResultData.SUCCESS, responseModel);
            logStr += "Response: " + responseModel.toString();
        } catch (Exception e) {
            log.error("getDashboard - Error: {}, class: {}, line: {}", e, e.getStackTrace()[0].getClassName(),
                    e.getStackTrace()[0].getLineNumber());
            responseModel = checkResultService.checkResult(ResultData.OTHER_ERROR, responseModel);
        } finally {
            log.info(logStr);
        }
        return Map.of("status", 200, "data", responseModel);
    }

    public Map<String, Object> getPending(JsonNode request) {
        String logStr = "";
        logStr += "Request Data : " + request;
        ResponseModel responseModel = new ResponseModel();
        responseModel.setReference_id(UUID.randomUUID().toString());
        responseModel.setDate_time(new Timestamp(new Date().getTime()));
        try {
            String SQL = "SELECT * FROM V_ALLOCATION_TEAM_CONFIG P WHERE P.USER_NAME = {USER_LOGIN}";
            SQL = SQL.replace("{USER_LOGIN}", "'" + request.path("body").path("userLogin").textValue() + "'");
            List<ViewTeamConfig> listViewTeamConfig = jdbcTemplate.query(SQL, new Object[]{}, (rs, rowNum) -> {
                ViewTeamConfig viewTeamConfig = new ViewTeamConfig();
                viewTeamConfig.setUserName(rs.getString("USER_NAME"));
                viewTeamConfig.setUserRole(rs.getString("USER_ROLE"));
                viewTeamConfig.setTeamName(rs.getString("TEAM_NAME"));
                viewTeamConfig.setMaxPending(rs.getInt("MAX_PENDING"));
                viewTeamConfig.setMaxQuota(rs.getInt("MAX_QUOTA"));
                return viewTeamConfig;
            });
            ArrayNode dataArrayNode = mapper.createArrayNode();
            if (!listViewTeamConfig.isEmpty()) {
                Iterator<ViewTeamConfig> it = listViewTeamConfig.iterator();
                while (it.hasNext()) {
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
            responseModel = checkResultService.checkResult(ResultData.SUCCESS, responseModel);
            logStr += "Response: " + responseModel.toString();
        } catch (Exception e) {
            log.error("getPending - Error: {}, class: {}, line: {}", e, e.getStackTrace()[0].getClassName(),
                    e.getStackTrace()[0].getLineNumber());
            responseModel = checkResultService.checkResult(ResultData.OTHER_ERROR, responseModel);
        } finally {
            log.info(logStr);
        }
        return Map.of("status", 200, "data", responseModel);
    }

    /**
     * Update config maxPending & Max App by team
     */
    public Map<String, Object> updatePending(JsonNode request) {
        String logStr = "";
        logStr += "Request Data : " + request;
        ResponseModel responseModel = new ResponseModel();
        responseModel.setReference_id(UUID.randomUUID().toString());
        responseModel.setDate_time(new Timestamp(new Date().getTime()));
        try {
            String userRole = checkRoleUser(request.path("body").path("userLogin").asText());
            if (userRole.equals(ROLE_SUB)) {
                List<TeamConfig> listTeamName = teamConfigDAO.findByTeamName(request.path("body").path("userTeam").textValue());
                Iterator<TeamConfig> it = listTeamName.iterator();
                while (it.hasNext()) {
                    TeamConfig teamConfig = it.next();
                    teamConfig.setMaxPending(request.path("body").path("maxPending").intValue());
                    teamConfig.setMaxQuota(request.path("body").path("maxQuota").intValue());
                    teamConfig.setUserUpdate(request.path("body").path("userLogin").textValue());
                    teamConfig.setUpdateDate(new Timestamp(new Date().getTime()));
                    teamConfigDAO.save(teamConfig);
                }
                responseModel = checkResultService.checkResult(ResultData.SUCCESS, responseModel);
            } else {
                responseModel = checkResultService.checkResult(ResultData.PERMISSION_FAILED, responseModel);
            }
        } catch (Exception e) {
            log.error("updatePending - Error: {}, class: {}, line: {}", e, e.getStackTrace()[0].getClassName(),
                    e.getStackTrace()[0].getLineNumber());
            responseModel = checkResultService.checkResult(ResultData.OTHER_ERROR, responseModel);
        } finally {
            log.info(logStr);
        }
        return Map.of("status", 200, "data", responseModel);
    }

    /**
     * Get user team information of 1 user
     */
    public Map<String, Object> getInfoUserLogin(JsonNode request) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setReference_id(UUID.randomUUID().toString());
        responseModel.setDate_time(new Timestamp(new Date().getTime()));
        log.info("getInfoUserLogin - Request: {} ", request);
        try {
            Assert.notNull(request.get("body"), "no body");
            UserDetailView requestModel = mapper.treeToValue(request.get("body"), UserDetailView.class);

            if (requestModel.getUserName() == null || requestModel.getUserName().isEmpty()) {
                responseModel = checkResultService.checkResult(ResultData.IS_REQUITED, responseModel);
            } else {
                List<UserDetailView> userDetailViewList = userDetailsViewDAO.findAllByUserName(requestModel.getUserName());
                if (userDetailViewList.size() <= 0 || userDetailViewList == null) {
                    responseModel = checkResultService.checkResult(ResultData.DATA_EMPTY, responseModel);
                } else {
                    responseModel.setData(userDetailViewList);
                    responseModel = checkResultService.checkResult(ResultData.SUCCESS, responseModel);
                }
            }

        } catch (Exception e) {
            log.error("getInfoUserLogin - Error: {}, class: {}, line: {}", e, e.getStackTrace()[0].getClassName(),
                    e.getStackTrace()[0].getLineNumber());
            responseModel = checkResultService.checkResult(ResultData.OTHER_ERROR, responseModel);
        }
        return Map.of("status", 200, "data", responseModel);
    }

    /**
     * Remove user team
     */
    public Map<String, Object> removeUser(JsonNode request) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setReference_id(UUID.randomUUID().toString());
        responseModel.setDate_time(new Timestamp(new Date().getTime()));
        log.info("removeUser - Request: {} ", request);
        try {
            Assert.notNull(request.get("body"), "no body");
            UserDetailView requestModel = mapper.treeToValue(request.get("body"), UserDetailView.class);
            String userRole = checkRoleUser(request.path("body").path("userLogin").asText());
            if (userRole.equals(ROLE_SUB)) {
                if (requestModel.getUserId() == null) {
                    responseModel = checkResultService.checkResult(ResultData.IS_REQUITED, responseModel);
                } else {
                    userDetailsDAO.deleteById(requestModel.getUserId());
                    responseModel = checkResultService.checkResult(ResultData.SUCCESS, responseModel);
                }
            } else {
                responseModel = checkResultService.checkResult(ResultData.PERMISSION_FAILED, responseModel);
            }
        } catch (Exception e) {
            log.error("removeUser - Error: {}, class: {}, line: {}", e, e.getStackTrace()[0].getClassName(),
                    e.getStackTrace()[0].getLineNumber());
            responseModel = checkResultService.checkResult(ResultData.OTHER_ERROR, responseModel);
        }
        return Map.of("status", 200, "data", responseModel);
    }

    /**
     * Update user team
     */
    public Map<String, Object> changeActiveUser(JsonNode request) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setReference_id(UUID.randomUUID().toString());
        responseModel.setDate_time(new Timestamp(new Date().getTime()));
        log.info("changeActiveUser - Request: {} ", request);
        try {
            Assert.notNull(request.get("body"), "no body");

            String userId = request.path("body").path("userId").asText();
            String userLogin = request.path("body").path("userLogin").asText();
            String teamLeader = request.path("body").path("teamLeader").asText();
            String activeFlag = request.path("body").path("activeFlag").asText();
            String userRole = checkRoleUser(userLogin);

            if ((userRole.equals(ROLE_SUB) || userRole.equals(ROLE_LEADER)) && userId != null) {
                UserDetail userDetail = userDetailsDAO.findById(Long.valueOf(userId)).get();
                log.info("changeActiveUser - userDetail: {}", userDetail);
                Date date = new Date();
                Timestamp now = new Timestamp(date.getTime());
                userDetail.setTeamLeader(teamLeader);
                userDetail.setActiveFlag(activeFlag);
                userDetail.setUpdateTime(now);
                userDetailsDAO.save(userDetail);
                responseModel = checkResultService.checkResult(ResultData.SUCCESS, responseModel);
            } else {
                // userId is required or permission user login
                log.error("changeActiveUser - userId: {}", userId);
                responseModel = checkResultService.checkResult(ResultData.FAIL, responseModel);
            }

        } catch (Exception e) {
            log.error("changeActiveUser - Error: {}, class: {}, line: {}", e, e.getStackTrace()[0].getClassName(),
                    e.getStackTrace()[0].getLineNumber());
            responseModel = checkResultService.checkResult(ResultData.OTHER_ERROR, responseModel);
        }
        return Map.of("status", 200, "data", responseModel);
    }

    /**
     * Robot reponse result to Allocation
     */
    public Map<String, Object> updateStatusApp(JsonNode request) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setReference_id(UUID.randomUUID().toString());
        responseModel.setDate_time(new Timestamp(new Date().getTime()));
        ResultData resultData;
        log.info("updateStatusApp - Request: {} ", request);
        try {
            String result = getResult("ROBOT_ASSIGN", request.path("body").path("appId").textValue(), "",
                    "", 0, request.path("body").path("automationResult").textValue(),
                    request.path("body").path("automationResultMessage").textValue(),
                    request.path("body").path("userAuto").textValue());

            log.info("updateStatusApp - result_DB: {}", result);
            resultData = ResultData.findResultData(result);
            responseModel = checkResultService.checkResult(resultData, responseModel);

//            if (result.equals(ResultData.SUCCESS.getResultCode())) {
//                responseModel.setResult_code(Integer.valueOf(ResultData.SUCCESS.getResultCode()));
//                responseModel.setMessage(ResultData.SUCCESS.getResultMessage());
//            } else if (result.equals("205")) {
//                responseModel.setResult_code(205);
//                responseModel.setMessage("Application not exist or status_assign difference assigning");
//            } else {
//                responseModel.setResult_code(203);
//                responseModel.setMessage("Fail");
//                log.error("Wrong DB: " + result);
//            }
        } catch (Exception e) {
            log.error("updateStatusApp - Error: {}, class: {}, line: {}", e, e.getStackTrace()[0].getClassName(),
                    e.getStackTrace()[0].getLineNumber());
            responseModel = checkResultService.checkResult(ResultData.OTHER_ERROR, responseModel);
        }
        return Map.of("status", 200, "data", responseModel);
    }

    /**
     *
     */
    public Map<String, Object> getAllPendingCode(JsonNode request) {
        ResponseModel responseModel = new ResponseModel();
        log.info("getAllPendingCode - Request: {}", request);
        responseModel.setReference_id(UUID.randomUUID().toString());
        responseModel.setDate_time(new Timestamp(new Date().getTime()));
        try {
            List<AllocationPendingCode> allocationPendingCodes = allocationPendingCodeDao.findAll();
            ArrayNode dataArrayNode = mapper.createArrayNode();
            for (int i = 0; i < allocationPendingCodes.size(); i++) {
                ObjectNode data = mapper.createObjectNode();
                data.put("codePending", allocationPendingCodes.get(i).getCodePending());
                data.put("codeDesc", allocationPendingCodes.get(i).getCodeDesc());
                data.put("stageName", allocationPendingCodes.get(i).getStageName());
                dataArrayNode.add(data);
            }
            responseModel.setData(dataArrayNode);
            responseModel = checkResultService.checkResult(ResultData.SUCCESS, responseModel);

        } catch (Exception e) {
            log.error("getAllPendingCode - Error: {}, class: {}, line: {}", e, e.getStackTrace()[0].getClassName(),
                    e.getStackTrace()[0].getLineNumber());
            responseModel = checkResultService.checkResult(ResultData.OTHER_ERROR, responseModel);
        }
        return Map.of("status", 200, "data", responseModel);
    }

//    public Map<String, Object> holdApplication(JsonNode request) {
//        ResponseModel responseModel = new ResponseModel();
//        log.info("updatePendingDetail - Request: {}", request);
//        responseModel.setReference_id(UUID.randomUUID().toString());
//        responseModel.setDate_time(new Timestamp(new Date().getTime()));
//        try {
//            String appNumber = request.path("body").path("appNumber").textValue();
//            String stageName = request.path("body").path("stageName").textValue();
//            Timestamp creation_appStage_time = Timestamp.valueOf(request.path("body").path("creation_appStage_time").textValue());
//            List<AssignmentDetail> listAssignmentDetail = assignmentDetailDAO.findByAppNumberAndStageNameAndAndCreationApplStageTime(appNumber, stageName, creation_appStage_time);
//            Iterator<AssignmentDetail> it = listAssignmentDetail.iterator();
//            if (listAssignmentDetail.size() == 0) {
//                responseModel.setResult_code(202);
//                responseModel.setMessage("Application can't be hold. Please reload page.");
//            } else {
//                UserDetail userDetail = userDetailsDAO.findByUserName(request.path("body").path("pendingUser").textValue());
//                if (userDetail.getPendingApp() < request.path("body").path("maxPending").intValue()) {
//                    updateInfor(it, request, userDetail, responseModel);
//                } else {
//                    responseModel.setResult_code(203);
//                    responseModel.setMessage("Number of pending app is limited. Please contact your supervisor for support.");
//                }
//            }
//        } catch (Exception e) {
//            log.error("getAllPendingCode - Error: {}, class: {}, line: {}", e, e.getStackTrace()[0].getClassName(),
//                    e.getStackTrace()[0].getLineNumber());
//            responseModel.setResult_code(500);
//            responseModel.setMessage("Others error");
//        }
//        return Map.of("status", 200, "data", responseModel);
//    }
//
//    private void updateInfor(Iterator<AssignmentDetail> it, JsonNode request, UserDetail userDetail, ResponseModel responseModel) {
//        while (it.hasNext()) {
//            try {
//                AssignmentDetail assignmentDetail = it.next();
//                AllocationPendingDetail allocationPendingDetail = new AllocationPendingDetail();
//                allocationPendingDetail.setAppNumber(assignmentDetail.getAppNumber());
//                allocationPendingDetail.setStageName(request.path("body").path("stageName").textValue());
//                allocationPendingDetail.setCreationApplstageTime(assignmentDetail.getCreationApplStageTime());
//                allocationPendingDetail.setPendingCode(request.path("body").path("pendingCode").textValue());
//                allocationPendingDetail.setPendingComments(request.path("body").path("pendingComments").textValue());
//                allocationPendingDetail.setPendingUser(request.path("body").path("pendingUser").textValue());
//                allocationPendingDetail.setPendingDate(new Timestamp(new Date().getTime()));
//                allocationPendingDetail.setTeamUser(request.path("body").path("teamUser").textValue());
//                allocationPendingDetail.setCurrentCycle(assignmentDetail.getCurrentCycle());
//
//                //update status assignment Detail
//                assignmentDetail.setStatusAssign("PENDING");
//                //add log quota
//                insertLogQuota(assignmentDetail, userDetail.getUserName(), "updateInfor", "ADD", "PENDING", userDetail.getPendingApp());
//                insertLogQuota(assignmentDetail, userDetail.getUserName(), "updateInfor", "SUB", "QUOTA", userDetail.getQuotaApp());
//
//                int pendingApp = userDetail.getPendingApp() + 1;
//                userDetail.setPendingApp(pendingApp);
//                int quotaApp = userDetail.getQuotaApp() - 1;
//                userDetail.setQuotaApp(quotaApp);
//                allocationPendingDetailDao.save(allocationPendingDetail);
//                assignmentDetailDAO.save(assignmentDetail);
//                userDetailsDAO.save(userDetail);
//                responseModel.setResult_code(200);
//                responseModel.setMessage("SUCCESS");
//            } catch (Exception e) {
//                log.error("updatePendingDetail - updateInfor - Error: {}, class: {}, line: {}", e, e.getStackTrace()[0].getClassName(),
//                        e.getStackTrace()[0].getLineNumber());
//                responseModel.setResult_code(201);
//                responseModel.setMessage("Insert into Assignment Detail not success");
//            }
//        }
//    }

    public Map<String, Object> holdApplication(JsonNode request) {
        ResponseModel responseModel = new ResponseModel();
        log.info("updatePendingDetail - Request: {}", request);
        responseModel.setReference_id(UUID.randomUUID().toString());
        responseModel.setDate_time(new Timestamp(new Date().getTime()));
        ResultData resultData;
        try {
            String userRole = checkRoleUser(request.path("body").path("pendingUser").textValue());
            if (userRole.equals(ROLE_USER)) {
                String result = getResult("HOLD", request.path("body").path("appNumber").textValue(),
                        request.path("body").path("pendingCode").textValue(),
                        request.path("body").path("pendingComments").textValue(),
                        request.path("body").path("maxPending").intValue(), "", "", "");

                log.info("holdApplication - result_DB: {}", result);
                resultData = ResultData.findResultData(result);
                responseModel = checkResultService.checkResult(resultData, responseModel);

            } else {
                responseModel = checkResultService.checkResult(ResultData.PERMISSION_FAILED, responseModel);
            }


        } catch (Exception e) {
            log.error("getAllPendingCode - Error: {}, class: {}, line: {}", e, e.getStackTrace()[0].getClassName(),
                    e.getStackTrace()[0].getLineNumber());
            responseModel = checkResultService.checkResult(ResultData.OTHER_ERROR, responseModel);
        }
        return Map.of("status", 200, "data", responseModel);
    }

    public Map<String, Object> updateVendor(JsonNode request) {
        ResponseModel responseModel = new ResponseModel();
        log.info("updateVendor - Request: {}", request);
        try {
            List<ETLDataPush> listETLData = etlDataPushDAO
                    .findByAppNumberAndSuorceEtl(request.path("body").path("appNumber").textValue(), "ESB");
            ETLDataPush etlDataPush = null;
            if (listETLData.isEmpty()) {
                etlDataPush = new ETLDataPush();
                etlDataPush.setCreatedTimeVendor(new Timestamp(new Date().getTime()));
                etlDataPush.setAssignedFlag("N");
                log.info("updateVendor - New_ETL_DataPush - ETLDataPush: {}", etlDataPush);
            } else {
                etlDataPush = listETLData.get(0);
                log.info("updateVendor - Query_DB - ETLDataPush: {}", etlDataPush);
                etlDataPush.setCreatedTimeVendor(new Timestamp(new Date().getTime()));
            }
            etlDataPush.setVendorName(request.path("body").path("vendorId").textValue());
            log.info("updateVendor - ETLDataPush: {}", etlDataPush);
            etlDataPushDAO.save(etlDataPush);
            responseModel = checkResultService.checkResult(ResultData.SUCCESS, responseModel);
            log.info("updateVendor success : " + request.path("body").path("appNumber").textValue());
        } catch (Exception e) {
            log.error("updateVendor - Error: {}, class: {}, line: {}", e, e.getStackTrace()[0].getClassName(),
                    e.getStackTrace()[0].getLineNumber());
            responseModel = checkResultService.checkResult(ResultData.OTHER_ERROR, responseModel);
        }

        return Map.of("status", 200, "data", responseModel);
    }

//    public Object updateRaiseQuery(JsonNode request) {
//        ResponseModel responseModel = new ResponseModel();
//        log.info("updateRaiseQuery - Request: {}", request);
//        try {
//            if (request.path("body").path("queryStatus").textValue().equals("Raised")) {
//                AssignmentDetail assignmentDetail = assignmentDetailDAO.findByAppNumberAndStageName(request.path("body").path("applicationNo").textValue(), request.path("body").path("stage").textValue());
//                if (assignmentDetail == null) {
//                    log.info("updateRaiseQuery: Can not find " + request.path("body").path("applicationNo").textValue() + "in Assignment Detail");
//                    responseModel.setResult_code(204);
//                    responseModel.setMessage("Can not found appNo");
//                } else if(assignmentDetail.getStatusAssign().equals("PENDING")){
//                    log.info("updateRaiseQuery: AppNo " + request.path("body").path("applicationNo").textValue() + "is PENDING");
//                    responseModel.setResult_code(206);
//                    responseModel.setMessage("Status assign is PENDING");
//                }else {
//                    AllocationPendingDetail allocationPendingDetail = new AllocationPendingDetail();
//                    allocationPendingDetail.setAppNumber(assignmentDetail.getAppNumber());
//                    allocationPendingDetail.setCreationApplstageTime(assignmentDetail.getCreationApplStageTime());
//                    allocationPendingDetail.setStageName(request.path("body").path("stage").textValue());
//                    allocationPendingDetail.setPendingCode("PENRS");
//                    allocationPendingDetail.setPendingComments(request.path("body").path("comment").textValue());
//                    allocationPendingDetail.setPendingUser(assignmentDetail.getAssignee());
//                    allocationPendingDetail.setPendingDate(new Timestamp(new Date().getTime()));
//                    allocationPendingDetail.setTeamUser(assignmentDetail.getTeamAssignee());
//                    allocationPendingDetail.setCurrentCycle(assignmentDetail.getCurrentCycle());
//                    UserDetail userDetail = userDetailsDAO.findByUserName(assignmentDetail.getAssignee());
//                    if (userDetail == null) {
//                        responseModel.setResult_code(205);
//                        responseModel.setMessage("Can not found user");
//                        log.info("updateRaiseQuery: Can not find " + assignmentDetail.getAssignee() + "in User Detail. For : " + request.path("body").path("applicationNo").textValue() + "," + request.path("body").path("stage").textValue());
//                    } else {
//                        allocationPendingDetailDao.save(allocationPendingDetail);
//                        //Check condition <> FAILED to sub Quota
//                        //add log quota
//                        insertLogQuota(assignmentDetail, userDetail.getUserName(), "updateRaiseQuery", "ADD", "PENDING", userDetail.getPendingApp());
//
//                        int pendingApp = userDetail.getPendingApp() + 1;
//                        userDetail.setPendingApp(pendingApp);
//
//                        if(!assignmentDetail.getStatusAssign().equals("FAILED")){
//                            insertLogQuota(assignmentDetail, userDetail.getUserName(), "updateRaiseQuery : <> FAILED", "SUB", "QUOTA", userDetail.getQuotaApp());
//                            int quotaApp = userDetail.getQuotaApp() - 1;
//                            userDetail.setQuotaApp(quotaApp);
//                        }
//                        userDetailsDAO.save(userDetail);
//                        //update status for assigment detail
//                        assignmentDetail.setStatusAssign("PENDING");
//                        assignmentDetailDAO.save(assignmentDetail);
//                        responseModel.setResult_code(200);
//                        responseModel.setMessage("Success");
//                    }
//                }
//            } else {
//                responseModel.setResult_code(207);
//                responseModel.setMessage("Query status different raised");
//                log.error("AppNum = " + request.path("body").path("applicationNo").textValue() + "with assign status = " + request.path("body").path("queryStatus").textValue() + "received raised query from raisedBy " + request.path("body").path("raiseBy").textValue() + "at stage" + request.path("body").path("stage").textValue());
//            }
//        } catch (Exception e) {
//            log.error("updateRaiseQuery - Error: {}, class: {}, line: {}", e, e.getStackTrace()[0].getClassName(),
//                    e.getStackTrace()[0].getLineNumber());
//            responseModel.setResult_code(500);
//            responseModel.setMessage("Others error");
//        }
//        return Map.of("status", 200, "data", responseModel);
//    }

    public Object updateRaiseQuery(JsonNode request) {
        ResponseModel responseModel = new ResponseModel();
        log.info("updateRaiseQuery - Request: {}", request);
        try {
            if (request.path("body").path("queryStatus").textValue().equals("Raised")) {

                String result = getResult("RAISE_QUERY", request.path("body").path("applicationNo").textValue(),
                        "PENRS", request.path("body").path("comment").textValue(),
                        0, "", "", "");
                ResultData resultData;
                log.info("updateRaiseQuery - result_DB: {}", result);
                resultData = ResultData.findResultData(result);
                responseModel = checkResultService.checkResult(resultData, responseModel);

            }
        } catch (Exception e) {
            log.error("updateRaiseQuery - Error: {}, class: {}, line: {}", e, e.getStackTrace()[0].getClassName(),
                    e.getStackTrace()[0].getLineNumber());
            responseModel = checkResultService.checkResult(ResultData.OTHER_ERROR, responseModel);
        }
        return Map.of("status", 200, "data", responseModel);
    }

    public Object reassign(JsonNode request) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setReference_id(UUID.randomUUID().toString());
        responseModel.setDate_time(new Timestamp(new Date().getTime()));
        log.info("reassign - Request: {}", request);
        try {
            String userRole = checkRoleUser(request.path("body").path("reassignBy").textValue());
            if (!userRole.equals(ROLE_USER)) {
                SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withFunctionName("FN_ALLOCATION_REASSIGN_APP");
                MapSqlParameterSource paramMap = new MapSqlParameterSource();
                paramMap.addValue("P_APP_NUM", request.path("body").path("appNumber").textValue());
                paramMap.addValue("P_STAGE_NAME", request.path("body").path("stageName").textValue());
                paramMap.addValue("P_REASSIGN_BY", request.path("body").path("reassignBy").textValue());
                paramMap.addValue("P_REASSIGN_TO", request.path("body").path("reassignTo").textValue());
                paramMap.addValue("P_COMMENTS", request.path("body").path("comments").textValue());

                String result = call.executeFunction(String.class, paramMap);

                ResultData resultData;
                log.info("updateRaiseQuery - result_DB: {}", result);
                resultData = ResultData.findResultData(result);
                responseModel = checkResultService.checkResult(resultData, responseModel);

            } else {
                responseModel = checkResultService.checkResult(ResultData.PERMISSION_FAILED, responseModel);
            }

        } catch (Exception e) {
            log.error("reassign - Error: {}, class: {}, line: {}", e, e.getStackTrace()[0].getClassName(),
                    e.getStackTrace()[0].getLineNumber());
            responseModel = checkResultService.checkResult(ResultData.OTHER_ERROR, responseModel);
        }
        return Map.of("status", 200, "data", responseModel);
    }

    private String getResult(String action, String appNumber, String pendingCode, String pendingDes,
                             int maxPending, String resultRobot, String resultMess, String botName) {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withFunctionName("FN_ALLOCATION_ACTION_APP");
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue("P_ACTION", action);
        paramMap.addValue("P_APP_NUM", appNumber);
        //HOLD APPLICATION
        paramMap.addValue("P_PENDING_CODE", pendingCode);
        paramMap.addValue("P_PENDING_DES", pendingDes);
        paramMap.addValue("P_MAX_PENDING", maxPending);
        // ROBOT ASSIGN
        paramMap.addValue("P_RESULT_ROBOT", resultRobot);
        paramMap.addValue("P_ERROR_MESSAGE", resultMess);
        paramMap.addValue("P_BOT_NAME", botName);

        return call.executeFunction(String.class, paramMap);

    }

    private String checkRoleUser(String userLogin) {
        if (StringUtils.isEmpty(userLogin)) {
            return null;
        }
        return userDetailsViewDAO.findAllByUserName(userLogin).get(0).getUserRole();
    }

//    private void insertLogQuota(AssignmentDetail assignmentDetail, String userName, String comment, String method, String type, int oldValue){
//        AllocationLogQuota logReasssignQuota = new AllocationLogQuota();
//        logReasssignQuota.setAppNumber(assignmentDetail.getAppNumber());
//        logReasssignQuota.setCreationApplStageTime(assignmentDetail.getCreationApplStageTime());
//        logReasssignQuota.setCurrentCycle(assignmentDetail.getCurrentCycle());
//        logReasssignQuota.setStageName(assignmentDetail.getStageName());
//        logReasssignQuota.setCreationTimeStamp(new Timestamp(new Date().getTime()));
//        logReasssignQuota.setAssignee(userName);
//        logReasssignQuota.setTeamAssignee(assignmentDetail.getTeamAssignee());
//        logReasssignQuota.setAssignedBy(assignmentDetail.getAssignedBy());
//        logReasssignQuota.setAppType(assignmentDetail.getAppType());
//        logReasssignQuota.setSourceChanel(assignmentDetail.getSourceChanel());
//        logReasssignQuota.setComments(comment);
//        logReasssignQuota.setMethodSubAdd(method);
//        logReasssignQuota.setQuotaPending(type);
//        logReasssignQuota.setOldValues(oldValue);
//        allocationLogQuotaDao.save(logReasssignQuota);
//    }
}