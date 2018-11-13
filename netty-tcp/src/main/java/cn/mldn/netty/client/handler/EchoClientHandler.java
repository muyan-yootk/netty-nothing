package cn.mldn.netty.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import cn.mldn.vo.Member ;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Member member = new Member() ;
        member.setName("李兴华");
        member.setAge(18);
        member.setSalary(19.2);
        ctx.writeAndFlush(member) ;	// 向服务器端发送消息
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Member member = (Member) msg;	// 接收发送来的对象数据
        System.out.println("【客户端接收回应Member对象】" + member);
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close(); // 出现异常就关闭
    }
}
