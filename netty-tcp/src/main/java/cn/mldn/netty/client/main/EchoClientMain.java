package cn.mldn.netty.client.main;

import cn.mldn.netty.client.EchoClient;

public class EchoClientMain {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        System.out.println("client:run()");
        new EchoClient("localhost", port).run();
    }
}
