package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.tpf.microservices.commons.Response;
import vn.com.tpf.microservices.commons.ResultData;
import vn.com.tpf.microservices.dao.Mobiles4csDAO;
import vn.com.tpf.microservices.models.ResponseModel;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.*;

@Service
public class Mobile4csService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private RabbitMQService rabbitMQService;

    @Autowired
    private Mobiles4csDAO mobiles4csDAO;

    public JsonNode getHadLoan(JsonNode request) {
        ResponseModel responseModel = new ResponseModel();
        String logStr = "";
        logStr += "Request Data : " + request;
        responseModel.setRequest_id(request.path("body").path(Response.REQUEST_ID).textValue());
        responseModel.setReference_id(UUID.randomUUID().toString());

        try {
            Map<String, String> sqlParam = new HashMap<>();
            sqlParam.put("P_CMND", request.path("body").path(Response.DATA).path("idCardNumber").textValue());
            sqlParam.put("P_PHONE", request.path("body").path(Response.DATA).path("phoneNumber").textValue());

            String sum = mobiles4csDAO.getStringData("FN_PRECHECK_MOBILE4CS", sqlParam);
            JSONObject dataSql = new JSONObject(sum);

            responseModel.setResult_code(ResultData.SUCCESS.getResultCode());
            responseModel.setResult_message(dataSql.getString("description"));
            if (1 == dataSql.getInt("result")) {
                responseModel.setValid(1);
            }

            logStr += "SQL Data : " + sum;
            logStr += "Response: " + responseModel.toString();
        } catch (Exception e) {
            log.info("Error: " + e);
            responseModel.setResult_code(ResultData.OTHER_ERROR.getResultCode());
            responseModel.setResult_message(ResultData.OTHER_ERROR.getResultMessage());
        } finally {
            log.info(logStr);
        }
        return responseModel.getResponseModel(mapper);
    }

    public JsonNode getQuickInquiry(JsonNode request) throws SQLException {
        ResponseModel responseModel = new ResponseModel();
        String logStr = "";
        logStr += "Request Data : " + request;
        responseModel.setRequest_id(request.path("body").path(Response.REQUEST_ID).textValue());
        responseModel.setReference_id(UUID.randomUUID().toString());

        try {
            Map<String, String> sqlParam = new HashMap<>();
            sqlParam.put("P_CMND", request.path("body").path(Response.DATA).path("idCardNumber").textValue());
            sqlParam.put("P_PHONE", request.path("body").path(Response.DATA).path("phoneNumber").textValue());

            Clob clob = mobiles4csDAO.getClobData("FN_QUICK_INQUIRY_MOBILE4CS", sqlParam);
            String dataClob = mobiles4csDAO.handleClob(clob);
            JSONArray parseDataClob = new JSONArray(dataClob);

            if (parseDataClob.isEmpty()) {
                responseModel.setResult_code(ResultData.DATAEMPTY.getResultCode());
                responseModel.setResult_message(ResultData.DATAEMPTY.getResultMessage());
            } else {
                responseModel.setResult_code(ResultData.SUCCESS.getResultCode());
                responseModel.setResult_message("Có " + parseDataClob.length() + " khoảng vay");
                ArrayNode dataArrayNode = mapper.createArrayNode();
                for (Object o : parseDataClob) {
                    if (o instanceof JSONObject) {
                        JSONObject dataLoan = (JSONObject) o;
                        ObjectNode data = mapper.createObjectNode();
                        data.put("typeOfLoan ", dataLoan.getString("loanType"));
                        data.put("loanAccountNumber  ", dataLoan.getString("loanId"));
                        data.put("productSchemaName", dataLoan.getString("loanProd"));
                        data.put("totalLoanAmount", dataLoan.getLong("loanAmount"));
                        data.put("remainingPrincipal", dataLoan.getLong("loanRemain"));
                        data.put("loanStatus", dataLoan.getString("loanStatus"));
                        data.put("daysPastDue", dataLoan.getInt("loanDPD"));
                        dataArrayNode.add(data);
                    }
                }
                responseModel.setData(dataArrayNode);
            }

            logStr += "SQL Data : " + clob;
            logStr += "Response: " + responseModel.toString();
        } catch (Exception e) {
            log.info("Error: " + e);
            responseModel.setResult_code(ResultData.OTHER_ERROR.getResultCode());
            responseModel.setResult_message(ResultData.OTHER_ERROR.getResultMessage());
        } finally {
            log.info(logStr);
        }

        return responseModel.getResponseModel(mapper);
    }

    public JsonNode getDetailInquiry(JsonNode request) throws SQLException {
        ResponseModel responseModel = new ResponseModel();
        String logStr = "";
        logStr += "Request Data : " + request;
        responseModel.setRequest_id(request.path("body").path(Response.REQUEST_ID).textValue());
        responseModel.setReference_id(UUID.randomUUID().toString());

        try {

            Map<String, String> sqlParam = new HashMap<>();
            sqlParam.put("P_LOAN_ACCOUNT", request.path("body").path(Response.DATA).path("loanAccountNumber").textValue());

            String sum = mobiles4csDAO.getStringData("FN_DETAIL_INQUIRY_MOBILE4CS", sqlParam);
            JSONObject dataSql = new JSONObject(sum);
            ObjectNode data = mapper.createObjectNode();

            if (dataSql.isEmpty()) {
                responseModel.setResult_code(ResultData.DATAEMPTY.getResultCode());
                responseModel.setResult_message(ResultData.DATAEMPTY.getResultMessage());
            }else{
                responseModel.setResult_code(ResultData.SUCCESS.getResultCode());
                responseModel.setResult_message(ResultData.SUCCESS.getResultMessage());
                data.put("loanAccountNumber  ", dataSql.getString("loanId"));
                data.put("productSchemaName", dataSql.getString("prodName"));
                data.put("totalLoanAmount", dataSql.getLong("loanAmount"));
                data.put("disbursementDate", dataSql.getString("disbDate"));
                data.put("maturityDate", dataSql.getString("maturDate"));
                data.put("daysPastDue", dataSql.getInt("DPD"));
                data.put("totalPrincipalPaid ", dataSql.getLong("princiPaid"));
                data.put("remainingPrincipal", dataSql.getLong("remainPrici"));
                data.put("repaymentAmount", dataSql.getLong("repayAmount"));
                data.put("loanStatus", dataSql.getString("loanStatus"));
                data.put("dueDate", dataSql.getString("dueDate"));
                ArrayNode dataArrayNode = mapper.createArrayNode();
                JSONArray parseDataClob = dataSql.getJSONArray("schedule");
                for (Object o : parseDataClob) {
                    if (o instanceof JSONObject) {
                        JSONObject dataLoan = (JSONObject) o;
                        ObjectNode dataObject = mapper.createObjectNode();
                        dataObject.put("installment ", dataLoan.getLong("instAmount"));
                        dataObject.put("emi ", dataLoan.getLong("EMI"));
                        dataObject.put("principal", dataLoan.getLong("principal"));
                        dataObject.put("interest", dataLoan.getLong("interest"));
                        dataObject.put("paidPrincipal ", dataLoan.getLong("paidPrincial"));
                        dataObject.put("paidInterest ", dataLoan.getLong("paidInterest"));
                        dataArrayNode.add(dataObject);
                    }
                }
                data.put("schedule", dataArrayNode);
                responseModel.setData(data);
            }

            logStr += "SQL Data : " + sum;
            logStr += "Response: " + responseModel.toString();
        } catch (Exception e) {
            log.info("Error: " + e);
            responseModel.setResult_code(ResultData.OTHER_ERROR.getResultCode());
            responseModel.setResult_message(ResultData.OTHER_ERROR.getResultMessage());
        } finally {
            log.info(logStr);
        }
        return responseModel.getResponseModel(mapper);
    }

    public JsonNode getProfile(JsonNode request) throws SQLException {
        ResponseModel responseModel = new ResponseModel();
        String logStr = "";
        logStr += "Request Data : " + request;
        responseModel.setRequest_id(request.path("body").path(Response.REQUEST_ID).textValue());
        responseModel.setReference_id(UUID.randomUUID().toString());
        try {
            Map<String, String> sqlParam = new HashMap<>();
            sqlParam.put("P_CMND", request.path("body").path(Response.DATA).path("idCardNumber").textValue());
            sqlParam.put("P_PHONE", request.path("body").path(Response.DATA).path("phoneNumber").textValue());

            String sum = mobiles4csDAO.getStringData("FN_USER_PROFILE_MOBILE4CS", sqlParam);
            JSONObject dataSql = new JSONObject(sum);

            ObjectNode data = mapper.createObjectNode();
            if(dataSql.isEmpty()){
                responseModel.setResult_code(ResultData.DATAEMPTY.getResultCode());
                responseModel.setResult_message(ResultData.DATAEMPTY.getResultMessage());
            }else {
                responseModel.setResult_code(ResultData.SUCCESS.getResultCode());
                responseModel.setResult_message(ResultData.SUCCESS.getResultMessage());
                data.put("fullName", dataSql.getString("fullName"));
                data.put("dateOfBirth", dataSql.getString("DOB"));
                data.put("phoneNumber", getStringFromJsonArray(dataSql.getJSONArray("phoneNo")));
                data.put("idCardNumber", dataSql.getString("idCard"));
                data.put("currentAddress", dataSql.getString("curAdd"));
                data.put("permanentAddress", dataSql.getString("perAdd"));
                data.put("companyName", dataSql.getString("comName"));
                data.put("workingAddress", getStringFromJsonArray(dataSql.getJSONArray("comAdd")));
                data.put("workingPhoneNumber", "telePhone: " + getStringFromJsonArray(dataSql.getJSONObject("comPhone").getJSONArray("telePhone")) + "mobiPhone: " + getStringFromJsonArray(dataSql.getJSONObject("comPhone").getJSONArray("mobiPhone")));
                data.put("referencePhoneNumber", getStringFromJsonArray(dataSql.getJSONArray("refPhone")));
                data.put("emailAddress", dataSql.getString("email"));
                data.put("zaloAddress", dataSql.getString("zalo"));
                responseModel.setData(data);
            }
        } catch (Exception e) {
            log.info("Error: " + e);
            responseModel.setResult_code(ResultData.OTHER_ERROR.getResultCode());
            responseModel.setResult_message(ResultData.OTHER_ERROR.getResultMessage());
        } finally {
            log.info(logStr);
        }
        return responseModel.getResponseModel(mapper);
    }

    private String getStringFromJsonArray(JSONArray arr) {
        JSONArray jsonArray = arr;
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.getString(i));
        }
        return list.toString().replace("[", "").replace("]", "");
    }

    public JsonNode sendSms(JsonNode request) throws Exception {
        ResponseModel responseModel = new ResponseModel();
        Map<String, Object> requestRabbit = new HashMap<>();
        try {
            requestRabbit.put("func", "sendSms");
            requestRabbit.put("body", request);
            JsonNode response = rabbitMQService.sendAndReceive("tpf-service-sms", requestRabbit);
            String resul_code = response.get("resultCode").asText();
            if (resul_code.equals("0")) {
                responseModel.setResult_code(ResultData.SUCCESS.getResultCode());
                responseModel.setResult_message(ResultData.SUCCESS.getResultMessage());
                log.info("Data: " + response.path("data").asText());
            } else {
                responseModel.setResult_code(ResultData.FAIL.getResultCode());
                responseModel.setResult_message(ResultData.FAIL.getResultMessage());
                log.info("Message: " + response.path("message").asText());
            }
        } catch (Exception e) {
            log.info("Error: " + e);
            responseModel.setResult_code(ResultData.OTHER_ERROR.getResultCode());
            responseModel.setResult_message(ResultData.OTHER_ERROR.getResultMessage());
        }

        return responseModel.getResponseModel(mapper);
    }
}
