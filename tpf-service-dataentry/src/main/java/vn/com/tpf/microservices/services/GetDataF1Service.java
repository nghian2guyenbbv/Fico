package vn.com.tpf.microservices.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

@Service
public class GetDataF1Service {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public String getProductType(){
        String sql = "SELECT CODE from NEO_CAS_LMS_GA25_GIR_SD.generic_parameter WHERE DTYPE='ProductCategory' AND NAME ='Personal Finance'";
        return jdbcTemplate.queryForObject(sql, String.class);
    }

    public String getLoanProduct(String schemeCode){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("schemeCode", schemeCode);
        String sql = "SELECT  lp.PRODUCT_CODE from NEO_CAS_LMS_GA25_GIR_SD.loan_PRODUCT lp,NEO_CAS_LMS_GA25_GIR_SD.loan_scheme ls " +
                "WHERE lp.ID = ls.LOAN_PRODUCT AND ls.SCHEME_NAME = :schemeCode and lp.approval_status=0 and ls.approval_status=0";
        return namedParameterJdbcTemplate.queryForObject(sql, namedParameters, String.class);
    }

    public String getCity(String city){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("city", city);
        String sql = "SELECT a.CITY_code FROM NEO_CM_GA25_GIR_SD.city a,NEO_CM_GA25_GIR_SD.state b,NEO_CM_GA25_GIR_SD.ZIP_CODE c where a.state=b.id and a.id=c.city " +
                "and a.APPROVAL_STATUS=0 and b.APPROVAL_STATUS=0 and c.APPROVAL_STATUS=0 and LOWER(a.CITY_NAME) LIKE LOWER(:city)";
        return namedParameterJdbcTemplate.queryForObject(sql, namedParameters, String.class);
    }

    public String getSourcingChannel(String sourcingChannel){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("sourcingChannel", sourcingChannel);
        String sql = "SELECT CODE from NEO_CAS_LMS_GA25_GIR_SD.generic_parameter WHERE DTYPE='SourcingChannel' AND NAME = :sourcingChannel";
        return namedParameterJdbcTemplate.queryForObject(sql, namedParameters, String.class);
    }

    public String getBranchCode(String sourcingBranch){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("sourcingBranch", sourcingBranch);
        String sql = "SELECT BRANCH_CODE FROM NEO_CAS_LMS_GA25_GIR_SD.Organization WHERE DTYPE='OrganizationBranch' AND LOWER(NAME) =LOWER(:sourcingBranch) and APPROVAL_STATUS=0";
        return namedParameterJdbcTemplate.queryForObject(sql, namedParameters, String.class);
    }

    public String getOccupationType(String natureOfOccupation){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("natureOfOccupation", natureOfOccupation);
        String sql = "SELECT CODE from NEO_CAS_LMS_GA25_GIR_SD.generic_parameter WHERE DTYPE='OccupationType' AND LOWER(NAME) =LOWER(:natureOfOccupation)";
        return namedParameterJdbcTemplate.queryForObject(sql, namedParameters, String.class);
    }

    public String getScheme(String schemeCode){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("schemeCode", schemeCode);
        String sql = "SELECT SCHEME_CODE from NEO_CAS_LMS_GA25_GIR_SD.loan_scheme WHERE SCHEME_NAME=:schemeCode and APPROVAL_STATUS=0";
        return namedParameterJdbcTemplate.queryForObject(sql, namedParameters, String.class);
    }

    public String getLeadStatus(String leadStatus){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("leadStatus", leadStatus);
        String sql = "SELECT CODE from NEO_CAS_LMS_GA25_GIR_SD.generic_parameter WHERE LOWER(DTYPE) =LOWER('communicationstatus') AND LOWER(NAME) =LOWER(:leadStatus)";
        return namedParameterJdbcTemplate.queryForObject(sql, namedParameters, String.class);
    }

}
