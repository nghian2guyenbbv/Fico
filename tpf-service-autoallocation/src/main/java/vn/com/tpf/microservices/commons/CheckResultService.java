package vn.com.tpf.microservices.commons;

import org.springframework.stereotype.Service;
import vn.com.tpf.microservices.models.ResponseModel;

@Service
public class CheckResultService {

    public ResponseModel checkResult(ResultData resultCode, ResponseModel responseModel) {
        String code = "";
        String message = "";
        switch (resultCode) {
            case SUCCESS:
                code = ResultData.SUCCESS.getResultCode();
                message = ResultData.SUCCESS.getResultMessage();
                break;
            case OTHER_ERROR:
                code = ResultData.OTHER_ERROR.getResultCode();
                message = ResultData.OTHER_ERROR.getResultMessage();
                break;
            case DATA_EMPTY:
                code = ResultData.DATA_EMPTY.getResultCode();
                message = ResultData.DATA_EMPTY.getResultMessage();
                break;
            case NOT_EXIST:
                code = ResultData.NOT_EXIST.getResultCode();
                message = ResultData.NOT_EXIST.getResultMessage();
                break;
            case IS_REQUITED:
                code = ResultData.IS_REQUITED.getResultCode();
                message = ResultData.IS_REQUITED.getResultMessage();
                break;
            case PERMISSION_FAILED:
                code = ResultData.PERMISSION_FAILED.getResultCode();
                message = ResultData.PERMISSION_FAILED.getResultMessage();
                break;
            case NOT_EXIST_STATUS:
                code = ResultData.NOT_EXIST_STATUS.getResultCode();
                message = ResultData.NOT_EXIST_STATUS.getResultMessage();
                break;
            case NOT_HOLD:
                code = ResultData.NOT_HOLD.getResultCode();
                message = ResultData.NOT_HOLD.getResultMessage();
                break;
            case PENDING_LIMIT:
                code = ResultData.PENDING_LIMIT.getResultCode();
                message = ResultData.PENDING_LIMIT.getResultMessage();
                break;
            case APP_NOT_EXIST:
                code = ResultData.APP_NOT_EXIST.getResultCode();
                message = ResultData.APP_NOT_EXIST.getResultMessage();
                break;
            case STATUS_PENDING:
                code = ResultData.STATUS_PENDING.getResultCode();
                message = ResultData.STATUS_PENDING.getResultMessage();
                break;
            case ERROR_RE_ASSIGN:
                code = ResultData.ERROR_RE_ASSIGN.getResultCode();
                message = ResultData.ERROR_RE_ASSIGN.getResultMessage();
                break;
            default:
                code = ResultData.FAIL.getResultCode();
                message = ResultData.FAIL.getResultMessage();
        }

        responseModel.setResult_code(Integer.valueOf(code));
        responseModel.setMessage(message);
        return responseModel;

    }
}
