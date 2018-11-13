package cn.mldn.netty.client.handler;

import cn.mldn.netty.util.InputUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {
    private static final int REPEAT = 500 ; // 消息重复发送500次
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String inputStr = InputUtil.getString("请输入要发送的信息：") ;
        for (int x = 0 ; x < REPEAT ; x ++) {
            ctx.writeAndFlush(inputStr + " - " + x + System.getProperty("line.separator")) ; // 发送数据
        }
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String content = (String) msg ; // 接收数据
        System.out.println("｛客户端｝" + content); // 服务器端的回应信息
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close(); // 出现异常就关闭
    }
}
