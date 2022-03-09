package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import vn.com.tpf.microservices.dao.FicoCustomerDAO;
import vn.com.tpf.microservices.dao.FicoImportPayooDAO;
import vn.com.tpf.microservices.dao.FicoTransPayDAO;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import vn.com.tpf.microservices.dao.*;
import vn.com.tpf.microservices.models.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.util.Properties;


@Service
//@EnableScheduling
@Transactional
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
	private FicoReceiptPaymentLogDAO ficoReceiptPaymentLogDAO;

	@Autowired
	private TransactionTemplate transactionTemplate;

	@PersistenceContext
	private EntityManager entityManager;

	@Value("${spring.hostRequest.url}")
	private String urlAPI;

	@Value("${spring.syncqueue.fromTime}")
	private String fromTime;

	@Value("${spring.syncqueue.toTime}")
	private String toTime;

	@Autowired
	private FicoTransPayQueueDAO ficoTransPayQueueDAO;

	@Autowired
	private FicoTransPaySettleDAO ficoTransPaySettleDAO;

	@SuppressWarnings("unchecked")

	@Transactional(readOnly = true)
	public Map<String, Object> getCustomers(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		String s="";
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
				//cộng phí
				FicoPartner ficoPartner = ficoPartnerDAO.findById(1);
				ficoCustomerList.forEach(a -> {
					if (a.getInstallmentAmount() > 0){
						a.setInstallmentAmount(a.getInstallmentAmount() + ficoPartner.getFee());
					}
					if (a.getNetAmount() > 0){
						a.setNetAmount(a.getNetAmount() + ficoPartner.getFee());
					}
				});

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

	@Transactional
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

//			Timestamp timestamp=new Timestamp(DateUtils.addMonths(new Date(),5).getTime());

			Timestamp timestamp=new Timestamp(new Date().getTime());

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

			//FicoTransPay getByTransactionId = ficoTransPayDAO.findByTransactionId(requestModel.getData().getTransaction_id());
            FicoTransPaySettle getByTransactionId = ficoTransPaySettleDAO.findByTransactionId(requestModel.getData().getTransaction_id());
			if (getByTransactionId == null) {
				FicoCustomer ficoLoanId = ficoCustomerDAO.findByLoanId(requestModel.getData().getLoan_id());
				List<FicoCustomer> ficoLoanAcct = ficoCustomerDAO.findByLoanAccountNo(requestModel.getData().getLoan_account_no());

				if (ficoLoanId != null && ficoLoanAcct.size() > 0){

					FicoPartner ficoPartner = ficoPartnerDAO.findById(1);
					//Trừ phí
					long amountNotFee = requestModel.getData().getAmount() - ficoPartner.getFee();

					ficoTransPay.setLoanId(requestModel.getData().getLoan_id());
					ficoTransPay.setLoanAccountNo(requestModel.getData().getLoan_account_no());
					ficoTransPay.setIdentificationNumber(requestModel.getData().getIdentification_number());
					ficoTransPay.setTransactionId(requestModel.getData().getTransaction_id());
					ficoTransPay.setAmount(amountNotFee);
					ficoTransPay.setCreateDate(new Timestamp(new Date().getTime()));

					//ficoTransPayDAO.save(ficoTransPay);

					//update save table settle
					FicoTransPaySettle ficoTransPaySettle=FicoTransPaySettle.builder()
														.amount(ficoTransPay.getAmount())
														.loanId(ficoTransPay.getLoanId())
														.loanAccountNo(ficoTransPay.getLoanAccountNo())
														.identificationNumber(ficoTransPay.getIdentificationNumber())
														.transactionId(ficoTransPay.getTransactionId())
														.createDate(ficoTransPay.getCreateDate())
														.transDate(new Date())
														.iscompleted(0)
														.flagsettle(1)
							.paymentFee(ficoPartner.getFee())
							.build();

					ficoTransPaySettleDAO.save(ficoTransPaySettle);

					//check thời gian EOD
					LocalTime now = LocalTime.now();
					LocalTime ninePM = LocalTime.parse(fromTime);
					LocalTime fourAM = LocalTime.parse(toTime);

					if(now.isBefore(ninePM)&&now.isAfter(fourAM))
					{
						//CALL API LMS
						new Thread(() -> {
							String errorMessage="";

							FicoRepaymentModel ficoRepaymentModel = new FicoRepaymentModel();
							ReceiptProcessingMO ficoReceiptPayment = new ReceiptProcessingMO();
							SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
							if (!requestModel.getData().getTransaction_id().isEmpty()){
								ficoReceiptPayment.setInstrumentReferenceNumber(requestModel.getData().getTransaction_id());
							}
							if (requestModel.getData().getTransaction_id().startsWith("PY")){
								ficoReceiptPayment.setSourceAccountNumber("45992855603");
								ficoReceiptPayment.setReceiptPayoutChannel("PAYOO");
							} else if (requestModel.getData().getTransaction_id().startsWith("MO")) {
								ficoReceiptPayment.setSourceAccountNumber("45992855306");
								ficoReceiptPayment.setReceiptPayoutChannel("MOMO");
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
							ficoReceiptPayment.setInstrumentDate(simpleDateFormat.format(new Date(timestamp.getTime())));
							ficoReceiptPayment.setTransactionValueDate(simpleDateFormat.format(new Date(timestamp.getTime())));
							ficoReceiptPayment.setReceiptOrPayoutAmount(ficoTransPaySettle.getAmount());
							ficoReceiptPayment.setAutoAllocation("Y");
							ficoReceiptPayment.setReceiptNo("");
							ficoReceiptPayment.setReceiptPurpose("ANY_DUE");
							ficoReceiptPayment.setDepositDate(simpleDateFormat.format(new Date(timestamp.getTime())));
							ficoReceiptPayment.setDepositBankAccountNumber("519200003");
							ficoReceiptPayment.setRealizationDate(simpleDateFormat.format(new Date(timestamp.getTime())));
							ficoReceiptPayment.setReceiptTransactionStatus("C");
							ficoReceiptPayment.setProcessTillMaker(false);
							ficoReceiptPayment.setRequestChannel("RECEIPT");
							ficoReceiptPayment.setRequestAt(date_time.toString());
							ficoRepaymentModel.setReceiptProcessingMO(ficoReceiptPayment);


							String urlReceiptLMS = urlAPI;
							URI uri = null;
							String response="";
							try {
								uri = new URI(urlAPI);


								RestTemplate restTemplate = new RestTemplate();
								MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
								headers.add("clientId", "1");
								headers.add("sign", "1");
								headers.add("Content-Type", "application/json");
								HttpEntity<FicoRepaymentModel> body = new HttpEntity<>(ficoRepaymentModel, headers);
								ResponseEntity<String> res = restTemplate.postForEntity(uri, body, String.class);

								response=res.getBody();

							} catch (Exception e) {
								errorMessage=e.getMessage();
								e.printStackTrace();
							}finally {
								//save report
								try {
									log.info("CALL API FIN1 - REQ:" + mapper.writeValueAsString(ficoRepaymentModel) +" - RES:" + response);

									saveReportReceiptPaymentLog(ficoRepaymentModel,response,timestamp,errorMessage);
								} catch (JsonProcessingException e) {
									log.info(e.toString());
								} catch (IOException e) {
									log.info(e.toString());
								} catch (ParseException e) {
									log.info(e.toString());
								}
							}
						}).start();
						//END CALL
					}
					else
					{//ghi table queue: status=0; chua upload, 1: upload
						ficoTransPayQueueDAO.save(FicoTransPayQueue.builder()
												.amount(ficoTransPay.getAmount())
												.loanId(ficoTransPay.getLoanId())
												.loanAccountNo(ficoTransPay.getLoanAccountNo())
												.identificationNumber(ficoTransPay.getIdentificationNumber())
												.transactionId(ficoTransPay.getTransactionId())
												.createDate(timestamp)
												.status(0)
												.build());
					}


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
					String storeProc = String.format("CALL payoo.sp_ora_data_net_amount_by_user();");
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

	@Transactional(readOnly = true)
	public Map<String, Object> getCustomers_vnPost(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		try{
			List<FicoCustomer> ficoCustomerList = null;

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
				responseModel.setResult_code(1);
				responseModel.setMessage("Input date_time is invalid format");
				return Map.of("status", 200, "data", responseModel);
			}

			String inputValue = requestModel.getData().getSearch_value();

			if(StringUtils.isEmpty(requestModel.getData().getSearch_value())
					|| requestModel.getData().getSearch_value().toUpperCase().contains("NULL")){
				responseModel.setRequest_id(requestModel.getRequest_id());
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(1);
				responseModel.setMessage("Input search_value is null, empty or contains 'NULL' ");
				return Map.of("status", 200, "data", responseModel);
			}

			if(isValidIdNumer(inputValue)) {
				ficoCustomerList = ficoCustomerDAO.findByIdentificationNumber(inputValue);
			}else{
				ficoCustomerList = ficoCustomerDAO.findByLoanAccountNo(inputValue);
			}
			if(ficoCustomerList.size() == 0) {
				responseModel.setRequest_id(requestModel.getRequest_id());
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(2);
				responseModel.setMessage("Customer does not exist");
			}else {
				FicoPartner ficoPartner = ficoPartnerDAO.findById(41);
				ficoCustomerList.forEach(ficoCustomer -> {
					if(!ficoCustomer.isLoanActive()){
						ficoCustomer.setNetAmount(0);
					}else if(ficoCustomer.getNetAmount() <= 0){
						//cộng phí
						long amount = ficoCustomer.getInstallmentAmount() > 0
								? ficoCustomer.getInstallmentAmount() + ficoPartner.getFee()
								: ficoCustomer.getInstallmentAmount();

						ficoCustomer.setInstallmentAmount(amount);
						ficoCustomer.setNetAmount(amount);
					} else{
						long amount = ficoCustomer.getInstallmentAmount() > 0
								? ficoCustomer.getInstallmentAmount() + ficoPartner.getFee()
								: ficoCustomer.getInstallmentAmount();

						ficoCustomer.setInstallmentAmount(amount);
						ficoCustomer.setNetAmount(ficoCustomer.getNetAmount() + ficoPartner.getFee());
					}
				});

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
			responseModel.setResult_code(96);
			responseModel.setMessage("System error");

			log.info("{}", e);
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> customers_pay_vnPost(JsonNode request) {
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		Timestamp date_time = new Timestamp(new Date().getTime());
		try{
			Assert.notNull(request.get("body"), "no body");
			RequestModel requestModel = mapper.treeToValue(request.get("body"), RequestModel.class);
			FicoTransPay ficoTransPay = new FicoTransPay();
			request_id = requestModel.getRequest_id();

			//update tren UAT
			//Timestamp timestamp=new Timestamp(DateUtils.addMonths(new Date(),4).getTime());
			Timestamp timestamp=new Timestamp(new Date().getTime());

			try{
				OffsetDateTime.parse(requestModel.getDate_time());
			}catch (Exception e) {
				log.info("Error: " + e);
				responseModel.setRequest_id(requestModel.getRequest_id());
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(1);
				responseModel.setMessage("Input date_time is invalid format");
				return Map.of("status", 200, "data", responseModel);
			}

			String errMsg = "";
			if(requestModel.getData() == null){
				errMsg = "Can not parse input data. ";
			}
			if (requestModel.getData().getAmount() <= 0){
				errMsg += "Input amount is equal or less than 0. ";
			}
			if(StringUtils.isEmpty(requestModel.getData().getTransaction_id())
					|| requestModel.getData().getTransaction_id().toUpperCase().contains("NULL")){
				errMsg += "Input transaction_id is null, empty or contains 'NULL'. ";
			}
			if(StringUtils.isEmpty(requestModel.getData().getIdentification_number())
					|| requestModel.getData().getIdentification_number().toUpperCase().contains("NULL")){
				errMsg += "Input identification_number is null, empty or contains 'NULL'. ";
			}
			if(StringUtils.isEmpty(requestModel.getData().getLoan_account_no())
					|| requestModel.getData().getLoan_account_no().toUpperCase().contains("NULL")){
				errMsg += "Input loan_account_no is null, empty or contains 'NULL'. ";
			}

			if(!StringUtils.isEmpty(errMsg)){
				responseModel.setRequest_id(requestModel.getRequest_id());
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(1);
				responseModel.setMessage(errMsg);
				return Map.of("status", 200, "data", responseModel);
			}

			FicoTransPay getByTransactionId = ficoTransPayDAO.findByTransactionId(requestModel.getData().getTransaction_id());
			if (getByTransactionId == null) {
				FicoCustomer ficoLoanId = ficoCustomerDAO.findByLoanId(requestModel.getData().getLoan_id());
				List<FicoCustomer> ficoLoanAcct = ficoCustomerDAO.findByLoanAccountNo(requestModel.getData().getLoan_account_no());

				if (ficoLoanId != null && ficoLoanAcct.size() > 0){
					if(ficoLoanId.getIdentificationNumber() != null && requestModel.getData().getIdentification_number() != null
							&& !ficoLoanId.getIdentificationNumber().equals(requestModel.getData().getIdentification_number())){
						responseModel.setRequest_id(requestModel.getRequest_id());
						responseModel.setReference_id(UUID.randomUUID().toString());
						responseModel.setDate_time(new Timestamp(new Date().getTime()));
						responseModel.setResult_code(2);
						responseModel.setMessage("Input identification_number does not match loan");
						return Map.of("status", 200, "data", responseModel);
					}

					//trừ phí
					FicoPartner ficoPartner = ficoPartnerDAO.findById(41);
					long amountNotFee = requestModel.getData().getAmount() - ficoPartner.getFee();

					ficoTransPay.setLoanId(requestModel.getData().getLoan_id());
					ficoTransPay.setLoanAccountNo(requestModel.getData().getLoan_account_no());
					ficoTransPay.setIdentificationNumber(requestModel.getData().getIdentification_number());
					ficoTransPay.setTransactionId(requestModel.getData().getTransaction_id());
					ficoTransPay.setAmount(amountNotFee);
					ficoTransPay.setCreateDate(new Timestamp(new Date().getTime()));

					//ficoTransPayDAO.save(ficoTransPay);


					//-------------- update save table settle - API FIN1 -------------------------
					FicoTransPaySettle ficoTransPaySettle=FicoTransPaySettle.builder()
							.amount(ficoTransPay.getAmount())
							.loanId(ficoTransPay.getLoanId())
							.loanAccountNo(ficoTransPay.getLoanAccountNo())
							.identificationNumber(ficoTransPay.getIdentificationNumber())
							.transactionId(ficoTransPay.getTransactionId())
							.createDate(ficoTransPay.getCreateDate())
							.transDate(new Date())
							.iscompleted(0)
							.flagsettle(1)
							.paymentFee(ficoPartner.getFee())
							.build();

					ficoTransPaySettleDAO.save(ficoTransPaySettle);

					//check thời gian EOD
					LocalTime now = LocalTime.now();
					LocalTime ninePM = LocalTime.parse(fromTime);
					LocalTime fourAM = LocalTime.parse(toTime);

					if(now.isBefore(ninePM)&&now.isAfter(fourAM))
					{
						//CALL API LMS
						new Thread(() -> {
							String errorMessage="";

							FicoRepaymentModel ficoRepaymentModel = new FicoRepaymentModel();
							ReceiptProcessingMO ficoReceiptPayment = new ReceiptProcessingMO();
							SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
							if (!requestModel.getData().getTransaction_id().isEmpty()){
								ficoReceiptPayment.setInstrumentReferenceNumber(requestModel.getData().getTransaction_id());
							}
							if (requestModel.getData().getTransaction_id().startsWith("PY")){
								ficoReceiptPayment.setSourceAccountNumber("45992855603");
								ficoReceiptPayment.setReceiptPayoutChannel("PAYOO");
							} else if (requestModel.getData().getTransaction_id().startsWith("MO")) {
								ficoReceiptPayment.setSourceAccountNumber("45992855306");
								ficoReceiptPayment.setReceiptPayoutChannel("MOMO");
							}else if (requestModel.getData().getTransaction_id().startsWith("VP")) {
								ficoReceiptPayment.setSourceAccountNumber("45992855108"); //update lai sau
								ficoReceiptPayment.setReceiptPayoutChannel("VNPOST"); //update lai sau
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
							ficoReceiptPayment.setInstrumentDate(simpleDateFormat.format(new Date(timestamp.getTime())));
							ficoReceiptPayment.setTransactionValueDate(simpleDateFormat.format(new Date(timestamp.getTime())));
							ficoReceiptPayment.setReceiptOrPayoutAmount(ficoTransPaySettle.getAmount());
							ficoReceiptPayment.setAutoAllocation("Y");
							ficoReceiptPayment.setReceiptNo("");
							ficoReceiptPayment.setReceiptPurpose("ANY_DUE");
							ficoReceiptPayment.setDepositDate(simpleDateFormat.format(new Date(timestamp.getTime())));
							ficoReceiptPayment.setDepositBankAccountNumber("519200003");
							ficoReceiptPayment.setRealizationDate(simpleDateFormat.format(new Date(timestamp.getTime())));
							ficoReceiptPayment.setReceiptTransactionStatus("C");
							ficoReceiptPayment.setProcessTillMaker(false);
							ficoReceiptPayment.setRequestChannel("RECEIPT");
                            ficoReceiptPayment.setRequestAt(date_time.toString());
							ficoRepaymentModel.setReceiptProcessingMO(ficoReceiptPayment);


							String urlReceiptLMS = urlAPI;
							URI uri = null;
							String response="";
							try {
								uri = new URI(urlAPI);


								RestTemplate restTemplate = new RestTemplate();
								MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
								headers.add("clientId", "1");
								headers.add("sign", "1");
								headers.add("Content-Type", "application/json");
								HttpEntity<FicoRepaymentModel> body = new HttpEntity<>(ficoRepaymentModel, headers);
								ResponseEntity<String> res = restTemplate.postForEntity(uri, body, String.class);

								response=res.getBody();

							} catch (Exception e) {
								errorMessage=e.getMessage();
								e.printStackTrace();
							}finally {
								//save report
								try {
									log.info("CALL API FIN1 - REQ:" + mapper.writeValueAsString(requestModel) +" - RES:" + response);

									saveReportReceiptPaymentLog(ficoRepaymentModel,response,timestamp,errorMessage);
								} catch (JsonProcessingException e) {
									log.info(e.toString());
								} catch (IOException e) {
									log.info(e.toString());
								} catch (ParseException e) {
									log.info(e.toString());
								}
							}
						}).start();
						//END CALL
					}
					else
					{//ghi table queue: status=0; chua upload, 1: upload
						ficoTransPayQueueDAO.save(FicoTransPayQueue.builder()
								.amount(ficoTransPay.getAmount())
								.loanId(ficoTransPay.getLoanId())
								.loanAccountNo(ficoTransPay.getLoanAccountNo())
								.identificationNumber(ficoTransPay.getIdentificationNumber())
								.transactionId(ficoTransPay.getTransactionId())
								.createDate(timestamp)
								.status(0)
								.build());
					}
					//---------------- END

					responseModel.setRequest_id(requestModel.getRequest_id());
					responseModel.setReference_id(UUID.randomUUID().toString());
					responseModel.setDate_time(new Timestamp(new Date().getTime()));
					responseModel.setResult_code(0);
				}else{
					responseModel.setRequest_id(request_id);
					responseModel.setReference_id(UUID.randomUUID().toString());
					responseModel.setDate_time(new Timestamp(new Date().getTime()));
					responseModel.setResult_code(2);
					responseModel.setMessage("Customer does not exist");
				}
			}else {
				responseModel.setRequest_id(request_id);
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(2);
				responseModel.setMessage("Transaction existed");
			}
		}
		catch (Exception e) {
			log.info("Error: " + e);
			responseModel.setRequest_id(request_id);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(96);
			responseModel.setMessage("System error");

			log.info("{}", e);
		}
		return Map.of("status", 200, "data", responseModel);
	}


	//---------------------- START FUNCTION SAVE REPORT -----------------
	public void saveReportReceiptPayment(RequestModel requestModel,Timestamp timestamp){
		FicoReceiptPayment ficoReceiptPayment = new FicoReceiptPayment();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		if (!requestModel.getData().getTransaction_id().isEmpty()){
			ficoReceiptPayment.setInstrumentReferenceNumber(requestModel.getData().getTransaction_id());
		}
		if (requestModel.getData().getTransaction_id().startsWith("PY")){
			ficoReceiptPayment.setSourceAccountNumber("45992855603");
			ficoReceiptPayment.setReceiptPayoutChannel("PAYOO");
		} else if (requestModel.getData().getTransaction_id().startsWith("MO")) {
			ficoReceiptPayment.setSourceAccountNumber("45992855306");
			ficoReceiptPayment.setReceiptPayoutChannel("MOMO");
		}
		ficoReceiptPayment.setTenantId(505);
		ficoReceiptPayment.setBranchId(5);
		ficoReceiptPayment.setUserCode("system");
		ficoReceiptPayment.setReceiptPayOutMode("ELECTRONIC_FUND_TRANSFER");
		ficoReceiptPayment.setPaymentSubMode("INTERNAL_TRANSFER");
		ficoReceiptPayment.setReceiptAgainst("SINGLE_LOAN");
		ficoReceiptPayment.setLoanAccountNo(requestModel.getData().getLoan_account_no());
		ficoReceiptPayment.setTransactionCurrencyCode("VND");
		ficoReceiptPayment.setInstrumentDate(simpleDateFormat.format(new Date(timestamp.getTime())));
		ficoReceiptPayment.setTransactionValueDate(simpleDateFormat.format(new Date(timestamp.getTime())));
		ficoReceiptPayment.setReceiptOrPayoutAmount(requestModel.getData().getAmount());
		ficoReceiptPayment.setAutoAllocation("Y");
		ficoReceiptPayment.setReceiptNo("");
		ficoReceiptPayment.setReceiptPurpose("ANY_DUE");
		ficoReceiptPayment.setDepositDate(simpleDateFormat.format(new Date(timestamp.getTime())));
		ficoReceiptPayment.setDepositBankAccountNumber("519200003");
		ficoReceiptPayment.setRealizationDate(simpleDateFormat.format(new Date(timestamp.getTime())));
		ficoReceiptPayment.setReceiptTransactionStatus("C");
		ficoReceiptPayment.setProcessTillMaker("FALSE");
		ficoReceiptPayment.setRequestChannel("RECEIPT");
		ficoReceiptPaymentDAO.save(ficoReceiptPayment);
	}

	public void saveReportReceiptPaymentLog(FicoRepaymentModel ficoRepaymentModel,String logResponse,Timestamp timestamp,String errorMesage) throws IOException, ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sssXXX");
		DateTimeFormatter parser = DateTimeFormatter.ofPattern("[yyyy-MM-dd'T'HH:mm:ss.SSSXXX][yyyy-MM-dd'T'HH:mm:ssXXX]");

		FicoReceiptPaymentLog ficoReceiptPaymentLog=null;
		String message="";
		try
		{
			if(!StringUtils.isEmpty(logResponse)) {
				JsonNode resNode = mapper.readTree(logResponse.replaceAll("\u00A0", ""));

				ficoReceiptPaymentLog=FicoReceiptPaymentLog.builder()
					.tenantId(ficoRepaymentModel.getReceiptProcessingMO().getRequestHeader().getTenantId())
					.userCode(ficoRepaymentModel.getReceiptProcessingMO().getRequestHeader().getUserDetail().getUserCode())
					.branchId(ficoRepaymentModel.getReceiptProcessingMO().getRequestHeader().getUserDetail().getBranchId())
					.receiptPayOutMode(ficoRepaymentModel.getReceiptProcessingMO().getReceiptPayOutMode())
					.receiptPayoutChannel(ficoRepaymentModel.getReceiptProcessingMO().getReceiptPayoutChannel())
					.receiptOrPayoutAmount(ficoRepaymentModel.getReceiptProcessingMO().getReceiptOrPayoutAmount())
					.receiptNo(ficoRepaymentModel.getReceiptProcessingMO().getReceiptNo())
					.paymentSubMode(ficoRepaymentModel.getReceiptProcessingMO().getPaymentSubMode())
					.receiptAgainst(ficoRepaymentModel.getReceiptProcessingMO().getReceiptAgainst())
					.transactionValueDate(timestamp)
					.receiptPurpose(ficoRepaymentModel.getReceiptProcessingMO().getReceiptPurpose())
					.transactionCurrencyCode(ficoRepaymentModel.getReceiptProcessingMO().getTransactionCurrencyCode())
					.loanAccountNo(ficoRepaymentModel.getReceiptProcessingMO().getLoanAccountNo())
					.autoAllocation(ficoRepaymentModel.getReceiptProcessingMO().getAutoAllocation())
					.receiptTransactionStatus(ficoRepaymentModel.getReceiptProcessingMO().getReceiptTransactionStatus())
					.receiptPayOutMode(ficoRepaymentModel.getReceiptProcessingMO().getReceiptPayOutMode())
					.depositBankAccountNumber(ficoRepaymentModel.getReceiptProcessingMO().getDepositBankAccountNumber())
					.sourceAccountNumber(ficoRepaymentModel.getReceiptProcessingMO().getSourceAccountNumber())
					.depositDate(timestamp)
					.realizationDate(timestamp)
					.bounceCancelReason(ficoRepaymentModel.getReceiptProcessingMO().getBounceCancelReason())
					.bankIdentificationCode(ficoRepaymentModel.getReceiptProcessingMO().getBankIdentificationCode())
					.bankIdentificationType(ficoRepaymentModel.getReceiptProcessingMO().getBankIdentificationType())
					.instrumentDate(timestamp)
					.requestChannel(ficoRepaymentModel.getReceiptProcessingMO().getRequestChannel())
					.instrumentReferenceNumber(ficoRepaymentModel.getReceiptProcessingMO().getInstrumentReferenceNumber())
					.processTillMaker(String.valueOf(ficoRepaymentModel.getReceiptProcessingMO().isProcessTillMaker()))
					.transactionId(resNode.path("transactionId").asText(""))
					.responseCode(resNode.path("responseCode").asText(""))
					.responseMessage(resNode.path("responseMessage").asText(""))
					//.responseDate( new Timestamp((sdf.parse(resNode.path("responseDate").asText("")).getTime())))
						.responseDate(Timestamp.valueOf(LocalDateTime.parse(resNode.path("responseDate").asText(""),parser)))
					.logResponse(logResponse.replaceAll("\u00A0", ""))
					.build();

				if(!resNode.path("responseData").isNull())
				{

					ficoReceiptPaymentLog.setPayinSlipReferencNumber(resNode.path("responseData").path("success").path("payinSlipReferencNumber").asText(""));
					ficoReceiptPaymentLog.setReceiptId(resNode.path("responseData").path("success").path("receiptId").asText(""));
					ficoReceiptPaymentLog.setI18nCode(resNode.path("responseData").hasNonNull("success")?resNode.path("responseData").path("success").path("message").path("i18nCode").asText("")
								:resNode.path("responseData").path("error").get(0).path("i18nCode").asText(""));
					ficoReceiptPaymentLog.setType(resNode.path("responseData").hasNonNull("success")? resNode.path("responseData").path("success").path("message").path("type").asText("")
								:resNode.path("responseData").path("error").get(0).path("type").asText(""));
					ficoReceiptPaymentLog.setValue(resNode.path("responseData").hasNonNull("success")?resNode.path("responseData").path("success").path("message").path("value").asText("")
								:resNode.path("responseData").path("error").get(0).path("value").asText(""));
					ficoReceiptPaymentLog.setMessageArguments(resNode.path("responseData").hasNonNull("success")? mapper.writeValueAsString(resNode.path("responseData").path("success").path("message"))
								:mapper.writeValueAsString(resNode.path("responseData").path("error").get(0).path("messageArguments")));

					//if call api success, call function update data
					if(resNode.path("responseData").hasNonNull("success"))
					{
						String resultF1=F1_Fn_get_loan_netamount(ficoRepaymentModel.getReceiptProcessingMO().getLoanAccountNo());
						message +="resultF1: " + resultF1;

						int resultCallUpdate = updateDataLoanFromF1(resultF1,ficoRepaymentModel.getReceiptProcessingMO().getLoanAccountNo());
						message +="resultCallUpdate: " + resultCallUpdate;
						ficoReceiptPaymentLog.setLogCallUpdate(String.valueOf(resultCallUpdate));
					}

				}
			}
			else
			{
				ficoReceiptPaymentLog=FicoReceiptPaymentLog.builder()
					.tenantId(ficoRepaymentModel.getReceiptProcessingMO().getRequestHeader().getTenantId())
					.userCode(ficoRepaymentModel.getReceiptProcessingMO().getRequestHeader().getUserDetail().getUserCode())
					.branchId(ficoRepaymentModel.getReceiptProcessingMO().getRequestHeader().getUserDetail().getBranchId())
					.receiptPayOutMode(ficoRepaymentModel.getReceiptProcessingMO().getReceiptPayOutMode())
					.receiptPayoutChannel(ficoRepaymentModel.getReceiptProcessingMO().getReceiptPayoutChannel())
					.receiptOrPayoutAmount(ficoRepaymentModel.getReceiptProcessingMO().getReceiptOrPayoutAmount())
					.receiptNo(ficoRepaymentModel.getReceiptProcessingMO().getReceiptNo())
					.paymentSubMode(ficoRepaymentModel.getReceiptProcessingMO().getPaymentSubMode())
					.receiptAgainst(ficoRepaymentModel.getReceiptProcessingMO().getReceiptAgainst())
					.transactionValueDate(timestamp)
					.receiptPurpose(ficoRepaymentModel.getReceiptProcessingMO().getReceiptPurpose())
					.transactionCurrencyCode(ficoRepaymentModel.getReceiptProcessingMO().getTransactionCurrencyCode())
					.loanAccountNo(ficoRepaymentModel.getReceiptProcessingMO().getLoanAccountNo())
					.autoAllocation(ficoRepaymentModel.getReceiptProcessingMO().getAutoAllocation())
					.receiptTransactionStatus(ficoRepaymentModel.getReceiptProcessingMO().getReceiptTransactionStatus())
					.receiptPayOutMode(ficoRepaymentModel.getReceiptProcessingMO().getReceiptPayOutMode())
					.depositBankAccountNumber(ficoRepaymentModel.getReceiptProcessingMO().getDepositBankAccountNumber())
					.depositDate(timestamp)
					.sourceAccountNumber(ficoRepaymentModel.getReceiptProcessingMO().getSourceAccountNumber())
					.realizationDate(timestamp)
					.bounceCancelReason(ficoRepaymentModel.getReceiptProcessingMO().getBounceCancelReason())
					.bankIdentificationCode(ficoRepaymentModel.getReceiptProcessingMO().getBankIdentificationCode())
					.bankIdentificationType(ficoRepaymentModel.getReceiptProcessingMO().getBankIdentificationType())
					.instrumentDate(timestamp)
					.requestChannel(ficoRepaymentModel.getReceiptProcessingMO().getRequestChannel())
					.instrumentReferenceNumber(ficoRepaymentModel.getReceiptProcessingMO().getInstrumentReferenceNumber())
					.processTillMaker(String.valueOf(ficoRepaymentModel.getReceiptProcessingMO().isProcessTillMaker()))
					.responseCode("500")
					.value(errorMesage.equals("")?"System error":errorMesage)
					.type("ERROR")
					.build();

			}
		}catch (Exception e)
		{
			message+="; Error: " + e.toString();
		}
		finally{
			ficoReceiptPaymentLogDAO.save(ficoReceiptPaymentLog);
			log.info("SaveLog: " + message);
		}
	}

	/**
	 * update flag_settle = 1 khi goi api receipt thanh cong
	 * @param instrumentReferenceNumber
	 * @return
	 */
	private boolean updateFlagSettle(String instrumentReferenceNumber) {
		try {
			SqlParameterSource namedParameters = new MapSqlParameterSource()
					.addValue("transactionid",instrumentReferenceNumber);
			String sql="Update payoo.fico_trans_pay_settle " +
					"set flag_settle = 1 " +
					"where flag_settle = 0 and transaction_id =:transactionid";

			int resultData = namedParameterJdbcTemplatePosgres.update(sql,namedParameters);
			log.info("updateFlagSettle: " + resultData);
			return true;
		}catch (Exception e){
			log.error("updateFlagSettle: " + e.toString());
			return false;
		}

	}
	//---------------------- END FUNCTION SAVE REPORT -----------------


	//----------------------- START CRON --------------------------

	@Value("${spring.ftp.server}")
	private String ftpServer;
	@Value("${spring.ftp.port}")
	private int ftpPort;
	@Value("${spring.ftp.username}")
	private String ftpUser;
	@Value("${spring.ftp.password}")
	private String ftpPass;
	@Value("${spring.ftp.folder}")
	private String folderFtp;

	@Autowired
	private FicoCronLogDAO ficoCronLogDAO;

	private boolean connectFTP(FTPClient ftpClient) {
		boolean success = false;

		try {
			ftpClient.connect(ftpServer,ftpPort);
			showServerReply(ftpClient);
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				System.out.println("Operation failed. Server reply code: " + replyCode);
				return success;
			}
			success = ftpClient.login(ftpUser, ftpPass);
			showServerReply(ftpClient);
		} catch (IOException ex) {
			System.out.println("Oops! Something wrong happened");
			ex.printStackTrace();
		}
		return success;
	}

//	@Scheduled(cron = "${spring.ftp.cronconfig}")
	@Transactional
	public Map<String, Object> getCron() throws IOException, MessagingException {
		ResponseModel responseModel = new ResponseModel();
		String message="";
		try {
//
//		FTPClient ftpClient = new FTPClient();
//		boolean success = connectFTP(ftpClient);
//		if (!success) {
//			System.out.println("Could not login to the server");
//		} else {
//			System.out.println("LOGGED IN SERVER");
////			String[] filesFTP = ftpClient.listNames();
////			for (String file : filesFTP) {
////				System.out.println("File:  " +  file);
////			}
//
////			Arrays.sort(ftpClient.listFiles(),
////					Comparator.comparing((FTPFile remoteFile) -> remoteFile.getTimestamp()).reversed());
//
//			FTPFile[] ftpFiles = ftpClient.listFiles();
//
//			//File folder=new File("D:\\testftp");

			File dir = new File(folderFtp);
			File[] files = dir.listFiles();

			if(files.length > 0) {

				File lastModified = Arrays.stream(files).filter(File::isDirectory).max(Comparator.comparing(File::lastModified)).orElse(null);
				message += "last folder:" + lastModified;

				if (lastModified != null) {
					//FTPFile file = lastFileModified(ftpFiles);

					String folderName = lastModified.getName();

					//check file ton tai hay chua
					List<FicoCronLogModel> ficoCronLogModelList = ficoCronLogDAO.findByFileName(folderName);

					if (ficoCronLogModelList.size() > 0) {
						message+="; File:  " + folderName + " exist " + ficoCronLogModelList.get(0).getCreatedDate();

						responseModel.setRequest_id(UUID.randomUUID().toString());
						responseModel.setMessage("File:  " + folderName + " exist " + ficoCronLogModelList.get(0).getCreatedDate());
						responseModel.setReference_id(UUID.randomUUID().toString());
						responseModel.setDate_time(new Timestamp(new Date().getTime()));
						responseModel.setResult_code(0);

						return Map.of("status", 200, "data", responseModel);
					}

					//ghi log
					FicoCronLogModel ficoCronLogModel = FicoCronLogModel.builder()
							.fileName(folderName)
							.createdDate(new Date())
							.fileCreatedDate(new Date(lastModified.lastModified()))
							.build();
					ficoCronLogDAO.save(ficoCronLogModel);

					//call proc sync
					Session session = entityManager.unwrap(Session.class);
					session.doWork(new Work() {
						@Override
						public void execute(Connection connection) throws SQLException {
							//connection, finally!
							String storeProc = String.format("CALL payoo.sp_ora_data_net_amount_by_user();");
							try (PreparedStatement stmt = connection.prepareStatement(storeProc)) {
								int resuk = stmt.executeUpdate();
								log.info("cronjob syncdata: Found : " + resuk + "result");
							}

						}
					});

					//send mail
					//sendMail();

					//return
					responseModel.setRequest_id(UUID.randomUUID().toString());
					responseModel.setReference_id(UUID.randomUUID().toString());
					responseModel.setMessage("File:  " + folderName + " complete at " + new Date(lastModified.lastModified()));
					responseModel.setDate_time(new Timestamp(new Date().getTime()));
					responseModel.setResult_code(0);

					message +="; File:  " + folderName + " complete at " + new Date(lastModified.lastModified());

					return Map.of("status", 200, "data", responseModel);
				}
			}

			//}
			responseModel.setRequest_id(UUID.randomUUID().toString());
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(0);

			System.out.println("job : " + new Date());
			return Map.of("status", 200, "data", responseModel);
		} catch (Exception e) {
			message +="; ERROR: " + e.toString();
			return Map.of("status", 500, "data", e.toString());
		}
		finally{
			log.info("GetCron - sync after FCC: " + message);
		}
	}

	public static FTPFile lastFileModified(FTPFile[] files) {
		Date lastMod = files[0].getTimestamp().getTime();
		FTPFile choice = files[0];
		for (FTPFile file : files) {
			if (file.getTimestamp().getTime().after(lastMod)) {
				choice = file;
				lastMod = file.getTimestamp().getTime();
			}
		}
		return choice;
	}

	private void sendMail() throws MessagingException {

		Properties prop = new Properties();
		prop.put("mail.smtp.auth", true);
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.host", "smtp.mailtrap.io");
		prop.put("mail.smtp.port", "25");
		prop.put("mail.smtp.ssl.trust", "smtp.mailtrap.io");


		javax.mail.Session session = javax.mail.Session.getInstance(prop, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("265d55fad40447", "2e3ac0f543683e");
			}
		});

		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress("hiepln512@gmail.com"));
		message.setRecipients(
				Message.RecipientType.TO, InternetAddress.parse("nghiahiep512@gmail.com"));
		message.setSubject("Mail Subject");

		String msg = "This is my first email using JavaMailer";

		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		mimeBodyPart.setContent(msg, "text/html");

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(mimeBodyPart);

		message.setContent(multipart);

		Transport.send(message);
	}

	private void showServerReply(FTPClient ftpClient) {
		String[] replies = ftpClient.getReplyStrings();
		if (replies != null && replies.length > 0) {
			for (String aReply : replies) {
				System.out.println("SERVER: " + aReply);
			}
		}
	}

//	@Scheduled(cron = "${spring.syncqueue.cronconfig}")
	public Map<String, Object> syncDataInQueue()
	{
		String message="";
		Timestamp date_time = new Timestamp(new Date().getTime());
		try{
			StoredProcedureQuery q = entityManager.createNamedStoredProcedureQuery("getlistqueue");
			q.setParameter(1, 0);
			List<FicoTransPayQueue> list=q.getResultList();

			if(list!=null && list.size()>0)
			{
				callFin1API(list);
			}

			return Map.of("status", 200, "data", Map.of("request_id","","reference_id",UUID.randomUUID().toString(),"date_time",date_time,"data",list,"result_code",0));
		}
		catch (Exception e) {
			e.printStackTrace();
			return Map.of("status", 200, "data", Map.of("request_id","","reference_id",UUID.randomUUID().toString(),"date_time",date_time,"result_code",500,"message",e.getMessage()));
		}
	}

	//----------------------- END CRON ----------------------------

	// --------------------- CALL FUNCTION F1 --------------------------
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplateF1;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplatePosgres;

	public String F1_Fn_get_loan_netamount(String loanAccount) {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("loanAccount", loanAccount);
		String sql="select serviceapp.fn_get_loan_netamount(:loanAccount) as RESULT from dual";

		Map<String, Object> resultData =namedParameterJdbcTemplateF1.queryForMap(sql,namedParameters);

		if(resultData!=null && resultData.size()>0 && resultData.get("RESULT")!=null)
		{
			return resultData.get("RESULT").toString();
		}

		return "";
	}

	public int updateDataLoanFromF1(String result, String loanAccount) {

		if (StringUtils.isEmpty(result)) {
			return 0;
		}

		DateTimeFormatter formatterParseInput = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd-MMM-yyyy").toFormatter(Locale.ENGLISH);

		String[] listParam = result.split("\\|");


		if (listParam[7].equals("C") || StringUtils.isEmpty(listParam[6])) {
			SqlParameterSource namedParameters = new MapSqlParameterSource().addValues(Map.of("netamount", Integer.parseInt(listParam[4]),
					"installmentamount", Integer.parseInt(listParam[5]),
					"syncdate", new Timestamp(new Date().getTime()),
					"loanstatus", listParam[7],
					"loanAccount", loanAccount));
			String sql = "Update payoo.TEMP_SOURCE_ORA_NETAMOUNT " +
					"set net_amount=:netamount, installment_amount=:installmentamount, syncdate=:syncdate, loan_status=:loanstatus " +
					"where loan_account_no=:loanAccount";

			int resultData = namedParameterJdbcTemplatePosgres.update(sql, namedParameters);

			return resultData;
		}

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValues(Map.of("netamount", Integer.parseInt(listParam[4]),
				"installmentamount", Integer.parseInt(listParam[5]),
				"duedate", LocalDate.parse(listParam[6], formatterParseInput),
				"syncdate", new Timestamp(new Date().getTime()),
				"loanstatus", listParam[7],
				"loanAccount", loanAccount));
		String sql = "Update payoo.TEMP_SOURCE_ORA_NETAMOUNT " +
				"set net_amount=:netamount, installment_amount=:installmentamount, duedate=:duedate, syncdate=:syncdate, loan_status=:loanstatus " +
				"where loan_account_no=:loanAccount";

		int resultData = namedParameterJdbcTemplatePosgres.update(sql, namedParameters);

		return resultData;

	}

	// --------------------- END ---------------------------------------

	// ---------------------- API FIN1 ---------------------------------
	public int updateSyncData(long id){

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValues(Map.of("id", id));
		String sql="Update payoo.fico_trans_pay_queue " +
				"set status=1 " +
				"where id=:id";

		int resultData =namedParameterJdbcTemplatePosgres.update(sql,namedParameters);

		return resultData;
	}

	public void callFin1API(List<FicoTransPayQueue> ficoTransPayQueueList) {
		for (FicoTransPayQueue fico: ficoTransPayQueueList) {

			updateSyncData(fico.getId());

			String errorMessage="";

			FicoRepaymentModel ficoRepaymentModel = new FicoRepaymentModel();
			ReceiptProcessingMO ficoReceiptPayment = new ReceiptProcessingMO();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			if (!fico.getTransactionId().isEmpty()){
				ficoReceiptPayment.setInstrumentReferenceNumber(fico.getTransactionId());
			}

			//check partnerid
			int partnerId=fico.getPartnerId();
			FicoPartner ficoPartner = ficoPartnerDAO.findById(partnerId);

			if(ficoPartner != null){
				ficoReceiptPayment.setSourceAccountNumber(ficoPartner.getSourceAccountNumber());
				ficoReceiptPayment.setReceiptPayoutChannel(ficoPartner.getPartnerName());
			}
			//end check partner

			if (fico.getTransactionId().startsWith("PY")){
				ficoReceiptPayment.setSourceAccountNumber("45992855603");
				ficoReceiptPayment.setReceiptPayoutChannel("PAYOO");
			} else if (fico.getTransactionId().startsWith("MO")) {
				ficoReceiptPayment.setSourceAccountNumber("45992855306");
				ficoReceiptPayment.setReceiptPayoutChannel("MOMO");
			}else if (fico.getTransactionId().startsWith("VP")) {
				ficoReceiptPayment.setSourceAccountNumber("45992855108"); //update lai sau
				ficoReceiptPayment.setReceiptPayoutChannel("VNPOST"); //update lai sau
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
			ficoReceiptPayment.setLoanAccountNo(fico.getLoanAccountNo());
			ficoReceiptPayment.setTransactionCurrencyCode("VND");
			ficoReceiptPayment.setInstrumentDate(simpleDateFormat.format(fico.getCreateDate()));
			ficoReceiptPayment.setTransactionValueDate(simpleDateFormat.format(fico.getCreateDate()));
			ficoReceiptPayment.setReceiptOrPayoutAmount(fico.getAmount());
			ficoReceiptPayment.setAutoAllocation("Y");
			ficoReceiptPayment.setReceiptNo("");
			ficoReceiptPayment.setReceiptPurpose("ANY_DUE");
			ficoReceiptPayment.setDepositDate(simpleDateFormat.format(fico.getCreateDate()));
			ficoReceiptPayment.setDepositBankAccountNumber("519200003");
			ficoReceiptPayment.setRealizationDate(simpleDateFormat.format(fico.getCreateDate()));
			ficoReceiptPayment.setReceiptTransactionStatus("C");
			ficoReceiptPayment.setProcessTillMaker(false);
			ficoReceiptPayment.setRequestChannel("RECEIPT");
            ficoReceiptPayment.setRequestAt(fico.getCreateDate().toString());
			ficoRepaymentModel.setReceiptProcessingMO(ficoReceiptPayment);


			String urlReceiptLMS = urlAPI;
			URI uri = null;
			String response="";
			try {
				uri = new URI(urlAPI);

				SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
				simpleClientHttpRequestFactory.setConnectTimeout(30_000);
				simpleClientHttpRequestFactory.setReadTimeout(30_000);
				ClientHttpRequestFactory requestFactory = new BufferingClientHttpRequestFactory(simpleClientHttpRequestFactory);

				RestTemplate restTemplate = new RestTemplate(requestFactory);
				MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
				headers.add("clientId", "1");
				headers.add("sign", "1");
				headers.add("Content-Type", "application/json");
				HttpEntity<FicoRepaymentModel> body = new HttpEntity<>(ficoRepaymentModel, headers);
				ResponseEntity<String> res = restTemplate.postForEntity(uri, body, String.class);

				response=res.getBody();

			} catch (Exception e) {
				errorMessage=e.getMessage();
				e.printStackTrace();
			}finally {
				//save report
				try {
					log.info("REQ:" + mapper.writeValueAsString(fico) +" - RES:" + response);

					saveReportReceiptPaymentLog(ficoRepaymentModel,response,fico.getCreateDate(),errorMessage);
				} catch (JsonProcessingException e) {
					log.info(e.toString());
				} catch (IOException e) {
					log.info(e.toString());
				} catch (ParseException e) {
					log.info(e.toString());
				}
			}
		}
	}
	// ---------------------- END --------------------------------------

	// ----------------------- call retry --------------------------------

	//@Scheduled(cron = "${spring.syncqueue.cronretry}")
	public String autoRetry()
	{
		String message="";
		Timestamp date_time = new Timestamp(new Date().getTime());
		try{
//			StoredProcedureQuery q = entityManager.createNamedStoredProcedureQuery("getlistretry");
//			q.setParameter(1, 0);
//			List<FicoReceiptPaymentLog> list=q.getResultList();
			List<FicoReceiptPaymentLog> list = ficoReceiptPaymentLogDAO.getListRetry(0);

			if(list!=null && list.size()>0)
			{
				callFin1API_retry(list);

				System.out.println("autoRetry:" + list.size() +"- OK" );
			}


			return "OK";
		}
		catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
	}

	public int updateTranRetry(long paymentLogId,String transactionId){

		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("paymentlogid", paymentLogId)
				.addValue("transactionid",transactionId);
		String sql="Update payoo.fico_trans_pay_auto_retry " +
				"set status=1 " +
				"where payment_log_id=:paymentlogid and transaction_id=:transactionid";

		int resultData =namedParameterJdbcTemplatePosgres.update(sql,namedParameters);

		return resultData;
	}

	public void callFin1API_retry(List<FicoReceiptPaymentLog> ficoReceiptPaymentLogList) {
		for (FicoReceiptPaymentLog fico: ficoReceiptPaymentLogList) {

			updateTranRetry(fico.getId(), fico.getInstrumentReferenceNumber());

			String errorMessage="";

			FicoRepaymentModel ficoRepaymentModel = new FicoRepaymentModel();
			ReceiptProcessingMO ficoReceiptPayment = new ReceiptProcessingMO();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			if (!fico.getInstrumentReferenceNumber().isEmpty()){
				ficoReceiptPayment.setInstrumentReferenceNumber(fico.getInstrumentReferenceNumber());
			}

			ficoReceiptPayment.setSourceAccountNumber(fico.getSourceAccountNumber());
			ficoReceiptPayment.setReceiptPayoutChannel(fico.getReceiptPayoutChannel());

			if (fico.getInstrumentReferenceNumber().startsWith("PY")){
				ficoReceiptPayment.setSourceAccountNumber("45992855603");
				ficoReceiptPayment.setReceiptPayoutChannel("PAYOO");
			} else if (fico.getInstrumentReferenceNumber().startsWith("MO")) {
				ficoReceiptPayment.setSourceAccountNumber("45992855306");
				ficoReceiptPayment.setReceiptPayoutChannel("MOMO");
			}else if (fico.getInstrumentReferenceNumber().startsWith("VP")) {
				ficoReceiptPayment.setSourceAccountNumber("45992855108"); //update lai sau
				ficoReceiptPayment.setReceiptPayoutChannel("VNPOST"); //update lai sau
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
			ficoReceiptPayment.setLoanAccountNo(fico.getLoanAccountNo());
			ficoReceiptPayment.setTransactionCurrencyCode("VND");
			ficoReceiptPayment.setInstrumentDate(simpleDateFormat.format(fico.getInstrumentDate()));
			ficoReceiptPayment.setTransactionValueDate(simpleDateFormat.format(fico.getInstrumentDate()));
			ficoReceiptPayment.setReceiptOrPayoutAmount(fico.getReceiptOrPayoutAmount());
			ficoReceiptPayment.setAutoAllocation("Y");
			ficoReceiptPayment.setReceiptNo("");
			ficoReceiptPayment.setReceiptPurpose("ANY_DUE");
			ficoReceiptPayment.setDepositDate(simpleDateFormat.format(fico.getInstrumentDate()));
			ficoReceiptPayment.setDepositBankAccountNumber("519200003");
			ficoReceiptPayment.setRealizationDate(simpleDateFormat.format(fico.getInstrumentDate()));
			ficoReceiptPayment.setReceiptTransactionStatus("C");
			ficoReceiptPayment.setProcessTillMaker(false);
			ficoReceiptPayment.setRequestChannel("RECEIPT");
			ficoReceiptPayment.setRequestAt(fico.getInstrumentDate().toString());
			ficoRepaymentModel.setReceiptProcessingMO(ficoReceiptPayment);


			String urlReceiptLMS = urlAPI;
			URI uri = null;
			String response="";
			try {
				uri = new URI(urlAPI);

				SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
				simpleClientHttpRequestFactory.setConnectTimeout(30_000);
				simpleClientHttpRequestFactory.setReadTimeout(30_000);
				ClientHttpRequestFactory requestFactory = new BufferingClientHttpRequestFactory(simpleClientHttpRequestFactory);

				RestTemplate restTemplate = new RestTemplate(requestFactory);

				MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
				headers.add("clientId", "1");
				headers.add("sign", "1");
				headers.add("Content-Type", "application/json");
				HttpEntity<FicoRepaymentModel> body = new HttpEntity<>(ficoRepaymentModel, headers);
				ResponseEntity<String> res = restTemplate.postForEntity(uri, body, String.class);

				response=res.getBody();

			} catch (Exception e) {
				errorMessage=e.getMessage();
				e.printStackTrace();
			}finally {
				//save report
				try {
					System.out.println("REQ:" + mapper.writeValueAsString(fico) +" - RES:" + response);

					saveReportReceiptPaymentLog(ficoRepaymentModel,response,fico.getInstrumentDate(),errorMessage);
				} catch (JsonProcessingException e) {
					System.out.println(e.toString());
				} catch (IOException e) {
					System.out.println(e.toString());
				} catch (ParseException e) {
					System.out.println(e.toString());
				}
			}
		}
	}
	// ----------------------- end ---------------------------------------

	@Autowired
	private FicoPartnerDAO ficoPartnerDAO;

	//region new api
	@Transactional(readOnly = true)
	public Map<String, Object> repayment_get(JsonNode request) throws JsonProcessingException {
		String sLogs= "request: " + mapper.writeValueAsString(request);
		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		try{
			List<FicoCustomer> ficoCustomerList = null;

			Assert.notNull(request.get("body"), "no body");
			RequestModel requestModel = mapper.treeToValue(request.get("body"), RequestModel.class);
			request_id = requestModel.getRequest_id();

			//check partnerid
			int partnerId=requestModel.getData().getPartner_id()!=null?requestModel.getData().getPartner_id():0;
			FicoPartner ficoPartner=ficoPartnerDAO.findById(partnerId);

			if(ficoPartner  == null)
			{
				responseModel.setRequest_id(requestModel.getRequest_id());
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(2);
				responseModel.setMessage("Partner invalid");
				return Map.of("status", 200, "data", responseModel);
			}
			//end check partner

			OffsetDateTime.parse(requestModel.getDate_time());
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
				//cộng phí
				ficoCustomerList.forEach(a -> {
					if (a.getInstallmentAmount() > 0){
						a.setInstallmentAmount(a.getInstallmentAmount() + ficoPartner.getFee());
					}
					if (a.getNetAmount() > 0){
						a.setNetAmount(a.getNetAmount() + ficoPartner.getFee());
					}
				});
				responseModel.setRequest_id(requestModel.getRequest_id());
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(0);
				responseModel.setData(ficoCustomerList);
			}
		}
		catch (Exception e) {
			sLogs+="; Error: " + e;
			responseModel.setRequest_id(request_id);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(500);
			responseModel.setMessage("Others error");
		}
		finally {
			log.info("{}", sLogs);
		}
		return Map.of("status", 200, "data", responseModel);
	}

	public Map<String, Object> repayment_pay(JsonNode request) throws JsonProcessingException {
		String sLogs= "request: " + mapper.writeValueAsString(request);

		ResponseModel responseModel = new ResponseModel();
		String request_id = null;
		Timestamp date_time = new Timestamp(new Date().getTime());

		try{
			Assert.notNull(request.get("body"), "no body");
			RequestModel requestModel = mapper.treeToValue(request.get("body"), RequestModel.class);
			FicoTransPay ficoTransPay = new FicoTransPay();
			request_id = requestModel.getRequest_id();
//			date_time = requestModel.getDate_time();

//			Timestamp timestamp=new Timestamp(DateUtils.addMonths(new Date(),5).getTime());

            Timestamp timestamp=new Timestamp(new Date().getTime());

			OffsetDateTime.parse(requestModel.getDate_time());

			//check partnerid
			int partnerId=requestModel.getData().getPartner_id()!=null?requestModel.getData().getPartner_id():0;
			FicoPartner ficoPartner=ficoPartnerDAO.findById(partnerId);

			if(ficoPartner  ==null)
			{
				responseModel.setRequest_id(requestModel.getRequest_id());
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(2);
				responseModel.setMessage("Partner invalid");
				return Map.of("status", 200, "data", responseModel);
			}
			//end check partner


			if (requestModel.getData().getAmount() <= 0){
				responseModel.setRequest_id(requestModel.getRequest_id());
				responseModel.setReference_id(UUID.randomUUID().toString());
				responseModel.setDate_time(new Timestamp(new Date().getTime()));
				responseModel.setResult_code(500);
				responseModel.setMessage("Others error");
				return Map.of("status", 200, "data", responseModel);
			}

			//FicoTransPay getByTransactionId = ficoTransPayDAO.findByTransactionId(requestModel.getData().getTransaction_id());

			String transactionId= ficoPartner.getPrefix() + requestModel.getData().getTransaction_id();
			FicoTransPaySettle getByTransactionId = ficoTransPaySettleDAO.findByTransactionId(transactionId);

			if (getByTransactionId == null) {
				FicoCustomer ficoLoanId = ficoCustomerDAO.findByLoanId(requestModel.getData().getLoan_id());
				List<FicoCustomer> ficoLoanAcct = ficoCustomerDAO.findByLoanAccountNo(requestModel.getData().getLoan_account_no());

				if (ficoLoanId != null && ficoLoanAcct.size() > 0){

					//Trừ phí
					long amountNotFee = requestModel.getData().getAmount() - ficoPartner.getFee();

                    ficoTransPay.setLoanId(requestModel.getData().getLoan_id());
                    ficoTransPay.setLoanAccountNo(ficoLoanId.getLoanAccountNo());
                    ficoTransPay.setIdentificationNumber(ficoLoanId.getIdentificationNumber());
                    ficoTransPay.setTransactionId(transactionId);
                    ficoTransPay.setAmount(amountNotFee);
                    ficoTransPay.setCreateDate(new Timestamp(new Date().getTime()));

					//ficoTransPayDAO.save(ficoTransPay);

					//update save table settle: partner, flagclosurem,device
					FicoTransPaySettle ficoTransPaySettle=FicoTransPaySettle.builder()
							.amount(ficoTransPay.getAmount())
							.loanId(ficoTransPay.getLoanId())
							.loanAccountNo(ficoTransPay.getLoanAccountNo())
							.identificationNumber(ficoTransPay.getIdentificationNumber())
							.transactionId(ficoTransPay.getTransactionId())
							.createDate(ficoTransPay.getCreateDate())
							.transDate(new Date())
							.iscompleted(0)
							.flagsettle(1)
							.partnerid(partnerId)
							.flagclosure(requestModel.getData().getFlag_closure()!=null?requestModel.getData().getFlag_closure():0)
							.device(requestModel.getData().getDevice()!=null?requestModel.getData().getDevice():"")
							.paymentFee(ficoPartner.getFee())
							.build();
					ficoTransPaySettleDAO.save(ficoTransPaySettle);

					//check thời gian EOD
					LocalTime now = LocalTime.now();
					LocalTime ninePM = LocalTime.parse(fromTime);
					LocalTime fourAM = LocalTime.parse(toTime);

					if(now.isBefore(ninePM)&&now.isAfter(fourAM))
					{
						//CALL API LMS
						new Thread(() -> {
							String errorMessage="";

							FicoRepaymentModel ficoRepaymentModel = new FicoRepaymentModel();
							ReceiptProcessingMO ficoReceiptPayment = new ReceiptProcessingMO();
							SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
							if (!requestModel.getData().getTransaction_id().isEmpty()){
								ficoReceiptPayment.setInstrumentReferenceNumber(transactionId);
							}

//                            if (requestModel.getData().getTransaction_id().startsWith("PY")){
//                                ficoReceiptPayment.setSourceAccountNumber("45992855603");
//                                ficoReceiptPayment.setReceiptPayoutChannel("PAYOO");
//                            } else if (requestModel.getData().getTransaction_id().startsWith("MO")) {
//                                ficoReceiptPayment.setSourceAccountNumber("45992855306");
//                                ficoReceiptPayment.setReceiptPayoutChannel("MOMO");
//                            }

							ficoReceiptPayment.setSourceAccountNumber(ficoPartner.getSourceAccountNumber());
							ficoReceiptPayment.setReceiptPayoutChannel(ficoPartner.getPartnerName());

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
							ficoReceiptPayment.setInstrumentDate(simpleDateFormat.format(new Date(timestamp.getTime())));
							ficoReceiptPayment.setTransactionValueDate(simpleDateFormat.format(new Date(timestamp.getTime())));
							ficoReceiptPayment.setReceiptOrPayoutAmount(ficoTransPaySettle.getAmount());
							ficoReceiptPayment.setAutoAllocation("Y");
							ficoReceiptPayment.setReceiptNo("");
							ficoReceiptPayment.setReceiptPurpose("ANY_DUE");
							ficoReceiptPayment.setDepositDate(simpleDateFormat.format(new Date(timestamp.getTime())));
							ficoReceiptPayment.setDepositBankAccountNumber("519200003");
							ficoReceiptPayment.setRealizationDate(simpleDateFormat.format(new Date(timestamp.getTime())));
							ficoReceiptPayment.setReceiptTransactionStatus("C");
							ficoReceiptPayment.setProcessTillMaker(false);
							ficoReceiptPayment.setRequestChannel("RECEIPT");
							ficoReceiptPayment.setRequestAt(date_time.toString());
							ficoRepaymentModel.setReceiptProcessingMO(ficoReceiptPayment);


							String urlReceiptLMS = urlAPI;
							URI uri = null;
							String response="";
							try {
								uri = new URI(urlAPI);


								SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
								simpleClientHttpRequestFactory.setConnectTimeout(30_000);
								simpleClientHttpRequestFactory.setReadTimeout(30_000);
								ClientHttpRequestFactory requestFactory = new BufferingClientHttpRequestFactory(simpleClientHttpRequestFactory);

								RestTemplate restTemplate = new RestTemplate(requestFactory);
								MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
								headers.add("clientId", "1");
								headers.add("sign", "1");
								headers.add("Content-Type", "application/json");
								HttpEntity<FicoRepaymentModel> body = new HttpEntity<>(ficoRepaymentModel, headers);

//								boolean updateFlagSettle = updateFlagSettle(ficoRepaymentModel.getReceiptProcessingMO().getInstrumentReferenceNumber());
//								StringBuilder sb = new StringBuilder();
//								sb.append(" ACCOUNT: ").append(ficoReceiptPayment.getLoanAccountNo());
//								sb.append(" TRANSACTION_ID: ").append(ficoReceiptPayment.getInstrumentReferenceNumber());
//								sb.append(" updateFlagSettle: ").append(updateFlagSettle);
//								log.info("{}", sb);

								ResponseEntity<String> res = restTemplate.postForEntity(uri, body, String.class);

								response=res.getBody();

							} catch (Exception e) {
								errorMessage=e.getMessage();
								e.printStackTrace();
							}finally {
								//save report
								try {
									log.info("CALL API FIN1 - REQ:" + mapper.writeValueAsString(ficoRepaymentModel) +" - RES:" + response);

									saveReportReceiptPaymentLog(ficoRepaymentModel,response,timestamp,errorMessage);
								} catch (JsonProcessingException e) {
									log.info(e.toString());
								} catch (IOException e) {
									log.info(e.toString());
								} catch (ParseException e) {
									log.info(e.toString());
								}
							}
						}).start();
						//END CALL
					}
					else
					{//ghi table queue: status=0; chua upload, 1: upload
						ficoTransPayQueueDAO.save(FicoTransPayQueue.builder()
								.amount(ficoTransPay.getAmount())
								.loanId(ficoTransPay.getLoanId())
								.loanAccountNo(ficoTransPay.getLoanAccountNo())
								.identificationNumber(ficoTransPay.getIdentificationNumber())
								.transactionId(ficoTransPay.getTransactionId())
								.createDate(timestamp)
								.status(0)
								.partnerId(ficoPartner.getId())
								.build());
					}


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
			sLogs+="; Error: " + e;
			responseModel.setRequest_id(request_id);
			responseModel.setReference_id(UUID.randomUUID().toString());
			responseModel.setDate_time(new Timestamp(new Date().getTime()));
			responseModel.setResult_code(500);
			responseModel.setMessage("Others error");
		}
		finally {
			log.info("{}", sLogs);
		}
		return Map.of("status", 200, "data", responseModel);
	}
	//endregion
}