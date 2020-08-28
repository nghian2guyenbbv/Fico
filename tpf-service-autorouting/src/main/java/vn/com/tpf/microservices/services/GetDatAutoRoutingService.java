package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.tpf.microservices.dao.ConfigRoutingDAO;
import vn.com.tpf.microservices.dao.ScheduleRoutingDAO;
import vn.com.tpf.microservices.models.ConfigRouting;
import vn.com.tpf.microservices.models.ScheduleRoute;

import java.util.*;

@Service
public class GetDatAutoRoutingService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ScheduleRoutingDAO scheduleRoutingDAO;

    @Autowired
    private ConfigRoutingDAO configRoutingDAO;

    public String getChanelConfig(String chanelId){
        if(redisService.hasKey("chanelConfig")){
            return redisService.getValueFromCache("chanelConfig",chanelId);
        }
        return redisService.getValueFromData("chanelConfig", chanelId, getChanelConfig());
    }

    public String getQuota(String chanelId){
        if(redisService.hasKey("chanelQuota")) {
            return redisService.getValueFromCache("chanelQuota", chanelId);
        }
        return redisService.getValueFromData("chanelQuota", chanelId, getQuota());
    }

    public String getTime(String chanelId){
        if(redisService.hasKey("chanelTimeStart")){
            return redisService.getValueFromCache("chanelTimeStart", chanelId);
        }
        return redisService.getValueFromData("chanelTimeStart", chanelId, getTimeStart());
    }

    public String getTimeEnd(String chanelId){
        if(redisService.hasKey("chanelTimeEnd")){
            return redisService.getValueFromCache("chanelTimeEnd", chanelId);
        }
        return redisService.getValueFromData("chanelTimeEnd", chanelId, getTimeEnd());
    }

    /**
     *
     * @return KEY, VALUE
     */
    public List<Map<String, Object>> getChanelConfig(){
        List<Map<String, Object>> result =  new ArrayList<Map<String, Object>>();
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        String day = Integer.toString(calendar.get(Calendar.DAY_OF_WEEK - 1));
        List<ScheduleRoute> scheduleRouteList  = scheduleRoutingDAO.findAllByDayId(day);
        for ( ScheduleRoute scheduleRoute: scheduleRouteList) {
            Map<String, Object> setDataMap = new HashMap<>();
            setDataMap.put("KEY" , scheduleRoute.getConfigRouting().getIdConfig() + "Config");
            setDataMap.put("VALUE",scheduleRoute.getChanelConfig());
            result.add(setDataMap);
        }
        return result;
    }

    /**
     *
     * @return KEY, VALUE
     */
    public List<Map<String, Object>> getQuota(){
        List<Map<String, Object>> result =  new ArrayList<Map<String, Object>>();
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        String day = Integer.toString(calendar.get(Calendar.DAY_OF_WEEK - 1));
        List<ScheduleRoute> scheduleRouteList  = scheduleRoutingDAO.findAllByDayId(day);
        for ( ScheduleRoute scheduleRoute: scheduleRouteList) {
            Map<String, Object> setDataMap = new HashMap<>();
            setDataMap.put("KEY" , scheduleRoute.getConfigRouting().getIdConfig() + "Quota");
            setDataMap.put("VALUE",scheduleRoute.getQuota());
            result.add(setDataMap);
        }
        return result;

    }

    /**
     *
     * @return KEY, VALUE
     */
    public List<Map<String, Object>> getTimeStart(){
        List<Map<String, Object>> result =  new ArrayList<Map<String, Object>>();
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        String day = Integer.toString(calendar.get(Calendar.DAY_OF_WEEK - 1));
        List<ScheduleRoute> scheduleRouteList  = scheduleRoutingDAO.findAllByDayId(day);
        for ( ScheduleRoute scheduleRoute: scheduleRouteList) {
            Map<String, Object> setDataMap = new HashMap<>();
            setDataMap.put("KEY" , scheduleRoute.getConfigRouting().getIdConfig() + "TimeStart");
            setDataMap.put("VALUE",scheduleRoute.getTimeStart());
            result.add(setDataMap);
        }
        return result;
    }
    /**
     *
     * @return KEY, VALUE
     */
    public List<Map<String, Object>> getTimeEnd(){
        List<Map<String, Object>> result =  new ArrayList<Map<String, Object>>();
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        String day = Integer.toString(calendar.get(Calendar.DAY_OF_WEEK - 1));
        List<ScheduleRoute> scheduleRouteList  = scheduleRoutingDAO.findAllByDayId(day);
        for ( ScheduleRoute scheduleRoute: scheduleRouteList) {
            Map<String, Object> setDataMap = new HashMap<>();
            setDataMap.put("KEY" , scheduleRoute.getConfigRouting().getIdConfig() + "TimeEnd");
            setDataMap.put("VALUE",scheduleRoute.getTimeEnd());
            result.add(setDataMap);
        }
        return result;
    }

    public List<Map<String, Object>> getAllRoutingConfig() {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        List<ConfigRouting> configRoutingList = configRoutingDAO.findAll();
        Map<String, Object> setDataMap = new HashMap<>();
        setDataMap.put("KEY", "CONFIG_ROUTING_KEY");
        setDataMap.put("VALUE", configRoutingList);
        result.add(setDataMap);
        return result;
    }

    public void updateRoutingConfig(Object dataUpdated) {
        redisService.updateCache("routingConfigInfo", "CONFIG_ROUTING_KEY", dataUpdated);
    }
}
