package com.nino.server;

import cn.hutool.core.lang.Console;
import com.nino.runner.HttpTast;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * 创建ServerSocket
 * 绑定端口接收请求
 * 在线程池中跑这个http服务
 * @author zengzhongjie
 * @date 2023/1/7
 */
public class BasicHttpServer {
    private static ExecutorService bootstrapExecutor = Executors.newSingleThreadExecutor();
    private static ExecutorService taskExecutor;
    private static int PORT = 8999;


    public static void startHttpServer() {
        int nThreads = Runtime.getRuntime().availableProcessors();
        taskExecutor = new ThreadPoolExecutor(nThreads, nThreads, 1000L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(100),
                new ThreadPoolExecutor.DiscardPolicy());

        while (true) {
            try {
                ServerSocket serverSocket = new ServerSocket(PORT);
                bootstrapExecutor.submit(new ServerThread(serverSocket));
                break;
            } catch (Exception e) {
                try{
                    // 重试
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        // bootstrapExecutor.shutdown();
    }

    private static class ServerThread implements Runnable {
        private ServerSocket serverSocket;

        public ServerThread(ServerSocket serverSocket) {
            this.serverSocket = serverSocket;
        }

        @Override
        public void run() {
            Console.log("服务器已经启动：127.0.0.1:8999");
            while (true) {
                try {
                    Socket socket = this.serverSocket.accept();
                    HttpTast eventTask = new HttpTast(socket);
                    taskExecutor.submit(eventTask);
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }
}
