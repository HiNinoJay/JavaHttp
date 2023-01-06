package com.nino.constant;

/**
 * @author zengzhongjie
 * @date 2023/1/6
 */
public enum HttpHeaderValue {
    APPLICATION_JSON("application/json")
    ;


    HttpHeaderValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private String value;
}
