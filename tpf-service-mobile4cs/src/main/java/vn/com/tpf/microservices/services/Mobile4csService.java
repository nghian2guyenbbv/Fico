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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import vn.com.tpf.microservices.commons.Response;
import vn.com.tpf.microservices.commons.ResultData;
import vn.com.tpf.microservices.dao.Mobiles4csDAO;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class Mobile4csService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RabbitMQService rabbitMQService;

    private JsonNode response(int status, JsonNode data) {
        ObjectNode response = mapper.createObjectNode();
        response.put("status", status).set(Response.DATA, data);
        return response;
    }

    public JsonNode getHadLoan(JsonNode request) {
        ObjectNode fileRes = mapper.createObjectNode();
        String logStr = "";
        logStr += "Request Data : " + request;
        fileRes.put(Response.REQUEST_ID, request.path("body").path(Response.REQUEST_ID).textValue());
        fileRes.put(Response.REFERENCE_ID, UUID.randomUUID().toString());

        try {
            Mobiles4csDAO dao = new Mobiles4csDAO();
            Map<String, String> sqlParam = new HashMap<>();
            sqlParam.put("P_CMND", request.path("body").path(Response.DATA).path("idCardNumber").textValue());
            sqlParam.put("P_PHONE", request.path("body").path(Response.DATA).path("phoneNumber").textValue());

            String sum = dao.getStringData(jdbcTemplate, "FN_PRECHECK_MOBILE4CS", sqlParam);
            JSONObject dataSql = new JSONObject(sum);

            fileRes.put(Response.RESULT_CODE, ResultData.SUCCESS.getResultCode());
            fileRes.put(Response.RESULT_MESSAGE, dataSql.getString("description"));
            String[] countLoan = dataSql.getString("description").split("\\s+");
            if(!countLoan[3].equals("có")){
                fileRes.put("valid", Integer.parseInt(countLoan[3]));
            }

            logStr += "SQL Data : " + sum;
            logStr += "Response: " + fileRes;
        } catch (Exception e) {
            log.info("Error: " + e);
            fileRes.put(Response.RESULT_CODE, ResultData.OTHER_ERROR.getResultCode());
            fileRes.put(Response.RESULT_MESSAGE, ResultData.OTHER_ERROR.getResultMessage());
        } finally {
            log.info(logStr);
        }
        return response(200, fileRes);
    }

    public JsonNode getQuickInquiry(JsonNode request) throws SQLException {
        ObjectNode fileRes = mapper.createObjectNode();
        String logStr = "";
        logStr += "Request Data : " + request;
        fileRes.put(Response.REQUEST_ID, request.path("body").path(Response.REQUEST_ID).textValue());
        fileRes.put(Response.REFERENCE_ID, UUID.randomUUID().toString());

        try {
            Mobiles4csDAO dao = new Mobiles4csDAO();
            Map<String, String> sqlParam = new HashMap<>();
            sqlParam.put("P_CMND", request.path("body").path(Response.DATA).path("idCardNumber").textValue());
            sqlParam.put("P_PHONE", request.path("body").path(Response.DATA).path("phoneNumber").textValue());

            Clob clob = dao.getClobData(jdbcTemplate, "FN_QUICK_INQUIRY_MOBILE4CS", sqlParam);
            String dataClob = dao.handleClob(clob);
            JSONArray parseDataClob = new JSONArray(dataClob);

            fileRes.put(Response.RESULT_CODE, ResultData.SUCCESS.getResultCode());

            if (parseDataClob.isEmpty()) {
                fileRes.put(Response.RESULT_MESSAGE, "Không có khoảng vay nào");
            } else {
                fileRes.put(Response.RESULT_MESSAGE, "Có " + parseDataClob.length() + " khoảng vay");
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
                fileRes.set(Response.DATA, dataArrayNode);
            }

            logStr += "SQL Data : " + clob;
            logStr += "Response: " + fileRes;
        } catch (Exception e) {
            log.info("Error: " + e);
            fileRes.put(Response.RESULT_CODE, ResultData.OTHER_ERROR.getResultCode());
            fileRes.put(Response.RESULT_MESSAGE, ResultData.OTHER_ERROR.getResultMessage());
        } finally {
            log.info(logStr);
        }

        return response(200, fileRes);
    }

    public JsonNode getDetailInquiry(JsonNode request) throws SQLException {
        ObjectNode fileRes = mapper.createObjectNode();
        String logStr = "";
        logStr += "Request Data : " + request;
        fileRes.put(Response.REQUEST_ID, request.path("body").path(Response.REQUEST_ID).textValue());
        fileRes.put(Response.REFERENCE_ID, UUID.randomUUID().toString());

        try {
            Mobiles4csDAO dao = new Mobiles4csDAO();
            Map<String, String> sqlParam = new HashMap<>();
            sqlParam.put("P_LOAN_ACCOUNT", request.path("body").path(Response.DATA).path("loanAccountNumber").textValue());

            String sum = dao.getStringData(jdbcTemplate, "FN_DETAIL_INQUIRY_MOBILE4CS", sqlParam);
            JSONObject dataSql = new JSONObject(sum);
            ObjectNode data = mapper.createObjectNode();
            fileRes.put(Response.RESULT_CODE, ResultData.SUCCESS.getResultCode());
            fileRes.put(Response.RESULT_MESSAGE, ResultData.SUCCESS.getResultMessage());
            if (!dataSql.isEmpty()) {
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

                fileRes.set(Response.DATA, data);
            }

            logStr += "SQL Data : " + sum;
            logStr += "Response: " + fileRes;
        } catch (Exception e) {
            log.info("Error: " + e);
            fileRes.put(Response.RESULT_CODE, ResultData.OTHER_ERROR.getResultCode());
            fileRes.put(Response.RESULT_MESSAGE, ResultData.OTHER_ERROR.getResultMessage());
        } finally {
            log.info(logStr);
        }
        return response(200, fileRes);
    }

    public JsonNode getProfile(JsonNode request) throws SQLException {
        ObjectNode fileRes = mapper.createObjectNode();
        fileRes.put(Response.REQUEST_ID, request.path("body").path(Response.REQUEST_ID).textValue());
        fileRes.put(Response.REFERENCE_ID, UUID.randomUUID().toString());
        fileRes.put(Response.RESULT_CODE, ResultData.SUCCESS.getResultCode());
        fileRes.put(Response.RESULT_MESSAGE, ResultData.SUCCESS.getResultMessage());
        ObjectNode data = mapper.createObjectNode();
        data.put(" softCopyUrl", " softCopyUrl");
        fileRes.set(Response.DATA, data);
        return response(200, fileRes);
    }

    public JsonNode sendSms(JsonNode request) throws Exception {
        ObjectNode fileRes = mapper.createObjectNode();
        Map<String, Object> requestRabbit = new HashMap<>();
        try{
            requestRabbit.put("func", "sendSms");
            requestRabbit.put("body",request);
            JsonNode response = rabbitMQService.sendAndReceive("tpf-service-sms", requestRabbit);
            String resul_code = response.get("resultCode").asText();
            if(resul_code.equals("0")){
                fileRes.put(Response.RESULT_CODE, ResultData.SUCCESS.getResultCode());
                fileRes.put(Response.RESULT_MESSAGE, ResultData.SUCCESS.getResultMessage());
                log.info("Data: " + response.path("data").asText());
            }else{
                fileRes.put(Response.RESULT_CODE, ResultData.FAIL.getResultCode());
                fileRes.put(Response.RESULT_MESSAGE, ResultData.FAIL.getResultMessage());
                log.info("Message: " + response.path("message").asText());
            }
        } catch (Exception e) {
            log.info("Error: " + e);
        }

        return response(200, fileRes);
    }
}
