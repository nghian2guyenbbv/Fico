package vn.com.tpf.microservices.services;

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
    private RedisService redisService;

    @Autowired
    private ScheduleRoutingDAO scheduleRoutingDAO;

    @Autowired
    private ConfigRoutingDAO configRoutingDAO;

    /**
     * @return KEY, VALUE
     */
    public List<Map<String, Object>> getChanelConfig() {
        log.info("getChanelConfig - load from db to cache");
        List<Map<String, Object>> result = new ArrayList<>();
        List<ScheduleRoute> scheduleRouteList = getScheduleRouteListFromDB();
        for (ScheduleRoute scheduleRoute : scheduleRouteList) {
            Map<String, Object> setDataMap = new HashMap<>();
            setDataMap.put("KEY", scheduleRoute.getConfigRouting().getIdConfig() + "Config");
            setDataMap.put("VALUE", scheduleRoute.getChanelConfig());
            result.add(setDataMap);
        }
        return result;
    }

    /**
     * @return KEY, VALUE
     */
    public List<Map<String, Object>> getQuota() {
        log.info("getQuota - load from db to cache");
        List<Map<String, Object>> result = new ArrayList<>();
        List<ScheduleRoute> scheduleRouteList = getScheduleRouteListFromDB();
        for (ScheduleRoute scheduleRoute : scheduleRouteList) {
            Map<String, Object> setDataMap = new HashMap<>();
            setDataMap.put("KEY", scheduleRoute.getConfigRouting().getIdConfig() + "Quota");
            setDataMap.put("VALUE", scheduleRoute.getQuota());
            result.add(setDataMap);
        }
        return result;

    }

    /**
     * @return KEY, VALUE
     */
    public List<Map<String, Object>> getTimeStart() {
        log.info("getTimeStart - load from db to cache");
        List<Map<String, Object>> result = new ArrayList<>();
        Date now = new Date();
        List<ScheduleRoute> scheduleRouteList = getScheduleRouteListFromDB();
        for (ScheduleRoute scheduleRoute : scheduleRouteList) {
            scheduleRoute.getTimeStart().setDate(now.getDate());
            scheduleRoute.getTimeStart().setMonth(now.getMonth());
            scheduleRoute.getTimeStart().setYear(now.getYear());
            Map<String, Object> setDataMap = new HashMap<>();
            setDataMap.put("KEY", scheduleRoute.getConfigRouting().getIdConfig() + "TimeStart");
            setDataMap.put("VALUE", scheduleRoute.getTimeStart());
            result.add(setDataMap);
        }
        return result;
    }

    /**
     * @return KEY, VALUE
     */
    public List<Map<String, Object>> getTimeEnd() {
        log.info("getTimeEnd - load from db to cache");
        List<Map<String, Object>> result = new ArrayList<>();
        Date now = new Date();
        List<ScheduleRoute> scheduleRouteList = getScheduleRouteListFromDB();
        for (ScheduleRoute scheduleRoute : scheduleRouteList) {
            scheduleRoute.getTimeEnd().setDate(now.getDate());
            scheduleRoute.getTimeEnd().setMonth(now.getMonth());
            scheduleRoute.getTimeEnd().setYear(now.getYear());
            Map<String, Object> setDataMap = new HashMap<>();
            setDataMap.put("KEY", scheduleRoute.getConfigRouting().getIdConfig() + "TimeEnd");
            setDataMap.put("VALUE", scheduleRoute.getTimeEnd());
            result.add(setDataMap);
        }
        return result;
    }

    public List<ScheduleRoute> getScheduleRouteListFromDB() {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        String day = Integer.toString(calendar.get(Calendar.DAY_OF_WEEK) - 1);
        List<ScheduleRoute> scheduleRouteList = scheduleRoutingDAO.findAllByDayId(day);
        return scheduleRouteList;
    }

    public List<Map<String, Object>> getAllRoutingConfig() {
        log.info("getAllRoutingConfig - load from db to cache");
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        List<ConfigRouting> configRoutingList = configRoutingDAO.findAll();
        Map<String, Object> setDataMap = new HashMap<>();
        setDataMap.put("KEY", "CONFIG_ROUTING_KEY");
        setDataMap.put("VALUE", configRoutingList);
        result.add(setDataMap);
        return result;
    }

    public void updateRoutingConfig(Object dataUpdated) {
        log.info("updateRoutingConfig - update to cache");
        redisService.updateCache("routingConfigInfo", "CONFIG_ROUTING_KEY", dataUpdated);
    }
}
