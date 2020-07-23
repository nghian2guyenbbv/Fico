package vn.com.tpf.microservices.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Map;

@Component
public class Mobiles4csDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String getStringData(String function, Map<String, String> param) {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withFunctionName(function);
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        param.forEach((k, v) -> {
            paramMap.addValue(k, v);
        });
        return call.executeFunction(String.class, paramMap);
    }

    public Clob getClobData(String function, Map<String, String> param) {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withFunctionName(function);
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        param.forEach((k, v) -> {
            paramMap.addValue(k, v);
        });
        return call.executeFunction(Clob.class, paramMap);
    }

    public String handleClob(Clob clob) throws SQLException {
        if (clob == null)
            return null;

        Reader reader = null;
        try {
            reader = clob.getCharacterStream();
            if (reader == null)
                return null;
            char[] buffer = new char[(int) clob.length()];
            if (buffer.length == 0)
                return null;
            reader.read(buffer);
            return new String(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        }
    }
}
