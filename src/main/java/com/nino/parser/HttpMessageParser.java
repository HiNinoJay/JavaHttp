package com.nino.parser;

import cn.hutool.core.convert.Convert;

import com.alibaba.fastjson.JSONObject;
import com.nino.constant.HttpCode;
import com.nino.constant.HttpHeaderKey;
import com.nino.constant.HttpHeaderValue;
import com.nino.pojo.Request;
import com.nino.pojo.Response;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zengzhongjie
 * @date 2023/1/6
 */
public class HttpMessageParser {

    /**
     *  * 既然已知http请求头由三部分（请求行，请求头，请求数据）组成
     *  * 那么提供三种方法来分别解析对应部分
     *  * 解析的结果是：填充到Request类中
     * @param in
     * @return
     * @throws IOException
     */
    public static Request parse2Request(InputStream in) throws IOException {
        BufferedReader httpReader = new BufferedReader(new InputStreamReader(in, "utf-8"));
        Request httpRequest = new Request();
        decodeRequestLine(httpReader, httpRequest);
        decodeRequestHeader(httpReader, httpRequest);
        decodeRequestMessage(httpReader, httpRequest);
        return httpRequest;
    }

    public static String buildResponse(Request request, String responseMessage) {
        Response httpResponse = new Response();
        httpResponse.setVersion(request.getVersion());
        httpResponse.setCode(HttpCode.OK.getCode());
        httpResponse.setStatus(HttpCode.OK.getStatus());

        Map<String, String> respnseHeaders = new HashMap<>();
        respnseHeaders.put(HttpHeaderKey.CONTENT_TYPE.getKey(), HttpHeaderValue.APPLICATION_JSON.getValue());

        httpResponse.setHeaders(respnseHeaders);
        responseMessage = JSONObject.toJSONString(httpResponse);

        httpResponse.setMessage(responseMessage);
        respnseHeaders.put(HttpHeaderKey.CONTENT_LENGTH.getKey(), Convert.toStr(responseMessage.length()));
        StringBuilder stringBuilder = new StringBuilder();
        buildResponseLine(httpResponse, stringBuilder);
        buildResponseHeaders(httpResponse, stringBuilder);
        buildResponseMessage(httpResponse, stringBuilder);
        return stringBuilder.toString();
    }

    private static void buildResponseMessage(Response httpResponse, StringBuilder stringBuilder) {
        stringBuilder.append(httpResponse.getMessage());
    }

    private static void buildResponseHeaders(Response httpResponse, StringBuilder stringBuilder) {
        for(Map.Entry<String, String> entry : httpResponse.getHeaders().entrySet()) {
            stringBuilder.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
        }
        stringBuilder.append("\n");
    }

    private static void buildResponseLine(Response httpResponse, StringBuilder stringBuilder) {
       stringBuilder.append(httpResponse.getVersion()).append(" ")
               .append(httpResponse.getCode()).append(" ")
               .append(httpResponse.getStatus()).append("\n");
    }

    /**
     * 根据标准的HTTP协议
     * 解析第一部分 请求行
     * @param reader
     * @param request
     */
    private static void decodeRequestLine(BufferedReader reader, Request request) throws IOException {
        String[] strings = StringUtils.split(reader.readLine(), " ");
        request.setMethod(strings[0]);
        request.setUrl(strings[1]);
        request.setVersion(strings[2]);
    }

    /**
     * 根据标准的HTTP协议
     * 解析第二部分 请求头
     * @param reader
     * @param request
     */
    private static void decodeRequestHeader(BufferedReader reader, Request request) throws IOException {
        Map<String,String> headers = new HashMap<>();
        String line = reader.readLine();
        String[] kv;
        while(!"".equals(line)) {
            kv = StringUtils.split(line, ":");
            headers.put(kv[0], kv[1]);
            line = reader.readLine();
        }
        request.setHeaders(headers);
    }

    /**
     * 根据标准的HTTP协议
     * 解析第三部分 请求参数
     *
     * 正文的解析，需要注意一点，正文可能为空，也可能有数据
     * 有数据时，我们要如何把所有的数据都取出来呢？
     * @param reader
     * @param request
     */
    private static void decodeRequestMessage(BufferedReader reader, Request request) throws IOException {
        // 通过请求头的Content-Length字段来判断正文的长度
        int contentLength = Convert.toInt(request.getHeaders().getOrDefault(HttpHeaderKey.CONTENT_LENGTH.getKey(), "0"));
        if(contentLength == 0) {
            return;
        }
        char[] message = new char[contentLength];
        reader.read(message);
        request.setMessage(new String(message));
    }


}
