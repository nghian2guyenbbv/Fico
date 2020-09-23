package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import vn.com.tpf.microservices.dao.ETLDataPushDAO;
import vn.com.tpf.microservices.dao.HistoryAllocationDAO;
import vn.com.tpf.microservices.dao.UserCheckingDAO;
import vn.com.tpf.microservices.dao.UserDetailsDAO;
import vn.com.tpf.microservices.models.*;

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
	HistoryAllocationDAO historyAllocationDAO;

	@Autowired
	UserDetailsDAO userDetailsDAO;

	private static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	private static String SHEET = "UserTeam";

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
			RequestGetUserDeatail requestModel = mapper.treeToValue(request.get("body"), RequestGetUserDeatail.class);

			Sort typeSort = Sort.by(requestModel.getSortItem()).descending();
			if (requestModel.getTypeSort() == "ESC") {
				typeSort = Sort.by(requestModel.getSortItem()).ascending();
			}

			Pageable pageable = PageRequest.of(requestModel.getPage() , requestModel.getItemPerPage(), typeSort);

			Page<UserDetail> listUserDetails;
			if ( requestModel.getRoleUserLogin() == "role_leader") {
				listUserDetails = userDetailsDAO.findAllUserForLeader(requestModel.getTeamName(), pageable);
			} else {
				listUserDetails = userDetailsDAO.findAllUserForSub(requestModel.getTeamName(), pageable);
			}

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
			responseModel.setResult_code(500);
			responseModel.setMessage("Get success");
			responseModel.setData(listUserDetails);

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
			if (hasExcelFormat(requestModel.getUrlFile())) {
				responseModel.setRequest_id(request_id);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(500);
				responseModel.setMessage("Import file with format Excel or CSV");
				return Map.of("status", 200, "data", responseModel);
			}

			List<UserChecking> userCheckingList = excelToUserChecking(requestModel.getUrlFile().getInputStream(), requestModel);
			userCheckingDAO.saveAll(userCheckingList);

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


	public static boolean hasExcelFormat(MultipartFile file) {

		if (!TYPE.equals(file.getContentType())) {
			return false;
		}

		return true;
	}

	public static List<UserChecking> excelToUserChecking(InputStream is, RequestImportFile infoFile) {
		try {
			Workbook workbook = new XSSFWorkbook(is);

			Sheet sheet = workbook.getSheet(SHEET);
			Iterator<Row> rows = sheet.iterator();

			List<UserChecking> listUserChecking = new ArrayList<UserChecking>();

			int rowNumber = 0;
			while (rows.hasNext()) {
				Row currentRow = rows.next();

				// skip header
				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}

				Iterator<Cell> cellsInRow = currentRow.iterator();

				UserChecking userChecking = new UserChecking();

				int cellIdx = 0;
				while (cellsInRow.hasNext()) {
					Cell currentCell = cellsInRow.next();

					switch (cellIdx) {
						case 0:
							userChecking.setUserName(currentCell.getStringCellValue());
							break;

						case 1:
							userChecking.setActiveFlag(currentCell.getStringCellValue());
							break;

						default:
							break;
					}
					userChecking.setUserLogin(infoFile.getUserLogin());
					userChecking.setUserRole("role_user");
					userChecking.setCheckedFlag("OPEN");
					userChecking.setCreateDate(new Timestamp(new Date().getTime()));
					cellIdx++;
				}

				listUserChecking.add(userChecking);
			}

			workbook.close();

			return listUserChecking;
		} catch (IOException e) {
			throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
		}
	}
}