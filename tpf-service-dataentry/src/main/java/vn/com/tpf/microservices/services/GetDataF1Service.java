package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
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
    private JdbcTemplate jdbcTemplateF1DE;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplateF1DE;

    @Autowired
    private ObjectMapper mapper;

    @PostConstruct
    public void init(){
        jdbcTemplateF1.setQueryTimeout(30_000);
        namedParameterJdbcTemplateF1 = new NamedParameterJdbcTemplate(jdbcTemplateF1);
        jdbcTemplateF1DE.setQueryTimeout(30_000);
        namedParameterJdbcTemplateF1DE = new NamedParameterJdbcTemplate(jdbcTemplateF1DE);
    }

    public String getProductType(){
        String sql = "SELECT CODE FROM ODS.MV_F1_SUB_GENERIC_PARAMETER WHERE DTYPE='ProductCategory' AND NAME ='Personal Finance'";
        try {
            return jdbcTemplateF1DE.queryForObject(sql, String.class);
        }catch (Exception e){
            log("getProductType", sql, e.toString());
            return "";
        }
    }

    public String getLoanProduct(String schemeCode){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("schemeCode", schemeCode);
        String sql = "SELECT  lp.PRODUCT_CODE FROM ODS.MV_F1_SUB_LOAN_PRODUCT lp,ODS.MV_F1_sub_loan_scheme ls " +
                "WHERE lp.ID = ls.LOAN_PRODUCT AND LOWER(ls.SCHEME_NAME) = LOWER(:schemeCode) and lp.approval_status=0 and ls.approval_status=0";
        return executeQuery(sql, namedParameters);
    }

    public String getCity(String city){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("city", city);
        String sql = "SELECT a.CITY_code " +
                "FROM ODS.MV_F1_sub_city a,ODS.MV_F1_sub_state b,ODS.MV_F1_sub_zip_code c " +
                "where a.state=b.id and a.id=c.city " +
                "and a.APPROVAL_STATUS=0 and b.APPROVAL_STATUS=0 and c.APPROVAL_STATUS=0 and LOWER(a.CITY_NAME) LIKE LOWER(:city)";
        return executeQuery(sql, namedParameters);
    }

    public String getSourcingChannel(String sourcingChannel){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("sourcingChannel", sourcingChannel);
        String sql = "SELECT CODE FROM ODS.mv_f1_sub_generic_parameter WHERE DTYPE='SourcingChannel' AND LOWER(NAME) = LOWER(:sourcingChannel)";
        return executeQuery(sql, namedParameters);
    }

    public String getAlternateChannelMode(String alternateChannelMode){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("alternateChannelMode", alternateChannelMode);
        String sql = "SELECT CODE from ODS.mv_f1_sub_generic_parameter WHERE LOWER(DTYPE) = LOWER('AlternateChannelMode') AND LOWER(NAME) = LOWER(:alternateChannelMode)";
        return executeQuery(sql, namedParameters);
    }

    public String getBranchCode(String sourcingBranch){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("sourcingBranch", sourcingBranch);
        String sql = "SELECT BRANCH_CODE FROM ODS.mv_f1_sub_organization WHERE DTYPE='OrganizationBranch' AND LOWER(NAME) =LOWER(:sourcingBranch) and APPROVAL_STATUS=0";
        return executeQuery(sql, namedParameters);
    }

    public String getOccupationType(String natureOfOccupation){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("natureOfOccupation", natureOfOccupation);
        String sql = "SELECT CODE FROM ODS.mv_f1_sub_generic_parameter WHERE DTYPE='OccupationType' AND LOWER(NAME) =LOWER(:natureOfOccupation)";
        return executeQuery(sql, namedParameters);
    }

    public String getScheme(String schemeCode){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("schemeCode", schemeCode);
        String sql = "SELECT SCHEME_CODE FROM ODS.mv_f1_sub_loan_scheme WHERE LOWER(SCHEME_NAME)=LOWER(:schemeCode) and APPROVAL_STATUS=0";
        return executeQuery(sql, namedParameters);
    }

    public String getLeadStatus(String leadStatus){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("leadStatus", leadStatus);
        String sql = "SELECT CODE FROM ODS.mv_f1_sub_generic_parameter WHERE LOWER(DTYPE) =LOWER('communicationstatus') AND LOWER(NAME) =LOWER(:leadStatus)";
        return executeQuery(sql, namedParameters);
    }

    public String getGender(String gender){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("gender", gender);
        String sql = "SELECT code FROM ODS.mv_f1_sub_generic_parameter WHERE DTYPE = 'GenderType' AND LOWER(name)= LOWER(:gender)";
        return executeQuery(sql, namedParameters);
    }

    public String getMaritalStatus(String maritalStatus){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("maritalStatus", maritalStatus);
        String sql = "SELECT CODE FROM ODS.mv_f1_sub_generic_parameter WHERE DTYPE='MaritalStatusType' " +
                "AND LOWER(name) = LOWER(:maritalStatus)";
        return executeQuery(sql, namedParameters);
    }

    public String getCustomerCategoryCode(String customerCategoryCode){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("customerCategoryCode", customerCategoryCode);
        String sql = "SELECT CUSTOMER_CATEGORY_CODE FROM ODS.mv_f1_sub_customer_category WHERE LOWER(CUSTOMER_CATEGORY_DESCRIPTION) = LOWER(:customerCategoryCode) and  APPROVAL_STATUS=0";
        return executeQuery(sql, namedParameters);
    }

    public String getIdentificationType(String identificationType){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("identificationType", identificationType);
        String sql = "SELECT code FROM ODS.mv_f1_sub_identification_type WHERE LOWER(IDENTIFICATION_TYPE_NAME)=LOWER(:identificationType)";
        return executeQuery(sql, namedParameters);
    }

    public String getAddressType(String addressType){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("addressType", addressType);
        String sql = "SELECT CODE FROM ODS.mv_f1_sub_generic_parameter WHERE dtype = 'AddressType' AND LOWER(NAME) = LOWER(:addressType)";
        return executeQuery(sql, namedParameters);
    }

    public String getCountry(String country){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("country", country);
        String sql = "SELECT COUNTRYISOCODE FROM ODS.mv_f1_sub_country WHERE  APPROVAL_STATUS=0 and LOWER(COUNTRY_NAME) = LOWER(:country)";
        return executeQuery(sql, namedParameters);
    }

    public Map<String, Object> getState_City_Zip(String city){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("city", city);
        String sql = "SELECT a.CITY_code as city, b.STATE_code as state, c.ZIP_CODE as zip \n" +
                "FROM ODS.mv_f1_sub_city a, ODS.mv_f1_sub_state b, ODS.mv_f1_sub_ZIP_CODE c \n" +
                "where a.state=b.id and a.id=c.city and a.APPROVAL_STATUS=0 and b.APPROVAL_STATUS=0 " +
                "and c.APPROVAL_STATUS=0 and TRIM(LOWER(a.CITY_NAME))=TRIM(LOWER(:city))";
        try {
            boolean hasNull = checkNull(namedParameters);
            if (hasNull)
                return Map.of("CITY", "", "STATE", "", "ZIP", "");
            Map<String, Object> result = namedParameterJdbcTemplateF1DE.queryForMap(sql, namedParameters);
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
        String sql = "SELECT AREA_CODE FROM ODS.mv_f1_sub_area a, ODS.mv_f1_sub_city c WHERE a.CITY = c.ID  " +
                "AND TRIM(AREA_NAME)  = TRIM(:area) " +
                "AND c.CITY_CODE = :city " +
                "AND  c.APPROVAL_STATUS=0 AND a.APPROVAL_STATUS =0";
        return executeQuery(sql, namedParameters);
    }

    public String getNatureOfBusiness(String natureOfBusiness){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("natureOfBusiness", natureOfBusiness);
        String sql = "SELECT code FROM mv_f1_sub_generic_parameter where dtype='NatureOfBusiness' AND LOWER(NAME)= LOWER(:natureOfBusiness)";
        return executeQuery(sql, namedParameters);
    }

    public String getNatureOfOccupation(String natureOfOccupation){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("natureOfOccupation", natureOfOccupation);
        String sql = "SELECT code FROM ODS.mv_f1_sub_generic_parameter where dtype='NatureOfOccupation' AND LOWER(NAME) = LOWER(:natureOfOccupation)";
        return executeQuery(sql, namedParameters);
    }

    public String getIndustry(String industry){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("industry", industry);
        String sql = "SELECT CODE FROM ODS.mv_f1_sub_industry WHERE LOWER(NAME) = LOWER(:industry) and  APPROVAL_STATUS=0";
        return executeQuery(sql, namedParameters);
    }

    public String getEmploymentStatus(String employmentStatus){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("employmentStatus", employmentStatus);
        String sql = "SELECT CODE FROM ODS.mv_f1_sub_generic_parameter WHERE DTYPE='EmploymentStatus' AND LOWER(NAME) = LOWER(:employmentStatus)";
        return executeQuery(sql, namedParameters);
    }

    public String getEmploymentType(String employmentType){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("employmentType", employmentType);
        String sql = "SELECT CODE FROM ODS.mv_f1_sub_generic_parameter WHERE DTYPE='EmploymentType' AND LOWER(NAME) = LOWER(:employmentType)";
        return executeQuery(sql, namedParameters);
    }

    public String getIncomeExpense(String incomeExpense){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("incomeExpense", incomeExpense);
        String sql = "SELECT CODE FROM ODS.mv_f1_sub_income_expense WHERE LOWER(DESCRIPTION) = LOWER(:incomeExpense) and APPROVAL_STATUS=0";
        return executeQuery(sql, namedParameters);
    }

    public String getPaymentMode(String paymentMode){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("paymentMode", paymentMode);
        String sql = "SELECT CODE FROM ODS.mv_f1_sub_generic_parameter WHERE DTYPE ='PaymentModeType' AND LOWER(NAME) = LOWER(:paymentMode)";
        return executeQuery(sql, namedParameters);
    }

    public String getLoanApplicationType(String loanApplicationType){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("loanApplicationType", loanApplicationType);
        String sql = "SELECT CODE FROM ODS.mv_f1_sub_generic_parameter WHERE DTYPE='LoanApplicationType' AND LOWER(NAME) = LOWER(:loanApplicationType)";
        return executeQuery(sql, namedParameters);
    }

    public String getVapProduct(String vapProduct){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("vapProduct", vapProduct);
        String sql = "SELECT CODE FROM ODS.mv_f1_sub_vap_para_policy vp where LOWER(vp.name) = LOWER(:vapProduct) and vp.approval_status=0";
        return executeQuery(sql, namedParameters);
    }

    public String getVapTreatment(String vapProduct, String productCode){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValues(Map.of("vapProduct", vapProduct, "productCode", productCode));
        String sql = "select gp.code from\n" +
                "ODS.mv_f1_sub_generic_parameter gp \n" +
                "join ODS.mv_f1_sub_amt_compt_mpp acm on gp.id = acm.treatment_type\n" +
                "join ODS.mv_f1_sub_vap_policy_mapping vpm on acm.vap_amt_comp_fk = vpm.amt_comp_policy\n" +
                "join ODS.mv_f1_sub_vap_para_policy vpp on vpm.vap_parameter_policy = vpp.id\n" +
                "join ODS.mv_f1_sub_product_policy pp on pp.loan_policy = vpm.fk_vap_policy\n" +
                "join ODS.mv_f1_sub_loan_product lp on lp.id = pp.product_fk\n" +
                "and vpm.approval_status=0\n" +
                "and vpp.approval_status=0\n" +
                "and pp.policy_type = (select id from ODS.mv_f1_sub_generic_parameter where code='VapPolicy')\n" +
                "and lp.approval_status=0\n" +
                "and LOWER(vpp.name) = LOWER(:vapProduct) \n" +
                "and LOWER(lp.product_code) = LOWER(:productCode)";
        try {
            boolean hasNull = checkNull(namedParameters);
            if (hasNull) return "";
            List<String> result = namedParameterJdbcTemplateF1DE.queryForList(sql, namedParameters, String.class);
            if (result.size() <= 0) return "";
            return result.get(0);
        }catch (Exception e){
            log("getVapTreatment", sql, e.toString());
            return "";
        }
    }

    public String getInsuranceCompany(String vapProduct, String productCode) {
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValues(Map.of("vapProduct", vapProduct, "productCode", productCode));
        String sql = "select bp.code from\n" +
                "ODS.mv_f1_sub_vap_comput_plcy_mpp vpsm\n" +
                "join ODS.mv_f1_sub_vap_policy_mapping vpm on vpsm.loan_policy_fk = vpm.pay_out_comp_policy\n" +
                "join ODS.mv_f1_sub_vap_para_policy vpp on vpm.vap_parameter_policy = vpp.id\n" +
                "join ODS.mv_f1_sub_bz_partner_type bpt on bpt.id = vpsm.business_partner_type\n" +
                "join ODS.mv_f1_sub_bz_partner bp on bp.id = vpsm.bp_id\n" +
                "join ODS.mv_f1_sub_product_policy pp on pp.loan_policy = vpm.fk_vap_policy\n" +
                "join ODS.mv_f1_sub_loan_product lp on lp.id = pp.product_fk\n" +
                "and bpt.code='Insurance_Company'\n" +
                "and vpm.approval_status=0\n" +
                "and vpp.approval_status=0\n" +
                "and pp.policy_type = (select id from ODS.mv_f1_sub_generic_parameter where code='VapPolicy')\n" +
                "and lp.approval_status=0\n" +
                "and LOWER(vpp.name) = LOWER(:vapProduct) \n" +
                "and LOWER(lp.product_code) = LOWER(:productCode)";
        return executeQuery(sql, namedParameters);
    }

    public String getHouseOwnership(String houseOwnership){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("houseOwnership", houseOwnership);
        String sql = "SELECT CUSTOME_ITEM_VALUE \n" +
                "FROM ODS.MV_F1_SUB_UIMETA UD \n" +
                "where UD.APPROVAL_STATUS = 0 \n" +
                "AND UD.MODEL_NAME LIKE '%frmAppDtl%'\n" +
                "AND UD.PANEL_KEY = 'familyinformation'\n" +
                "AND UD.FIELD_KEY = 'house_ownership'" +
                "AND LOWER(CUSTOME_ITEM_LABEL) = LOWER(:houseOwnership)";
        return executeQuery(sql, namedParameters);
    }

    public String getOfficer(String saleAgentCode){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("saleAgentCode", saleAgentCode);
        String sql = "SELECT USERNAME FROM ODS.MV_F1_SUB_USERS WHERE ID IN (SELECT parent_user FROM ODS.MV_F1_SUB_OFFICER WHERE FULL_NAME =:saleAgentCode)";
        return executeQuery(sql, namedParameters);
    }

    public String getIncomeSource(String dayOfSalaryPayment){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("dayOfSalaryPayment", dayOfSalaryPayment);
        String sql = "select code from ODS.mv_f1_sub_generic_parameter where LOWER(name) = LOWER(:dayOfSalaryPayment) and dtype='IncomeSource'";
        try {
            List<String> result = namedParameterJdbcTemplateF1.queryForList(sql, namedParameters, String.class);
            if (result.size() <= 0) return "";
            return result.get(0);
        }catch (Exception e){
            log("getIncomeSource", sql, e.toString());
            return "";
        }
    }

//    public String getDocumentReferenceId(String applicationNumber, String documentName){
//        SqlParameterSource namedParameters = new MapSqlParameterSource().addValues(Map.of("applicationNumber", applicationNumber, "documentName", documentName));
//        String sql = "SELECT ID FROM NEO_CM_GA25_GIR_SD.LENDING_DOCUMENT WHERE ID IN (\n" +
//                "SELECT PARTY_DOCUMENTS FROM NEO_CM_GA25_GIR_SD.PARTY_DOCUMENTS WHERE PARTY=(SELECT ID FROM NEO_CM_GA25_GIR_SD.PARTY WHERE LOAN_APPLICATION_FK=(SELECT ID \n" +
//                "FROM NEO_CM_GA25_GIR_SD.LOAN_APPLICATION WHERE LOWER(APPLICATION_NUMBER) = LOWER(:applicationNumber)))) AND PRIMARY_DOCUMENT_DEFINITION IN (SELECT ID\n" +
//                "FROM NEO_CM_GA25_GIR_SD.DOCUMENT_DEFINITION WHERE NAME = :documentName AND APPROVAL_STATUS=0)";
//        return executeQuery(sql, namedParameters);
//    }

    public String getDocumentReferenceId(String applicationId, String type) {
        String query = String.format("SELECT TPF_F1.fn_get_lending_doc_id ('%s','%s') RESULT FROM DUAL",
                applicationId,
                type);

        return jdbcTemplateF1.queryForObject(query, new Object[]{},
                (rs, rowNum) ->
                        rs.getString(("RESULT")
                        ));
    }

    public String getResidentType(){
        String sql = "SELECT code FROM ODS.mv_f1_sub_generic_parameter where dtype='ResidentType'";
        return queryForList(sql);
    }

    public String getSalutation(){
        String sql = "SELECT code FROM ODS.mv_f1_sub_generic_parameter where dtype='SalutationType'";
        return queryForList(sql);
    }

    public String getConstitutionCode(){
        String sql = "SELECT constitution_code FROM ODS.mv_f1_sub_cus_constitution cc where approval_status=0";
        return queryForList(sql);
    }

    public Map<String, Object> getAmtCompPolicy(String vapProduct, String productCode){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValues(Map.of("vapProduct", vapProduct, "productCode", productCode));
        String sql = "SELECT vpm.AMT_COMP_POLICY,\n" +
                "    (\n" +
                "        SELECT CODE \n" +
                "        FROM ODS.MV_F1_SUB_Loan_policy\n" +
                "        WHERE APPROVAL_STATUS = 0 \n" +
                "        AND ID = VPM.AMT_COMP_POLICY\n" +
                "    ) CODE_AMT_COMP,\n" +
                "    vpm.PAY_OUT_COMP_POLICY,\n" +
                "    (\n" +
                "        SELECT CODE \n" +
                "        FROM ODS.MV_F1_SUB_Loan_policy\n" +
                "        WHERE APPROVAL_STATUS = 0 \n" +
                "        AND ID = VPM.PAY_OUT_COMP_POLICY\n" +
                "    ) CODE_PAY_OUT\n" +
                "FROM ODS.MV_F1_SUB_vap_policy_mapping vpm\n" +
                "WHERE\n" +
                "    vpm.VAP_PARAMETER_POLICY IN (\n" +
                "        SELECT ID\n" +
                "        FROM ODS.MV_F1_SUB_VAP_PARA_POLICY\n" +
                "        WHERE LOWER(NAME) = LOWER(:vapProduct) \n" +
                "        AND APPROVAL_STATUS = 0\n" +
                "    )\n" +
                "    AND vpm.fk_vap_policy = (\n" +
                "        select loan_policy\n" +
                "        from ODS.MV_F1_SUB_PRODUCT_POLICY\n" +
                "        where\n" +
                "            product_fk =(\n" +
                "                select id\n" +
                "                from ODS.MV_F1_SUB_LOAN_PRODUCT\n" +
                "                where LOWER(product_code) = LOWER(:productCode)\n" +
                "                and approval_status = 0\n" +
                "            )\n" +
                "            and policy_type =(\n" +
                "                select id\n" +
                "                from ODS.MV_F1_SUB_generic_parameter\n" +
                "                where code = 'VapPolicy'\n" +
                "            )\n" +
                "    )\n" +
                "    AND APPROVAL_STATUS = 0";
        try {
            boolean hasNull = checkNull(namedParameters);
            if(hasNull) return Map.of("CODE_AMT_COMP", "", "CODE_PAY_OUT", "");
            Map<String, Object> result = namedParameterJdbcTemplateF1DE.queryForMap(sql, namedParameters);
            if (result == null) return Map.of("CODE_AMT_COMP", "", "CODE_PAY_OUT", "");
            return result;
        }catch(Exception e){
            log("getAmtCompPolicy", sql, e.toString());
            return Map.of("CODE_AMT_COMP", "", "CODE_PAY_OUT", "");
        }
    }

    public List<Integer> getLoanPurpose(List<String> loanPurpose){
        SqlParameterSource namedParameters = new MapSqlParameterSource("loanPurpose", loanPurpose);
        String sql = "SELECT CUSTOME_ITEM_VALUE \n" +
                "FROM ODS.MV_F1_SUB_UIMETA UD \n" +
                "AND APPROVAL_STATUS=0\n" +
                "AND MODEL_NAME LIKE '%frmAppDtl%'\n" +
                "AND PANEL_KEY='Loan_details_1'\n" +
                "AND FIELD_KEY='Loan_purpose_1'\n" +
                "AND FCO.CUSTOME_ITEM_LABEL IN (:loanPurpose)";
        try{
            boolean hasNull = checkNull(namedParameters);
            if(hasNull) return new ArrayList<>();
            List<Integer> result = namedParameterJdbcTemplateF1DE.queryForList(sql, namedParameters, Integer.class);
            return result;
        }catch(Exception e){
            log("getLoanPurpose", sql, e.toString());
            return new ArrayList<>();
        }
    }

//    public List<Object> getListError(String appNum){
//        String sql = "SELECT ERROR_MESSAGE\n" +
//                "FROM NEO_CAS_LMS_GA25_GIR_SD.re_rule \n" +
//                "    where id in (\n" +
//                "    select RULE_ID \n" +
//                "    from NEO_CAS_LMS_GA25_GIR_SD.RULES_AUDIT_LOG\n" +
//                "    where RULE_INVOCATIONUUID=\n" +
//                "    (   \n" +
//                "        SELECT c.RULES_AUDITUUID \n" +
//                "        FROM NEO_CAS_LMS_GA25_GIR_SD.RULE_EXECUTION_ENTITY_MAPPING c \n" +
//                "        where id=(SELECT max(b.id)\n" +
//                "        FROM NEO_CAS_LMS_GA25_GIR_SD.LOAN_APPLICATION a,NEO_CAS_LMS_GA25_GIR_SD.RULE_EXECUTION_ENTITY_MAPPING b \n" +
//                "        where a.id=b.LOAN_APPLICATION_ID\n" +
//                "        and a.APPLICATION_NUMBER = :appNum )\n" +
//                "    ) \n" +
//                "    and RULE_RESULT='false'" +
//                ")";
//        SqlParameterSource namedParameters = new MapSqlParameterSource("appNum", appNum);
//        try{
//            boolean hasNull = checkNull(namedParameters);
//            if (hasNull) return new ArrayList<>();
//            List<Object> result = namedParameterJdbcTemplateF1.queryForList(sql, namedParameters, Object.class);
//            if(result == null) return new ArrayList<>();
//            return result;
//        }catch (Exception e){
//            log("getListError", sql, e.toString());
//            return new ArrayList<>();
//        }
//    }

    public String getListError(String applicationId){
        String query = String.format("select TPF_F1.fn_get_f1_err_msg ('%s') RESULT FROM DUAL", applicationId);
        return jdbcTemplateF1.queryForObject(query, new Object[]{},
                (rs, rowNum) -> rs.getString(("RESULT"))
        );
    }

    private String executeQuery(String sql, SqlParameterSource namedParameters){
        try {
            boolean hasNull = checkNull(namedParameters);
            if (hasNull) return "";
            String result = namedParameterJdbcTemplateF1DE.queryForObject(sql, namedParameters, String.class);
            if(result == null) return "";
            return result;
        }catch (Exception e){
            log("executeQuery", sql, e.toString());
            return "";
        }
    }

    private String queryForList(String sql){
        try {
            List<String> result = jdbcTemplateF1DE.queryForList(sql, String.class);
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

    public List<Map<String, Object>> getListReasonToCancel() {
        String sql = "SELECT name as \"name\",DESCRIPTION as \"description\" from ODS.MV_F1_SUB_CANCEL_DECISION order by name desc";
        try {
            List<Map<String, Object>> result = jdbcTemplateF1DE.queryForList(sql);
            return result;
        }catch (Exception e){
            log("getListReasonToCancel", sql, e.toString());
            return new ArrayList<>();
        }

    }
}
