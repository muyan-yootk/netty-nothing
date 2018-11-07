package cn.mldn.netty.server.main;

import cn.mldn.netty.server.EchoServer;

public class EchoServerMain {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        new EchoServer(port).run();
        System.out.println("server:run()");

    }
}