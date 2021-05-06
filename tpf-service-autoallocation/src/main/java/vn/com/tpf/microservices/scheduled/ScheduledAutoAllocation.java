package vn.com.tpf.microservices.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vn.com.tpf.microservices.dao.AssignmentDetailDAO;
import vn.com.tpf.microservices.dao.UserDetailsDAO;
import vn.com.tpf.microservices.models.*;
import vn.com.tpf.microservices.services.RabbitMQService;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class ScheduledAutoAllocation {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    AssignmentDetailDAO assignmentDetailDAO;

    @Autowired
    private RabbitMQService rabbitMQService;
    @Autowired
    UserDetailsDAO userDetailsDAO;

    private static String KEY_ASSIGN_APP = "WAITING";


    @Async
    @Scheduled(fixedRateString = "${spring.syncfunc.fixedRate}")
    public void callFunctionAssignApp() {
        log.info("callFunctionAssignApp is start.");
        String sql = "select * from VIEW_ALLOCATION_FUNCTION";
        List<ConfigFunction> listConfigFunction = jdbcTemplate.query(sql, new Object[]{}, (rs, rowNum) -> {
            ConfigFunction configFunction = new ConfigFunction();
            configFunction.setConfig(rs.getString("CONFIG"));
            configFunction.setFromTime(rs.getString("FROM_TIME"));
            configFunction.setToTime(rs.getString("TO_TIME"));
            configFunction.setLimit(rs.getInt("LIMIT"));
            return configFunction;
        });
        ConfigFunction configFunction = listConfigFunction.get(0);
        if (configFunction == null) {
            log.error("No config");
        } else {
            LocalTime now = LocalTime.now();
            LocalTime fromTime = LocalTime.parse(configFunction.getFromTime());
            LocalTime toTime = LocalTime.parse(configFunction.getToTime());
            if (now.isBefore(toTime) && now.isAfter(fromTime) && configFunction.getConfig().equals("TRUE")) {
                try {
                    log.info("callFunctionAssignApp is running " + now);
                    String query = String.format("SELECT FN_ALLOCATION_ASSIGN_APP() FROM DUAL");
                    String result = jdbcTemplate.queryForObject(
                            query, String.class);

                    log.info("callFunctionAssignApp - time: {} - result: {} ", now, result);
                } catch (Exception e) {
                    log.info("callFunctionAssignApp: - Error: {}, class: {}, line: {}", e, e.getStackTrace()[0].getClassName(),
                            e.getStackTrace()[0].getLineNumber());
                }
            }
        }
    }

    @Scheduled(fixedRateString = "${spring.syncrobot.fixedRate}")
    public void pushAssignToRobot() {
        log.info("pushAssignToRobot - get config from DB");
        String sql = "select * from VIEW_ALLOCATION_ROBOT";
        List<ConfigRobot> listConfigRobot = jdbcTemplate.query(sql, new Object[]{}, (rs, rowNum) -> {
            ConfigRobot configRobot = new ConfigRobot();
            configRobot.setConfig(rs.getString("CONFIG"));
            configRobot.setFromTime(rs.getString("FROM_TIME"));
            configRobot.setToTime(rs.getString("TO_TIME"));
            configRobot.setLimit(rs.getInt("LIMIT"));
            return configRobot;
        });
        ConfigRobot configRobot = listConfigRobot.get(0);
        if (configRobot == null) {
            log.error("No Config");
        } else {
            LocalTime now = LocalTime.now();
            LocalTime fromTime = LocalTime.parse(configRobot.getFromTime());
            LocalTime toTime = LocalTime.parse(configRobot.getToTime());
            if (now.isBefore(toTime) && now.isAfter(fromTime) && configRobot.getConfig().equals("TRUE")) {
                log.info("pushAssignToRobot is start.");
                try {
                    Pageable pageable = PageRequest.of(0, configRobot.getLimit(), Sort.by("id").ascending());
                    Page<AssignmentDetail> assignmentDetailsList = assignmentDetailDAO.findByStatusAssign(KEY_ASSIGN_APP, pageable);

                    if (assignmentDetailsList.getContent() == null || assignmentDetailsList.getContent().size() <= 0
                            || assignmentDetailsList.getContent().isEmpty()) {
                        log.info("pushAssignToRobotApplication to assign is empty");
                    } else {
                        log.info("pushAssignToRobot is running");
                        BodyAssignRobot bodyAssignRobot = new BodyAssignRobot();
                        bodyAssignRobot.setProject("allocation");
                        bodyAssignRobot.setReference_id(UUID.randomUUID().toString());

                        RequestAssignRobot requestAssignRobot = new RequestAssignRobot();
                        requestAssignRobot.setFunc("autoAssignUser");
                        requestAssignRobot.setBody(bodyAssignRobot);
                        List<AutoAssignModel> autoAssignModelsList = new ArrayList<AutoAssignModel>();
                        for (AssignmentDetail ad : assignmentDetailsList.getContent()) {
                            AutoAssignModel autoAssignModel = new AutoAssignModel();
                            autoAssignModel.setIdAutoAllocation(UUID.randomUUID().toString());
                            autoAssignModel.setAppId(ad.getAppNumber());
                            autoAssignModel.setUserName(ad.getAssignee());
                            autoAssignModelsList.add(autoAssignModel);
                            ad.setPickUptime(new Timestamp(new Date().getTime()));
                            ad.setStatusAssign("ASSIGNING");
                            assignmentDetailDAO.save(ad);
                            log.info("pushAssignToRobot is save to DB: appNum - {}", ad.getAppNumber());
                            bodyAssignRobot.setAutoAssign(autoAssignModelsList);
                        }
                        rabbitMQService.send("tpf-service-automation-allocation",
                                requestAssignRobot);
                        log.info("pushAssignToRobot push success " + requestAssignRobot);
                    }
                } catch (Exception e) {
                    log.info("pushAssignToRobot - Error: {}, class: {}, line: {}", e, e.getStackTrace()[0].getClassName(),
                            e.getStackTrace()[0].getLineNumber());
                }
            }
        }
    }

    @Bean
    @Scheduled(fixedDelayString ="3600000")
    public String getTimeCronjob(){
        String cronJob = null;
        try{
            String sql = "SELECT PARAMETER_VALUE FROM ALLOCATION_PARAMETERS WHERE PARAMETER_NAME = 'INACTIVE_FLAG'";

            cronJob = (String) jdbcTemplate.queryForObject(
                    sql, new Object[]{}, String.class);
            log.info("Time CronJob: " + cronJob);
        }catch (Exception e) {
            log.error("Error getTimeCronjob: " + e);
        }
        return cronJob;
    }

    @Scheduled(cron = "#{@getTimeCronjob}")
    public void cronJobInactive(){
        try{
            List<UserDetail> userDetail = userDetailsDAO.findByActiveFlag("Y");
            userDetail.forEach(u->
                    u.setActiveFlag("N")
            );
            userDetailsDAO.saveAll(userDetail);

            String sql = "SELECT FN_ALLOCATION_BACKUP_DAILY() FROM DUAL";
            String backupDaily = jdbcTemplate.queryForObject(sql, String.class);
            log.info("Result run backupDaily : " + backupDaily);

        }catch (Exception e) {
            log.error("Error cronJobInactive: " + e);
        }
    }


}
