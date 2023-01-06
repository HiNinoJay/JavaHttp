package com.nino.constant;

/**
 * @author zengzhongjie
 * @date 2023/1/6
 */
public enum HttpHeaderKey {
    CONTENT_TYPE("Content-Type"),
    CONTENT_LENGTH("Content-Length");


    HttpHeaderKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    private String key;




}
