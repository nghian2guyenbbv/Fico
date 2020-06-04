package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import vn.com.tpf.microservices.dao.FicoCustomerDAO;
import vn.com.tpf.microservices.dao.FicoImportPayooDAO;
import vn.com.tpf.microservices.dao.FicoReceiptPaymentDAO;
import vn.com.tpf.microservices.dao.FicoTransPayDAO;
import vn.com.tpf.microservices.models.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class RepaymentService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private static final String AUTOMATION = "automation";
	private static final String DOCUMENT_CHECK = "document_check";
	private static final String LOAN_BOOKING = "loan_booking";

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private RabbitMQService rabbitMQService;

	@Autowired
	private FicoCustomerDAO ficoCustomerDAO;

	@Autowired
	private FicoTransPayDAO ficoTransPayDAO;

	@Autowired
	private FicoImportPayooDAO ficoImportPayooDAO;

	@Autowired
	private FicoReceiptPaymentDAO ficoReceiptPaymentDAO;

	@Autowired
	private TransactionTemplate transactionTemplate;

	@PersistenceContext
	private EntityManager entityManager;

	@Value("${spring.hostRequest.url}")
	private String urlAPI;

	@SuppressWarnings("unchecked")

	public Map<String, Object> getCustomers(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		try{
			List<FicoCustomer> ficoCustomerList = null;

			Assert.notNull(request.get("body"), "no body");
			RequestModel requestModel = mapper.treeToValue(request.get("body"), RequestModel.class);
			request_id = requestModel.getRequest_id();

//            DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            try{
				OffsetDateTime.parse(requestModel.getDate_time());
            }catch (Exception e) {
				log.info("Error: " + e);
                responseModel.setRequest_id(requestModel.getRequest_id());
                responseModel.setReference_id(UUID.randomUUID().toString());
                responseModel.setDate_time(new Timestamp(new Date().getTime()));
                responseModel.setResult_code(500);
                responseModel.setMessage("Others error");
                return Map.of("status", 200, "data", responseModel);
            }

			String inputValue = requestModel.getData().getSearch_value();

			if(isValidIdNumer(inputValue)) {
				ficoCustomerList = ficoCustomerDAO.findByIdentificationNumber(inputValue);
			}else{
				ficoCustomerList = ficoCustomerDAO.findByLoanAccountNo(inputValue);
			}
			if(ficoCustomerList.size() == 0) {
				responseModel.setRequest_id(requestModel.getRequest_id());
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(1);
				responseModel.setMessage("not exist");
				responseModel.setData(ficoCustomerList);
			}else {
				responseModel.setRequest_id(requestModel.getRequest_id());
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(0);
				responseModel.setData(ficoCustomerList);
			}
		}
		catch (Exception e) {
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

	public Map<String, Object> getCustomers_pay(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		try{
			Assert.notNull(request.get("body"), "no body");
			RequestModel requestModel = mapper.treeToValue(request.get("body"), RequestModel.class);
			request_id = requestModel.getRequest_id();

			try{
				OffsetDateTime.parse(requestModel.getDate_time());
			}catch (Exception e) {
				log.info("Error: " + e);
				responseModel.setRequest_id(requestModel.getRequest_id());
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(500);
				responseModel.setMessage("Others error");
				return Map.of("status", 200, "data", responseModel);
			}

			String inputValue = requestModel.getData().getTransaction_id();

			FicoTransPay ficoTransPay = ficoTransPayDAO.findByTransactionId(inputValue);
			if(ficoTransPay == null) {
				responseModel.setRequest_id(requestModel.getRequest_id());
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(1);
				responseModel.setMessage("not exist");
				responseModel.setData(ficoTransPay);
			}else {
				responseModel.setRequest_id(requestModel.getRequest_id());
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(0);
				responseModel.setData(ficoTransPay);
			}
		}
		catch (Exception e) {
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

	public Map<String, Object> customers_pay(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		Timestamp date_time = new Timestamp(new Date().getTime());
		try{
			Assert.notNull(request.get("body"), "no body");
			RequestModel requestModel = mapper.treeToValue(request.get("body"), RequestModel.class);
			FicoTransPay ficoTransPay = new FicoTransPay();
			request_id = requestModel.getRequest_id();
//			date_time = requestModel.getDate_time();

			try{
				OffsetDateTime.parse(requestModel.getDate_time());
			}catch (Exception e) {
				log.info("Error: " + e);
				responseModel.setRequest_id(requestModel.getRequest_id());
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(500);
				responseModel.setMessage("Others error");
				return Map.of("status", 200, "data", responseModel);
			}

			if (requestModel.getData().getAmount() <= 0){
                responseModel.setRequest_id(requestModel.getRequest_id());
                responseModel.setReference_id(UUID.randomUUID().toString());
                responseModel.setDate_time(new Timestamp(new Date().getTime()));
                responseModel.setResult_code(500);
                responseModel.setMessage("Others error");
                return Map.of("status", 200, "data", responseModel);
            }

			FicoTransPay getByTransactionId = ficoTransPayDAO.findByTransactionId(requestModel.getData().getTransaction_id());
			if (getByTransactionId == null) {
				FicoCustomer ficoLoanId = ficoCustomerDAO.findByLoanId(requestModel.getData().getLoan_id());
				List<FicoCustomer> ficoLoanAcct = ficoCustomerDAO.findByLoanAccountNo(requestModel.getData().getLoan_account_no());

				if (ficoLoanId != null && ficoLoanAcct.size() > 0){
					ficoTransPay.setLoanId(requestModel.getData().getLoan_id());
					ficoTransPay.setLoanAccountNo(requestModel.getData().getLoan_account_no());
					ficoTransPay.setIdentificationNumber(requestModel.getData().getIdentification_number());
					ficoTransPay.setTransactionId(requestModel.getData().getTransaction_id());
					ficoTransPay.setAmount(requestModel.getData().getAmount());
					ficoTransPay.setCreateDate(new Timestamp(new Date().getTime()));

					ficoTransPayDAO.save(ficoTransPay);
					new Thread(() -> {
						FicoRepaymentModel ficoRepaymentModel = new FicoRepaymentModel();
						ReceiptProcessingMO ficoReceiptPayment = new ReceiptProcessingMO();
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
						if (!requestModel.getData().getTransaction_id().isEmpty()){
							ficoReceiptPayment.setInstrumentReferenceNumber(requestModel.getData().getTransaction_id());
						}
						if (requestModel.getData().getTransaction_id().startsWith("PY")){
							ficoReceiptPayment.setSourceAccountNumber("45992855603");
						} else if (requestModel.getData().getTransaction_id().startsWith("MO")) {
							ficoReceiptPayment.setSourceAccountNumber("45992855306");
						}
						ReqHeader requestHeader = new ReqHeader();
						requestHeader.setTenantId(505);
						UserDetail userDetail = new UserDetail();
						userDetail.setBranchId(5);
						userDetail.setUserCode("system");
						requestHeader.setUserDetail(userDetail);
						ficoReceiptPayment.setRequestHeader(requestHeader);
						ficoReceiptPayment.setReceiptPayOutMode("ELECTRONIC_FUND_TRANSFER");
						ficoReceiptPayment.setPaymentSubMode("INTERNAL_TRANSFER");
						ficoReceiptPayment.setReceiptAgainst("SINGLE_LOAN");
						ficoReceiptPayment.setLoanAccountNo(requestModel.getData().getLoan_account_no());
						ficoReceiptPayment.setTransactionCurrencyCode("VND");
						ficoReceiptPayment.setInstrumentDate(simpleDateFormat.format(requestModel.getData().getCreate_date()));
						ficoReceiptPayment.setTransactionValueDate(simpleDateFormat.format(requestModel.getData().getCreate_date()));
						ficoReceiptPayment.setReceiptOrPayoutAmount(requestModel.getData().getAmount());
						ficoReceiptPayment.setAutoAllocation("Y");
						ficoReceiptPayment.setReceiptNo("");
						ficoReceiptPayment.setReceiptPurpose("ANY_DUE");
						ficoReceiptPayment.setDepositDate(simpleDateFormat.format(requestModel.getData().getCreate_date()));
						ficoReceiptPayment.setDepositBankAccountNumber("519200003");
						ficoReceiptPayment.setRealizationDate(simpleDateFormat.format(requestModel.getData().getCreate_date()));
						ficoReceiptPayment.setReceiptTransactionStatus("C");
						ficoReceiptPayment.setProcessTillMaker(false);
						ficoReceiptPayment.setRequestChannel("RECEIPT");
						ficoRepaymentModel.setReceiptProcessingMO(ficoReceiptPayment);

						// Save report receipt payment
						saveReportReceiptPayment(requestModel);

						String urlReceiptLMS = urlAPI;
						URI uri = null;
						try {
							uri = new URI(urlAPI);
						} catch (URISyntaxException e) {
							e.printStackTrace();
						}
						RestTemplate restTemplate = new RestTemplate();
						MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
						headers.add("clientId", "1");
						headers.add("sign", "1");
						headers.add("Content-Type", "application/json");
						HttpEntity<FicoRepaymentModel> body = new HttpEntity<>(ficoRepaymentModel, headers);
						ResponseEntity<JsonNode> res = restTemplate.postForEntity(uri, body, JsonNode.class);
						log.info("{}", res.getBody());
					}).start();
					responseModel.setRequest_id(requestModel.getRequest_id());
					responseModel.setReference_id(UUID.randomUUID().toString());
					responseModel.setDate_time(new Timestamp(new Date().getTime()));
					responseModel.setResult_code(0);
				}else{
					responseModel.setRequest_id(request_id);
					responseModel.setReference_id(UUID.randomUUID().toString());
					responseModel.setDate_time(new Timestamp(new Date().getTime()));
					responseModel.setResult_code(2);
					responseModel.setMessage("Not exits");
				}
			}else {
				responseModel.setRequest_id(request_id);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(2);
				responseModel.setMessage("transaction_id already exists.");
			}
		}
		catch (Exception e) {
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

	private static boolean isValidIdNumer(String strNum) {
		if(!strNum.matches("^[0-9]+$") && strNum.length()!=9 && strNum.length()!=12)
		{
			return false;
		}
		return true;
	}

	//---------------------- FUNCTION IMPORT ---------------------
	public Map<String, Object> importTrans(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String request_id = request.path("body").path("request_id").textValue();
		Timestamp date_time = new Timestamp(new Date().getTime());
		DateTimeFormatter formatterParseInput = DateTimeFormatter.ofPattern("d/M/yyyy H:m:s");
		DateTimeFormatter formatterParseOutput = DateTimeFormatter.ofPattern("M/d/yyyy H:m:s");

		try{
			Assert.notNull(request.get("body"), "no body");
			List<FicoPayooImp> requestModel = mapper.convertValue(request.path("body").path("data"), new TypeReference<List<FicoPayooImp>>(){});

			//parse createDate
			for (FicoPayooImp ficoPayooImp:requestModel)
			{
				ficoPayooImp.setCreateDateTrans(Timestamp.valueOf(LocalDateTime.parse(ficoPayooImp.getCreateDate(),formatterParseInput)));
			}

			//get ra maxdate
			Comparator<FicoPayooImp> comparator = Comparator.comparing(FicoPayooImp::getCreateDateTrans);
			FicoPayooImp maxObject = requestModel.stream().max(comparator).get();

			//set transDate
			for (FicoPayooImp ficoPayooImp:requestModel)
			{
				ficoPayooImp.setTransDate(maxObject.getCreateDateTrans());
			}

			List<FicoPayooImp> listResult= ficoImportPayooDAO.saveAll(requestModel);

			responseModel.setRequest_id(request_id);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(date_time);
			responseModel.setResult_code(0);
			responseModel.setData(Map.of("totalRow",listResult.size()));
		}
		catch (Exception e) {
			responseModel.setRequest_id(request_id);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(date_time);
			responseModel.setResult_code(500);
			responseModel.setMessage(e.getMessage());
		}
		return Map.of("status", 200, "data", responseModel);
	}

	@Transactional
	public Map<String, Object> settle(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String request_id = request.path("body").path("request_id").textValue();
		Timestamp date_time = new Timestamp(new Date().getTime());
		try{
			Date transDate = mapper.convertValue(request.path("body").path("data").path("transDate"), Date.class);
			Session session = entityManager.unwrap(Session.class);
			session.doWork(new Work() {
				@Override
				public void execute(Connection connection) throws SQLException {
					//connection, finally!
					String storeProc = String.format("CALL payoo.sp_insert_payment_settle('%s');", transDate);
					try (PreparedStatement stmt = connection.prepareStatement(storeProc)) {
//						ResultSet rs = stmt.executeQuery();
//						while (rs.next()) {
//							log.info("Found " + rs.getInt(1) + "result");
//						}
						int resuk=stmt.executeUpdate();
						log.info("Found : " + resuk + "result");
					}

//					Statement stmt = connection.createStatement();
//
//					String storeProc = String.format("CALL payoo.sp_insert_payment_settle('%s');", transDate);
//					int result = stmt.executeUpdate(storeProc);
//					System.out.println("settleStore: OK=>" + result);
//					connection.setAutoCommit(false);
//					connection.close();
				}
			});
			responseModel.setRequest_id(request_id);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(date_time);
			responseModel.setResult_code(0);
		}
		catch (Exception e) {
			e.printStackTrace();
			responseModel.setRequest_id(request_id);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(date_time);
			responseModel.setResult_code(500);
			responseModel.setMessage(e.getMessage());
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> getListTrans(JsonNode request) {
		String request_id = request.path("body").path("request_id").textValue();
		Timestamp date_time = new Timestamp(new Date().getTime());
		try{
			Timestamp fromDate = mapper.convertValue(request.path("body").path("data").path("fromDate"), Timestamp.class);
			Timestamp toDate = mapper.convertValue(request.path("body").path("data").path("toDate"), Timestamp.class);

			System.out.println("fromdate:" + fromDate + ", todate:" + toDate);

			StoredProcedureQuery q = entityManager.createNamedStoredProcedureQuery("getListTrans");
			q.setParameter(1, fromDate);
			q.setParameter(2, toDate);
			List<FicoTransPay> list=q.getResultList();

			return Map.of("status", 200, "data", Map.of("request_id",request_id,"reference_id",UUID.randomUUID().toString(),"date_time",date_time,"data",list,"result_code",0));
		}
		catch (Exception e) {
			e.printStackTrace();
			return Map.of("status", 200, "data", Map.of("request_id",request_id,"reference_id",UUID.randomUUID().toString(),"date_time",date_time,"result_code",500,"message",e.getMessage()));
		}
	}

	public Map<String, Object> getReport(JsonNode request) {
		String request_id = request.path("body").path("request_id").textValue();
		Timestamp date_time = new Timestamp(new Date().getTime());
		try{
			Timestamp fromDate = mapper.convertValue(request.path("body").path("data").path("fromDate"), Timestamp.class);
			Timestamp toDate = mapper.convertValue(request.path("body").path("data").path("toDate"), Timestamp.class);

			System.out.println("fromdate:" + fromDate + ", todate:" + toDate);

			StoredProcedureQuery q = entityManager.createNamedStoredProcedureQuery("getreport");
			q.setParameter(1, fromDate);
			q.setParameter(2, toDate);
			List<Object[]> list=q.getResultList();

			ArrayNode documents = mapper.createArrayNode();
			for (Object item: list) {
				ObjectNode doc = mapper.createObjectNode();
				doc.put("row", item.toString());
				documents.add(doc);
			}

			return Map.of("status", 200, "data", Map.of("request_id",request_id,"reference_id",UUID.randomUUID().toString(),"date_time",date_time,"data",documents,"result_code",0));
		}
		catch (Exception e) {
			e.printStackTrace();
			return Map.of("status", 200, "data", Map.of("request_id",request_id,"reference_id",UUID.randomUUID().toString(),"date_time",date_time,"result_code",500,"message",e.getMessage()));
		}
	}

	public Map<String, Object> getTransDate(JsonNode request) {
		String request_id = request.path("body").path("request_id").textValue();
		Timestamp date_time = new Timestamp(new Date().getTime());
		try{

			StoredProcedureQuery q = entityManager.createNamedStoredProcedureQuery("getTransDate");
			List<Object[]> list=q.getResultList();

			ArrayNode documents = mapper.createArrayNode();
			for (Object item: list) {
				ObjectNode doc = mapper.createObjectNode();
				doc.put("date", item.toString());
				documents.add(doc);
			}

			return Map.of("status", 200, "data", Map.of("request_id",request_id,"reference_id",UUID.randomUUID().toString(),"date_time",date_time,"data",documents,"result_code",0));
		}
		catch (Exception e) {
			e.printStackTrace();
			return Map.of("status", 200, "data", Map.of("request_id",request_id,"reference_id",UUID.randomUUID().toString(),"date_time",date_time,"result_code",500,"message",e.getMessage()));
		}
	}

	@Transactional
	public Map<String, Object> syncData(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String request_id = request.path("body").path("request_id").textValue();
		Timestamp date_time = new Timestamp(new Date().getTime());
		try{
			Session session = entityManager.unwrap(Session.class);
			session.doWork(new Work() {
				@Override
				public void execute(Connection connection) throws SQLException {
					//connection, finally!
					String storeProc = String.format("CALL payoo.sp_ora_data_net_amount();");
					try (PreparedStatement stmt = connection.prepareStatement(storeProc)) {
						int resuk=stmt.executeUpdate();
						log.info("Found : " + resuk + "result");
					}

				}
			});
			responseModel.setRequest_id(request_id);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(date_time);
			responseModel.setResult_code(0);
		}
		catch (Exception e) {
			e.printStackTrace();
			responseModel.setRequest_id(request_id);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(date_time);
			responseModel.setResult_code(500);
			responseModel.setMessage(e.getMessage());
		}
		return Map.of("status", 200, "data", responseModel);
	}

	//---------------------- END FUNCTION IMPORT -----------------
	//---------------------- START FUNCTION SAVE REPORT -----------------
	public void saveReportReceiptPayment(RequestModel requestModel){
		FicoReceiptPayment ficoReceiptPayment = new FicoReceiptPayment();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		if (!requestModel.getData().getTransaction_id().isEmpty()){
			ficoReceiptPayment.setInstrumentReferenceNumber(requestModel.getData().getTransaction_id());
		}
		if (requestModel.getData().getTransaction_id().startsWith("PY")){
			ficoReceiptPayment.setSourceAccountNumber("45992855603");
		} else if (requestModel.getData().getTransaction_id().startsWith("MO")) {
			ficoReceiptPayment.setSourceAccountNumber("45992855306");
		}
		ficoReceiptPayment.setTenantId(505);
		ficoReceiptPayment.setBranchId(5);
		ficoReceiptPayment.setUserCode("system");
		ficoReceiptPayment.setReceiptPayOutMode("ELECTRONIC_FUND_TRANSFER");
		ficoReceiptPayment.setPaymentSubMode("INTERNAL_TRANSFER");
		ficoReceiptPayment.setReceiptAgainst("SINGLE_LOAN");
		ficoReceiptPayment.setLoanAccountNo(requestModel.getData().getLoan_account_no());
		ficoReceiptPayment.setTransactionCurrencyCode("VND");
		ficoReceiptPayment.setInstrumentDate(simpleDateFormat.format(requestModel.getData().getCreate_date()));
		ficoReceiptPayment.setTransactionValueDate(simpleDateFormat.format(requestModel.getData().getCreate_date()));
		ficoReceiptPayment.setReceiptOrPayoutAmount(requestModel.getData().getAmount());
		ficoReceiptPayment.setAutoAllocation("Y");
		ficoReceiptPayment.setReceiptNo("");
		ficoReceiptPayment.setReceiptPurpose("ANY_DUE");
		ficoReceiptPayment.setDepositDate(simpleDateFormat.format(requestModel.getData().getCreate_date()));
		ficoReceiptPayment.setDepositBankAccountNumber("519200003");
		ficoReceiptPayment.setRealizationDate(simpleDateFormat.format(requestModel.getData().getCreate_date()));
		ficoReceiptPayment.setReceiptTransactionStatus("C");
		ficoReceiptPayment.setProcessTillMaker("FALSE");
		ficoReceiptPayment.setRequestChannel("RECEIPT");
		ficoReceiptPaymentDAO.save(ficoReceiptPayment);
	}
	//---------------------- END FUNCTION SAVE REPORT -----------------
}