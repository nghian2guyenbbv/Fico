package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.com.tpf.microservices.config.BscScoreConfig;

@Service
@EnableScheduling
public class BscScoreCronService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RabbitMQService rabbitMQService;

    @Autowired
    private ObjectMapper mapper;


    @Autowired
    private BscScoreConfig bscScoreConfig;

    @Scheduled(cron = "${spring.bsc_score_time.bt1}")
    public void job_bscScore1()
    {
        ObjectNode node = mapper.createObjectNode();
        node.put("bt_id", Integer.valueOf(bscScoreConfig.getBsc_score_body().get("bt1").get("bt_id")));
        node.put("skip_fail", Boolean.valueOf(bscScoreConfig.getBsc_score_body().get("bt1").get("skip_fail")));
        job_bscScore("job_bscScore1", node);
    }

    @Scheduled(cron = "${spring.bsc_score_time.bt2}")
    public void job_bscScore2()
    {
        ObjectNode node = mapper.createObjectNode();
        node.put("bt_id", Integer.valueOf(bscScoreConfig.getBsc_score_body().get("bt2").get("bt_id")));
        node.put("skip_fail", Boolean.valueOf(bscScoreConfig.getBsc_score_body().get("bt2").get("skip_fail")));
        job_bscScore("job_bscScore2", node);
    }

    @Scheduled(cron = "${spring.bsc_score_time.bt3}")
    public void job_bscScore3()
    {
        ObjectNode node = mapper.createObjectNode();
        node.put("bt_id", Integer.valueOf(bscScoreConfig.getBsc_score_body().get("bt3").get("bt_id")));
        node.put("skip_fail", Boolean.valueOf(bscScoreConfig.getBsc_score_body().get("bt3").get("skip_fail")));
        job_bscScore("job_bscScore3", node);
    }

    @Scheduled(cron = "${spring.bsc_score_time.bt4}")
    public void job_bscScore4()
    {
        ObjectNode node = mapper.createObjectNode();
        node.put("bt_id", Integer.valueOf(bscScoreConfig.getBsc_score_body().get("bt4").get("bt_id")));
        node.put("skip_fail", Boolean.valueOf(bscScoreConfig.getBsc_score_body().get("bt4").get("skip_fail")));
        job_bscScore("job_bscScore4", node);
    }

    @Scheduled(cron = "${spring.bsc_score_time.bt5}")
    public void job_bscScore5()
    {
        ObjectNode node = mapper.createObjectNode();
        node.put("bt_id", Integer.valueOf(bscScoreConfig.getBsc_score_body().get("bt5").get("bt_id")));
        node.put("skip_fail", Boolean.valueOf(bscScoreConfig.getBsc_score_body().get("bt5").get("skip_fail")));
        job_bscScore("job_bscScore5", node);
    }

    private void job_bscScore(String func, Object body){
        String slog = func;
        try{
            rabbitMQService.send("tpf-service-bcscore-schedule", body);
        }catch (Exception e)
        {
            slog += " - exception: " + e.toString();

        }finally {
            log.info("{}", slog);
        }
    }
}
