package cn.mldn.netty.client;

import cn.mldn.netty.client.handler.EchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

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
                    .handler(new ChannelInitializer<SocketChannel>() { // 如果不设置则无法设置子线程
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new FixedLengthFrameDecoder(50)) ; // 每一个数据占50个字节
                            ch.pipeline().addLast(new LineBasedFrameDecoder(1024)) ;
                            ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8)) ;
                            ch.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8)) ;
                            ch.pipeline().addLast(new EchoClientHandler()); // 追加责任链
                        }
                    });
            ChannelFuture channelFuture = client.connect(this.hostname, this.port).sync() ; // 等待客户端连接
            channelFuture.channel().closeFuture().sync() ; // 等待关闭
        } finally {	// 退出连接之后会自动执行此操作
            group.shutdownGracefully() ; // 释放NIO线程组
        }
    }
}
