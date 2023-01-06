package com.nino.runner;

import cn.hutool.core.lang.Console;
import cn.hutool.http.HttpRequest;
import com.nino.parser.HttpMessageParser;
import com.nino.pojo.Request;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 每个HTTP请求
 * 单独分配一个任务来解析
 * 支持并发
 * 对于ServerSocket而言，接收到了请求，那就创建一个HttpTask任务来实现http通信
 * @author zengzhongjie
 * @date 2023/1/6
 */
public class HttpTast implements Runnable{

    private Socket socket;

    public HttpTast(Socket socket) {
        this.socket = socket;
    }

    /**
     * 1.从请求中捞数据
     * 2.响应请求
     * 3.封装结果并返回
     */
    @Override
    public void run() {
        if(socket == null) {
            throw new IllegalStateException("socket can't be null");
        }
        try {
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter out = new PrintWriter(outputStream);
            Request httpRequest = HttpMessageParser.parse2Request(socket.getInputStream());
            Console.log(httpRequest);
            try{
                String responseMessage = "Here we are. It's gonna be okay.";
                String httpRespnse = HttpMessageParser.buildResponse(httpRequest, responseMessage);
                out.write(httpRespnse);
            } catch (Exception e) {
                String errorHttpResponse = HttpMessageParser.buildResponse(httpRequest, e.getMessage());
                out.write(errorHttpResponse);
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
