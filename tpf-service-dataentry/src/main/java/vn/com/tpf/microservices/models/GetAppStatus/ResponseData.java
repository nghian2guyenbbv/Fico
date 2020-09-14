package vn.com.tpf.microservices.models.GetAppStatus;

import lombok.Data;

@Data
public class ResponseData{
        private boolean existsOrNot;
        private CustomerInfoVO customerInfoVO;
        private LeadInfoVO leadInfoVO;
        private MlSpecificInfoVO mlSpecificInfoVO;
        private CcSpecificInfoVO ccSpecificInfoVO;
        private OtherInfo otherInfo;
}
