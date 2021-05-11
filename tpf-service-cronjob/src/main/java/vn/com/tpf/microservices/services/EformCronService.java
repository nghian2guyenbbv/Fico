package vn.com.tpf.microservices.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
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
    private RabbitMQService rabbitMQService;

    @Scheduled(cron = "${spring.cron.applicationEformSyncStatus}")
    public void application_eForm_syncStatus()
    {
        String slog = "func: application_eForm_syncStatus";
        try{
//            List<ApplicationEformSyncModel> list=applicationUserDao.getListCollectSyncStatus();
//            for (ApplicationEformSyncModel app: list) {
//                AppStatusModel appStatusModel = apiFin1Service.apiF1_AppStatus(app.getApplicationid());
//
//                //check stage : POLICY_EXECUTION, LOGIN_ACCEPTANCE
//                if(appStatusModel.getResponseCode().equals("0"))
//                {
//                    if(appStatusModel.getResponseData().getOtherInfo()!=null){
//                        String stage=appStatusModel.getResponseData().getOtherInfo().getCurrentProcessingStage();
//                        String status=appStatusModel.getResponseData().getLeadInfoVO().getLoanStageName();
//
//                        //update status app dataentry
////                        if (stage.indexOf("LEAD_DETAILS") >= 0)
////                        {
//                            //send rabbit update status app-pool
//                            rabbitMQService.send("tpf-service-eform",
//                                    Map.of("func", "applicationEformSyncDataUpdateStatus","reference_id", UUID.randomUUID().toString(),
//                                            "body", Map.of("applicationId",app.getApplicationid(),"stage",stage,"f1status",status)));
//                        //}
//                    }
//                }
//            }

            rabbitMQService.send("tpf-service-eform",
                    Map.of("func", "applicationEformSyncDataUpdateStatus","reference_id", UUID.randomUUID().toString()));
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

    @Scheduled(cron = "${spring.cron.updateAppStatus}")
    public void jobUpdateStatusIH()
    {
        String slog = "func: jobUpdateStatusIH";
        try{
//            List<ApplicationDataPushModel> list=applicationDataPushDao.getListSyncDataPush();
//            for (ApplicationDataPushModel app: list) {
//                AppStatusModel appStatusModel = apiFin1Service.apiF1_AppStatus(app.getApplicationNo());
//
//                //check stage : POLICY_EXECUTION, LOGIN_ACCEPTANCE
//                if(appStatusModel.getResponseCode().equals("0"))
//                {
//                    if(appStatusModel.getResponseData().getOtherInfo()!=null){
//                        String stage=appStatusModel.getResponseData().getOtherInfo().getCurrentProcessingStage();
//                        String status=appStatusModel.getResponseData().getLeadInfoVO().getLoanStageName();
//
//                        //update status app dataentry
////                        if (stage.indexOf("LEAD_DETAILS") < 0)
////                        {
//                        app.setStage(stage);
//                        app.setStatus(status);
//                        if (stage.indexOf("LEAD_DETAILS") < 0)
//                        {
//                            //send rabbit update status app-pool
//                            rabbitMQService.send("tpf-service-eform",
//                                    Map.of("func", "applicationPoolSyncUpdateStatus","reference_id", UUID.randomUUID().toString(),
//                                            "body", Map.of("applicationId",app.getApplicationNo())));
//                        }
//                    }
//                }
//            }
//            applicationDataPushDao.saveAll(list);

            rabbitMQService.send("tpf-service-eform",
                    Map.of("func", "applicationPoolSyncUpdateStatus","reference_id", UUID.randomUUID().toString()));

        }catch (Exception e)
        {
            slog += " - exception: " + e.toString();

        }finally {
            log.info("{}", slog);
        }
    }

    @Scheduled(cron = "${spring.cron.eformSyncMasterData}")
    public void eform_syncMasterData()
    {
        String slog = "func: eform_syncMasterData";
        try{
            //ResponseFunctionModel responseFunctionModel= applicationDataPushDao.cronSyncMasterData();

            rabbitMQService.send("tpf-service-eform",
                    Map.of("func", "cronSyncMasterData","reference_id", UUID.randomUUID().toString()));

        }catch (Exception e)
        {
            slog += " - ERROR: " + e.toString();

        }finally {
            log.info(slog);
        }
    }
}
