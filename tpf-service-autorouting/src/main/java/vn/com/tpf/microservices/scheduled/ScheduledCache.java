package vn.com.tpf.microservices.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vn.com.tpf.microservices.services.RedisService;

@Component
public class ScheduledCache {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RedisService redisService;

    @Scheduled(cron = "0 1 0 * * *")
    public void initCache() {
        log.info("Re-init Cache");
        redisService.initAllCache();
        log.info("End-init Cache");

    }
}
