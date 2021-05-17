package vn.com.tpf.microservices.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import vn.com.tpf.microservices.services.RedisService;

@Service
public class ScheduledCacheService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RedisService redisService;

    //@Scheduled(cron = "0 1 0 * * *")
    public String initCache() {
        log.info("Re-init Cache");
        redisService.initAllCache();
        log.info("End-init Cache");
        return "Run AutoRouting-Scheduled-Cache";

    }
}
