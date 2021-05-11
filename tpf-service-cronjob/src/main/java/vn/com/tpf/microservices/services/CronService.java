package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.com.tpf.microservices.dao.ApplicationDataPushDao;
import vn.com.tpf.microservices.models.ApplicationDataPushModel;
import vn.com.tpf.microservices.models.apiFin1.AppStatusModel;
import vn.com.tpf.microservices.models.function.ResponseFunctionModel;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@EnableScheduling
public class CronService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTemplate jdbcTemplateFico;

    @Autowired
    public JdbcTemplate jdbcTemplateFicoDe;

    @Autowired
    public JdbcTemplate jdbcTemplateFicoIh;

    @Autowired
    private ApplicationDataPushDao applicationDataPushDao;

    @Autowired
    private ApiFin1Service apiFin1Service;

    @Autowired
    private RabbitMQService rabbitMQService;

}
