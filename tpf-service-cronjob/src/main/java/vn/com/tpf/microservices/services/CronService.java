package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

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
    private RabbitMQService rabbitMQService;

}
