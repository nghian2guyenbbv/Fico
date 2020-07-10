package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class GetDataF1Service {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JdbcTemplate jdbcTemplateF1;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplateF1;

    @Autowired
    private ObjectMapper mapper;

    public String getProductType(){
        String sql = "SELECT CODE FROM NEO_CAS_LMS_GA25_GIR_SD.GENERIC_PARAMETER WHERE DTYPE='ProductCategory' AND NAME ='Personal Finance'";
        try {
            return jdbcTemplateF1.queryForObject(sql, String.class);
        }catch (Exception e){
            log("getProductType", sql, e.toString());
            return "";
        }
    }

    @Cacheable(value = "", key = "#schemeCode")
    public String getLoanProduct(String schemeCode){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("schemeCode", schemeCode);
        String sql = "SELECT  lp.PRODUCT_CODE FROM NEO_CAS_LMS_GA25_GIR_SD.loan_PRODUCT lp,NEO_CAS_LMS_GA25_GIR_SD.loan_scheme ls " +
                "WHERE lp.ID = ls.LOAN_PRODUCT AND ls.SCHEME_NAME = :schemeCode and lp.approval_status=0 and ls.approval_status=0";
        return executeQuery(sql, namedParameters);
    }

    public String getCity(String city){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("city", city);
        String sql = "SELECT a.CITY_code FROM NEO_CM_GA25_GIR_SD.city a,NEO_CM_GA25_GIR_SD.state b,NEO_CM_GA25_GIR_SD.ZIP_CODE c where a.state=b.id and a.id=c.city " +
                "and a.APPROVAL_STATUS=0 and b.APPROVAL_STATUS=0 and c.APPROVAL_STATUS=0 and LOWER(a.CITY_NAME) LIKE LOWER(:city)";
        return executeQuery(sql, namedParameters);
    }

    public String getSourcingChannel(String sourcingChannel){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("sourcingChannel", sourcingChannel);
        String sql = "SELECT CODE FROM NEO_CAS_LMS_GA25_GIR_SD.GENERIC_PARAMETER WHERE DTYPE='SourcingChannel' AND NAME = :sourcingChannel";
        return executeQuery(sql, namedParameters);
    }

    public String getBranchCode(String sourcingBranch){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("sourcingBranch", sourcingBranch);
        String sql = "SELECT BRANCH_CODE FROM NEO_CAS_LMS_GA25_GIR_SD.Organization WHERE DTYPE='OrganizationBranch' AND LOWER(NAME) =LOWER(:sourcingBranch) and APPROVAL_STATUS=0";
        return executeQuery(sql, namedParameters);
    }

    public String getOccupationType(String natureOfOccupation){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("natureOfOccupation", natureOfOccupation);
        String sql = "SELECT CODE FROM NEO_CAS_LMS_GA25_GIR_SD.GENERIC_PARAMETER WHERE DTYPE='OccupationType' AND LOWER(NAME) =LOWER(:natureOfOccupation)";
        return executeQuery(sql, namedParameters);
    }

    public String getScheme(String schemeCode){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("schemeCode", schemeCode);
        String sql = "SELECT SCHEME_CODE FROM NEO_CAS_LMS_GA25_GIR_SD.loan_scheme WHERE SCHEME_NAME=:schemeCode and APPROVAL_STATUS=0";
        return executeQuery(sql, namedParameters);
    }

    public String getLeadStatus(String leadStatus){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("leadStatus", leadStatus);
        String sql = "SELECT CODE FROM NEO_CAS_LMS_GA25_GIR_SD.GENERIC_PARAMETER WHERE LOWER(DTYPE) =LOWER('communicationstatus') AND LOWER(NAME) =LOWER(:leadStatus)";
        return executeQuery(sql, namedParameters);
    }

    public String getGender(String gender){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("gender", gender);
        String sql = "SELECT code FROM NEO_CAS_LMS_GA25_GIR_SD.GENERIC_PARAMETER  WHERE DTYPE = 'GenderType' AND name=:gender";
        return executeQuery(sql, namedParameters);
    }

    public String getMaritalStatus(String maritalStatus){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("maritalStatus", maritalStatus);
        String sql = "SELECT CODE FROM NEO_CAS_LMS_GA25_GIR_SD.GENERIC_PARAMETER WHERE DTYPE='MaritalStatusType' " +
                "AND name = :maritalStatus";
        return executeQuery(sql, namedParameters);
    }

    public String getCustomerCategoryCode(String customerCategoryCode){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("customerCategoryCode", customerCategoryCode);
        String sql = "SELECT CUSTOMER_CATEGORY_CODE FROM NEO_CAS_LMS_GA25_GIR_SD.CUSTOMER_CATEGORY WHERE CUSTOMER_CATEGORY_DESCRIPTION = :customerCategoryCode and  APPROVAL_STATUS=0";
        return executeQuery(sql, namedParameters);
    }

    public String getIdentificationType(String identificationType){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("identificationType", identificationType);
        String sql = "SELECT code FROM NEO_CM_GA25_GIR_SD.IDENTIFICATION_TYPE WHERE LOWER(IDENTIFICATION_TYPE_NAME)=LOWER(:identificationType)";
        return executeQuery(sql, namedParameters);
    }

    public String getAddressType(String addressType){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("addressType", addressType);
        String sql = "SELECT CODE FROM NEO_CAS_LMS_GA25_GIR_SD.GENERIC_PARAMETER  WHERE dtype = 'AddressType' AND NAME = :addressType";
        return executeQuery(sql, namedParameters);
    }

    public String getCountry(String country){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("country", country);
        String sql = "SELECT COUNTRYISOCODE FROM NEO_CAS_LMS_GA25_GIR_SD.COUNTRY WHERE COUNTRY_NAME = :country and APPROVAL_STATUS=0";
        return executeQuery(sql, namedParameters);
    }

    public Map<String, Object> getState_City_Zip(String city){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("city", city);
        String sql = "SELECT a.CITY_code as city, b.STATE_code as state, c.ZIP_CODE as zip FROM NEO_CM_GA25_GIR_SD.city a, NEO_CM_GA25_GIR_SD.state b, NEO_CM_GA25_GIR_SD.ZIP_CODE c where a.state=b.id and a.id=c.city " +
                "and a.APPROVAL_STATUS=0 and b.APPROVAL_STATUS=0 and c.APPROVAL_STATUS=0 and TRIM(LOWER(a.CITY_NAME))=TRIM(LOWER(:city))";
        try {
            boolean hasNull = checkNull(namedParameters);
            if (hasNull)
                return Map.of("CITY", "", "STATE", "", "ZIP", "");
            Map<String, Object> result = namedParameterJdbcTemplateF1.queryForMap(sql, namedParameters);
            if (result == null)
                return Map.of("CITY", "", "STATE", "", "ZIP", "");
            return result;
        }catch (Exception e){
            log("getState_City_Zip", sql, e.toString());
            return Map.of("CITY", "", "STATE", "", "ZIP", "");
        }
    }

    public String getArea(String area, String city){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValues(Map.of("area", area, "city", city));
        String sql = "SELECT AREA_CODE FROM NEO_CAS_LMS_GA25_GIR_SD.AREA a, NEO_CAS_LMS_GA25_GIR_SD.city c WHERE a.CITY = c.ID  " +
                "AND TRIM(AREA_NAME)  = TRIM(:area) " +
                "AND c.CITY_CODE = :city " +
                "AND  c.APPROVAL_STATUS=0 AND a.APPROVAL_STATUS =0";
        return executeQuery(sql, namedParameters);
    }

    public String getNatureOfBusiness(String natureOfBusiness){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("natureOfBusiness", natureOfBusiness);
        String sql = "SELECT code FROM NEO_CAS_LMS_GA25_GIR_SD.GENERIC_PARAMETER  where dtype='NatureOfBusiness' AND NAME=:natureOfBusiness";
        return executeQuery(sql, namedParameters);
    }

    public String getNatureOfOccupation(String natureOfOccupation){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("natureOfOccupation", natureOfOccupation);
        String sql = "SELECT code FROM NEO_CAS_LMS_GA25_GIR_SD.GENERIC_PARAMETER  where dtype='NatureOfOccupation' AND NAME=:natureOfOccupation";
        return executeQuery(sql, namedParameters);
    }

    public String getIndustry(String industry){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("industry", industry);
        String sql = "SELECT CODE FROM NEO_CAS_LMS_GA25_GIR_SD.INDUSTRY WHERE NAME=:industry and  APPROVAL_STATUS=0";
        return executeQuery(sql, namedParameters);
    }

    public String getEmploymentStatus(String employmentStatus){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("employmentStatus", employmentStatus);
        String sql = "SELECT CODE FROM NEO_CAS_LMS_GA25_GIR_SD.GENERIC_PARAMETER WHERE DTYPE='EmploymentStatus' AND NAME =:employmentStatus";
        return executeQuery(sql, namedParameters);
    }

    public String getEmploymentType(String employmentType){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("employmentType", employmentType);
        String sql = "SELECT CODE FROM NEO_CAS_LMS_GA25_GIR_SD.GENERIC_PARAMETER WHERE DTYPE='EmploymentType' AND NAME =:employmentType";
        return executeQuery(sql, namedParameters);
    }

    public String getIncomeExpense(String incomeExpense){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("incomeExpense", incomeExpense);
        String sql = "SELECT CODE FROM NEO_CAS_LMS_GA25_GIR_SD.INCOME_EXPENSE WHERE DESCRIPTION=:incomeExpense and APPROVAL_STATUS=0";
        return executeQuery(sql, namedParameters);
    }

    public String getPaymentMode(String paymentMode){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("paymentMode", paymentMode);
        String sql = "SELECT CODE FROM NEO_CM_GA25_GIR_SD.GENERIC_PARAMETER WHERE DTYPE ='PaymentModeType' AND NAME =:paymentMode";
        return executeQuery(sql, namedParameters);
    }

    public String getLoanApplicationType(String loanApplicationType){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("loanApplicationType", loanApplicationType);
        String sql = "SELECT CODE FROM NEO_CAS_LMS_GA25_GIR_SD.GENERIC_PARAMETER WHERE DTYPE='LoanApplicationType' AND NAME =:loanApplicationType";
        return executeQuery(sql, namedParameters);
    }

    public String getVapProduct(String vapProduct){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("vapProduct", vapProduct);
        String sql = "SELECT CODE FROM NEO_CM_GA25_GIR_SD.VAP_PARAMETER_POLICY vp where vp.name=:vapProduct and vp.approval_status=0";
        return executeQuery(sql, namedParameters);
    }

    public String getVapTreatment(String vapProduct){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("vapProduct", vapProduct);
        String sql = "SELECT gp.code FROM NEO_CM_GA25_GIR_SD.GENERIC_PARAMETER gp \n" +
                "join NEO_CM_GA25_GIR_SD.amount_computation_mapping acm on gp.id = acm.treatment_type\n" +
                "join NEO_CM_GA25_GIR_SD.vap_policy_mapping vpm on acm.vap_amt_comp_fk = vpm.amt_comp_policy\n" +
                "join NEO_CM_GA25_GIR_SD.vap_parameter_policy vpp on vpm.vap_parameter_policy = vpp.id\n" +
                "and vpp.name=:vapProduct and vpm.approval_status=0 and vpp.approval_status=0";
        try {
            boolean hasNull = checkNull(namedParameters);
            if (hasNull) return "";
            List<String> result = namedParameterJdbcTemplateF1.queryForList(sql, namedParameters, String.class);
            if (result.size() <= 0) return "";
            return result.get(0);
        }catch (Exception e){
            log("getVapTreatment", sql, e.toString());
            return "";
        }
    }

    public String getInsuranceCompany(String vapProduct){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("vapProduct", vapProduct);
        String sql = "SELECT DISTINCT BP.CODE FROM NEO_CM_GA25_GIR_SD.VAP_COMPUT_POLICY_SET_MAPPING VPSM \n" +
                "JOIN NEO_CM_GA25_GIR_SD.VAP_POLICY_MAPPING VPM ON VPSM.LOAN_POLICY_FK = VPM.PAY_OUT_COMP_POLICY \n" +
                "JOIN NEO_CM_GA25_GIR_SD.VAP_PARAMETER_POLICY VPP ON VPM.VAP_PARAMETER_POLICY = VPP.ID \n" +
                "JOIN NEO_CM_GA25_GIR_SD.BUSINESS_PARTNER_TYPE BPT ON BPT.ID = VPSM.BUSINESS_PARTNER_TYPE \n" +
                "JOIN NEO_CM_GA25_GIR_SD.BUSINESS_PARTNER BP ON BP.ID = VPSM.BP_ID \n" +
                "AND VPP.NAME = :vapProduct AND BPT.CODE = 'Insurance_Company'\n" +
                "AND VPM.APPROVAL_STATUS=0 AND VPP.APPROVAL_STATUS=0";
        return executeQuery(sql, namedParameters);
    }

    public String getHouseOwnership(String houseOwnership){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("houseOwnership", houseOwnership);
        String sql = "SELECT FCO.CUSTOME_ITEM_VALUE FROM NEO_CM_GA25_GIR_SD.UIMETA_DATA UD \n" +
                "INNER JOIN NEO_CM_GA25_GIR_SD.PANEL_DEFINITION PD ON UD.ID = PD.UI_PANEL_DEF_FK \n" +
                "JOIN NEO_CM_GA25_GIR_SD.FIELD_DEFINITION FD ON PD.ID = FD.PANEL_FIELD_DEF_FK \n" +
                "JOIN NEO_CM_GA25_GIR_SD.FIELD_CUSTOM_OPTIONS FCO ON FD.ID = FCO.FK_FIELD_CUSTOM_OPTION \n" +
                "AND UD.APPROVAL_STATUS = 0 \n" +
                "AND UD.MODEL_NAME LIKE '%frmAppDtl%'\n" +
                "AND PD.PANEL_KEY = 'familyinformation'\n" +
                "AND FD.FIELD_KEY = 'house_ownership'\n" +
                "AND FCO.CUSTOME_ITEM_LABEL =   :houseOwnership";
        return executeQuery(sql, namedParameters);
    }

    public String getOfficer(String saleAgentCode){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("saleAgentCode", saleAgentCode);
        String sql = "SELECT USERNAME FROM NEO_CM_GA25_GIR_SD.USERS WHERE ID IN (SELECT PARENT_USER FROM NEO_CM_GA25_GIR_SD.OFFICER WHERE APPROVAL_STATUS=0 AND FULL_NAME =:saleAgentCode)";
        return executeQuery(sql, namedParameters);
    }

    public String getDocumentReferenceId(String applicationNumber, String documentName){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValues(Map.of("applicationNumber", applicationNumber, "documentName", documentName));
        String sql = "SELECT ID FROM NEO_CM_GA25_GIR_SD.LENDING_DOCUMENT WHERE ID IN (\n" +
                "SELECT PARTY_DOCUMENTS FROM NEO_CM_GA25_GIR_SD.PARTY_DOCUMENTS WHERE PARTY=(SELECT ID FROM NEO_CM_GA25_GIR_SD.PARTY WHERE LOAN_APPLICATION_FK=(SELECT ID \n" +
                "FROM NEO_CM_GA25_GIR_SD.LOAN_APPLICATION WHERE APPLICATION_NUMBER=:applicationNumber))) AND PRIMARY_DOCUMENT_DEFINITION IN (SELECT ID\n" +
                "FROM NEO_CM_GA25_GIR_SD.DOCUMENT_DEFINITION WHERE NAME = :documentName AND APPROVAL_STATUS=0)";
        return executeQuery(sql, namedParameters);
    }

    public String getResidentType(){
        String sql = "SELECT code FROM NEO_CM_GA25_GIR_SD.generic_parameter where dtype='ResidentType'";
        return queryForList(sql);
    }

    public String getSalutation(){
        String sql = "SELECT code FROM NEO_CM_GA25_GIR_SD.generic_parameter where dtype='SalutationType'";
        return queryForList(sql);
    }

    public String getConstitutionCode(){
        String sql = "SELECT constitution_code FROM NEO_CM_GA25_GIR_SD.customer_constitution cc where approval_status=0";
        return queryForList(sql);
    }

    public Map<String, Object> getAmtCompPolicy(String vapProduct, String schemeCode){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValues(Map.of("vapProduct", vapProduct, "schemeCode", schemeCode));
        String sql = "SELECT DISTINCT AMT_COMP_POLICY,\n" +
                "    (SELECT CODE FROM NEO_CAS_LMS_GA25_GIR_SD.LOAN_POLICY WHERE DTYPE='AmountComputationPolicy' AND APPROVAL_STATUS=0 AND ID=AMT_COMP_POLICY) CODE_AMT_COMP,\n" +
                "    PAY_OUT_COMP_POLICY,\n" +
                "    (SELECT CODE FROM NEO_CAS_LMS_GA25_GIR_SD.LOAN_POLICY WHERE DTYPE='VapComputationPolicy' AND APPROVAL_STATUS=0 AND ID=PAY_OUT_COMP_POLICY) CODE_PAY_OUT\n" +
                "FROM NEO_CAS_LMS_GA25_GIR_SD.VAP_POLICY_MAPPING \n" +
                "WHERE VAP_PARAMETER_POLICY IN (SELECT ID FROM NEO_CAS_LMS_GA25_GIR_SD.VAP_PARAMETER_POLICY WHERE NAME=:vapProduct AND APPROVAL_STATUS=0) AND\n" +
                "FK_VAP_POLICY = (SELECT ID FROM NEO_CAS_LMS_GA25_GIR_SD.LOAN_POLICY WHERE CODE=(SELECT CODE FROM NEO_CAS_LMS_GA25_GIR_SD.LOAN_POLICY WHERE APPROVAL_STATUS=0 AND DTYPE='VapPolicy' \n" +
                "AND ID IN (SELECT SCHEME_POLICIES FROM NEO_CAS_LMS_GA25_GIR_SD.LOAN_SCHEME_SCHEME_POLICIES WHERE LOAN_SCHEME=(SELECT ID FROM NEO_CAS_LMS_GA25_GIR_SD.LOAN_SCHEME WHERE\n" +
                "SCHEME_CODE=:schemeCode AND APPROVAL_STATUS=0))) AND APPROVAL_STATUS=0) AND APPROVAL_STATUS =0";
        try {
            boolean hasNull = checkNull(namedParameters);
            if(hasNull) return Map.of("CODE_AMT_COMP", "", "CODE_PAY_OUT", "");
            Map<String, Object> result = namedParameterJdbcTemplateF1.queryForMap(sql, namedParameters);
            if (result == null) return Map.of("CODE_AMT_COMP", "", "CODE_PAY_OUT", "");
            return result;
        }catch(Exception e){
            log("getAmtCompPolicy", sql, e.toString());
            return Map.of("CODE_AMT_COMP", "", "CODE_PAY_OUT", "");
        }
    }

    public List<Integer> getLoanPurpose(List<String> loanPurpose){
        SqlParameterSource namedParameters = new MapSqlParameterSource("loanPurpose", loanPurpose);
        String sql = "SELECT FCO.CUSTOME_ITEM_VALUE FROM\n" +
                "NEO_CM_GA25_GIR_SD.UIMETA_DATA UD INNER JOIN\n" +
                "NEO_CM_GA25_GIR_SD.PANEL_DEFINITION PD ON UD.ID = PD.UI_PANEL_DEF_FK \n" +
                "JOIN NEO_CM_GA25_GIR_SD.FIELD_DEFINITION FD ON PD.ID = FD.PANEL_FIELD_DEF_FK\n" +
                "JOIN NEO_CM_GA25_GIR_SD.FIELD_CUSTOM_OPTIONS FCO ON FD.ID = FCO.FK_FIELD_CUSTOM_OPTION\n" +
                "AND UD.APPROVAL_STATUS=0\n" +
                "AND UD.MODEL_NAME LIKE '%frmAppDtl%'\n" +
                "AND PD.PANEL_KEY='Loan_details_1'\n" +
                "AND FD.FIELD_KEY='Loan_purpose_1'\n" +
                "AND FCO.CUSTOME_ITEM_LABEL IN (:loanPurpose)";
        try{
            boolean hasNull = checkNull(namedParameters);
            if(hasNull) return new ArrayList<>();
            List<Integer> result = namedParameterJdbcTemplateF1.queryForList(sql, namedParameters, Integer.class);
            return result;
        }catch(Exception e){
            log("getLoanPurpose", sql, e.toString());
            return new ArrayList<>();
        }
    }

    private String executeQuery(String sql, SqlParameterSource namedParameters){
        try {
            boolean hasNull = checkNull(namedParameters);
            if (hasNull) return "";
            String result = namedParameterJdbcTemplateF1.queryForObject(sql, namedParameters, String.class);
            if(result == null) return "";
            return result;
        }catch (Exception e){
            log("executeQuery", sql, e.toString());
            return "";
        }
    }

    private String queryForList(String sql){
        try {
            List<String> result = jdbcTemplateF1.queryForList(sql, String.class);
            if(result.size() <= 0) return "";
            return result.get(0);
        }catch (Exception e){
            log("queryForList", sql, e.toString());
            return "";
        }
    }

    private boolean checkNull(SqlParameterSource namedParameters){
        AtomicBoolean hasNull = new AtomicBoolean(false);
        ((MapSqlParameterSource) namedParameters).getValues().entrySet().forEach(stringObjectEntry -> {
            if (StringUtils.isEmpty(stringObjectEntry.getValue())){
                hasNull.set(true);
            }
        });
        return hasNull.get();
    }

    private void log(String method, String query, String msgError){
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("type","[==DATAENTRY-QUERY-DBF1==]");
        objectNode.put("method", method);
        objectNode.put("query", query);
        objectNode.put("message", msgError);
        log.info("{}", objectNode);
    }
}
