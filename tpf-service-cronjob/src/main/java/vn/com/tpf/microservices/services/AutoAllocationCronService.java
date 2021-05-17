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
public class AutoAllocationCronService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RabbitMQService rabbitMQService;

    @Scheduled(fixedRateString = "${spring.cron.autoAllocationScheduledCallFunction}")
    public void job_autoAllocationScheduledCallFunction()
    {
        String slog = "func: job_autoAllocationScheduledCallFunction";
        try{
            rabbitMQService.send("tpf-service-autoallocation",
                    Map.of("func", "scheduledCallFunction","reference_id", UUID.randomUUID().toString()));
        }catch (Exception e)
        {
            slog += " - exception: " + e.toString();

        }finally {
            log.info("{}", slog);
        }
    }

    @Scheduled(fixedRateString = "${spring.cron.autoAllocationScheduledPushAssignRobot}")
    public void job_autoAllocationScheduledPushAssignRobot()
    {
        String slog = "func: job_autoAllocationScheduledPushAssignRobot";
        try{
            rabbitMQService.send("tpf-service-autoallocation",
                    Map.of("func", "scheduledPushAssignRobot","reference_id", UUID.randomUUID().toString()));
        }catch (Exception e)
        {
            slog += " - exception: " + e.toString();

        }finally {
            log.info("{}", slog);
        }
    }
}
