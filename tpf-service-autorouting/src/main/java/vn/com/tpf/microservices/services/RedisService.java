package vn.com.tpf.microservices.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RedisService implements CommandLineRunner {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private GetDatAutoRoutingService getDatAutoRoutingService;

    @PostConstruct
    private void init() {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        redisTemplate.setHashKeySerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
    }

    @Override
    public void run(String... args) throws Exception {
        initAllCache();
//        String a = getValueFromCache("chanelConfig","DEConfig");
//        System.out.println(a.toString());
    }

    public void initAllCache() {
//        if (!hasKey("chanelConfig")){
//            initCache("chanelConfig", getDataF1Service.getChanelConfig(), 1);
//        }
//
//        if (!hasKey("chanelQuota")){
//            initCache("chanelQuota", getDataF1Service.getQuota(), 1);
//        }
//
//        if(!hasKey("chanelTimeStart")){
//            initCache("chanelTimeStart", getDataF1Service.getTimeStart(), 1);
//        }
//
//        if(!hasKey("chanelTimeEnd")){
//            initCache("chanelTimeEnd", getDataF1Service.getTimeEnd(), 1);
//        }

//        String a = redisTemplate.opsForHash().get("productType", "productType").toString();
        if (!hasKey("routingConfigInfo")) {
            initCache("routingConfigInfo", getDatAutoRoutingService.getAllRoutingConfig(), 1);
        }

    }

    /**
     * @param parent
     * @param dataDB
     * @param timeout
     */
    private void initCache(String parent, List<Map<String, Object>> dataDB, long timeout){
        Map map = dataDB.stream().collect(Collectors.toMap(o ->  o.get("KEY"), o -> o.get("VALUE"), (a, b) -> b));
        setValueToCache(parent, map, timeout);
    }

    private void initCache(String key, String value, long timeout){
        redisTemplate.opsForValue().set(key, value);
        setExpireKey(key, timeout);
    }

    /**
     * @param key
     * @param map
     * @param timeout
     */
    private void setValueToCache(String key, Map map, long timeout){
        redisTemplate.opsForHash().putAll(key, map);
        setExpireKey(key, timeout);
    }

    /**
     * @param key
     * @param timeout
     */
    private void setExpireKey(String key, long timeout){
        Date currentDate = new Date();
        LocalDateTime localDateTime = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        localDateTime = localDateTime.plusDays(timeout).withHour(0).withMinute(0).withSecond(0);
        Date currentDatePlusOne = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        redisTemplate.expireAt(key, currentDatePlusOne);
    }

    public String getValueFromCache(String parent, String child){
        return redisTemplate.opsForHash().get(parent, child).toString();
    }

    public Object getObjectValueFromCache(String parent, String child){
        return redisTemplate.opsForHash().get(parent, child);
    }

    public String getValueFromCache(String key) {
        return redisTemplate.opsForValue().get(key).toString();
    }

    public String getValueFromData(String parent, String child, List<Map<String, Object>> dataDB){
        initCache(parent, dataDB, 1);
        return getValueFromCache(parent, child);
    }

    public String getValueFromData(String key, String value){
        initCache(key, value, 1);
        return getValueFromCache(key);
    }

    public boolean hasKey(String parent){
        return redisTemplate.opsForHash().keys(parent).size() > 0;
    }

    public boolean hasKeyValue(String parent){
        return redisTemplate.opsForValue().size(parent) > 0;
    }


    public void updateCache(String parent, String child,Object dataUpdated){
        redisTemplate.opsForHash().put(parent, child, dataUpdated);
    }
}
