package com.nino.pojo;

import lombok.Data;

import java.util.Map;

/**
 * http协议的响应头由三部分组成：
 * 第一部分 状态行（都在同一行）：协议版本 空格 响应码 空格 响应解释 回车符 换行符
 * 第二部分 消息报头（每个占一行）： 头部字段名:值 回车符 换行符（ps:如 Content-Type:100）
 * 回车符 换行符
 * 第三部分 响应正文
 * @author zengzhongjie
 * @date 2023/1/6
 */
@Data
public class Response {
    /**
     * 第一部分 状态行：协议版本（如HTTP/1.1）
     */
    private String version;

    /**
     * 第一部分 状态行: 响应码（如200）
     */
    private Integer code;

    /**
     * 第一部分 状态行：响应解释（如OK）
     */
    private String status;

    /**
     * 第二部分 消息报头
     */
    private Map<String, String> headers;

    /**
     * 第三部分 响应正文
     */
    private String message;
}
