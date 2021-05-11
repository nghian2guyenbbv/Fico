package vn.com.tpf.microservices.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@EnableScheduling
public class AutoAssignCronService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RabbitMQService rabbitMQService;

    @Scheduled(cron = "${spring.cron.autoAssignSyncConfig}")
    public void job_autoAssignSyncConfig()
    {
        String slog = "func: job_autoAssignSyncConfig";
        try{
            rabbitMQService.send("tpf-service-autoassign",
                    Map.of("func", "job_autoAssignSyncConfig","reference_id", UUID.randomUUID().toString()));
        }catch (Exception e)
        {
            slog += " - exception: " + e.toString();

        }finally {
            log.info("{}", slog);
        }
    }

    @Scheduled(cron = "${spring.cron.autoAssignSyncCountStatus}")
    public void job_autoAssignSyncCountStatus()
    {
        String slog = "func: job_autoAssignSyncCountStatus";
        try{
            rabbitMQService.send("tpf-service-autoassign",
                    Map.of("func", "job_autoAssignSyncCountStatus","reference_id", UUID.randomUUID().toString()));
        }catch (Exception e)
        {
            slog += " - exception: " + e.toString();

        }finally {
            log.info("{}", slog);
        }
    }
}
