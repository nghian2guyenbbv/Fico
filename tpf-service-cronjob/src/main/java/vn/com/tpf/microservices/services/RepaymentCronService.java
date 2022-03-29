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
public class RepaymentCronService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RabbitMQService rabbitMQService;

    @Scheduled(cron = "${spring.cron.repaymentSyncFcc}")
    public void job_repaymentSyncFcc()
    {
        String slog = "func: job_repaymentSyncFcc";
        try{
            rabbitMQService.send("tpf-service-repayment",
                    Map.of("func", "job_repaymentSyncFcc","reference_id", UUID.randomUUID().toString()));
        }catch (Exception e)
        {
            slog += " - exception: " + e.toString();

        }finally {
            log.info("{}", slog);
        }
    }

    @Scheduled(cron = "${spring.cron.repaymentSyncQueue}")
    public void job_repaymentSyncQueue()
    {
        String slog = "func: job_repaymentSyncQueue";
        try{
            rabbitMQService.send("tpf-service-repayment",
                    Map.of("func", "job_repaymentSyncDataInQueue","reference_id", UUID.randomUUID().toString()));
        }catch (Exception e)
        {
            slog += " - exception: " + e.toString();

        }finally {
            log.info("{}", slog);
        }
    }

    @Scheduled(cron = "${spring.cron.repaymentSyncRetry}")
    public void job_repaymentSyncRetry()
    {
        String slog = "func: job_repaymentSyncRetry";
        try{
            rabbitMQService.send("tpf-service-repayment",
                    Map.of("func", "job_repaymentAutoRetry","reference_id", UUID.randomUUID().toString()));
        }catch (Exception e)
        {
            slog += " - exception: " + e.toString();

        }finally {
            log.info("{}", slog);
        }
    }

    @Scheduled(cron = "${spring.cron.repaymentSyncCancelTrans}")
    public void job_repaymentSyncCancelTrans()
    {
        String slog = "func: job_repaymentSyncCancelTrans";
        try{
            rabbitMQService.send("tpf-service-repayment",
                    Map.of("func", "job_repaymentSyncCancelTrans","reference_id", UUID.randomUUID().toString()));
        }catch (Exception e)
        {
            slog += " - exception: " + e.toString();

        }finally {
            log.info("{}", slog);
        }
    }
}
