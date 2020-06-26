package vn.com.tpf.microservices.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class GetDataF1Service {

    @Autowired
    private JdbcTemplate jdbcTemplateF1;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplateF1;

    public String getProductType(){
        try {
            String sql = "SELECT CODE from NEO_CAS_LMS_GA25_GIR_SD.GENERIC_PARAMETER WHERE DTYPE='ProductCategory' AND NAME ='Personal Finance'";
            return jdbcTemplateF1.queryForObject(sql, String.class);
        }catch (Exception e){
            return "";
        }
    }

    public String getLoanProduct(String schemeCode){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("schemeCode", schemeCode);
        String sql = "SELECT  lp.PRODUCT_CODE from NEO_CAS_LMS_GA25_GIR_SD.loan_PRODUCT lp,NEO_CAS_LMS_GA25_GIR_SD.loan_scheme ls " +
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
        String sql = "SELECT CODE from NEO_CAS_LMS_GA25_GIR_SD.GENERIC_PARAMETER WHERE DTYPE='SourcingChannel' AND NAME = :sourcingChannel";
        return executeQuery(sql, namedParameters);
    }

    public String getBranchCode(String sourcingBranch){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("sourcingBranch", sourcingBranch);
        String sql = "SELECT BRANCH_CODE FROM NEO_CAS_LMS_GA25_GIR_SD.Organization WHERE DTYPE='OrganizationBranch' AND LOWER(NAME) =LOWER(:sourcingBranch) and APPROVAL_STATUS=0";
        return executeQuery(sql, namedParameters);
    }

    public String getOccupationType(String natureOfOccupation){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("natureOfOccupation", natureOfOccupation);
        String sql = "SELECT CODE from NEO_CAS_LMS_GA25_GIR_SD.GENERIC_PARAMETER WHERE DTYPE='OccupationType' AND LOWER(NAME) =LOWER(:natureOfOccupation)";
        return executeQuery(sql, namedParameters);
    }

    public String getScheme(String schemeCode){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("schemeCode", schemeCode);
        String sql = "SELECT SCHEME_CODE from NEO_CAS_LMS_GA25_GIR_SD.loan_scheme WHERE SCHEME_NAME=:schemeCode and APPROVAL_STATUS=0";
        return executeQuery(sql, namedParameters);
    }

    public String getLeadStatus(String leadStatus){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("leadStatus", leadStatus);
        String sql = "SELECT CODE from NEO_CAS_LMS_GA25_GIR_SD.GENERIC_PARAMETER WHERE LOWER(DTYPE) =LOWER('communicationstatus') AND LOWER(NAME) =LOWER(:leadStatus)";
        return executeQuery(sql, namedParameters);
    }

    public String getGender(String gender){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("gender", gender);
        String sql = "SELECT code from NEO_CAS_LMS_GA25_GIR_SD.GENERIC_PARAMETER  WHERE DTYPE = 'GenderType' AND name=:gender";
        return executeQuery(sql, namedParameters);
    }

    public String getMaritalStatus(String maritalStatus){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("maritalStatus", maritalStatus);
        String sql = "SELECT CODE from NEO_CAS_LMS_GA25_GIR_SD.GENERIC_PARAMETER WHERE DTYPE='MaritalStatusType' " +
                "AND name = :maritalStatus";
        return executeQuery(sql, namedParameters);
    }

    public String getCustomerCategoryCode(String customerCategoryCode){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("customerCategoryCode", customerCategoryCode);
        String sql = "SELECT CUSTOMER_CATEGORY_CODE from NEO_CAS_LMS_GA25_GIR_SD.CUSTOMER_CATEGORY WHERE CUSTOMER_CATEGORY_DESCRIPTION = :customerCategoryCode and  APPROVAL_STATUS=0";
        return executeQuery(sql, namedParameters);
    }

    public String getIdentificationType(String identificationType){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("identificationType", identificationType);
        String sql = "SELECT code FROM NEO_CM_GA25_GIR_SD.IDENTIFICATION_TYPE WHERE LOWER(IDENTIFICATION_TYPE_NAME)=LOWER(:identificationType)";
        return executeQuery(sql, namedParameters);
    }

    public String getAddressType(String addressType){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("addressType", addressType);
        String sql = "SELECT CODE from NEO_CAS_LMS_GA25_GIR_SD.GENERIC_PARAMETER  WHERE dtype = 'AddressType' AND NAME = :addressType";
        return executeQuery(sql, namedParameters);
    }

    public String getCountry(String country){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("country", country);
        String sql = "SELECT COUNTRYISOCODE from NEO_CAS_LMS_GA25_GIR_SD.COUNTRY WHERE COUNTRY_NAME = :country and APPROVAL_STATUS=0";
        return executeQuery(sql, namedParameters);
    }

    public Map<String, Object> getState_City_Zip(String city){
        try {
            SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("city", city);
            boolean hasNull = checkNull(namedParameters);
            if (hasNull) return Map.of("CITY", "", "STATE", "", "ZIP", "");
            String sql = "SELECT a.CITY_code as city, b.STATE_code as state, c.ZIP_CODE as zip FROM NEO_CM_GA25_GIR_SD.city a, NEO_CM_GA25_GIR_SD.state b, NEO_CM_GA25_GIR_SD.ZIP_CODE c where a.state=b.id and a.id=c.city " +
                    "and a.APPROVAL_STATUS=0 and b.APPROVAL_STATUS=0 and c.APPROVAL_STATUS=0 and TRIM(LOWER(a.CITY_NAME))=TRIM(LOWER(:city))";
            Map<String, Object> result = namedParameterJdbcTemplateF1.queryForMap(sql, namedParameters);
            if (result == null) return Map.of("CITY", "", "STATE", "", "ZIP", "");
            return result;
        }catch (Exception e){
            return Map.of("CITY", "", "STATE", "", "ZIP", "");
        }
    }

    public String getArea(String area, String city){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValues(Map.of("area", area, "city", city));
        String sql = "SELECT AREA_CODE from NEO_CAS_LMS_GA25_GIR_SD.AREA a, NEO_CAS_LMS_GA25_GIR_SD.city c WHERE a.CITY = c.ID  " +
                "AND TRIM(AREA_NAME)  = TRIM(:area) " +
                "AND c.CITY_CODE = :city " +
                "AND  c.APPROVAL_STATUS=0 AND a.APPROVAL_STATUS =0";
        return executeQuery(sql, namedParameters);
    }

    public String getNatureOfBusiness(String natureOfBusiness){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("natureOfBusiness", natureOfBusiness);
        String sql = "select code from NEO_CAS_LMS_GA25_GIR_SD.GENERIC_PARAMETER  where dtype='NatureOfBusiness' AND NAME=:natureOfBusiness";
        return executeQuery(sql, namedParameters);
    }

    public String getNatureOfOccupation(String natureOfOccupation){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("natureOfOccupation", natureOfOccupation);
        String sql = "select code from NEO_CAS_LMS_GA25_GIR_SD.GENERIC_PARAMETER  where dtype='NatureOfOccupation' AND NAME=:natureOfOccupation";
        return executeQuery(sql, namedParameters);
    }

    public String getIndustry(String industry){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("industry", industry);
        String sql = "SELECT CODE from NEO_CAS_LMS_GA25_GIR_SD.INDUSTRY WHERE NAME=:industry and  APPROVAL_STATUS=0";
        return executeQuery(sql, namedParameters);
    }

    public String getEmploymentStatus(String employmentStatus){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("employmentStatus", employmentStatus);
        String sql = "SELECT CODE from NEO_CAS_LMS_GA25_GIR_SD.GENERIC_PARAMETER WHERE DTYPE='EmploymentStatus' AND NAME =:employmentStatus";
        return executeQuery(sql, namedParameters);
    }

    public String getEmploymentType(String employmentType){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("employmentType", employmentType);
        String sql = "SELECT CODE from NEO_CAS_LMS_GA25_GIR_SD.GENERIC_PARAMETER WHERE DTYPE='EmploymentType' AND NAME =:employmentType";
        return executeQuery(sql, namedParameters);
    }

    public String getIncomeExpense(String incomeExpense){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("incomeExpense", incomeExpense);
        String sql = "SELECT CODE from NEO_CAS_LMS_GA25_GIR_SD.INCOME_EXPENSE WHERE DESCRIPTION=:incomeExpense and APPROVAL_STATUS=0";
        return executeQuery(sql, namedParameters);
    }

    public String getPaymentMode(String paymentMode){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("paymentMode", paymentMode);
        String sql = "SELECT CODE FROM NEO_CM_GA25_GIR_SD.GENERIC_PARAMETER WHERE DTYPE ='PaymentModeType' AND NAME =:paymentMode";
        return executeQuery(sql, namedParameters);
    }

    public String getLoanApplicationType(String loanApplicationType){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("loanApplicationType", loanApplicationType);
        String sql = "SELECT CODE from NEO_CAS_LMS_GA25_GIR_SD.GENERIC_PARAMETER WHERE DTYPE='LoanApplicationType' AND NAME =:loanApplicationType";
        return executeQuery(sql, namedParameters);
    }

    public String getVapProduct(String vapProduct){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("vapProduct", vapProduct);
        String sql = "select CODE from NEO_CM_GA25_GIR_SD.VAP_PARAMETER_POLICY vp where vp.name=:vapProduct and vp.approval_status=0";
        return executeQuery(sql, namedParameters);
    }

    public String getVapTreatment(String vapProduct){
        try {
            SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("vapProduct", vapProduct);
            boolean hasNull = checkNull(namedParameters);
            if (hasNull) return "";
            String sql = "select gp.code from NEO_CM_GA25_GIR_SD.GENERIC_PARAMETER gp \n" +
                    "join NEO_CM_GA25_GIR_SD.amount_computation_mapping acm on gp.id = acm.treatment_type\n" +
                    "join NEO_CM_GA25_GIR_SD.vap_policy_mapping vpm on acm.vap_amt_comp_fk = vpm.amt_comp_policy\n" +
                    "join NEO_CM_GA25_GIR_SD.vap_parameter_policy vpp on vpm.vap_parameter_policy = vpp.id\n" +
                    "and vpp.name=:vapProduct and vpm.approval_status=0 and vpp.approval_status=0";
            List<String> result = namedParameterJdbcTemplateF1.queryForList(sql, namedParameters, String.class);
            if (result.size() <= 0) return "";
            return result.get(0);
        }catch (Exception e){
            return "";
        }
    }

    public String getInsuranceCompany(String vapProduct){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("vapProduct", vapProduct);
        String sql = "select DISTINCT  bp.code from NEO_CM_GA25_GIR_SD.VAP_COMPUT_POLICY_SET_MAPPING vpsm\n" +
                "join NEO_CM_GA25_GIR_SD.vap_policy_mapping vpm on vpsm.loan_policy_fk = vpm.pay_out_comp_policy\n" +
                "join NEO_CM_GA25_GIR_SD.vap_parameter_policy vpp on vpm.vap_parameter_policy = vpp.id\n" +
                "join NEO_CM_GA25_GIR_SD.business_partner_type bpt on bpt.id = vpsm.business_partner_type\n" +
                "join NEO_CM_GA25_GIR_SD.business_partner bp on bp.id = vpsm.bp_id\n" +
                "and vpp.name=:vapProduct and bpt.code='Insurance_Company'\n" +
                "and vpm.approval_status=0 and vpp.approval_status=0";
        return executeQuery(sql, namedParameters);
    }

    public String getHouseOwnership(String houseOwnership){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("houseOwnership", houseOwnership);
        String sql = "select fco.CUSTOME_ITEM_VALUE from NEO_CM_GA25_GIR_SD.uimeta_data ud \n" +
                "inner join NEO_CM_GA25_GIR_SD.panel_definition pd on ud.id = pd.ui_panel_def_fk \n" +
                "join NEO_CM_GA25_GIR_SD.field_definition fd on pd.id = fd.panel_field_def_fk\n" +
                "JOIN NEO_CM_GA25_GIR_SD.field_custom_options fco on fd.id = fco.fk_field_custom_option\n" +
                "and ud.approval_status=0\n" +
                "and ud.model_name like '%frmAppDtl%'\n" +
                "and pd.PANEL_KEY='familyinformation'\n" +
                "and fd.FIELD_KEY='house_ownership'\n" +
                "AND fco.CUSTOME_ITEM_LABEL=   :houseOwnership";
        return executeQuery(sql, namedParameters);
    }

    public String getOfficer(String saleAgentCode){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("saleAgentCode", saleAgentCode);
        String sql = "select username from NEO_CM_GA25_GIR_SD.users where id in (select parent_user from NEO_CM_GA25_GIR_SD.officer where approval_status=0 AND FULL_NAME =:saleAgentCode)";
        return executeQuery(sql, namedParameters);
    }

    public String getDocumentReferenceId(String applicationNumber){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("applicationNumber", applicationNumber);
        String sql = "Select id from lending_document where primary_document_definition=(select id from document_definition where name={API.documentName} and approval_status=0) and application_fk=(\n" +
                "select id from loan_application where application_number=:applicationNumber)";
        return executeQuery(sql, namedParameters);
    }

    public String getResidentType(){
        String sql = "select code from NEO_CM_GA25_GIR_SD.generic_parameter where dtype='ResidentType'";
        try {
            List<String> result = jdbcTemplateF1.queryForList(sql, String.class);
            if(result.size() <= 0) return "";
            return result.get(0);
        }catch (Exception e){
            return "";
        }
    }

    public String getSalutation(){
        String sql = "select code from NEO_CM_GA25_GIR_SD.generic_parameter where dtype='SalutationType'";
        try {
            List<String> result = jdbcTemplateF1.queryForList(sql, String.class);
            if(result.size() <= 0) return "";
            return result.get(0);
        }catch (Exception e){
            return "";
        }
    }

    public String getConstitutionCode(){
        try {
            String sql = "select constitution_code from NEO_CM_GA25_GIR_SD.customer_constitution cc where approval_status=0";
            List<String> result = jdbcTemplateF1.queryForList(sql, String.class);
            if(result.size() <= 0) return "";
            return result.get(0);
        }catch (Exception e){
            return "";
        }
    }

    public Map<String, Object> getAmtCompPolicy(String vapProduct){
        try {
            SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("vapProduct", vapProduct);
            String sql = "SELECT distinct AMT_COMP_POLICY,\n" +
                    "    (SELECT CODE FROM NEO_CAS_LMS_GA25_GIR_SD.Loan_policy WHERE dtype='AmountComputationPolicy' and APPROVAL_STATUS=0 AND ID=AMT_COMP_POLICY) CODE_AMT_COMP,\n" +
                    "    PAY_OUT_COMP_POLICY,\n" +
                    "    (SELECT CODE FROM NEO_CAS_LMS_GA25_GIR_SD.Loan_policy WHERE dtype='VapComputationPolicy' and APPROVAL_STATUS=0 AND ID=PAY_OUT_COMP_POLICY) CODE_PAY_OUT\n" +
                    "FROM NEO_CAS_LMS_GA25_GIR_SD.vap_policy_mapping \n" +
                    "WHERE VAP_PARAMETER_POLICY IN (SELECT ID FROM NEO_CAS_LMS_GA25_GIR_SD.VAP_PARAMETER_POLICY WHERE NAME=:vapProduct AND APPROVAL_STATUS=0) AND\n" +
                    "fk_vap_policy = (select id from NEO_CAS_LMS_GA25_GIR_SD.loan_policy where code='INS01' and approval_status=0) AND APPROVAL_STATUS =0";

            boolean hasNull = checkNull(namedParameters);
            if(hasNull) return Map.of("CODE_AMT_COMP", "", "CODE_PAY_OUT", "");
            Map<String, Object> result = namedParameterJdbcTemplateF1.queryForMap(sql, namedParameters);
            if (result == null) return Map.of("CODE_AMT_COMP", "", "CODE_PAY_OUT", "");
            return result;
        }catch(Exception e){
            return Map.of("CODE_AMT_COMP", "", "CODE_PAY_OUT", "");
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
}
