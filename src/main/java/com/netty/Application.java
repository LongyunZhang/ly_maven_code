package com.netty;

public class Application {
    public static void main(String[] args) throws Exception {
        HttpServer server = new HttpServer(8082);// 8082为启动端口
        server.start();
    }
}
