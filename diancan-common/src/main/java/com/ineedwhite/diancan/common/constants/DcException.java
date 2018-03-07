package com.ineedwhite.diancan.common.constants;

import com.ineedwhite.diancan.common.ErrorCodeEnum;

/**
 * @author ruanxin
 * @create 2018-03-07
 * @desc 点餐异常处理
 */
public class DcException extends RuntimeException {
    private String errorCode;
    private String errorMsg;

    public DcException(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public DcException(ErrorCodeEnum errorCodeEnum) {
        this.errorCode = errorCodeEnum.getCode();
        this.errorMsg = errorCodeEnum.getDesc();
    }

    public DcException(ErrorCodeEnum errorCodeEnum, String... para) {
        this.errorCode = errorCodeEnum.getCode();
        this.errorMsg = String.format(errorCodeEnum.getDesc(), para);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
