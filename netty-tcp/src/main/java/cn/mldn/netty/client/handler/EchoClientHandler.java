package cn.mldn.netty.client.handler;

import cn.mldn.netty.util.InputUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        byte data[] = "www.mldn.cn".getBytes(); // 要发送的头部消息
//        ByteBuf message = Unpooled.buffer(data.length);
//        message.writeBytes(data); // 将数据写入到缓存之中
//        ctx.writeAndFlush(message); // 写入数据
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            ByteBuf buf = (ByteBuf) msg; // 将接收到的数据进行转型
            String echoContent = buf.toString(CharsetUtil.UTF_8) ; // 接收回应消息
            if ("quit".equalsIgnoreCase(echoContent)) { // 程序处理结束
                System.out.println("您已退出本次网络传输，再见！");
                ctx.close() ; // 关闭连接
            } else {
                System.out.println("【客户端】" + echoContent);
                String inputStr = InputUtil.getString("请输入要发送的消息：") ;
                byte [] data = inputStr.getBytes() ;
                ByteBuf newBuf = Unpooled.buffer(data.length) ;
                newBuf.writeBytes(data) ;	// 进行消息输出
                ctx.writeAndFlush(newBuf) ; // 将接收到的消息发送到服务器端
            }
        } catch (Exception e) {
            ReferenceCountUtil.release(msg); // 抛弃收到的数据
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close(); // 出现异常就关闭
    }
}
