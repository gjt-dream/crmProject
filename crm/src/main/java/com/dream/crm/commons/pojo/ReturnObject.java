package com.dream.crm.commons.pojo;

public class ReturnObject {
    private String code;//处理成功或失败的标记：1成功，0 失败
    private String message;//提示信息
    private Object retData;//其他数据

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getRetData() {
        return retData;
    }

    public void setRetData(Object reData) {
        this.retData = reData;
    }
}
