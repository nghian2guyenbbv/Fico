package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import vn.com.tpf.microservices.dao.FicoCustomerDAO;
import vn.com.tpf.microservices.dao.FicoTransPayDAO;
import vn.com.tpf.microservices.models.FicoCustomer;
import vn.com.tpf.microservices.models.FicoTransPay;
import vn.com.tpf.microservices.models.RequestModel;
import vn.com.tpf.microservices.models.ResponseModel;

import java.sql.Timestamp;
import java.text.ParseException;
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

}