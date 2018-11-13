package cn.mldn.netty.client;

import cn.mldn.netty.client.handler.EchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class EchoClient {
    private String hostname;
    private int port;
    public EchoClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }
    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup(); // 创建一个线程连接组
        try {
            Bootstrap client = new Bootstrap(); // 创建客户端处理
            client.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new EchoClientHandler());
            ChannelFuture channelFuture = client.connect(this.hostname, this.port).sync() ; // 等待客户端连接
            channelFuture.addListener(new GenericFutureListener(){

                @Override
                public void operationComplete(Future future) throws Exception {
                    if (future.isSuccess()) {	// 消息发送完毕
                        System.out.println("消息发送完毕，已经确保服务器可以接收！");
                    }
                }
            }) ;
            channelFuture.channel().closeFuture().sync() ; // 等待关闭
        } finally {	// 退出连接之后会自动执行此操作
            group.shutdownGracefully() ; // 释放NIO线程组
        }
    }
}
