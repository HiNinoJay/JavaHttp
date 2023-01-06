package com.nino.constant;

/**
 * @author zengzhongjie
 * @date 2023/1/6
 */
public enum HttpCode {
    OK(200, "OK"),
    NOTFOUND(404, "NOT_FOUND");
    ;
    private Integer code;
    private String status;

    public Integer getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    HttpCode(Integer code, String status) {
        this.code = code;
        this.status = status;
    }
}
