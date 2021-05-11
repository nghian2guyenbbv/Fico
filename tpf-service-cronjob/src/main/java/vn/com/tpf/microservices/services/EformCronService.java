package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.com.tpf.microservices.dao.ApplicationDataPushDao;
import vn.com.tpf.microservices.dao.ApplicationUserDao;
import vn.com.tpf.microservices.models.ApplicationDataPushModel;
import vn.com.tpf.microservices.models.apiFin1.AppStatusModel;
import vn.com.tpf.microservices.models.eform.ApplicationEformSyncModel;
import vn.com.tpf.microservices.models.eform.ApplicationUser;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@EnableScheduling
public class EformCronService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTemplate jdbcTemplateFico;

    @Autowired
    public JdbcTemplate jdbcTemplateFicoDe;

    @Autowired
    public JdbcTemplate jdbcTemplateFicoIh;

    @Autowired
    private ApplicationDataPushDao applicationDataPushDao;

    @Autowired
    private ApplicationUserDao applicationUserDao;

    @Autowired
    private RabbitMQService rabbitMQService;

    @Autowired
    private ApiFin1Service apiFin1Service;

//    @Scheduled(cron = "${spring.cron.applicationUserSyncStatus}")
//    public void applicationUser_syncStatus()
//    {
//        String slog = "func: applicationUser_syncStatus";
//        try{
//            List<ApplicationUser> list=applicationUserDao.getListSyncStatus();
//            for (ApplicationUser app: list) {
//                AppStatusModel appStatusModel = apiFin1Service.apiF1_AppStatus(app.getApplicationId());
//
//                //check stage : POLICY_EXECUTION, LOGIN_ACCEPTANCE
//                if(appStatusModel.getResponseCode().equals("0"))
//                {
//                    if(appStatusModel.getResponseData().getOtherInfo()!=null){
//                        String stage=appStatusModel.getResponseData().getOtherInfo().getCurrentProcessingStage();
//
//                        //update status app dataentry
//                        if (stage.indexOf("LEAD_DETAILS") >= 0)
//                        {
//                            //send rabbit update status app-pool
//                            rabbitMQService.send("tpf-service-eform",
//                                    Map.of("func", "applicationUserSyncUpdateStatus","reference_id", UUID.randomUUID().toString(),
//                                            "body", Map.of("applicationId",app.getApplicationId())));
//                        }
//                    }
//                }
//            }
//        }catch (Exception e)
//        {
//            slog += " - exception: " + e.toString();
//
//        }finally {
//            log.info("{}", slog);
//        }
//    }

    @Scheduled(cron = "${spring.cron.applicationEformSyncStatus}")
    public void application_eForm_syncStatus()
    {
        String slog = "func: application_eForm_syncStatus";
        try{
            List<ApplicationEformSyncModel> list=applicationUserDao.getListCollectSyncStatus();
            for (ApplicationEformSyncModel app: list) {
                AppStatusModel appStatusModel = apiFin1Service.apiF1_AppStatus(app.getApplicationid());

                //check stage : POLICY_EXECUTION, LOGIN_ACCEPTANCE
                if(appStatusModel.getResponseCode().equals("0"))
                {
                    if(appStatusModel.getResponseData().getOtherInfo()!=null){
                        String stage=appStatusModel.getResponseData().getOtherInfo().getCurrentProcessingStage();
                        String status=appStatusModel.getResponseData().getLeadInfoVO().getLoanStageName();

                        //update status app dataentry
//                        if (stage.indexOf("LEAD_DETAILS") >= 0)
//                        {
                            //send rabbit update status app-pool
                            rabbitMQService.send("tpf-service-eform",
                                    Map.of("func", "applicationEformSyncDataUpdateStatus","reference_id", UUID.randomUUID().toString(),
                                            "body", Map.of("applicationId",app.getApplicationid(),"stage",stage,"f1status",status)));
                        //}
                    }
                }
            }
        }catch (Exception e)
        {
            slog += " - exception: " + e.toString();

        }finally {
            log.info("{}", slog);
        }
    }

    @Scheduled(cron = "${spring.cron.applicationEformQaScore}")
    public void application_eForm_qA_score()
    {
        String slog = "func: application_eForm_qA_score";
        try{
                        rabbitMQService.send("tpf-service-eform",
                                Map.of("func", "applicationEformQaScore","reference_id", UUID.randomUUID().toString()));
        }catch (Exception e)
        {
            slog += " - exception: " + e.toString();

        }finally {
            log.info("{}", slog);
        }
    }

    @Scheduled(cron = "${spring.cron.applicationEformCancelReturn10day}")
    public void application_eForm_cancel_10_day()
    {
        String slog = "func: application_eForm_cancel_10_day";
        try{
            rabbitMQService.send("tpf-service-eform",
                    Map.of("func", "applicationEformCancelReturn10day","reference_id", UUID.randomUUID().toString()));
        }catch (Exception e)
        {
            slog += " - exception: " + e.toString();

        }finally {
            log.info("{}", slog);
        }
    }
}
