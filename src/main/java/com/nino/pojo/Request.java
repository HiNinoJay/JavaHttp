package com.nino.pojo;

import lombok.Data;

import java.util.Map;

/**
 * http协议的请求头由三部分组成：
 * 第一部分 请求行（都在同一行）：请求方法 空格 URL 空格 协议版本 回车符 换行符
 * 第二部分 请求头部（每个占一行）： 头部字段名:值 回车符 换行符（ps:如 Content-Type:100）
 * 回车符 换行符
 * 第三部分 请求数据(即请求参数)
 * @author zengzhongjie
 * @date 2023/1/6
 */
@Data
public class Request {
    /**
     * 第一部分 请求行：请求方法 GET/POST/PUT/DELETE/OPTION
     */
    private String method;

    /**
     * 第一部分 请求行: 请求的url
     */
    private String url;
    /**
     * 第一部分 请求行：http版本
      */
    private String version;
    /**
     * 第二部分：请求头
      */
    private Map<String, String> headers;

    /**
     * 第三部分：请求参数相关
     */
    private String message;
}
