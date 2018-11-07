package cn.mldn.netty.server;

import cn.mldn.netty.server.handler.EchoServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {
    private int port;    // 服务器运行端口

    public EchoServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {    // 服务器运行
        /***
         * NioEventLoopGroup 是用来处理I/O操作的多线程事件循环器，
         * Netty提供了许多不同的EventLoopGroup的实现用来处理不同传输协议。 在这个例子中我们实现了一个服务端的应用，因此会有2个NioEventLoopGroup会被使用。 第一个经常被叫做‘boss’，用来接收进来的连接。
         * 第二个经常被叫做‘worker’，用来处理已经被接收的连接， 一旦‘boss’接收到连接，就会把连接信息注册到‘worker’上。如何知道多少个线程已经被使用，如何映射到已经创建的Channels上都需要依赖于EventLoopGroup的实现，并且可以通过构造函数来配置他们的关系。
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();    // 主线程池
        EventLoopGroup workerGroup = new NioEventLoopGroup();    // 子线程池
        System.out.println("准备运行端口：" + port);
        try {
            // ServerBootstrap 是一个启动NIO服务的辅助启动类 你可以在这个服务中直接使用Channel
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class);
            // 如果没有设置group将会报java.lang.IllegalStateException: group not set异常
            // serverBootstrap = serverBootstrap.group(bossGroup, workerGroup);
            // ServerSocketChannel以NIO的selector为基础进行实现的，用来接收新的连接 这里告诉Channel如何获取新的连接.
            // serverBootstrap = serverBootstrap.channel(NioServerSocketChannel.class);
            /***
             * 这里的事件处理类经常会被用来处理一个最近的已经接收的Channel。 ChannelInitializer是一个特殊的处理类，
             * 他的目的是帮助使用者配置一个新的Channel。 也许你想通过增加一些处理类比如NettyServerHandler来配置一个新的Channel
             * 或者其对应的ChannelPipeline来实现你的网络程序。 当你的程序变的复杂时，可能你会增加更多的处理类到pipline上，然后提取这些匿名类到最顶层的类上。
             */
            serverBootstrap = serverBootstrap.childHandler(
                    // ChannelInitializer覆写了channelRegistered以及inboundBufferUpdated两个方法，
                    // 另外定义了一个抽象方法initChannel留给用户定义的类来实现
                    new ChannelInitializer<SocketChannel>() { // 如果不设置则无法设置子线程
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoServerHandler()); // 追加责任链
                        }
                    });
            // 设置服务器端的TCP相关配置属性
            // option()是提供给NioServerSocketChannel用来接收进来的连接。
            serverBootstrap = serverBootstrap.option(ChannelOption.SO_BACKLOG, 128);
            serverBootstrap = serverBootstrap.option(ChannelOption.TCP_NODELAY, true);
            // childOption()是提供给由父管道ServerChannel接收到的连接， 在这个例子中也是NioServerSocketChannel。
            serverBootstrap = serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            // 绑定端口并启动去接收进来的连接
            ChannelFuture f = serverBootstrap.bind(this.port).sync();
            f.channel().closeFuture().sync();    // 这里会一直等待，直到socket被关闭
        } finally {    // 关闭
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
